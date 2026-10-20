package com.deutschai.app

internal data class GuidedDialogueOption(
    val german: String,
    val czech: String,
    val replyGerman: String,
    val replyCzech: String
)

internal data class GuidedDialogueTurn(
    val promptGerman: String,
    val promptCzech: String,
    val options: List<GuidedDialogueOption>
)

internal data class GuidedDialogueTopic(
    val id: String,
    val title: String,
    val subtitle: String,
    val turns: List<GuidedDialogueTurn>
)

internal val guidedDialogueTopics = listOf(
    GuidedDialogueTopic(
        id = "kennenlernen",
        title = "Seznámení",
        subtitle = "Jméno, původ, bydliště a jazyky · A1",
        turns = listOf(
            GuidedDialogueTurn(
                "Hallo! Wie heißt du?",
                "Ahoj! Jak se jmenuješ?",
                listOf(
                    GuidedDialogueOption("Ich heiße Erik.", "Jmenuji se Erik.", "Freut mich, Erik!", "Těší mě, Eriku!"),
                    GuidedDialogueOption("Mein Name ist Erik.", "Moje jméno je Erik.", "Schön, dich kennenzulernen.", "Rád/a tě poznávám."),
                    GuidedDialogueOption("Ich bin Erik.", "Jsem Erik.", "Hallo, Erik! Willkommen.", "Ahoj, Eriku! Vítej.")
                )
            ),
            GuidedDialogueTurn(
                "Woher kommst du?",
                "Odkud pocházíš?",
                listOf(
                    GuidedDialogueOption("Ich komme aus Tschechien.", "Pocházím z Česka.", "Oh, schön! Tschechien ist sehr interessant.", "To je hezké! Česko je velmi zajímavé."),
                    GuidedDialogueOption("Ich komme aus Brünn.", "Pocházím z Brna.", "Brünn kenne ich! Eine schöne Stadt.", "Brno znám! Krásné město."),
                    GuidedDialogueOption("Ich komme aus Prag.", "Pocházím z Prahy.", "Prag ist eine wunderschöne Stadt.", "Praha je nádherné město.")
                )
            ),
            GuidedDialogueTurn(
                "Wo wohnst du jetzt?",
                "Kde teď bydlíš?",
                listOf(
                    GuidedDialogueOption("Ich wohne in Brünn.", "Bydlím v Brně.", "Wie gefällt dir Brünn?", "Jak se ti Brno líbí?"),
                    GuidedDialogueOption("Ich wohne in Prag.", "Bydlím v Praze.", "Prag hat viel zu bieten.", "Praha toho nabízí opravdu hodně."),
                    GuidedDialogueOption("Ich wohne in Tschechien.", "Bydlím v Česku.", "Das klingt gut. Wohnst du in einer Stadt?", "To zní dobře. Bydlíš ve městě?")
                )
            ),
            GuidedDialogueTurn(
                "Welche Sprachen sprichst du?",
                "Jakými jazyky mluvíš?",
                listOf(
                    GuidedDialogueOption("Ich spreche Tschechisch und ein bisschen Deutsch.", "Mluvím česky a trochu německy.", "Sehr gut! Wir üben zusammen Deutsch.", "Výborně! Budeme spolu procvičovat němčinu."),
                    GuidedDialogueOption("Ich spreche Tschechisch und Englisch.", "Mluvím česky a anglicky.", "Prima! Deutsch lernst du Schritt für Schritt.", "Skvělé! Němčinu se budeš učit krok za krokem."),
                    GuidedDialogueOption("Ich lerne Deutsch.", "Učím se německy.", "Das ist toll. Bleib dran!", "To je skvělé. Vydrž u toho!")
                )
            ),
            GuidedDialogueTurn(
                "Freut mich! Möchtest du noch etwas erzählen?",
                "Těší mě! Chceš ještě něco říct?",
                listOf(
                    GuidedDialogueOption("Ich arbeite in einer Firma.", "Pracuji ve firmě.", "Interessant! Was machst du dort?", "Zajímavé! Co tam děláš?"),
                    GuidedDialogueOption("Ich lerne gern Sprachen.", "Rád/a se učím jazyky.", "Das ist eine gute Gewohnheit.", "To je dobrý zvyk."),
                    GuidedDialogueOption("Danke, das war alles.", "Děkuji, to je vše.", "Gern geschehen. Bis bald!", "Rádo se stalo. Brzy na shledanou!")
                )
            )
        )
    ),
    GuidedDialogueTopic(
        id = "cafe",
        title = "V kavárně",
        subtitle = "Objednávka, nápoj, placení · A1",
        turns = listOf(
            GuidedDialogueTurn(
                "Guten Tag! Was möchten Sie bestellen?",
                "Dobrý den! Co si přejete objednat?",
                listOf(
                    GuidedDialogueOption("Ich hätte gern einen Kaffee.", "Dal/a bych si kávu.", "Gern. Möchten Sie Milch oder Zucker?", "Samozřejmě. Přejete si mléko nebo cukr?"),
                    GuidedDialogueOption("Ich möchte einen Tee, bitte.", "Chtěl/a bych čaj, prosím.", "Sehr gern. Möchten Sie Zitrone dazu?", "Samozřejmě. Přejete si k němu citron?"),
                    GuidedDialogueOption("Ein Wasser, bitte.", "Jednu vodu, prosím.", "Still oder mit Kohlensäure?", "Neperlivou, nebo perlivou?")
                )
            ),
            GuidedDialogueTurn(
                "Möchten Sie Milch oder Zucker?",
                "Přejete si mléko nebo cukr?",
                listOf(
                    GuidedDialogueOption("Mit Milch, bitte.", "S mlékem, prosím.", "Natürlich. Sonst noch etwas?", "Samozřejmě. Ještě něco?"),
                    GuidedDialogueOption("Ohne Zucker, bitte.", "Bez cukru, prosím.", "In Ordnung. Sonst noch etwas?", "Dobře. Ještě něco?"),
                    GuidedDialogueOption("Nein, danke.", "Ne, děkuji.", "Alles klar. Ich bringe Ihre Bestellung.", "Dobře. Přinesu vám objednávku.")
                )
            ),
            GuidedDialogueTurn(
                "Möchten Sie auch etwas essen?",
                "Dáte si také něco k jídlu?",
                listOf(
                    GuidedDialogueOption("Ein Stück Kuchen, bitte.", "Kousek dortu, prosím.", "Gern. Welchen Kuchen möchten Sie?", "Samozřejmě. Který dort si přejete?"),
                    GuidedDialogueOption("Ein Käsebrötchen, bitte.", "Sýrovou housku, prosím.", "Gute Wahl! Kommt sofort.", "Dobrá volba! Hned to bude."),
                    GuidedDialogueOption("Nein, nur das Getränk.", "Ne, jen nápoj.", "Natürlich. Ich bringe es gleich.", "Samozřejmě. Hned vám to přinesu.")
                )
            ),
            GuidedDialogueTurn(
                "Möchten Sie hier trinken oder zum Mitnehmen?",
                "Budete pít tady, nebo s sebou?",
                listOf(
                    GuidedDialogueOption("Hier, bitte.", "Tady, prosím.", "Sehr gern. Suchen Sie sich einen Platz aus.", "Samozřejmě. Vyberte si místo."),
                    GuidedDialogueOption("Zum Mitnehmen, bitte.", "S sebou, prosím.", "In Ordnung. Ich mache es fertig.", "Dobře. Připravím vám to."),
                    GuidedDialogueOption("Ich weiß noch nicht.", "Ještě nevím.", "Kein Problem. Sie können kurz überlegen.", "Žádný problém. Můžete si to v klidu rozmyslet.")
                )
            ),
            GuidedDialogueTurn(
                "Das macht zusammen sechs Euro. Möchten Sie die Rechnung?",
                "Celkem to stojí šest eur. Přejete si účtenku?",
                listOf(
                    GuidedDialogueOption("Ja, bitte.", "Ano, prosím.", "Hier ist Ihre Rechnung. Vielen Dank!", "Tady je vaše účtenka. Moc děkuji!"),
                    GuidedDialogueOption("Mit Karte, bitte.", "Kartou, prosím.", "Gern. Bitte halten Sie die Karte ans Gerät.", "Jistě. Přiložte prosím kartu k terminálu."),
                    GuidedDialogueOption("Bar, bitte.", "Hotově, prosím.", "Danke schön. Einen schönen Tag noch!", "Děkuji. Přeji hezký den!")
                )
            )
        )
    ),
    GuidedDialogueTopic(
        id = "einkaufen",
        title = "Nakupování",
        subtitle = "Hledání zboží, velikost a placení · A1",
        turns = listOf(
            GuidedDialogueTurn(
                "Guten Tag! Kann ich Ihnen helfen?",
                "Dobrý den! Mohu vám pomoci?",
                listOf(
                    GuidedDialogueOption("Ja, ich suche eine Jacke.", "Ano, hledám bundu.", "Natürlich. Welche Größe haben Sie?", "Samozřejmě. Jakou máte velikost?"),
                    GuidedDialogueOption("Ich sehe mich nur um, danke.", "Jen se rozhlížím, děkuji.", "Kein Problem. Sagen Sie Bescheid, wenn Sie Hilfe brauchen.", "Žádný problém. Řekněte, až budete potřebovat pomoct."),
                    GuidedDialogueOption("Ich suche schwarze Schuhe.", "Hledám černé boty.", "Die Schuhe finden Sie dort drüben.", "Boty najdete támhle.")
                )
            ),
            GuidedDialogueTurn(
                "Welche Größe brauchen Sie?",
                "Jakou velikost potřebujete?",
                listOf(
                    GuidedDialogueOption("Ich brauche Größe M.", "Potřebuji velikost M.", "Ich schaue nach, ob wir sie haben.", "Podívám se, jestli ji máme."),
                    GuidedDialogueOption("Haben Sie Größe L?", "Máte velikost L?", "Ja, ich bringe Ihnen Größe L.", "Ano, přinesu vám velikost L."),
                    GuidedDialogueOption("Ich bin nicht sicher.", "Nejsem si jistý/á.", "Sie können die Jacke gern anprobieren.", "Bundě si můžete vyzkoušet.")
                )
            ),
            GuidedDialogueTurn(
                "Möchten Sie die Jacke anprobieren?",
                "Chcete si bundu vyzkoušet?",
                listOf(
                    GuidedDialogueOption("Ja, wo ist die Umkleidekabine?", "Ano, kde je kabinka?", "Die Umkleidekabine ist rechts.", "Kabinka je vpravo."),
                    GuidedDialogueOption("Nein, die Größe passt.", "Ne, velikost sedí.", "Sehr gut. Möchten Sie sie kaufen?", "Výborně. Chcete ji koupit?"),
                    GuidedDialogueOption("Ja, bitte.", "Ano, prosím.", "Natürlich. Bitte folgen Sie mir.", "Samozřejmě. Pojďte prosím za mnou.")
                )
            ),
            GuidedDialogueTurn(
                "Die Jacke kostet fünfzig Euro. Ist das in Ordnung?",
                "Bunda stojí padesát eur. Je to v pořádku?",
                listOf(
                    GuidedDialogueOption("Ja, ich nehme sie.", "Ano, vezmu si ji.", "Prima. Sie können an der Kasse bezahlen.", "Skvělé. Zaplatit můžete u pokladny."),
                    GuidedDialogueOption("Das ist mir zu teuer.", "To je na mě příliš drahé.", "Ich kann Ihnen ein günstigeres Modell zeigen.", "Mohu vám ukázat levnější model."),
                    GuidedDialogueOption("Gibt es einen Rabatt?", "Je na ni sleva?", "Heute gibt es zehn Prozent Rabatt.", "Dnes je sleva deset procent.")
                )
            ),
            GuidedDialogueTurn(
                "Wie möchten Sie bezahlen?",
                "Jak si přejete zaplatit?",
                listOf(
                    GuidedDialogueOption("Mit Karte, bitte.", "Kartou, prosím.", "Danke. Hier ist Ihr Beleg.", "Děkuji. Tady je váš doklad."),
                    GuidedDialogueOption("Bar, bitte.", "Hotově, prosím.", "Vielen Dank. Einen schönen Tag!", "Moc děkuji. Hezký den!"),
                    GuidedDialogueOption("Kann ich kontaktlos bezahlen?", "Mohu zaplatit bezkontaktně?", "Ja, das ist möglich.", "Ano, to je možné.")
                )
            )
        )
    )
)

internal data class ArticleWord(val article: String, val word: String, val czech: String)
internal data class ArticleCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val words: List<ArticleWord>
)

private fun aw(article: String, word: String, czech: String) = ArticleWord(article, word, czech)

internal val articleCategories = listOf(
    ArticleCategory("zuhause", "Bydlení a domácnost", "Pokoje, nábytek a vybavení", listOf(
        aw("die","Wohnung","byt"), aw("das","Zimmer","pokoj"), aw("die","Küche","kuchyně"), aw("das","Bad","koupelna"), aw("der","Flur","chodba"),
        aw("die","Tür","dveře"), aw("das","Fenster","okno"), aw("der","Tisch","stůl"), aw("der","Stuhl","židle"), aw("das","Bett","postel"),
        aw("das","Sofa","pohovka"), aw("der","Schrank","skříň"), aw("die","Lampe","lampa"), aw("der","Teppich","koberec"), aw("der","Spiegel","zrcadlo"),
        aw("der","Schlüssel","klíč"), aw("die","Wand","zeď"), aw("der","Boden","podlaha"), aw("das","Dach","střecha"), aw("die","Treppe","schody"),
        aw("der","Balkon","balkon"), aw("der","Garten","zahrada"), aw("der","Kühlschrank","lednice"), aw("der","Herd","sporák"), aw("die","Waschmaschine","pračka")
    )),
    ArticleCategory("essen", "Jídlo a pití", "Běžné potraviny a nápoje", listOf(
        aw("der","Apfel","jablko"), aw("die","Banane","banán"), aw("das","Brot","chléb"), aw("der","Käse","sýr"), aw("die","Milch","mléko"),
        aw("das","Wasser","voda"), aw("der","Kaffee","káva"), aw("der","Tee","čaj"), aw("das","Fleisch","maso"), aw("der","Fisch","ryba"),
        aw("das","Ei","vejce"), aw("die","Suppe","polévka"), aw("der","Salat","salát"), aw("die","Kartoffel","brambora"), aw("der","Reis","rýže"),
        aw("die","Nudel","nudle"), aw("die","Tomate","rajče"), aw("die","Gurke","okurka"), aw("die","Zwiebel","cibule"), aw("die","Butter","máslo"),
        aw("der","Zucker","cukr"), aw("das","Salz","sůl"), aw("das","Öl","olej"), aw("der","Saft","džus"), aw("der","Kuchen","dort")
    )),
    ArticleCategory("familie", "Rodina a lidé", "Členové rodiny a blízcí", listOf(
        aw("die","Mutter","matka"), aw("der","Vater","otec"), aw("der","Bruder","bratr"), aw("die","Schwester","sestra"), aw("die","Eltern","rodiče"),
        aw("der","Sohn","syn"), aw("die","Tochter","dcera"), aw("das","Kind","dítě"), aw("das","Baby","miminko"), aw("die","Großmutter","babička"),
        aw("der","Großvater","dědeček"), aw("die","Oma","babička"), aw("der","Opa","dědeček"), aw("die","Tante","teta"), aw("der","Onkel","strýc"),
        aw("der","Cousin","bratranec"), aw("die","Cousine","sestřenice"), aw("der","Ehemann","manžel"), aw("die","Ehefrau","manželka"), aw("die","Familie","rodina"),
        aw("der","Mann","muž"), aw("die","Frau","žena"), aw("der","Nachbar","soused"), aw("die","Nachbarin","sousedka"), aw("der","Freund","kamarád")
    )),
    ArticleCategory("arbeit", "Práce a kancelář", "Povolání a pracovní prostředí", listOf(
        aw("die","Arbeit","práce"), aw("der","Beruf","povolání"), aw("das","Büro","kancelář"), aw("die","Firma","firma"), aw("der","Chef","šéf"),
        aw("die","Kollegin","kolegyně"), aw("der","Kollege","kolega"), aw("die","Besprechung","porada"), aw("der","Termin","termín"), aw("die","Aufgabe","úkol"),
        aw("das","Projekt","projekt"), aw("der","Computer","počítač"), aw("der","Drucker","tiskárna"), aw("der","Bildschirm","obrazovka"), aw("die","Tastatur","klávesnice"),
        aw("das","Telefon","telefon"), aw("die","E-Mail","e-mail"), aw("der","Vertrag","smlouva"), aw("das","Gehalt","plat"), aw("der","Urlaub","dovolená"),
        aw("die","Pause","přestávka"), aw("die","Bewerbung","žádost o práci"), aw("der","Kunde","zákazník"), aw("die","Kundin","zákaznice"), aw("die","Fabrik","továrna")
    )),
    ArticleCategory("stadt", "Město a doprava", "Orientace a cestování po městě", listOf(
        aw("die","Stadt","město"), aw("die","Straße","ulice"), aw("der","Platz","náměstí"), aw("der","Bahnhof","nádraží"), aw("der","Flughafen","letiště"),
        aw("die","Haltestelle","zastávka"), aw("der","Bus","autobus"), aw("die","Bahn","dráha / vlak"), aw("der","Zug","vlak"), aw("das","Auto","auto"),
        aw("das","Fahrrad","jízdní kolo"), aw("das","Taxi","taxi"), aw("das","Ticket","lístek"), aw("die","Fahrkarte","jízdenka"), aw("die","Ampel","semafor"),
        aw("die","Kreuzung","křižovatka"), aw("die","Brücke","most"), aw("der","Weg","cesta"), aw("die","Karte","mapa"), aw("das","Zentrum","centrum"),
        aw("das","Geschäft","obchod"), aw("der","Supermarkt","supermarket"), aw("die","Apotheke","lékárna"), aw("das","Krankenhaus","nemocnice"), aw("die","Polizei","policie")
    )),
    ArticleCategory("kleidung", "Oblečení a nakupování", "Velikosti, oblečení a obchod", listOf(
        aw("die","Kleidung","oblečení"), aw("das","Hemd","košile"), aw("das","T-Shirt","tričko"), aw("die","Hose","kalhoty"), aw("das","Kleid","šaty"),
        aw("der","Rock","sukně"), aw("die","Jacke","bunda"), aw("der","Mantel","kabát"), aw("der","Schuh","bota"), aw("die","Socke","ponožka"),
        aw("die","Mütze","čepice"), aw("der","Schal","šála"), aw("die","Tasche","taška"), aw("der","Rucksack","batoh"), aw("die","Geldbörse","peněženka"),
        aw("der","Preis","cena"), aw("der","Rabatt","sleva"), aw("die","Größe","velikost"), aw("die","Farbe","barva"), aw("die","Kasse","pokladna"),
        aw("die","Rechnung","účet"), aw("die","Quittung","účtenka"), aw("der","Markt","trh"), aw("der","Laden","obchod"), aw("das","Produkt","výrobek")
    )),
    ArticleCategory("koerper", "Tělo a zdraví", "Části těla a péče o zdraví", listOf(
        aw("der","Kopf","hlava"), aw("das","Auge","oko"), aw("das","Ohr","ucho"), aw("die","Nase","nos"), aw("der","Mund","ústa"),
        aw("der","Zahn","zub"), aw("der","Hals","krk"), aw("die","Schulter","rameno"), aw("der","Arm","paže"), aw("die","Hand","ruka"),
        aw("der","Finger","prst"), aw("der","Bauch","břicho"), aw("der","Rücken","záda"), aw("das","Bein","noha"), aw("das","Knie","koleno"),
        aw("der","Fuß","chodidlo"), aw("das","Herz","srdce"), aw("das","Blut","krev"), aw("die","Haut","kůže"), aw("der","Arzt","lékař"),
        aw("die","Ärztin","lékařka"), aw("die","Krankheit","nemoc"), aw("der","Schmerz","bolest"), aw("die","Tablette","tableta"), aw("das","Medikament","lék")
    )),
    ArticleCategory("zeit", "Čas a kalendář", "Dny, měsíce a časové údaje", listOf(
        aw("die","Zeit","čas"), aw("die","Uhr","hodiny"), aw("der","Tag","den"), aw("die","Woche","týden"), aw("der","Monat","měsíc"),
        aw("das","Jahr","rok"), aw("der","Morgen","ráno"), aw("der","Abend","večer"), aw("die","Nacht","noc"), aw("der","Mittag","poledne"),
        aw("die","Stunde","hodina"), aw("die","Minute","minuta"), aw("die","Sekunde","sekunda"), aw("der","Kalender","kalendář"), aw("das","Datum","datum"),
        aw("der","Geburtstag","narozeniny"), aw("der","Feiertag","svátek"), aw("das","Wochenende","víkend"), aw("der","Montag","pondělí"), aw("der","Dienstag","úterý"),
        aw("der","Mittwoch","středa"), aw("der","Donnerstag","čtvrtek"), aw("der","Freitag","pátek"), aw("der","Samstag","sobota"), aw("der","Sonntag","neděle")
    )),
    ArticleCategory("wetter", "Počasí a příroda", "Roční období a přírodní svět", listOf(
        aw("das","Wetter","počasí"), aw("die","Sonne","slunce"), aw("der","Regen","déšť"), aw("der","Schnee","sníh"), aw("der","Wind","vítr"),
        aw("die","Wolke","mrak"), aw("der","Himmel","nebe"), aw("die","Temperatur","teplota"), aw("der","Frühling","jaro"), aw("der","Sommer","léto"),
        aw("der","Herbst","podzim"), aw("der","Winter","zima"), aw("der","Baum","strom"), aw("die","Blume","květina"), aw("das","Gras","tráva"),
        aw("der","Wald","les"), aw("der","Berg","hora"), aw("der","See","jezero"), aw("der","Fluss","řeka"), aw("das","Meer","moře"),
        aw("der","Strand","pláž"), aw("die","Insel","ostrov"), aw("die","Erde","země"), aw("die","Luft","vzduch"), aw("das","Feuer","oheň")
    )),
    ArticleCategory("reise", "Cestování a hotel", "Ubytování a užitečná slovíčka na cestách", listOf(
        aw("die","Reise","cesta"), aw("der","Urlaub","dovolená"), aw("das","Hotel","hotel"), aw("das","Zimmer","pokoj"), aw("die","Rezeption","recepce"),
        aw("die","Reservierung","rezervace"), aw("der","Pass","pas"), aw("der","Koffer","kufr"), aw("der","Rucksack","batoh"), aw("der","Flug","let"),
        aw("das","Flugzeug","letadlo"), aw("der","Flughafen","letiště"), aw("der","Zug","vlak"), aw("der","Bahnhof","nádraží"), aw("der","Fahrplan","jízdní řád"),
        aw("der","Ausgang","východ"), aw("der","Eingang","vchod"), aw("die","Adresse","adresa"), aw("der","Stadtplan","plán města"), aw("der","Tourist","turista"),
        aw("die","Touristin","turistka"), aw("die","Sehenswürdigkeit","pamětihodnost"), aw("der","Passagier","cestující"), aw("der","Reiseplan","cestovní plán"), aw("die","Reiseversicherung","cestovní pojištění")
    )),
    ArticleCategory("restaurant", "Restaurace", "Objednávání jídla a placení", listOf(
        aw("das","Restaurant","restaurace"), aw("die","Speisekarte","jídelní lístek"), aw("der","Kellner","číšník"), aw("die","Kellnerin","servírka"), aw("die","Bestellung","objednávka"),
        aw("das","Getränk","nápoj"), aw("die","Vorspeise","předkrm"), aw("das","Hauptgericht","hlavní chod"), aw("der","Nachtisch","dezert"), aw("das","Trinkgeld","spropitné"),
        aw("die","Gabel","vidlička"), aw("das","Messer","nůž"), aw("der","Löffel","lžíce"), aw("der","Teller","talíř"), aw("das","Glas","sklenice"),
        aw("die","Tasse","šálek"), aw("die","Flasche","láhev"), aw("der","Wein","víno"), aw("das","Bier","pivo"), aw("der","Kaffee","káva"),
        aw("der","Tee","čaj"), aw("das","Wasser","voda"), aw("die","Rechnung","účet"), aw("der","Tisch","stůl"), aw("der","Platz","místo")
    )),
    ArticleCategory("schule", "Škola a vzdělávání", "Výuka, pomůcky a studium", listOf(
        aw("die","Schule","škola"), aw("der","Lehrer","učitel"), aw("die","Lehrerin","učitelka"), aw("der","Schüler","žák"), aw("die","Schülerin","žákyně"),
        aw("die","Klasse","třída"), aw("der","Unterricht","výuka"), aw("das","Fach","předmět"), aw("das","Buch","kniha"), aw("das","Heft","sešit"),
        aw("der","Stift","pero"), aw("der","Bleistift","tužka"), aw("der","Kugelschreiber","kuličkové pero"), aw("der","Radiergummi","guma"), aw("das","Lineal","pravítko"),
        aw("die","Tafel","tabule"), aw("die","Aufgabe","úloha"), aw("die","Frage","otázka"), aw("die","Antwort","odpověď"), aw("die","Prüfung","zkouška"),
        aw("die","Note","známka"), aw("die","Sprache","jazyk"), aw("das","Wort","slovo"), aw("der","Satz","věta"), aw("die","Grammatik","gramatika")
    )),
    ArticleCategory("freizeit", "Volný čas a sport", "Koníčky, sport a kultura", listOf(
        aw("die","Freizeit","volný čas"), aw("der","Sport","sport"), aw("der","Fußball","fotbal"), aw("das","Tennis","tenis"), aw("das","Schwimmen","plavání"),
        aw("das","Laufen","běh"), aw("die","Musik","hudba"), aw("das","Lied","píseň"), aw("das","Konzert","koncert"), aw("das","Kino","kino"),
        aw("der","Film","film"), aw("das","Theater","divadlo"), aw("das","Spiel","hra"), aw("die","Mannschaft","tým"), aw("der","Ball","míč"),
        aw("der","Trainer","trenér"), aw("das","Training","trénink"), aw("das","Hobby","koníček"), aw("das","Buch","kniha"), aw("der","Roman","román"),
        aw("die","Gitarre","kytara"), aw("das","Foto","fotografie"), aw("der","Tanz","tanec"), aw("die","Reise","cesta"), aw("das","Museum","muzeum")
    )),
    ArticleCategory("kommunikation", "Komunikace a telefon", "Rozhovory, zprávy a kontakty", listOf(
        aw("die","Sprache","jazyk"), aw("das","Wort","slovo"), aw("der","Satz","věta"), aw("die","Frage","otázka"), aw("die","Antwort","odpověď"),
        aw("das","Gespräch","rozhovor"), aw("die","Nachricht","zpráva"), aw("der","Brief","dopis"), aw("die","E-Mail","e-mail"), aw("das","Telefon","telefon"),
        aw("das","Handy","mobil"), aw("der","Anruf","telefonát"), aw("die","Nummer","číslo"), aw("die","Adresse","adresa"), aw("der","Name","jméno"),
        aw("der","Vorname","křestní jméno"), aw("der","Nachname","příjmení"), aw("das","Formular","formulář"), aw("die","Unterschrift","podpis"), aw("die","Post","pošta"),
        aw("das","Paket","balík"), aw("die","Einladung","pozvánka"), aw("der","Gruß","pozdrav"), aw("die","Übersetzung","překlad"), aw("das","Wörterbuch","slovník")
    )),
    ArticleCategory("haushalt", "Domácí práce", "Úklid a vybavení domácnosti", listOf(
        aw("der","Haushalt","domácnost"), aw("die","Reinigung","úklid"), aw("die","Wäsche","prádlo"), aw("der","Staubsauger","vysavač"), aw("der","Besen","koště"),
        aw("der","Eimer","kýbl"), aw("der","Lappen","hadr"), aw("das","Spülmittel","prostředek na nádobí"), aw("das","Geschirr","nádobí"), aw("der","Teller","talíř"),
        aw("die","Tasse","šálek"), aw("der","Topf","hrnec"), aw("die","Pfanne","pánev"), aw("das","Messer","nůž"), aw("die","Gabel","vidlička"),
        aw("der","Löffel","lžíce"), aw("der","Mülleimer","odpadkový koš"), aw("der","Müll","odpad"), aw("die","Seife","mýdlo"), aw("das","Handtuch","ručník"),
        aw("die","Dusche","sprcha"), aw("das","Bad","koupelna"), aw("das","Bett","postel"), aw("das","Kissen","polštář"), aw("die","Decke","deka")
    )),
    ArticleCategory("geld", "Peníze a služby", "Platby, ceny a banka", listOf(
        aw("das","Geld","peníze"), aw("der","Euro","euro"), aw("der","Cent","cent"), aw("der","Preis","cena"), aw("die","Kosten","náklady"),
        aw("das","Konto","účet"), aw("die","Bank","banka"), aw("die","Karte","karta"), aw("die","Kreditkarte","kreditní karta"), aw("das","Bargeld","hotovost"),
        aw("die","Münze","mince"), aw("der","Schein","bankovka"), aw("die","Rechnung","účet"), aw("die","Quittung","účtenka"), aw("das","Gehalt","plat"),
        aw("das","Einkommen","příjem"), aw("die","Ausgabe","výdaj"), aw("die","Einnahme","příjem"), aw("die","Miete","nájem"), aw("der","Kredit","úvěr"),
        aw("der","Zins","úrok"), aw("der","Vertrag","smlouva"), aw("die","Überweisung","převod"), aw("der","Geldautomat","bankomat"), aw("der","Wechselkurs","směnný kurz")
    )),
    ArticleCategory("technik", "Technologie a elektronika", "Zařízení a digitální svět", listOf(
        aw("der","Computer","počítač"), aw("der","Laptop","notebook"), aw("der","Bildschirm","obrazovka"), aw("die","Tastatur","klávesnice"), aw("die","Maus","myš"),
        aw("der","Drucker","tiskárna"), aw("das","Kabel","kabel"), aw("das","Ladegerät","nabíječka"), aw("der","Akku","akumulátor"), aw("die","Batterie","baterie"),
        aw("die","Steckdose","zásuvka"), aw("das","Smartphone","chytrý telefon"), aw("das","Handy","mobil"), aw("das","Tablet","tablet"), aw("das","Internet","internet"),
        aw("die","Webseite","webová stránka"), aw("das","Passwort","heslo"), aw("die","Datei","soubor"), aw("der","Ordner","složka"), aw("das","Programm","program"),
        aw("die","App","aplikace"), aw("die","Software","software"), aw("das","Update","aktualizace"), aw("die","Nachricht","zpráva"), aw("die","Kamera","kamera")
    )),
    ArticleCategory("gefuehle", "Pocity a vlastnosti", "Slova pro každodenní komunikaci", listOf(
        aw("das","Gefühl","pocit"), aw("die","Freude","radost"), aw("das","Glück","štěstí"), aw("die","Liebe","láska"), aw("die","Angst","strach"),
        aw("die","Sorge","starost"), aw("die","Hoffnung","naděje"), aw("der","Mut","odvaha"), aw("die","Geduld","trpělivost"), aw("die","Lust","chuť"),
        aw("die","Wut","vztek"), aw("die","Trauer","smutek"), aw("der","Stolz","hrdost"), aw("der","Stress","stres"), aw("die","Ruhe","klid"),
        aw("die","Freundschaft","přátelství"), aw("das","Vertrauen","důvěra"), aw("der","Respekt","respekt"), aw("die","Hilfe","pomoc"), aw("das","Problem","problém"),
        aw("die","Frage","otázka"), aw("die","Meinung","názor"), aw("die","Idee","nápad"), aw("die","Entscheidung","rozhodnutí"), aw("der","Fehler","chyba")
    )),
    ArticleCategory("tiere", "Zvířata", "Domácí i volně žijící zvířata", listOf(
        aw("das","Tier","zvíře"), aw("der","Hund","pes"), aw("die","Katze","kočka"), aw("das","Pferd","kůň"), aw("die","Kuh","kráva"),
        aw("das","Schwein","prase"), aw("das","Schaf","ovce"), aw("die","Ziege","koza"), aw("das","Huhn","slepice"), aw("der","Hahn","kohout"),
        aw("der","Vogel","pták"), aw("die","Ente","kachna"), aw("die","Gans","husa"), aw("der","Fisch","ryba"), aw("die","Maus","myš"),
        aw("das","Kaninchen","králík"), aw("der","Hase","zajíc"), aw("der","Bär","medvěd"), aw("der","Wolf","vlk"), aw("der","Fuchs","liška"),
        aw("der","Löwe","lev"), aw("der","Tiger","tygr"), aw("der","Elefant","slon"), aw("der","Affe","opice"), aw("die","Schlange","had")
    )),
    ArticleCategory("unterwegs", "Na cestě a orientace", "Užitečné předměty a místa", listOf(
        aw("der","Weg","cesta"), aw("die","Richtung","směr"), aw("die","Seite","strana"), aw("die","Ecke","roh"), aw("die","Nähe","blízkost"),
        aw("die","Entfernung","vzdálenost"), aw("der","Eingang","vchod"), aw("der","Ausgang","východ"), aw("die","Treppe","schody"), aw("der","Aufzug","výtah"),
        aw("die","Etage","patro"), aw("der","Plan","plán"), aw("die","Information","informace"), aw("die","Auskunft","informace"), aw("der","Wegweiser","ukazatel"),
        aw("die","Haltestelle","zastávka"), aw("die","Fahrkarte","jízdenka"), aw("der","Fahrgast","cestující"), aw("die","Verspätung","zpoždění"), aw("der","Anschluss","přípoj"),
        aw("die","Abfahrt","odjezd"), aw("die","Ankunft","příjezd"), aw("der","Gepäckwagen","vozík na zavazadla"), aw("die","Toilette","toaleta"), aw("der","Notausgang","nouzový východ")
    ))
)

internal data class VerbPracticeTopic(
    val id: String,
    val infinitive: String,
    val czech: String,
    val description: String,
    val forms: List<String>,
    val subjects: List<String>,
    val phrases: List<String>
)

internal val verbPracticeTopics = listOf(
    VerbPracticeTopic(
        "sein", "sein", "být", "Stav, vlastnosti a představení",
        listOf("bin","bist","ist","sind","seid","sind"),
        listOf("Ich","Du","Er","Wir","Ihr","Sie"),
        listOf("heute müde","sehr freundlich","im Büro","zu Hause","pünktlich")
    ),
    VerbPracticeTopic(
        "haben", "haben", "mít", "Vlastnictví, čas a běžné potřeby",
        listOf("habe","hast","hat","haben","habt","haben"),
        listOf("Ich","Du","Er","Wir","Ihr","Sie"),
        listOf("einen Bruder","heute Zeit","ein Auto","eine Frage","viel Arbeit")
    ),
    VerbPracticeTopic(
        "werden", "werden", "stávat se / být v budoucnu", "Budoucnost a změna stavu",
        listOf("werde","wirst","wird","werden","werdet","werden"),
        listOf("Ich","Du","Er","Wir","Ihr","Sie"),
        listOf("bald müde","später fertig","morgen Arzt","bald erwachsen","nächstes Jahr älter")
    ),
    VerbPracticeTopic(
        "gehen", "gehen", "jít", "Pohyb a každodenní plány",
        listOf("gehe","gehst","geht","gehen","geht","gehen"),
        listOf("Ich","Du","Er","Wir","Ihr","Sie"),
        listOf("zur Arbeit","nach Hause","ins Kino","spazieren","in die Stadt")
    ),
    VerbPracticeTopic(
        "sprechen", "sprechen", "mluvit", "Jazyky a komunikace",
        listOf("spreche","sprichst","spricht","sprechen","sprecht","sprechen"),
        listOf("Ich","Du","Er","Wir","Ihr","Sie"),
        listOf("Deutsch","Englisch","mit dem Lehrer","sehr langsam","am Telefon")
    )
)

internal fun expandedCourseQuestions(id: String): List<LessonQuestion> {
    fun q(prompt: String, choices: List<String>, answer: Int, explanation: String) =
        LessonQuestion(prompt, choices, answer, explanation)

    return when (id) {
        "first_steps" -> listOf(
            q("Jak německy řekneš „Dobré ráno“?", listOf("Gute Nacht","Guten Morgen","Guten Abend","Danke"), 1, "„Guten Morgen“ znamená „Dobré ráno“."),
            q("Co znamená „Auf Wiedersehen“?", listOf("Dobrý večer","Prosím","Na shledanou","Dobrou noc"), 2, "„Auf Wiedersehen“ je zdvořilé rozloučení — „Na shledanou“."),
            q("Jak se řekne „Prosím / Není zač“?", listOf("Bitte","Danke","Tschüss","Nein"), 0, "„Bitte“ podle kontextu znamená „prosím“ nebo „není zač“."),
            q("Co znamená „Wie heißt du?“", listOf("Kde bydlíš?","Jak se jmenuješ?","Odkud jsi?","Kolik ti je?"), 1, "„Wie heißt du?“ znamená „Jak se jmenuješ?“"),
            q("Co znamená „Woher kommst du?“", listOf("Kam jdeš?","Co děláš?","Odkud pocházíš?","Jak se máš?"), 2, "„Woher“ se ptá na původ — „Odkud“."),
            q("„Ich komme aus Tschechien.“ znamená:", listOf("Bydlím v Německu.","Pocházím z Česka.","Učím se česky.","Jedu do Česka."), 1, "„Ich komme aus …“ znamená „Pocházím z …“."),
            q("Jak německy řekneš „Promiňte“?", listOf("Entschuldigung","Guten Morgen","Bitte sehr","Vielleicht"), 0, "„Entschuldigung“ znamená „Promiňte / Omlouvám se“."),
            q("Co znamená „Tschüss“?", listOf("Děkuji","Ahoj při loučení","Dobrou chuť","Nerozumím"), 1, "„Tschüss“ je neformální rozloučení."),
            q("Jak řekneš „Nerozumím“?", listOf("Ich verstehe nicht.","Ich weiß nicht.","Ich bin hier.","Ich komme gleich."), 0, "„Ich verstehe nicht“ znamená „Nerozumím“."),
            q("„Sprechen Sie Englisch?“ znamená:", listOf("Mluvíte anglicky?","Jste Angličan?","Kde je nádraží?","Rozumím anglicky."), 0, "„Sprechen Sie …?“ je zdvořilá otázka „Mluvíte …?“."),
            q("Jak se zeptáš na cenu?", listOf("Wie spät ist es?","Was kostet das?","Wo ist das?","Wer ist das?"), 1, "„Was kostet das?“ znamená „Kolik to stojí?“."),
            q("Které slovo znamená „voda“?", listOf("das Brot","der Kaffee","das Wasser","die Milch"), 2, "„Das Wasser“ znamená „voda“."),
            q("Které slovo znamená „chléb“?", listOf("das Brot","die Tür","der Tisch","das Buch"), 0, "„Das Brot“ znamená „chléb“."),
            q("Které slovo znamená „mléko“?", listOf("der Saft","die Milch","das Ei","die Suppe"), 1, "„Die Milch“ znamená „mléko“."),
            q("Doplň zdvořilou objednávku: Ich hätte gern ___ Kaffee.", listOf("einen","eine","ein","einem"), 0, "Říkáme „einen Kaffee“ — „Kaffee“ je mužského rodu a je zde ve 4. pádě."),
            q("„links“ znamená:", listOf("vpravo","rovně","vlevo","vzadu"), 2, "„Links“ znamená „vlevo“, „rechts“ znamená „vpravo“."),
            q("„rechts“ znamená:", listOf("vpravo","vlevo","daleko","nahoře"), 0, "„Rechts“ znamená „vpravo“."),
            q("Jak řekneš „Ano, děkuji“?", listOf("Nein, bitte.","Ja, danke.","Guten Abend.","Ich nicht."), 1, "„Ja, danke“ znamená „Ano, děkuji“."),
            q("Co znamená „Gute Nacht“?", listOf("Dobrou noc","Dobré ráno","Dobrý den","Na shledanou"), 0, "„Gute Nacht“ říkáme před spaním nebo při nočním loučení."),
            q("Co odpovíš na „Danke“?", listOf("Bitte!","Tschüss!","Nein!","Guten Morgen!"), 0, "Běžná odpověď na „Danke“ je „Bitte“ — „Není zač“.")
        )
        "sein" -> listOf(
            q("Doplň: Ich ___ heute zu Hause.", listOf("bin","bist","ist","seid"), 0, "S „ich“ používáme tvar „bin“."),
            q("Doplň: Du ___ mein Freund.", listOf("bin","bist","sind","seid"), 1, "S „du“ používáme tvar „bist“."),
            q("Doplň: Sie (ona) ___ sehr nett.", listOf("sind","seid","ist","bin"), 2, "Ve 3. osobě jednotného čísla používáme „ist“."),
            q("Doplň: Wir ___ bereit.", listOf("ist","seid","sind","bin"), 2, "S „wir“ používáme „sind“."),
            q("Doplň: Ihr ___ pünktlich.", listOf("seid","sind","bist","ist"), 0, "S „ihr“ používáme „seid“."),
            q("Doplň: Sie (oni) ___ im Büro.", listOf("ist","sind","seid","bin"), 1, "Ve 3. osobě množného čísla používáme „sind“."),
            q("Která věta je správně?", listOf("Ich bist müde.","Ich bin müde.","Ich sind müde.","Ich ist müde."), 1, "Správně: „Ich bin müde“ — „Jsem unavený/á“."),
            q("Která věta znamená „Ty jsi tady“?", listOf("Du ist hier.","Du sind hier.","Du bist hier.","Du bin hier."), 2, "Správně je „Du bist hier“."),
            q("Která věta znamená „On je doma“?", listOf("Er ist zu Hause.","Er sind zu Hause.","Er bist zu Hause.","Er bin zu Hause."), 0, "Pro „er“ používáme „ist“."),
            q("Doplň: Meine Eltern ___ nett.", listOf("ist","bin","seid","sind"), 3, "„Meine Eltern“ je množné číslo, proto „sind“."),
            q("Doplň: Das Wetter ___ schön.", listOf("sind","ist","bist","bin"), 1, "„Das Wetter“ je jednotné číslo: „ist“."),
            q("Doplň: Du und ich ___ Freunde.", listOf("sind","seid","ist","bin"), 0, "„Ty a já“ znamená „wir“, proto „sind“."),
            q("Jak německy řekneš „Jsem z Česka“?", listOf("Ich bist aus Tschechien.","Ich bin aus Tschechien.","Ich ist aus Tschechien.","Ich sind aus Tschechien."), 1, "S „ich“ používáme „bin“."),
            q("Doplň: Ihr ___ sehr freundlich.", listOf("ist","bin","seid","sind"), 2, "S „ihr“ používáme „seid“."),
            q("Doplň: Heute ___ Montag.", listOf("ist","sind","bist","seid"), 0, "„Heute ist Montag“ — „Dnes je pondělí“."),
            q("Doplň: Warum ___ du hier?", listOf("bin","ist","bist","sind"), 2, "Otázka s „du“ používá „bist“: „Warum bist du hier?“."),
            q("Který tvar patří k „es“?", listOf("bin","ist","bist","sind"), 1, "„Es ist“ — „ono je“."),
            q("Která věta je správně?", listOf("Wir sind müde.","Wir ist müde.","Wir seid müde.","Wir bin müde."), 0, "S „wir“ je správně „sind“."),
            q("Doplň: Die Kinder ___ im Park.", listOf("ist","seid","sind","bist"), 2, "„Die Kinder“ je množné číslo, proto „sind“."),
            q("Doplň: Ich ___ nicht krank.", listOf("ist","bin","bist","sind"), 1, "Zápor „nicht“ nemění tvar slovesa: „Ich bin nicht krank“.")
        )
        "articles" -> listOf(
            q("Který člen patří ke slovu „Apfel“ (jablko)?", listOf("die","das","der"), 2, "Správně je „der Apfel“. Uč se podstatná jména společně se členem."),
            q("Který člen patří ke slovu „Buch“ (kniha)?", listOf("das","der","die"), 0, "Správně je „das Buch“."),
            q("Který člen patří ke slovu „Tür“ (dveře)?", listOf("der","die","das"), 1, "Správně je „die Tür“."),
            q("Který člen patří ke slovu „Stuhl“ (židle)?", listOf("das","die","der"), 2, "Správně je „der Stuhl“."),
            q("Který člen patří ke slovu „Fenster“ (okno)?", listOf("die","das","der"), 1, "Správně je „das Fenster“."),
            q("Který člen patří ke slovu „Milch“ (mléko)?", listOf("der","die","das"), 1, "Správně je „die Milch“."),
            q("Který člen patří ke slovu „Kaffee“ (káva)?", listOf("das","der","die"), 1, "Správně je „der Kaffee“."),
            q("Který člen patří ke slovu „Auto“ (auto)?", listOf("die","das","der"), 1, "Správně je „das Auto“."),
            q("Který člen patří ke slovu „Schule“ (škola)?", listOf("der","die","das"), 1, "Správně je „die Schule“."),
            q("Který člen patří ke slovu „Arbeit“ (práce)?", listOf("der","das","die"), 2, "Správně je „die Arbeit“."),
            q("Který člen patří ke slovu „Kind“ (dítě)?", listOf("die","der","das"), 2, "Správně je „das Kind“."),
            q("Který člen patří ke slovu „Mutter“ (matka)?", listOf("das","die","der"), 1, "Správně je „die Mutter“."),
            q("Který člen patří ke slovu „Vater“ (otec)?", listOf("die","der","das"), 1, "Správně je „der Vater“."),
            q("Který člen patří ke slovu „Wasser“ (voda)?", listOf("der","das","die"), 1, "Správně je „das Wasser“."),
            q("Který člen patří ke slovu „Straße“ (ulice)?", listOf("der","die","das"), 1, "Správně je „die Straße“."),
            q("Který člen patří ke slovu „Bahnhof“ (nádraží)?", listOf("die","der","das"), 1, "Správně je „der Bahnhof“."),
            q("Který člen patří ke slovu „Zeit“ (čas)?", listOf("der","das","die"), 2, "Správně je „die Zeit“."),
            q("Který člen patří ke slovu „Problem“ (problém)?", listOf("die","das","der"), 1, "Správně je „das Problem“."),
            q("Který člen patří ke slovu „Sprache“ (jazyk)?", listOf("das","der","die"), 2, "Správně je „die Sprache“."),
            q("Který člen patří ke slovu „Termin“ (termín)?", listOf("die","das","der"), 2, "Správně je „der Termin“.")
        )
        else -> emptyList()
    }
}
