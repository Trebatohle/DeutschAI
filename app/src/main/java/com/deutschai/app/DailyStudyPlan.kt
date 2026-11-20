package com.deutschai.app

import android.content.Context
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

internal val dailyStudyGoalOptions = listOf(5, 10, 15)

internal data class DailyStudyDayProgress(
    val date: LocalDate,
    val newWords: Int
)

internal data class DailyStudyPlanProgress(
    val targetWords: Int,
    val learnedToday: Int,
    val remainingToday: Int,
    val goalReachedToday: Boolean,
    val learnedThisWeek: Int,
    val week: List<DailyStudyDayProgress>
)

internal fun isValidDailyStudyGoal(targetWords: Int): Boolean =
    targetWords in dailyStudyGoalOptions

/**
 * Counts each vocabulary entry once, on the device-local calendar date when
 * the learner first completed all three stages of the vocabulary drill.
 */
internal fun buildDailyStudyPlanProgress(
    targetWords: Int,
    firstLearnedAtMillis: Map<String, Long>,
    today: LocalDate = LocalDate.now(),
    zoneId: ZoneId = ZoneId.systemDefault()
): DailyStudyPlanProgress {
    require(isValidDailyStudyGoal(targetWords)) { "Daily goal must be 5, 10, or 15 words." }

    val learnedCountsByDate = firstLearnedAtMillis.values
        .groupingBy { timestamp ->
            java.time.Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalDate()
        }
        .eachCount()

    val startOfWeek = today.with(DayOfWeek.MONDAY)
    val week = (0L..6L).map { offset ->
        val date = startOfWeek.plusDays(offset)
        DailyStudyDayProgress(date = date, newWords = learnedCountsByDate[date] ?: 0)
    }
    val learnedToday = learnedCountsByDate[today] ?: 0
    val learnedThisWeek = week.filter { !it.date.isAfter(today) }.sumOf { it.newWords }

    return DailyStudyPlanProgress(
        targetWords = targetWords,
        learnedToday = learnedToday,
        remainingToday = (targetWords - learnedToday).coerceAtLeast(0),
        goalReachedToday = learnedToday >= targetWords,
        learnedThisWeek = learnedThisWeek,
        week = week
    )
}

/** Local-only daily word target and first-learned timestamps. */
internal object DailyStudyPlanStore {
    private const val PREFS = "deutschai_daily_study_plan"
    private const val DAILY_GOAL = "daily_word_goal_v1"
    private const val FIRST_LEARNED_AT = "first_learned_word_timestamps_v1"
    private const val DEFAULT_DAILY_GOAL = 5

    private fun preferences(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun dailyGoal(context: Context): Int =
        preferences(context).getInt(DAILY_GOAL, DEFAULT_DAILY_GOAL)
            .takeIf(::isValidDailyStudyGoal) ?: DEFAULT_DAILY_GOAL

    fun setDailyGoal(context: Context, targetWords: Int) {
        require(isValidDailyStudyGoal(targetWords)) { "Daily goal must be 5, 10, or 15 words." }
        preferences(context).edit().putInt(DAILY_GOAL, targetWords).apply()
    }

    fun firstLearnedAtMillis(context: Context): Map<String, Long> {
        val json = runCatching {
            JSONObject(preferences(context).getString(FIRST_LEARNED_AT, "{}") ?: "{}")
        }.getOrElse { JSONObject() }
        return json.keys().asSequence().mapNotNull { id ->
            json.optLong(id, Long.MIN_VALUE)
                .takeIf { it != Long.MIN_VALUE }
                ?.let { id to it }
        }.toMap()
    }

    /**
     * Records the first successful three-stage drill pass. Repeated passes
     * cannot inflate the number of new words.
     */
    fun recordNewWord(
        context: Context,
        entryId: String,
        learnedAtMillis: Long = System.currentTimeMillis()
    ): Boolean {
        if (entryId.isBlank()) return false

        val prefs = preferences(context)
        val json = runCatching {
            JSONObject(prefs.getString(FIRST_LEARNED_AT, "{}") ?: "{}")
        }.getOrElse { JSONObject() }
        if (json.has(entryId)) return false

        json.put(entryId, learnedAtMillis)
        prefs.edit().putString(FIRST_LEARNED_AT, json.toString()).apply()
        return true
    }

    fun loadProgress(
        context: Context,
        today: LocalDate = LocalDate.now(),
        zoneId: ZoneId = ZoneId.systemDefault()
    ): DailyStudyPlanProgress = buildDailyStudyPlanProgress(
        targetWords = dailyGoal(context),
        firstLearnedAtMillis = firstLearnedAtMillis(context),
        today = today,
        zoneId = zoneId
    )
}
