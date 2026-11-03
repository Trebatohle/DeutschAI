package com.deutschai.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun LessonPracticeScreen(
    lesson: Lesson,
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    val speaker = rememberGermanSpeechPlayer()

    var selection by remember(lesson.id) {
        mutableStateOf(createLessonPracticeSelection(lesson.questions))
    }
    var stage by remember(lesson.id) { mutableStateOf(LessonPracticeStage.INTRODUCTION) }
    var tasks by remember(lesson.id) {
        mutableStateOf(selection.introduction.map { LessonExercise(it, LessonExerciseMode.INTRODUCTION) })
    }
    var questionIndex by remember(lesson.id) { mutableStateOf(0) }
    var answer by remember(lesson.id) { mutableStateOf(TextFieldValue("")) }
    var answerChecked by remember(lesson.id) { mutableStateOf(false) }
    var answerCorrect by remember(lesson.id) { mutableStateOf(false) }

    var writingScore by remember(lesson.id) { mutableStateOf(0) }
    var dictationScore by remember(lesson.id) { mutableStateOf(0) }
    var wrongTasks by remember(lesson.id) { mutableStateOf(emptyList<LessonExercise>()) }
    var reviewRoundMistakes by remember(lesson.id) { mutableStateOf(emptyList<LessonExercise>()) }
    var remainingTasks by remember(lesson.id) { mutableStateOf(emptyList<LessonExercise>()) }
    var completionRecorded by remember(lesson.id) { mutableStateOf(false) }

    fun clearAnswer() {
        answer = TextFieldValue("")
        answerChecked = false
        answerCorrect = false
    }

    fun finishLesson() {
        stage = LessonPracticeStage.COMPLETE
        if (!completionRecorded) {
            val totalCorrect = writingScore + dictationScore
            CourseResultStore.save(context, "sentence_${lesson.id}", totalCorrect, 10)
            onComplete(totalCorrect, 10)
            completionRecorded = true
        }
    }

    fun restartLesson() {
        selection = createLessonPracticeSelection(lesson.questions)
        tasks = selection.introduction.map {
            LessonExercise(it, LessonExerciseMode.INTRODUCTION)
        }
        stage = LessonPracticeStage.INTRODUCTION
        questionIndex = 0
        writingScore = 0
        dictationScore = 0
        wrongTasks = emptyList()
        reviewRoundMistakes = emptyList()
        remainingTasks = emptyList()
        completionRecorded = false
        clearAnswer()
    }

    fun beginWriting() {
        tasks = selection.writing.map { LessonExercise(it, LessonExerciseMode.WRITING) }
        stage = LessonPracticeStage.WRITING
        questionIndex = 0
        clearAnswer()
    }

    fun beginDictation() {
        tasks = selection.dictation.map { LessonExercise(it, LessonExerciseMode.DICTATION) }
        stage = LessonPracticeStage.DICTATION
        questionIndex = 0
        clearAnswer()
    }

    fun beginReview(reviewTasks: List<LessonExercise>) {
        tasks = reviewTasks
        reviewRoundMistakes = emptyList()
        stage = LessonPracticeStage.REVIEW
        questionIndex = 0
        clearAnswer()
    }

    fun advance() {
        if (questionIndex < tasks.lastIndex) {
            questionIndex += 1
            clearAnswer()
            return
        }

        when (stage) {
            LessonPracticeStage.INTRODUCTION -> beginWriting()
            LessonPracticeStage.WRITING -> beginDictation()
            LessonPracticeStage.DICTATION -> {
                if (wrongTasks.isNotEmpty()) beginReview(wrongTasks) else finishLesson()
            }
            LessonPracticeStage.REVIEW -> {
                remainingTasks = reviewRoundMistakes
                finishLesson()
            }
            LessonPracticeStage.COMPLETE -> Unit
        }
    }

    fun checkAnswer() {
        if (answerChecked || answer.text.isBlank()) return
        val task = tasks.getOrNull(questionIndex) ?: return
        val question = task.question
        val correct = isGermanWrittenAnswerCorrect(question, answer.text)

        answerChecked = true
        answerCorrect = correct

        when (stage) {
            LessonPracticeStage.WRITING -> {
                if (correct) writingScore += 1 else wrongTasks = wrongTasks + task
            }
            LessonPracticeStage.DICTATION -> {
                if (correct) dictationScore += 1 else wrongTasks = wrongTasks + task
            }
            LessonPracticeStage.REVIEW -> {
                if (!correct) reviewRoundMistakes = reviewRoundMistakes + task
            }
            else -> Unit
        }

        val sourceIndex = lesson.questions.indexOfFirst {
            it.germanSentence == question.germanSentence &&
                it.translation == question.translation
        }
        if (sourceIndex >= 0) {
            val reviewQuestion = question.toReviewQuestion(
                id = stableReviewId("sentence", lesson.id, sourceIndex.toString()),
                sourceKind = "sentences",
                sourceTitle = lesson.title
            )
            LearningReviewStore.recordAnswer(
                context = context,
                question = reviewQuestion,
                wasCorrect = correct,
                isMistakeReview = stage == LessonPracticeStage.REVIEW
            )
            if (!correct) {
                markPhraseNeedsReview(context, lesson.level, lesson.id, sourceIndex)
            }
        }
    }

    val currentTask = tasks.getOrNull(questionIndex)
    val currentQuestion = currentTask?.question
    val stageIndex = when (stage) {
        LessonPracticeStage.INTRODUCTION -> 0
        LessonPracticeStage.WRITING -> 1
        LessonPracticeStage.DICTATION -> 2
        LessonPracticeStage.REVIEW -> 3
        LessonPracticeStage.COMPLETE -> 3
    }
    val stageName = when (stage) {
        LessonPracticeStage.INTRODUCTION -> "Seznámení s větami"
        LessonPracticeStage.WRITING -> "Psaní vět"
        LessonPracticeStage.DICTATION -> "Poslechový diktát"
        LessonPracticeStage.REVIEW -> "Opakování chyb"
        LessonPracticeStage.COMPLETE -> "Výsledek lekce"
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppScreenHeader(
                title = lesson.title + " · " + lesson.level,
                onBack = onBack,
                onHome = onHome
            )

            if (stage != LessonPracticeStage.COMPLETE) {
                Text(
                    text = "KROK ${stageIndex + 1} ZE 4  ·  ${stageName.uppercase()}",
                    color = Muted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(5.dp)
                                .background(
                                    color = if (index <= stageIndex) Forest else Color(0xFFE3E5DF),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

            if (stage == LessonPracticeStage.COMPLETE) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Lekce dokončena!",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${writingScore + dictationScore} z 10",
                            color = Gold,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Psaní: $writingScore z 5  ·  Poslech: $dictationScore z 5",
                            color = Color(0xFFE0EAE5),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (remainingTasks.isEmpty()) {
                                "Výborně! Všechny procvičované věty máš zvládnuté."
                            } else {
                                "Několik vět ještě potřebuje procvičit. Můžeš si zopakovat pouze tyto chyby."
                            },
                            color = Color(0xFFE0EAE5),
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                }

                if (remainingTasks.isNotEmpty()) {
                    Button(
                        onClick = {
                            val retry = remainingTasks
                            remainingTasks = emptyList()
                            beginReview(retry)
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Opakovat zbývající chyby (${remainingTasks.size})", fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = { restartLesson() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Zopakovat lekci", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Vybrat jinou lekci", fontWeight = FontWeight.Bold)
                }
            } else if (currentTask != null && currentQuestion != null) {
                Text(
                    text = when (stage) {
                        LessonPracticeStage.INTRODUCTION ->
                            "Věty ${questionIndex + 1}–${minOf(questionIndex + 5, tasks.size)} z ${tasks.size}"
                        LessonPracticeStage.REVIEW -> "Chyba ${questionIndex + 1} z ${tasks.size}"
                        else -> "Otázka ${questionIndex + 1} z ${tasks.size}"
                    },
                    color = ForestText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                if (stage == LessonPracticeStage.INTRODUCTION) {
                    val pageTasks = tasks.drop(questionIndex).take(5)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            pageTasks.forEachIndexed { index, item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Text(
                                            item.question.germanSentence,
                                            color = Ink,
                                            fontSize = 16.sp,
                                            lineHeight = 22.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            item.question.translation,
                                            color = Muted,
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                    androidx.compose.material3.IconButton(
                                        onClick = { speaker.speak(item.question.germanSentence) },
                                        enabled = speaker.status == GermanVoiceStatus.READY
                                    ) {
                                        androidx.compose.material3.Icon(
                                            androidx.compose.material.icons.Icons.Default.VolumeUp,
                                            contentDescription = "Přehrát německou větu",
                                            tint = ActionColor,
                                            modifier = Modifier.size(23.dp)
                                        )
                                    }
                                }
                                if (index < pageTasks.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(OptionSurface)
                                    )
                                }
                            }
                        }
                    }

                    if (speaker.status != GermanVoiceStatus.READY) {
                        Text(
                            "Pro přehrávání je potřeba dostupný offline německý hlas (de-DE).",
                            color = Muted,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }

                    Button(
                        onClick = {
                            if (questionIndex + 5 < tasks.size) {
                                questionIndex += 5
                            } else {
                                beginWriting()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            if (questionIndex + 5 < tasks.size) "Další skupina" else "Začít psaní",
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(13.dp)
                        ) {
                            when (currentTask.mode) {
                                LessonExerciseMode.WRITING -> {
                                    Text(
                                        if (stage == LessonPracticeStage.REVIEW) "NAPIŠ ZNOVU NĚMECKY" else "PŘELOŽ DO NĚMČINY",
                                        color = Muted,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        currentQuestion.translation,
                                        color = Ink,
                                        fontSize = 22.sp,
                                        lineHeight = 30.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                LessonExerciseMode.DICTATION -> {
                                    Text(
                                        if (stage == LessonPracticeStage.REVIEW) {
                                            "POSLECHNI A ZNOVU PŘEPIŠ"
                                        } else {
                                            "POSLECHNI A PŘEPIŠ"
                                        },
                                        color = Muted,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "Větu uslyšíš německy. Napiš přesně to, co slyšíš.",
                                        color = Ink,
                                        fontSize = 17.sp,
                                        lineHeight = 24.sp
                                    )
                                    GermanAudioControls(
                                        player = speaker,
                                        text = currentQuestion.germanSentence,
                                        showSlow = true
                                    )
                                }
                                LessonExerciseMode.INTRODUCTION -> Unit
                            }

                            OutlinedTextField(
                                value = answer,
                                onValueChange = { if (!answerChecked) answer = it },
                                readOnly = answerChecked,
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Tvoje německá odpověď") },
                                placeholder = { Text("Napiš celou větu…") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences
                                )
                            )

                            Text(
                                "Německé znaky",
                                color = Muted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                                listOf("ä", "ö", "ü", "ß").forEach { character ->
                                    Button(
                                        onClick = { answer = insertGermanCharacter(answer, character) },
                                        enabled = !answerChecked,
                                        modifier = Modifier.weight(1f).heightIn(min = 42.dp),
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = OptionSurface,
                                            contentColor = Ink
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(character, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            if (answerChecked) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (answerCorrect) Correct else Incorrect
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            if (answerCorrect) "Správně! Gut gemacht!" else "Tady je správné řešení:",
                                            color = ForestText,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            if (answerCorrect) answer.text else currentQuestion.germanSentence,
                                            color = if (answerCorrect) ForestText else Ink,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp,
                                            lineHeight = 25.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (answerChecked) advance() else checkAnswer()
                        },
                        enabled = answerChecked || answer.text.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ActionColor,
                            contentColor = ActionText,
                            disabledContainerColor = OptionSurface,
                            disabledContentColor = Muted
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        val buttonText = if (!answerChecked) {
                            "Zkontrolovat"
                        } else if (questionIndex < tasks.lastIndex) {
                            "Další věta"
                        } else {
                            when (stage) {
                                LessonPracticeStage.WRITING -> "Pokračovat k diktátu"
                                LessonPracticeStage.DICTATION -> if (wrongTasks.isEmpty()) "Dokončit lekci" else "Opakovat chyby"
                                LessonPracticeStage.REVIEW -> "Zobrazit výsledek"
                                else -> "Pokračovat"
                            }
                        }
                        Text(buttonText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun insertGermanCharacter(
    current: TextFieldValue,
    character: String
): TextFieldValue {
    val start = minOf(current.selection.start, current.selection.end)
        .coerceIn(0, current.text.length)
    val end = maxOf(current.selection.start, current.selection.end)
        .coerceIn(0, current.text.length)
    val updatedText = current.text.replaceRange(start, end, character)
    return current.copy(
        text = updatedText,
        selection = TextRange(start + character.length)
    )
}
