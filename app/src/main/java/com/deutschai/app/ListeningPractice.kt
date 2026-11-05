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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    val displayWithGap: String = ""
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
                answerText = source.item.czech
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
                    displayWithGap = source.item.german.replaceRange(selected.range, "_____")
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
                answerText = source.item.german
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
                    answerText = course.title
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
        }
    )
}

@Composable
internal fun ListeningPracticeScreen(
    level: String,
    onBack: () -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    var mode by remember(level) { mutableStateOf<ListeningExerciseMode?>(null) }
    var questions by remember(level) { mutableStateOf(emptyList<ListeningQuestion>()) }
    var questionIndex by remember(level) { mutableStateOf(0) }
    var score by remember(level) { mutableStateOf(0) }
    var finished by remember(level) { mutableStateOf(false) }
    var heard by remember(level) { mutableStateOf(false) }
    var selectedAnswer by remember(level) { mutableStateOf<Int?>(null) }
    var typedAnswer by remember(level) { mutableStateOf("") }
    var answerChecked by remember(level) { mutableStateOf(false) }
    var answerWasCorrect by remember(level) { mutableStateOf(false) }
    var ratingSaved by remember(level) { mutableStateOf(false) }
    val question = questions.getOrNull(questionIndex)

    fun startExercise(selectedMode: ListeningExerciseMode) {
        mode = selectedMode
        questions = buildListeningQuestions(level, selectedMode)
        questionIndex = 0
        score = 0
        finished = false
        heard = false
        selectedAnswer = null
        typedAnswer = ""
        answerChecked = false
        answerWasCorrect = false
        ratingSaved = false
    }

    fun recordCurrentAnswer(correct: Boolean) {
        val current = question ?: return
        if (answerChecked) return
        answerWasCorrect = correct
        answerChecked = true
        if (correct) score += 1
    }

    fun moveToNextQuestion() {
        if (questionIndex < questions.lastIndex) {
            questionIndex += 1
            heard = false
            selectedAnswer = null
            typedAnswer = ""
            answerChecked = false
            answerWasCorrect = false
            ratingSaved = false
        } else {
            CourseResultStore.save(context, "listening_$level", score, questions.size)
            StudyHabitStore.recordSession(context)
            finished = true
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding().verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AppScreenHeader(title = "Poslech a porozumění · $level", onBack = onBack, onHome = onHome)

            if (mode == null) {
                Text("Rozvíjej porozumění němčině sluchem", color = Ink, fontSize = 25.sp, lineHeight = 31.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Vyber si typ poslechového cvičení. Vše funguje offline pomocí německého hlasu nainstalovaného v telefonu.",
                    color = Muted, fontSize = 14.sp, lineHeight = 21.sp
                )
                ListeningExerciseMode.entries.forEach { exercise ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = if (exercise == ListeningExerciseMode.CONTEXT) Mint else CardSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            Text(exercise.title, color = if (exercise == ListeningExerciseMode.CONTEXT) ForestText else Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text(exercise.description, color = if (exercise == ListeningExerciseMode.CONTEXT) ForestText else Muted, fontSize = 13.sp, lineHeight = 19.sp)
                            Button(
                                onClick = { startExercise(exercise) },
                                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                                shape = RoundedCornerShape(12.dp)
                            ) { Text("Spustit cvičení", fontWeight = FontWeight.Bold) }
                        }
                    }
                }
            } else if (questions.isEmpty()) {
                Text("Pro tento typ cvičení zatím nejsou na úrovni $level připravené dostatečné podklady.", color = Muted, fontSize = 14.sp, lineHeight = 21.sp)
                Button(
                    onClick = { mode = null },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(13.dp)
                ) { Text("Zpět k výběru poslechu", fontWeight = FontWeight.Bold) }
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
                        Text("$score z ${questions.size} správně", color = Gold, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Výsledky se uložily do dlouhodobého přehledu znalostí. Položky k opakování se znovu objeví v osobním tréninku.",
                            color = Color(0xFFE0EAE5), fontSize = 14.sp, lineHeight = 21.sp
                        )
                        LatestResultLine(CourseResultStore.load(context, "listening_$level"))
                    }
                }
                Button(
                    onClick = { mode?.let { startExercise(it) } },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zkusit novou sadu", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = { mode = null },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Vybrat jiný typ poslechu", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OptionSurface, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zpět ke kurzům", fontWeight = FontWeight.Bold) }
            } else if (question != null) {
                Text(
                    "${mode?.title?.uppercase()} · OTÁZKA ${questionIndex + 1} Z ${questions.size}",
                    color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp
                )
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size.coerceAtLeast(1).toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = ForestText)
                            Text(question.source.courseTitle, color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(question.taskPrompt, color = Ink, fontSize = 19.sp, lineHeight = 27.sp, fontWeight = FontWeight.SemiBold)
                        if (question.displayWithGap.isNotBlank()) {
                            Text(question.displayWithGap, color = Ink, fontSize = 17.sp, lineHeight = 25.sp, fontWeight = FontWeight.Bold)
                        }
                        GermanAudioControls(
                            player = speaker,
                            text = question.listeningText,
                            onPlaybackCompleted = { heard = true },
                            showSlow = true
                        )
                        if (!heard && speaker.status == GermanVoiceStatus.READY) {
                            Text("Nejdřív si poslechni celý záznam. Potom se odemkne odpověď.", color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
                        }
                    }
                }

                if (mode == ListeningExerciseMode.DICTATION) {
                    OutlinedTextField(
                        value = typedAnswer,
                        onValueChange = { if (!answerChecked) typedAnswer = it },
                        enabled = heard && !answerChecked,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Napiš, co jsi slyšel/a") },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done
                        )
                    )
                    if (!answerChecked) {
                        Button(
                            onClick = {
                                recordCurrentAnswer(
                                    normalizeListeningDictation(typedAnswer) ==
                                        normalizeListeningDictation(question.answerText)
                                )
                            },
                            enabled = heard && typedAnswer.isNotBlank(),
                            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = RoundedCornerShape(13.dp)
                        ) { Text("Zkontrolovat přepis", fontWeight = FontWeight.Bold) }
                    }
                } else {
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
                }

                if (answerChecked) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = if (answerWasCorrect) Correct else Incorrect)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                if (answerWasCorrect) "Správně! Gut gemacht!" else "Správná odpověď: ${question.answerText}",
                                color = ForestText, fontWeight = FontWeight.Bold, fontSize = 16.sp
                            )
                            if (question.mode == ListeningExerciseMode.DICTATION ||
                                question.mode == ListeningExerciseMode.GAP ||
                                question.mode == ListeningExerciseMode.MEANING
                            ) {
                                Text(question.source.item.german, color = Ink, fontSize = 18.sp, lineHeight = 26.sp, fontWeight = FontWeight.Bold)
                                GermanAudioControls(player = speaker, text = question.source.item.german, showSlow = true)
                                Text(question.source.item.czech, color = Muted, fontSize = 14.sp, lineHeight = 20.sp)
                            } else {
                                Text(question.listeningText, color = Ink, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold)
                                GermanAudioControls(player = speaker, text = question.listeningText, showSlow = true)
                                Text("Téma: ${question.answerText}", color = Muted, fontSize = 14.sp)
                            }
                        }
                    }

                    Text("Jak jistě jsi poslechu porozuměl/a?", color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    val phraseKey = "${level}_${question.source.courseId}_${question.source.itemIndex}"
                    val dayMillis = 24L * 60L * 60L * 1000L
                    val ratings = listOf(
                        Triple("Zatím ne", "Znovu", 10L * 60L * 1000L),
                        Triple("Ještě si nejsem jistý", "Zítra", dayMillis),
                        Triple("Umím", "Za 14 dní", 14L * dayMillis)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ratings.forEachIndexed { index, rating ->
                            Button(
                                onClick = {
                                    saveListeningConfidence(
                                        context = context,
                                        phraseKey = phraseKey,
                                        intervalMillis = rating.third,
                                        reviewQuestion = listeningReviewQuestion(question),
                                        wasCorrect = answerWasCorrect
                                    )
                                    ratingSaved = true
                                },
                                enabled = !ratingSaved,
                                modifier = Modifier.weight(1f).heightIn(min = 76.dp, max = 76.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 2.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when (index) {
                                        0 -> Incorrect
                                        1 -> RatingHard
                                        else -> RatingGood
                                    },
                                    contentColor = Ink
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        rating.first,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        maxLines = 2,
                                        softWrap = true
                                    )
                                    Text(rating.second, fontSize = 10.sp, maxLines = 1, softWrap = false)
                                }
                            }
                        }
                    }
                    if (ratingSaved) {
                        Text("Uloženo do chytrého opakování ✓", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = { moveToNextQuestion() },
                        enabled = ratingSaved,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            when {
                                !ratingSaved -> "Nejprve označ, jak jistě frázi znáš"
                                questionIndex == questions.lastIndex -> "Zobrazit výsledek"
                                else -> "Další poslech"
                            },
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
