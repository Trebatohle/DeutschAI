package com.deutschai.app

/**
 * First independently reviewed A1 vocabulary batch.
 * Words are selected for beginner everyday communication (Goethe Start Deutsch 1 scope).
 */
internal const val FREQUENCY_VOCABULARY_A1_BATCH1_COUNT = 50

private fun a1BatchId(kind: FrequencyWordKind, word: String, article: String?): String =
    if (article != null) {
        val legacyId = stableReviewId("vocabulary", article, word.lowercase())
        stableReviewId("vocabulary", legacyId)
    } else {
        stableReviewId("frequency_vocabulary", kind.name, word.lowercase())
    }

private fun a1BatchNoun(
    article: String, word: String, czech: String, plural: String,
    german: String, translation: String
) = FrequencyVocabularyEntry(
    id = a1BatchId(FrequencyWordKind.NOUN, word, article),
    level = "A1", kind = FrequencyWordKind.NOUN, word = word, czech = czech,
    article = article, extraInfo = plural, exampleGerman = german, exampleCzech = translation
)

private fun a1BatchVerb(
    word: String, czech: String, forms: String, german: String, translation: String,
    heard: String = word
) = FrequencyVocabularyEntry(
    id = a1BatchId(FrequencyWordKind.VERB, word, null),
    level = "A1", kind = FrequencyWordKind.VERB, word = word, czech = czech,
    extraInfo = forms, exampleGerman = german, exampleCzech = translation, listeningAnswer = heard
)

private fun a1BatchAdjective(word: String, czech: String, german: String, translation: String) =
    FrequencyVocabularyEntry(
        id = a1BatchId(FrequencyWordKind.ADJECTIVE, word, null),
        level = "A1", kind = FrequencyWordKind.ADJECTIVE, word = word, czech = czech,
        exampleGerman = german, exampleCzech = translation
    )

private fun a1BatchAdverb(word: String, czech: String, german: String, translation: String) =
    FrequencyVocabularyEntry(
        id = a1BatchId(FrequencyWordKind.ADVERB, word, null),
        level = "A1", kind = FrequencyWordKind.ADVERB, word = word, czech = czech,
        exampleGerman = german, exampleCzech = translation
    )

private fun a1BatchFunction(word: String, czech: String, german: String, translation: String) =
    FrequencyVocabularyEntry(
        id = a1BatchId(FrequencyWordKind.FUNCTION, word, null),
        level = "A1", kind = FrequencyWordKind.FUNCTION, word = word, czech = czech,
        exampleGerman = german, exampleCzech = translation
    )

internal val frequencyVocabularyA1Batch1: List<FrequencyVocabularyEntry> = listOf(
    // 25 nouns
    a1BatchNoun("die", "Bäckerei", "pekárna", "Pl.: die Bäckereien",
        "Ich kaufe Brot in der Bäckerei.", "Kupuji chléb v pekárně."),
    a1BatchNoun("die", "Briefmarke", "poštovní známka", "Pl.: die Briefmarken",
        "Eine Briefmarke kostet einen Euro.", "Jedna poštovní známka stojí jedno euro."),
    a1BatchNoun("das", "Café", "kavárna", "Pl.: die Cafés",
        "Wir treffen uns im Café.", "Setkáme se v kavárně."),
    a1BatchNoun("der", "Einkauf", "nákup", "Pl.: die Einkäufe",
        "Der Einkauf dauert nicht lange.", "Nákup netrvá dlouho."),
    a1BatchNoun("das", "Erdgeschoss", "přízemí", "Pl.: die Erdgeschosse",
        "Unsere Wohnung ist im Erdgeschoss.", "Náš byt je v přízemí."),
    a1BatchNoun("das", "Gemüse", "zelenina", "Obvykle bez množného čísla",
        "Ich esse jeden Tag Gemüse.", "Každý den jím zeleninu."),
    a1BatchNoun("das", "Gepäck", "zavazadla", "Obvykle bez množného čísla",
        "Mein Gepäck ist sehr schwer.", "Moje zavazadla jsou velmi těžká."),
    a1BatchNoun("die", "Hausaufgabe", "domácí úkol", "Pl.: die Hausaufgaben",
        "Ich mache jetzt meine Hausaufgabe.", "Teď si dělám domácí úkol."),
    a1BatchNoun("das", "Kleingeld", "drobné peníze", "Obvykle bez množného čísla",
        "Hast du etwas Kleingeld?", "Máš nějaké drobné?"),
    a1BatchNoun("der", "Kugelschreiber", "propisovací tužka", "Pl.: die Kugelschreiber",
        "Mein Kugelschreiber ist blau.", "Moje propisovací tužka je modrá."),
    a1BatchNoun("die", "Lebensmittel", "potraviny", "Používá se převážně v množném čísle",
        "Wir kaufen Lebensmittel im Supermarkt.", "Nakupujeme potraviny v supermarketu."),
    a1BatchNoun("die", "Lieblingsfarbe", "oblíbená barva", "Pl.: die Lieblingsfarben",
        "Blau ist meine Lieblingsfarbe.", "Modrá je moje oblíbená barva."),
    a1BatchNoun("das", "Obst", "ovoce", "Obvykle bez množného čísla",
        "Zum Frühstück esse ich Obst.", "K snídani jím ovoce."),
    a1BatchNoun("der", "Ort", "místo", "Pl.: die Orte",
        "Das ist ein schöner Ort.", "To je krásné místo."),
    a1BatchNoun("das", "Papier", "papír", "Pl.: die Papiere",
        "Ich brauche ein Blatt Papier.", "Potřebuji list papíru."),
    a1BatchNoun("die", "Person", "osoba", "Pl.: die Personen",
        "Eine Person wartet vor dem Haus.", "Jedna osoba čeká před domem."),
    a1BatchNoun("das", "Radio", "rádio", "Pl.: die Radios",
        "Am Morgen höre ich Radio.", "Ráno poslouchám rádio."),
    a1BatchNoun("der", "Reisepass", "cestovní pas", "Pl.: die Reisepässe",
        "Mein Reisepass ist noch gültig.", "Můj cestovní pas je stále platný."),
    a1BatchNoun("das", "Schiff", "loď", "Pl.: die Schiffe",
        "Das Schiff fährt nach Deutschland.", "Loď pluje do Německa."),
    a1BatchNoun("der", "Schirm", "deštník", "Pl.: die Schirme",
        "Nimm bitte deinen Schirm mit.", "Vezmi si prosím deštník."),
    a1BatchNoun("das", "Schlafzimmer", "ložnice", "Pl.: die Schlafzimmer",
        "Das Schlafzimmer ist oben.", "Ložnice je nahoře."),
    a1BatchNoun("das", "Schwimmbad", "plavecký bazén", "Pl.: die Schwimmbäder",
        "Wir gehen heute ins Schwimmbad.", "Dnes jdeme do bazénu."),
    a1BatchNoun("der", "Stadtplan", "plán města", "Pl.: die Stadtpläne",
        "Hast du einen Stadtplan?", "Máš plán města?"),
    a1BatchNoun("die", "Telefonnummer", "telefonní číslo", "Pl.: die Telefonnummern",
        "Wie ist deine Telefonnummer?", "Jaké je tvoje telefonní číslo?"),
    a1BatchNoun("die", "Zeitung", "noviny", "Pl.: die Zeitungen",
        "Mein Vater liest jeden Morgen die Zeitung.", "Můj otec čte každé ráno noviny."),

    // 10 verbs
    a1BatchVerb("bauen", "stavět", "baut · baute · hat gebaut",
        "Sie bauen ein neues Haus.", "Staví nový dům.", "bauen"),
    a1BatchVerb("beginnen", "začít / začínat", "beginnt · begann · hat begonnen",
        "Der Film beginnt um acht Uhr.", "Film začíná v osm hodin.", "beginnt"),
    a1BatchVerb("danken", "děkovat", "dankt · dankte · hat gedankt",
        "Ich danke dir für deine Hilfe.", "Děkuji ti za pomoc.", "danke"),
    a1BatchVerb("sich entschuldigen", "omluvit se", "entschuldigt sich · entschuldigte sich · hat sich entschuldigt",
        "Ich möchte mich entschuldigen.", "Chtěl bych se omluvit.", "entschuldigen"),
    a1BatchVerb("funktionieren", "fungovat", "funktioniert · funktionierte · hat funktioniert",
        "Mein Computer funktioniert wieder.", "Můj počítač zase funguje.", "funktioniert"),
    a1BatchVerb("gratulieren", "blahopřát", "gratuliert · gratulierte · hat gratuliert",
        "Ich gratuliere dir zum Geburtstag.", "Blahopřeji ti k narozeninám.", "gratulieren"),
    a1BatchVerb("grüßen", "pozdravit", "grüßt · grüßte · hat gegrüßt",
        "Bitte grüß deine Eltern von mir.", "Pozdravuj prosím ode mě své rodiče.", "grüß"),
    a1BatchVerb("probieren", "zkusit / ochutnat", "probiert · probierte · hat probiert",
        "Möchtest du den Kuchen probieren?", "Chceš ochutnat ten koláč?", "probieren"),
    a1BatchVerb("rufen", "volat / zavolat", "ruft · rief · hat gerufen",
        "Ich rufe dich heute Abend an.", "Zavolám ti dnes večer.", "rufe"),
    a1BatchVerb("wecken", "vzbudit", "weckt · weckte · hat geweckt",
        "Der Wecker weckt mich um sieben Uhr.", "Budík mě budí v sedm hodin.", "weckt"),

    // 8 adjectives
    a1BatchAdjective("billig", "levný", "Das Brot ist hier billig.", "Chléb je tady levný."),
    a1BatchAdjective("dunkel", "tmavý", "Im Winter wird es früh dunkel.", "V zimě se brzy stmívá."),
    a1BatchAdjective("frisch", "čerstvý", "Das Brot ist frisch.", "Chléb je čerstvý."),
    a1BatchAdjective("glücklich", "šťastný", "Ich bin heute sehr glücklich.", "Dnes jsem velmi šťastný."),
    a1BatchAdjective("hell", "světlý", "Das Zimmer ist hell.", "Pokoj je světlý."),
    a1BatchAdjective("kaputt", "rozbitý", "Mein Fahrrad ist kaputt.", "Moje kolo je rozbité."),
    a1BatchAdjective("leer", "prázdný", "Die Flasche ist leer.", "Láhev je prázdná."),
    a1BatchAdjective("nett", "milý", "Unsere Nachbarn sind sehr nett.", "Naši sousedé jsou velmi milí."),

    // 4 adverbs
    a1BatchAdverb("draußen", "venku", "Die Kinder spielen draußen.", "Děti si hrají venku."),
    a1BatchAdverb("früh", "brzy", "Ich stehe jeden Tag früh auf.", "Každý den brzy vstávám."),
    a1BatchAdverb("gestern", "včera", "Gestern war ich zu Hause.", "Včera jsem byl doma."),
    a1BatchAdverb("links", "vlevo", "Die Apotheke ist links.", "Lékárna je vlevo."),

    // 3 useful words
    a1BatchFunction("natürlich", "samozřejmě", "Natürlich helfe ich dir.", "Samozřejmě ti pomohu."),
    a1BatchFunction("wieder", "znovu / zase", "Wir sehen uns morgen wieder.", "Zítra se zase uvidíme."),
    a1BatchFunction("genug", "dost", "Das ist genug für heute.", "To je pro dnešek dost.")
)
