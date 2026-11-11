package com.deutschai.app

/**
 * Reuses the app's own A1-B2 sentence courses as real context for vocabulary practice.
 * Only curated vocabulary entries are enriched: no Czech meanings or lemmas are guessed
 * from raw sentence tokenization.
 */
internal fun enrichFrequencyVocabularyWithSentenceContext(
    entries: List<FrequencyVocabularyEntry>,
    courses: List<SentenceCourseDefinition> = sentenceCourseCatalog
): List<FrequencyVocabularyEntry> {
    val sentenceItems = courses.flatMap { course ->
        course.items.map { item -> Triple(course, item, course.level) }
    }

    fun surfaceForm(sentence: String, phrase: String): String? {
        val tokens = Regex("[\\p{L}]+").findAll(phrase).map { it.value }.toList()
        if (tokens.isEmpty()) return null
        val pattern = tokens.joinToString("\\s+") { Regex.escape(it) }
        return Regex("(?iu)(?<![\\p{L}])$pattern(?![\\p{L}])")
            .find(sentence)?.value
    }

    fun candidateForms(entry: FrequencyVocabularyEntry): List<String> {
        val forms = buildList {
            add(entry.word)
            if (entry.extraInfo.isNotBlank()) {
                entry.extraInfo.split('·').map(String::trim)
                    .filter(String::isNotBlank)
                    .forEach { form ->
                        add(form.removePrefix("Pl.:").trim())
                    }
            }
        }
        return forms.distinctBy { it.lowercase() }
    }

    return entries.map { entry ->
        val forms = candidateForms(entry)
        val matches = sentenceItems.mapNotNull { (course, item, level) ->
            val matchedForm = forms.firstNotNullOfOrNull { form ->
                surfaceForm(item.german, form)
            } ?: return@mapNotNull null
            val levelDistance = listOf("A1", "A2", "B1", "B2")
                .indexOf(level).let { it - listOf("A1", "A2", "B1", "B2").indexOf(entry.level) }
                .let(kotlin.math::abs)
            val exactLemma = matchedForm.equals(entry.word, ignoreCase = true)
            val score = (if (level == entry.level) 1000 else 0) +
                (if (exactLemma) 20 else 0) - levelDistance * 100
            Triple(score, course, item to matchedForm)
        }
        val best = matches.maxByOrNull { it.first } ?: return@map entry
        val course = best.second
        val item = best.third.first
        val matchedForm = best.third.second
        entry.copy(
            exampleGerman = item.german,
            exampleCzech = item.czech,
            listeningAnswer = if (entry.kind == FrequencyWordKind.VERB) matchedForm else entry.listeningAnswer,
            contextCourseTitle = course.title
        )
    }
}
