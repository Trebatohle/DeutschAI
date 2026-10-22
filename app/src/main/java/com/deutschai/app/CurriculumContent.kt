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

internal val starterGuidedDialogueTopics = listOf(
    GuidedDialogueTopic(
        id = "kennenlernen",
        title = "Seznámení",
        subtitle = "Jméno, původ, bydliště a jazyky · A1",
        turns = listOf(
            GuidedDialogueTurn(
                "Hallo! Wie heißt du?",
                "Ahoj! Jak se jmenuješ?",
                listOf(
                    GuidedDialogueOption("Ich heiße Erik, und ich freue mich, heute neue Leute kennenzulernen.", "Jmenuji se Erik a těším se, že dnes poznám nové lidi.", "Freut mich, Erik! Ich hoffe, dass du dich hier wohlfühlst und viele interessante Gespräche führst.", "Těší mě, Eriku! Doufám, že se tu budeš cítit dobře a povedeš mnoho zajímavých rozhovorů."),
                    GuidedDialogueOption("Mein Name ist Erik, aber meine Freunde nennen mich manchmal Eri.", "Jmenuji se Erik, ale přátelé mi někdy říkají Eri.", "Schön, dich kennenzulernen, und ich merke mir gern den Namen, den du am liebsten benutzt.", "Rád/a tě poznávám a ráda si zapamatuji jméno, které používáš nejraději."),
                    GuidedDialogueOption("Ich bin Erik und ich lerne Deutsch, weil ich mich auf Reisen besser verständigen möchte.", "Jsem Erik a učím se německy, protože se chci na cestách lépe domluvit.", "Hallo, Erik! Das ist ein schönes Ziel, und wir üben Schritt für Schritt, damit du sicherer sprechen kannst.", "Ahoj, Eriku! To je hezký cíl a budeme procvičovat krok za krokem, abys mohl mluvit jistěji.")
                )
            ),
            GuidedDialogueTurn(
                "Woher kommst du?",
                "Odkud pocházíš?",
                listOf(
                    GuidedDialogueOption("Ich komme aus Tschechien, wo es viele schöne Städte und Landschaften gibt.", "Pocházím z Česka, kde je mnoho krásných měst a krajiny.", "Wie schön! Ich würde das Land gern einmal besuchen, weil ich seine Kultur besser kennenlernen möchte.", "To je hezké! Ráda bych zemi někdy navštívila, protože chci lépe poznat její kulturu."),
                    GuidedDialogueOption("Ich komme aus Brünn, einer Stadt, in der es viele Cafés und interessante Veranstaltungen gibt.", "Pocházím z Brna, města, ve kterém je mnoho kaváren a zajímavých akcí.", "Brünn klingt spannend, und ich kann mir vorstellen, dass man dort auch am Wochenende viel unternehmen kann.", "Brno zní zajímavě a dokážu si představit, že se tam dá o víkendu podniknout mnoho věcí."),
                    GuidedDialogueOption("Ich komme aus Prag.", "Pocházím z Prahy.", "Prag ist eine wunderschöne Stadt.", "Praha je nádherné město.")
                )
            ),
            GuidedDialogueTurn(
                "Wo wohnst du jetzt?",
                "Kde teď bydlíš?",
                listOf(
                    GuidedDialogueOption("Ich wohne in Brünn.", "Bydlím v Brně.", "Brünn ist eine lebendige Stadt.", "Brno je živé město."),
                    GuidedDialogueOption("Ich wohne in Prag.", "Bydlím v Praze.", "Prag hat viel zu bieten.", "Praha toho nabízí opravdu hodně."),
                    GuidedDialogueOption("Ich wohne in Tschechien.", "Bydlím v Česku.", "Tschechien hat viele schöne Orte.", "Česko má mnoho krásných míst.")
                )
            ),
            GuidedDialogueTurn(
                "Welche Sprachen sprichst du?",
                "Jakými jazyky mluvíš?",
                listOf(
                    GuidedDialogueOption("Ich spreche Tschechisch und ein bisschen Deutsch, aber ich möchte flüssiger sprechen, wenn ich mit Muttersprachlern rede.", "Mluvím česky a trochu německy, ale chci mluvit plynuleji, když mluvím s rodilými mluvčími.", "Sehr gut! Wenn wir regelmäßig üben, wirst du die häufigsten Sätze immer schneller und sicherer benutzen.", "Výborně! Když budeme pravidelně procvičovat, budeš nejběžnější věty používat stále rychleji a jistěji."),
                    GuidedDialogueOption("Ich spreche Tschechisch und Englisch.", "Mluvím česky a anglicky.", "Prima! Deutsch lernst du Schritt für Schritt.", "Skvělé! Němčinu se budeš učit krok za krokem."),
                    GuidedDialogueOption("Ich lerne Deutsch.", "Učím se německy.", "Das ist toll. Bleib dran!", "To je skvělé. Vydrž u toho!")
                )
            ),
            GuidedDialogueTurn(
                "Freut mich! Möchtest du noch etwas erzählen?",
                "Těší mě! Chceš ještě něco říct?",
                listOf(
                    GuidedDialogueOption("Ich arbeite in einer Firma.", "Pracuji ve firmě.", "Das klingt interessant. Viel Erfolg bei der Arbeit!", "To zní zajímavě. Ať se ti v práci daří!"),
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
                    GuidedDialogueOption("Ich hätte gern einen Cappuccino, weil ich einen milden Kaffee mit etwas Milchschaum mag.", "Dal/a bych si cappuccino, protože mám rád/a jemnou kávu s trochou mléčné pěny.", "Sehr gern, ich bereite Ihren Cappuccino frisch zu und bringe ihn an den Tisch, sobald er fertig ist.", "Rád/a, připravím vám čerstvé cappuccino a přinesu ho ke stolu, jakmile bude hotové."),
                    GuidedDialogueOption("Einen Kaffee mit Milch, bitte, aber ohne Zucker, weil ich den natürlichen Geschmack besser finde.", "Kávu s mlékem, prosím, ale bez cukru, protože mi více vyhovuje přirozená chuť.", "Natürlich, ich bereite ihn genau so zu, damit er Ihren Wünschen entspricht.", "Samozřejmě, připravím ji přesně tak, aby odpovídala vašim přáním."),
                    GuidedDialogueOption("Einen Cappuccino, bitte.", "Cappuccino, prosím.", "Natürlich, Ihr Cappuccino kommt gleich.", "Samozřejmě, cappuccino bude hned.")
                )
            ),
            GuidedDialogueTurn(
                "Möchten Sie Milch oder Zucker?",
                "Přejete si mléko nebo cukr?",
                listOf(
                    GuidedDialogueOption("Mit Milch, bitte.", "S mlékem, prosím.", "Natürlich. Ich notiere das.", "Samozřejmě. Poznamenám si to."),
                    GuidedDialogueOption("Ohne Zucker, bitte.", "Bez cukru, prosím.", "In Ordnung, ohne Zucker.", "Dobře, bez cukru."),
                    GuidedDialogueOption("Nein, danke.", "Ne, děkuji.", "Alles klar. Ich bringe Ihre Bestellung.", "Dobře. Přinesu vám objednávku.")
                )
            ),
            GuidedDialogueTurn(
                "Möchten Sie auch etwas essen?",
                "Dáte si také něco k jídlu?",
                listOf(
                    GuidedDialogueOption("Ein Stück Kuchen, bitte.", "Kousek dortu, prosím.", "Sehr gern. Ich bringe Ihnen ein Stück Kuchen.", "Rád/a. Přinesu vám kousek dortu."),
                    GuidedDialogueOption("Ein Käsebrötchen, bitte.", "Sýrovou housku, prosím.", "Gute Wahl! Kommt sofort.", "Dobrá volba! Hned to bude."),
                    GuidedDialogueOption("Nein, nur das Getränk.", "Ne, jen nápoj.", "Natürlich. Ich bringe es gleich.", "Samozřejmě. Hned vám to přinesu.")
                )
            ),
            GuidedDialogueTurn(
                "Möchten Sie hier trinken oder zum Mitnehmen?",
                "Budete pít tady, nebo s sebou?",
                listOf(
                    GuidedDialogueOption("Ich bleibe hier, weil ich noch etwas lesen möchte, bevor ich wieder an die Arbeit gehe.", "Zůstanu tady, protože si chci ještě něco přečíst, než se vrátím do práce.", "Dann suchen Sie sich gern einen ruhigen Platz am Fenster aus, wo Sie sich gut konzentrieren können.", "Vyberte si tedy klidné místo u okna, kde se můžete dobře soustředit."),
                    GuidedDialogueOption("Zum Mitnehmen, bitte, weil ich in zehn Minuten einen Termin habe und pünktlich sein muss.", "S sebou, prosím, protože mám za deset minut schůzku a musím přijít včas.", "Natürlich, ich verpacke alles sorgfältig, damit Sie es unterwegs bequem mitnehmen können.", "Samozřejmě, všechno pečlivě zabalím, abyste si to mohl/a pohodlně vzít s sebou."),
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
                    GuidedDialogueOption("Ja, ich suche eine warme Jacke, weil ich im Winter oft zu Fuß unterwegs bin.", "Ano, hledám teplou bundu, protože v zimě často chodím pěšky.", "Dann zeige ich Ihnen dieses Modell, das winddicht ist und sich auch bei Regen gut eignet.", "Ukážu vám tedy tento model, který neprofoukne a hodí se i do deště."),
                    GuidedDialogueOption("Haben Sie eine blaue Jacke?", "Máte modrou bundu?", "Ja, blaue Jacken haben wir auch.", "Ano, máme i modré bundy."),
                    GuidedDialogueOption("Ich brauche eine warme Jacke.", "Potřebuji teplou bundu.", "Gern, ich helfe Ihnen, eine warme Jacke zu finden.", "Rád/a vám pomohu najít teplou bundu.")
                )
            ),
            GuidedDialogueTurn(
                "Welche Größe brauchen Sie?",
                "Jakou velikost potřebujete?",
                listOf(
                    GuidedDialogueOption("Ich brauche Größe M.", "Potřebuji velikost M.", "Ich schaue nach, ob wir sie haben.", "Podívám se, jestli ji máme."),
                    GuidedDialogueOption("Haben Sie Größe L?", "Máte velikost L?", "Ja, ich bringe Ihnen Größe L.", "Ano, přinesu vám velikost L."),
                    GuidedDialogueOption("Ich bin mir nicht sicher, deshalb würde ich gern zwei Größen anprobieren, bevor ich mich entscheide.", "Nejsem si jistý/á, proto bych si rád/a vyzkoušel/a dvě velikosti, než se rozhodnu.", "Das ist sinnvoll, denn je nach Schnitt kann die gleiche Größe unterschiedlich ausfallen.", "To je rozumné, protože podle střihu může stejná velikost sedět jinak.")
                )
            ),
            GuidedDialogueTurn(
                "Möchten Sie die Jacke anprobieren?",
                "Chcete si bundu vyzkoušet?",
                listOf(
                    GuidedDialogueOption("Ja, wo ist die Umkleidekabine?", "Ano, kde je kabinka?", "Die Umkleidekabine ist rechts.", "Kabinka je vpravo."),
                    GuidedDialogueOption("Nein, die Größe passt.", "Ne, velikost sedí.", "Sehr gut, dann passt die Jacke.", "Výborně, bunda tedy sedí."),
                    GuidedDialogueOption("Ja, bitte.", "Ano, prosím.", "Natürlich. Bitte folgen Sie mir.", "Samozřejmě. Pojďte prosím za mnou.")
                )
            ),
            GuidedDialogueTurn(
                "Die Jacke kostet fünfzig Euro. Ist das in Ordnung?",
                "Bunda stojí padesát eur. Je to v pořádku?",
                listOf(
                    GuidedDialogueOption("Ja, ich nehme sie.", "Ano, vezmu si ji.", "Prima. Sie können an der Kasse bezahlen.", "Skvělé. Zaplatit můžete u pokladny."),
                    GuidedDialogueOption("Der Preis ist in Ordnung. Ich kaufe sie.", "Cena je v pořádku. Koupím si ji.", "Sehr gut, ich packe die Jacke für Sie ein.", "Výborně, bundu vám zabalím."),
                    GuidedDialogueOption("Sie gefällt mir. Ich nehme die Jacke.", "Líbí se mi. Vezmu si tu bundu.", "Gute Wahl. Die Kasse ist dort drüben.", "Dobrá volba. Pokladna je támhle.")
                )
            ),
            GuidedDialogueTurn(
                "Wie möchten Sie bezahlen?",
                "Jak si přejete zaplatit?",
                listOf(
                    GuidedDialogueOption("Mit Karte, bitte.", "Kartou, prosím.", "Danke. Hier ist Ihr Beleg.", "Děkuji. Tady je váš doklad."),
                    GuidedDialogueOption("Bar, bitte.", "Hotově, prosím.", "Vielen Dank. Einen schönen Tag!", "Moc děkuji. Hezký den!"),
                    GuidedDialogueOption("Kann ich kontaktlos bezahlen?", "Mohu zaplatit bezkontaktně?", "Ja, das ist möglich. Vielen Dank für Ihren Einkauf!", "Ano, to je možné. Děkujeme za nákup!")
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

internal data class VerbPhrase(
    val german: String,
    val czech: String
)

internal data class VerbDefinition(
    val id: String,
    val infinitive: String,
    val czech: String,
    val description: String,
    val subjects: List<String>,
    val czechSubjects: List<String>,
    val forms: List<String>,
    val czechForms: List<String>,
    val phrases: List<VerbPhrase>
)

internal data class VerbPracticeLesson(
    val id: String,
    val title: String,
    val subtitle: String,
    val verbIds: List<String>
)

private fun vp(german: String, czech: String) = VerbPhrase(german, czech)

private val baseVerbDefinitions = listOf(
    VerbDefinition(
        "sein", "sein", "být", "Stav a vlastnosti",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("bin","bist","ist","sind","sind"), listOf("jsem","jsi","je","jsme","jsou"),
        listOf(vp("heute zu Hause","dnes doma"),vp("sehr müde","velmi unavený/á"),vp("im Büro","v kanceláři"),vp("pünktlich","včas"),vp("für die Prüfung bereit","připravený/á na zkoušku"))
    ),
    VerbDefinition(
        "haben", "haben", "mít", "Vlastnictví, čas a potřeby",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("habe","hast","hat","haben","haben"), listOf("mám","máš","má","máme","mají"),
        listOf(vp("heute viel Zeit","dnes hodně času"),vp("eine wichtige Frage","důležitou otázku"),vp("einen kleinen Hund","malého psa"),vp("am Wochenende frei","o víkendu volno"),vp("große Pläne","velké plány"))
    ),
    VerbDefinition(
        "wissen", "wissen", "vědět", "Informace a znalosti",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("weiß","weißt","weiß","wissen","wissen"), listOf("vím","víš","ví","víme","vědí"),
        listOf(vp("die richtige Antwort","správnou odpověď"),vp("wo der Bahnhof ist","kde je nádraží"),vp("wann der Zug fährt","kdy odjíždí vlak"),vp("das leider nicht","to bohužel ne"),vp("wie man dorthin kommt","jak se tam dostat"))
    ),
    VerbDefinition(
        "sprechen", "sprechen", "mluvit", "Jazyky a komunikace",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("spreche","sprichst","spricht","sprechen","sprechen"), listOf("mluvím","mluvíš","mluví","mluvíme","mluví"),
        listOf(vp("sehr gut Deutsch","velmi dobře německy"),vp("langsam und deutlich","pomalu a zřetelně"),vp("mit der Kollegin","s kolegyní"),vp("mehrere Fremdsprachen","několika cizími jazyky"),vp("am liebsten persönlich","nejraději osobně"))
    ),
    VerbDefinition(
        "laufen", "laufen", "běžet / chodit", "Pohyb a běh",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("laufe","läufst","läuft","laufen","laufen"), listOf("běžím","běžíš","běží","běžíme","běží"),
        listOf(vp("jeden Morgen im Park","každé ráno v parku"),vp("sehr schnell","velmi rychle"),vp("zur nächsten Haltestelle","k další zastávce"),vp("zusammen fünf Kilometer","společně pět kilometrů"),vp("nicht gern bei Regen","neradi v dešti"))
    ),
    VerbDefinition(
        "essen", "essen", "jíst", "Jídlo a každodenní návyky",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("esse","isst","isst","essen","essen"), listOf("jím","jíš","jí","jíme","jedí"),
        listOf(vp("morgens gern Haferflocken","ráno rád/a ovesné vločky"),vp("mittags in der Kantine","v poledne v kantýně"),vp("heute einen Salat","dnes salát"),vp("am Sonntag mit der Familie","v neděli s rodinou"),vp("kein Fleisch","žádné maso"))
    ),
    VerbDefinition(
        "trinken", "trinken", "pít", "Nápoje a zdravé návyky",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("trinke","trinkst","trinkt","trinken","trinken"), listOf("piji","piješ","pije","pijeme","pijí"),
        listOf(vp("jeden Tag genug Wasser","každý den dost vody"),vp("morgens schwarzen Kaffee","ráno černou kávu"),vp("nach dem Sport einen Tee","po sportu čaj"),vp("lieber Wasser als Limonade","raději vodu než limonádu"),vp("zum Essen ein Glas Saft","k jídlu sklenici džusu"))
    ),
    VerbDefinition(
        "gehen", "gehen", "jít", "Cesta, volný čas a plány",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("gehe","gehst","geht","gehen","gehen"), listOf("jdu","jdeš","jde","jdeme","jdou"),
        listOf(vp("nach der Arbeit nach Hause","po práci domů"),vp("am Freitag ins Kino","v pátek do kina"),vp("zu Fuß zur Schule","pěšky do školy"),vp("am Abend spazieren","večer na procházku"),vp("am Wochenende oft wandern","o víkendu často na túru"))
    ),
    VerbDefinition(
        "machen", "machen", "dělat", "Úkoly a každodenní činnosti",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("mache","machst","macht","machen","machen"), listOf("dělám","děláš","dělá","děláme","dělají"),
        listOf(vp("heute meine Hausaufgaben","dnes své domácí úkoly"),vp("am Wochenende Sport","o víkendu sport"),vp("eine kurze Pause","krátkou přestávku"),vp("jeden Abend das Abendessen","každý večer večeři"),vp("gemeinsam einen Plan","společně plán"))
    ),
    VerbDefinition(
        "können", "können", "moci / umět", "Schopnosti a možnosti",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("kann","kannst","kann","können","können"), listOf("mohu","můžeš","může","můžeme","mohou"),
        listOf(vp("heute länger bleiben","dnes zůstat déle"),vp("gut schwimmen","dobře plavat"),vp("dir morgen helfen","ti zítra pomoci"),vp("das Problem gemeinsam lösen","problém vyřešit společně"),vp("leider nicht kommen","bohužel přijít"))
    ),
    VerbDefinition(
        "wollen", "wollen", "chtít", "Přání a plány",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("will","willst","will","wollen","wollen"), listOf("chci","chceš","chce","chceme","chtějí"),
        listOf(vp("Deutsch besser lernen","lépe se učit německy"),vp("am Samstag verreisen","v sobotu odcestovat"),vp("ein neues Buch kaufen","koupit novou knihu"),vp("heute zu Hause bleiben","dnes zůstat doma"),vp("am Abend zusammen kochen","večer společně vařit"))
    ),
    VerbDefinition(
        "müssen", "müssen", "muset", "Povinnosti a nutnost",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("muss","musst","muss","müssen","müssen"), listOf("musím","musíš","musí","musíme","musí"),
        listOf(vp("morgen früh aufstehen","zítra brzy vstávat"),vp("heute länger arbeiten","dnes pracovat déle"),vp("noch einkaufen","ještě nakoupit"),vp("den Termin verschieben","termín přesunout"),vp("rechtzeitig am Bahnhof sein","být včas na nádraží"))
    ),
    VerbDefinition(
        "kommen", "kommen", "přijít / pocházet", "Původ, návštěvy a pohyb",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("komme","kommst","kommt","kommen","kommen"), listOf("přicházím","přicházíš","přichází","přicházíme","přicházejí"),
        listOf(vp("aus Tschechien","z Česka"),vp("heute etwas später","dnes o něco později"),vp("morgen zu Besuch","zítra na návštěvu"),vp("gerade vom Bahnhof","právě z nádraží"),vp("aus einer großen Familie","z velké rodiny"))
    ),
    VerbDefinition(
        "sehen", "sehen", "vidět", "Vnímání a pozorování",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("sehe","siehst","sieht","sehen","sehen"), listOf("vidím","vidíš","vidí","vidíme","vidí"),
        listOf(vp("den Bus schon","už autobus"),vp("gern deutsche Filme","rád/a německé filmy"),vp("einen Vogel im Garten","ptáka na zahradě"),vp("uns am Freitag","se v pátek"),vp("die Berge aus dem Fenster","hory z okna"))
    ),
    VerbDefinition(
        "nehmen", "nehmen", "vzít / brát", "Volba a každodenní situace",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("nehme","nimmst","nimmt","nehmen","nehmen"), listOf("beru","bereš","bere","bereme","berou"),
        listOf(vp("den nächsten Zug","další vlak"),vp("einen Kaffee ohne Zucker","kávu bez cukru"),vp("meinen Regenschirm mit","si s sebou deštník"),vp("heute ein Taxi","dnes taxi"),vp("die Schlüssel vom Tisch","klíče ze stolu"))
    ),
    VerbDefinition(
        "geben", "geben", "dát / dávat", "Předávání a zdvořilé prosby",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("gebe","gibst","gibt","geben","geben"), listOf("dávám","dáváš","dává","dáváme","dávají"),
        listOf(vp("dir meine Telefonnummer","ti své telefonní číslo"),vp("dem Kind ein Buch","dítěti knihu"),vp("heute eine Antwort","dnes odpověď"),vp("unserem Gast einen Kaffee","našemu hostu kávu"),vp("den Kindern etwas Zeit","dětem trochu času"))
    ),
    VerbDefinition(
        "fahren", "fahren", "jet / řídit", "Doprava a cestování",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("fahre","fährst","fährt","fahren","fahren"), listOf("jedu","jedeš","jede","jedeme","jedou"),
        listOf(vp("jeden Morgen mit dem Bus","každé ráno autobusem"),vp("morgen nach Berlin","zítra do Berlína"),vp("sehr vorsichtig","velmi opatrně"),vp("im Sommer ans Meer","v létě k moři"),vp("am liebsten mit dem Zug","nejraději vlakem"))
    ),
    VerbDefinition(
        "lesen", "lesen", "číst", "Čtení a studium",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("lese","liest","liest","lesen","lesen"), listOf("čtu","čteš","čte","čteme","čtou"),
        listOf(vp("jeden Abend ein paar Seiten","každý večer několik stran"),vp("gerne spannende Geschichten","rád/a napínavé příběhy"),vp("die Nachrichten am Morgen","ráno zprávy"),vp("zusammen deutsche Texte","společně německé texty"),vp("vor dem Schlafen ein Buch","před spaním knihu"))
    ),
    VerbDefinition(
        "schreiben", "schreiben", "psát", "Zprávy a psaní",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("schreibe","schreibst","schreibt","schreiben","schreiben"), listOf("píšu","píšeš","píše","píšeme","píšou"),
        listOf(vp("meiner Familie eine Nachricht","své rodině zprávu"),vp("heute eine E-Mail","dnes e-mail"),vp("seinen Namen auf das Formular","své jméno na formulář"),vp("eine Liste für den Einkauf","seznam na nákup"),vp("jeden Tag ein paar Sätze","každý den několik vět"))
    ),
    VerbDefinition(
        "arbeiten", "arbeiten", "pracovat", "Práce a pracovní návyky",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("arbeite","arbeitest","arbeitet","arbeiten","arbeiten"), listOf("pracuji","pracuješ","pracuje","pracujeme","pracují"),
        listOf(vp("heute bis fünf Uhr","dnes do pěti hodin"),vp("in einem kleinen Büro","v malé kanceláři"),vp("gerne im Team","rád/a v týmu"),vp("an einem neuen Projekt","na novém projektu"),vp("morgen von zu Hause","zítra z domova"))
    ),
    VerbDefinition(
        "kaufen", "kaufen", "kupovat", "Nakupování a potřeby",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("kaufe","kaufst","kauft","kaufen","kaufen"), listOf("kupuji","kupuješ","kupuje","kupujeme","kupují"),
        listOf(vp("frisches Brot beim Bäcker","čerstvý chléb u pekaře"),vp("heute eine Fahrkarte","dnes jízdenku"),vp("ein Geschenk für die Mutter","dárek pro maminku"),vp("Obst auf dem Markt","ovoce na trhu"),vp("nur das, was wir brauchen","jen to, co potřebujeme"))
    ),
    VerbDefinition(
        "finden", "finden", "najít / považovat", "Názory a hledání",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("finde","findest","findet","finden","finden"), listOf("nacházím","nacházíš","nachází","nacházíme","nacházejí"),
        listOf(vp("diesen Film sehr spannend","tento film velmi napínavý"),vp("meine Schlüssel nicht","své klíče nemohu najít"),vp("die Idee wirklich gut","ten nápad opravdu dobrý"),vp("den Weg zum Hotel","cestu k hotelu"),vp("eine gute Lösung","dobré řešení"))
    ),
    VerbDefinition(
        "bleiben", "bleiben", "zůstat", "Plány a změny",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("bleibe","bleibst","bleibt","bleiben","bleiben"), listOf("zůstávám","zůstáváš","zůstává","zůstáváme","zůstávají"),
        listOf(vp("heute zu Hause","dnes doma"),vp("bis morgen hier","tady do zítřka"),vp("am Wochenende länger","o víkendu déle"),vp("bei meiner Familie","u své rodiny"),vp("trotz des Regens draußen","navzdory dešti venku"))
    ),
    VerbDefinition(
        "lernen", "lernen", "učit se", "Jazyky a dovednosti",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("lerne","lernst","lernt","lernen","lernen"), listOf("učím se","učíš se","učí se","učíme se","učí se"),
        listOf(vp("jeden Tag zehn neue Wörter","každý den deset nových slov"),vp("Deutsch mit einer App","němčinu pomocí aplikace"),vp("für die Prüfung","na zkoušku"),vp("am liebsten mit Beispielen","nejraději s příklady"),vp("zusammen mit Freunden","společně s přáteli"))
    ),
    VerbDefinition(
        "schlafen", "schlafen", "spát", "Denní režim a odpočinek",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("schlafe","schläfst","schläft","schlafen","schlafen"), listOf("spím","spíš","spí","spíme","spí"),
        listOf(vp("normalerweise acht Stunden","obvykle osm hodin"),vp("am Sonntag länger","v neděli déle"),vp("bei offenem Fenster","u otevřeného okna"),vp("im Urlaub besonders gut","na dovolené obzvlášť dobře"),vp("heute wegen des Lärms schlecht","dnes kvůli hluku špatně"))
    ),
    VerbDefinition(
        "hören", "hören", "slyšet / poslouchat", "Hudba a porozumění",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("höre","hörst","hört","hören","hören"), listOf("slyším","slyšíš","slyší","slyšíme","slyší"),
        listOf(vp("gerne Musik beim Kochen","rád/a hudbu při vaření"),vp("die Durchsage am Bahnhof","hlášení na nádraží"),vp("ein interessantes Hörbuch","zajímavou audioknihu"),vp("den Lehrer gut","učitele dobře"),vp("im Radio die Nachrichten","v rádiu zprávy"))
    ),
    VerbDefinition(
        "denken", "denken", "myslet", "Myšlení a názory",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("denke","denkst","denkt","denken","denken"), listOf("myslím","myslíš","myslí","myslíme","myslí"),
        listOf(vp("oft an meine Familie","často na svou rodinu"),vp("dass die Idee gut ist","že je ten nápad dobrý"),vp("gerade über das Problem nach","právě o tom problému"),vp("gemeinsam über eine Lösung nach","společně o řešení"),vp("an unseren nächsten Urlaub","na naši příští dovolenou"))
    ),
    VerbDefinition(
        "spielen", "spielen", "hrát", "Sport a volný čas",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("spiele","spielst","spielt","spielen","spielen"), listOf("hraji","hraješ","hraje","hrajeme","hrají"),
        listOf(vp("am Wochenende Fußball","o víkendu fotbal"),vp("gerne Gitarre","rád/a na kytaru"),vp("mit seinen Kindern","se svými dětmi"),vp("jeden Freitag Karten","každý pátek karty"),vp("zusammen im Park","společně v parku"))
    ),
    VerbDefinition(
        "wohnen", "wohnen", "bydlet", "Bydlení a domov",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("wohne","wohnst","wohnt","wohnen","wohnen"), listOf("bydlím","bydlíš","bydlí","bydlíme","bydlí"),
        listOf(vp("in einer kleinen Wohnung","v malém bytě"),vp("seit zwei Jahren in Brünn","už dva roky v Brně"),vp("nicht weit vom Zentrum","nedaleko centra"),vp("zusammen mit meiner Familie","společně se svou rodinou"),vp("gerne auf dem Land","rád/a na venkově"))
    ),
    VerbDefinition(
        "kochen", "kochen", "vařit", "Jídlo a domácnost",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("koche","kochst","kocht","kochen","kochen"), listOf("vařím","vaříš","vaří","vaříme","vaří"),
        listOf(vp("jeden Abend etwas Warmes","každý večer něco teplého"),vp("gerne mit frischem Gemüse","rád/a s čerstvou zeleninou"),vp("heute für die ganze Familie","dnes pro celou rodinu"),vp("am Wochenende gemeinsam","o víkendu společně"),vp("nach einem neuen Rezept","podle nového receptu"))
    ),
    VerbDefinition(
        "brauchen", "brauchen", "potřebovat", "Předměty a potřeby",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("brauche","brauchst","braucht","brauchen","brauchen"), listOf("potřebuji","potřebuješ","potřebuje","potřebujeme","potřebují"),
        listOf(vp("mehr Zeit für die Aufgabe","více času na úkol"),vp("heute deine Hilfe","dnes tvou pomoc"),vp("noch zwei Fahrkarten","ještě dvě jízdenky"),vp("einen Termin beim Arzt","termín u lékaře"),vp("für die Reise einen Reisepass","na cestu cestovní pas"))
    ),
    VerbDefinition(
        "dürfen", "dürfen", "smět", "Pravidla a dovolení",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("darf","darfst","darf","dürfen","dürfen"), listOf("smím","smíš","smí","smíme","smí"),
        listOf(vp("hier parken","tady parkovat"),vp("das Fenster öffnen","otevřít okno"),vp("heute früher gehen","dnes odejít dříve"),vp("hier fotografieren","tady fotografovat"),vp("deinen Stift benutzen","použít tvé pero"))
    ),
    VerbDefinition(
        "sollen", "sollen", "mít (povinnost / doporučení)", "Doporučení a pokyny",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("soll","sollst","soll","sollen","sollen"), listOf("mám","máš","má","máme","mají"),
        listOf(vp("mehr Wasser trinken","pít více vody"),vp("die Aufgabe heute abgeben","dnes odevzdat úkol"),vp("den Arzt anrufen","zavolat lékaři"),vp("ruhiger sprechen","mluvit klidněji"),vp("das Formular unterschreiben","podepsat formulář"))
    ),
    VerbDefinition(
        "verstehen", "verstehen", "rozumět", "Porozumění a komunikace",
        listOf("Ich","Du","Er","Wir","Sie"), listOf("Já","Ty","On/Ona","My","Oni"),
        listOf("verstehe","verstehst","versteht","verstehen","verstehen"), listOf("rozumím","rozumíš","rozumí","rozumíme","rozumí"),
        listOf(vp("die Frage sehr gut","otázce velmi dobře"),vp("diesen Satz nicht","této větě nerozumím"),vp("den Text ohne Wörterbuch","textu bez slovníku"),vp("einander immer besser","si navzájem stále lépe"),vp("die Erklärung jetzt","vysvětlení teď"))
    )
)

internal val verbDefinitions = baseVerbDefinitions + extendedVerbDefinitions

private val baseVerbPracticeTopics = listOf(
    VerbPracticeLesson("verb_basics", "1. Základní slovesa", "Být, mít, přijít, dělat a jít", listOf("sein","haben","kommen","machen","gehen")),
    VerbPracticeLesson("verb_communication", "2. Komunikace a znalosti", "Mluvit, vědět, rozumět, číst a psát", listOf("sprechen","wissen","verstehen","lesen","schreiben")),
    VerbPracticeLesson("verb_food", "3. Jídlo a nákup", "Jíst, pít, kupovat, brát a potřebovat", listOf("essen","trinken","kaufen","nehmen","brauchen")),
    VerbPracticeLesson("verb_movement", "4. Pohyb a cestování", "Jít, běžet, jet, přijít a zůstat", listOf("gehen","laufen","fahren","kommen","bleiben")),
    VerbPracticeLesson("verb_daily", "5. Každodenní režim", "Pracovat, učit se, spát, vařit a dělat", listOf("arbeiten","lernen","schlafen","kochen","machen")),
    VerbPracticeLesson("verb_modal", "6. Možnosti a povinnosti", "Umět, chtít, muset, smět a mít povinnost", listOf("können","wollen","müssen","dürfen","sollen")),
    VerbPracticeLesson("verb_perception", "7. Vnímání a myšlení", "Vidět, slyšet, myslet, vědět a najít", listOf("sehen","hören","denken","wissen","finden")),
    VerbPracticeLesson("verb_home", "8. Domov a potřeby", "Bydlet, vařit, potřebovat, mít a zůstat", listOf("wohnen","kochen","brauchen","haben","bleiben")),
    VerbPracticeLesson("verb_work", "9. Práce a komunikace", "Pracovat, mluvit, psát, dávat a brát", listOf("arbeiten","sprechen","schreiben","geben","nehmen")),
    VerbPracticeLesson("verb_mixed", "10. Smíšené opakování", "Mix důležitých sloves ze všech předchozích sad", listOf("sein","haben","wissen","essen","trinken"))
)

internal val verbPracticeTopics = baseVerbPracticeTopics + extendedVerbPracticeTopics

internal val advancedVerbPracticeTopics = verbPracticeTopics.mapIndexed { index, topic ->
    topic.copy(
        id = "advanced_${topic.id}",
        title = "${index + 1}. ${topic.title.substringAfter(". ")} · Pokročilé",
        subtitle = "Stejných pět sloves, ale vybíráš správné sloveso ze čtyř různých možností."
    )
}

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

// Sentence-first A1 courses: all answers are practical phrases, not article drills.
internal data class SentencePracticeItem(
    val prompt: String,
    val german: String,
    val czech: String
)

internal data class SentenceCourseDefinition(
    val id: String,
    val title: String,
    val subtitle: String,
    val items: List<SentencePracticeItem>,
    val level: String = "A1"
)

private fun sentenceCourse(
    id: String,
    title: String,
    subtitle: String,
    vararg rows: String
) = SentenceCourseDefinition(
    id = id,
    title = title,
    subtitle = subtitle,
    items = rows.map { row ->
        val fields = row.split("§", limit = 3)
        require(fields.size == 3) { "Invalid sentence-course row: $row" }
        SentencePracticeItem(fields[0], fields[1], fields[2])
    }
)

private val a1SentenceCourseCatalog = listOf(
    sentenceCourse("first_steps", "První kroky", "Pozdravy, seznamování a zdvořilé fráze",
        "Pozdravíš někoho ráno.§Guten Morgen!§Dobré ráno!",
        "Přes den někoho zdvořile pozdravíš.§Guten Tag!§Dobrý den!",
        "Večer někoho pozdravíš.§Guten Abend!§Dobrý večer!",
        "Před spaním popřeješ dobrou noc.§Gute Nacht!§Dobrou noc!",
        "Neformálně pozdravíš kamaráda.§Hallo!§Ahoj!",
        "Rozloučíš se s kamarádem.§Tschüss!§Ahoj při loučení!",
        "Zdvořile se rozloučíš.§Auf Wiedersehen!§Na shledanou!",
        "Poděkuješ za pomoc.§Vielen Dank!§Moc děkuji!",
        "Odpovíš na poděkování.§Gern geschehen!§Rádo se stalo!",
        "Požádáš někoho zdvořile.§Bitte, helfen Sie mir.§Prosím, pomozte mi.",
        "Omluvíš se, když do někoho lehce vrazíš.§Entschuldigung!§Promiňte!",
        "Představíš se novému známému.§Ich heiße Anna.§Jmenuji se Anna.",
        "Zeptáš se na jméno druhého člověka.§Wie heißt du?§Jak se jmenuješ?",
        "Řekneš, odkud pocházíš.§Ich komme aus Tschechien.§Pocházím z Česka.",
        "Zeptáš se na původ druhého člověka.§Woher kommst du?§Odkud pocházíš?",
        "Řekneš, kde bydlíš.§Ich wohne in Brünn.§Bydlím v Brně.",
        "Zeptáš se, kde někdo bydlí.§Wo wohnst du?§Kde bydlíš?",
        "Zeptáš se, jak se druhý má.§Wie geht es dir?§Jak se máš?",
        "Odpovíš, že se máš dobře.§Mir geht es gut, danke.§Mám se dobře, děkuji.",
        "Zeptáš se zdvořile, zda někdo mluví německy.§Sprechen Sie Deutsch?§Mluvíte německy?",
        "Řekneš, že mluvíš jen trochu německy.§Ich spreche nur ein bisschen Deutsch.§Mluvím jen trochu německy.",
        "Požádáš někoho, aby mluvil pomaleji.§Können Sie bitte langsamer sprechen?§Můžete prosím mluvit pomaleji?",
        "Řekneš, že nerozumíš.§Ich verstehe das nicht.§Tomu nerozumím.",
        "Požádáš, aby někdo větu zopakoval.§Können Sie das bitte wiederholen?§Můžete to prosím zopakovat?",
        "Poděkuješ a zdvořile se rozloučíš.§Danke und auf Wiedersehen!§Děkuji a na shledanou!"
    ),
    sentenceCourse("alltag", "Každodenní život", "Ranní rutina, čas a běžné činnosti",
        "Řekneš, že vstáváš v sedm.§Ich stehe um sieben Uhr auf.§Vstávám v sedm hodin.",
        "Vysvětlíš, že ráno piješ kávu.§Am Morgen trinke ich Kaffee.§Ráno piji kávu.",
        "Řekneš, že snídáš doma.§Ich frühstücke zu Hause.§Snídám doma.",
        "Popíšeš cestu do práce.§Ich fahre mit dem Bus zur Arbeit.§Jedu do práce autobusem.",
        "Řekneš, že dnes pracuješ.§Ich arbeite heute.§Dnes pracuji.",
        "Vysvětlíš, že máš hodně práce.§Heute habe ich viel zu tun.§Dnes mám hodně práce.",
        "Řekneš, že máš krátkou přestávku.§Ich mache eine kurze Pause.§Dělám si krátkou přestávku.",
        "Popíšeš oběd v poledne.§Ich esse um zwölf Uhr zu Mittag.§Obědvám ve dvanáct hodin.",
        "Řekneš, že odpoledne nakupuješ.§Am Nachmittag gehe ich einkaufen.§Odpoledne jdu nakupovat.",
        "Vysvětlíš, že přijdeš domů v šest.§Ich komme um sechs Uhr nach Hause.§Přijdu domů v šest hodin.",
        "Řekneš, že večer vaříš večeři.§Am Abend koche ich das Abendessen.§Večer vařím večeři.",
        "Popíšeš, co děláš po práci.§Nach der Arbeit ruhe ich mich aus.§Po práci odpočívám.",
        "Řekneš, že dnes nemáš čas.§Heute habe ich keine Zeit.§Dnes nemám čas.",
        "Zeptáš se, kolik je hodin.§Wie spät ist es?§Kolik je hodin?",
        "Odpovíš, že jsou tři hodiny.§Es ist drei Uhr.§Jsou tři hodiny.",
        "Řekneš, že zítra vstáváš brzy.§Morgen stehe ich früh auf.§Zítra vstávám brzy.",
        "Vysvětlíš, že dnes pracuješ z domova.§Heute arbeite ich von zu Hause.§Dnes pracuji z domova.",
        "Řekneš, že každý den chodíš pěšky.§Ich gehe jeden Tag zu Fuß.§Každý den chodím pěšky.",
        "Popíšeš večerní rutinu.§Vor dem Schlafen lese ich ein Buch.§Před spaním čtu knihu.",
        "Řekneš, že jsi unavený a chceš spát.§Ich bin müde und möchte schlafen.§Jsem unavený a chci spát.",
        "Zeptáš se na plán na dnešek.§Was machst du heute?§Co dnes děláš?",
        "Odpovíš, že dnes uklízíš byt.§Heute räume ich die Wohnung auf.§Dnes uklízím byt.",
        "Řekneš, že o víkendu odpočíváš.§Am Wochenende schlafe ich länger.§O víkendu déle spím.",
        "Vysvětlíš, že teď nemůžeš mluvit.§Ich kann gerade nicht telefonieren.§Teď nemohu telefonovat.",
        "Řekneš, že se ozveš později.§Ich rufe dich später zurück.§Zavolám ti později zpět."
    ),
    sentenceCourse("familie", "Rodina a přátelé", "Představování blízkých a společné plány",
        "Představíš svou rodinu.§Das ist meine Familie.§Tohle je moje rodina.",
        "Představíš svou sestru.§Das ist meine Schwester.§Tohle je moje sestra.",
        "Řekneš, že máš jednoho bratra.§Ich habe einen Bruder.§Mám jednoho bratra.",
        "Zeptáš se, zda má někdo děti.§Hast du Kinder?§Máš děti?",
        "Řekneš, že tvoji rodiče bydlí v Brně.§Meine Eltern wohnen in Brünn.§Moji rodiče bydlí v Brně.",
        "Představíš kamaráda.§Das ist mein guter Freund.§Tohle je můj dobrý kamarád.",
        "Řekneš, že dnes navštívíš babičku.§Heute besuche ich meine Großmutter.§Dnes navštívím svou babičku.",
        "Pozveš kamaráda na kávu.§Möchtest du mit mir einen Kaffee trinken?§Chceš se mnou zajít na kávu?",
        "Přijmeš pozvání.§Ja, sehr gern!§Ano, moc rád!",
        "Zdvořile pozvání odmítneš.§Danke, aber heute habe ich leider keine Zeit.§Děkuji, ale dnes bohužel nemám čas.",
        "Zeptáš se, kdy se uvidíte.§Wann treffen wir uns?§Kdy se setkáme?",
        "Navrhneš setkání v sobotu.§Treffen wir uns am Samstag.§Sejdeme se v sobotu.",
        "Řekneš, že přijdeš o něco později.§Ich komme ein bisschen später.§Přijdu o něco později.",
        "Omluvíš se za zpoždění.§Entschuldige bitte, ich bin zu spät.§Promiň, jdu pozdě.",
        "Zeptáš se na telefonní číslo.§Wie ist deine Telefonnummer?§Jaké je tvoje telefonní číslo?",
        "Řekneš, že rád trávíš čas s přáteli.§Ich verbringe gern Zeit mit Freunden.§Rád trávím čas s přáteli.",
        "Popíšeš společný rodinný oběd.§Am Sonntag essen wir zusammen zu Mittag.§V neděli společně obědváme.",
        "Řekneš, že tvá sestra ráda čte.§Meine Schwester liest sehr gern.§Moje sestra velmi ráda čte.",
        "Zeptáš se, jak se má něčí maminka.§Wie geht es deiner Mutter?§Jak se má tvoje maminka?",
        "Odpovíš, že se má dobře.§Ihr geht es gut, danke.§Má se dobře, děkuji.",
        "Řekneš, že máš dnes návštěvu.§Heute bekomme ich Besuch.§Dnes budu mít návštěvu.",
        "Nabídneš hostovi něco k pití.§Möchtest du etwas trinken?§Chceš něco k pití?",
        "Řekneš, že přátelé už jsou na cestě.§Meine Freunde sind schon unterwegs.§Moji přátelé už jsou na cestě.",
        "Poděkuješ za hezký večer.§Danke für den schönen Abend.§Děkuji za krásný večer.",
        "Rozloučíš se s rodinou.§Bis morgen, passt gut auf euch auf!§Do zítřka, opatrujte se!"
    ),
    sentenceCourse("essen", "Jídlo a pití", "Objednávání, chutě a placení v restauraci",
        "Objednáš si vodu.§Ich hätte gern ein Wasser.§Dal bych si vodu.",
        "Poprosíš o kávu s mlékem.§Einen Kaffee mit Milch, bitte.§Kávu s mlékem, prosím.",
        "Řekneš, že máš hlad.§Ich habe Hunger.§Mám hlad.",
        "Řekneš, že máš žízeň.§Ich habe Durst.§Mám žízeň.",
        "Zeptáš se na jídelní lístek.§Kann ich bitte die Speisekarte haben?§Mohu prosím dostat jídelní lístek?",
        "Objednáš si polévku.§Ich nehme eine Suppe.§Dám si polévku.",
        "Objednáš si vegetariánské jídlo.§Haben Sie ein vegetarisches Gericht?§Máte vegetariánské jídlo?",
        "Vysvětlíš, že nejíš maso.§Ich esse kein Fleisch.§Nejím maso.",
        "Řekneš, že jídlo chutná výborně.§Das Essen schmeckt sehr gut.§Jídlo chutná velmi dobře.",
        "Poprosíš o další sklenici vody.§Kann ich noch ein Glas Wasser bekommen?§Mohu dostat ještě sklenici vody?",
        "Zeptáš se, co číšník doporučuje.§Was empfehlen Sie?§Co doporučujete?",
        "Řekneš, že si dáš salát.§Ich nehme einen Salat.§Dám si salát.",
        "Požádáš o účet.§Die Rechnung, bitte.§Účet, prosím.",
        "Řekneš, že chceš platit kartou.§Ich möchte mit Karte bezahlen.§Chci zaplatit kartou.",
        "Oznámíš, že zaplatíš hotově.§Ich bezahle bar.§Zaplatím hotově.",
        "Zeptáš se, zda je jídlo pikantní.§Ist das Gericht scharf?§Je toto jídlo pálivé?",
        "Požádáš o jídelní příbor.§Könnte ich bitte ein Messer und eine Gabel haben?§Mohl bych prosím dostat nůž a vidličku?",
        "Řekneš, že káva je příliš horká.§Der Kaffee ist noch zu heiß.§Káva je ještě příliš horká.",
        "Objednáš si čaj místo kávy.§Ich möchte lieber einen Tee.§Raději bych čaj.",
        "Zeptáš se, zda je volný stůl.§Haben Sie noch einen freien Tisch?§Máte ještě volný stůl?",
        "Řekneš, že máš rezervaci pro dva.§Ich habe einen Tisch für zwei Personen reserviert.§Mám rezervovaný stůl pro dvě osoby.",
        "Požádáš o jídlo s sebou.§Kann ich das bitte zum Mitnehmen bekommen?§Mohu to prosím dostat s sebou?",
        "Řekneš, že už jsi sytý.§Danke, ich bin schon satt.§Děkuji, už jsem sytý.",
        "Pochválíš dezert.§Der Kuchen ist wirklich lecker.§Dort je opravdu výborný.",
        "Rozloučíš se a poděkuješ za jídlo.§Vielen Dank, es hat sehr gut geschmeckt.§Moc děkuji, velmi mi chutnalo."
    ),
    sentenceCourse("shopping", "Nakupování", "Ceny, velikosti, placení a reklamace",
        "Zeptáš se na cenu trička.§Was kostet dieses T-Shirt?§Kolik stojí toto tričko?",
        "Řekneš, že je to příliš drahé.§Das ist mir zu teuer.§To je na mě příliš drahé.",
        "Zeptáš se, zda mají jinou velikost.§Haben Sie das auch in einer anderen Größe?§Máte to i v jiné velikosti?",
        "Požádáš o větší velikost.§Ich brauche eine größere Größe.§Potřebuji větší velikost.",
        "Požádáš o menší velikost.§Haben Sie das eine Nummer kleiner?§Máte to o číslo menší?",
        "Zeptáš se, zda si můžeš oblečení vyzkoušet.§Kann ich das anprobieren?§Mohu si to vyzkoušet?",
        "Řekneš, že ti bunda dobře sedí.§Die Jacke passt mir gut.§Bunda mi dobře sedí.",
        "Vysvětlíš, že potřebuješ jinou barvu.§Ich suche diese Hose in Schwarz.§Hledám tyto kalhoty v černé barvě.",
        "Řekneš, že si výrobek koupíš.§Ich nehme das, bitte.§Vezmu si to, prosím.",
        "Požádáš o účtenku.§Kann ich bitte den Kassenbon bekommen?§Mohu prosím dostat účtenku?",
        "Zeptáš se, zda lze platit kartou.§Kann ich mit Karte zahlen?§Mohu platit kartou?",
        "Řekneš, že potřebuješ tašku.§Ich brauche bitte eine Tasche.§Potřebuji prosím tašku.",
        "Zeptáš se na slevu.§Gibt es heute einen Rabatt?§Je dnes nějaká sleva?",
        "Oznámíš, že hledáš dárek.§Ich suche ein Geschenk für meine Mutter.§Hledám dárek pro svou maminku.",
        "Požádáš prodavače o pomoc.§Können Sie mir bitte helfen?§Můžete mi prosím pomoci?",
        "Řekneš, že se jen díváš.§Ich sehe mich nur um, danke.§Jen se rozhlížím, děkuji.",
        "Oznámíš, že zboží je poškozené.§Dieses Produkt ist leider beschädigt.§Tento výrobek je bohužel poškozený.",
        "Zeptáš se na možnost výměny.§Kann ich den Artikel umtauschen?§Mohu zboží vyměnit?",
        "Řekneš, že chceš vrátit peníze.§Ich möchte mein Geld zurückbekommen.§Chtěl bych dostat peníze zpět.",
        "Zeptáš se, kde je pokladna.§Wo ist die Kasse, bitte?§Kde je prosím pokladna?",
        "Řekneš, že koupíš ještě chléb.§Ich kaufe noch Brot und Milch.§Koupím ještě chleba a mléko.",
        "Ověříš, jestli můžeš zaplatit hotově.§Kann ich auch bar bezahlen?§Mohu zaplatit také hotově?",
        "Odmítneš další pomoc zdvořile.§Danke, ich komme allein zurecht.§Děkuji, poradím si sám.",
        "Řekneš, že chceš účtenku e-mailem.§Können Sie mir die Rechnung per E-Mail schicken?§Můžete mi poslat účtenku e-mailem?",
        "Poděkuješ prodavači a rozloučíš se.§Vielen Dank für Ihre Hilfe. Auf Wiedersehen!§Moc děkuji za pomoc. Na shledanou!"
    ),
    sentenceCourse("city", "Ve městě a doprava", "Ptaní na cestu, lístky a veřejná doprava",
        "Zeptáš se na cestu k nádraží.§Wie komme ich zum Bahnhof?§Jak se dostanu na nádraží?",
        "Zeptáš se, kde je autobusová zastávka.§Wo ist die Bushaltestelle?§Kde je autobusová zastávka?",
        "Požádáš o lístek do Prahy.§Eine Fahrkarte nach Prag, bitte.§Jízdenku do Prahy, prosím.",
        "Zeptáš se na čas odjezdu.§Wann fährt der nächste Zug?§Kdy jede další vlak?",
        "Řekneš, že hledáš centrum.§Ich suche das Stadtzentrum.§Hledám centrum města.",
        "Požádáš o vysvětlení cesty.§Können Sie mir den Weg zeigen?§Můžete mi ukázat cestu?",
        "Dostaneš pokyn odbočit doleva.§Biegen Sie an der Kreuzung links ab.§Na křižovatce odbočte doleva.",
        "Zeptáš se na cestu doprava.§Muss ich hier rechts abbiegen?§Musím tady odbočit doprava?",
        "Řekneš, že půjdeš rovně.§Ich gehe immer geradeaus.§Půjdu pořád rovně.",
        "Zeptáš se, jak daleko je hotel.§Wie weit ist das Hotel von hier?§Jak daleko je odtud hotel?",
        "Řekneš, že zastávka je blízko.§Die Haltestelle ist ganz in der Nähe.§Zastávka je úplně blízko.",
        "Oznámíš, že autobus má zpoždění.§Der Bus hat zehn Minuten Verspätung.§Autobus má deset minut zpoždění.",
        "Zeptáš se, na které nástupiště máš jít.§Von welchem Gleis fährt der Zug?§Z kterého nástupiště jede vlak?",
        "Požádáš o pomoc s automatem na jízdenky.§Können Sie mir beim Fahrkartenautomaten helfen?§Můžete mi pomoci s automatem na jízdenky?",
        "Řekneš, že vystupuješ na další zastávce.§Ich steige an der nächsten Haltestelle aus.§Vystoupím na další zastávce.",
        "Zeptáš se, zda tento autobus jede do centra.§Fährt dieser Bus ins Zentrum?§Jede tento autobus do centra?",
        "Řekneš, že jsi zabloudil.§Ich habe mich verlaufen.§Zabloudil jsem.",
        "Požádáš o ukázání místa na mapě.§Können Sie es mir auf der Karte zeigen?§Můžete mi to ukázat na mapě?",
        "Zeptáš se, kde je nejbližší lékárna.§Wo ist die nächste Apotheke?§Kde je nejbližší lékárna?",
        "Řekneš, že pojedeš taxíkem.§Ich nehme ein Taxi zum Hotel.§Pojedu taxíkem do hotelu.",
        "Oznámíš, že jízdenka platí celý den.§Die Fahrkarte gilt den ganzen Tag.§Jízdenka platí celý den.",
        "Zeptáš se, zda lze platit bezkontaktně.§Kann ich kontaktlos bezahlen?§Mohu zaplatit bezkontaktně?",
        "Řekneš, že chceš vystoupit tady.§Ich möchte hier aussteigen.§Chci vystoupit tady.",
        "Poděkuješ za vysvětlení cesty.§Vielen Dank für die Wegbeschreibung.§Moc děkuji za popis cesty.",
        "Zeptáš se, jak dlouho cesta trvá.§Wie lange dauert die Fahrt?§Jak dlouho trvá cesta?"
    ),
    sentenceCourse("work_school", "Práce a škola", "Pracovní den, výuka a běžná komunikace",
        "Řekneš, kde pracuješ.§Ich arbeite in einem Büro.§Pracuji v kanceláři.",
        "Zeptáš se kolegy na jeho úkol.§Woran arbeitest du gerade?§Na čem právě pracuješ?",
        "Oznámíš, že máš dnes poradu.§Heute habe ich eine Besprechung.§Dnes mám poradu.",
        "Požádáš kolegu o pomoc.§Kannst du mir bitte helfen?§Můžeš mi prosím pomoci?",
        "Řekneš, že pošleš e-mail.§Ich schicke dir gleich eine E-Mail.§Hned ti pošlu e-mail.",
        "Požádáš o zopakování informace.§Können Sie die Information bitte wiederholen?§Můžete prosím informaci zopakovat?",
        "Řekneš, že rozumíš zadání.§Ich verstehe die Aufgabe.§Rozumím zadání.",
        "Přiznáš, že potřebuješ více času.§Ich brauche noch etwas Zeit.§Potřebuji ještě trochu času.",
        "Zeptáš se na termín odevzdání.§Wann ist die Aufgabe fertig?§Kdy bude úkol hotový?",
        "Řekneš, že schůzka začíná v devět.§Das Meeting beginnt um neun Uhr.§Schůzka začíná v devět hodin.",
        "Oznámíš, že jsi připravil dokumenty.§Ich habe die Dokumente vorbereitet.§Připravil jsem dokumenty.",
        "Požádáš o poslání souboru.§Kannst du mir die Datei schicken?§Můžeš mi poslat soubor?",
        "Řekneš, že se učíš německy.§Ich lerne Deutsch nach der Arbeit.§Učím se německy po práci.",
        "Zeptáš se učitele na význam slova.§Was bedeutet dieses Wort?§Co znamená toto slovo?",
        "Požádáš o pomalejší vysvětlení.§Können Sie das bitte langsamer erklären?§Můžete to prosím vysvětlit pomaleji?",
        "Řekneš, že máš domácí úkol.§Ich muss heute meine Hausaufgaben machen.§Dnes musím udělat domácí úkoly.",
        "Oznámíš, že zkouška je v pátek.§Die Prüfung ist am Freitag.§Zkouška je v pátek.",
        "Zeptáš se, kde je učebna.§In welchem Raum findet der Unterricht statt?§V jaké místnosti probíhá výuka?",
        "Řekneš, že jsi na schůzku připravený.§Ich bin für das Gespräch bereit.§Jsem připravený na rozhovor.",
        "Požádáš kolegu o názor.§Was hältst du von diesem Vorschlag?§Co si myslíš o tomto návrhu?",
        "Řekneš, že návrh vypadá dobře.§Der Vorschlag klingt sehr gut.§Návrh zní velmi dobře.",
        "Oznámíš, že pošleš výsledek zítra.§Ich schicke das Ergebnis morgen.§Výsledek pošlu zítra.",
        "Požádáš o krátkou přestávku.§Können wir eine kurze Pause machen?§Můžeme si udělat krátkou přestávku?",
        "Řekneš, že práci dokončíš dnes.§Ich beende die Arbeit heute.§Dokončím práci dnes.",
        "Poděkuješ za spolupráci.§Vielen Dank für die gute Zusammenarbeit.§Moc děkuji za dobrou spolupráci."
    ),
    sentenceCourse("leisure", "Volný čas a koníčky", "Sport, kultura a plány na víkend",
        "Řekneš, že rád hraješ fotbal.§Ich spiele gern Fußball.§Rád hraji fotbal.",
        "Povíš, že rád posloucháš hudbu.§Ich höre gern Musik.§Rád poslouchám hudbu.",
        "Navrhneš návštěvu kina.§Wollen wir heute ins Kino gehen?§Půjdeme dnes do kina?",
        "Řekneš, že rád čteš knihy.§Ich lese gern Bücher.§Rád čtu knihy.",
        "Zeptáš se na něčí koníček.§Was machst du gern in deiner Freizeit?§Co rád děláš ve volném čase?",
        "Řekneš, že o víkendu běháš.§Am Wochenende gehe ich joggen.§O víkendu chodím běhat.",
        "Oznámíš, že hraješ na kytaru.§Ich spiele Gitarre.§Hraji na kytaru.",
        "Pozveš kamaráda na procházku.§Möchtest du mit mir spazieren gehen?§Chceš se se mnou jít projít?",
        "Řekneš, že dnes raději zůstaneš doma.§Heute bleibe ich lieber zu Hause.§Dnes raději zůstanu doma.",
        "Zeptáš se na oblíbený film.§Welchen Film magst du am liebsten?§Který film máš nejraději?",
        "Povíš, že rád cestuješ.§Ich reise sehr gern.§Velmi rád cestuji.",
        "Oznámíš, že máš vstupenky na koncert.§Ich habe Karten für das Konzert.§Mám lístky na koncert.",
        "Řekneš, že rád vaříš pro přátele.§Ich koche gern für meine Freunde.§Rád vařím pro své přátele.",
        "Zeptáš se, zda někdo rád plave.§Schwimmst du gern?§Rád plaveš?",
        "Řekneš, že dnes nemáš chuť sportovat.§Heute habe ich keine Lust auf Sport.§Dnes nemám chuť sportovat.",
        "Navrhneš výlet do přírody.§Lass uns am Sonntag einen Ausflug machen.§Udělejme si v neděli výlet.",
        "Popíšeš, že rád fotíš.§Ich mache gern Fotos von der Natur.§Rád fotím přírodu.",
        "Řekneš, že o víkendu navštívíš muzeum.§Am Samstag besuche ich ein Museum.§V sobotu navštívím muzeum.",
        "Zeptáš se, kdy začíná představení.§Wann beginnt die Vorstellung?§Kdy začíná představení?",
        "Řekneš, že hudba je příliš hlasitá.§Die Musik ist mir zu laut.§Hudba je na mě příliš hlasitá.",
        "Povíš, že raději jezdíš na kole.§Ich fahre lieber mit dem Fahrrad.§Raději jezdím na kole.",
        "Odmítneš pozvání, protože jsi unavený.§Danke, aber ich bin heute müde.§Děkuji, ale dnes jsem unavený.",
        "Řekneš, že se na výlet těšíš.§Ich freue mich auf den Ausflug.§Těším se na výlet.",
        "Popíšeš plán na neděli.§Am Sonntag möchte ich lange schlafen und danach frühstücken.§V neděli chci dlouho spát a potom snídat.",
        "Rozloučíš se a popřeješ hezký víkend.§Ich wünsche dir ein schönes Wochenende!§Přeji ti hezký víkend!"
    ),
    sentenceCourse("home", "Doma a v bytě", "Pokoje, každodenní potřeby a sousedé",
        "Řekneš, že bydlíš v bytě.§Ich wohne in einer Wohnung.§Bydlím v bytě.",
        "Zeptáš se na klíč.§Wo ist mein Schlüssel?§Kde je můj klíč?",
        "Popíšeš, že pokoj je světlý.§Das Zimmer ist hell.§Pokoj je světlý.",
        "Řekneš, že potřebuješ uklidit kuchyň.§Ich muss die Küche aufräumen.§Musím uklidit kuchyň.",
        "Požádáš někoho, aby otevřel okno.§Kannst du bitte das Fenster öffnen?§Můžeš prosím otevřít okno?",
        "Řekneš, že je doma zima.§Mir ist zu Hause kalt.§Je mi doma zima.",
        "Oznámíš, že pračka právě pere.§Die Waschmaschine läuft gerade.§Pračka právě běží.",
        "Řekneš, že koupelna je volná.§Das Badezimmer ist frei.§Koupelna je volná.",
        "Požádáš souseda o pomoc.§Können Sie mir kurz helfen, bitte?§Můžete mi na chvíli pomoci?",
        "Řekneš, že dnes vysáváš.§Heute sauge ich die Wohnung.§Dnes vysávám byt.",
        "Povíš, že potřebuješ novou lampu.§Ich brauche eine neue Lampe.§Potřebuji novou lampu.",
        "Zeptáš se, kde je kuchyně.§Wo ist die Küche?§Kde je kuchyň?",
        "Nabídneš návštěvě místo k sezení.§Setz dich bitte hierhin.§Posaď se prosím sem.",
        "Řekneš, že připravíš čaj.§Ich mache uns einen Tee.§Udělám nám čaj.",
        "Vysvětlíš, že dnes vaříš večeři.§Heute koche ich das Abendessen.§Dnes vařím večeři.",
        "Řekneš, že lednice je téměř prázdná.§Der Kühlschrank ist fast leer.§Lednice je skoro prázdná.",
        "Požádáš, aby někdo zavřel dveře.§Kannst du bitte die Tür schließen?§Můžeš prosím zavřít dveře?",
        "Řekneš, že v obývacím pokoji je pohodlně.§Im Wohnzimmer ist es gemütlich.§V obývacím pokoji je útulno.",
        "Oznámíš, že nájem platíš na začátku měsíce.§Ich bezahle die Miete am Monatsanfang.§Nájem platím na začátku měsíce.",
        "Řekneš, že byt má balkon.§Die Wohnung hat einen Balkon.§Byt má balkon.",
        "Zeptáš se, zda můžeš použít koupelnu.§Darf ich das Bad benutzen?§Mohu použít koupelnu?",
        "Vysvětlíš, že hledáš poštovní schránku.§Ich suche den Briefkasten.§Hledám poštovní schránku.",
        "Řekneš, že balík už dorazil.§Das Paket ist schon angekommen.§Balík už dorazil.",
        "Poprosíš sousedy o trochu ticha.§Könnten Sie bitte etwas leiser sein?§Mohli byste být prosím trochu tišší?",
        "Popřeješ sousedům hezký den.§Ich wünsche Ihnen einen schönen Tag.§Přeji vám hezký den."
    ),
    sentenceCourse("travel", "Cestování a hotel", "Ubytování, rezervace a cestovní situace",
        "Řekneš na recepci, že máš rezervaci.§Ich habe eine Reservierung.§Mám rezervaci.",
        "Oznámíš, že je rezervace na dvě noci.§Die Reservierung ist für zwei Nächte.§Rezervace je na dvě noci.",
        "Zeptáš se, od kolika je snídaně.§Ab wann gibt es Frühstück?§Od kolika se podává snídaně?",
        "Požádáš o pokoj s výhledem.§Ich hätte gern ein Zimmer mit Aussicht.§Chtěl bych pokoj s výhledem.",
        "Zeptáš se na heslo k Wi-Fi.§Wie lautet das WLAN-Passwort?§Jaké je heslo k Wi-Fi?",
        "Řekneš, že chceš zaplatit při odjezdu.§Ich möchte beim Auschecken bezahlen.§Chci zaplatit při odjezdu.",
        "Zeptáš se na čas odjezdu z hotelu.§Bis wann muss ich auschecken?§Do kdy se musím odhlásit z hotelu?",
        "Oznámíš, že máš dva kufry.§Ich habe zwei Koffer dabei.§Mám s sebou dva kufry.",
        "Požádáš o pomoc se zavazadly.§Können Sie mir mit meinem Gepäck helfen?§Můžete mi pomoci se zavazadly?",
        "Řekneš, že vlak má zpoždění.§Mein Zug hat Verspätung.§Můj vlak má zpoždění.",
        "Zeptáš se, odkud jede vlak na letiště.§Von welchem Gleis fährt der Zug zum Flughafen?§Z kterého nástupiště jede vlak na letiště?",
        "Oznámíš, že chceš taxi na letiště.§Ich brauche ein Taxi zum Flughafen.§Potřebuji taxi na letiště.",
        "Zeptáš se, kde je vchod.§Wo ist der Eingang?§Kde je vchod?",
        "Řekneš, že potřebuješ mapu města.§Ich brauche einen Stadtplan.§Potřebuji plán města.",
        "Požádáš o doporučení památek.§Welche Sehenswürdigkeiten empfehlen Sie?§Které památky doporučujete?",
        "Řekneš, že zůstaneš ještě jednu noc.§Ich bleibe noch eine Nacht.§Zůstanu ještě jednu noc.",
        "Oznámíš, že pokoj nebyl uklizený.§Das Zimmer wurde leider nicht gereinigt.§Pokoj bohužel nebyl uklizený.",
        "Požádáš o další ručník.§Könnte ich bitte ein zusätzliches Handtuch bekommen?§Mohl bych dostat další ručník?",
        "Požádáš o další polštář na pokoji.§Könnte ich bitte ein zusätzliches Kissen bekommen?§Mohl bych prosím dostat další polštář.",
        "Zeptáš se, zda je poblíž restaurace.§Gibt es hier in der Nähe ein Restaurant?§Je tady poblíž restaurace?",
        "Řekneš, že chceš zavolat na recepci.§Ich möchte die Rezeption anrufen.§Chci zavolat na recepci.",
        "Oznámíš, že pas máš v batohu.§Mein Reisepass ist in meinem Rucksack.§Můj cestovní pas je v batohu.",
        "Zeptáš se na cenu za noc.§Wie viel kostet eine Nacht?§Kolik stojí jedna noc?",
        "Řekneš, že pokoj je příliš hlučný.§Das Zimmer ist leider zu laut.§Pokoj je bohužel příliš hlučný.",
        "Poděkuješ za pobyt a rozloučíš se.§Vielen Dank für den angenehmen Aufenthalt.§Moc děkuji za příjemný pobyt."
    )
)

internal val sentenceCourseCatalog = a1SentenceCourseCatalog + expandedSentenceCourseCatalog

internal fun sentenceCourseQuestions(courseId: String): List<LessonQuestion> {
    val course = sentenceCourseCatalog.firstOrNull { it.id == courseId } ?: return emptyList()
    val items = course.items
    return items.mapIndexed { index, item ->
        val options = listOf(
            item,
            items[(index + 5) % items.size],
            items[(index + 11) % items.size],
            items[(index + 17) % items.size]
        ).distinctBy { it.german }.shuffled()
        LessonQuestion(
            prompt = item.prompt,
            choices = options.map { it.german },
            correctIndex = options.indexOfFirst { it.german == item.german },
            explanation = "Správná věta znamená: " + item.czech,
            germanSentence = item.german,
            translation = item.czech
        )
    }
}
