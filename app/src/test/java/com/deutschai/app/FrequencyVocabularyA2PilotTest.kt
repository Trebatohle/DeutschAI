package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class FrequencyVocabularyA2PilotTest {

    @Test
    fun pilotContainsExactlyFiftyNewA2Items() {
        assertEquals(FREQUENCY_VOCABULARY_A2_PILOT_COUNT, frequencyVocabularyA2Pilot.size)
        assertEquals(
            FREQUENCY_VOCABULARY_A2_PILOT_COUNT,
            frequencyVocabularyA2Pilot.map { it.word.trim().lowercase() }.toSet().size
        )
        assertTrue(frequencyVocabularyA2Pilot.all { it.level == "A2" })

        val previousCatalogue = frequencyVocabularyCatalog.dropLast(FREQUENCY_VOCABULARY_A2_PILOT_COUNT)
        val previousWords = previousCatalogue.map { it.word.trim().lowercase() }.toSet()
        val newWords = frequencyVocabularyA2Pilot.map { it.word.trim().lowercase() }
        val overlaps = newWords.filter { it in previousWords }
        println("A2 PILOT DIAGNOSTIC: pilotCount=${frequencyVocabularyA2Pilot.size}, previousUnique=${previousWords.size}, pilotWords=${newWords.joinToString()}, overlaps=${overlaps.joinToString()}")
        assertTrue("Pilot contains words already present in the old catalogue: " + overlaps.joinToString(), overlaps.isEmpty())

        assertEquals(
            1396 + FREQUENCY_VOCABULARY_A2_PILOT_COUNT,
            frequencyVocabularyCatalog.map { it.word.trim().lowercase() }.toSet().size
        )
    }

    @Test
    fun frequencyProgressSeparatesFirstPerfectPassFromLongTermMastery() {
        val words = frequencyVocabularyForLevel("A1").take(3)
        val memories = listOf(
            ReviewMemoryEntry(
                question = frequencyVocabularyReviewQuestion(words[0]),
                wrongCount = 0,
                correctStreak = 1,
                intervalDays = 1,
                dueAtMillis = 5_000L,
                attemptCount = 1,
                correctCount = 1,
                lastAnsweredAtMillis = 900L
            ),
            ReviewMemoryEntry(
                question = frequencyVocabularyReviewQuestion(words[1]),
                wrongCount = 1,
                correctStreak = 0,
                intervalDays = 0,
                dueAtMillis = 500L,
                attemptCount = 1,
                correctCount = 0,
                lastAnsweredAtMillis = 800L
            ),
            ReviewMemoryEntry(
                question = frequencyVocabularyReviewQuestion(words[2]),
                wrongCount = 0,
                correctStreak = 3,
                intervalDays = 7,
                dueAtMillis = 6_000L,
                attemptCount = 3,
                correctCount = 3,
                lastAnsweredAtMillis = 700L
            )
        )

        val progress = buildFrequencyVocabularyProgress(
            words = words,
            reviewEntries = memories,
            nowMillis = 1_000L
        )

        assertEquals(3, progress.totalWords)
        assertEquals(3, progress.practicedWords)
        assertEquals(2, progress.successfullyPassedWords)
        assertEquals(1, progress.longTermMasteredWords)
        assertEquals(1, progress.dueWords)
    }

    @Test
    fun pilotItemsHaveExamplesAndValidReviewQuestions() {
        frequencyVocabularyA2Pilot.forEach { entry ->
            assertTrue(entry.word.isNotBlank())
            assertTrue(entry.czech.isNotBlank())
            assertTrue(entry.exampleGerman.isNotBlank())
            assertTrue(entry.exampleCzech.isNotBlank())
            assertEquals(entry.kind == FrequencyWordKind.NOUN, entry.article != null)
            if (entry.article != null) assertTrue(entry.article in setOf("der", "die", "das"))
            assertFalse(entry.listeningTarget.contains("..."))

            entry.listeningTarget.split(" ").filter { it.isNotBlank() }.forEach { spokenPart ->
                assertTrue(
                    entry.word + " listening answer part is absent from its example sentence: " + spokenPart,
                    entry.exampleGerman.contains(spokenPart, ignoreCase = true)
                )
            }

            val question = frequencyVocabularyReviewQuestion(
                entry = entry,
                random = Random(entry.id.hashCode())
            )
            assertEquals(entry.id, question.id)
            assertEquals(entry.level, question.cefrLevel)
            assertEquals(entry.germanAnswer, question.choices[question.correctIndex])
            assertEquals(4, question.choices.size)
            assertEquals(4, question.choices.toSet().size)
            assertEquals(entry.exampleGerman, question.germanSentence)
            assertEquals(entry.exampleCzech, question.translation)
        }
    }
}
