package com.deutschai.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun HomeProgressSnapshotCard(
    summary: LearningActivityOverview,
    currentStreakDays: Int,
    totalLearnedWords: Int,
    onClick: () -> Unit
) {
    val maximumAttempts = summary.lastSevenDays.maxOfOrNull { it.attempts }?.coerceAtLeast(1) ?: 1
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(21.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "TVŮJ POKROK",
                    color = ForestText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.9.sp
                )
                Text(
                    "Zobrazit přehled  →",
                    color = ForestText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HomeProgressMetric(
                    modifier = Modifier.weight(1f),
                    value = totalLearnedWords.toString(),
                    label = "zvládnutých slov"
                )
                HomeProgressMetric(
                    modifier = Modifier.weight(1f),
                    value = currentStreakDays.toString(),
                    label = "dní v řadě"
                )
                HomeProgressMetric(
                    modifier = Modifier.weight(1f),
                    value = summary.monthAccuracyPercent?.let { "$it %" } ?: "—",
                    label = "úspěšnost v měsíci"
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "PROCVIČOVÁNÍ · POSLEDNÍCH 7 DNŮ",
                        color = Muted,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.55.sp
                    )
                    Text(
                        "${summary.lastSevenDaysActiveDays}/7 aktivních dnů",
                        color = Muted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
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
                            Text(
                                day.attempts.toString(),
                                color = Muted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Box(
                                modifier = Modifier.width(24.dp).height(32.dp)
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
                            Text(
                                homeProgressDayLabel(day.date.dayOfWeek),
                                color = Muted,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
                if (summary.lastSevenDaysAttempts == 0) {
                    Text(
                        "Graf se naplní, až budeš procvičovat testové otázky a opakování.",
                        color = Muted,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeProgressMetric(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            value,
            color = Ink,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            label,
            color = Muted,
            fontSize = 10.sp,
            lineHeight = 13.sp,
            maxLines = 2
        )
    }
}

private fun homeProgressDayLabel(day: java.time.DayOfWeek): String = when (day) {
    java.time.DayOfWeek.MONDAY -> "Po"
    java.time.DayOfWeek.TUESDAY -> "Út"
    java.time.DayOfWeek.WEDNESDAY -> "St"
    java.time.DayOfWeek.THURSDAY -> "Čt"
    java.time.DayOfWeek.FRIDAY -> "Pá"
    java.time.DayOfWeek.SATURDAY -> "So"
    java.time.DayOfWeek.SUNDAY -> "Ne"
}
