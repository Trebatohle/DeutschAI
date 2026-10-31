package com.deutschai.app

internal data class ReadingComprehensionQuestion(
    val id: String,
    val facet: String,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String
)

/**
 * Builds five offline comprehension checks from statements that really occur in the
 * selected story. Distractors are genuine sentences from other bundled stories, so
 * each question has exactly one statement that belongs to the current text.
 */
internal fun buildReadingComprehensionQuestions(article: ReadingArticle): List<ReadingComprehensionQuestion> {
    fun sentences(text: String): List<String> = text
        .replace("\\n", " ")
        .split(Regex("(?<=[.!?])\\s+"))
        .map { it.trim().replace(Regex("\\s+"), " ") }
        .filter { sentence ->
            val words = sentence.split(Regex("\\s+")).filter { it.isNotBlank() }
            words.size in 7..28 && sentence.lastOrNull() in listOf('.', '!', '?')
        }
        .distinctBy { it.lowercase() }

    val sourceSentences = sentences(article.body)
    require(sourceSentences.size >= 5) { "Reading article ${article.id} needs at least five usable sentences." }

    val sameLevelDistractors = readingArticles
        .filter { it.id != article.id && it.level == article.level }
        .flatMap { sentences(it.body) }
    val otherLevelDistractors = readingArticles
        .filter { it.id != article.id && it.level != article.level }
        .flatMap { sentences(it.body) }

    val allDistractors = (sameLevelDistractors + otherLevelDistractors)
        .filterNot { article.body.contains(it, ignoreCase = true) }
        .distinctBy { it.lowercase() }
        .shuffled()
        .toMutableList()

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
        val distractors = allDistractors
            .asSequence()
            .filterNot { it.equals(correctSentence, ignoreCase = true) }
            .filterNot { candidate -> candidate.lowercase() in targets.map { it.lowercase() } }
            .take(3)
            .toList()
        require(distractors.size == 3) {
            "Not enough distinct distractor sentences to build a five-question quiz for ${article.id}."
        }
        val options = (listOf(correctSentence) + distractors).shuffled()
        ReadingComprehensionQuestion(
            id = stableReviewId("reading", article.id, index.toString()),
            facet = facets[index],
            prompt = "Která informace se skutečně objevila v přečteném textu?",
            choices = options,
            correctIndex = options.indexOf(correctSentence),
            explanation = "Tato informace se v článku „${article.title}“ skutečně objevila."
        )
    }
}
