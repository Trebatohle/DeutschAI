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
    if (entries.isEmpty()) return emptyList()

    val tokenRegex = Regex("[\\p{L}]+")
    fun phraseTokens(phrase: String): List<String> =
        tokenRegex.findAll(phrase).map { it.value.lowercase() }.toList()

    fun candidateForms(entry: FrequencyVocabularyEntry): List<List<String>> =
        buildList {
            add(entry.word)
            if (entry.extraInfo.isNotBlank()) {
                entry.extraInfo.split('·').map(String::trim)
                    .filter(String::isNotBlank)
                    .forEach { add(it.removePrefix("Pl.:").trim()) }
            }
        }.map(::phraseTokens).filter { it.isNotEmpty() }.distinct()

    val formsByEntry = entries.associateWith(::candidateForms)
    val requestedFirstTokens = formsByEntry.values
        .flatten()
        .mapNotNull { it.firstOrNull() }
        .toSet()

    // Index only sentence tokens that could begin one of the selected vocabulary forms.
    // This avoids keeping a large index of every word from every course in memory.
    val sourcesByToken = mutableMapOf<String, MutableList<SentenceVocabularySource>>()
    courses.forEach { course ->
        course.items.forEach { item ->
            val tokens = tokenRegex.findAll(item.german).toList()
            val matchingKeys = tokens.asSequence()
                .map { it.value.lowercase() }
                .filter { it in requestedFirstTokens }
                .distinct()
                .toList()
            if (matchingKeys.isNotEmpty()) {
                val source = SentenceVocabularySource(course, item, tokens)
                matchingKeys.forEach { key ->
                    sourcesByToken.getOrPut(key) { mutableListOf() }.add(source)
                }
            }
        }
    }
    val index: Map<String, List<SentenceVocabularySource>> = sourcesByToken

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

    fun bestMatch(
        entry: FrequencyVocabularyEntry,
        forms: List<List<String>>,
        level: String?
    ): Triple<SentenceVocabularySource, String, Int>? {
        val uniqueSources = linkedMapOf<String, SentenceVocabularySource>()
        forms.forEach { form ->
            index[form.first()].orEmpty().forEach { source ->
                if (level == null || source.course.level == level) {
                    uniqueSources.putIfAbsent(source.course.id + "\u0000" + source.item.german, source)
                }
            }
        }
        return uniqueSources.values.mapNotNull { source ->
            val matchedForm = forms.firstNotNullOfOrNull { form -> findSurface(source, form) }
                ?: return@mapNotNull null
            val exactLemma = matchedForm.equals(entry.word, ignoreCase = true)
            Triple(source, matchedForm, if (exactLemma) 1 else 0)
        }.maxByOrNull { it.third }
    }

    return entries.map { entry ->
        val forms = formsByEntry[entry].orEmpty()
        val best = bestMatch(entry, forms, entry.level)
            ?: bestMatch(entry, forms, null)
            ?: return@map entry

        entry.copy(
            exampleGerman = best.first.item.german,
            exampleCzech = best.first.item.czech,
            listeningAnswer = best.second,
            contextCourseTitle = best.first.course.title
        )
    }
}
