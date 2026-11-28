package com.deutschai.app

private data class PrepositionContextExample(
    val id: String,
    val preposition: String,
    val sentenceWithBlank: String,
    val germanSentence: String,
    val translation: String,
    val explanation: String
)

/**
 * Additional offline examples for the three high-frequency two-way/contextual prepositions.
 * Each sentence is authored as a complete example; no grammar is guessed at runtime.
 */
private val prepositionContextExamples = listOf(
    PrepositionContextExample(
        id = "an-denken",
        preposition = "an",
        sentenceWithBlank = "Ich denke oft _____ meine Familie.",
        germanSentence = "Ich denke oft an meine Familie.",
        translation = "Často myslím na svou rodinu.",
        explanation = "Sloveso „denken“ se pojí s předložkou „an“ a 4. pádem: an meine Familie."
    ),
    PrepositionContextExample(
        id = "an-teilnehmen",
        preposition = "an",
        sentenceWithBlank = "Viele Teilnehmer nehmen _____ dem Kurs teil.",
        germanSentence = "Viele Teilnehmer nehmen an dem Kurs teil.",
        translation = "Mnoho účastníků se účastní kurzu.",
        explanation = "Vazba „an etwas teilnehmen“ používá 3. pád: an dem Kurs."
    ),
    PrepositionContextExample(
        id = "an-erinnern",
        preposition = "an",
        sentenceWithBlank = "Sie erinnert sich _____ den Termin.",
        germanSentence = "Sie erinnert sich an den Termin.",
        translation = "Vzpomíná si na termín.",
        explanation = "Vazba „sich an etwas erinnern“ používá 4. pád: an den Termin."
    ),
    PrepositionContextExample(
        id = "auf-warten",
        preposition = "auf",
        sentenceWithBlank = "Ich warte _____ den Bus.",
        germanSentence = "Ich warte auf den Bus.",
        translation = "Čekám na autobus.",
        explanation = "Sloveso „warten“ se pojí s „auf“ a 4. pádem: auf den Bus."
    ),
    PrepositionContextExample(
        id = "auf-freuen",
        preposition = "auf",
        sentenceWithBlank = "Er freut sich _____ den Urlaub.",
        germanSentence = "Er freut sich auf den Urlaub.",
        translation = "Těší se na dovolenou.",
        explanation = "„Sich auf etwas freuen“ vyjadřuje těšení se na budoucí událost a používá 4. pád."
    ),
    PrepositionContextExample(
        id = "auf-vorbereiten",
        preposition = "auf",
        sentenceWithBlank = "Wir bereiten uns _____ die Prüfung vor.",
        germanSentence = "Wir bereiten uns auf die Prüfung vor.",
        translation = "Připravujeme se na zkoušku.",
        explanation = "Vazba „sich auf etwas vorbereiten“ používá předložku „auf“ a 4. pád."
    ),
    PrepositionContextExample(
        id = "in-wohnen",
        preposition = "in",
        sentenceWithBlank = "Sie wohnt _____ einem kleinen Dorf.",
        germanSentence = "Sie wohnt in einem kleinen Dorf.",
        translation = "Bydlí v malé vesnici.",
        explanation = "Při popisu místa (kde?) používáme po „in“ 3. pád: in einem kleinen Dorf."
    ),
    PrepositionContextExample(
        id = "in-fahren",
        preposition = "in",
        sentenceWithBlank = "Wir fahren morgen _____ die Stadt.",
        germanSentence = "Wir fahren morgen in die Stadt.",
        translation = "Zítra jedeme do města.",
        explanation = "Při vyjádření směru (kam?) používáme po „in“ 4. pád: in die Stadt."
    ),
    PrepositionContextExample(
        id = "in-liegen",
        preposition = "in",
        sentenceWithBlank = "Der Schlüssel liegt _____ der Tasche.",
        germanSentence = "Der Schlüssel liegt in der Tasche.",
        translation = "Klíč leží v tašce.",
        explanation = "Při popisu polohy (kde?) používáme po „in“ 3. pád: in der Tasche."
    )
)

internal fun buildPrepositionContextQuestions(): List<ReviewQuestion> {
    val choices = listOf("an", "auf", "in", "neben")
    return prepositionContextExamples.map { example ->
        ReviewQuestion(
            id = stableReviewId("preposition-context", example.id),
            sourceKind = "wordtools",
            sourceTitle = "Předložky · nové věty",
            prompt = "Doplň správnou předložku:\n${example.sentenceWithBlank}",
            choices = choices,
            correctIndex = choices.indexOf(example.preposition),
            explanation = example.explanation,
            germanSentence = example.germanSentence,
            translation = example.translation,
            cefrLevel = "A2",
            focusTopicId = "wordtools:prepositions",
            focusTopicTitle = "Předložky",
            focusTerm = example.preposition
        )
    }
}
