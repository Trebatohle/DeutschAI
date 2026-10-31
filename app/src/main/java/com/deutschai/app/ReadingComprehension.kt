package com.deutschai.app

internal data class ReadingComprehensionQuestion(
    val id: String,
    val facet: String,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val passage: String = ""
)

/**
 * Builds five offline comprehension checks from statements that really occur in the
 * selected story. Distractors are genuine sentences from other bundled stories, so
 * each question has exactly one statement that belongs to the current text.
 */
private fun extractReadingSentences(text: String): List<String> = text
    .replace("\n", " ")
    .split(Regex("(?<=[.!?])\\s+"))
    .map { it.trim().replace(Regex("\\s+"), " ") }
    .filter { sentence ->
        val words = sentence.split(Regex("\\s+")).filter { it.isNotBlank() }
        words.size in 7..28 &&
            (sentence.endsWith(".") || sentence.endsWith("!") || sentence.endsWith("?"))
    }
    .distinctBy { it.lowercase() }

// Parse the bundled library once; quizzes and the placement test reuse the same offline stories.
private val readingSentencesByArticle: Map<String, List<String>> by lazy(LazyThreadSafetyMode.PUBLICATION) {
    readingArticles.associate { article -> article.id to extractReadingSentences(article.body) }
}

/**
 * Builds five offline comprehension checks from statements that really occur in the
 * selected story. Distractors are genuine sentences from other bundled stories, so
 * each question has exactly one statement that belongs to the current text.
 */
internal fun buildReadingComprehensionQuestions(article: ReadingArticle): List<ReadingComprehensionQuestion> {
    val sourceSentences = readingSentencesByArticle[article.id] ?: extractReadingSentences(article.body)
    require(sourceSentences.size >= 5) { "Reading article ${article.id} needs at least five usable sentences." }

    val sameLevelDistractors = readingArticles
        .filter { it.id != article.id && it.level == article.level }
        .flatMap { readingSentencesByArticle[it.id].orEmpty() }
    val otherLevelDistractors = readingArticles
        .filter { it.id != article.id && it.level != article.level }
        .flatMap { readingSentencesByArticle[it.id].orEmpty() }

    val targetSentenceKeys = sourceSentences.map { it.lowercase() }.toSet()
    val safeSameLevelDistractors = sameLevelDistractors
        .filterNot { it.lowercase() in targetSentenceKeys }
        .distinctBy { it.lowercase() }
    val sameLevelKeys = safeSameLevelDistractors.map { it.lowercase() }.toSet()
    val safeOtherLevelDistractors = otherLevelDistractors
        .filterNot { it.lowercase() in targetSentenceKeys || it.lowercase() in sameLevelKeys }
        .distinctBy { it.lowercase() }

    val facets = listOf(
        "Informace z úvodu",
        "Důležitý detail",
        "Událost z první poloviny",
        "Událost z druhé poloviny",
        "Informace ze závěru"
    )
    val positions = listOf(0, 1, 2, 3, 4).map { slot ->
        ((sourceSentences.lastIndex * slot) / 4).coerceIn(0, sourceSentences.lastIndex)
    }.distinct()
    val targets = positions.map { sourceSentences[it] }.toMutableList()
    if (targets.size < 5) {
        sourceSentences.filterNot { it in targets }.take(5 - targets.size).forEach(targets::add)
    }
    require(targets.size == 5) { "Reading article ${article.id} must produce five distinct facts." }

    return targets.mapIndexed { index, correctSentence ->
        // Prefer distractors from the same CEFR level, and reshuffle them for each question.
        val distractors = (
            safeSameLevelDistractors.shuffled() + safeOtherLevelDistractors.shuffled()
        ).asSequence()
            .filterNot { it.equals(correctSentence, ignoreCase = true) }
            .take(3)
            .toList()
        require(distractors.size == 3) {
            "Not enough distinct distractor sentences to build a five-question quiz for ${article.id}."
        }
        val options = (listOf(correctSentence) + distractors).shuffled()
        val passage = article.body
            .split(Regex("\\n\\s*\\n"))
            .firstOrNull { paragraph ->
                paragraph.replace(Regex("\\s+"), " ")
                    .contains(correctSentence, ignoreCase = true)
            }
            ?.trim()
            ?.replace(Regex("\\s+"), " ")
            ?: article.body.trim().replace(Regex("\\s+"), " ")

        ReadingComprehensionQuestion(
            id = stableReviewId("reading", article.id, index.toString()),
            facet = facets[index],
            prompt = "Která informace se skutečně objevila v přečteném textu?",
            choices = options,
            correctIndex = options.indexOf(correctSentence),
            explanation = "Tato informace se v článku „${article.title}“ skutečně objevila.",
            passage = passage
        )
    }
}
