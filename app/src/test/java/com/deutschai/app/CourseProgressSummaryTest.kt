package com.deutschai.app

import org.junit.Assert.assertEquals
import org.junit.Test

class CourseProgressSummaryTest {
    @Test
    fun summaryCountsCompletedStartedAndNotStartedCoursesForSelectedLevel() {
        val courses = listOf(
            course("a1-1", "A1", answered = 25, result = CourseResult(25, 25)),
            course("a1-2", "A1", answered = 25, result = CourseResult(20, 25)),
            course("a1-3", "A1", answered = 8),
            course("a1-4", "A1"),
            course("a2-1", "A2", answered = 25, result = CourseResult(25, 25))
        )

        val summary = buildSentenceCourseLevelSummary(courses, "A1")

        assertEquals(4, summary.totalCourses)
        assertEquals(2, summary.completedCourses)
        assertEquals(1, summary.startedCourses)
        assertEquals(1, summary.notStartedCourses)
        assertEquals(50, summary.completionPercent)
    }

    @Test
    fun emptyLevelHasZeroProgress() {
        val summary = buildSentenceCourseLevelSummary(emptyList(), "B2")

        assertEquals(0, summary.totalCourses)
        assertEquals(0, summary.completedCourses)
        assertEquals(0, summary.startedCourses)
        assertEquals(0, summary.notStartedCourses)
        assertEquals(0, summary.completionPercent)
    }

    private fun course(
        id: String,
        level: String,
        answered: Int = 0,
        result: CourseResult? = null
    ) = SentenceCourseLearningProgress(
        courseId = id,
        title = id,
        subtitle = "",
        level = level,
        totalQuestions = 25,
        answeredQuestions = answered,
        latestResult = result
    )
}
