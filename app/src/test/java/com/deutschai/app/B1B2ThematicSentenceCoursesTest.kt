package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class B1B2ThematicSentenceCoursesTest {
    @Test
    fun addsExactlyTwentyNewCoursesAndFiveHundredQuestionsPerAdvancedLevel() {
        val b1 = b1B2ThematicSentenceCourseCatalog.filter { it.level == "B1" }
        val b2 = b1B2ThematicSentenceCourseCatalog.filter { it.level == "B2" }

        assertEquals(20, b1.size)
        assertEquals(20, b2.size)
        assertEquals(500, b1.sumOf { it.items.size })
        assertEquals(500, b2.sumOf { it.items.size })
    }

    @Test
    fun everyNewCourseContainsTwentyFiveUniqueUsableSentenceQuestions() {
        assertEquals(
            b1B2ThematicSentenceCourseCatalog.size,
            b1B2ThematicSentenceCourseCatalog.map { it.id }.toSet().size
        )

        b1B2ThematicSentenceCourseCatalog.forEach { course ->
            assertTrue(course.title.isNotBlank())
            assertTrue(course.subtitle.isNotBlank())
            assertEquals(25, course.items.size)
            assertEquals(25, course.items.map { it.german }.toSet().size)

            course.items.forEach { item ->
                assertTrue(item.prompt.startsWith("Přelož do němčiny:"))
                assertTrue(item.german.isNotBlank())
                assertTrue(item.czech.isNotBlank())
                assertFalse(item.german.contains("§"))
                assertFalse(item.czech.contains("§"))
            }
        }
    }

    @Test
    fun newAdvancedCoursesAreRegisteredInTheMainCourseCatalog() {
        b1B2ThematicSentenceCourseCatalog.forEach { course ->
            assertTrue(sentenceCourseCatalog.any { it.id == course.id })
            assertEquals(25, sentenceCourseQuestions(course.id).size)
        }

        assertEquals(30, sentenceCourseCatalog.count { it.level == "B1" })
        assertEquals(30, sentenceCourseCatalog.count { it.level == "B2" })
    }
}
