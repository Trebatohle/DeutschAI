package com.deutschai.app

/**
 * Additional B1/B2 thematic sentence courses.
 *
 * Each topic starts from five reusable, level-appropriate subordinate clauses.
 * Five different sentence frames create 25 distinct Czech-to-German prompts.
 * Keeping the clauses as explicit data makes the vocabulary easy to mine later
 * for the shared vocabulary drill without duplicating the source sentences.
 */
private fun additionalLevelSentenceCourse(
    level: String,
    id: String,
    title: String,
    subtitle: String,
    vararg rows: String
): SentenceCourseDefinition = SentenceCourseDefinition(
    id = id,
    title = title,
    subtitle = subtitle,
    level = level,
    items = rows.map { row ->
        val fields = row.split("§", limit = 3)
        require(fields.size == 3) { "Invalid thematic sentence-course row: $row" }
        SentencePracticeItem(fields[0], fields[1], fields[2])
    }

)
