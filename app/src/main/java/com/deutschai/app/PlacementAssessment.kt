package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.School
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
import org.json.JSONObject
import kotlin.random.Random

internal data class PlacementAssessmentQuestion(
    val id: String,
    val level: String,
    val skill: String,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val germanSentence: String = "",
    val translation: String = "",
    val passage: String = ""
)

internal data class PlacementSkillResult(val correct: Int, val total: Int) {
    val accuracyPercent: Int get() = if (total == 0) 0 else correct * 100 / total
}

internal data class PlacementAssessmentReport(
    val estimatedLevel: String,
    val assessedAtMillis: Long,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val levelResults: Map<String, PlacementSkillResult>,
    val skillResults: Map<String, PlacementSkillResult>
)

private data class PlacementQuestionSeed(
    val id: String,
    val level: String,
    val skill: String,
    val prompt: String,
    val correctAnswer: String,
    val distractors: List<String>,
    val explanation: String,
    val germanSentence: String = "",
    val translation: String = ""
)

private val placementQuestionSeeds = listOf(
    PlacementQuestionSeed("a1-verb", "A1", "Gramatika", "Doplň: Ich ___ aus Tschechien.", "komme",
        listOf("kommt", "kommen", "kommst"), "U „ich“ má sloveso tvar „komme“.",
        "Ich komme aus Tschechien.", "Pocházím z Česka."),
    PlacementQuestionSeed("a1-article", "A1", "Gramatika", "Doplň: Ich sehe ___ Mann.", "den",
        listOf("der", "dem", "des"), "Mužský rod v tomto předmětu vyžaduje 4. pád.",
        "Ich sehe den Mann.", "Vidím toho muže."),
    PlacementQuestionSeed("a1-gestern", "A1", "Slovní zásoba a fráze", "Co znamená „gestern“?", "včera",
        listOf("dnes", "zítra", "brzy"), "„Gestern“ znamená „včera“."),
    PlacementQuestionSeed("a1-time", "A1", "Slovní zásoba a fráze", "8:30 se německy řekne:", "halb neun",
        listOf("halb acht", "acht Uhr halb", "neun halb"), "„Halb neun“ znamená půl deváté.",
        "Es ist halb neun.", "Je půl deváté."),

    PlacementQuestionSeed("a2-perfect", "A2", "Gramatika", "Doplň: Gestern ___ ich lange gearbeitet.", "habe",
        listOf("bin", "hatte", "werde"), "Sloveso „arbeiten“ tvoří Perfekt se slovesem „haben“.",
        "Gestern habe ich lange gearbeitet.", "Včera jsem dlouho pracoval/a."),
    PlacementQuestionSeed("a2-preposition", "A2", "Gramatika", "Doplň: Ich freue mich ___ deinen Besuch.", "auf",
        listOf("über", "an", "um"), "Na budoucí událost se těšíme vazbou „sich freuen auf“.",
        "Ich freue mich auf deinen Besuch.", "Těším se na tvou návštěvu."),
    PlacementQuestionSeed("a2-erlaubnis", "A2", "Slovní zásoba a fráze", "Co znamená „die Erlaubnis“?", "povolení",
        listOf("zkušenost", "pozvánka", "rozhodnutí"), "„Die Erlaubnis“ znamená „povolení“."),
    PlacementQuestionSeed("a2-relative", "A2", "Slovní zásoba a fráze", "Doplň: Das Café, in ___ wir uns treffen.", "dem",
        listOf("den", "das", "dessen"), "Předložka „in“ zde vyžaduje 3. pád.",
        "Das ist das Café, in dem wir uns treffen.", "To je kavárna, kde se setkáváme."),

    PlacementQuestionSeed("b1-obwohl", "B1", "Gramatika", "Doplň: Obwohl es regnete, ___ wir spazieren.", "gingen",
        listOf("gehen", "sind", "gegangen"), "Po vedlejší větě s „obwohl“ následuje v hlavní větě časované sloveso.",
        "Obwohl es regnete, gingen wir spazieren.", "Přestože pršelo, šli jsme na procházku."),
    PlacementQuestionSeed("b1-kon2", "B1", "Gramatika", "Doplň: Hätte ich das gewusst, ___ ich früher gekommen.", "wäre",
        listOf("bin", "war", "hätte"), "Konjunktiv II minulosti u „kommen“ tvoří „wäre gekommen“.",
        "Hätte ich das gewusst, wäre ich früher gekommen.", "Kdybych to věděl/a, přišel/přišla bych dříve."),
    PlacementQuestionSeed("b1-verlassen", "B1", "Slovní zásoba a fráze", "„Sich auf jemanden verlassen“ znamená:", "spolehnout se na někoho",
        listOf("vyhnout se někomu", "postarat se o někoho", "přizpůsobit se někomu"),
        "Vazba „sich auf jemanden verlassen“ znamená „spolehnout se na někoho“."),
    PlacementQuestionSeed("b1-verschieben", "B1", "Slovní zásoba a fráze", "„Der Termin wurde verschoben“ znamená:", "termín byl odložen",
        listOf("termín byl zrušen", "termín byl potvrzen", "termín byl zkrácen"),
        "„Verschieben“ znamená přesunout nebo odložit termín."),

    PlacementQuestionSeed("b2-als", "B2", "Gramatika", "Doplň: Er tut so, als ___ er alles.", "wüsste",
        listOf("weiß", "wissen", "gewusst"), "Po „als ob/als“ se v tomto významu používá Konjunktiv II.",
        "Er tut so, als wüsste er alles.", "Chová se, jako by všechno věděl."),
    PlacementQuestionSeed("b2-je-desto", "B2", "Gramatika", "Doplň: Je länger ich warte, ___ ungeduldiger werde ich.", "desto",
        listOf("weil", "als", "ob"), "U stupňovací konstrukce patří dvojice „je …, desto …“.",
        "Je länger ich warte, desto ungeduldiger werde ich.", "Čím déle čekám, tím jsem netrpělivější."),
    PlacementQuestionSeed("b2-betracht", "B2", "Slovní zásoba a fráze", "„Etwas in Betracht ziehen“ znamená:", "zvážit něco",
        listOf("odmítnout něco", "zapomenout na něco", "předpokládat něco"),
        "Vazba znamená vzít něco v úvahu nebo to zvážit."),
    PlacementQuestionSeed("b2-stocken", "B2", "Slovní zásoba a fráze", "„Die Verhandlungen sind ins Stocken geraten“ znamená:", "jednání se zadrhla",
        listOf("jednání skončila", "jednání byla odložena", "jednání se zrychlila"),
        "„Ins Stocken geraten“ znamená zadrhnout se nebo ustrnout."),

    PlacementQuestionSeed("c1-kritik", "C1", "Gramatika", "Doplň: Der Vorschlag stieß auf ___ Kritik.", "erhebliche",
        listOf("erheblichen", "erheblicher", "erhebliches"), "„Kritik“ je ženského rodu; po „auf“ je zde 4. pád.",
        "Der Vorschlag stieß auf erhebliche Kritik.", "Návrh se setkal se značnou kritikou."),
    PlacementQuestionSeed("c1-eingeraeumt", "C1", "Gramatika", "Doplň: Er räumte ein, den Aufwand falsch ___ zu haben.", "eingeschätzt",
        listOf("einschätzen", "einschätzt", "einschätzend"), "V konstrukci „zu haben“ stojí příčestí minulé před infinitivem.",
        "Er räumte ein, den Aufwand falsch eingeschätzt zu haben.", "Přiznal, že náročnost špatně odhadl."),
    PlacementQuestionSeed("c1-wacklig", "C1", "Slovní zásoba a fráze", "„Auf wackligen Beinen stehen“ znamená:", "mít nejisté základy",
        listOf("být téměř hotový", "být finančně zajištěný", "mít všeobecnou podporu"),
        "Obrazné spojení popisuje něco nejistého nebo nedostatečně podloženého."),
    PlacementQuestionSeed("c1-nicht-zuletzt", "C1", "Slovní zásoba a fráze", "„Nicht zuletzt“ znamená:", "v neposlední řadě",
        listOf("jen výjimečně", "bez jakékoli výjimky", "teprve později"),
        "Výraz zdůrazňuje důležitý důvod: „v neposlední řadě“."),

    PlacementQuestionSeed("c2-ungeachtet", "C2", "Gramatika", "Doplň: Ungeachtet ___ Einwände wurde entschieden.", "der",
        listOf("den", "die", "dem"), "Předložka „ungeachtet“ se pojí s 2. pádem.",
        "Ungeachtet der Einwände wurde entschieden.", "Navzdory námitkám bylo rozhodnuto."),
    PlacementQuestionSeed("c2-widerlegen", "C2", "Gramatika", "Doplň: Seine Position lässt sich kaum ___.", "widerlegen",
        listOf("widerlegt", "widerlegte", "zu widerlegen"), "Po konstrukci „sich lassen“ následuje infinitiv bez „zu“.",
        "Seine Position lässt sich kaum widerlegen.", "Jeho stanovisko lze jen těžko vyvrátit."),
    PlacementQuestionSeed("c2-mitnichten", "C2", "Slovní zásoba a fráze", "„Mitnichten“ znamená:", "v žádném případě",
        listOf("možná", "obvykle", "přinejmenším"), "„Mitnichten“ znamená „v žádném případě“ nebo „ani zdaleka ne“."),
    PlacementQuestionSeed("c2-revidieren", "C2", "Slovní zásoba a fráze", "„Eine Entscheidung revidieren“ znamená:", "přehodnotit rozhodnutí",
        listOf("veřejně oznámit rozhodnutí", "odložit rozhodnutí beze změny", "rozhodnutí potvrdit"),
        "„Revidieren“ znamená rozhodnutí přezkoumat a případně změnit.")
)

internal fun buildPlacementAssessmentQuestions(
    random: Random = Random.Default
): List<PlacementAssessmentQuestion> {
    val levels = listOf("A1", "A2", "B1", "B2", "C1", "C2")
    check(placementQuestionSeeds.size == 24) { "Placement test must contain exactly 24 questions." }
    val result = levels.flatMap { level ->
        val levelSeeds = placementQuestionSeeds.filter { it.level == level }
        check(levelSeeds.size == 4) { "Placement test must contain exactly four questions for $level." }
        levelSeeds.shuffled(random).map { seed ->
            check(seed.distractors.size == 3)
            val choices = (listOf(seed.correctAnswer) + seed.distractors).shuffled(random)
            check(choices.size == 4 && choices.distinct().size == 4) {
                "Placement question ${seed.id} must contain four unique answers."
            }
            PlacementAssessmentQuestion(
                id = stableReviewId("placement", seed.level, seed.id),
                level = seed.level,
                skill = seed.skill,
                prompt = seed.prompt,
                choices = choices,
                correctIndex = choices.indexOf(seed.correctAnswer),
                explanation = seed.explanation,
                germanSentence = seed.germanSentence,
                translation = seed.translation
            )
        }
    }
    check(result.size == 24) { "Placement test must contain exactly 24 questions." }
    check(result.map { it.id }.toSet().size == result.size) { "Placement test question IDs must be unique." }
    return result
}

internal fun evaluatePlacementAssessment(
    questions: List<PlacementAssessmentQuestion>,
    answers: Map<String, Int>,
    nowMillis: Long = System.currentTimeMillis()
): PlacementAssessmentReport {
    require(questions.isNotEmpty())
    require(questions.all { question ->
        answers[question.id]?.let { answerIndex -> answerIndex in question.choices.indices } == true
    }) {
        "Every placement question must have one selected answer."
    }
    val levels = listOf("A1", "A2", "B1", "B2", "C1", "C2")
    val levelResults = levels.associateWith { level ->
        val rows = questions.filter { it.level == level }
        PlacementSkillResult(rows.count { answers[it.id] == it.correctIndex }, rows.size)
    }
    val skillResults = questions.groupBy { it.skill }.mapValues { (_, rows) ->
        PlacementSkillResult(rows.count { answers[it.id] == it.correctIndex }, rows.size)
    }

    // A level is considered passed when the student gets at least 3 of its 4 questions right.
    // Report the highest consecutive CEFR band passed, not an isolated higher score.
    var estimated = "A1"
    for (level in levels) {
        if (levelResults.getValue(level).correct >= 3) {
            estimated = level
        } else {
            break
        }
    }

    return PlacementAssessmentReport(
        estimatedLevel = estimated,
        assessedAtMillis = nowMillis,
        totalQuestions = questions.size,
        correctAnswers = questions.count { answers[it.id] == it.correctIndex },
        levelResults = levelResults,
        skillResults = skillResults
    )
}

internal object PlacementAssessmentStore {
    private const val PREFS = "deutschai_placement_assessment"
    private const val RESULT = "latest_result_v1"

    fun save(context: Context, report: PlacementAssessmentReport) {
        val levels = JSONObject().apply {
            report.levelResults.forEach { (key, value) ->
                put(key, JSONObject().put("correct", value.correct).put("total", value.total))
            }
        }
        val skills = JSONObject().apply {
            report.skillResults.forEach { (key, value) ->
                put(key, JSONObject().put("correct", value.correct).put("total", value.total))
            }
        }
        val json = JSONObject()
            .put("estimatedLevel", report.estimatedLevel)
            .put("assessedAtMillis", report.assessedAtMillis)
            .put("totalQuestions", report.totalQuestions)
            .put("correctAnswers", report.correctAnswers)
            .put("levelResults", levels)
            .put("skillResults", skills)
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(RESULT, json.toString()).apply()
    }

    fun load(context: Context): PlacementAssessmentReport? {
        val raw = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(RESULT, null) ?: return null
        return runCatching {
            val json = JSONObject(raw)
            fun readResults(key: String): Map<String, PlacementSkillResult> {
                val values = json.optJSONObject(key) ?: return emptyMap()
                return values.keys().asSequence().mapNotNull { item ->
                    val obj = values.optJSONObject(item) ?: return@mapNotNull null
                    item to PlacementSkillResult(obj.optInt("correct"), obj.optInt("total"))
                }.toMap()
            }
            PlacementAssessmentReport(
                estimatedLevel = json.getString("estimatedLevel"),
                assessedAtMillis = json.optLong("assessedAtMillis"),
                totalQuestions = json.optInt("totalQuestions"),
                correctAnswers = json.optInt("correctAnswers"),
                levelResults = readResults("levelResults"),
                skillResults = readResults("skillResults")
            )
        }.getOrNull()
    }
}

@Composable
internal fun PlacementAssessmentScreen(
    onBack: () -> Unit,
    onComplete: (PlacementAssessmentReport) -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    var questions by remember { mutableStateOf(buildPlacementAssessmentQuestions()) }
    var index by remember { mutableStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var answers by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var report by remember { mutableStateOf<PlacementAssessmentReport?>(null) }
    val current = questions.getOrNull(index)

    fun restart() {
        questions = buildPlacementAssessmentQuestions()
        index = 0
        selected = null
        answers = emptyMap()
        report = null
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AppScreenHeader(title = "Vstupní rozřazovací test", onBack = onBack, onHome = onHome)

            if (report != null) {
                val result = report!!
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        androidx.compose.material3.Icon(Icons.Default.School, contentDescription = null, tint = Gold, modifier = Modifier.size(34.dp))
                        Text("Tvoje doporučená úroveň", color = Color.White, fontSize = 16.sp)
                        Text(result.estimatedLevel, color = Gold, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                        Text("${result.correctAnswers} z ${result.totalQuestions} správně", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            "Výsledek je orientační a doporučuje vhodný začátek. Během studia se může zpřesnit.",
                            color = Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 19.sp
                        )
                    }
                }
                Text("Výsledek podle úrovní", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                result.levelResults.forEach { (level, value) ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = CardSurface)) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(level, color = Ink, fontWeight = FontWeight.Bold)
                                Text("${value.correct}/${value.total}", color = ForestText, fontWeight = FontWeight.Bold)
                            }
                            LinearProgressIndicator(progress = { value.accuracyPercent / 100f }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
                Text("Výsledek podle dovedností", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                result.skillResults.forEach { (skill, value) ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(skill, color = Ink, fontSize = 14.sp)
                        Text("${value.accuracyPercent} %", color = ForestText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    LinearProgressIndicator(progress = { value.accuracyPercent / 100f }, modifier = Modifier.fillMaxWidth())
                }
                val incorrectQuestions = questions.filter { answers[it.id] != it.correctIndex }
                Text("Co si ještě procvičit", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                if (incorrectQuestions.isEmpty()) {
                    Text("Výborně! V tomto testu jsi neztratil/a žádnou odpověď.", color = ForestText, fontSize = 13.sp)
                } else {
                    incorrectQuestions.forEach { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text("${item.level} · ${item.skill}", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(item.prompt, color = Ink, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold)
                                Text("Správná odpověď: ${item.choices[item.correctIndex]}", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(item.explanation, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
                            }
                        }
                    }
                }
                Button(
                    onClick = { restart() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zopakovat rozřazovací test", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zpět na přehled", fontWeight = FontWeight.Bold) }
            } else if (current != null) {
                Text("TEST ÚROVNĚ A1–C2 · 24 OTÁZEK", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text("Otázka ${index + 1} z ${questions.size}", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                LinearProgressIndicator(progress = { (index + 1).toFloat() / questions.size }, modifier = Modifier.fillMaxWidth())
                Text(current.level + " · " + current.skill, color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = CardSurface)) {
                    Text(current.prompt, modifier = Modifier.padding(20.dp), color = Ink, fontSize = 19.sp, lineHeight = 27.sp, fontWeight = FontWeight.SemiBold)
                }
                current.choices.forEachIndexed { choiceIndex, choice ->
                    val background = when {
                        selected == null -> OptionSurface
                        choiceIndex == current.correctIndex -> Correct
                        choiceIndex == selected -> Incorrect
                        else -> OptionSurface
                    }
                    Card(
                        onClick = {
                            if (selected == null) {
                                selected = choiceIndex
                                answers = answers + (current.id to choiceIndex)
                                val correct = choiceIndex == current.correctIndex
                                LearningReviewStore.recordAnswer(
                                    context = context,
                                    question = ReviewQuestion(
                                        id = current.id,
                                        sourceKind = if (current.skill == "Gramatika") "sentences" else "wordtools",
                                        sourceTitle = "Rozřazovací test ${current.level}",
                                        prompt = current.prompt,
                                        choices = current.choices,
                                        correctIndex = current.correctIndex,
                                        explanation = current.explanation,
                                        germanSentence = current.germanSentence,
                                        translation = current.translation,
                                        cefrLevel = current.level
                                    ),
                                    wasCorrect = correct
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = background)
                    ) {
                        Text(choice, modifier = Modifier.fillMaxWidth().padding(16.dp), color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
                if (selected != null) {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = Mint)) {
                        Text(
                            "Odpověď uložena. Řešení uvidíš na konci.",
                            modifier = Modifier.padding(15.dp),
                            color = ForestText, fontSize = 13.sp, lineHeight = 19.sp
                        )
                    }
                    Button(
                        onClick = {
                            if (index < questions.lastIndex) {
                                index += 1
                                selected = null
                            } else {
                                val result = evaluatePlacementAssessment(questions, answers)
                                PlacementAssessmentStore.save(context, result)
                                report = result
                                onComplete(result)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text(if (index == questions.lastIndex) "Vyhodnotit test" else "Další otázka", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
internal fun PlacementWelcomeScreen(
    onStart: () -> Unit,
    onSkip: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, top = 30.dp, end = 24.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(24.dp))
            androidx.compose.material3.Icon(
                Icons.Default.School,
                contentDescription = null,
                tint = ForestText,
                modifier = Modifier.size(54.dp)
            )
            Text("Vítej v DeutschAI", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text(
                "Chceš si nejdřív ověřit svou úroveň němčiny?",
                color = Ink,
                fontSize = 26.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("24 krátkých otázek", color = ForestText, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    Text("4 otázky na každou úroveň A1–C2 · plně offline", color = Muted, fontSize = 14.sp, lineHeight = 20.sp)
                    Text("Na konci dostaneš doporučenou startovní úroveň.", color = Muted, fontSize = 14.sp, lineHeight = 20.sp)
                }
            }
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Spustit vstupní test", fontWeight = FontWeight.Bold) }
            Button(
                onClick = onSkip,
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Přeskočit a začít se učit", fontWeight = FontWeight.Bold) }
            Text("Test je dobrovolný. Najdeš ho i později v části Pokroky.", color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
        }
    }
}
