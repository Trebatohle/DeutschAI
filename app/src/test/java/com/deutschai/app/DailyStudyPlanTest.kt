package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class DailyStudyPlanTest {

    private val prague = ZoneId.of("Europe/Prague")

    @Test
    fun dailyGoalOffersOnlyFiveTenAndFifteenWords() {
        assertEquals(listOf(5, 10, 15), dailyStudyGoalOptions)
        assertTrue(isValidDailyStudyGoal(5))
        assertTrue(isValidDailyStudyGoal(10))
        assertTrue(isValidDailyStudyGoal(15))
        assertFalse(isValidDailyStudyGoal(3))
        assertFalse(isValidDailyStudyGoal(20))
    }

    @Test
    fun dailyPlanCountsFirstLearnedWordsOnTheirLocalCalendarDay() {
        val today = LocalDate.of(2026, 11, 20)
        val learned = buildMap {
            repeat(5) { index ->
                put(
                    "word_$index",
                    today.atTime(12, 0).plusMinutes(index.toLong())
                        .atZone(prague).toInstant().toEpochMilli()
                )
            }
            put(
                "yesterday_word",
                today.minusDays(1).atTime(23, 30).atZone(prague).toInstant().toEpochMilli()
            )
            put(
                "last_week_word",
                today.minusDays(5).atTime(10, 0).atZone(prague).toInstant().toEpochMilli()
            )
        }

        val progress = buildDailyStudyPlanProgress(
            targetWords = 5,
            firstLearnedAtMillis = learned,
            today = today,
            zoneId = prague
        )

        assertEquals(5, progress.learnedToday)
        assertEquals(0, progress.remainingToday)
        assertTrue(progress.goalReachedToday)
        assertEquals(7, progress.learnedThisWeek)
        assertEquals(today.with(java.time.DayOfWeek.MONDAY), progress.week.first().date)
        assertEquals(today.with(java.time.DayOfWeek.MONDAY).plusDays(6), progress.week.last().date)
    }

    @Test
    fun dailyGoalCanBeTenOrFifteenWithoutChangingTheRecordedWordCount() {
        val today = LocalDate.of(2026, 11, 20)
        val learned = (1..10).associate { index ->
            "word_$index" to today.atTime(9, 0).plusMinutes(index.toLong())
                .atZone(prague).toInstant().toEpochMilli()
        }

        val tenWordGoal = buildDailyStudyPlanProgress(10, learned, today, prague)
        val fifteenWordGoal = buildDailyStudyPlanProgress(15, learned, today, prague)

        assertTrue(tenWordGoal.goalReachedToday)
        assertEquals(0, tenWordGoal.remainingToday)
        assertFalse(fifteenWordGoal.goalReachedToday)
        assertEquals(5, fifteenWordGoal.remainingToday)
        assertEquals(10, fifteenWordGoal.learnedToday)
    }

    @Test
    fun wordsLearnedAcrossTheLocalMidnightBelongToDifferentDays() {
        val today = LocalDate.of(2026, 11, 20)
        val learned = mapOf(
            "late_word" to today.minusDays(1).atTime(23, 59).atZone(prague).toInstant().toEpochMilli(),
            "new_day_word" to today.atStartOfDay(prague).toInstant().toEpochMilli()
        )

        val progress = buildDailyStudyPlanProgress(
            targetWords = 5,
            firstLearnedAtMillis = learned,
            today = today,
            zoneId = prague
        )

        assertEquals(1, progress.learnedToday)
        assertEquals(1, progress.week.first { it.date == today }.newWords)
        assertEquals(1, progress.week.first { it.date == today.minusDays(1) }.newWords)
    }
}
