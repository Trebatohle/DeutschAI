package com.deutschai.app

/**
 * Small, explicit A1 safety net for frequent beginner mistakes.
 *
 * This is not a full grammar checker. It catches only high-confidence patterns so that
 * the small on-device model cannot teach an obviously wrong correction for them.
 */
internal object GermanA1QuickReview {

    fun evaluate(question: String, answer: String): String? {
        val raw = answer.trim().trimEnd('.', '!', '?')
        if (raw.isBlank()) return null
        val normalized = normalize(raw)

        if (normalize(question).contains("wie heisst du")) {
            val nameMatch = Regex(
                "(?i)^(?:ich\\s+hei(?:ße|sse)|mein\\s+name\\s+ist|ich\\s+bin)\\s+([\\p{L}][\\p{L}'’-]*)$"
            ).find(raw)
            if (nameMatch != null) {
                val name = nameMatch.groupValues[1].replaceFirstChar { it.uppercase() }
                return "Oprava: „Ich heiße $name.“\n" +
                    "Vysvětlení: Tato věta je správná pro představení. Můžeš také říct „Mein Name ist $name.“"
            }

            if (Regex("^[\\p{L}][\\p{L}'’-]*$").matches(raw)) {
                val name = raw.replaceFirstChar { it.uppercase() }
                return "Můžeš odpovědět: „Ich heiße $name.“\n" +
                    "Vysvětlení: „Ich heiße …“ znamená „Jmenuji se …“."
            }
        }

        // German verb "sprechen": ich spreche, du sprichst, er/sie/es spricht.
        val languageMatch = Regex(
            "^(ich|du|er|sie|es)\\s+(\\S+)\\s+deutsch$"
        ).find(normalized)
        if (languageMatch != null) {
            val subject = languageMatch.groupValues[1]
            val verb = languageMatch.groupValues[2]
            val knownSpeakForms = setOf(
                "sprech", "spreche", "sprechen", "sprechst", "sprich", "sprichst",
                "spricht", "sprichts", "sprechst", "sprecht"
            )
            if (verb !in knownSpeakForms) return null

            val expectedVerb = when (subject) {
                "ich" -> "spreche"
                "du" -> "sprichst"
                else -> "spricht"
            }
            val expectedSentence = when (subject) {
                "ich" -> "Ich spreche Deutsch."
                "du" -> "Du sprichst Deutsch."
                "er" -> "Er spricht Deutsch."
                "sie" -> "Sie spricht Deutsch."
                else -> "Es spricht Deutsch."
            }
            val acceptedForms = when (subject) {
                "ich" -> setOf("spreche")
                "du" -> setOf("sprichst")
                else -> setOf("spricht")
            }
            val prefix = if (verb in acceptedForms) "Správně:" else "Oprava:"
            val explanation = when (subject) {
                "ich" -> "Po „ich“ používáme tvar „spreche“. Název jazyka „Deutsch“ píšeme s velkým písmenem."
                "du" -> "Po „du“ používáme tvar „sprichst“. Název jazyka „Deutsch“ píšeme s velkým písmenem."
                else -> "Po „er/sie/es“ používáme tvar „spricht“. Název jazyka „Deutsch“ píšeme s velkým písmenem."
            }
            return "$prefix „$expectedSentence“\nVysvětlení: $explanation"
        }

        // A highly common A1 word-order error: "ich gern Fußball spielen".
        if (
            normalized.startsWith("ich ") &&
            normalized.contains("fussball") &&
            (normalized.contains("gern") || normalized.contains("gerne")) &&
            normalized.contains("spiel")
        ) {
            val alreadyCorrect = Regex(
                "^ich\\s+spiele\\s+(?:gern|gerne)\\s+fussball$"
            ).matches(normalized)
            val prefix = if (alreadyCorrect) "Správně:" else "Oprava:"
            return "$prefix „Ich spiele gern Fußball.“\n" +
                "Vysvětlení: Po „ich“ má sloveso tvar „spiele“. Ve větě „Ich spiele gern Fußball“ je časované sloveso na druhém místě."
        }

        return null
    }

    /**
     * Rejects the most obvious failure mode: the model repeats the learner's incorrect
     * sentence as the correction and again as the explanation.
     */
    fun looksLikeRepeatedWrongAnswer(answer: String, response: String): Boolean {
        val input = normalize(answer).trim()
        if (input.length < 5) return false

        val correction = Regex(
            "(?is)\\b(?:oprava|spravne)\\s*:\\s*(.*?)(?=\\bvysvetleni\\s*:|$)"
        ).find(response)?.groupValues?.get(1)?.trim().orEmpty()
        val explanation = Regex(
            "(?is)\\bvysvetleni\\s*:\\s*(.*)$"
        ).find(response)?.groupValues?.get(1)?.trim().orEmpty()

        val compactCorrection = normalize(correction)
            .removeSuffix(" a")
            .removeSuffix(" and")
            .trim()
        val compactExplanation = normalize(explanation).trim()
        return compactCorrection == input || compactExplanation == input
    }

    private fun normalize(value: String): String {
        return java.text.Normalizer.normalize(
            value.lowercase().replace("ß", "ss"),
            java.text.Normalizer.Form.NFD
        )
            .replace(Regex("\\p{Mn}+"), "")
            .replace(Regex("[^\\p{L}\\p{N} ]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}
