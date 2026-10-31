package com.deutschai.app

private fun extraDialogueOption(
    german: String,
    czech: String,
    replyGerman: String,
    replyCzech: String,
    nextNodeId: String?,
    quality: DialogueAnswerQuality,
    feedback: String
) = BranchingDialogueOption(
    german = german,
    czech = czech,
    replyGerman = replyGerman,
    replyCzech = replyCzech,
    nextNodeId = nextNodeId,
    quality = quality,
    feedback = feedback
)

private fun extraDialogueNode(
    id: String,
    promptGerman: String,
    promptCzech: String,
    vararg options: BranchingDialogueOption
) = BranchingDialogueNode(
    id = id,
    promptGerman = promptGerman,
    promptCzech = promptCzech,
    options = options.toList()
)

/**
 * Additional fully pre-authored dialogue scenarios. They are bundled with the app
 * and use the same offline branching engine as the original restaurant and hotel topics.
 */
internal val additionalBranchingDialogueTopics = listOf(
    BranchingDialogueTopic(
        id = "team_meeting_b1",
        title = "Pracovní schůzka – stav projektu",
        subtitle = "Status, otevřené body, zpoždění a rozdělení úkolů · B1",
        level = "B1",
        startNodeId = "kickoff",
        nodes = listOf(
            extraDialogueNode(
                "kickoff",
                "Guten Morgen zusammen. Können wir mit einem kurzen Statusbericht beginnen?",
                "Dobré ráno všem. Můžeme začít krátkou zprávou o aktuálním stavu?",
                extraDialogueOption(
                    "Gern. Das Projekt liegt im Zeitplan; die ersten Muster sind bereits fertig.",
                    "Rád. Projekt je podle plánu; první vzorky jsou už hotové.",
                    "Das klingt gut. Was ist der nächste wichtige Schritt?",
                    "To zní dobře. Jaký je další důležitý krok?",
                    "status_on_track",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Stručně shrnuješ stav a přidáváš konkrétní informaci o hotových vzorcích."
                ),
                extraDialogueOption(
                    "Ja, ich würde zuerst die offenen Punkte aus der letzten Sitzung klären.",
                    "Ano, nejprve bych vyřešil/a otevřené body z poslední schůzky.",
                    "Einverstanden. Welchen Punkt sollten wir zuerst besprechen?",
                    "Souhlasím. Který bod bychom měli probrat jako první?",
                    "open_points",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "To je vhodná alternativa, pokud chceš nejprve navázat na minulou schůzku."
                ),
                extraDialogueOption(
                    "Ich brauche erst einmal einen Kaffee. Wo ist die Küche?",
                    "Nejdřív potřebuji kávu. Kde je kuchyňka?",
                    "Die Küche ist neben dem Besprechungsraum. Wir beginnen inzwischen mit der Tagesordnung.",
                    "Kuchyňka je vedle zasedací místnosti. Mezitím začneme programem schůzky.",
                    "open_points",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Věta je srozumitelná, ale neodpovídá na žádost o pracovní status."
                )
            ),
            extraDialogueNode(
                "status_on_track",
                "Sehr gut. Was brauchen Sie, um die nächsten Schritte abzuschließen?",
                "Velmi dobře. Co potřebujete k dokončení dalších kroků?",
                extraDialogueOption(
                    "Eine Person aus der Qualitätssicherung wäre für die Prüfung der Muster hilfreich.",
                    "Na kontrolu vzorků by se hodil jeden pracovník z oddělení kvality.",
                    "Ich kläre das mit der Qualitätssicherung und gebe Ihnen heute Bescheid.",
                    "Domluvím to s oddělením kvality a dnes vám dám vědět.",
                    "plan",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Konkrétně pojmenováváš potřebný zdroj a vysvětluješ, k čemu bude."
                ),
                extraDialogueOption(
                    "Nein, wir schaffen das mit dem bestehenden Team.",
                    "Ne, zvládneme to se stávajícím týmem.",
                    "Gut. Dann halten wir den aktuellen Zeitplan ein.",
                    "Dobře. Pak dodržíme současný harmonogram.",
                    "plan",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Přirozená odpověď, pokud další podporu nepotřebujete."
                ),
                extraDialogueOption(
                    "Ich möchte lieber an einem anderen Projekt arbeiten.",
                    "Raději bych pracoval/a na jiném projektu.",
                    "Das können wir später besprechen. Jetzt sollten wir die nächsten Schritte festlegen.",
                    "To můžeme probrat později. Teď bychom měli stanovit další kroky.",
                    "plan",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Odvádíš pozornost od otázky, co je potřeba pro dokončení projektu."
                )
            ),
            extraDialogueNode(
                "open_points",
                "Welchen Punkt sollten wir zuerst besprechen?",
                "Který bod bychom měli probrat jako první?",
                extraDialogueOption(
                    "Die Liefertermine sind am kritischsten, weil sie den Produktionsstart beeinflussen.",
                    "Nejdůležitější jsou termíny dodávek, protože ovlivňují začátek výroby.",
                    "Verstanden. Es gibt tatsächlich ein Risiko bei einer wichtigen Lieferung.",
                    "Rozumím. U jedné důležité dodávky skutečně existuje riziko.",
                    "delivery_risk",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Dobře určuješ prioritu a vysvětluješ její dopad na projekt."
                ),
                extraDialogueOption(
                    "Ich würde gern die Zuständigkeiten für die nächsten Aufgaben klären.",
                    "Rád/a bych vyjasnil/a odpovědnosti za další úkoly.",
                    "Gute Idee. Wir sollten die Aufgaben eindeutig verteilen.",
                    "Dobrý nápad. Měli bychom úkoly jednoznačně rozdělit.",
                    "responsibilities",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Tímto způsobem můžeš schůzku zaměřit rovnou na rozdělení práce."
                ),
                extraDialogueOption(
                    "Können wir stattdessen über das Wetter sprechen?",
                    "Můžeme místo toho mluvit o počasí?",
                    "Vielleicht nach der Sitzung. Zuerst müssen wir die offenen Projektpunkte klären.",
                    "Možná po schůzce. Nejdříve musíme vyřešit otevřené body projektu.",
                    "responsibilities",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Téma počasí se nehodí k pracovnímu bodu, který má tým vyřešit."
                )
            ),
            extraDialogueNode(
                "delivery_risk",
                "Die Lieferung könnte sich um eine Woche verzögern. Wie gehen wir damit um?",
                "Dodávka se může zpozdit o týden. Jak to vyřešíme?",
                extraDialogueOption(
                    "Ich prüfe heute eine alternative Quelle und melde mich bis morgen mit einem Vorschlag.",
                    "Ještě dnes prověřím alternativní zdroj a do zítřka se ozvu s návrhem.",
                    "Das ist ein guter Plan. Bitte informieren Sie auch den Einkauf.",
                    "To je dobrý plán. Informujte prosím také nákup.",
                    "plan",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Navrhuješ konkrétní opatření a stanovuješ termín další informace."
                ),
                extraDialogueOption(
                    "Könnten wir den Produktionsstart vorsorglich um eine Woche verschieben?",
                    "Mohli bychom preventivně posunout začátek výroby o týden?",
                    "Wir prüfen diese Möglichkeit zusammen mit der Produktionsplanung.",
                    "Tuto možnost prověříme společně s plánováním výroby.",
                    "plan",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Je to relevantní návrh, jen je dobré současně hledat i jiné možnosti."
                ),
                extraDialogueOption(
                    "Das ist nicht mein Problem. Fragen Sie jemand anderen.",
                    "To není můj problém. Zeptejte se někoho jiného.",
                    "Wir sollten gemeinsam eine Lösung finden. Ich notiere den Punkt für die Aufgabenliste.",
                    "Měli bychom společně najít řešení. Zapíšu tento bod do seznamu úkolů.",
                    "plan",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Tón působí odmítavě. Na poradě je lepší navrhnout další krok nebo předat odpovědnost zdvořile."
                )
            ),
            extraDialogueNode(
                "responsibilities",
                "Wer übernimmt die nächsten Aufgaben?",
                "Kdo převezme další úkoly?",
                extraDialogueOption(
                    "Ich koordiniere die Lieferanten und Anna aktualisiert den Zeitplan.",
                    "Já budu koordinovat dodavatele a Anna aktualizuje harmonogram.",
                    "Gut. Dann halten wir beide Verantwortlichkeiten im Protokoll fest.",
                    "Dobře. Obě odpovědnosti tedy zapíšeme do zápisu.",
                    "plan",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Jasně přiřazuješ konkrétní odpovědnosti jednotlivým lidem."
                ),
                extraDialogueOption(
                    "Könnten wir die Aufgaben bitte schriftlich verteilen?",
                    "Mohli bychom prosím úkoly rozdělit písemně?",
                    "Natürlich. Ich schicke nach der Sitzung eine Aufgabenliste.",
                    "Samozřejmě. Po schůzce pošlu seznam úkolů.",
                    "plan",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Zdvořile žádáš o písemné shrnutí, které pomůže předejít nedorozumění."
                ),
                extraDialogueOption(
                    "Alle sollen einfach alles machen.",
                    "Všichni mají prostě dělat všechno.",
                    "Damit wären die Zuständigkeiten nicht klar. Wir teilen die Aufgaben lieber konkret auf.",
                    "Tím by odpovědnosti nebyly jasné. Raději úkoly rozdělíme konkrétně.",
                    "plan",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Úkoly je potřeba přiřadit konkrétním osobám, aby bylo jasné, kdo za co odpovídá."
                )
            ),
            extraDialogueNode(
                "plan",
                "Fassen wir die Vereinbarung kurz zusammen. Ist das für alle in Ordnung?",
                "Shrňme si krátce dohodu. Souhlasí to takto všem?",
                extraDialogueOption(
                    "Einverstanden. Ich sende das Protokoll heute noch an alle Beteiligten.",
                    "Souhlasím. Ještě dnes pošlu zápis všem zúčastněným.",
                    "Vielen Dank. Dann ist die Sitzung für heute beendet.",
                    "Děkuji. Tím dnešní schůzka končí.",
                    null,
                    DialogueAnswerQuality.RECOMMENDED,
                    "Potvrzuješ dohodu a přebíráš jasný navazující úkol."
                ),
                extraDialogueOption(
                    "Ja, das passt. Wann ist unser nächstes Treffen?",
                    "Ano, to mi vyhovuje. Kdy bude naše další schůzka?",
                    "Wir treffen uns nächsten Mittwoch um neun Uhr.",
                    "Sejdeme se příští středu v devět hodin.",
                    null,
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Dobře uzavíráš schůzku a ověřuješ další termín."
                ),
                extraDialogueOption(
                    "Nein, ich habe nicht zugehört.",
                    "Ne, neposlouchal/a jsem.",
                    "Dann fasse ich die wichtigsten Punkte gern noch einmal zusammen.",
                    "Pak rád ještě jednou shrnu nejdůležitější body.",
                    null,
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Na závěr je vhodné potvrdit dohodu nebo požádat o konkrétní dovysvětlení."
                )
            )
        )
    ),
    BranchingDialogueTopic(
        id = "train_travel_b1",
        title = "Cestování vlakem – změna spojení",
        subtitle = "Zpoždění, přestup, nástupiště a změna jízdenky · B1",
        level = "B1",
        startNodeId = "station",
        nodes = listOf(
            extraDialogueNode(
                "station",
                "Entschuldigung, ich möchte nach München fahren. Können Sie mir helfen?",
                "Promiňte, chtěl/a bych jet do Mnichova. Můžete mi pomoci?",
                extraDialogueOption(
                    "Natürlich. Wann fährt der nächste Zug, und muss ich umsteigen?",
                    "Samozřejmě. Kdy jede další vlak a musím přestupovat?",
                    "Der direkte Zug fällt heute leider aus. Es gibt aber eine Verbindung mit Umstieg.",
                    "Přímý vlak dnes bohužel nejede. Existuje ale spojení s přestupem.",
                    "connection",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Ptáš se na odjezd i přestup, tedy na dvě důležité informace najednou."
                ),
                extraDialogueOption(
                    "Ich habe bereits ein Ticket. Können Sie mir bitte das Gleis nennen?",
                    "Už mám jízdenku. Můžete mi prosím říct nástupiště?",
                    "Gern. Ich prüfe zuerst, ob Ihr Zug planmäßig fährt.",
                    "Rád. Nejdříve ověřím, zda váš vlak jede podle plánu.",
                    "platform",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Vhodná otázka, pokud už máš jízdenku a potřebuješ se zorientovat."
                ),
                extraDialogueOption(
                    "Geben Sie mir sofort eine Fahrkarte für gestern.",
                    "Dejte mi okamžitě jízdenku na včerejšek.",
                    "Für gestern kann ich keine neue Fahrkarte ausstellen. Wohin möchten Sie heute fahren?",
                    "Na včerejšek nemohu vystavit novou jízdenku. Kam chcete jet dnes?",
                    "connection",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Pozor na datum. Při nákupu jízdenky je potřeba uvést skutečný den cesty."
                )
            ),
            extraDialogueNode(
                "connection",
                "Der direkte Zug fällt aus. Es gibt eine Verbindung mit Umstieg in Nürnberg.",
                "Přímý vlak nejede. Existuje spojení s přestupem v Norimberku.",
                extraDialogueOption(
                    "Wie viel Zeit habe ich in Nürnberg zum Umsteigen?",
                    "Kolik času mám v Norimberku na přestup?",
                    "Sie haben acht Minuten. Der Anschluss fährt vom gegenüberliegenden Bahnsteig.",
                    "Máte osm minut. Navazující vlak odjíždí z protějšího nástupiště.",
                    "transfer",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Před cestou si správně ověřuješ délku přestupu."
                ),
                extraDialogueOption(
                    "Gibt es später noch eine andere direkte Verbindung?",
                    "Existuje později ještě jiné přímé spojení?",
                    "Ja, der nächste direkte Zug fährt in ungefähr zwei Stunden.",
                    "Ano, další přímý vlak jede přibližně za dvě hodiny.",
                    "later_train",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Přirozeně zvažuješ alternativu bez přestupu."
                ),
                extraDialogueOption(
                    "Dann fahre ich einfach ohne Ticket mit dem Bus.",
                    "Tak pojedu autobusem bez jízdenky.",
                    "Bitte beachten Sie, dass Sie auch für den Bus eine gültige Fahrkarte brauchen.",
                    "Upozorňuji, že i na autobus potřebujete platnou jízdenku.",
                    "later_train",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Cestovat bez platné jízdenky není řešení. Zeptej se raději na legální alternativní spoj."
                )
            ),
            extraDialogueNode(
                "transfer",
                "Sie haben acht Minuten Umsteigezeit. Ist das für Sie in Ordnung?",
                "Na přestup máte osm minut. Je to pro vás v pořádku?",
                extraDialogueOption(
                    "Ist das Gleis weit entfernt, und ist der Weg gut ausgeschildert?",
                    "Je nástupiště daleko a je cesta dobře označená?",
                    "Nein, Sie müssen nur die Unterführung benutzen. Die Schilder zeigen Ihnen den Weg.",
                    "Ne, stačí použít podchod. Značky vám ukážou cestu.",
                    "platform",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Ověřuješ si cestu, abys krátký přestup bezpečně stihl/a."
                ),
                extraDialogueOption(
                    "Könnten Sie mir den Weg auf dem Lageplan zeigen?",
                    "Mohl/a byste mi ukázat cestu na plánku nádraží?",
                    "Natürlich. Hier sehen Sie den Weg zum gegenüberliegenden Bahnsteig.",
                    "Samozřejmě. Tady vidíte cestu k protějšímu nástupišti.",
                    "platform",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Zdvořilá žádost o praktickou pomoc s orientací."
                ),
                extraDialogueOption(
                    "Acht Minuten sind mir zu lang. Ich möchte zurückfahren.",
                    "Osm minut je na mě příliš dlouho. Chci jet zpátky.",
                    "Wenn Sie möchten, prüfe ich eine andere Verbindung für Sie.",
                    "Jestli chcete, prověřím pro vás jiné spojení.",
                    "refund",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Osm minut není dlouhá doba. Pokud ti přestup nevyhovuje, požádej raději o jinou trasu."
                )
            ),
            extraDialogueNode(
                "later_train",
                "Der nächste direkte Zug fährt in zwei Stunden. Was möchten Sie tun?",
                "Další přímý vlak jede za dvě hodiny. Co chcete udělat?",
                extraDialogueOption(
                    "Dann nehme ich lieber die Verbindung über Nürnberg.",
                    "Pak raději pojedu spojem přes Norimberk.",
                    "Gern. Ich zeige Ihnen die Umsteigeverbindung und prüfe Ihr Ticket.",
                    "Rád. Ukážu vám spoj s přestupem a ověřím vaši jízdenku.",
                    "transfer",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Rozhoduješ se podle dostupných možností a vybíráš rychlejší cestu."
                ),
                extraDialogueOption(
                    "Kann ich mein Ticket auf den späteren Zug umbuchen?",
                    "Mohu si jízdenku přebookovat na pozdější vlak?",
                    "Ja, das ist möglich. Ich prüfe kurz die Bedingungen Ihres Tickets.",
                    "Ano, je to možné. Krátce ověřím podmínky vaší jízdenky.",
                    "refund",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Správně se ptáš na změnu rezervace nebo jízdenky."
                ),
                extraDialogueOption(
                    "Ich steige einfach in irgendeinen Zug ein.",
                    "Prostě nastoupím do nějakého vlaku.",
                    "Bitte prüfen Sie vorher die Gültigkeit Ihres Tickets und das Fahrtziel.",
                    "Nejdříve prosím ověřte platnost jízdenky a cílovou stanici.",
                    "platform",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Nastupovat do náhodného vlaku může vést špatným směrem nebo k jízdě bez platné jízdenky."
                )
            ),
            extraDialogueNode(
                "platform",
                "Der Anschluss fährt von Gleis sieben. Möchten Sie noch etwas wissen?",
                "Navazující spoj odjíždí z nástupiště číslo sedm. Chcete ještě něco vědět?",
                extraDialogueOption(
                    "Ist mein Ticket auch für den Regionalzug gültig?",
                    "Platí moje jízdenka také v regionálním vlaku?",
                    "Ja, laut Ihren Ticketbedingungen können Sie diesen Regionalzug benutzen.",
                    "Ano, podle podmínek vaší jízdenky můžete tento regionální vlak použít.",
                    "ticket",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Správně si ověřuješ platnost jízdenky pro konkrétní typ vlaku."
                ),
                extraDialogueOption(
                    "Könnten Sie mir bitte den Weg zu Gleis sieben erklären?",
                    "Mohl/a byste mi prosím vysvětlit cestu k nástupišti sedm?",
                    "Gehen Sie geradeaus und dann links. Die Anzeige zeigt Ihnen den Zug.",
                    "Jděte rovně a potom doleva. Na tabuli uvidíte svůj vlak.",
                    "ticket",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Dobře žádáš o navigaci na nádraží."
                ),
                extraDialogueOption(
                    "Kann ich mitten auf den Gleisen einsteigen?",
                    "Mohu nastoupit uprostřed kolejí?",
                    "Nein. Bitte benutzen Sie ausschließlich den gekennzeichneten Bahnsteig.",
                    "Ne. Používejte prosím výhradně označené nástupiště.",
                    "ticket",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Na nástupiště a do vlaku se vstupuje pouze bezpečným a označeným přístupem."
                )
            ),
            extraDialogueNode(
                "refund",
                "Wenn Sie nicht fahren möchten, können wir das Ticket erstatten oder umbuchen.",
                "Pokud nechcete jet, můžeme jízdenku vrátit nebo změnit.",
                extraDialogueOption(
                    "Bitte buchen Sie es auf die spätere Direktverbindung um.",
                    "Převeďte ji prosím na pozdější přímé spojení.",
                    "Gern. Sie erhalten gleich eine Bestätigung für die neue Verbindung.",
                    "Rád. Hned dostanete potvrzení nového spoje.",
                    "ticket",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Jasně sděluješ, jaké řešení preferuješ."
                ),
                extraDialogueOption(
                    "Dann hätte ich gern eine Rückerstattung. Wo kann ich sie beantragen?",
                    "V tom případě bych chtěl/a vrácení peněz. Kde o něj mohu požádat?",
                    "Sie können die Rückerstattung hier am Schalter beantragen.",
                    "O vrácení peněz můžete požádat zde u přepážky.",
                    "ticket",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Zdvořile žádáš o vrácení peněz a ověřuješ postup."
                ),
                extraDialogueOption(
                    "Ich möchte das Geld zurück und trotzdem mitfahren.",
                    "Chci vrátit peníze a zároveň přesto jet.",
                    "Das ist leider nicht gleichzeitig möglich. Sie müssen sich für eine Option entscheiden.",
                    "To bohužel není možné současně. Musíte si vybrat jednu možnost.",
                    "ticket",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Vrácení peněz a použití stejné jízdenky se navzájem vylučují."
                )
            ),
            extraDialogueNode(
                "ticket",
                "Ihre Verbindung ist jetzt geklärt. Brauchen Sie noch Hilfe?",
                "Vaše spojení je nyní vyřešeno. Potřebujete ještě s něčím pomoci?",
                extraDialogueOption(
                    "Nein, vielen Dank. Ich gehe jetzt zu Gleis sieben.",
                    "Ne, děkuji. Teď půjdu na nástupiště sedm.",
                    "Gern geschehen. Gute Reise!",
                    "Rádo se stalo. Šťastnou cestu!",
                    null,
                    DialogueAnswerQuality.RECOMMENDED,
                    "Přirozeně ukončuješ rozhovor a potvrzuješ další krok."
                ),
                extraDialogueOption(
                    "Ja, wo finde ich die Anzeige für die Wagenreihung?",
                    "Ano, kde najdu tabuli s řazením vozů?",
                    "Die Anzeige befindet sich direkt am Zugang zu Gleis sieben.",
                    "Tabule se nachází přímo u vstupu na nástupiště sedm.",
                    null,
                    DialogueAnswerQuality.ACCEPTABLE,
                    "To je užitečná doplňující otázka před nástupem."
                ),
                extraDialogueOption(
                    "Ich warte lieber hier, obwohl der Zug gleich abfährt.",
                    "Raději počkám tady, přestože vlak brzy odjíždí.",
                    "Dann könnten Sie Ihren Zug verpassen. Gehen Sie bitte rechtzeitig zum Bahnsteig.",
                    "Pak byste mohl/a vlak zmeškat. Vydejte se prosím včas na nástupiště.",
                    null,
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Když vlak brzy odjíždí, je důležité přesunout se k nástupišti."
                )
            )
        )
    ),
    BranchingDialogueTopic(
        id = "customer_complaint_b1",
        title = "Reklamace v obchodě",
        subtitle = "Popis závady, doklad o koupi, výměna a oprava · B1",
        level = "B1",
        startNodeId = "store_start",
        nodes = listOf(
            extraDialogueNode(
                "store_start",
                "Guten Tag. Wie kann ich Ihnen helfen?",
                "Dobrý den. Jak vám mohu pomoci?",
                extraDialogueOption(
                    "Ich möchte diese Kopfhörer reklamieren. Sie funktionieren seit gestern nicht mehr.",
                    "Chtěl/a bych reklamovat tato sluchátka. Od včerejška nefungují.",
                    "Das tut mir leid. Haben Sie den Kassenbon oder eine andere Kaufbestätigung dabei?",
                    "To mě mrzí. Máte s sebou účtenku nebo jiné potvrzení o koupi?",
                    "receipt",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Hned vysvětluješ, co reklamuješ, a popisuješ problém."
                ),
                extraDialogueOption(
                    "Ich habe ein Problem mit einem Artikel, den ich hier gekauft habe.",
                    "Mám problém se zbožím, které jsem si koupil/a zde.",
                    "Natürlich. Um welchen Artikel handelt es sich, und haben Sie den Beleg dabei?",
                    "Samozřejmě. O jaké zboží jde a máte doklad o koupi?",
                    "receipt",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Dobře zahajuješ reklamaci, i když zatím neuvádíš konkrétní závadu."
                ),
                extraDialogueOption(
                    "Geben Sie mir sofort ein neues Gerät!",
                    "Dejte mi okamžitě nové zařízení!",
                    "Ich helfe Ihnen gern. Zuerst müssen wir uns den Defekt und den Kaufbeleg ansehen.",
                    "Rád vám pomohu. Nejdříve se musíme podívat na závadu a doklad o koupi.",
                    "receipt",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Rozkazovací způsob působí zbytečně útočně. Zdvořilá žádost obvykle pomůže víc."
                )
            ),
            extraDialogueNode(
                "receipt",
                "Haben Sie den Kassenbon dabei?",
                "Máte s sebou účtenku?",
                extraDialogueOption(
                    "Ja, hier ist der Kassenbon. Ich habe das Gerät vor zwei Wochen gekauft.",
                    "Ano, tady je účtenka. Zařízení jsem koupil/a před dvěma týdny.",
                    "Vielen Dank. Dann können wir den Kauf leicht zuordnen. Was genau funktioniert nicht?",
                    "Děkuji. Tak můžeme nákup snadno dohledat. Co přesně nefunguje?",
                    "inspection",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Předkládáš doklad a uvádíš, kdy jsi zboží koupil/a."
                ),
                extraDialogueOption(
                    "Ich habe den Bon nicht mehr, aber die Kartenzahlung ist auf meinem Kontoauszug zu sehen.",
                    "Účtenku už nemám, ale platba kartou je vidět na výpisu z účtu.",
                    "Das kann uns helfen. Können Sie mir bitte den Kontoauszug zeigen?",
                    "To nám může pomoci. Můžete mi prosím ukázat výpis z účtu?",
                    "no_receipt",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Nabízíš jiný způsob, jak doložit nákup."
                ),
                extraDialogueOption(
                    "Warum fragen Sie? Das geht Sie nichts an.",
                    "Proč se ptáte? Do toho vám nic není.",
                    "Für die Bearbeitung der Reklamation müssen wir den Kauf nachvollziehen können.",
                    "Abychom mohli reklamaci vyřídit, potřebujeme ověřit nákup.",
                    "inspection",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Prodavač potřebuje ověřit nákup. Zachovej věcný a zdvořilý tón."
                )
            ),
            extraDialogueNode(
                "inspection",
                "Können Sie den Fehler genauer beschreiben?",
                "Můžete závadu popsat podrobněji?",
                extraDialogueOption(
                    "Der linke Kopfhörer lädt nicht mehr, obwohl ich verschiedene Kabel ausprobiert habe.",
                    "Levé sluchátko se už nenabíjí, přestože jsem vyzkoušel/a různé kabely.",
                    "Danke für die genaue Beschreibung. Wir können das Gerät prüfen und Ihnen eine Lösung anbieten.",
                    "Děkuji za přesný popis. Můžeme zařízení zkontrolovat a nabídnout vám řešení.",
                    "solution",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Popisuješ konkrétní příznak i to, co už jsi vyzkoušel/a."
                ),
                extraDialogueOption(
                    "Das Gerät lässt sich nicht einschalten. Könnten Sie es bitte überprüfen?",
                    "Zařízení nejde zapnout. Mohl/a byste ho prosím zkontrolovat?",
                    "Natürlich. Ich prüfe zuerst den Einschaltknopf und den Akku.",
                    "Samozřejmě. Nejdříve zkontroluji tlačítko zapnutí a baterii.",
                    "solution",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Stručně a jasně popisuješ závadu a žádáš o kontrolu."
                ),
                extraDialogueOption(
                    "Es ist einfach schlecht. Mehr kann ich nicht sagen.",
                    "Je to prostě špatné. Víc k tomu nemohu říct.",
                    "Können Sie mir vielleicht zeigen, wann und wie das Problem auftritt?",
                    "Můžete mi prosím ukázat, kdy a jak se problém projevuje?",
                    "solution",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Konkrétní popis závady pomůže obchodu najít správné řešení."
                )
            ),
            extraDialogueNode(
                "no_receipt",
                "Können wir den Kauf über die Kartenzahlung oder die Seriennummer nachweisen?",
                "Můžeme nákup doložit platbou kartou nebo sériovým číslem?",
                extraDialogueOption(
                    "Ja, ich kann Ihnen die Kartenzahlung auf meinem Kontoauszug zeigen.",
                    "Ano, mohu vám ukázat platbu kartou na výpisu z účtu.",
                    "Das reicht zunächst. Beschreiben Sie mir bitte noch den Defekt.",
                    "To zatím stačí. Popište mi prosím ještě závadu.",
                    "solution",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Spolupracuješ a nabízíš konkrétní doklad o platbě."
                ),
                extraDialogueOption(
                    "Die Seriennummer steht auf der Verpackung. Soll ich sie Ihnen geben?",
                    "Sériové číslo je na obalu. Mám vám ho sdělit?",
                    "Ja, bitte. Damit kann ich das Produkt im System suchen.",
                    "Ano, prosím. Díky němu mohu výrobek dohledat v systému.",
                    "solution",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Nabízíš další praktickou cestu k ověření výrobku."
                ),
                extraDialogueOption(
                    "Nein, suchen Sie selbst in Ihrem System.",
                    "Ne, najděte si to sami ve svém systému.",
                    "Ich versuche es gern. Dafür brauche ich aber möglichst genaue Angaben von Ihnen.",
                    "Rád to zkusím. Potřebuji k tomu ale co nejpřesnější údaje od vás.",
                    "solution",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Spolupráce a poskytnutí údajů proces reklamace obvykle urychlí."
                )
            ),
            extraDialogueNode(
                "solution",
                "Wir können das Gerät umtauschen oder zur Reparatur einschicken. Was wäre Ihnen lieber?",
                "Můžeme zařízení vyměnit nebo poslat do opravy. Co by vám vyhovovalo více?",
                extraDialogueOption(
                    "Ein Umtausch wäre mir lieber, sofern das Gerät vorrätig ist.",
                    "Více by mi vyhovovala výměna, pokud je zařízení skladem.",
                    "Ja, das Modell ist vorrätig. Ich bereite den Umtausch vor.",
                    "Ano, model máme skladem. Připravím výměnu.",
                    "closing",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Zdvořile vyjadřuješ preferenci a zároveň zohledňuješ dostupnost."
                ),
                extraDialogueOption(
                    "Wie lange würde eine Reparatur ungefähr dauern?",
                    "Jak dlouho by přibližně trvala oprava?",
                    "Die Reparatur dauert normalerweise fünf bis sieben Werktage.",
                    "Oprava obvykle trvá pět až sedm pracovních dnů.",
                    "closing",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Před rozhodnutím si správně zjišťuješ očekávanou dobu opravy."
                ),
                extraDialogueOption(
                    "Ich nehme einfach ein anderes Produkt und bezahle nichts extra.",
                    "Prostě si vezmu jiný výrobek a nic navíc nezaplatím.",
                    "Wir prüfen zuerst, welche Austauschmöglichkeiten im Rahmen der Reklamation bestehen.",
                    "Nejdříve ověříme, jaké možnosti výměny v rámci reklamace existují.",
                    "closing",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Nárok na konkrétní výměnu je potřeba nejdříve ověřit. Formuluj raději svůj požadavek."
                )
            ),
            extraDialogueNode(
                "closing",
                "Der Umtausch ist möglich. Möchten Sie eine schriftliche Bestätigung?",
                "Výměna je možná. Přejete si písemné potvrzení?",
                extraDialogueOption(
                    "Ja, bitte. Vielen Dank für Ihre Hilfe.",
                    "Ano, prosím. Děkuji za pomoc.",
                    "Sehr gern. Hier ist Ihre Bestätigung. Ich wünsche Ihnen einen schönen Tag.",
                    "Samozřejmě. Tady je vaše potvrzení. Přeji vám hezký den.",
                    null,
                    DialogueAnswerQuality.RECOMMENDED,
                    "Slušně uzavíráš reklamaci a žádáš o potvrzení vyřízení."
                ),
                extraDialogueOption(
                    "Nein, danke. Bekomme ich trotzdem einen neuen Beleg?",
                    "Ne, děkuji. Dostanu přesto nový doklad?",
                    "Natürlich. Ich drucke Ihnen den Beleg gleich aus.",
                    "Samozřejmě. Hned vám doklad vytisknu.",
                    null,
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Prakticky ověřuješ, jaký doklad po výměně obdržíš."
                ),
                extraDialogueOption(
                    "Beeilen Sie sich, ich habe keine Geduld mehr.",
                    "Pospěšte si, už nemám trpělivost.",
                    "Ich verstehe. Ich schließe den Vorgang so schnell wie möglich ab.",
                    "Rozumím. Dokončím proces co nejrychleji.",
                    null,
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Na konci jednání není potřeba zbytečně zvyšovat napětí."
                )
            )
        )
    ),
    BranchingDialogueTopic(
        id = "phone_appointment_a2",
        title = "Telefonát – domluvení termínu",
        subtitle = "Představení, důvod hovoru, volné termíny a potvrzení · A2",
        level = "A2",
        startNodeId = "call_start",
        nodes = listOf(
            extraDialogueNode(
                "call_start",
                "Guten Tag, Praxis Dr. Schneider. Was kann ich für Sie tun?",
                "Dobrý den, ordinace doktora Schneidera. Co pro vás mohu udělat?",
                extraDialogueOption(
                    "Guten Tag. Ich würde gern einen Termin vereinbaren.",
                    "Dobrý den. Rád/a bych si domluvil/a termín.",
                    "Gern. Geht es um eine Untersuchung oder um einen Kontrolltermin?",
                    "Ráda. Jde o vyšetření, nebo kontrolní návštěvu?",
                    "reason",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Zdvořile a jasně říkáš, proč voláš. V telefonu se hodí vazba „Ich würde gern…“."
                ),
                extraDialogueOption(
                    "Haben Sie diese Woche noch einen freien Termin?",
                    "Máte tento týden ještě volný termín?",
                    "Ich sehe gern nach. Ist der Termin für eine Untersuchung oder eine Kontrolle?",
                    "Ráda se podívám. Jde o vyšetření, nebo kontrolu?",
                    "availability",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Rovnou se ptáš na dostupnost; je to přirozené zahájení hovoru."
                ),
                extraDialogueOption(
                    "Geben Sie mir den Arzt, ich warte.",
                    "Dejte mi lékaře, počkám.",
                    "Der Arzt ist gerade beschäftigt. Ich kann Ihnen aber bei der Terminvereinbarung helfen.",
                    "Lékař je právě zaneprázdněný. Mohu vám ale pomoci s objednáním.",
                    "reason",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Rozkazovací způsob působí v telefonu nezdvořile. Představ se a řekni, s čím potřebuješ pomoci."
                )
            ),
            extraDialogueNode(
                "reason",
                "Geht es um eine Untersuchung oder um einen Kontrolltermin?",
                "Jde o vyšetření, nebo kontrolní návštěvu?",
                extraDialogueOption(
                    "Ich brauche einen Termin für eine allgemeine Untersuchung.",
                    "Potřebuji termín na všeobecné vyšetření.",
                    "Verstanden. Ich prüfe, wann wir einen passenden Termin frei haben.",
                    "Rozumím. Podívám se, kdy máme vhodný volný termín.",
                    "availability",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Jednoduše a přesně vysvětluješ důvod návštěvy."
                ),
                extraDialogueOption(
                    "Ich möchte einen Kontrolltermin vereinbaren.",
                    "Chtěl/a bych si domluvit kontrolní termín.",
                    "Natürlich. Ich sehe mir die freien Zeiten an.",
                    "Samozřejmě. Podívám se na volné termíny.",
                    "availability",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Přirozeně upřesňuješ, že potřebuješ kontrolní návštěvu."
                ),
                extraDialogueOption(
                    "Das sage ich Ihnen später. Wann haben Sie Zeit?",
                    "To vám řeknu později. Kdy máte čas?",
                    "Ich kann Ihnen die verfügbaren Termine nennen. Für die Buchung brauche ich aber den Grund des Besuchs.",
                    "Mohu vám říct dostupné termíny. Pro objednání ale potřebuji znát důvod návštěvy.",
                    "availability",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Krátké vysvětlení důvodu návštěvy pomůže najít správný typ termínu."
                )
            ),
            extraDialogueNode(
                "availability",
                "Wir hätten am Dienstag um zehn Uhr oder am Donnerstag um vierzehn Uhr Zeit.",
                "Měli bychom volno v úterý v deset nebo ve čtvrtek ve čtrnáct hodin.",
                extraDialogueOption(
                    "Der Termin am Dienstag um zehn Uhr passt mir gut.",
                    "Termín v úterý v deset mi dobře vyhovuje.",
                    "Sehr gut. Dann reserviere ich diesen Termin für Sie.",
                    "Výborně. Tak vám tento termín zarezervuji.",
                    "details",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Jasně potvrzuješ konkrétní nabídnutý termín."
                ),
                extraDialogueOption(
                    "Gibt es auch einen Termin am Vormittag?",
                    "Je k dispozici také termín dopoledne?",
                    "Am Donnerstag wäre alternativ um elf Uhr etwas frei.",
                    "Ve čtvrtek by byla alternativně volná jedenáctá hodina.",
                    "time_alt",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Správně žádáš o alternativu podle své časové preference."
                ),
                extraDialogueOption(
                    "Ich komme irgendwann nächste Woche.",
                    "Přijdu někdy příští týden.",
                    "Für eine feste Buchung brauche ich bitte einen konkreten Tag und eine Uhrzeit.",
                    "Pro pevnou rezervaci potřebuji konkrétní den a čas.",
                    "time_alt",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Při sjednávání termínu je potřeba vybrat konkrétní den a čas."
                )
            ),
            extraDialogueNode(
                "time_alt",
                "Am Donnerstag wäre alternativ um elf Uhr etwas frei.",
                "Ve čtvrtek by byl alternativně volný termín v jedenáct hodin.",
                extraDialogueOption(
                    "Dann nehme ich bitte den Donnerstag um elf Uhr.",
                    "Tak si prosím vezmu čtvrtek v jedenáct hodin.",
                    "Sehr gern. Ich trage Sie für Donnerstag um elf Uhr ein.",
                    "Samozřejmě. Zapíšu vás na čtvrtek v jedenáct hodin.",
                    "details",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Přijímáš nabídnutou alternativu a přesně opakuješ termín."
                ),
                extraDialogueOption(
                    "Könnten Sie mich auf die Warteliste setzen, falls früher etwas frei wird?",
                    "Mohli byste mě dát na čekací listinu, kdyby se dříve něco uvolnilo?",
                    "Ja, ich notiere Ihren Wunsch und rufe Sie an, falls ein früherer Termin frei wird.",
                    "Ano, poznamenám si vaše přání a zavolám vám, pokud se uvolní dřívější termín.",
                    "details",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Užitečná zdvořilá formulace pro žádost o dřívější termín."
                ),
                extraDialogueOption(
                    "Nein, das ist viel zu spät. Sagen Sie nichts mehr.",
                    "Ne, to je příliš pozdě. Už nic neříkejte.",
                    "Ich verstehe. Möchten Sie vielleicht einen anderen Tag prüfen?",
                    "Rozumím. Nechcete prověřit jiný den?",
                    "details",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "I když ti termín nevyhovuje, je lepší požádat o jinou možnost."
                )
            ),
            extraDialogueNode(
                "details",
                "Könnten Sie mir bitte Ihren Namen und Ihr Geburtsdatum nennen?",
                "Můžete mi prosím sdělit své jméno a datum narození?",
                extraDialogueOption(
                    "Mein Name ist Novák. Mein Geburtsdatum ist der dritte Mai 1992.",
                    "Jmenuji se Novák. Narodil/a jsem se třetího května 1992.",
                    "Vielen Dank. Ich habe Ihre Daten eingetragen. Haben Sie noch eine Frage?",
                    "Děkuji. Vaše údaje jsem zapsala. Máte ještě nějakou otázku?",
                    "confirm",
                    DialogueAnswerQuality.RECOMMENDED,
                    "Sdělíš požadované údaje v jedné jasné a úplné větě."
                ),
                extraDialogueOption(
                    "Ich buchstabiere Ihnen gern meinen Nachnamen: N-O-V-Á-K.",
                    "Rád/a vám své příjmení vyhláskuji: N-O-V-Á-K.",
                    "Vielen Dank. Und könnten Sie mir bitte noch Ihr Geburtsdatum nennen?",
                    "Děkuji. A můžete mi prosím ještě sdělit datum narození?",
                    "confirm",
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Hláskování jména je při telefonátu velmi praktické."
                ),
                extraDialogueOption(
                    "Das müssen Sie doch wissen.",
                    "To přece musíte vědět.",
                    "Ich möchte nur sicherstellen, dass ich den richtigen Datensatz öffne.",
                    "Chci se pouze ujistit, že otevřu správný záznam.",
                    "confirm",
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Recepce potřebuje ověřit totožnost. Odpověz věcně a poskytni potřebné údaje."
                )
            ),
            extraDialogueNode(
                "confirm",
                "Vielen Dank. Haben Sie noch eine Frage zu Ihrem Termin?",
                "Děkuji. Máte ještě nějakou otázku k termínu?",
                extraDialogueOption(
                    "Muss ich etwas mitbringen? Vielen Dank und auf Wiederhören.",
                    "Musím si něco přinést? Děkuji a na slyšenou.",
                    "Bitte bringen Sie Ihre Versichertenkarte mit. Auf Wiederhören!",
                    "Přineste si prosím kartičku pojištěnce. Na slyšenou!",
                    null,
                    DialogueAnswerQuality.RECOMMENDED,
                    "Ověřuješ si praktickou informaci a zdvořile ukončuješ telefonát."
                ),
                extraDialogueOption(
                    "Könnten Sie die Uhrzeit bitte noch einmal bestätigen?",
                    "Můžete mi prosím ještě jednou potvrdit čas?",
                    "Natürlich. Ihr Termin ist am Donnerstag um elf Uhr. Auf Wiederhören!",
                    "Samozřejmě. Váš termín je ve čtvrtek v jedenáct hodin. Na slyšenou!",
                    null,
                    DialogueAnswerQuality.ACCEPTABLE,
                    "Opakování času je při telefonickém objednání užitečné."
                ),
                extraDialogueOption(
                    "Ich rufe später wieder an, obwohl alles geklärt ist.",
                    "Zavolám později znovu, přestože je vše vyřešeno.",
                    "In Ordnung. Dann wünsche ich Ihnen einen schönen Tag.",
                    "V pořádku. Přeji vám hezký den.",
                    null,
                    DialogueAnswerQuality.NEEDS_WORK,
                    "Pokud je vše domluvené, můžeš hovor přirozeně ukončit."
                )
            )
        )
    )
)
