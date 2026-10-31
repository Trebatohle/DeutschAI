package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal data class CourseResult(val score: Int, val total: Int) {
    val isPerfect: Boolean get() = total > 0 && score == total
}

internal object CourseResultStore {
    private const val PREFS = "deutschai_course_results"

    fun save(context: Context, key: String, score: Int, total: Int) {
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putInt("${key}_score", score)
            .putInt("${key}_total", total)
            .putLong("${key}_time", System.currentTimeMillis())
            .apply()
    }

    fun load(context: Context, key: String): CourseResult? {
        val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (!prefs.contains("${key}_time")) return null
        val total = prefs.getInt("${key}_total", 0)
        if (total <= 0) return null
        return CourseResult(prefs.getInt("${key}_score", 0), total)
    }
}

internal data class LearningProgressSnapshot(
    val successfullyCompletedActivities: Int,
    val successfulSessions: Int,
    val practicedActivities: Int
)

/**
 * Counts only quiz attempts with a perfect score. Activity totals count each unique
 * course/category/mode once, while successfulSessions counts every 100% attempt.
 */
internal object LearningProgressStore {
    private const val PREFS = "deutschai_progress"
    private const val SUCCESSFUL_ACTIVITY_IDS = "successful_activity_ids"
    private const val SUCCESSFUL_SESSIONS = "successful_sessions"
    private const val MIGRATION_DONE = "perfect_activity_results_migrated_v1"

    fun load(context: Context): LearningProgressSnapshot {
        val appContext = context.applicationContext
        val prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val successfulIds = (prefs.getStringSet(SUCCESSFUL_ACTIVITY_IDS, emptySet()) ?: emptySet())
            .toMutableSet()

        // Migrate known perfect latest results from the existing per-course result store.
        // The old total-session counter included failed exercises and conversations,
        // so it cannot truthfully be reused for the new perfect-session counter.
        if (!prefs.getBoolean(MIGRATION_DONE, false)) {
            val legacyPerfectResults = resultKeys().filter { key ->
                CourseResultStore.load(appContext, key)?.isPerfect == true
            }
            successfulIds.addAll(legacyPerfectResults)
            val migration = prefs.edit()
                .putStringSet(SUCCESSFUL_ACTIVITY_IDS, successfulIds)
                .putBoolean(MIGRATION_DONE, true)
            // The old generic session counter included failed attempts and unscored
            // activities. Seed the new counter only from saved results known to be perfect.
            if (!prefs.contains(SUCCESSFUL_SESSIONS)) {
                migration.putInt(SUCCESSFUL_SESSIONS, legacyPerfectResults.size)
            }
            migration.apply()
        }

        return LearningProgressSnapshot(
            successfullyCompletedActivities = successfulIds.size,
            successfulSessions = prefs.getInt(SUCCESSFUL_SESSIONS, 0),
            practicedActivities = resultKeys().count { CourseResultStore.load(appContext, it) != null }
        )
    }

    fun recordPerfectSession(context: Context, activityKey: String): LearningProgressSnapshot {
        require(activityKey.isNotBlank()) { "Activity key must not be blank." }
        val appContext = context.applicationContext
        // Ensure legacy perfect results are migrated before adding this result.
        load(appContext)
        val prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val successfulIds = (prefs.getStringSet(SUCCESSFUL_ACTIVITY_IDS, emptySet()) ?: emptySet())
            .toMutableSet()
        successfulIds.add(activityKey)
        val sessions = prefs.getInt(SUCCESSFUL_SESSIONS, 0) + 1
        prefs.edit()
            .putStringSet(SUCCESSFUL_ACTIVITY_IDS, successfulIds)
            .putInt(SUCCESSFUL_SESSIONS, sessions)
            .apply()

        return LearningProgressSnapshot(
            successfullyCompletedActivities = successfulIds.size,
            successfulSessions = sessions,
            practicedActivities = resultKeys().count { CourseResultStore.load(appContext, it) != null }
        )
    }

    private fun resultKeys(): List<String> =
        listOf("daily_training", "vocabulary_learning") +
            sentenceCourseCatalog.map { "sentence_${it.id}" } +
            articleCategories.map { "articles_${it.id}" } +
            (verbPracticeTopics + advancedVerbPracticeTopics).map { "verbs_${it.id}" } +
            wordToolGroups.flatMap { group ->
                listOf("words", "sentences", "challenge").map { mode ->
                    "wordtools_${group.id}_$mode"
                }
            }
}

@Composable
internal fun LatestResultLine(result: CourseResult?) {
    if (result != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text("Poslední výsledek:", color = Color(0xFF66736D), fontSize = 12.sp)
            Text(
                if (result.isPerfect) "${result.score}/${result.total} · Úspěch ✓"
                else "${result.score}/${result.total}",
                color = if (result.isPerfect) Color(0xFF21864D) else Color(0xFF163D35),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
