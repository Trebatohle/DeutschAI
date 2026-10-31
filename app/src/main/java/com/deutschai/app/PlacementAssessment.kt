package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject
import kotlin.random.Random

internal data class PlacementAssessmentQuestion(
    val id: String,
    val level: String,
    val skill: String,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val germanSentence: String = "",
    val translation: String = ""
)

internal data class PlacementSkillResult(val correct: Int, val total: Int) {
    val accuracyPercent: Int get() = if (total == 0) 0 else correct * 100 / total
}

internal data class PlacementAssessmentReport(
    val estimatedLevel: String,
    val assessedAtMillis: Long,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val levelResults: Map<String, PlacementSkillResult>,
    val skillResults: Map<String, PlacementSkillResult>
)

internal fun buildPlacementAssessmentQuestions(
    random: Random = Random.Default
): List<PlacementAssessmentQuestion> {
    val levels = listOf("A1", "A2", "B1", "B2", "C1", "C2")
    val result = mutableListOf<PlacementAssessmentQuestion>()

    levels.forEach { level ->
        val courses = sentenceCourseCatalog.filter { it.level == level }
        val sentenceCount = if (level in setOf("A1", "A2", "B1", "B2")) 4 else 0
        if (sentenceCount > 0) {
            require(courses.isNotEmpty()) { "No sentence courses are available for $level." }
            val sentenceQuestions = courses.flatMap { course ->
                course.items.mapIndexed { index, item ->
                    val options = listOf(
                        item,
                        course.items[(index + 5) % course.items.size],
                        course.items[(index + 11) % course.items.size],
                        course.items[(index + 17) % course.items.size]
                    ).distinctBy { it.german }.shuffled(random)
                    PlacementAssessmentQuestion(
                        id = stableReviewId("placement", "sentences", level, course.id, index.toString()),
                        level = level,
                        skill = "Věty a gramatika",
                        prompt = item.prompt,
                        choices = options.map { it.german },
                        correctIndex = options.indexOfFirst { it.german == item.german },
                        explanation = "Správná věta znamená: ${item.czech}",
                        germanSentence = item.german,
                        translation = item.czech
                    )
                }
            }
            result += sentenceQuestions.shuffled(random).take(sentenceCount)
        }

        val articles = readingArticles.filter { it.level == level }
        require(articles.isNotEmpty()) { "No reading articles are available for $level." }
        val comprehensionCount = if (sentenceCount > 0) 2 else 6
        val readingQuestions = articles.shuffled(random)
            .flatMap { buildReadingComprehensionQuestions(it).shuffled(random) }
            .distinctBy { it.id }
            .take(comprehensionCount)
        require(readingQuestions.size == comprehensionCount) {
            "Not enough reading comprehension questions are available for $level."
        }

        result += readingQuestions.map { question ->
            PlacementAssessmentQuestion(
                id = stableReviewId("placement", "reading", level, question.id),
                level = level,
                skill = "Čtení s porozuměním",
                prompt = question.prompt,
                choices = question.choices,
                correctIndex = question.correctIndex,
                explanation = question.explanation
            )
        }
    }

    check(result.size == 36) { "Placement test must contain exactly 36 questions (6 per level)." }
    check(result.map { it.id }.toSet().size == result.size) { "Placement test question IDs must be unique." }
    return result.shuffled(random)
}

internal fun evaluatePlacementAssessment(
    questions: List<PlacementAssessmentQuestion>,
    answers: Map<String, Int>,
    nowMillis: Long = System.currentTimeMillis()
): PlacementAssessmentReport {
    require(questions.isNotEmpty())
    require(questions.all { question ->
        answers[question.id]?.let { answerIndex -> answerIndex in question.choices.indices } == true
    }) {
        "Every placement question must have one selected answer."
    }
    val levels = listOf("A1", "A2", "B1", "B2", "C1", "C2")
    val levelResults = levels.associateWith { level ->
        val rows = questions.filter { it.level == level }
        PlacementSkillResult(rows.count { answers[it.id] == it.correctIndex }, rows.size)
    }
    val skillResults = questions.groupBy { it.skill }.mapValues { (_, rows) ->
        PlacementSkillResult(rows.count { answers[it.id] == it.correctIndex }, rows.size)
    }

    // A level is considered passed when the student gets at least 4 of its 6 questions right.
    // Report the highest consecutive CEFR band passed, not an isolated higher score.
    var estimated = "A1"
    for (level in levels) {
        if (levelResults.getValue(level).accuracyPercent >= 66) {
            estimated = level
        } else {
            break
        }
    }

    return PlacementAssessmentReport(
        estimatedLevel = estimated,
        assessedAtMillis = nowMillis,
        totalQuestions = questions.size,
        correctAnswers = questions.count { answers[it.id] == it.correctIndex },
        levelResults = levelResults,
        skillResults = skillResults
    )
}

internal object PlacementAssessmentStore {
    private const val PREFS = "deutschai_placement_assessment"
    private const val RESULT = "latest_result_v1"

    fun save(context: Context, report: PlacementAssessmentReport) {
        val levels = JSONObject().apply {
            report.levelResults.forEach { (key, value) ->
                put(key, JSONObject().put("correct", value.correct).put("total", value.total))
            }
        }
        val skills = JSONObject().apply {
            report.skillResults.forEach { (key, value) ->
                put(key, JSONObject().put("correct", value.correct).put("total", value.total))
            }
        }
        val json = JSONObject()
            .put("estimatedLevel", report.estimatedLevel)
            .put("assessedAtMillis", report.assessedAtMillis)
            .put("totalQuestions", report.totalQuestions)
            .put("correctAnswers", report.correctAnswers)
            .put("levelResults", levels)
            .put("skillResults", skills)
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(RESULT, json.toString()).apply()
    }

    fun load(context: Context): PlacementAssessmentReport? {
        val raw = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(RESULT, null) ?: return null
        return runCatching {
            val json = JSONObject(raw)
            fun readResults(key: String): Map<String, PlacementSkillResult> {
                val values = json.optJSONObject(key) ?: return emptyMap()
                return values.keys().asSequence().mapNotNull { item ->
                    val obj = values.optJSONObject(item) ?: return@mapNotNull null
                    item to PlacementSkillResult(obj.optInt("correct"), obj.optInt("total"))
                }.toMap()
            }
            PlacementAssessmentReport(
                estimatedLevel = json.getString("estimatedLevel"),
                assessedAtMillis = json.optLong("assessedAtMillis"),
                totalQuestions = json.optInt("totalQuestions"),
                correctAnswers = json.optInt("correctAnswers"),
                levelResults = readResults("levelResults"),
                skillResults = readResults("skillResults")
            )
        }.getOrNull()
    }
}

@Composable
internal fun PlacementAssessmentScreen(
    onBack: () -> Unit,
    onComplete: (PlacementAssessmentReport) -> Unit
) {
    val context = LocalContext.current
    var questions by remember { mutableStateOf(buildPlacementAssessmentQuestions()) }
    var index by remember { mutableStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var answers by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var report by remember { mutableStateOf<PlacementAssessmentReport?>(null) }
    var score by remember { mutableStateOf(0) }
    val current = questions.getOrNull(index)

    fun restart() {
        questions = buildPlacementAssessmentQuestions()
        index = 0
        selected = null
        answers = emptyMap()
        report = null
        score = 0
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    androidx.compose.material3.Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                    Text("Vstupní rozřazovací test", color = Muted, fontSize = 13.sp)
                }
            }

            if (report != null) {
                val result = report!!
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        androidx.compose.material3.Icon(Icons.Default.School, contentDescription = null, tint = Gold, modifier = Modifier.size(34.dp))
                        Text("Tvoje doporučená úroveň", color = Color.White, fontSize = 16.sp)
                        Text(result.estimatedLevel, color = Gold, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                        Text("${result.correctAnswers} z ${result.totalQuestions} správně", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            "Výsledek je orientační. Test ověřuje především praktické věty a porozumění textu; během dalšího studia se doporučení může zpřesnit.",
                            color = Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 19.sp
                        )
                    }
                }
                Text("Výsledek podle úrovní", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                result.levelResults.forEach { (level, value) ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = CardSurface)) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(level, color = Ink, fontWeight = FontWeight.Bold)
                                Text("${value.correct}/${value.total}", color = ForestText, fontWeight = FontWeight.Bold)
                            }
                            LinearProgressIndicator(progress = { value.accuracyPercent / 100f }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
                Text("Výsledek podle dovedností", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                result.skillResults.forEach { (skill, value) ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(skill, color = Ink, fontSize = 14.sp)
                        Text("${value.accuracyPercent} %", color = ForestText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Button(
                    onClick = { restart() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zopakovat rozřazovací test", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zpět na přehled", fontWeight = FontWeight.Bold) }
            } else if (current != null) {
                Text("TEST ÚROVNĚ A1–C2 · 36 OTÁZEK", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text("Otázka ${index + 1} z ${questions.size}", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                LinearProgressIndicator(progress = { (index + 1).toFloat() / questions.size }, modifier = Modifier.fillMaxWidth())
                Text(current.level + " · " + current.skill, color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = CardSurface)) {
                    Text(current.prompt, modifier = Modifier.padding(20.dp), color = Ink, fontSize = 19.sp, lineHeight = 27.sp, fontWeight = FontWeight.SemiBold)
                }
                current.choices.forEachIndexed { choiceIndex, choice ->
                    val background = when {
                        selected == null -> OptionSurface
                        choiceIndex == current.correctIndex -> Correct
                        choiceIndex == selected -> Incorrect
                        else -> OptionSurface
                    }
                    Card(
                        onClick = {
                            if (selected == null) {
                                selected = choiceIndex
                                answers = answers + (current.id to choiceIndex)
                                if (choiceIndex == current.correctIndex) score += 1
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = background)
                    ) {
                        Text(choice, modifier = Modifier.fillMaxWidth().padding(16.dp), color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
                if (selected != null) {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = if (selected == current.correctIndex) Correct else Incorrect)) {
                        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(if (selected == current.correctIndex) "Správně!" else "Správná odpověď: ${current.choices[current.correctIndex]}", color = ForestText, fontWeight = FontWeight.Bold)
                            Text(current.explanation, color = Ink, fontSize = 13.sp, lineHeight = 19.sp)
                        }
                    }
                    Button(
                        onClick = {
                            if (index < questions.lastIndex) {
                                index += 1
                                selected = null
                            } else {
                                val result = evaluatePlacementAssessment(questions, answers)
                                PlacementAssessmentStore.save(context, result)
                                report = result
                                onComplete(result)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text(if (index == questions.lastIndex) "Vyhodnotit test" else "Další otázka", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}
