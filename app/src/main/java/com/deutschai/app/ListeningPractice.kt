package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
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

private data class ListeningSourcePhrase(
    val courseId: String,
    val courseTitle: String,
    val itemIndex: Int,
    val item: SentencePracticeItem
)

private data class ListeningQuestion(
    val source: ListeningSourcePhrase,
    val choices: List<String>,
    val correctIndex: Int
)

private fun buildListeningQuestions(level: String, count: Int = 25): List<ListeningQuestion> {
    val sources = sentenceCourseCatalog
        .filter { it.level == level }
        .flatMap { course ->
            course.items.mapIndexed { index, item ->
                ListeningSourcePhrase(course.id, course.title, index, item)
            }
        }
    if (sources.isEmpty()) return emptyList()

    return sources.shuffled().take(count).map { source ->
        val alternatives = sources
            .filter { it.item.czech != source.item.czech }
            .distinctBy { it.item.czech }
            .shuffled()
            .take(3)
            .map { it.item.czech }
        val options = (alternatives + source.item.czech).shuffled()
        ListeningQuestion(
            source = source,
            choices = options,
            correctIndex = options.indexOf(source.item.czech)
        )
    }
}

internal fun markPhraseNeedsReview(
    context: Context,
    level: String,
    courseId: String,
    itemIndex: Int
) {
    val key = "${level}_${courseId}_${itemIndex}"
    val prefs = context.applicationContext.getSharedPreferences("deutschai_smart_phrase_review", Context.MODE_PRIVATE)
    prefs.edit()
        .putLong("${key}_due", System.currentTimeMillis())
        .putInt("${key}_mistakes", prefs.getInt("${key}_mistakes", 0) + 1)
        .apply()
}

@Composable
internal fun ListeningPracticeScreen(level: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    var questions by remember(level) { mutableStateOf(buildListeningQuestions(level)) }
    var questionIndex by remember(level) { mutableStateOf(0) }
    var score by remember(level) { mutableStateOf(0) }
    var finished by remember(level) { mutableStateOf(false) }
    var completedCount by remember(level) { mutableStateOf(0) }
    val question = questions.getOrNull(questionIndex)
    var heard by remember(level, questionIndex) { mutableStateOf(false) }
    var selectedAnswer by remember(level, questionIndex) { mutableStateOf<Int?>(null) }

    fun restart() {
        questions = buildListeningQuestions(level)
        questionIndex = 0
        score = 0
        finished = false
        completedCount = 0
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding().verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = Forest, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                    Text("Poslech a porozumění · $level", color = Muted, fontSize = 13.sp)
                }
            }

            Text("Rozuměj němčině sluchem", color = Ink, fontSize = 26.sp, lineHeight = 31.sp, fontWeight = FontWeight.Bold)
            Text(
                "Uslyšíš větu ve standardní němčině pro Německo (de-DE). Německý text zůstane skrytý, dokud neodpovíš. Odpovědi se aktivují až po přehrání celé věty.",
                color = Muted, fontSize = 14.sp, lineHeight = 21.sp
            )

            if (finished) {
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
                            "Věty, u kterých ses spletl/a, jsme zařadili mezi fráze k opakování.",
                            color = Color(0xFFE0EAE5), fontSize = 14.sp, lineHeight = 21.sp
                        )
                        LatestResultLine(CourseResultStore.load(context, "listening_$level"))
                    }
                }
                Button(
                    onClick = { restart() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zkusit nový poslech", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zpět ke kurzům", fontWeight = FontWeight.Bold) }
            } else if (question != null) {
                Text(
                    "OTÁZKA ${questionIndex + 1} Z ${questions.size}",
                    color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp
                )
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size.coerceAtLeast(1).toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Forest)
                            Text(question.source.courseTitle, color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Co znamená věta, kterou uslyšíš?", color = Ink, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold)
                        GermanAudioControls(
                            player = speaker,
                            text = question.source.item.german,
                            onPlaybackCompleted = { heard = true }
                        )
                        if (!heard && speaker.status == GermanVoiceStatus.READY) {
                            Text("Nejdřív si celou větu poslechni. Potom se odemknou odpovědi.", color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
                        }
                    }
                }

                question.choices.forEachIndexed { index, choice ->
                    val choiceColor = when {
                        selectedAnswer == null -> Color.White
                        index == question.correctIndex -> Correct
                        selectedAnswer == index -> Incorrect
                        else -> Color.White
                    }
                    Card(
                        onClick = {
                            if (heard && selectedAnswer == null) {
                                selectedAnswer = index
                                completedCount += 1
                                if (index == question.correctIndex) {
                                    score += 1
                                } else {
                                    markPhraseNeedsReview(
                                        context,
                                        level,
                                        question.source.courseId,
                                        question.source.itemIndex
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = choiceColor)
                    ) {
                        Text(
                            choice,
                            modifier = Modifier.fillMaxWidth().padding(17.dp),
                            color = if (!heard) Muted else Ink,
                            fontSize = 15.sp,
                            fontWeight = if (selectedAnswer != null && index == question.correctIndex) FontWeight.Bold else FontWeight.Medium,
                            lineHeight = 22.sp
                        )
                    }
                }

                if (selectedAnswer == null && !heard) {
                    Text("Po dokončení přehrávání bude možné vybrat odpověď.", color = Muted, fontSize = 12.sp)
                }

                if (selectedAnswer != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = if (selectedAnswer == question.correctIndex) Correct else Incorrect)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                if (selectedAnswer == question.correctIndex) "Správně! Gut gemacht!" else "Správný význam:",
                                color = Forest, fontWeight = FontWeight.Bold, fontSize = 16.sp
                            )
                            Text(question.source.item.german, color = Ink, fontSize = 19.sp, lineHeight = 27.sp, fontWeight = FontWeight.Bold)
                            GermanAudioControls(
                                player = speaker,
                                text = question.source.item.german,
                                showSlow = true
                            )
                            Text(question.source.item.czech, color = Ink, fontSize = 15.sp, lineHeight = 22.sp)
                            Text("Situace: ${question.source.item.prompt}", color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                        }
                    }
                    Button(
                        onClick = {
                            if (questionIndex < questions.lastIndex) {
                                questionIndex += 1
                            } else {
                                CourseResultStore.save(context, "listening_$level", score, questions.size)
                                finished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(if (questionIndex == questions.lastIndex) "Zobrazit výsledek" else "Další otázka", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Text("Pro tuto úroveň zatím nejsou připravené fráze.", color = Muted, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.heightIn(min = 4.dp))
        }
    }
}
