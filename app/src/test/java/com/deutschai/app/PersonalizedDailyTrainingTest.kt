package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonalizedDailyTrainingTest {
    private fun question(
        id: String,
        kind: String,
        focusTopicId: String = "",
        focusTopicTitle: String = "",
        focusTerm: String = ""
    ) = ReviewQuestion(
        id = id,
        sourceKind = kind,
        sourceTitle = kind,
        prompt = "Vyber správnou odpověď.",
        choices = listOf("Správně", "Špatně"),
        correctIndex = 0,
        explanation = "Vysvětlení",
        focusTopicId = focusTopicId,
        focusTopicTitle = focusTopicTitle,
        focusTerm = focusTerm
    )

    private fun memoryEntry(question: ReviewQuestion, wrongCount: Int = 1) = ReviewMemoryEntry(
        question = question,
        wrongCount = wrongCount,
        correctStreak = 0,
        intervalDays = 0,
        dueAtMillis = 0L,
        attemptCount = wrongCount,
        correctCount = 0,
        lastAnsweredAtMillis = 100L
    )

    private fun stat(kind: String, accuracy: Int, attempts: Int = 10) = LearningSkillStats(
        sourceKind = kind,
        title = learningSkillTitle(kind),
        attempts = attempts,
        correct = attempts * accuracy / 100,
        accuracyPercent = accuracy,
        trackedItems = 5
    )

    @Test
    fun dueQuestionsArePlacedFirstAndNeverDuplicated() {
        val due = listOf(question("due-1", "verbs"), question("due-2", "reading"))
        val unseen = due + List(8) { question("new-" + it, "sentences") }

        val plan = buildPersonalizedDailyTrainingPlan(
            dueQuestions = due,
            unseenQuestions = unseen,
            practicedQuestions = emptyList(),
            skillStats = emptyList(),
            count = 5
        )

        assertEquals(5, plan.questions.size)
        assertEquals(listOf("due-1", "due-2"), plan.questions.take(2).map { it.id })
        assertEquals(5, plan.questions.map { it.id }.distinct().size)
        assertEquals(2, plan.dueReviewCount)
    }

    @Test
    fun weakAreasReceiveMoreFreshQuestionsWithoutTakingOverTheWholeSession() {
        val unseen = buildList {
            listOf("verbs", "articles", "reading", "listening").forEach { kind ->
                repeat(8) { add(question(kind + "-" + it, kind)) }
            }
        }
        val stats = listOf(
            stat("articles", 40),
            stat("verbs", 75),
            stat("reading", 90),
            stat("listening", 90)
        )

        val plan = buildPersonalizedDailyTrainingPlan(
            dueQuestions = emptyList(),
            unseenQuestions = unseen,
            practicedQuestions = emptyList(),
            skillStats = stats,
            count = 10
        )

        val weakAreaCount = plan.questions.count { it.sourceKind == "articles" }
        assertTrue("The weakest category should receive extra practice.", weakAreaCount >= 3)
        assertTrue("One category must not consume more than half the fresh set.", weakAreaCount <= 5)
        assertEquals("articles", plan.focusKind)
        assertEquals(40, plan.focusAccuracyPercent)
    }

    @Test
    fun noReliableStatsProducesBalancedCoverageAcrossAvailableSkills() {
        val unseen = buildList {
            listOf("sentences", "verbs", "listening").forEach { kind ->
                repeat(8) { add(question(kind + "-" + it, kind)) }
            }
        }

        val plan = buildPersonalizedDailyTrainingPlan(
            dueQuestions = emptyList(),
            unseenQuestions = unseen,
            practicedQuestions = emptyList(),
            skillStats = listOf(stat("verbs", 80, attempts = 3)),
            count = 6
        )

        val counts = plan.questions.groupingBy { it.sourceKind }.eachCount()
        assertEquals(2, counts["sentences"])
        assertEquals(2, counts["verbs"])
        assertEquals(2, counts["listening"])
        assertFalse(plan.hasReliableSkillData)
        assertEquals(null, plan.focusKind)
    }

    @Test
    fun aSmallSingleSkillPoolCanFillTheRestOfTheSession() {
        val plan = buildPersonalizedDailyTrainingPlan(
            dueQuestions = emptyList(),
            unseenQuestions = List(8) { question("only-" + it, "reading") },
            practicedQuestions = emptyList(),
            skillStats = emptyList(),
            count = 8
        )

        assertEquals(8, plan.questions.size)
        assertTrue(plan.questions.all { it.sourceKind == "reading" })
    }

    @Test
    fun repeatedMistakesOnOnePrepositionPrioritizeOtherPracticeModesForThatTerm() {
        val history = listOf(
            memoryEntry(question("old-an-words", "wordtools", "wordtools:prepositions", "Předložky", "an")),
            memoryEntry(question("old-an-sentences", "wordtools", "wordtools:prepositions", "Předložky", "an"))
        )
        val patterns = buildRecurringLearningErrors(history)
        val unseen = listOf(
            question("new-an-sentences", "wordtools", "wordtools:prepositions", "Předložky", "an"),
            question("new-an-challenge", "wordtools", "wordtools:prepositions", "Předložky", "an"),
            question("new-auf-sentences", "wordtools", "wordtools:prepositions", "Předložky", "auf"),
            question("new-verb", "verbs")
        )

        val plan = buildPersonalizedDailyTrainingPlan(
            dueQuestions = emptyList(),
            unseenQuestions = unseen,
            practicedQuestions = emptyList(),
            skillStats = emptyList(),
            recurringErrors = patterns,
            count = 4
        )

        assertEquals("Předložky · an", plan.focusError?.displayTitle)
        assertEquals(2, plan.focusErrorQuestionCount)
        assertEquals(
            listOf("new-an-sentences", "new-an-challenge"),
            plan.questions.take(2).map { it.id }
        )
        assertTrue(plan.focusTitle.contains("Předložky · an"))
    }

    @Test
    fun aSingleMistakeDoesNotCreateARecurringErrorRecommendation() {
        val history = listOf(
            memoryEntry(question("one-an", "wordtools", "wordtools:prepositions", "Předložky", "an"))
        )
        assertTrue(buildRecurringLearningErrors(history).isEmpty())
    }

    @Test
    fun masteredTopicsAreExcludedFromRecurringErrorRecommendations() {
        val repeated = memoryEntry(
            question("mastered-an", "wordtools", "wordtools:prepositions", "Předložky", "an"),
            wrongCount = 3
        ).copy(correctStreak = 3, intervalDays = 7)
        assertTrue(buildRecurringLearningErrors(listOf(repeated)).isEmpty())
    }

    @Test
    fun zeroQuestionCountReturnsAnEmptyPlan() {
        val plan = buildPersonalizedDailyTrainingPlan(
            dueQuestions = listOf(question("due", "verbs")),
            unseenQuestions = listOf(question("new", "reading")),
            practicedQuestions = emptyList(),
            skillStats = emptyList(),
            count = 0
        )

        assertTrue(plan.questions.isEmpty())
        assertEquals(0, plan.dueReviewCount)
    }
}
