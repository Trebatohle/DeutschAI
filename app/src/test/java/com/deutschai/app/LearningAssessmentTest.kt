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
    fun offlinePlacementAssessmentContainsSixQuestionsAtEveryLevel() {
        val questions = buildPlacementAssessmentQuestions(kotlin.random.Random(2026))
        assertEquals(36, questions.size)
        assertEquals(24, questions.map { it.id }.toSet().size)
        val expectedLevels = setOf("A1", "A2", "B1", "B2", "C1", "C2")
        assertEquals(expectedLevels, questions.map { it.level }.toSet())
        expectedLevels.forEach { level ->
            assertEquals(6, questions.count { it.level == level })
        }
        questions.forEach { question ->
            assertEquals(4, question.choices.size)
            assertEquals(4, question.choices.toSet().size)
            assertTrue(question.correctIndex in question.choices.indices)
            assertTrue(question.prompt.isNotBlank())
            assertTrue(question.skill.isNotBlank())
        }
    }

    @Test
    fun placementAssessmentCalculatesHighestConsecutivePassedLevel() {
        val questions = buildPlacementAssessmentQuestions(kotlin.random.Random(7))
        val answers = questions.associate { question ->
            question.id to if (question.level in setOf("A1", "A2")) {
                question.correctIndex
            } else {
                (question.correctIndex + 1) % question.choices.size
            }
        }
        val report = evaluatePlacementAssessment(questions, answers, nowMillis = 1234L)
        assertEquals("A2", report.estimatedLevel)
        assertEquals(12, report.correctAnswers)
        assertEquals(36, report.totalQuestions)
        assertEquals(1234L, report.assessedAtMillis)
        assertEquals(6, report.levelResults.size)
        assertEquals(2, report.skillResults.size)
    }
}
