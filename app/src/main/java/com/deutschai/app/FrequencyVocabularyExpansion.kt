package com.deutschai.app

/**
 * Expansion of the everyday vocabulary drill.
 *
 * Frequency-band reference: OpenSubtitles 2018 German list from
 * hermitdave/FrequencyWords. Rank bands are an orientation aid, not an official
 * Goethe CEFR classification. A1 <= 3,000; A2 <= 9,000; B1 <= 25,000;
 * remaining / unranked items are placed in B2 unless curated explicitly.
 */
private val spokenLevelOverrides: Map<String, String> = """
A1§hund
A1§katze
A1§vogel
A1§schwein
A1§kuh
A1§pferd
A1§wolf
A1§schlange
A1§fliege
A1§rose
A1§quelle
A1§strom
A1§ton
A1§boden
A1§feld
A1§grenze
A1§pass
A1§gebiet
A1§dorf
A1§kuchen
A1§pizza
A1§champagner
A1§rum
A1§frühstück
A1§mittagessen
A1§abendessen
A1§stück
A1§menge
A1§glas
A1§hammer
A1§mutter
A1§lager
A1§farbe
A1§leiter
A1§motor
A1§tür
A1§reifen
A1§schlüssel
A1§unfall
A1§richter
A1§polizist
A1§arzt
A1§lehrer
A1§professor
A1§fahrer
A1§soldat
A1§schüler
A1§labor
A1§kurs
A1§aufmerksamkeit
A1§beispiel
A1§erklärung
A1§übersetzung
A1§laut
A1§zeichen
A1§punkt
A1§gesundheit
A1§untersuchung
A1§operation
A1§knochen
A1§gehirn
A1§zunge
A1§brille
A1§klinik
A1§krankenwagen
A1§bewegung
A1§schlaf
A1§sucht
A1§anzug
A1§tasche
A1§stoff
A1§spitze
A1§ring
A1§hut
A1§spiegel
A1§holz
A1§gold
A1§schloss
A1§werk
A1§maschine
A1§presse
A1§fehler
A1§form
A1§energie
A1§leitung
A1§kontakt
A1§druck
A1§verlust
A1§vermögen
A1§rechnung
A1§auftrag
A1§angebot
A1§wohnung
A1§zimmer
A1§küche
A1§bad
A1§fenster
A1§tisch
A1§stuhl
A1§bett
A1§schrank
A1§wand
A1§dach
A1§treppe
A1§garten
A1§brot
A1§käse
A1§milch
A1§wasser
A1§kaffee
A1§tee
A1§fleisch
A1§fisch
A1§ei
A1§vater
A1§bruder
A1§schwester
A1§eltern
A1§sohn
A1§tochter
A1§kind
A1§baby
A1§großmutter
A1§großvater
A1§oma
A1§opa
A1§tante
A1§onkel
A1§cousin
A1§ehemann
A1§familie
A1§mann
A1§frau
A1§freund
A1§arbeit
A1§beruf
A1§büro
A1§firma
A1§chef
A1§termin
A1§aufgabe
A1§projekt
A1§computer
A1§telefon
A1§vertrag
A1§urlaub
A1§pause
A1§stadt
A1§straße
A1§platz
A1§flughafen
A1§bus
A1§zug
A1§auto
A1§taxi
A1§brücke
A1§weg
A1§karte
A1§geschäft
A1§krankenhaus
A1§polizei
A1§kleidung
A1§hemd
A1§hose
A1§kleid
A1§rock
A1§jacke
A1§mantel
A1§preis
A1§größe
A1§laden
A1§kopf
A1§auge
A1§ohr
A1§nase
A1§mund
A1§hals
A1§schulter
A1§arm
A1§hand
A1§finger
A1§bauch
A1§rücken
A1§bein
A1§knie
A1§fuß
A1§herz
A1§blut
A1§haut
A1§krankheit
A1§schmerz
A1§zeit
A1§uhr
A1§tag
A1§woche
A1§monat
A1§jahr
A1§morgen
A1§abend
A1§nacht
A1§mittag
A1§stunde
A1§minute
A1§sekunde
A1§geburtstag
A1§wochenende
A1§montag
A1§freitag
A1§samstag
A1§sonntag
A1§wetter
A1§sonne
A1§regen
A1§schnee
A1§wind
A1§himmel
A1§sommer
A1§winter
A1§baum
A1§gras
A1§wald
A1§berg
A1§see
A1§fluss
A1§meer
A1§strand
A1§insel
A1§erde
A1§luft
A1§feuer
A1§reise
A1§hotel
A1§koffer
A1§flug
A1§flugzeug
A1§adresse
A1§restaurant
A1§messer
A1§flasche
A1§wein
A1§bier
A1§schule
A1§klasse
A1§unterricht
A1§buch
A1§frage
A1§antwort
A1§sprache
A1§wort
A1§schwimmen
A1§laufen
A1§musik
A1§lied
A1§kino
A1§film
A1§theater
A1§spiel
A1§ball
A1§foto
A1§tanz
A1§gespräch
A1§nachricht
A1§brief
A1§handy
A1§anruf
A1§nummer
A1§name
A1§post
A1§einladung
A1§müll
A1§decke
A1§geld
A1§cent
A1§kosten
A1§bank
A1§internet
A1§programm
A1§kamera
A1§gefühl
A1§freude
A1§glück
A1§liebe
A1§angst
A1§sorge
A1§hoffnung
A1§mut
A1§geduld
A1§lust
A1§wut
A1§stolz
A1§stress
A1§ruhe
A1§freundschaft
A1§vertrauen
A1§respekt
A1§hilfe
A1§problem
A1§meinung
A1§idee
A1§entscheidung
A1§tier
A1§richtung
A1§seite
A1§ecke
A1§nähe
A1§plan
A1§information
A1§toilette
A1§sein
A1§haben
A1§wissen
A1§sprechen
A1§essen
A1§trinken
A1§gehen
A1§machen
A1§können
A1§wollen
A1§müssen
A1§kommen
A1§sehen
A1§nehmen
A1§geben
A1§fahren
A1§lesen
A1§schreiben
A1§arbeiten
A1§kaufen
A1§finden
A1§bleiben
A1§lernen
A1§schlafen
A1§hören
A1§denken
A1§spielen
A1§wohnen
A1§kochen
A1§brauchen
A1§dürfen
A1§sollen
A1§verstehen
A1§öffnen
A1§schließen
A1§waschen
A1§fragen
A1§antworten
A1§erklären
A1§zeigen
A1§erzählen
A1§bestätigen
A1§bezahlen
A1§verdienen
A1§warten
A1§fliegen
A1§landen
A1§starten
A1§abholen
A1§stellen
A1§legen
A1§treffen
A1§einladen
A1§anbieten
A1§unterschreiben
A1§erledigen
A1§kontrollieren
A1§glauben
A1§meinen
A1§behaupten
A1§erkennen
A1§fühlen
A1§riechen
A1§genießen
A1§bitten
A1§erlauben
A1§versprechen
A1§zählen
A1§bestellen
A1§entscheiden
A1§annehmen
A1§übernehmen
A1§erreichen
A1§erwarten
A1§hoffen
A1§bringen
A1§tragen
A1§verlieren
A1§gewinnen
A1§vergessen
A1§sitzen
A1§stehen
A1§liegen
A1§steigen
A1§folgen
A1§gefallen
A1§aufmachen
A1§sich entspannen
A1§sich anziehen
A1§praktisch
A1§dringend
A1§geöffnet
A1§geschlossen
A1§hungrig
A1§satt
A1§wach
A1§erledigt
A1§peinlich
A1§normalerweise
A1§täglich
A1§nachts
A1§morgens
A1§abends
A1§vorher
A1§nachher
A1§davor
A1§zuletzt
A1§jedenfalls
A1§übrigens
A1§immerhin
A1§zumindest
A1§wenigstens
A1§eher
A1§beinahe
A1§getrennt
A1§drinnen
A1§gegenüber
A1§dahinter
A1§rechtzeitig
A1§nämlich
A1§weder
A1§entweder
A1§solange
A1§dadurch
A1§wofür
A1§worüber
A1§worauf
A2§kaninchen
A2§schildkröte
A2§maus
A2§ratte
A2§schaf
A2§ziege
A2§esel
A2§ente
A2§gans
A2§huhn
A2§hahn
A2§truthahn
A2§taube
A2§adler
A2§fuchs
A2§bär
A2§hase
A2§eichhörnchen
A2§maulwurf
A2§löwe
A2§tiger
A2§elefant
A2§affe
A2§wal
A2§hai
A2§hummer
A2§frosch
A2§käfer
A2§spinne
A2§wurm
A2§floh
A2§blatt
A2§samen
A2§busch
A2§pflanze
A2§mais
A2§reis
A2§brunnen
A2§tal
A2§ebene
A2§gipfel
A2§höhle
A2§küste
A2§bucht
A2§ufer
A2§wüste
A2§dschungel
A2§bach
A2§kanal
A2§sumpf
A2§vulkan
A2§erdbeben
A2§felsen
A2§sand
A2§ozean
A2§region
A2§bezirk
A2§gemeinde
A2§orange
A2§feige
A2§wurst
A2§schinken
A2§speck
A2§leber
A2§braten
A2§mahlzeit
A2§soße
A2§honig
A2§schokolade
A2§keks
A2§torte
A2§brötchen
A2§sahne
A2§limonade
A2§cola
A2§whisky
A2§wodka
A2§dessert
A2§scheibe
A2§dose
A2§schachtel
A2§deckel
A2§schüssel
A2§schale
A2§becher
A2§tasse
A2§herd
A2§kühlschrank
A2§schwamm
A2§regal
A2§schreibtisch
A2§couch
A2§matratze
A2§vorhang
A2§klimaanlage
A2§kerze
A2§wecker
A2§schublade
A2§winkel
A2§schere
A2§werkstatt
A2§schlauch
A2§nagel
A2§feder
A2§eimer
A2§bock
A2§fahrzeug
A2§lieferwagen
A2§transporter
A2§motorrad
A2§fahrrad
A2§anhänger
A2§u-bahn
A2§weiche
A2§batterie
A2§sitz
A2§kofferraum
A2§rad
A2§kennzeichen
A2§autobahn
A2§sackgasse
A2§tankstelle
A2§parkplatz
A2§ingenieur
A2§techniker
A2§mechaniker
A2§verkäufer
A2§buchhalter
A2§polizistin
A2§ärztin
A2§zahnarzt
A2§chirurg
A2§krankenschwester
A2§lehrerin
A2§wissenschaftler
A2§maler
A2§koch
A2§köchin
A2§kellner
A2§kellnerin
A2§friseur
A2§schneider
A2§pilot
A2§journalist
A2§fotograf
A2§trainer
A2§gärtner
A2§fischer
A2§hausmeister
A2§geschäftsführer
A2§student
A2§schülerin
A2§mitarbeiter
A2§kollege
A2§kollegin
A2§universität
A2§institut
A2§ausbildung
A2§lektion
A2§abschluss
A2§bibliothek
A2§kapitel
A2§konzentration
A2§gedächtnis
A2§kenntnis
A2§fähigkeit
A2§übung
A2§akzent
A2§behandlung
A2§diagnose
A2§therapie
A2§rezept
A2§apotheke
A2§verband
A2§spritze
A2§nadel
A2§blutdruck
A2§puls
A2§fieber
A2§grippe
A2§husten
A2§kopfschmerzen
A2§ausschlag
A2§infektion
A2§verletzung
A2§wunde
A2§bruch
A2§blutung
A2§narbe
A2§lunge
A2§niere
A2§magen
A2§blase
A2§zahn
A2§rollstuhl
A2§praxis
A2§notaufnahme
A2§spende
A2§unterwäsche
A2§bh
A2§jeans
A2§pullover
A2§krawatte
A2§gürtel
A2§knopf
A2§kragen
A2§ärmel
A2§wolle
A2§leder
A2§stiefel
A2§handtasche
A2§beutel
A2§schmuck
A2§armband
A2§mütze
A2§helm
A2§schal
A2§seife
A2§parfüm
A2§lippenstift
A2§metall
A2§eisen
A2§stahl
A2§silber
A2§brett
A2§platte
A2§mauer
A2§profil
A2§rohr
A2§draht
A2§stab
A2§produktion
A2§produkt
A2§fabrik
A2§anlage
A2§roboter
A2§werkzeug
A2§qualität
A2§lehre
A2§oberfläche
A2§mangel
A2§muster
A2§lieferung
A2§kunde
A2§generator
A2§kabel
A2§schalter
A2§klemme
A2§spannung
A2§widerstand
A2§leistung
A2§frequenz
A2§temperatur
A2§treibstoff
A2§benzin
A2§wärme
A2§investition
A2§gewinn
A2§münze
A2§kreditkarte
A2§chip
A2§kasse
A2§quittung
A2§bestellung
A2§versicherung
A2§flur
A2§sofa
A2§lampe
A2§teppich
A2§balkon
A2§apfel
A2§suppe
A2§salat
A2§butter
A2§zucker
A2§salz
A2§öl
A2§saft
A2§cousine
A2§ehefrau
A2§nachbar
A2§nachbarin
A2§besprechung
A2§bildschirm
A2§e-mail
A2§gehalt
A2§bahnhof
A2§bahn
A2§ticket
A2§kreuzung
A2§zentrum
A2§supermarkt
A2§t-shirt
A2§schuh
A2§rucksack
A2§markt
A2§medikament
A2§kalender
A2§datum
A2§dienstag
A2§mittwoch
A2§donnerstag
A2§wolke
A2§frühling
A2§herbst
A2§blume
A2§ausgang
A2§eingang
A2§nachtisch
A2§trinkgeld
A2§löffel
A2§teller
A2§fach
A2§stift
A2§tafel
A2§prüfung
A2§note
A2§satz
A2§freizeit
A2§sport
A2§fußball
A2§tennis
A2§konzert
A2§mannschaft
A2§training
A2§hobby
A2§roman
A2§gitarre
A2§museum
A2§vorname
A2§nachname
A2§unterschrift
A2§paket
A2§gruß
A2§haushalt
A2§reinigung
A2§wäsche
A2§geschirr
A2§topf
A2§handtuch
A2§dusche
A2§kissen
A2§euro
A2§konto
A2§bargeld
A2§schein
A2§ausgabe
A2§miete
A2§kredit
A2§laptop
A2§passwort
A2§datei
A2§software
A2§trauer
A2§entfernung
A2§aufzug
A2§etage
A2§verspätung
A2§ankunft
A2§aufräumen
A2§putzen
A2§vorbereiten
A2§verschieben
A2§absagen
A2§sparen
A2§leihen
A2§ausgeben
A2§einsteigen
A2§aussteigen
A2§ankommen
A2§mieten
A2§umziehen
A2§teilnehmen
A2§empfehlen
A2§drucken
A2§löschen
A2§bemerken
A2§schmecken
A2§entdecken
A2§verbieten
A2§vermeiden
A2§planen
A2§organisieren
A2§entwickeln
A2§herstellen
A2§liefern
A2§prüfen
A2§messen
A2§vergleichen
A2§rechnen
A2§ablehnen
A2§vorschlagen
A2§zustimmen
A2§badezimmer
A2§nachbarschaft
A2§verabredung
A2§feierabend
A2§ausrede
A2§umzug
A2§kaution
A2§lautsprecher
A2§ausweis
A2§schicht
A2§strafzettel
A2§fahrstuhl
A2§mitbewohner
A2§notruf
A2§einpacken
A2§zumachen
A2§wegwerfen
A2§zurückgeben
A2§vorbeikommen
A2§nachsehen
A2§ausmachen
A2§einschalten
A2§ausschalten
A2§losgehen
A2§mitfahren
A2§zurückbringen
A2§ausleihen
A2§verleihen
A2§verteilen
A2§aufheben
A2§sich beschweren
A2§sich erholen
A2§sich bemühen
A2§sich wundern
A2§sich bedanken
A2§sich hinlegen
A2§sich umziehen
A2§bequem
A2§nützlich
A2§unnötig
A2§pünktlich
A2§höflich
A2§unhöflich
A2§geduldig
A2§ungeduldig
A2§beschädigt
A2§verfügbar
A2§besetzt
A2§privat
A2§öffentlich
A2§zuständig
A2§durstig
A2§fit
A2§anstrengend
A2§entspannt
A2§angenehm
A2§unangenehm
A2§häufig
A2§gelegentlich
A2§mittags
A2§zunächst
A2§mittlerweile
A2§sowohl
B1§hamster
B1§papagei
B1§goldfisch
B1§stier
B1§kalb
B1§hengst
B1§stute
B1§fohlen
B1§maultier
B1§rind
B1§küken
B1§pfau
B1§schwan
B1§spatz
B1§eule
B1§falke
B1§hirsch
B1§reh
B1§wildschwein
B1§igel
B1§dachs
B1§biber
B1§giraffe
B1§zebra
B1§gorilla
B1§pinguin
B1§delfin
B1§krabbe
B1§muschel
B1§schnecke
B1§kröte
B1§eidechse
B1§krokodil
B1§biene
B1§ameise
B1§schmetterling
B1§mücke
B1§motte
B1§grille
B1§laus
B1§raupe
B1§ast
B1§zweig
B1§wurzel
B1§blüte
B1§hecke
B1§moos
B1§pilz
B1§unkraut
B1§kraut
B1§veilchen
B1§lavendel
B1§kaktus
B1§palme
B1§eiche
B1§buche
B1§kiefer
B1§weide
B1§getreide
B1§weizen
B1§rasenmäher
B1§schaufel
B1§gewächshaus
B1§dünger
B1§grill
B1§terrasse
B1§teich
B1§gebirge
B1§hang
B1§schlucht
B1§kontinent
B1§urwald
B1§gletscher
B1§eisberg
B1§wasserfall
B1§moor
B1§krater
B1§lawine
B1§fels
B1§kies
B1§lehm
B1§gestein
B1§marmor
B1§klippe
B1§riff
B1§wiese
B1§acker
B1§lichtung
B1§provinz
B1§siedlung
B1§vorstadt
B1§stadtrand
B1§innenstadt
B1§erdbeere
B1§kirsche
B1§pflaume
B1§pfirsich
B1§melone
B1§ananas
B1§zitrone
B1§kokosnuss
B1§linse
B1§bohne
B1§spinat
B1§brokkoli
B1§kohl
B1§knoblauch
B1§kürbis
B1§paprika
B1§rübe
B1§karotte
B1§rindfleisch
B1§schweinefleisch
B1§hackfleisch
B1§salami
B1§schnitzel
B1§lachs
B1§thunfisch
B1§tintenfisch
B1§zutat
B1§sauce
B1§mayonnaise
B1§senf
B1§ketchup
B1§essig
B1§mehl
B1§teig
B1§marmelade
B1§sirup
B1§kakao
B1§bonbon
B1§gebäck
B1§lasagne
B1§pudding
B1§schlagsahne
B1§joghurt
B1§quark
B1§pfeffer
B1§zimt
B1§vanille
B1§mineralwasser
B1§eistee
B1§espresso
B1§cappuccino
B1§weißwein
B1§rotwein
B1§sekt
B1§vorspeise
B1§imbiss
B1§portion
B1§packung
B1§verpackung
B1§zange
B1§untertasse
B1§serviette
B1§timer
B1§toaster
B1§kaffeemaschine
B1§mixer
B1§mikrowelle
B1§mülleimer
B1§kommode
B1§kleiderschrank
B1§nachttisch
B1§sessel
B1§bettlaken
B1§kopfkissen
B1§rollo
B1§läufer
B1§parkett
B1§ventilator
B1§alarmanlage
B1§sprechanlage
B1§briefkasten
B1§klingel
B1§glühbirne
B1§kronleuchter
B1§waschmaschine
B1§staubsauger
B1§besen
B1§schraubenzieher
B1§säge
B1§kettensäge
B1§bohrer
B1§bleistift
B1§kreide
B1§zirkel
B1§kupplung
B1§schraube
B1§bolzen
B1§niete
B1§klebeband
B1§lack
B1§pinsel
B1§kran
B1§winde
B1§pumpe
B1§kombi
B1§geländewagen
B1§sportwagen
B1§cabrio
B1§moped
B1§busfahrer
B1§straßenbahn
B1§lokomotive
B1§waggon
B1§gleis
B1§bahnsteig
B1§getriebe
B1§bremse
B1§lenkrad
B1§achse
B1§kolben
B1§zylinder
B1§zündung
B1§kühler
B1§auspuff
B1§sensor
B1§windschutzscheibe
B1§scheinwerfer
B1§stoßstange
B1§motorhaube
B1§autoschlüssel
B1§nummernschild
B1§landstraße
B1§baustelle
B1§parkhaus
B1§stau
B1§panne
B1§abschleppwagen
B1§elektriker
B1§verkäuferin
B1§kaufmann
B1§rechtsanwalt
B1§richterin
B1§feuerwehrmann
B1§chirurgin
B1§pfleger
B1§pflegerin
B1§apotheker
B1§professorin
B1§forscher
B1§wissenschaftlerin
B1§designer
B1§architekt
B1§bauarbeiter
B1§maurer
B1§zimmermann
B1§bäcker
B1§journalistin
B1§fotografin
B1§übersetzer
B1§dolmetscher
B1§sozialarbeiter
B1§sozialarbeiterin
B1§psychologe
B1§psychologin
B1§therapeut
B1§therapeutin
B1§förster
B1§seemann
B1§postbote
B1§abteilungsleiter
B1§unternehmer
B1§praktikant
B1§praktikantin
B1§studentin
B1§rentner
B1§arbeitslose
B1§bewerber
B1§mitarbeiterin
B1§vorgesetzte
B1§hochschule
B1§fakultät
B1§vorlesung
B1§seminar
B1§praktikum
B1§gymnasium
B1§grundschule
B1§lehrbuch
B1§wörterbuch
B1§abitur
B1§zeugnis
B1§schulabschluss
B1§zitat
B1§motivation
B1§kompetenz
B1§aussprache
B1§betonung
B1§alphabet
B1§buchstabe
B1§komma
B1§fragezeichen
B1§narkose
B1§arznei
B1§schmerzmittel
B1§vitamin
B1§salbe
B1§creme
B1§pflaster
B1§infusion
B1§blutprobe
B1§blutbild
B1§herzfrequenz
B1§erkältung
B1§schnupfen
B1§migräne
B1§allergie
B1§entzündung
B1§verbrennung
B1§schwellung
B1§muskel
B1§sehne
B1§wirbelsäule
B1§wirbel
B1§darm
B1§milz
B1§luftröhre
B1§gaumen
B1§zahnbürste
B1§zahnpasta
B1§zahnspange
B1§füllung
B1§thermometer
B1§ekg
B1§krankenversicherung
B1§massage
B1§ernährung
B1§müdigkeit
B1§erschöpfung
B1§wohlbefinden
B1§depression
B1§panikattacke
B1§demenz
B1§blutgruppe
B1§befund
B1§überweisung
B1§wartezeit
B1§slip
B1§unterhose
B1§strumpfhose
B1§shorts
B1§badehose
B1§badeanzug
B1§bademantel
B1§pyjama
B1§nachthemd
B1§bluse
B1§regenmantel
B1§reißverschluss
B1§kapuze
B1§baumwolle
B1§seide
B1§leinen
B1§socke
B1§absatz
B1§schnürsenkel
B1§aktentasche
B1§aktenkoffer
B1§geldbeutel
B1§portemonnaie
B1§halskette
B1§ohrring
B1§brosche
B1§armbanduhr
B1§sonnenbrille
B1§regenschirm
B1§kappe
B1§handschuh
B1§kopftuch
B1§kamm
B1§shampoo
B1§sonnencreme
B1§rasierer
B1§nagellack
B1§puder
B1§foundation
B1§zahnstocher
B1§zahnseide
B1§aluminium
B1§kupfer
B1§bronze
B1§blei
B1§nickel
B1§titan
B1§gummi
B1§porzellan
B1§ziegel
B1§beton
B1§zement
B1§gips
B1§putz
B1§teer
B1§balken
B1§leim
B1§fassade
B1§riegel
B1§schwelle
B1§tapete
B1§wachs
B1§blech
B1§montage
B1§vorrichtung
B1§kaliber
B1§toleranz
B1§maß
B1§abweichung
B1§ausschuss
B1§defekt
B1§stillstand
B1§wartung
B1§reparatur
B1§kapazität
B1§prototyp
B1§ventil
B1§filter
B1§vakuum
B1§abfall
B1§lieferant
B1§kundin
B1§elektrizität
B1§kraftwerk
B1§spule
B1§magnet
B1§sicherung
B1§stecker
B1§anschluss
B1§feuchtigkeit
B1§akku
B1§diesel
B1§wasserstoff
B1§heizung
B1§brenner
B1§trinkwasser
B1§wasserhahn
B1§abfluss
B1§buchhaltung
B1§bilanz
B1§steuererklärung
B1§finanzamt
B1§finanzierung
B1§ertrag
B1§umsatz
B1§forderung
B1§zahlung
B1§anzahlung
B1§bankkonto
B1§depot
B1§aktie
B1§fonds
B1§währung
B1§kassiererin
B1§kassierer
B1§nachlass
B1§provision
B1§gebühr
B1§lebensversicherung
B1§versicherte
B1§banane
B1§kartoffel
B1§tomate
B1§gurke
B1§zwiebel
B1§drucker
B1§tastatur
B1§bewerbung
B1§haltestelle
B1§fahrkarte
B1§ampel
B1§geldbörse
B1§rabatt
B1§tablette
B1§feiertag
B1§rezeption
B1§reservierung
B1§tourist
B1§passagier
B1§speisekarte
B1§getränk
B1§gabel
B1§heft
B1§kugelschreiber
B1§grammatik
B1§formular
B1§lappen
B1§pfanne
B1§einkommen
B1§webseite
B1§ordner
B1§app
B1§update
B1§auskunft
B1§wegweiser
B1§abfahrt
B1§notausgang
B1§vereinbaren
B1§umsteigen
B1§abfahren
B1§vermieten
B1§speichern
B1§ausfüllen
B1§eintragen
B1§reservieren
B1§buchen
B1§beitragen
B1§alltag
B1§mülltonne
B1§einkauf
B1§vermieter
B1§fernbedienung
B1§wlan
B1§kopfhörer
B1§reisepass
B1§visum
B1§bürgersteig
B1§mitbewohnerin
B1§mitteilung
B1§gutschein
B1§anprobieren
B1§auspacken
B1§aufdrehen
B1§nachfragen
B1§nachschauen
B1§anmachen
B1§aufladen
B1§losfahren
B1§abbiegen
B1§wegbringen
B1§anmelden
B1§nachweisen
B1§umtauschen
B1§zurückschicken
B1§aufteilen
B1§weglegen
B1§aufwärmen
B1§abkühlen
B1§sich verspäten
B1§sich verabreden
B1§sich erkundigen
B1§sich weigern
B1§unbequem
B1§zuverlässig
B1§unzuverlässig
B1§unfreundlich
B1§unvorsichtig
B1§kostenlos
B1§ausverkauft
B1§schriftlich
B1§telefonisch
B1§erreichbar
B1§stressig
B1§üblicherweise
B1§wöchentlich
B1§monatlich
B1§jährlich
B1§anschließend
B1§andernfalls
""".lineSequence()
    .mapNotNull { row ->
        val fields = row.split('§', limit = 2)
        if (fields.size == 2) fields[1].lowercase() to fields[0] else null
    }
    .toMap()

private fun spokenLevelFor(word: String): String =
    spokenLevelOverrides[word.lowercase().removePrefix("sich ")] ?: "B2"

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
    return FrequencyVocabularyEntry(
        id = id,
        level = level,
        kind = seed.kind,
        word = seed.word,
        czech = seed.czech,
        article = seed.article,
        exampleGerman = exampleGerman,
        exampleCzech = exampleCzech,
        listeningAnswer = if (seed.kind == FrequencyWordKind.VERB) seed.word.removePrefix("sich ") else seed.word
    )
}

/** Merge curated items with existing vocabulary and verb content; keep one lemma per word. */
internal fun expandFrequencyVocabularyCatalog(
    curated: List<FrequencyVocabularyEntry>
): List<FrequencyVocabularyEntry> {
    val curatedUnique = curated.distinctBy { it.word.trim().lowercase() }
    val known = curatedUnique.map { it.word.trim().lowercase() }.toMutableSet()
    val expanded = buildList {
        addAll(curatedUnique)
        verbDefinitions.forEach { definition ->
            if (known.add(definition.infinitive.trim().lowercase())) add(verbDefinitionEntry(definition))
        }
        vocabularyCatalog.forEach { noun ->
            if (known.add(noun.word.trim().lowercase())) add(spokenNounEntry(noun))
        }
        spokenExtraSeeds.forEach { seed ->
            if (known.add(seed.word.trim().lowercase())) add(spokenExtraEntry(seed))
        }
    }.distinctBy { it.word.trim().lowercase() }

    check(expanded.size == 3000) {
        "Everyday frequency drill must contain exactly 3,000 unique words, found ${expanded.size}."
    }
    return expanded
}
