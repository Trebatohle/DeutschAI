package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject

internal data class ReadingNotebookWord(
    val id: String,
    val word: String,
    val meaning: String,
    val exampleGerman: String,
    val sourceTitle: String,
    val sourceLevel: String,
    val savedAtMillis: Long
)

internal fun filterReadingNotebookWords(words: List<ReadingNotebookWord>): List<ReadingNotebookWord> =
    words.sortedWith(compareBy<ReadingNotebookWord> { it.word.lowercase() }.thenBy { it.meaning.lowercase() })

internal fun buildPersonalNotebookQuestions(
    catalogEntries: List<VocabularyEntry>,
    readingEntries: List<ReadingNotebookWord>,
    count: Int = 25,
    random: kotlin.random.Random = kotlin.random.Random.Default
): List<ReviewQuestion> {
    if (count <= 0) return emptyList()
    val questions = mutableListOf<ReviewQuestion>()

    catalogEntries.distinctBy { it.id }.forEach { entry ->
        val item = makeVocabularyQuestion(
            entry = entry,
            presentation = VocabularyPresentation.WORD,
            allEntries = vocabularyCatalog,
            random = random
        )
        questions += ReviewQuestion(
            id = stableReviewId("notebook_vocab", entry.id),
            sourceKind = "vocabulary",
            sourceTitle = "Můj slovníček",
            prompt = "Vyber český význam německého výrazu:",
            choices = item.choices,
            correctIndex = item.correctIndex,
            explanation = "Správně: ${entry.article} ${entry.word} = ${entry.czech}.",
            germanSentence = vocabularySentenceGerman(entry),
            translation = vocabularySentenceCzech(entry)
        )
    }

    readingEntries.distinctBy { it.id }.forEach { entry ->
        val distractors = vocabularyCatalog.asSequence()
            .map { it.czech }
            .filter { it.isNotBlank() && !it.equals(entry.meaning, ignoreCase = true) }
            .distinct()
            .shuffled(random)
            .take(3)
            .toList()
        if (distractors.size == 3) {
            val choices = (distractors + entry.meaning).shuffled(random)
            questions += ReviewQuestion(
                id = stableReviewId("notebook_reading", entry.id),
                sourceKind = "vocabulary",
                sourceTitle = "Slova z četby · ${entry.sourceLevel}",
                prompt = "Co znamená německé slovo „${entry.word}“?",
                choices = choices,
                correctIndex = choices.indexOf(entry.meaning),
                explanation = "${entry.word} = ${entry.meaning}. Uloženo při čtení příběhu „${entry.sourceTitle}“.",
                germanSentence = entry.exampleGerman,
                translation = entry.meaning
            )
        }
    }

    return questions.shuffled(random).take(count)
}

/** Words saved from reading remain separate from the curated noun catalogue. */
internal object ReadingWordNotebookStore {
    private const val PREFS = "deutschai_personal_vocabulary_v1"
    private const val READING_WORDS = "reading_words_v1"

    private fun preferences(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun entries(context: Context): List<ReadingNotebookWord> {
        val raw = preferences(context).getString(READING_WORDS, "[]") ?: "[]"
        val parsed = runCatching {
            val array = JSONArray(raw)
            buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    val word = item.optString("word", "").trim()
                    val meaning = item.optString("meaning", "").trim()
                    if (word.isBlank() || meaning.isBlank()) continue
                    add(
                        ReadingNotebookWord(
                            id = item.optString("id", stableReviewId("reading_word", word.lowercase(), meaning.lowercase())),
                            word = word,
                            meaning = meaning,
                            exampleGerman = item.optString("exampleGerman", word),
                            sourceTitle = item.optString("sourceTitle", "Německé čtení"),
                            sourceLevel = item.optString("sourceLevel", ""),
                            savedAtMillis = item.optLong("savedAtMillis", 0L)
                        )
                    )
                }
            }
        }.getOrElse { emptyList() }
        return filterReadingNotebookWords(parsed)
    }

    fun isSaved(context: Context, word: String, meaning: String): Boolean {
        val key = stableReviewId("reading_word", word.trim().lowercase(), meaning.trim().lowercase())
        return entries(context).any { it.id == key }
    }

    fun save(
        context: Context,
        word: String,
        meaning: String,
        exampleGerman: String,
        sourceTitle: String,
        sourceLevel: String,
        nowMillis: Long = System.currentTimeMillis()
    ): ReadingNotebookWord? {
        val cleanWord = word.trim()
        val cleanMeaning = meaning.trim()
        if (cleanWord.isBlank() || cleanMeaning.isBlank()) return null
        val id = stableReviewId("reading_word", cleanWord.lowercase(), cleanMeaning.lowercase())
        val item = ReadingNotebookWord(
            id = id,
            word = cleanWord,
            meaning = cleanMeaning,
            exampleGerman = exampleGerman.trim().ifBlank { cleanWord },
            sourceTitle = sourceTitle.trim().ifBlank { "Německé čtení" },
            sourceLevel = sourceLevel.trim(),
            savedAtMillis = nowMillis
        )
        val updated = (entries(context).filterNot { it.id == id } + item)
        write(context, updated)
        return item
    }

    fun remove(context: Context, id: String) {
        write(context, entries(context).filterNot { it.id == id })
    }

    private fun write(context: Context, entries: List<ReadingNotebookWord>) {
        val array = JSONArray()
        filterReadingNotebookWords(entries).forEach { item ->
            array.put(
                JSONObject()
                    .put("id", item.id)
                    .put("word", item.word)
                    .put("meaning", item.meaning)
                    .put("exampleGerman", item.exampleGerman)
                    .put("sourceTitle", item.sourceTitle)
                    .put("sourceLevel", item.sourceLevel)
                    .put("savedAtMillis", item.savedAtMillis)
            )
        }
        preferences(context).edit().putString(READING_WORDS, array.toString()).apply()
    }
}

@androidx.compose.runtime.Composable
internal fun PersonalNotebookPracticeScreen(
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    val catalogEntries = VocabularyNotebookStore.entries(context)
    val readingEntries = ReadingWordNotebookStore.entries(context)
    var questions by androidx.compose.runtime.remember(catalogEntries.map { it.id }, readingEntries.map { it.id }) {
        androidx.compose.runtime.mutableStateOf(
            buildPersonalNotebookQuestions(catalogEntries, readingEntries)
        )
    }
    var questionIndex by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
    var selectedAnswer by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<Int?>(null) }
    var score by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
    var finished by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    val question = questions.getOrNull(questionIndex)

    fun restart() {
        questions = buildPersonalNotebookQuestions(
            VocabularyNotebookStore.entries(context),
            ReadingWordNotebookStore.entries(context)
        )
        questionIndex = 0
        selectedAnswer = null
        score = 0
        finished = false
    }

    androidx.compose.material3.Surface(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        color = Cream
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = androidx.compose.ui.Modifier.fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 30.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(14.dp)
        ) {
            androidx.compose.foundation.layout.Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
            ) {
                androidx.compose.material3.Button(
                    onClick = onBack,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    androidx.compose.material3.Icon(
                        androidx.compose.material.icons.Icons.Default.ArrowBack,
                        contentDescription = "Zpět"
                    )
                    androidx.compose.material3.Text("Zpět")
                }
                androidx.compose.foundation.layout.Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(2.dp)) {
                    androidx.compose.material3.Text("DEUTSCHAI", color = ForestText, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                    androidx.compose.material3.Text("Procvičování mého slovníčku", color = Muted, fontSize = 13.sp)
                }
            }

            if (questions.isEmpty()) {
                androidx.compose.material3.Card(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    androidx.compose.material3.Text(
                        "Tvůj slovníček je zatím prázdný. Ulož slova při procvičování nebo při čtení německých příběhů.",
                        modifier = androidx.compose.ui.Modifier.padding(18.dp),
                        color = Muted, fontSize = 14.sp, lineHeight = 21.sp
                    )
                }
            } else if (finished) {
                androidx.compose.material3.Card(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Forest)
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = androidx.compose.ui.Modifier.padding(22.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(9.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material3.Text("Vlastní slovníček procvičen", color = Color.White, fontSize = 22.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        androidx.compose.material3.Text("$score z ${questions.size} správně", color = Gold, fontSize = 27.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        androidx.compose.material3.Text(
                            "Odpovědi se uložily do dlouhodobého opakování, takže se k obtížnějším slovům můžeš vrátit.",
                            color = Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 20.sp
                        )
                    }
                }
                androidx.compose.material3.Button(
                    onClick = { restart() },
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                ) { androidx.compose.material3.Text("Procvičit znovu", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                androidx.compose.material3.Button(
                    onClick = onBack,
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                ) { androidx.compose.material3.Text("Zpět do slovníčku", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
            } else if (question != null) {
                androidx.compose.material3.Text(
                    "OTÁZKA ${questionIndex + 1} Z ${questions.size}",
                    color = ForestText, fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, letterSpacing = 1.sp
                )
                androidx.compose.material3.LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size.toFloat() },
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                )
                androidx.compose.material3.Text(question.prompt, color = Ink, fontSize = 22.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                question.choices.forEachIndexed { index, choice ->
                    val background = when {
                        selectedAnswer == null -> OptionSurface
                        index == question.correctIndex -> Correct
                        index == selectedAnswer -> Incorrect
                        else -> OptionSurface
                    }
                    androidx.compose.material3.Card(
                        onClick = {
                            if (selectedAnswer == null) {
                                selectedAnswer = index
                                val correct = index == question.correctIndex
                                if (correct) score += 1
                                LearningReviewStore.recordAnswer(context, question, correct)
                            }
                        },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
                        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = background)
                    ) {
                        androidx.compose.material3.Text(
                            choice,
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(16.dp),
                            color = Ink, fontSize = 16.sp,
                            fontWeight = if (selectedAnswer != null && index == question.correctIndex) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium
                        )
                    }
                }
                if (selectedAnswer != null) {
                    androidx.compose.material3.Card(
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = if (selectedAnswer == question.correctIndex) Correct else Incorrect
                        )
                    ) {
                        androidx.compose.foundation.layout.Column(
                            modifier = androidx.compose.ui.Modifier.padding(15.dp),
                            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(7.dp)
                        ) {
                            androidx.compose.material3.Text(
                                if (selectedAnswer == question.correctIndex) "Správně! Gut gemacht!" else "Správná odpověď: ${question.choices[question.correctIndex]}",
                                color = ForestText, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                            if (question.germanSentence.isNotBlank()) {
                                androidx.compose.material3.Text(question.germanSentence, color = Ink, fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                                GermanAudioControls(player = speaker, text = question.germanSentence)
                            }
                            if (question.translation.isNotBlank()) {
                                androidx.compose.material3.Text(question.translation, color = Muted, fontSize = 14.sp)
                            }
                            androidx.compose.material3.Text(question.explanation, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                        }
                    }
                    androidx.compose.material3.Button(
                        onClick = {
                            if (questionIndex < questions.lastIndex) {
                                questionIndex += 1
                                selectedAnswer = null
                            } else {
                                finished = true
                                onComplete(score, questions.size)
                            }
                        },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 54.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                    ) {
                        androidx.compose.material3.Text(
                            if (questionIndex < questions.lastIndex) "Další slovo" else "Dokončit",
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
