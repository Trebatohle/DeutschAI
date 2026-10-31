package com.deutschai.app

import android.content.Context
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
