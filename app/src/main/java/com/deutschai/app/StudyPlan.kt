package com.deutschai.app

import kotlin.math.roundToInt

internal enum class HomeRecommendationType {
    PLACEMENT,
    REVIEW_DUE,
    FOCUS_SKILL,
    CONTINUE_ACTIVITY,
    EXPLORE_LIBRARY
}

internal data class SkillLearningPerformance(
    val sourceKind: String,
    val title: String,
    val attempts: Int,
    val correctAnswers: Int,
    val accuracyPercent: Int,
    val trackedQuestions: Int
)

internal data class HomeStudyRecommendation(
    val type: HomeRecommendationType,
    val title: String,
    val explanation: String,
    val actionLabel: String,
    val sourceKind: String? = null,
    val dueCount: Int = 0,
    val accuracyPercent: Int? = null,
    val estimatedMinutes: Int = 5
)

/**
 * Summarizes only answers that have a recorded attempt count.
 * Old review records without answer-history data are deliberately excluded
 * rather than being presented as a guessed accuracy percentage.
 */
internal fun summarizeLearningSkillPerformance(
    entries: List<ReviewMemoryEntry>
): List<SkillLearningPerformance> =
    entries
        .filter { it.question.sourceKind.isNotBlank() && it.question.sourceKind != "placement" }
        .groupBy { it.question.sourceKind }
        .mapNotNull { (sourceKind, skillEntries) ->
            val attempts = skillEntries.sumOf { it.attemptCount.coerceAtLeast(0) }
            if (attempts <= 0) return@mapNotNull null

            val correct = skillEntries.sumOf { entry ->
                entry.correctCount.coerceIn(0, entry.attemptCount.coerceAtLeast(0))
            }.coerceAtMost(attempts)
            SkillLearningPerformance(
                sourceKind = sourceKind,
                title = learningSkillTitle(sourceKind),
                attempts = attempts,
                correctAnswers = correct,
                accuracyPercent = (correct * 100.0 / attempts).roundToInt(),
                trackedQuestions = skillEntries.size
            )
        }
        .sortedWith(
            compareBy<SkillLearningPerformance> { it.accuracyPercent }
                .thenByDescending { it.attempts }
        )

/**
 * Selects one useful next step from real local learning history.
 * Due reviews take priority, then a repeatedly weak skill, then onboarding
 * or a continuation/exploration prompt when the app has too little evidence.
 */
internal fun buildHomeStudyRecommendation(
    allEntries: List<ReviewMemoryEntry>,
    dueEntries: List<ReviewMemoryEntry>,
    hasPlacementResult: Boolean,
    hasLastActivity: Boolean
): HomeStudyRecommendation {
    if (dueEntries.isNotEmpty()) {
        val nextDue = dueEntries.first()
        val sourceKind = nextDue.question.sourceKind
        val skillTitle = learningSkillTitle(sourceKind)
        val matchingDueCount = dueEntries.count { it.question.sourceKind == sourceKind }
        val repeatedMistake = nextDue.wrongCount >= 2
        val dueExplanation = if (matchingDueCount == 1) {
            "1 položka z této oblasti je připravená k opakování."
        } else {
            matchingDueCount.toString() + " položky z této oblasti jsou připravené k opakování."
        }
        return HomeStudyRecommendation(
            type = HomeRecommendationType.REVIEW_DUE,
            title = "Opakuj: " + skillTitle,
            explanation = if (repeatedMistake) {
                "V této oblasti se ti už některé otázky opakovaně pletly. Teď je vhodný čas vrátit se k nim a upevnit si správné řešení."
            } else {
                dueExplanation + " Krátký návrat pomůže přesunout znalosti do dlouhodobé paměti."
            },
            actionLabel = "Spustit denní trénink",
            sourceKind = sourceKind,
            dueCount = dueEntries.size,
            estimatedMinutes = 5
        )
    }

    val weakestSkill = summarizeLearningSkillPerformance(allEntries)
        .firstOrNull { it.attempts >= 4 && it.accuracyPercent <= 75 }

    if (weakestSkill != null) {
        return HomeStudyRecommendation(
            type = HomeRecommendationType.FOCUS_SKILL,
            title = "Zaměř se na: " + weakestSkill.title,
            explanation = "V uložených odpovědích máš v této oblasti úspěšnost " +
                weakestSkill.accuracyPercent + " % (" + weakestSkill.attempts +
                " odpovědí). Doporučuji krátké cílené procvičení, než přejdeš k další látce.",
            actionLabel = "Procvičit tuto oblast",
            sourceKind = weakestSkill.sourceKind,
            accuracyPercent = weakestSkill.accuracyPercent,
            estimatedMinutes = 7
        )
    }

    if (!hasPlacementResult && allEntries.none { it.attemptCount > 0 }) {
        return HomeStudyRecommendation(
            type = HomeRecommendationType.PLACEMENT,
            title = "Nejdřív zjistíme, co už umíš",
            explanation = "Krátký rozřazovací test odhadne tvou výchozí úroveň a pomůže vybrat lekce, které pro tebe budou mít největší přínos.",
            actionLabel = "Zjistit svou úroveň",
            estimatedMinutes = 8
        )
    }

    if (hasLastActivity) {
        return HomeStudyRecommendation(
            type = HomeRecommendationType.CONTINUE_ACTIVITY,
            title = "Navazuj tam, kde jsi skončil/a",
            explanation = "Udrž návaznost ve studiu a pokračuj v poslední rozpracované aktivitě.",
            actionLabel = "Pokračovat v učení",
            estimatedMinutes = 5
        )
    }

    return HomeStudyRecommendation(
        type = HomeRecommendationType.EXPLORE_LIBRARY,
        title = "Vyber si další smysluplný krok",
        explanation = "Zvol si téma nebo dovednost, které chceš posílit. Výsledky dalších cvičení pomohou sestavit přesnější osobní plán.",
        actionLabel = "Vybrat první lekci",
        estimatedMinutes = 5
    )
}
