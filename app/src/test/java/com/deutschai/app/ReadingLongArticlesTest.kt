package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReadingLongArticlesTest {
    @Test
    fun tenNewA1AndA2StoriesAreIncludedAndEachExceedsOneThousandWords() {
        val expectedA1 = setOf(
            "a1-garten-hinter-dem-haus",
            "a1-tag-im-tierheim",
            "a1-neue-klasse",
            "a1-fahrradgeschaeft",
            "a1-besonderer-geburtstag"
        )
        val expectedA2 = setOf(
            "a2-fahrradreise",
            "a2-cafe-der-ideen",
            "a2-praktikum-im-hotel",
            "a2-alter-bahnhof",
            "a2-brief-aus-der-vergangenheit"
        )
        val allIds = readingArticles.map { it.id }
        assertEquals("Reading article IDs must remain unique", allIds.size, allIds.toSet().size)

        val newA1 = readingArticles.filter { it.id in expectedA1 }
        val newA2 = readingArticles.filter { it.id in expectedA2 }
        assertEquals("Exactly five new A1 articles must be available", 5, newA1.size)
        assertEquals("Exactly five new A2 articles must be available", 5, newA2.size)
        assertEquals(expectedA1, newA1.map { it.id }.toSet())
        assertEquals(expectedA2, newA2.map { it.id }.toSet())

        (newA1 + newA2).forEach { article ->
            assertTrue("${article.id} must contain more than 1,000 words (found ${article.wordCount})", article.wordCount > 1000)
            assertTrue("${article.id} must contain multiple paragraphs", article.body.split("\n\n").size >= 10)
            assertEquals(
                "${article.id} must not repeat entire paragraphs",
                article.body.split("\n\n").size,
                article.body.split("\n\n").toSet().size
            )
        }
        newA1.forEach { assertEquals("A1", it.level) }
        newA2.forEach { assertEquals("A2", it.level) }
    }
}
