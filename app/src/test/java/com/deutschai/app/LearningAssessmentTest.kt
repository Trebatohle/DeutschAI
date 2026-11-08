package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LearningAssessmentTest {
    @Test
    fun everyReadingArticleCreatesFiveUniqueFourChoiceComprehensionQuestions() {
        readingArticles.forEach { article ->
            val questions = buildReadingComprehensionQuestions(article)
            assertEquals("${article.id} should have five comprehension questions", 5, questions.size)
            assertEquals(5, questions.map { it.id }.toSet().size)
            questions.forEach { question ->
                assertEquals(4, question.choices.size)
                assertEquals(4, question.choices.map { it.lowercase() }.toSet().size)
                assertTrue(question.correctIndex in question.choices.indices)
                assertTrue(
                    "The correct statement for ${article.id} must occur in the source story",
                    article.body.contains(question.choices[question.correctIndex], ignoreCase = true)
                )
                assertTrue("${article.id} question must provide a reading context", question.passage.isNotBlank())
                assertTrue(
                    "${article.id} question's passage must contain the correct statement",
                    question.passage.contains(question.choices[question.correctIndex], ignoreCase = true)
                )
                question.choices.filterIndexed { index, _ -> index != question.correctIndex }.forEach { distractor ->
                    assertTrue(
                        "A distractor for ${article.id} must not occur in the source story",
                        !article.body.contains(distractor, ignoreCase = true)
                    )
                }
                assertTrue(question.facet.isNotBlank())
                assertTrue(question.explanation.isNotBlank())
            }
        }
    }

    @Test
    fun offlinePlacementAssessmentContainsFourShortQuestionsAtEveryLevel() {
        val questions = buildPlacementAssessmentQuestions(kotlin.random.Random(2026))
        assertEquals(24, questions.size)
        assertEquals(24, questions.map { it.id }.toSet().size)
        val expectedLevels = listOf("A1", "A2", "B1", "B2", "C1", "C2")
        assertEquals(expectedLevels.flatMap { level -> List(4) { level } }, questions.map { it.level })
        expectedLevels.forEach { level ->
            assertEquals(4, questions.count { it.level == level })
        }
        questions.forEach { question ->
            assertEquals(4, question.choices.size)
            assertEquals(4, question.choices.toSet().size)
            assertTrue(question.correctIndex in question.choices.indices)
            assertTrue(question.prompt.isNotBlank())
            assertTrue("Placement prompts should be concise", question.prompt.length <= 90)
            assertTrue(question.skill in setOf("Gramatika", "Slovní zásoba a fráze"))
            assertTrue("The entry test must not show long reading passages", question.passage.isBlank())
        }
    }

    @Test
    fun placementAssessmentCalculatesHighestConsecutivePassedLevel() {
        val questions = buildPlacementAssessmentQuestions(kotlin.random.Random(7))
        val a2ItemsNeededToPass = questions.filter { it.level == "A2" }.take(3).map { it.id }.toSet()
        val answers = questions.associate { question ->
            val shouldBeCorrect = question.level == "A1" ||
                (question.level == "A2" && question.id in a2ItemsNeededToPass)
            question.id to if (shouldBeCorrect) {
                question.correctIndex
            } else {
                (question.correctIndex + 1) % question.choices.size
            }
        }
        val report = evaluatePlacementAssessment(questions, answers, nowMillis = 1234L)
        assertEquals("A2", report.estimatedLevel)
        assertEquals(7, report.correctAnswers)
        assertEquals(24, report.totalQuestions)
        assertEquals(1234L, report.assessedAtMillis)
        assertEquals(6, report.levelResults.size)
        assertEquals(2, report.skillResults.size)
    }

    @Test
    fun cefrKnowledgeMapSummarizesRealPracticeSeparatelyForEveryLevel() {
        val entries = listOf(
            cefrTestEntry("a1-1", "A1", attempts = 3, correct = 2),
            cefrTestEntry("a1-2", "A1", attempts = 2, correct = 2),
            cefrTestEntry("b1-1", "B1", attempts = 4, correct = 1)
        )

        val map = buildCefrKnowledgeMap(entries)

        assertEquals(listOf("A1", "A2", "B1", "B2", "C1", "C2"), map.map { it.level })
        val a1 = map.single { it.level == "A1" }
        assertEquals(80, a1.accuracyPercent)
        assertEquals(5, a1.answeredQuestions)
        assertEquals("Procvičování", a1.sourceLabel)
        assertEquals("Rozvíjíš", a1.statusLabel)

        val a2 = map.single { it.level == "A2" }
        assertEquals(null, a2.accuracyPercent)
        assertEquals("Zatím neověřeno", a2.statusLabel)

        val b1 = map.single { it.level == "B1" }
        assertEquals(25, b1.accuracyPercent)
        assertEquals("První výsledky", b1.statusLabel)
    }

    @Test
    fun cefrKnowledgeMapFallsBackToThePlacementReportForLegacyUnlabelledEntries() {
        val placement = PlacementAssessmentReport(
            estimatedLevel = "B1",
            assessedAtMillis = 1234L,
            totalQuestions = 24,
            correctAnswers = 15,
            levelResults = mapOf(
                "A1" to PlacementSkillResult(4, 4),
                "A2" to PlacementSkillResult(3, 4),
                "B1" to PlacementSkillResult(2, 4)
            ),
            skillResults = emptyMap()
        )
        val legacy = cefrTestEntry("legacy", "", attempts = 1, correct = 1)

        val map = buildCefrKnowledgeMap(listOf(legacy), placement)

        assertEquals(100, map.single { it.level == "A1" }.accuracyPercent)
        assertEquals(75, map.single { it.level == "A2" }.accuracyPercent)
        assertEquals("Vstupní test", map.single { it.level == "A2" }.sourceLabel)
        assertEquals(null, map.single { it.level == "B2" }.accuracyPercent)
    }

    @Test
    fun legacyPracticeWithoutLevelIsNeverAssignedToAnA1ToC2BandByGuesswork() {
        val legacy = cefrTestEntry("old-record", "", attempts = 20, correct = 18)

        val map = buildCefrKnowledgeMap(listOf(legacy))

        assertTrue(map.all { it.accuracyPercent == null })
        assertTrue(map.all { it.answeredQuestions == 0 })
    }

    private fun cefrTestEntry(
        id: String,
        level: String,
        attempts: Int,
        correct: Int
    ): ReviewMemoryEntry {
        val question = ReviewQuestion(
            id = id,
            sourceKind = "sentences",
            sourceTitle = "Testovací kurz",
            prompt = "Vyber správnou odpověď.",
            choices = listOf("A", "B"),
            correctIndex = 0,
            explanation = "Testovací vysvětlení.",
            cefrLevel = level
        )
        return ReviewMemoryEntry(
            question = question,
            wrongCount = attempts - correct,
            correctStreak = 0,
            intervalDays = 0,
            dueAtMillis = 0L,
            attemptCount = attempts,
            correctCount = correct,
            lastAnsweredAtMillis = 1L
        )
    }


    @Test
    fun courseMistakeCorrectionIsOfferedOnlyAsAnOptionalStepWhenThereAreErrors() {
        assertTrue(!shouldOfferOptionalMistakeReview(0))
        assertTrue(shouldOfferOptionalMistakeReview(1))
        assertTrue(shouldOfferOptionalMistakeReview(25))
    }

}
