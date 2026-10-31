package com.deutschai.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val numberWordToolGroupIds = setOf("numbers", "ordinal-numbers", "multiplicative-numbers")

internal data class NumberPracticeQuestion(
    val id: String,
    val kind: String,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val fullGerman: String,
    val fullCzech: String,
    val explanation: String
)

private data class DrillSeed(
    val id: String, val kind: String, val answer: String, val sentence: String, val czech: String
)

private fun parseBasicSeed(row: String): DrillSeed {
    val p = row.split("|", limit = 4)
    require(p.size == 4) { "Invalid basic-number drill row: $row" }
    return DrillSeed(p[0], "basic", p[1], p[2], p[3])
}

private val basicSeeds = listOf(
    "basic-rolls-2|zwei|Ich kaufe ___ Brötchen.|Kupuji dvě housky.",
    "basic-rolls-3|drei|Ich kaufe ___ Brötchen.|Kupuji tři housky.",
    "basic-rolls-4|vier|Ich kaufe ___ Brötchen.|Kupuji čtyři housky.",
    "basic-rolls-5|fünf|Ich kaufe ___ Brötchen.|Kupuji pět housek.",
    "basic-rolls-6|sechs|Ich kaufe ___ Brötchen.|Kupuji šest housek.",
    "basic-rolls-7|sieben|Ich kaufe ___ Brötchen.|Kupuji sedm housek.",
    "basic-rolls-8|acht|Ich kaufe ___ Brötchen.|Kupuji osm housek.",
    "basic-rolls-9|neun|Ich kaufe ___ Brötchen.|Kupuji devět housek.",
    "basic-rolls-10|zehn|Ich kaufe ___ Brötchen.|Kupuji deset housek.",
    "basic-minutes-2|zwei|Ich brauche ___ Minuten bis zum Bahnhof.|Na cestu na nádraží potřebuji dvě minuty.",
    "basic-minutes-3|drei|Ich brauche ___ Minuten bis zum Bahnhof.|Na cestu na nádraží potřebuji tři minuty.",
    "basic-minutes-4|vier|Ich brauche ___ Minuten bis zum Bahnhof.|Na cestu na nádraží potřebuji čtyři minuty.",
    "basic-minutes-5|fünf|Ich brauche ___ Minuten bis zum Bahnhof.|Na cestu na nádraží potřebuji pět minut.",
    "basic-minutes-6|sechs|Ich brauche ___ Minuten bis zum Bahnhof.|Na cestu na nádraží potřebuji šest minut.",
    "basic-minutes-7|sieben|Ich brauche ___ Minuten bis zum Bahnhof.|Na cestu na nádraží potřebuji sedm minut.",
    "basic-minutes-8|acht|Ich brauche ___ Minuten bis zum Bahnhof.|Na cestu na nádraží potřebuji osm minut.",
    "basic-minutes-9|neun|Ich brauche ___ Minuten bis zum Bahnhof.|Na cestu na nádraží potřebuji devět minut.",
    "basic-ticket-10|zehn|Das Ticket kostet ___ Euro.|Jízdenka stojí deset eur.",
    "basic-ticket-11|elf|Das Ticket kostet ___ Euro.|Jízdenka stojí jedenáct eur.",
    "basic-ticket-12|zwölf|Das Ticket kostet ___ Euro.|Jízdenka stojí dvanáct eur.",
    "basic-ticket-13|dreizehn|Das Ticket kostet ___ Euro.|Jízdenka stojí třináct eur.",
    "basic-ticket-14|vierzehn|Das Ticket kostet ___ Euro.|Jízdenka stojí čtrnáct eur.",
    "basic-ticket-15|fünfzehn|Das Ticket kostet ___ Euro.|Jízdenka stojí patnáct eur.",
    "basic-ticket-20|zwanzig|Das Ticket kostet ___ Euro.|Jízdenka stojí dvacet eur.",
    "basic-ticket-50|fünfzig|Das Ticket kostet ___ Euro.|Jízdenka stojí padesát eur.",
    "basic-age-18|achtzehn|Mein Bruder ist ___ Jahre alt.|Mému bratrovi je osmnáct let.",
    "basic-age-19|neunzehn|Mein Bruder ist ___ Jahre alt.|Mému bratrovi je devatenáct let.",
    "basic-age-20|zwanzig|Mein Bruder ist ___ Jahre alt.|Mému bratrovi je dvacet let.",
    "basic-age-21|einundzwanzig|Mein Bruder ist ___ Jahre alt.|Mému bratrovi je dvacet jedna let.",
    "basic-age-25|fünfundzwanzig|Mein Bruder ist ___ Jahre alt.|Mému bratrovi je dvacet pět let.",
    "basic-age-30|dreißig|Mein Bruder ist ___ Jahre alt.|Mému bratrovi je třicet let.",
    "basic-age-40|vierzig|Mein Bruder ist ___ Jahre alt.|Mému bratrovi je čtyřicet let.",
    "basic-age-50|fünfzig|Mein Bruder ist ___ Jahre alt.|Mému bratrovi je padesát let.",
    "basic-people-12|zwölf|Heute sind ___ Personen im Büro.|Dnes je v kanceláři dvanáct lidí."
).map(::parseBasicSeed)

private data class OrdinalForm(val base: String, val czech: String, val locative: String, val capitalized: String)

private val ordinalForms = listOf(
    OrdinalForm("erste", "první", "prvním", "První"),
    OrdinalForm("zweite", "druhý", "druhém", "Druhý"),
    OrdinalForm("dritte", "třetí", "třetím", "Třetí"),
    OrdinalForm("vierte", "čtvrtý", "čtvrtém", "Čtvrtý"),
    OrdinalForm("fünfte", "pátý", "pátém", "Pátý"),
    OrdinalForm("sechste", "šestý", "šestém", "Šestý"),
    OrdinalForm("siebte", "sedmý", "sedmém", "Sedmý"),
    OrdinalForm("achte", "osmý", "osmém", "Osmý"),
    OrdinalForm("neunte", "devátý", "devátém", "Devátý"),
    OrdinalForm("zehnte", "desátý", "desátém", "Desátý"),
    OrdinalForm("elfte", "jedenáctý", "jedenáctém", "Jedenáctý")
)

private val ordinalSeeds = ordinalForms.flatMapIndexed { index, form ->
    val dative = form.base.removeSuffix("e") + "en"
    listOf(
        DrillSeed("ordinal-day-${index + 1}", "ordinal", dative,
            "Am ___ Tag der Reise besuchen wir das Museum.",
            "${form.capitalized} den cesty navštívíme muzeum."),
        DrillSeed("ordinal-floor-${index + 1}", "ordinal", dative,
            "Wir wohnen im ___ Stock des Hotels.",
            "Bydlíme v ${form.locative} patře hotelu."),
        DrillSeed("ordinal-attempt-${index + 1}", "ordinal", form.base,
            "Das ist der ___ Versuch.",
            "To je ${form.czech} pokus.")
    )
}

private data class MultForm(val word: String, val czech: String)

private val multiplicativeForms = listOf(
    MultForm("einmal", "jednou"), MultForm("zweimal", "dvakrát"),
    MultForm("dreimal", "třikrát"), MultForm("viermal", "čtyřikrát"),
    MultForm("fünfmal", "pětkrát"), MultForm("sechsmal", "šestkrát"),
    MultForm("siebenmal", "sedmkrát"), MultForm("achtmal", "osmkrát"),
    MultForm("neunmal", "devětkrát"), MultForm("zehnmal", "desetkrát"),
    MultForm("elfmal", "jedenáctkrát")
)

private val multiplicativeSeeds = multiplicativeForms.flatMapIndexed { index, form ->
    listOf(
        DrillSeed("mult-read-${index + 1}", "multiplicative", form.word,
            "Ich lese den Satz ___ laut vor.",
            "Čtu větu ${form.czech} nahlas."),
        DrillSeed("mult-bus-${index + 1}", "multiplicative", form.word,
            "Der Bus fährt ___ am Tag.",
            "Autobus jezdí ${form.czech} denně."),
        DrillSeed("mult-call-${index + 1}", "multiplicative", form.word,
            "Wir haben ___ versucht, dich anzurufen.",
            "Zkusili jsme ti zavolat ${form.czech}.")
    )
}

private fun allDrillSeeds(): List<DrillSeed> = basicSeeds + ordinalSeeds + multiplicativeSeeds
private fun kindLabel(kind: String): String = when (kind) {
    "basic" -> "základní číslovka"
    "ordinal" -> "řadová číslovka"
    else -> "násobná číslovka"
}

internal fun buildMixedNumberDrillQuestions(): List<NumberPracticeQuestion> {
    val seeds = allDrillSeeds()
    check(seeds.size == 100) { "The mixed number drill must contain exactly 100 questions." }
    val optionsByKind = seeds.groupBy { it.kind }.mapValues { (_, rows) -> rows.map { it.answer }.distinct() }
    val kinds = listOf("basic", "ordinal", "multiplicative")
    return seeds.shuffled().map { seed ->
        val otherKinds = kinds.filter { it != seed.kind }
        val sameKindDistractor = optionsByKind.getValue(seed.kind).filter { it != seed.answer }.shuffled().first()
        val distractors = listOf(
            optionsByKind.getValue(otherKinds[0]).shuffled().first(),
            optionsByKind.getValue(otherKinds[1]).shuffled().first(),
            sameKindDistractor
        )
        val choices = (listOf(seed.answer) + distractors).shuffled()
        NumberPracticeQuestion(
            id = seed.id,
            kind = seed.kind,
            prompt = "Doplň správnou číslovku do věty. Pozor na její druh:\n${seed.sentence}",
            choices = choices,
            correctIndex = choices.indexOf(seed.answer),
            fullGerman = seed.sentence.replace("___", seed.answer),
            fullCzech = seed.czech,
            explanation = "Správně je „${seed.answer}“ – jde o ${kindLabel(seed.kind)}."
        )
    }
}

internal fun makeNumberCategoryQuestions(groupId: String): List<NumberPracticeQuestion> {
    require(groupId in numberWordToolGroupIds) { "Unknown number category: $groupId" }
    val group = wordToolGroups.single { it.id == groupId }
    val label = when (groupId) {
        "numbers" -> "základní číslovku"
        "ordinal-numbers" -> "řadovou číslovku"
        else -> "násobnou číslovku"
    }
    val kind = when (groupId) {
        "numbers" -> "basic"
        "ordinal-numbers" -> "ordinal"
        else -> "multiplicative"
    }
    return group.items.shuffled().map { item ->
        val pattern = Regex("(?i)(?<!\\p{L})" + Regex.escape(item.german) + "(?!\\p{L})")
        val blank = pattern.replaceFirst(item.sentenceGerman, "___")
        val choices = (listOf(item) + group.items.filter { it.german != item.german }.shuffled().take(3))
            .shuffled().map { it.german }
        NumberPracticeQuestion(
            id = "number-${group.id}-${item.german}-${item.sentenceGerman.hashCode()}",
            kind = kind,
            prompt = "Doplň správnou $label:\n$blank\nČeský význam: ${item.czech}",
            choices = choices,
            correctIndex = choices.indexOf(item.german),
            fullGerman = item.sentenceGerman,
            fullCzech = item.sentenceCzech,
            explanation = "„${item.german}“ znamená „${item.czech}“."
        )
    }
}

@Composable
internal fun NumberPracticeScreen(
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int, activityKey: String) -> Unit
) {
    LocalContext.current
    var selectedSet by remember { mutableStateOf<String?>(null) }
    var questions by remember { mutableStateOf<List<NumberPracticeQuestion>>(emptyList()) }
    var questionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    fun startSet(setId: String) {
        selectedSet = setId
        questions = if (setId == "mixed") buildMixedNumberDrillQuestions() else {
            val groupId = when (setId) {
                "basic" -> "numbers"
                "ordinal" -> "ordinal-numbers"
                else -> "multiplicative-numbers"
            }
            makeNumberCategoryQuestions(groupId)
        }
        questionIndex = 0
        selectedAnswer = null
        score = 0
        finished = false
    }

    fun goBackOneLevel() {
        if (selectedSet != null) {
            selectedSet = null
            questions = emptyList()
            selectedAnswer = null
            finished = false
        } else onBack()
    }

    val title = when (selectedSet) {
        "basic" -> "Základní číslovky"
        "ordinal" -> "Řadové číslovky"
        "multiplicative" -> "Násobné číslovky"
        "mixed" -> "Smíšený dril"
        else -> "Číslovky"
    }
    val question = questions.getOrNull(questionIndex)

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { goBackOneLevel() },
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    androidx.compose.material3.Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                    Text(title, color = Muted, fontSize = 13.sp)
                }
            }

            if (selectedSet == null) {
                Text("Číslovky", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Procvičuj každý druh samostatně, nebo si vyzkoušej smíšený dril. V něm se typy číslovek střídají a musíš je rozlišovat podle významu i větného kontextu.",
                    color = Muted, fontSize = 14.sp, lineHeight = 21.sp
                )
                NumberCategoryCard("1", "Základní číslovky", "eins, zwei, drei… Počty, věk, ceny a čas.", "25 otázek") { startSet("basic") }
                NumberCategoryCard("2", "Řadové číslovky", "erste, zweite, dritte… Pořadí, data a umístění.", "25 otázek") { startSet("ordinal") }
                NumberCategoryCard("3", "Násobné číslovky", "einmal, zweimal, dreimal… Opakování a četnost.", "25 otázek") { startSet("multiplicative") }
                NumberCategoryCard(
                    "4", "Smíšený dril",
                    "Krátké věty, ve kterých se střídají všechny tři druhy číslovek.",
                    "100 náhodně seřazených otázek", highlighted = true
                ) { startSet("mixed") }
            } else if (finished) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Sada dokončena", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                        Text("$score z " + questions.size + " správně", color = Gold, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                        Text(
                            if (score == questions.size) "Výborně! Všechny číslovky máš správně."
                            else "Při dalším kole se pořadí otázek i možností znovu zamíchá.",
                            color = Color(0xFFE0EAE5), fontSize = 14.sp, lineHeight = 21.sp
                        )
                    }
                }
                Button(
                    onClick = { startSet(selectedSet!!) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zopakovat – zamíchat otázky", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = { selectedSet = null; questions = emptyList(); finished = false },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Vybrat jinou podkategorii", fontWeight = FontWeight.Bold) }
            } else if (question != null) {
                Text(
                    "OTÁZKA " + (questionIndex + 1) + " Z " + questions.size,
                    color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp
                )
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Text(
                        question.prompt,
                        modifier = Modifier.padding(21.dp),
                        color = Ink, fontSize = 19.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold
                    )
                }
                question.choices.forEachIndexed { index, choice ->
                    val background = when {
                        selectedAnswer == null -> OptionSurface
                        index == question.correctIndex -> Correct
                        index == selectedAnswer -> Incorrect
                        else -> OptionSurface
                    }
                    Card(
                        onClick = {
                            if (selectedAnswer == null) {
                                selectedAnswer = index
                                if (index == question.correctIndex) score += 1
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = background)
                    ) {
                        Text(
                            choice,
                            modifier = Modifier.fillMaxWidth().padding(17.dp),
                            color = Ink, fontSize = 17.sp,
                            fontWeight = if (selectedAnswer != null && index == question.correctIndex) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
                if (selectedAnswer != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = if (selectedAnswer == question.correctIndex) Correct else Incorrect)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                            Text(
                                if (selectedAnswer == question.correctIndex) "Správně! Gut gemacht!"
                                else "Správná odpověď: " + question.choices[question.correctIndex],
                                color = ForestText, fontWeight = FontWeight.Bold, fontSize = 16.sp
                            )
                            if (selectedSet == "mixed") {
                                Text("Druh: " + kindLabel(question.kind), color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("Celá německá věta:", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(question.fullGerman, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, lineHeight = 23.sp)
                            Text("Český překlad:", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(question.fullCzech, color = Ink, fontSize = 15.sp, lineHeight = 22.sp)
                            Text(question.explanation, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                        }
                    }
                    Button(
                        onClick = {
                            if (questionIndex + 1 < questions.size) {
                                questionIndex += 1
                                selectedAnswer = null
                            } else {
                                finished = true
                                onComplete(score, questions.size, "numbers_" + selectedSet)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(if (questionIndex + 1 < questions.size) "Další otázka" else "Dokončit sadu", fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

@Composable
private fun NumberCategoryCard(
    number: String,
    title: String,
    subtitle: String,
    detail: String,
    highlighted: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (highlighted) Mint else CardSurface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(17.dp),
            horizontalArrangement = Arrangement.spacedBy(13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(46.dp).background(if (highlighted) Forest else Mint, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(number, color = if (highlighted) Color.White else ForestText, fontWeight = FontWeight.Bold, fontSize = 19.sp)
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, color = if (highlighted) ForestText else Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                Text(detail, color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
