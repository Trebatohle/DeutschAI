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
    var selectedChoice by remember(lesson.id) { mutableStateOf<Int?>(null) }
    var answer by remember(lesson.id) { mutableStateOf(TextFieldValue("")) }
    var answerChecked by remember(lesson.id) { mutableStateOf(false) }
    var answerCorrect by remember(lesson.id) { mutableStateOf(false) }

    var introductionScore by remember(lesson.id) { mutableStateOf(0) }
    var writingScore by remember(lesson.id) { mutableStateOf(0) }
    var dictationScore by remember(lesson.id) { mutableStateOf(0) }
    var wrongTasks by remember(lesson.id) { mutableStateOf(emptyList<LessonExercise>()) }
    var reviewRoundMistakes by remember(lesson.id) { mutableStateOf(emptyList<LessonExercise>()) }
    var remainingTasks by remember(lesson.id) { mutableStateOf(emptyList<LessonExercise>()) }
    var completionRecorded by remember(lesson.id) { mutableStateOf(false) }

    fun clearAnswer() {
        selectedChoice = null
        answer = TextFieldValue("")
        answerChecked = false
        answerCorrect = false
    }

    fun finishLesson() {
        stage = LessonPracticeStage.COMPLETE
        if (!completionRecorded) {
            val totalCorrect = introductionScore + writingScore + dictationScore
            CourseResultStore.save(context, "sentence_${lesson.id}", totalCorrect, 35)
            onComplete(totalCorrect, 35)
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
        introductionScore = 0
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
                // The core lesson is complete after the introduction, writing and dictation.
                // Error correction is offered afterwards, but never starts automatically.
                remainingTasks = wrongTasks
                finishLesson()
            }
            LessonPracticeStage.REVIEW -> {
                remainingTasks = reviewRoundMistakes
                finishLesson()
            }
            LessonPracticeStage.COMPLETE -> Unit
        }
    }

    fun chooseMultipleChoiceAnswer(choiceIndex: Int) {
        if (selectedChoice != null) return
        val task = tasks.getOrNull(questionIndex) ?: return
        if (task.mode != LessonExerciseMode.INTRODUCTION) return

        val question = task.question
        val correct = choiceIndex == question.correctIndex
        selectedChoice = choiceIndex

        when (stage) {
            LessonPracticeStage.INTRODUCTION -> {
                if (correct) introductionScore += 1 else wrongTasks = wrongTasks + task
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
                sourceTitle = lesson.title,
                cefrLevel = lesson.level,
                courseId = lesson.id
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
                sourceTitle = lesson.title,
                cefrLevel = lesson.level,
                courseId = lesson.id
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
        LessonPracticeStage.REVIEW -> 2
        LessonPracticeStage.COMPLETE -> 2
    }
    val stageName = when (stage) {
        LessonPracticeStage.INTRODUCTION -> "Úvodní test 25 vět"
        LessonPracticeStage.WRITING -> "Psaní vět"
        LessonPracticeStage.DICTATION -> "Poslechový diktát"
        LessonPracticeStage.REVIEW -> "Dobrovolná oprava chyb"
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 13.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(
                                    if (stage == LessonPracticeStage.REVIEW) "OPAKOVÁNÍ CHYB" else "KROK ${stageIndex + 1} ZE 3",
                                    color = ForestText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.1.sp
                                )
                                Text(
                                    stageName,
                                    color = Ink,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Surface(
                                color = Mint,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "${questionIndex + 1} / ${tasks.size}",
                                    modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                                    color = ForestText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            repeat(3) { index ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .background(
                                            color = if (index <= stageIndex) Forest else Color(0xFFE3E5DF),
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                        if (stage != LessonPracticeStage.REVIEW) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listOf("Výběr", "Psaní", "Poslech").forEachIndexed { index, label ->
                                    Text(
                                        label,
                                        color = if (index == stageIndex) ForestText else Muted,
                                        fontSize = 10.sp,
                                        fontWeight = if (index == stageIndex) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
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
                        modifier = Modifier.fillMaxWidth().padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(9.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = Color(0xFF36594B),
                            shape = CircleShape,
                            modifier = Modifier.size(58.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("✓", color = Gold, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(
                            "LEKCE DOKONČENA",
                            color = Color(0xFFD3E4DA),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            "${introductionScore + writingScore + dictationScore}",
                            color = Gold,
                            fontSize = 42.sp,
                            lineHeight = 46.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "z 35 bodů",
                            color = Color(0xFFE0EAE5),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (remainingTasks.isEmpty()) {
                                "Výborně! Dokončil/a jsi celou lekci. Všechny odpovědi máš zvládnuté."
                            } else {
                                "Základní lekce je hotová. Zbývá ${remainingTasks.size} chyb, které si můžeš dobrovolně opravit hned teď. Chybné odpovědi jsou zároveň uložené do dlouhodobého opakování."
                            },
                            color = Color(0xFFE0EAE5),
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        Triple("Výběr", introductionScore, 25),
                        Triple("Psaní", writingScore, 5),
                        Triple("Poslech", dictationScore, 5)
                    ).forEach { metric ->
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(17.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 13.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(metric.first, color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                Text("${metric.second}/${metric.third}", color = ForestText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (shouldOfferOptionalMistakeReview(remainingTasks.size)) {
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
                        Text("Ano, opravit chyby (${remainingTasks.size})", fontWeight = FontWeight.Bold)
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
                    Text(
                        if (shouldOfferOptionalMistakeReview(remainingTasks.size)) "Teď ne – dokončit bez opravy" else "Vybrat jinou lekci",
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (currentTask != null && currentQuestion != null) {
                if (currentTask.mode == LessonExerciseMode.INTRODUCTION) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .background(Color(0xFFE3E5DF), CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth((questionIndex + 1).toFloat() / tasks.size.coerceAtLeast(1))
                                .height(6.dp)
                                .background(Forest, CircleShape)
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                if (stage == LessonPracticeStage.REVIEW) "ZKUS TO ZNOVU" else "VYBER SPRÁVNOU ODPOVĚĎ",
                                color = ForestText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.1.sp
                            )
                            Text(
                                currentQuestion.prompt,
                                color = Ink,
                                fontSize = 22.sp,
                                lineHeight = 29.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            currentQuestion.choices.forEachIndexed { index, choice ->
                                val choiceColor = when {
                                    selectedChoice == null -> OptionSurface
                                    index == currentQuestion.correctIndex -> Correct
                                    index == selectedChoice -> Incorrect
                                    else -> OptionSurface
                                }
                                Card(
                                    onClick = { chooseMultipleChoiceAnswer(index) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = choiceColor)
                                ) {
                                    Text(
                                        text = choice,
                                        color = Ink,
                                        fontSize = 16.sp,
                                        fontWeight = if (
                                            selectedChoice != null && index == currentQuestion.correctIndex
                                        ) FontWeight.Bold else FontWeight.Medium,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 15.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (selectedChoice != null) {
                        val wasCorrect = selectedChoice == currentQuestion.correctIndex
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (wasCorrect) Correct else Incorrect
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    if (wasCorrect) "Správně! Gut gemacht!" else "To nevadí — učíme se.",
                                    color = ForestText,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Celá věta:",
                                    color = ForestText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    currentQuestion.germanSentence,
                                    color = Ink,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 25.sp
                                )
                                GermanAudioControls(
                                    player = speaker,
                                    text = currentQuestion.germanSentence
                                )
                                Text(
                                    "Česky:",
                                    color = ForestText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    currentQuestion.translation,
                                    color = Ink,
                                    fontSize = 15.sp,
                                    lineHeight = 22.sp
                                )
                            }
                        }

                        Button(
                            onClick = { advance() },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ActionColor,
                                contentColor = ActionText
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                when {
                                    stage == LessonPracticeStage.REVIEW && questionIndex == tasks.lastIndex ->
                                        "Zobrazit výsledek"
                                    stage == LessonPracticeStage.REVIEW -> "Další chyba"
                                    questionIndex == tasks.lastIndex -> "Pokračovat k psaní"
                                    else -> "Další otázka"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Text(
                            "Vyber jednu odpověď. Po výběru uvidíš správnou větu i její překlad.",
                            color = Muted,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
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
