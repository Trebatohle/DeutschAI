package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class FrequencyVocabularyA1Batch1Test {
    @Test
    fun batchContainsExactlyFiftyUniqueA1WordsAndNoExistingWords() {
        val batch = frequencyVocabularyA1Batch1
        assertEquals(FREQUENCY_VOCABULARY_A1_BATCH1_COUNT, batch.size)
        assertTrue(batch.all { it.level == "A1" })
        assertEquals(50, batch.map { it.word.trim().lowercase() }.toSet().size)

        val previousWords = (frequencyVocabularyCatalog - batch)
            .map { it.word.trim().lowercase() }.toSet()
        val overlaps = batch.map { it.word.trim().lowercase() }.filter { it in previousWords }
        assertTrue("A1 batch duplicates existing words: ${overlaps.joinToString()}", overlaps.isEmpty())

        batch.forEach { entry ->
            assertTrue(entry.word.isNotBlank())
            assertTrue(entry.czech.isNotBlank())
            assertTrue(entry.exampleGerman.isNotBlank())
            assertTrue(entry.exampleCzech.isNotBlank())
            assertEquals(entry.kind == FrequencyWordKind.NOUN, entry.article != null)
            if (entry.article != null) assertTrue(entry.article in setOf("der", "die", "das"))
            val question = frequencyVocabularyReviewQuestion(entry, Random(entry.id.hashCode()))
            assertEquals(entry.id, question.id)
            assertEquals("A1", question.cefrLevel)
            assertEquals(entry.germanAnswer, question.choices[question.correctIndex])
            assertEquals(4, question.choices.size)
            assertEquals(4, question.choices.toSet().size)
            assertEquals(entry.exampleGerman, question.germanSentence)
            assertEquals(entry.exampleCzech, question.translation)
        }
    }
}
