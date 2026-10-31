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
}
