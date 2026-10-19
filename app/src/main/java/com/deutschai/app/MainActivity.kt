package com.deutschai.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import java.text.Normalizer

private val Forest = Color(0xFF163D35)
private val Mint = Color(0xFFDDEFE6)
private val Cream = Color(0xFFF7F5EF)
private val Ink = Color(0xFF1E2925)
private val Muted = Color(0xFF66736D)
private val Gold = Color(0xFFE7B96A)
private val Correct = Color(0xFFD9EDE1)
private val Incorrect = Color(0xFFF7E0DC)

private data class LessonQuestion(
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String
)

private data class Lesson(
    val id: String,
    val title: String,
    val subtitle: String,
    val questions: List<LessonQuestion>
)

private val lessons = listOf(
    Lesson(
        id = "first_steps",
        title = "První kroky",
        subtitle = "Pozdravy, slovíčka a jednoduché věty",
        questions = listOf(
            LessonQuestion(
                "Jak se německy řekne „Dobrý den“?",
                listOf("Gute Nacht", "Guten Tag", "Auf Wiedersehen", "Danke"),
                1,
                "„Guten Tag“ znamená „Dobrý den“. Použiješ ho při běžném denním pozdravu."
            ),
            LessonQuestion(
                "Doplň větu: Ich ___ Erik.",
                listOf("bist", "ist", "bin", "sind"),
                2,
                "Sloveso „sein“ (být): ich bin, du bist, er/sie/es ist. Proto říkáme „Ich bin Erik“."
            ),
            LessonQuestion(
                "Co znamená německé „Danke“?",
                listOf("Prosím", "Promiňte", "Na shledanou", "Děkuji"),
                3,
                "„Danke“ znamená „Děkuji“. Odpovědět můžeš například „Bitte“ — „Prosím / Není zač“."
            ),
            LessonQuestion(
                "Vyber správný člen: ___ Haus (dům)",
                listOf("der", "die", "das", "den"),
                2,
                "Správně je „das Haus“. Německá podstatná jména se učíme společně s členem: das Haus, die Tür, der Tisch."
            ),
            LessonQuestion(
                "Co znamená „Wie geht es dir?“",
                listOf("Jak se máš?", "Kde bydlíš?", "Kolik je hodin?", "Jak se jmenuješ?"),
                0,
                "„Wie geht es dir?“ znamená „Jak se máš?“. Neformálně můžeš odpovědět „Gut, danke“ — „Dobře, děkuji“."
            )
        )
    ),
    Lesson(
        id = "sein",
        title = "Sloveso sein",
        subtitle = "Řekni, kdo jsi, jak se máš a odkud jsi",
        questions = listOf(
            LessonQuestion(
                "Doplň: Ich ___ müde. (Jsem unavený.)",
                listOf("bist", "bin", "ist", "seid"),
                1,
                "S osobou „ich“ používáme „bin“: ich bin — já jsem."
            ),
            LessonQuestion(
                "Doplň: Du ___ nett. (Jsi milý.)",
                listOf("bin", "sind", "bist", "ist"),
                2,
                "S osobou „du“ používáme „bist“: du bist — ty jsi."
            ),
            LessonQuestion(
                "Která věta je správně?",
                listOf("Er bin hier.", "Er bist hier.", "Er sind hier.", "Er ist hier."),
                3,
                "Pro „er/sie/es“ používáme „ist“: er ist — on je, sie ist — ona je."
            ),
            LessonQuestion(
                "Jak německy řekneš „My jsme tady“?",
                listOf("Wir sind hier.", "Wir ist hier.", "Wir seid hier.", "Wir bin hier."),
                0,
                "S „wir“ (my) používáme tvar „sind“: wir sind."
            ),
            LessonQuestion(
                "Doplň: Ihr ___ heute pünktlich. (Vy jste dnes přesní.)",
                listOf("sind", "bist", "seid", "bin"),
                2,
                "S „ihr“ (vy, množné číslo) používáme „seid“: ihr seid."
            )
        )
    ),
    Lesson(
        id = "articles",
        title = "Členy der, die, das",
        subtitle = "Procvič si rod běžných německých podstatných jmen",
        questions = listOf(
            LessonQuestion(
                "Který člen patří ke slovu „Tisch“ (stůl)?",
                listOf("die Tisch", "das Tisch", "der Tisch", "den Tisch"),
                2,
                "Správně je „der Tisch“. Uč se vždy celé spojení včetně členu."
            ),
            LessonQuestion(
                "Který člen patří ke slovu „Schule“ (škola)?",
                listOf("der Schule", "die Schule", "das Schule", "den Schule"),
                1,
                "Správně je „die Schule“."
            ),
            LessonQuestion(
                "Který člen patří ke slovu „Auto“ (auto)?",
                listOf("das Auto", "die Auto", "der Auto", "den Auto"),
                0,
                "Správně je „das Auto“."
            ),
            LessonQuestion(
                "Který člen patří ke slovu „Frau“ (žena/paní)?",
                listOf("das Frau", "der Frau", "die Frau", "den Frau"),
                2,
                "Správně je „die Frau“."
            ),
            LessonQuestion(
                "Který člen patří ke slovu „Kind“ (dítě)?",
                listOf("der Kind", "die Kind", "das Kind", "den Kind"),
                2,
                "Správně je „das Kind“. Pozor: gramatický rod se nemusí vždy shodovat s přirozeným rodem."
            )
        )
    )
)

private data class ConversationPrompt(
    val prompt: String,
    val recognizedPhrases: List<String>,
    val example: String,
    val explanation: String
)

private data class ConversationMessage(
    val text: String,
    val fromBeatrice: Boolean,
    val feedback: Boolean = false,
    val recognized: Boolean = false,
    val translation: String? = null
)

private val conversationPrompts = listOf(
    ConversationPrompt(
        prompt = "Hallo! Wie heißt du?",
        recognizedPhrases = listOf("ich heiße", "ich heisse", "mein name ist", "ich bin"),
        example = "Ich heiße …",
        explanation = "Při představování můžeš použít „Ich heiße …“ nebo „Mein Name ist …“."
    ),
    ConversationPrompt(
        prompt = "Woher kommst du?",
        recognizedPhrases = listOf("ich komme aus", "ich wohne in"),
        example = "Ich komme aus Tschechien.",
        explanation = "„Woher kommst du?“ znamená „Odkud pocházíš?“"
    ),
    ConversationPrompt(
        prompt = "Wie geht es dir heute?",
        recognizedPhrases = listOf("mir geht es gut", "mir geht's gut", "gut", "prima", "super", "nicht so gut", "es geht", "müde"),
        example = "Mir geht es gut, danke.",
        explanation = "„Wie geht es dir?“ znamená „Jak se máš?“. „Mir geht es gut“ znamená „Mám se dobře“."
    ),
    ConversationPrompt(
        prompt = "Was machst du gern in deiner Freizeit?",
        recognizedPhrases = listOf("ich spiele", "ich lese", "ich höre", "ich hore", "ich koche", "ich schwimme", "ich gehe", "ich mache", "ich mag", "gern"),
        example = "Ich lese gern.",
        explanation = "Slovo „gern“ používáme, když říkáme, co rádi děláme. Například „Ich lese gern“ — „Rád čtu“."
    ),
    ConversationPrompt(
        prompt = "Was machst du am Wochenende?",
        recognizedPhrases = listOf("am wochenende", "ich gehe", "ich spiele", "ich treffe", "ich besuche", "ich bleibe", "ich fahre"),
        example = "Am Wochenende treffe ich Freunde.",
        explanation = "„Am Wochenende“ znamená „o víkendu“. Můžeš začít právě touto frází."
    )
)

private fun normalizeGerman(text: String): String {
    val expanded = text.lowercase().replace("ß", "ss")
    return Normalizer.normalize(expanded, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private fun evaluateConversationAnswer(
    answer: String,
    prompt: ConversationPrompt
): Pair<Boolean, String> {
    val normalized = normalizeGerman(answer)
    val knownPhraseMatched = prompt.recognizedPhrases.any {
        val phrase = normalizeGerman(it)
        phrase.isNotBlank() && normalized.contains(phrase)
    }

    // Forgiving, context-aware offline check: common word stems and short natural replies.
    val contextualMatch = when (prompt.prompt) {
        "Hallo! Wie heißt du?" ->
            normalized.split(" ").size in 1..4 ||
                listOf("heiss", "heisse", "name", "ich bin").any { normalized.contains(it) }

        "Woher kommst du?" ->
            listOf(
                "komm", "komme", "wohn", "wohne", "aus",
                "tschechien", "deutschland", "osterreich", "schweiz",
                "prag", "praha", "brno", "slowakei", "berlin", "wien"
            ).any { normalized.contains(it) } ||
                normalized.split(" ").size in 1..3

        "Wie geht es dir heute?" ->
            listOf("gut", "prima", "super", "schlecht", "mude", "geht",
                "danke", "okay", "ok", "nicht so", "fine")
                .any { normalized.contains(it) }

        "Was machst du gern in deiner Freizeit?" ->
            listOf(
                "spiel", "les", "hor", "koch", "schwimm", "geh", "mach",
                "mag", "lieb", "sport", "musik", "buch", "rad", "fussball",
                "reise", "freund", "garten", "wander", "kino", "tanz",
                "lauf", "jogg", "zeichn", "lern", "back", "fotograf"
            ).any { normalized.contains(it) }

        "Was machst du am Wochenende?" ->
            listOf(
                "wochenende", "samstag", "sonntag", "geh", "spiel", "treff",
                "besuch", "bleib", "fahr", "arbeit", "entspann", "schlaf",
                "reis", "seh", "mach", "koch", "freund", "familie", "kino",
                "sport", "ausflug"
            ).any { normalized.contains(it) }

        else -> false
    }

    // These offline rules are only hints, not a full grammar checker or generative AI.
    val recognized = knownPhraseMatched || contextualMatch
    return recognized to prompt.explanation
}

private fun promptTranslation(prompt: ConversationPrompt): String = when (prompt.prompt) {
    "Hallo! Wie heißt du?" -> "Ahoj! Jak se jmenuješ?"
    "Woher kommst du?" -> "Odkud pocházíš?"
    "Wie geht es dir heute?" -> "Jak se dnes máš?"
    "Was machst du gern in deiner Freizeit?" -> "Co rád/a děláš ve volném čase?"
    "Was machst du am Wochenende?" -> "Co děláš o víkendu?"
    else -> "Český překlad zatím není k dispozici."
}

private fun exampleTranslation(example: String): String = when (example) {
    "Ich heiße …" -> "Jmenuji se …"
    "Ich komme aus Tschechien." -> "Pocházím z Česka."
    "Mir geht es gut, danke." -> "Mám se dobře, děkuji."
    "Ich lese gern." -> "Rád/a čtu."
    "Am Wochenende treffe ich Freunde." -> "O víkendu se setkávám s přáteli."
    else -> "Český překlad zatím není k dispozici."
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DeutschAIHome()
            }
        }
    }
}

@Composable
private fun DeutschAIHome() {
    val context = LocalContext.current
    val preferences = remember(context) {
        context.getSharedPreferences("deutschai_progress", Context.MODE_PRIVATE)
    }
    var completedSessions by remember(preferences) {
        mutableStateOf(preferences.getInt("completed_sessions", 0))
    }
    var bestScore by remember(preferences) {
        mutableStateOf(preferences.getInt("best_score", 0))
    }

    var showConversation by remember { mutableStateOf(false) }
    var showLessonLibrary by remember { mutableStateOf(false) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    var selectedTab by remember { mutableStateOf("Učení") }

    if (showConversation) {
        ConversationScreen(onBack = { showConversation = false })
        return
    }

    if (selectedLesson != null) {
        LessonScreen(
            lesson = selectedLesson!!,
            onBack = { selectedLesson = null; showLessonLibrary = true },
            onComplete = { score, total ->
                completedSessions += 1
                preferences.edit().putInt("completed_sessions", completedSessions).apply()
                if (score > bestScore) {
                    bestScore = score
                    preferences.edit().putInt("best_score", bestScore).apply()
                }
            }
        )
        return
    }

    if (showLessonLibrary) {
        LessonLibraryScreen(
            onBack = { showLessonLibrary = false },
            onSelectLesson = { lesson ->
                selectedLesson = lesson
                showLessonLibrary = false
            }
        )
        return
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Cream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 24.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DEUTSCHAI",
                        color = Forest,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Hallo! Schön, dass du da bist.",
                        color = Ink,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Mint, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "DeutschAI",
                        tint = Forest,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Forest)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "TVŮJ OSOBNÍ JAZYKOVÝ PRŮVODCE",
                        color = Color(0xFFC7DFD3),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Každý den o kousek blíž k plynulé němčině.",
                        color = Color.White,
                        fontSize = 25.sp,
                        lineHeight = 31.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Uč se vlastním tempem. Bez reklam a bez tlaku.",
                        color = Color(0xFFE0EAE5),
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                    Button(
                        onClick = { showLessonLibrary = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gold,
                            contentColor = Ink
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Vybrat lekci", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = "Tvoje cesta",
                    color = Ink,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Začni jednoduše a postupuj vlastním tempem.",
                    color = Muted,
                    fontSize = 14.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProgressCard(
                    modifier = Modifier.weight(1f),
                    title = "Úroveň",
                    value = "A1",
                    detail = "Začátečník"
                )
                ProgressCard(
                    modifier = Modifier.weight(1f),
                    title = "Dokončeno",
                    value = completedSessions.toString(),
                    detail = "Výukových sezení"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProgressCard(
                    modifier = Modifier.weight(1f),
                    title = "Nejlepší skóre",
                    value = bestScore.toString() + "/5",
                    detail = "Správných odpovědí"
                )
                ProgressCard(
                    modifier = Modifier.weight(1f),
                    title = "Dostupné",
                    value = lessons.size.toString(),
                    detail = "Offline lekce"
                )
            }

            Text(
                text = "Co chceš procvičit?",
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            FeatureCard(
                icon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = Forest) },
                title = "Slovíčka a gramatika",
                subtitle = "Tři první A1 lekce s vysvětlením v češtině",
                onClick = { showLessonLibrary = true }
            )
            FeatureCard(
                icon = { Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Forest) },
                title = "Konverzace s Beatrice",
                subtitle = "Mluv vlastními slovy a uč se z oprav",
                onClick = { showConversation = true }
            )
            FeatureCard(
                icon = { Icon(Icons.Default.School, contentDescription = null, tint = Forest) },
                title = "Moje pokroky",
                subtitle = "Výsledky se ukládají přímo v telefonu",
                onClick = { selectedTab = "Pokroky" }
            )

            if (selectedTab != "Učení") {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Mint),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (selectedTab == "Konverzace") "Konverzace s Beatrice" else "Tvoje pokroky",
                            color = Forest,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (selectedTab == "Konverzace") {
                                "Tuto část připravíme jako další. Kvízy a vysvětlení fungují offline."
                            } else {
                                "Dokončená výuková sezení: " + completedSessions +
                                    "\nNejlepší výsledek: " + bestScore + "/5" +
                                    "\nVýsledky zůstávají uložené v tomto telefonu."
                            },
                            color = Forest,
                            lineHeight = 22.sp,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "DEUTSCHAI  •  UČENÍ PODLE TEBE",
                color = Muted,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun LessonLibraryScreen(
    onBack: () -> Unit,
    onSelectLesson: (Lesson) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = Forest, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                    Text("Knihovna lekcí", color = Muted, fontSize = 13.sp)
                }
            }

            Text(
                text = "Co se dnes naučíme?",
                color = Ink,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )
            Text(
                text = "Vyber si krátkou lekci. Všechny otázky a vysvětlení jsou dostupné bez internetu.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            lessons.forEachIndexed { index, lesson ->
                Card(
                    onClick = { onSelectLesson(lesson) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Mint, RoundedCornerShape(15.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                color = Forest,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(lesson.title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                            Text(lesson.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                            Text("A1  •  " + lesson.questions.size + " otázek", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonScreen(
    lesson: Lesson,
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit
) {
    var questionIndex by remember(lesson.id) { mutableStateOf(0) }
    var selectedAnswer by remember(lesson.id) { mutableStateOf<Int?>(null) }
    var score by remember(lesson.id) { mutableStateOf(0) }
    var finished by remember(lesson.id) { mutableStateOf(false) }

    fun restartLesson() {
        questionIndex = 0
        selectedAnswer = null
        score = 0
        finished = false
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = Forest, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                    Text(lesson.title + " · A1", color = Muted, fontSize = 13.sp)
                }
            }

            if (finished) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.School, contentDescription = null, tint = Gold, modifier = Modifier.size(48.dp))
                        Text("Lekce dokončena!", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text(
                            score.toString() + " z " + lesson.questions.size + " správně",
                            color = Gold,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = when {
                                score == lesson.questions.size -> "Výborně! Všechny odpovědi jsou správně."
                                score >= 3 -> "Dobrá práce. Krátké opakování ti pomůže zapamatovat si zbytek."
                                else -> "Každý začátek se počítá. Zkus si lekci zopakovat."
                            },
                            color = Color(0xFFE0EAE5),
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                }
                Button(
                    onClick = { restartLesson() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Zopakovat lekci", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Vybrat jinou lekci", fontWeight = FontWeight.Bold)
                }
            } else {
                val question = lesson.questions[questionIndex]

                Text(
                    text = "PRVNÍ KROKY V NĚMČINĚ",
                    color = Muted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.3.sp
                )
                Text(
                    text = "Otázka " + (questionIndex + 1) + " z " + lesson.questions.size,
                    color = Forest,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(lesson.questions.size) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .background(
                                    color = if (index <= questionIndex) Forest else Color(0xFFE3E5DF),
                                    shape = CircleShape
                                )
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = question.prompt,
                            color = Ink,
                            fontSize = 21.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        question.choices.forEachIndexed { index, choice ->
                            val choiceColor = when {
                                selectedAnswer == null -> Cream
                                index == question.correctIndex -> Correct
                                selectedAnswer == index -> Incorrect
                                else -> Cream
                            }
                            Card(
                                onClick = {
                                    if (selectedAnswer == null) {
                                        selectedAnswer = index
                                        if (index == question.correctIndex) score += 1
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = choiceColor)
                            ) {
                                Text(
                                    text = choice,
                                    color = Ink,
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedAnswer != null && index == question.correctIndex) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp)
                                )
                            }
                        }
                    }
                }

                if (selectedAnswer != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = if (selectedAnswer == question.correctIndex) Correct else Incorrect)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (selectedAnswer == question.correctIndex) "Správně! Gut gemacht!" else "To nevadí — učíme se.",
                                color = Forest,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(question.explanation, color = Ink, fontSize = 14.sp, lineHeight = 21.sp)
                        }
                    }
                    Button(
                        onClick = {
                            if (questionIndex < lesson.questions.lastIndex) {
                                questionIndex += 1
                                selectedAnswer = null
                            } else {
                                onComplete(score, lesson.questions.size)
                                finished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (questionIndex == lesson.questions.lastIndex) "Zobrazit výsledek" else "Další otázka",
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = "Vyber jednu odpověď. Lekce funguje i bez připojení k internetu.",
                        color = Muted,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ConversationScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var promptIndex by remember { mutableStateOf(0) }
    var recognizedCount by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    var messages by remember {
        mutableStateOf(
            listOf(
                ConversationMessage(
                    "Hallo! Ich bin Beatrice. Wir üben heute ein paar einfache Gespräche auf Deutsch.",
                    true,
                    translation = "Ahoj! Jsem Beatrice. Dnes si procvičíme několik jednoduchých rozhovorů v němčině."
                ),
                ConversationMessage(
                    conversationPrompts[0].prompt,
                    true,
                    translation = promptTranslation(conversationPrompts[0])
                )
            )
        )
    }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    fun sendAnswer() {
        val answer = input.trim()
        if (answer.isEmpty() || finished) return
        val currentPrompt = conversationPrompts[promptIndex]
        val evaluation = evaluateConversationAnswer(answer, currentPrompt)
        val shortFeedback = if (evaluation.first) {
            ConversationMessage(
                "Sehr gut! Danke für deine Antwort.",
                true,
                feedback = true,
                recognized = true,
                translation = "Výborně! Děkuji za odpověď."
            )
        } else {
            ConversationMessage(
                "Danke für deine Antwort!",
                true,
                feedback = true,
                recognized = false,
                translation = "Děkuji za odpověď."
            )
        }

        messages = messages +
            ConversationMessage(answer, false) +
            shortFeedback +
            ConversationMessage(
                "Beispiel: " + currentPrompt.example,
                true,
                translation = "Česky: " + exampleTranslation(currentPrompt.example)
            ) +
            ConversationMessage(evaluation.second, true, feedback = true, recognized = evaluation.first)
        if (evaluation.first) recognizedCount += 1
        input = ""

        if (promptIndex < conversationPrompts.lastIndex) {
            promptIndex += 1
            val nextPrompt = conversationPrompts[promptIndex]
            messages = messages + ConversationMessage(
                nextPrompt.prompt,
                true,
                translation = promptTranslation(nextPrompt)
            )
        } else {
            finished = true
            messages = messages + ConversationMessage(
                "Danke für das Gespräch! Du kannst es jederzeit noch einmal versuchen.",
                true,
                translation = "Děkuji za rozhovor! Můžeš si ho kdykoliv zkusit znovu."
            )
        }
    }

    fun restartConversation() {
        input = ""
        promptIndex = 0
        recognizedCount = 0
        finished = false
        messages = listOf(
            ConversationMessage(
                "Hallo! Ich bin Beatrice. Wir üben heute ein paar einfache Gespräche auf Deutsch.",
                true,
                translation = "Ahoj! Jsem Beatrice. Dnes si procvičíme několik jednoduchých rozhovorů v němčině."
            ),
            ConversationMessage(
                conversationPrompts[0].prompt,
                true,
                translation = promptTranslation(conversationPrompts[0])
            )
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.heightIn(min = 52.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Forest, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("B", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Beatrice", color = Forest, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Němčina A1 · offline konverzace", color = Muted, fontSize = 12.sp)
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Mint)
            ) {
                Text(
                    text = "Odpovídej německy vlastními slovy. Beatrice ti ukáže užitečný vzor a vysvětlí ho česky.",
                    modifier = Modifier.padding(12.dp),
                    color = Forest,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages) { message ->
                    ChatBubble(message)
                }
            }

            if (finished) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Rozhovor dokončen", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Rozpoznané fráze: " + recognizedCount + " z " + conversationPrompts.size,
                            color = Gold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Jde o jednoduchou offline kontrolu vybraných frází, ne o plnou AI ani úplnou kontrolu gramatiky.",
                            color = Color(0xFFE0EAE5),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
                Button(
                    onClick = { restartConversation() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Začít znovu", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Zpět na hlavní obrazovku", fontWeight = FontWeight.Bold)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Napiš odpověď německy…") },
                        maxLines = 3,
                        shape = RoundedCornerShape(16.dp)
                    )
                    Button(
                        onClick = { sendAnswer() },
                        enabled = input.isNotBlank(),
                        modifier = Modifier.heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Odeslat")
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ConversationMessage) {
    var showTranslation by remember(message.text) { mutableStateOf(false) }
    val background = when {
        !message.fromBeatrice -> Forest
        message.feedback -> Mint
        else -> Color.White
    }
    val foreground = if (message.fromBeatrice) Ink else Color.White
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.fromBeatrice) Arrangement.Start else Arrangement.End
    ) {
        Card(
            onClick = {
                if (message.fromBeatrice && message.translation != null) {
                    showTranslation = !showTranslation
                }
            },
            modifier = Modifier.fillMaxWidth(0.92f),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = background)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (message.fromBeatrice) {
                    Text(
                        text = if (message.feedback) "BEATRICE · VYSVĚTLENÍ" else "BEATRICE",
                        color = Forest,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                } else {
                    Text(
                        text = "TY",
                        color = Color(0xFFDDEFE6),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = message.text,
                    color = foreground,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
                if (message.fromBeatrice && message.translation != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (showTranslation) {
                            "Česky: " + message.translation + "  ·  klepnutím skryješ"
                        } else {
                            "Klepnutím zobrazíš český překlad  ↓"
                        },
                        color = Forest,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
@Composable
private fun ProgressCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    detail: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(title, color = Muted, fontSize = 13.sp)
            Text(value, color = Forest, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(detail, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun FeatureCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(Mint, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
            }
        }
    }
}
