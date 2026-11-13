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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

internal data class VocabularyDrillQuestion(
    val entry: VocabularyEntry,
    val choices: List<String>,
    val correctIndex: Int
)

internal enum class VocabularyDrillStage(val title: String, val instruction: String) {
    LEARN("Seznámení se slovíčky", "Nejprve si spoj německé slovo, člen, český význam a výslovnost."),
    MEANING("Vybav si český význam", "Přečti německý výraz a bez nabídky odpovědí napiš jeho český význam."),
    WRITE("Napiš německý výraz", "Vybav si německé podstatné jméno z češtiny. Nezapomeň na správný člen."),
    LISTENING("Poznej slovo podle poslechu", "Poslechni si německou větu a zapiš podstatné jméno včetně členu."),
    FINISHED("Procvičování dokončeno", "Slovíčka se podle výsledků zařadila do dlouhodobého opakování.")
}

internal fun vocabularyDrillReviewId(entry: VocabularyEntry): String =
    stableReviewId("vocabulary", entry.id)

internal fun makeVocabularyDrillQuestion(
    entry: VocabularyEntry,
    allEntries: List<VocabularyEntry> = vocabularyCatalog,
    random: Random = Random.Default
): VocabularyDrillQuestion {
    val correct = entry.czech.trim()
    val distractors = allEntries.asSequence()
        .filter { it.id != entry.id }
        .map { it.czech.trim() }
        .filter { it.isNotBlank() && !it.equals(correct, ignoreCase = true) }
        .distinctBy { it.lowercase() }
        .toList()
        .shuffled(random)
        .take(2)
    check(distractors.size == 2) {
        "Vocabulary drill for ${entry.word} needs two distinct Czech distractors."
    }
    val choices = (distractors + correct).shuffled(random)
    return VocabularyDrillQuestion(
        entry = entry,
        choices = choices,
        correctIndex = choices.indexOf(correct)
    )
}

internal fun buildVocabularyDrillBatch(
    count: Int = 5,
    random: Random = Random.Default
): List<VocabularyDrillQuestion> {
    if (count <= 0) return emptyList()
    val selectedEntries = vocabularyCatalog
        .distinctBy { it.id }
        .shuffled(random)
        .take(count.coerceAtMost(vocabularyCatalog.size))
    return selectedEntries.map { entry ->
        makeVocabularyDrillQuestion(entry, vocabularyCatalog, random)
    }
}

/**
 * A session mixes at most two due words with new material. If the catalogue
 * has already been covered, weakly retained words are selected before mastered ones.
 */
internal fun selectVocabularyDrillEntries(
    entries: List<VocabularyEntry>,
    reviewEntries: List<ReviewMemoryEntry>,
    count: Int = 5,
    nowMillis: Long = System.currentTimeMillis(),
    random: Random = Random.Default
): List<VocabularyEntry> {
    if (count <= 0 || entries.isEmpty()) return emptyList()
    val uniqueEntries = entries.distinctBy { it.id }
    val byReviewId = reviewEntries
        .filter { it.question.sourceKind == "vocabulary" }
        .associateBy { it.question.id }
    val due = reviewEntries
        .filter { it.question.sourceKind == "vocabulary" && it.dueAtMillis <= nowMillis }
        .sortedWith(compareByDescending<ReviewMemoryEntry> { it.wrongCount }.thenBy { it.dueAtMillis })
        .mapNotNull { review -> uniqueEntries.firstOrNull { vocabularyDrillReviewId(it) == review.question.id } }
        .distinctBy { it.id }
        .take(minOf(2, count))
    val selectedIds = due.map { it.id }.toSet()
    val newEntries = uniqueEntries
        .filter { it.id !in selectedIds && vocabularyDrillReviewId(it) !in byReviewId }
        .shuffled(random)
    val weakEntries = uniqueEntries
        .filter { entry ->
            val history = byReviewId[vocabularyDrillReviewId(entry)]
            entry.id !in selectedIds && history != null &&
                (history.wrongCount > 0 || history.correctStreak < 3)
        }
        .sortedWith(
            compareByDescending<VocabularyEntry> { byReviewId[vocabularyDrillReviewId(it)]?.wrongCount ?: 0 }
                .thenBy { byReviewId[vocabularyDrillReviewId(it)]?.correctStreak ?: 0 }
        )
    val masteredOrOther = uniqueEntries.filter {
        it.id !in selectedIds &&
            it !in newEntries &&
            it !in weakEntries
    }.shuffled(random)

    return (due + newEntries + weakEntries + masteredOrOther)
        .distinctBy { it.id }
        .take(count)
}

internal fun makeVocabularyDrillReviewQuestion(
    entry: VocabularyEntry,
    allEntries: List<VocabularyEntry> = vocabularyCatalog,
    random: Random = Random.Default
): ReviewQuestion {
    val correct = "${entry.article} ${entry.word}"
    val distractors = allEntries.asSequence()
        .filter { it.id != entry.id }
        .map { "${it.article} ${it.word}" }
        .filter { it.isNotBlank() && !it.equals(correct, ignoreCase = true) }
        .distinctBy { it.lowercase() }
        .toList()
        .shuffled(random)
        .take(3)
    check(distractors.size == 3) {
        "Vocabulary review for ${entry.word} needs three distinct German distractors."
    }
    val choices = (distractors + correct).shuffled(random)
    return ReviewQuestion(
        id = vocabularyDrillReviewId(entry),
        sourceKind = "vocabulary",
        sourceTitle = "Dril slovní zásoby",
        prompt = "Jak se německy řekne „${entry.czech}“? Napiš i člen.",
        choices = choices,
        correctIndex = choices.indexOf(correct),
        explanation = "${entry.article} ${entry.word} = ${entry.czech}. Podstatné jméno se uč vždy společně se členem.",
        germanSentence = vocabularySentenceGerman(entry),
        translation = vocabularySentenceCzech(entry)
    )
}

internal fun normalizeVocabularyDrillAnswer(value: String): String =
    value.trim()
        .lowercase()
        .replace(Regex("\\s+"), " ")
        .trimEnd('.', ',', ';', ':', '!', '?')

internal fun isVocabularyDrillMeaningCorrect(answer: String, expected: String): Boolean {
    val normalized = normalizeVocabularyDrillAnswer(answer)
    if (normalized.isBlank()) return false
    val accepted = expected.split(Regex("\\s*(?:/|;|\\bor\\b)\\s*"))
        .map(::normalizeVocabularyDrillAnswer)
        .filter(String::isNotBlank)
    return normalized in accepted
}

private fun buildSessionEntries(context: Context): List<VocabularyEntry> =
    selectVocabularyDrillEntries(
        entries = VocabularyPreferenceStore.available(context),
        reviewEntries = LearningReviewStore.all(context),
        count = 5
    )

@Composable
internal fun VocabularyDrillScreen(
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    var entries by remember { mutableStateOf(buildSessionEntries(context)) }
    var stage by remember { mutableStateOf(VocabularyDrillStage.LEARN) }
    var questionIndex by remember { mutableStateOf(0) }
    var stageOrders by remember(entries) {
        mutableStateOf(List(3) { entries.indices.shuffled() })
    }
    var answer by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    var answerWasCorrect by remember { mutableStateOf(false) }
    var stageResults by remember { mutableStateOf<Map<String, Map<VocabularyDrillStage, Boolean>>>(emptyMap()) }
    var score by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    val currentEntry = when (stage) {
        VocabularyDrillStage.LEARN -> entries.getOrNull(questionIndex)
        VocabularyDrillStage.MEANING -> stageOrders.getOrNull(0)?.getOrNull(questionIndex)?.let(entries::getOrNull)
        VocabularyDrillStage.WRITE -> stageOrders.getOrNull(1)?.getOrNull(questionIndex)?.let(entries::getOrNull)
        VocabularyDrillStage.LISTENING -> stageOrders.getOrNull(2)?.getOrNull(questionIndex)?.let(entries::getOrNull)
        VocabularyDrillStage.FINISHED -> null
    }

    fun resetQuestion() {
        answer = ""
        checked = false
        answerWasCorrect = false
    }

    fun startNewDrill() {
        entries = buildSessionEntries(context)
        stage = VocabularyDrillStage.LEARN
        questionIndex = 0
        stageResults = emptyMap()
        score = 0
        finished = false
        resetQuestion()
    }

    fun advance() {
        if (questionIndex < entries.lastIndex) {
            questionIndex += 1
            resetQuestion()
            return
        }
        when (stage) {
            VocabularyDrillStage.LEARN -> stage = VocabularyDrillStage.MEANING
            VocabularyDrillStage.MEANING -> stage = VocabularyDrillStage.WRITE
            VocabularyDrillStage.WRITE -> stage = VocabularyDrillStage.LISTENING
            VocabularyDrillStage.LISTENING -> {
                stage = VocabularyDrillStage.FINISHED
                finished = true
                CourseResultStore.save(context, "vocabulary_drill", score, entries.size * 3)
                onComplete(score, entries.size * 3)
            }
            VocabularyDrillStage.FINISHED -> Unit
        }
        questionIndex = 0
        resetQuestion()
    }

    fun checkAnswer() {
        val entry = currentEntry ?: return
        if (checked || answer.isBlank()) return
        val expected = when (stage) {
            VocabularyDrillStage.MEANING -> entry.czech
            VocabularyDrillStage.WRITE, VocabularyDrillStage.LISTENING -> "${entry.article} ${entry.word}"
            else -> return
        }
        answerWasCorrect = if (stage == VocabularyDrillStage.MEANING) {
            isVocabularyDrillMeaningCorrect(answer, expected)
        } else {
            normalizeVocabularyDrillAnswer(answer) == normalizeVocabularyDrillAnswer(expected)
        }
        if (answerWasCorrect) score += 1
        checked = true
        val updatedForEntry = stageResults[entry.id].orEmpty() + (stage to answerWasCorrect)
        stageResults = stageResults + (entry.id to updatedForEntry)

        if (stage == VocabularyDrillStage.LISTENING) {
            val masteredThisSession = listOf(
                VocabularyDrillStage.MEANING,
                VocabularyDrillStage.WRITE,
                VocabularyDrillStage.LISTENING
            ).all { updatedForEntry[it] == true }
            LearningReviewStore.recordAnswer(
                context = context,
                question = makeVocabularyDrillReviewQuestion(entry),
                wasCorrect = masteredThisSession
            )
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AppScreenHeader(
                title = "Dril slovní zásoby",
                onBack = onBack,
                onHome = onHome
            )

            if (entries.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Teď tu nejsou dostupná slovíčka.", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Zkontroluj, jestli sis v části Slovní zásoba neoznačil/a všechna slova jako přeskočená.",
                            color = Muted, fontSize = 14.sp, lineHeight = 21.sp
                        )
                    }
                }
            } else if (stage == VocabularyDrillStage.FINISHED || finished) {
                val fullyKnown = entries.count { entry ->
                    listOf(
                        VocabularyDrillStage.MEANING,
                        VocabularyDrillStage.WRITE,
                        VocabularyDrillStage.LISTENING
                    ).all { stageResults[entry.id]?.get(it) == true }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(23.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Slovíčka procvičena", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "$fullyKnown z ${entries.size}",
                            color = Gold,
                            fontSize = 31.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "slovíček zvládnuto ve všech třech úlohách",
                            color = Color(0xFFE0EAE5),
                            fontSize = 14.sp
                        )
                        Text(
                            "$score z ${entries.size * 3} správných odpovědí",
                            color = Color(0xFFE0EAE5),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Slova, která ještě nejdou jistě, se vrátí dříve. Ta zvládnutá budou mít delší rozestupy.",
                            color = Color(0xFFE0EAE5),
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
                entries.forEach { entry ->
                    val mastered = listOf(
                        VocabularyDrillStage.MEANING,
                        VocabularyDrillStage.WRITE,
                        VocabularyDrillStage.LISTENING
                    ).all { stageResults[entry.id]?.get(it) == true }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text("${entry.article} ${entry.word}", color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(entry.czech, color = Muted, fontSize = 13.sp)
                            }
                            Text(
                                if (mastered) "Zvládnuto" else "Vrátíme se k němu",
                                color = if (mastered) ForestText else Gold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Button(
                    onClick = { startNewDrill() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Procvičit další slovíčka", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zpět do knihovny", fontWeight = FontWeight.Bold) }
            } else if (currentEntry != null) {
                val stageNumber = when (stage) {
                    VocabularyDrillStage.LEARN -> 1
                    VocabularyDrillStage.MEANING -> 2
                    VocabularyDrillStage.WRITE -> 3
                    VocabularyDrillStage.LISTENING -> 4
                    VocabularyDrillStage.FINISHED -> 4
                }
                Text(
                    "FÁZE $stageNumber ZE 4",
                    color = ForestText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / entries.size.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                    color = ForestText,
                    trackColor = Mint
                )
                Text(stage.title, color = Ink, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(stage.instruction, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Slovíčko ${questionIndex + 1} z ${entries.size}", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(
                        when (stage) {
                            VocabularyDrillStage.LEARN -> "Seznámení"
                            VocabularyDrillStage.MEANING -> "Význam"
                            VocabularyDrillStage.WRITE -> "Psaní"
                            VocabularyDrillStage.LISTENING -> "Poslech"
                            VocabularyDrillStage.FINISHED -> ""
                        },
                        color = Muted,
                        fontSize = 12.sp
                    )
                }

                when (stage) {
                    VocabularyDrillStage.LEARN -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(22.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("NĚMECKY", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                Text("${currentEntry.article} ${currentEntry.word}", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                                Text(currentEntry.czech, color = Muted, fontSize = 18.sp)
                                Text("Ve větě", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(vocabularySentenceGerman(currentEntry), color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                GermanAudioControls(player = speaker, text = vocabularySentenceGerman(currentEntry), showSlow = true)
                                Text(vocabularySentenceCzech(currentEntry), color = Muted, fontSize = 13.sp)
                            }
                        }
                        Button(
                            onClick = { advance() },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(if (questionIndex == entries.lastIndex) "Vyzkoušet si slovíčka" else "Další slovíčko", fontWeight = FontWeight.Bold)
                        }
                    }

                    VocabularyDrillStage.MEANING,
                    VocabularyDrillStage.WRITE,
                    VocabularyDrillStage.LISTENING -> {
                        val expectedGerman = "${currentEntry.article} ${currentEntry.word}"
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(21.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                when (stage) {
                                    VocabularyDrillStage.MEANING -> {
                                        Text("NĚMECKÝ VÝRAZ", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(expectedGerman, color = Ink, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                                    }
                                    VocabularyDrillStage.WRITE -> {
                                        Text("ČESKÝ VÝZNAM", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(currentEntry.czech, color = Ink, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                                    }
                                    VocabularyDrillStage.LISTENING -> {
                                        Text("POSLECHNI SI VĚTU", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("Zapiš německé podstatné jméno včetně členu.", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                        GermanAudioControls(player = speaker, text = vocabularySentenceGerman(currentEntry), showSlow = true)
                                    }
                                    else -> Unit
                                }
                            }
                        }
                        OutlinedTextField(
                            value = answer,
                            onValueChange = { if (!checked) answer = it },
                            enabled = !checked,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = {
                                Text(
                                    when (stage) {
                                        VocabularyDrillStage.MEANING -> "Český význam"
                                        VocabularyDrillStage.WRITE, VocabularyDrillStage.LISTENING -> "Německý výraz včetně členu"
                                        else -> "Odpověď"
                                    }
                                )
                            },
                            placeholder = {
                                Text(
                                    when (stage) {
                                        VocabularyDrillStage.MEANING -> "např. pes"
                                        VocabularyDrillStage.WRITE, VocabularyDrillStage.LISTENING -> "např. der Hund"
                                        else -> ""
                                    }
                                )
                            }
                        )
                        if (!checked) {
                            Button(
                                onClick = { checkAnswer() },
                                enabled = answer.isNotBlank(),
                                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                                shape = RoundedCornerShape(14.dp)
                            ) { Text("Zkontrolovat odpověď", fontWeight = FontWeight.Bold) }
                        } else {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(17.dp),
                                colors = CardDefaults.cardColors(containerColor = if (answerWasCorrect) Correct else Incorrect)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        if (answerWasCorrect) "Správně" else when (stage) {
                                            VocabularyDrillStage.MEANING -> "Správný význam: ${currentEntry.czech}"
                                            else -> "Správný zápis: $expectedGerman"
                                        },
                                        color = ForestText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(vocabularySentenceGerman(currentEntry), color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                    Text(vocabularySentenceCzech(currentEntry), color = Muted, fontSize = 13.sp)
                                    GermanAudioControls(player = speaker, text = vocabularySentenceGerman(currentEntry))
                                    if (stage != VocabularyDrillStage.MEANING) {
                                        Text("Důležité: podstatné jméno si ukládej společně se členem.", color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
                                    }
                                }
                            }
                            Button(
                                onClick = { advance() },
                                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    when (stage) {
                                        VocabularyDrillStage.MEANING -> if (questionIndex == entries.lastIndex) "Pokračovat k psaní" else "Další slovíčko"
                                        VocabularyDrillStage.WRITE -> if (questionIndex == entries.lastIndex) "Pokračovat k poslechu" else "Další slovíčko"
                                        VocabularyDrillStage.LISTENING -> if (questionIndex == entries.lastIndex) "Zobrazit výsledek" else "Další slovíčko"
                                        else -> "Pokračovat"
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    VocabularyDrillStage.FINISHED -> Unit
                }
            }
        }
    }
}
