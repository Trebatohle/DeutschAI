package com.deutschai.app

/**
 * Reuses the app's own A1-B2 sentence courses as real context for vocabulary practice.
 * Only curated vocabulary entries are enriched: no Czech meanings or lemmas are guessed
 * from raw sentence tokenization.
 */
private data class SentenceVocabularySource(
    val course: SentenceCourseDefinition,
    val item: SentencePracticeItem,
    val tokens: List<MatchResult>
)

internal fun enrichFrequencyVocabularyWithSentenceContext(
    entries: List<FrequencyVocabularyEntry>,
    courses: List<SentenceCourseDefinition> = sentenceCourseCatalog
): List<FrequencyVocabularyEntry> {
    val tokenRegex = Regex("[\\p{L}]+")
    val sources = courses.flatMap { course ->
        course.items.map { item ->
            SentenceVocabularySource(course, item, tokenRegex.findAll(item.german).toList())
        }
    }
    val sourcesByFirstToken = sources
        .flatMap { source -> source.tokens.firstOrNull()?.value?.lowercase()?.let { listOf(it to source) }.orEmpty() }
        .groupBy({ it.first }, { it.second })

    fun phraseTokens(phrase: String): List<String> =
        tokenRegex.findAll(phrase).map { it.value.lowercase() }.toList()

    fun findSurface(source: SentenceVocabularySource, phrase: List<String>): String? {
        if (phrase.isEmpty() || phrase.size > source.tokens.size) return null
        for (start in 0..(source.tokens.size - phrase.size)) {
            var matches = true
            for (offset in phrase.indices) {
                if (!source.tokens[start + offset].value.equals(phrase[offset], ignoreCase = true)) {
                    matches = false
                    break
                }
            }
            if (matches) {
                val first = source.tokens[start].range.first
                val last = source.tokens[start + phrase.lastIndex].range.last + 1
                return source.item.german.substring(first, last)
            }
        }
        return null
    }

    fun candidateForms(entry: FrequencyVocabularyEntry): List<List<String>> =
        buildList {
            add(entry.word)
            if (entry.extraInfo.isNotBlank()) {
                entry.extraInfo.split('·').map(String::trim)
                    .filter(String::isNotBlank)
                    .forEach { add(it.removePrefix("Pl.:").trim()) }
            }
        }.map(::phraseTokens).filter(List<String>::isNotEmpty).distinct()

    return entries.map { entry ->
        val forms = candidateForms(entry)
        val possibleSources = forms
            .flatMap { form -> sourcesByFirstToken[form.first()].orEmpty() }
            .distinct()
        val matches = possibleSources.mapNotNull { source ->
            val matchedForm = forms.firstNotNullOfOrNull { form -> findSurface(source, form) }
                ?: return@mapNotNull null
            val levelDistance = kotlin.math.abs(
                listOf("A1", "A2", "B1", "B2").indexOf(source.course.level) -
                    listOf("A1", "A2", "B1", "B2").indexOf(entry.level)
            )
            val exactLemma = matchedForm.equals(entry.word, ignoreCase = true)
            val score = (if (source.course.level == entry.level) 1000 else 0) +
                (if (exactLemma) 20 else 0) - levelDistance * 100
            Triple(score, source, matchedForm)
        }
        val best = matches.maxByOrNull { it.first } ?: return@map entry
        entry.copy(
            exampleGerman = best.second.item.german,
            exampleCzech = best.second.item.czech,
            listeningAnswer = if (entry.kind == FrequencyWordKind.VERB) best.third else entry.listeningAnswer,
            contextCourseTitle = best.second.course.title
        )
    }
}
