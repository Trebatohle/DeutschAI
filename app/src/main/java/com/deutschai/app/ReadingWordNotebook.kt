package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

internal data class ActiveVocabularyQuestion(
    val id: String,
    val sourceTitle: String,
    val czechPrompt: String,
    val answerGerman: String,
    val exampleGerman: String,
    val exampleCzech: String,
    val reviewChoices: List<String>
)

internal fun normalizeGermanRecallAnswer(value: String): String =
    value.trim()
        .lowercase()
        .replace(Regex("[.!?,;:]+$"), "")
        .replace(Regex("\\s+"), " ")

internal fun buildActiveVocabularyQuestions(
    catalogEntries: List<VocabularyEntry>,
    readingEntries: List<ReadingNotebookWord>,
    count: Int = 25,
    random: kotlin.random.Random = kotlin.random.Random.Default
): List<ActiveVocabularyQuestion> {
    if (count <= 0) return emptyList()
    val catalogWords = catalogEntries.distinctBy { it.id }
    val catalogNormalized = catalogWords.map { it.word.trim().lowercase() }.toSet()
    val baseQuestions = catalogWords.map { entry ->
        ActiveVocabularyQuestion(
            id = stableReviewId("active_catalog_word", entry.id),
            sourceTitle = "Můj slovníček",
            czechPrompt = entry.czech,
            answerGerman = "${entry.article} ${entry.word}",
            exampleGerman = vocabularySentenceGerman(entry),
            exampleCzech = vocabularySentenceCzech(entry),
            reviewChoices = emptyList()
        )
    } + readingEntries.distinctBy { it.id }
        .filterNot { it.word.trim().lowercase() in catalogNormalized }
        .map { entry ->
            ActiveVocabularyQuestion(
                id = stableReviewId("active_reading_word", entry.id),
                sourceTitle = "Slova z četby · ${entry.sourceLevel}",
                czechPrompt = entry.meaning,
                answerGerman = entry.word,
                exampleGerman = entry.exampleGerman,
                exampleCzech = entry.meaning,
                reviewChoices = emptyList()
            )
        }
    val answerPool = baseQuestions.map { it.answerGerman }.distinct()
    return baseQuestions.map { question ->
        val distractors = answerPool
            .filterNot { it.equals(question.answerGerman, ignoreCase = true) }
            .shuffled(random)
            .take(3)
        val choices = (distractors + question.answerGerman).shuffled(random)
            .ifEmpty { listOf(question.answerGerman) }
            .let { if (it.size == 1) it + "Nevím" else it }
        question.copy(reviewChoices = choices)
    }.shuffled(random).take(count)
}

internal enum class PersonalNotebookPracticeMode { RECOGNITION, ACTIVE_RECALL }

@androidx.compose.runtime.Composable
internal fun PersonalNotebookPracticeScreen(
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    val catalogEntries = VocabularyNotebookStore.entries(context)
    val readingEntries = ReadingWordNotebookStore.entries(context)
    var mode by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<PersonalNotebookPracticeMode?>(null) }
    var questions by androidx.compose.runtime.remember(catalogEntries.map { it.id }, readingEntries.map { it.id }) {
        androidx.compose.runtime.mutableStateOf(buildPersonalNotebookQuestions(catalogEntries, readingEntries))
    }
    var questionIndex by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
    var selectedAnswer by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<Int?>(null) }
    var score by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
    var finished by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    var activeQuestions by androidx.compose.runtime.remember(catalogEntries.map { it.id }, readingEntries.map { it.id }) {
        androidx.compose.runtime.mutableStateOf(buildActiveVocabularyQuestions(catalogEntries, readingEntries))
    }
    var activeIndex by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
    var activeAnswer by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var activeChecked by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var activeWasCorrect by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var activeScore by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
    var activeFinished by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    val question = questions.getOrNull(questionIndex)
    val activeQuestion = activeQuestions.getOrNull(activeIndex)

    fun restartRecognition() {
        questions = buildPersonalNotebookQuestions(
            VocabularyNotebookStore.entries(context),
            ReadingWordNotebookStore.entries(context)
        )
        questionIndex = 0
        selectedAnswer = null
        score = 0
        finished = false
    }

    fun restartActiveRecall() {
        activeQuestions = buildActiveVocabularyQuestions(
            VocabularyNotebookStore.entries(context),
            ReadingWordNotebookStore.entries(context)
        )
        activeIndex = 0
        activeAnswer = ""
        activeChecked = false
        activeWasCorrect = false
        activeScore = 0
        activeFinished = false
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
            AppScreenHeader(title = "Procvičování mého slovníčku", onBack = onBack, onHome = onHome)

            if (catalogEntries.isEmpty() && readingEntries.isEmpty()) {
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
            } else if (mode == null) {
                androidx.compose.material3.Text(
                    "Vyber způsob procvičování",
                    color = Ink, fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                androidx.compose.material3.Text(
                    "Nejprve si můžeš ověřit, zda slovům rozumíš. Potom si je zkus vybavit bez nabídnutých odpovědí.",
                    color = Muted, fontSize = 14.sp, lineHeight = 21.sp
                )
                androidx.compose.material3.Card(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = androidx.compose.ui.Modifier.padding(17.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(7.dp)
                    ) {
                        androidx.compose.material3.Text("Rozpoznání významu", color = Ink, fontSize = 17.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        androidx.compose.material3.Text("Uvidíš německé slovo a vybereš správný český význam. Vhodné pro zopakování slovní zásoby.", color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                        androidx.compose.material3.Button(
                            onClick = { restartRecognition(); mode = PersonalNotebookPracticeMode.RECOGNITION },
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 50.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                        ) { androidx.compose.material3.Text("Procvičit rozpoznání", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                    }
                }
                androidx.compose.material3.Card(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Mint)
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = androidx.compose.ui.Modifier.padding(17.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(7.dp)
                    ) {
                        androidx.compose.material3.Text("Aktivní vybavení", color = ForestText, fontSize = 17.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        androidx.compose.material3.Text("Dostaneš český význam a sám/sama napíšeš německé slovo. U podstatných jmen bude součástí odpovědi také správný člen.", color = ForestText, fontSize = 13.sp, lineHeight = 19.sp)
                        androidx.compose.material3.Button(
                            onClick = { restartActiveRecall(); mode = PersonalNotebookPracticeMode.ACTIVE_RECALL },
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 50.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                        ) { androidx.compose.material3.Text("Procvičit aktivní vybavení", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                    }
                }
            } else if (mode == PersonalNotebookPracticeMode.RECOGNITION) {
                if (finished) {
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
                                "Odpovědi se uložily do dlouhodobého opakování. Pro ověření, že si slova opravdu vybavíš, můžeš pokračovat aktivním psaním.",
                                color = Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 20.sp
                            )
                        }
                    }
                    androidx.compose.material3.Button(
                        onClick = { restartActiveRecall(); mode = PersonalNotebookPracticeMode.ACTIVE_RECALL },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 52.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                    ) { androidx.compose.material3.Text("Vyzkoušet aktivní vybavení", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                    androidx.compose.material3.Button(
                        onClick = { restartRecognition() },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 52.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = OptionSurface, contentColor = Ink),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                    ) { androidx.compose.material3.Text("Procvičit znovu", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                    androidx.compose.material3.Button(
                        onClick = { mode = null },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 52.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                    ) { androidx.compose.material3.Text("Zpět k výběru", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                } else if (question != null) {
                    androidx.compose.material3.Text(
                        "ROZPOZNÁNÍ · OTÁZKA ${questionIndex + 1} Z ${questions.size}",
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
            } else if (mode == PersonalNotebookPracticeMode.ACTIVE_RECALL) {
                if (activeFinished) {
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
                            androidx.compose.material3.Text("Aktivní procvičení dokončeno", color = Color.White, fontSize = 22.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            androidx.compose.material3.Text("$activeScore z ${activeQuestions.size} správně", color = Gold, fontSize = 27.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            androidx.compose.material3.Text(
                                "Slova, která bylo obtížné si vybavit, se vrátí v dlouhodobém opakování.",
                                color = Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 20.sp
                            )
                        }
                    }
                    androidx.compose.material3.Button(
                        onClick = { restartActiveRecall() },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 52.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                    ) { androidx.compose.material3.Text("Procvičit aktivně znovu", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                    androidx.compose.material3.Button(
                        onClick = { mode = null },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 52.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                    ) { androidx.compose.material3.Text("Zpět k výběru", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                } else if (activeQuestion != null) {
                    androidx.compose.material3.Text(
                        "AKTIVNÍ VYBAVENÍ · OTÁZKA ${activeIndex + 1} Z ${activeQuestions.size}",
                        color = ForestText, fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, letterSpacing = 1.sp
                    )
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { (activeIndex + 1).toFloat() / activeQuestions.size.toFloat() },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                    )
                    androidx.compose.material3.Text(
                        "Jak se německy řekne:",
                        color = Muted, fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                    androidx.compose.material3.Text(
                        activeQuestion.czechPrompt,
                        color = Ink, fontSize = 25.sp, lineHeight = 33.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    if (activeQuestion.answerGerman.startsWith("der ") ||
                        activeQuestion.answerGerman.startsWith("die ") ||
                        activeQuestion.answerGerman.startsWith("das ")
                    ) {
                        androidx.compose.material3.Text(
                            "Napiš podstatné jméno včetně správného členu.",
                            color = Muted, fontSize = 13.sp, lineHeight = 18.sp
                        )
                    }
                    androidx.compose.material3.OutlinedTextField(
                        value = activeAnswer,
                        onValueChange = { if (!activeChecked) activeAnswer = it },
                        enabled = !activeChecked,
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { androidx.compose.material3.Text("Německá odpověď") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Sentences,
                            imeAction = androidx.compose.ui.text.input.ImeAction.Done
                        )
                    )
                    if (!activeChecked) {
                        androidx.compose.material3.Button(
                            onClick = {
                                val correct = normalizeGermanRecallAnswer(activeAnswer) ==
                                    normalizeGermanRecallAnswer(activeQuestion.answerGerman)
                                activeWasCorrect = correct
                                activeChecked = true
                                if (correct) activeScore += 1
                                val reviewQuestion = ReviewQuestion(
                                    id = stableReviewId("notebook_active", activeQuestion.id),
                                    sourceKind = "vocabulary",
                                    sourceTitle = activeQuestion.sourceTitle,
                                    prompt = "Jak se německy řekne: ${activeQuestion.czechPrompt}?",
                                    choices = activeQuestion.reviewChoices,
                                    correctIndex = activeQuestion.reviewChoices.indexOf(activeQuestion.answerGerman),
                                    explanation = "Správně: ${activeQuestion.answerGerman}.",
                                    germanSentence = activeQuestion.exampleGerman,
                                    translation = activeQuestion.exampleCzech
                                )
                                LearningReviewStore.recordAnswer(context, reviewQuestion, correct)
                            },
                            enabled = activeAnswer.isNotBlank(),
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 52.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                        ) { androidx.compose.material3.Text("Zkontrolovat odpověď", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                    } else {
                        androidx.compose.material3.Card(
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = if (activeWasCorrect) Correct else Incorrect)
                        ) {
                            androidx.compose.foundation.layout.Column(
                                modifier = androidx.compose.ui.Modifier.padding(15.dp),
                                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(7.dp)
                            ) {
                                androidx.compose.material3.Text(
                                    if (activeWasCorrect) "Správně! Gut gemacht!" else "Správná odpověď: ${activeQuestion.answerGerman}",
                                    color = ForestText, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                )
                                if (activeQuestion.exampleGerman.isNotBlank()) {
                                    androidx.compose.material3.Text(activeQuestion.exampleGerman, color = Ink, fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                                    GermanAudioControls(player = speaker, text = activeQuestion.exampleGerman)
                                }
                                if (activeQuestion.exampleCzech.isNotBlank()) {
                                    androidx.compose.material3.Text(activeQuestion.exampleCzech, color = Muted, fontSize = 14.sp)
                                }
                            }
                        }
                        androidx.compose.material3.Button(
                            onClick = {
                                if (activeIndex < activeQuestions.lastIndex) {
                                    activeIndex += 1
                                    activeAnswer = ""
                                    activeChecked = false
                                    activeWasCorrect = false
                                } else {
                                    activeFinished = true
                                    onComplete(activeScore, activeQuestions.size)
                                }
                            },
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth().heightIn(min = 54.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp)
                        ) {
                            androidx.compose.material3.Text(
                                if (activeIndex < activeQuestions.lastIndex) "Další slovo" else "Dokončit",
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                        }
                    }
                } else {
                    androidx.compose.material3.Text("Ve slovníčku zatím nejsou žádná slova k aktivnímu procvičení.", color = Muted, fontSize = 14.sp)
                }
            }

            androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.heightIn(min = 4.dp))
        }
    }
}
