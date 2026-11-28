package com.deutschai.app

internal data class RecurringLearningError(
    val topicId: String,
    val topicTitle: String,
    val term: String,
    val totalWrongCount: Int,
    val affectedQuestionCount: Int,
    val lastAnsweredAtMillis: Long
) {
    val displayTitle: String
        get() = if (term.isNotBlank()) "$topicTitle · $term" else topicTitle

    val detail: String
        get() = if (term.isNotBlank()) {
            "Výraz „$term“ se ti v minulých otázkách opakovaně pletl. Procvičíš ho v dalších typech úloh."
        } else {
            "V tématu „$topicTitle“ se ti opakovaně objevovaly chyby. Dnešní trénink se k němu cíleně vrátí."
        }
}

/** Groups persisted mistakes by explicit curriculum metadata instead of guessing from sentence text. */
internal fun buildRecurringLearningErrors(
    entries: List<ReviewMemoryEntry>,
    minimumWrongCount: Int = 2,
    limit: Int = 5
): List<RecurringLearningError> {
    if (minimumWrongCount <= 0 || limit <= 0) return emptyList()
    return entries.asSequence()
        .filter { entry ->
            entry.question.focusTopicId.isNotBlank() &&
                entry.wrongCount > 0 &&
                !(entry.correctStreak >= 3 && entry.intervalDays >= 7)
        }
        .groupBy { entry ->
            entry.question.focusTopicId to entry.question.focusTerm.trim().lowercase()
        }
        .mapNotNull { (key, grouped) ->
            val wrongCount = grouped.sumOf { it.wrongCount }
            if (wrongCount < minimumWrongCount) return@mapNotNull null
            val latestQuestion = grouped.maxByOrNull { it.lastAnsweredAtMillis }?.question
            RecurringLearningError(
                topicId = key.first,
                topicTitle = latestQuestion?.focusTopicTitle?.takeIf { it.isNotBlank() }
                    ?: grouped.first().question.sourceTitle,
                term = latestQuestion?.focusTerm?.trim().orEmpty(),
                totalWrongCount = wrongCount,
                affectedQuestionCount = grouped.count { it.wrongCount > 0 },
                lastAnsweredAtMillis = grouped.maxOfOrNull { it.lastAnsweredAtMillis } ?: 0L
            )
        }
        .sortedWith(
            compareByDescending<RecurringLearningError> { it.totalWrongCount }
                .thenByDescending { it.affectedQuestionCount }
                .thenByDescending { it.lastAnsweredAtMillis }
        )
        .take(limit)
}

internal data class PersonalizedDailyTrainingPlan(
    val questions: List<ReviewQuestion>,
    val focusKind: String?,
    val focusAccuracyPercent: Int?,
    val hasReliableSkillData: Boolean,
    val dueReviewCount: Int,
    val newQuestionCountsByKind: Map<String, Int>,
    val focusError: RecurringLearningError? = null,
    val focusErrorQuestionCount: Int = 0
) {
    val focusTitle: String
        get() = when {
            focusError != null -> "Cílené procvičení: " + focusError.displayTitle
            focusKind != null -> "Doporučené zaměření: " + learningSkillTitle(focusKind)
            dueReviewCount > 0 -> "Nejdříve upevníme rozpracované znalosti"
            else -> "Vyvážené procvičování"
        }

    val focusDetail: String
        get() = when {
            focusError != null -> focusError.detail
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
    recurringErrors: List<RecurringLearningError> = emptyList(),
    count: Int = 10,
    maxDueCount: Int = 6,
    maxFocusQuestionCount: Int = 3
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

    // Prefer a repeated error with enough distinct questions available for a useful follow-up.
    val focusError = recurringErrors.firstOrNull { error ->
        candidates.any { candidate ->
            candidate.focusTopicId == error.topicId &&
                candidate.focusTerm.trim().equals(error.term.trim(), ignoreCase = true)
        }
    }
    val focusedQuestions = focusError?.let { error ->
        candidates.filter { candidate ->
            candidate.focusTopicId == error.topicId &&
                candidate.focusTerm.trim().equals(error.term.trim(), ignoreCase = true)
        }.take(minOf(maxFocusQuestionCount.coerceAtLeast(0), newSlots))
    }.orEmpty()
    selected += focusedQuestions
    focusedQuestions.forEach { question ->
        selectedByKind[question.sourceKind] = (selectedByKind[question.sourceKind] ?: 0) + 1
    }

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

    // Keep the remaining fresh questions spread across areas; relax the cap only if needed.
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
        newQuestionCountsByKind = selected.groupingBy { it.sourceKind }.eachCount(),
        focusError = focusError?.takeIf { focusedQuestions.isNotEmpty() },
        focusErrorQuestionCount = focusedQuestions.size
    )
}
