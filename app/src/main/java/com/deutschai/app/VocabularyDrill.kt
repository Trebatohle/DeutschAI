package com.deutschai.app

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

internal data class VocabularyDrillQuestion(
    val entry: VocabularyEntry,
    val choices: List<String>,
    val correctIndex: Int
)

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

internal fun normalizeVocabularyDrillAnswer(value: String): String =
    value.trim()
        .lowercase()
        .replace(Regex("\\s+"), " ")

@Composable
internal fun VocabularyDrillScreen(
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var questions by remember { mutableStateOf(buildVocabularyDrillBatch()) }
    var part by remember { mutableStateOf(0) }
    var questionIndex by remember { mutableStateOf(0) }
    var selectedMeaningIndex by remember { mutableStateOf<Int?>(null) }
    var meaningScore by remember { mutableStateOf(0) }
    var writtenAnswer by remember { mutableStateOf("") }
    var writingChecked by remember { mutableStateOf(false) }
    var writingWasCorrect by remember { mutableStateOf(false) }
    var writingScore by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    val question = questions.getOrNull(questionIndex)

    fun startNewDrill() {
        questions = buildVocabularyDrillBatch()
        part = 0
        questionIndex = 0
        selectedMeaningIndex = null
        meaningScore = 0
        writtenAnswer = ""
        writingChecked = false
        writingWasCorrect = false
        writingScore = 0
        finished = false
    }

    fun advance() {
        if (part == 0) {
            if (questionIndex < questions.lastIndex) {
                questionIndex += 1
                selectedMeaningIndex = null
            } else {
                part = 1
                questionIndex = 0
                writtenAnswer = ""
                writingChecked = false
                writingWasCorrect = false
            }
        } else if (questionIndex < questions.lastIndex) {
            questionIndex += 1
            writtenAnswer = ""
            writingChecked = false
            writingWasCorrect = false
        } else {
            finished = true
            CourseResultStore.save(context, "vocabulary_drill", meaningScore + writingScore, questions.size * 2)
            onComplete(meaningScore + writingScore, questions.size * 2)
        }
    }

    fun checkWrittenAnswer() {
        val current = question ?: return
        if (writingChecked || writtenAnswer.isBlank()) return
        writingWasCorrect = normalizeVocabularyDrillAnswer(writtenAnswer) ==
            normalizeVocabularyDrillAnswer(current.entry.article + " " + current.entry.word)
        if (writingWasCorrect) writingScore += 1
        writingChecked = true
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

            if (questions.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Text(
                        "Slovní zásoba zatím neobsahuje dostatek slov pro sestavení drilu.",
                        modifier = Modifier.padding(18.dp),
                        color = Ink,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            } else if (finished) {
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
                        Text("Dril dokončen", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(
                            (meaningScore + writingScore).toString() + " z " + (questions.size * 2) + " bodů",
                            color = Gold,
                            fontSize = 29.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Významy: " + meaningScore + "/" + questions.size +
                                "  ·  Psaní se členem: " + writingScore + "/" + questions.size,
                            color = Color(0xFFE0EAE5),
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                        Text(
                            "Ve druhé části jsi znovu pracoval/a se stejnými pěti slovíčky jako v první části.",
                            color = Color(0xFFE0EAE5),
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
                Button(
                    onClick = { startNewDrill() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Procvičit dalších 5 slov", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zpět k členům a slovíčkům", fontWeight = FontWeight.Bold) }
            } else if (question != null) {
                Text(
                    if (part == 0) "ČÁST 1 ZE 2 · VÝZNAM" else "ČÁST 2 ZE 2 · PSANÍ",
                    color = ForestText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                    color = ForestText,
                    trackColor = Mint
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (part == 0) "Vyber český význam" else "Napiš německy včetně členu",
                        color = Ink,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        (questionIndex + 1).toString() + "/5",
                        color = ForestText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    if (part == 0) "Přečti německé podstatné jméno a vyber správný překlad." else "Teď si stejné slovo vybav z češtiny a napiš celý německý výraz.",
                    color = Muted,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            if (part == 0) question.entry.article + " " + question.entry.word else question.entry.czech,
                            color = Ink,
                            fontSize = 25.sp,
                            lineHeight = 33.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (part == 0) "Německy → česky" else "Česky → německy",
                            color = Muted,
                            fontSize = 12.sp
                        )
                    }
                }

                if (part == 0) {
                    question.choices.forEachIndexed { index, choice ->
                        val background = when {
                            selectedMeaningIndex == null -> OptionSurface
                            index == question.correctIndex -> Correct
                            index == selectedMeaningIndex -> Incorrect
                            else -> OptionSurface
                        }
                        Card(
                            onClick = {
                                if (selectedMeaningIndex == null) {
                                    selectedMeaningIndex = index
                                    if (index == question.correctIndex) meaningScore += 1
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp),
                            colors = CardDefaults.cardColors(containerColor = background)
                        ) {
                            Text(
                                choice,
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                color = Ink,
                                fontSize = 16.sp,
                                fontWeight = if (selectedMeaningIndex != null && index == question.correctIndex) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                    if (selectedMeaningIndex != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedMeaningIndex == question.correctIndex) Correct else Incorrect
                            )
                        ) {
                            Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text(
                                    if (selectedMeaningIndex == question.correctIndex) "Správně" else "Správný význam: " + question.entry.czech,
                                    color = ForestText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Ve druhé části napíšeš tento výraz německy i se členem.",
                                    color = Ink,
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                        Button(
                            onClick = { advance() },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                if (questionIndex == questions.lastIndex) "Pokračovat k psaní" else "Další slovíčko",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = writtenAnswer,
                        onValueChange = { if (!writingChecked) writtenAnswer = it },
                        enabled = !writingChecked,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Německý výraz včetně členu") },
                        placeholder = { Text("např. der Hund") }
                    )
                    if (!writingChecked) {
                        Button(
                            onClick = { checkWrittenAnswer() },
                            enabled = writtenAnswer.isNotBlank(),
                            modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = RoundedCornerShape(14.dp)
                        ) { Text("Zkontrolovat odpověď", fontWeight = FontWeight.Bold) }
                    } else {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = if (writingWasCorrect) Correct else Incorrect)
                        ) {
                            Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text(
                                    if (writingWasCorrect) "Správně" else "Správný zápis: " + question.entry.article + " " + question.entry.word,
                                    color = ForestText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Česky: " + question.entry.czech, color = Ink, fontSize = 13.sp)
                            }
                        }
                        Button(
                            onClick = { advance() },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                if (questionIndex == questions.lastIndex) "Zobrazit výsledek" else "Další slovíčko",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
