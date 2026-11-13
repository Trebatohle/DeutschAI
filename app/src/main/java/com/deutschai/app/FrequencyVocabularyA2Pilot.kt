package com.deutschai.app

/**
 * First small, independently reviewable A2 vocabulary increment.
 *
 * Eligibility is based on the Goethe-Zertifikat A2 word list. Czech meanings and
 * example sentences below are authored for DeutschAI, not copied from that list.
 */
internal const val FREQUENCY_VOCABULARY_A2_PILOT_COUNT = 50

private fun a2PilotId(kind: FrequencyWordKind, word: String, article: String?): String =
    if (article != null) {
        val legacyId = stableReviewId("vocabulary", article, word.lowercase())
        stableReviewId("vocabulary", legacyId)
    } else {
        stableReviewId("frequency_vocabulary", kind.name, word.lowercase())
    }

private fun a2PilotNoun(
    article: String,
    word: String,
    czech: String,
    plural: String,
    german: String,
    translation: String
) = FrequencyVocabularyEntry(
    id = a2PilotId(FrequencyWordKind.NOUN, word, article),
    level = "A2",
    kind = FrequencyWordKind.NOUN,
    word = word,
    czech = czech,
    article = article,
    extraInfo = plural,
    exampleGerman = german,
    exampleCzech = translation
)

private fun a2PilotVerb(
    word: String,
    czech: String,
    forms: String,
    german: String,
    translation: String,
    heard: String
) = FrequencyVocabularyEntry(
    id = a2PilotId(FrequencyWordKind.VERB, word, null),
    level = "A2",
    kind = FrequencyWordKind.VERB,
    word = word,
    czech = czech,
    extraInfo = forms,
    exampleGerman = german,
    exampleCzech = translation,
    listeningAnswer = heard
)

private fun a2PilotAdjective(
    word: String,
    czech: String,
    german: String,
    translation: String
) = FrequencyVocabularyEntry(
    id = a2PilotId(FrequencyWordKind.ADJECTIVE, word, null),
    level = "A2",
    kind = FrequencyWordKind.ADJECTIVE,
    word = word,
    czech = czech,
    exampleGerman = german,
    exampleCzech = translation
)

private fun a2PilotAdverb(
    word: String,
    czech: String,
    german: String,
    translation: String
) = FrequencyVocabularyEntry(
    id = a2PilotId(FrequencyWordKind.ADVERB, word, null),
    level = "A2",
    kind = FrequencyWordKind.ADVERB,
    word = word,
    czech = czech,
    exampleGerman = german,
    exampleCzech = translation
)

internal val frequencyVocabularyA2Pilot: List<FrequencyVocabularyEntry> = listOf(
    // Nouns
    a2PilotNoun("der", "Anfang", "začátek", "Pl.: die Anfänge",
        "Am Anfang war ich etwas nervös.", "Na začátku jsem byl trochu nervózní."),

    a2PilotNoun("der", "Besuch", "návštěva", "Pl.: die Besuche",
        "Am Wochenende bekomme ich Besuch von meiner Schwester.", "O víkendu mě navštíví sestra."),
    a2PilotNoun("der", "Buchstabe", "písmeno", "Pl.: die Buchstaben",
        "Diesen Buchstaben gibt es in meiner Sprache nicht.", "Toto písmeno v mém jazyce neexistuje."),
    a2PilotNoun("der", "Führerschein", "řidičský průkaz", "Pl.: die Führerscheine",
        "Hast du schon deinen Führerschein?", "Už máš řidičský průkaz?"),
    a2PilotNoun("die", "Hochzeit", "svatba", "Pl.: die Hochzeiten",
        "Die Hochzeit meiner Schwester ist im Juni.", "Svatba mé sestry je v červnu."),
    a2PilotNoun("die", "Landschaft", "krajina", "Pl.: die Landschaften",
        "Die Landschaft hier ist sehr schön.", "Zdejší krajina je velmi krásná."),
    a2PilotNoun("die", "Notiz", "poznámka", "Pl.: die Notizen",
        "Schreib mir bitte eine kurze Notiz.", "Napiš mi prosím krátkou poznámku."),
    a2PilotNoun("das", "Lokal", "restaurace / podnik", "Pl.: die Lokale",
        "Kennst du ein gutes Lokal in der Nähe?", "Znáš v okolí dobrou restauraci?"),
    a2PilotNoun("die", "Party", "párty / oslava", "Pl.: die Partys",
        "Am Samstag machen wir eine Party.", "V sobotu pořádáme párty."),
    a2PilotNoun("der", "Kiosk", "kiosek", "Pl.: die Kioske",
        "Zeitungen kannst du am Kiosk kaufen.", "Noviny si můžeš koupit v kiosku."),
    a2PilotNoun("der", "Partner", "partner", "Pl.: die Partner",
        "Mein Partner kommt heute später nach Hause.", "Můj partner dnes přijde domů později."),
    a2PilotNoun("die", "Halle", "hala", "Pl.: die Hallen",
        "Wir treffen uns in Halle B.", "Sejdeme se v hale B."),
    a2PilotNoun("der", "Apparat", "přístroj", "Pl.: die Apparate",
        "Der Apparat funktioniert nicht mehr.", "Ten přístroj už nefunguje."),
    a2PilotNoun("der", "Ausflug", "výlet", "Pl.: die Ausflüge",
        "Am Samstag machen wir einen Ausflug ans Meer.", "V sobotu pojedeme na výlet k moři."),
    a2PilotNoun("die", "Abteilung", "oddělení", "Pl.: die Abteilungen",
        "In welcher Abteilung arbeitet sie?", "V jakém oddělení pracuje?"),
    a2PilotNoun("die", "Bäckerei", "pekárna", "Pl.: die Bäckereien",
        "Ich kaufe frische Brötchen in der Bäckerei.", "V pekárně kupuji čerstvé housky."),
    a2PilotNoun("das", "Doppelzimmer", "dvoulůžkový pokoj", "Pl.: die Doppelzimmer",
        "Wir brauchen ein Doppelzimmer für zwei Nächte.", "Potřebujeme dvoulůžkový pokoj na dvě noci."),
    a2PilotNoun("die", "Dame", "dáma / paní", "Pl.: die Damen",
        "Die Dame am Schalter hilft mir.", "Paní u přepážky mi pomáhá."),
    a2PilotNoun("die", "Geldbörse", "peněženka", "Pl.: die Geldbörsen",
        "Meine Geldbörse liegt noch zu Hause.", "Moje peněženka je ještě doma."),
    a2PilotNoun("der", "Glückwunsch", "blahopřání", "Pl.: die Glückwünsche",
        "Herzlichen Glückwunsch zum Geburtstag!", "Všechno nejlepší k narozeninám!"),
    a2PilotNoun("die", "Haltestelle", "zastávka", "Pl.: die Haltestellen",
        "Die nächste Haltestelle ist am Marktplatz.", "Další zastávka je na náměstí."),
    a2PilotNoun("die", "Ausstellung", "výstava", "Pl.: die Ausstellungen",
        "Die Ausstellung ist noch bis Sonntag geöffnet.", "Výstava je otevřená až do neděle."),
    a2PilotNoun("das", "Rathaus", "radnice", "Pl.: die Rathäuser",
        "Das Rathaus ist im Stadtzentrum.", "Radnice je v centru města."),
    a2PilotNoun("die", "Postkarte", "pohlednice", "Pl.: die Postkarten",
        "Ich schicke meiner Oma eine Postkarte.", "Pošlu babičce pohlednici."),
    a2PilotNoun("die", "Erlaubnis", "povolení", "Pl.: die Erlaubnisse",
        "Für diese Reise brauche ich die Erlaubnis meiner Eltern.", "Na tuto cestu potřebuji povolení rodičů."),
    a2PilotNoun("die", "Veranstaltung", "akce / událost", "Pl.: die Veranstaltungen",
        "Die Veranstaltung beginnt um 18 Uhr.", "Akce začíná v 18 hodin."),
    a2PilotNoun("der", "Verein", "spolek / klub", "Pl.: die Vereine",
        "Mein Bruder spielt in einem Fußballverein.", "Můj bratr hraje ve fotbalovém klubu."),

    // Verbs
    a2PilotVerb("abgeben", "odevzdat / předat", "gibt ab · gab ab · hat abgegeben",
        "Ich gebe den Schlüssel an der Rezeption ab.", "Odevzdám klíč na recepci.", "gebe"),
    a2PilotVerb("abschließen", "zamknout / uzavřít", "schließt ab · schloss ab · hat abgeschlossen",
        "Ich schließe die Tür jeden Abend ab.", "Každý večer zamykám dveře.", "schließe"),
    a2PilotVerb("verreisen", "odjet na dovolenou / cestovat", "verreist · verreiste · ist verreist",
        "Verreist ihr dieses Jahr in den Ferien?", "Odjedete letos o prázdninách na dovolenou?", "verreist"),
    a2PilotVerb("wählen", "zvolit / vybrat / telefonicky vytočit", "wählt · wählte · hat gewählt",
        "Bitte wählen Sie ein Getränk.", "Vyberte si prosím nápoj.", "wählen"),
    a2PilotVerb("diskutieren", "diskutovat", "diskutiert · diskutierte · hat diskutiert",
        "Wir diskutieren heute über den neuen Film.", "Dnes diskutujeme o novém filmu.", "diskutieren"),
    a2PilotVerb("fallen", "padat / spadnout", "fällt · fiel · ist gefallen",
        "Das Glas fällt gleich vom Tisch.", "Ta sklenice hned spadne ze stolu.", "fällt"),
    a2PilotVerb("rauchen", "kouřit", "raucht · rauchte · hat geraucht",
        "Hier darf man nicht rauchen.", "Tady se nesmí kouřit.", "rauchen"),
    a2PilotVerb("raten", "radit", "rät · riet · hat geraten",
        "Ich rate dir, früher ins Bett zu gehen.", "Radím ti chodit spát dříve.", "rate"),
    a2PilotVerb("überweisen", "poslat převodem / převést peníze", "überweist · überwies · hat überwiesen",
        "Ich überweise die Miete immer am ersten Tag des Monats.", "Nájem posílám převodem vždy první den v měsíci.", "überweise"),
    a2PilotVerb("markieren", "označit", "markiert · markierte · hat markiert",
        "Bitte markieren Sie die richtige Antwort.", "Označte prosím správnou odpověď.", "markieren"),
    a2PilotVerb("herunterladen", "stáhnout", "lädt herunter · lud herunter · hat heruntergeladen",
        "Ich lade die Datei gerade herunter.", "Právě stahuji soubor.", "lade herunter"),
    a2PilotVerb("frühstücken", "snídat", "frühstückt · frühstückte · hat gefrühstückt",
        "Ich frühstücke jeden Morgen um sieben Uhr.", "Každé ráno snídám v sedm hodin.", "frühstücke"),
    a2PilotVerb("baden", "koupat se", "badet · badete · hat gebadet",
        "Im Sommer bade ich gern im See.", "V létě se rád koupu v jezeře.", "bade"),
    a2PilotVerb("heiraten", "oženit se / vdát se", "heiratet · heiratete · hat geheiratet",
        "Meine Schwester möchte nächstes Jahr heiraten.", "Moje sestra se chce příští rok vdát.", "heiraten"),
    a2PilotVerb("notieren", "zapsat si / poznamenat si", "notiert · notierte · hat notiert",
        "Notieren Sie bitte meine Telefonnummer.", "Poznamenejte si prosím moje telefonní číslo.", "Notieren"),

    a2PilotVerb("danken", "děkovat", "dankt · dankte · hat gedankt",
        "Ich danke dir für deine Hilfe.", "Děkuji ti za pomoc.", "danke"),
    a2PilotVerb("drücken", "tlačit / stisknout", "drückt · drückte · hat gedrückt",
        "Du musst hier auf den Knopf drücken.", "Tady musíš stisknout tlačítko.", "drücken"),
    a2PilotVerb("beantworten", "zodpovědět", "beantwortet · beantwortete · hat beantwortet",
        "Kannst du meine Frage bitte beantworten?", "Můžeš prosím zodpovědět mou otázku?", "beantworten"),
    // Adjectives and adverbs
    a2PilotAdjective("arbeitslos", "nezaměstnaný", "Mein Onkel ist seit einem Jahr arbeitslos.", "Můj strýc je už rok nezaměstnaný."),
    a2PilotAdjective("bekannt", "známý", "Diese Stadt ist für ihren alten Markt bekannt.", "Toto město je známé svým starým tržištěm."),
    a2PilotAdverb("damals", "tehdy", "Damals wohnte ich noch bei meinen Eltern.", "Tehdy jsem ještě bydlel u rodičů."),
    a2PilotAdjective("fantastisch", "fantastický", "Der Ausflug war fantastisch.", "Ten výlet byl fantastický."),
    a2PilotAdjective("echt", "skutečný / opravdový", "Ist das ein echter Picasso?", "Je to skutečný Picasso?")
)
