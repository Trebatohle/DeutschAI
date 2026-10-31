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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
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
import kotlin.random.Random

internal enum class VocabularyPreference { LEARN, SKIP }
internal enum class VocabularyPresentation { WORD, SENTENCE }

internal data class VocabularyQuestion(
    val entry: VocabularyEntry,
    val presentation: VocabularyPresentation,
    val germanText: String,
    val correctCzech: String,
    val choices: List<String>,
    val correctIndex: Int
)

private const val VOCABULARY_PREFS = "deutschai_vocabulary_preferences_v1"
private const val VOCABULARY_LEARN_SET = "learn_ids"
private const val VOCABULARY_SKIP_SET = "skip_ids"

internal object VocabularyPreferenceStore {
    private fun preferences(context: Context) =
        context.applicationContext.getSharedPreferences(VOCABULARY_PREFS, Context.MODE_PRIVATE)

    fun get(context: Context, entryId: String): VocabularyPreference? {
        val prefs = preferences(context)
        val skipped = prefs.getStringSet(VOCABULARY_SKIP_SET, emptySet()).orEmpty()
        if (entryId in skipped) return VocabularyPreference.SKIP
        val learning = prefs.getStringSet(VOCABULARY_LEARN_SET, emptySet()).orEmpty()
        if (entryId in learning) return VocabularyPreference.LEARN
        return null
    }

    fun set(context: Context, entryId: String, preference: VocabularyPreference) {
        val prefs = preferences(context)
        val learning = prefs.getStringSet(VOCABULARY_LEARN_SET, emptySet()).orEmpty().toMutableSet()
        val skipped = prefs.getStringSet(VOCABULARY_SKIP_SET, emptySet()).orEmpty().toMutableSet()
        when (preference) {
            VocabularyPreference.LEARN -> {
                skipped.remove(entryId)
                learning.add(entryId)
            }
            VocabularyPreference.SKIP -> {
                learning.remove(entryId)
                skipped.add(entryId)
            }
        }
        prefs.edit()
            .putStringSet(VOCABULARY_LEARN_SET, learning.toSet())
            .putStringSet(VOCABULARY_SKIP_SET, skipped.toSet())
            .apply()
    }

    fun available(context: Context): List<VocabularyEntry> {
        val skipped = preferences(context)
            .getStringSet(VOCABULARY_SKIP_SET, emptySet())
            .orEmpty()
        return vocabularyCatalog.filterNot { it.id in skipped }
    }

    fun learnedCount(context: Context): Int =
        preferences(context).getStringSet(VOCABULARY_LEARN_SET, emptySet()).orEmpty().size

    fun skippedCount(context: Context): Int =
        preferences(context).getStringSet(VOCABULARY_SKIP_SET, emptySet()).orEmpty().size

    fun resetSkipped(context: Context) {
        preferences(context).edit().remove(VOCABULARY_SKIP_SET).apply()
    }
}

private val germanPluralNouns = setOf("Eltern", "Kosten")

private val czechPluralMeanings = setOf(
    "brýle", "dveře", "džíny", "hodiny", "játra", "kalhoty", "náklady",
    "narozeniny", "nůžky", "peníze", "plavky", "plíce", "prázdniny",
    "rodiče", "schody", "sluchátka", "záda"
)

internal fun vocabularySentenceGerman(entry: VocabularyEntry): String =
    if (entry.word in germanPluralNouns) {
        "Das sind ${entry.article} ${entry.word}."
    } else {
        "Das ist ${entry.article} ${entry.word}."
    }

internal fun vocabularySentenceCzech(entry: VocabularyEntry): String =
    if (entry.word in germanPluralNouns || entry.czech.trim().lowercase() in czechPluralMeanings) {
        "To jsou ${entry.czech}."
    } else {
        "To je ${entry.czech}."
    }

private fun vocabularyAnswer(entry: VocabularyEntry, presentation: VocabularyPresentation): String =
    when (presentation) {
        VocabularyPresentation.WORD -> entry.czech
        VocabularyPresentation.SENTENCE -> vocabularySentenceCzech(entry)
    }

internal fun makeVocabularyQuestion(
    entry: VocabularyEntry,
    presentation: VocabularyPresentation,
    allEntries: List<VocabularyEntry> = vocabularyCatalog,
    random: Random = Random.Default
): VocabularyQuestion {
    val correct = vocabularyAnswer(entry, presentation)
    val distractors = allEntries.asSequence()
        .filter { it.id != entry.id }
        .map { vocabularyAnswer(it, presentation) }
        .filter { it != correct }
        .distinct()
        .toList()
        .shuffled(random)
        .take(3)
    check(distractors.size == 3) {
        "Vocabulary question for ${entry.word} needs three distinct Czech distractors."
    }
    val choices = (distractors + correct).shuffled(random)
    return VocabularyQuestion(
        entry = entry,
        presentation = presentation,
        germanText = when (presentation) {
            VocabularyPresentation.WORD -> "${entry.article} ${entry.word}"
            VocabularyPresentation.SENTENCE -> vocabularySentenceGerman(entry)
        },
        correctCzech = correct,
        choices = choices,
        correctIndex = choices.indexOf(correct)
    )
}

internal fun buildVocabularyBatch(
    availableEntries: List<VocabularyEntry> = vocabularyCatalog,
    count: Int = 25,
    random: Random = Random.Default
): List<VocabularyQuestion> {
    if (availableEntries.isEmpty() || count <= 0) return emptyList()
    return availableEntries.shuffled(random)
        .take(count.coerceAtMost(availableEntries.size))
        .map { entry ->
            val presentation = if (random.nextBoolean()) {
                VocabularyPresentation.WORD
            } else {
                VocabularyPresentation.SENTENCE
            }
            makeVocabularyQuestion(entry, presentation, availableEntries, random)
        }
}

@Composable
internal fun VocabularyPracticeScreen(
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit
) {
    val context = LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    var questions by remember {
        mutableStateOf(buildVocabularyBatch(VocabularyPreferenceStore.available(context)))
    }
    var questionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    val currentQuestion = questions.getOrNull(questionIndex)
    var currentPreference by remember(currentQuestion?.entry?.id) {
        mutableStateOf(currentQuestion?.let { VocabularyPreferenceStore.get(context, it.entry.id) })
    }

    fun startNextBatch() {
        questions = buildVocabularyBatch(VocabularyPreferenceStore.available(context))
        questionIndex = 0
        selectedAnswer = null
        score = 0
        finished = false
        currentPreference = null
    }

    fun setPreference(preference: VocabularyPreference) {
        val question = currentQuestion ?: return
        VocabularyPreferenceStore.set(context, question.entry.id, preference)
        currentPreference = preference
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
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
                    Text("Slovní zásoba · 2 000 výrazů", color = Muted, fontSize = 13.sp)
                }
            }

            if (questions.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Text("V učebním balíčku teď nejsou žádné položky.", color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Všechna slovíčka byla označena jako „Nechci se učit“. Můžeš je znovu vrátit do nabídky.",
                            color = Muted, fontSize = 14.sp, lineHeight = 21.sp
                        )
                    }
                }
                Button(
                    onClick = {
                        VocabularyPreferenceStore.resetSkipped(context)
                        startNextBatch()
                    },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Vrátit vyřazená slovíčka", fontWeight = FontWeight.Bold) }
            } else if (finished) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(23.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material3.Icon(Icons.Default.Book, contentDescription = null, tint = Gold, modifier = Modifier.size(42.dp))
                        Text("Sada dokončena", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("${score} z ${questions.size} správně", color = Gold, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "V učebním balíčku je ${VocabularyPreferenceStore.available(context).size} položek. " +
                                "Chceš se učit: ${VocabularyPreferenceStore.learnedCount(context)} · " +
                                "Nechceš se učit: ${VocabularyPreferenceStore.skippedCount(context)}.",
                            color = Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 20.sp
                        )
                    }
                }
                Button(
                    onClick = { startNextBatch() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Další náhodná sada", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zpět k členům a podstatným jménům", fontWeight = FontWeight.Bold) }
            } else if (currentQuestion != null) {
                Text(
                    "OTÁZKA ${questionIndex + 1} Z ${questions.size}",
                    color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp
                )
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Vyber správný český význam", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    if (currentQuestion.presentation == VocabularyPresentation.WORD) {
                        "Německé podstatné jméno včetně členu"
                    } else {
                        "Krátká německá věta"
                    },
                    color = Muted, fontSize = 13.sp
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(21.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            currentQuestion.germanText,
                            color = Ink, fontSize = 25.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold
                        )
                        GermanAudioControls(player = speaker, text = currentQuestion.germanText)
                    }
                }
                currentQuestion.choices.forEachIndexed { index, choice ->
                    val background = when {
                        selectedAnswer == null -> OptionSurface
                        index == currentQuestion.correctIndex -> Correct
                        index == selectedAnswer -> Incorrect
                        else -> OptionSurface
                    }
                    Card(
                        onClick = {
                            if (selectedAnswer == null) {
                                selectedAnswer = index
                                val correct = index == currentQuestion.correctIndex
                                LearningReviewStore.recordAnswer(
                                    context = context,
                                    question = ReviewQuestion(
                                        id = stableReviewId("vocabulary", currentQuestion.entry.id),
                                        sourceKind = "vocabulary",
                                        sourceTitle = "Slovní zásoba",
                                        prompt = currentQuestion.germanText,
                                        choices = currentQuestion.choices,
                                        correctIndex = currentQuestion.correctIndex,
                                        explanation = "Správný význam: ${currentQuestion.correctCzech}",
                                        germanSentence = currentQuestion.germanText,
                                        translation = currentQuestion.correctCzech
                                    ),
                                    wasCorrect = correct
                                )
                                if (correct) score += 1
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = background)
                    ) {
                        Text(
                            choice,
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            color = Ink, fontSize = 16.sp,
                            fontWeight = if (selectedAnswer != null && index == currentQuestion.correctIndex) FontWeight.Bold else FontWeight.Medium,
                            lineHeight = 22.sp
                        )
                    }
                }
                if (selectedAnswer != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = if (selectedAnswer == currentQuestion.correctIndex) Correct else Incorrect)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                if (selectedAnswer == currentQuestion.correctIndex) "Správně! Gut gemacht!" else "Správný význam: ${currentQuestion.correctCzech}",
                                color = ForestText, fontSize = 16.sp, fontWeight = FontWeight.Bold
                            )
                            Text("Celý výraz: ${currentQuestion.entry.article} ${currentQuestion.entry.word}", color = Ink, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("Česky: ${currentQuestion.entry.czech}", color = Ink, fontSize = 15.sp)
                            Text(
                                "Příklad: ${vocabularySentenceGerman(currentQuestion.entry)}",
                                color = Ink, fontSize = 14.sp, lineHeight = 20.sp
                            )
                            Text(
                                vocabularySentenceCzech(currentQuestion.entry),
                                color = Muted, fontSize = 13.sp, lineHeight = 19.sp
                            )
                        }
                    }

                    Text("Chceš tuto položku dál procvičovat?", color = Ink, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { setPreference(VocabularyPreference.LEARN) },
                            modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentPreference == VocabularyPreference.LEARN) ActionColor else OptionSurface,
                                contentColor = if (currentPreference == VocabularyPreference.LEARN) ActionText else Ink
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            androidx.compose.material3.Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text("Chci se učit", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Button(
                            onClick = { setPreference(VocabularyPreference.SKIP) },
                            modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentPreference == VocabularyPreference.SKIP) Incorrect else OptionSurface,
                                contentColor = Ink
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            androidx.compose.material3.Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text("Nechci se učit", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    Text(
                        when (currentPreference) {
                            VocabularyPreference.LEARN -> "Uloženo: položka zůstává v dalších náhodných sadách."
                            VocabularyPreference.SKIP -> "Uloženo: položka se v dalších sadách nebude nabízet."
                            null -> "Preference je volitelná. Když ji nevybereš, položka zůstane v běžném výběru."
                        },
                        color = Muted, fontSize = 12.sp, lineHeight = 17.sp
                    )
                    Button(
                        onClick = {
                            if (questionIndex < questions.lastIndex) {
                                questionIndex += 1
                                selectedAnswer = null
                            } else {
                                finished = true
                                CourseResultStore.save(context, "vocabulary_learning", score, questions.size)
                                onComplete(score, questions.size)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            if (questionIndex < questions.lastIndex) "Další položka" else "Dokončit sadu",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}
