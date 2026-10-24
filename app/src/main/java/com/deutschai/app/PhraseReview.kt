package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AutoAwesome
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

private const val PHRASE_REVIEW_PREFS = "deutschai_smart_phrase_review"

private data class PhraseReviewCard(
    val id: String,
    val courseTitle: String,
    val prompt: String,
    val german: String,
    val czech: String
)

internal enum class PhraseRating { AGAIN, HARD, GOOD, EASY }

private fun phraseReviewCards(level: String): List<PhraseReviewCard> =
    sentenceCourseCatalog.filter { it.level == level }.flatMap { course ->
        course.items.mapIndexed { index, item ->
            PhraseReviewCard(
                id = "${level}_${course.id}_${index}",
                courseTitle = course.title,
                prompt = item.prompt,
                german = item.german,
                czech = item.czech
            )
        }
    }

private fun duePhraseCards(
    context: Context,
    cards: List<PhraseReviewCard>,
    now: Long = System.currentTimeMillis()
): List<PhraseReviewCard> {
    val prefs = context.getSharedPreferences(PHRASE_REVIEW_PREFS, Context.MODE_PRIVATE)
    return cards.filter { card ->
        !prefs.contains("${card.id}_due") || prefs.getLong("${card.id}_due", 0L) <= now
    }
}

private fun savePhraseRating(
    context: Context,
    card: PhraseReviewCard,
    rating: PhraseRating,
    now: Long = System.currentTimeMillis()
) = savePhraseRating(context, card.id, rating, now)

internal fun savePhraseRating(
    context: Context,
    phraseKey: String,
    rating: PhraseRating,
    now: Long = System.currentTimeMillis()
) {
    val prefs = context.applicationContext.getSharedPreferences(PHRASE_REVIEW_PREFS, Context.MODE_PRIVATE)
    val oldReps = prefs.getInt("${phraseKey}_reps", 0)
    val oldInterval = prefs.getFloat("${phraseKey}_interval", 0f).toDouble()
    val nextReps: Int
    val intervalDays: Double
    when (rating) {
        PhraseRating.AGAIN -> {
            nextReps = 0
            intervalDays = 10.0 / 1440.0
        }
        PhraseRating.HARD -> {
            nextReps = oldReps + 1
            intervalDays = if (oldInterval <= 0.0) 1.0 else max(1.0, oldInterval * 1.2)
        }
        PhraseRating.GOOD -> {
            nextReps = oldReps + 1
            intervalDays = when (oldReps) {
                0 -> 1.0
                1 -> 3.0
                else -> max(1.0, oldInterval * 2.0)
            }
        }
        PhraseRating.EASY -> {
            nextReps = oldReps + 1
            intervalDays = when (oldReps) {
                0 -> 3.0
                1 -> 6.0
                else -> max(1.0, oldInterval * 2.5)
            }
        }
    }
    prefs.edit()
        .putInt("${phraseKey}_reps", nextReps)
        .putFloat("${phraseKey}_interval", intervalDays.toFloat())
        .putLong("${phraseKey}_due", now + (intervalDays * 24.0 * 60.0 * 60.0 * 1000.0).roundToInt())
        .apply()
}

@Composable
internal fun PhraseReviewScreen(level: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    val cards = remember(level) { phraseReviewCards(level) }
    var session by remember(level) {
        mutableStateOf(duePhraseCards(context, cards).shuffled().take(25))
    }
    var initialDueCount by remember(level) { mutableStateOf(duePhraseCards(context, cards).size) }
    var index by remember(level) { mutableStateOf(0) }
    var revealed by remember(level) { mutableStateOf(false) }
    var completed by remember(level) { mutableStateOf(0) }

    fun startNextBatch() {
        val due = duePhraseCards(context, cards).shuffled()
        session = due.take(25)
        initialDueCount = due.size
        index = 0
        revealed = false
        completed = 0
    }

    val current = session.getOrNull(index)
    val remainingDue = duePhraseCards(context, cards).size
    val nextDueAt = cards.mapNotNull { card ->
        val prefs = context.getSharedPreferences(PHRASE_REVIEW_PREFS, Context.MODE_PRIVATE)
        val key = "${card.id}_due"
        if (prefs.contains(key)) prefs.getLong(key, 0L).takeIf { it > System.currentTimeMillis() } else null
    }.minOrNull()

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                    Text("Chytré opakování · $level", color = Muted, fontSize = 13.sp)
                }
            }

            Text("Zapamatuj si užitečné fráze", color = Ink, fontSize = 26.sp, lineHeight = 31.sp, fontWeight = FontWeight.Bold)
            Text(
                "Nejprve si zkus frázi říct německy. Potom odhal odpověď a označ, jak dobře sis ji vybavil/a. Aplikace si podle toho naplánuje další opakování.",
                color = Muted, fontSize = 14.sp, lineHeight = 21.sp
            )

            if (current != null) {
                Text("SADA ${index + 1} / ${session.size}  ·  ${cards.size} FRÁZÍ V KURZU", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                LinearProgressIndicator(
                    progress = { (index + 1).toFloat() / session.size.coerceAtLeast(1).toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ForestText)
                            Text(current.courseTitle, color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Jak bys to řekl/a německy?", color = Muted, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text(current.prompt, color = Ink, fontSize = 21.sp, lineHeight = 29.sp, fontWeight = FontWeight.SemiBold)
                        if (revealed) {
                            Spacer(Modifier.heightIn(min = 2.dp))
                            Text("NĚMECKY", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text(current.german, color = Ink, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold)
                            GermanAudioControls(player = speaker, text = current.german)
                            Text("ČESKY", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text(current.czech, color = Muted, fontSize = 15.sp, lineHeight = 22.sp)
                        }
                    }
                }

                if (!revealed) {
                    Button(
                        onClick = { revealed = true },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text("Zobrazit odpověď", fontWeight = FontWeight.Bold) }
                } else {
                    Text("Jak dobře jsi frázi znal/a?", color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    val ratings = listOf(
                        PhraseRating.AGAIN to ("Znovu" to "za 10 minut"),
                        PhraseRating.HARD to ("Těžké" to "zítra"),
                        PhraseRating.GOOD to ("Dobře" to "za 1–3 dny"),
                        PhraseRating.EASY to ("Snadné" to "za 3–6 dní")
                    )
                    ratings.forEach { (rating, labels) ->
                        Button(
                            onClick = {
                                savePhraseRating(context, current, rating)
                                completed += 1
                                index += 1
                                revealed = false
                            },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when (rating) {
                                    PhraseRating.AGAIN -> Incorrect
                                    PhraseRating.HARD -> Color(0xFFF5E9D4)
                                    PhraseRating.GOOD -> Mint
                                    PhraseRating.EASY -> Color(0xFFD9EDE1)
                                },
                                contentColor = Ink
                            ),
                            shape = RoundedCornerShape(13.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Text(labels.first, fontWeight = FontWeight.Bold)
                                Text(labels.second, fontSize = 11.sp)
                            }
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Mint)
                ) {
                    Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            if (cards.isEmpty()) "Tato úroveň zatím nemá fráze." else "Tato sada je hotová!",
                            color = ForestText, fontSize = 22.sp, fontWeight = FontWeight.Bold
                        )
                        Text(
                            when {
                                cards.isEmpty() -> "Jakmile budou do kurzu přidány lekce, objeví se zde i fráze k opakování."
                                remainingDue > 0 -> "Dobrá práce. Ještě čeká $remainingDue frází. Pokračuj další sadou 25 výrazů."
                                nextDueAt != null -> "Dnes máš vše procvičeno. Další opakování je naplánováno na " +
                                    SimpleDateFormat("dd.MM. 'v' HH:mm", Locale.getDefault()).format(Date(nextDueAt)) + "."
                                else -> "Výborně! V této úrovni zatím není další fráze k opakování."
                            },
                            color = ForestText, fontSize = 14.sp, lineHeight = 21.sp
                        )
                        Text("V této sadě zopakováno: $completed", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                if (remainingDue > 0) {
                    Button(
                        onClick = { startNextBatch() },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text("Pokračovat dalšími 25 frázemi", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}
