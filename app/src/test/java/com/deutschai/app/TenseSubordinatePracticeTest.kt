package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TenseSubordinatePracticeTest {
    @Test
    fun everyQuestionHasOneValidAnswerAndAnExplicitLevelMatchingItsTitle() {
        val questions = buildTenseSubordinateQuestions()
        assertEquals(60, questions.size)
        assertEquals(60, questions.map { it.id }.toSet().size)
        assertEquals(20, questions.count { it.cefrLevel == "A2" })
        assertEquals(20, questions.count { it.cefrLevel == "B1" })
        assertEquals(20, questions.count { it.cefrLevel == "B2" })

        questions.forEach { question ->
            assertTrue(question.cefrLevel in setOf("A2", "B1", "B2"))
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

        assertEquals(20, a2.size)
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

        assertEquals(20, b1.size)
        assertEquals(10, past.size)
        assertEquals(10, clauses.size)
        assertTrue(past.any { it.focusTerm == "Plusquamperfekt" })
        assertTrue(past.any { it.focusTerm == "Präteritum · vyprávění" })
        assertTrue(past.any { it.focusTerm == "Perfekt · mluvené vyprávění" })
        assertTrue(clauses.map { it.focusTerm }.toSet().containsAll(setOf("obwohl", "da", "bevor", "nachdem", "seitdem", "sodass")))
        assertTrue(b1.all { it.cefrLevel == "B1" })
    }

    @Test
    fun b2ContainsReportedSpeechPassiveAlternativesAndFormalSentencePatterns() {
        val b2 = buildTenseSubordinateQuestions("B2")
        val reported = b2.filter { it.focusTopicId == "grammar:b2:reported-speech" }
        val passive = b2.filter { it.focusTopicId == "grammar:b2:passive-alternatives" }
        val complex = b2.filter { it.focusTopicId == "grammar:b2:complex-sentences" }

        assertEquals(20, b2.size)
        assertEquals(8, reported.size)
        assertEquals(6, passive.size)
        assertEquals(6, complex.size)
        assertTrue(reported.map { it.focusTerm }.toSet().containsAll(setOf(
            "Konjunktiv I · sein",
            "Konjunktiv I · haben",
            "Konjunktiv I · minulost se sein",
            "Konjunktiv I · modální slovesa",
            "Konjunktiv I · werden",
            "Nepřímá řeč · zájmena a místní údaje",
            "Konjunktiv II · náhradní tvar",
            "Konjunktiv I · pasivum minulosti"
        )))
        assertTrue(passive.map { it.focusTerm }.toSet().containsAll(setOf(
            "sich lassen + infinitiv",
            "sein + zu + infinitiv",
            "přípona -bar"
        )))
        assertTrue(complex.map { it.focusTerm }.toSet().containsAll(setOf(
            "wenngleich · ústupek",
            "sofern · podmínka",
            "zumal · důvod",
            "wohingegen · protiklad",
            "je … umso · srovnání",
            "nicht nur … sondern auch"
        )))
        assertTrue(b2.all { it.cefrLevel == "B2" })
    }

    @Test
    fun b2SpecificTopicsNeverLeakIntoA2OrB1() {
        val lowerLevels = buildTenseSubordinateQuestions("A2") + buildTenseSubordinateQuestions("B1")
        val b2Terms = setOf(
            "Konjunktiv I · sein",
            "Konjunktiv I · haben",
            "Konjunktiv I · minulost se sein",
            "Konjunktiv I · modální slovesa",
            "Konjunktiv I · werden",
            "Konjunktiv II · náhradní tvar",
            "Konjunktiv I · pasivum minulosti",
            "sich lassen + infinitiv",
            "sein + zu + infinitiv",
            "přípona -bar",
            "wenngleich · ústupek",
            "sofern · podmínka",
            "zumal · důvod",
            "wohingegen · protiklad",
            "je … umso · srovnání",
            "nicht nur … sondern auch"
        )

        assertTrue(lowerLevels.none { it.focusTopicId.startsWith("grammar:b2:") })
        assertTrue(lowerLevels.none { it.id.contains("b2-") })
        assertTrue(lowerLevels.none { it.focusTerm in b2Terms })
    }

    @Test
    fun unsupportedLevelsAreRejectedRatherThanSilentlyMislabelled() {
        listOf("A1", "C1", "C2", "", "b2").forEach { level ->
            val rejected = runCatching { buildTenseSubordinateQuestions(level) }.isFailure
            assertTrue("Level $level must not receive these questions.", rejected)
        }
    }

    @Test
    fun dailyPoolIncludesEveryLevelSpecificGrammarItemWithoutChangingItsLevel() {
        val grammar = buildDailyTrainingPool().filter { it.id.startsWith("tense-subordinate_") }

        assertEquals(60, grammar.size)
        assertEquals(20, grammar.count { it.cefrLevel == "A2" })
        assertEquals(20, grammar.count { it.cefrLevel == "B1" })
        assertEquals(20, grammar.count { it.cefrLevel == "B2" })
        assertTrue(grammar.all {
            it.sourceTitle.startsWith(it.cefrLevel + " ·") &&
                it.focusTopicId.startsWith("grammar:${it.cefrLevel.lowercase()}:")
        })
    }
}
