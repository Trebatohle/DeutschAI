package com.deutschai.app

internal const val FREQUENCY_VOCABULARY_A1_BATCH2_COUNT = 39

private fun a1Batch2Entry(
    kind: FrequencyWordKind,
    word: String,
    czech: String,
    german: String,
    translation: String,
    article: String? = null,
    extra: String = ""
): FrequencyVocabularyEntry {
    val id = if (article != null) {
        val legacyId = stableReviewId("vocabulary", article, word.lowercase())
        stableReviewId("vocabulary", legacyId)
    } else {
        stableReviewId("frequency_vocabulary", kind.name, word.lowercase())
    }
    return FrequencyVocabularyEntry(
        id = id, level = "A1", kind = kind, word = word, czech = czech,
        article = article, extraInfo = extra, exampleGerman = german, exampleCzech = translation
    )
}

/** Second curated A1 batch: 39 entries checked against the complete existing frequency catalogue. */
internal val frequencyVocabularyA1Batch2: List<FrequencyVocabularyEntry> = listOf(
    // Nouns
    a1Batch2Entry(FrequencyWordKind.NOUN, "Briefumschlag", "obálka", "Der Briefumschlag liegt auf dem Tisch.", "Obálka leží na stole.", "der", "Pl.: die Briefumschläge"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Waschbecken", "umyvadlo", "Das Waschbecken ist sauber.", "Umyvadlo je čisté.", "das", "Pl.: die Waschbecken"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Bettwäsche", "ložní prádlo", "Die Bettwäsche ist frisch.", "Ložní prádlo je čisté.", "die"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Dachboden", "půda", "Die Kisten stehen auf dem Dachboden.", "Krabice stojí na půdě.", "der", "Pl.: die Dachböden"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Werkzeugkasten", "kufřík na nářadí", "Der Werkzeugkasten steht in der Garage.", "Kufřík na nářadí stojí v garáži.", "der", "Pl.: die Werkzeugkästen"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Verlängerungskabel", "prodlužovací kabel", "Das Verlängerungskabel ist zu kurz.", "Prodlužovací kabel je příliš krátký.", "das", "Pl.: die Verlängerungskabel"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Taschenlampe", "baterka", "Ich brauche eine Taschenlampe.", "Potřebuji baterku.", "die", "Pl.: die Taschenlampen"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Geschirrtuch", "utěrka na nádobí", "Das Geschirrtuch hängt in der Küche.", "Utěrka na nádobí visí v kuchyni.", "das", "Pl.: die Geschirrtücher"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Topflappen", "chňapka", "Der Topflappen liegt neben dem Herd.", "Chňapka leží vedle sporáku.", "der", "Pl.: die Topflappen"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Schöpfkelle", "naběračka", "Die Schöpfkelle liegt in der Schublade.", "Naběračka leží v zásuvce.", "die", "Pl.: die Schöpfkellen"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Pfandflasche", "vratná láhev", "Die Pfandflasche ist leer.", "Vratná láhev je prázdná.", "die", "Pl.: die Pfandflaschen"),
    a1Batch2Entry(FrequencyWordKind.NOUN, "Einkaufsbeleg", "doklad o nákupu", "Ich brauche den Einkaufsbeleg.", "Potřebuji doklad o nákupu.", "der", "Pl.: die Einkaufsbelege"),

    // Verbs
    a1Batch2Entry(FrequencyWordKind.VERB, "gießen", "zalévat", "Ich gieße jeden Morgen die Blumen.", "Každé ráno zalévám květiny."),
    a1Batch2Entry(FrequencyWordKind.VERB, "spülen", "mýt nádobí / oplachovat", "Wir spülen das Geschirr nach dem Essen.", "Po jídle myjeme nádobí."),
    a1Batch2Entry(FrequencyWordKind.VERB, "bügeln", "žehlit", "Ich bügle heute mein Hemd.", "Dnes žehlím svou košili."),
    a1Batch2Entry(FrequencyWordKind.VERB, "fegen", "zametat", "Er fegt den Hof.", "Zametá dvůr."),
    a1Batch2Entry(FrequencyWordKind.VERB, "abstellen", "odložit / postavit", "Stell die Tasche bitte hier ab.", "Odlož prosím tašku sem."),
    a1Batch2Entry(FrequencyWordKind.VERB, "ausleeren", "vyprázdnit", "Ich muss den Papierkorb ausleeren.", "Musím vyprázdnit koš na papír."),
    a1Batch2Entry(FrequencyWordKind.VERB, "ausziehen", "svléknout se / odstěhovat se", "Zieh bitte deine Jacke aus.", "Svlékni si prosím bundu."),

    // Adjectives
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "sauber", "čistý", "Die Küche ist sauber.", "Kuchyně je čistá."),
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "schmutzig", "špinavý", "Meine Schuhe sind schmutzig.", "Moje boty jsou špinavé."),
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "ordentlich", "pořádný / uklizený", "Sein Zimmer ist ordentlich.", "Jeho pokoj je uklizený."),
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "leicht", "lehký", "Die Tasche ist leicht.", "Taška je lehká."),
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "breit", "široký", "Die Straße ist sehr breit.", "Ulice je velmi široká."),
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "eng", "úzký / těsný", "Der Weg ist hier sehr eng.", "Cesta je tady velmi úzká."),
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "nötig", "nutný", "Eine Anmeldung ist nicht nötig.", "Registrace není nutná."),
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "gefährlich", "nebezpečný", "Diese Straße ist gefährlich.", "Tato ulice je nebezpečná."),
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "neugierig", "zvědavý", "Das Kind ist sehr neugierig.", "Dítě je velmi zvědavé."),
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "traurig", "smutný", "Warum bist du traurig?", "Proč jsi smutný?"),
    a1Batch2Entry(FrequencyWordKind.ADJECTIVE, "nass", "mokrý", "Meine Haare sind noch nass.", "Moje vlasy jsou ještě mokré."),

    // Adverbs and useful words
    a1Batch2Entry(FrequencyWordKind.ADVERB, "überall", "všude", "Überall liegen Bücher.", "Všude leží knihy."),
    a1Batch2Entry(FrequencyWordKind.ADVERB, "nirgendwo", "nikde", "Ich finde meine Schlüssel nirgendwo.", "Nikde nemohu najít své klíče."),
    a1Batch2Entry(FrequencyWordKind.ADVERB, "irgendwo", "někde", "Der Schlüssel muss irgendwo sein.", "Klíč musí být někde."),
    a1Batch2Entry(FrequencyWordKind.ADVERB, "weiter", "dál", "Gehen Sie bitte weiter.", "Jděte prosím dál."),
    a1Batch2Entry(FrequencyWordKind.ADVERB, "übermorgen", "pozítří", "Übermorgen habe ich frei.", "Pozítří mám volno."),
    a1Batch2Entry(FrequencyWordKind.ADVERB, "vorhin", "před chvílí", "Vorhin war er noch hier.", "Ještě před chvílí tu byl."),
    a1Batch2Entry(FrequencyWordKind.ADVERB, "hierher", "sem", "Komm bitte hierher.", "Pojď prosím sem."),
    a1Batch2Entry(FrequencyWordKind.ADVERB, "daneben", "vedle toho", "Der Stuhl steht direkt daneben.", "Židle stojí hned vedle."),
    a1Batch2Entry(FrequencyWordKind.ADVERB, "dazwischen", "mezi tím", "Der Tisch steht zwischen den Betten.", "Stůl stojí mezi postelemi.")
)
