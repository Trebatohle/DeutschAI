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
