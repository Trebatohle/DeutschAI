package com.deutschai.app

/**
 * A1 additions based on the Goethe-Zertifikat A1: Start Deutsch 1 Wortliste.
 * Czech meanings and short example sentences are original DeutschAI content.
 * Existing words are filtered across every CEFR level before anything is added.
 */
internal const val FREQUENCY_VOCABULARY_A1_ADDITION_COUNT = 200

private data class A1VocabularySeed(
    val kind: FrequencyWordKind,
    val word: String,
    val czech: String,
    val article: String? = null,
    val extraInfo: String = "",
    val exampleGerman: String,
    val exampleCzech: String
)

private val a1NounSeeds = """
der§Absender§odesílatel
die§Abfahrt§odjezd
die§Adresse§adresa
das§Angebot§nabídka
der§Anfang§začátek
die§Ankunft§příjezd
die§Ansage§hlášení
der§Anschluss§přípoj / spojení
der§Anruf§telefonát
der§Anrufbeantworter§telefonní záznamník
die§Antwort§odpověď
die§Anzeige§inzerát / oznámení
das§Apartment§apartmán
der§Arbeitsplatz§pracoviště
die§Aufgabe§úkol
der§Aufzug§výtah
der§Ausflug§výlet
der§Ausgang§východ
die§Auskunft§informace
das§Ausland§zahraničí
der§Ausländer§cizinec
der§Ausweis§průkaz
die§Autobahn§dálnice
der§Automat§automat
das§Baby§miminko
die§Bäckerei§pekárna
das§Bad§koupelna / vana
die§Bahn§dráha / vlak
der§Bahnsteig§nástupiště
der§Balkon§balkon
die§Banane§banán
die§Bank§banka / lavička
der§Bauch§břicho
der§Baum§strom
der§Beamte§úředník
das§Bein§noha
das§Beispiel§příklad
der§Beruf§povolání
die§Birne§hruška
der§Bleistift§tužka
der§Blick§pohled
die§Blume§květina
der§Bogen§oblouk / list papíru
der§Brief§dopis
die§Briefmarke§poštovní známka
das§Brötchen§houska
der§Buchstabe§písmeno
die§Butter§máslo
das§Café§kavárna
die§CD§CD
der§Chef§šéf
der§Computer§počítač
die§Dame§dáma / paní
das§Datum§datum
die§Disco§diskotéka
der§Doktor§doktor
das§Doppelzimmer§dvoulůžkový pokoj
das§Dorf§vesnice
der§Drucker§tiskárna
die§Durchsage§hlášení
die§Dusche§sprcha
die§Ecke§roh
die§Ehefrau§manželka
der§Ehemann§manžel
das§Ei§vejce
der§Eingang§vchod
die§Einladung§pozvání
der§Eintritt§vstup
das§Einzelzimmer§jednolůžkový pokoj
die§Eltern§rodiče§P
die§E-Mail§e-mail
der§Empfänger§příjemce
das§Ende§konec
die§Entschuldigung§omluva
das§Ergebnis§výsledek
der§Erwachsene§dospělý
der§Fahrer§řidič
die§Fahrkarte§jízdenka
das§Fahrrad§jízdní kolo
der§Familienname§příjmení
der§Familienstand§rodinný stav
die§Farbe§barva
das§Fax§fax
der§Feiertag§státní svátek
der§Fehler§chyba
das§Feuer§oheň
das§Fieber§horečka
der§Film§film
die§Firma§firma
der§Fisch§ryba
die§Flasche§láhev
das§Fleisch§maso
der§Flughafen§letiště
das§Flugzeug§letadlo
das§Formular§formulář
das§Foto§fotografie
die§Frage§otázka
die§Freizeit§volný čas
die§Führung§prohlídka / vedení
der§Fuß§chodidlo / noha
der§Fußball§fotbalový míč / fotbal
der§Garten§zahrada
der§Gast§host
das§Geburtsjahr§rok narození
der§Geburtsort§místo narození
das§Gemüse§zelenina
das§Gepäck§zavazadla
das§Geschäft§obchod
das§Geschenk§dárek
die§Geschwister§sourozenci§P
das§Gespräch§rozhovor
das§Getränk§nápoj
das§Gewicht§váha
das§Glas§sklenice
das§Gleis§kolej
der§Glückwunsch§blahopřání
die§Größe§velikost
die§Großeltern§prarodiče§P
die§Großmutter§babička
der§Großvater§dědeček
die§Gruppe§skupina
der§Gruß§pozdrav
das§Haar§vlas
das§Hähnchen§kuře
die§Halbpension§polopenze
die§Halle§hala
die§Haltestelle§zastávka
die§Hand§ruka
das§Handy§mobilní telefon
die§Hausaufgabe§domácí úkol
die§Hausfrau§žena v domácnosti
die§Heimat§domov / vlast
der§Herd§sporák
der§Herr§pán
das§Hobby§koníček
die§Hochzeit§svatba
der§Hund§pes
die§Information§informace
das§Internet§internet
die§Jacke§bunda
der§Job§zaměstnání / brigáda
der§Jugendliche§mladistvý
der§Junge§chlapec
die§Karte§karta / lístek / mapa
die§Kartoffel§brambora
die§Kasse§pokladna
der§Kindergarten§mateřská škola
das§Kino§kino
der§Kiosk§kiosek
die§Klasse§třída
die§Kleidung§oblečení
der§Koffer§kufr
der§Kollege§kolega
das§Konto§účet
der§Kopf§hlava
der§Kuchen§koláč
der§Kugelschreiber§kuličkové pero
der§Kühlschrank§lednice
der§Kunde§zákazník
der§Kurs§kurz
der§Laden§obchod
das§Land§země
das§Leben§život
die§Lebensmittel§potraviny§P
der§Lehrer§učitel
die§Leute§lidé§P
das§Licht§světlo
das§Lied§píseň
der§Lkw§nákladní auto
das§Lokal§podnik / restaurace
die§Lösung§řešení
das§Mädchen§dívka
die§Maschine§stroj
das§Meer§moře
der§Mensch§člověk
die§Miete§nájem
die§Milch§mléko
die§Möbel§nábytek§P
der§Moment§okamžik
der§Mund§ústa
die§Nummer§číslo
das§Obst§ovoce
das§Öl§olej
die§Oma§babička
der§Opa§dědeček
die§Ordnung§pořádek
der§Ort§místo
das§Papier§papír
die§Papiere§doklady§P
der§Partner§partner
die§Party§večírek
der§Pass§pas
die§Pause§přestávka
der§Plan§plán
der§Platz§místo / náměstí
die§Polizei§policie
die§Pommes frites§hranolky§P
die§Post§pošta
die§Postleitzahl§poštovní směrovací číslo
das§Praktikum§odborná praxe
die§Praxis§ordinace / praxe
der§Preis§cena
der§Prospekt§prospekt
die§Prüfung§zkouška
der§Raum§místnost
die§Rechnung§účet / faktura
der§Regen§déšť
der§Reis§rýže
die§Reise§cesta
das§Reisebüro§cestovní kancelář
der§Reiseführer§průvodce
die§Reparatur§oprava
das§Restaurant§restaurace
die§Rezeption§recepce
der§Saft§džus
der§Salat§salát
das§Salz§sůl
der§Satz§věta
die§S-Bahn§městská / příměstská železnice
der§Schalter§přepážka
das§Schild§cedule
der§Schinken§šunka
der§Schluss§konec / závěr
der§Schlüssel§klíč
der§Schrank§skříň
der§Schuh§bota
der§Schüler§žák
das§Schwimmbad§plavecký bazén
der§See§jezero
die§Sehenswürdigkeit§pamětihodnost
das§Sofa§pohovka
der§Sohn§syn
die§Sonne§slunce
die§Speisekarte§jídelní lístek
der§Sport§sport
die§Sprache§jazyk
die§Stelle§místo / pracovní pozice
der§Stock§patro
die§Straßenbahn§tramvaj
das§Studium§studium
der§Student§student
die§Stunde§hodina
die§Tasche§taška
das§Taxi§taxi
der§Tee§čaj
der§Teil§část
das§Telefon§telefon
der§Termin§termín / schůzka
der§Test§test
der§Text§text
das§Thema§téma
das§Ticket§jízdenka / vstupenka
der§Tisch§stůl
die§Tochter§dcera
die§Toilette§toaleta
die§Tomate§rajče
die§Treppe§schody
die§Uhr§hodiny / hodinky
der§Unterricht§výuka
die§Unterschrift§podpis
der§Urlaub§dovolená
der§Vater§otec
der§Verein§spolek / klub
der§Verkäufer§prodavač
der§Vermieter§pronajímatel
der§Verwandte§příbuzný
das§Wetter§počasí
die§Welt§svět
der§Wind§vítr
die§Zeitung§noviny
die§Zigarette§cigareta
der§Zoll§clo
""".trimIndent().lineSequence().map { row ->
    val fields = row.split("§")
    val article = fields[0]
    val word = fields[1]
    val czech = fields[2]
    val pluralOnly = fields.getOrNull(3) == "P"
    val german = if (pluralOnly) "Die $word sind heute hier." else "Hier ist $article $word."
    val translation = if (pluralOnly) "$czech jsou dnes tady." else "Tady je $czech."
    A1VocabularySeed(
        kind = FrequencyWordKind.NOUN,
        word = word,
        czech = czech,
        article = article,
        extraInfo = if (pluralOnly) "Pouze množné číslo" else "",
        exampleGerman = german,
        exampleCzech = translation
    )
}.toList()

private val a1VerbSeeds = listOf(
    A1VocabularySeed(FrequencyWordKind.VERB, "abfahren", "odjíždět", exampleGerman = "Wir müssen um acht Uhr abfahren.", exampleCzech = "Musíme odjet v osm hodin."),
    A1VocabularySeed(FrequencyWordKind.VERB, "abgeben", "odevzdat", exampleGerman = "Ich muss den Schlüssel abgeben.", exampleCzech = "Musím odevzdat klíč."),
    A1VocabularySeed(FrequencyWordKind.VERB, "abholen", "vyzvednout", exampleGerman = "Ich möchte dich vom Bahnhof abholen.", exampleCzech = "Chci tě vyzvednout na nádraží."),
    A1VocabularySeed(FrequencyWordKind.VERB, "anfangen", "začít", exampleGerman = "Wir müssen jetzt anfangen.", exampleCzech = "Musíme teď začít."),
    A1VocabularySeed(FrequencyWordKind.VERB, "ankommen", "přijet / dorazit", exampleGerman = "Wir möchten pünktlich ankommen.", exampleCzech = "Chceme dorazit včas."),
    A1VocabularySeed(FrequencyWordKind.VERB, "ankreuzen", "zaškrtnout", exampleGerman = "Bitte das richtige Kästchen ankreuzen.", exampleCzech = "Zaškrtněte prosím správné políčko."),
    A1VocabularySeed(FrequencyWordKind.VERB, "anmachen", "zapnout / rozsvítit", exampleGerman = "Kannst du bitte das Licht anmachen?", exampleCzech = "Můžeš prosím rozsvítit?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "anmelden", "přihlásit", exampleGerman = "Ich möchte mich für den Kurs anmelden.", exampleCzech = "Chci se přihlásit do kurzu."),
    A1VocabularySeed(FrequencyWordKind.VERB, "anziehen", "obléknout si", exampleGerman = "Ich möchte eine warme Jacke anziehen.", exampleCzech = "Chci si obléknout teplou bundu."),
    A1VocabularySeed(FrequencyWordKind.VERB, "aufhören", "přestat / skončit", exampleGerman = "Wir müssen jetzt aufhören.", exampleCzech = "Musíme teď skončit."),
    A1VocabularySeed(FrequencyWordKind.VERB, "aufstehen", "vstát", exampleGerman = "Ich möchte morgen früh aufstehen.", exampleCzech = "Zítra chci brzy vstát."),
    A1VocabularySeed(FrequencyWordKind.VERB, "ausfüllen", "vyplnit", exampleGerman = "Ich muss das Formular ausfüllen.", exampleCzech = "Musím vyplnit formulář."),
    A1VocabularySeed(FrequencyWordKind.VERB, "ausmachen", "vypnout", exampleGerman = "Bitte das Licht ausmachen.", exampleCzech = "Prosím zhasnout světlo."),
    A1VocabularySeed(FrequencyWordKind.VERB, "aussehen", "vypadat", exampleGerman = "Du kannst heute gut aussehen.", exampleCzech = "Dnes můžeš vypadat dobře."),
    A1VocabularySeed(FrequencyWordKind.VERB, "aussteigen", "vystoupit", exampleGerman = "Wir müssen an der nächsten Haltestelle aussteigen.", exampleCzech = "Musíme vystoupit na příští zastávce."),
    A1VocabularySeed(FrequencyWordKind.VERB, "ausziehen", "svléknout si", exampleGerman = "Ich muss meine Schuhe ausziehen.", exampleCzech = "Musím si zout boty."),
    A1VocabularySeed(FrequencyWordKind.VERB, "baden", "koupat se", exampleGerman = "Im Sommer möchte ich im See baden.", exampleCzech = "V létě se chci koupat v jezeře."),
    A1VocabularySeed(FrequencyWordKind.VERB, "bedeuten", "znamenat", exampleGerman = "Was kann dieses Wort bedeuten?", exampleCzech = "Co může toto slovo znamenat?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "beginnen", "začínat", exampleGerman = "Der Film wird gleich beginnen.", exampleCzech = "Film za chvíli začne."),
    A1VocabularySeed(FrequencyWordKind.VERB, "benutzen", "používat", exampleGerman = "Darf ich dein Telefon benutzen?", exampleCzech = "Smím použít tvůj telefon?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "besichtigen", "prohlédnout si", exampleGerman = "Wir möchten heute das Schloss besichtigen.", exampleCzech = "Dnes si chceme prohlédnout zámek."),
    A1VocabularySeed(FrequencyWordKind.VERB, "bestellen", "objednat", exampleGerman = "Ich möchte eine Suppe bestellen.", exampleCzech = "Chci si objednat polévku."),
    A1VocabularySeed(FrequencyWordKind.VERB, "besuchen", "navštívit", exampleGerman = "Am Sonntag möchte ich meine Oma besuchen.", exampleCzech = "V neděli chci navštívit babičku."),
    A1VocabularySeed(FrequencyWordKind.VERB, "bezahlen", "zaplatit", exampleGerman = "Kann ich bitte mit Karte bezahlen?", exampleCzech = "Mohu prosím zaplatit kartou?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "bitten", "požádat / prosit", exampleGerman = "Ich möchte dich um Hilfe bitten.", exampleCzech = "Chtěl bych tě požádat o pomoc."),
    A1VocabularySeed(FrequencyWordKind.VERB, "buchstabieren", "hláskovat", exampleGerman = "Können Sie bitte Ihren Namen buchstabieren?", exampleCzech = "Můžete prosím vyhláskovat své jméno?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "danken", "poděkovat", exampleGerman = "Ich möchte dir für deine Hilfe danken.", exampleCzech = "Chci ti poděkovat za pomoc."),
    A1VocabularySeed(FrequencyWordKind.VERB, "dauern", "trvat", exampleGerman = "Der Film wird nicht lange dauern.", exampleCzech = "Film nebude trvat dlouho."),
    A1VocabularySeed(FrequencyWordKind.VERB, "drucken", "tisknout", exampleGerman = "Ich muss das Formular drucken.", exampleCzech = "Musím vytisknout formulář."),
    A1VocabularySeed(FrequencyWordKind.VERB, "drücken", "stisknout", exampleGerman = "Bitte hier auf den Knopf drücken.", exampleCzech = "Stiskněte prosím zde tlačítko."),
    A1VocabularySeed(FrequencyWordKind.VERB, "duschen", "sprchovat se", exampleGerman = "Ich möchte jetzt duschen.", exampleCzech = "Teď se chci osprchovat."),
    A1VocabularySeed(FrequencyWordKind.VERB, "einkaufen", "nakupovat", exampleGerman = "Wir müssen heute noch einkaufen.", exampleCzech = "Dnes ještě musíme nakoupit."),
    A1VocabularySeed(FrequencyWordKind.VERB, "einladen", "pozvat", exampleGerman = "Ich möchte dich zum Essen einladen.", exampleCzech = "Chci tě pozvat na jídlo."),
    A1VocabularySeed(FrequencyWordKind.VERB, "einsteigen", "nastoupit", exampleGerman = "Bitte jetzt in den Bus einsteigen.", exampleCzech = "Prosím, nastupte teď do autobusu."),
    A1VocabularySeed(FrequencyWordKind.VERB, "empfehlen", "doporučit", exampleGerman = "Kannst du mir ein gutes Buch empfehlen?", exampleCzech = "Můžeš mi doporučit dobrou knihu?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "enden", "končit", exampleGerman = "Der Kurs wird um zwölf Uhr enden.", exampleCzech = "Kurz skončí ve dvanáct hodin."),
    A1VocabularySeed(FrequencyWordKind.VERB, "entschuldigen", "omluvit", exampleGerman = "Ich möchte mich für den Fehler entschuldigen.", exampleCzech = "Chci se omluvit za chybu."),
    A1VocabularySeed(FrequencyWordKind.VERB, "erlauben", "dovolit", exampleGerman = "Meine Eltern erlauben mir die Reise.", exampleCzech = "Rodiče mi dovolí cestu."),
    A1VocabularySeed(FrequencyWordKind.VERB, "erklären", "vysvětlit", exampleGerman = "Kannst du mir das bitte erklären?", exampleCzech = "Můžeš mi to prosím vysvětlit?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "erzählen", "vyprávět", exampleGerman = "Ich möchte dir etwas erzählen.", exampleCzech = "Chci ti něco vyprávět."),
    A1VocabularySeed(FrequencyWordKind.VERB, "fehlen", "chybět", exampleGerman = "Du wirst mir sehr fehlen.", exampleCzech = "Budeš mi moc chybět."),
    A1VocabularySeed(FrequencyWordKind.VERB, "fernsehen", "dívat se na televizi", exampleGerman = "Wir möchten heute Abend fernsehen.", exampleCzech = "Dnes večer se chceme dívat na televizi."),
    A1VocabularySeed(FrequencyWordKind.VERB, "führen", "vést", exampleGerman = "Der Lehrer möchte die Gruppe führen.", exampleCzech = "Učitel chce vést skupinu."),
    A1VocabularySeed(FrequencyWordKind.VERB, "gefallen", "líbit se", exampleGerman = "Das neue Buch wird dir gefallen.", exampleCzech = "Nová kniha se ti bude líbit."),
    A1VocabularySeed(FrequencyWordKind.VERB, "gewinnen", "vyhrát", exampleGerman = "Wir möchten das Spiel gewinnen.", exampleCzech = "Chceme vyhrát zápas."),
    A1VocabularySeed(FrequencyWordKind.VERB, "glauben", "věřit", exampleGerman = "Ich kann dir glauben.", exampleCzech = "Mohu ti věřit."),
    A1VocabularySeed(FrequencyWordKind.VERB, "gratulieren", "blahopřát", exampleGerman = "Wir möchten dir zum Geburtstag gratulieren.", exampleCzech = "Chceme ti popřát k narozeninám."),
    A1VocabularySeed(FrequencyWordKind.VERB, "grillen", "grilovat", exampleGerman = "Am Samstag möchten wir im Garten grillen.", exampleCzech = "V sobotu chceme grilovat na zahradě."),
    A1VocabularySeed(FrequencyWordKind.VERB, "halten", "zastavit / držet", exampleGerman = "Der Bus wird hier kurz halten.", exampleCzech = "Autobus zde krátce zastaví."),
    A1VocabularySeed(FrequencyWordKind.VERB, "heiraten", "oženit se / vdát se", exampleGerman = "Sie möchten nächstes Jahr heiraten.", exampleCzech = "Příští rok se chtějí vzít."),
    A1VocabularySeed(FrequencyWordKind.VERB, "holen", "přinést / dojít pro", exampleGerman = "Ich muss noch Wasser holen.", exampleCzech = "Ještě musím dojít pro vodu."),
    A1VocabularySeed(FrequencyWordKind.VERB, "kennenlernen", "poznat / seznámit se", exampleGerman = "Ich möchte neue Leute kennenlernen.", exampleCzech = "Chci poznat nové lidi."),
    A1VocabularySeed(FrequencyWordKind.VERB, "kosten", "stát (o ceně)", exampleGerman = "Wie viel wird das Ticket kosten?", exampleCzech = "Kolik bude stát ta jízdenka?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "kriegen", "dostat", exampleGerman = "Kann ich bitte eine Quittung kriegen?", exampleCzech = "Mohu prosím dostat účtenku?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "lachen", "smát se", exampleGerman = "Wir möchten zusammen lachen.", exampleCzech = "Chceme se společně smát."),
    A1VocabularySeed(FrequencyWordKind.VERB, "mieten", "pronajmout si", exampleGerman = "Wir möchten eine kleine Wohnung mieten.", exampleCzech = "Chceme si pronajmout malý byt."),
    A1VocabularySeed(FrequencyWordKind.VERB, "mitbringen", "přinést s sebou", exampleGerman = "Kannst du bitte Brot mitbringen?", exampleCzech = "Můžeš prosím přinést chleba?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "mitkommen", "jít / jet s někým", exampleGerman = "Möchtest du heute mitkommen?", exampleCzech = "Chceš jít dnes s námi?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "mitmachen", "zúčastnit se", exampleGerman = "Alle möchten mitmachen.", exampleCzech = "Všichni se chtějí zúčastnit."),
    A1VocabularySeed(FrequencyWordKind.VERB, "mitnehmen", "vzít s sebou", exampleGerman = "Darf ich meinen Hund mitnehmen?", exampleCzech = "Smím si vzít s sebou psa?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "öffnen", "otevřít", exampleGerman = "Kannst du bitte das Fenster öffnen?", exampleCzech = "Můžeš prosím otevřít okno?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "rauchen", "kouřit", exampleGerman = "Hier darf man nicht rauchen.", exampleCzech = "Tady se nesmí kouřit."),
    A1VocabularySeed(FrequencyWordKind.VERB, "reisen", "cestovat", exampleGerman = "Wir möchten im Sommer nach Österreich reisen.", exampleCzech = "V létě chceme cestovat do Rakouska."),
    A1VocabularySeed(FrequencyWordKind.VERB, "reparieren", "opravit", exampleGerman = "Kannst du mein Fahrrad reparieren?", exampleCzech = "Můžeš mi opravit jízdní kolo?"),
    A1VocabularySeed(FrequencyWordKind.VERB, "riechen", "vonět / cítit", exampleGerman = "Die Blumen können sehr gut riechen.", exampleCzech = "Květiny mohou krásně vonět."),
    A1VocabularySeed(FrequencyWordKind.VERB, "schmecken", "chutnat", exampleGerman = "Die Suppe wird gut schmecken.", exampleCzech = "Polévka bude chutnat dobře."),
    A1VocabularySeed(FrequencyWordKind.VERB, "schicken", "poslat", exampleGerman = "Ich möchte dir eine E-Mail schicken.", exampleCzech = "Chci ti poslat e-mail."),
    A1VocabularySeed(FrequencyWordKind.VERB, "schließen", "zavřít", exampleGerman = "Bitte die Tür schließen.", exampleCzech = "Prosím zavřít dveře."),
    A1VocabularySeed(FrequencyWordKind.VERB, "übernachten", "přespat / nocovat", exampleGerman = "Wir möchten in einem Hotel übernachten.", exampleCzech = "Chceme přespat v hotelu."),
    A1VocabularySeed(FrequencyWordKind.VERB, "überweisen", "převést peníze", exampleGerman = "Ich möchte die Miete überweisen.", exampleCzech = "Chci převést nájem."),
    A1VocabularySeed(FrequencyWordKind.VERB, "umziehen", "přestěhovat se", exampleGerman = "Wir möchten nächstes Jahr umziehen.", exampleCzech = "Příští rok se chceme přestěhovat."),
    A1VocabularySeed(FrequencyWordKind.VERB, "unterschreiben", "podepsat", exampleGerman = "Sie müssen hier unterschreiben.", exampleCzech = "Tady se musíte podepsat."),
    A1VocabularySeed(FrequencyWordKind.VERB, "verkaufen", "prodat", exampleGerman = "Wir möchten unser altes Auto verkaufen.", exampleCzech = "Chceme prodat naše staré auto."),
    A1VocabularySeed(FrequencyWordKind.VERB, "verdienen", "vydělávat", exampleGerman = "Er möchte mehr Geld verdienen.", exampleCzech = "Chce vydělávat více peněz."),
    A1VocabularySeed(FrequencyWordKind.VERB, "vermieten", "pronajímat", exampleGerman = "Sie möchten das Zimmer vermieten.", exampleCzech = "Chtějí pronajímat pokoj."),
    A1VocabularySeed(FrequencyWordKind.VERB, "verstehen", "rozumět", exampleGerman = "Ich möchte dich besser verstehen.", exampleCzech = "Chci ti lépe rozumět."),
    A1VocabularySeed(FrequencyWordKind.VERB, "vorstellen", "představit", exampleGerman = "Ich möchte mich kurz vorstellen.", exampleCzech = "Chci se krátce představit."),
    A1VocabularySeed(FrequencyWordKind.VERB, "wandern", "chodit na túry", exampleGerman = "Wir möchten am Wochenende wandern.", exampleCzech = "O víkendu chceme jít na túru."),
    A1VocabularySeed(FrequencyWordKind.VERB, "waschen", "mýt / prát", exampleGerman = "Ich muss meine Kleidung waschen.", exampleCzech = "Musím vyprat oblečení."),
    A1VocabularySeed(FrequencyWordKind.VERB, "wiederholen", "zopakovat", exampleGerman = "Kannst du das bitte wiederholen?", exampleCzech = "Můžeš to prosím zopakovat?")
)

private val a1AdjectiveSeeds = listOf(
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "bitter", "hořký", exampleGerman = "Der Kaffee ist bitter.", exampleCzech = "Káva je hořká."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "böse", "zlý / rozzlobený", exampleGerman = "Der Mann ist nicht böse.", exampleCzech = "Ten muž není rozzlobený."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "breit", "široký", exampleGerman = "Die Straße ist sehr breit.", exampleCzech = "Ulice je velmi široká."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "eilig", "spěšný / ve spěchu", exampleGerman = "Ich habe es heute eilig.", exampleCzech = "Dnes spěchám."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "fertig", "hotový", exampleGerman = "Das Essen ist fertig.", exampleCzech = "Jídlo je hotové."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "frei", "volný", exampleGerman = "Der Platz ist noch frei.", exampleCzech = "Místo je ještě volné."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "fremd", "cizí", exampleGerman = "Ich bin neu und alles ist mir fremd.", exampleCzech = "Jsem tu nový a všechno je mi cizí."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "gültig", "platný", exampleGerman = "Mein Pass ist noch gültig.", exampleCzech = "Můj pas je ještě platný."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "glücklich", "šťastný", exampleGerman = "Die Kinder sind glücklich.", exampleCzech = "Děti jsou šťastné."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "hell", "světlý", exampleGerman = "Das Zimmer ist hell.", exampleCzech = "Pokoj je světlý."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "herzlich", "srdečný", exampleGerman = "Herzlich willkommen!", exampleCzech = "Srdečně vítejte!"),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "hoch", "vysoký", exampleGerman = "Der Turm ist sehr hoch.", exampleCzech = "Věž je velmi vysoká."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "jung", "mladý", exampleGerman = "Mein Bruder ist noch jung.", exampleCzech = "Můj bratr je ještě mladý."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "kaputt", "rozbitý", exampleGerman = "Mein Handy ist kaputt.", exampleCzech = "Můj mobil je rozbitý."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "klar", "jasný", exampleGerman = "Alles ist jetzt klar.", exampleCzech = "Teď je všechno jasné."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "krank", "nemocný", exampleGerman = "Mein Sohn ist heute krank.", exampleCzech = "Můj syn je dnes nemocný."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "kurz", "krátký", exampleGerman = "Der Film ist sehr kurz.", exampleCzech = "Film je velmi krátký."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "lang", "dlouhý", exampleGerman = "Der Weg ist nicht lang.", exampleCzech = "Cesta není dlouhá."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "laut", "hlasitý", exampleGerman = "Die Musik ist zu laut.", exampleCzech = "Hudba je příliš hlasitá."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "ledig", "svobodný (rodinný stav)", exampleGerman = "Ich bin ledig.", exampleCzech = "Jsem svobodný."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "leicht", "lehký / snadný", exampleGerman = "Die Tasche ist sehr leicht.", exampleCzech = "Taška je velmi lehká."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "lustig", "veselý / zábavný", exampleGerman = "Der Film ist lustig.", exampleCzech = "Film je zábavný."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "männlich", "mužský", exampleGerman = "Bitte männlich oder weiblich ankreuzen.", exampleCzech = "Zaškrtněte prosím mužské nebo ženské pohlaví."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "normal", "normální", exampleGerman = "Das ist ganz normal.", exampleCzech = "To je úplně normální."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "pünktlich", "dochvilný / přesný", exampleGerman = "Der Zug ist pünktlich.", exampleCzech = "Vlak jede přesně."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "ruhig", "klidný", exampleGerman = "Das Hotel ist ruhig.", exampleCzech = "Hotel je klidný."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "spät", "pozdní / pozdě", exampleGerman = "Es ist schon spät.", exampleCzech = "Už je pozdě."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "teuer", "drahý", exampleGerman = "Das Hotel ist sehr teuer.", exampleCzech = "Hotel je velmi drahý."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "tot", "mrtvý", exampleGerman = "Der Fisch ist tot.", exampleCzech = "Ryba je mrtvá."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "verboten", "zakázaný", exampleGerman = "Rauchen ist hier verboten.", exampleCzech = "Kouření je zde zakázáno."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "verheiratet", "ženatý / vdaná", exampleGerman = "Meine Eltern sind verheiratet.", exampleCzech = "Moji rodiče jsou manželé."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "weiblich", "ženský", exampleGerman = "Bitte männlich oder weiblich ankreuzen.", exampleCzech = "Zaškrtněte prosím mužské nebo ženské pohlaví."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "weit", "daleký", exampleGerman = "Der Bahnhof ist nicht weit.", exampleCzech = "Nádraží není daleko."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "wunderbar", "nádherný", exampleGerman = "Das Wetter ist wunderbar.", exampleCzech = "Počasí je nádherné."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "zufrieden", "spokojený", exampleGerman = "Ich bin mit dem Essen zufrieden.", exampleCzech = "Jsem s jídlem spokojený.")
)

private val a1AdverbSeeds = listOf(
    A1VocabularySeed(FrequencyWordKind.ADVERB, "bald", "brzy", exampleGerman = "Bis bald!", exampleCzech = "Brzy na viděnou!"),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "draußen", "venku", exampleGerman = "Die Kinder spielen draußen.", exampleCzech = "Děti si hrají venku."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "einmal", "jednou", exampleGerman = "Ich war einmal in Berlin.", exampleCzech = "Jednou jsem byl v Berlíně."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "gerade", "právě / zrovna", exampleGerman = "Ich komme gerade nach Hause.", exampleCzech = "Právě přicházím domů."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "geradeaus", "rovně", exampleGerman = "Gehen Sie bitte geradeaus.", exampleCzech = "Jděte prosím rovně."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "gestern", "včera", exampleGerman = "Gestern war ich zu Hause.", exampleCzech = "Včera jsem byl doma."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "hinten", "vzadu", exampleGerman = "Der Eingang ist hinten.", exampleCzech = "Vchod je vzadu."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "oben", "nahoře", exampleGerman = "Meine Wohnung ist oben.", exampleCzech = "Můj byt je nahoře."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "unten", "dole", exampleGerman = "Die Toilette ist unten.", exampleCzech = "Toaleta je dole."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "zurück", "zpět", exampleGerman = "Ich komme morgen zurück.", exampleCzech = "Zítra se vrátím."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "daneben", "vedle toho", exampleGerman = "Die Bank ist gleich daneben.", exampleCzech = "Banka je hned vedle."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "vorher", "předtím", exampleGerman = "Bitte ruf mich vorher an.", exampleCzech = "Zavolej mi prosím předem."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "später", "později", exampleGerman = "Wir sprechen später darüber.", exampleCzech = "Promluvíme si o tom později."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "meistens", "většinou", exampleGerman = "Ich fahre meistens mit dem Bus.", exampleCzech = "Většinou jezdím autobusem."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "selten", "zřídka", exampleGerman = "Ich esse selten Fleisch.", exampleCzech = "Maso jím zřídka."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "ungefähr", "přibližně", exampleGerman = "Der Weg dauert ungefähr zehn Minuten.", exampleCzech = "Cesta trvá přibližně deset minut."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "zuerst", "nejdříve", exampleGerman = "Zuerst frühstücken wir.", exampleCzech = "Nejdříve se nasnídáme."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "danach", "potom", exampleGerman = "Danach gehen wir nach Hause.", exampleCzech = "Potom půjdeme domů."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "sonst", "jinak", exampleGerman = "Beeil dich, sonst kommen wir zu spät.", exampleCzech = "Pospěš si, jinak přijdeme pozdě."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "zurzeit", "momentálně", exampleGerman = "Zurzeit wohne ich in Prag.", exampleCzech = "Momentálně bydlím v Praze.")
)

private val a1FunctionSeeds = listOf(
    A1VocabularySeed(FrequencyWordKind.FUNCTION, "bisschen", "trochu", exampleGerman = "Ich spreche ein bisschen Deutsch.", exampleCzech = "Mluvím trochu německy."),
    A1VocabularySeed(FrequencyWordKind.FUNCTION, "erst", "teprve", exampleGerman = "Der Zug kommt erst um neun Uhr.", exampleCzech = "Vlak přijede teprve v devět hodin."),
    A1VocabularySeed(FrequencyWordKind.FUNCTION, "nichts", "nic", exampleGerman = "Ich habe heute nichts zu tun.", exampleCzech = "Dnes nemám co dělat."),
    A1VocabularySeed(FrequencyWordKind.FUNCTION, "nie", "nikdy", exampleGerman = "Ich trinke nie Kaffee am Abend.", exampleCzech = "Večer nikdy nepiji kávu."),
    A1VocabularySeed(FrequencyWordKind.FUNCTION, "sogar", "dokonce", exampleGerman = "Sogar mein Bruder kommt mit.", exampleCzech = "Dokonce i můj bratr jde s námi."),
    A1VocabularySeed(FrequencyWordKind.FUNCTION, "circa", "asi / přibližně", exampleGerman = "Die Fahrt dauert circa eine Stunde.", exampleCzech = "Cesta trvá asi hodinu.")
)


private val a1AdditionalSeeds = listOf(
    // Additional nouns from the Goethe Start Deutsch 1 A1 list.
    A1VocabularySeed(FrequencyWordKind.NOUN, "Abflug", "odlet", article = "der", exampleGerman = "Der Abflug ist um elf Uhr.", exampleCzech = "Odlet je v jedenáct hodin."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Bild", "obrázek / fotografie", article = "das", exampleGerman = "Das Bild ist sehr schön.", exampleCzech = "Obrázek je velmi pěkný."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Dank", "poděkování", article = "der", exampleGerman = "Vielen Dank für die Hilfe.", exampleCzech = "Mnohokrát děkuji za pomoc."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Mitte", "střed / prostředek", article = "die", exampleGerman = "Der Lehrer steht in der Mitte.", exampleCzech = "Učitel stojí uprostřed."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Norden", "sever", article = "der", exampleGerman = "Der Norden ist heute kalt.", exampleCzech = "Na severu je dnes chladno."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Osten", "východ", article = "der", exampleGerman = "Der Osten liegt dort.", exampleCzech = "Východ leží tam."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Süden", "jih", article = "der", exampleGerman = "Im Süden ist es warm.", exampleCzech = "Na jihu je teplo."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Vormittag", "dopoledne", article = "der", exampleGerman = "Am Vormittag habe ich Zeit.", exampleCzech = "Dopoledne mám čas."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Westen", "západ", article = "der", exampleGerman = "Der Westen liegt links.", exampleCzech = "Západ leží vlevo."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Appetit", "chuť k jídlu", article = "der", exampleGerman = "Ich habe Appetit auf einen Apfel.", exampleCzech = "Mám chuť na jablko."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Anmeldung", "přihlášení", article = "die", exampleGerman = "Die Anmeldung ist heute möglich.", exampleCzech = "Přihlášení je dnes možné."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Anrede", "oslovení", article = "die", exampleGerman = "Die Anrede steht am Anfang des Briefes.", exampleCzech = "Oslovení stojí na začátku dopisu."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Aussage", "výrok / tvrzení", article = "die", exampleGerman = "Die Aussage ist richtig.", exampleCzech = "Tvrzení je správné."),
    A1VocabularySeed(FrequencyWordKind.NOUN, "Vorwahl", "telefonní předvolba", article = "die", exampleGerman = "Die Vorwahl von Berlin ist 030.", exampleCzech = "Telefonní předvolba Berlína je 030."),

    // More useful verbs, including separable forms and everyday phrases.
    A1VocabularySeed(FrequencyWordKind.VERB, "abfliegen", "odletět", exampleGerman = "Wir müssen um elf Uhr abfliegen.", exampleCzech = "Musíme odletět v jedenáct hodin."),
    A1VocabularySeed(FrequencyWordKind.VERB, "auf sein", "být otevřený", exampleGerman = "Die Tür kann noch auf sein.", exampleCzech = "Dveře mohou být ještě otevřené."),
    A1VocabularySeed(FrequencyWordKind.VERB, "aus sein", "být vypnutý / zhasnutý", exampleGerman = "Das Licht kann schon aus sein.", exampleCzech = "Světlo už může být zhasnuté."),
    A1VocabularySeed(FrequencyWordKind.VERB, "frühstücken", "snídat", exampleGerman = "Wir frühstücken um acht Uhr.", exampleCzech = "Snídáme v osm hodin."),
    A1VocabularySeed(FrequencyWordKind.VERB, "regnen", "pršet", exampleGerman = "Es kann heute regnen.", exampleCzech = "Dnes může pršet."),

    // Additional A1 adjectives.
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "blau", "modrý", exampleGerman = "Der Himmel ist blau.", exampleCzech = "Obloha je modrá."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "braun", "hnědý", exampleGerman = "Der Tisch ist braun.", exampleCzech = "Stůl je hnědý."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "kalt", "studený", exampleGerman = "Das Wasser ist kalt.", exampleCzech = "Voda je studená."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "kulturell", "kulturní", exampleGerman = "Das ist ein kulturelles Fest.", exampleCzech = "To je kulturní slavnost."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "leise", "potichu / tichý", exampleGerman = "Bitte sprechen Sie leise.", exampleCzech = "Mluvte prosím potichu."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "weiß", "bílý", exampleGerman = "Der Schnee ist weiß.", exampleCzech = "Sníh je bílý."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "geboren", "narozený", exampleGerman = "Ich bin in Brünn geboren.", exampleCzech = "Narodil jsem se v Brně."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "selbstständig", "samostatný / podnikající", exampleGerman = "Meine Schwester ist selbstständig.", exampleCzech = "Moje sestra podniká."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "bekannt", "známý", exampleGerman = "Der Mann ist hier bekannt.", exampleCzech = "Ten muž je zde známý."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "besser", "lepší", exampleGerman = "Heute geht es mir besser.", exampleCzech = "Dnes se cítím lépe."),
    A1VocabularySeed(FrequencyWordKind.ADJECTIVE, "billig", "levný", exampleGerman = "Die Hose ist billig.", exampleCzech = "Kalhoty jsou levné."),

    // Adverbs and short function words from everyday A1 communication.
    A1VocabularySeed(FrequencyWordKind.ADVERB, "manchmal", "někdy", exampleGerman = "Manchmal fahre ich mit dem Bus.", exampleCzech = "Někdy jezdím autobusem."),
    A1VocabularySeed(FrequencyWordKind.ADVERB, "gleich", "hned / za chvíli", exampleGerman = "Ich komme gleich.", exampleCzech = "Hned přijdu."),
    A1VocabularySeed(FrequencyWordKind.FUNCTION, "dich", "tě / tebe", exampleGerman = "Ich sehe dich heute.", exampleCzech = "Dnes tě vidím."),
    A1VocabularySeed(FrequencyWordKind.FUNCTION, "dir", "ti / tobě", exampleGerman = "Ich gebe dir das Buch.", exampleCzech = "Dám ti tu knihu."),
    A1VocabularySeed(FrequencyWordKind.FUNCTION, "ein", "jeden / nějaký", exampleGerman = "Ein Kaffee, bitte.", exampleCzech = "Jednu kávu, prosím."),
    A1VocabularySeed(FrequencyWordKind.FUNCTION, "viel", "mnoho / hodně", exampleGerman = "Ich habe heute viel Zeit.", exampleCzech = "Dnes mám hodně času.")
)

private val allA1VocabularySeeds = a1NounSeeds + a1VerbSeeds + a1AdjectiveSeeds + a1AdverbSeeds + a1FunctionSeeds + a1AdditionalSeeds

private fun A1VocabularySeed.toFrequencyEntry(): FrequencyVocabularyEntry {
    require(word.isNotBlank() && czech.isNotBlank())
    require(exampleGerman.contains(word, ignoreCase = true)) {
        "Listening word '$word' is missing from example: $exampleGerman"
    }
    val id = if (article != null) {
        val legacyId = stableReviewId("vocabulary", article, word.lowercase())
        stableReviewId("vocabulary", legacyId)
    } else {
        stableReviewId("frequency_vocabulary", kind.name, word.lowercase())
    }
    return FrequencyVocabularyEntry(
        id = id,
        level = "A1",
        kind = kind,
        word = word,
        czech = czech,
        article = article,
        extraInfo = extraInfo,
        exampleGerman = exampleGerman,
        exampleCzech = exampleCzech
    )
}

internal fun buildFrequencyVocabularyA1Expansion(
    existing: List<FrequencyVocabularyEntry>
): List<FrequencyVocabularyEntry> {
    val known = existing.map { it.word.trim().lowercase() }.toMutableSet()
    val candidates = allA1VocabularySeeds
        .distinctBy { it.word.trim().lowercase() }
        .filter { known.add(it.word.trim().lowercase()) }

    val selected = mutableListOf<A1VocabularySeed>()
    fun takeKind(kind: FrequencyWordKind, count: Int) {
        candidates.asSequence()
            .filter { candidate ->
                candidate.kind == kind && selected.none { already -> already.word.equals(candidate.word, ignoreCase = true) }
            }
            .take(count.coerceAtLeast(0))
            .forEach { selected.add(it) }
    }
    takeKind(FrequencyWordKind.NOUN, 120)
    takeKind(FrequencyWordKind.VERB, 40)
    takeKind(FrequencyWordKind.ADJECTIVE, 20)
    takeKind(FrequencyWordKind.ADVERB, 15)
    takeKind(FrequencyWordKind.FUNCTION, 5)

    if (selected.size < FREQUENCY_VOCABULARY_A1_ADDITION_COUNT) {
        candidates.asSequence()
            .filter { candidate -> selected.none { it.word.equals(candidate.word, ignoreCase = true) } }
            .take(FREQUENCY_VOCABULARY_A1_ADDITION_COUNT - selected.size)
            .forEach { selected.add(it) }
    }

    check(selected.size == FREQUENCY_VOCABULARY_A1_ADDITION_COUNT) {
        "Expected exactly 200 new A1 entries; matched " + selected.size + "."
    }
    return selected.map { it.toFrequencyEntry() }
}
