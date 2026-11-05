package com.deutschai.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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

internal fun buildDailyTrainingPool(): List<ReviewQuestion> {
    val result = mutableListOf<ReviewQuestion>()

    sentenceCourseCatalog.forEach { course ->
        course.items.forEachIndexed { index, item ->
            val options = (
                listOf(item) +
                    course.items.filter { it.german != item.german }
                        .distinctBy { it.german }
                        .shuffled()
                        .take(3)
                ).shuffled()
            if (options.size >= 3) {
                result += ReviewQuestion(
                    id = stableReviewId("sentence", course.id, index.toString()),
                    sourceKind = "sentences",
                    sourceTitle = course.title,
                    prompt = item.prompt,
                    choices = options.map { it.german },
                    correctIndex = options.indexOfFirst { it.german == item.german },
                    explanation = "Správná věta znamená: ${item.czech}",
                    germanSentence = item.german,
                    translation = item.czech
                )
            }
        }
    }

    articleCategories.forEach { category ->
        category.words.forEach { word ->
            val choices = listOf("der", "die", "das")
            result += ReviewQuestion(
                id = stableReviewId("articles", category.id, word.word.lowercase()),
                sourceKind = "articles",
                sourceTitle = category.title,
                prompt = "Jaký člen patří ke slovu „${word.word}“?\nČesky: ${word.czech}",
                choices = choices,
                correctIndex = choices.indexOf(word.article),
                explanation = "${word.article} ${word.word} = ${word.czech}. Uč se podstatné jméno vždy společně se členem.",
                germanSentence = "${word.article} ${word.word}",
                translation = word.czech
            )
        }
    }

    wordToolGroups.forEach { group ->
        wordToolModes.forEach { mode ->
            makeWordToolQuestions(group, mode).forEach { question ->
                result += question.toReviewQuestion(group, mode)
            }
        }
    }

    (verbPracticeTopics + advancedVerbPracticeTopics).forEach { topic ->
        buildVerbQuestions(topic).forEach { question ->
            if (question.correctIndex in question.choices.indices && question.choices.distinct().size == question.choices.size) {
                result += question.toReviewQuestion(
                    id = stableReviewId("verbs", topic.id, question.prompt, question.germanSentence, question.translation),
                    sourceKind = "verbs",
                    sourceTitle = topic.title
                )
            }
        }
    }

    return result.distinctBy { it.id }.filter { it.correctIndex in it.choices.indices }
}

internal fun enrichScheduledReviewQuestion(
    question: ReviewQuestion,
    random: kotlin.random.Random = kotlin.random.Random(question.id.hashCode())
): ReviewQuestion {
    fun fourChoices(correctAnswer: String, candidates: List<String>): Pair<List<String>, Int> {
        val distractors = candidates.asSequence()
            .filter { it.isNotBlank() && !it.equals(correctAnswer, ignoreCase = true) }
            .distinctBy { it.lowercase() }
            .shuffled(random)
            .take(3)
            .toList()
        val choices = (distractors + correctAnswer).shuffled(random)
        return choices to choices.indexOf(correctAnswer)
    }

    return when {
        // Older persisted phrase cards used a dummy "I don't remember" option.
        // Upgrade them on display so every answer is a real, plausible German phrase.
        question.sourceKind == "phrases" && question.germanSentence.isNotBlank() -> {
            val (choices, correctIndex) = fourChoices(
                question.germanSentence,
                sentenceCourseCatalog.flatMap { course -> course.items.map { it.german } }
            )
            question.copy(choices = choices, correctIndex = correctIndex)
        }

        // Old dictation review entries had only "the answer" and "other answer".
        // Turn them into a real four-option sentence recognition question in the daily quiz.
        question.sourceKind == "listening" &&
            question.choices.size < 3 &&
            question.germanSentence.isNotBlank() -> {
            val correctAnswer = question.choices.getOrNull(question.correctIndex)
                ?.takeIf { it.isNotBlank() }
                ?: question.germanSentence
            val (choices, correctIndex) = fourChoices(
                correctAnswer,
                sentenceCourseCatalog.flatMap { course -> course.items.map { it.german } }
            )
            question.copy(
                prompt = "Kterou německou větu jsi v poslechu slyšel/a?",
                choices = choices,
                correctIndex = correctIndex
            )
        }

        // Active-recall notebook items saved by an earlier app version may still
        // contain a single real answer plus a dummy fallback.
        question.sourceKind == "vocabulary" &&
            question.id.startsWith("notebook_active_") &&
            question.choices.size < 4 -> {
            val correctAnswer = question.choices[question.correctIndex]
            val candidateWords = vocabularyCatalog.map { "${it.article} ${it.word}" } +
                sentenceCourseCatalog.flatMap { course ->
                    course.items.flatMap { item ->
                        Regex("[\\p{L}ÄÖÜäöüßẞ-]{3,}").findAll(item.german).map { it.value }.toList()
                    }
                }
            val (choices, correctIndex) = fourChoices(correctAnswer, candidateWords)
            question.copy(choices = choices, correctIndex = correctIndex)
        }

        else -> question
    }
}

internal fun makeDailyTrainingQuestions(
    context: android.content.Context,
    nowMillis: Long = System.currentTimeMillis()
): List<ReviewQuestion> {
    val history = LearningReviewStore.all(context).associateBy { it.question.id }
    val dueEntries = LearningReviewStore.prioritizedDue(context, nowMillis).take(6)
    val due = dueEntries
        .map { enrichScheduledReviewQuestion(it.question) }
        .distinctBy { it.id }
    val needed = (10 - due.size).coerceAtLeast(0)
    val dueIds = due.map { it.id }.toSet()
    val availablePool = buildDailyTrainingPool().filterNot { it.id in dueIds }
    // Prefer unseen material first; previously practiced material remains available for variety.
    val available = (
        availablePool.filterNot { it.id in history } +
            availablePool.filter { it.id in history }.shuffled()
        ).toMutableList()
    val kindOrder = listOf("sentences", "wordtools", "articles", "verbs", "wordtools", "sentences", "articles", "verbs", "sentences", "wordtools")
    val fresh = mutableListOf<ReviewQuestion>()

    kindOrder.forEach { kind ->
        if (fresh.size < needed) {
            val next = available.firstOrNull { it.sourceKind == kind && fresh.none { picked -> picked.id == it.id } }
            if (next != null) fresh += next
        }
    }
    if (fresh.size < needed) {
        fresh += available.filterNot { candidate -> fresh.any { it.id == candidate.id } }.take(needed - fresh.size)
    }
    // Keep scheduled reviews at the beginning so the learner sees their due material first.
    return (due + fresh).take(10)
}

@Composable
internal fun DailyTrainingScreen(
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    var questions by remember { mutableStateOf(makeDailyTrainingQuestions(context)) }
    var questionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    var wrongQuestions by remember { mutableStateOf<List<ReviewQuestion>>(emptyList()) }
    var isMistakeReview by remember { mutableStateOf(false) }
    var heardCurrentAudio by remember { mutableStateOf(false) }
    val question = questions.getOrNull(questionIndex)

    fun startMistakeReview() {
        if (wrongQuestions.isEmpty()) return
        questions = wrongQuestions.shuffled()
        wrongQuestions = emptyList()
        questionIndex = 0
        selectedAnswer = null
        heardCurrentAudio = false
        score = 0
        finished = false
        isMistakeReview = true
    }

    fun startNewDailyTraining() {
        questions = makeDailyTrainingQuestions(context)
        questionIndex = 0
        selectedAnswer = null
        heardCurrentAudio = false
        score = 0
        finished = false
        wrongQuestions = emptyList()
        isMistakeReview = false
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding().verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            AppScreenHeader(title = "Osobní denní trénink", onBack = onBack, onHome = onHome)

            if (finished) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Gold, modifier = Modifier.size(44.dp))
                        Text(if (isMistakeReview) "Opakování chyb dokončeno" else "Dnešní trénink dokončen", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                        Text("$score z ${questions.size} správně", color = Gold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(
                            when {
                                isMistakeReview && wrongQuestions.isEmpty() -> "Skvěle! Všechny dnes opakované chyby už zvládáš. Další opakování se naplánuje automaticky."
                                isMistakeReview -> "Některé odpovědi ještě potřebují procvičit. Můžeš je zopakovat znovu."
                                wrongQuestions.isEmpty() -> "Výborně. Dnes jsi zvládl/a všechno bez chyby."
                                else -> "Chybné otázky jsme uložili do dlouhodobého opakování, aby se ti později znovu připomněly."
                            },
                            color = Color(0xFFE0EAE5), fontSize = 14.sp, lineHeight = 21.sp
                        )
                    }
                }
                if (wrongQuestions.isNotEmpty()) {
                    Button(
                        onClick = { startMistakeReview() },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text("Opakovat jen chyby (${wrongQuestions.size})", fontWeight = FontWeight.Bold) }
                }
                Button(
                    onClick = { startNewDailyTraining() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Další trénink · nové otázky", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zpět na úvodní obrazovku", fontWeight = FontWeight.Bold) }
            } else if (question != null) {
                Text("10 OTÁZEK · VLASTNÍ TEMPO", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text("Otázka ${questionIndex + 1} z ${questions.size}", color = Ink, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Mint)
                ) {
                    Column(modifier = Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text(question.sourceTitle, color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(question.prompt, color = Ink, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                val requiresListening = question.sourceKind == "listening" && question.germanSentence.isNotBlank()
                if (requiresListening) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(15.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            Text("NEJDŘÍVE SI POSLECHNI VĚTU", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            GermanAudioControls(
                                player = speaker,
                                text = question.germanSentence,
                                onPlaybackCompleted = { heardCurrentAudio = true },
                                showSlow = true
                            )
                            if (!heardCurrentAudio) {
                                Text("Po dokončení přehrávání se odemknou odpovědi.", color = Muted, fontSize = 12.sp)
                            }
                        }
                    }
                }
                question.choices.forEachIndexed { index, choice ->
                    val background = when {
                        selectedAnswer == null -> OptionSurface
                        index == question.correctIndex -> Correct
                        index == selectedAnswer -> Incorrect
                        else -> OptionSurface
                    }
                    Card(
                        onClick = {
                            if (selectedAnswer == null && (!requiresListening || heardCurrentAudio)) {
                                selectedAnswer = index
                                val correct = index == question.correctIndex
                                if (correct) score += 1 else wrongQuestions = wrongQuestions + question
                                LearningReviewStore.recordAnswer(
                                    context = context,
                                    question = question,
                                    wasCorrect = correct,
                                    isMistakeReview = true
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = background)
                    ) {
                        Text(
                            choice, modifier = Modifier.fillMaxWidth().padding(17.dp),
                            color = Ink, fontSize = 16.sp,
                            fontWeight = if (selectedAnswer != null && index == question.correctIndex) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
                if (selectedAnswer != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = if (selectedAnswer == question.correctIndex) Correct else Incorrect)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                            Text(
                                if (selectedAnswer == question.correctIndex) "Správně! Gut gemacht!" else "Správná odpověď: ${question.choices[question.correctIndex]}",
                                color = ForestText, fontWeight = FontWeight.Bold, fontSize = 16.sp
                            )
                            if (question.germanSentence.isNotBlank()) {
                                Text("Celá věta:", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(question.germanSentence, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, lineHeight = 23.sp)
                                GermanAudioControls(player = speaker, text = question.germanSentence)
                            }
                            if (question.translation.isNotBlank()) {
                                Text("Česky:", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(question.translation, color = Ink, fontSize = 15.sp, lineHeight = 22.sp)
                            }
                            Text(question.explanation, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                        }
                    }
                    Button(
                        onClick = {
                            if (questionIndex < questions.lastIndex) {
                                questionIndex += 1
                                selectedAnswer = null
                                heardCurrentAudio = false
                            } else {
                                if (!isMistakeReview) onComplete(score, questions.size)
                                finished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text(if (questionIndex == questions.lastIndex) "Dokončit trénink" else "Další otázka", fontWeight = FontWeight.Bold) }
                }
            } else {
                Text("Pro dnešní trénink nejsou připravené otázky. Zkus lekci z knihovny.", color = Muted, fontSize = 15.sp)
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Zpět") }
            }
        }
    }
}
