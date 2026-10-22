package com.deutschai.app

// Additional original reading stories. Each text is stored independently so its
// language and difficulty can be checked without shared filler paragraphs.
internal val readingArticlesA1Extra = listOf(
    ReadingArticle(
        id = "a1-verlorene-schluessel",
        level = "A1",
        title = "Der verlorene Schlüssel",
        subtitle = "Paul sucht seinen Schlüssel und lernt eine neue Nachbarin kennen.",
        body = """
            Paul wohnt seit zwei Monaten in einer kleinen Wohnung. Jeden Morgen fährt er mit dem Bus zur Arbeit. Heute hat er es eilig, denn um acht Uhr beginnt eine wichtige Besprechung. Er zieht seine Jacke an, nimmt seine Tasche und sucht seinen Schlüssel. Auf dem Tisch liegt er nicht. Paul schaut in die Küche, ins Schlafzimmer und sogar unter das Sofa. Der Schlüssel ist weg.

            Paul wird nervös. Ohne Schlüssel kann er die Wohnung später nicht abschließen. Er ruft seine Kollegin an und sagt, dass er vielleicht ein paar Minuten zu spät kommt. Dann sucht er noch einmal in seiner Tasche. Dort findet er ein Buch, sein Portemonnaie und zwei alte Fahrkarten, aber keinen Schlüssel.

            Im Treppenhaus trifft Paul seine Nachbarin Frau Keller. Sie trägt eine Einkaufstasche und sieht, dass Paul unruhig ist. Er erklärt ihr das Problem. Frau Keller fragt, ob er gestern mit dem Fahrrad nach Hause gefahren ist. Paul denkt kurz nach. Ja, gestern ist er mit dem Fahrrad gefahren und hat vor dem Haus sein Licht repariert.

            Die beiden gehen nach draußen. Neben dem Fahrradständer liegt nichts. Frau Keller schaut zum kleinen Garten hinter dem Haus. Dort steht eine Bank, auf der Paul am Abend oft sitzt. Neben der Bank liegt etwas Metall im Gras. Es ist tatsächlich sein Schlüssel.

            Paul ist erleichtert und bedankt sich bei Frau Keller. Er fragt, wie er ihr helfen kann. Sie lächelt und sagt, dass sie am Nachmittag einen schweren Karton tragen muss. Paul verspricht, nach der Arbeit bei ihr vorbeizukommen.

            Bei der Besprechung kommt Paul gerade noch rechtzeitig an. In der Mittagspause schreibt er seiner Nachbarin eine Nachricht und bestätigt den Termin. Am Nachmittag hilft er ihr mit dem Karton. Darin sind Bücher und Teller für ihre neue Küche. Danach trinken sie zusammen Tee.

            Als Paul am Abend nach Hause geht, legt er seinen Schlüssel in eine kleine Schale neben die Tür. So findet er ihn am nächsten Morgen schneller. Er denkt an den Tag und lächelt. Wegen eines verlorenen Schlüssels hat er eine freundliche Nachbarin besser kennengelernt.
        """.trimIndent(),
        glossary = mapOf(
            "eilig" to "spěchat / ve spěchu",
            "Besprechung" to "porada",
            "Treppenhaus" to "schodiště",
            "unruhig" to "neklidný",
            "erleichtert" to "ulevený"
        )
    ),
    ReadingArticle(
        id = "a1-bibliothek",
        level = "A1",
        title = "Ein Nachmittag in der Bibliothek",
        subtitle = "Mila sucht ein Buch, findet Hilfe und entdeckt ein neues Hobby.",
        body = """
            Mila lernt seit einem Jahr Deutsch. Sie kann schon kurze Texte lesen, aber lange Geschichten sind noch schwierig. Am Mittwoch hat sie am Nachmittag frei. Sie möchte ein Buch finden, das nicht zu schwer ist. Deshalb fährt sie mit dem Bus zur Stadtbibliothek.

            Die Bibliothek ist groß und ruhig. Gleich am Eingang steht ein Tisch mit neuen Büchern. Mila sieht viele bunte Umschläge, weiß aber nicht, welches Buch zu ihr passt. Eine Mitarbeiterin kommt zu ihr und fragt, ob sie Hilfe braucht. Mila erklärt, dass sie einfache Geschichten auf Deutsch lesen möchte.

            Die Mitarbeiterin zeigt ihr ein Regal mit Büchern für Lernende. Auf den Büchern stehen kleine Buchstaben: A1, A2 und B1. Mila wählt ein Buch über eine Familie, die auf dem Land lebt. Es hat viele Bilder und kurze Kapitel. Sie setzt sich an einen Tisch am Fenster und beginnt zu lesen.

            Nach zehn Minuten versteht Mila die Geschichte gut. Einige Wörter kennt sie noch nicht. Sie schreibt sie in ein kleines Heft und liest den Satz noch einmal. Wenn sie den Sinn versteht, macht sie neben dem Wort ein kleines Zeichen. So kann sie später die wichtigsten Wörter wiederholen.

            Neben Mila sitzt ein älterer Mann und liest eine Zeitung. Er bemerkt das Heft und fragt, ob sie Deutsch lernt. Er erzählt, dass er früher Deutsch in der Schule gelernt hat und jetzt in einem Sprachcafé freiwillig hilft. Dort sprechen Menschen aus verschiedenen Ländern miteinander. Mila findet die Idee interessant und fragt nach dem nächsten Treffen.

            Der Mann schreibt ihr die Adresse und die Uhrzeit auf einen Zettel. Das Treffen ist am Samstag um drei Uhr. Mila steckt den Zettel in ihr Buch, damit sie ihn nicht verliert. Danach leiht sie die Geschichte für zwei Wochen aus.

            Zu Hause liest Mila noch ein Kapitel. Sie versteht nicht jedes Wort, aber sie kann der Handlung folgen. Am Samstag besucht sie das Sprachcafé. Dort lernt sie zwei neue Menschen kennen und spricht eine halbe Stunde Deutsch. Sie macht Fehler, doch alle hören geduldig zu.

            Am Abend ist Mila müde und zufrieden. Sie hat ein neues Buch, neue Wörter und eine neue Möglichkeit gefunden, Deutsch zu üben. Sie beschließt, jede Woche in die Bibliothek zu gehen.
        """.trimIndent(),
        glossary = mapOf(
            "Stadtbibliothek" to "městská knihovna",
            "Umschlag" to "obálka",
            "Lernende" to "studenti / lidé, kteří se učí",
            "ausleihen" to "vypůjčit si",
            "Handlung" to "děj"
        )
    )
)

internal val readingArticlesA2Extra = listOf(
    ReadingArticle(
        id = "a2-tauschschrank",
        level = "A2",
        title = "Der Tauschschrank im Haus",
        subtitle = "Ein leerer Schrank wird zu einem Treffpunkt für die Nachbarn.",
        body = """
            Als Nora in ein neues Wohnhaus zog, kannte sie dort fast niemanden. Sie grüßte die Menschen im Treppenhaus, aber meistens gingen alle schnell weiter. Eines Tages las sie am Eingang einen Zettel der Hausverwaltung. Im Erdgeschoss stand ein alter Schrank, den niemand mehr brauchte. Die Bewohner durften ihn gemeinsam nutzen, wenn sie eine gute Idee dafür hatten.

            Nora schlug vor, einen Tauschschrank daraus zu machen. Jeder konnte saubere Dinge hineinstellen, die er nicht mehr brauchte, und etwas anderes mitnehmen. Bücher, Tassen, kleine Spiele und Zimmerpflanzen waren erlaubt. Kleidung sollte nur dann hineinkommen, wenn sie gewaschen und in gutem Zustand war.

            Am Samstag traf Nora zwei Nachbarn, die ihr beim Putzen des Schranks halfen. Herr Yilmaz brachte ein Regalbrett mit, und Jana aus dem dritten Stock schrieb einfache Regeln auf ein Blatt Papier. Sie beschlossen, keine kaputten Geräte und keine Lebensmittel in den Schrank zu legen. Außerdem sollte jeder nur so viel mitnehmen, wie er wirklich gebrauchen konnte.

            In den ersten Tagen passierte nicht viel. Eine Person stellte zwei Romane hinein, und jemand anderes nahm eine Tasse mit Blumen darauf. Am Mittwoch entdeckte Nora eine kleine Schachtel mit Buntstiften. Sie legte sie auf den unteren Boden, damit Kinder sie leicht erreichen konnten. Am nächsten Morgen lag dort ein selbst gemaltes Bild mit einem großen roten Haus. Daneben stand: Danke!

            Nach einer Woche kamen mehr Menschen vorbei. Eine Familie brachte ein Brettspiel, das ihre Kinder nicht mehr spielten. Eine Studentin legte Kochbücher hinein. Herr Yilmaz fand einen passenden Blumentopf für seine Küche. Die Bewohner begannen, nicht nur Dinge zu tauschen, sondern auch miteinander zu sprechen.

            Natürlich gab es auch ein Problem. Eines Abends war der Schrank so voll, dass sich die Tür nicht mehr schließen ließ. Einige Dinge lagen auf dem Boden. Nora machte ein Foto und schrieb in die Hausgruppe, dass der Schrank etwas Ordnung brauchte. Am nächsten Samstag halfen mehrere Nachbarn beim Sortieren. Sie brachten zu viel gebrachte Sachen zu einer gemeinnützigen Stelle.

            Beim Aufräumen schlug Jana vor, einmal im Monat einen kleinen Nachbarschaftsnachmittag zu organisieren. Jeder konnte etwas zu essen oder zu trinken mitbringen. Der erste Nachmittag fand im Innenhof statt. Obwohl es am Morgen geregnet hatte, kam die Sonne heraus. Die Kinder spielten, die Erwachsenen tauschten Rezepte aus und zwei ältere Bewohner erzählten Geschichten aus dem Viertel.

            Nora merkte, dass sie inzwischen viele Namen kannte. Der Tauschschrank war für sie nicht nur ein Ort für gebrauchte Dinge geworden. Er half den Menschen im Haus, sich kennenzulernen und einander im Alltag zu unterstützen.
        """.trimIndent(),
        glossary = mapOf(
            "Hausverwaltung" to "správa domu",
            "Bewohner" to "obyvatelé",
            "in gutem Zustand" to "v dobrém stavu",
            "gemeinnützig" to "neziskový / prospěšný veřejnosti",
            "Innenhof" to "vnitroblok"
        )
    ),
    ReadingArticle(
        id = "a2-fotowettbewerb",
        level = "A2",
        title = "Der kleine Fotowettbewerb",
        subtitle = "Ein Klassenprojekt zeigt den Schülern ihre Stadt aus einer neuen Perspektive.",
        body = """
            In Leons Deutschkurs hatte die Lehrerin eine Aufgabe vorbereitet. Jeder sollte ein Foto von einem Ort machen, der ihm wichtig war. In der nächsten Woche wollten sie die Bilder in der Klasse zeigen und auf Deutsch erklären, warum sie diesen Ort gewählt hatten. Leon dachte zuerst an den großen Platz im Zentrum. Dort war er oft mit Freunden, und es gab viele schöne Gebäude.

            Am Samstag nahm er seine Kamera und ging in die Stadt. Auf dem Platz waren viele Menschen, und vor einem Café standen Tische in der Sonne. Leon machte einige Fotos, aber keines gefiel ihm richtig. Alles sah aus wie auf den Bildern, die er schon kannte. Deshalb ging er weiter, bis er zu einem kleinen Park am Fluss kam.

            Dort saß ein älterer Mann auf einer Bank und fütterte keine Vögel, sondern las ihnen laut aus einer Zeitung vor. In der Nähe spielte ein Mädchen mit einem roten Ball. Ein Radfahrer hielt kurz an, um einem anderen Menschen den Weg zu erklären. Leon fand, dass dieser Ort das alltägliche Leben seiner Stadt zeigte. Er machte ein Foto von der Brücke, den Bäumen und den Menschen im Hintergrund.

            Zu Hause wählte Leon das Bild aus und schrieb drei Sätze dazu. Er erklärte, dass er den Park mochte, weil man dort Ruhe finden konnte, obwohl die Stadt nicht weit entfernt war. Er schrieb auch, dass verschiedene Menschen den Ort auf unterschiedliche Weise nutzten. Seine Schwester las den Text und half ihm, zwei Sätze klarer zu formulieren.

            In der Klasse zeigten alle ihre Fotos. Ein Schüler hatte den Bahnhof gewählt, weil er dort jeden Morgen seine Freunde traf. Eine andere Schülerin präsentierte einen kleinen Laden, in dem ihre Familie seit Jahren Brot kaufte. Die Lehrerin bat die Klasse, nach jeder Präsentation eine Frage zu stellen. So sprachen die Schüler nicht nur über ihre Bilder, sondern übten auch, aufmerksam zuzuhören.

            Als Leon an der Reihe war, war er zuerst nervös. Dann sah er auf sein Foto und begann zu erzählen. Er sprach langsam und machte eine kleine Pause, wenn ihm ein Wort fehlte. Seine Mitschüler fragten, ob der Park auch im Winter schön sei und ob er oft dort spazieren gehe. Leon beantwortete beide Fragen und merkte, dass er sicherer sprach als erwartet.

            Am Ende wählte die Klasse kein einziges Gewinnerfoto. Die Lehrerin sagte, dass jedes Bild etwas anderes über das Leben in der Stadt erzählte. Leon war damit zufrieden. Auf dem Heimweg beschloss er, öfter mit offenen Augen durch die Straßen zu gehen. Manchmal musste man nicht weit reisen, um etwas Interessantes zu entdecken.
        """.trimIndent(),
        glossary = mapOf(
            "Wettbewerb" to "soutěž",
            "Perspektive" to "pohled / perspektiva",
            "alltäglich" to "každodenní",
            "Präsentation" to "prezentace",
            "Mitschüler" to "spolužáci"
        )
    )
)

internal val readingArticlesB1Extra = listOf(
    ReadingArticle(
        id = "b1-parkprojekt",
        level = "B1",
        title = "Ein Projekt für den alten Park",
        subtitle = "Eine Nachbarschaft muss entscheiden, wie ein öffentlicher Ort genutzt werden soll.",
        body = """
            Als die Stadt ankündigte, den alten Park im Viertel neu zu gestalten, waren viele Bewohner zunächst begeistert. Die Wege waren beschädigt, einige Bänke waren kaum noch zu benutzen, und an heißen Tagen fehlte Schatten. Doch sobald die ersten Pläne veröffentlicht wurden, zeigte sich, dass die Menschen sehr unterschiedliche Vorstellungen hatten. Während Familien einen Spielplatz forderten, wünschten sich ältere Bewohner mehr ruhige Sitzplätze. Jugendliche wollten einen Platz für Basketball, und mehrere Anwohner befürchteten, dass neue Veranstaltungen zu laut werden könnten.

            Die Stadt beauftragte eine kleine Arbeitsgruppe, Vorschläge zu sammeln. Zu ihr gehörte auch Lena, die als Landschaftsplanerin arbeitete und in der Nähe aufgewachsen war. Sie wollte verhindern, dass die Diskussion zu einem Streit zwischen einzelnen Gruppen wurde. Deshalb organisierte sie einen Spaziergang durch den Park. Die Teilnehmer sollten zeigen, welche Orte sie im Alltag nutzten und an welchen Stellen sie Probleme sahen.

            Während des Spaziergangs fiel auf, dass manche Schwierigkeiten bisher kaum erwähnt worden waren. Ein schmaler Weg war für Menschen mit Kinderwagen schwer zu befahren. An einer Kreuzung übersahen Radfahrer häufig die Fußgänger. Außerdem stand eine Bank direkt in der Sonne, obwohl sie gerade von älteren Menschen oft gebraucht wurde. Lena schrieb alle Beobachtungen auf, ohne sofort zu versprechen, dass jede Forderung umgesetzt werden könne.

            Beim nächsten Treffen stellte die Arbeitsgruppe drei Varianten vor. Die erste war günstig, bot aber nur wenige neue Möglichkeiten. Die zweite enthielt einen größeren Spielbereich und mehr Bäume, kostete jedoch deutlich mehr. Die dritte sah zusätzliche Sportflächen vor, ließ dafür weniger Raum für ruhige Erholung. Sofort begannen die Teilnehmer, einzelne Punkte zu kritisieren. Ein Vater meinte, dass die Stadt zu wenig für Kinder tue. Eine Anwohnerin antwortete, dass sie im Sommer nicht bei offenem Fenster schlafen könne, wenn dort regelmäßig gespielt werde.

            Lena unterbrach die Diskussion und schlug vor, zunächst die gemeinsamen Ziele festzuhalten. Fast alle wollten sichere Wege, saubere Grünflächen und einen Park, der für verschiedene Altersgruppen geeignet war. Darüber hinaus waren die Meinungen verschieden. Diese Unterschiede verschwanden nicht, aber die Gruppe konnte nun gezielter darüber sprechen, welche Lösung möglichst vielen Bedürfnissen gerecht werden würde.

            Schließlich entstand ein Kompromiss. Der Spielbereich sollte nicht direkt neben den Wohnhäusern liegen. Zusätzliche Bäume würden im Laufe mehrerer Jahre Schatten schaffen, und die Sportfläche sollte so geplant werden, dass sie auch für kleinere Veranstaltungen genutzt werden konnte. Für die Beleuchtung und die Wege wurden klare Prioritäten festgelegt. Einige Wünsche mussten verschoben werden, weil das Budget begrenzt war.

            Drei Monate nach Baubeginn besuchte Lena den Park erneut. Noch waren nicht alle Arbeiten abgeschlossen, aber Familien nutzten bereits den neuen Weg, und zwei ältere Nachbarn saßen auf einer frisch gestrichenen Bank. Ein Jugendlicher erklärte ihr, dass die geplante Sportfläche etwas kleiner ausfiel als erhofft. Trotzdem fand er es gut, dass die Gruppe überhaupt mitreden durfte.

            Lena wusste, dass ein öffentlicher Raum niemals allen Menschen auf dieselbe Weise gefallen würde. Entscheidend war für sie, dass die Beteiligten ihre Gründe erklären und die Interessen anderer ernst nehmen konnten. Der Park war nicht perfekt geworden. Aber die Menschen hatten erlebt, dass eine schwierige Entscheidung besser ausfallen konnte, wenn man sie gemeinsam vorbereitete.
        """.trimIndent(),
        glossary = mapOf(
            "gestalten" to "utvářet / upravovat",
            "Anwohner" to "místní obyvatelé",
            "Arbeitsgruppe" to "pracovní skupina",
            "Kompromiss" to "kompromis",
            "gerecht werden" to "vyhovět / dostát něčemu"
        )
    ),
    ReadingArticle(
        id = "b1-nachricht",
        level = "B1",
        title = "Die Nachricht, die alles veränderte",
        subtitle = "Ein Missverständnis im Team wird erst durch ein offenes Gespräch gelöst.",
        body = """
            Als David am Montagmorgen sein E-Mail-Postfach öffnete, fand er eine kurze Nachricht seiner Kollegin Nina. Sie schrieb, dass sie die Präsentation am Freitag nicht übernehmen könne und dass er bitte selbst mit dem Kunden sprechen solle. David war überrascht. Sie hatten die Aufgaben vor einer Woche verteilt, und Nina hatte damals zugesagt, den Termin vorzubereiten. Er las die Nachricht noch einmal und ärgerte sich darüber, dass sie ihn so spät informierte.

            In der folgenden Besprechung erwähnte David, dass er nun die Präsentation übernehmen müsse. Er klang dabei gereizter, als er beabsichtigt hatte. Nina antwortete knapp, sie habe bereits erklärt, warum sie den Termin nicht wahrnehmen könne. Danach wandte sie sich ihrem Computer zu. Die anderen Kollegen wechselten einen Blick, aber niemand sagte etwas. David beschloss, die Sache erst einmal zu vergessen und die Präsentation selbst vorzubereiten.

            Am Nachmittag stellte er fest, dass ihm wichtige Informationen fehlten. Nina hatte die letzten Gespräche mit dem Kunden geführt, und einige Anforderungen waren nur in ihren Notizen dokumentiert. David schrieb ihr eine weitere Nachricht. Er bat sie, ihm die Unterlagen bis zum nächsten Morgen zu schicken. Nina antwortete, dass sie alles im gemeinsamen Ordner abgelegt habe. Als David nachsah, fand er dort jedoch nur eine ältere Version.

            Er dachte kurz darüber nach, ob er sich bei der Teamleitung beschweren sollte. Dann bemerkte er, dass beide Nachrichten sehr kurz gewesen waren und dass sie vielleicht aneinander vorbeigeredet hatten. Am nächsten Tag bat er Nina um ein Gespräch. Er wollte verstehen, was passiert war, bevor er weitere Schlussfolgerungen zog.

            Nina erklärte, dass ihre Mutter am Wochenende unerwartet ins Krankenhaus gekommen war. Sie hatte deshalb den Freitag freinehmen müssen und wollte David nicht unter Druck setzen. Ihre Nachricht sollte keine Kritik an ihm sein; sie hatte nur schnell Bescheid geben wollen. Die neue Präsentation lag auf ihrem privaten Arbeitslaufwerk, und sie hatte vergessen, sie in den gemeinsamen Ordner zu kopieren. David verstand jetzt, warum sie so knapp geschrieben hatte.

            Er entschuldigte sich für seinen Ton in der Besprechung. Nina wiederum entschuldigte sich dafür, dass sie die Unterlagen nicht rechtzeitig geteilt hatte. Gemeinsam aktualisierten sie die Präsentation und gingen die wichtigsten Fragen des Kunden durch. Nina konnte zwar nicht am Termin teilnehmen, nahm sich aber eine halbe Stunde Zeit, um David auf mögliche Rückfragen vorzubereiten.

            Das Gespräch mit dem Kunden verlief besser als erwartet. David konnte die offenen Punkte erklären und versprach, zwei Details nachzureichen. Als er später dem Team davon erzählte, erwähnte er auch, dass die Vorbereitung nur durch Ninas Unterstützung gelungen war. Er wollte nicht so tun, als hätte er alles allein geschafft.

            In den nächsten Wochen änderte das Team eine Kleinigkeit an seiner Arbeitsweise. Wichtige Aufgaben und Dokumente wurden in einer gemeinsamen Übersicht festgehalten. Wenn jemand einen Termin nicht übernehmen konnte, sollte er möglichst früh Bescheid geben und die benötigten Informationen an einem vereinbarten Ort speichern.

            David dachte noch öfter an die Nachricht vom Montag. Sie hatte ihn zunächst verärgert, weil er ihren Ton als mangelnde Unterstützung verstanden hatte. Erst als er nachfragte, entdeckte er den tatsächlichen Grund. Seitdem versuchte er, kurze Nachrichten nicht sofort als persönliche Kritik zu deuten. Er wusste, dass Missverständnisse nicht immer vermeidbar waren, aber ein ehrliches Gespräch konnte verhindern, dass sie größer wurden.
        """.trimIndent(),
        glossary = mapOf(
            "gereizt" to "podrážděný",
            "Schlussfolgerung" to "závěr / vyvození",
            "Unterlagen" to "podklady",
            "nachreichen" to "dodatečně doplnit",
            "deuten" to "vykládat / chápat"
        )
    )
)

internal val readingArticlesB2Extra = listOf(
    ReadingArticle(
        id = "b2-eine-woche-ohne-auto",
        level = "B2",
        title = "Eine Woche ohne Auto",
        subtitle = "Ein Selbstversuch verändert den Blick auf Mobilität und Stadtplanung.",
        body = """
            Als die Stadt eine Kampagne für umweltfreundlichere Mobilität startete, hielt Martin sie zunächst für eine weitere gut gemeinte Aktion, die im Alltag wenig verändern würde. Er arbeitete am Stadtrand, brachte seine Tochter morgens zur Schule und erledigte nach Feierabend häufig Einkäufe. Das Auto schien ihm nicht nur bequem, sondern nahezu unverzichtbar. Trotzdem reizte ihn die Idee, eine Woche lang auf den Wagen zu verzichten und zu prüfen, welche Schwierigkeiten tatsächlich auftraten.

            Am ersten Morgen plante Martin seine Strecke sorgfältig. Er wollte mit dem Fahrrad zum Bahnhof fahren und von dort den Zug nehmen. Schon nach wenigen Minuten begann es stark zu regnen. Als er am Bahnhof ankam, war sein Mantel nass, und der Zug hatte Verspätung. Martin ärgerte sich und fragte sich, weshalb er sich freiwillig diesen Aufwand machte. Dennoch blieb er bei seinem Versuch, denn er wollte die Erfahrung nicht nach einem einzigen schlechten Morgen abbrechen.

            In den nächsten Tagen entdeckte er sowohl Vorteile als auch Probleme. Auf dem Weg zur Arbeit konnte er einen Podcast hören, statt sich auf den Verkehr konzentrieren zu müssen. Weil er nicht selbst fuhr, beantwortete er im Zug sogar einige Nachrichten. Andererseits war die Verbindung am Abend unzuverlässig, und nach einem späten Termin musste er fast vierzig Minuten auf den nächsten Zug warten. Für Kollegen, die in kleineren Orten lebten, war die Situation noch schwieriger.

            Martin begann, seine Wege genauer zu dokumentieren. Er notierte nicht nur die Fahrzeit, sondern auch Wartezeiten, Kosten und die Entfernung, die er zu Fuß zurücklegte. Dabei stellte er fest, dass eine direkte Verbindung oft wichtiger war als die reine Geschwindigkeit des Zuges. Wenn der Anschluss nicht funktionierte, wurde aus einer kurzen Reise eine lange Unterbrechung. Auch die sichere Abstellmöglichkeit für das Fahrrad spielte eine größere Rolle, als er erwartet hatte.

            Am Donnerstag sprach Martin mit zwei Kollegen über seinen Versuch. Eine Kollegin erzählte, dass sie gerne mit dem Bus fahren würde, aber nach ihrer Spätschicht keine Verbindung mehr nach Hause habe. Ein anderer Kollege wohnte weit entfernt und musste regelmäßig Werkzeuge transportieren. Martin wurde klar, dass es keine einzelne Lösung für alle gab. Wer umweltfreundlicher unterwegs sein wollte, brauchte Alternativen, die zu den tatsächlichen Lebensumständen passten.

            Am Freitag erstellte Martin eine kurze Auswertung für die Arbeitsgruppe seiner Stadt. Er schlug vor, zunächst die Anschlüsse am frühen Morgen und späten Abend zu verbessern, sichere Fahrradstellplätze an Bahnhöfen einzurichten und Informationen über kombinierte Wege einfacher zugänglich zu machen. Er behauptete nicht, dass Autos überflüssig seien. Vielmehr argumentierte er, dass Menschen nur dann freiwillig umsteigen würden, wenn die Alternativen zuverlässig und planbar wären.

            Die Woche endete mit einer überraschend positiven Erfahrung. Martin hatte weniger Geld für Kraftstoff ausgegeben und auf einigen Strecken mehr Bewegung gehabt. Gleichzeitig hatte er mehr Zeit für die Planung gebraucht und konnte bestimmte Wege nicht so spontan erledigen wie zuvor. Er beschloss deshalb, das Auto nicht vollständig aufzugeben, sondern an drei Tagen pro Woche anders zur Arbeit zu fahren.

            Der Versuch hatte Martins Meinung nicht einfach umgedreht. Er betrachtete die Frage nun differenzierter. Persönliche Entscheidungen konnten einen Unterschied machen, doch Infrastruktur und Arbeitszeiten bestimmten mit, welche Wahl überhaupt möglich war. Für eine ernsthafte Verkehrswende, fand er, musste man deshalb nicht nur an die Menschen appellieren, sondern auch Bedingungen schaffen, unter denen nachhaltiges Verhalten praktisch umsetzbar wurde.
        """.trimIndent(),
        glossary = mapOf(
            "unverzichtbar" to "nepostradatelný",
            "Verspätung" to "zpoždění",
            "Auswertung" to "vyhodnocení",
            "Infrastruktur" to "infrastruktura",
            "umsetzbar" to "proveditelný"
        )
    ),
    ReadingArticle(
        id = "b2-entscheidung-im-archiv",
        level = "B2",
        title = "Die Entscheidung im Archiv",
        subtitle = "Eine Archivarin muss entscheiden, wie persönliche Erinnerungen öffentlich zugänglich werden.",
        body = """
            Als die Stadt dem historischen Archiv eine Sammlung privater Briefe übergab, schien der Auftrag zunächst eindeutig: Die Dokumente sollten digitalisiert und für die Forschung zugänglich gemacht werden. Die Briefe stammten aus mehreren Jahrzehnten und beschrieben das Leben einer Familie, die während einer wirtschaftlich schwierigen Zeit in verschiedene Länder gezogen war. Für die Archivarin Miriam waren sie eine wertvolle Quelle, weil sie nicht nur politische Ereignisse, sondern auch den Alltag und die Gefühle der Menschen sichtbar machten.

            Beim Lesen bemerkte Miriam jedoch, dass einige Briefe sehr persönliche Informationen enthielten. Es ging um Krankheiten, familiäre Konflikte und Vorwürfe, die sich gegen Menschen richteten, deren Angehörige noch lebten. Die Familie hatte der Übergabe zugestimmt, aber im Vertrag stand nicht eindeutig, ob jedes Dokument sofort online veröffentlicht werden durfte. Miriam fragte deshalb ihre Vorgesetzte, ob die Digitalisierung vorläufig auf die interne Nutzung beschränkt werden sollte.

            Die Antwort war zurückhaltend. Einerseits hatte das Archiv die Aufgabe, Quellen zu bewahren und Forschung zu ermöglichen. Andererseits durfte es die Privatsphäre der Betroffenen nicht leichtfertig gefährden. Eine vollständige Sperre hätte bedeutet, dass wichtige historische Erfahrungen weiterhin schwer zugänglich gewesen wären. Eine sofortige Veröffentlichung hätte dagegen Menschen verletzen können, die nie damit gerechnet hatten, dass private Briefe öffentlich gelesen würden.

            Miriam schlug vor, die Sammlung in drei Gruppen zu teilen. Dokumente ohne sensible Angaben könnten direkt veröffentlicht werden. Briefe mit persönlichen Details sollten zunächst nur im Lesesaal und unter klaren Bedingungen einsehbar sein. Für besonders heikle Unterlagen wollte sie prüfen lassen, ob eine zeitlich begrenzte Sperrfrist oder eine geschwärzte Fassung möglich wäre. Das Verfahren würde zusätzliche Arbeit verursachen, aber die Entscheidung wäre besser begründet als eine pauschale Freigabe oder ein vollständiges Verbot.

            Bei einer Sitzung widersprach ein Historiker. Er befürchtete, dass eine Auswahl durch das Archiv das Bild der Vergangenheit verzerren könnte. Wenn gerade unangenehme Dokumente zurückgehalten würden, könnten spätere Forschende falsche Schlüsse ziehen. Miriam nahm den Einwand ernst. Sie erklärte, dass nicht der historische Inhalt allein das Problem sei, sondern die Frage, in welcher Form und zu welchem Zeitpunkt er zugänglich gemacht werde. Entscheidungen über Einschränkungen müssten dokumentiert und regelmäßig überprüft werden.

            Anschließend nahm das Archiv Kontakt zu den noch erreichbaren Familienmitgliedern auf. Einige unterstützten die Veröffentlichung, andere baten darum, bestimmte Briefe vorerst nicht online zu stellen. Bei einem Gespräch erfuhr Miriam, dass eine erwähnte Person noch lebte und die familiären Vorwürfe anders erinnerte, als es in den Briefen dargestellt wurde. Diese Information bestätigte für sie, dass historische Quellen wichtig waren, aber nicht automatisch eine vollständige oder neutrale Wahrheit darstellten.

            Nach mehreren Wochen verabschiedete das Archiv ein Verfahren, das Zugänglichkeit und Verantwortung miteinander verbinden sollte. Jede Einschränkung musste einen konkreten Grund haben, und Entscheidungen sollten nach einer festgelegten Frist neu bewertet werden. Gleichzeitig wurden die veröffentlichten Dokumente mit Informationen über ihre Herkunft und ihren Kontext versehen. Die Sammlung wurde dadurch nicht sofort vollständig geöffnet, aber ein großer Teil konnte zugänglich gemacht werden, ohne persönliche Risiken zu ignorieren.

            Als Miriam später die ersten Rückmeldungen von Forschenden las, war sie erleichtert. Einige nutzten die Briefe für eine Untersuchung über Arbeitsmigration, andere interessierten sich für die Sprache und die Alltagserfahrungen der Familie. Miriam wusste, dass keine Regel alle Konflikte lösen konnte. Die Entscheidung hatte ihr jedoch gezeigt, dass verantwortungsvolle Arbeit nicht darin bestand, einen einzigen Wert absolut zu setzen. Es ging darum, verschiedene berechtigte Interessen sichtbar zu machen und offen zu erklären, warum eine Lösung gewählt worden war.
        """.trimIndent(),
        glossary = mapOf(
            "zugänglich" to "přístupný",
            "Sperrfrist" to "ochranná lhůta / doba embarga",
            "verzerren" to "zkreslit",
            "Rückmeldung" to "zpětná vazba",
            "Migration" to "migrace"
        )
    )
)
