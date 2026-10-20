package com.deutschai.app

private fun turn(
    promptDe: String,
    promptCs: String,
    vararg options: GuidedDialogueOption
) = GuidedDialogueTurn(promptDe, promptCs, options.toList())

private fun option(
    answerDe: String,
    answerCs: String,
    replyDe: String,
    replyCs: String
) = GuidedDialogueOption(answerDe, answerCs, replyDe, replyCs)

internal val additionalGuidedDialogueTopics = listOf(
    GuidedDialogueTopic(
        "bahnhof",
        "Na nádraží",
        "Jízdenky, nástupiště, zpoždění a přestupy · A2",
        listOf(
            turn("Guten Morgen. Wohin möchten Sie fahren?", "Dobré ráno. Kam chcete jet?",
                option("Ich möchte nach Wien fahren, weil ich dort am Nachmittag einen Geschäftstermin habe.", "Chci jet do Vídně, protože tam mám odpoledne pracovní schůzku.", "Dann empfehle ich den Zug um neun Uhr, der normalerweise direkt fährt und kurz nach elf ankommt.", "Doporučuji tedy vlak v devět hodin, který obvykle jede přímo a přijíždí krátce po jedenácté."),
                option("Ich fahre nach Berlin und möchte unterwegs möglichst nicht umsteigen, obwohl die direkte Verbindung etwas länger dauert.", "Jedu do Berlína a během cesty bych pokud možno nechtěl/a přestupovat, přestože přímý spoj trvá trochu déle.", "Ich suche eine Verbindung heraus, bei der Sie sitzen bleiben können und Ihr Gepäck nicht wechseln müssen.", "Najdu spoj, ve kterém můžete zůstat sedět a nemusíte přenášet zavazadla."),
                option("Ich möchte nach München, aber ich bin flexibel, sofern ein anderer Zug günstiger ist und rechtzeitig ankommt.", "Chci jet do Mnichova, ale jsem flexibilní, pokud je jiný vlak levnější a přijede včas.", "Dann vergleichen wir die Preise und Ankunftszeiten, damit Sie eine passende Verbindung auswählen können.", "Porovnáme tedy ceny a časy příjezdu, abyste si mohl/a vybrat vhodný spoj.")
            ),
            turn("Möchten Sie eine einfache Fahrt oder eine Rückfahrkarte?", "Přejete si jednosměrnou, nebo zpáteční jízdenku?",
                option("Eine Rückfahrkarte, bitte, weil ich am Sonntag wieder nach Hause fahren werde.", "Zpáteční jízdenku, prosím, protože v neděli pojedu zase domů.", "In Ordnung, ich wähle eine Rückfahrt am Sonntag, und Sie können die genaue Uhrzeit später noch prüfen.", "Dobře, zvolím návrat v neděli a přesný čas si můžete později ještě ověřit."),
                option("Nur eine einfache Fahrt, denn ich weiß noch nicht, wann ich zurückkomme.", "Jen jednosměrnou, protože ještě nevím, kdy se vrátím.", "Das ist kein Problem; sobald Ihre Pläne feststehen, können Sie die Rückfahrt separat buchen.", "To není problém; až budete znát své plány, můžete si zpáteční cestu rezervovat zvlášť."),
                option("Ich nehme eine Tageskarte, sofern ich damit auch die Regionalzüge benutzen darf.", "Vezmu si celodenní jízdenku, pokud s ní smím používat i regionální vlaky.", "Ich prüfe die Bedingungen, damit Sie später nicht versehentlich einen Zuschlag bezahlen müssen.", "Ověřím podmínky, abyste později nemusel/a omylem platit příplatek.")
            ),
            turn("Der Zug fährt heute von Gleis sieben ab. Ist das klar?", "Vlak dnes odjíždí z nástupiště sedm. Je to jasné?",
                option("Ja, ich gehe gleich zu Gleis sieben, damit ich rechtzeitig einsteigen kann.", "Ano, hned půjdu na nástupiště sedm, abych mohl/a včas nastoupit.", "Sehr gut, und achten Sie bitte auf die Anzeigen, falls sich das Gleis kurzfristig ändert.", "Výborně a sledujte prosím informační tabule, kdyby se nástupiště na poslední chvíli změnilo."),
                option("Könnten Sie mir erklären, wie ich zu Gleis sieben komme, weil ich mich hier noch nicht auskenne?", "Mohl/a byste mi vysvětlit, jak se dostanu na nástupiště sedm, protože to tu ještě neznám?", "Gehen Sie geradeaus bis zur Unterführung und nehmen Sie danach die Treppe auf der linken Seite.", "Jděte rovně k podchodu a potom použijte schody na levé straně."),
                option("Ich habe es verstanden, obwohl ich mich lieber noch einmal auf der Anzeigetafel vergewissern möchte.", "Rozumím, i když bych si to raději ještě jednou ověřil/a na informační tabuli.", "Das ist vernünftig, denn dort finden Sie auch Hinweise zu möglichen Verspätungen.", "To je rozumné, protože tam najdete i informace o případných zpožděních.")
            ),
            turn("Ihr Zug hat zwanzig Minuten Verspätung. Möchten Sie eine andere Verbindung nehmen?", "Váš vlak má dvacet minut zpoždění. Chcete jet jiným spojem?",
                option("Ja, bitte, wenn ich dadurch meinen Termin noch erreichen kann.", "Ano, prosím, pokud díky tomu ještě stihnu schůzku.", "Ich sehe nach, ob ein schnellerer Anschluss fährt, der Sie rechtzeitig nach Wien bringt.", "Podívám se, zda jede rychlejší spoj, který vás dopraví do Vídně včas."),
                option("Ich bleibe lieber bei diesem Zug, weil ich keine Lust auf einen komplizierten Umstieg habe.", "Raději zůstanu u tohoto vlaku, protože se mi nechce složitě přestupovat.", "Das verstehe ich, und ich erkläre Ihnen gern, wie Sie bei einer Verspätung Ihre Rechte prüfen können.", "Rozumím a rád/a vám vysvětlím, jak si při zpoždění ověřit svá práva."),
                option("Ich nehme die andere Verbindung, sofern mein Ticket dafür gültig bleibt und ich keinen Aufpreis zahlen muss.", "Vezmu jiný spoj, pokud na něj zůstane jízdenka platná a nebudu muset platit příplatek.", "Ich prüfe das zuerst, damit Sie nicht versehentlich ein zusätzliches Ticket kaufen.", "Nejprve to ověřím, abyste si omylem nekupoval/a další jízdenku.")
            ),
            turn("Kann ich Ihnen sonst noch helfen?", "Mohu vám ještě nějak pomoci?",
                option("Können Sie mir bitte sagen, wo ich etwas zu essen kaufen kann, bevor ich einsteige?", "Můžete mi prosím říct, kde si mohu koupit něco k jídlu, než nastoupím?", "Natürlich, im Erdgeschoss gibt es eine Bäckerei, die bis zum Abend geöffnet bleibt.", "Samozřejmě, v přízemí je pekárna, která zůstává otevřená až do večera."),
                option("Nein, vielen Dank, jetzt weiß ich, wohin ich gehen muss, und werde rechtzeitig am Bahnsteig sein.", "Ne, děkuji, teď už vím, kam jít, a budu včas na nástupišti.", "Gern geschehen, und ich wünsche Ihnen eine angenehme Fahrt und einen erfolgreichen Tag.", "Rádo se stalo, přeji vám příjemnou cestu a úspěšný den."),
                option("Ja, ich möchte wissen, ob ich mein Fahrrad im Zug mitnehmen darf, damit ich am Zielort mobil bin.", "Ano, chci vědět, zda si smím vzít kolo do vlaku, abych se v cíli mohl/a pohybovat.", "Ich informiere mich über die Fahrradregeln, weil sie je nach Zug unterschiedlich sein können.", "Zjistím pravidla pro jízdní kola, protože se mohou podle vlaku lišit.")
            )
        )
    ),
    GuidedDialogueTopic(
        "hotel",
        "V hotelu",
        "Rezervace, pokoj, snídaně a řešení problémů · A2",
        listOf(
            turn("Guten Abend. Haben Sie eine Reservierung?", "Dobrý večer. Máte rezervaci?",
                option("Ja, ich habe ein Zimmer auf den Namen Novák reserviert, weil ich drei Nächte bleiben werde.", "Ano, rezervoval/a jsem pokoj na jméno Novák, protože zůstanu tři noci.", "Vielen Dank, ich prüfe Ihre Reservierung und bereite die Schlüsselkarte für Sie vor.", "Děkuji, ověřím rezervaci a připravím vám kartu od pokoje."),
                option("Leider nicht, aber ich suche ein ruhiges Einzelzimmer, in dem ich auch arbeiten kann.", "Bohužel ne, ale hledám klidný jednolůžkový pokoj, ve kterém mohu také pracovat.", "Wir haben noch ein Zimmer zum Innenhof, das einen kleinen Schreibtisch und kostenloses WLAN bietet.", "Máme ještě pokoj do dvora, který má malý pracovní stůl a bezplatnou Wi-Fi."),
                option("Die Reservierung wurde über eine Webseite gemacht, auf der ich auch das Frühstück ausgewählt habe.", "Rezervace byla vytvořena přes web, na kterém jsem vybral/a i snídani.", "Ich suche die Buchung heraus, damit wir prüfen können, ob alle Wünsche eingetragen wurden.", "Najdu rezervaci, abychom mohli ověřit, zda jsou zapsána všechna přání.")
            ),
            turn("Möchten Sie ein Zimmer mit Frühstück?", "Přejete si pokoj se snídaní?",
                option("Ja, bitte, weil ich morgens lieber im Hotel frühstücke, bevor ich losgehe.", "Ano, prosím, protože ráno raději snídám v hotelu, než vyrazím.", "Sehr gern, das Frühstück wird zwischen sieben und zehn Uhr serviert, damit Sie genug Zeit haben.", "Rád/a, snídaně se podává mezi sedmou a desátou, abyste měl/a dost času."),
                option("Nein, danke, denn ich möchte morgens die Cafés in der Umgebung ausprobieren.", "Ne, děkuji, protože chci ráno vyzkoušet kavárny v okolí.", "Das ist eine gute Idee, und ich kann Ihnen einige Cafés empfehlen, die früh öffnen.", "To je dobrý nápad a mohu vám doporučit několik kaváren, které otevírají brzy."),
                option("Ist es möglich, das Frühstück nur für zwei der drei Tage zu buchen, wenn ich am dritten Tag früh abreise?", "Je možné objednat snídani jen na dva ze tří dnů, pokud třetí den odjíždím brzy?", "Ich prüfe gern, ob wir die Frühstückstage getrennt berechnen können.", "Rád/a ověřím, zda můžeme snídaně účtovat po jednotlivých dnech.")
            ),
            turn("Entspricht der Raum Ihren Erwartungen?", "Odpovídá pokoj vašim očekáváním?",
                option("Ja, er ist hell und ruhig, sodass ich mich hier gut auf meine Arbeit konzentrieren kann.", "Ano, je světlý a klidný, takže se tu mohu dobře soustředit na práci.", "Das freut mich, und wenn Sie noch Handtücher brauchen, geben Sie einfach an der Rezeption Bescheid.", "To mě těší a pokud budete potřebovat další ručníky, stačí říct na recepci."),
                option("Leider ist die Heizung sehr laut, weshalb ich nachts wahrscheinlich schlecht schlafen würde.", "Bohužel je topení velmi hlučné, takže bych v noci pravděpodobně špatně spal/a.", "Es tut mir leid, ich lasse die Heizung überprüfen und kann Ihnen bis dahin ein anderes Zimmer anbieten.", "Omlouvám se, nechám topení zkontrolovat a do té doby vám mohu nabídnout jiný pokoj."),
                option("Das Zimmer gefällt mir, obwohl ich gern wissen würde, ob es auch einen Wasserkocher gibt.", "Pokoj se mi líbí, i když bych rád/a věděl/a, zda je zde také rychlovarná konvice.", "Ich bringe Ihnen gern einen Wasserkocher, wenn wir noch einen im Lager haben.", "Rád/a vám přinesu rychlovarnou konvici, pokud máme ještě jednu ve skladu.")
            ),
            turn("Wann möchten Sie morgen auschecken?", "Kdy se chcete zítra odhlásit?",
                option("Ich würde gern um acht Uhr auschecken, weil mein Zug kurz nach neun fährt.", "Rád/a bych se odhlásil/a v osm, protože můj vlak odjíždí krátce po deváté.", "Das ist kein Problem, und ich kann Ihnen die Rechnung schon heute Abend vorbereiten.", "To není problém a účet vám mohu připravit už dnes večer."),
                option("Ich weiß es noch nicht genau, deshalb wäre ein später Check-out hilfreich.", "Ještě to přesně nevím, proto by se hodilo pozdější odhlášení.", "Ich prüfe, ob das Zimmer frei bleibt, damit wir Ihnen eine spätere Abreise ermöglichen können.", "Ověřím, zda pokoj zůstane volný, abychom vám mohli umožnit pozdější odjezd."),
                option("Könnte ich meinen Koffer nach dem Check-out noch hier lassen, während ich die Stadt besichtige?", "Mohl/a bych si po odhlášení ještě nechat kufr zde, zatímco si prohlédnu město?", "Natürlich, wir bewahren Ihr Gepäck sicher auf, bis Sie es am Nachmittag abholen.", "Samozřejmě, vaše zavazadlo bezpečně uschováme, dokud si ho odpoledne nevyzvednete.")
            ),
            turn("Haben Sie Ihren Aufenthalt genossen?", "Užil/a jste si pobyt?",
                option("Ja, besonders das freundliche Personal hat dazu beigetragen, dass ich mich wohlgefühlt habe.", "Ano, zejména milý personál přispěl k tomu, že jsem se cítil/a dobře.", "Vielen Dank für das nette Feedback, und wir würden uns freuen, Sie wieder begrüßen zu dürfen.", "Děkujeme za milou zpětnou vazbu a rádi vás znovu přivítáme."),
                option("Im Großen und Ganzen ja, obwohl das WLAN am ersten Abend nicht funktioniert hat.", "Celkově ano, i když první večer nefungovala Wi-Fi.", "Es tut mir leid, dass es am Anfang Probleme gab, und ich gebe Ihre Rückmeldung an die Technik weiter.", "Mrzí mě, že se zpočátku objevily problémy, a předám vaši připomínku technikům."),
                option("Ja, ich würde das Hotel empfehlen, weil es sauber ist und eine gute Lage hat.", "Ano, hotel bych doporučil/a, protože je čistý a má dobrou polohu.", "Das freut uns sehr, und wir hoffen, dass Sie auch bei Ihrer nächsten Reise an uns denken.", "To nás velmi těší a doufáme, že na nás budete myslet i při příští cestě.")
            )
        )
    ),
    GuidedDialogueTopic(
        "arbeit",
        "V práci",
        "Schůzka, úkoly, termíny a spolupráce · B1",
        listOf(
            turn("Guten Morgen. Haben Sie die Unterlagen für die Besprechung vorbereitet?", "Dobré ráno. Připravil/a jste podklady na poradu?",
                option("Ja, ich habe die wichtigsten Zahlen zusammengestellt, damit wir die Entscheidung auf einer guten Grundlage treffen können.", "Ano, sestavil/a jsem nejdůležitější čísla, abychom mohli rozhodnout na dobrém základě.", "Vielen Dank, ich sehe mir die Unterlagen vor Beginn an und notiere offene Fragen, die wir gemeinsam klären sollten.", "Děkuji, před začátkem si podklady prohlédnu a zapíšu otevřené otázky, které bychom měli společně vyřešit."),
                option("Fast, aber ich warte noch auf eine Rückmeldung vom Lieferanten, ohne die der Vergleich nicht vollständig wäre.", "Téměř, ale ještě čekám na odpověď dodavatele, bez které by porovnání nebylo úplné.", "Dann erklären wir kurz, welche Angaben fehlen, und legen fest, wann wir die endgültige Bewertung abschließen können.", "Krátce tedy vysvětlíme, které údaje chybí, a stanovíme, kdy můžeme dokončit konečné hodnocení."),
                option("Ich habe eine kurze Zusammenfassung vorbereitet, weil die ausführlichen Tabellen für die erste Diskussion zu umfangreich wären.", "Připravil/a jsem krátké shrnutí, protože podrobné tabulky by pro první diskusi byly příliš obsáhlé.", "Das ist sinnvoll, denn wir können die Details anschließend prüfen, wenn die Richtung bereits klar ist.", "To dává smysl, protože podrobnosti můžeme ověřit později, až bude jasný směr.")
            ),
            turn("Welche Punkte sollten wir zuerst besprechen?", "Které body bychom měli probrat jako první?",
                option("Ich würde mit den Kosten beginnen, weil sie den größten Einfluss auf die Entscheidung haben.", "Začal/a bych náklady, protože mají na rozhodnutí největší vliv.", "Einverstanden, und danach vergleichen wir die Risiken, damit wir nicht nur nach dem niedrigsten Preis entscheiden.", "Souhlasím a potom porovnáme rizika, abychom se nerozhodovali pouze podle nejnižší ceny."),
                option("Meiner Meinung nach sollten wir zuerst die Qualität bewerten, bevor wir über Einsparungen sprechen.", "Podle mého názoru bychom měli nejprve vyhodnotit kvalitu, než začneme mluvit o úsporách.", "Das ist ein guter Ansatz, denn eine kurzfristige Einsparung kann langfristig höhere Kosten verursachen.", "To je dobrý přístup, protože krátkodobá úspora může dlouhodobě způsobit vyšší náklady."),
                option("Ich schlage vor, dass wir die Lieferfähigkeit prüfen, da ein günstiges Angebot wenig hilft, wenn der Termin nicht eingehalten wird.", "Navrhuji prověřit schopnost dodávat, protože levná nabídka moc nepomůže, pokud nebude dodržen termín.", "Richtig, wir sollten Preis, Qualität und Termin gemeinsam betrachten, bevor wir eine Empfehlung aussprechen.", "Správně, měli bychom společně posoudit cenu, kvalitu a termín, než vydáme doporučení.")
            ),
            turn("Der aktuelle Termin ist sehr knapp. Wie können wir das lösen?", "Aktuální termín je velmi napjatý. Jak to můžeme vyřešit?",
                option("Wir könnten die Aufgaben parallel bearbeiten, sofern alle Beteiligten dieselben Informationen erhalten.", "Mohli bychom úkoly řešit souběžně, pokud všichni zúčastnění dostanou stejné informace.", "Ich koordiniere die Zuständigkeiten, damit keine Arbeit doppelt gemacht wird und wir den Überblick behalten.", "Zkoordinuji odpovědnosti, aby se práce nedělala dvakrát a abychom měli přehled."),
                option("Ich würde den Lieferanten direkt anrufen, weil wir auf eine E-Mail möglicherweise zu lange warten müssten.", "Dodavateli bych rovnou zavolal/a, protože na e-mail bychom možná čekali příliš dlouho.", "Das klingt pragmatisch, und ich bereite die konkreten Fragen vor, damit wir im Gespräch keine Zeit verlieren.", "To zní pragmaticky a připravím konkrétní otázky, abychom během hovoru neztráceli čas."),
                option("Wir sollten den Umfang reduzieren, falls sich der ursprüngliche Termin unter den aktuellen Bedingungen nicht halten lässt.", "Měli bychom zmenšit rozsah, pokud za současných podmínek nelze původní termín dodržet.", "Dann dokumentieren wir, welche Punkte unverändert bleiben müssen und welche wir in eine zweite Phase verschieben können.", "Zdokumentujeme tedy, které body musí zůstat beze změny a které lze přesunout do druhé fáze.")
            ),
            turn("Ein Kollege ist mit dem Vorschlag nicht einverstanden. Was sagen Sie?", "Kolega s návrhem nesouhlasí. Co řeknete?",
                option("Ich möchte verstehen, welche Risiken Sie sehen, bevor wir gemeinsam nach einer besseren Lösung suchen.", "Chci pochopit, jaká rizika vidíte, než společně najdeme lepší řešení.", "Das ist ein guter Weg, denn sachliche Einwände können helfen, Schwachstellen früh zu erkennen.", "To je dobrý postup, protože věcné námitky mohou pomoci odhalit slabá místa včas."),
                option("Ich erkläre noch einmal die Zahlen, obwohl ich weiß, dass wir unterschiedliche Prioritäten haben.", "Ještě jednou vysvětlím čísla, i když vím, že máme odlišné priority.", "Dann vergleichen wir die Kriterien offen, damit jeder nachvollziehen kann, warum wir eine Entscheidung treffen.", "Otevřeně tedy porovnáme kritéria, aby každý mohl pochopit, proč rozhodujeme právě tak."),
                option("Vielleicht sollten wir eine dritte Möglichkeit prüfen, die sowohl die Kosten als auch die technischen Anforderungen berücksichtigt.", "Možná bychom měli prověřit třetí možnost, která zohlední náklady i technické požadavky.", "Ich sammle die Vorschläge und erstelle einen Vergleich, damit wir die Alternativen fair beurteilen können.", "Shromáždím návrhy a připravím porovnání, abychom mohli alternativy spravedlivě vyhodnotit.")
            ),
            turn("Können Sie die Ergebnisse bis morgen zusammenfassen?", "Můžete do zítřka shrnout výsledky?",
                option("Ja, ich schicke Ihnen bis morgen Mittag eine Zusammenfassung, in der die offenen Punkte klar gekennzeichnet sind.", "Ano, do zítřejšího poledne vám pošlu shrnutí, ve kterém budou jasně označené otevřené body.", "Perfekt, dann können wir die wichtigsten Fragen rechtzeitig klären, bevor die Entscheidung getroffen wird.", "Výborně, pak můžeme nejdůležitější otázky vyřešit včas, než se rozhodne."),
                option("Ich kann eine erste Version vorbereiten, aber einige Zahlen muss ich noch mit der Buchhaltung abstimmen.", "Mohu připravit první verzi, ale některá čísla musím ještě sladit s účetnictvím.", "Das reicht für den Anfang, solange klar ist, welche Werte noch bestätigt werden müssen.", "To pro začátek stačí, pokud bude jasné, které hodnoty je ještě třeba potvrdit."),
                option("Ich übernehme die Zusammenfassung, wenn ein Kollege parallel die technischen Daten final überprüft.", "Shrnutí připravím, pokud kolega současně definitivně ověří technické údaje.", "Einverstanden, ich kläre die Unterstützung ab, damit Sie sich auf die Auswertung konzentrieren können.", "Souhlasím, zajistím podporu, abyste se mohl/a soustředit na vyhodnocení.")
            )
        )
    ),
    GuidedDialogueTopic(
        "arzt",
        "U lékaře",
        "Popis potíží, doporučení a další postup · A2",
        listOf(
            turn("Guten Tag. Was führt Sie heute zu uns?", "Dobrý den. Co vás dnes přivádí?",
                option("Ich habe seit drei Tagen Halsschmerzen, und seit gestern bekomme ich auch schlecht Luft durch die Nase.", "Už tři dny mě bolí v krku a od včerejška se mi také špatně dýchá nosem.", "Ich stelle Ihnen ein paar Fragen, damit wir besser einschätzen können, welche Behandlung sinnvoll ist.", "Položím vám několik otázek, abychom lépe posoudili, jaká léčba je vhodná."),
                option("Ich fühle mich ständig müde, obwohl ich in letzter Zeit eigentlich genug geschlafen habe.", "Cítím se pořád unavený/á, přestože jsem v poslední době vlastně dost spal/a.", "Seit wann besteht die Müdigkeit, und hat sich in Ihrem Alltag etwas verändert, das damit zusammenhängen könnte?", "Jak dlouho únava trvá a změnilo se ve vašem každodenním životě něco, co by s tím mohlo souviset?"),
                option("Ich habe seit gestern starke Bauchschmerzen, die nach dem Essen noch schlimmer werden.", "Od včerejška mám silné bolesti břicha, které se po jídle ještě zhoršují.", "Wir sollten das sorgfältig untersuchen, damit wir entscheiden können, ob weitere Tests notwendig sind.", "Měli bychom to pečlivě vyšetřit, abychom rozhodli, zda jsou nutná další vyšetření.")
            ),
            turn("Haben Sie Fieber oder andere Beschwerden bemerkt?", "Máte horečku nebo jste si všiml/a jiných potíží?",
                option("Gestern hatte ich leichtes Fieber, aber heute ist die Temperatur etwas niedriger.", "Včera jsem měl/a mírnou horečku, ale dnes je teplota o něco nižší.", "Gut, dass Sie die Temperatur notiert haben, denn diese Information hilft uns, den Verlauf besser zu beurteilen.", "Je dobře, že jste si teplotu poznamenal/a, protože nám to pomůže lépe posoudit průběh."),
                option("Nein, ich habe kein Fieber, allerdings bin ich ungewöhnlich erschöpft und kann mich kaum konzentrieren.", "Ne, nemám horečku, ale jsem nezvykle vyčerpaný/á a téměř se nemohu soustředit.", "Dann prüfen wir auch andere mögliche Ursachen, weil nicht jede Erkrankung mit Fieber einhergeht.", "Prověříme také jiné možné příčiny, protože ne každá nemoc je doprovázena horečkou."),
                option("Ich habe außerdem Husten, der nachts stärker wird und mich häufig aufweckt.", "Mám také kašel, který se v noci zhoršuje a často mě budí.", "Das sollten wir berücksichtigen, und ich werde Ihre Atmung abhören, bevor wir über die nächsten Schritte sprechen.", "To bychom měli zohlednit a poslechnu vám dýchání, než probereme další postup.")
            ),
            turn("Nehmen Sie regelmäßig Medikamente?", "Užíváte pravidelně nějaké léky?",
                option("Ich nehme jeden Morgen ein Medikament gegen Allergien, das mir normalerweise gut hilft.", "Každé ráno užívám lék na alergii, který mi obvykle dobře pomáhá.", "Bitte nennen Sie mir den Namen, damit wir prüfen können, ob es Wechselwirkungen geben könnte.", "Řekněte mi prosím název, abychom ověřili, zda by mohly nastat interakce."),
                option("Nein, ich nehme keine Medikamente, weil ich bisher keine chronischen Krankheiten hatte.", "Ne, neužívám žádné léky, protože jsem dosud neměl/a chronická onemocnění.", "Gut, ich notiere das, und falls wir etwas verschreiben, erkläre ich Ihnen die Anwendung genau.", "Dobře, poznamenám si to a pokud něco předepíšeme, přesně vám vysvětlím užívání."),
                option("Ich habe vor zwei Tagen ein Schmerzmittel genommen, dessen Namen ich gerade nicht mehr weiß.", "Před dvěma dny jsem si vzal/a lék proti bolesti, jehož název si teď už nepamatuji.", "Wenn Sie die Packung zu Hause finden, bringen Sie bitte ein Foto davon zum nächsten Termin mit.", "Pokud doma najdete balení, přineste prosím příště jeho fotografii.")
            ),
            turn("Ich empfehle Ihnen, sich auszuruhen und ausreichend zu trinken. Haben Sie dazu Fragen?", "Doporučuji vám odpočívat a dostatečně pít. Máte k tomu otázky?",
                option("Wie lange sollte ich zu Hause bleiben, bevor ich wieder Sport treibe?", "Jak dlouho bych měl/a zůstat doma, než se znovu vrátím ke sportu?", "Warten Sie, bis die Beschwerden deutlich besser sind, und steigern Sie die Belastung anschließend langsam.", "Počkejte, až se potíže výrazně zlepší, a potom zátěž zvyšujte postupně."),
                option("Gibt es Anzeichen, bei denen ich sofort wiederkommen oder medizinische Hilfe suchen sollte?", "Existují příznaky, při kterých bych se měl/a ihned vrátit nebo vyhledat lékařskou pomoc?", "Ja, wenn Sie deutlich schlechter Luft bekommen, starke Schmerzen entwickeln oder sich Ihr Zustand rasch verschlechtert, suchen Sie bitte sofort Hilfe.", "Ano, pokud se vám výrazně zhorší dýchání, objeví se silné bolesti nebo se stav rychle zhorší, ihned vyhledejte pomoc."),
                option("Kann ich weiterarbeiten, wenn ich mich morgen besser fühle, oder sollte ich lieber einen Tag länger warten?", "Mohu dál pracovat, pokud mi zítra bude lépe, nebo bych měl/a raději počkat ještě den?", "Entscheiden Sie nach Ihrem Befinden, und vermeiden Sie zusätzliche Belastung, solange Sie sich noch deutlich krank fühlen.", "Rozhodněte se podle svého stavu a vyhněte se další zátěži, dokud se stále cítíte výrazně nemocný/á.")
            ),
            turn("Haben Sie noch etwas, das Sie ansprechen möchten?", "Ještě něco, co byste chtěl/a zmínit?",
                option("Ja, ich möchte wissen, ob ich mich bei meinen Kollegen angesteckt haben könnte, weil mehrere von ihnen krank sind.", "Ano, chci vědět, zda jsem se mohl/a nakazit od kolegů, protože několik z nich je nemocných.", "Das ist möglich, deshalb sollten Sie sich ausruhen und engen Kontakt vermeiden, solange die Beschwerden anhalten.", "To je možné, proto odpočívejte a vyhýbejte se blízkému kontaktu, dokud potíže trvají."),
                option("Nein, danke, Sie haben alles erklärt, was ich wissen wollte, und ich werde Ihre Empfehlungen befolgen.", "Ne, děkuji, vysvětlil/a jste mi vše, co jsem chtěl/a vědět, a budu se řídit doporučeními.", "Gern geschehen, und melden Sie sich bitte, wenn die Beschwerden nicht besser werden oder neue Symptome auftreten.", "Rádo se stalo a ozvěte se, pokud se potíže nezlepší nebo se objeví nové příznaky."),
                option("Ich möchte nur sicher sein, dass ich die Dosierung richtig verstanden habe, bevor ich die Medikamente nehme.", "Chci si jen ověřit, že jsem správně pochopil/a dávkování, než začnu léky užívat.", "Natürlich, wir gehen die Dosierung noch einmal gemeinsam durch, damit keine Unklarheiten bleiben.", "Samozřejmě, dávkování ještě jednou společně projdeme, aby nezůstaly žádné nejasnosti.")
            )
        )
    ),
    GuidedDialogueTopic(
        "freizeit",
        "Plány s přáteli",
        "Pozvání, domluva, změna plánu a reakce · A2–B1",
        listOf(
            turn("Hast du am Samstag schon etwas vor?", "Máš už něco v plánu na sobotu?",
                option("Ich habe noch nichts geplant, weil ich die Woche ziemlich anstrengend fand.", "Ještě nic neplánuji, protože mi tento týden připadal docela náročný.", "Dann könnten wir etwas Entspanntes unternehmen, bei dem wir trotzdem ein bisschen Zeit draußen verbringen.", "Mohli bychom tedy podniknout něco odpočinkového, při čemž přesto strávíme trochu času venku."),
                option("Ich wollte eigentlich wandern gehen, aber ich könnte den Ausflug verschieben, wenn du Zeit hast.", "Původně jsem chtěl/a jít na túru, ale výlet bych mohl/a přesunout, pokud máš čas.", "Das wäre schön, denn ich wollte schon lange eine neue Strecke ausprobieren, die nicht zu schwierig ist.", "To by bylo hezké, protože už dlouho chci vyzkoušet novou trasu, která není příliš náročná."),
                option("Am Nachmittag besuche ich meine Eltern, aber am Abend bin ich wieder frei.", "Odpoledne navštívím rodiče, ale večer už mám volno.", "Dann treffen wir uns am Abend, damit du vorher genug Zeit für deine Familie hast.", "Sejdeme se tedy večer, abys měl/a předtím dost času na rodinu.")
            ),
            turn("Worauf hättest du Lust?", "Na co bys měl/a chuť?",
                option("Wir könnten ins Kino gehen, sofern ein Film läuft, den wir beide sehen möchten.", "Mohli bychom jít do kina, pokud dávají film, který chceme vidět oba.", "Ich schaue nach dem Programm und schicke dir zwei Vorschläge, damit wir gemeinsam entscheiden können.", "Podívám se na program a pošlu ti dva návrhy, abychom se mohli společně rozhodnout."),
                option("Ich würde lieber draußen sitzen und etwas essen, weil ich bei dem Wetter nicht den ganzen Abend drinnen verbringen möchte.", "Raději bych seděl/a venku a něco jedl/a, protože při takovém počasí nechci strávit celý večer uvnitř.", "Dann reserviere ich einen Tisch auf der Terrasse, falls es am Abend nicht zu kalt wird.", "Zarezervuji tedy stůl na terase, pokud večer nebude příliš chladno."),
                option("Lass uns zusammen kochen, denn wir haben das schon lange nicht mehr gemacht.", "Pojďme společně vařit, protože už jsme to dlouho nedělali.", "Gute Idee, und jeder kann etwas mitbringen, damit wir neue Rezepte ausprobieren können.", "Dobrý nápad a každý může něco přinést, abychom mohli vyzkoušet nové recepty.")
            ),
            turn("Um wie viel Uhr sollen wir uns treffen?", "V kolik hodin se máme sejít?",
                option("Wie wäre es um sechs, damit wir genug Zeit haben, bevor es dunkel wird?", "Co třeba v šest, abychom měli dost času, než se setmí?", "Das passt mir gut, und ich komme etwas früher, falls wir noch etwas vorbereiten müssen.", "To mi vyhovuje a přijdu o něco dříve, kdybychom ještě museli něco připravit."),
                option("Ich schaffe es wahrscheinlich erst um halb sieben, weil ich vorher noch einen Termin habe.", "Pravděpodobně to stihnu až v půl sedmé, protože mám předtím ještě schůzku.", "Kein Problem, ich warte auf dich, und wir können schon einmal einen Platz suchen.", "Žádný problém, počkám na tebe a můžeme zatím najít místo."),
                option("Am Nachmittag wäre mir lieber, denn dann könnten wir noch einen Spaziergang machen.", "Raději bych se sešel/sešla odpoledne, protože bychom potom mohli jít ještě na procházku.", "Dann treffen wir uns um drei, sofern das für alle anderen auch in Ordnung ist.", "Sejdeme se tedy ve tři, pokud to vyhovuje i všem ostatním.")
            ),
            turn("Es sieht nach Regen aus. Sollen wir den Plan ändern?", "Vypadá to na déšť. Máme změnit plán?",
                option("Wir könnten ins Museum gehen, das wir schon lange besuchen wollten, obwohl wir dafür Karten brauchen.", "Mohli bychom jít do muzea, které jsme chtěli už dlouho navštívit, i když na to potřebujeme vstupenky.", "Ich prüfe die Öffnungszeiten und reserviere Karten, wenn es noch Plätze gibt.", "Ověřím otevírací dobu a rezervuji vstupenky, pokud jsou ještě místa."),
                option("Lass uns trotzdem spazieren gehen, wenn der Regen nicht zu stark wird und wir Regenschirme mitnehmen.", "Pojďme se přesto projít, pokud déšť nebude příliš silný a vezmeme si deštníky.", "Einverstanden, aber wir können spontan in ein Café gehen, falls das Wetter unangenehm wird.", "Souhlasím, ale kdyby počasí bylo nepříjemné, můžeme spontánně zajít do kavárny."),
                option("Wir bleiben lieber zu Hause, weil ich nach dieser Woche ohnehin etwas Ruhe brauche.", "Raději zůstaneme doma, protože po tomto týdnu stejně potřebuji trochu klidu.", "Das verstehe ich, und wir können uns nächste Woche treffen, wenn du dich besser erholt hast.", "Rozumím a můžeme se sejít příští týden, až si lépe odpočineš.")
            ),
            turn("Danke für den schönen Nachmittag. Wollen wir das wiederholen?", "Děkuji za hezké odpoledne. Zopakujeme si to?",
                option("Sehr gern, denn ich fand es schön, dass wir endlich wieder in Ruhe miteinander reden konnten.", "Velmi rád/a, protože bylo hezké, že jsme spolu konečně mohli v klidu zase mluvit.", "Das freut mich, und beim nächsten Mal können wir etwas unternehmen, das wir beide noch nicht ausprobiert haben.", "To mě těší a příště můžeme podniknout něco, co jsme oba ještě nevyzkoušeli."),
                option("Ja, vielleicht nächsten Samstag, sofern wir beide noch keinen anderen Termin haben.", "Ano, možná příští sobotu, pokud ani jeden nebudeme mít jiný program.", "Ich trage es mir vorläufig ein und wir bestätigen die Uhrzeit später.", "Předběžně si to zapíšu a čas si potvrdíme později."),
                option("Auf jeden Fall, und ich bringe diesmal ein Rezept mit, das ich unbedingt ausprobieren möchte.", "Určitě a tentokrát přinesu recept, který bych chtěl/a určitě vyzkoušet.", "Darauf freue ich mich, denn gemeinsam kochen macht immer mehr Spaß als allein.", "Na to se těším, protože společné vaření je vždy zábavnější než vařit o samotě.")
            )
        )
    ),
    GuidedDialogueTopic(
        "telefon",
        "Telefonování",
        "Volání do firmy, předání zprávy a domluva termínu · B1",
        listOf(
            turn("Guten Tag, hier ist die Firma Müller. Wie kann ich Ihnen helfen?", "Dobrý den, zde firma Müller. Jak vám mohu pomoci?",
                option("Guten Tag, mein Name ist Novák, und ich rufe an, weil ich mit Frau Schneider über das Angebot sprechen möchte.", "Dobrý den, jmenuji se Novák a volám, protože chci s paní Schneiderovou mluvit o nabídce.", "Einen Moment bitte, ich prüfe, ob Frau Schneider gerade erreichbar ist, und verbinde Sie gegebenenfalls.", "Moment prosím, ověřím, zda je paní Schneiderová dostupná, a případně vás spojím."),
                option("Ich habe gestern eine E-Mail geschickt, auf die ich gern eine kurze Rückmeldung bekommen würde.", "Včera jsem poslal/a e-mail, ke kterému bych rád/a dostal/a krátkou odpověď.", "Verstanden, ich notiere Ihre Anfrage und leite sie an die zuständige Kollegin weiter.", "Rozumím, poznamenám váš požadavek a předám ho odpovědné kolegyni."),
                option("Ich möchte einen Termin vereinbaren, da wir die technischen Einzelheiten noch persönlich besprechen müssen.", "Chci domluvit schůzku, protože technické podrobnosti musíme ještě probrat osobně.", "Gern, ich sehe im Kalender nach, welche Termine in dieser Woche noch frei sind.", "Rád/a se podívám do kalendáře, které termíny jsou tento týden ještě volné.")
            ),
            turn("Frau Schneider ist gerade in einer Besprechung. Möchten Sie eine Nachricht hinterlassen?", "Paní Schneiderová je právě na poradě. Chcete zanechat zprávu?",
                option("Ja, sagen Sie ihr bitte, dass ich heute Nachmittag zurückrufe, falls sie dann Zeit hat.", "Ano, řekněte jí prosím, že dnes odpoledne zavolám znovu, pokud bude mít čas.", "Natürlich, ich gebe die Nachricht weiter und bitte sie, sich bei Ihnen zu melden, falls sich etwas ändert.", "Samozřejmě, zprávu předám a požádám ji, aby se vám ozvala, pokud se něco změní."),
                option("Bitte teilen Sie ihr mit, dass die aktualisierte Datei bereits in ihrem Postfach liegt.", "Sdělte jí prosím, že aktualizovaný soubor už má ve schránce.", "Mache ich, und ich erwähne, dass Sie für Rückfragen erreichbar sind.", "Vyřídím to a zmíním, že jste dostupný/á pro případné dotazy."),
                option("Ich rufe später noch einmal an, weil die Angelegenheit nicht dringend ist.", "Zavolám později znovu, protože to není naléhavé.", "In Ordnung, dann wünsche ich Ihnen einen angenehmen Tag und richte nichts Weiteres aus.", "Dobře, přeji hezký den a nic dalšího předávat nebudu.")
            ),
            turn("Welche Telefonnummer kann ich notieren?", "Jaké telefonní číslo si mohu poznamenat?",
                option("Sie erreichen mich unter der Nummer, die auch in meiner letzten E-Mail steht.", "Dovoláte se mi na čísle, které je uvedené také v mém posledním e-mailu.", "Danke, ich prüfe die Kontaktdaten, damit ich keine falsche Nummer weitergebe.", "Děkuji, ověřím kontaktní údaje, abych nepředal/a špatné číslo."),
                option("Ich gebe Ihnen die Nummer langsam durch, damit Sie jede Ziffer richtig notieren können.", "Nadiktuji vám číslo pomalu, abyste mohl/a správně zapsat každou číslici.", "Sehr gut, ich lese die Nummer anschließend noch einmal vor, damit wir sie überprüfen können.", "Výborně, potom číslo ještě jednou přečtu, abychom je mohli ověřit."),
                option("Sie können mich am besten vormittags erreichen, weil ich nachmittags häufig in Besprechungen bin.", "Nejlépe mě zastihnete dopoledne, protože odpoledne bývám často na poradách.", "Ich notiere den Hinweis, damit Ihre Kollegin möglichst zu einer passenden Zeit zurückruft.", "Poznamenám si to, aby vám kolegyně zavolala zpět pokud možno ve vhodnou dobu.")
            ),
            turn("Passt Ihnen Dienstag um zehn Uhr?", "Vyhovuje vám úterý v deset hodin?",
                option("Ja, der Termin passt, und ich schicke Ihnen vorher noch die Unterlagen, damit wir direkt anfangen können.", "Ano, termín mi vyhovuje a předem vám pošlu podklady, abychom mohli rovnou začít.", "Vielen Dank, ich sende Ihnen eine Bestätigung, in der auch die Teilnehmer und der Ort stehen.", "Děkuji, pošlu vám potvrzení, ve kterém budou i účastníci a místo."),
                option("Leider kann ich am Dienstag nicht, aber am Mittwochvormittag wäre ich flexibel.", "V úterý bohužel nemohu, ale ve středu dopoledne bych měl/a čas.", "Ich prüfe den Kalender noch einmal und schlage Ihnen eine passende Alternative vor.", "Znovu zkontroluji kalendář a navrhnu vám vhodnou alternativu."),
                option("Wäre es möglich, das Gespräch online zu führen, weil ich an diesem Tag nicht in der Stadt bin?", "Bylo by možné uskutečnit schůzku online, protože ten den nebudu ve městě?", "Das sollte möglich sein, und ich schicke Ihnen einen Link, sobald ich den Termin bestätigt habe.", "To by mělo být možné a po potvrzení termínu vám pošlu odkaz.")
            ),
            turn("Kann ich sonst noch etwas für Sie tun?", "Mohu pro vás udělat ještě něco?",
                option("Nein, vielen Dank, Sie haben mir sehr geholfen und alle wichtigen Informationen erklärt.", "Ne, děkuji, velmi jste mi pomohl/a a vysvětlil/a všechny důležité informace.", "Gern geschehen, und ich wünsche Ihnen ein erfolgreiches Gespräch am Dienstag.", "Rádo se stalo a přeji úspěšnou schůzku v úterý."),
                option("Könnten Sie mir bitte auch die direkte E-Mail-Adresse von Frau Schneider nennen, sofern das erlaubt ist?", "Mohl/a byste mi také sdělit přímou e-mailovou adresu paní Schneiderové, pokud je to dovoleno?", "Ich darf Ihnen die allgemeine Kontaktadresse geben, über die Ihre Nachricht zuverlässig weitergeleitet wird.", "Mohu vám dát obecnou kontaktní adresu, přes kterou bude vaše zpráva spolehlivě předána."),
                option("Bitte sagen Sie Frau Schneider, dass sie mich jederzeit zurückrufen kann, sobald sie etwas Zeit findet.", "Řekněte prosím paní Schneiderové, že mi může zavolat zpět, až bude mít chvíli čas.", "Das richte ich aus, und ich wünsche Ihnen noch einen angenehmen Nachmittag.", "Vyřídím to a přeji příjemné odpoledne.")
            )
        )
    ),
    GuidedDialogueTopic(
        "wohnung",
        "Bydlení a pronájem",
        "Prohlídka bytu, náklady a závady · B1",
        listOf(
            turn("Guten Tag, Sie interessieren sich für die Wohnung?", "Dobrý den, máte zájem o byt?",
                option("Ja, ich suche eine Wohnung, die nicht weit vom Zentrum entfernt ist, weil ich täglich zur Arbeit fahren muss.", "Ano, hledám byt nedaleko centra, protože musím denně dojíždět do práce.", "Dann könnte diese Wohnung passen, denn die Straßenbahn hält nur wenige Minuten entfernt.", "Tento byt by vám mohl vyhovovat, protože tramvajová zastávka je vzdálená jen několik minut."),
                option("Ich möchte erst die Zimmer sehen, bevor ich entscheide, ob die Wohnung für meine Familie geeignet ist.", "Nejprve bych chtěl/a vidět pokoje, než se rozhodnu, zda je byt vhodný pro mou rodinu.", "Natürlich, ich zeige Ihnen alle Räume und erkläre, welche Möbel der Vormieter zurücklassen wird.", "Samozřejmě, ukážu vám všechny místnosti a vysvětlím, jaký nábytek tu předchozí nájemník nechá."),
                option("Die Anzeige hat mich angesprochen, weil der Balkon groß ist und die Wohnung viele Fenster hat.", "Inzerát mě zaujal, protože balkon je velký a byt má mnoho oken.", "Das stimmt, und die Wohnung ist besonders am Nachmittag hell, wenn die Sonne auf den Balkon scheint.", "To je pravda a byt je obzvlášť světlý odpoledne, když na balkon svítí slunce.")
            ),
            turn("Wie viele Personen würden hier wohnen?", "Kolik lidí by zde bydlelo?",
                option("Wir wären zwei Erwachsene und ein Kind, das ein eigenes kleines Zimmer braucht.", "Byli bychom dva dospělí a jedno dítě, které potřebuje vlastní malý pokoj.", "Dann ist die Aufteilung praktisch, weil das kleinere Zimmer als Kinderzimmer genutzt werden kann.", "Rozvržení je tedy praktické, protože menší pokoj lze využít jako dětský."),
                option("Ich würde allein einziehen, da ich momentan näher an meinem Arbeitsplatz wohnen möchte.", "Nastěhoval/a bych se sám/sama, protože chci momentálně bydlet blíž pracovišti.", "Das dürfte gut passen, und Sie hätten im Wohnzimmer genug Platz für einen Schreibtisch.", "To by mohlo vyhovovat a v obývacím pokoji byste měl/a dost místa na pracovní stůl."),
                option("Wir sind eine ruhige Wohngemeinschaft, in der jeder arbeitet oder studiert.", "Jsme klidná spolubydlící skupina, ve které každý pracuje nebo studuje.", "Ich kläre mit dem Vermieter, ob eine Wohngemeinschaft für diese Wohnung möglich ist.", "Ověřím u majitele, zda je pro tento byt možné spolubydlení.")
            ),
            turn("Die Nebenkosten kommen zur Miete hinzu. Ist das für Sie in Ordnung?", "Poplatky se připočítávají k nájmu. Je to pro vás přijatelné?",
                option("Ja, wenn die monatlichen Gesamtkosten im Vertrag klar aufgeführt werden.", "Ano, pokud budou celkové měsíční náklady jasně uvedené ve smlouvě.", "Selbstverständlich, wir zeigen Ihnen die Aufstellung, bevor Sie irgendetwas unterschreiben.", "Samozřejmě, přehled vám ukážeme, než cokoli podepíšete."),
                option("Ich würde gern wissen, welche Kosten bereits enthalten sind, damit ich mein Budget genau planen kann.", "Rád/a bych věděl/a, které náklady jsou už zahrnuté, abych mohl/a přesně plánovat rozpočet.", "Ich erkläre Ihnen die einzelnen Positionen und markiere, welche Ausgaben zusätzlich entstehen können.", "Vysvětlím vám jednotlivé položky a označím, které výdaje mohou vzniknout navíc."),
                option("Das klingt etwas teuer, weshalb ich gern auch eine kleinere Wohnung vergleichen würde.", "To zní trochu draze, proto bych rád/a porovnal/a i menší byt.", "Das ist vernünftig, und ich kann Ihnen eine günstigere Wohnung in derselben Gegend zeigen.", "To je rozumné a mohu vám ukázat levnější byt ve stejné oblasti.")
            ),
            turn("Wann könnten Sie einziehen?", "Kdy byste se mohl/a nastěhovat?",
                option("Am ersten September, sofern die Reparaturen bis dahin abgeschlossen sind.", "Prvního září, pokud do té doby budou dokončeny opravy.", "Ich bestätige den Termin, sobald der Handwerker den Abschluss der Arbeiten zugesagt hat.", "Potvrdím termín, jakmile řemeslník potvrdí dokončení prací."),
                option("So bald wie möglich, denn mein bisheriger Mietvertrag endet schon nächste Woche.", "Co nejdříve, protože mi současná nájemní smlouva končí už příští týden.", "Ich frage den Vermieter, ob eine frühere Übergabe möglich wäre, ohne dass wichtige Dokumente fehlen.", "Zeptám se majitele, zda je možné předání dříve, aniž by chyběly důležité dokumenty."),
                option("Ich könnte flexibel einziehen, wenn wir den Vertrag mindestens zwei Wochen vorher vorbereiten.", "Mohl/a bych se nastěhovat flexibilně, pokud smlouvu připravíme alespoň dva týdny předem.", "Das sollte machbar sein, und wir vereinbaren einen Termin, an dem Sie alles in Ruhe lesen können.", "To by mělo být možné a domluvíme termín, kdy si budete moci vše v klidu přečíst.")
            ),
            turn("Haben Sie noch Fragen zur Wohnung?", "Máte ještě otázky k bytu?",
                option("Wer ist für Reparaturen zuständig, wenn später zum Beispiel die Heizung nicht funktioniert?", "Kdo odpovídá za opravy, pokud později například nebude fungovat topení?", "Im Vertrag steht, wie solche Fälle gemeldet werden, und ich zeige Ihnen die entsprechende Kontaktstelle.", "Ve smlouvě je uvedeno, jak se takové případy hlásí, a ukážu vám příslušný kontakt."),
                option("Darf ich ein kleines Regal an der Wand befestigen, wenn ich die Bohrlöcher beim Auszug wieder schließe?", "Smím připevnit na zeď malou polici, pokud při vystěhování otvory zase opravím?", "Das sollten wir mit dem Vermieter abstimmen, damit später keine Missverständnisse entstehen.", "Měli bychom to dohodnout s majitelem, aby později nevznikla nedorozumění."),
                option("Ich würde gern eine Nacht darüber schlafen, bevor ich mich endgültig entscheide.", "Rád/a bych se na to ještě vyspal/a, než se definitivně rozhodnu.", "Natürlich, nehmen Sie sich Zeit, und melden Sie sich, sobald Sie eine Entscheidung getroffen haben.", "Samozřejmě, dejte si čas a ozvěte se, jakmile se rozhodnete.")
            )
        )
    )
)

internal val guidedDialogueTopics = starterGuidedDialogueTopics + additionalGuidedDialogueTopics
