package com.deutschai.app

private val extendedSubjects = listOf("Ich", "Du", "Er", "Wir", "Sie")
private val extendedCzechSubjects = listOf("Já", "Ty", "On/Ona", "My", "Oni")

private fun extVerb(
    id: String,
    czech: String,
    description: String,
    forms: String,
    czechForms: String,
    vararg examples: String
): VerbDefinition {
    val germanForms = forms.split("|")
    val translatedForms = czechForms.split("|")
    require(germanForms.size == 5 && translatedForms.size == 5) { "Expected five forms for $id" }
    require(examples.size == 5) { "Expected five example phrases for $id" }
    return VerbDefinition(
        id, id, czech, description,
        extendedSubjects, extendedCzechSubjects,
        germanForms, translatedForms,
        examples.map { row ->
            val fields = row.split("§", limit = 2)
            require(fields.size == 2) { "Invalid extended verb example: $row" }
            VerbPhrase(fields[0], fields[1])
        }
    )
}

internal val extendedVerbDefinitions = listOf(
    // 11. Domácnost a každodenní péče
    extVerb("öffnen", "otevírat", "Domácnost", "öffne|öffnest|öffnet|öffnen|öffnen", "otevírám|otevíráš|otevírá|otevíráme|otevírají",
        "die Tür§dveře", "das Fenster§okno", "den Brief§dopis", "das Geschenk§dárek", "die Flasche§láhev"),
    extVerb("schließen", "zavírat", "Domácnost", "schließe|schließt|schließt|schließen|schließen", "zavírám|zavíráš|zavírá|zavíráme|zavírají",
        "die Haustür§vchodové dveře", "das Fenster im Schlafzimmer§okno v ložnici", "den Koffer§kufr", "den Laden um sechs Uhr§obchod v šest hodin", "die Datei nach der Arbeit§soubor po práci"),
    extVerb("aufräumen", "uklízet", "Domácnost", "räume|räumst|räumt|räumen|räumen", "uklízím|uklízíš|uklízí|uklízíme|uklízí",
        "mein Zimmer auf§svůj pokoj", "den Schreibtisch auf§psací stůl", "die Küche nach dem Essen auf§kuchyň po jídle", "die Garage am Samstag auf§garáž v sobotu", "das Wohnzimmer nach der Feier auf§obývací pokoj po oslavě"),
    extVerb("waschen", "mýt", "Čistota a péče", "wasche|wäschst|wäscht|waschen|waschen", "myji|myješ|myje|myjeme|myjí",
        "das Gesicht§obličej", "das Auto am Sonntag§auto v neděli", "das Gemüse vor dem Kochen§zeleninu před vařením", "meine Hände vor dem Essen§ruce před jídlem", "die Fenster im Frühling§okna na jaře"),
    extVerb("putzen", "čistit / uklízet", "Čistota a péče", "putze|putzt|putzt|putzen|putzen", "čistím|čistíš|čistí|čistíme|čistí",
        "die Fenster im Frühling§okna na jaře", "die Schuhe nach dem Spaziergang§boty po procházce", "das Badezimmer gründlich§koupelnu důkladně", "den Boden jeden Freitag§podlahu každý pátek", "die Küche nach dem Kochen§kuchyň po vaření"),

    // 12. Komunikace a vysvětlování
    extVerb("fragen", "ptát se", "Komunikace", "frage|fragst|fragt|fragen|fragen", "se ptám|se ptáš|se ptá|se ptáme|se ptají",
        "den Lehrer nach der Aufgabe§učitele na úkol", "eine Kollegin nach dem Weg§kolegyně na cestu", "den Kunden nach seiner Meinung§zákazníka na jeho názor", "nach der Uhrzeit§na čas", "den Nachbarn um Rat§souseda o radu"),
    extVerb("antworten", "odpovídat", "Komunikace", "antworte|antwortest|antwortet|antworten|antworten", "odpovídám|odpovídáš|odpovídá|odpovídáme|odpovídají",
        "auf die E-Mail§na e-mail", "auf die Frage des Lehrers§na otázku učitele", "dem Kunden höflich§zákazníkovi zdvořile", "so schnell wie möglich§co nejrychleji", "auf alle Nachrichten am Abend§na všechny zprávy večer"),
    extVerb("erklären", "vysvětlovat", "Komunikace", "erkläre|erklärst|erklärt|erklären|erklären", "vysvětluji|vysvětluješ|vysvětluje|vysvětlujeme|vysvětlují",
        "die Aufgabe Schritt für Schritt§úkol krok za krokem", "den Unterschied zwischen zwei Wörtern§rozdíl mezi dvěma slovy", "das Problem dem Kollegen§problém kolegovi", "die Regeln noch einmal§pravidla ještě jednou", "den Weg zum Bahnhof§cestu na nádraží"),
    extVerb("zeigen", "ukazovat", "Komunikace", "zeige|zeigst|zeigt|zeigen|zeigen", "ukazuji|ukazuješ|ukazuje|ukazujeme|ukazují",
        "den Weg zum Hotel§cestu k hotelu", "dem Gast sein Zimmer§hostovi jeho pokoj", "die Lösung auf dem Bildschirm§řešení na obrazovce", "den Kollegen die neue Maschine§kolegům nový stroj", "ein Foto von der Reise§fotografii z cesty"),
    extVerb("erzählen", "vyprávět", "Komunikace a příběhy", "erzähle|erzählst|erzählt|erzählen|erzählen", "vyprávím|vyprávíš|vypráví|vyprávíme|vyprávějí",
        "eine spannende Geschichte§napínavý příběh", "von den Ferien§o dovolené", "dem Freund von der Arbeit§kamarádovi o práci", "was gestern passiert ist§co se včera stalo", "den Kindern ein Märchen§dětem pohádku"),

    // 13. Plánování a domluvy
    extVerb("vorbereiten", "připravovat", "Plánování", "bereite|bereitest|bereitet|bereiten|bereiten", "připravuji|připravuješ|připravuje|připravujeme|připravují",
        "das Meeting für morgen vor§poradu na zítřek", "die Unterlagen sorgfältig vor§podklady pečlivě", "eine Präsentation für den Kunden vor§prezentaci pro zákazníka", "das Abendessen für Gäste vor§večeři pro hosty", "den Raum für die Besprechung vor§místnost na poradu"),
    extVerb("vereinbaren", "domlouvat", "Termíny a dohody", "vereinbare|vereinbarst|vereinbart|vereinbaren|vereinbaren", "domlouvám|domlouváš|domlouvá|domlouváme|domlouvají",
        "einen Termin beim Arzt§termín u lékaře", "ein Treffen für Freitag§setkání na pátek", "klare Regeln mit dem Team§jasná pravidla s týmem", "einen Gesprächstermin§termín rozhovoru", "einen neuen Liefertermin§nový termín dodání"),
    extVerb("verschieben", "přesouvat", "Termíny a změny", "verschiebe|verschiebst|verschiebt|verschieben|verschieben", "přesouvám|přesouváš|přesouvá|přesouváme|přesouvají",
        "den Termin auf morgen§termín na zítřek", "die Besprechung auf nächste Woche§poradu na příští týden", "die Reise wegen des Wetters§cestu kvůli počasí", "die Abgabe um zwei Tage§odevzdání o dva dny", "das Treffen auf den Nachmittag§setkání na odpoledne"),
    extVerb("absagen", "rušit / odříkávat", "Termíny a změny", "sage|sagst|sagt|sagen|sagen", "ruším|rušíš|ruší|rušíme|ruší",
        "den Termin kurzfristig ab§termín na poslední chvíli", "die Reise wegen Krankheit ab§cestu kvůli nemoci", "die Besprechung für heute ab§dnešní poradu", "das Treffen mit dem Kunden ab§setkání se zákazníkem", "den Kurs wegen zu weniger Teilnehmer ab§kurz kvůli malému počtu účastníků"),
    extVerb("bestätigen", "potvrzovat", "Termíny a dohody", "bestätige|bestätigst|bestätigt|bestätigen|bestätigen", "potvrzuji|potvrzuješ|potvrzuje|potvrzujeme|potvrzují",
        "die Reservierung per E-Mail§rezervaci e-mailem", "den vereinbarten Termin§dohodnutý termín", "den Eingang der Unterlagen§přijetí dokumentů", "die Bestellung schriftlich§objednávku písemně", "die Teilnahme an der Veranstaltung§účast na akci"),

    // 14. Peníze a běžné výdaje
    extVerb("bezahlen", "platit", "Peníze a nákupy", "bezahle|bezahlst|bezahlt|bezahlen|bezahlen", "platím|platíš|platí|platíme|platí",
        "die Rechnung mit Karte§účet kartou", "den Kaffee bar§kávu v hotovosti", "die Miete am Monatsanfang§nájem na začátku měsíce", "das Ticket online§lístek online", "die Bestellung im Voraus§objednávku předem"),
    extVerb("sparen", "šetřit / spořit", "Peníze a plánování", "spare|sparst|spart|sparen|sparen", "šetřím|šetříš|šetří|šetříme|šetří",
        "Geld für den Urlaub§peníze na dovolenou", "jeden Monat einen festen Betrag§každý měsíc pevnou částku", "Strom und Wasser§elektřinu a vodu", "für ein neues Fahrrad§na nové kolo", "Zeit durch gute Planung§čas díky dobrému plánování"),
    extVerb("verdienen", "vydělávat", "Práce a příjmy", "verdiene|verdienst|verdient|verdienen|verdienen", "vydělávám|vyděláváš|vydělává|vyděláváme|vydělávají",
        "genug Geld für die Miete§dost peněz na nájem", "sein Geld mit ehrlicher Arbeit§své peníze poctivou prací", "in diesem Beruf gut§v tomto povolání dobře", "zusätzliches Geld am Wochenende§peníze navíc o víkendu", "seinen Lebensunterhalt selbst§své živobytí samostatně"),
    extVerb("leihen", "půjčovat", "Půjčování a věci", "leihe|leihst|leiht|leihen|leihen", "půjčuji|půjčuješ|půjčuje|půjčujeme|půjčují",
        "dem Nachbarn ein Werkzeug§sousedovi nářadí", "meiner Schwester das Fahrrad§své sestře kolo", "einem Kollegen meinen Stift§kolegovi pero", "der Bibliothek ein altes Buch§knihovně starou knihu", "einem Freund etwas Geld§kamarádovi nějaké peníze"),
    extVerb("ausgeben", "utratit / vydávat", "Peníze a rozpočet", "gebe|gibst|gibt|geben|geben", "utrácím|utrácíš|utrácí|utrácíme|utrácí", 
        "Geld für Bücher aus§peníze za knihy", "zu viel Geld für Kleidung aus§příliš mnoho peněz za oblečení", "im Urlaub weniger Geld aus§na dovolené méně peněz", "monatlich einen festen Betrag aus§každý měsíc pevnou částku", "das gesamte Budget für die Reparatur aus§celý rozpočet na opravu"),

    // 15. Vlak a veřejná doprava
    extVerb("warten", "čekat", "Doprava a čas", "warte|wartest|wartet|warten|warten", "čekám|čekáš|čeká|čekáme|čekají",
        "auf den Bus§na autobus", "vor dem Bahnhof auf die Freunde§před nádražím na přátele", "zehn Minuten auf den Zug§deset minut na vlak", "auf eine Antwort§na odpověď", "geduldig auf den Kunden§trpělivě na zákazníka"),
    extVerb("umsteigen", "přestupovat", "Cestování vlakem", "steige|steigst|steigt|steigen|steigen", "přestupuji|přestupuješ|přestupuje|přestupujeme|přestupují",
        "in München um§v Mnichově", "am Hauptbahnhof um§na hlavním nádraží", "in Berlin in einen anderen Zug um§v Berlíně na jiný vlak", "zweimal auf dem Weg nach Hamburg um§dvakrát cestou do Hamburku", "an der nächsten Station um§na příští stanici"),
    extVerb("einsteigen", "nastupovat", "Cestování vlakem", "steige|steigst|steigt|steigen|steigen", "nastupuji|nastupuješ|nastupuje|nastupujeme|nastupují",
        "in den Zug ein§do vlaku", "vorne in den Bus ein§předními dveřmi do autobusu", "an der nächsten Haltestelle ein§na příští zastávce", "in das Flugzeug ein§do letadla", "vorsichtig in die Straßenbahn ein§opatrně do tramvaje"),
    extVerb("aussteigen", "vystupovat", "Cestování vlakem", "steige|steigst|steigt|steigen|steigen", "vystupuji|vystupuješ|vystupuje|vystupujeme|vystupují",
        "an der nächsten Haltestelle aus§na příští zastávce", "am Hauptbahnhof aus§na hlavním nádraží", "in Prag aus§v Praze", "vor dem Museum aus§před muzeem", "an der Endstation aus§na konečné stanici"),
    extVerb("abfahren", "odjíždět", "Odjezdy a příjezdy", "fahre|fährst|fährt|fahren|fahren", "odjíždím|odjíždíš|odjíždí|odjíždíme|odjíždějí",
        "in zehn Minuten ab§za deset minut", "pünktlich um acht Uhr ab§přesně v osm hodin", "vom Gleis drei ab§z třetího nástupiště", "wegen der Bauarbeiten später ab§kvůli stavebním pracím později", "jeden Morgen um halb sieben ab§každé ráno v půl sedmé"),

    // 16. Létání, příjezdy a vyzvedávání
    extVerb("fliegen", "létat / letět", "Letecká doprava", "fliege|fliegst|fliegt|fliegen|fliegen", "letím|letíš|letí|letíme|letí",
        "morgen nach Berlin§zítra do Berlína", "direkt nach Wien§přímo do Vídně", "zum ersten Mal allein§poprvé sám/sama", "wegen der Arbeit nach Frankfurt§kvůli práci do Frankfurtu", "im Sommer ans Meer§v létě k moři"),
    extVerb("landen", "přistávat", "Letecká doprava", "lande|landest|landet|landen|landen", "přistávám|přistáváš|přistává|přistáváme|přistávají",
        "um neun Uhr§v devět hodin", "wegen des Nebels später§kvůli mlze později", "sicher auf der Landebahn§bezpečně na přistávací dráze", "in einer anderen Stadt§v jiném městě", "nach einem langen Flug§po dlouhém letu"),
    extVerb("starten", "startovat", "Letecká doprava a projekty", "starte|startest|startet|starten|starten", "startuji|startuješ|startuje|startujeme|startují",
        "den Motor vor der Abfahrt§motor před odjezdem", "das Programm am Computer§program na počítači", "morgen früh um sechs§zítra brzy v šest", "ein neues Projekt im November§nový projekt v listopadu", "pünktlich vom Flughafen§včas z letiště"),
    extVerb("ankommen", "přijíždět / dorazit", "Příjezdy a schůzky", "komme|kommst|kommt|kommen|kommen", "přijíždím|přijíždíš|přijíždí|přijíždíme|přijíždějí",
        "um acht Uhr an§v osm hodin", "heute etwas später an§dnes o něco později", "pünktlich am Flughafen an§včas na letiště", "erst am Abend an§až večer", "nach einer langen Reise an§po dlouhé cestě"),
    extVerb("abholen", "vyzvedávat", "Příjezdy a návštěvy", "hole|holst|holt|holen|holen", "vyzvedávám|vyzvedáváš|vyzvedává|vyzvedáváme|vyzvedávají",
        "die Gäste vom Bahnhof ab§hosty z nádraží", "meine Schwester von der Schule ab§svou sestru ze školy", "das Paket bei der Post ab§balík na poště", "den Kollegen nach der Arbeit ab§kolegu po práci", "die Kinder um vier Uhr ab§děti ve čtyři hodiny"),

    // 17. Bydlení, stěhování a uspořádání věcí
    extVerb("mieten", "pronajímat si", "Bydlení", "miete|mietest|mietet|mieten|mieten", "pronajímám si|pronajímáš si|pronajímá si|pronajímáme si|pronajímají si",
        "eine kleine Wohnung im Zentrum§malý byt v centru", "ein Auto für das Wochenende§auto na víkend", "ein Zimmer für zwei Nächte§pokoj na dvě noci", "ein Büro in der Nähe§kancelář poblíž", "eine Ferienwohnung am Meer§apartmán u moře"),
    extVerb("vermieten", "pronajímat", "Bydlení a nemovitosti", "vermiete|vermietest|vermietet|vermieten|vermieten", "pronajímám|pronajímáš|pronajímá|pronajímáme|pronajímají",
        "eine Wohnung an Studenten§byt studentům", "ein Zimmer für kurze Aufenthalte§pokoj na krátkodobé pobyty", "das Haus ab September§dům od září", "eine Garage an den Nachbarn§garáž sousedovi", "die Ferienwohnung im Sommer§apartmán v létě"),
    extVerb("umziehen", "stěhovat se", "Bydlení a změny", "ziehe|ziehst|zieht|ziehen|ziehen", "stěhuji se|stěhuješ se|stěhuje se|stěhujeme se|stěhují se",
        "nächsten Monat um§příští měsíc", "wegen der Arbeit nach Berlin um§kvůli práci do Berlína", "in eine größere Wohnung um§do většího bytu", "am Wochenende um§o víkendu", "im Frühling in ein anderes Haus um§na jaře do jiného domu"),
    extVerb("stellen", "stavět / postavit", "Domov a předměty", "stelle|stellst|stellt|stellen|stellen", "stavím|stavíš|staví|stavíme|staví",
        "die Tasche neben die Tür§tašku vedle dveří", "die Vase auf den Tisch§vázu na stůl", "den Stuhl an das Fenster§židli k oknu", "die Bücher ins Regal§knihy do police", "die Tasse auf den Schreibtisch§hrnek na psací stůl"),
    extVerb("legen", "pokládat", "Domov a předměty", "lege|legst|legt|legen|legen", "pokládám|pokládáš|pokládá|pokládáme|pokládají",
        "das Buch auf den Tisch§knihu na stůl", "die Schlüssel neben das Telefon§klíče vedle telefonu", "die Kleidung aufs Bett§oblečení na postel", "den Brief in die Schublade§dopis do zásuvky", "die Karte auf den Tresen§kartu na pult"),

    // 18. Spolupráce, pozvání a doporučení
    extVerb("treffen", "setkávat se / potkávat", "Lidé a setkávání", "treffe|triffst|trifft|treffen|treffen", "potkávám|potkáváš|potkává|potkáváme|potkávají",
        "einen Freund nach der Arbeit§kamaráda po práci", "uns vor dem Kino§se před kinem", "die Kollegen um neun Uhr§kolegy v devět hodin", "den Kunden persönlich§zákazníka osobně", "die Nachbarn im Treppenhaus§sousedy na schodišti"),
    extVerb("teilnehmen", "účastnit se", "Spolupráce a akce", "nehme|nimmst|nimmt|nehmen|nehmen", "se účastním|se účastníš|se účastní|se účastníme|se účastní",
        "an der Besprechung teil§porady", "an einem Sprachkurs teil§jazykového kurzu", "an der Konferenz teil§konference", "regelmäßig am Training teil§tréninku pravidelně", "an einer Umfrage teil§průzkumu"),
    extVerb("einladen", "zvat", "Pozvání a společenský život", "lade|lädst|lädt|laden|laden", "zvu|zveš|zve|zveme|zvou",
        "unsere Nachbarn zum Essen ein§naše sousedy na večeři", "die Kollegen zur Feier ein§kolegy na oslavu", "deine Freunde zum Geburtstag ein§své přátele na narozeniny", "die ganze Familie ein§celou rodinu", "den neuen Mitarbeiter auf einen Kaffee ein§nového kolegu na kávu"),
    extVerb("anbieten", "nabízet", "Pomoc a služby", "biete|bietest|bietet|bieten|bieten", "nabízím|nabízíš|nabízí|nabízíme|nabízejí",
        "dem Gast einen Kaffee an§hostovi kávu", "meine Hilfe an§svou pomoc", "dem Kunden eine Alternative an§zákazníkovi alternativu", "eine kostenlose Beratung an§bezplatnou konzultaci", "den Teilnehmern Getränke an§účastníkům nápoje"),
    extVerb("empfehlen", "doporučovat", "Rady a doporučení", "empfehle|empfiehlst|empfiehlt|empfehlen|empfehlen", "doporučuji|doporučuješ|doporučuje|doporučujeme|doporučují",
        "ein gutes Restaurant§dobrou restauraci", "dieses Buch Freunden§tuto knihu přátelům", "einen Besuch im Museum§návštěvu muzea", "den neuen Kollegen§nového kolegu", "eine andere Lösung§jiné řešení"),

    // 19. Digitální komunikace
    extVerb("drucken", "tisknout", "Dokumenty a kancelář", "drucke|druckst|druckt|drucken|drucken", "tisknu|tiskneš|tiskne|tiskneme|tisknou",
        "die Rechnung zweimal§fakturu dvakrát", "die Unterlagen für das Meeting§podklady na poradu", "das Foto in Farbe§fotografii barevně", "den Vertrag auf beiden Seiten§smlouvu z obou stran", "das Ticket vor der Reise§jízdenku před cestou"),
    extVerb("speichern", "ukládat", "Počítače a data", "speichere|speicherst|speichert|speichern|speichern", "ukládám|ukládáš|ukládá|ukládáme|ukládají",
        "das Dokument auf dem Computer§dokument do počítače", "die Fotos in der Cloud§fotografie do cloudu", "alle Änderungen regelmäßig§všechny změny pravidelně", "die Datei unter einem neuen Namen§soubor pod novým názvem", "die Telefonnummer im Handy§telefonní číslo do mobilu"),
    extVerb("löschen", "mazat", "Počítače a data", "lösche|löschst|löscht|löschen|löschen", "mažu|mažeš|maže|mažeme|mažou",
        "die alte Datei§starý soubor", "unnötige Nachrichten§zbytečné zprávy", "den Verlauf im Browser§historii v prohlížeči", "versehentlich ein Foto§omylem fotografii", "die doppelten Einträge§duplicitní záznamy"),
    extVerb("herunterladen", "stahovat", "Digitální soubory", "lade|lädst|lädt|laden|laden", "stahuji|stahuješ|stahuje|stahujeme|stahují",
        "die Datei herunter§soubor", "die neue Anwendung herunter§novou aplikaci", "die Unterlagen von der Webseite herunter§podklady z webové stránky", "das Hörbuch auf das Handy herunter§audioknihu do telefonu", "das Update vor der Reise herunter§aktualizaci před cestou"),
    extVerb("hochladen", "nahrávat", "Digitální soubory", "lade|lädst|lädt|laden|laden", "nahrávám|nahráváš|nahrává|nahráváme|nahrávají",
        "die Fotos hoch§fotografie", "das Dokument auf die Plattform hoch§dokument na platformu", "die Unterlagen in den gemeinsamen Ordner hoch§podklady do sdílené složky", "ein neues Video hoch§nové video", "die Daten sicher hoch§data bezpečně"),

    // 20. Formuláře a pracovní úkoly
    extVerb("unterschreiben", "podepisovat", "Dokumenty a smlouvy", "unterschreibe|unterschreibst|unterschreibt|unterschreiben|unterschreiben", "podepisuji|podepisuješ|podepisuje|podepisujeme|podepisují",
        "den Vertrag heute§smlouvu dnes", "das Formular an der Rezeption§formulář na recepci", "alle Seiten des Dokuments§všechny stránky dokumentu", "die Bestellung elektronisch§objednávku elektronicky", "die Vereinbarung nach der Prüfung§dohodu po kontrole"),
    extVerb("ausfüllen", "vyplňovat", "Formuláře a úřady", "fülle|füllst|füllt|füllen|füllen", "vyplňuji|vyplňuješ|vyplňuje|vyplňujeme|vyplňují",
        "das Formular vollständig aus§formulář úplně", "den Antrag am Computer aus§žádost na počítači", "die Anmeldung leserlich aus§přihlášku čitelně", "alle notwendigen Felder aus§všechna potřebná pole", "das Dokument mit schwarzem Stift aus§dokument černým perem"),
    extVerb("eintragen", "zapisovat", "Plánování a evidence", "trage|trägst|trägt|tragen|tragen", "zapisuji|zapisuješ|zapisuje|zapisujeme|zapisují",
        "den Termin in den Kalender ein§termín do kalendáře", "die Adresse in das Formular ein§adresu do formuláře", "den Namen in die Liste ein§jméno do seznamu", "die Ergebnisse in die Tabelle ein§výsledky do tabulky", "die neuen Daten in das System ein§nová data do systému"),
    extVerb("erledigen", "vyřizovat / dokončovat", "Práce a úkoly", "erledige|erledigst|erledigt|erledigen|erledigen", "vyřizuji|vyřizuješ|vyřizuje|vyřizujeme|vyřizují",
        "die Aufgabe bis morgen§úkol do zítřka", "die Arbeit vor der Pause§práci před přestávkou", "alle offenen Punkte§všechny otevřené body", "die Bestellung noch heute§objednávku ještě dnes", "die Formalitäten am Vormittag§formality dopoledne"),
    extVerb("kontrollieren", "kontrolovat", "Práce a kontrola", "kontrolliere|kontrollierst|kontrolliert|kontrollieren|kontrollieren", "kontroluji|kontroluješ|kontroluje|kontrolujeme|kontrolují",
        "die Qualität der Teile§kvalitu dílů", "die Rechnung vor der Zahlung§fakturu před platbou", "die Angaben im Formular§údaje ve formuláři", "den Bericht auf Fehler§zprávu kvůli chybám", "die Lieferung bei der Ankunft§dodávku při příjezdu"),

    // 21. Názory a rozpoznávání
    extVerb("glauben", "věřit / domnívat se", "Názory", "glaube|glaubst|glaubt|glauben|glauben", "věřím|věříš|věří|věříme|věří",
        "an eine gute Lösung§v dobré řešení", "dass der Zug pünktlich ist§že vlak jede včas", "seinen Worten nicht§jeho slovům ne", "an den Erfolg des Projekts§v úspěch projektu", "dass wir genug Zeit haben§že máme dost času"),
    extVerb("meinen", "myslet / mít na mysli", "Názory a vyjádření", "meine|meinst|meint|meinen|meinen", "myslím|myslíš|myslí|myslíme|myslí",
        "diesen Vorschlag§tento návrh", "dass es eine gute Idee ist§že je to dobrý nápad", "etwas ganz anderes§něco úplně jiného", "mit der Aussage etwas Bestimmtes§něco konkrétního tím výrokem", "dass wir früher anfangen sollten§že bychom měli začít dříve"),
    extVerb("behaupten", "tvrdit", "Názory a argumentace", "behaupte|behauptest|behauptet|behaupten|behaupten", "tvrdím|tvrdíš|tvrdí|tvrdíme|tvrdí",
        "dass der Preis zu hoch ist§že je cena příliš vysoká", "das Gegenteil§opak", "etwas ohne Beweise§něco bez důkazů", "dass alles schon fertig ist§že je všechno hotové", "seine Unschuld§svou nevinu"),
    extVerb("erkennen", "rozpoznávat", "Vnímání a porozumění", "erkenne|erkennst|erkennt|erkennen|erkennen", "rozpoznávám|rozpoznáváš|rozpoznává|rozpoznáváme|rozpoznávají",
        "den Unterschied sofort§rozdíl okamžitě", "die Stimme eines Freundes§hlas kamaráda", "das Problem auf den ersten Blick§problém na první pohled", "den Fehler in der Berechnung§chybu ve výpočtu", "eine bekannte Melodie§známou melodii"),
    extVerb("bemerken", "všímat si / zpozorovat", "Pozorování", "bemerke|bemerkst|bemerkt|bemerken|bemerken", "si všímám|si všímáš|si všímá|si všímáme|si všímají",
        "die Veränderung sofort§změny okamžitě", "einen Fehler im Text§chyby v textu", "den Unterschied zwischen den Bildern§rozdílu mezi obrázky", "die Verspätung erst später§zpoždění až později", "dass die Tür offen steht§že jsou dveře otevřené"),

    // 22. Vnímání a zážitky
    extVerb("fühlen", "cítit", "Vnímání a pocity", "fühle|fühlst|fühlt|fühlen|fühlen", "cítím|cítíš|cítí|cítíme|cítí",
        "die Wärme der Sonne§teplo slunce", "den kalten Wind im Gesicht§studený vítr v obličeji", "eine leichte Vibration§lehké vibrace", "den Stoff zwischen den Fingern§látku mezi prsty", "die Spannung vor dem Gespräch§napětí před rozhovorem"),
    extVerb("schmecken", "ochutnávat / chutnat", "Jídlo a smysly", "schmecke|schmeckst|schmeckt|schmecken|schmecken", "ochutnávám|ochutnáváš|ochutnává|ochutnáváme|ochutnávají",
        "die Kräuter in der Suppe§bylinky v polévce", "eine neue Soße§novou omáčku", "den Unterschied zwischen zwei Käsesorten§rozdíl mezi dvěma druhy sýra", "das Gericht vor dem Servieren§jídlo před podáváním", "etwas Süßes zum Nachtisch§něco sladkého jako dezert"),
    extVerb("riechen", "cítit / vonět", "Vůně a smysly", "rieche|riechst|riecht|riechen|riechen", "cítím vůni|cítíš vůni|cítí vůni|cítíme vůni|cítí vůni",
        "den frischen Kaffee§čerstvé kávy", "das Brot aus der Bäckerei§chléb z pekárny", "den Rauch im Flur§kouř na chodbě", "die Blumen im Garten§květiny na zahradě", "den Duft von Zitronen§vůni citronů"),
    extVerb("genießen", "užívat si", "Volný čas a zážitky", "genieße|genießt|genießt|genießen|genießen", "užívám si|užíváš si|užívá si|užíváme si|užívají si",
        "die Ruhe am Morgen§klid ráno", "den Urlaub am Meer§dovolenou u moře", "ein gutes Essen mit Freunden§dobré jídlo s přáteli", "den letzten Sonnenschein§poslední sluneční paprsky", "die freie Zeit mit der Familie§volný čas s rodinou"),
    extVerb("entdecken", "objevovat", "Cestování a poznávání", "entdecke|entdeckst|entdeckt|entdecken|entdecken", "objevuji|objevuješ|objevuje|objevujeme|objevují",
        "eine kleine Galerie in der Altstadt§malou galerii ve starém městě", "neue Orte auf Reisen§nová místa na cestách", "einen Fehler im Bericht§chybu ve zprávě", "ein interessantes Café§zajímavou kavárnu", "eine bessere Möglichkeit§lepší možnost"),

    // 23. Prosby, pravidla a sliby
    extVerb("bitten", "prosit", "Zdvořilé prosby", "bitte|bittest|bittet|bitten|bitten", "prosím|prosíš|prosí|prosíme|prosí",
        "einen Kollegen um Hilfe§kolegu o pomoc", "den Lehrer um eine Erklärung§učitele o vysvětlení", "die Nachbarn um Ruhe§sousedy o klid", "den Kunden um etwas Geduld§zákazníka o trochu trpělivosti", "meine Schwester um einen Gefallen§svou sestru o laskavost"),
    extVerb("erlauben", "dovolovat", "Pravidla a dovolení", "erlaube|erlaubst|erlaubt|erlauben|erlauben", "dovoluji|dovoluješ|dovoluje|dovolujeme|dovolují",
        "den Kindern eine kurze Pause§dětem krátkou přestávku", "Gästen das Fotografieren§hostům fotografování", "die Nutzung des Parkplatzes§používání parkoviště", "den Mitarbeitern flexible Arbeitszeiten§zaměstnancům pružnou pracovní dobu", "Hunden den Zutritt nicht§psům vstup ne"),
    extVerb("verbieten", "zakazovat", "Pravidla a dovolení", "verbiete|verbietest|verbietet|verbieten|verbieten", "zakazuji|zakazuješ|zakazuje|zakazujeme|zakazují",
        "das Rauchen im Gebäude§kouření v budově", "den Kindern das Handy beim Essen§dětem mobil při jídle", "das Parken vor dem Eingang§parkování před vchodem", "die Nutzung privater Geräte§používání soukromých zařízení", "den Zutritt ohne Erlaubnis§vstup bez povolení"),
    extVerb("versprechen", "slibovat", "Sliby a dohody", "verspreche|versprichst|verspricht|versprechen|versprechen", "slibuji|slibuješ|slibuje|slibujeme|slibují",
        "eine schnelle Antwort§rychlou odpověď", "pünktlich zu kommen§že přijdu včas", "den Bericht bis Freitag§zprávu do pátku", "bei dem Problem zu helfen§pomoci s problémem", "die Informationen vertraulich zu behandeln§zacházet s informacemi důvěrně"),
    extVerb("vermeiden", "vyhýbat se / předcházet", "Prevence a rozhodování", "vermeide|vermeidest|vermeidet|vermeiden|vermeiden", "se vyhýbám|se vyhýbáš|se vyhýbá|se vyhýbáme|se vyhýbají",
        "unnötige Fehler§zbytečným chybám", "lange Wartezeiten§dlouhému čekání", "Konflikte im Team§konfliktům v týmu", "unnötige Kosten§zbytečným nákladům", "Missverständnisse durch klare Absprachen§nedorozuměním díky jasným dohodám"),

    // 24. Výroba, projekty a dodávky
    extVerb("planen", "plánovat", "Projekty a organizace", "plane|planst|plant|planen|planen", "plánuji|plánuješ|plánuje|plánujeme|plánují",
        "den nächsten Schritt§další krok", "eine Reise für den Sommer§cestu na léto", "das Budget für das Projekt§rozpočet projektu", "eine Besprechung mit dem Team§poradu s týmem", "die Arbeit für die kommende Woche§práci na příští týden"),
    extVerb("organisieren", "organizovat", "Projekty a organizace", "organisiere|organisierst|organisiert|organisieren|organisieren", "organizuji|organizuješ|organizuje|organizujeme|organizují",
        "eine Veranstaltung für Kunden§akci pro zákazníky", "den Transport der Waren§přepravu zboží", "die Unterlagen für die Reise§dokumenty na cestu", "ein Treffen mit allen Kollegen§setkání se všemi kolegy", "die Arbeit in kleinen Gruppen§práci v malých skupinách"),
    extVerb("entwickeln", "vyvíjet", "Vývoj a zlepšování", "entwickle|entwickelst|entwickelt|entwickeln|entwickeln", "vyvíjím|vyvíjíš|vyvíjí|vyvíjíme|vyvíjejí",
        "eine neue Lösung§nové řešení", "ein besseres Verfahren§lepší postup", "ein Konzept für das Projekt§koncept projektu", "eine Anwendung für das Handy§aplikaci pro mobil", "neue Ideen im Team§nové nápady v týmu"),
    extVerb("herstellen", "vyrábět", "Výroba a produkty", "stelle|stellst|stellt|stellen|stellen", "vyrábím|vyrábíš|vyrábí|vyrábíme|vyrábějí",
        "ein neues Bauteil her§novou součástku", "die Produkte in Deutschland her§výrobky v Německu", "Ersatzteile nach Zeichnung her§náhradní díly podle výkresu", "die Verpackung selbst her§obal vlastními silami", "kleine Serien für den Kunden her§malé série pro zákazníka"),
    extVerb("liefern", "dodávat", "Dodávky a logistika", "liefere|lieferst|liefert|liefern|liefern", "dodávám|dodáváš|dodává|dodáváme|dodávají",
        "die Ware pünktlich§zboží včas", "die Ersatzteile an den Kunden§náhradní díly zákazníkovi", "das Material noch heute§materiál ještě dnes", "direkt an das Lager§přímo do skladu", "die Bestellung in zwei Paketen§objednávku ve dvou balících"),

    // 25. Kontrola, měření a počítání
    extVerb("prüfen", "kontrolovat / prověřovat", "Kontrola a kvalita", "prüfe|prüfst|prüft|prüfen|prüfen", "kontroluji|kontroluješ|kontroluje|kontrolujeme|kontrolují",
        "die Qualität vor dem Versand§kvalitu před odesláním", "die Unterlagen auf Vollständigkeit§úplnost dokumentů", "die Angaben im Vertrag§údaje ve smlouvě", "ob die Maschine richtig funktioniert§zda stroj funguje správně", "die Ergebnisse noch einmal§výsledky ještě jednou"),
    extVerb("messen", "měřit", "Měření a technika", "messe|misst|misst|messen|messen", "měřím|měříš|měří|měříme|měří",
        "die Länge des Bauteils§délku součástky", "die Temperatur im Raum§teplotu v místnosti", "den Abstand genau§vzdálenost přesně", "den Stromverbrauch regelmäßig§spotřebu elektřiny pravidelně", "den Durchmesser mit einem Messgerät§průměr měřicím přístrojem"),
    extVerb("vergleichen", "porovnávat", "Analýza a rozhodování", "vergleiche|vergleichst|vergleicht|vergleichen|vergleichen", "porovnávám|porovnáváš|porovnává|porovnáváme|porovnávají",
        "die beiden Angebote§obě nabídky", "die Preise verschiedener Anbieter§ceny různých dodavatelů", "die Ergebnisse mit dem Vorjahr§výsledky s předchozím rokem", "zwei unterschiedliche Lösungen§dvě různá řešení", "die Daten aus mehreren Quellen§data z několika zdrojů"),
    extVerb("rechnen", "počítat", "Počty a rozpočet", "rechne|rechnest|rechnet|rechnen|rechnen", "počítám|počítáš|počítá|počítáme|počítají",
        "mit höheren Kosten§s vyššími náklady", "den Gesamtpreis genau§celkovou cenu přesně", "mit einem Taschenrechner§kalkulačkou", "die Steuer selbst§daň samostatně", "mit einer längeren Lieferzeit§s delší dodací lhůtou"),
    extVerb("zählen", "počítat / sčítat", "Počty a evidence", "zähle|zählst|zählt|zählen|zählen", "počítám|počítáš|počítá|počítáme|počítají",
        "die Teile vor dem Versand§díly před odesláním", "die Gäste an der Tür§hosty u dveří", "das Wechselgeld sorgfältig§drobné peníze pečlivě", "die Tage bis zum Urlaub§dny do dovolené", "die Stimmen nach der Abstimmung§hlasy po hlasování"),

    // 26. Objednávky, rezervace a reklamace
    extVerb("bestellen", "objednávat", "Objednávky a nákupy", "bestelle|bestellst|bestellt|bestellen|bestellen", "objednávám|objednáváš|objednává|objednáváme|objednávají",
        "einen Tisch für zwei Personen§stůl pro dvě osoby", "Ersatzteile beim Lieferanten§náhradní díly u dodavatele", "das Mittagessen im Voraus§oběd předem", "ein neues Ladegerät online§novou nabíječku online", "die Waren für nächste Woche§zboží na příští týden"),
    extVerb("reservieren", "rezervovat", "Rezervace a cestování", "reserviere|reservierst|reserviert|reservieren|reservieren", "rezervuji|rezervuješ|rezervuje|rezervujeme|rezervují",
        "ein Zimmer für drei Nächte§pokoj na tři noci", "einen Tisch am Fenster§stůl u okna", "zwei Plätze im Zug§dvě místa ve vlaku", "einen Parkplatz am Flughafen§parkovací místo na letišti", "die Tickets für das Konzert§lístky na koncert"),
    extVerb("buchen", "rezervovat / objednávat", "Rezervace a cestování", "buche|buchst|bucht|buchen|buchen", "rezervuji|rezervuješ|rezervuje|rezervujeme|rezervují",
        "einen Flug nach Wien§let do Vídně", "die Reise online§cestu online", "ein Hotel im Stadtzentrum§hotel v centru města", "eine Führung für die Gruppe§prohlídku pro skupinu", "die Fahrkarten über die App§jízdenky přes aplikaci"),
    extVerb("stornieren", "stornovat", "Rezervace a objednávky", "storniere|stornierst|storniert|stornieren|stornieren", "stornuji|stornuješ|stornuje|stornujeme|stornují",
        "die Hotelreservierung§rezervaci hotelu", "die Bestellung wegen eines Fehlers§objednávku kvůli chybě", "den Flug ohne zusätzliche Gebühren§let bez dalších poplatků", "einen Termin online§termín online", "die Reise noch heute§cestu ještě dnes"),
    extVerb("reklamieren", "reklamovat", "Zboží a zákaznický servis", "reklamiere|reklamierst|reklamiert|reklamieren|reklamieren", "reklamuji|reklamuješ|reklamuje|reklamujeme|reklamují",
        "ein beschädigtes Produkt§poškozený výrobek", "die Ware wegen eines Mangels§zboží kvůli závadě", "den falschen Artikel§nesprávnou položku", "das Gerät beim Händler§zařízení u prodejce", "die Lieferung schriftlich§dodávku písemně"),

    // 27. Rozhodnutí a názory
    extVerb("entscheiden", "rozhodovat", "Rozhodování", "entscheide|entscheidest|entscheidet|entscheiden|entscheiden", "rozhoduji|rozhoduješ|rozhoduje|rozhodujeme|rozhodují",
        "über den Vorschlag§o návrhu", "über die weitere Vorgehensweise§o dalším postupu", "heute gemeinsam§dnes společně", "nach sorgfältiger Prüfung§po pečlivém posouzení", "zwischen zwei Möglichkeiten§mezi dvěma možnostmi"),
    extVerb("annehmen", "přijímat", "Nabídky a dohody", "nehme|nimmst|nimmt|nehmen|nehmen", "přijímám|přijímáš|přijímá|přijímáme|přijímají",
        "den Vorschlag an§návrh", "die Einladung gern an§pozvání rád/a", "das Angebot bis Freitag an§nabídku do pátku", "die Lieferung persönlich an§dodávku osobně", "die Bedingungen des Vertrags an§podmínky smlouvy"),
    extVerb("ablehnen", "odmítat", "Nabídky a dohody", "lehne|lehnst|lehnt|lehnen|lehnen", "odmítám|odmítáš|odmítá|odmítáme|odmítají",
        "den Vorschlag höflich ab§návrh zdvořile", "das Angebot wegen des Preises ab§nabídku kvůli ceně", "die Einladung leider ab§pozvání bohužel", "eine unfaire Bedingung ab§nespravedlivou podmínku", "den Antrag schriftlich ab§žádost písemně"),
    extVerb("vorschlagen", "navrhovat", "Návrhy a spolupráce", "schlage|schlägst|schlägt|schlagen|schlagen", "navrhuji|navrhuješ|navrhuje|navrhujeme|navrhují",
        "eine andere Lösung vor§jiné řešení", "ein kurzes Treffen vor§krátké setkání", "eine Pause von zehn Minuten vor§desetiminutovou přestávku", "einen neuen Zeitplan vor§nový časový plán", "eine gemeinsame Prüfung vor§společnou kontrolu"),
    extVerb("zustimmen", "souhlasit", "Souhlas a diskuse", "stimme|stimmst|stimmt|stimmen|stimmen", "souhlasím|souhlasíš|souhlasí|souhlasíme|souhlasí",
        "dem Vorschlag zu§s návrhem", "dem neuen Termin zu§s novým termínem", "der Entscheidung des Teams zu§s rozhodnutím týmu", "dieser Bedingung nicht zu§s touto podmínkou ne", "dem Plan grundsätzlich zu§s plánem v zásadě"),

    // 28. Odpovědnost a dosažení cílů
    extVerb("übernehmen", "přebírat", "Odpovědnost a práce", "übernehme|übernimmst|übernimmt|übernehmen|übernehmen", "přebírám|přebíráš|přebírá|přebíráme|přebírají",
        "die Verantwortung für das Projekt§odpovědnost za projekt", "eine neue Aufgabe§nový úkol", "die Leitung des Teams§vedení týmu", "die Kosten für den Transport§náklady na přepravu", "die Arbeit des Kollegen§práci kolegy"),
    extVerb("erreichen", "dosahovat / dosáhnout", "Cíle a výsledky", "erreiche|erreichst|erreicht|erreichen|erreichen", "dosahuji|dosahuješ|dosahuje|dosahujeme|dosahují",
        "das nächste Zwischenziel§další dílčí cíl", "den Bahnhof in zehn Minuten§nádraží za deset minut", "ein gutes Ergebnis§dobrý výsledek", "die vereinbarte Qualität§dohodnutou kvalitu", "das Ziel trotz Schwierigkeiten§cíle navzdory obtížím"),
    extVerb("erwarten", "očekávat", "Očekávání a plány", "erwarte|erwartest|erwartet|erwarten|erwarten", "očekávám|očekáváš|očekává|očekáváme|očekávají",
        "heute eine Antwort§dnes odpověď", "eine schnelle Lieferung§rychlé dodání", "mehr Unterstützung vom Team§větší podporu od týmu", "einen Anruf am Nachmittag§telefonát odpoledne", "bessere Ergebnisse im nächsten Quartal§lepší výsledky v příštím čtvrtletí"),
    extVerb("hoffen", "doufat", "Přání a očekávání", "hoffe|hoffst|hofft|hoffen|hoffen", "doufám|doufáš|doufá|doufáme|doufají",
        "auf gutes Wetter§v dobré počasí", "auf eine positive Antwort§v kladnou odpověď", "dass der Zug pünktlich kommt§že vlak přijede včas", "auf bessere Ergebnisse§v lepší výsledky", "dass alles gut läuft§že vše dobře dopadne"),
    extVerb("beitragen", "přispívat", "Spolupráce a výsledky", "trage|trägst|trägt|tragen|tragen", "přispívám|přispíváš|přispívá|přispíváme|přispívají",
        "zum Erfolg des Projekts bei§k úspěchu projektu", "zur Verbesserung der Qualität bei§ke zlepšení kvality", "zu einer guten Zusammenarbeit bei§k dobré spolupráci", "zur Lösung des Problems bei§k řešení problému", "zur Sicherheit am Arbeitsplatz bei§k bezpečnosti na pracovišti"),

    // 29. Přinášení, nošení a výsledky
    extVerb("bringen", "přinášet / přivézt", "Věci a každodenní situace", "bringe|bringst|bringt|bringen|bringen", "přináším|přinášíš|přináší|přinášíme|přinášejí",
        "die Unterlagen zum Meeting§podklady na poradu", "den Kindern etwas zu essen§dětem něco k jídlu", "einen Regenschirm mit§deštník s sebou", "das Paket zur Post§balík na poštu", "dem Kunden die bestellten Teile§zákazníkovi objednané díly"),
    extVerb("tragen", "nést / nosit", "Oblečení a předměty", "trage|trägst|trägt|tragen|tragen", "nesu / nosím|neseš / nosíš|nese / nosí|neseme / nosíme|nesou / nosí",
        "eine schwere Tasche§těžkou tašku", "im Winter einen warmen Mantel§v zimě teplý kabát", "die Kisten in den Keller§bedny do sklepa", "bei der Arbeit Schutzkleidung§v práci ochranný oděv", "die Verantwortung für das Ergebnis§odpovědnost za výsledek"),
    extVerb("verlieren", "ztrácet", "Věci a soutěže", "verliere|verlierst|verliert|verlieren|verlieren", "ztrácím|ztrácíš|ztrácí|ztrácíme|ztrácejí",
        "meine Schlüssel§své klíče", "bei einem Spiel die Nerven§při hře nervy", "den Überblick über die Kosten§přehled o nákladech", "Zeit durch unnötige Aufgaben§čas zbytečnými úkoly", "leider oft den Anschluss§bohužel často přípoj"),
    extVerb("gewinnen", "vyhrávat / získávat", "Soutěže a výsledky", "gewinne|gewinnst|gewinnt|gewinnen|gewinnen", "vyhrávám|vyhráváš|vyhrává|vyhráváme|vyhrávají",
        "das Spiel mit zwei Toren§zápas o dva góly", "einen Preis bei einem Wettbewerb§cenu v soutěži", "mehr Kunden durch gute Beratung§více zákazníků díky dobrému poradenství", "das Vertrauen der Kollegen§důvěru kolegů", "die Unterstützung des Teams§podporu týmu"),
    extVerb("vergessen", "zapomínat", "Paměť a každodenní situace", "vergesse|vergisst|vergisst|vergessen|vergessen", "zapomínám|zapomínáš|zapomíná|zapomínáme|zapomínají",
        "meinen Regenschirm§svůj deštník", "den Termin beim Arzt§termín u lékaře", "das Passwort schon wieder§heslo zase", "die Schlüssel zu Hause§klíče doma", "den Namen des neuen Kollegen§jméno nového kolegy"),

    // 30. Poloha, pohyb a směr
    extVerb("sitzen", "sedět", "Poloha a místa", "sitze|sitzt|sitzt|sitzen|sitzen", "sedím|sedíš|sedí|sedíme|sedí",
        "am Fenster§u okna", "den ganzen Tag am Schreibtisch§celý den u psacího stolu", "im Wartezimmer§v čekárně", "neben meiner Schwester§vedle své sestry", "im Zug in Fahrtrichtung§ve vlaku po směru jízdy"),
    extVerb("stehen", "stát", "Poloha a místa", "stehe|stehst|steht|stehen|stehen", "stojím|stojíš|stojí|stojíme|stojí",
        "vor dem Eingang§před vchodem", "an der Bushaltestelle§na autobusové zastávce", "schon eine Stunde in der Schlange§už hodinu ve frontě", "neben dem Schrank§vedle skříně", "auf der Liste ganz oben§na seznamu úplně nahoře"),
    extVerb("liegen", "ležet", "Poloha a předměty", "liege|liegst|liegt|liegen|liegen", "ležím|ležíš|leží|ležíme|leží",
        "auf dem Sofa§na pohovce", "den ganzen Tag im Bett§celý den v posteli", "auf dem Tisch§na stole", "direkt am See§přímo u jezera", "im zweiten Stock§ve druhém patře"),
    extVerb("steigen", "stoupat / lézt", "Pohyb a výšky", "steige|steigst|steigt|steigen|steigen", "stoupám|stoupáš|stoupá|stoupáme|stoupají",
        "auf den Berg§na horu", "langsam die Treppe hinauf§pomalu po schodech nahoru", "in den nächsten Bus§do dalšího autobusu", "bei hohen Temperaturen schnell§při vysokých teplotách rychle", "aus dem Wasser§z vody"),
    extVerb("folgen", "následovat / řídit se", "Směr a pokyny", "folge|folgst|folgt|folgen|folgen", "následuji|následuješ|následuje|následujeme|následují",
        "dem Weg bis zur Brücke§cestu až k mostu", "den Anweisungen des Lehrers§pokyny učitele", "dem Fluss nach Norden§řeku směrem na sever", "dem Gespräch aufmerksam§rozhovoru pozorně", "den aktuellen Nachrichten§aktuálním zprávám")
)

private fun extTopic(id: String, title: String, subtitle: String, vararg verbIds: String) =
    VerbPracticeLesson("verb_extended_$id", title, subtitle, verbIds.toList())

internal val extendedVerbPracticeTopics = listOf(
    extTopic("household", "11. Domácnost a každodenní péče", "Otevírání, zavírání, úklid a péče o věci", "öffnen", "schließen", "aufräumen", "waschen", "putzen"),
    extTopic("communication", "12. Komunikace a vysvětlování", "Otázky, odpovědi, vysvětlování a vyprávění", "fragen", "antworten", "erklären", "zeigen", "erzählen"),
    extTopic("planning", "13. Plánování a domluvy", "Domlouvání termínů, změny a potvrzování", "vorbereiten", "vereinbaren", "verschieben", "absagen", "bestätigen"),
    extTopic("money", "14. Peníze a běžné výdaje", "Placení, spoření, výdělky a půjčování", "bezahlen", "sparen", "verdienen", "leihen", "ausgeben"),
    extTopic("rail", "15. Vlak a veřejná doprava", "Čekání, přestupování, nastupování a odjezdy", "warten", "umsteigen", "einsteigen", "aussteigen", "abfahren"),
    extTopic("air_travel", "16. Létání a příjezdy", "Letadla, přistávání, příjezdy a vyzvedávání", "fliegen", "landen", "starten", "ankommen", "abholen"),
    extTopic("housing", "17. Bydlení a uspořádání věcí", "Pronájem, stěhování a ukládání předmětů", "mieten", "vermieten", "umziehen", "stellen", "legen"),
    extTopic("social", "18. Spolupráce a pozvání", "Setkávání, účast, nabídky a doporučení", "treffen", "teilnehmen", "einladen", "anbieten", "empfehlen"),
    extTopic("digital", "19. Digitální komunikace", "Tisk, ukládání, mazání a přenos souborů", "drucken", "speichern", "löschen", "herunterladen", "hochladen"),
    extTopic("office", "20. Formuláře a pracovní úkoly", "Podepisování, vyplňování a kontrola údajů", "unterschreiben", "ausfüllen", "eintragen", "erledigen", "kontrollieren"),
    extTopic("opinions", "21. Názory a rozpoznávání", "Vyjádření názoru a rozpoznání souvislostí", "glauben", "meinen", "behaupten", "erkennen", "bemerken"),
    extTopic("senses", "22. Vnímání a zážitky", "Pocity, chutě, vůně a objevování", "fühlen", "schmecken", "riechen", "genießen", "entdecken"),
    extTopic("requests", "23. Prosby, pravidla a sliby", "Zdvořilé žádosti, povolení a závazky", "bitten", "erlauben", "verbieten", "versprechen", "vermeiden"),
    extTopic("production", "24. Výroba, projekty a dodávky", "Plánování, vývoj, výroba a dodání", "planen", "organisieren", "entwickeln", "herstellen", "liefern"),
    extTopic("analysis", "25. Kontrola, měření a počítání", "Ověřování, měření a porovnávání údajů", "prüfen", "messen", "vergleichen", "rechnen", "zählen"),
    extTopic("orders", "26. Objednávky a rezervace", "Objednat, rezervovat, stornovat a reklamovat", "bestellen", "reservieren", "buchen", "stornieren", "reklamieren"),
    extTopic("decisions", "27. Rozhodnutí a názory", "Přijímání návrhů, odmítnutí a souhlas", "entscheiden", "annehmen", "ablehnen", "vorschlagen", "zustimmen"),
    extTopic("responsibility", "28. Odpovědnost a dosažení cílů", "Přebírání úkolů, výsledky a očekávání", "übernehmen", "erreichen", "erwarten", "hoffen", "beitragen"),
    extTopic("actions", "29. Přinášení, nošení a výsledky", "Přinášení věcí, ztráty a úspěchy", "bringen", "tragen", "verlieren", "gewinnen", "vergessen"),
    extTopic("position", "30. Poloha, pohyb a směr", "Poloha těla, stoupání a následování pokynů", "sitzen", "stehen", "liegen", "steigen", "folgen")
)
