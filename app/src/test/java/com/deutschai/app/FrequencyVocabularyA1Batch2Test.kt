package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FrequencyVocabularyA1Batch2Test {
    @Test
    fun containsExactlyThirtyNineUniqueA1WordsWithoutCatalogueDuplicates() {
        assertEquals(FREQUENCY_VOCABULARY_A1_BATCH2_COUNT, frequencyVocabularyA1Batch2.size)
        assertEquals(39, frequencyVocabularyA1Batch2.map { it.word.trim().lowercase() }.toSet().size)
        assertTrue(frequencyVocabularyA1Batch2.all { it.level == "A1" })
        assertTrue(frequencyVocabularyA1Batch2.all {
            it.word.isNotBlank() && it.czech.isNotBlank() &&
                it.exampleGerman.isNotBlank() && it.exampleCzech.isNotBlank()
        })
        assertTrue(frequencyVocabularyA1Batch2.all {
            (it.kind == FrequencyWordKind.NOUN) == (it.article != null)
        })
        assertTrue(frequencyVocabularyA1Batch2.filter { it.article != null }.all {
            it.article in setOf("der", "die", "das")
        })

        val catalogueCounts = frequencyVocabularyCatalog
            .groupingBy { it.word.trim().lowercase() }
            .eachCount()
        val duplicates = frequencyVocabularyA1Batch2
            .map { it.word.trim().lowercase() }
            .filter { catalogueCounts[it] != 1 }
        assertTrue(
            "A1 batch must contain no duplicate of any existing catalogue entry: " +
                duplicates.joinToString(),
            duplicates.isEmpty()
        )
    }
}
