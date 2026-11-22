package com.deutschai.app

internal fun LessonQuestion.verbFocusTerm(topic: VerbPracticeLesson): String {
    val correctChoice = choices.getOrNull(correctIndex).orEmpty()
    val advancedInfinitive = Regex("""\(([^()]+)\)\s*$""")
        .find(correctChoice)
        ?.groupValues
        ?.getOrNull(1)
        ?.takeIf { it in topic.verbIds }

    return advancedInfinitive
        ?: topic.verbIds.firstOrNull { prompt.contains("„$it“") }
        ?: ""
}

private data class ArticleContextExample(
    val noun: String,
    val article: String,
    val sentenceWithBlank: String,
    val germanSentence: String,
    val translation: String
)

private val articleContextExamples = listOf(
    ArticleContextExample("Schlüssel", "Der", "_____ Schlüssel liegt neben der Tür.", "Der Schlüssel liegt neben der Tür.", "Klíč leží vedle dveří."),
    ArticleContextExample("Tisch", "Der", "_____ Tisch steht am Fenster.", "Der Tisch steht am Fenster.", "Stůl stojí u okna."),
    ArticleContextExample("Bahnhof", "Der", "_____ Bahnhof ist heute sehr voll.", "Der Bahnhof ist heute sehr voll.", "Nádraží je dnes velmi plné."),
    ArticleContextExample("Wohnung", "Die", "_____ Wohnung hat einen kleinen Balkon.", "Die Wohnung hat einen kleinen Balkon.", "Byt má malý balkon."),
    ArticleContextExample("Milch", "Die", "_____ Milch steht im Kühlschrank.", "Die Milch steht im Kühlschrank.", "Mléko je v lednici."),
    ArticleContextExample("Rechnung", "Die", "_____ Rechnung liegt auf dem Tisch.", "Die Rechnung liegt auf dem Tisch.", "Účet leží na stole."),
    ArticleContextExample("Buch", "Das", "_____ Buch liegt neben dem Telefon.", "Das Buch liegt neben dem Telefon.", "Kniha leží vedle telefonu."),
    ArticleContextExample("Wasser", "Das", "_____ Wasser ist heute besonders kalt.", "Das Wasser ist heute besonders kalt.", "Voda je dnes obzvlášť studená."),
    ArticleContextExample("Kind", "Das", "_____ Kind spielt im Garten.", "Das Kind spielt im Garten.", "Dítě si hraje na zahradě.")
)

internal fun buildArticleContextQuestions(): List<ReviewQuestion> {
    val choices = listOf("Der", "Die", "Das", "Den")
    return articleContextExamples.map { example ->
        ReviewQuestion(
            id = stableReviewId("article-context", example.noun),
            sourceKind = "articles",
            sourceTitle = "Členy · nové věty",
            prompt = "Doplň správný člen v 1. pádě:\n${example.sentenceWithBlank}",
            choices = choices,
            correctIndex = choices.indexOf(example.article),
            explanation = "„${example.article.lowercase()} ${example.noun}“ je správné spojení. V této větě podstatné jméno vystupuje jako podmět v 1. pádě.",
            germanSentence = example.germanSentence,
            translation = example.translation,
            cefrLevel = "A1",
            focusTopicId = "articles:nouns",
            focusTopicTitle = "Členy a podstatná jména",
            focusTerm = example.noun
        )
    }
}

private data class CaseContextExample(
    val id: String,
    val grammaticalCase: String,
    val sentenceWithBlank: String,
    val germanSentence: String,
    val translation: String,
    val correctAnswer: String,
    val choices: List<String>,
    val explanation: String
)

private val caseContextExamples = listOf(
    CaseContextExample(
        "nominative-mann", "Nominativ", "_____ Mann arbeitet heute.",
        "Der Mann arbeitet heute.", "Muž dnes pracuje.", "Der",
        listOf("Der", "Den", "Dem", "Des"),
        "„Der Mann“ je podmět věty, proto používáme 1. pád (Nominativ)."
    ),
    CaseContextExample(
        "nominative-frau", "Nominativ", "_____ Frau kommt später.",
        "Die Frau kommt später.", "Žena přijde později.", "Die",
        listOf("Die", "Der", "Den", "Das"),
        "„Die Frau“ je podmět věty, proto používáme 1. pád (Nominativ)."
    ),
    CaseContextExample(
        "nominative-kind", "Nominativ", "_____ Kind spielt draußen.",
        "Das Kind spielt draußen.", "Dítě si hraje venku.", "Das",
        listOf("Das", "Dem", "Den", "Der"),
        "„Das Kind“ je podmět věty, proto používáme 1. pád (Nominativ)."
    ),
    CaseContextExample(
        "accusative-mann", "Akkusativ", "Ich sehe _____ Mann.",
        "Ich sehe den Mann.", "Vidím toho muže.", "den",
        listOf("den", "der", "dem", "des"),
        "„Sehen“ zde vyžaduje 4. pád (Akkusativ): den Mann."
    ),
    CaseContextExample(
        "accusative-apfel", "Akkusativ", "Sie kauft _____ Apfel.",
        "Sie kauft den Apfel.", "Kupuje jablko.", "den",
        listOf("den", "der", "dem", "des"),
        "„Kaufen“ má přímý předmět ve 4. pádě: den Apfel."
    ),
    CaseContextExample(
        "accusative-brief", "Akkusativ", "Er liest _____ Brief.",
        "Er liest den Brief.", "Čte dopis.", "den",
        listOf("den", "der", "dem", "des"),
        "„Lesen“ má přímý předmět ve 4. pádě: den Brief."
    ),
    CaseContextExample(
        "dative-mann", "Dativ", "Ich helfe _____ Mann.",
        "Ich helfe dem Mann.", "Pomáhám tomu muži.", "dem",
        listOf("dem", "den", "der", "des"),
        "Sloveso „helfen“ se pojí se 3. pádem (Dativ): dem Mann."
    ),
    CaseContextExample(
        "dative-frau", "Dativ", "Sie dankt _____ Frau.",
        "Sie dankt der Frau.", "Děkuje té ženě.", "der",
        listOf("der", "den", "dem", "des"),
        "Sloveso „danken“ se pojí se 3. pádem (Dativ): der Frau."
    ),
    CaseContextExample(
        "dative-children", "Dativ", "Wir sprechen mit _____ Kindern.",
        "Wir sprechen mit den Kindern.", "Mluvíme s dětmi.", "den",
        listOf("den", "die", "der", "dem"),
        "Předložka „mit“ vyžaduje 3. pád. V množném čísle používáme „den“ a podstatné jméno často dostává -n."
    )
)

internal fun buildCaseContextQuestions(): List<ReviewQuestion> =
    caseContextExamples.map { example ->
        ReviewQuestion(
            id = stableReviewId("case-context", example.id),
            sourceKind = "wordtools",
            sourceTitle = "Pády · nové věty",
            prompt = "Doplň správný tvar členu:\n${example.sentenceWithBlank}",
            choices = example.choices,
            correctIndex = example.choices.indexOf(example.correctAnswer),
            explanation = example.explanation,
            germanSentence = example.germanSentence,
            translation = example.translation,
            cefrLevel = "A2",
            focusTopicId = "cases:declension",
            focusTopicTitle = "Pády",
            focusTerm = example.grammaticalCase
        )
    }
