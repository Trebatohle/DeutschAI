package com.deutschai.app

import java.text.Normalizer
import kotlin.random.Random

internal enum class LessonPracticeStage {
    INTRODUCTION,
    WRITING,
    DICTATION,
    REVIEW,
    COMPLETE
}

internal enum class LessonExerciseMode {
    INTRODUCTION,
    WRITING,
    DICTATION
}

internal data class LessonExercise(
    val question: LessonQuestion,
    val mode: LessonExerciseMode
)

internal data class LessonPracticeSelection(
    val introduction: List<LessonQuestion>,
    val writing: List<LessonQuestion>,
    val dictation: List<LessonQuestion>
)

/**
 * Presents every lesson sentence, then tests ten different sentences:
 * five by Czech-to-German writing and five by German audio dictation.
 */
internal fun createLessonPracticeSelection(
    questions: List<LessonQuestion>,
    random: Random = Random.Default
): LessonPracticeSelection {
    require(questions.size >= 10) {
        "A lesson needs at least ten sentences for five writing and five dictation questions."
    }

    val shuffled = questions.shuffled(random)
    return LessonPracticeSelection(
        introduction = shuffled,
        writing = shuffled.take(5),
        dictation = shuffled.drop(5).take(5)
    )
}

/**
 * Only harmless whitespace and Unicode composition are normalized.
 * Capitalization, punctuation, word order, spelling and umlauts remain strict.
 */
internal fun normalizeGermanWrittenAnswer(value: String): String {
    val normalizedWhitespace = value.trim().replace(Regex("\\s+"), " ")
    return Normalizer.normalize(normalizedWhitespace, Normalizer.Form.NFC)
}

internal fun isGermanWrittenAnswerCorrect(
    question: LessonQuestion,
    submitted: String
): Boolean {
    if (submitted.isBlank()) return false

    val normalizedSubmission = normalizeGermanWrittenAnswer(submitted)
    val acceptedAnswers = listOf(question.germanSentence) + question.acceptedAnswers

    return acceptedAnswers.any { expected ->
        expected.isNotBlank() && normalizeGermanWrittenAnswer(expected) == normalizedSubmission
    }
}
