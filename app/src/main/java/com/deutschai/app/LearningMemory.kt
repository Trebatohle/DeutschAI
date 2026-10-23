package com.deutschai.app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.time.LocalDate

internal data class ReviewQuestion(
    val id: String,
    val sourceKind: String,
    val sourceTitle: String,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val germanSentence: String = "",
    val translation: String = ""
) {
    init {
        require(id.isNotBlank()) { "Review question ID must not be blank." }
        require(choices.size >= 2) { "A review question must have at least two choices." }
        require(correctIndex in choices.indices) { "Review question must have one valid correct answer." }
    }
}

internal data class ReviewMemoryEntry(
    val question: ReviewQuestion,
    val wrongCount: Int,
    val correctStreak: Int,
    val intervalDays: Int,
    val dueAtMillis: Long
)

/** Compact, local-only spaced repetition. The queue never leaves the device. */
internal object LearningReviewStore {
    private const val PREFS = "deutschai_long_term_memory"
    private const val ITEMS = "items_v1"
    private const val MAX_ITEMS = 300
    private const val DAY_MILLIS = 24L * 60L * 60L * 1000L

    fun all(context: Context): List<ReviewMemoryEntry> =
        readEntries(context).values.sortedBy { it.dueAtMillis }

    fun due(context: Context, nowMillis: Long = System.currentTimeMillis()): List<ReviewMemoryEntry> =
        all(context).filter { it.dueAtMillis <= nowMillis }

    fun pendingCount(context: Context): Int = readEntries(context).size

    fun recordAnswer(
        context: Context,
        question: ReviewQuestion,
        wasCorrect: Boolean,
        isMistakeReview: Boolean = false,
        nowMillis: Long = System.currentTimeMillis()
    ) {
        val entries = readEntries(context)
        val previous = entries[question.id]

        if (wasCorrect) {
            if (previous == null) return
            val nextStreak = previous.correctStreak + 1
            val schedule = listOf(1, 3, 7, 14, 30)
            val nextInterval = schedule[(nextStreak - 1).coerceIn(schedule.indices)]
            entries[question.id] = previous.copy(
                question = question,
                correctStreak = nextStreak,
                intervalDays = nextInterval,
                dueAtMillis = nowMillis + nextInterval * DAY_MILLIS
            )
        } else {
            entries[question.id] = ReviewMemoryEntry(
                question = question,
                wrongCount = (previous?.wrongCount ?: 0) + 1,
                correctStreak = 0,
                intervalDays = if (isMistakeReview) 1 else 0,
                dueAtMillis = if (isMistakeReview) nowMillis + DAY_MILLIS else nowMillis
            )
        }

        val trimmed = entries.values
            .sortedWith(compareBy<ReviewMemoryEntry> { it.dueAtMillis }.thenByDescending { it.wrongCount })
            .take(MAX_ITEMS)
        writeEntries(context, trimmed)
    }

    private fun readEntries(context: Context): MutableMap<String, ReviewMemoryEntry> {
        val raw = context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(ITEMS, "[]") ?: "[]"
        return runCatching {
            val array = JSONArray(raw)
            buildMap {
                for (index in 0 until array.length()) {
                    val obj = array.optJSONObject(index) ?: continue
                    val question = obj.toReviewQuestion() ?: continue
                    val dueAt = obj.optLong("dueAtMillis", 0L)
                    put(
                        question.id,
                        ReviewMemoryEntry(
                            question = question,
                            wrongCount = obj.optInt("wrongCount", 1).coerceAtLeast(1),
                            correctStreak = obj.optInt("correctStreak", 0).coerceAtLeast(0),
                            intervalDays = obj.optInt("intervalDays", 0).coerceAtLeast(0),
                            dueAtMillis = dueAt
                        )
                    )
                }
            }.toMutableMap()
        }.getOrElse { mutableMapOf() }
    }

    private fun writeEntries(context: Context, entries: List<ReviewMemoryEntry>) {
        val array = JSONArray()
        entries.forEach { entry ->
            val q = entry.question
            val choices = JSONArray().apply { q.choices.forEach(::put) }
            array.put(
                JSONObject()
                    .put("id", q.id)
                    .put("sourceKind", q.sourceKind)
                    .put("sourceTitle", q.sourceTitle)
                    .put("prompt", q.prompt)
                    .put("choices", choices)
                    .put("correctIndex", q.correctIndex)
                    .put("explanation", q.explanation)
                    .put("germanSentence", q.germanSentence)
                    .put("translation", q.translation)
                    .put("wrongCount", entry.wrongCount)
                    .put("correctStreak", entry.correctStreak)
                    .put("intervalDays", entry.intervalDays)
                    .put("dueAtMillis", entry.dueAtMillis)
            )
        }
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(ITEMS, array.toString())
            .apply()
    }

    private fun JSONObject.toReviewQuestion(): ReviewQuestion? {
        val id = optString("id", "")
        val choicesJson = optJSONArray("choices") ?: return null
        val choices = (0 until choicesJson.length()).map { choicesJson.optString(it, "") }
        val correctIndex = optInt("correctIndex", -1)
        if (id.isBlank() || choices.size < 2 || correctIndex !in choices.indices) return null
        return ReviewQuestion(
            id = id,
            sourceKind = optString("sourceKind", "other"),
            sourceTitle = optString("sourceTitle", "Opakování"),
            prompt = optString("prompt", ""),
            choices = choices,
            correctIndex = correctIndex,
            explanation = optString("explanation", ""),
            germanSentence = optString("germanSentence", ""),
            translation = optString("translation", "")
        )
    }
}

internal fun stableReviewId(vararg parts: String): String {
    val input = parts.joinToString(separator = "\u0000")
    val digest = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
    val suffix = digest.take(12).joinToString("") { byte -> "%02x".format(byte) }
    return "${parts.firstOrNull()?.replace(Regex("[^A-Za-z0-9_-]"), "_") ?: "question"}_$suffix"
}

internal data class StudyHabitSnapshot(
    val currentStreakDays: Int,
    val studyDaysInLastWeek: Int,
    val bestStreakDays: Int
)

internal object StudyHabitStore {
    private const val PREFS = "deutschai_study_habits"

    fun load(context: Context, today: LocalDate = LocalDate.now()): StudyHabitSnapshot {
        val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val lastDate = runCatching { prefs.getString("last_study_date", null)?.let(LocalDate::parse) }.getOrNull()
        val savedStreak = prefs.getInt("current_streak", 0)
        val currentStreak = if (lastDate == today || lastDate == today.minusDays(1)) savedStreak else 0
        val weeklyDays = (0L..6L).count { offset ->
            prefs.getBoolean("study_day_${today.minusDays(offset)}", false)
        }
        return StudyHabitSnapshot(
            currentStreakDays = currentStreak,
            studyDaysInLastWeek = weeklyDays,
            bestStreakDays = prefs.getInt("best_streak", 0)
        )
    }

    /** Counts at most one study day per local calendar day, no matter how many lessons were done. */
    fun recordSession(context: Context, today: LocalDate = LocalDate.now()): StudyHabitSnapshot {
        val appContext = context.applicationContext
        val prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val lastDate = runCatching { prefs.getString("last_study_date", null)?.let(LocalDate::parse) }.getOrNull()
        if (lastDate != today) {
            val previousStreak = prefs.getInt("current_streak", 0)
            val nextStreak = if (lastDate == today.minusDays(1)) previousStreak + 1 else 1
            val edit = prefs.edit()
                .putString("last_study_date", today.toString())
                .putInt("current_streak", nextStreak)
                .putInt("best_streak", maxOf(prefs.getInt("best_streak", 0), nextStreak))
                .putBoolean("study_day_$today", true)
            // Keep only recent day markers to prevent unbounded preference growth.
            (15L..90L).forEach { offset -> edit.remove("study_day_${today.minusDays(offset)}") }
            edit.apply()
        }
        return load(appContext, today)
    }
}
