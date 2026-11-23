package com.deutschai.app

internal const val FREQUENCY_VOCABULARY_A1_BATCH1_COUNT = 50

private fun a1BatchEntry(
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

internal val frequencyVocabularyA1Batch1: List<FrequencyVocabularyEntry> = listOf(
    a1BatchEntry(FrequencyWordKind.NOUN, "Mülleimer", "odpadkový koš", "Der Mülleimer steht in der Küche.", "Odpadkový koš stojí v kuchyni.", "der", "Pl.: die Mülleimer"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Briefmarke", "poštovní známka", "Ich brauche eine Briefmarke.", "Potřebuji poštovní známku.", "die", "Pl.: die Briefmarken"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Café", "kavárna", "Wir treffen uns im Café.", "Setkáme se v kavárně.", "das", "Pl.: die Cafés"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Einkauf", "nákup", "Der Einkauf dauert nicht lange.", "Nákup netrvá dlouho.", "der", "Pl.: die Einkäufe"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Erdgeschoss", "přízemí", "Meine Wohnung ist im Erdgeschoss.", "Můj byt je v přízemí.", "das"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Gemüse", "zelenina", "Wir kaufen frisches Gemüse.", "Kupujeme čerstvou zeleninu.", "das"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Gepäck", "zavazadla", "Mein Gepäck ist sehr schwer.", "Moje zavazadla jsou velmi těžká.", "das"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Hausaufgabe", "domácí úkol", "Ich mache meine Hausaufgabe.", "Dělám svůj domácí úkol.", "die", "Pl.: die Hausaufgaben"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Kleingeld", "drobné", "Hast du Kleingeld?", "Máš drobné?", "das"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Kugelschreiber", "kuličkové pero", "Mein Kugelschreiber ist blau.", "Moje kuličkové pero je modré.", "der", "Pl.: die Kugelschreiber"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Lebensmittel", "potraviny", "Die Lebensmittel sind im Kühlschrank.", "Potraviny jsou v lednici.", "die", "Pl.: die Lebensmittel"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Lieblingsfarbe", "oblíbená barva", "Blau ist meine Lieblingsfarbe.", "Modrá je moje oblíbená barva.", "die", "Pl.: die Lieblingsfarben"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Obst", "ovoce", "Ich esse jeden Tag Obst.", "Každý den jím ovoce.", "das"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Ort", "místo", "Der Ort ist sehr ruhig.", "To místo je velmi klidné.", "der", "Pl.: die Orte"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Papier", "papír", "Das Papier liegt auf dem Tisch.", "Papír leží na stole.", "das"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Person", "osoba", "Eine Person wartet draußen.", "Venku čeká jedna osoba.", "die", "Pl.: die Personen"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Radio", "rádio", "Das Radio ist zu laut.", "Rádio je příliš hlasité.", "das", "Pl.: die Radios"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Reisepass", "cestovní pas", "Wo ist mein Reisepass?", "Kde je můj cestovní pas?", "der", "Pl.: die Reisepässe"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Schiff", "loď", "Das Schiff fährt nach Italien.", "Loď jede do Itálie.", "das", "Pl.: die Schiffe"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Schirm", "deštník", "Nimm bitte deinen Schirm mit.", "Vezmi si prosím deštník.", "der", "Pl.: die Schirme"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Schlafzimmer", "ložnice", "Das Schlafzimmer ist oben.", "Ložnice je nahoře.", "das", "Pl.: die Schlafzimmer"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Schwimmbad", "plavecký bazén", "Das Schwimmbad ist heute offen.", "Bazén je dnes otevřený.", "das", "Pl.: die Schwimmbäder"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Stadtplan", "plán města", "Hast du einen Stadtplan?", "Máš plán města?", "der", "Pl.: die Stadtpläne"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Telefonnummer", "telefonní číslo", "Wie ist deine Telefonnummer?", "Jaké je tvoje telefonní číslo?", "die", "Pl.: die Telefonnummern"),
    a1BatchEntry(FrequencyWordKind.NOUN, "Zeitung", "noviny", "Ich lese jeden Morgen die Zeitung.", "Každé ráno čtu noviny.", "die", "Pl.: die Zeitungen"),

    a1BatchEntry(FrequencyWordKind.VERB, "bauen", "stavět", "Wir bauen ein kleines Haus.", "Stavíme malý dům."),
    a1BatchEntry(FrequencyWordKind.VERB, "beginnen", "začít", "Der Film beginnt um acht Uhr.", "Film začíná v osm hodin."),
    a1BatchEntry(FrequencyWordKind.VERB, "packen", "balit", "Ich packe meinen Koffer für die Reise.", "Balím si kufr na cestu."),
    a1BatchEntry(FrequencyWordKind.VERB, "sich entschuldigen", "omluvit se", "Ich entschuldige mich für den Fehler.", "Omlouvám se za chybu."),
    a1BatchEntry(FrequencyWordKind.VERB, "funktionieren", "fungovat", "Mein Computer funktioniert wieder.", "Můj počítač zase funguje."),
    a1BatchEntry(FrequencyWordKind.VERB, "gratulieren", "gratulovat", "Wir gratulieren dir zum Geburtstag.", "Gratulujeme ti k narozeninám."),
    a1BatchEntry(FrequencyWordKind.VERB, "grüßen", "pozdravit", "Bitte grüße deine Eltern von mir.", "Pozdravuj prosím ode mě své rodiče."),
    a1BatchEntry(FrequencyWordKind.VERB, "probieren", "vyzkoušet / ochutnat", "Möchtest du den Kuchen probieren?", "Chceš ochutnat ten koláč?"),
    a1BatchEntry(FrequencyWordKind.VERB, "rufen", "volat", "Ich rufe dich heute Abend an.", "Zavolám ti dnes večer."),
    a1BatchEntry(FrequencyWordKind.VERB, "wecken", "vzbudit", "Der Wecker weckt mich um sieben.", "Budík mě budí v sedm."),

    a1BatchEntry(FrequencyWordKind.ADJECTIVE, "billig", "levný", "Dieses Brot ist billig.", "Tento chléb je levný."),
    a1BatchEntry(FrequencyWordKind.ADJECTIVE, "dunkel", "tmavý", "Das Zimmer ist dunkel.", "Pokoj je tmavý."),
    a1BatchEntry(FrequencyWordKind.ADJECTIVE, "frisch", "čerstvý", "Das Brot ist frisch.", "Chléb je čerstvý."),
    a1BatchEntry(FrequencyWordKind.ADJECTIVE, "glücklich", "šťastný", "Sie ist heute sehr glücklich.", "Dnes je velmi šťastná."),
    a1BatchEntry(FrequencyWordKind.ADJECTIVE, "hell", "světlý", "Die Küche ist hell.", "Kuchyně je světlá."),
    a1BatchEntry(FrequencyWordKind.ADJECTIVE, "kaputt", "rozbitý", "Mein Handy ist kaputt.", "Můj mobil je rozbitý."),
    a1BatchEntry(FrequencyWordKind.ADJECTIVE, "leer", "prázdný", "Die Flasche ist leer.", "Láhev je prázdná."),
    a1BatchEntry(FrequencyWordKind.ADJECTIVE, "nett", "milý", "Unser Nachbar ist sehr nett.", "Náš soused je velmi milý."),

    a1BatchEntry(FrequencyWordKind.ADVERB, "draußen", "venku", "Die Kinder spielen draußen.", "Děti si hrají venku."),
    a1BatchEntry(FrequencyWordKind.ADVERB, "früh", "brzy", "Ich stehe morgen früh auf.", "Zítra brzy vstávám."),
    a1BatchEntry(FrequencyWordKind.ADVERB, "gestern", "včera", "Gestern war ich zu Hause.", "Včera jsem byl doma."),
    a1BatchEntry(FrequencyWordKind.ADVERB, "links", "vlevo", "Die Bank ist links.", "Banka je vlevo."),

    a1BatchEntry(FrequencyWordKind.FUNCTION, "natürlich", "samozřejmě", "Natürlich helfe ich dir.", "Samozřejmě ti pomohu."),
    a1BatchEntry(FrequencyWordKind.FUNCTION, "wieder", "znovu", "Wir sehen uns morgen wieder.", "Zítra se znovu uvidíme."),
    a1BatchEntry(FrequencyWordKind.FUNCTION, "genug", "dost", "Das ist genug für heute.", "To je pro dnešek dost.")
)
