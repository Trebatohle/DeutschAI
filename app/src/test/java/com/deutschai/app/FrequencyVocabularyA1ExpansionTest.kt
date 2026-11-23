package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FrequencyVocabularyA1ExpansionTest {
    @Test
    fun exactlyTwoHundredNewA1WordsAreAddedWithoutDuplicates() {
        assertEquals(FREQUENCY_VOCABULARY_A1_ADDITION_COUNT, frequencyVocabularyA1Expansion.size)
        assertEquals(200, frequencyVocabularyA1Expansion.map { it.word.lowercase() }.toSet().size)
        assertTrue(frequencyVocabularyA1Expansion.all { it.level == "A1" })

        val previousWords = (frequencyVocabularyCoreCatalog + frequencyVocabularyA2Pilot)
            .map { it.word.trim().lowercase() }
            .toSet()
        assertTrue(frequencyVocabularyA1Expansion.none { it.word.trim().lowercase() in previousWords })

        val allA1 = frequencyVocabularyForLevel("A1")
        assertEquals(frequencyVocabularyCoreCatalog.count { it.level == "A1" } + 200, allA1.size)
        assertEquals(434, allA1.size)
        assertTrue(allA1.all { it.level == "A1" })
    }

    @Test
    fun everyNewEntryHasAValidArticleExampleAndReviewQuestion() {
        frequencyVocabularyA1Expansion.forEach { entry ->
            assertTrue(entry.word.isNotBlank())
            assertTrue(entry.czech.isNotBlank())
            assertTrue(entry.exampleGerman.isNotBlank())
            assertTrue(entry.exampleCzech.isNotBlank())
            assertTrue(entry.exampleGerman.contains(entry.word, ignoreCase = true))
            assertEquals(entry.kind == FrequencyWordKind.NOUN, entry.article != null)
            if (entry.article != null) {
                assertTrue(entry.article in setOf("der", "die", "das"))
                assertTrue(entry.germanAnswer.startsWith(entry.article + " "))
            }
            val question = frequencyVocabularyReviewQuestion(entry)
            assertEquals(entry.id, question.id)
            assertEquals("A1", question.cefrLevel)
            assertEquals(entry.germanAnswer, question.choices[question.correctIndex])
            assertEquals(entry.exampleGerman, question.germanSentence)
            assertEquals(entry.exampleCzech, question.translation)
        }
    }

    @Test
    fun a1AdditionIsBalancedAcrossUsefulWordTypes() {
        assertTrue(frequencyVocabularyA1Expansion.count { it.kind == FrequencyWordKind.NOUN } >= 100)
        assertTrue(frequencyVocabularyA1Expansion.count { it.kind == FrequencyWordKind.VERB } >= 30)
        assertTrue(frequencyVocabularyA1Expansion.count { it.kind == FrequencyWordKind.ADJECTIVE } >= 15)
        assertTrue(frequencyVocabularyA1Expansion.count {
            it.kind == FrequencyWordKind.ADVERB || it.kind == FrequencyWordKind.FUNCTION
        } >= 10)
    }

    @Test
    fun newA1VocabularyAppearsInTheActualLevelDrill() {
        val a1 = frequencyVocabularyForLevel("A1")
        val selectedIds = a1.map { it.id }.toSet()
        assertTrue(frequencyVocabularyA1Expansion.all { it.id in selectedIds })
        assertEquals(434, a1.size)
    }
}
