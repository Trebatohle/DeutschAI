package com.deutschai.app

internal data class VocabularyEntry(
    val id: String,
    val article: String,
    val word: String,
    val czech: String
)

private fun parseVocabularyRows(raw: String): List<VocabularyEntry> =
    raw.lineSequence()
        .map { it.trim() }
        .filter { it.isNotEmpty() && !it.startsWith("#") }
        .map { row ->
            val fields = row.split("§", limit = 3)
            require(fields.size == 3) { "Invalid vocabulary row: $row" }
            val article = fields[0].trim()
            val word = fields[1].trim()
            val czech = fields[2].trim()
            require(article in setOf("der", "die", "das")) { "Invalid article in row: $row" }
            require(word.isNotBlank() && czech.isNotBlank()) { "Incomplete vocabulary row: $row" }
            VocabularyEntry(
                id = stableReviewId("vocabulary", article, word.lowercase()),
                article = article,
                word = word,
                czech = czech
            )
        }
        .toList()

private val vocabularyRowsPart1 = """
# Tiere und Haustiere
der§Hund§pes
die§Katze§kočka
das§Kaninchen§králík
der§Hamster§křeček
das§Meerschweinchen§morče
der§Papagei§papoušek
der§Wellensittich§andulka
die§Schildkröte§želva
der§Goldfisch§zlatá rybka
der§Aquariumfisch§akvarijní ryba
der§Vogel§pták
die§Maus§myš
die§Ratte§krysa
das§Schwein§prase
die§Kuh§kráva
der§Stier§býk
das§Kalb§tele
das§Schaf§ovce
der§Widder§beran
die§Ziege§koza
der§Ziegenbock§kozel
das§Pferd§kůň
der§Hengst§hřebec
die§Stute§klisna
das§Fohlen§hříbě
der§Esel§osel
das§Maultier§mula
das§Rind§dobytek
das§Geflügel§drůbež
die§Ente§kachna
die§Gans§husa
das§Huhn§kuře
der§Hahn§kohout
das§Küken§kuřátko
der§Truthahn§krocan
der§Pfau§páv
der§Schwan§labuť
die§Taube§holub
der§Spatz§vrabec
die§Amsel§kos
die§Drossel§drozd
der§Specht§datel
die§Eule§sova
der§Adler§orel
der§Falke§sokol
der§Fuchs§liška
der§Wolf§vlk
der§Bär§medvěd
der§Hirsch§jelen
das§Reh§srnec
das§Wildschwein§divoké prase
der§Hase§zajíc
das§Eichhörnchen§veverka
der§Igel§ježek
der§Maulwurf§krtek
der§Dachs§jezevec
der§Otter§vydra
der§Biber§bobr
der§Luchs§rys
der§Löwe§lev
der§Tiger§tygr
der§Elefant§slon
die§Giraffe§žirafa
das§Zebra§zebra
das§Känguru§klokan
der§Affe§opice
der§Gorilla§gorila
der§Pinguin§tučňák
der§Delfin§delfín
der§Wal§velryba
der§Hai§žralok
der§Oktopus§chobotnice
die§Qualle§medúza
die§Krabbe§krab
der§Hummer§humr
die§Muschel§mušle
die§Schnecke§hlemýžď
der§Frosch§žába
die§Kröte§ropucha
die§Eidechse§ještěrka
die§Schlange§had
das§Krokodil§krokodýl
das§Chamäleon§chameleon
die§Biene§včela
die§Wespe§vosa
die§Hornisse§sršeň
die§Ameise§mravenec
der§Schmetterling§motýl
die§Mücke§komár
die§Fliege§moucha
der§Käfer§brouk
der§Marienkäfer§slunéčko
die§Libelle§vážka
die§Spinne§pavouk
der§Wurm§červ
der§Regenwurm§žížala
die§Motte§můra
die§Heuschrecke§kobylka
die§Grille§cvrček
die§Zecke§klíště
der§Floh§blecha
die§Laus§veš
die§Raupe§housenka

# Pflanzen und Garten
der§Baumstamm§kmen stromu
der§Ast§větev
der§Zweig§větvička
die§Wurzel§kořen
das§Blatt§list
die§Blüte§květ
die§Knospe§poupě
die§Rinde§kůra
der§Samen§semeno
der§Setzling§sazenice
der§Sämling§semenáček
der§Strauch§keř
die§Hecke§živý plot
der§Busch§křoví
das§Moos§mech
der§Pilz§houba
der§Steinpilz§hřib
der§Champignon§žampion
die§Pflanze§rostlina
das§Unkraut§plevel
das§Kraut§bylina
das§Gewürz§koření
die§Rose§růže
die§Tulpe§tulipán
die§Lilie§lilie
die§Nelke§karafiát
die§Orchidee§orchidej
die§Narzisse§narcis
die§Sonnenblume§slunečnice
die§Margerite§kopretina
das§Veilchen§fialka
die§Geranie§pelargonie
die§Begonie§begonie
die§Petunie§petúnie
der§Lavendel§levandule
die§Kamille§heřmánek
die§Minze§máta
der§Rosmarin§rozmarýn
der§Thymian§tymián
die§Petersilie§petržel
der§Dill§kopr
der§Schnittlauch§pažitka
der§Basilikum§bazalka
die§Brennnessel§kopřiva
der§Löwenzahn§pampeliška
das§Gänseblümchen§sedmikráska
das§Schneeglöckchen§sněženka
die§Hyazinthe§hyacint
der§Krokus§krokus
der§Kaktus§kaktus
die§Palme§palma
der§Farn§kapradina
die§Eiche§dub
die§Buche§buk
die§Birke§bříza
die§Linde§lípa
die§Fichte§smrk
die§Tanne§jedle
die§Kiefer§borovice
die§Lärche§modřín
die§Esche§jasan
der§Ahorn§javor
die§Ulme§jilm
die§Pappel§topol
die§Weide§vrba
der§Kastanienbaum§kaštanovník
der§Kirschbaum§třešeň
der§Apfelbaum§jabloň
der§Birnbaum§hrušeň
der§Pflaumenbaum§švestka
der§Pfirsichbaum§broskvoň
der§Walnussbaum§ořešák
die§Weinrebe§vinná réva
der§Weinstock§réva
das§Getreide§obilí
der§Weizen§pšenice
der§Roggen§žito
die§Gerste§ječmen
der§Hafer§oves
der§Mais§kukuřice
der§Reis§rýže
die§Rapsblüte§řepkové květy
die§Sonnenblume§slunečnice
der§Gartenzaun§zahradní plot
das§Gartenbeet§záhon
der§Blumentopf§květináč
die§Blumenerde§zemina na květiny
die§Gießkanne§konvička
der§Gartenschlauch§zahradní hadice
der§Rasenmäher§sekačka na trávu
die§Gartenschere§zahradnické nůžky
der§Spaten§rýč
die§Schaufel§lopata
der§Rechen§hrábě
die§Harke§hrábě
die§Gartenhacke§motyčka
die§Schubkarre§kolečko
das§Gewächshaus§skleník
der§Kompost§kompost
der§Dünger§hnojivo
das§Saatgut§osivo
die§Bewässerung§zavlažování
der§Blumenkasten§truhlík
die§Gartenbank§zahradní lavička
der§Gartentisch§zahradní stůl
der§Gartenstuhl§zahradní židle
der§Grill§gril
die§Gartenlaube§zahradní altán
die§Terrasse§terasa
der§Teich§rybník
der§Brunnen§studna
die§Gartenmauer§zahradní zeď

# Landschaft und Geografie
das§Tal§údolí
die§Ebene§rovina
das§Gebirge§pohoří
der§Gipfel§vrchol
der§Hang§svah
der§Abhang§sráz
die§Schlucht§soutěska
die§Höhle§jeskyně
die§Küste§pobřeží
die§Bucht§zátoka
das§Ufer§břeh
die§Halbinsel§poloostrov
der§Kontinent§světadíl
die§Wüste§poušť
die§Steppe§step
die§Savanne§savana
der§Dschungel§džungle
der§Urwald§prales
der§Gletscher§ledovec
der§Eisberg§ledovec
die§Quelle§pramen
der§Bach§potok
der§Strom§veletok
der§Kanal§kanál
der§Wasserfall§vodopád
der§Sumpf§bažina
das§Moor§rašeliniště
der§Sumpf§močál
der§Vulkan§sopka
der§Krater§kráter
das§Erdbeben§zemětřesení
die§Lawine§lavina
der§Fels§skála
der§Felsen§skalní útvar
der§Geröll§suť
der§Sand§písek
der§Kies§štěrk
der§Lehm§jíl
der§Ton§hlína
das§Gestein§hornina
der§Granit§žula
der§Marmor§mramor
der§Kalkstein§vápenec
die§Klippe§útes
das§Riff§útes
die§Koralle§korál
der§Ozean§oceán
das§Polarmeer§polární moře
die§Lagune§laguna
die§Quelle§pramen
die§Wasserader§vodní žíla
der§Boden§půda
die§Wiese§louka
die§Weide§pastvina
das§Feld§pole
der§Acker§orná půda
die§Lichtung§mýtina
die§Waldlichtung§lesní mýtina
der§Waldweg§lesní cesta
die§Landzunge§mys
die§Grenze§hranice
das§Grenzgebiet§pohraniční oblast
der§Pass§průsmyk
die§Passhöhe§sedlo
die§Region§region
das§Gebiet§oblast
der§Bezirk§okres
die§Provinz§provincie
das§Bundesland§spolková země
der§Landkreis§správní okres
die§Gemeinde§obec
das§Dorf§vesnice
die§Siedlung§osada
die§Vorstadt§předměstí
der§Stadtrand§okraj města
das§Stadtzentrum§centrum města
die§Innenstadt§vnitřní město
das§Wohngebiet§obytná oblast
das§Industriegebiet§průmyslová zóna
das§Gewerbegebiet§komerční zóna
der§Nationalpark§národní park
das§Naturschutzgebiet§chráněná krajinná oblast
das§Klimaschutzgebiet§oblast ochrany klimatu

# Lebensmittel und Zutaten
die§Erdbeere§jahoda
die§Himbeere§malina
die§Brombeere§ostružina
die§Heidelbeere§borůvka
die§Blaubeere§borůvka
die§Johannisbeere§rybíz
die§Stachelbeere§angrešt
die§Kirsche§třešeň
die§Sauerkirsche§višeň
die§Pflaume§švestka
die§Zwetschge§švestka
die§Aprikose§meruňka
der§Pfirsich§broskev
die§Nektarine§nektarinka
die§Weintraube§vinná réva
die§Rosine§rozinka
die§Melone§meloun
die§Wassermelone§vodní meloun
die§Honigmelone§cukrový meloun
die§Ananas§ananas
die§Mango§mango
die§Kiwi§kiwi
die§Papaya§papája
die§Avocado§avokádo
die§Zitrone§citron
die§Limette§limetka
die§Orange§pomeranč
die§Mandarine§mandarinka
die§Grapefruit§grapefruit
die§Feige§fík
die§Dattel§datle
die§Granatapfel§granátové jablko
die§Kokosnuss§kokosový ořech
die§Mandel§mandle
die§Haselnuss§lískový ořech
die§Walnuss§vlašský ořech
die§Erdnuss§arašíd
die§Pistazie§pistácie
die§Cashewnuss§kešu ořech
die§Kürbiskern§dýňové semínko
die§Sonnenblumenkerne§slunečnicová semínka
die§Linse§čočka
die§Bohne§fazole
die§Erbse§hrášek
die§Kichererbse§cizrna
die§Sojabohne§sójový bob
der§Spinat§špenát
der§Brokkoli§brokolice
der§Blumenkohl§květák
der§Rosenkohl§růžičková kapusta
der§Kohl§zelí
der§Rotkohl§červené zelí
der§Weißkohl§bílé zelí
der§Wirsing§kapusta
der§Kopfsalat§hlávkový salát
der§Rucola§rukola
der§Lauch§pórek
der§Porree§pórek
der§Sellerie§celer
der§Knoblauch§česnek
der§Ingwer§zázvor
der§Meerrettich§křen
der§Rettich§ředkev
das§Radieschen§ředkvička
der§Kürbis§dýně
die§Zucchini§cuketa
die§Aubergine§lilek
die§Paprika§paprika
die§Chilischote§chilli paprička
die§Rote Bete§červená řepa
die§Rübe§řepa
die§Karotte§mrkev
die§Möhre§mrkev
die§Pastinake§pastinák
die§Petersilienwurzel§kořenová petržel
die§Süßkartoffel§batát
die§Avocadofrucht§plod avokáda
das§Rindfleisch§hovězí maso
das§Schweinefleisch§vepřové maso
das§Hühnerfleisch§kuřecí maso
das§Lammfleisch§jehněčí maso
das§Kalbfleisch§telecí maso
das§Hackfleisch§mleté maso
die§Wurst§uzenina
die§Salami§salám
der§Schinken§šunka
der§Speck§slanina
die§Leber§játra
das§Filet§filet
die§Bratwurst§klobása
das§Schnitzel§řízek
der§Braten§pečeně
die§Frikadelle§karbanátek
das§Gulasch§guláš
die§Frikadelle§karbanátek
der§Lachs§losos
die§Forelle§pstruh
der§Karpfen§kapr
der§Thunfisch§tuňák
die§Sardine§sardinka
die§Makrele§makrela
die§Garnele§kreveta
die§Auster§ústřice
der§Tintenfisch§sépie
die§Fischfrikadelle§rybí karbanátek
die§Mahlzeit§jídlo
die§Zutat§přísada
die§Beilage§příloha
die§Soße§omáčka
die§Sauce§omáčka
die§Mayonnaise§majonéza
der§Senf§hořčice
der§Ketchup§kečup
der§Essig§ocet
das§Mehl§mouka
der§Grieß§krupice
der§Teig§těsto
die§Hefe§droždí
das§Backpulver§prášek do pečiva
der§Honig§med
die§Marmelade§marmeláda
die§Konfitüre§džem
der§Sirup§sirup
der§Kakao§kakao
die§Schokolade§čokoláda
die§Praline§pralinka
das§Bonbon§bonbon
der§Keks§sušenka
der§Kuchen§koláč
die§Torte§dort
das§Gebäck§pečivo
das§Croissant§croissant
das§Brötchen§houska
das§Baguette§bageta
das§Vollkornbrot§celozrnný chléb
das§Roggenbrot§žitný chléb
das§Toastbrot§toastový chléb
der§Zwieback§suchar
die§Pizza§pizza
die§Lasagne§lasagne
die§Marmeladenstulle§chléb s marmeládou
der§Pudding§pudink
die§Sahne§smetana
die§Schlagsahne§šlehačka
der§Joghurt§jogurt
der§Quark§tvaroh
der§Frischkäse§čerstvý sýr
der§Hartkäse§tvrdý sýr
der§Parmesan§parmezán
der§Mozzarella§mozzarella
die§Buttermilch§podmáslí
der§Kefir§kefír
die§Margarine§margarín
das§Eigelb§vaječný žloutek
das§Eiweiß§vaječný bílek
das§Gewürz§koření
der§Pfeffer§pepř
das§Paprikapulver§mletá paprika
der§Zimt§skořice
die§Muskatnuss§muškátový oříšek
der§Kreuzkümmel§římský kmín
der§Koriander§koriandr
der§Oregano§oregano
der§Majoran§majoránka
der§Lorbeer§bobkový list
der§Safran§šafrán
der§Kümmel§kmín
die§Vanille§vanilka
der§Vanillezucker§vanilkový cukr
der§Puderzucker§moučkový cukr
der§Braunzucker§hnědý cukr
das§Mineralwasser§minerální voda
die§Limonade§limonáda
die§Cola§cola
die§Brause§limonáda
der§Eistee§ledový čaj
der§Espresso§espresso
der§Cappuccino§cappuccino
der§Latte macchiato§latte macchiato
der§Kräutertee§bylinný čaj
der§Früchtetee§ovocný čaj
der§Weißwein§bílé víno
der§Rotwein§červené víno
der§Sekt§sekt
der§Champagner§šampaňské
der§Likör§likér
der§Whisky§whisky
der§Rum§rum
der§Wodka§vodka
das§Getreideprodukt§obilný výrobek
der§Speiseplan§jídelníček
das§Frühstück§snídaně
das§Mittagessen§oběd
das§Abendessen§večeře
die§Zwischenmahlzeit§svačina
die§Nachspeise§dezert
die§Vorspeise§předkrm
das§Dessert§dezert
der§Imbiss§svačina
die§Portion§porce
die§Scheibe§plátek
die§Scheibe Brot§plátek chleba
das§Stück§kus
die§Menge§množství
die§Packung§balení
die§Dose§plechovka
das§Glas§sklenice
die§Schachtel§krabička
die§Verpackung§obal
die§Zutat§přísada

# Küche und Haushaltsgeräte
der§Kochtopf§hrnec
die§Bratpfanne§pánev
der§Deckel§poklička
das§Schneidebrett§prkénko
das§Küchenmesser§kuchyňský nůž
das§Brotmesser§nůž na chléb
der§Schäler§škrabka
die§Reibe§struhadlo
der§Schneebesen§metla
der§Rührlöffel§vařečka
der§Kochlöffel§vařečka
die§Kelle§naběračka
der§Pfannenwender§obracečka
die§Zange§kleště
die§Küchenzange§kuchyňské kleště
das§Sieb§síto
das§Abtropfsieb§cedník
die§Schüssel§mísa
die§Rührschüssel§mísa na míchání
die§Schale§miska
der§Becher§hrnek
die§Tasse§šálek
die§Untertasse§podšálek
der§Kaffeelöffel§kávová lžička
der§Teelöffel§čajová lžička
der§Esslöffel§polévková lžíce
die§Serviette§ubrousek
die§Tischdecke§ubrus
der§Untersetzer§podložka
der§Flaschenöffner§otvírák na láhve
der§Dosenöffner§otvírák na konzervy
der§Korkenzieher§vývrtka
der§Trichter§trychtýř
der§Messbecher§odměrka
die§Küchenwaage§kuchyňská váha
der§Timer§časovač
der§Toaster§topinkovač
der§Wasserkocher§rychlovarná konvice
die§Kaffeemaschine§kávovar
die§Espressomaschine§kávovar na espresso
der§Mixer§mixér
der§Pürierstab§tyčový mixér
die§Küchenmaschine§kuchyňský robot
der§Backofen§trouba
die§Mikrowelle§mikrovlnná trouba
der§Herd§sporák
die§Kochplatte§varná plotýnka
der§Kühlschrank§lednice
das§Gefrierfach§mrazicí přihrádka
der§Gefrierschrank§mrazák
die§Spülmaschine§myčka nádobí
die§Dunstabzugshaube§digestoř
der§Reiskocher§rýžovar
der§Slow Cooker§pomalý hrnec
die§Fritteuse§fritéza
die§Heißluftfritteuse§horkovzdušná fritéza
der§Sandwichmaker§sendvičovač
der§Waffelautomat§vaflovač
das§Raclette§raclette gril
der§Grillrost§grilovací rošt
die§Backform§forma na pečení
das§Backblech§plech na pečení
die§Springform§dortová forma
der§Kuchenheber§lopatka na dort
die§Teigrolle§váleček na těsto
das§Nudelholz§váleček na těsto
die§Küchenreibe§kuchyňské struhadlo
die§Zitronenpresse§lis na citron
der§Entsafter§odšťavňovač
der§Dampfgarer§parní hrnec
der§Dampfkochtopf§tlakový hrnec
die§Thermoskanne§termoska
die§Trinkflasche§láhev na pití
der§Wasserfilter§vodní filtr
der§Mülleimer§odpadkový koš
der§Bioabfallbehälter§nádoba na bioodpad
die§Frischhaltefolie§potravinová fólie
die§Alufolie§alobal
das§Backpapier§pečicí papír
der§Gefrierbeutel§sáček do mrazáku
der§Müllbeutel§pytel na odpadky
die§Küchenrolle§kuchyňská utěrka
das§Spültuch§utěrka na nádobí
der§Schwamm§houbička
die§Spülbürste§kartáč na nádobí
das§Spülbecken§dřez
die§Arbeitsplatte§pracovní deska
der§Küchenschrank§kuchyňská skříňka
das§Regal§police
die§Vorratsdose§dóza na potraviny
das§Gewürzregal§kořenka

# Haushalt, Möbel und Wohntechnik
die§Kommode§komoda
das§Sideboard§komoda
das§Bücherregal§knihovna
das§Schuhregal§botník
der§Kleiderschrank§šatní skříň
der§Einbauschrank§vestavěná skříň
der§Nachttisch§noční stolek
der§Couchtisch§konferenční stolek
der§Esstisch§jídelní stůl
der§Schreibtisch§psací stůl
der§Beistelltisch§odkládací stolek
der§Klappstuhl§skládací židle
der§Bürostuhl§kancelářská židle
der§Sessel§křeslo
die§Couch§pohovka
die§Matratze§matrace
der§Lattenrost§rošt postele
die§Bettdecke§peřina
das§Bettlaken§prostěradlo
der§Kissenbezug§povlak na polštář
das§Kopfkissen§polštář
die§Tagesdecke§přehoz na postel
der§Vorhang§závěs
die§Gardine§záclona
die§Jalousie§žaluzie
das§Rollo§roleta
der§Fensterrahmen§okenní rám
die§Fensterbank§parapet
die§Türklinke§klika
das§Türschloss§zámek dveří
der§Türrahmen§zárubeň
der§Türspion§kukátko
die§Fußmatte§rohožka
der§Läufer§běhoun
das§Parkett§parkety
der§Laminatboden§laminátová podlaha
der§Fliesenboden§dlážděná podlaha
die§Fußbodenheizung§podlahové topení
der§Heizkörper§radiátor
der§Thermostat§termostat
die§Klimaanlage§klimatizace
der§Ventilator§ventilátor
der§Luftreiniger§čistička vzduchu
der§Luftbefeuchter§zvlhčovač vzduchu
der§Luftentfeuchter§odvlhčovač vzduchu
der§Rauchmelder§detektor kouře
der§Kohlenmonoxidmelder§detektor oxidu uhelnatého
die§Alarmanlage§zabezpečovací systém
die§Videoüberwachung§kamerový dohled
das§Türschloss§zámek dveří
die§Sprechanlage§domovní telefon
die§Gegensprechanlage§domovní interkom
der§Briefkasten§poštovní schránka
die§Hausnummer§číslo domu
das§Klingelschild§štítek na zvonku
die§Klingel§zvonek
der§Schlüsselbund§svazek klíčů
der§Ersatzschlüssel§náhradní klíč
die§Steckdose§zásuvka
der§Lichtschalter§vypínač
die§Glühbirne§žárovka
die§LED-Lampe§LED žárovka
die§Deckenlampe§stropní světlo
die§Nachttischlampe§noční lampička
die§Stehlampe§stojací lampa
die§Wandleuchte§nástěnné světlo
der§Kronleuchter§lustr
die§Kerze§svíčka
der§Kerzenständer§svícen
der§Spiegelrahmen§rám zrcadla
die§Wanduhr§nástěnné hodiny
der§Wecker§budík
die§Badezimmerwaage§osobní váha
der§Föhn§fén
der§Haartrockner§vysoušeč vlasů
die§Waschmaschine§pračka
der§Wäschetrockner§sušička prádla
der§Wäscheständer§sušák na prádlo
das§Bügeleisen§žehlička
das§Bügelbrett§žehlicí prkno
der§Nähkorb§šicí košík
der§Wäschekorb§koš na prádlo
das§Bügelbrett§žehlicí prkno
die§Nähmaschine§šicí stroj
der§Staubsauger§vysavač
der§Saugroboter§robotický vysavač
der§Dampfreiniger§parní čistič
der§Fensterputzer§čistič oken
der§Wischmopp§mop
der§Besen§koště
die§Kehrschaufel§lopatka na smetí
der§Handfeger§smetáček
der§Putzeimer§úklidový kbelík
das§Reinigungsmittel§čisticí prostředek
das§Waschmittel§prací prostředek
der§Weichspüler§aviváž
das§Spülmittel§prostředek na nádobí
der§Fleckentferner§odstraňovač skvrn
der§Möbelgriff§nábytková úchytka
der§Türstopper§zarážka dveří
der§Kleiderbügel§ramínko
der§Wäscheklammer§kolíček na prádlo
der§Wäschekorbdeckel§víko koše na prádlo
die§Aufbewahrungsbox§úložný box
die§Schublade§zásuvka
der§Schubladengriff§úchytka zásuvky
das§Scharnier§pant
der§Türstopper§zarážka dveří

# Werkzeuge und Werkstatt
der§Hammer§kladivo
der§Schraubenzieher§šroubovák
der§Schraubendreher§šroubovák
der§Inbusschlüssel§imbusový klíč
der§Maulschlüssel§plochý klíč
der§Ringschlüssel§očkový klíč
der§Steckschlüssel§nástrčný klíč
der§Drehmomentschlüssel§momentový klíč
die§Ratsche§ráčna
die§Zange§kleště
die§Kombizange§kombinačky
die§Spitzzange§dlouhé kleště
der§Seitenschneider§štípací kleště
der§Bolzenschneider§štípač šroubů
die§Rohrzange§hasák
der§Schraubstock§svěrák
die§Feile§pilník
die§Raspel§rašple
die§Säge§pila
die§Handsäge§ruční pila
die§Bügelsäge§pila na kov
die§Stichsäge§přímočará pila
die§Kreissäge§kotoučová pila
die§Kettensäge§motorová pila
die§Gehrungssäge§pokosová pila
der§Bohrer§vrták
der§Metallbohrer§vrták na kov
der§Holzbohrer§vrták do dřeva
der§Bohrhammer§vrtací kladivo
die§Bohrmaschine§vrtačka
der§Akkuschrauber§aku šroubovák
der§Schlagschrauber§rázový utahovák
der§Winkelschleifer§úhlová bruska
die§Schleifmaschine§bruska
das§Schleifpapier§brusný papír
der§Schleifstein§brusný kámen
die§Poliermaschine§leštička
die§Fräsmaschine§frézka
die§Drehmaschine§soustruh
die§Ständerbohrmaschine§stolní vrtačka
die§Kappsäge§pokosová pila
der§Hobel§hoblík
die§Oberfräse§horní frézka
der§Meißel§sekáč
der§Stechbeitel§dláto
der§Spachtel§špachtle
die§Kelle§zednická lžíce
die§Maurerkelle§zednická lžíce
die§Wasserwaage§vodováha
das§Maßband§svinovací metr
der§Meterstab§skládací metr
der§Zollstock§skládací metr
der§Winkel§úhelník
der§Anschlagwinkel§příložný úhelník
der§Messschieber§posuvné měřítko
die§Mikrometerschraube§mikrometr
das§Nivelliergerät§nivelační přístroj
das§Lasergerät§laserový přístroj
der§Laserentfernungsmesser§laserový dálkoměr
der§Bleistift§tužka
der§Markierstift§značkovací fix
die§Kreide§křída
der§Zirkel§kružítko
das§Cuttermesser§odlamovací nůž
das§Teppichmesser§řezací nůž
die§Schere§nůžky
das§Universalmesser§univerzální nůž
die§Nietzange§nýtovací kleště
die§Blindnietzange§nýtovací kleště
die§Heißklebepistole§tavná pistole
der§Lötkolben§páječka
die§Lötstation§pájecí stanice
das§Schweißgerät§svářečka
die§Schweißmaschine§svařovací stroj
die§Schutzbrille§ochranné brýle
der§Gehörschutz§ochrana sluchu
der§Arbeitshandschuh§pracovní rukavice
der§Schutzhelm§ochranná přilba
die§Werkbank§pracovní stůl
die§Werkzeugkiste§bedna na nářadí
der§Werkzeugwagen§vozík na nářadí
die§Werkstatt§dílna
die§Werkstattlampe§dílenská lampa
die§Druckluftpistole§ofukovací pistole
der§Kompressor§kompresor
der§Schlauch§hadice
die§Kupplung§spojka
der§Adapter§adaptér
das§Gewinde§závit
die§Schraube§šroub
die§Mutter§matice
die§Unterlegscheibe§podložka
der§Bolzen§čep
der§Dübel§hmoždinka
der§Nagel§hřebík
die§Niete§nýt
die§Feder§pružina
der§Splint§závlačka
das§Lager§ložisko
die§Dichtung§těsnění
der§O-Ring§O-kroužek
das§Schmiermittel§mazivo
das§Klebeband§lepicí páska
der§Klebstoff§lepidlo
der§Dichtstoff§těsnicí hmota
die§Farbe§barva
der§Lack§lak
der§Pinsel§štětec
die§Farbrolle§malířský váleček
der§Eimer§kbelík
die§Abdeckfolie§zakrývací fólie
das§Abdeckband§maskovací páska
die§Leiter§žebřík
das§Gerüst§lešení
der§Bock§koza
die§Hebebühne§zvedací plošina
der§Wagenheber§hever
der§Flaschenzug§kladkostroj
der§Kran§jeřáb
die§Winde§naviják
die§Pumpe§čerpadlo
der§Schraubstockgriff§rukojeť svěráku
die§Werkzeugaufnahme§upínání nástroje

# Fahrzeuge und Verkehrstechnik
das§Fahrzeug§vozidlo
der§Personenkraftwagen§osobní automobil
der§Lastkraftwagen§nákladní automobil
der§Lieferwagen§dodávka
der§Transporter§dodávka
der§Kleinwagen§malé auto
der§Kombi§kombi
der§Geländewagen§terénní vůz
der§Geländewagen§terénní automobil
der§Sportwagen§sportovní vůz
das§Cabrio§kabriolet
das§Motorrad§motocykl
der§Motorroller§skútr
das§Moped§moped
das§Fahrrad§jízdní kolo
das§Mountainbike§horské kolo
das§Rennrad§silniční kolo
das§E-Bike§elektrokolo
der§Anhänger§přívěs
der§Auflieger§návěs
der§Sattelzug§kamion
der§Busfahrer§řidič autobusu
die§Straßenbahn§tramvaj
die§U-Bahn§metro
die§S-Bahn§příměstský vlak
die§Regionalbahn§regionální vlak
der§Schnellzug§rychlík
der§Fernzug§dálkový vlak
der§Güterzug§nákladní vlak
der§Triebwagen§motorový vůz
die§Lokomotive§lokomotiva
der§Waggon§vagón
der§Eisenbahnwagen§železniční vůz
die§Weiche§výhybka
das§Gleis§kolej
der§Bahnsteig§nástupiště
der§Fahrgast§cestující
der§Fahrkartenautomat§automat na jízdenky
der§Fahrkartenentwerter§označovač jízdenek
die§Fahrzeugflotte§vozový park
der§Motor§motor
das§Getriebe§převodovka
die§Kupplung§spojka
die§Bremse§brzda
die§Bremsscheibe§brzdový kotouč
der§Bremsbelag§brzdová destička
der§Bremssattel§brzdový třmen
die§Bremsleitung§brzdové vedení
das§Lenkrad§volant
die§Lenkung§řízení
die§Servolenkung§posilovač řízení
das§Fahrwerk§podvozek
die§Federung§odpružení
der§Stoßdämpfer§tlumič
die§Achse§náprava
die§Antriebswelle§hnací hřídel
die§Kardanwelle§kardanový hřídel
die§Kurbelwelle§klikový hřídel
der§Kolben§píst
der§Zylinder§válec
der§Zylinderkopf§hlava válce
der§Motorblock§blok motoru
der§Luftfilter§vzduchový filtr
der§Ölfilter§olejový filtr
der§Kraftstofffilter§palivový filtr
die§Kraftstoffpumpe§palivové čerpadlo
der§Einspritzer§vstřikovač
die§Düse§tryska
die§Zündkerze§zapalovací svíčka
die§Zündung§zapalování
die§Lichtmaschine§alternátor
der§Anlasser§startér
die§Batterie§baterie
der§Kühler§chladič
der§Kühlmittelschlauch§hadice chladicí kapaliny
der§Auspuff§výfuk
der§Schalldämpfer§tlumič výfuku
die§Abgasanlage§výfuková soustava
das§Abgas§výfukový plyn
der§Katalysator§katalyzátor
der§Turbolader§turbodmychadlo
der§Ladeluftkühler§mezichladič
das§Steuergerät§řídicí jednotka
der§Sensor§snímač
der§Drehzahlsensor§snímač otáček
der§Temperatursensor§teplotní snímač
der§Drucksensor§tlakový snímač
der§Lambdasensor§lambda sonda
das§Armaturenbrett§palubní deska
der§Sitz§sedadlo
der§Autositz§autosedačka
der§Kindersitz§dětská sedačka
der§Sicherheitsgurt§bezpečnostní pás
der§Airbag§airbag
die§Windschutzscheibe§čelní sklo
die§Heckscheibe§zadní sklo
der§Scheibenwischer§stěrač
der§Außenspiegel§zpětné zrcátko
der§Innenspiegel§vnitřní zpětné zrcátko
der§Scheinwerfer§světlomet
das§Rücklicht§zadní světlo
das§Blinklicht§směrové světlo
die§Stoßstange§nárazník
die§Motorhaube§kapota
der§Kofferraum§kufr
die§Tür§dveře
der§Reifen§pneumatika
das§Rad§kolo
die§Felge§ráfek
das§Reserverad§rezervní kolo
der§Schlüssel§klíč
der§Autoschlüssel§klíč od auta
das§Nummernschild§registrační značka
das§Kennzeichen§poznávací značka
die§Fahrzeugidentifikationsnummer§identifikační číslo vozidla
die§Autobahn§dálnice
die§Landstraße§silnice
die§Bundesstraße§spolková silnice
die§Kreisstraße§okresní silnice
der§Radweg§cyklostezka
der§Gehweg§chodník
der§Zebrastreifen§přechod pro chodce
der§Kreisverkehr§kruhový objezd
die§Einbahnstraße§jednosměrná ulice
die§Baustelle§staveniště
die§Umleitung§objížďka
die§Sackgasse§slepá ulice
die§Mautstelle§mýtná brána
die§Tankstelle§čerpací stanice
die§Ladesäule§nabíjecí stanice
der§Parkplatz§parkoviště
das§Parkhaus§parkovací dům
der§Parkautomat§parkovací automat
die§Parkuhr§parkovací hodiny
die§Verkehrsampel§semafor
das§Verkehrsschild§dopravní značka
die§Geschwindigkeitsbegrenzung§omezení rychlosti
die§Vorfahrtsregel§pravidlo přednosti
die§Verkehrskontrolle§dopravní kontrola
der§Stau§dopravní zácpa
der§Unfall§nehoda
der§Blechschaden§škoda na karoserii
die§Panne§porucha
der§Abschleppwagen§odtahové vozidlo
die§Fahrzeugprüfung§technická kontrola vozidla
die§Hauptuntersuchung§technická prohlídka
die§Werkstattrechnung§faktura za opravu
das§Ersatzteil§náhradní díl
die§Autowerkstatt§autoservis
der§Autohändler§prodejce aut
die§Autovermietung§autopůjčovna

# Berufe und Arbeitswelt
der§Ingenieur§inženýr
die§Ingenieurin§inženýrka
der§Techniker§technik
die§Technikerin§technička
der§Mechaniker§mechanik
die§Mechanikerin§mechanická
der§Elektriker§elektrikář
die§Elektrikerin§elektrikářka
der§Installateur§instalatér
die§Installateurin§instalatérka
der§Schlosser§zámečník
die§Schlosserin§zámečnice
der§Schweißer§svářeč
die§Schweißerin§svářečka
der§Dreher§soustružník
die§Dreherin§soustružnice
der§Fräser§frézař
die§Fräserin§frézařka
der§Werkzeugmacher§nástrojař
die§Werkzeugmacherin§nástrojařka
der§Maschinenführer§obsluha stroje
die§Maschinenführerin§obsluha stroje
der§Produktionsleiter§vedoucí výroby
die§Produktionsleiterin§vedoucí výroby
der§Qualitätsmanager§manažer kvality
die§Qualitätsmanagerin§manažerka kvality
der§Einkäufer§nákupčí
die§Einkäuferin§nákupčí
der§Verkäufer§prodavač
die§Verkäuferin§prodavačka
der§Kaufmann§obchodník
die§Kauffrau§obchodnice
der§Buchhalter§účetní
die§Buchhalterin§účetní
der§Steuerberater§daňový poradce
die§Steuerberaterin§daňová poradkyně
der§Rechtsanwalt§advokát
die§Rechtsanwältin§advokátka
der§Richter§soudce
die§Richterin§soudkyně
der§Polizist§policista
die§Polizistin§policistka
der§Feuerwehrmann§hasič
die§Feuerwehrfrau§hasička
der§Arzt§lékař
die§Ärztin§lékařka
der§Zahnarzt§zubní lékař
die§Zahnärztin§zubní lékařka
der§Chirurg§chirurg
die§Chirurgin§chirurgyně
der§Pfleger§ošetřovatel
die§Pflegerin§ošetřovatelka
der§Krankenpfleger§zdravotní bratr
die§Krankenschwester§zdravotní sestra
der§Apotheker§lékárník
die§Apothekerin§lékárnice
der§Lehrer§učitel
die§Lehrerin§učitelka
der§Erzieher§vychovatel
die§Erzieherin§vychovatelka
der§Professor§profesor
die§Professorin§profesorka
der§Dozent§docent
die§Dozentin§docentka
der§Forscher§výzkumník
die§Forscherin§výzkumnice
der§Wissenschaftler§vědec
die§Wissenschaftlerin§vědkyně
der§Programmierer§programátor
die§Programmiererin§programátorka
der§Softwareentwickler§vývojář softwaru
die§Softwareentwicklerin§vývojářka softwaru
der§Datenanalyst§datový analytik
die§Datenanalystin§datová analytička
der§Designer§designér
die§Designerin§designérka
der§Architekt§architekt
die§Architektin§architektka
der§Bauarbeiter§stavební dělník
die§Bauarbeiterin§stavební dělnice
der§Maurer§zedník
die§Maurerin§zednice
der§Dachdecker§pokrývač
die§Dachdeckerin§pokrývačka
der§Zimmermann§tesař
die§Zimmerin§tesařka
der§Maler§malíř
die§Malerin§malířka
der§Tischler§truhlář
die§Tischlerin§truhlářka
der§Bäcker§pekař
die§Bäckerin§pekařka
der§Konditor§cukrář
die§Konditorin§cukrářka
der§Koch§kuchař
die§Köchin§kuchařka
der§Kellner§číšník
die§Kellnerin§servírka
der§Friseur§kadeřník
die§Friseurin§kadeřnice
der§Schneider§krejčí
die§Schneiderin§krejčová
der§Fahrer§řidič
die§Fahrerin§řidička
der§Lokführer§strojvedoucí
die§Lokführerin§strojvedoucí
der§Pilot§pilot
die§Pilotin§pilotka
der§Flugbegleiter§stevard
die§Flugbegleiterin§stevardka
der§Rezeptionist§recepční
die§Rezeptionistin§recepční
der§Journalist§novinář
die§Journalistin§novinářka
der§Fotograf§fotograf
die§Fotografin§fotografka
der§Übersetzer§překladatel
die§Übersetzerin§překladatelka
der§Dolmetscher§tlumočník
die§Dolmetscherin§tlumočnice
der§Sozialarbeiter§sociální pracovník
die§Sozialarbeiterin§sociální pracovnice
der§Psychologe§psycholog
die§Psychologin§psycholožka
der§Therapeut§terapeut
die§Therapeutin§terapeutka
der§Trainer§trenér
die§Trainerin§trenérka
der§Friseurmeister§mistr kadeřník
die§Friseurmeisterin§mistryně kadeřnice
der§Landwirt§zemědělec
die§Landwirtin§zemědělkyně
der§Gärtner§zahradník
die§Gärtnerin§zahradnice
der§Förster§lesník
die§Försterin§lesnice
der§Fischer§rybář
die§Fischerin§rybářka
der§Bergmann§horník
die§Bergfrau§hornice
der§Bergarbeiter§horník
die§Bergarbeiterin§hornice
der§Soldat§voják
die§Soldatin§vojanda
der§Seemann§námořník
die§Seefahrerin§námořnice
der§Postbote§pošťák
die§Postbotin§pošťačka
der§Paketbote§doručovatel balíků
die§Paketbotin§doručovatelka balíků
der§Reinigungskraft§uklízeč
die§Reinigungskraft§uklízečka
der§Hausmeister§domovník
die§Hausmeisterin§domovnice
der§Sicherheitsmitarbeiter§pracovník ostrahy
die§Sicherheitsmitarbeiterin§pracovnice ostrahy
der§Geschäftsführer§jednatel
die§Geschäftsführerin§jednatelka
der§Abteilungsleiter§vedoucí oddělení
die§Abteilungsleiterin§vedoucí oddělení
der§Projektmanager§projektový manažer
die§Projektmanagerin§projektová manažerka
der§Personalreferent§personální specialista
die§Personalreferentin§personální specialistka
der§Kundenberater§zákaznický poradce
die§Kundenberaterin§zákaznická poradkyně
der§Sachbearbeiter§referent
die§Sachbearbeiterin§referentka
der§Logistiker§logistik
die§Logistikerin§logistička
der§Lagerist§skladník
die§Lageristin§skladnice
der§Disponent§dispečer
die§Disponentin§dispečerka
der§Spediteur§spediteur
die§Spediteurin§speditéra
der§Unternehmer§podnikatel
die§Unternehmerin§podnikatelka
der§Selbstständige§osoba samostatně výdělečně činná
die§Selbstständige§osoba samostatně výdělečně činná
der§Praktikant§stážista
die§Praktikantin§stážistka
der§Auszubildende§učeň
die§Auszubildende§učnice
der§Student§student
die§Studentin§studentka
der§Doktorand§doktorand
die§Doktorandin§doktorandka
der§Schüler§žák
die§Schülerin§žákyně
der§Rentner§důchodce
die§Rentnerin§důchodkyně
der§Arbeitslose§nezaměstnaný
die§Arbeitslose§nezaměstnaná
der§Bewerber§uchazeč
die§Bewerberin§uchazečka
der§Mitarbeiter§zaměstnanec
die§Mitarbeiterin§zaměstnankyně
der§Kollege§kolega
die§Kollegin§kolegyně
der§Vorgesetzte§nadřízený
die§Vorgesetzte§nadřízená

# Schule und Ausbildung
die§Universität§univerzita
die§Hochschule§vysoká škola
die§Fakultät§fakulta
das§Institut§institut
das§Labor§laboratoř
die§Vorlesung§přednáška
das§Seminar§seminář
das§Praktikum§praxe
die§Ausbildung§vzdělání
die§Berufsschule§odborná škola
das§Gymnasium§gymnázium
die§Grundschule§základní škola
die§Mittelschule§střední škola
die§Abendschule§večerní škola
die§Sprachschule§jazyková škola
der§Kurs§kurz
der§Sprachkurs§jazykový kurz
die§Lektion§lekce
die§Unterrichtsstunde§vyučovací hodina
der§Lehrplan§učební plán
das§Lehrbuch§učebnice
das§Arbeitsbuch§pracovní sešit
das§Wörterbuch§slovník
das§Nachschlagewerk§příručka
das§Lernmaterial§studijní materiál
die§Lernkarte§učební kartička
der§Vokabeltest§test ze slovíček
die§Hausaufgabe§domácí úkol
die§Klassenarbeit§písemná práce
die§Klausur§zkouška
die§Abschlussprüfung§závěrečná zkouška
die§Aufnahmeprüfung§přijímací zkouška
das§Abitur§maturita
das§Zeugnis§vysvědčení
der§Abschluss§ukončení studia
der§Schulabschluss§ukončení školy
der§Hochschulabschluss§vysokoškolský titul
der§Bachelorabschluss§bakalářský titul
der§Masterabschluss§magisterský titul
die§Dissertation§disertační práce
die§Diplomarbeit§diplomová práce
die§Seminararbeit§seminární práce
die§Forschungsarbeit§výzkumná práce
die§Bibliothek§knihovna
der§Lesesaal§studovna
das§Regal§regál
der§Bibliotheksausweis§čtenářský průkaz
das§Lesezeichen§záložka
der§Buchrücken§hřbet knihy
das§Kapitel§kapitola
das§Inhaltsverzeichnis§obsah knihy
das§Vorwort§předmluva
das§Nachwort§doslov
die§Fußnote§poznámka pod čarou
das§Zitat§citát
die§Quelle§zdroj
das§Literaturverzeichnis§seznam literatury
der§Lernstoff§učivo
die§Lernmethode§metoda učení
der§Lernerfolg§studijní úspěch
die§Lernschwierigkeit§potíž při učení
die§Konzentration§soustředění
das§Gedächtnis§paměť
die§Aufmerksamkeit§pozornost
die§Motivation§motivace
die§Kenntnis§znalost
die§Fähigkeit§schopnost
die§Kompetenz§kompetence
die§Fertigkeit§dovednost
die§Übung§cvičení
das§Beispiel§příklad
die§Erklärung§vysvětlení
die§Übersetzung§překlad
die§Aussprache§výslovnost
die§Betonung§přízvuk
der§Akzent§přízvuk
die§Grammatikregel§gramatické pravidlo
die§Rechtschreibung§pravopis
die§Wortbildung§tvoření slov
die§Wortart§slovní druh
die§Wortfamilie§slovní rodina
die§Silbe§slabika
der§Laut§hláska
der§Vokal§samohláska
der§Konsonant§souhláska
das§Alphabet§abeceda
der§Buchstabe§písmeno
das§Zeichen§znak
das§Satzzeichen§interpunkční znaménko
der§Punkt§tečka
das§Komma§čárka
das§Fragezeichen§otazník
das§Ausrufezeichen§vykřičník

# Medizin und Gesundheit
die§Gesundheit§zdraví
die§Gesundheitsversorgung§zdravotní péče
die§Behandlung§léčba
die§Untersuchung§vyšetření
die§Diagnose§diagnóza
die§Therapie§terapie
die§Operation§operace
die§Notoperation§akutní operace
die§Narkose§narkóza
die§Anästhesie§anestezie
das§Rezept§recept
die§Verschreibung§předpis
die§Apotheke§lékárna
der§Apotheker§lékárník
die§Apothekerin§lékárnice
die§Arznei§léčivo
das§Arzneimittel§léčivý přípravek
das§Schmerzmittel§lék proti bolesti
das§Antibiotikum§antibiotikum
das§Vitamin§vitamín
das§Mineral§minerál
die§Salbe§mast
die§Creme§krém
der§Verband§obvaz
das§Pflaster§náplast
die§Bandage§bandáž
die§Spritze§injekce
die§Nadel§jehla
die§Kanüle§kanyla
die§Infusion§infuze
die§Blutprobe§vzorek krve
die§Blutuntersuchung§vyšetření krve
der§Blutdruck§krevní tlak
das§Blutbild§krevní obraz
der§Puls§puls
die§Herzfrequenz§srdeční frekvence
das§Fieber§horečka
die§Erkältung§nachlazení
die§Grippe§chřipka
der§Husten§kašel
der§Schnupfen§rýma
die§Halsschmerzen§bolest v krku
die§Kopfschmerzen§bolest hlavy
die§Migräne§migréna
die§Allergie§alergie
der§Ausschlag§vyrážka
die§Entzündung§zánět
die§Infektion§infekce
die§Verletzung§zranění
die§Wunde§rána
die§Verbrennung§popálenina
der§Bruch§zlomenina
die§Fraktur§zlomenina
die§Verstauchung§podvrtnutí
die§Prellung§pohmoždění
die§Schwellung§otok
die§Blutung§krvácení
die§Narbe§jizva
der§Knochen§kost
der§Muskel§sval
die§Sehne§šlacha
das§Gelenk§kloub
die§Wirbelsäule§páteř
der§Wirbel§obratel
das§Gehirn§mozek
die§Lunge§plíce
die§Leber§játra
die§Niere§ledvina
der§Magen§žaludek
der§Darm§střevo
die§Bauchspeicheldrüse§slinivka
die§Milz§slezina
die§Blase§močový měchýř
die§Speiseröhre§jícen
die§Luftröhre§průdušnice
der§Kehlkopf§hrtan
die§Zunge§jazyk
der§Gaumen§patro
das§Zahnfleisch§dásně
der§Zahn§zub
die§Zahnbürste§zubní kartáček
die§Zahnpasta§zubní pasta
die§Zahnspange§rovnátka
die§Zahnkrone§zubní korunka
die§Füllung§výplň
die§Sehhilfe§zraková pomůcka
die§Brille§brýle
die§Kontaktlinse§kontaktní čočka
das§Hörgerät§naslouchátko
der§Rollstuhl§invalidní vozík
der§Gehstock§hůl
die§Krücke§berle
das§Thermometer§teploměr
das§Stethoskop§stetoskop
das§Blutdruckmessgerät§tlakoměr
das§EKG§EKG
das§Röntgenbild§rentgenový snímek
die§Ultraschalluntersuchung§ultrazvukové vyšetření
die§Klinik§klinika
die§Praxis§ordinace
die§Notaufnahme§pohotovost
der§Krankenwagen§sanitka
der§Rettungsdienst§záchranná služba
die§Krankenversicherung§zdravotní pojištění
die§Krankenkasse§zdravotní pojišťovna
die§Rehabilitation§rehabilitace
die§Physiotherapie§fyzioterapie
die§Massage§masáž
die§Impfung§očkování
die§Vorsorge§prevence
die§Ernährung§výživa
die§Bewegung§pohyb
der§Schlaf§spánek
die§Müdigkeit§únava
die§Erschöpfung§vyčerpání
das§Wohlbefinden§pohoda
die§Lebensqualität§kvalita života
die§Sucht§závislost
die§Raucherentwöhnung§odvykání kouření
die§Psychotherapie§psychoterapie
die§Depression§deprese
die§Angststörung§úzkostná porucha
die§Panikattacke§panická ataka
die§Demenz§demence
die§Blutgruppe§krevní skupina
die§Organspende§dárcovství orgánů
die§Spende§dar
die§Patientenakte§zdravotní dokumentace
der§Befund§nález
die§Überweisung§žádanka
die§Wartezeit§čekací doba

# Kleidung und Körperpflege
die§Unterwäsche§spodní prádlo
der§BH§podprsenka
der§Slip§kalhotky
die§Unterhose§spodní kalhoty
das§Unterhemd§nátělník
die§Strumpfhose§punčocháče
der§Strumpf§punčocha
die§Leggings§legíny
die§Jeans§džíny
die§Shorts§kraťasy
die§Badehose§plavky
der§Badeanzug§plavky
der§Bademantel§župan
der§Pyjama§pyžamo
das§Nachthemd§noční košile
die§Bluse§halenka
der§Pullover§svetr
die§Strickjacke§kardigan
das§Sweatshirt§mikina
der§Kapuzenpullover§mikina s kapucí
der§Trainingsanzug§tepláková souprava
die§Sporthose§sportovní kalhoty
das§Sportshirt§sportovní tričko
die§Regenjacke§pláštěnka
der§Regenmantel§nepromokavý kabát
die§Winterjacke§zimní bunda
der§Anorak§anorak
die§Daunenjacke§péřová bunda
der§Blazer§sako
der§Anzug§oblek
die§Anzughose§oblekové kalhoty
die§Krawatte§kravata
die§Fliege§motýlek
der§Gürtel§opasek
die§Hosenträger§šle
der§Knopf§knoflík
der§Reißverschluss§zip
der§Kragen§límec
der§Ärmel§rukáv
die§Manschette§manžeta
die§Kapuze§kapuce
die§Tasche§kapsa
der§Saum§lem
der§Stoff§látka
die§Baumwolle§bavlna
die§Wolle§vlna
die§Seide§hedvábí
das§Leinen§len
das§Leder§kůže
der§Kunststoff§plast
die§Spitze§krajka
der§Kniestrumpf§podkolenka
die§Sportsocke§sportovní ponožka
der§Turnschuh§teniska
der§Wanderschuh§turistická obuv
der§Stiefel§kozačka
die§Sandale§sandál
die§Socke§ponožka
der§Hausschuh§domácí pantofel
die§Pantolette§pantofel
der§Schlappen§pantofel
der§Absatz§podpatek
die§Sohle§podrážka
der§Schnürsenkel§tkanička
die§Einlegesohle§vložka do bot
die§Schuhcreme§krém na boty
die§Schuhbürste§kartáč na boty
die§Handtasche§kabelka
die§Umhängetasche§taška přes rameno
die§Aktentasche§aktovka
der§Aktenkoffer§kufřík
die§Clutch§psaníčko
der§Beutel§váček
die§Einkaufstasche§nákupní taška
der§Geldbeutel§peněženka
das§Portemonnaie§peněženka
der§Geldbeutel§peněženka
der§Schmuck§šperky
der§Ring§prsten
die§Halskette§náhrdelník
das§Armband§náramek
der§Ohrring§náušnice
der§Anhänger§přívěsek
die§Brosche§brož
die§Armbanduhr§náramkové hodinky
die§Sonnenbrille§sluneční brýle
der§Regenschirm§deštník
die§Mütze§čepice
der§Hut§klobouk
die§Kappe§kšiltovka
der§Helm§přilba
der§Schutzhelm§ochranná přilba
die§Baseballkappe§baseballová čepice
der§Handschuh§rukavice
der§Fäustling§palčák
der§Fingerhandschuh§prstová rukavice
der§Schal§šála
das§Halstuch§šátek
das§Kopftuch§šátek na hlavu
der§Ohrring§náušnice
die§Haarspange§spona do vlasů
das§Haargummi§gumička do vlasů
die§Haarbürste§kartáč na vlasy
der§Kamm§hřeben
das§Shampoo§šampon
die§Spülung§kondicionér
das§Duschgel§sprchový gel
die§Seife§mýdlo
die§Handcreme§krém na ruce
die§Gesichtscreme§pleťový krém
die§Sonnencreme§opalovací krém
das§Parfüm§parfém
das§Deodorant§deodorant
der§Rasierapparat§holicí strojek
der§Rasierer§holicí strojek
die§Rasierklinge§žiletka
der§Rasierschaum§pěna na holení
der§Lippenstift§rtěnka
der§Nagellack§lak na nehty
die§Nagelfeile§pilník na nehty
die§Nagelschere§nůžky na nehty
die§Pinzette§pinzeta
die§Wimperntusche§řasenka
der§Lidschatten§oční stín
der§Eyeliner§oční linka
der§Puder§pudr
die§Foundation§make-up
der§Make-up-Pinsel§štětec na make-up
das§Kosmetiktäschchen§kosmetická taštička
der§Waschbeutel§toaletní taška
der§Kulturbeutel§toaletní taška
der§Spiegel§zrcadlo
der§Zahnstocher§párátko
die§Zahnbürste§zubní kartáček
der§Mundspiegel§dentální zrcátko
die§Zahnseide§zubní nit
die§Mundspülung§ústní voda
der§Mundschutz§rouška
die§Hygienemaske§hygienická maska

# Materialien und Baustoffe
das§Holz§dřevo
das§Metall§kov
das§Eisen§železo
der§Stahl§ocel
der§Edelstahl§nerezová ocel
das§Aluminium§hliník
das§Kupfer§měď
das§Messing§mosaz
die§Bronze§bronz
das§Zink§zinek
das§Blei§olovo
das§Zinn§cín
das§Nickel§nikl
das§Chrom§chrom
das§Titan§titan
das§Magnesium§hořčík
das§Silber§stříbro
das§Gold§zlato
das§Platin§platina
das§Glas§sklo
das§Fensterglas§sklo do oken
das§Sicherheitsglas§bezpečnostní sklo
das§Kunststoffgranulat§plastový granulát
der§Kunststoff§plast
das§Polyamid§polyamid
das§Polyethylen§polyetylen
das§Polypropylen§polypropylen
das§Polycarbonat§polykarbonát
das§Polyurethan§polyuretan
das§Silikon§silikon
der§Gummi§guma
der§Naturkautschuk§přírodní kaučuk
der§Schaumstoff§pěnová hmota
der§Dämmstoff§izolační materiál
der§Isolierstoff§izolační hmota
die§Glasfaser§skleněné vlákno
die§Kohlefaser§uhlíkové vlákno
die§Keramik§keramika
das§Porzellan§porcelán
die§Fliese§dlaždice
der§Ziegel§cihla
der§Backstein§pálená cihla
der§Beton§beton
der§Stahlbeton§železobeton
der§Mörtel§malta
der§Zement§cement
der§Kalk§vápno
der§Gips§sádra
der§Putz§omítka
der§Estrich§potěr
der§Asphalt§asfalt
der§Bitumen§bitumen
der§Teer§dehet
der§Dachziegel§střešní taška
die§Dachrinne§okap
die§Dachpappe§lepenka
der§Dachstuhl§krov
der§Balken§trám
das§Brett§prkno
die§Platte§deska
die§Spanplatte§dřevotříska
die§Faserplatte§vláknitá deska
die§MDF-Platte§MDF deska
die§OSB-Platte§OSB deska
das§Sperrholz§překližka
das§Furnier§dýha
der§Leim§lepidlo
der§Holzleim§lepidlo na dřevo
der§Montageschaum§montážní pěna
das§Dichtband§těsnicí páska
das§Dichtmittel§těsnicí prostředek
die§Dichtmasse§těsnicí hmota
der§Dichtstoff§těsnicí tmel
die§Dämmwolle§izolační vata
die§Mineralwolle§minerální vata
die§Glaswolle§skelná vata
die§Steinwolle§kamenná vata
das§Dämmvlies§izolační rouno
die§Dämmplatte§izolační deska
die§Wärmedämmung§tepelná izolace
die§Schalldämmung§zvuková izolace
die§Abdichtung§hydroizolace
die§Dachisolierung§izolace střechy
die§Fassade§fasáda
die§Fassadenplatte§fasádní deska
der§Putzmörtel§omítková malta
der§Fliesenkleber§lepidlo na obklady
die§Fugenmasse§spárovací hmota
die§Fuge§spára
das§Mauerwerk§zdivo
die§Mauer§zeď
die§Stützmauer§opěrná zeď
die§Trennwand§příčka
die§Trockenbauwand§sádrokartonová příčka
die§Gipskartonplatte§sádrokartonová deska
das§Fensterprofil§okenní profil
der§Fenstergriff§okenní klika
der§Türbeschlag§dveřní kování
das§Schloss§zámek
das§Vorhängeschloss§visací zámek
der§Riegel§závora
die§Schwelle§práh
der§Türdrücker§dveřní klika
der§Türknauf§dveřní koule
der§Bodenbelag§podlahová krytina
die§Parkettdiele§parketová lamela
das§Linoleum§linoleum
der§Vinylboden§vinylová podlaha
der§Teppichboden§koberec
die§Tapete§tapeta
der§Tapetenkleister§lepidlo na tapety
die§Wandfarbe§barva na zeď
der§Lackanstrich§lakový nátěr
die§Grundierung§základní nátěr
die§Lasur§lazura
das§Holzöl§olej na dřevo
das§Wachs§vosk
das§Harz§pryskyřice
das§Epoxidharz§epoxidová pryskyřice
das§Polyesterharz§polyesterová pryskyřice
das§Glasfasermaterial§sklolaminát
das§Verbundmaterial§kompozitní materiál
der§Verbundwerkstoff§kompozit
das§Blech§plech
das§Stahlblech§ocelový plech
das§Aluminiumblech§hliníkový plech
das§Kupferblech§měděný plech
das§Profil§profil
das§Stahlprofil§ocelový profil
das§Aluminiumprofil§hliníkový profil
das§Rohr§trubka
das§Stahlrohr§ocelová trubka
das§Kupferrohr§měděná trubka
das§Kunststoffrohr§plastová trubka
der§Schlauch§hadice
das§Glasrohr§skleněná trubice
der§Draht§drát
der§Stahldraht§ocelový drát
der§Kupferdraht§měděný drát
der§Aluminiumdraht§hliníkový drát
der§Stab§tyč
der§Stahlstab§ocelová tyč
das§Rundmaterial§kruhový materiál
das§Flachmaterial§plochý materiál
das§Rohmaterial§surovina
der§Rohstoff§surovina
der§Werkstoff§materiál
das§Halbzeug§polotovar
das§Fertigteil§hotový díl
das§Bauteil§součást
das§Ersatzteil§náhradní díl

# Produktion und Industrie
die§Produktion§výroba
die§Fertigung§výroba
das§Produkt§výrobek
die§Produktionslinie§výrobní linka
die§Montagelinie§montážní linka
die§Montage§montáž
die§Endmontage§finální montáž
die§Vormontage§předmontáž
die§Fertigungshalle§výrobní hala
die§Montagehalle§montážní hala
das§Werk§závod
das§Werkgelände§areál závodu
das§Fabrikgebäude§budova továrny
die§Fabrik§továrna
die§Werkhalle§výrobní hala
die§Maschine§stroj
die§Anlage§zařízení
die§Produktionsanlage§výrobní zařízení
die§Bearbeitungsmaschine§obráběcí stroj
die§CNC-Maschine§CNC stroj
die§CNC-Fräse§CNC frézka
die§Drehmaschine§soustruh
die§Schleifmaschine§bruska
die§Stanzmaschine§lisovací stroj
die§Presse§lis
die§Hydraulikpresse§hydraulický lis
die§Exzenterpresse§výstředníkový lis
die§Spritzgießmaschine§vstřikovací lis
die§Druckgussmaschine§tlakový licí stroj
die§Gießmaschine§licí stroj
die§Schweißanlage§svařovací zařízení
die§Lackieranlage§lakovací zařízení
die§Beschichtungsanlage§povlakovací zařízení
die§Waschanlage§mycí zařízení
die§Prüfanlage§zkušební zařízení
die§Messmaschine§měřicí stroj
der§Roboter§robot
der§Industrieroboter§průmyslový robot
der§Schweißroboter§svařovací robot
der§Manipulator§manipulátor
das§Förderband§dopravní pás
die§Förderanlage§dopravní zařízení
der§Förderer§dopravník
der§Kettenförderer§řetězový dopravník
der§Rollenförderer§válečkový dopravník
der§Gabelstapler§vysokozdvižný vozík
der§Hubwagen§paletový vozík
der§Deckenkran§mostový jeřáb
der§Hallenkran§halový jeřáb
die§Hebevorrichtung§zvedací zařízení
die§Greifzange§uchopovací kleště
der§Greifer§úchopné zařízení
die§Vorrichtung§přípravek
die§Spannvorrichtung§upínací přípravek
die§Spannbacke§upínací čelist
die§Spannzange§upínací kleština
das§Werkzeug§nástroj
das§Schneidwerkzeug§řezný nástroj
das§Fräswerkzeug§frézovací nástroj
das§Drehwerkzeug§soustružnický nástroj
der§Bohrer§vrták
der§Fräser§fréza
der§Drehmeißel§soustružnický nůž
die§Schneidplatte§řezná destička
der§Werkzeughalter§držák nástroje
die§Werkzeugaufnahme§upnutí nástroje
der§Werkzeugwechsel§výměna nástroje
die§Werkzeugmaschine§obráběcí stroj
die§Werkzeugausgabe§výdej nástrojů
die§Werkzeugverwaltung§správa nástrojů
die§Qualität§kvalita
die§Qualitätskontrolle§kontrola kvality
die§Qualitätssicherung§zajištění kvality
die§Qualitätsprüfung§zkouška kvality
der§Prüfplan§kontrolní plán
das§Prüfprotokoll§protokol o zkoušce
der§Messbericht§měřicí protokol
das§Messmittel§měřidlo
das§Messgerät§měřicí přístroj
die§Messuhr§úchylkoměr
der§Messschieber§posuvné měřítko
die§Lehre§měrka
die§Grenzlehre§mezní kalibr
der§Kaliber§kalibr
die§Oberflächenrauheit§drsnost povrchu
die§Oberfläche§povrch
die§Toleranz§tolerance
das§Maß§rozměr
die§Maßabweichung§rozměrová odchylka
die§Abweichung§odchylka
der§Fehler§chyba
der§Ausschuss§zmetek
die§Nacharbeit§oprava
die§Nachbearbeitung§dodatečné opracování
die§Reklamation§reklamace
der§Mangel§vada
der§Defekt§závada
der§Stillstand§odstávka
der§Maschinenausfall§porucha stroje
die§Wartung§údržba
die§Instandhaltung§údržba
die§Reparatur§oprava
der§Wartungsplan§plán údržby
das§Ersatzteillager§sklad náhradních dílů
die§Ersatzteilversorgung§zásobování náhradními díly
die§Produktivität§produktivita
die§Auslastung§vytížení
die§Kapazität§kapacita
die§Durchlaufzeit§průběžná doba
die§Taktzeit§takt
die§Rüstzeit§seřizovací čas
die§Rüstkosten§náklady na seřízení
die§Losgröße§velikost dávky
die§Stückzahl§počet kusů
die§Serienfertigung§sériová výroba
die§Einzelfertigung§kusová výroba
die§Kleinserie§malá série
die§Großserie§velká série
der§Prototyp§prototyp
das§Muster§vzorek
das§Urmodell§model
die§Form§forma
das§Werkzeug§nástroj
die§Gießform§licí forma
die§Druckgussform§forma pro tlakové lití
die§Spritzgussform§vstřikovací forma
der§Formeinsatz§vložka formy
der§Formkern§jádro formy
der§Schieber§posuvník
der§Auswerfer§vyhazovač
die§Auswerferstange§vyhazovací tyč
die§Trennfuge§dělící rovina
die§Entlüftung§odvzdušnění
die§Kühlung§chlazení
der§Kühlkanal§chladicí kanál
das§Kühlmittel§chladicí kapalina
das§Schmieröl§mazací olej
das§Hydrauliköl§hydraulický olej
die§Hydraulik§hydraulika
die§Pneumatik§pneumatika
der§Zylinder§válec
der§Hydraulikzylinder§hydraulický válec
der§Pneumatikzylinder§pneumatický válec
das§Ventil§ventil
das§Magnetventil§magnetický ventil
die§Dichtung§těsnění
der§Filter§filtr
die§Filteranlage§filtrační zařízení
der§Druckluftfilter§filtr stlačeného vzduchu
der§Luftanschluss§vzduchová přípojka
die§Druckluft§stlačený vzduch
der§Druckregler§regulátor tlaku
der§Druckminderer§redukční ventil
die§Pumpe§čerpadlo
die§Vakuumpumpe§vývěva
das§Vakuum§vakuum
die§Absaugung§odsávání
die§Absauganlage§odsávací zařízení
die§Entsorgung§likvidace
der§Abfall§odpad
der§Metallschrott§kovový šrot
das§Restmaterial§zbytkový materiál
der§Verschnitt§odřezek
die§Schrottmenge§množství šrotu
der§Materialverbrauch§spotřeba materiálu
die§Materialkosten§materiálové náklady
die§Herstellkosten§výrobní náklady
die§Fertigungskosten§výrobní náklady
die§Produktionskosten§výrobní náklady
die§Lieferzeit§dodací lhůta
die§Liefermenge§dodané množství
die§Lieferung§dodávka
der§Lieferant§dodavatel
die§Lieferantin§dodavatelka
der§Kunde§zákazník
die§Kundin§zákaznice

# Energie und Versorgung
die§Energie§energie
die§Elektrizität§elektřina
der§Strom§elektřina
der§Stromverbrauch§spotřeba elektřiny
der§Stromzähler§elektroměr
der§Gaszähler§plynoměr
der§Wasserzähler§vodoměr
die§Stromrechnung§účet za elektřinu
die§Gasrechnung§účet za plyn
die§Wasserrechnung§účet za vodu
die§Stromversorgung§dodávka elektřiny
die§Energieversorgung§zásobování energií
das§Kraftwerk§elektrárna
das§Wasserkraftwerk§vodní elektrárna
das§Kohlekraftwerk§uhelná elektrárna
das§Kernkraftwerk§jaderná elektrárna
das§Solarkraftwerk§solární elektrárna
der§Windpark§větrný park
die§Windkraftanlage§větrná elektrárna
die§Solaranlage§solární zařízení
das§Solarmodul§solární panel
die§Solarzelle§solární článek
der§Wechselrichter§měnič
der§Generator§generátor
der§Elektromotor§elektromotor
der§Motor§motor
der§Transformator§transformátor
die§Spule§cívka
der§Magnet§magnet
der§Dauermagnet§permanentní magnet
der§Elektromagnet§elektromagnet
der§Stator§stator
der§Rotor§rotor
die§Wicklung§vinutí
der§Draht§drát
der§Kupferdraht§měděný drát
das§Kabel§kabel
die§Kabeltrommel§kabelový buben
die§Leitung§vedení
die§Stromleitung§elektrické vedení
der§Schalter§spínač
der§Leistungsschalter§výkonový jistič
die§Sicherung§pojistka
der§Sicherungskasten§pojistková skříň
der§Schutzschalter§ochranný vypínač
der§FI-Schalter§proudový chránič
die§Steckverbindung§konektor
der§Stecker§zástrčka
die§Steckdose§zásuvka
der§Kontakt§kontakt
der§Anschluss§přípojka
die§Klemme§svorka
der§Klemmenblock§svorkovnice
die§Kabelverschraubung§kabelová průchodka
der§Isolator§izolátor
die§Isolierung§izolace
das§Isolierband§izolační páska
der§Isolierschlauch§izolační bužírka
die§Spannung§napětí
der§Strom§proud
die§Stromstärke§proudová síla
der§Widerstand§odpor
die§Leistung§výkon
die§Frequenz§frekvence
die§Drehzahl§otáčky
das§Drehmoment§točivý moment
die§Temperatur§teplota
der§Druck§tlak
die§Feuchtigkeit§vlhkost
der§Wirkungsgrad§účinnost
der§Verbrauch§spotřeba
die§Batterie§baterie
der§Akku§akumulátor
der§Energiespeicher§zásobník energie
der§Batteriespeicher§bateriové úložiště
die§Ladestation§nabíjecí stanice
das§Ladegerät§nabíječka
die§Ladebuchse§nabíjecí konektor
der§Ladestrom§nabíjecí proud
der§Ladezyklus§nabíjecí cyklus
der§Brennstoff§palivo
der§Treibstoff§pohonné palivo
das§Erdgas§zemní plyn
das§Flüssiggas§zkapalněný plyn
das§Heizöl§topný olej
das§Benzin§benzín
der§Diesel§nafta
der§Wasserstoff§vodík
das§Biogas§bioplyn
die§Solarenergie§solární energie
die§Windenergie§větrná energie
die§Wasserkraft§vodní energie
die§Kernenergie§jaderná energie
die§Geothermie§geotermální energie
die§Wärme§teplo
die§Abwärme§odpadní teplo
das§Heizsystem§topný systém
die§Heizung§topení
der§Heizkessel§kotel
der§Brenner§hořák
der§Boiler§bojler
der§Warmwasserspeicher§zásobník teplé vody
die§Wärmepumpe§tepelné čerpadlo
die§Fernwärme§dálkové vytápění
die§Kühlung§chlazení
die§Kälteanlage§chladicí zařízení
das§Kühlaggregat§chladicí jednotka
die§Kühlanlage§chladicí zařízení
die§Abwasseranlage§čistírenské zařízení
die§Kläranlage§čistírna odpadních vod
die§Wasseraufbereitung§úprava vody
das§Trinkwasser§pitná voda
das§Abwasser§odpadní voda
die§Wasserleitung§vodovodní potrubí
das§Abwasserrohr§kanalizační potrubí
der§Wasserhahn§vodovodní kohoutek
das§Abflussrohr§odpadní potrubí
der§Abfluss§odtok
der§Siphon§sifon
der§Wasserdruck§tlak vody
der§Wasserspeicher§vodojem
das§Regenwasser§dešťová voda
die§Regenwasserleitung§dešťová kanalizace
die§Pumpe§čerpadlo
die§Tauchpumpe§ponorné čerpadlo
die§Umwälzpumpe§oběhové čerpadlo
die§Druckerhöhungspumpe§tlakové čerpadlo

# Büro, Handel und Finanzen
die§Buchhaltung§účetnictví
die§Bilanz§rozvaha
die§Gewinnrechnung§výkaz zisku
die§Verlustrechnung§výkaz ztráty
die§Gewinnspanne§marže
die§Umsatzsteuer§daň z přidané hodnoty
die§Mehrwertsteuer§daň z přidané hodnoty
die§Einkommensteuer§daň z příjmu
die§Körperschaftsteuer§daň z příjmů právnických osob
die§Gewerbesteuer§daň z podnikání
die§Steuererklärung§daňové přiznání
die§Steuerprüfung§daňová kontrola
die§Steuerlast§daňové zatížení
die§Steuerbehörde§finanční úřad
das§Finanzamt§finanční úřad
die§Finanzabteilung§finanční oddělení
die§Finanzplanung§finanční plánování
die§Finanzierung§financování
die§Investition§investice
das§Investment§investice
die§Rendite§výnos
der§Ertrag§výnos
der§Umsatz§obrat
der§Gewinn§zisk
der§Verlust§ztráta
die§Bilanzsumme§rozvaha celkem
das§Eigenkapital§vlastní kapitál
das§Fremdkapital§cizí kapitál
das§Vermögen§majetek
die§Verbindlichkeit§závazek
die§Forderung§pohledávka
die§Verbindlichkeit§závazek
die§Liquidität§likvidita
der§Cashflow§cash flow
die§Zahlung§platba
die§Vorauszahlung§záloha
die§Anzahlung§záloha
die§Rückzahlung§vrácení peněz
die§Erstattung§náhrada
die§Überweisung§bankovní převod
der§Dauerauftrag§trvalý příkaz
die§Lastschrift§inkaso
die§Barzahlung§platba v hotovosti
die§Kartenzahlung§platba kartou
die§Rechnung§faktura
die§Sammelrechnung§souhrnná faktura
die§Gutschrift§dobropis
die§Mahnung§upomínka
die§Zahlungsfrist§splatnost
das§Zahlungsziel§termín splatnosti
der§Zahlungseingang§příchozí platba
der§Zahlungsausgang§odchozí platba
das§Bankkonto§bankovní účet
das§Geschäftskonto§firemní účet
das§Sparkonto§spořicí účet
das§Tagesgeldkonto§spořicí účet
das§Depot§investiční účet
das§Wertpapier§cenný papír
die§Aktie§akcie
die§Anleihe§dluhopis
der§Fonds§fond
der§Investmentfonds§investiční fond
der§Indexfonds§indexový fond
der§ETF§ETF
die§Dividende§dividenda
der§Aktienkurs§kurz akcie
der§Wechselkurs§směnný kurz
die§Währung§měna
der§Geldschein§bankovka
die§Banknote§bankovka
die§Münze§mince
die§Kreditkarte§kreditní karta
die§Debitkarte§debetní karta
die§EC-Karte§bankovní karta
der§Chip§čip
der§Magnetstreifen§magnetický proužek
das§Kartenlesegerät§čtečka karet
der§Geldautomat§bankomat
die§Bankfiliale§pobočka banky
der§Schalter§přepážka
die§Kasse§pokladna
die§Kassiererin§pokladní
der§Kassierer§pokladní
die§Quittung§účtenka
der§Kassenbon§účtenka
die§Bestellung§objednávka
der§Auftrag§zakázka
die§Bestellnummer§číslo objednávky
die§Auftragsnummer§číslo zakázky
die§Lieferantenrechnung§faktura dodavatele
die§Kundenrechnung§faktura zákazníkovi
die§Auftragsbestätigung§potvrzení objednávky
der§Lieferschein§dodací list
die§Gutschrift§dobropis
der§Kostenvoranschlag§cenová nabídka
das§Angebot§nabídka
der§Preisvergleich§porovnání cen
die§Preisliste§ceník
der§Listenpreis§katalogová cena
der§Nettopreis§cena bez DPH
der§Bruttopreis§cena s DPH
der§Einkaufspreis§nákupní cena
der§Verkaufspreis§prodejní cena
der§Stückpreis§kusová cena
der§Gesamtpreis§celková cena
der§Grundpreis§základní cena
der§Aufpreis§příplatek
der§Nachlass§sleva
der§Mengenrabatt§množstevní sleva
der§Skonto§skonto
die§Provision§provize
die§Gebühr§poplatek
die§Bankgebühr§bankovní poplatek
die§Kontoführungsgebühr§poplatek za vedení účtu
die§Versicherung§pojištění
die§Lebensversicherung§životní pojištění
die§Haftpflichtversicherung§pojištění odpovědnosti
die§Kfz-Versicherung§pojištění vozidla
die§Hausratversicherung§pojištění domácnosti
die§Rechtsschutzversicherung§pojištění právní ochrany
die§Reiseversicherung§cestovní pojištění
die§Versicherungspolice§pojistná smlouva
der§Versicherungsbeitrag§pojistné
die§Schadenmeldung§hlášení škody
der§Schadensfall§pojistná událost
die§Schadenshöhe§výše škody
die§Schadensregulierung§likvidace škody
der§Versicherer§pojistitel
der§Versicherungsnehmer§pojistník
der§Versicherte§pojištěný
die§Versicherte§pojištěná


"""

private val vocabularyRowsPart2 = """
"""

internal val vocabularyCatalog: List<VocabularyEntry> = run {
    val existing = articleCategories.flatMap { category ->
        category.words.map { word ->
            VocabularyEntry(
                id = stableReviewId("vocabulary", word.article, word.word.lowercase()),
                article = word.article,
                word = word.word,
                czech = word.czech
            )
        }
    }
    (existing + parseVocabularyRows(vocabularyRowsPart1 + vocabularyRowsPart2))
        .distinctBy { it.word.trim().lowercase() }
        .take(2000)
}
