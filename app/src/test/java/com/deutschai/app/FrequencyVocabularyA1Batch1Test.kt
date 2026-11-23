package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FrequencyVocabularyA1Batch1Test {
    @Test
    fun containsExactlyFiftyUniqueA1Words() {
        assertEquals(FREQUENCY_VOCABULARY_A1_BATCH1_COUNT, frequencyVocabularyA1Batch1.size)
        assertEquals(50, frequencyVocabularyA1Batch1.map { it.word.trim().lowercase() }.toSet().size)
        assertTrue(frequencyVocabularyA1Batch1.all { it.level == "A1" })
        assertTrue(frequencyVocabularyA1Batch1.all {
            it.word.isNotBlank() && it.czech.isNotBlank() &&
                it.exampleGerman.isNotBlank() && it.exampleCzech.isNotBlank()
        })
        assertTrue(frequencyVocabularyA1Batch1.all {
            (it.kind == FrequencyWordKind.NOUN) == (it.article != null)
        })
        assertTrue(frequencyVocabularyA1Batch1.filter { it.article != null }.all {
            it.article in setOf("der", "die", "das")
        })
        val batchIds = frequencyVocabularyA1Batch1.map(FrequencyVocabularyEntry::id).toSet()
        val oldWords = frequencyVocabularyCatalog
            .filterNot { it.level == "A1" && it.id in batchIds }
            .map { it.word.trim().lowercase() }.toSet()
        val duplicates = frequencyVocabularyA1Batch1.map { it.word.trim().lowercase() }.filter { it in oldWords }
        println("A1 BATCH DIAGNOSTIC: overlaps=${duplicates.joinToString()}, catalogueUnique=${frequencyVocabularyCatalog.map { it.word.trim().lowercase() }.toSet().size}")
        assertTrue("A1 batch overlaps existing vocabulary: " + duplicates.joinToString(), duplicates.isEmpty())
    }
}
