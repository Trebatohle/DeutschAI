package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

internal enum class FrequencyDrillStage(val title: String, val instruction: String) {
    LEARN("Seznam se se slovem", "Přečti si význam a větu. Poslechni si přirozenou výslovnost."),
    MEANING("Vybav si český význam", "Bez nabídky odpovědí napiš, co německý výraz znamená."),
    WRITE("Napiš německý výraz", "Z českého významu si vybav německý základní tvar."),
    LISTENING("Poznej výraz podle poslechu", "Poslechni si větu a napiš procvičovaný německý výraz."),
    FINISHED("Procvičování dokončeno", "Slova byla zařazena do tvého dlouhodobého opakování.")
}

internal fun frequencyVocabularyReviewQuestion(
    entry: FrequencyVocabularyEntry,
    allEntries: List<FrequencyVocabularyEntry> = frequencyVocabularyCatalog,
    random: Random = Random(entry.id.hashCode())
): ReviewQuestion {
    val correct = entry.germanAnswer
    val distractors = allEntries.asSequence()
        .filter { it.id != entry.id && it.kind == entry.kind }
        .map { it.germanAnswer }
        .filter { it.isNotBlank() && !it.equals(correct, ignoreCase = true) }
        .distinctBy { it.lowercase() }
        .shuffled(random)
        .take(3)
        .toMutableList()
    if (distractors.size < 3) {
        distractors += allEntries.asSequence()
            .filter { it.id != entry.id && it.kind != entry.kind }
            .map { it.germanAnswer }
            .filter { it.isNotBlank() && !it.equals(correct, ignoreCase = true) }
            .filterNot { candidate -> distractors.any { it.equals(candidate, ignoreCase = true) } }
            .distinctBy { it.lowercase() }
            .shuffled(random)
            .take(3 - distractors.size)
    }
    check(distractors.size == 3) { "Frequency vocabulary needs three distinct review distractors for " + entry.word }
    val choices = (distractors + correct).shuffled(random)
    val sourceTitle = if (entry.kind == FrequencyWordKind.VERB) "Frekvenční slovesa " + entry.level
        else "Frekvenční slovní zásoba " + entry.level
    val details = entry.extraInfo.takeIf { it.isNotBlank() }?.let { " Důležité tvary: " + it + "." }.orEmpty()
    return ReviewQuestion(
        id = entry.id,
        sourceKind = "vocabulary",
        sourceTitle = sourceTitle,
        prompt = "Jak se německy řekne „" + entry.czech + "“? Napiš základní tvar.",
        choices = choices,
        correctIndex = choices.indexOf(correct),
        explanation = correct + " = " + entry.czech + "." + details,
        germanSentence = entry.exampleGerman,
        translation = entry.exampleCzech,
        cefrLevel = entry.level
    )
}

/** Due items first; A1/A2 reserve at least 60% of fresh slots for verbs. */
internal fun selectFrequencyVocabularyEntries(
    level: String,
    verbsOnly: Boolean,
    count: Int,
    reviewEntries: List<ReviewMemoryEntry>,
    nowMillis: Long = System.currentTimeMillis(),
    random: Random = Random.Default
): List<FrequencyVocabularyEntry> {
    if (count <= 0) return emptyList()
    val pool = frequencyVocabularyForLevel(level, verbsOnly).distinctBy { it.id }
    if (pool.isEmpty()) return emptyList()
    val history = reviewEntries.filter { it.question.sourceKind == "vocabulary" }
        .associateBy { it.question.id }
    val due = reviewEntries
        .filter { it.question.sourceKind == "vocabulary" && it.dueAtMillis <= nowMillis }
        .sortedWith(compareByDescending<ReviewMemoryEntry> { it.wrongCount }.thenBy { it.dueAtMillis })
        .mapNotNull { review -> pool.firstOrNull { it.id == review.question.id } }
        .distinctBy { it.id }.take(minOf(2, count))
    val selectedIds = due.map { it.id }.toSet()
    val fresh = pool.filter { it.id !in selectedIds && it.id !in history }
    val weak = pool.filter { entry ->
        val memory = history[entry.id]
        entry.id !in selectedIds && memory != null && (memory.wrongCount > 0 || memory.correctStreak < 3)
    }.sortedWith(
        compareByDescending<FrequencyVocabularyEntry> { history[it.id]?.wrongCount ?: 0 }
            .thenBy { history[it.id]?.correctStreak ?: 0 }
    )
    val mastered = pool.filter { it.id !in selectedIds && it !in fresh && it !in weak }.shuffled(random)
    val target = count.coerceAtMost(pool.size)
    val selected = due.toMutableList()
    if (verbsOnly) {
        // The dedicated verb library must fill the whole session with verbs.
        selected += fresh.shuffled(random).take((target - selected.size).coerceAtLeast(0))
    } else {
        val verbQuota = if (level in setOf("A1", "A2")) (target * 7 + 9) / 10 else 0
        val alreadySelectedVerbs = selected.count { it.kind == FrequencyWordKind.VERB }
        val verbsToAdd = minOf(
            (verbQuota - alreadySelectedVerbs).coerceAtLeast(0),
            (target - selected.size).coerceAtLeast(0)
        )
        selected += fresh.filter { it.kind == FrequencyWordKind.VERB }.shuffled(random).take(verbsToAdd)
        val freshNonVerbs = fresh.filterNot { it.kind == FrequencyWordKind.VERB }
            .filterNot { candidate -> selected.any { it.id == candidate.id } }
        if (verbQuota > 0) {
            val functionWordPriority = mapOf(
                FrequencyWordKind.PRONOUN to 0,
                FrequencyWordKind.QUESTION to 1,
                FrequencyWordKind.PREPOSITION to 2,
                FrequencyWordKind.ARTICLE to 3,
                FrequencyWordKind.CONJUNCTION to 4,
                FrequencyWordKind.PARTICLE to 5,
                FrequencyWordKind.FUNCTION to 6,
                FrequencyWordKind.NOUN to 7,
                FrequencyWordKind.ADJECTIVE to 8,
                FrequencyWordKind.ADVERB to 9
            )
            selected += freshNonVerbs.shuffled(random).sortedBy { functionWordPriority[it.kind] ?: 10 }
        } else {
            selected += freshNonVerbs.shuffled(random)
        }
    }
    selected += weak.filterNot { candidate -> selected.any { it.id == candidate.id } }
    selected += mastered.filterNot { candidate -> selected.any { it.id == candidate.id } }
    return selected.distinctBy { it.id }.take(target)
}

internal data class FrequencyVocabularyProgress(
    val totalWords: Int,
    val practicedWords: Int,
    val successfullyPassedWords: Int,
    val longTermMasteredWords: Int,
    val dueWords: Int
)

/**
 * Separates a first perfect drill pass from long-term mastery.
 * A word is "successfully passed" after all three drill stages were correct once;
 * long-term mastery still requires three successful spaced repetitions.
 */
internal object FrequencyVocabularyProgressStore {
    private const val PREFS = "deutschai_frequency_vocabulary_progress"
    private const val PERFECTLY_PASSED_IDS = "perfectly_passed_word_ids_v1"

    fun perfectlyPassedIds(context: Context): Set<String> =
        context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getStringSet(PERFECTLY_PASSED_IDS, emptySet())
            ?.toSet()
            .orEmpty()

    fun recordPerfectDrillWord(context: Context, entry: FrequencyVocabularyEntry) {
        val preferences = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val updated = (preferences.getStringSet(PERFECTLY_PASSED_IDS, emptySet()) ?: emptySet())
            .toMutableSet()
            .apply { add(entry.id) }
        preferences.edit().putStringSet(PERFECTLY_PASSED_IDS, updated).apply()
    }
}

internal fun buildFrequencyVocabularyProgress(
    words: List<FrequencyVocabularyEntry>,
    reviewEntries: List<ReviewMemoryEntry>,
    perfectlyPassedIds: Set<String> = emptySet(),
    nowMillis: Long = System.currentTimeMillis()
): FrequencyVocabularyProgress {
    val uniqueWords = words.distinctBy { it.id }
    val history = reviewEntries
        .filter { it.question.sourceKind == "vocabulary" }
        .associateBy { it.question.id }

    return FrequencyVocabularyProgress(
        totalWords = uniqueWords.size,
        practicedWords = uniqueWords.count { (history[it.id]?.attemptCount ?: 0) > 0 },
        successfullyPassedWords = uniqueWords.count { it.id in perfectlyPassedIds },
        longTermMasteredWords = uniqueWords.count { word ->
            val memory = history[word.id]
            memory != null && memory.correctStreak >= 3 && memory.intervalDays >= 7
        },
        dueWords = uniqueWords.count { word ->
            history[word.id]?.dueAtMillis?.let { it <= nowMillis } == true
        }
    )
}

@Composable
internal fun FrequencyVocabularyDrillScreen(
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit,
    verbsOnly: Boolean = false,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val speaker = rememberGermanSpeechPlayer()
    var selectedLevel by remember(verbsOnly) { mutableStateOf("A1") }
    var selectedCount by remember { mutableStateOf(5) }
    var entries by remember { mutableStateOf(emptyList<FrequencyVocabularyEntry>()) }
    var started by remember { mutableStateOf(false) }
    var stage by remember { mutableStateOf(FrequencyDrillStage.LEARN) }
    var questionIndex by remember { mutableStateOf(0) }
    var stageOrders by remember { mutableStateOf(emptyList<List<Int>>()) }
    var answer by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    var answerWasCorrect by remember { mutableStateOf(false) }
    var stageResults by remember { mutableStateOf<Map<String, Map<FrequencyDrillStage, Boolean>>>(emptyMap()) }
    var score by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    val currentEntry = when (stage) {
        FrequencyDrillStage.LEARN -> entries.getOrNull(questionIndex)
        FrequencyDrillStage.MEANING -> stageOrders.getOrNull(0)?.getOrNull(questionIndex)?.let(entries::getOrNull)
        FrequencyDrillStage.WRITE -> stageOrders.getOrNull(1)?.getOrNull(questionIndex)?.let(entries::getOrNull)
        FrequencyDrillStage.LISTENING -> stageOrders.getOrNull(2)?.getOrNull(questionIndex)?.let(entries::getOrNull)
        FrequencyDrillStage.FINISHED -> null
    }

    fun resetAnswer() {
        answer = ""
        checked = false
        answerWasCorrect = false
    }

    fun startDrill() {
        val selected = selectFrequencyVocabularyEntries(
            selectedLevel, verbsOnly, selectedCount, LearningReviewStore.all(context)
        )
        entries = selected.shuffled()
        stageOrders = List(3) { entries.indices.shuffled() }
        questionIndex = 0
        stage = FrequencyDrillStage.LEARN
        stageResults = emptyMap()
        score = 0
        finished = false
        started = true
        resetAnswer()
    }

    fun advance() {
        focusManager.clearFocus()
        if (questionIndex < entries.lastIndex) {
            questionIndex += 1
            resetAnswer()
            return
        }
        when (stage) {
            FrequencyDrillStage.LEARN -> stage = FrequencyDrillStage.MEANING
            FrequencyDrillStage.MEANING -> stage = FrequencyDrillStage.WRITE
            FrequencyDrillStage.WRITE -> stage = FrequencyDrillStage.LISTENING
            FrequencyDrillStage.LISTENING -> {
                // Persist the completed set once more at the session boundary. This makes
                // the 100% counter robust even if the learner leaves immediately after
                // the last answer or the screen is recreated.
                entries.forEach { entry ->
                    val mastered = listOf(
                        FrequencyDrillStage.MEANING, FrequencyDrillStage.WRITE, FrequencyDrillStage.LISTENING
                    ).all { stageResults[entry.id]?.get(it) == true }
                    if (mastered) FrequencyVocabularyProgressStore.recordPerfectDrillWord(context, entry)
                }
                stage = FrequencyDrillStage.FINISHED
                finished = true
                CourseResultStore.save(context, "frequency_vocabulary_" + selectedLevel, score, entries.size * 3)
                onComplete(score, entries.size * 3)
            }
            FrequencyDrillStage.FINISHED -> Unit
        }
        questionIndex = 0
        resetAnswer()
    }

    fun checkAnswer() {
        val entry = currentEntry ?: return
        if (checked || answer.isBlank()) return
        val expected = when (stage) {
            FrequencyDrillStage.MEANING -> entry.czech
            FrequencyDrillStage.WRITE -> entry.germanAnswer
            FrequencyDrillStage.LISTENING -> entry.listeningTarget
            else -> return
        }
        answerWasCorrect = if (stage == FrequencyDrillStage.MEANING) {
            isVocabularyDrillMeaningCorrect(answer, expected)
        } else {
            normalizeVocabularyDrillAnswer(answer) == normalizeVocabularyDrillAnswer(expected)
        }
        if (answerWasCorrect) score += 1
        checked = true
        val updatedForEntry = stageResults[entry.id].orEmpty() + (stage to answerWasCorrect)
        stageResults = stageResults + (entry.id to updatedForEntry)
        if (stage == FrequencyDrillStage.LISTENING) {
            val mastered = listOf(
                FrequencyDrillStage.MEANING, FrequencyDrillStage.WRITE, FrequencyDrillStage.LISTENING
            ).all { updatedForEntry[it] == true }
            if (mastered) {
                FrequencyVocabularyProgressStore.recordPerfectDrillWord(context, entry)
            }
            LearningReviewStore.recordAnswer(context, frequencyVocabularyReviewQuestion(entry), mastered)
        }
        focusManager.clearFocus()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        BoxWithConstraints(Modifier.fillMaxSize().navigationBarsPadding().imePadding()) {
            val compact = maxHeight < 600.dp
            Column(
                Modifier.fillMaxSize().padding(horizontal = if (compact) 14.dp else 20.dp, vertical = if (compact) 5.dp else 12.dp),
                verticalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppScreenHeader(
                    title = if (verbsOnly) "Dril sloves" else "Dril slovní zásoby",
                    onBack = onBack, onHome = onHome
                )
                if (!started) {
                    Text(
                        if (verbsOnly) "Procvič běžná slovesa v němčině" else "Slova pro každodenní mluvu",
                        color = Ink, fontSize = if (compact) 18.sp else 23.sp,
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                    )
                    if (!compact) Text(
                        if (verbsOnly) "Význam, tvary, běžné věty a výslovnost."
                        else "V A1 a A2 dostávají přednost nejdůležitější slovesa.",
                        color = Muted, fontSize = 13.sp, textAlign = TextAlign.Center
                    )
                    Text("ÚROVEŇ", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    listOf(listOf("A1", "A2"), listOf("B1", "B2")).forEach { levels ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                            levels.forEach { level ->
                                val chosen = selectedLevel == level
                                Card(
                                    onClick = { selectedLevel = level },
                                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = if (chosen) Forest else CardSurface)
                                ) {
                                    Column(
                                        Modifier.fillMaxWidth().padding(vertical = if (compact) 9.dp else 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(level, color = if (chosen) Color.White else Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            when (level) {
                                                "A1" -> "Začátečník"
                                                "A2" -> "Mírně pokročilý"
                                                "B1" -> "Středně pokročilý"
                                                else -> "Pokročilejší"
                                            },
                                            color = if (chosen) Color(0xFFE0EAE5) else Muted, fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Text("POČET SLOV", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        listOf(5, 10).forEach { count ->
                            val chosen = selectedCount == count
                            Card(
                                onClick = { selectedCount = count },
                                modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = if (chosen) Mint else CardSurface)
                            ) {
                                Column(
                                    Modifier.fillMaxWidth().padding(vertical = if (compact) 9.dp else 11.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(count.toString() + " slov", color = if (chosen) ForestText else Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                    Text(if (count == 5) "Krátký trénink" else "Delší trénink", color = Muted, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                    val levelWords = frequencyVocabularyForLevel(selectedLevel, verbsOnly)
                    val levelProgress = buildFrequencyVocabularyProgress(
                        words = levelWords,
                        reviewEntries = LearningReviewStore.all(context),
                        perfectlyPassedIds = FrequencyVocabularyProgressStore.perfectlyPassedIds(context)
                    )
                    Text(
                        "Úspěšně zvládnuto na 100 %: " +
                            levelProgress.successfullyPassedWords + " / " + levelProgress.totalWords,
                        color = ForestText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Procvičeno " + levelProgress.practicedWords +
                            " · dlouhodobě upevněno " + levelProgress.longTermMasteredWords +
                            " · k opakování " + levelProgress.dueWords,
                        color = Muted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = { startDrill() }, modifier = Modifier.fillMaxWidth().heightIn(min = if (compact) 48.dp else 54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text("Spustit dril", fontWeight = FontWeight.Bold) }
                } else if (finished || stage == FrequencyDrillStage.FINISHED) {
                    val mastered = entries.count { entry ->
                        listOf(FrequencyDrillStage.MEANING, FrequencyDrillStage.WRITE, FrequencyDrillStage.LISTENING)
                            .all { stageResults[entry.id]?.get(it) == true }
                    }
                    Spacer(Modifier.weight(1f))
                    Card(
                        Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Forest)
                    ) {
                        Column(
                            Modifier.fillMaxWidth().padding(if (compact) 17.dp else 23.dp),
                            verticalArrangement = Arrangement.spacedBy(if (compact) 7.dp else 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Sada dokončena", color = Color.White, fontSize = if (compact) 20.sp else 24.sp, fontWeight = FontWeight.Bold)
                            Text(mastered.toString() + " z " + entries.size, color = Gold, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                            Text("slov zvládnuto ve všech třech úlohách", color = Color(0xFFE0EAE5), fontSize = 14.sp, textAlign = TextAlign.Center)
                            Text(score.toString() + " z " + (entries.size * 3) + " správných odpovědí", color = Color(0xFFE0EAE5), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            val overallProgress = buildFrequencyVocabularyProgress(
                                words = frequencyVocabularyForLevel(selectedLevel, verbsOnly),
                                reviewEntries = LearningReviewStore.all(context),
                                perfectlyPassedIds = FrequencyVocabularyProgressStore.perfectlyPassedIds(context)
                            )
                            Text(
                                "Celkem na úrovni $selectedLevel zvládnuto na 100 %: " +
                                    overallProgress.successfullyPassedWords + " / " + overallProgress.totalWords,
                                color = Gold, fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                            )
                            Text("Nejistá slova se vrátí dříve. Zvládnutá se budou opakovat s delšími rozestupy.", color = Color(0xFFE0EAE5), fontSize = 14.sp, lineHeight = 20.sp, textAlign = TextAlign.Center)
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = { startDrill() }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text("Další sada", fontWeight = FontWeight.Bold) }
                    Button(
                        onClick = { started = false; finished = false }, modifier = Modifier.fillMaxWidth().heightIn(min = 44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text("Změnit úroveň nebo délku", fontWeight = FontWeight.Bold) }
                } else if (currentEntry != null) {
                    val stageNumber = when (stage) {
                        FrequencyDrillStage.LEARN -> 1
                        FrequencyDrillStage.MEANING -> 2
                        FrequencyDrillStage.WRITE -> 3
                        FrequencyDrillStage.LISTENING -> 4
                        FrequencyDrillStage.FINISHED -> 4
                    }
                    Text("FÁZE " + stageNumber + " ZE 4", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    LinearProgressIndicator(
                        progress = { (questionIndex + 1).toFloat() / entries.size.toFloat() },
                        modifier = Modifier.fillMaxWidth(), color = ForestText, trackColor = Mint
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text((questionIndex + 1).toString() + " / " + entries.size + " slov", color = ForestText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(stage.title, color = Ink, fontSize = if (compact) 16.sp else 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(stage.instruction, color = Muted, fontSize = 14.sp, textAlign = TextAlign.Center)
                    when (stage) {
                        FrequencyDrillStage.LEARN -> {
                            Spacer(Modifier.weight(1f, fill = false))
                            Card(
                                Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = CardSurface)
                            ) {
                                Column(
                                    Modifier.fillMaxWidth().padding(if (compact) 13.dp else 18.dp),
                                    verticalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(currentEntry.kind.label, color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                    Text(currentEntry.germanAnswer, color = Ink, fontSize = if (compact) 29.sp else 33.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                    Text(currentEntry.czech, color = Muted, fontSize = if (compact) 18.sp else 20.sp, textAlign = TextAlign.Center)
                                    if (currentEntry.extraInfo.isNotBlank()) Text(currentEntry.extraInfo, color = ForestText, fontSize = 14.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                                    Text(currentEntry.exampleGerman, color = Ink, fontSize = if (compact) 17.sp else 19.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                                    if (!compact) Text(currentEntry.exampleCzech, color = Muted, fontSize = 14.sp, textAlign = TextAlign.Center)
                                    currentEntry.contextCourseTitle?.let { sourceTitle ->
                                        Text("Věta z kurzu: $sourceTitle", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                                    }
                                    GermanAudioControls(player = speaker, text = currentEntry.exampleGerman, showSlow = true)
                                }
                            }
                            Spacer(Modifier.weight(1f, fill = false))
                            Button(
                                onClick = { advance() }, modifier = Modifier.fillMaxWidth().heightIn(min = if (compact) 46.dp else 52.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                                shape = RoundedCornerShape(14.dp)
                            ) { Text(if (questionIndex == entries.lastIndex) "Vyzkoušet si slovíčka" else "Další slovíčko", fontWeight = FontWeight.Bold) }
                        }
                        FrequencyDrillStage.MEANING, FrequencyDrillStage.WRITE, FrequencyDrillStage.LISTENING -> {
                            Spacer(Modifier.weight(1f, fill = false))
                            Card(
                                Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = CardSurface)
                            ) {
                                Column(
                                    Modifier.fillMaxWidth().padding(if (compact) 13.dp else 18.dp),
                                    verticalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    when (stage) {
                                        FrequencyDrillStage.MEANING -> {
                                            Text("NĚMECKÝ VÝRAZ", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            Text(currentEntry.germanAnswer, color = Ink, fontSize = if (compact) 29.sp else 32.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                        }
                                        FrequencyDrillStage.WRITE -> {
                                            Text("ČESKÝ VÝZNAM", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            Text(currentEntry.czech, color = Ink, fontSize = if (compact) 25.sp else 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                        }
                                        FrequencyDrillStage.LISTENING -> {
                                            Text("POSLECHNI SI VĚTU", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            GermanAudioControls(player = speaker, text = currentEntry.exampleGerman, showSlow = true)
                                        }
                                        else -> Unit
                                    }
                                }
                            }
                            OutlinedTextField(
                                value = answer, onValueChange = { if (!checked) answer = it }, enabled = !checked,
                                modifier = Modifier.fillMaxWidth(), singleLine = true,
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                                label = {
                                    Text(when (stage) {
                                        FrequencyDrillStage.MEANING -> "Český význam"
                                        FrequencyDrillStage.WRITE -> "Německý základní tvar"
                                        FrequencyDrillStage.LISTENING -> "Co jsi slyšel/a?"
                                        else -> "Odpověď"
                                    })
                                },
                                placeholder = {
                                    Text(when (stage) {
                                        FrequencyDrillStage.MEANING -> "např. jít / chodit"
                                        FrequencyDrillStage.WRITE -> if (currentEntry.article != null) "např. der Hund" else "např. gehen"
                                        FrequencyDrillStage.LISTENING -> "Napiš slyšený tvar"
                                        else -> ""
                                    })
                                }
                            )
                            if (checked) {
                                Card(
                                    Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp),
                                    colors = CardDefaults.cardColors(containerColor = if (answerWasCorrect) Correct else Incorrect)
                                ) {
                                    Column(Modifier.fillMaxWidth().padding(if (compact) 10.dp else 13.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            if (answerWasCorrect) "Správně" else when (stage) {
                                                FrequencyDrillStage.MEANING -> "Správný význam: " + currentEntry.czech
                                                FrequencyDrillStage.WRITE -> "Správný tvar: " + currentEntry.germanAnswer
                                                FrequencyDrillStage.LISTENING -> "Slyšený tvar: " + currentEntry.listeningTarget
                                                else -> "Správná odpověď"
                                            },
                                            color = ForestText, fontSize = 16.sp, fontWeight = FontWeight.Bold
                                        )
                                        Text(currentEntry.exampleGerman, color = Ink, fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold)
                                        Text(currentEntry.exampleCzech, color = Muted, fontSize = 14.sp, lineHeight = 19.sp)
                                    }
                                }
                            } else {
                                Button(
                                    onClick = { checkAnswer() }, enabled = answer.isNotBlank(),
                                    modifier = Modifier.fillMaxWidth().heightIn(min = if (compact) 44.dp else 50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                                    shape = RoundedCornerShape(14.dp)
                                ) { Text("Zkontrolovat odpověď", fontWeight = FontWeight.Bold) }
                            }
                            if (checked) {
                                Button(
                                    onClick = { advance() }, modifier = Modifier.fillMaxWidth().heightIn(min = if (compact) 44.dp else 50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text(when (stage) {
                                        FrequencyDrillStage.MEANING -> if (questionIndex == entries.lastIndex) "Pokračovat k psaní" else "Další slovíčko"
                                        FrequencyDrillStage.WRITE -> if (questionIndex == entries.lastIndex) "Pokračovat k poslechu" else "Další slovíčko"
                                        FrequencyDrillStage.LISTENING -> if (questionIndex == entries.lastIndex) "Zobrazit výsledek" else "Další slovíčko"
                                        else -> "Pokračovat"
                                    }, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(Modifier.weight(1f, fill = false))
                        }
                        FrequencyDrillStage.FINISHED -> Unit
                    }
                }
            }
        }
    }
}
