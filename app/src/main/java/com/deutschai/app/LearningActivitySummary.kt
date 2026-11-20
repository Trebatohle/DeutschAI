package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

internal data class DailyLearningActivity(
    val attempts: Int,
    val correct: Int
)

internal data class LearningActivityDay(
    val date: LocalDate,
    val attempts: Int,
    val correct: Int
)

internal data class LearningActivityOverview(
    val month: YearMonth,
    val monthAttempts: Int,
    val monthCorrect: Int,
    val monthAccuracyPercent: Int?,
    val monthActiveDays: Int,
    val monthNewWords: Int,
    val lastSevenDays: List<LearningActivityDay>,
    val lastSevenDaysAttempts: Int,
    val lastSevenDaysActiveDays: Int
)

/**
 * Builds calendar-period statistics from day-level answer counts.
 * Older per-question history is intentionally not assigned invented dates.
 */
internal fun buildLearningActivityOverview(
    dailyActivities: Map<LocalDate, DailyLearningActivity>,
    firstLearnedAtMillis: Map<String, Long>,
    today: LocalDate = LocalDate.now(),
    zoneId: ZoneId = ZoneId.systemDefault()
): LearningActivityOverview {
    val month = YearMonth.from(today)
    fun safeAttempts(day: DailyLearningActivity?): Int = day?.attempts?.coerceAtLeast(0) ?: 0
    fun safeCorrect(day: DailyLearningActivity?): Int =
        day?.let { it.correct.coerceIn(0, it.attempts.coerceAtLeast(0)) } ?: 0

    val monthDays = dailyActivities.filterKeys { YearMonth.from(it) == month }
    val monthAttempts = monthDays.values.sumOf { safeAttempts(it) }
    val monthCorrect = monthDays.values.sumOf { safeCorrect(it) }
    val monthActiveDays = monthDays.count { (_, activity) -> safeAttempts(activity) > 0 }
    val monthNewWords = firstLearnedAtMillis.values.count { timestamp ->
        runCatching {
            YearMonth.from(Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalDate()) == month
        }.getOrDefault(false)
    }
    val lastSevenDays = (6L downTo 0L).map { offset ->
        val date = today.minusDays(offset)
        val activity = dailyActivities[date]
        LearningActivityDay(
            date = date,
            attempts = safeAttempts(activity),
            correct = safeCorrect(activity)
        )
    }

    return LearningActivityOverview(
        month = month,
        monthAttempts = monthAttempts,
        monthCorrect = monthCorrect,
        monthAccuracyPercent = if (monthAttempts == 0) null else monthCorrect * 100 / monthAttempts,
        monthActiveDays = monthActiveDays,
        monthNewWords = monthNewWords,
        lastSevenDays = lastSevenDays,
        lastSevenDaysAttempts = lastSevenDays.sumOf { it.attempts },
        lastSevenDaysActiveDays = lastSevenDays.count { it.attempts > 0 }
    )
}

/**
 * Stores compact daily aggregates only (no prompts, answers or vocabulary text).
 * A rolling 400-day window keeps the local statistics small.
 */
internal object LearningActivityStore {
    private const val PREFS = "deutschai_learning_activity"
    private const val DAILY_ACTIVITY = "daily_activity_v1"
    private const val RETENTION_DAYS = 400L

    fun recordAnswer(
        context: Context,
        wasCorrect: Boolean,
        answeredAtMillis: Long = System.currentTimeMillis(),
        zoneId: ZoneId = ZoneId.systemDefault()
    ) {
        val date = Instant.ofEpochMilli(answeredAtMillis).atZone(zoneId).toLocalDate()
        val preferences = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val daily = runCatching {
            JSONObject(preferences.getString(DAILY_ACTIVITY, "{}") ?: "{}")
        }.getOrElse { JSONObject() }
        val key = date.toString()
        val previous = daily.optJSONObject(key) ?: JSONObject()
        val oldAttempts = previous.optInt("attempts", 0).coerceAtLeast(0)
        val oldCorrect = previous.optInt("correct", 0).coerceIn(0, oldAttempts)
        val attempts = oldAttempts + 1
        val correct = oldCorrect + if (wasCorrect) 1 else 0

        daily.put(
            key,
            JSONObject()
                .put("attempts", attempts)
                .put("correct", correct.coerceIn(0, attempts))
        )

        val firstRetainedDate = date.minusDays(RETENTION_DAYS)
        daily.keys().asSequence().toList().forEach { storedDate ->
            val parsedDate = runCatching { LocalDate.parse(storedDate) }.getOrNull()
            if (parsedDate != null && parsedDate.isBefore(firstRetainedDate)) {
                daily.remove(storedDate)
            }
        }
        preferences.edit().putString(DAILY_ACTIVITY, daily.toString()).apply()
    }

    fun loadOverview(
        context: Context,
        today: LocalDate = LocalDate.now(),
        zoneId: ZoneId = ZoneId.systemDefault()
    ): LearningActivityOverview {
        val preferences = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val daily = runCatching {
            JSONObject(preferences.getString(DAILY_ACTIVITY, "{}") ?: "{}")
        }.getOrElse { JSONObject() }
        val activities = buildMap {
            daily.keys().asSequence().forEach { key ->
                val date = runCatching { LocalDate.parse(key) }.getOrNull() ?: return@forEach
                val item = daily.optJSONObject(key) ?: return@forEach
                val attempts = item.optInt("attempts", 0).coerceAtLeast(0)
                val correct = item.optInt("correct", 0).coerceIn(0, attempts)
                put(date, DailyLearningActivity(attempts, correct))
            }
        }
        return buildLearningActivityOverview(
            dailyActivities = activities,
            firstLearnedAtMillis = DailyStudyPlanStore.firstLearnedAtMillis(context),
            today = today,
            zoneId = zoneId
        )
    }
}

@Composable
internal fun LearningActivitySummaryCard(summary: LearningActivityOverview) {
    val locale = remember { Locale.forLanguageTag("cs-CZ") }
    val monthLabel = remember(summary.month, locale) {
        summary.month.atDay(1)
            .format(DateTimeFormatter.ofPattern("LLLL yyyy", locale))
            .uppercase(locale)
    }
    val maximumAttempts = summary.lastSevenDays.maxOfOrNull { it.attempts }?.coerceAtLeast(1) ?: 1

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier.padding(17.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = ForestText, modifier = Modifier.size(18.dp))
                    Text("MĚSÍČNÍ PŘEHLED", color = ForestText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                }
                Text(monthLabel, color = Muted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LearningActivityMetric(
                    modifier = Modifier.weight(1f),
                    value = summary.monthAttempts.toString(),
                    label = "procvičovacích odpovědí"
                )
                LearningActivityMetric(
                    modifier = Modifier.weight(1f),
                    value = summary.monthAccuracyPercent?.let { "$it %" } ?: "—",
                    label = "měsíční úspěšnost"
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LearningActivityMetric(
                    modifier = Modifier.weight(1f),
                    value = summary.monthActiveDays.toString(),
                    label = "dnů s procvičováním"
                )
                LearningActivityMetric(
                    modifier = Modifier.weight(1f),
                    value = summary.monthNewWords.toString(),
                    label = "nových slov"
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("POSLEDNÍCH 7 DNŮ", color = ForestText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.7.sp)
                    Text(
                        "${summary.lastSevenDaysAttempts} odpovědí · ${summary.lastSevenDaysActiveDays} dnů s procvičováním",
                        color = Muted,
                        fontSize = 10.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    summary.lastSevenDays.forEach { day ->
                        val fraction = day.attempts.toFloat() / maximumAttempts.toFloat()
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(day.attempts.toString(), color = Muted, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            Box(
                                modifier = Modifier.width(23.dp).height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Mint),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                if (day.attempts > 0) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().fillMaxHeight(fraction)
                                            .background(ForestText)
                                    )
                                }
                            }
                            Text(learningActivityDayLabel(day.date.dayOfWeek), color = Muted, fontSize = 10.sp)
                        }
                    }
                }
            }

            if (summary.monthAttempts == 0 && summary.monthNewWords == 0) {
                Text(
                    "Denní statistiky se začínají ukládat od této verze. Starší souhrnné výsledky zůstávají zachované výše v přehledu znalostí.",
                    color = Muted,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            } else {
                Text(
                    "Nová slovíčka se počítají od zavedení denního plánu; starší slovní zásoba nebyla zpětně započítána.",
                    color = Muted,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun LearningActivityMetric(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(value, color = Ink, fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Muted, fontSize = 11.sp, lineHeight = 15.sp)
    }
}

private fun learningActivityDayLabel(day: DayOfWeek): String = when (day) {
    DayOfWeek.MONDAY -> "Po"
    DayOfWeek.TUESDAY -> "Út"
    DayOfWeek.WEDNESDAY -> "St"
    DayOfWeek.THURSDAY -> "Čt"
    DayOfWeek.FRIDAY -> "Pá"
    DayOfWeek.SATURDAY -> "So"
    DayOfWeek.SUNDAY -> "Ne"
}
