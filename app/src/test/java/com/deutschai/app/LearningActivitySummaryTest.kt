package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class LearningActivitySummaryTest {
    private val prague = ZoneId.of("Europe/Prague")

    @Test
    fun monthlyOverviewCountsOnlyTheCurrentCalendarMonth() {
        val overview = buildLearningActivityOverview(
            dailyActivities = mapOf(
                LocalDate.parse("2026-10-31") to DailyLearningActivity(attempts = 9, correct = 9),
                LocalDate.parse("2026-11-18") to DailyLearningActivity(attempts = 8, correct = 5),
                LocalDate.parse("2026-11-20") to DailyLearningActivity(attempts = 7, correct = 6)
            ),
            firstLearnedAtMillis = mapOf(
                "november-word" to Instant.parse("2026-11-07T12:00:00Z").toEpochMilli(),
                "october-word" to Instant.parse("2026-10-31T12:00:00Z").toEpochMilli()
            ),
            today = LocalDate.parse("2026-11-20"),
            zoneId = prague
        )

        assertEquals(15, overview.monthAttempts)
        assertEquals(11, overview.monthCorrect)
        assertEquals(73, overview.monthAccuracyPercent)
        assertEquals(2, overview.monthActiveDays)
        assertEquals(1, overview.monthNewWords)
        assertEquals(15, overview.lastSevenDaysAttempts)
        assertEquals(2, overview.lastSevenDaysActiveDays)
        assertEquals(7, overview.lastSevenDays.size)
        assertEquals(LocalDate.parse("2026-11-14"), overview.lastSevenDays.first().date)
        assertEquals(LocalDate.parse("2026-11-20"), overview.lastSevenDays.last().date)
    }

    @Test
    fun newWordsUseTheDeviceLocalCalendarDateAtMonthBoundary() {
        val timestamp = Instant.parse("2026-10-31T22:30:00Z").toEpochMilli()

        val overview = buildLearningActivityOverview(
            dailyActivities = emptyMap(),
            firstLearnedAtMillis = mapOf("word" to timestamp),
            today = LocalDate.parse("2026-11-20"),
            zoneId = prague
        )

        assertEquals(1, overview.monthNewWords)
        assertEquals(0, overview.monthAttempts)
        assertNull(overview.monthAccuracyPercent)
    }

    @Test
    fun emptyHistoryDoesNotPretendThatAccuracyIsZeroPercent() {
        val overview = buildLearningActivityOverview(
            dailyActivities = emptyMap(),
            firstLearnedAtMillis = emptyMap(),
            today = LocalDate.parse("2026-11-20"),
            zoneId = prague
        )

        assertNull(overview.monthAccuracyPercent)
        assertEquals(0, overview.monthActiveDays)
        assertEquals(0, overview.lastSevenDaysAttempts)
    }
}
