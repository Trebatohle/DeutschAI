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
        assertEquals("Seven grammar and vocabulary areas are expected", 7, wordToolGroups.size)
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
            "sein", "seine", "seinem",
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
    fun generatedWordToolQuestionsHaveFourDistinctChoicesAndOneValidCorrectIndex() {
        wordToolGroups.forEach { group ->
            wordToolModes.forEach { mode ->
                val questions = makeWordToolQuestions(group, mode)
                assertEquals("${group.id}/${mode.id} should have one question per item", group.items.size, questions.size)
                questions.forEachIndexed { index, question ->
                    assertEquals("${group.id}/${mode.id} question $index should have four choices", 4, question.choices.size)
                    val normalizedChoices = question.choices.map { it.trim().lowercase() }
                    assertEquals(
                        "${group.id}/${mode.id} question $index has duplicate visible answers",
                        normalizedChoices.size,
                        normalizedChoices.toSet().size
                    )
                    assertTrue(
                        "${group.id}/${mode.id} question $index has invalid correct index ${question.correctIndex}",
                        question.correctIndex in question.choices.indices
                    )
                    when (mode.id) {
                        "words" -> assertEquals(question.fullCzech, question.choices[question.correctIndex])
                        "challenge" -> assertEquals(question.fullCzech, question.choices[question.correctIndex])
                        "sentences" -> assertTrue(
                            "${group.id} correct form is missing from the full German sentence",
                            question.fullGerman.contains(question.choices[question.correctIndex], ignoreCase = true)
                        )
                    }
                }
            }
        }
    }

    @Test
    fun possessivePracticeHasCorrectOurTranslationAndNoDuplicateMeinemMeaning() {
        val group = wordToolGroups.single { it.id == "possessives" }
        assertFalse(
            "The misspelling 'náše' must not appear in possessive practice",
            group.items.any { it.czech.contains("náše", ignoreCase = true) }
        )
        val unser = group.items.single { it.german == "unsere" }
        assertTrue(unser.czech.startsWith("naše", ignoreCase = true))
        assertEquals("Unsere Schule ist groß.", unser.sentenceGerman)
        assertEquals("Naše škola je velká.", unser.sentenceCzech)

        val wordMode = wordToolModes.single { it.id == "words" }
        val meinemQuestions = makeWordToolQuestions(group, wordMode).filter { it.prompt.contains("meinem") }
        assertTrue("The question for 'meinem' must be present", meinemQuestions.isNotEmpty())
        meinemQuestions.forEach { question ->
            assertTrue("The 'meinem' question should include a sentence context", question.prompt.contains("Ich helfe meinem Vater."))
            assertEquals(
                "The identical Czech gloss must appear only once as a choice",
                1,
                question.choices.count { it == "svému (mužský rod, 3. pád)" }
            )
        }
    }

    @Test
    fun offlineDailyTrainingPoolIsLargeUniqueAndEveryQuestionHasOneCorrectChoice() {
        val pool = buildDailyTrainingPool()
        assertTrue("The daily pool should reuse a broad amount of bundled content", pool.size >= 100)
        val ids = pool.map { it.id }
        assertEquals("Daily training IDs must be unique", ids.size, ids.toSet().size)

        pool.forEach { question ->
            assertTrue("Question ${question.id} has no prompt", question.prompt.isNotBlank())
            assertTrue("Question ${question.id} has too few answers", question.choices.size >= 3)
            assertTrue(
                "Question ${question.id} must have a valid correct answer",
                question.correctIndex in question.choices.indices
            )
            val normalizedChoices = question.choices.map { it.trim().lowercase() }
            assertEquals(
                "Question ${question.id} contains a duplicate displayed answer",
                normalizedChoices.size,
                normalizedChoices.toSet().size
            )
            assertTrue("Question ${question.id} has no source kind", question.sourceKind.isNotBlank())
        }
        assertEquals(
            "Stable review IDs should not change between daily-trainer builds",
            stableReviewId("sentence", "first_steps", "0"),
            stableReviewId("sentence", "first_steps", "0")
        )
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

    @Test
    fun numberPracticeHasThreeSeparateTwentyFiveQuestionGroups() {
        assertEquals(setOf("numbers", "ordinal-numbers", "multiplicative-numbers"), numberWordToolGroupIds)
        numberWordToolGroupIds.forEach { groupId ->
            val group = wordToolGroups.single { it.id == groupId }
            assertEquals("$groupId should contain 25 items", 25, group.items.size)
            group.items.forEachIndexed { index, item ->
                assertTrue("$groupId item $index has no German form", item.german.isNotBlank())
                assertTrue("$groupId item $index has no Czech meaning", item.czech.isNotBlank())
                assertTrue("$groupId item $index has no German example", item.sentenceGerman.isNotBlank())
                assertTrue("$groupId item $index has no Czech example", item.sentenceCzech.isNotBlank())
            }
        }
    }

    @Test
    fun mixedNumberDrillContainsOneHundredQuestionsAcrossAllThreeKinds() {
        val questions = buildMixedNumberDrillQuestions()
        assertEquals(100, questions.size)
        assertEquals(100, questions.map { it.id }.toSet().size)

        val counts = questions.groupingBy { it.kind }.eachCount()
        assertEquals(setOf("basic", "ordinal", "multiplicative"), counts.keys)
        assertEquals(34, counts["basic"])
        assertEquals(33, counts["ordinal"])
        assertEquals(33, counts["multiplicative"])
    }

    @Test
    fun everyMixedNumberQuestionHasFourUniqueAnswersAndACompleteSentence() {
        buildMixedNumberDrillQuestions().forEach { question ->
            assertEquals("Question ${question.id} should have four options", 4, question.choices.size)
            assertEquals(
                "Question ${question.id} has duplicate answer options",
                4,
                question.choices.map { it.lowercase() }.toSet().size
            )
            assertTrue(question.correctIndex in question.choices.indices)
            assertTrue(question.fullGerman.isNotBlank())
            assertFalse(question.fullGerman.contains("___"))
            assertTrue(question.fullGerman.contains(question.choices[question.correctIndex]))
            assertTrue(question.fullCzech.isNotBlank())
            assertTrue(question.explanation.contains(question.choices[question.correctIndex]))
        }
    }

    @Test
    fun individualNumberCategoriesGenerateTwentyFiveFourChoiceQuestions() {
        numberWordToolGroupIds.forEach { groupId ->
            val questions = makeNumberCategoryQuestions(groupId)
            assertEquals(groupId, 25, questions.size)
            questions.forEach { question ->
                assertEquals(4, question.choices.size)
                assertEquals(4, question.choices.toSet().size)
                assertTrue(question.correctIndex in question.choices.indices)
                assertTrue(
                    "${question.id} correct number form is missing from its German sentence",
                    question.fullGerman.contains(question.choices[question.correctIndex], ignoreCase = true)
                )
                assertTrue(question.fullCzech.isNotBlank())
            }
        }
    }


    @Test
    fun vocabularyCatalogueContainsExactlyTwoThousandUniqueCompleteNouns() {
        assertEquals("The vocabulary learning pack must contain exactly 2,000 items", 2000, vocabularyCatalog.size)
        val normalizedWords = vocabularyCatalog.map { it.word.trim().lowercase() }
        assertEquals("Vocabulary words must be unique", normalizedWords.size, normalizedWords.toSet().size)
        val ids = vocabularyCatalog.map { it.id }
        assertEquals("Vocabulary IDs must be unique", ids.size, ids.toSet().size)

        vocabularyCatalog.forEach { entry ->
            assertTrue("${entry.word} has an invalid German article", entry.article in setOf("der", "die", "das"))
            assertTrue("A vocabulary item has a blank German noun", entry.word.isNotBlank())
            assertTrue("${entry.word} has a blank Czech meaning", entry.czech.isNotBlank())
            assertTrue("${entry.word} has a blank stable ID", entry.id.isNotBlank())
        }
    }

    @Test
    fun vocabularyNounsGenerateGermanAndCzechExampleSentences() {
        vocabularyCatalog.forEach { entry ->
            val german = vocabularySentenceGerman(entry)
            val czech = vocabularySentenceCzech(entry)
            assertTrue("${entry.word} has no German example", german.isNotBlank())
            assertTrue("${entry.word} example omits its article", german.contains(entry.article))
            assertTrue("${entry.word} example omits its noun", german.contains(entry.word))
            assertTrue("${entry.word} has no Czech example", czech.isNotBlank())
            assertTrue("${entry.word} Czech example omits its meaning", czech.contains(entry.czech))
        }
        assertEquals("Das ist der Tisch.", vocabularySentenceGerman(
            vocabularyCatalog.single { it.word == "Tisch" }
        ))
        assertEquals("To je stůl.", vocabularySentenceCzech(
            vocabularyCatalog.single { it.word == "Tisch" }
        ))
    }

    @Test
    fun vocabularyBatchContainsTwentyFiveRandomizedUniqueItems() {
        val batch = buildVocabularyBatch(vocabularyCatalog, count = 25, random = kotlin.random.Random(2026))
        assertEquals(25, batch.size)
        assertEquals(25, batch.map { it.entry.id }.toSet().size)
        batch.forEach { question ->
            assertTrue(question.germanText.isNotBlank())
            assertTrue(question.correctCzech.isNotBlank())
            assertEquals(4, question.choices.size)
            assertEquals(4, question.choices.toSet().size)
            assertTrue(question.correctIndex in question.choices.indices)
            assertEquals(question.correctCzech, question.choices[question.correctIndex])
            when (question.presentation) {
                VocabularyPresentation.WORD ->
                    assertEquals("${question.entry.article} ${question.entry.word}", question.germanText)
                VocabularyPresentation.SENTENCE ->
                    assertEquals(vocabularySentenceGerman(question.entry), question.germanText)
            }
        }
    }

    @Test
    fun vocabularyWordAndSentenceModesBothHaveFourUniqueCzechChoices() {
        val entry = vocabularyCatalog.single { it.word == "Tisch" }
        listOf(VocabularyPresentation.WORD, VocabularyPresentation.SENTENCE).forEach { presentation ->
            val question = makeVocabularyQuestion(entry, presentation, vocabularyCatalog, kotlin.random.Random(17))
            assertEquals(4, question.choices.size)
            assertEquals(4, question.choices.toSet().size)
            assertTrue(question.correctIndex in question.choices.indices)
            assertEquals(question.correctCzech, question.choices[question.correctIndex])
        }
    }

}
