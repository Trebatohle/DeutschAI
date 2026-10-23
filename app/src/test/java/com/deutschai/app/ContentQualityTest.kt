package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentQualityTest {
    private val additionalStories = readingArticlesA1Extra +
        readingArticlesA2Extra +
        readingArticlesB1Extra +
        readingArticlesB2Extra

    @Test
    fun everyNewA1ToB2StoryExceedsOneThousandWords() {
        assertEquals("Eight additional stories are expected", 8, additionalStories.size)
        additionalStories.forEach { article ->
            assertTrue(
                "${article.id} has only ${article.wordCount} words",
                article.wordCount > 1000
            )
        }
    }

    @Test
    fun bundledReadingStoriesHaveUniqueIdsAndNonEmptyBodies() {
        val allIds = readingArticles.map { it.id }
        assertEquals("Reading article IDs must be unique", allIds.size, allIds.toSet().size)
        readingArticles.forEach { article ->
            assertTrue("${article.id} has no title", article.title.isNotBlank())
            assertTrue("${article.id} has no German text", article.body.isNotBlank())
        }
    }

    @Test
    fun everyReadingStoryExceedsOneThousandWordsAndHasNoRepeatedParagraphs() {
        readingArticles.forEach { article ->
            assertTrue(
                "${article.id} has only ${article.wordCount} words",
                article.wordCount > 1000
            )
            val paragraphs = article.body.trim()
                .split(Regex("\\n\\s*\\n"))
                .map { it.trim() }
                .filter { it.isNotEmpty() }
            assertEquals(
                "${article.id} contains a duplicated paragraph",
                paragraphs.size,
                paragraphs.toSet().size
            )
        }
    }

    @Test
    fun additionalStoriesDoNotRepeatTheirOwnParagraphs() {
        additionalStories.forEach { article ->
            val paragraphs = article.body.trim()
                .split(Regex("\\n\\s*\\n"))
                .map { it.trim() }
                .filter { it.isNotEmpty() }
            assertEquals(
                "${article.id} contains a duplicated paragraph",
                paragraphs.size,
                paragraphs.toSet().size
            )
        }
    }

    @Test
    fun nounTranslationLookupFindsEntriesStoredWithAnArticle() {
        val sample = ReadingArticle(
            id = "test",
            level = "A1",
            title = "Test",
            subtitle = "Test",
            body = ""
        )

        assertEquals("stůl", lookupReadingWordTranslation(sample, "Tisch"))
        assertEquals("tabulka", lookupReadingWordTranslation(sample, "Tabelle"))
        assertEquals("tabulky", lookupReadingWordTranslation(sample, "Tabellen"))
        assertEquals("stoly", lookupReadingWordTranslation(sample, "Tische"))
        assertEquals("stolech", lookupReadingWordTranslation(sample, "Tischen"))
    }

    @Test
    fun correctedA2GlossaryEntryMatchesTheGermanPlural() {
        val article = readingArticlesA2.first { it.id == "a2-hund-max" }
        assertEquals("misky", lookupReadingWordTranslation(article, "Näpfe"))
        assertFalse(article.glossary.containsKey("Nä pfe"))
    }

    @Test
    fun vocabularyEntriesAreStructurallyComplete() {
        assertTrue(articleCategories.isNotEmpty())
        articleCategories.forEach { category ->
            assertTrue("${category.id} has no vocabulary", category.words.isNotEmpty())
            category.words.forEach { word ->
                assertTrue("${category.id} has a blank German word", word.word.isNotBlank())
                assertTrue("${category.id} has a blank Czech meaning for ${word.word}", word.czech.isNotBlank())
                assertTrue(
                    "${category.id} has an invalid article for ${word.word}",
                    word.article in setOf("der", "die", "das")
                )
            }
        }
    }

    @Test
    fun verbPracticeReferencesExistingVerbsAndHasFiveExamples() {
        val verbIds = verbDefinitions.map { it.id }
        assertEquals("Verb IDs must be unique", verbIds.size, verbIds.toSet().size)
        verbDefinitions.forEach { verb ->
            assertTrue("${verb.id} is missing its infinitive", verb.infinitive.isNotBlank())
            assertTrue("${verb.id} is missing its Czech meaning", verb.czech.isNotBlank())
            assertEquals("${verb.id} should have five phrase examples", 5, verb.phrases.size)
            verb.phrases.forEach { phrase ->
                assertTrue("${verb.id} has an empty German phrase", phrase.german.isNotBlank())
                assertTrue("${verb.id} has an empty Czech phrase", phrase.czech.isNotBlank())
            }
        }

        val knownVerbIds = verbIds.toSet()
        val allTopics = verbPracticeTopics + advancedVerbPracticeTopics
        val topicIds = allTopics.map { it.id }
        assertEquals("Verb topic IDs must be unique", topicIds.size, topicIds.toSet().size)
        allTopics.forEach { topic ->
            assertEquals("${topic.id} should contain five verbs", 5, topic.verbIds.size)
            topic.verbIds.forEach { verbId ->
                assertTrue("${topic.id} refers to missing verb ${verbId}", verbId in knownVerbIds)
            }
        }
    }

    @Test
    fun sentenceCoursesContainCompleteGermanAndCzechPairs() {
        assertTrue(sentenceCourseCatalog.isNotEmpty())
        val ids = sentenceCourseCatalog.map { it.id }
        assertEquals("Sentence course IDs must be unique", ids.size, ids.toSet().size)
        sentenceCourseCatalog.forEach { course ->
            assertEquals("${course.id} should have 25 sentence items", 25, course.items.size)
            course.items.forEachIndexed { index, item ->
                assertTrue("${course.id} item ${index} has no German sentence", item.german.isNotBlank())
                assertTrue("${course.id} item ${index} has no Czech translation", item.czech.isNotBlank())
            }
        }
    }

    @Test
    fun guidedDialoguesHaveThreeCompleteChoicesPerTurn() {
        assertTrue(guidedDialogueTopics.isNotEmpty())
        guidedDialogueTopics.forEach { topic ->
            assertTrue("${topic.id} has no turns", topic.turns.isNotEmpty())
            topic.turns.forEachIndexed { index, turn ->
                assertTrue("${topic.id} turn ${index} has no German prompt", turn.promptGerman.isNotBlank())
                assertTrue("${topic.id} turn ${index} has no Czech prompt", turn.promptCzech.isNotBlank())
                assertEquals("${topic.id} turn ${index} should have three options", 3, turn.options.size)
                turn.options.forEach { option ->
                    assertTrue("${topic.id} has an empty German answer", option.german.isNotBlank())
                    assertTrue("${topic.id} has an empty Czech answer", option.czech.isNotBlank())
                    assertTrue("${topic.id} has an empty German reply", option.replyGerman.isNotBlank())
                    assertTrue("${topic.id} has an empty Czech reply", option.replyCzech.isNotBlank())
                }
            }
        }
    }

    @Test
    fun cakeAndTableTranslationsRemainDistinctAndCorrect() {
        val translatedItems = sentenceCourseCatalog.flatMap { it.items }
        assertTrue(
            translatedItems.any {
                it.german == "Der Kuchen ist wirklich lecker." &&
                    it.czech == "Ten dort je opravdu výborný."
            }
        )
        assertFalse(
            translatedItems.any {
                it.german == "Der Kuchen ist wirklich lecker." &&
                    it.czech == "Dort je opravdu výborný."
            }
        )
    }

    @Test
    fun wordToolGroupsContainTwentyFiveCompletePracticeItems() {
        assertEquals("Five grammar and vocabulary areas are expected", 5, wordToolGroups.size)
        val ids = wordToolGroups.map { it.id }
        assertEquals("Word-tool group IDs must be unique", ids.size, ids.toSet().size)

        wordToolGroups.forEach { group ->
            assertEquals("${group.id} should contain 25 items", 25, group.items.size)
            group.items.forEachIndexed { index, item ->
                assertTrue("${group.id} item ${index} has no German expression", item.german.isNotBlank())
                assertTrue("${group.id} item ${index} has no Czech meaning", item.czech.isNotBlank())
                assertTrue("${group.id} item ${index} has no German example", item.sentenceGerman.isNotBlank())
                assertTrue("${group.id} item ${index} has no Czech example", item.sentenceCzech.isNotBlank())
            }
        }
    }

    @Test
    fun possessivePronounPracticeCoversCoreFormsAndGrammaticalCases() {
        val group = wordToolGroups.single { it.id == "possessives" }
        val forms = group.items.map { it.german }.toSet()
        listOf(
            "mein", "meine", "meinen", "meinem", "meiner",
            "dein", "deine", "deinen", "deinem", "deiner",
            "sein", "seine", "seinen", "seinem",
            "ihr", "ihre", "ihren", "unser", "unsere", "euer", "Ihre"
        ).forEach { required ->
            assertTrue("Possessive form $required is missing", required in forms)
        }
        assertTrue(group.subtitle.contains("rod", ignoreCase = true))
        assertTrue(group.subtitle.contains("pád", ignoreCase = true))
        assertTrue(group.subtitle.contains("čísla", ignoreCase = true))
        group.items.forEach { item ->
            assertTrue(
                "Example for ${item.german} should include the form it teaches",
                item.sentenceGerman.contains(item.german, ignoreCase = true)
            )
        }
    }

    @Test
    fun wellKnownDictionaryMeaningsAreNotOverriddenByMachineTranslation() {
        val article = ReadingArticle(
            id = "test",
            level = "A1",
            title = "Test",
            subtitle = "Test",
            body = ""
        )
        assertNotEquals("tabulka", lookupReadingWordTranslation(article, "Tisch"))
        assertEquals("stůl", lookupReadingWordTranslation(article, "Tisch"))
    }
}
