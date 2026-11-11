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
    // Index every token once per sentence, so a vocabulary word can be found anywhere
    // in a sentence without repeatedly scanning the complete sentence catalogue.
    val sourcesByToken = sources
        .flatMap { source ->
            source.tokens.map { it.value.lowercase() }.distinct().map { token -> token to source }
        }
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
        }.map(::phraseTokens).filter { it.isNotEmpty() }.distinct()

    fun candidateSources(forms: List<List<String>>, level: String?): List<SentenceVocabularySource> {
        val unique = linkedMapOf<String, SentenceVocabularySource>()
        forms.forEach { form ->
            sourcesByToken[form.first()].orEmpty().forEach { source ->
                if (level == null || source.course.level == level) {
                    unique.putIfAbsent(source.course.id + "\u0000" + source.item.german, source)
                }
            }
        }
        return unique.values.toList()
    }

    return entries.map { entry ->
        val forms = candidateForms(entry)
        fun bestMatch(level: String?): Triple<SentenceVocabularySource, String, Int>? =
            candidateSources(forms, level).mapNotNull { source ->
                val matchedForm = forms.firstNotNullOfOrNull { form -> findSurface(source, form) }
                    ?: return@mapNotNull null
                val exactLemma = matchedForm.equals(entry.word, ignoreCase = true)
                Triple(source, matchedForm, if (exactLemma) 1 else 0)
            }.maxByOrNull { it.third }

        val best = bestMatch(entry.level) ?: bestMatch(null) ?: return@map entry
        entry.copy(
            exampleGerman = best.first.item.german,
            exampleCzech = best.first.item.czech,
            listeningAnswer = best.second,
            contextCourseTitle = best.first.course.title
        )
    }
}
