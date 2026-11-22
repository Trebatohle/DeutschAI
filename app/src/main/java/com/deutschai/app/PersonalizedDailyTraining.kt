package com.deutschai.app

internal data class PersonalizedDailyTrainingPlan(
    val questions: List<ReviewQuestion>,
    val focusKind: String?,
    val focusAccuracyPercent: Int?,
    val hasReliableSkillData: Boolean,
    val dueReviewCount: Int,
    val newQuestionCountsByKind: Map<String, Int>
) {
    val focusTitle: String
        get() = when {
            focusKind != null -> "Doporučené zaměření: " + learningSkillTitle(focusKind)
            dueReviewCount > 0 -> "Nejdříve upevníme rozpracované znalosti"
            else -> "Vyvážené procvičování"
        }

    val focusDetail: String
        get() = when {
            focusKind != null ->
                "V oblasti " + learningSkillTitle(focusKind) + " máš úspěšnost " +
                    (focusAccuracyPercent ?: 0) + " %. Trénink jí proto dá více prostoru."
            hasReliableSkillData ->
                "Žádná procvičovaná oblast výrazně nezaostává. Otázky střídají témata a udržují znalosti aktivní."
            else ->
                "Zatím sbíráme dost odpovědí pro přesnější doporučení. Trénink proto kombinuje dostupné oblasti rovnoměrně."
        }
}

internal fun buildPersonalizedDailyTrainingPlan(
    dueQuestions: List<ReviewQuestion>,
    unseenQuestions: List<ReviewQuestion>,
    practicedQuestions: List<ReviewQuestion>,
    skillStats: List<LearningSkillStats>,
    count: Int = 10,
    maxDueCount: Int = 6
): PersonalizedDailyTrainingPlan {
    if (count <= 0) {
        return PersonalizedDailyTrainingPlan(
            questions = emptyList(),
            focusKind = null,
            focusAccuracyPercent = null,
            hasReliableSkillData = false,
            dueReviewCount = 0,
            newQuestionCountsByKind = emptyMap()
        )
    }

    val due = dueQuestions.asSequence()
        .filterNot { isPlacementAssessmentQuestion(it) }
        .distinctBy { it.id }
        .take(minOf(maxDueCount.coerceAtLeast(0), count))
        .toList()
    val dueIds = due.map { it.id }.toSet()
    val candidates = (unseenQuestions + practicedQuestions).asSequence()
        .filterNot { it.id in dueIds || isPlacementAssessmentQuestion(it) }
        .distinctBy { it.id }
        .toList()
    val newSlots = (count - due.size).coerceAtLeast(0)
    val selected = mutableListOf<ReviewQuestion>()
    val selectedByKind = mutableMapOf<String, Int>()
    val statsByKind = skillStats.associateBy { it.sourceKind }
    val maxInitialPerKind = if (newSlots == 0) 0 else ((newSlots + 1) / 2).coerceAtLeast(1)

    fun priority(kind: String): Double {
        val stat = statsByKind[kind]
        return if (stat != null && stat.attempts >= 5) {
            (100 - stat.accuracyPercent).coerceAtLeast(1).toDouble()
        } else {
            20.0
        }
    }

    fun selectFresh(maxPerKind: Int) {
        while (selected.size < newSlots) {
            val remaining = candidates.filterNot { candidate -> selected.any { it.id == candidate.id } }
            if (remaining.isEmpty()) break
            val groups = remaining.groupBy { it.sourceKind }
            val eligible = groups.keys.filter { (selectedByKind[it] ?: 0) < maxPerKind }
                .ifEmpty { return }
            val chosenKind = eligible.maxByOrNull { kind ->
                priority(kind) / ((selectedByKind[kind] ?: 0) + 1).toDouble()
            } ?: break
            val next = groups.getValue(chosenKind).first()
            selected += next
            selectedByKind[chosenKind] = (selectedByKind[chosenKind] ?: 0) + 1
        }
    }

    // Keep the first half of the new questions spread across several areas, then
    // relax the cap only when a small content pool would otherwise leave gaps.
    if (newSlots > 0) {
        selectFresh(maxInitialPerKind)
        if (selected.size < newSlots) selectFresh(newSlots)
    }

    val selectedKinds = (due + selected).map { it.sourceKind }.toSet()
    val weakest = skillStats
        .filter { it.attempts >= 5 && it.accuracyPercent < 85 && it.sourceKind in selectedKinds }
        .minByOrNull { it.accuracyPercent }
    val hasReliableData = skillStats.any { it.attempts >= 5 && it.sourceKind in selectedKinds }

    return PersonalizedDailyTrainingPlan(
        questions = (due + selected).take(count),
        focusKind = weakest?.sourceKind,
        focusAccuracyPercent = weakest?.accuracyPercent,
        hasReliableSkillData = hasReliableData,
        dueReviewCount = due.size,
        newQuestionCountsByKind = selected.groupingBy { it.sourceKind }.eachCount()
    )
}
