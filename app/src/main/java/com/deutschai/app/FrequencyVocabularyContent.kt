package com.deutschai.app

internal enum class FrequencyWordKind(val label: String) {
    VERB("SLOVESO"),
    NOUN("PODSTATNÉ JMÉNO"),
    ADJECTIVE("PŘÍDAVNÉ JMÉNO"),
    ADVERB("PŘÍSLOVCE"),
    ARTICLE("ČLEN"),
    PRONOUN("ZÁJMENO"),
    QUESTION("TÁZACÍ SLOVO"),
    PREPOSITION("PŘEDLOŽKA"),
    CONJUNCTION("SPOJKA"),
    PARTICLE("ČÁSTICE"),
    FUNCTION("UŽITEČNÉ SLOVO")
}

internal data class FrequencyVocabularyEntry(
    val id: String,
    val level: String,
    val kind: FrequencyWordKind,
    val word: String,
    val czech: String,
    val article: String? = null,
    val extraInfo: String = "",
    val exampleGerman: String,
    val exampleCzech: String,
    val listeningAnswer: String = ""
) {
    val germanAnswer: String
        get() = if (article == null) word else article + " " + word

    val listeningTarget: String
        get() = listeningAnswer.ifBlank { if (kind == FrequencyWordKind.NOUN) word else germanAnswer }
}

private fun frequencyWord(
    level: String,
    kind: FrequencyWordKind,
    word: String,
    czech: String,
    exampleGerman: String,
    exampleCzech: String,
    article: String? = null,
    extraInfo: String = "",
    listeningAnswer: String = ""
): FrequencyVocabularyEntry {
    require(level in setOf("A1", "A2", "B1", "B2"))
    require(word.isNotBlank() && czech.isNotBlank())
    require(exampleGerman.isNotBlank() && exampleCzech.isNotBlank())
    require((kind == FrequencyWordKind.NOUN) == (article != null))
    if (article != null) require(article in setOf("der", "die", "das"))

    // Nouns deliberately reuse the legacy vocabulary review identity where possible.
    // That way a noun learned in the former noun drill keeps its existing history.
    val reviewId = if (article != null) {
        val legacyId = stableReviewId("vocabulary", article, word.lowercase())
        stableReviewId("vocabulary", legacyId)
    } else {
        stableReviewId("frequency_vocabulary", kind.name, word.lowercase())
    }
    return FrequencyVocabularyEntry(
        id = reviewId,
        level = level,
        kind = kind,
        word = word,
        czech = czech,
        article = article,
        extraInfo = extraInfo,
        exampleGerman = exampleGerman,
        exampleCzech = exampleCzech,
        listeningAnswer = listeningAnswer
    )
}

private fun fvVerb(
    level: String,
    word: String,
    czech: String,
    exampleGerman: String,
    exampleCzech: String,
    forms: String = "",
    heard: String = ""
) = frequencyWord(
    level, FrequencyWordKind.VERB, word, czech, exampleGerman, exampleCzech,
    extraInfo = forms, listeningAnswer = heard
)

private fun fvNoun(
    level: String,
    article: String,
    word: String,
    czech: String,
    plural: String,
    exampleGerman: String,
    exampleCzech: String,
    heard: String = ""
) = frequencyWord(
    level, FrequencyWordKind.NOUN, word, czech, exampleGerman, exampleCzech,
    article = article, extraInfo = plural, listeningAnswer = heard
)

private fun fvAdjective(
    level: String,
    word: String,
    czech: String,
    exampleGerman: String,
    exampleCzech: String,
    forms: String = ""
) = frequencyWord(
    level, FrequencyWordKind.ADJECTIVE, word, czech, exampleGerman, exampleCzech,
    extraInfo = forms
)

private fun fvAdverb(
    level: String,
    word: String,
    czech: String,
    exampleGerman: String,
    exampleCzech: String
) = frequencyWord(
    level, FrequencyWordKind.ADVERB, word, czech, exampleGerman, exampleCzech
)

private fun fvFunction(
    level: String,
    word: String,
    czech: String,
    exampleGerman: String,
    exampleCzech: String
) = frequencyWord(
    level, FrequencyWordKind.FUNCTION, word, czech, exampleGerman, exampleCzech
)

private fun fvArticle(level: String, word: String, czech: String, exampleGerman: String, exampleCzech: String) =
    frequencyWord(level, FrequencyWordKind.ARTICLE, word, czech, exampleGerman, exampleCzech)

private fun fvPronoun(level: String, word: String, czech: String, exampleGerman: String, exampleCzech: String, note: String = "") =
    frequencyWord(level, FrequencyWordKind.PRONOUN, word, czech, exampleGerman, exampleCzech, extraInfo = note)

private fun fvQuestion(level: String, word: String, czech: String, exampleGerman: String, exampleCzech: String) =
    frequencyWord(level, FrequencyWordKind.QUESTION, word, czech, exampleGerman, exampleCzech)

private fun fvPreposition(level: String, word: String, czech: String, exampleGerman: String, exampleCzech: String) =
    frequencyWord(level, FrequencyWordKind.PREPOSITION, word, czech, exampleGerman, exampleCzech)

private fun fvConjunction(level: String, word: String, czech: String, exampleGerman: String, exampleCzech: String) =
    frequencyWord(level, FrequencyWordKind.CONJUNCTION, word, czech, exampleGerman, exampleCzech)

private fun fvParticle(level: String, word: String, czech: String, exampleGerman: String, exampleCzech: String) =
    frequencyWord(level, FrequencyWordKind.PARTICLE, word, czech, exampleGerman, exampleCzech)

/**
 * Editorial frequency vocabulary for everyday spoken German.
 * The first two levels intentionally contain a high share of immediately useful verbs.
 * Examples and Czech translations are authored for DeutschAI; they are not copied corpus lines.
 */
internal val frequencyVocabularyCatalog: List<FrequencyVocabularyEntry> = listOf(
    // A1 — first words for everyday communication (verbs are deliberately front-loaded).
    fvVerb("A1", "sein", "být", "Ich bin heute zu Hause.", "Dnes jsem doma.", "ist · war · gewesen", "bin"),
    fvVerb("A1", "haben", "mít", "Ich habe heute Zeit.", "Dnes mám čas.", "hat · hatte · gehabt", "habe"),
    fvVerb("A1", "werden", "stávat se / být (budoucnost)", "Es wird schon dunkel.", "Už se stmívá.", "wird · wurde · geworden", "wird"),
    fvVerb("A1", "können", "moci / umět", "Kannst du mir helfen?", "Můžeš mi pomoct?", "kann · konnte · gekonnt", "kannst"),
    fvVerb("A1", "müssen", "muset", "Ich muss jetzt gehen.", "Teď musím jít.", "muss · musste · gemusst", "muss"),
    fvVerb("A1", "wollen", "chtít", "Wir wollen heute kochen.", "Dnes chceme vařit.", "will · wollte · gewollt", "wollen"),
    fvVerb("A1", "sollen", "mít (něco) udělat", "Du sollst mehr Wasser trinken.", "Měl bys pít více vody.", "soll · sollte · gesollt", "sollst"),
    fvVerb("A1", "dürfen", "smět", "Darf ich hier sitzen?", "Smím si tady sednout?", "darf · durfte · gedurft", "darf"),
    fvVerb("A1", "mögen", "mít rád", "Ich mag diesen Kaffee.", "Mám rád tuto kávu.", "mag · mochte · gemocht", "mag"),
    fvVerb("A1", "machen", "dělat", "Was machst du heute Abend?", "Co dnes večer děláš?", "macht · machte · gemacht", "machst"),
    fvVerb("A1", "gehen", "jít / chodit", "Ich gehe jetzt nach Hause.", "Teď jdu domů.", "geht · ging · ist gegangen", "gehe"),
    fvVerb("A1", "kommen", "přijít / přicházet", "Wann kommst du nach Hause?", "Kdy přijdeš domů?", "kommt · kam · ist gekommen", "kommst"),
    fvVerb("A1", "fahren", "jet / jezdit", "Wir fahren morgen nach Berlin.", "Zítra jedeme do Berlína.", "fährt · fuhr · ist gefahren", "fahren"),
    fvVerb("A1", "sehen", "vidět", "Ich sehe dich morgen.", "Uvidím tě zítra.", "sieht · sah · hat gesehen", "sehe"),
    fvVerb("A1", "geben", "dát / dávat", "Gib mir bitte einen Moment.", "Dej mi prosím chvilku.", "gibt · gab · hat gegeben", "Gib"),
    fvVerb("A1", "nehmen", "vzít / brát", "Ich nehme einen Kaffee.", "Dám si kávu.", "nimmt · nahm · hat genommen", "nehme"),
    fvVerb("A1", "finden", "najít / považovat", "Ich finde den Schlüssel nicht.", "Nemohu najít klíč.", "findet · fand · hat gefunden", "finde"),
    fvVerb("A1", "bleiben", "zůstat", "Wir bleiben heute zu Hause.", "Dnes zůstaneme doma.", "bleibt · blieb · ist geblieben", "bleiben"),
    fvVerb("A1", "wohnen", "bydlet", "Ich wohne in einer kleinen Stadt.", "Bydlím v malém městě.", "wohnt · wohnte · hat gewohnt", "wohne"),
    fvVerb("A1", "leben", "žít", "Meine Familie lebt in Tschechien.", "Moje rodina žije v Česku.", "lebt · lebte · hat gelebt", "lebt"),
    fvVerb("A1", "arbeiten", "pracovat", "Ich arbeite von Montag bis Freitag.", "Pracuji od pondělí do pátku.", "arbeitet · arbeitete · hat gearbeitet", "arbeite"),
    fvVerb("A1", "lernen", "učit se", "Ich lerne jeden Tag Deutsch.", "Každý den se učím německy.", "lernt · lernte · hat gelernt", "lerne"),
    fvVerb("A1", "sprechen", "mluvit", "Sprechen Sie bitte langsam.", "Mluvte prosím pomalu.", "spricht · sprach · hat gesprochen", "Sprechen"),
    fvVerb("A1", "sagen", "říci / říkat", "Was möchtest du sagen?", "Co chceš říct?", "sagt · sagte · hat gesagt", "sagen"),
    fvVerb("A1", "fragen", "zeptat se / ptát se", "Darf ich dich etwas fragen?", "Mohu se tě na něco zeptat?", "fragt · fragte · hat gefragt", "fragen"),
    fvVerb("A1", "antworten", "odpovědět / odpovídat", "Er antwortet sehr schnell.", "Odpovídá velmi rychle.", "antwortet · antwortete · hat geantwortet", "antwortet"),
    fvVerb("A1", "hören", "slyšet / poslouchat", "Ich höre gern Musik.", "Rád poslouchám hudbu.", "hört · hörte · hat gehört", "höre"),
    fvVerb("A1", "lesen", "číst", "Ich lese gerade ein Buch.", "Právě čtu knihu.", "liest · las · hat gelesen", "lese"),
    fvVerb("A1", "schreiben", "psát", "Ich schreibe dir später.", "Napíšu ti později.", "schreibt · schrieb · hat geschrieben", "schreibe"),
    fvVerb("A1", "essen", "jíst", "Wir essen heute zusammen.", "Dnes jíme společně.", "isst · aß · hat gegessen", "essen"),
    fvVerb("A1", "trinken", "pít", "Ich trinke morgens Tee.", "Ráno piji čaj.", "trinkt · trank · hat getrunken", "trinke"),
    fvVerb("A1", "schlafen", "spát", "Das Kind schläft schon.", "Dítě už spí.", "schläft · schlief · hat geschlafen", "schläft"),
    fvVerb("A1", "kaufen", "koupit / kupovat", "Ich kaufe heute Brot.", "Dnes koupím chleba.", "kauft · kaufte · hat gekauft", "kaufe"),
    fvVerb("A1", "brauchen", "potřebovat", "Brauchst du meine Hilfe?", "Potřebuješ mou pomoc?", "braucht · brauchte · hat gebraucht", "Brauchst"),
    fvVerb("A1", "suchen", "hledat", "Ich suche meine Brille.", "Hledám své brýle.", "sucht · suchte · hat gesucht", "suche"),
    fvVerb("A1", "spielen", "hrát", "Die Kinder spielen im Garten.", "Děti si hrají na zahradě.", "spielt · spielte · hat gespielt", "spielen"),
    fvVerb("A1", "lieben", "milovat", "Ich liebe diese Stadt.", "Miluji toto město.", "liebt · liebte · hat geliebt", "liebe"),
    fvVerb("A1", "kennen", "znát", "Kennst du diesen Mann?", "Znáš tohoto muže?", "kennt · kannte · hat gekannt", "Kennst"),
    fvVerb("A1", "wissen", "vědět", "Ich weiß die Antwort nicht.", "Neznám odpověď.", "weiß · wusste · hat gewusst", "weiß"),
    fvVerb("A1", "denken", "myslet", "Ich denke oft an dich.", "Často na tebe myslím.", "denkt · dachte · hat gedacht", "denke"),
    fvVerb("A1", "helfen", "pomáhat", "Kannst du mir bitte helfen?", "Můžeš mi prosím pomoct?", "hilft · half · hat geholfen", "helfen"),
    fvVerb("A1", "warten", "čekat", "Ich warte auf den Bus.", "Čekám na autobus.", "wartet · wartete · hat gewartet", "warte"),
    fvVerb("A1", "laufen", "běžet / chodit pěšky", "Ich laufe jeden Morgen.", "Každé ráno běhám.", "läuft · lief · ist gelaufen", "laufe"),
    fvVerb("A1", "heißen", "jmenovat se", "Wie heißt du?", "Jak se jmenuješ?", "heißt · hieß · hat geheißen", "heißt"),
    fvVerb("A1", "stehen", "stát", "Der Bus steht vor dem Haus.", "Autobus stojí před domem.", "steht · stand · hat gestanden", "steht"),
    fvVerb("A1", "sitzen", "sedět", "Ich sitze am Fenster.", "Sedím u okna.", "sitzt · saß · hat gesessen", "sitze"),
    fvVerb("A1", "liegen", "ležet", "Das Buch liegt auf dem Tisch.", "Kniha leží na stole.", "liegt · lag · hat gelegen", "liegt"),
    fvVerb("A1", "tun", "dělat / činit", "Was kann ich für dich tun?", "Co pro tebe mohu udělat?", "tut · tat · hat getan", "tun"),
    fvVerb("A1", "stellen", "postavit / umístit", "Ich stelle die Tasche hier ab.", "Položím tašku sem.", "stellt · stellte · hat gestellt", "stelle"),
    fvVerb("A1", "legen", "položit", "Leg das Buch bitte auf den Tisch.", "Polož prosím knihu na stůl.", "legt · legte · hat gelegt", "Leg"),
    fvVerb("A1", "setzen", "posadit / umístit", "Ich setze das Kind auf den Stuhl.", "Posadím dítě na židli.", "setzt · setzte · hat gesetzt", "setze"),
    fvVerb("A1", "zeigen", "ukázat", "Kannst du mir den Weg zeigen?", "Můžeš mi ukázat cestu?", "zeigt · zeigte · hat gezeigt", "zeigen"),
    fvVerb("A1", "zahlen", "zaplatit", "Ich zahle heute bar.", "Dnes platím hotově.", "zahlt · zahlte · hat gezahlt", "zahle"),
    fvVerb("A1", "telefonieren", "telefonovat", "Ich telefoniere mit meiner Mutter.", "Telefonuji s maminkou.", "telefoniert · telefonierte · hat telefoniert", "telefoniere"),
    fvVerb("A1", "studieren", "studovat na vysoké škole", "Er studiert in Berlin.", "Studuje v Berlíně.", "studiert · studierte · hat studiert", "studiert"),
    fvVerb("A1", "tanzen", "tančit", "Wir tanzen heute Abend.", "Dnes večer tančíme.", "tanzt · tanzte · hat getanzt", "tanzen"),
    fvVerb("A1", "schwimmen", "plavat", "Ich schwimme gern.", "Rád plavu.", "schwimmt · schwamm · ist geschwommen", "schwimme"),
    fvVerb("A1", "fliegen", "letět / létat", "Wir fliegen morgen nach Wien.", "Zítra letíme do Vídně.", "fliegt · flog · ist geflogen", "fliegen"),
    fvVerb("A1", "wünschen", "přát si / přát", "Ich wünsche dir einen schönen Tag.", "Přeji ti hezký den.", "wünscht · wünschte · hat gewünscht", "wünsche"),
    fvVerb("A1", "feiern", "slavit", "Wir feiern heute Geburtstag.", "Dnes slavíme narozeniny.", "feiert · feierte · hat gefeiert", "feiern"),
    fvVerb("A1", "üben", "procvičovat", "Ich übe jeden Tag Deutsch.", "Každý den procvičuji němčinu.", "übt · übte · hat geübt", "übe"),
    fvVerb("A1", "schauen", "dívat se", "Ich schaue gern Filme.", "Rád se dívám na filmy.", "schaut · schaute · hat geschaut", "schaue"),

    fvNoun("A1", "die", "Zeit", "čas", "Pl.: die Zeiten", "Hast du heute Zeit?", "Máš dnes čas?"),
    fvNoun("A1", "der", "Tag", "den", "Pl.: die Tage", "Heute ist ein schöner Tag.", "Dnes je krásný den."),
    fvNoun("A1", "der", "Freund", "kamarád / přítel", "Pl.: die Freunde", "Mein Freund kommt am Abend.", "Můj kamarád přijde večer."),
    fvNoun("A1", "die", "Familie", "rodina", "Pl.: die Familien", "Meine Familie wohnt in Brünn.", "Moje rodina bydlí v Brně."),
    fvNoun("A1", "die", "Frau", "žena / paní", "Pl.: die Frauen", "Die Frau wartet vor dem Haus.", "Ta žena čeká před domem."),
    fvNoun("A1", "der", "Mann", "muž", "Pl.: die Männer", "Der Mann spricht mit dem Arzt.", "Muž mluví s lékařem."),
    fvNoun("A1", "das", "Kind", "dítě", "Pl.: die Kinder", "Das Kind spielt im Zimmer.", "Dítě si hraje v pokoji."),
    fvNoun("A1", "die", "Arbeit", "práce", "Pl.: die Arbeiten", "Meine Arbeit beginnt um acht Uhr.", "Moje práce začíná v osm hodin."),
    fvNoun("A1", "das", "Haus", "dům", "Pl.: die Häuser", "Unser Haus ist nicht groß.", "Náš dům není velký."),
    fvNoun("A1", "die", "Wohnung", "byt", "Pl.: die Wohnungen", "Die Wohnung liegt im Zentrum.", "Byt se nachází v centru."),
    fvNoun("A1", "die", "Schule", "škola", "Pl.: die Schulen", "Die Schule ist nicht weit.", "Škola není daleko."),
    fvNoun("A1", "die", "Stadt", "město", "Pl.: die Städte", "Ich wohne in einer großen Stadt.", "Bydlím ve velkém městě."),
    fvNoun("A1", "das", "Geld", "peníze", "Obvykle bez množného čísla", "Hast du etwas Geld dabei?", "Máš u sebe nějaké peníze?"),
    fvNoun("A1", "das", "Wasser", "voda", "Obvykle bez množného čísla", "Ein Glas Wasser, bitte.", "Sklenici vody, prosím."),
    fvNoun("A1", "das", "Essen", "jídlo", "Obvykle bez množného čísla", "Das Essen ist schon fertig.", "Jídlo je už hotové."),
    fvNoun("A1", "der", "Name", "jméno", "Pl.: die Namen", "Wie ist dein Name?", "Jak se jmenuješ?"),

    fvAdjective("A1", "gut", "dobrý", "Das Essen schmeckt gut.", "Jídlo chutná dobře.", "besser · am besten"),
    fvAdjective("A1", "schlecht", "špatný", "Heute ist das Wetter schlecht.", "Dnes je špatné počasí."),
    fvAdjective("A1", "groß", "velký", "Das Zimmer ist sehr groß.", "Pokoj je velmi velký.", "größer · am größten"),
    fvAdjective("A1", "klein", "malý", "Ich habe eine kleine Tasche.", "Mám malou tašku."),
    fvAdjective("A1", "neu", "nový", "Mein Handy ist ganz neu.", "Můj telefon je úplně nový."),
    fvAdjective("A1", "alt", "starý", "Das Auto ist schon sehr alt.", "To auto je už velmi staré."),
    fvAdjective("A1", "schön", "hezký / krásný", "Wir haben einen schönen Garten.", "Máme krásnou zahradu."),
    fvAdjective("A1", "wichtig", "důležitý", "Das ist für mich sehr wichtig.", "To je pro mě velmi důležité."),
    fvAdjective("A1", "richtig", "správný", "Deine Antwort ist richtig.", "Tvoje odpověď je správná."),
    fvAdjective("A1", "falsch", "špatný / nesprávný", "Diese Adresse ist falsch.", "Tato adresa je špatná."),
    fvAdjective("A1", "schnell", "rychlý", "Der Zug ist sehr schnell.", "Vlak je velmi rychlý."),
    fvAdjective("A1", "langsam", "pomalý", "Kannst du bitte langsam sprechen?", "Můžeš prosím mluvit pomalu?"),
    fvAdjective("A1", "einfach", "jednoduchý", "Die Aufgabe ist ganz einfach.", "Úkol je úplně jednoduchý."),
    fvAdjective("A1", "schwer", "těžký / obtížný", "Die Frage ist nicht schwer.", "Ta otázka není obtížná."),
    fvAdverb("A1", "heute", "dnes", "Heute habe ich keine Zeit.", "Dnes nemám čas."),
    fvAdverb("A1", "morgen", "zítra", "Morgen fahren wir nach Wien.", "Zítra pojedeme do Vídně."),
    fvAdverb("A1", "jetzt", "teď", "Ich muss jetzt gehen.", "Teď musím jít."),
    fvAdverb("A1", "hier", "tady", "Warte bitte hier.", "Počkej prosím tady."),
    fvAdverb("A1", "dort", "tam", "Dort steht mein Auto.", "Tam stojí moje auto."),
    fvAdverb("A1", "immer", "vždy", "Er kommt immer pünktlich.", "Vždy chodí včas."),
    fvAdverb("A1", "oft", "často", "Wir sehen uns oft.", "Často se vídáme."),
    fvAdverb("A1", "zusammen", "společně", "Wir essen heute zusammen.", "Dnes jíme společně."),
    fvAdverb("A1", "allein", "sám / sama", "Ich wohne allein.", "Bydlím sám."),
    fvFunction("A1", "auch", "také", "Ich komme auch mit.", "Půjdu také s vámi."),
    fvFunction("A1", "nicht", "ne", "Ich verstehe das nicht.", "Tomu nerozumím."),
    fvFunction("A1", "aber", "ale", "Ich bin müde, aber zufrieden.", "Jsem unavený, ale spokojený."),
    fvFunction("A1", "und", "a", "Ich trinke Tee und Wasser.", "Piji čaj a vodu."),
    fvFunction("A1", "oder", "nebo", "Möchtest du Tee oder Kaffee?", "Chceš čaj, nebo kávu?"),
    fvFunction("A1", "weil", "protože", "Ich bleibe zu Hause, weil ich krank bin.", "Zůstanu doma, protože jsem nemocný."),
    fvFunction("A1", "ja", "ano", "Ja, das ist richtig.", "Ano, to je správně."),
    fvFunction("A1", "nein", "ne", "Nein, heute geht es nicht.", "Ne, dnes to nejde."),
    fvPronoun("A1", "ich", "já", "Ich lerne heute Deutsch.", "Dnes se učím německy."),
    fvPronoun("A1", "du", "ty", "Du bist heute sehr nett.", "Dnes jsi velmi milý."),
    fvPronoun("A1", "er", "on", "Er kommt gleich.", "Hned přijde."),
    fvPronoun("A1", "sie", "ona / oni", "Sie wohnen in Berlin.", "Bydlí v Berlíně.", "S velkým S: Sie = Vy (zdvořile)."),
    fvPronoun("A1", "es", "ono / to", "Es ist schon spät.", "Už je pozdě."),
    fvPronoun("A1", "wir", "my", "Wir gehen jetzt nach Hause.", "Teď jdeme domů."),
    fvPronoun("A1", "ihr", "vy (množné číslo)", "Ihr seid heute sehr früh.", "Dnes jste velmi brzy."),
    fvPronoun("A1", "man", "člověk / obecně se", "Hier spricht man Deutsch.", "Tady se mluví německy."),
    fvPronoun("A1", "alle", "všichni", "Alle sind schon da.", "Všichni už jsou tady."),
    fvQuestion("A1", "wer", "kdo", "Wer ist das?", "Kdo to je?"),
    fvQuestion("A1", "was", "co", "Was machst du heute?", "Co dnes děláš?"),
    fvQuestion("A1", "wo", "kde", "Wo wohnst du?", "Kde bydlíš?"),
    fvQuestion("A1", "wann", "kdy", "Wann kommst du?", "Kdy přijdeš?"),
    fvQuestion("A1", "wie", "jak", "Wie geht es dir?", "Jak se máš?"),
    fvQuestion("A1", "warum", "proč", "Warum bist du müde?", "Proč jsi unavený?"),
    fvQuestion("A1", "wohin", "kam", "Wohin gehen wir?", "Kam jdeme?"),
    fvPreposition("A1", "in", "v / do", "Ich wohne in Brünn.", "Bydlím v Brně."),
    fvPreposition("A1", "mit", "s / se", "Ich fahre mit dem Bus.", "Jedu autobusem."),
    fvPreposition("A1", "aus", "z / ze", "Ich komme aus Tschechien.", "Pocházím z Česka."),
    fvPreposition("A1", "für", "pro", "Das ist für dich.", "To je pro tebe."),
    fvPreposition("A1", "nach", "do / po", "Wir fahren nach Hause.", "Jedeme domů."),
    fvPreposition("A1", "zu", "k / do", "Ich gehe zu Fuß.", "Jdu pěšky."),
    fvPreposition("A1", "bei", "u / při", "Ich bin heute bei meiner Freundin.", "Dnes jsem u své kamarádky."),
    fvPreposition("A1", "an", "na / u", "Ich warte an der Haltestelle.", "Čekám na zastávce."),
    fvPreposition("A1", "auf", "na", "Das Buch liegt auf dem Tisch.", "Kniha leží na stole."),
    fvPreposition("A1", "von", "od / z", "Ich komme gerade von der Arbeit.", "Právě jdu z práce."),
    fvPreposition("A1", "um", "v / kolem", "Wir treffen uns um acht Uhr.", "Sejdeme se v osm hodin."),
    fvPreposition("A1", "vor", "před", "Der Bus wartet vor dem Haus.", "Autobus čeká před domem."),
    fvPreposition("A1", "ohne", "bez", "Einen Kaffee ohne Zucker, bitte.", "Jednu kávu bez cukru, prosím."),
    fvArticle("A1", "der", "určitý člen mužského rodu", "Der Bus kommt gleich.", "Autobus hned přijede."),
    fvArticle("A1", "die", "určitý člen ženského rodu a množného čísla", "Die Schule ist nicht weit.", "Škola není daleko."),
    fvArticle("A1", "das", "určitý člen středního rodu", "Das Kind spielt im Zimmer.", "Dítě si hraje v pokoji."),
    fvParticle("A1", "bitte", "prosím", "Ein Glas Wasser, bitte.", "Sklenici vody, prosím."),
    fvParticle("A1", "danke", "děkuji", "Danke, das ist sehr nett.", "Děkuji, to je velmi milé."),
    fvParticle("A1", "hallo", "ahoj / dobrý den", "Hallo, wie geht es dir?", "Ahoj, jak se máš?"),
    fvParticle("A1", "tschüss", "ahoj / na shledanou", "Tschüss, bis morgen!", "Ahoj, do zítra!"),
    fvParticle("A1", "nur", "jen / pouze", "Ich habe nur zehn Minuten.", "Mám jen deset minut."),
    fvParticle("A1", "mal", "chvilku / někdy (částice)", "Warte mal bitte.", "Počkej chvilku, prosím."),
    fvParticle("A1", "schon", "už", "Bist du schon fertig?", "Už jsi hotový?"),
    fvParticle("A1", "noch", "ještě", "Ich brauche noch fünf Minuten.", "Potřebuji ještě pět minut."),
    fvParticle("A1", "sehr", "velmi", "Das ist sehr gut.", "To je velmi dobré."),
    fvParticle("A1", "mehr", "více", "Ich möchte mehr Wasser.", "Chtěl bych více vody."),
    fvParticle("A1", "so", "tak", "Warum bist du so müde?", "Proč jsi tak unavený?"),
    fvParticle("A1", "dann", "potom / pak", "Dann sehen wir uns morgen.", "Tak se uvidíme zítra."),
    fvParticle("A1", "gern", "rád", "Ich trinke gern Kaffee.", "Rád piji kávu."),
    fvParticle("A1", "genau", "přesně", "Ja, genau!", "Ano, přesně!"),

    // A2 — everyday tasks, planning, travel, social life and common verb combinations.
    fvVerb("A2", "anfangen", "začít / začínat", "Der Film fängt um acht Uhr an.", "Film začíná v osm hodin.", "fängt an · fing an · hat angefangen", "fängt an"),
    fvVerb("A2", "aufhören", "přestat / končit", "Du kannst jetzt aufhören.", "Teď můžeš přestat.", "hört auf · hörte auf · hat aufgehört", "aufhören"),
    fvVerb("A2", "aufstehen", "vstávat", "Ich muss morgen früh aufstehen.", "Zítra musím brzy vstávat.", "steht auf · stand auf · ist aufgestanden", "aufstehen"),
    fvVerb("A2", "anrufen", "zavolat / telefonovat", "Ich möchte dich später anrufen.", "Chci ti později zavolat.", "ruft an · rief an · hat angerufen", "anrufen"),
    fvVerb("A2", "einkaufen", "nakupovat", "Wir müssen heute einkaufen.", "Dnes musíme nakoupit.", "kauft ein · kaufte ein · hat eingekauft", "einkaufen"),
    fvVerb("A2", "kochen", "vařit", "Heute koche ich für meine Freunde.", "Dnes vařím pro své přátele.", "kocht · kochte · hat gekocht", "koche"),
    fvVerb("A2", "backen", "péct", "Am Sonntag backen wir einen Kuchen.", "V neděli upečeme koláč.", "backt · buk · hat gebacken", "backen"),
    fvVerb("A2", "waschen", "mýt / prát", "Ich muss heute meine Kleidung waschen.", "Dnes musím vyprat své oblečení.", "wäscht · wusch · hat gewaschen", "waschen"),
    fvVerb("A2", "putzen", "čistit / uklízet", "Ich putze jeden Samstag die Küche.", "Každou sobotu uklízím kuchyň.", "putzt · putzte · hat geputzt", "putze"),
    fvVerb("A2", "öffnen", "otevřít / otevírat", "Kannst du bitte das Fenster öffnen?", "Můžeš prosím otevřít okno?", "öffnet · öffnete · hat geöffnet", "öffnen"),
    fvVerb("A2", "schließen", "zavřít / zavírat", "Der Laden schließt um sechs.", "Obchod zavírá v šest.", "schließt · schloss · hat geschlossen", "schließt"),
    fvVerb("A2", "bringen", "přinést / přivést", "Ich bringe dir morgen das Buch.", "Zítra ti přinesu knihu.", "bringt · brachte · hat gebracht", "bringe"),
    fvVerb("A2", "holen", "dojít pro / přinést", "Ich hole schnell Brot.", "Rychle dojdu pro chleba.", "holt · holte · hat geholt", "hole"),
    fvVerb("A2", "abholen", "vyzvednout", "Ich hole dich um sieben Uhr ab.", "Vyzvednu tě v sedm hodin.", "holt ab · holte ab · hat abgeholt", "hole ab"),
    fvVerb("A2", "mitbringen", "přinést s sebou", "Kannst du bitte etwas Wasser mitbringen?", "Můžeš prosím přinést trochu vody?", "bringt mit · brachte mit · hat mitgebracht", "mitbringen"),
    fvVerb("A2", "mitkommen", "jít / jet s někým", "Möchtest du heute Abend mitkommen?", "Chceš jít dnes večer s námi?", "kommt mit · kam mit · ist mitgekommen", "mitkommen"),
    fvVerb("A2", "mitnehmen", "vzít s sebou", "Ich möchte einen Regenschirm mitnehmen.", "Chci si vzít deštník s sebou.", "nimmt mit · nahm mit · hat mitgenommen", "mitnehmen"),
    fvVerb("A2", "treffen", "potkat / setkat se", "Ich treffe heute einen Freund.", "Dnes se setkám s kamarádem.", "trifft · traf · hat getroffen", "treffe"),
    fvVerb("A2", "besuchen", "navštívit", "Am Wochenende besuchen wir meine Eltern.", "O víkendu navštívíme rodiče.", "besucht · besuchte · hat besucht", "besuchen"),
    fvVerb("A2", "erzählen", "vyprávět", "Er erzählt gern von seiner Reise.", "Rád vypráví o své cestě.", "erzählt · erzählte · hat erzählt", "erzählt"),
    fvVerb("A2", "erklären", "vysvětlit", "Kannst du mir das bitte erklären?", "Můžeš mi to prosím vysvětlit?", "erklärt · erklärte · hat erklärt", "erklären"),
    fvVerb("A2", "verstehen", "rozumět / pochopit", "Ich verstehe die Frage jetzt.", "Teď té otázce rozumím.", "versteht · verstand · hat verstanden", "verstehe"),
    fvVerb("A2", "vergessen", "zapomenout", "Ich habe meinen Schlüssel vergessen.", "Zapomněl jsem klíč.", "vergisst · vergaß · hat vergessen", "vergessen"),
    fvVerb("A2", "sich erinnern", "vzpomínat / pamatovat si", "Ich erinnere mich an diesen Tag.", "Na tento den si pamatuji.", "erinnert sich · erinnerte sich · hat sich erinnert", "erinnere mich"),
    fvVerb("A2", "versuchen", "zkusit / pokusit se", "Ich versuche es noch einmal.", "Zkusím to ještě jednou.", "versucht · versuchte · hat versucht", "versuche"),
    fvVerb("A2", "entscheiden", "rozhodnout", "Wir müssen heute entscheiden.", "Dnes se musíme rozhodnout.", "entscheidet · entschied · hat entschieden", "entscheiden"),
    fvVerb("A2", "planen", "plánovat", "Wir planen eine Reise nach Hamburg.", "Plánujeme cestu do Hamburku.", "plant · plante · hat geplant", "planen"),
    fvVerb("A2", "reisen", "cestovat", "Ich reise gern mit dem Zug.", "Rád cestuji vlakem.", "reist · reiste · ist gereist", "reise"),
    fvVerb("A2", "buchen", "rezervovat / objednat", "Ich möchte ein Zimmer buchen.", "Chtěl bych rezervovat pokoj.", "bucht · buchte · hat gebucht", "buchen"),
    fvVerb("A2", "reservieren", "rezervovat", "Wir möchten einen Tisch reservieren.", "Chtěli bychom rezervovat stůl.", "reserviert · reservierte · hat reserviert", "reservieren"),
    fvVerb("A2", "umsteigen", "přestoupit", "Wir müssen in Wien umsteigen.", "Musíme přestoupit ve Vídni.", "steigt um · stieg um · ist umgestiegen", "umsteigen"),
    fvVerb("A2", "einsteigen", "nastoupit", "Bitte steigen Sie vorne ein.", "Nastupte prosím předními dveřmi.", "steigt ein · stieg ein · ist eingestiegen", "steigen ein"),
    fvVerb("A2", "aussteigen", "vystoupit", "Wir steigen an der nächsten Station aus.", "Vystoupíme na příští zastávce.", "steigt aus · stieg aus · ist ausgestiegen", "steigen aus"),
    fvVerb("A2", "ankommen", "dorazit / přijet", "Der Zug kommt pünktlich an.", "Vlak přijíždí včas.", "kommt an · kam an · ist angekommen", "kommt an"),
    fvVerb("A2", "abfahren", "odjet / odjíždět", "Der Bus fährt in fünf Minuten ab.", "Autobus odjíždí za pět minut.", "fährt ab · fuhr ab · ist abgefahren", "fährt ab"),
    fvVerb("A2", "kosten", "stát (o ceně)", "Wie viel kostet das Ticket?", "Kolik stojí jízdenka?", "kostet · kostete · hat gekostet", "kostet"),
    fvVerb("A2", "bezahlen", "zaplatit", "Kann ich mit Karte bezahlen?", "Mohu zaplatit kartou?", "bezahlt · bezahlte · hat bezahlt", "bezahlen"),
    fvVerb("A2", "verkaufen", "prodat / prodávat", "Sie verkaufen frisches Brot.", "Prodávají čerstvý chléb.", "verkauft · verkaufte · hat verkauft", "verkaufen"),
    fvVerb("A2", "bestellen", "objednat", "Ich möchte eine Suppe bestellen.", "Chtěl bych si objednat polévku.", "bestellt · bestellte · hat bestellt", "bestellen"),
    fvVerb("A2", "schicken", "poslat", "Ich schicke dir die Adresse.", "Pošlu ti adresu.", "schickt · schickte · hat geschickt", "schicke"),
    fvVerb("A2", "bekommen", "dostat / obdržet", "Hast du meine Nachricht bekommen?", "Dostal jsi mou zprávu?", "bekommt · bekam · hat bekommen", "bekommen"),
    fvVerb("A2", "benutzen", "používat", "Darf ich dein Telefon benutzen?", "Smím použít tvůj telefon?", "benutzt · benutzte · hat benutzt", "benutzen"),
    fvVerb("A2", "reparieren", "opravit", "Wir müssen das Fahrrad reparieren.", "Musíme opravit kolo.", "repariert · reparierte · hat repariert", "reparieren"),
    fvVerb("A2", "tragen", "nést / nosit", "Kannst du diese Tasche tragen?", "Můžeš nést tuto tašku?", "trägt · trug · hat getragen", "tragen"),
    fvVerb("A2", "ziehen", "táhnout / stěhovat se", "Wir ziehen nächstes Jahr um.", "Příští rok se stěhujeme.", "zieht · zog · ist gezogen", "ziehen"),
    fvVerb("A2", "umziehen", "přestěhovat se", "Sie möchte nach Berlin umziehen.", "Chce se přestěhovat do Berlína.", "zieht um · zog um · ist umgezogen", "umziehen"),
    fvVerb("A2", "aufwachen", "probudit se", "Ich wache um sieben Uhr auf.", "Probouzím se v sedm hodin.", "wacht auf · wachte auf · ist aufgewacht", "wache auf"),
    fvVerb("A2", "einschlafen", "usnout", "Das Kind schläft schnell ein.", "Dítě rychle usne.", "schläft ein · schlief ein · ist eingeschlafen", "schläft ein"),
    fvVerb("A2", "sich freuen", "radovat se / těšit se", "Ich freue mich auf das Wochenende.", "Těším se na víkend.", "freut sich · freute sich · hat sich gefreut", "freue mich"),
    fvVerb("A2", "sich beeilen", "pospíšit si", "Wir müssen uns beeilen.", "Musíme si pospíšit.", "beeilt sich · beeilte sich · hat sich beeilt", "uns beeilen"),
    fvVerb("A2", "sich fühlen", "cítit se", "Ich fühle mich heute gut.", "Dnes se cítím dobře.", "fühlt sich · fühlte sich · hat sich gefühlt", "fühle mich"),
    fvVerb("A2", "fehlen", "chybět", "Du fehlst mir.", "Chybíš mi.", "fehlt · fehlte · hat gefehlt", "fehlst"),
    fvVerb("A2", "lachen", "smát se", "Wir lachen viel zusammen.", "Hodně se spolu smějeme.", "lacht · lachte · hat gelacht", "lachen"),
    fvVerb("A2", "weinen", "plakat", "Das Baby weint.", "Miminko pláče.", "weint · weinte · hat geweint", "weint"),
    fvVerb("A2", "glauben", "věřit / myslet si", "Ich glaube dir.", "Věřím ti.", "glaubt · glaubte · hat geglaubt", "glaube"),
    fvVerb("A2", "hoffen", "doufat", "Ich hoffe, du hast Zeit.", "Doufám, že máš čas.", "hofft · hoffte · hat gehofft", "hoffe"),
    fvVerb("A2", "dauern", "trvat", "Die Fahrt dauert eine Stunde.", "Cesta trvá jednu hodinu.", "dauert · dauerte · hat gedauert", "dauert"),
    fvVerb("A2", "schmecken", "chutnat", "Die Suppe schmeckt sehr gut.", "Polévka chutná velmi dobře.", "schmeckt · schmeckte · hat geschmeckt", "schmeckt"),
    fvVerb("A2", "gehören", "patřit", "Das Buch gehört mir.", "Tato kniha mi patří.", "gehört · gehörte · hat gehört", "gehört"),
    fvVerb("A2", "sich vorstellen", "představit se", "Ich stelle mich kurz vor.", "Krátce se představím.", "stellt sich vor · stellte sich vor · hat sich vorgestellt", "stelle mich vor"),
    fvVerb("A2", "passieren", "stát se / přihodit se", "Was ist hier passiert?", "Co se tady stalo?", "passiert · passierte · ist passiert", "passiert"),
    fvVerb("A2", "ändern", "změnit", "Wir ändern den Plan.", "Změníme plán.", "ändert · änderte · hat geändert", "ändern"),
    fvVerb("A2", "sich ausruhen", "odpočinout si", "Ich ruhe mich am Wochenende aus.", "O víkendu si odpočinu.", "ruht sich aus · ruhte sich aus · hat sich ausgeruht", "ruhe mich aus"),
    fvVerb("A2", "aufräumen", "uklidit", "Ich räume heute mein Zimmer auf.", "Dnes si uklidím pokoj.", "räumt auf · räumte auf · hat aufgeräumt", "räume auf"),
    fvVerb("A2", "abwaschen", "umýt nádobí / umývat", "Ich wasche das Geschirr ab.", "Umývám nádobí.", "wäscht ab · wusch ab · hat abgewaschen", "wasche ab"),
    fvVerb("A2", "sich ärgern", "zlobit se", "Ich ärgere mich über den Fehler.", "Zlobím se kvůli té chybě.", "ärgert sich · ärgerte sich · hat sich geärgert", "ärgere mich"),

    fvNoun("A2", "der", "Termin", "termín / schůzka", "Pl.: die Termine", "Ich habe morgen einen Termin.", "Zítra mám schůzku."),
    fvNoun("A2", "die", "Nachricht", "zpráva", "Pl.: die Nachrichten", "Danke für deine Nachricht.", "Děkuji za tvoji zprávu."),
    fvNoun("A2", "die", "Frage", "otázka", "Pl.: die Fragen", "Ich habe noch eine Frage.", "Mám ještě jednu otázku."),
    fvNoun("A2", "die", "Antwort", "odpověď", "Pl.: die Antworten", "Deine Antwort ist richtig.", "Tvoje odpověď je správná."),
    fvNoun("A2", "der", "Weg", "cesta", "Pl.: die Wege", "Ist das der richtige Weg?", "Je to správná cesta?"),
    fvNoun("A2", "der", "Bahnhof", "nádraží", "Pl.: die Bahnhöfe", "Wir treffen uns am Bahnhof.", "Sejdeme se na nádraží."),
    fvNoun("A2", "der", "Zug", "vlak", "Pl.: die Züge", "Der Zug hat zehn Minuten Verspätung.", "Vlak má deset minut zpoždění."),
    fvNoun("A2", "der", "Bus", "autobus", "Pl.: die Busse", "Der Bus kommt gleich.", "Autobus přijede každou chvíli."),
    fvNoun("A2", "die", "Reise", "cesta / výlet", "Pl.: die Reisen", "Unsere Reise beginnt am Montag.", "Naše cesta začíná v pondělí."),
    fvNoun("A2", "der", "Urlaub", "dovolená", "Pl.: die Urlaube", "Im Sommer machen wir Urlaub.", "V létě jedeme na dovolenou."),
    fvNoun("A2", "der", "Arzt", "lékař", "Pl.: die Ärzte", "Ich muss morgen zum Arzt.", "Zítra musím k lékaři."),
    fvNoun("A2", "die", "Hilfe", "pomoc", "Obvykle bez množného čísla", "Danke für deine Hilfe.", "Děkuji za tvou pomoc."),
    fvNoun("A2", "das", "Problem", "problém", "Pl.: die Probleme", "Wir haben ein kleines Problem.", "Máme malý problém."),
    fvNoun("A2", "der", "Grund", "důvod", "Pl.: die Gründe", "Was ist der Grund dafür?", "Jaký je pro to důvod?"),
    fvNoun("A2", "die", "Meinung", "názor", "Pl.: die Meinungen", "Was ist deine Meinung?", "Jaký je tvůj názor?"),
    fvNoun("A2", "der", "Preis", "cena", "Pl.: die Preise", "Der Preis ist zu hoch.", "Cena je příliš vysoká."),

    fvAdjective("A2", "möglich", "možný", "Ist ein Termin am Freitag möglich?", "Je možný termín v pátek?"),
    fvAdjective("A2", "fertig", "hotový", "Bist du schon fertig?", "Už jsi hotový?"),
    fvAdjective("A2", "frei", "volný", "Ist dieser Platz noch frei?", "Je toto místo ještě volné?"),
    fvAdjective("A2", "krank", "nemocný", "Ich bin heute krank.", "Dnes jsem nemocný."),
    fvAdjective("A2", "gesund", "zdravý", "Bewegung ist gut und gesund.", "Pohyb je dobrý a zdravý."),
    fvAdjective("A2", "müde", "unavený", "Nach der Arbeit bin ich oft müde.", "Po práci bývám často unavený."),
    fvAdjective("A2", "beschäftigt", "zaneprázdněný", "Ich bin gerade beschäftigt.", "Právě jsem zaneprázdněný."),
    fvAdjective("A2", "freundlich", "přátelský / milý", "Die Mitarbeiter sind sehr freundlich.", "Zaměstnanci jsou velmi milí."),
    fvAdjective("A2", "teuer", "drahý", "Dieses Hotel ist zu teuer.", "Tento hotel je příliš drahý."),
    fvAdjective("A2", "günstig", "výhodný / levný", "Wir haben ein günstiges Hotel gefunden.", "Našli jsme výhodný hotel."),
    fvAdjective("A2", "anders", "jiný", "Heute machen wir es anders.", "Dnes to uděláme jinak."),
    fvAdverb("A2", "vielleicht", "možná", "Vielleicht komme ich etwas später.", "Možná přijdu o něco později."),
    fvAdverb("A2", "sofort", "ihned", "Ich rufe dich sofort zurück.", "Hned ti zavolám zpět."),
    fvAdverb("A2", "später", "později", "Wir sprechen später darüber.", "Promluvíme si o tom později."),
    fvAdverb("A2", "früher", "dříve", "Früher habe ich hier gewohnt.", "Dříve jsem tady bydlel."),
    fvAdverb("A2", "meistens", "většinou", "Meistens fahre ich mit dem Zug.", "Většinou jezdím vlakem."),
    fvAdverb("A2", "selten", "zřídka", "Ich esse selten Fleisch.", "Maso jím zřídka."),
    fvAdverb("A2", "ungefähr", "přibližně", "Die Fahrt dauert ungefähr eine Stunde.", "Cesta trvá přibližně hodinu."),
    fvAdverb("A2", "zuerst", "nejprve", "Zuerst trinken wir einen Kaffee.", "Nejprve si dáme kávu."),
    fvAdverb("A2", "danach", "potom", "Danach gehen wir nach Hause.", "Potom půjdeme domů."),
    fvFunction("A2", "deshalb", "proto", "Ich bin müde, deshalb bleibe ich zu Hause.", "Jsem unavený, proto zůstanu doma."),
    fvFunction("A2", "trotzdem", "přesto", "Es regnet, trotzdem gehen wir spazieren.", "Prší, přesto jdeme na procházku."),
    fvFunction("A2", "obwohl", "ačkoli", "Obwohl ich müde bin, lerne ich Deutsch.", "Ačkoli jsem unavený, učím se německy."),
    fvFunction("A2", "während", "během / zatímco", "Während ich koche, hört er Musik.", "Zatímco vařím, on poslouchá hudbu."),
    fvPronoun("A2", "jemand", "někdo", "Ist jemand zu Hause?", "Je někdo doma?"),
    fvPronoun("A2", "niemand", "nikdo", "Heute ist niemand im Büro.", "Dnes není v kanceláři nikdo."),
    fvPronoun("A2", "etwas", "něco", "Möchtest du etwas trinken?", "Chceš něco k pití?"),
    fvPronoun("A2", "nichts", "nic", "Ich habe nichts verstanden.", "Ničemu jsem nerozuměl."),
    fvPronoun("A2", "jeder", "každý", "Jeder macht Fehler.", "Každý dělá chyby."),
    fvPronoun("A2", "beide", "oba / obě", "Beide kommen heute.", "Oba dnes přijdou."),
    fvQuestion("A2", "wieso", "proč / jak to", "Wieso bist du heute so müde?", "Proč jsi dnes tak unavený?"),
    fvQuestion("A2", "woher", "odkud", "Woher kennst du ihn?", "Odkud ho znáš?"),
    fvPreposition("A2", "über", "nad / o", "Wir sprechen über den Urlaub.", "Mluvíme o dovolené."),
    fvPreposition("A2", "unter", "pod / mezi", "Der Schlüssel liegt unter dem Tisch.", "Klíč leží pod stolem."),
    fvPreposition("A2", "hinter", "za", "Das Auto steht hinter dem Haus.", "Auto stojí za domem."),
    fvPreposition("A2", "neben", "vedle", "Setz dich bitte neben mich.", "Posaď se prosím vedle mě."),
    fvPreposition("A2", "zwischen", "mezi", "Das Café liegt zwischen dem Bahnhof und der Schule.", "Kavárna leží mezi nádražím a školou."),
    fvPreposition("A2", "gegen", "proti / kolem", "Wir spielen heute gegen die Nachbarn.", "Dnes hrajeme proti sousedům."),
    fvPreposition("A2", "durch", "skrz / přes", "Wir gehen durch den Park.", "Jdeme přes park."),
    fvPreposition("A2", "seit", "od (časově)", "Ich wohne seit einem Jahr hier.", "Bydlím tady rok."),
    fvPreposition("A2", "bis", "do / až do", "Ich bleibe bis morgen.", "Zůstanu do zítřka."),
    fvConjunction("A2", "dass", "že", "Ich glaube, dass er heute kommt.", "Myslím, že dnes přijde."),
    fvConjunction("A2", "wenn", "když / jestliže", "Wenn ich Zeit habe, rufe ich dich an.", "Když budu mít čas, zavolám ti."),
    fvConjunction("A2", "als", "když (v minulosti) / jako", "Als ich klein war, wohnte ich hier.", "Když jsem byl malý, bydlel jsem tady."),
    fvConjunction("A2", "damit", "aby", "Ich spreche langsam, damit du mich verstehst.", "Mluvím pomalu, abys mi rozuměl."),
    fvParticle("A2", "eigentlich", "vlastně", "Was machst du eigentlich heute?", "Co vlastně dnes děláš?"),
    fvParticle("A2", "leider", "bohužel", "Leider habe ich heute keine Zeit.", "Bohužel dnes nemám čas."),
    fvParticle("A2", "sogar", "dokonce", "Sogar mein Bruder kann das.", "Dokonce i můj bratr to umí."),
    fvParticle("A2", "sonst", "jinak / jinak řečeno", "Beeil dich, sonst kommen wir zu spät.", "Pospěš si, jinak přijdeme pozdě."),
    fvParticle("A2", "erst", "teprve / až", "Ich komme erst um acht Uhr.", "Přijdu až v osm hodin."),
    fvParticle("A2", "bald", "brzy", "Bis bald!", "Brzy na viděnou!"),

    // B1 — opinions, work, decisions and more independent everyday communication.
    fvVerb("B1", "sich bewerben", "ucházet se o místo", "Ich bewerbe mich um eine neue Stelle.", "Ucházím se o nové pracovní místo.", "bewirbt sich · bewarb sich · hat sich beworben", "bewerbe mich"),
    fvVerb("B1", "gehören", "patřit", "Dieses Buch gehört meiner Schwester.", "Tato kniha patří mé sestře.", "gehört · gehörte · hat gehört", "gehört"),
    fvVerb("B1", "teilnehmen", "účastnit se", "Ich nehme an einem Sprachkurs teil.", "Účastním se jazykového kurzu.", "nimmt teil · nahm teil · hat teilgenommen", "nehme teil"),
    fvVerb("B1", "sich interessieren", "zajímat se", "Ich interessiere mich für Geschichte.", "Zajímám se o historii.", "interessiert sich · interessierte sich · hat sich interessiert", "interessiere mich"),
    fvVerb("B1", "sich kümmern", "starat se", "Ich kümmere mich um die Kinder.", "Starám se o děti.", "kümmert sich · kümmerte sich · hat sich gekümmert", "kümmere mich"),
    fvVerb("B1", "sich fühlen", "cítit se", "Heute fühle ich mich besser.", "Dnes se cítím lépe.", "fühlt sich · fühlte sich · hat sich gefühlt", "fühle mich"),
    fvVerb("B1", "anbieten", "nabídnout", "Das Hotel bietet ein Frühstück an.", "Hotel nabízí snídani.", "bietet an · bot an · hat angeboten", "bietet an"),
    fvVerb("B1", "empfehlen", "doporučit", "Kannst du mir ein gutes Buch empfehlen?", "Můžeš mi doporučit dobrou knihu?", "empfiehlt · empfahl · hat empfohlen", "empfehlen"),
    fvVerb("B1", "vorschlagen", "navrhnout", "Ich schlage ein anderes Restaurant vor.", "Navrhuji jinou restauraci.", "schlägt vor · schlug vor · hat vorgeschlagen", "schlage vor"),
    fvVerb("B1", "zustimmen", "souhlasit", "Ich stimme deinem Vorschlag zu.", "Souhlasím s tvým návrhem.", "stimmt zu · stimmte zu · hat zugestimmt", "stimme zu"),
    fvVerb("B1", "ablehnen", "odmítnout", "Sie lehnt das Angebot höflich ab.", "Zdvořile odmítá nabídku.", "lehnt ab · lehnte ab · hat abgelehnt", "lehnt ab"),
    fvVerb("B1", "vergleichen", "porovnat", "Wir vergleichen heute zwei Angebote.", "Dnes porovnáme dvě nabídky.", "vergleicht · verglich · hat verglichen", "vergleichen"),
    fvVerb("B1", "beschreiben", "popsat", "Kannst du den Weg beschreiben?", "Můžeš popsat cestu?", "beschreibt · beschrieb · hat beschrieben", "beschreiben"),
    fvVerb("B1", "entwickeln", "vyvíjet / rozvíjet", "Wir entwickeln eine neue Idee.", "Rozvíjíme nový nápad.", "entwickelt · entwickelte · hat entwickelt", "entwickeln"),
    fvVerb("B1", "verbessern", "zlepšit", "Ich möchte mein Deutsch verbessern.", "Chci zlepšit svou němčinu.", "verbessert · verbesserte · hat verbessert", "verbessern"),
    fvVerb("B1", "vermeiden", "vyhnout se", "Ich versuche, Fehler zu vermeiden.", "Snažím se vyhýbat chybám.", "vermeidet · vermied · hat vermieden", "vermeiden"),
    fvVerb("B1", "erreichen", "dosáhnout", "Wir erreichen unser Ziel gemeinsam.", "Společně dosáhneme svého cíle.", "erreicht · erreichte · hat erreicht", "erreichen"),
    fvVerb("B1", "erwarten", "očekávat", "Ich erwarte heute einen Anruf.", "Dnes očekávám telefonát.", "erwartet · erwartete · hat erwartet", "erwarte"),
    fvVerb("B1", "hoffen", "doufat", "Ich hoffe auf gutes Wetter.", "Doufám v dobré počasí.", "hofft · hoffte · hat gehofft", "hoffe"),
    fvVerb("B1", "passieren", "stát se / přihodit se", "Was ist gestern passiert?", "Co se včera stalo?", "passiert · passierte · ist passiert", "passiert"),
    fvVerb("B1", "stattfinden", "konat se", "Das Treffen findet morgen statt.", "Setkání se koná zítra.", "findet statt · fand statt · hat stattgefunden", "findet statt"),
    fvVerb("B1", "gewinnen", "vyhrát / získat", "Unsere Mannschaft gewinnt das Spiel.", "Náš tým vyhraje zápas.", "gewinnt · gewann · hat gewonnen", "gewinnt"),
    fvVerb("B1", "verlieren", "ztratit / prohrát", "Ich möchte keine Zeit verlieren.", "Nechci ztrácet čas.", "verliert · verlor · hat verloren", "verlieren"),

    fvNoun("B1", "der", "Vorteil", "výhoda", "Pl.: die Vorteile", "Ein großer Vorteil ist die Lage.", "Velkou výhodou je poloha."),
    fvNoun("B1", "der", "Nachteil", "nevýhoda", "Pl.: die Nachteile", "Jede Lösung hat auch einen Nachteil.", "Každé řešení má také nevýhodu."),
    fvNoun("B1", "die", "Entscheidung", "rozhodnutí", "Pl.: die Entscheidungen", "Das war eine schwierige Entscheidung.", "Bylo to těžké rozhodnutí."),
    fvNoun("B1", "die", "Erfahrung", "zkušenost", "Pl.: die Erfahrungen", "Die Erfahrungen waren sehr wichtig.", "Tyto zkušenosti byly velmi důležité.", "Erfahrungen"),
    fvNoun("B1", "das", "Verhalten", "chování", "Obvykle bez množného čísla", "Sein Verhalten war sehr freundlich.", "Jeho chování bylo velmi přátelské."),
    fvNoun("B1", "die", "Beziehung", "vztah", "Pl.: die Beziehungen", "Eine gute Beziehung braucht Vertrauen.", "Dobrý vztah potřebuje důvěru."),
    fvNoun("B1", "die", "Umgebung", "okolí / prostředí", "Pl.: die Umgebungen", "Die Umgebung ist hier sehr ruhig.", "Okolí je zde velmi klidné."),
    fvNoun("B1", "die", "Gesellschaft", "společnost", "Pl.: die Gesellschaften", "Die Gesellschaft verändert sich schnell.", "Společnost se rychle mění."),
    fvNoun("B1", "die", "Lösung", "řešení", "Pl.: die Lösungen", "Wir brauchen eine praktische Lösung.", "Potřebujeme praktické řešení."),
    fvNoun("B1", "die", "Ursache", "příčina", "Pl.: die Ursachen", "Die Ursache des Problems ist noch unklar.", "Příčina problému je stále nejasná."),
    fvNoun("B1", "das", "Ziel", "cíl", "Pl.: die Ziele", "Unser Ziel ist ein gutes Ergebnis.", "Naším cílem je dobrý výsledek."),
    fvNoun("B1", "die", "Möglichkeit", "možnost", "Pl.: die Möglichkeiten", "Gibt es eine andere Möglichkeit?", "Existuje jiná možnost?"),
    fvNoun("B1", "die", "Entwicklung", "vývoj", "Pl.: die Entwicklungen", "Die Entwicklung ist positiv.", "Vývoj je pozitivní."),
    fvNoun("B1", "die", "Verantwortung", "odpovědnost", "Obvykle bez množného čísla", "Jeder trägt Verantwortung.", "Každý nese odpovědnost."),
    fvNoun("B1", "die", "Kenntnis", "znalost", "Pl.: die Kenntnisse", "Gute Sprachkenntnisse sind wichtig.", "Dobré jazykové znalosti jsou důležité."),

    fvAdjective("B1", "wahrscheinlich", "pravděpodobný", "Er kommt wahrscheinlich etwas später.", "Pravděpodobně přijde o něco později."),
    fvAdjective("B1", "tatsächlich", "skutečný", "Das ist tatsächlich eine gute Idee.", "To je skutečně dobrý nápad."),
    fvAdjective("B1", "bestimmt", "určitý / jistý", "Sie kommt bestimmt morgen.", "Určitě přijde zítra."),
    fvAdjective("B1", "regelmäßig", "pravidelný", "Ich gehe regelmäßig schwimmen.", "Pravidelně chodím plavat."),
    fvAdjective("B1", "gemeinsam", "společný", "Wir suchen gemeinsam eine Lösung.", "Společně hledáme řešení."),
    fvAdjective("B1", "verantwortlich", "odpovědný", "Wer ist dafür verantwortlich?", "Kdo za to odpovídá?"),
    fvAdjective("B1", "zufrieden", "spokojený", "Bist du mit dem Ergebnis zufrieden?", "Jsi s výsledkem spokojený?"),
    fvAdjective("B1", "notwendig", "nezbytný", "Eine Pause ist manchmal notwendig.", "Přestávka je někdy nezbytná."),
    fvAdjective("B1", "verschieden", "různý", "Wir haben verschiedene Möglichkeiten.", "Máme různé možnosti."),
    fvAdjective("B1", "ähnlich", "podobný", "Unsere Meinungen sind sehr ähnlich.", "Naše názory jsou si velmi podobné."),
    fvAdverb("B1", "inzwischen", "mezitím / mezitím už", "Inzwischen ist es dunkel geworden.", "Mezitím se setmělo."),
    fvAdverb("B1", "schließlich", "nakonec", "Schließlich haben wir eine Lösung gefunden.", "Nakonec jsme našli řešení."),
    fvFunction("B1", "sobald", "jakmile", "Sobald ich Zeit habe, rufe ich dich an.", "Jakmile budu mít čas, zavolám ti."),
    fvFunction("B1", "dennoch", "přesto", "Es war schwierig, dennoch haben wir es geschafft.", "Bylo to obtížné, přesto jsme to zvládli."),
    fvFunction("B1", "außerdem", "kromě toho", "Außerdem brauchen wir mehr Zeit.", "Kromě toho potřebujeme více času."),
    fvFunction("B1", "insbesondere", "zejména", "Das gilt insbesondere für Anfänger.", "To platí zejména pro začátečníky."),
    fvFunction("B1", "währenddessen", "mezitím", "Ich koche, währenddessen deckt er den Tisch.", "Já vařím a mezitím on prostírá stůl."),

    // B2 — nuance, argumentation, longer conversations and work-related everyday speech.
    fvVerb("B2", "beeinflussen", "ovlivňovat", "Das Wetter beeinflusst meine Stimmung.", "Počasí ovlivňuje mou náladu.", "beeinflusst · beeinflusste · hat beeinflusst", "beeinflusst"),
    fvVerb("B2", "berücksichtigen", "zohlednit", "Wir müssen alle Kosten berücksichtigen.", "Musíme zohlednit všechny náklady.", "berücksichtigt · berücksichtigte · hat berücksichtigt", "berücksichtigen"),
    fvVerb("B2", "überzeugen", "přesvědčit", "Seine Argumente haben mich überzeugt.", "Jeho argumenty mě přesvědčily.", "überzeugt · überzeugte · hat überzeugt", "überzeugt"),
    fvVerb("B2", "feststellen", "zjistit / konstatovat", "Wir haben einen Fehler festgestellt.", "Zjistili jsme chybu.", "stellt fest · stellte fest · hat festgestellt", "festgestellt"),
    fvVerb("B2", "behaupten", "tvrdit", "Er behauptet, dass alles in Ordnung ist.", "Tvrdí, že je všechno v pořádku.", "behauptet · behauptete · hat behauptet", "behauptet"),
    fvVerb("B2", "beweisen", "dokázat", "Kannst du das wirklich beweisen?", "Dokážeš to opravdu?", "beweist · bewies · hat bewiesen", "beweisen"),
    fvVerb("B2", "voraussetzen", "předpokládat / vyžadovat", "Diese Arbeit setzt Erfahrung voraus.", "Tato práce vyžaduje zkušenosti.", "setzt voraus · setzte voraus · hat vorausgesetzt", "setzt voraus"),
    fvVerb("B2", "ermöglichen", "umožnit", "Die neue Regel ermöglicht mehr Flexibilität.", "Nové pravidlo umožňuje větší flexibilitu.", "ermöglicht · ermöglichte · hat ermöglicht", "ermöglicht"),
    fvVerb("B2", "entsprechen", "odpovídat", "Das Ergebnis entspricht unseren Erwartungen.", "Výsledek odpovídá našim očekáváním.", "entspricht · entsprach · hat entsprochen", "entspricht"),
    fvVerb("B2", "entstehen", "vzniknout", "Wie ist dieses Problem entstanden?", "Jak tento problém vznikl?", "entsteht · entstand · ist entstanden", "entstanden"),
    fvVerb("B2", "bestehen", "skládat se / existovat", "Das Team besteht aus fünf Personen.", "Tým se skládá z pěti lidí.", "besteht · bestand · hat bestanden", "besteht"),
    fvVerb("B2", "sich verhalten", "chovat se", "Wie soll ich mich in dieser Situation verhalten?", "Jak se mám v této situaci zachovat?", "verhält sich · verhielt sich · hat sich verhalten", "verhalten"),
    fvVerb("B2", "wahrnehmen", "vnímat / zaznamenat", "Ich nehme eine Veränderung wahr.", "Vnímám změnu.", "nimmt wahr · nahm wahr · hat wahrgenommen", "nehme wahr"),
    fvVerb("B2", "vermitteln", "zprostředkovat", "Sie vermittelt zwischen den beiden Gruppen.", "Zprostředkovává komunikaci mezi oběma skupinami.", "vermittelt · vermittelte · hat vermittelt", "vermittelt"),
    fvVerb("B2", "beitragen", "přispět", "Jeder kann zu einer guten Lösung beitragen.", "Každý může přispět k dobrému řešení.", "trägt bei · trug bei · hat beigetragen", "beitragen"),
    fvVerb("B2", "bewältigen", "zvládnout", "Gemeinsam können wir diese Aufgabe bewältigen.", "Společně můžeme tento úkol zvládnout.", "bewältigt · bewältigte · hat bewältigt", "bewältigen"),
    fvVerb("B2", "verzichten", "vzdát se", "Ich möchte nicht auf meinen Urlaub verzichten.", "Nechci se vzdát své dovolené.", "verzichtet · verzichtete · hat verzichtet", "verzichten"),
    fvVerb("B2", "übernehmen", "převzít", "Ich kann diese Aufgabe übernehmen.", "Mohu tento úkol převzít.", "übernimmt · übernahm · hat übernommen", "übernehmen"),
    fvVerb("B2", "durchführen", "provést / uskutečnit", "Wir führen morgen eine Kontrolle durch.", "Zítra provedeme kontrolu.", "führt durch · führte durch · hat durchgeführt", "führen durch"),
    fvVerb("B2", "gewährleisten", "zajistit / garantovat", "Diese Maßnahme soll die Sicherheit gewährleisten.", "Toto opatření má zajistit bezpečnost.", "gewährleistet · gewährleistete · hat gewährleistet", "gewährleisten"),
    fvVerb("B2", "festlegen", "stanovit", "Wir müssen einen klaren Termin festlegen.", "Musíme stanovit jasný termín.", "legt fest · legte fest · hat festgelegt", "festlegen"),
    fvVerb("B2", "verändern", "změnit", "Diese Erfahrung hat meine Meinung verändert.", "Tato zkušenost změnila můj názor.", "verändert · veränderte · hat verändert", "verändert"),
    fvVerb("B2", "unterstützen", "podpořit", "Meine Kollegen unterstützen mich dabei.", "Kolegové mě v tom podporují.", "unterstützt · unterstützte · hat unterstützt", "unterstützen"),
    fvVerb("B2", "erfordern", "vyžadovat", "Diese Aufgabe erfordert viel Geduld.", "Tento úkol vyžaduje hodně trpělivosti.", "erfordert · erforderte · hat erfordert", "erfordert"),

    fvNoun("B2", "der", "Zusammenhang", "souvislost", "Pl.: die Zusammenhänge", "Jetzt verstehe ich den Zusammenhang.", "Teď chápu souvislost."),
    fvNoun("B2", "die", "Auswirkung", "dopad / důsledek", "Pl.: die Auswirkungen", "Die Auswirkung ist groß.", "Dopad je velký."),
    fvNoun("B2", "die", "Herausforderung", "výzva", "Pl.: die Herausforderungen", "Das ist eine interessante Herausforderung.", "To je zajímavá výzva."),
    fvNoun("B2", "die", "Maßnahme", "opatření", "Pl.: die Maßnahmen", "Wir müssen sofort eine Maßnahme ergreifen.", "Musíme okamžitě přijmout opatření."),
    fvNoun("B2", "der", "Beitrag", "příspěvek", "Pl.: die Beiträge", "Danke für deinen Beitrag.", "Děkuji za tvůj příspěvek."),
    fvNoun("B2", "das", "Vertrauen", "důvěra", "Obvykle bez množného čísla", "Vertrauen ist sehr wichtig.", "Důvěra je velmi důležitá."),
    fvNoun("B2", "der", "Unterschied", "rozdíl", "Pl.: die Unterschiede", "Der Unterschied ist nicht sehr groß.", "Rozdíl není příliš velký."),
    fvNoun("B2", "der", "Aufwand", "úsilí / náklady", "Pl.: die Aufwände", "Der Aufwand ist größer als erwartet.", "Úsilí je větší, než se čekalo."),
    fvNoun("B2", "der", "Nutzen", "užitek / přínos", "Obvykle bez množného čísla", "Der Nutzen ist klar erkennbar.", "Přínos je jasně viditelný."),
    fvNoun("B2", "der", "Anspruch", "nárok / požadavek", "Pl.: die Ansprüche", "Der Anspruch ist hoch.", "Požadavek je vysoký."),
    fvNoun("B2", "der", "Fortschritt", "pokrok", "Pl.: die Fortschritte", "Der Fortschritt ist sichtbar.", "Pokrok je viditelný."),
    fvNoun("B2", "die", "Fähigkeit", "schopnost", "Pl.: die Fähigkeiten", "Diese Fähigkeit ist sehr nützlich.", "Tato schopnost je velmi užitečná."),
    fvNoun("B2", "das", "Ereignis", "událost", "Pl.: die Ereignisse", "Das Ereignis hat viele Menschen überrascht.", "Tato událost překvapila mnoho lidí."),
    fvNoun("B2", "der", "Einfluss", "vliv", "Pl.: die Einflüsse", "Das hat einen großen Einfluss auf uns.", "To na nás má velký vliv."),
    fvNoun("B2", "die", "Absicht", "záměr / úmysl", "Pl.: die Absichten", "Das war nicht meine Absicht.", "To nebyl můj záměr."),

    fvAdjective("B2", "erheblich", "značný", "Die Kosten sind erheblich gestiegen.", "Náklady výrazně vzrostly."),
    fvAdjective("B2", "grundsätzlich", "zásadní / obecně platný", "Grundsätzlich bin ich damit einverstanden.", "V zásadě s tím souhlasím."),
    fvAdjective("B2", "offensichtlich", "zřejmý", "Das ist offensichtlich ein Fehler.", "To je zjevně chyba."),
    fvAdjective("B2", "vermutlich", "pravděpodobný", "Er kommt vermutlich erst am Abend.", "Pravděpodobně přijde až večer."),
    fvAdjective("B2", "langfristig", "dlouhodobý", "Wir brauchen eine langfristige Lösung.", "Potřebujeme dlouhodobé řešení."),
    fvAdjective("B2", "zunehmend", "rostoucí", "Das Problem wird zunehmend wichtig.", "Tento problém je stále důležitější."),
    fvAdjective("B2", "wesentlich", "podstatný", "Es gibt einen wesentlichen Unterschied.", "Existuje podstatný rozdíl."),
    fvAdjective("B2", "erforderlich", "nezbytný", "Weitere Informationen sind erforderlich.", "Jsou nutné další informace."),
    fvAdjective("B2", "geeignet", "vhodný", "Diese Lösung ist gut geeignet.", "Toto řešení je velmi vhodné."),
    fvAdjective("B2", "umfassend", "rozsáhlý / komplexní", "Wir brauchen eine umfassende Erklärung.", "Potřebujeme komplexní vysvětlení."),
    fvAdverb("B2", "kaum", "sotva", "Ich habe heute kaum Zeit.", "Dnes mám sotva čas."),
    fvAdverb("B2", "durchaus", "rozhodně / docela", "Das ist durchaus möglich.", "To je rozhodně možné."),
    fvAdverb("B2", "gleichzeitig", "zároveň", "Wir müssen mehrere Dinge gleichzeitig erledigen.", "Musíme vyřídit několik věcí zároveň."),
    fvAdverb("B2", "folglich", "tudíž", "Die Frist ist abgelaufen, folglich ist eine Änderung nötig.", "Lhůta uplynula, tudíž je nutná změna."),
    fvFunction("B2", "allerdings", "ovšem / nicméně", "Ich verstehe dich, allerdings sehe ich das anders.", "Rozumím ti, ovšem vidím to jinak."),
    fvFunction("B2", "hingegen", "naproti tomu", "Er fährt gern Auto, ich hingegen nehme den Zug.", "On rád jezdí autem, já naproti tomu jedu vlakem."),
    fvFunction("B2", "demnach", "podle toho / tudíž", "Demnach müssen wir den Termin ändern.", "Podle toho musíme změnit termín."),
    fvFunction("B2", "einerseits", "na jedné straně", "Einerseits bin ich müde, andererseits möchte ich ausgehen.", "Na jedné straně jsem unavený, na druhé straně chci jít ven."),
    fvFunction("B2", "andererseits", "na druhé straně", "Andererseits ist die Lösung nicht teuer.", "Na druhé straně není řešení drahé.")
).distinctBy { it.kind.name + ":" + it.word.lowercase() }

internal fun frequencyVocabularyForLevel(level: String, verbsOnly: Boolean = false): List<FrequencyVocabularyEntry> =
    frequencyVocabularyCatalog.filter { it.level == level && (!verbsOnly || it.kind == FrequencyWordKind.VERB) }

internal fun frequencyVocabularyVerbCount(level: String): Int =
    frequencyVocabularyForLevel(level).count { it.kind == FrequencyWordKind.VERB }

