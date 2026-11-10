package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

internal fun markPhraseNeedsReview(
    context: Context,
    level: String,
    courseId: String,
    itemIndex: Int
) {
    val key = "${level}_${courseId}_${itemIndex}"
    val prefs = context.applicationContext.getSharedPreferences(
        "deutschai_smart_phrase_review",
        Context.MODE_PRIVATE
    )
    prefs.edit()
        .putLong("${key}_due", System.currentTimeMillis())
        .putInt("${key}_mistakes", prefs.getInt("${key}_mistakes", 0) + 1)
        .apply()
}

internal data class ListeningConfidenceOption(
    val title: String,
    val subtitle: String,
    val intervalMillis: Long
)

internal fun listeningConfidenceOptions(): List<ListeningConfidenceOption> {
    val dayMillis = 24L * 60L * 60L * 1000L
    return listOf(
        ListeningConfidenceOption("Zatím ne", "Znovu", 10L * 60L * 1000L),
        ListeningConfidenceOption("Nejsem si jistý", "Zítra", dayMillis),
        ListeningConfidenceOption("Umím", "Za 14 dní", 14L * dayMillis)
    )
}

internal enum class ListeningExerciseMode(
    val title: String,
    val description: String
) {
    MEANING(
        "Poslech a význam",
        "Uslyšíš větu a vybereš její správný český význam."
    ),
    GAP(
        "Doplň chybějící slovo",
        "Poslechni si celou větu a vyber německé slovo, které v přepisu chybí."
    ),
    DICTATION(
        "Poslechový diktát",
        "Uslyšíš německou větu a zapíšeš ji celou vlastními slovy."
    ),
    CONTEXT(
        "Poslech v kontextu",
        "Uslyšíš několik vět z jednoho tematického kurzu a určíš, o jaké situaci mluvily."
    )
}

internal data class ListeningSourcePhrase(
    val courseId: String,
    val courseTitle: String,
    val itemIndex: Int,
    val item: SentencePracticeItem
)

internal data class ListeningQuestion(
    val id: String,
    val mode: ListeningExerciseMode,
    val source: ListeningSourcePhrase,
    val listeningText: String,
    val taskPrompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val answerText: String,
    val displayWithGap: String = "",
    val cefrLevel: String = ""
)

private val listeningGapStopWords = setOf(
    "aber", "auch", "bin", "bist", "das", "dem", "den", "der", "des", "die",
    "du", "ein", "eine", "einer", "einem", "einen", "er", "es", "hat", "haben",
    "ich", "im", "in", "ist", "mit", "nicht", "sie", "sind", "und", "wir", "zu"
)

private val listeningWordRegex = Regex("[\\p{L}ÄÖÜäöüßẞ-]{3,}")

private fun listeningWords(sentence: String): List<String> =
    listeningWordRegex.findAll(sentence)
        .map { it.value }
        .toList()

internal fun normalizeListeningDictation(value: String): String =
    value.trim()
        .lowercase()
        .replace(Regex("[.!?]+$"), "")
        .replace(Regex("[,;:]"), "")
        .replace(Regex("\\s+"), " ")

internal fun buildListeningQuestions(
    level: String,
    mode: ListeningExerciseMode,
    count: Int = 25,
    random: Random = Random.Default
): List<ListeningQuestion> {
    if (count <= 0) return emptyList()
    val sources = sentenceCourseCatalog
        .filter { it.level == level }
        .flatMap { course ->
            course.items.mapIndexed { index, item ->
                ListeningSourcePhrase(course.id, course.title, index, item)
            }
        }
    if (sources.isEmpty()) return emptyList()

    return when (mode) {
        ListeningExerciseMode.MEANING -> sources.shuffled(random).take(count).map { source ->
            val alternatives = sources
                .filter { it.item.czech != source.item.czech }
                .distinctBy { it.item.czech }
                .shuffled(random)
                .take(3)
                .map { it.item.czech }
            val options = (alternatives + source.item.czech).shuffled(random)
            ListeningQuestion(
                id = stableReviewId("listening", level, mode.name, source.courseId, source.itemIndex.toString()),
                mode = mode,
                source = source,
                listeningText = source.item.german,
                taskPrompt = "Co znamená věta, kterou uslyšíš?",
                choices = options,
                correctIndex = options.indexOf(source.item.czech),
                answerText = source.item.czech,
                cefrLevel = level
            )
        }

        ListeningExerciseMode.GAP -> {
            // Build the vocabulary pool once, then process only the 25 questions shown in the session.
            // The previous implementation mapped every phrase and re-filtered/shuffled the whole pool
            // for every phrase before taking 25, which caused long UI-thread stalls and Android ANRs.
            val tokenPool = sources.asSequence()
                .flatMap { listeningWords(it.item.german).asSequence() }
                .distinctBy { it.lowercase() }
                .toList()
            val shuffledTokens = tokenPool.shuffled(random)
            val selectedSources = sources.shuffled(random).take(count)
            selectedSources.mapNotNull { source ->
                val matches = listeningWordRegex.findAll(source.item.german).toList()
                val selected = matches.lastOrNull {
                    it.value.lowercase() !in listeningGapStopWords
                } ?: matches.lastOrNull()
                if (selected == null) return@mapNotNull null
                val answer = selected.value
                val distractors = shuffledTokens.asSequence()
                    .filterNot { it.equals(answer, ignoreCase = true) }
                    .distinctBy { it.lowercase() }
                    .take(3)
                    .toList()
                if (distractors.size < 3) return@mapNotNull null
                val options = (distractors + answer).shuffled(random)
                ListeningQuestion(
                    id = stableReviewId("listening", level, mode.name, source.courseId, source.itemIndex.toString()),
                    mode = mode,
                    source = source,
                    listeningText = source.item.german,
                    taskPrompt = "Poslechni si větu a vyber slovo, které do ní patří:",
                    choices = options,
                    correctIndex = options.indexOf(answer),
                    answerText = answer,
                    displayWithGap = source.item.german.replaceRange(selected.range, "_____"),
                    cefrLevel = level
                )
            }
        }

        ListeningExerciseMode.DICTATION -> sources.shuffled(random).take(count).map { source ->
            ListeningQuestion(
                id = stableReviewId("listening", level, mode.name, source.courseId, source.itemIndex.toString()),
                mode = mode,
                source = source,
                listeningText = source.item.german,
                taskPrompt = "Zapiš celou německou větu, kterou uslyšíš:",
                choices = emptyList(),
                correctIndex = -1,
                answerText = source.item.german,
                cefrLevel = level
            )
        }

        ListeningExerciseMode.CONTEXT -> {
            val courses = sentenceCourseCatalog
                .filter { it.level == level && it.items.size >= 3 }
                .shuffled(random)
            courses.take(count).mapNotNull { course ->
                val contextSources = course.items.indices.shuffled(random).take(3).map { index ->
                    ListeningSourcePhrase(course.id, course.title, index, course.items[index])
                }
                val distractors = courses
                    .filterNot { it.id == course.id }
                    .map { it.title }
                    .distinct()
                    .shuffled(random)
                    .take(3)
                if (contextSources.size < 3 || distractors.size < 3) return@mapNotNull null
                val options = (distractors + course.title).shuffled(random)
                val listeningText = contextSources.joinToString(" ") { it.item.german }
                val first = contextSources.first()
                ListeningQuestion(
                    id = stableReviewId(
                        "listening_context",
                        level,
                        course.id,
                        contextSources.joinToString("_") { it.itemIndex.toString() }
                    ),
                    mode = mode,
                    source = first,
                    listeningText = listeningText,
                    taskPrompt = "O jaké situaci se v poslechu mluvilo?",
                    choices = options,
                    correctIndex = options.indexOf(course.title),
                    answerText = course.title,
                    cefrLevel = level
                )
            }
        }
    }
}

internal fun listeningReviewQuestion(question: ListeningQuestion): ReviewQuestion {
    val reviewChoices = if (question.choices.size >= 2) {
        question.choices
    } else {
        listOf(question.answerText, "Jiná odpověď")
    }
    val reviewCorrectIndex = if (question.choices.size >= 2) {
        question.correctIndex
    } else {
        0
    }
    val explanation = when (question.mode) {
        ListeningExerciseMode.MEANING -> "Správný význam: ${question.answerText}"
        ListeningExerciseMode.GAP -> "Chybějící slovo: ${question.answerText}"
        ListeningExerciseMode.DICTATION -> "Správný přepis: ${question.answerText}"
        ListeningExerciseMode.CONTEXT -> "Téma poslechu: ${question.answerText}"
    }
    return ReviewQuestion(
        id = question.id,
        sourceKind = "listening",
        sourceTitle = question.source.courseTitle,
        prompt = question.taskPrompt,
        choices = reviewChoices,
        correctIndex = reviewCorrectIndex,
        explanation = explanation,
        germanSentence = question.listeningText,
        translation = if (question.mode == ListeningExerciseMode.CONTEXT) {
            question.answerText
        } else {
            question.source.item.czech
        },
        cefrLevel = question.cefrLevel
    )
}

@Composable
internal fun ListeningLibraryScreen(
    onBack: () -> Unit,
    onSelectLevel: (String) -> Unit,
    onHome: () -> Unit = onBack
) {
    val levels = listOf(
        Triple("A1", "Začátečník", "Krátké věty a běžná slovní zásoba."),
        Triple("A2", "Mírně pokročilý", "Každodenní situace a praktičtější sdělení."),
        Triple("B1", "Středně pokročilý", "Delší věty a samostatnější porozumění."),
        Triple("B2", "Vyšší středně pokročilý", "Přesnější formulace a náročnější témata.")
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AppScreenHeader(title = "Poslech a porozumění", onBack = onBack, onHome = onHome)
            Text("Vyber úroveň němčiny", color = Ink, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(
                "Každá úroveň obsahuje jednu samostatnou sadu 25 poslechových otázek. Uslyšíš německou větu a vybereš její český význam.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
            levels.forEach { (level, name, description) ->
                Card(
                    onClick = { onSelectLevel(level) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.size(54.dp)
                                .background(learningCategoryBackground(LearningCategoryVisual.LISTENING), RoundedCornerShape(16.dp)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                LearningCategoryVisual.LISTENING.image,
                                contentDescription = null,
                                tint = learningCategoryTint(LearningCategoryVisual.LISTENING),
                                modifier = Modifier.size(23.dp)
                            )
                            Text(level, color = learningCategoryTint(LearningCategoryVisual.LISTENING), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(level + " · " + name, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text(description, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                            Text("25 otázek · poslech a význam", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ListeningPracticeScreen(
    level: String,
    onBack: () -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    var questions by remember(level) {
        mutableStateOf(buildListeningQuestions(level, ListeningExerciseMode.MEANING, count = 25))
    }
    var questionIndex by remember(level) { mutableStateOf(0) }
    var score by remember(level) { mutableStateOf(0) }
    var finished by remember(level) { mutableStateOf(false) }
    var heard by remember(level) { mutableStateOf(false) }
    var selectedAnswer by remember(level) { mutableStateOf<Int?>(null) }
    var answerChecked by remember(level) { mutableStateOf(false) }
    var answerWasCorrect by remember(level) { mutableStateOf(false) }
    val question = questions.getOrNull(questionIndex)

    fun startExercise() {
        questions = buildListeningQuestions(level, ListeningExerciseMode.MEANING, count = 25)
        questionIndex = 0
        score = 0
        finished = false
        heard = false
        selectedAnswer = null
        answerChecked = false
        answerWasCorrect = false
    }

    fun recordCurrentAnswer(correct: Boolean) {
        if (question == null || answerChecked) return
        answerWasCorrect = correct
        answerChecked = true
        if (correct) score += 1
    }

    fun moveToNextQuestion() {
        if (questionIndex < questions.lastIndex) {
            questionIndex += 1
            heard = false
            selectedAnswer = null
            answerChecked = false
            answerWasCorrect = false
        } else {
            CourseResultStore.save(context, "listening_" + level, score, questions.size)
            StudyHabitStore.recordSession(context)
            finished = true
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AppScreenHeader(title = "Poslech a porozumění · " + level, onBack = onBack, onHome = onHome)

            if (questions.isEmpty()) {
                Text(
                    "Pro úroveň " + level + " zatím nejsou připravené dostatečné podklady.",
                    color = Muted,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(13.dp)
                ) { Text("Zpět k úrovním", fontWeight = FontWeight.Bold) }
            } else if (finished) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Gold)
                        Text("Poslech dokončen", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                        Text(
                            score.toString() + " z " + questions.size + " správně",
                            color = Gold,
                            fontSize = 29.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Výsledek jediné poslechové sady pro úroveň " + level + " se uložil do tvých pokroků.",
                            color = Color(0xFFE0EAE5),
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                        LatestResultLine(CourseResultStore.load(context, "listening_" + level))
                    }
                }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zpět k úrovním A1–B2", fontWeight = FontWeight.Bold) }
            } else if (question != null) {
                Text(
                    "POSLECH A POROZUMĚNÍ · OTÁZKA " + (questionIndex + 1) + " Z " + questions.size,
                    color = ForestText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size.coerceAtLeast(1).toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                    color = ForestText,
                    trackColor = Mint
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = ForestText)
                            Text(question.source.courseTitle, color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            "Poslechni si německou větu a vyber její český význam.",
                            color = Ink,
                            fontSize = 19.sp,
                            lineHeight = 27.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        GermanAudioControls(
                            player = speaker,
                            text = question.listeningText,
                            onPlaybackCompleted = { heard = true },
                            showSlow = true
                        )
                        if (!heard && speaker.status == GermanVoiceStatus.READY) {
                            Text(
                                "Nejdřív si poslechni celý záznam. Potom vyber správný význam.",
                                color = Muted,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                question.choices.forEachIndexed { index, choice ->
                    val choiceColor = when {
                        !answerChecked -> OptionSurface
                        index == question.correctIndex -> Correct
                        index == selectedAnswer -> Incorrect
                        else -> OptionSurface
                    }
                    Card(
                        onClick = {
                            if (heard && !answerChecked) {
                                selectedAnswer = index
                                recordCurrentAnswer(index == question.correctIndex)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = choiceColor)
                    ) {
                        Text(
                            choice,
                            modifier = Modifier.fillMaxWidth().padding(17.dp),
                            color = if (heard) Ink else Muted,
                            fontSize = 15.sp,
                            fontWeight = if (answerChecked && index == question.correctIndex) FontWeight.Bold else FontWeight.Medium,
                            lineHeight = 22.sp
                        )
                    }
                }

                if (answerChecked) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = if (answerWasCorrect) Correct else Incorrect)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                if (answerWasCorrect) "Správně! Gut gemacht!" else "Správná odpověď: " + question.answerText,
                                color = ForestText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                question.source.item.german,
                                color = Ink,
                                fontSize = 18.sp,
                                lineHeight = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                            GermanAudioControls(player = speaker, text = question.source.item.german, showSlow = true)
                            Text(question.source.item.czech, color = Muted, fontSize = 14.sp, lineHeight = 20.sp)
                        }
                    }
                    Button(
                        onClick = { moveToNextQuestion() },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            if (questionIndex == questions.lastIndex) "Zobrazit výsledek" else "Další poslech",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (!heard) {
                    Text("Po dokončení přehrávání bude možné odpovědět.", color = Muted, fontSize = 12.sp)
                }
            }
        }
    }
}
