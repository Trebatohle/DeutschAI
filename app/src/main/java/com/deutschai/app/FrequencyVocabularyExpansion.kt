package com.deutschai.app

/**
 * 1,000 new everyday German words, selected from existing Czech vocabulary and
 * verb lessons by their rank in the OpenSubtitles 2018 German frequency list.
 * The CEFR bands are frequency-informed estimates, not official exam lists.
 */
internal const val SPOKEN_FREQUENCY_ADDITION_COUNT = 1000

private val spokenFrequencyRanks: Map<String, Int> = """
102§mann
129§weg
137§zeit
153§frau
165§meinen
178§vater
184§tag
188§liebe
193§geld
197§mutter
219§nacht
254§abend
260§freund
267§angst
295§musik
296§arbeit
303§familie
310§problem
311§sohn
323§hilfe
336§kind
341§bruder
356§frage
368§uhr
369§stadt
371§baby
375§kopf
380§glück
401§tür
402§auto
406§polizei
429§tochter
431§idee
432§schwester
440§name
463§hand
465§jahr
468§ruhe
491§wasser
502§woche
513§zimmer
516§gefallen
533§seite
538§spiel
554§bett
559§schule
561§wort
582§sorge
591§blut
593§eltern
595§herz
614§platz
621§nummer
633§telefon
637§plan
643§fehler
648§pass
650§stunde
653§menge
660§büro
674§film
676§hund
694§feuer
706§kaffee
707§luft
723§buch
729§arzt
732§straße
740§onkel
747§vertrauen
753§gefühl
763§stück
772§himmel
773§nachricht
778§laut
783§erde
785§schlüssel
788§eher
808§wohnung
811§krankenhaus
815§klasse
824§wenigstens
827§stolz
837§solange
844§fühlen
846§boden
854§mund
868§laden
872§meinung
878§geschäft
888§vorher
899§handy
916§folgen
917§bitten
919§nähe
926§tisch
937§hotel
942§antwort
968§auge
977§entscheidung
980§fenster
990§rum
995§bewegung
1014§bier
1022§finger
1027§monat
1032§wach
1034§arm
1036§chef
1039§erledigt
1049§reise
1056§jedenfalls
1064§foto
1070§minute
1072§zug
1074§wofür
1087§brief
1089§sucht
1092§unfall
1095§beispiel
1111§zumindest
1117§richtung
1127§tasche
1141§preis
1148§firma
1150§rücken
1163§tante
1165§pferd
1183§punkt
1192§geburtstag
1193§haut
1196§sekunde
1205§anruf
1208§karte
1218§sonne
1220§nämlich
1224§professor
1226§nase
1229§bank
1231§computer
1239§fuß
1245§gold
1248§wein
1255§erledigen
1257§gegenüber
1258§schlaf
1259§lust
1264§übrigens
1283§hoffnung
1296§weder
1300§flugzeug
1306§tee
1307§entweder
1328§hals
1338§zeichen
1339§richter
1341§küche
1368§worauf
1371§kamera
1383§wald
1388§erkennen
1389§glas
1395§kleid
1400§abendessen
1414§aufgabe
1417§drinnen
1433§freude
1434§ehemann
1437§schwein
1450§adresse
1452§messer
1454§respekt
1457§davor
1460§ball
1486§wochenende
1487§worüber
1496§nachts
1499§schmerz
1516§ring
1518§lied
1527§insel
1531§dorf
1559§versprechen
1562§verdienen
1582§fleisch
1593§gespräch
1595§taxi
1597§see
1599§wind
1604§wand
1605§bus
1614§polizist
1618§flasche
1622§meer
1627§bein
1633§frühstück
1655§ecke
1656§brücke
1662§oma
1670§pause
1681§schloss
1682§hut
1705§operation
1710§dach
1715§gehirn
1718§mut
1721§tier
1726§rose
1728§geschlossen
1735§urlaub
1741§baum
1745§lehrer
1772§katze
1778§dringend
1780§großvater
1781§sommer
1797§hose
1810§restaurant
1828§bad
1831§normalerweise
1834§flug
1842§morgens
1854§steigen
1858§labor
1863§fisch
1868§kuchen
1874§farbe
1878§garten
1884§koffer
1886§kurs
1904§lager
1908§vogel
1910§strand
1925§nachher
1931§soldat
1935§termin
1946§vertrag
1954§fluss
1963§dadurch
1971§großmutter
1979§sprache
2001§knie
2014§erklärung
2021§opa
2028§milch
2041§grenze
2044§peinlich
2048§aufmerksamkeit
2066§satt
2076§übersetzung
2082§krankheit
2093§anzug
2097§freitag
2110§kontrollieren
2111§knochen
2113§getrennt
2114§programm
2128§stuhl
2139§toilette
2140§annehmen
2151§genießen
2162§theater
2163§regen
2166§erlauben
2171§flughafen
2183§fahrer
2200§post
2225§projekt
2227§immerhin
2232§zählen
2243§zuletzt
2247§ohr
2250§schüler
2257§größe
2267§sonntag
2275§rechnung
2284§abends
2289§cousin
2305§kino
2309§tanz
2310§geöffnet
2322§aufmachen
2345§geduld
2354§unterschreiben
2365§samstag
2367§motor
2372§beinahe
2381§sich anziehen
2384§bauch
2392§landen
2393§berg
2397§untersuchung
2417§montag
2432§freundschaft
2437§beruf
2438§gebiet
2450§jacke
2462§ton
2476§müll
2483§decke
2490§mittagessen
2493§rock
2502§spiegel
2508§wetter
2517§champagner
2528§kleidung
2529§gras
2535§brot
2548§hemd
2551§praktisch
2553§zunge
2556§winter
2559§dahinter
2576§strom
2582§quelle
2588§spitze
2640§starten
2653§hungrig
2657§kuh
2668§rechtzeitig
2672§treppe
2676§wolf
2680§wut
2681§schlange
2687§pizza
2690§mantel
2697§mittag
2725§fliege
2733§stoff
2746§schrank
2751§sich entspannen
2754§einladung
2767§feld
2770§information
2795§klinik
2801§hammer
2803§internet
2815§krankenwagen
2822§schnee
2839§brille
2845§leiter
2850§cent
2856§bestätigen
2879§holz
2889§riechen
2892§ei
2905§stress
2932§schulter
2944§unterricht
2960§einladen
2961§käse
2967§gesundheit
2972§täglich
2985§reifen
3009§liefern
3030§schreibtisch
3042§vorbereiten
3048§zucker
3059§tasse
3064§magen
3070§behandlung
3073§markt
3116§roman
3117§pünktlich
3137§sparen
3143§miete
3148§wüste
3149§prüfen
3159§dienstag
3165§training
3171§suppe
3173§sitz
3188§gedächtnis
3208§tiger
3222§wissenschaftler
3226§dusche
3227§mitarbeiter
3237§fieber
3240§donnerstag
3249§ausweis
3251§sport
3262§lektion
3268§ratte
3276§wunde
3287§nachsehen
3302§verabredung
3320§kühlschrank
3322§lehrerin
3337§sand
3353§stahl
3361§fabrik
3375§angenehm
3429§höhle
3430§schokolade
3476§mauer
3481§abschluss
3513§nützlich
3520§gemeinde
3525§bär
3526§euro
3546§satz
3595§teller
3604§wäsche
3640§fahrrad
3644§bequem
3654§kollege
3659§konto
3680§sich umziehen
3681§zunächst
3695§unhöflich
3698§universität
3701§koch
3706§prüfung
3712§ehefrau
3730§bibliothek
3744§badezimmer
3751§bahnhof
3762§konzert
3778§leihen
3809§übung
3814§couch
3815§cousine
3824§knopf
3838§rad
3884§sich bedanken
3886§pilot
3895§ärztin
3903§kopfschmerzen
3904§öl
3915§museum
3934§therapie
3947§ankunft
3959§kunde
3962§unangenehm
3982§kapitel
4004§stiefel
4014§wolle
4031§paket
4040§schmuck
4041§ausgang
4057§ausbildung
4071§küste
4079§puls
4081§flur
4085§parkplatz
4089§mittwoch
4112§zurückbringen
4129§motorrad
4140§huhn
4164§stift
4176§teppich
4181§trainer
4183§schuh
4204§löschen
4220§fähigkeit
4237§vorbeikommen
4274§ausmachen
4275§rezept
4300§kofferraum
4325§felsen
4332§zurückgeben
4339§ebene
4355§tal
4362§trauer
4371§rechnen
4372§aufzug
4402§höflich
4415§unterschrift
4420§eingang
4442§privat
4449§eimer
4452§salz
4455§esel
4456§krankenschwester
4459§fahrzeug
4460§unterwäsche
4491§maus
4520§verschieben
4521§verspätung
4525§entdecken
4530§akzent
4541§mannschaft
4557§ausschalten
4560§whisky
4569§kaution
4589§sich hinlegen
4596§nachbar
4605§affe
4623§besetzt
4625§kanal
4626§blutdruck
4628§ausgeben
4646§blume
4662§praxis
4676§zentrum
4678§entfernung
4704§zuständig
4705§losgehen
4711§bargeld
4723§öffentlich
4732§kissen
4752§salat
4753§schicht
4770§entspannt
4786§wodka
4838§sofa
4847§butter
4848§nachbarschaft
4853§saft
4858§werkstatt
4872§fußball
4877§gürtel
4883§kabel
4886§zahn
4895§beschädigt
4898§bahn
4915§bach
4932§leber
4936§krawatte
4946§blatt
4948§dschungel
4968§schachtel
4974§brunnen
4977§lampe
4987§frühling
4997§gehalt
5006§kasse
5011§gitarre
5065§feige
5072§e-mail
5084§lautsprecher
5085§ticket
5097§etage
5115§verteilen
5116§fuchs
5129§reis
5186§kredit
5225§nadel
5228§apfel
5245§notaufnahme
5248§trinkgeld
5267§datum
5277§brett
5340§ozean
5350§notruf
5353§häufig
5356§anstrengend
5360§kellner
5379§ausrede
5403§handtuch
5420§unnötig
5431§platte
5434§mütze
5452§fit
5479§sowohl
5484§spritze
5515§sich wundern
5556§metall
5570§absagen
5584§temperatur
5589§verletzung
5597§adler
5599§lunge
5629§frosch
5637§cola
5645§silber
5655§ente
5676§ziege
5677§löwe
5713§bock
5746§laptop
5752§u-bahn
5768§narbe
5780§zahnarzt
5799§helm
5803§bemerken
5804§herbst
5817§organisieren
5820§besprechung
5823§kreditkarte
5862§feierabend
5869§passwort
5871§mittlerweile
5893§husten
5894§schublade
5909§anhänger
5924§rucksack
5929§t-shirt
5951§tankstelle
5954§schneider
5958§handtasche
5968§münze
5998§sich erholen
6002§sich beschweren
6007§produkt
6009§tafel
6020§kaninchen
6022§orange
6036§mahlzeit
6043§kenntnis
6093§infektion
6096§winkel
6100§torte
6113§schinken
6119§hai
6135§supermarkt
6174§ufer
6181§bezirk
6197§scheibe
6209§diagnose
6236§löffel
6245§fahrstuhl
6263§mitfahren
6282§hahn
6288§speck
6304§seife
6317§erdbeben
6330§rollstuhl
6375§transporter
6383§becher
6428§herstellen
6432§honig
6485§hausmeister
6499§schein
6501§kellnerin
6515§hobby
6518§nachtisch
6519§kollegin
6526§eisen
6541§kennzeichen
6557§dose
6571§nachbarin
6577§wegwerfen
6578§spinne
6583§nagel
6616§fischer
6626§vorhang
6737§sich bemühen
6770§ingenieur
6794§hase
6802§blutung
6808§messen
6814§keks
6818§mieten
6843§truthahn
6852§jeans
6868§busch
6873§bestellung
6893§ausleihen
6909§pflanze
6936§medikament
6974§schere
7057§wurm
7059§kerze
7084§beutel
7119§gelegentlich
7121§braten
7165§lippenstift
7169§maler
7177§polizistin
7182§aufheben
7185§freizeit
7204§blase
7212§konzentration
7221§reinigung
7234§batterie
7285§schüssel
7301§mitbewohner
7317§balkon
7375§soße
7397§wolke
7406§institut
7426§sackgasse
7435§verkäufer
7460§geduldig
7480§region
7484§gipfel
7493§journalist
7517§student
7534§kalender
7543§friseur
7575§verband
7609§sumpf
7610§apotheke
7621§schal
7634§niere
7660§käfer
7665§limonade
7677§schale
7687§bh
7718§armband
7724§einschalten
7746§grippe
7751§floh
7755§schaf
7770§spende
7773§gans
7790§buchhalter
7871§samen
7878§parfüm
7918§ärmel
7930§gärtner
7941§topf
7960§techniker
7967§ausgabe
8042§maulwurf
8047§taube
8048§mais
8052§feder
8055§dessert
8075§fach
8080§kragen
8104§mechaniker
8116§chirurg
8117§wal
8129§note
8130§datei
8137§wecker
8152§herd
8176§köchin
8188§wurst
8209§hummer
8215§lieferwagen
8242§verfügbar
8243§autobahn
8261§brötchen
8263§haushalt
8283§verbieten
8304§mittags
8313§drucken
8326§tennis
8330§kreuzung
8332§weiche
8342§bucht
8343§bildschirm
8358§geschirr
8371§vorname
8378§elefant
8385§eichhörnchen
8398§klimaanlage
8399§vulkan
8472§geschäftsführer
8475§strafzettel
8493§durstig
8507§zumachen
8530§pullover
8568§sahne
8623§fotograf
8655§schlauch
8671§schülerin
8681§regal
8692§matratze
8721§einpacken
8734§umzug
8754§nachname
8765§software
8779§ungeduldig
8786§bruch
8796§leder
8851§gruß
8901§schildkröte
8920§verleihen
8923§deckel
8930§schwamm
8961§ausschlag
8970§quittung
9039§auspacken
9050§studentin
9064§teich
9102§schmetterling
9111§baustelle
9133§anschluss
9180§sessel
9181§wegbringen
9190§bluse
9207§klingel
9229§provinz
9322§besen
9344§senf
9355§getränk
9394§erkältung
9433§rechtsanwalt
9478§kiefer
9483§gabel
9514§innenstadt
9515§mitbewohnerin
9531§schaufel
9544§grill
9582§anschließend
9586§schriftlich
9592§stier
9623§handschuh
9624§seide
9647§zahnbürste
9649§blei
9653§erreichbar
9656§zuverlässig
9673§unterhose
9686§massage
9688§pudding
9734§anmachen
9744§fernbedienung
9766§kostenlos
9783§hengst
9786§banane
9816§terrasse
9835§seemann
9853§sich weigern
9905§abfahrt
9907§briefkasten
9930§packung
9935§aufteilen
9957§autoschlüssel
9995§forscher
10031§depression
10066§ausfüllen
10091§aufwärmen
10121§passagier
10136§winde
10137§wirbel
10139§kakao
10189§marmelade
10205§zitrone
10213§quark
10282§fels
10290§gorilla
10295§pflaster
10301§ampel
10307§hirsch
10326§portion
10338§halskette
10349§mehl
10380§läufer
10394§einkommen
10415§richterin
10427§formular
10429§architekt
10439§bremse
10483§zeugnis
10496§sonnenbrille
10519§kappe
10537§auskunft
10570§nummernschild
10602§losfahren
10611§papagei
10614§ketchup
10618§feiertag
10647§vermieter
10653§scheinwerfer
10680§falke
10696§zitat
10717§alarmanlage
10730§klippe
10756§journalistin
10762§fahrkarte
10772§seminar
10802§schlucht
10833§biene
10843§ast
10848§schmerzmittel
10849§lappen
10875§blutgruppe
10881§vermieten
11064§schnecke
11105§knoblauch
11111§socke
11127§rezeption
11141§portemonnaie
11171§wiese
11186§pfeffer
11191§stau
11208§absatz
11232§fassade
11296§praktikum
11309§klebeband
11328§eule
11329§mitteilung
11342§therapeut
11392§bewerbung
11399§panne
11403§abbiegen
11450§vorlesung
11455§lenkrad
11483§anmelden
11494§andernfalls
11519§pumpe
11590§therapeutin
11591§pfanne
11592§komma
11614§lachs
11730§hang
11744§grundschule
11747§sauce
11749§schwelle
11783§pyjama
11797§staubsauger
11811§rabatt
11817§sich verabreden
11829§putz
11845§pfleger
11853§gummi
11877§niete
11896§kundin
11962§ausverkauft
11967§migräne
12009§sehne
12019§kürbis
12020§kröte
12026§kalb
12031§foundation
12042§vereinbaren
12051§waschmaschine
12052§shorts
12124§säge
12153§gleis
12159§nachschauen
12192§beton
12205§rotwein
12227§wirbelsäule
12231§reservierung
12262§kontinent
12277§aufladen
12290§joghurt
12308§blüte
12363§mikrowelle
12368§mülleimer
12382§getreide
12388§bleistift
12474§creme
12496§kamm
12537§geldbeutel
12544§motivation
12571§küken
12632§unkraut
12638§nachweisen
12681§feuerwehrmann
12682§kopfhörer
12695§sekt
12740§fakultät
12764§heft
12858§app
12878§weide
12889§speisekarte
12890§zurückschicken
12904§weizen
12905§gips
12906§siedlung
12957§pinguin
13014§balken
13018§acker
13052§akku
13077§alltag
13106§krokodil
13142§reißverschluss
13172§zebra
13213§ernährung
13259§reh
13304§slip
13403§kapuze
13425§kohl
13465§kühler
13472§zündung
13486§wasserfall
13498§bademantel
13528§zange
13529§bäcker
13534§aktenkoffer
13579§toaster
13616§schwan
13671§eiche
13678§espresso
13690§spatz
13725§darm
13738§imbiss
13834§vanille
13854§riegel
13875§nachthemd
13898§shampoo
13929§wurzel
13954§cappuccino
13983§webseite
13985§zylinder
14020§lasagne
14091§kartoffel
14106§kaufmann
14139§tablette
14226§serviette
14231§ananas
14315§unfreundlich
14406§palme
14427§schraube
14453§visum
14511§kommode
14527§thunfisch
14538§nachfragen
14588§melone
14596§ohrring
14622§blutprobe
14689§zweig
14729§mülltonne
""".lineSequence()
    .mapNotNull { row ->
        val fields = row.split('§', limit = 2)
        if (fields.size == 2) fields[0].toIntOrNull()?.let { fields[1].lowercase() to it } else null
    }
    .toMap()

private fun spokenLevelFor(word: String): String {
    val key = word.lowercase()
    val rank = spokenFrequencyRanks[key] ?: spokenFrequencyRanks[key.removePrefix("sich ")] ?: 50000
    return when (rank) {
        in 1..1000 -> "A1"
        in 1001..4000 -> "A2"
        in 4001..10000 -> "B1"
        else -> "B2"
    }
}

private data class SpokenExtraSeed(
    val kind: FrequencyWordKind,
    val word: String,
    val czech: String,
    val article: String? = null,
    val exampleGerman: String = "",
    val exampleCzech: String = ""
)

private fun spokenExtra(
    kind: FrequencyWordKind,
    word: String,
    czech: String,
    article: String? = null,
    exampleGerman: String = "",
    exampleCzech: String = ""
) = SpokenExtraSeed(kind, word, czech, article, exampleGerman, exampleCzech)

private val spokenExtraSeeds = listOf(
    spokenExtra(FrequencyWordKind.NOUN, "Alltag", "každodenní život", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Badezimmer", "koupelna", article = "das"),
    spokenExtra(FrequencyWordKind.NOUN, "Mülltonne", "popelnice", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Nachbarschaft", "sousedství", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Einkauf", "nákup", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Einkaufswagen", "nákupní vozík", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Einkaufsliste", "nákupní seznam", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Warteschlange", "fronta", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Öffnungszeit", "otevírací doba", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Schließzeit", "zavírací doba", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Verabredung", "schůzka / domluva", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Feierabend", "čas po práci", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Ausrede", "výmluva", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Gefallen", "laskavost", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Umzug", "stěhování", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Vermieter", "pronajímatel", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Kaution", "kauce", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Nebenkosten", "vedlejší náklady", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Geschirrspüler", "myčka nádobí", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Fernbedienung", "dálkový ovladač", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "WLAN", "bezdrátová síť", article = "das"),
    spokenExtra(FrequencyWordKind.NOUN, "Router", "router", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Kopfhörer", "sluchátko / sluchátka", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Lautsprecher", "reproduktor", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Ausweis", "průkaz", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Personalausweis", "občanský průkaz", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Reisepass", "cestovní pas", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Visum", "vízum", article = "das"),
    spokenExtra(FrequencyWordKind.NOUN, "Bankkarte", "bankovní karta", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Schicht", "směna", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Arbeitsweg", "cesta do práce", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Homeoffice", "práce z domova", article = "das"),
    spokenExtra(FrequencyWordKind.NOUN, "Reiseführer", "průvodce", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Reisetasche", "cestovní taška", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Strafzettel", "pokutový lístek", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Parkschein", "parkovací lístek", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Fahrstuhl", "výtah", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Rolltreppe", "eskalátor", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Bürgersteig", "chodník", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Fahrradhelm", "cyklistická helma", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Hausverwaltung", "správa domu", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Mitbewohner", "spolubydlící", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Mitbewohnerin", "spolubydlící", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Mitteilung", "sdělení", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Aushang", "oznámení na nástěnce", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Rückgabe", "vrácení", article = "die"),
    spokenExtra(FrequencyWordKind.NOUN, "Umtausch", "výměna zboží", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Gutschein", "poukaz", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Kundenservice", "zákaznický servis", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Notruf", "tísňové volání", article = "der"),
    spokenExtra(FrequencyWordKind.NOUN, "Bescheinigung", "potvrzení / osvědčení", article = "die"),
    spokenExtra(FrequencyWordKind.VERB, "anprobieren", "vyzkoušet si", exampleGerman = "Ich möchte die Jacke anprobieren.", exampleCzech = "Chci si vyzkoušet bundu."),
    spokenExtra(FrequencyWordKind.VERB, "einpacken", "zabalit", exampleGerman = "Ich packe die Geschenke ein.", exampleCzech = "Balím dárky."),
    spokenExtra(FrequencyWordKind.VERB, "auspacken", "vybalit", exampleGerman = "Ich packe meinen Koffer aus.", exampleCzech = "Vybalím si kufr."),
    spokenExtra(FrequencyWordKind.VERB, "aufdrehen", "pustit / otevřít (kohoutek)", exampleGerman = "Dreh bitte das Wasser auf.", exampleCzech = "Prosím, pusť vodu."),
    spokenExtra(FrequencyWordKind.VERB, "zudrehen", "zavřít (kohoutek)", exampleGerman = "Dreh bitte den Wasserhahn zu.", exampleCzech = "Prosím, zavři kohoutek."),
    spokenExtra(FrequencyWordKind.VERB, "aufmachen", "otevřít", exampleGerman = "Mach bitte das Fenster auf.", exampleCzech = "Prosím, otevři okno."),
    spokenExtra(FrequencyWordKind.VERB, "zumachen", "zavřít", exampleGerman = "Mach bitte die Tür zu.", exampleCzech = "Prosím, zavři dveře."),
    spokenExtra(FrequencyWordKind.VERB, "wegwerfen", "vyhodit", exampleGerman = "Wirf das bitte nicht weg.", exampleCzech = "Prosím, to nevyhazuj."),
    spokenExtra(FrequencyWordKind.VERB, "zurückgeben", "vrátit", exampleGerman = "Ich muss das Buch morgen zurückgeben.", exampleCzech = "Zítra musím vrátit knihu."),
    spokenExtra(FrequencyWordKind.VERB, "vorbeikommen", "zastavit se", exampleGerman = "Kannst du morgen vorbeikommen?", exampleCzech = "Můžeš se zítra zastavit?"),
    spokenExtra(FrequencyWordKind.VERB, "nachfragen", "doptat se", exampleGerman = "Ich frage noch einmal nach.", exampleCzech = "Ještě se jednou doptám."),
    spokenExtra(FrequencyWordKind.VERB, "nachschauen", "podívat se / zkontrolovat", exampleGerman = "Ich schaue kurz im Kalender nach.", exampleCzech = "Krátce se podívám do kalendáře."),
    spokenExtra(FrequencyWordKind.VERB, "nachsehen", "ověřit / podívat se", exampleGerman = "Ich sehe schnell im Internet nach.", exampleCzech = "Rychle se podívám na internet."),
    spokenExtra(FrequencyWordKind.VERB, "anmachen", "zapnout", exampleGerman = "Mach bitte das Licht an.", exampleCzech = "Prosím, rozsviť."),
    spokenExtra(FrequencyWordKind.VERB, "ausmachen", "vypnout", exampleGerman = "Mach bitte den Fernseher aus.", exampleCzech = "Prosím, vypni televizi."),
    spokenExtra(FrequencyWordKind.VERB, "einschalten", "zapnout", exampleGerman = "Kannst du die Heizung einschalten?", exampleCzech = "Můžeš zapnout topení?"),
    spokenExtra(FrequencyWordKind.VERB, "ausschalten", "vypnout", exampleGerman = "Schalte bitte dein Handy aus.", exampleCzech = "Prosím, vypni si mobil."),
    spokenExtra(FrequencyWordKind.VERB, "aufladen", "nabít", exampleGerman = "Ich muss mein Handy aufladen.", exampleCzech = "Musím si nabít mobil."),
    spokenExtra(FrequencyWordKind.VERB, "losfahren", "vyrazit (vozidlem)", exampleGerman = "Wir fahren um acht Uhr los.", exampleCzech = "Vyjíždíme v osm hodin."),
    spokenExtra(FrequencyWordKind.VERB, "losgehen", "vyrazit / vyjít", exampleGerman = "Wann wollen wir losgehen?", exampleCzech = "Kdy chceme vyrazit?"),
    spokenExtra(FrequencyWordKind.VERB, "mitfahren", "jet s někým", exampleGerman = "Kann ich mit euch mitfahren?", exampleCzech = "Mohu jet s vámi?"),
    spokenExtra(FrequencyWordKind.VERB, "abbiegen", "odbočit", exampleGerman = "Hier musst du links abbiegen.", exampleCzech = "Tady musíš odbočit doleva."),
    spokenExtra(FrequencyWordKind.VERB, "wegbringen", "odnést / odvézt", exampleGerman = "Ich bringe das Paket zur Post weg.", exampleCzech = "Odnesu balík na poštu."),
    spokenExtra(FrequencyWordKind.VERB, "zurückbringen", "přinést zpět / vrátit", exampleGerman = "Ich bringe dir das Buch morgen zurück.", exampleCzech = "Zítra ti vrátím knihu."),
    spokenExtra(FrequencyWordKind.VERB, "anmelden", "přihlásit", exampleGerman = "Ich möchte mich für den Kurs anmelden.", exampleCzech = "Chci se přihlásit do kurzu."),
    spokenExtra(FrequencyWordKind.VERB, "abmelden", "odhlásit", exampleGerman = "Ich muss mich noch abmelden.", exampleCzech = "Ještě se musím odhlásit."),
    spokenExtra(FrequencyWordKind.VERB, "ummelden", "přehlásit", exampleGerman = "Nach dem Umzug muss ich mich ummelden.", exampleCzech = "Po stěhování se musím přehlásit."),
    spokenExtra(FrequencyWordKind.VERB, "nachweisen", "prokázat / doložit", exampleGerman = "Kannst du das schriftlich nachweisen?", exampleCzech = "Můžeš to písemně doložit?"),
    spokenExtra(FrequencyWordKind.VERB, "umtauschen", "vyměnit zboží", exampleGerman = "Kann ich die Hose umtauschen?", exampleCzech = "Mohu si vyměnit kalhoty?"),
    spokenExtra(FrequencyWordKind.VERB, "zurückschicken", "poslat zpět", exampleGerman = "Ich muss die Ware zurückschicken.", exampleCzech = "Musím zboží poslat zpět."),
    spokenExtra(FrequencyWordKind.VERB, "ausleihen", "půjčit si", exampleGerman = "Kann ich mir dein Fahrrad ausleihen?", exampleCzech = "Můžu si půjčit tvoje kolo?"),
    spokenExtra(FrequencyWordKind.VERB, "verleihen", "půjčovat někomu", exampleGerman = "Die Bibliothek verleiht Bücher.", exampleCzech = "Knihovna půjčuje knihy."),
    spokenExtra(FrequencyWordKind.VERB, "aufteilen", "rozdělit", exampleGerman = "Wir teilen die Kosten unter uns auf.", exampleCzech = "Rozdělíme si mezi sebou náklady."),
    spokenExtra(FrequencyWordKind.VERB, "verteilen", "rozdělit / rozdat", exampleGerman = "Sie verteilt die Aufgaben im Team.", exampleCzech = "Rozděluje úkoly v týmu."),
    spokenExtra(FrequencyWordKind.VERB, "aufheben", "zvednout / uschovat", exampleGerman = "Heb bitte den Zettel auf.", exampleCzech = "Prosím, zvedni ten lístek."),
    spokenExtra(FrequencyWordKind.VERB, "weglegen", "odložit", exampleGerman = "Leg bitte dein Handy weg.", exampleCzech = "Prosím, odlož mobil."),
    spokenExtra(FrequencyWordKind.VERB, "aufwärmen", "ohřát", exampleGerman = "Ich wärme das Essen auf.", exampleCzech = "Ohřívám jídlo."),
    spokenExtra(FrequencyWordKind.VERB, "abkühlen", "nechat vychladnout", exampleGerman = "Lass den Tee kurz abkühlen.", exampleCzech = "Nech čaj chvíli vychladnout."),
    spokenExtra(FrequencyWordKind.VERB, "nachfüllen", "doplnit", exampleGerman = "Ich muss Wasser nachfüllen.", exampleCzech = "Musím doplnit vodu."),
    spokenExtra(FrequencyWordKind.VERB, "sich beschweren", "stěžovat si", exampleGerman = "Ich möchte mich über den Lärm beschweren.", exampleCzech = "Chci si stěžovat na hluk."),
    spokenExtra(FrequencyWordKind.VERB, "sich erholen", "zotavit se / odpočinout si", exampleGerman = "Am Wochenende kann ich mich erholen.", exampleCzech = "O víkendu si mohu odpočinout."),
    spokenExtra(FrequencyWordKind.VERB, "sich verspäten", "mít zpoždění", exampleGerman = "Der Bus verspätet sich heute.", exampleCzech = "Autobus má dnes zpoždění."),
    spokenExtra(FrequencyWordKind.VERB, "sich verabreden", "domluvit si schůzku", exampleGerman = "Wir verabreden uns für Samstag.", exampleCzech = "Domluvíme se na sobotu."),
    spokenExtra(FrequencyWordKind.VERB, "sich erkundigen", "informovat se", exampleGerman = "Ich erkundige mich nach dem Preis.", exampleCzech = "Informuji se na cenu."),
    spokenExtra(FrequencyWordKind.VERB, "sich weigern", "odmítnout / vzpírat se", exampleGerman = "Er weigert sich, mitzukommen.", exampleCzech = "Odmítá jít s námi."),
    spokenExtra(FrequencyWordKind.VERB, "sich bemühen", "snažit se", exampleGerman = "Ich bemühe mich um eine Lösung.", exampleCzech = "Snažím se najít řešení."),
    spokenExtra(FrequencyWordKind.VERB, "sich wundern", "divit se", exampleGerman = "Ich wundere mich über seine Antwort.", exampleCzech = "Divím se jeho odpovědi."),
    spokenExtra(FrequencyWordKind.VERB, "sich bedanken", "poděkovat", exampleGerman = "Ich möchte mich bei dir bedanken.", exampleCzech = "Chtěl bych ti poděkovat."),
    spokenExtra(FrequencyWordKind.VERB, "sich entspannen", "uvolnit se / relaxovat", exampleGerman = "Am Abend möchte ich mich entspannen.", exampleCzech = "Večer si chci odpočinout."),
    spokenExtra(FrequencyWordKind.VERB, "sich hinlegen", "lehnout si", exampleGerman = "Ich lege mich kurz hin.", exampleCzech = "Na chvíli si lehnu."),
    spokenExtra(FrequencyWordKind.VERB, "sich umziehen", "převléknout se", exampleGerman = "Ich muss mich schnell umziehen.", exampleCzech = "Musím se rychle převléknout."),
    spokenExtra(FrequencyWordKind.VERB, "sich anziehen", "obléknout se", exampleGerman = "Zieh dich warm an.", exampleCzech = "Teple se obleč."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "bequem", "pohodlný", exampleGerman = "Der Sitz ist bequem.", exampleCzech = "Sedadlo je pohodlné."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "unbequem", "nepohodlný", exampleGerman = "Dieser Stuhl ist unbequem.", exampleCzech = "Tato židle je nepohodlná."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "praktisch", "praktický", exampleGerman = "Diese Tasche ist sehr praktisch.", exampleCzech = "Tato taška je velmi praktická."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "unpraktisch", "nepraktický", exampleGerman = "Das ist im Alltag unpraktisch.", exampleCzech = "V běžném životě je to nepraktické."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "nützlich", "užitečný", exampleGerman = "Diese App ist wirklich nützlich.", exampleCzech = "Tato aplikace je opravdu užitečná."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "unnötig", "zbytečný", exampleGerman = "Der Aufwand ist unnötig.", exampleCzech = "Tato námaha je zbytečná."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "dringend", "naléhavý", exampleGerman = "Ich brauche dringend deine Hilfe.", exampleCzech = "Naléhavě potřebuji tvoji pomoc."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "pünktlich", "přesný / dochvilný", exampleGerman = "Der Zug ist heute pünktlich.", exampleCzech = "Vlak jede dnes přesně."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "unpünktlich", "nepřesný / nedochvilný", exampleGerman = "Der Bus ist oft unpünktlich.", exampleCzech = "Autobus často nejezdí včas."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "zuverlässig", "spolehlivý", exampleGerman = "Sie ist sehr zuverlässig.", exampleCzech = "Je velmi spolehlivá."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "unzuverlässig", "nespolehlivý", exampleGerman = "Der alte Drucker ist unzuverlässig.", exampleCzech = "Stará tiskárna je nespolehlivá."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "höflich", "zdvořilý", exampleGerman = "Der Verkäufer war sehr höflich.", exampleCzech = "Prodavač byl velmi zdvořilý."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "unhöflich", "nezdvořilý", exampleGerman = "Das war ziemlich unhöflich.", exampleCzech = "To bylo dost nezdvořilé."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "geduldig", "trpělivý", exampleGerman = "Bitte sei ein wenig geduldig.", exampleCzech = "Buď prosím trochu trpělivý."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "ungeduldig", "netrpělivý", exampleGerman = "Die Kinder werden ungeduldig.", exampleCzech = "Děti začínají být netrpělivé."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "unfreundlich", "nepříjemný / nepřátelský", exampleGerman = "Die Antwort klang unfreundlich.", exampleCzech = "Odpověď zněla nepříjemně."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "unvorsichtig", "neopatrný", exampleGerman = "Sei bitte nicht so unvorsichtig.", exampleCzech = "Nebuď prosím tak neopatrný."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "beschädigt", "poškozený", exampleGerman = "Das Paket ist beschädigt angekommen.", exampleCzech = "Balík dorazil poškozený."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "kostenlos", "zdarma / bezplatný", exampleGerman = "Der Eintritt ist heute kostenlos.", exampleCzech = "Vstup je dnes zdarma."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "kostenpflichtig", "zpoplatněný", exampleGerman = "Dieser Parkplatz ist kostenpflichtig.", exampleCzech = "Toto parkoviště je placené."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "verfügbar", "dostupný / k dispozici", exampleGerman = "Das Zimmer ist noch verfügbar.", exampleCzech = "Pokoj je stále volný."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "ausverkauft", "vyprodaný", exampleGerman = "Das Konzert ist ausverkauft.", exampleCzech = "Koncert je vyprodaný."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "geöffnet", "otevřený", exampleGerman = "Die Apotheke ist noch geöffnet.", exampleCzech = "Lékárna je ještě otevřená."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "geschlossen", "zavřený", exampleGerman = "Der Laden ist schon geschlossen.", exampleCzech = "Obchod už je zavřený."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "besetzt", "obsazený", exampleGerman = "Die Leitung ist gerade besetzt.", exampleCzech = "Linka je právě obsazená."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "privat", "soukromý", exampleGerman = "Das ist eine private Nachricht.", exampleCzech = "To je soukromá zpráva."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "öffentlich", "veřejný", exampleGerman = "Der Park ist öffentlich zugänglich.", exampleCzech = "Park je veřejně přístupný."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "schriftlich", "písemný", exampleGerman = "Bitte gib mir eine schriftliche Bestätigung.", exampleCzech = "Dej mi prosím písemné potvrzení."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "mündlich", "ústní", exampleGerman = "Die Prüfung ist mündlich.", exampleCzech = "Zkouška je ústní."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "telefonisch", "telefonický", exampleGerman = "Wir klären das telefonisch.", exampleCzech = "Vyřešíme to telefonicky."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "erreichbar", "k zastižení / dostupný", exampleGerman = "Ich bin am Nachmittag erreichbar.", exampleCzech = "Odpoledne budu k zastižení."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "zuständig", "příslušný / odpovědný", exampleGerman = "Wer ist dafür zuständig?", exampleCzech = "Kdo to má na starosti?"),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "hungrig", "hladový", exampleGerman = "Ich bin schon wieder hungrig.", exampleCzech = "Už mám zase hlad."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "durstig", "žíznivý", exampleGerman = "Nach dem Sport bin ich durstig.", exampleCzech = "Po sportu mám žízeň."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "satt", "sytý", exampleGerman = "Danke, ich bin satt.", exampleCzech = "Děkuji, jsem sytý."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "ausgeschlafen", "vyspaný", exampleGerman = "Heute bin ich endlich ausgeschlafen.", exampleCzech = "Dnes jsem se konečně vyspal."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "wach", "vzhůru / bdělý", exampleGerman = "Bist du schon wach?", exampleCzech = "Už jsi vzhůru?"),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "fit", "v kondici", exampleGerman = "Nach dem Urlaub fühle ich mich fit.", exampleCzech = "Po dovolené se cítím fit."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "erledigt", "vyřízený / hotový", exampleGerman = "Die Aufgabe ist erledigt.", exampleCzech = "Úkol je hotový."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "anstrengend", "náročný / vyčerpávající", exampleGerman = "Der Tag war sehr anstrengend.", exampleCzech = "Den byl velmi náročný."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "stressig", "stresující", exampleGerman = "Diese Woche ist ziemlich stressig.", exampleCzech = "Tento týden je dost stresující."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "entspannt", "uvolněný / klidný", exampleGerman = "Am Wochenende bin ich entspannt.", exampleCzech = "O víkendu jsem v klidu."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "angenehm", "příjemný", exampleGerman = "Das Wetter ist heute angenehm.", exampleCzech = "Počasí je dnes příjemné."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "unangenehm", "nepříjemný", exampleGerman = "Die Situation war unangenehm.", exampleCzech = "Situace byla nepříjemná."),
    spokenExtra(FrequencyWordKind.ADJECTIVE, "peinlich", "trapný", exampleGerman = "Das war mir wirklich peinlich.", exampleCzech = "To mi bylo opravdu trapné."),
    spokenExtra(FrequencyWordKind.ADVERB, "häufig", "často", exampleGerman = "Ich fahre häufig mit dem Zug.", exampleCzech = "Často jezdím vlakem."),
    spokenExtra(FrequencyWordKind.ADVERB, "gelegentlich", "občas", exampleGerman = "Ich koche gelegentlich zu Hause.", exampleCzech = "Občas vařím doma."),
    spokenExtra(FrequencyWordKind.ADVERB, "normalerweise", "normálně / obvykle", exampleGerman = "Normalerweise stehe ich um sieben auf.", exampleCzech = "Normálně vstávám v sedm."),
    spokenExtra(FrequencyWordKind.ADVERB, "üblicherweise", "zpravidla / obvykle", exampleGerman = "Üblicherweise bezahlen wir mit Karte.", exampleCzech = "Obvykle platíme kartou."),
    spokenExtra(FrequencyWordKind.ADVERB, "täglich", "denně", exampleGerman = "Ich übe täglich Deutsch.", exampleCzech = "Denně procvičuji němčinu."),
    spokenExtra(FrequencyWordKind.ADVERB, "wöchentlich", "týdně", exampleGerman = "Wir treffen uns wöchentlich.", exampleCzech = "Setkáváme se každý týden."),
    spokenExtra(FrequencyWordKind.ADVERB, "monatlich", "měsíčně", exampleGerman = "Ich bezahle die Miete monatlich.", exampleCzech = "Nájem platím měsíčně."),
    spokenExtra(FrequencyWordKind.ADVERB, "jährlich", "ročně", exampleGerman = "Wir fahren jährlich ans Meer.", exampleCzech = "Každý rok jezdíme k moři."),
    spokenExtra(FrequencyWordKind.ADVERB, "nachts", "v noci", exampleGerman = "Nachts ist es hier ruhig.", exampleCzech = "V noci je tady klid."),
    spokenExtra(FrequencyWordKind.ADVERB, "morgens", "ráno", exampleGerman = "Morgens trinke ich zuerst Kaffee.", exampleCzech = "Ráno nejdřív piju kávu."),
    spokenExtra(FrequencyWordKind.ADVERB, "abends", "večer", exampleGerman = "Abends lese ich gern ein Buch.", exampleCzech = "Večer si rád čtu knihu."),
    spokenExtra(FrequencyWordKind.ADVERB, "mittags", "v poledne", exampleGerman = "Mittags esse ich warm.", exampleCzech = "V poledne jím teplé jídlo."),
    spokenExtra(FrequencyWordKind.ADVERB, "zunächst", "nejprve", exampleGerman = "Zunächst müssen wir die Unterlagen prüfen.", exampleCzech = "Nejprve musíme zkontrolovat podklady."),
    spokenExtra(FrequencyWordKind.ADVERB, "mittlerweile", "mezitím / nyní už", exampleGerman = "Mittlerweile spreche ich viel besser Deutsch.", exampleCzech = "Nyní už mluvím mnohem lépe německy."),
    spokenExtra(FrequencyWordKind.ADVERB, "vorher", "předtím", exampleGerman = "Ruf mich bitte vorher an.", exampleCzech = "Zavolej mi prosím předem."),
    spokenExtra(FrequencyWordKind.ADVERB, "nachher", "později / potom", exampleGerman = "Ich rufe dich nachher an.", exampleCzech = "Zavolám ti později."),
    spokenExtra(FrequencyWordKind.ADVERB, "davor", "předtím / před tím", exampleGerman = "Davor müssen wir noch einkaufen.", exampleCzech = "Předtím ještě musíme nakoupit."),
    spokenExtra(FrequencyWordKind.ADVERB, "zuletzt", "naposledy", exampleGerman = "Zuletzt habe ich ihn am Montag gesehen.", exampleCzech = "Naposledy jsem ho viděl v pondělí."),
    spokenExtra(FrequencyWordKind.ADVERB, "anschließend", "následně / potom", exampleGerman = "Anschließend fahren wir nach Hause.", exampleCzech = "Potom pojedeme domů."),
    spokenExtra(FrequencyWordKind.ADVERB, "andernfalls", "jinak / v opačném případě", exampleGerman = "Beeil dich, andernfalls verpassen wir den Zug.", exampleCzech = "Pospěš si, jinak zmeškáme vlak."),
    spokenExtra(FrequencyWordKind.ADVERB, "jedenfalls", "každopádně", exampleGerman = "Jedenfalls müssen wir jetzt los.", exampleCzech = "Každopádně teď musíme vyrazit."),
    spokenExtra(FrequencyWordKind.ADVERB, "übrigens", "mimochodem", exampleGerman = "Übrigens, ich habe dich gestern angerufen.", exampleCzech = "Mimochodem, včera jsem ti volal."),
    spokenExtra(FrequencyWordKind.ADVERB, "immerhin", "alespoň", exampleGerman = "Immerhin haben wir es versucht.", exampleCzech = "Alespoň jsme to zkusili."),
    spokenExtra(FrequencyWordKind.ADVERB, "zumindest", "alespoň / přinejmenším", exampleGerman = "Zumindest weiß ich jetzt Bescheid.", exampleCzech = "Alespoň teď už vím, o co jde."),
    spokenExtra(FrequencyWordKind.ADVERB, "wenigstens", "aspoň", exampleGerman = "Wenigstens ist das Wetter schön.", exampleCzech = "Aspoň je hezké počasí."),
    spokenExtra(FrequencyWordKind.ADVERB, "eher", "spíše", exampleGerman = "Ich komme heute eher nicht.", exampleCzech = "Dnes spíše nepřijdu."),
    spokenExtra(FrequencyWordKind.ADVERB, "beinahe", "málem / téměř", exampleGerman = "Ich hätte beinahe den Bus verpasst.", exampleCzech = "Málem mi ujel autobus."),
    spokenExtra(FrequencyWordKind.ADVERB, "getrennt", "odděleně", exampleGerman = "Wir bezahlen heute getrennt.", exampleCzech = "Dnes zaplatíme odděleně."),
    spokenExtra(FrequencyWordKind.ADVERB, "drinnen", "uvnitř", exampleGerman = "Drinnen ist es viel wärmer.", exampleCzech = "Uvnitř je mnohem tepleji."),
    spokenExtra(FrequencyWordKind.ADVERB, "gegenüber", "naproti", exampleGerman = "Die Apotheke ist gleich gegenüber.", exampleCzech = "Lékárna je hned naproti."),
    spokenExtra(FrequencyWordKind.ADVERB, "dahinter", "za tím", exampleGerman = "Der Parkplatz ist gleich dahinter.", exampleCzech = "Parkoviště je hned za tím."),
    spokenExtra(FrequencyWordKind.ADVERB, "rechtzeitig", "včas", exampleGerman = "Wir sind rechtzeitig am Bahnhof.", exampleCzech = "Na nádraží jsme včas."),
    spokenExtra(FrequencyWordKind.FUNCTION, "nämlich", "totiž", exampleGerman = "Ich kann nicht kommen, ich bin nämlich krank.", exampleCzech = "Nemohu přijít, protože jsem nemocný."),
    spokenExtra(FrequencyWordKind.FUNCTION, "sowohl", "jak … tak / nejen … ale i", exampleGerman = "Ich spreche sowohl Deutsch als auch Englisch.", exampleCzech = "Mluvím německy i anglicky."),
    spokenExtra(FrequencyWordKind.FUNCTION, "weder", "ani", exampleGerman = "Ich trinke weder Kaffee noch Tee.", exampleCzech = "Nepiji ani kávu, ani čaj."),
    spokenExtra(FrequencyWordKind.FUNCTION, "entweder", "buď", exampleGerman = "Wir fahren entweder heute oder morgen.", exampleCzech = "Pojedeme buď dnes, nebo zítra."),
    spokenExtra(FrequencyWordKind.FUNCTION, "solange", "dokud / po dobu, co", exampleGerman = "Du kannst bleiben, solange du willst.", exampleCzech = "Můžeš zůstat, jak dlouho chceš."),
    spokenExtra(FrequencyWordKind.FUNCTION, "dadurch", "tím / díky tomu", exampleGerman = "Dadurch sparen wir viel Zeit.", exampleCzech = "Tím ušetříme spoustu času."),
    spokenExtra(FrequencyWordKind.FUNCTION, "wofür", "na co / k čemu", exampleGerman = "Wofür brauchst du das?", exampleCzech = "Na co to potřebuješ?"),
    spokenExtra(FrequencyWordKind.FUNCTION, "worüber", "o čem", exampleGerman = "Worüber sprecht ihr?", exampleCzech = "O čem mluvíte?"),
    spokenExtra(FrequencyWordKind.FUNCTION, "worauf", "na co", exampleGerman = "Worauf wartest du?", exampleCzech = "Na co čekáš?")
)

private fun spokenNounEntry(entry: VocabularyEntry): FrequencyVocabularyEntry {
    val legacyId = stableReviewId("vocabulary", entry.article, entry.word.lowercase())
    val plural = entry.word.lowercase() in setOf("eltern", "nebenkosten")
    return FrequencyVocabularyEntry(
        id = stableReviewId("vocabulary", legacyId),
        level = spokenLevelFor(entry.word),
        kind = FrequencyWordKind.NOUN,
        word = entry.word,
        czech = entry.czech,
        article = entry.article,
        exampleGerman = if (plural) "Hier sind ${entry.article} ${entry.word}." else "Hier ist ${entry.article} ${entry.word}.",
        exampleCzech = if (plural) "Tady jsou ${entry.czech}." else "Tady je ${entry.czech}."
    )
}

private fun verbDefinitionEntry(definition: VerbDefinition): FrequencyVocabularyEntry {
    val phrase = definition.phrases.firstOrNull()
    val germanExample = if (phrase == null) {
        "Ich möchte ${definition.infinitive}."
    } else {
        "${definition.subjects.first()} ${definition.forms.first()} ${phrase.german}."
    }
    val czechExample = if (phrase == null) {
        "Chci ${definition.czech}."
    } else {
        "${definition.czechSubjects.first()} ${definition.czechForms.first()} ${phrase.czech}."
    }
    return FrequencyVocabularyEntry(
        id = stableReviewId("frequency_vocabulary", FrequencyWordKind.VERB.name, definition.infinitive.lowercase()),
        level = spokenLevelFor(definition.infinitive),
        kind = FrequencyWordKind.VERB,
        word = definition.infinitive,
        czech = definition.czech,
        extraInfo = definition.forms.joinToString(" · "),
        exampleGerman = germanExample,
        exampleCzech = czechExample,
        listeningAnswer = definition.forms.first()
    )
}

private fun spokenExtraEntry(seed: SpokenExtraSeed): FrequencyVocabularyEntry {
    val level = spokenLevelFor(seed.word)
    val exampleGerman: String
    val exampleCzech: String
    when (seed.kind) {
        FrequencyWordKind.NOUN -> {
            val article = requireNotNull(seed.article)
            val plural = seed.word.lowercase() == "nebenkosten"
            exampleGerman = seed.exampleGerman.ifBlank {
                if (plural) "Hier sind $article ${seed.word}." else "Hier ist $article ${seed.word}."
            }
            exampleCzech = seed.exampleCzech.ifBlank {
                if (plural) "Tady jsou ${seed.czech}." else "Tady je ${seed.czech}."
            }
        }
        FrequencyWordKind.VERB -> {
            val reflexive = seed.word.startsWith("sich ")
            val infinitive = seed.word.removePrefix("sich ")
            exampleGerman = seed.exampleGerman.ifBlank {
                if (reflexive) "Ich möchte mich $infinitive." else "Ich möchte $infinitive."
            }
            exampleCzech = seed.exampleCzech.ifBlank { "Chci ${seed.czech}." }
        }
        FrequencyWordKind.ADJECTIVE -> {
            exampleGerman = seed.exampleGerman.ifBlank { "Das ist ${seed.word}." }
            exampleCzech = seed.exampleCzech.ifBlank { "To je ${seed.czech}." }
        }
        else -> {
            exampleGerman = seed.exampleGerman.ifBlank { "Ich mache das ${seed.word}." }
            exampleCzech = seed.exampleCzech.ifBlank { "Dělám to ${seed.czech}." }
        }
    }
    val id = if (seed.article != null) {
        val legacyId = stableReviewId("vocabulary", seed.article, seed.word.lowercase())
        stableReviewId("vocabulary", legacyId)
    } else {
        stableReviewId("frequency_vocabulary", seed.kind.name, seed.word.lowercase())
    }
    val listeningAnswer = if (seed.kind == FrequencyWordKind.VERB) {
        when (seed.word) {
            "einpacken", "auspacken" -> "packe"
            "aufmachen", "anmachen", "ausmachen" -> "Mach"
            "zumachen" -> "zu"
            "wegwerfen" -> "Wirf"
            "nachfragen" -> "frage"
            "nachschauen" -> "schaue"
            "nachsehen" -> "sehe"
            "ausschalten" -> "Schalte"
            "losfahren" -> "fahren"
            "wegbringen", "zurückbringen" -> "bringe"
            "verleihen" -> "verleiht"
            "aufteilen" -> "teilen"
            "verteilen" -> "verteilt"
            "aufheben" -> "Heb"
            "aufwärmen" -> "wärme"
            "sich weigern" -> "weigert"
            "sich bemühen" -> "bemühe"
            "sich wundern" -> "wundere"
            "sich hinlegen" -> "lege"
            "sich anziehen" -> "Zieh"
            else -> seed.word.removePrefix("sich ")
        }
    } else {
        seed.word
    }
    return FrequencyVocabularyEntry(
        id = id,
        level = level,
        kind = seed.kind,
        word = seed.word,
        czech = seed.czech,
        article = seed.article,
        exampleGerman = exampleGerman,
        exampleCzech = exampleCzech,
        listeningAnswer = listeningAnswer
    )
}

/**
 * Keep all curated entries and add exactly 1,000 new words in descending
 * everyday-corpus frequency order. The rank map and entry sources are prepared
 * together, so missing translations cannot silently enter the catalogue.
 */
internal fun expandFrequencyVocabularyCatalog(
    curated: List<FrequencyVocabularyEntry>
): List<FrequencyVocabularyEntry> {
    val known = curated.map { it.word.trim().lowercase() }.toMutableSet()
    val candidates = linkedMapOf<String, FrequencyVocabularyEntry>()

    verbDefinitions.forEach { definition ->
        val key = definition.infinitive.trim().lowercase()
        if (key !in known) candidates.putIfAbsent(key, verbDefinitionEntry(definition))
    }
    vocabularyCatalog.forEach { noun ->
        val key = noun.word.trim().lowercase()
        if (key !in known) candidates.putIfAbsent(key, spokenNounEntry(noun))
    }
    spokenExtraSeeds.forEach { seed ->
        val key = seed.word.trim().lowercase()
        if (key !in known) candidates.putIfAbsent(key, spokenExtraEntry(seed))
    }

    val additions = spokenFrequencyRanks.entries
        .sortedBy { it.value }
        .mapNotNull { (word, _) -> candidates[word] }
        .distinctBy { it.word.trim().lowercase() }
        .take(SPOKEN_FREQUENCY_ADDITION_COUNT)

    println("FREQUENCY_DEBUG curated=${curated.size} candidates=${candidates.size} rankRows=${spokenFrequencyRanks.size} matched=${additions.size}")
    return curated + additions
}
