package com.deutschai.app

internal data class ReadingArticle(
    val id: String,
    val level: String,
    val title: String,
    val subtitle: String,
    val body: String,
    val glossary: Map<String, String> = emptyMap()
) {
    val wordCount: Int
        get() = body.trim().split(Regex("\\s+")).count { it.isNotBlank() }
}

internal val readingArticles: List<ReadingArticle>
    get() = readingArticlesA1 + readingArticlesA2 + readingArticlesB1 +
        readingArticlesB2 + readingArticlesC1 + readingArticlesC2

private val coreGermanGlossary = mapOf(
    "ich" to "já", "du" to "ty", "er" to "on", "sie" to "ona / oni / ony", "es" to "to / ono",
    "wir" to "my", "ihr" to "vy", "Sie" to "Vy", "und" to "a", "oder" to "nebo", "aber" to "ale",
    "weil" to "protože", "denn" to "neboť / protože", "obwohl" to "ačkoli", "wenn" to "když / pokud",
    "dass" to "že", "damit" to "aby", "als" to "když (v minulosti)", "während" to "zatímco",
    "bevor" to "než", "nachdem" to "poté co", "deshalb" to "proto", "trotzdem" to "přesto",
    "doch" to "přece / ale", "auch" to "také", "nur" to "jen", "schon" to "už", "noch" to "ještě",
    "nicht" to "ne", "kein" to "žádný", "keine" to "žádná / žádné", "ja" to "ano", "nein" to "ne",
    "bitte" to "prosím", "danke" to "děkuji", "gern" to "rád / rád(a)", "gerne" to "rád / rád(a)",
    "sehr" to "velmi", "mehr" to "více", "weniger" to "méně", "viel" to "hodně", "viele" to "mnoho",
    "man" to "člověk / obecné „se“", "alle" to "všichni / všechny", "alles" to "všechno",
    "jeder" to "každý", "jede" to "každá", "jedes" to "každé", "etwas" to "něco", "nichts" to "nic",
    "jemand" to "někdo", "niemand" to "nikdo", "hier" to "tady", "dort" to "tam", "da" to "tam / zde",
    "heute" to "dnes", "morgen" to "zítra / ráno", "gestern" to "včera", "jetzt" to "teď",
    "später" to "později", "früher" to "dříve", "immer" to "vždy", "oft" to "často", "manchmal" to "někdy",
    "selten" to "zřídka", "nie" to "nikdy", "bald" to "brzy", "bereits" to "již", "wieder" to "znovu",
    "dann" to "potom", "danach" to "poté", "zuerst" to "nejprve", "zuletzt" to "nakonec",
    "hierher" to "sem", "dorthin" to "tam", "überall" to "všude", "zusammen" to "společně",
    "allein" to "sám / sama", "vielleicht" to "možná", "wahrscheinlich" to "pravděpodobně",
    "wirklich" to "opravdu", "eigentlich" to "vlastně", "besonders" to "zvláště", "fast" to "téměř",
    "ganz" to "celý / úplně", "genug" to "dost", "so" to "tak", "anders" to "jinak", "genau" to "přesně",
    "gut" to "dobře / dobrý", "besser" to "lépe / lepší", "schlecht" to "špatně / špatný",
    "groß" to "velký", "klein" to "malý", "lang" to "dlouhý", "kurz" to "krátký", "alt" to "starý",
    "jung" to "mladý", "neu" to "nový", "schön" to "krásný", "wichtig" to "důležitý", "richtig" to "správný",
    "falsch" to "špatný / nesprávný", "leicht" to "lehký / snadný", "schwer" to "těžký / obtížný",
    "einfach" to "jednoduchý", "schwierig" to "obtížný", "schnell" to "rychle", "langsam" to "pomalu",
    "früh" to "brzy", "spät" to "pozdě", "freundlich" to "přátelský", "ruhig" to "klidný", "laut" to "hlasitý",
    "müde" to "unavený", "glücklich" to "šťastný", "traurig" to "smutný", "froh" to "rád / veselý",
    "warm" to "teplý", "kalt" to "studený", "heiß" to "horký", "sauber" to "čistý", "voll" to "plný",
    "leer" to "prázdný", "offen" to "otevřený", "geschlossen" to "zavřený", "frei" to "volný",
    "möglich" to "možný", "sicher" to "bezpečný / jistý", "bereit" to "připravený",
    "sein" to "být", "bin" to "jsem", "bist" to "jsi", "ist" to "je", "sind" to "jsme / jsou",
    "war" to "byl(a) jsem / byl(a)", "waren" to "byli jsme / byli", "gewesen" to "byl(a)",
    "haben" to "mít", "habe" to "mám", "hast" to "máš", "hat" to "má", "hatte" to "měl(a)",
    "hatten" to "měli", "werden" to "stát se / budoucí pomocné sloveso", "wird" to "stane se / bude",
    "können" to "moci / umět", "kann" to "mohu / umím", "kannst" to "můžeš / umíš",
    "müssen" to "muset", "muss" to "musím", "wollen" to "chtít", "will" to "chci",
    "dürfen" to "smět", "darf" to "smím", "sollen" to "mít povinnost / měl by", "soll" to "mám / má",
    "machen" to "dělat", "mache" to "dělám", "gehen" to "jít", "gehe" to "jdu", "kommt" to "přichází",
    "kommen" to "přijít / pocházet", "sprechen" to "mluvit", "spreche" to "mluvím", "spricht" to "mluví",
    "wissen" to "vědět", "weiß" to "vím / ví", "sehen" to "vidět", "sehen" to "vidět", "sieht" to "vidí",
    "essen" to "jíst", "isst" to "jí", "trinken" to "pít", "trinkt" to "pije", "laufen" to "běžet / chodit",
    "lesen" to "číst", "liest" to "čte", "schreiben" to "psát", "arbeitet" to "pracuje", "arbeiten" to "pracovat",
    "lernen" to "učit se", "findet" to "nachází / považuje", "finden" to "najít / považovat",
    "denken" to "myslet", "bleiben" to "zůstat", "fahren" to "jet / řídit", "nimmt" to "bere",
    "geben" to "dát / dávat", "gibt" to "dává / existuje", "brauchen" to "potřebovat",
    "wohnen" to "bydlet", "kochen" to "vařit", "schlafen" to "spát", "hören" to "slyšet / poslouchat",
    "kaufen" to "kupovat", "spielen" to "hrát", "sprechen" to "mluvit", "verstehen" to "rozumět",
    "fragen" to "ptát se", "antworten" to "odpovídat", "sagen" to "říkat", "erzählen" to "vyprávět",
    "erklären" to "vysvětlit", "zeigen" to "ukázat", "helfen" to "pomoci", "suchen" to "hledat",
    "brachte" to "přinesl(a)", "bringen" to "přinést", "nehmen" to "vzít", "geben" to "dávat",
    "fahren" to "jet", "lief" to "běžel(a)", "liefen" to "běželi", "sah" to "viděl(a)", "sagte" to "řekl(a)",
    "dachte" to "myslel(a)", "wusste" to "věděl(a)", "konnte" to "mohl(a)", "musste" to "musel(a)",
    "wollte" to "chtěl(a)", "ging" to "šel / šla", "gingen" to "šli", "kam" to "přišel / přišla",
    "kamen" to "přišli", "nahm" to "vzal(a)", "gab" to "dal(a)", "las" to "četl(a)", "schrieb" to "psal(a)",
    "der" to "člen mužského rodu", "die" to "člen ženského rodu / množné číslo", "das" to "člen středního rodu",
    "ein" to "jeden / nějaký", "eine" to "jedna / nějaká", "einen" to "jednoho / jeden (4. pád mužského rodu)",
    "einem" to "jednomu / jednom (3. pád)", "einer" to "jedné / jednoho (podle pádu)",
    "mein" to "můj", "meine" to "moje", "dein" to "tvůj", "deine" to "tvoje", "sein" to "jeho / být",
    "ihr" to "její / jejich / vy", "unser" to "náš", "unsere" to "naše", "dieser" to "tento",
    "diese" to "tato", "dieses" to "toto", "welche" to "který / která / které", "was" to "co",
    "wer" to "kdo", "wo" to "kde", "wohin" to "kam", "woher" to "odkud", "wann" to "kdy",
    "warum" to "proč", "wie" to "jak", "welcher" to "který", "mit" to "s", "ohne" to "bez",
    "für" to "pro", "von" to "od / z", "aus" to "z", "nach" to "po / do", "vor" to "před",
    "hinter" to "za", "neben" to "vedle", "zwischen" to "mezi", "unter" to "pod", "über" to "nad / o",
    "auf" to "na", "an" to "na / u", "in" to "v", "bei" to "u / při", "bis" to "do / až",
    "durch" to "skrz", "gegen" to "proti / kolem", "seit" to "od (časově)", "um" to "kolem / v (čas)",
    "wegen" to "kvůli", "trotz" to "navzdory", "während" to "během / zatímco", "innerhalb" to "uvnitř / během",
    "außerhalb" to "mimo", "zwischenzeitlich" to "mezitím", "also" to "tedy", "sonst" to "jinak",
    "deswegen" to "z tohoto důvodu", "darum" to "proto", "jedoch" to "avšak", "hingegen" to "naproti tomu",
    "sondern" to "nýbrž", "sobald" to "jakmile", "solange" to "dokud", "falls" to "v případě, že",
    "ob" to "zda", "damals" to "tehdy", "inzwischen" to "mezitím", "endlich" to "konečně",
    "plötzlich" to "náhle", "zunächst" to "nejprve", "schließlich" to "nakonec", "tatsächlich" to "skutečně",
    "offenbar" to "zjevně", "vermutlich" to "pravděpodobně", "vielleicht" to "možná", "kaum" to "sotva",
    "mindestens" to "nejméně", "höchstens" to "nejvýše", "ungefähr" to "přibližně", "circa" to "asi",
    "die Zeit" to "čas", "der Tag" to "den", "die Woche" to "týden", "der Monat" to "měsíc",
    "das Jahr" to "rok", "die Stunde" to "hodina", "die Minute" to "minuta", "der Morgen" to "ráno",
    "der Abend" to "večer", "die Nacht" to "noc", "die Arbeit" to "práce", "der Beruf" to "povolání",
    "die Familie" to "rodina", "der Freund" to "kamarád", "die Freundin" to "kamarádka",
    "das Haus" to "dům", "die Wohnung" to "byt", "das Zimmer" to "pokoj", "die Küche" to "kuchyně",
    "das Bad" to "koupelna", "die Tür" to "dveře", "das Fenster" to "okno", "der Tisch" to "stůl",
    "der Stuhl" to "židle", "das Bett" to "postel", "das Wasser" to "voda", "das Brot" to "chléb",
    "die Milch" to "mléko", "der Kaffee" to "káva", "der Tee" to "čaj", "das Essen" to "jídlo",
    "das Getränk" to "nápoj", "der Apfel" to "jablko", "die Banane" to "banán", "das Buch" to "kniha",
    "die Schule" to "škola", "der Lehrer" to "učitel", "die Lehrerin" to "učitelka", "das Kind" to "dítě",
    "die Mutter" to "matka", "der Vater" to "otec", "die Schwester" to "sestra", "der Bruder" to "bratr",
    "die Stadt" to "město", "das Dorf" to "vesnice", "die Straße" to "ulice", "der Bahnhof" to "nádraží",
    "der Zug" to "vlak", "der Bus" to "autobus", "das Auto" to "auto", "das Fahrrad" to "kolo",
    "die Arbeit" to "práce", "das Büro" to "kancelář", "die Firma" to "firma", "der Kollege" to "kolega",
    "die Kollegin" to "kolegyně", "der Kunde" to "zákazník", "die Kundin" to "zákaznice",
    "das Geld" to "peníze", "der Preis" to "cena", "der Einkauf" to "nákup", "das Geschäft" to "obchod",
    "der Markt" to "trh", "die Frage" to "otázka", "die Antwort" to "odpověď", "das Gespräch" to "rozhovor",
    "die Sprache" to "jazyk", "das Wort" to "slovo", "der Satz" to "věta", "die Geschichte" to "příběh",
    "das Ende" to "konec", "der Anfang" to "začátek", "der Weg" to "cesta", "der Ort" to "místo",
    "der Grund" to "důvod", "die Möglichkeit" to "možnost", "die Entscheidung" to "rozhodnutí",
    "die Erfahrung" to "zkušenost", "die Erinnerung" to "vzpomínka", "die Hoffnung" to "naděje",
    "die Angst" to "strach", "die Freude" to "radost", "das Problem" to "problém", "die Lösung" to "řešení",
    "der Fehler" to "chyba", "die Hilfe" to "pomoc", "die Frage" to "otázka", "das Ziel" to "cíl",
    "der Erfolg" to "úspěch", "die Arbeit" to "práce", "die Aufgabe" to "úkol", "der Plan" to "plán",
    "die Nachricht" to "zpráva", "die E-Mail" to "e-mail", "das Telefon" to "telefon", "das Handy" to "mobil",
    "das Wetter" to "počasí", "die Sonne" to "slunce", "der Regen" to "déšť", "der Schnee" to "sníh",
    "der Wind" to "vítr", "der Himmel" to "nebe", "der Baum" to "strom", "die Blume" to "květina",
    "der Wald" to "les", "der Berg" to "hora", "der See" to "jezero", "das Meer" to "moře",
    "das Herz" to "srdce", "der Kopf" to "hlava", "die Hand" to "ruka", "der Fuß" to "chodidlo",
    "der Schmerz" to "bolest", "der Arzt" to "lékař", "die Ärztin" to "lékařka", "die Gesundheit" to "zdraví",
    "die Reise" to "cesta", "der Urlaub" to "dovolená", "das Hotel" to "hotel", "das Ticket" to "jízdenka",
    "die Karte" to "mapa / karta", "die Rechnung" to "účet", "die Frage" to "otázka",
    "die Geschichte" to "příběh", "der Traum" to "sen", "die Welt" to "svět", "das Leben" to "život",
    "die Zukunft" to "budoucnost", "die Vergangenheit" to "minulost", "die Gegenwart" to "přítomnost",
    "die Verantwortung" to "odpovědnost", "die Gesellschaft" to "společnost", "die Umwelt" to "životní prostředí",
    "die Wissenschaft" to "věda", "die Forschung" to "výzkum", "die Kunst" to "umění", "die Kultur" to "kultura",
    "die Sprache" to "jazyk", "die Gesellschaft" to "společnost", "die Entwicklung" to "vývoj",
    "die Veränderung" to "změna", "die Beziehung" to "vztah", "die Begegnung" to "setkání",
    "die Entscheidung" to "rozhodnutí", "die Möglichkeit" to "možnost", "die Herausforderung" to "výzva",
    "die Voraussetzung" to "předpoklad", "die Bedeutung" to "význam", "der Zusammenhang" to "souvislost",
    "die Schwierigkeit" to "obtíž", "die Unterstützung" to "podpora", "die Verbesserung" to "zlepšení",
    "die Lösung" to "řešení", "die Aufmerksamkeit" to "pozornost", "die Geduld" to "trpělivost",
    "die Wahrheit" to "pravda", "die Lüge" to "lež", "die Freiheit" to "svoboda", "die Sicherheit" to "bezpečí",
    "die Gerechtigkeit" to "spravedlnost", "die Verantwortung" to "odpovědnost", "das Vertrauen" to "důvěra",
    "die Erinnerung" to "vzpomínka", "die Vorstellung" to "představa", "der Eindruck" to "dojem",
    "der Unterschied" to "rozdíl", "die Gemeinsamkeit" to "společný rys", "die Möglichkeit" to "možnost"
)

internal fun translateReadingWord(article: ReadingArticle, rawWord: String): String {
    val key = rawWord.lowercase()
        .replace("ä", "ae")
        .replace("ö", "oe")
        .replace("ü", "ue")
        .replace("ß", "ss")
        .trim()
    val directKey = rawWord.lowercase().trim()
    return article.glossary[directKey]
        ?: article.glossary[key]
        ?: coreGermanGlossary[directKey]
        ?: coreGermanGlossary[key]
        ?: coreGermanGlossary.entries.firstOrNull { it.key.equals(rawWord, ignoreCase = true) }?.value
        ?: "Toto slovo zatím není v offline slovníčku. Jeho význam můžeme doplnit do další aktualizace."
}
