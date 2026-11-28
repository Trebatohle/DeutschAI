package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class DailyVocabularySelectionTest {

    private fun word(id: String, kind: FrequencyWordKind, level: String = "B1") =
        FrequencyVocabularyEntry(
            id = id,
            level = level,
            kind = kind,
            word = id,
            czech = "význam $id",
            article = if (kind == FrequencyWordKind.NOUN) "der" else null,
            exampleGerman = "Ich kenne $id.",
            exampleCzech = "Znám $id."
        )

    private fun memory(id: String) = ReviewMemoryEntry(
        question = ReviewQuestion(
            id = id,
            sourceKind = "vocabulary",
            sourceTitle = "Dril slovní zásoby",
            prompt = "Přelož",
            choices = listOf("správně", "špatně"),
            correctIndex = 0,
            explanation = ""
        ),
        wrongCount = 1,
        correctStreak = 0,
        intervalDays = 0,
        dueAtMillis = 0L,
        attemptCount = 1
    )

    @Test
    fun dailyNewWordSelectionExcludesPassedAndPreviouslyAttemptedWords() {
        val words = listOf(
            word("passed", FrequencyWordKind.VERB),
            word("needs-review", FrequencyWordKind.NOUN),
            word("new-1", FrequencyWordKind.ADJECTIVE),
            word("new-2", FrequencyWordKind.ADVERB)
        )

        val selected = selectDailyNewFrequencyVocabularyEntries(
            level = "B1",
            count = 5,
            reviewEntries = listOf(memory("needs-review")),
            perfectlyPassedIds = setOf("passed"),
            random = Random(1),
            candidatePool = words
        )

        assertEquals(listOf("new-1", "new-2"), selected.map { it.id }.sorted())
        assertTrue(selected.none { it.id == "passed" || it.id == "needs-review" })
    }

    @Test
    fun a1DailySessionKeepsAtLeastSeventyPercentVerbs() {
        val words = buildList {
            repeat(10) { add(word("verb-$it", FrequencyWordKind.VERB, "A1")) }
            repeat(10) { add(word("noun-$it", FrequencyWordKind.NOUN, "A1")) }
        }

        val selected = selectDailyNewFrequencyVocabularyEntries(
            level = "A1",
            count = 5,
            reviewEntries = emptyList(),
            perfectlyPassedIds = emptySet(),
            random = Random(2),
            candidatePool = words
        )

        assertEquals(5, selected.size)
        assertTrue(selected.count { it.kind == FrequencyWordKind.VERB } >= 4)
    }

    @Test
    fun bLevelSessionPrioritizesWordTypesWithLowerMastery() {
        val words = buildList {
            FrequencyWordKind.values().forEach { kind ->
                repeat(4) { add(word("${kind.name.lowercase()}-$it", kind)) }
            }
        }
        val passedIds = words.filter { entry ->
            when (entry.kind) {
                FrequencyWordKind.ADJECTIVE -> false
                FrequencyWordKind.ADVERB -> entry.word.endsWith("-0") || entry.word.endsWith("-1")
                else -> !entry.word.endsWith("-3")
            }
        }.map { it.id }.toSet()

        val selected = selectDailyNewFrequencyVocabularyEntries(
            level = "B1",
            count = 1,
            reviewEntries = emptyList(),
            perfectlyPassedIds = passedIds,
            random = Random(3),
            candidatePool = words
        )

        assertEquals(1, selected.size)
        assertEquals(FrequencyWordKind.ADJECTIVE, selected.single().kind)
    }

    @Test
    fun placementRecommendationUsesSupportedFrequencyLevels() {
        assertEquals("A1", recommendedDailyVocabularyLevel(null))
        assertEquals("A1", recommendedDailyVocabularyLevel(placement("A1")))
        assertEquals("A2", recommendedDailyVocabularyLevel(placement("A2")))
        assertEquals("B1", recommendedDailyVocabularyLevel(placement("B1")))
        assertEquals("B2", recommendedDailyVocabularyLevel(placement("B2")))
        assertEquals("B2", recommendedDailyVocabularyLevel(placement("C1")))
        assertEquals("B2", recommendedDailyVocabularyLevel(placement("C2")))
    }

    private fun placement(level: String) = PlacementAssessmentReport(
        estimatedLevel = level,
        assessedAtMillis = 0L,
        totalQuestions = 24,
        correctAnswers = 18,
        levelResults = emptyMap(),
        skillResults = emptyMap()
    )
}
