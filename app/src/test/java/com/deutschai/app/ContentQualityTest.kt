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
    fun readingNotebookQuestionsIncludeSavedWordAndOriginalSentence() {
        val item = ReadingNotebookWord(
            id = "reader-test-1",
            word = "entscheiden",
            meaning = "rozhodnout",
            exampleGerman = "Sie muss heute entscheiden.",
            sourceTitle = "Ein neuer Anfang",
            sourceLevel = "B1",
            savedAtMillis = 123L
        )

        val questions = buildPersonalNotebookQuestions(
            catalogEntries = emptyList(),
            readingEntries = listOf(item),
            count = 10,
            random = kotlin.random.Random(19)
        )

        assertEquals(1, questions.size)
        val question = questions.single()
        assertTrue(question.prompt.contains("entscheiden"))
        assertEquals(4, question.choices.size)
        assertEquals(4, question.choices.toSet().size)
        assertEquals("rozhodnout", question.choices[question.correctIndex])
        assertEquals("Sie muss heute entscheiden.", question.germanSentence)
        assertTrue(question.explanation.contains("Ein neuer Anfang"))
    }

    @Test
    fun readingNotebookWordsAreSortedAlphabeticallyAndMeaningIsStoredSeparately() {
        val words = listOf(
            ReadingNotebookWord("2", "zuerst", "nejprve", "Zuerst essen wir.", "Text", "A2", 2L),
            ReadingNotebookWord("1", "ankommen", "dorazit", "Wir kommen an.", "Text", "A2", 1L)
        )

        val result = filterReadingNotebookWords(words)

        assertEquals(listOf("ankommen", "zuerst"), result.map { it.word })
        assertEquals("dorazit", result.first().meaning)
    }

    @Test
    fun readingExampleSentenceReturnsTheSentenceContainingTheSelectedWord() {
        val article = readingArticles.first()
        val word = article.body.split(Regex("\\s+")).first { it.length > 4 }
            .trim(',', '.', '!', '?', ';', ':', '„', '“', '"', '(', ')')
        val sentence = readingExampleSentence(article, word)

        assertTrue(sentence.isNotBlank())
        assertTrue(sentence.contains(word, ignoreCase = true))
    }

    @Test
    fun personalVocabularyNotebookFiltersAndSortsOnlySavedEntries() {
        val sample = vocabularyCatalog.take(12)
        val savedIds = sample.filterIndexed { index, _ -> index % 3 == 0 }.map { it.id }.toSet()

        val result = filterVocabularyNotebookEntries(sample, savedIds)

        assertEquals(savedIds, result.map { it.id }.toSet())
        assertEquals(result.size, result.map { it.id }.toSet().size)
        assertEquals(
            result.map { it.word.lowercase() }.sorted(),
            result.map { it.word.lowercase() }
        )
    }

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
    fun rewardPointsAreOnlyEarnedForPerfectResultsAndOnlyOncePerUniqueTest() {
        assertFalse(qualifiesForRewardPoint(9, 10))
        assertFalse(qualifiesForRewardPoint(10, 0))
        assertTrue(qualifiesForRewardPoint(10, 10))

        val firstAward = addRewardedTest(emptySet(), "sentence_a1_greetings")
        assertEquals(1, firstAward.size)

        val sameTestAgain = addRewardedTest(firstAward, "sentence_a1_greetings")
        assertEquals("Repeating the same test must not award another point", 1, sameTestAgain.size)

        val secondTest = addRewardedTest(sameTestAgain, "sentence_a1_shopping")
        assertEquals(2, secondTest.size)
    }

    @Test
    fun fifteenUniquePerfectTestsUnlockTheFifteenPercentLicenseReward() {
        val beforeUnlock = RewardPointsProgress(14)
        assertEquals(14, beforeUnlock.displayedPoints)
        assertEquals(1, beforeUnlock.pointsRemaining)
        assertEquals(14f / 15f, beforeUnlock.progressFraction, 0.0001f)
        assertFalse(beforeUnlock.discountUnlocked)

        val unlocked = RewardPointsProgress(15)
        assertEquals(15, unlocked.displayedPoints)
        assertEquals(0, unlocked.pointsRemaining)
        assertEquals(1f, unlocked.progressFraction)
        assertTrue(unlocked.discountUnlocked)
        assertEquals(15, LICENSE_DISCOUNT_PERCENT)
    }

    @Test
    fun sentenceLessonPracticeUsesFiveWritingAndFiveDifferentDictationQuestions() {
        val course = sentenceCourseCatalog.first { it.level == "A1" }
        val questions = sentenceCourseQuestions(course.id)
        val selection = createLessonPracticeSelection(questions, kotlin.random.Random(41))

        assertEquals(25, selection.introduction.size)
        assertEquals(5, selection.writing.size)
        assertEquals(5, selection.dictation.size)
        assertEquals(25, selection.introduction.toSet().size)
        assertEquals(
            10,
            (selection.writing + selection.dictation)
                .map { it.germanSentence }
                .toSet()
                .size
        )
        assertEquals(
            selection.introduction.map { it.germanSentence }.toSet(),
            (selection.writing + selection.dictation).map { it.germanSentence }
                .toSet() + selection.introduction.drop(10).map { it.germanSentence }
        )
    }

    @Test
    fun everySentenceCourseKeepsTwentyFiveFourChoiceIntroductionQuestions() {
        sentenceCourseCatalog.forEach { course ->
            val questions = sentenceCourseQuestions(course.id)
            assertEquals("${course.id} must keep 25 introduction questions", 25, questions.size)
            questions.forEachIndexed { index, question ->
                assertEquals("${course.id} question $index must have four choices", 4, question.choices.size)
                assertEquals("${course.id} question $index must have unique choices", 4, question.choices.toSet().size)
                assertTrue(
                    "${course.id} question $index must point to the correct German phrase",
                    question.correctIndex in question.choices.indices &&
                        question.choices[question.correctIndex] == question.germanSentence
                )
                assertTrue("${course.id} question $index needs a Czech prompt", question.prompt.isNotBlank())
            }
        }
    }

    @Test
    fun writtenGermanAnswersAreStrictExceptForHarmlessWhitespaceAndExplicitAlternatives() {
        val question = LessonQuestion(
            prompt = "Přelož do němčiny.",
            choices = emptyList(),
            correctIndex = 0,
            explanation = "",
            germanSentence = "Ich möchte einen Kaffee.",
            translation = "Chtěl bych kávu.",
            acceptedAnswers = listOf("Ich hätte gern einen Kaffee.")
        )

        assertTrue(isGermanWrittenAnswerCorrect(question, "Ich möchte einen Kaffee."))
        assertTrue(isGermanWrittenAnswerCorrect(question, "  Ich   möchte einen Kaffee.  "))
        assertTrue(isGermanWrittenAnswerCorrect(question, "Ich hätte gern einen Kaffee."))
        assertFalse(isGermanWrittenAnswerCorrect(question, "ich möchte einen Kaffee."))
        assertFalse(isGermanWrittenAnswerCorrect(question, "Ich mochte einen Kaffee."))
        assertFalse(isGermanWrittenAnswerCorrect(question, "Ich möchte einen Kaffee"))
        assertFalse(isGermanWrittenAnswerCorrect(question, "Ich möchte Kaffee."))
        assertFalse(isGermanWrittenAnswerCorrect(question, ""))
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
    fun branchingDialogueScenariosHaveValidGraphsAndMoreThanOnePossibleRoute() {
        assertEquals(6, branchingGuidedDialogueTopics.size)
        branchingGuidedDialogueTopics.forEach { topic ->
            assertTrue(
                "${topic.id} has graph errors: ${branchingDialogueValidationErrors(topic)}",
                branchingDialogueValidationErrors(topic).isEmpty()
            )
            val paths = branchingDialoguePaths(topic)
            assertTrue("${topic.id} should have at least two possible paths", paths.size >= 2)
            assertTrue("${topic.id} paths should actually diverge", paths.distinct().size >= 2)
            assertTrue(paths.all { it.first() == topic.startNodeId })
            topic.nodes.forEach { node ->
                assertEquals(3, node.options.size)
                assertEquals(1, node.options.count { it.quality == DialogueAnswerQuality.RECOMMENDED })
            }
        }
    }

    @Test
    fun additionalBranchingScenariosCoverWorkTravelComplaintsAndPhoneCalls() {
        val expectedIds = setOf(
            "team_meeting_b1",
            "train_travel_b1",
            "customer_complaint_b1",
            "phone_appointment_a2"
        )
        assertEquals(expectedIds, additionalBranchingDialogueTopics.map { it.id }.toSet())
        additionalBranchingDialogueTopics.forEach { topic ->
            assertTrue("Scenario should expose more than one route", branchingDialoguePaths(topic).size >= 2)
            assertTrue("Every added scenario should have at least five situations", topic.nodes.size >= 5)
            assertTrue(topic.subtitle.contains(topic.level))
        }
    }

    @Test
    fun branchingDialogueMistakesCreateSpacedReviewWithRecommendedAnswer() {
        val topic = branchingGuidedDialogueTopics.first()
        val node = topic.nodes.first { it.id == topic.startNodeId }
        val mistakeIndex = node.options.indexOfFirst { it.quality == DialogueAnswerQuality.NEEDS_WORK }
        val recommendedIndex = node.options.indexOfFirst { it.quality == DialogueAnswerQuality.RECOMMENDED }

        val review = buildBranchingDialogueReview(topic, node, mistakeIndex)

        assertFalse(review.wasCorrect)
        assertEquals(recommendedIndex, review.question.correctIndex)
        assertEquals(3, review.question.choices.size)
        assertTrue(review.question.explanation.contains("Lepší formulace"))
        assertEquals("phrases", review.question.sourceKind)
    }

    @Test
    fun acceptableBranchingDialogueAlternativesAreNotMarkedAsMistakes() {
        val topic = branchingGuidedDialogueTopics.last()
        val node = topic.nodes.first { it.id == topic.startNodeId }
        val alternativeIndex = node.options.indexOfFirst { it.quality == DialogueAnswerQuality.ACCEPTABLE }

        val review = buildBranchingDialogueReview(topic, node, alternativeIndex)

        assertTrue(review.wasCorrect)
        assertEquals(alternativeIndex, review.question.correctIndex)
        assertTrue(review.question.explanation.isNotBlank())
    }

    @Test
    fun expandedA1AndA2SentenceCoursesAddTenNewSectionsAndTwoHundredFiftyPhrases() {
        val expectedIds = setOf("a1_visiting", "a1_health_everyday", "a1_hobbies_sports", "a1_weather_clothes", "a1_phone_messages", "a2_money_payments", "a2_transport_city", "a2_services_travel", "a2_cooking_household", "a2_goals_experiences")
        val addedCourses = sentenceCourseCatalog.filter { it.id in expectedIds }
        assertEquals("Ten new course sections should be present", 10, addedCourses.size)
        assertEquals("Five added sections should belong to A1", 5, addedCourses.count { it.level == "A1" })
        assertEquals("Five added sections should belong to A2", 5, addedCourses.count { it.level == "A2" })
        assertEquals("The new sections should contain exactly 250 phrases", 250, addedCourses.sumOf { it.items.size })
        addedCourses.forEach { course ->
            assertEquals("${course.id} should contain 25 phrases", 25, course.items.size)
            assertEquals("${course.id} should produce 25 lesson questions", 25, sentenceCourseQuestions(course.id).size)
            course.items.forEachIndexed { index, item ->
                assertTrue("${course.id} item ${index + 1} has a blank prompt", item.prompt.isNotBlank())
                assertTrue("${course.id} item ${index + 1} has a blank German phrase", item.german.isNotBlank())
                assertTrue("${course.id} item ${index + 1} has a blank Czech translation", item.czech.isNotBlank())
            }
        }
    }

    @Test
    fun additionalA1AndA2SpeakingCoursesContainFiftyCoursesAnd1250UniqueNewPhrases() {
        val addedCourseIds = setOf("a1_pets_at_home", "a1_veterinary_visit", "a1_post_office", "a1_library", "a1_hairdresser", "a1_florist", "a1_optician", "a1_photo_service", "a1_fuel_station", "a1_car_wash", "a1_bicycle_repair", "a1_swimming_pool", "a1_camping", "a1_forest_walk", "a1_gardening", "a1_houseplants", "a1_laundry_service", "a1_recycling", "a1_home_tools", "a1_lost_property", "a1_information_desk", "a1_zoo", "a1_museum", "a1_cinema", "a1_theatre_concerts", "a1_birthday_party", "a1_neighbours", "a1_school_supplies", "a1_kindergarten", "a1_community_volunteering", "a1_playground", "a1_stationery_shop", "a1_farmers_market", "a1_local_festival", "a1_animal_shelter", "a2_insurance_claim", "a2_car_rental", "a2_product_warranty", "a2_mobile_internet_contract", "a2_online_orders_delivery", "a2_apartment_handover", "a2_job_interview", "a2_shift_planning", "a2_parent_teacher_meeting", "a2_training_certification", "a2_volunteer_project", "a2_event_organisation", "a2_travel_disruptions", "a2_group_reservations", "a2_feedback_misunderstandings")
        val addedCourses = sentenceCourseCatalog.filter { it.id in addedCourseIds }

        assertEquals("Exactly 50 additional speaking courses should be present", 50, addedCourses.size)
        assertEquals("A1 should receive 35 new courses", 35, addedCourses.count { it.level == "A1" })
        assertEquals("A2 should receive 15 new courses", 15, addedCourses.count { it.level == "A2" })
        assertEquals("The added catalogue should contain 1,250 phrases", 1250, addedCourses.sumOf { it.items.size })

        addedCourses.forEach { course ->
            assertEquals("${course.id} should contain exactly 25 questions", 25, course.items.size)
            course.items.forEachIndexed { index, item ->
                assertTrue("${course.id} question ${index + 1} has no prompt", item.prompt.isNotBlank())
                assertTrue("${course.id} question ${index + 1} has no German phrase", item.german.isNotBlank())
                assertTrue("${course.id} question ${index + 1} has no Czech translation", item.czech.isNotBlank())
            }
        }

        fun normalizeGermanPhrase(value: String): String = value.lowercase()
            .filter { it.isLetterOrDigit() || it.isWhitespace() }
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .joinToString(" ")

        val previousGermanPhrases = sentenceCourseCatalog
            .filterNot { it.id in addedCourseIds }
            .flatMap { it.items }
            .map { normalizeGermanPhrase(it.german) }
            .toSet()
        val newGermanPhrases = addedCourses
            .flatMap { it.items }
            .map { normalizeGermanPhrase(it.german) }

        assertEquals("All 1,250 newly added German phrases must be unique", 1250, newGermanPhrases.toSet().size)
        val repeatedPhrases = newGermanPhrases.filter { it in previousGermanPhrases }
        assertTrue(
            "New phrases must not repeat existing course phrases: ${repeatedPhrases.joinToString()}",
            repeatedPhrases.isEmpty()
        )
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
    fun mixedNumberDrillContainsTwentyFiveRandomQuestionsAcrossAllThreeKinds() {
        val questions = buildMixedNumberDrillQuestions(kotlin.random.Random(2026))
        assertEquals(25, questions.size)
        assertEquals(25, questions.map { it.id }.toSet().size)

        val counts = questions.groupingBy { it.kind }.eachCount()
        assertEquals(setOf("basic", "ordinal", "multiplicative"), counts.keys)
        assertEquals(25, counts.values.sum())
        assertEquals(9, counts["basic"])
        assertEquals(8, counts["ordinal"])
        assertEquals(8, counts["multiplicative"])

        val nextRound = buildMixedNumberDrillQuestions(kotlin.random.Random(2027))
        assertEquals(25, nextRound.size)
        assertNotEquals(
            "A new round should select a different randomized question order and sample",
            questions.map { it.id },
            nextRound.map { it.id }
        )
    }

    @Test
    fun everyMixedNumberQuestionHasExactlyThreeChoicesAndOneOfEachNumberKind() {
        val requiredKinds = setOf("basic", "ordinal", "multiplicative")
        buildMixedNumberDrillQuestions(kotlin.random.Random(42)).forEach { question ->
            assertEquals("Question ${question.id} should have exactly three options", 3, question.choices.size)
            assertEquals(
                "Question ${question.id} has duplicate answer options",
                3,
                question.choices.map { it.lowercase() }.toSet().size
            )
            assertEquals(
                "Question ${question.id} must identify the kind of every answer",
                3,
                question.choiceKinds.size
            )
            assertEquals(
                "Question ${question.id} must offer one basic, one ordinal and one multiplicative numeral",
                requiredKinds,
                question.choiceKinds.toSet()
            )
            assertEquals(
                "Only the option matching the question's target kind may be correct",
                1,
                question.choiceKinds.count { it == question.kind }
            )
            assertTrue(question.correctIndex in question.choices.indices)
            assertEquals(question.kind, question.choiceKinds[question.correctIndex])
            assertTrue(question.fullGerman.isNotBlank())
            assertFalse(question.fullGerman.contains("___"))
            assertTrue(question.fullGerman.contains(question.choices[question.correctIndex]))
            assertTrue(question.fullCzech.isNotBlank())
            assertTrue(question.explanation.contains(question.choices[question.correctIndex]))
            assertTrue(question.prompt.startsWith("Doplň správnou číslovku:"))
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
    fun racletteGrillHasCorrectGermanArticleAndCzechMeaning() {
        val raclette = vocabularyCatalog.single { it.word == "Raclette-Grill" }
        assertEquals("der", raclette.article)
        assertEquals("stolní gril na raclette", raclette.czech)
    }

    @Test
    fun readingTranslationFallsBackToTheFullBundledVocabulary() {
        val uncommon = vocabularyCatalog.first { it.word == "Raclette-Grill" }
        val article = ReadingArticle(
            id = "translation-test",
            level = "A1",
            title = "Test",
            subtitle = "",
            body = "Das ist ein Test."
        )
        assertEquals(uncommon.czech, lookupReadingWordTranslation(article, uncommon.word))
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


    @Test
    fun activeVocabularyRecallRequiresTypedGermanAndKeepsNounArticle() {
        val table = vocabularyCatalog.single { it.word == "Tisch" }
        val reading = ReadingNotebookWord(
            id = "active-reading-1",
            word = "entscheiden",
            meaning = "rozhodnout",
            exampleGerman = "Sie muss heute entscheiden.",
            sourceTitle = "Ein neuer Anfang",
            sourceLevel = "B1",
            savedAtMillis = 123L
        )

        val questions = buildActiveVocabularyQuestions(
            catalogEntries = listOf(table),
            readingEntries = listOf(reading),
            count = 10,
            random = kotlin.random.Random(21)
        )

        assertEquals(2, questions.size)
        assertEquals(2, questions.map { it.id }.toSet().size)
        assertTrue(questions.any { it.czechPrompt == table.czech && it.answerGerman == "der Tisch" })
        assertTrue(questions.any { it.czechPrompt == "rozhodnout" && it.answerGerman == "entscheiden" })
        assertTrue(questions.all { it.exampleGerman.isNotBlank() })
        questions.forEach { question ->
            assertTrue(question.reviewChoices.size >= 2)
            assertEquals(
                "The correct German term must appear among review choices",
                1,
                question.reviewChoices.count { it == question.answerGerman }
            )
            assertTrue("Review choices must be unique", question.reviewChoices.size == question.reviewChoices.toSet().size)
        }
    }

    @Test
    fun activeVocabularyRecallDoesNotDuplicateWordsAlreadyInTheCuratedNotebook() {
        val table = vocabularyCatalog.single { it.word == "Tisch" }
        val reading = ReadingNotebookWord(
            id = "reading-table",
            word = "Tisch",
            meaning = "stůl",
            exampleGerman = "Der Tisch ist groß.",
            sourceTitle = "Ein Zimmer",
            sourceLevel = "A1",
            savedAtMillis = 123L
        )

        val questions = buildActiveVocabularyQuestions(
            catalogEntries = listOf(table),
            readingEntries = listOf(reading),
            random = kotlin.random.Random(5)
        )

        assertEquals(1, questions.size)
        assertEquals("der Tisch", questions.single().answerGerman)
    }

    @Test
    fun germanRecallAnswerNormalizationIgnoresCaseExtraSpacesAndFinalPunctuation() {
        assertEquals(
            normalizeGermanRecallAnswer("der Tisch."),
            normalizeGermanRecallAnswer("  DER   Tisch  ")
        )
        assertFalse(
            normalizeGermanRecallAnswer("das Tisch") == normalizeGermanRecallAnswer("der Tisch")
        )
        assertFalse(
            normalizeGermanRecallAnswer("schon") == normalizeGermanRecallAnswer("schön")
        )
    }

    @Test
    fun adaptiveReviewIntervalsUsePredictableEarlyStepsAndAdjustLater() {
        assertEquals(1, calculateNextReviewIntervalDays(0, 0, 0, 0, 0))
        assertEquals(3, calculateNextReviewIntervalDays(1, 1, 0, 1, 1))
        assertEquals(7, calculateNextReviewIntervalDays(2, 3, 0, 2, 2))
        assertEquals(14, calculateNextReviewIntervalDays(3, 7, 0, 3, 3))

        val highAccuracy = calculateNextReviewIntervalDays(
            correctStreak = 4,
            previousIntervalDays = 14,
            wrongCount = 0,
            attemptCount = 10,
            correctCount = 10
        )
        val repeatedMistakes = calculateNextReviewIntervalDays(
            correctStreak = 4,
            previousIntervalDays = 14,
            wrongCount = 4,
            attemptCount = 10,
            correctCount = 5
        )

        assertTrue("Strongly retained items should receive a longer interval", highAccuracy > repeatedMistakes)
        assertTrue("Review intervals must not exceed sixty days", calculateNextReviewIntervalDays(
            correctStreak = 12,
            previousIntervalDays = 60,
            wrongCount = 0,
            attemptCount = 20,
            correctCount = 20
        ) <= 60)
    }


    @Test
    fun listeningPracticeProvidesAllFourExerciseModesWithValidQuestions() {
        val meaning = buildListeningQuestions(
            "A1",
            ListeningExerciseMode.MEANING,
            count = 5,
            random = kotlin.random.Random(10)
        )
        assertEquals(5, meaning.size)
        meaning.forEach { question ->
            assertEquals(ListeningExerciseMode.MEANING, question.mode)
            assertEquals(4, question.choices.size)
            assertTrue(question.correctIndex in question.choices.indices)
            assertEquals(question.source.item.czech, question.answerText)
        }

        val gaps = buildListeningQuestions(
            "A1",
            ListeningExerciseMode.GAP,
            count = 5,
            random = kotlin.random.Random(11)
        )
        assertEquals(5, gaps.size)
        gaps.forEach { question ->
            assertEquals(ListeningExerciseMode.GAP, question.mode)
            assertEquals(4, question.choices.size)
            assertTrue(question.correctIndex in question.choices.indices)
            assertEquals(question.answerText, question.choices[question.correctIndex])
            assertTrue(question.displayWithGap.contains("_____"))
        }

        val dictation = buildListeningQuestions(
            "A1",
            ListeningExerciseMode.DICTATION,
            count = 5,
            random = kotlin.random.Random(12)
        )
        assertEquals(5, dictation.size)
        dictation.forEach { question ->
            assertEquals(ListeningExerciseMode.DICTATION, question.mode)
            assertTrue(question.choices.isEmpty())
            assertEquals(question.listeningText, question.answerText)
        }

        val context = buildListeningQuestions(
            "A1",
            ListeningExerciseMode.CONTEXT,
            count = 5,
            random = kotlin.random.Random(13)
        )
        assertEquals(5, context.size)
        context.forEach { question ->
            assertEquals(ListeningExerciseMode.CONTEXT, question.mode)
            assertEquals(4, question.choices.size)
            assertTrue(question.correctIndex in question.choices.indices)
            assertEquals(question.source.courseTitle, question.choices[question.correctIndex])
            assertTrue(question.listeningText.split(Regex("\\s+")).size > 5)
        }
    }

    @Test
    fun listeningModesKeepIndependentReviewHistoryForTheSamePhrase() {
        val courseId = sentenceCourseCatalog.first { it.level == "A1" }.id
        val itemIndex = 0
        val meaningId = stableReviewId("listening", "A1", ListeningExerciseMode.MEANING.name, courseId, itemIndex.toString())
        val gapId = stableReviewId("listening", "A1", ListeningExerciseMode.GAP.name, courseId, itemIndex.toString())
        assertNotEquals(meaningId, gapId)
    }

    @Test
    fun listeningConfidenceOffersExactlyThreeFixedIntervals() {
        val options = listeningConfidenceOptions()
        assertEquals(3, options.size)
        assertEquals(
            listOf(10L * 60L * 1000L, 24L * 60L * 60L * 1000L, 14L * 24L * 60L * 60L * 1000L),
            options.map { it.intervalMillis }
        )
        assertEquals(listOf("Zatím ne", "Nejsem si jistý", "Umím"), options.map { it.title })
        assertEquals(listOf("Znovu", "Zítra", "Za 14 dní"), options.map { it.subtitle })
    }

    @Test
    fun gapListeningCreatesOnlyTheRequestedTwentyFiveQuestionSession() {
        val questions = buildListeningQuestions(
            level = "A1",
            mode = ListeningExerciseMode.GAP,
            count = 25,
            random = kotlin.random.Random(404)
        )
        assertEquals(25, questions.size)
        assertEquals(25, questions.map { it.id }.toSet().size)
        questions.forEach { question ->
            assertEquals(4, question.choices.size)
            assertEquals(4, question.choices.toSet().size)
            assertTrue(question.correctIndex in question.choices.indices)
            assertEquals(question.answerText, question.choices[question.correctIndex])
            assertTrue(question.displayWithGap.contains("_____"))
        }
    }

    @Test
    fun listeningDictationNormalizationIgnoresCapitalizationAndPunctuationButKeepsUmlauts() {
        assertEquals(
            normalizeListeningDictation("Ich habe heute Zeit."),
            normalizeListeningDictation("  ICH habe heute ZEIT  ")
        )
        assertFalse(
            normalizeListeningDictation("schon heute") ==
                normalizeListeningDictation("schön heute")
        )
    }


    @Test
    fun phraseReviewQuestionsHaveFourRealGermanOptions() {
        val card = PhraseReviewCard(
            id = "A1_test_course_0",
            courseTitle = "První kroky",
            prompt = "Jak řekneš německy: Dnes mám čas.",
            german = "Ich habe heute Zeit.",
            czech = "Dnes mám čas."
        )

        val question = phraseReviewQuestion(card)

        assertEquals(4, question.choices.size)
        assertEquals(4, question.choices.toSet().size)
        assertEquals(card.german, question.choices[question.correctIndex])
        assertFalse(question.choices.contains("Ještě si nevzpomínám"))
    }

    @Test
    fun legacyScheduledListeningAndPhraseQuestionsAreUpgradedToFourRealChoices() {
        val legacyListening = ReviewQuestion(
            id = "listening_legacy_test",
            sourceKind = "listening",
            sourceTitle = "Test poslechu",
            prompt = "Zapiš celou německou větu, kterou uslyšíš:",
            choices = listOf("Ich habe heute Zeit.", "Jiná odpověď"),
            correctIndex = 0,
            explanation = "Správný přepis.",
            germanSentence = "Ich habe heute Zeit.",
            translation = "Dnes mám čas."
        )
        val listening = enrichScheduledReviewQuestion(legacyListening)
        assertEquals(4, listening.choices.size)
        assertEquals(4, listening.choices.toSet().size)
        assertEquals("Ich habe heute Zeit.", listening.choices[listening.correctIndex])
        assertTrue(listening.prompt.contains("slyšel/a"))

        val legacyPhrase = ReviewQuestion(
            id = "phrases_legacy_test",
            sourceKind = "phrases",
            sourceTitle = "Test fráze",
            prompt = "Jak bys řekl/a německy: Dnes mám čas.",
            choices = listOf("Ich habe heute Zeit.", "Ještě si nevzpomínám"),
            correctIndex = 0,
            explanation = "Překlad.",
            germanSentence = "Ich habe heute Zeit.",
            translation = "Dnes mám čas."
        )
        val phrase = enrichScheduledReviewQuestion(legacyPhrase)
        assertEquals(4, phrase.choices.size)
        assertEquals(4, phrase.choices.toSet().size)
        assertEquals("Ich habe heute Zeit.", phrase.choices[phrase.correctIndex])
        assertFalse(phrase.choices.contains("Ještě si nevzpomínám"))
    }

    @Test
    fun oldActiveVocabularyReviewsReceiveRealGermanDistractors() {
        val legacy = ReviewQuestion(
            id = "notebook_active_old_word",
            sourceKind = "vocabulary",
            sourceTitle = "Můj slovníček",
            prompt = "Jak se německy řekne: stůl?",
            choices = listOf("der Tisch", "Jiná odpověď"),
            correctIndex = 0,
            explanation = "Správně: der Tisch.",
            germanSentence = "Der Tisch ist groß.",
            translation = "Stůl je velký."
        )

        val enriched = enrichScheduledReviewQuestion(legacy)

        assertEquals(4, enriched.choices.size)
        assertEquals(4, enriched.choices.toSet().size)
        assertEquals("der Tisch", enriched.choices[enriched.correctIndex])
        assertFalse(enriched.choices.contains("Jiná odpověď"))
    }


    @Test
    fun homeRecommendationPrioritizesDueReviewsFromTheNextDueSkill() {
        val dueArticle = studyPlanTestEntry(
            id = "due-article",
            sourceKind = "articles",
            attempts = 3,
            correct = 1,
            wrongCount = 2
        )
        val dueSentence = studyPlanTestEntry(
            id = "due-sentence",
            sourceKind = "sentences",
            attempts = 2,
            correct = 1,
            wrongCount = 1
        )

        val recommendation = buildHomeStudyRecommendation(
            allEntries = listOf(dueArticle, dueSentence),
            dueEntries = listOf(dueArticle, dueSentence),
            hasPlacementResult = true,
            hasLastActivity = true
        )

        assertEquals(HomeRecommendationType.REVIEW_DUE, recommendation.type)
        assertEquals("articles", recommendation.sourceKind)
        assertEquals(2, recommendation.dueCount)
        assertEquals("Spustit denní trénink", recommendation.actionLabel)
        assertTrue(recommendation.explanation.contains("opakovaně pletly"))
    }

    @Test
    fun homeRecommendationSelectsWeakSkillOnlyFromRecordedAccuracy() {
        val weakerArticles = (1..4).map { index ->
            studyPlanTestEntry(
                id = "article-$index",
                sourceKind = "articles",
                attempts = 1,
                correct = 0,
                wrongCount = 1
            )
        }
        val strongerVocabulary = (1..5).map { index ->
            studyPlanTestEntry(
                id = "vocabulary-$index",
                sourceKind = "vocabulary",
                attempts = 1,
                correct = 1,
                wrongCount = 0
            )
        }

        val summaries = summarizeLearningSkillPerformance(weakerArticles + strongerVocabulary)
        assertEquals(4, summaries.first().attempts)
        assertEquals(0, summaries.first().accuracyPercent)

        val recommendation = buildHomeStudyRecommendation(
            allEntries = weakerArticles + strongerVocabulary,
            dueEntries = emptyList(),
            hasPlacementResult = true,
            hasLastActivity = false
        )

        assertEquals(HomeRecommendationType.FOCUS_SKILL, recommendation.type)
        assertEquals("articles", recommendation.sourceKind)
        assertEquals(0, recommendation.accuracyPercent)
        assertTrue(recommendation.explanation.contains("4 odpovědí"))
    }

    @Test
    fun homeRecommendationNeverInventsAWeakSkillFromLegacyEntriesWithoutAttemptCounts() {
        val legacyEntry = studyPlanTestEntry(
            id = "legacy-article",
            sourceKind = "articles",
            attempts = 0,
            correct = 0,
            wrongCount = 4
        )

        val recommendation = buildHomeStudyRecommendation(
            allEntries = listOf(legacyEntry),
            dueEntries = emptyList(),
            hasPlacementResult = true,
            hasLastActivity = false
        )

        assertEquals(HomeRecommendationType.EXPLORE_LIBRARY, recommendation.type)
        assertEquals(null, recommendation.accuracyPercent)
        assertTrue(summarizeLearningSkillPerformance(listOf(legacyEntry)).isEmpty())
    }

    @Test
    fun homeRecommendationStartsWithPlacementWhenThereIsNoLearningEvidence() {
        val recommendation = buildHomeStudyRecommendation(
            allEntries = emptyList(),
            dueEntries = emptyList(),
            hasPlacementResult = false,
            hasLastActivity = false
        )

        assertEquals(HomeRecommendationType.PLACEMENT, recommendation.type)
        assertEquals("Zjistit svou úroveň", recommendation.actionLabel)
        assertEquals(8, recommendation.estimatedMinutes)
    }

    private fun studyPlanTestEntry(
        id: String,
        sourceKind: String,
        attempts: Int,
        correct: Int,
        wrongCount: Int
    ): ReviewMemoryEntry {
        val question = ReviewQuestion(
            id = id,
            sourceKind = sourceKind,
            sourceTitle = "Test",
            prompt = "Vyber správnou odpověď.",
            choices = listOf("A", "B"),
            correctIndex = 0,
            explanation = "Testovací vysvětlení."
        )
        return ReviewMemoryEntry(
            question = question,
            wrongCount = wrongCount,
            correctStreak = if (correct > 0) 1 else 0,
            intervalDays = if (correct > 0) 1 else 0,
            dueAtMillis = 1L,
            attemptCount = attempts,
            correctCount = correct,
            lastAnsweredAtMillis = 100L
        )
    }

}
