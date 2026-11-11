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
    a2PilotNoun("die", "Anmeldung", "přihlášení / registrace", "Pl.: die Anmeldungen",
        "Die Anmeldung für den Kurs ist noch möglich.", "Přihlášení do kurzu je stále možné."),
    a2PilotNoun("der", "Anrufbeantworter", "telefonní záznamník", "Pl.: die Anrufbeantworter",
        "Bitte sprich nach dem Ton auf den Anrufbeantworter.", "Po zaznění tónu prosím namluv zprávu na záznamník."),
    a2PilotNoun("die", "Antwort", "odpověď", "Pl.: die Antworten",
        "Ich kenne die Antwort auf diese Frage.", "Znám odpověď na tuto otázku."),
    a2PilotNoun("die", "Anzeige", "inzerát / oznámení", "Pl.: die Anzeigen",
        "Ich habe eine interessante Anzeige im Internet gefunden.", "Na internetu jsem našel zajímavý inzerát."),
    a2PilotNoun("der", "Apparat", "přístroj", "Pl.: die Apparate",
        "Der Apparat funktioniert nicht mehr.", "Ten přístroj už nefunguje."),
    a2PilotNoun("der", "Ausflug", "výlet", "Pl.: die Ausflüge",
        "Am Samstag machen wir einen Ausflug ans Meer.", "V sobotu pojedeme na výlet k moři."),
    a2PilotNoun("der", "Ausgang", "východ", "Pl.: die Ausgänge",
        "Der Ausgang ist gleich links neben der Tür.", "Východ je hned vlevo vedle dveří."),
    a2PilotNoun("der", "Bahnhof", "nádraží", "Pl.: die Bahnhöfe",
        "Ich hole dich am Bahnhof ab.", "Vyzvednu tě na nádraží."),
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
    a2PilotNoun("die", "Nachricht", "zpráva", "Pl.: die Nachrichten",
        "Ich schreibe dir später eine Nachricht.", "Později ti napíšu zprávu."),
    a2PilotNoun("das", "Rathaus", "radnice", "Pl.: die Rathäuser",
        "Das Rathaus ist im Stadtzentrum.", "Radnice je v centru města."),
    a2PilotNoun("die", "Postkarte", "pohlednice", "Pl.: die Postkarten",
        "Ich schicke meiner Oma eine Postkarte.", "Pošlu babičce pohlednici."),
    a2PilotNoun("die", "Postleitzahl", "poštovní směrovací číslo", "Pl.: die Postleitzahlen",
        "Wie lautet deine Postleitzahl?", "Jaké je tvoje poštovní směrovací číslo?"),
    a2PilotNoun("das", "Formular", "formulář", "Pl.: die Formulare",
        "Bitte füllen Sie dieses Formular aus.", "Vyplňte prosím tento formulář."),
    a2PilotNoun("der", "Raum", "místnost / prostor", "Pl.: die Räume",
        "Der Unterricht findet heute in Raum zehn statt.", "Výuka se dnes koná v místnosti číslo deset."),
    a2PilotNoun("die", "Erlaubnis", "povolení", "Pl.: die Erlaubnisse",
        "Für diese Reise brauche ich die Erlaubnis meiner Eltern.", "Na tuto cestu potřebuji povolení rodičů."),
    a2PilotNoun("die", "Veranstaltung", "akce / událost", "Pl.: die Veranstaltungen",
        "Die Veranstaltung beginnt um 18 Uhr.", "Akce začíná v 18 hodin."),
    a2PilotNoun("der", "Verein", "spolek / klub", "Pl.: die Vereine",
        "Mein Bruder spielt in einem Fußballverein.", "Můj bratr hraje ve fotbalovém klubu."),
    a2PilotNoun("das", "Zelt", "stan", "Pl.: die Zelte",
        "Im Sommer schlafen wir oft im Zelt.", "V létě často spíme ve stanu."),
    a2PilotNoun("die", "Garage", "garáž", "Pl.: die Garagen",
        "Das Auto steht heute in der Garage.", "Auto dnes stojí v garáži."),
    a2PilotNoun("der", "Gast", "host", "Pl.: die Gäste",
        "Heute Abend kommt ein Gast zum Essen.", "Dnes večer přijde na večeři host."),

    // Verbs
    a2PilotVerb("abgeben", "odevzdat / předat", "gibt ab · gab ab · hat abgegeben",
        "Ich gebe den Schlüssel an der Rezeption ab.", "Odevzdám klíč na recepci.", "gebe"),
    a2PilotVerb("abschließen", "zamknout / uzavřít", "schließt ab · schloss ab · hat abgeschlossen",
        "Ich schließe die Tür jeden Abend ab.", "Každý večer zamykám dveře.", "schließe"),
    a2PilotVerb("ansehen", "prohlédnout si / podívat se na", "sieht an · sah an · hat angesehen",
        "Ich sehe mir heute Abend einen Film an.", "Dnes večer se podívám na film.", "sehe mir"),
    a2PilotVerb("sich ärgern", "rozčilovat se", "ärgert sich · ärgerte sich · hat sich geärgert",
        "Ich ärgere mich über den Lärm.", "Rozčiluji se kvůli hluku.", "ärgere mich"),
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
    a2PilotAdjective("preiswert", "cenově výhodný / levný", "Das Hotel ist sauber und preiswert.", "Hotel je čistý a cenově výhodný.")
)
