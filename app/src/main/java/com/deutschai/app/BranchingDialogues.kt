package com.deutschai.app

internal enum class DialogueAnswerQuality(val points: Int) {
    RECOMMENDED(2),
    ACCEPTABLE(1),
    NEEDS_WORK(0)
}

internal data class BranchingDialogueOption(
    val german: String,
    val czech: String,
    val replyGerman: String,
    val replyCzech: String,
    val nextNodeId: String?,
    val quality: DialogueAnswerQuality,
    val feedback: String
)

internal data class BranchingDialogueNode(
    val id: String,
    val promptGerman: String,
    val promptCzech: String,
    val options: List<BranchingDialogueOption>
)

internal data class BranchingDialogueTopic(
    val id: String,
    val title: String,
    val subtitle: String,
    val level: String,
    val startNodeId: String,
    val nodes: List<BranchingDialogueNode>
) {
    val maximumSteps: Int
        get() = nodes.size
}

internal fun branchingDialogueValidationErrors(topic: BranchingDialogueTopic): List<String> {
    val errors = mutableListOf<String>()
    val ids = topic.nodes.map { it.id }
    val idSet = ids.toSet()
    if (topic.id.isBlank() || topic.title.isBlank() || topic.level.isBlank()) {
        errors += "Topic metadata must not be blank."
    }
    if (ids.any { it.isBlank() }) errors += "Node IDs must not be blank."
    if (ids.size != idSet.size) errors += "Node IDs must be unique."
    if (topic.startNodeId !in idSet) errors += "Start node does not exist."

    topic.nodes.forEach { node ->
        if (node.promptGerman.isBlank() || node.promptCzech.isBlank()) {
            errors += "Node ${node.id} has an empty prompt."
        }
        if (node.options.size != 3) {
            errors += "Node ${node.id} must have exactly three options."
        }
        node.options.forEach { option ->
            if (option.german.isBlank() || option.czech.isBlank() ||
                option.replyGerman.isBlank() || option.replyCzech.isBlank() || option.feedback.isBlank()
            ) {
                errors += "Node ${node.id} contains an incomplete option."
            }
            if (option.nextNodeId != null && option.nextNodeId !in idSet) {
                errors += "Node ${node.id} points to missing node ${option.nextNodeId}."
            }
        }
        if (node.options.count { it.quality == DialogueAnswerQuality.RECOMMENDED } != 1) {
            errors += "Node ${node.id} must have exactly one recommended answer."
        }
    }

    val nodesById = topic.nodes.associateBy { it.id }
    val visiting = mutableSetOf<String>()
    val visited = mutableSetOf<String>()
    fun visit(id: String) {
        if (id in visiting) {
            errors += "Branching graph contains a cycle at node $id."
            return
        }
        if (!visited.add(id)) return
        visiting.add(id)
        nodesById[id]?.options?.mapNotNull { it.nextNodeId }?.forEach(::visit)
        visiting.remove(id)
    }
    if (topic.startNodeId in idSet) visit(topic.startNodeId)

    fun canFinish(id: String, cache: MutableMap<String, Boolean>): Boolean {
        cache[id]?.let { return it }
        val node = nodesById[id] ?: return false
        val result = node.options.any { option ->
            option.nextNodeId == null || canFinish(option.nextNodeId, cache)
        }
        cache[id] = result
        return result
    }
    val finishCache = mutableMapOf<String, Boolean>()
    topic.nodes.forEach { node ->
        if (!canFinish(node.id, finishCache)) {
            errors += "Node ${node.id} has no route to a conversation ending."
        }
    }
    return errors.distinct()
}

internal fun branchingDialoguePaths(
    topic: BranchingDialogueTopic,
    maxPaths: Int = 100
): List<List<String>> {
    if (topic.startNodeId !in topic.nodes.map { it.id }.toSet() || maxPaths <= 0) return emptyList()
    val nodes = topic.nodes.associateBy { it.id }
    val results = mutableListOf<List<String>>()
    fun walk(id: String, path: List<String>) {
        if (results.size >= maxPaths || id in path) return
        val nextPath = path + id
        val options = nodes[id]?.options ?: return
        options.forEach { option ->
            if (results.size >= maxPaths) return
            if (option.nextNodeId == null) results += nextPath
            else walk(option.nextNodeId, nextPath)
        }
    }
    walk(topic.startNodeId, emptyList())
    return results.distinct()
}

internal val branchingGuidedDialogueTopics = listOf(
    BranchingDialogueTopic(
        id = "restaurant_branch_a2",
        title = "V restauraci – vlastní cesta",
        subtitle = "Rezervace, objednání jídla, alergie a placení · A2",
        level = "A2",
        startNodeId = "arrival",
        nodes = listOf(
            BranchingDialogueNode(
                id = "arrival",
                promptGerman = "Guten Abend! Haben Sie einen Tisch reserviert?",
                promptCzech = "Dobrý večer! Máte rezervovaný stůl?",
                options = listOf(
                    BranchingDialogueOption(
                        "Ja, ich habe für zwei Personen auf den Namen Novák reserviert.",
                        "Ano, mám rezervaci pro dvě osoby na jméno Novák.",
                        "Herzlich willkommen! Ich habe Ihre Reservierung gefunden. Ihr Tisch am Fenster ist gleich fertig.",
                        "Srdečně vás vítám! Našel jsem vaši rezervaci. Váš stůl u okna bude hned připraven.",
                        "seated",
                        DialogueAnswerQuality.RECOMMENDED,
                        "Přesně tak. U rezervace uveď počet osob a jméno, na které je vystavená."
                    ),
                    BranchingDialogueOption(
                        "Nein, wir haben nicht reserviert. Gibt es trotzdem einen freien Tisch für zwei?",
                        "Ne, nemáme rezervaci. Je přesto volný stůl pro dva?",
                        "Ich schaue kurz nach. Wir hätten noch einen Tisch im hinteren Teil des Restaurants.",
                        "Hned se podívám. Ještě bychom měli stůl v zadní části restaurace.",
                        "walk_in",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Tohle je také přirozená a zdvořilá odpověď, když přicházíš bez rezervace."
                    ),
                    BranchingDialogueOption(
                        "Ich möchte bitte bezahlen.",
                        "Chtěl/a bych prosím zaplatit.",
                        "Natürlich. Möchten Sie zuerst einen Tisch oder gleich die Rechnung?",
                        "Samozřejmě. Přejete si nejprve stůl, nebo rovnou účet?",
                        "clarify_arrival",
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Pozor na kontext: obsluha se ptá na rezervaci. Placením bys přeskočil/a několik kroků."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "seated",
                promptGerman = "Möchten Sie lieber am Fenster oder auf der Terrasse sitzen?",
                promptCzech = "Přejete si sedět raději u okna, nebo na terase?",
                options = listOf(
                    BranchingDialogueOption(
                        "Ein Tisch am Fenster wäre wunderbar, wenn das möglich ist.",
                        "Stůl u okna by byl skvělý, jestli je to možné.",
                        "Sehr gern. Folgen Sie mir bitte. Hier ist Ihre Speisekarte.",
                        "Velmi rád. Pojďte prosím za mnou. Tady je váš jídelní lístek.",
                        "menu",
                        DialogueAnswerQuality.RECOMMENDED,
                        "Výborně. „Wäre wunderbar“ je zdvořilý způsob, jak vyjádřit přání."
                    ),
                    BranchingDialogueOption(
                        "Die Terrasse klingt gut. Ist dort noch ein Platz frei?",
                        "Terasa zní dobře. Je tam ještě volné místo?",
                        "Ja, draußen ist es heute angenehm. Ich bringe Ihnen gleich die Speisekarte.",
                        "Ano, venku je dnes příjemně. Hned vám přinesu jídelní lístek.",
                        "menu",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Přirozená alternativa. Ptáš se navíc, zda je místo skutečně volné."
                    ),
                    BranchingDialogueOption(
                        "Ich habe schon gegessen. Wo ist der Bahnhof?",
                        "Už jsem jedl/a. Kde je nádraží?",
                        "Dann sind Sie wahrscheinlich im falschen Gespräch. Der Bahnhof ist zehn Minuten von hier entfernt.",
                        "Pak jste nejspíš v nesprávném rozhovoru. Nádraží je odsud deset minut.",
                        null,
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Odpověď nesouvisí s výběrem místa k sezení."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "walk_in",
                promptGerman = "Der Tisch ist in etwa zwanzig Minuten frei. Möchten Sie kurz warten?",
                promptCzech = "Stůl bude volný přibližně za dvacet minut. Chcete chvíli počkat?",
                options = listOf(
                    BranchingDialogueOption(
                        "Ja, gern. Wir warten hier. Könnten Sie uns bitte Bescheid sagen?",
                        "Ano, rádi. Počkáme tady. Mohl/a byste nám prosím dát vědět?",
                        "Natürlich. Nehmen Sie bitte dort Platz. Ich komme zu Ihnen, sobald der Tisch frei ist.",
                        "Samozřejmě. Posaďte se prosím tamhle. Přijdu za vámi, jakmile se stůl uvolní.",
                        "wait",
                        DialogueAnswerQuality.RECOMMENDED,
                        "Skvělé. „Bescheid sagen“ znamená dát někomu vědět."
                    ),
                    BranchingDialogueOption(
                        "Könnten wir solange an der Bar etwas trinken?",
                        "Mohli bychom si mezitím dát něco k pití u baru?",
                        "Sehr gern. An der Bar sind noch zwei Plätze frei.",
                        "Samozřejmě. U baru jsou ještě dvě volná místa.",
                        "wait",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Velmi přirozené řešení, když nechceš jen čekat."
                    ),
                    BranchingDialogueOption(
                        "Ich habe für zwei Personen reserviert. Sie müssen uns sofort setzen.",
                        "Mám rezervaci pro dva. Musíte nás okamžitě usadit.",
                        "Es tut mir leid, aber ich kann keinen Tisch herbeizaubern. Ich prüfe gern noch einmal die Möglichkeiten.",
                        "Je mi líto, ale nemohu vykouzlit stůl. Rád znovu prověřím možnosti.",
                        "clarify_arrival",
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Příliš důrazná formulace může působit nezdvořile. Zkus raději „Könnten Sie bitte…?“."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "wait",
                promptGerman = "Danke für Ihre Geduld. Möchten Sie jetzt bestellen?",
                promptCzech = "Děkujeme za trpělivost. Přejete si nyní objednat?",
                options = listOf(
                    BranchingDialogueOption(
                        "Ja, danke. Ich hätte gern die Gemüsepfanne und ein Mineralwasser.",
                        "Ano, děkuji. Dal/a bych si zeleninovou pánev a minerální vodu.",
                        "Sehr gern. Möchten Sie das Wasser mit oder ohne Kohlensäure?",
                        "Velmi rád. Přejete si vodu s bublinkami, nebo bez?",
                        "order",
                        DialogueAnswerQuality.RECOMMENDED,
                        "Objednávku můžeš zdvořile zahájit vazbou „Ich hätte gern…“."
                    ),
                    BranchingDialogueOption(
                        "Könnten Sie mir zuerst die vegetarischen Gerichte empfehlen?",
                        "Mohl/a byste mi nejprve doporučit vegetariánská jídla?",
                        "Natürlich. Unsere Gemüsepfanne ist heute besonders beliebt.",
                        "Samozřejmě. Naše zeleninová pánev je dnes obzvlášť oblíbená.",
                        "order",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Dobrá otázka, která ti pomůže vybrat vhodné jídlo."
                    ),
                    BranchingDialogueOption(
                        "Ich möchte sofort die Rechnung.",
                        "Chci okamžitě účet.",
                        "Gern, aber Sie haben noch nichts bestellt. Möchten Sie vielleicht zuerst etwas essen?",
                        "Rád, ale ještě jste si nic neobjednal/a. Nechcete se nejprve najíst?",
                        "clarify_order",
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Účet se obvykle žádá až po jídle. Nejdříve si něco objednej."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "menu",
                promptGerman = "Haben Sie schon etwas gefunden, oder kann ich Ihnen etwas empfehlen?",
                promptCzech = "Už jste si něco vybral/a, nebo vám mohu něco doporučit?",
                options = listOf(
                    BranchingDialogueOption(
                        "Was würden Sie empfehlen? Ich esse gern vegetarisch.",
                        "Co byste doporučil/a? Rád/a jím vegetariánsky.",
                        "Dann empfehle ich unsere Gemüsepfanne mit frischen Kräutern. Sie ist auch ohne Käse erhältlich.",
                        "Pak doporučuji naši zeleninovou pánev s čerstvými bylinkami. Je k dostání také bez sýra.",
                        "vegetarian",
                        DialogueAnswerQuality.RECOMMENDED,
                        "Výborně. Řekneš, co jíš rád/a, a zároveň požádáš o doporučení."
                    ),
                    BranchingDialogueOption(
                        "Ich nehme die Tagesempfehlung, bitte. Was ist heute darin?",
                        "Dám si denní nabídku, prosím. Co v ní dnes je?",
                        "Heute gibt es eine Kartoffelsuppe und danach gebratenes Gemüse.",
                        "Dnes máme bramborovou polévku a potom pečenou zeleninu.",
                        "order",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Správná a přirozená odpověď, pokud chceš denní nabídku."
                    ),
                    BranchingDialogueOption(
                        "Ich möchte die Rechnung, aber ich habe noch gar nichts gegessen.",
                        "Chci účet, ale ještě jsem vůbec nejedl/a.",
                        "Möchten Sie vielleicht zuerst bestellen? Ich bringe Ihnen gern die Speisekarte.",
                        "Nechcete si nejprve objednat? Rád vám přinesu jídelní lístek.",
                        "clarify_order",
                        DialogueAnswerQuality.NEEDS_WORK,
                        "V této chvíli se ještě vybírá jídlo, účet přijde až později."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "vegetarian",
                promptGerman = "Möchten Sie das Gericht mit Käse oder lieber ohne Milchprodukte?",
                promptCzech = "Přejete si jídlo se sýrem, nebo raději bez mléčných výrobků?",
                options = listOf(
                    BranchingDialogueOption(
                        "Bitte ohne Milchprodukte. Ich vertrage keine Milch.",
                        "Prosím bez mléčných výrobků. Nesnáším mléko.",
                        "Kein Problem. Ich notiere das und frage in der Küche noch einmal nach.",
                        "Žádný problém. Poznamenám si to a ještě se zeptám v kuchyni.",
                        "order",
                        DialogueAnswerQuality.RECOMMENDED,
                        "„Ich vertrage keine Milch“ je užitečná věta pro vysvětlení intolerance."
                    ),
                    BranchingDialogueOption(
                        "Mit Käse ist es in Ordnung, aber bitte nicht zu viel.",
                        "Se sýrem je to v pořádku, ale prosím ne moc.",
                        "Natürlich, ich gebe Ihre Bitte an die Küche weiter.",
                        "Samozřejmě, předám vaše přání do kuchyně.",
                        "order",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Jasně a zdvořile upřesňuješ, co ti vyhovuje."
                    ),
                    BranchingDialogueOption(
                        "Ich nehme lieber ein großes Steak.",
                        "Raději si dám velký steak.",
                        "Das ist natürlich möglich. Soll ich Ihnen unsere Fleischgerichte zeigen?",
                        "To je samozřejmě možné. Mám vám ukázat naše masové pokrmy?",
                        "order",
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Změnil/a jsi požadavek na vegetariánské doporučení. Vysvětli raději své omezení."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "order",
                promptGerman = "Möchten Sie noch etwas trinken?",
                promptCzech = "Přejete si ještě něco k pití?",
                options = listOf(
                    BranchingDialogueOption(
                        "Ein stilles Wasser, bitte. Das wäre alles, danke.",
                        "Neperlivou vodu, prosím. To by bylo vše, děkuji.",
                        "Sehr gern. Ich wünsche Ihnen einen guten Appetit!",
                        "Velmi rád. Přeji vám dobrou chuť!",
                        null,
                        DialogueAnswerQuality.RECOMMENDED,
                        "„Das wäre alles“ zdvořile uzavírá objednávku."
                    ),
                    BranchingDialogueOption(
                        "Haben Sie auch einen ungesüßten Tee?",
                        "Máte také neslazený čaj?",
                        "Ja, wir haben schwarzen und grünen Tee ohne Zucker.",
                        "Ano, máme černý a zelený čaj bez cukru.",
                        null,
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Dobrá doplňující otázka. Umíš upřesnit, co přesně chceš."
                    ),
                    BranchingDialogueOption(
                        "Ich möchte gern bezahlen.",
                        "Chtěl/a bych zaplatit.",
                        "Gern, ich bringe Ihnen die Rechnung, sobald Ihre Bestellung fertig ist.",
                        "Rád, přinesu vám účet, jakmile bude vaše objednávka hotová.",
                        null,
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Na nápoj se obvykle odpovídá výběrem nápoje nebo odmítnutím, ne žádostí o účet."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "clarify_arrival",
                promptGerman = "Gern helfe ich Ihnen. Möchten Sie einen Tisch reservieren oder nach einem freien Tisch fragen?",
                promptCzech = "Rád vám pomohu. Chcete si rezervovat stůl, nebo se zeptat na volné místo?",
                options = listOf(
                    BranchingDialogueOption(
                        "Entschuldigung, ich suche einen freien Tisch für zwei Personen.",
                        "Promiňte, hledám volný stůl pro dvě osoby.",
                        "Kein Problem. Ich sehe nach, was wir Ihnen anbieten können.",
                        "Žádný problém. Podívám se, co vám můžeme nabídnout.",
                        "wait",
                        DialogueAnswerQuality.RECOMMENDED,
                        "Teď už jasně říkáš, co potřebuješ, a uvádíš počet osob."
                    ),
                    BranchingDialogueOption(
                        "Ich habe doch reserviert. Die Reservierung ist auf den Namen Novák.",
                        "Vždyť mám rezervaci. Je na jméno Novák.",
                        "Einen Moment bitte, ich suche noch einmal. Ah, jetzt habe ich sie gefunden.",
                        "Moment prosím, znovu se podívám. Aha, teď jsem ji našel.",
                        "seated",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Tato odpověď dává smysl, pokud jsi rezervaci skutečně vytvořil/a."
                    ),
                    BranchingDialogueOption(
                        "Wo kann ich hier ein Zugticket kaufen?",
                        "Kde si zde mohu koupit jízdenku na vlak?",
                        "Die Auskunft ist am Bahnhof. Hier sind Sie in einem Restaurant.",
                        "Informace jsou na nádraží. Tady jste v restauraci.",
                        "wait",
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Pozor na místo a situaci. Jsi v restauraci, ne na nádraží."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "clarify_order",
                promptGerman = "Natürlich. Möchten Sie zuerst bestellen oder haben Sie eine Frage zu den Gerichten?",
                promptCzech = "Samozřejmě. Chcete si nejprve objednat, nebo máte otázku k jídlům?",
                options = listOf(
                    BranchingDialogueOption(
                        "Ich möchte zuerst etwas bestellen. Was ist heute besonders frisch?",
                        "Nejprve si chci něco objednat. Co je dnes obzvlášť čerstvé?",
                        "Unser Gemüse kommt heute Morgen vom Markt. Ich kann Ihnen ein Gericht damit empfehlen.",
                        "Naše zelenina je dnes ráno z trhu. Mohu vám doporučit jídlo, které ji obsahuje.",
                        "order",
                        DialogueAnswerQuality.RECOMMENDED,
                        "Vhodně se vracíš k objednání a ptáš se na nabídku."
                    ),
                    BranchingDialogueOption(
                        "Könnten Sie mir die Speisekarte noch einmal bringen?",
                        "Mohl/a byste mi jídelní lístek ještě jednou přinést?",
                        "Selbstverständlich. Hier ist die Karte.",
                        "Samozřejmě. Tady je jídelní lístek.",
                        "order",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Tohle je zdvořilá žádost o jídelní lístek."
                    ),
                    BranchingDialogueOption(
                        "Ich möchte sofort gehen, obwohl ich noch nichts bestellt habe.",
                        "Chci hned odejít, přestože jsem si nic neobjednal/a.",
                        "Natürlich. Ich wünsche Ihnen noch einen schönen Abend.",
                        "Samozřejmě. Přeji vám hezký večer.",
                        null,
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Věta je gramaticky srozumitelná, ale rozhovor už ukončuje. Zvol ji jen tehdy, když opravdu odcházíš."
                    )
                )
            )
        )
    ),
    BranchingDialogueTopic(
        id = "hotel_branch_b1",
        title = "Hotelová recepce – komplikace s rezervací",
        subtitle = "Příjezd, potvrzení rezervace, pokoj a řešení problému · B1",
        level = "B1",
        startNodeId = "check_in",
        nodes = listOf(
            BranchingDialogueNode(
                id = "check_in",
                promptGerman = "Guten Abend. Haben Sie eine Reservierung bei uns?",
                promptCzech = "Dobrý večer. Máte u nás rezervaci?",
                options = listOf(
                    BranchingDialogueOption(
                        "Ja, ich habe ein Doppelzimmer für drei Nächte auf den Namen Dvořák gebucht.",
                        "Ano, rezervoval/a jsem dvoulůžkový pokoj na tři noci na jméno Dvořák.",
                        "Vielen Dank. Ich habe Ihre Buchung gefunden. Darf ich kurz Ihren Ausweis sehen?",
                        "Děkuji. Našel jsem vaši rezervaci. Mohu na chvíli vidět váš doklad?",
                        "identity",
                        DialogueAnswerQuality.RECOMMENDED,
                        "Dobrá odpověď obsahuje typ pokoje, délku pobytu i jméno rezervace."
                    ),
                    BranchingDialogueOption(
                        "Ich bin mir nicht sicher, ob die Buchung bestätigt wurde. Ich habe aber eine E-Mail dabei.",
                        "Nejsem si jistý/jistá, zda byla rezervace potvrzena. Mám ale s sebou e-mail.",
                        "Kein Problem. Zeigen Sie mir bitte die Bestätigung, dann prüfe ich die Buchung.",
                        "Žádný problém. Ukažte mi prosím potvrzení a rezervaci prověřím.",
                        "confirmation",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Velmi užitečná a zdvořilá formulace pro nejistou rezervaci."
                    ),
                    BranchingDialogueOption(
                        "Wo kann ich hier frühstücken?",
                        "Kde se zde mohu nasnídat?",
                        "Das Frühstück servieren wir morgen ab sieben Uhr. Zuerst sollten wir aber Ihre Ankunft klären.",
                        "Snídani podáváme zítra od sedmi. Nejprve bychom ale měli vyřešit váš příjezd.",
                        "confirmation",
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Recepce se ptá na rezervaci. Na snídani se můžeš zeptat po vyřízení příjezdu."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "identity",
                promptGerman = "Vielen Dank. Möchten Sie ein Zimmer in einer höheren Etage oder lieber in der Nähe des Aufzugs?",
                promptCzech = "Děkuji. Přejete si pokoj ve vyšším patře, nebo raději blízko výtahu?",
                options = listOf(
                    BranchingDialogueOption(
                        "Eine höhere Etage wäre mir lieber, möglichst mit Blick auf die Stadt.",
                        "Více by mi vyhovovalo vyšší patro, pokud možno s výhledem na město.",
                        "Ich notiere Ihren Wunsch. Ich kann Ihnen ein Zimmer im fünften Stock anbieten.",
                        "Poznamenám si vaše přání. Mohu vám nabídnout pokoj v pátém patře.",
                        "room_ready",
                        DialogueAnswerQuality.RECOMMENDED,
                        "„Wäre mir lieber“ je přirozená zdvořilá formulace preference."
                    ),
                    BranchingDialogueOption(
                        "Ein Zimmer in der Nähe des Aufzugs wäre praktischer, weil ich viel Gepäck habe.",
                        "Pokoj blízko výtahu by byl praktičtější, protože mám hodně zavazadel.",
                        "Natürlich. Ich suche ein passendes Zimmer in der Nähe des Aufzugs.",
                        "Samozřejmě. Najdu vhodný pokoj poblíž výtahu.",
                        "room_ready",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Přirozeně vysvětluješ praktický důvod své preference."
                    ),
                    BranchingDialogueOption(
                        "Ich möchte das Frühstück jetzt bezahlen.",
                        "Chci nyní zaplatit snídani.",
                        "Das können wir gern später erledigen. Zuerst gebe ich Ihnen Ihre Zimmerkarte.",
                        "To můžeme vyřídit později. Nejprve vám předám kartu od pokoje.",
                        "room_ready",
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Recepce se ptá na umístění pokoje, ne na platbu za snídani."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "confirmation",
                promptGerman = "Auf der E-Mail sehe ich eine Buchungsnummer. Können Sie mir bitte die letzten vier Ziffern nennen?",
                promptCzech = "V e-mailu vidím číslo rezervace. Můžete mi prosím říci poslední čtyři číslice?",
                options = listOf(
                    BranchingDialogueOption(
                        "Natürlich. Die letzten vier Ziffern sind vier, acht, zwei und eins.",
                        "Samozřejmě. Poslední čtyři číslice jsou čtyři, osm, dva a jedna.",
                        "Danke. Jetzt habe ich die Reservierung gefunden. Es gab nur einen Schreibfehler im Nachnamen.",
                        "Děkuji. Teď jsem rezervaci našel. V příjmení byla pouze chyba v zápisu.",
                        "spelling",
                        DialogueAnswerQuality.RECOMMENDED,
                        "Při sdělování čísla vyslovuj číslice jednotlivě a zřetelně."
                    ),
                    BranchingDialogueOption(
                        "Ich kann Ihnen die ganze E-Mail zeigen, wenn das einfacher ist.",
                        "Mohu vám ukázat celý e-mail, pokud je to jednodušší.",
                        "Das wäre sehr hilfreich. Ich vergleiche die Angaben mit unserem System.",
                        "To by velmi pomohlo. Porovnám údaje s naším systémem.",
                        "spelling",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Nabízíš alternativní způsob ověření rezervace."
                    ),
                    BranchingDialogueOption(
                        "Ich habe keine Zeit. Geben Sie mir einfach irgendein Zimmer.",
                        "Nemám čas. Prostě mi dejte nějaký pokoj.",
                        "Ich verstehe, dass Sie es eilig haben. Ohne bestätigte Buchung darf ich jedoch kein Zimmer zuweisen.",
                        "Chápu, že spěcháte. Bez potvrzené rezervace ale nemohu přidělit pokoj.",
                        "spelling",
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Rozkazovací způsob zní nezdvořile. U podobné komplikace je lepší spolupracovat."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "spelling",
                promptGerman = "Vielen Dank. Soll ich den Namen in der Reservierung korrigieren?",
                promptCzech = "Děkuji. Mám opravit jméno v rezervaci?",
                options = listOf(
                    BranchingDialogueOption(
                        "Ja, bitte korrigieren Sie den Nachnamen. Der richtige Name ist Dvořák.",
                        "Ano, opravte prosím příjmení. Správné jméno je Dvořák.",
                        "Erledigt. Ihre Reservierung ist jetzt korrekt hinterlegt.",
                        "Hotovo. Vaše rezervace je nyní správně zanesena.",
                        "room_ready",
                        DialogueAnswerQuality.RECOMMENDED,
                        "Jasně potvrzuješ opravu a zopakuješ správný údaj."
                    ),
                    BranchingDialogueOption(
                        "Nein, der Name auf dem Ausweis ist wichtiger. Ich zeige ihn Ihnen gern.",
                        "Ne, důležitější je jméno na dokladu. Rád/a vám ho ukážu.",
                        "Das ist in Ordnung. Ich gleiche die Angaben mit Ihrem Ausweis ab.",
                        "To je v pořádku. Porovnám údaje s vaším dokladem.",
                        "room_ready",
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Také funkční řešení. Nabízíš ověření podle dokladu."
                    ),
                    BranchingDialogueOption(
                        "Können Sie mir stattdessen ein Taxi bestellen?",
                        "Můžete mi místo toho objednat taxi?",
                        "Gern nach dem Check-in. Zuerst schließen wir bitte die Anmeldung ab.",
                        "Rád po check-inu. Nejdříve prosím dokončíme registraci.",
                        "room_ready",
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Téma taxi odvádí pozornost od vyřešení chybného jména."
                    )
                )
            ),
            BranchingDialogueNode(
                id = "room_ready",
                promptGerman = "Ihr Zimmer ist vorbereitet. Möchten Sie noch etwas über das Hotel wissen?",
                promptCzech = "Váš pokoj je připraven. Chcete se ještě něco dozvědět o hotelu?",
                options = listOf(
                    BranchingDialogueOption(
                        "Wann wird das Frühstück serviert, und wo befindet sich der Speisesaal?",
                        "Kdy se podává snídaně a kde se nachází jídelna?",
                        "Das Frühstück gibt es von sieben bis zehn Uhr im Erdgeschoss, gleich neben der Lobby.",
                        "Snídaně se podává od sedmi do deseti v přízemí hned vedle recepce.",
                        null,
                        DialogueAnswerQuality.RECOMMENDED,
                        "Skvělá otázka: spojuje čas i místo pomocí „und“."
                    ),
                    BranchingDialogueOption(
                        "Könnten Sie mir bitte das WLAN-Passwort aufschreiben?",
                        "Mohl/a byste mi prosím napsat heslo k Wi-Fi?",
                        "Natürlich. Das Passwort steht auch auf der Karte in Ihrem Zimmer.",
                        "Samozřejmě. Heslo je také na kartičce ve vašem pokoji.",
                        null,
                        DialogueAnswerQuality.ACCEPTABLE,
                        "Zdvořilá a praktická otázka, kterou host často potřebuje."
                    ),
                    BranchingDialogueOption(
                        "Ich möchte jetzt auschecken.",
                        "Chci se nyní odhlásit.",
                        "Sie sind gerade erst angekommen. Der Check-out ist am Abreisetag bis elf Uhr.",
                        "Právě jste přijel/a. Check-out je v den odjezdu do jedenácti hodin.",
                        null,
                        DialogueAnswerQuality.NEEDS_WORK,
                        "Právě proběhl check-in. Na check-out se ptáš až při odjezdu."
                    )
                )
            )
        )
    )
) + additionalBranchingDialogueTopics
internal data class BranchingDialogueReview(
    val question: ReviewQuestion,
    val wasCorrect: Boolean
)

internal fun buildBranchingDialogueReview(
    topic: BranchingDialogueTopic,
    node: BranchingDialogueNode,
    selectedOptionIndex: Int
): BranchingDialogueReview {
    require(selectedOptionIndex in node.options.indices) { "Selected dialogue option is outside the node." }
    val selected = node.options[selectedOptionIndex]
    val recommendedIndex = node.options.indexOfFirst {
        it.quality == DialogueAnswerQuality.RECOMMENDED
    }
    require(recommendedIndex >= 0) { "Every branching dialogue node needs a recommended answer." }
    val recommended = node.options[recommendedIndex]
    val wasCorrect = selected.quality != DialogueAnswerQuality.NEEDS_WORK
    val correctIndex = if (wasCorrect) selectedOptionIndex else recommendedIndex
    val explanation = if (wasCorrect) {
        selected.feedback
    } else {
        "${selected.feedback} Lepší formulace: ${recommended.german} — ${recommended.czech}"
    }
    return BranchingDialogueReview(
        question = ReviewQuestion(
            id = stableReviewId("dialogue", topic.id, node.id),
            sourceKind = "phrases",
            sourceTitle = "Beatrice · ${topic.title}",
            prompt = "${node.promptGerman}\n${node.promptCzech}",
            choices = node.options.map { it.german },
            correctIndex = correctIndex,
            explanation = explanation,
            germanSentence = if (wasCorrect) selected.german else recommended.german,
            translation = if (wasCorrect) selected.czech else recommended.czech,
            cefrLevel = topic.level
        ),
        wasCorrect = wasCorrect
    )
}
