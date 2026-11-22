
package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TenseSubordinatePracticeTest {
    @Test
    fun everyQuestionHasOneValidAnswerAndAnExplicitLevelMatchingItsTitle() {
        val questions = buildTenseSubordinateQuestions()
        assertEquals(40, questions.size)
        assertEquals(40, questions.map { it.id }.toSet().size)

        questions.forEach { question ->
            assertTrue(question.cefrLevel in setOf("A2", "B1"))
            assertTrue(question.sourceTitle.startsWith(question.cefrLevel + " ·"))
            assertEquals(4, question.choices.size)
            assertEquals(4, question.choices.distinct().size)
            assertTrue(question.correctIndex in question.choices.indices)
            assertTrue(question.prompt.isNotBlank())
            assertTrue(question.explanation.isNotBlank())
            assertTrue(question.germanSentence.isNotBlank())
            assertTrue(question.translation.isNotBlank())
            assertTrue(question.focusTopicId.startsWith("grammar:${question.cefrLevel.lowercase()}:"))
        }
    }

    @Test
    fun a2ContainsOnlyTheIntendedFoundationalPastAndSubordinateClauseTopics() {
        val a2 = buildTenseSubordinateQuestions("A2")
        val past = a2.filter { it.focusTopicId == "grammar:a2:past" }
        val clauses = a2.filter { it.focusTopicId == "grammar:a2:subordinate" }

        assertEquals(12, past.size)
        assertEquals(8, clauses.size)
        assertTrue(past.any { it.focusTerm == "Perfekt · haben/sein" })
        assertTrue(past.any { it.focusTerm == "Perfekt · Partizip II" })
        assertTrue(past.any { it.focusTerm == "Präteritum · sein/haben" })
        assertTrue(past.any { it.focusTerm == "Präteritum · modální slovesa" })
        assertTrue(clauses.map { it.focusTerm }.toSet().containsAll(setOf("weil", "dass", "als", "wenn", "damit")))
        assertFalse(clauses.any { it.focusTerm in setOf("obwohl", "nachdem", "seitdem", "sodass", "da") })
        assertFalse(past.any { it.focusTerm == "Plusquamperfekt" })
        assertTrue(a2.all { it.cefrLevel == "A2" })
    }

    @Test
    fun b1ContainsPlusquamperfektAndMoreDemandingSubordinateClauses() {
        val b1 = buildTenseSubordinateQuestions("B1")
        val past = b1.filter { it.focusTopicId == "grammar:b1:past" }
        val clauses = b1.filter { it.focusTopicId == "grammar:b1:subordinate" }

        assertEquals(10, past.size)
        assertEquals(10, clauses.size)
        assertTrue(past.any { it.focusTerm == "Plusquamperfekt" })
        assertTrue(past.any { it.focusTerm == "Präteritum · vyprávění" })
        assertTrue(past.any { it.focusTerm == "Perfekt · mluvené vyprávění" })
        assertTrue(clauses.map { it.focusTerm }.toSet().containsAll(setOf("obwohl", "da", "bevor", "nachdem", "seitdem", "sodass")))
        assertTrue(b1.all { it.cefrLevel == "B1" })
    }

    @Test
    fun unsupportedLevelsAreRejectedRatherThanSilentlyMislabelled() {
        listOf("A1", "B2", "C1", "").forEach { level ->
            val rejected = runCatching { buildTenseSubordinateQuestions(level) }.isFailure
            assertTrue("Level $level must not receive these questions.", rejected)
        }
    }

    @Test
    fun dailyPoolIncludesEveryLevelSpecificGrammarItemWithoutChangingItsLevel() {
        val grammar = buildDailyTrainingPool().filter { it.id.startsWith("tense-subordinate_") }

        assertEquals(40, grammar.size)
        assertEquals(20, grammar.count { it.cefrLevel == "A2" })
        assertEquals(20, grammar.count { it.cefrLevel == "B1" })
        assertTrue(grammar.all {
            it.sourceTitle.startsWith(it.cefrLevel + " ·") &&
                it.focusTopicId.startsWith("grammar:${it.cefrLevel.lowercase()}:")
        })
    }
}
