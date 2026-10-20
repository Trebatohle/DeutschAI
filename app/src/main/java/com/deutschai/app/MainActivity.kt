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
import androidx.compose.material3.Icon
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

internal val Forest = Color(0xFF163D35)
internal val Mint = Color(0xFFDDEFE6)
internal val Cream = Color(0xFFF7F5EF)
internal val Ink = Color(0xFF1E2925)
internal val Muted = Color(0xFF66736D)
internal val Gold = Color(0xFFE7B96A)
internal val Correct = Color(0xFFD9EDE1)
internal val Incorrect = Color(0xFFF7E0DC)

internal data class LessonQuestion(
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val germanSentence: String = "",
    val translation: String = ""
)

private data class Lesson(
    val id: String,
    val title: String,
    val subtitle: String,
    val questions: List<LessonQuestion>
)

private val baseLessons = listOf(
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

private val lessons = baseLessons.map { lesson ->
    lesson.copy(questions = lesson.questions + expandedCourseQuestions(lesson.id))
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // One-time cleanup from the previous experimental free-form AI model.
        // Guided lessons are bundled with the app and do not need this 329 MB file.
        val migration = getSharedPreferences("deutschai_migration", Context.MODE_PRIVATE)
        if (!migration.getBoolean("removed_experimental_ai_model", false)) {
            runCatching { java.io.File(filesDir, "offline_ai").deleteRecursively() }
            runCatching { java.io.File(cacheDir, "litertlm").deleteRecursively() }
            migration.edit().putBoolean("removed_experimental_ai_model", true).apply()
        }

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
    var showLearningHub by remember { mutableStateOf(false) }
    var showLessonLibrary by remember { mutableStateOf(false) }
    var showArticleCategories by remember { mutableStateOf(false) }
    var selectedArticleCategory by remember { mutableStateOf<ArticleCategory?>(null) }
    var showVerbCategories by remember { mutableStateOf(false) }
    var selectedVerbTopic by remember { mutableStateOf<VerbPracticeLesson?>(null) }
    var showReadingLibrary by remember { mutableStateOf(false) }
    var selectedReadingArticle by remember { mutableStateOf<ReadingArticle?>(null) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    var selectedTab by remember { mutableStateOf("Učení") }

    fun recordSession() {
        completedSessions += 1
        preferences.edit().putInt("completed_sessions", completedSessions).apply()
    }

    fun recordScore(score: Int) {
        recordSession()
        if (score > bestScore) {
            bestScore = score
            preferences.edit().putInt("best_score", bestScore).apply()
        }
    }

    if (showConversation) {
        GuidedDialogueScreen(
            onBack = { showConversation = false },
            onComplete = { recordSession() }
        )
        return
    }

    if (selectedReadingArticle != null) {
        ReadingArticleScreen(
            article = selectedReadingArticle!!,
            onBack = { selectedReadingArticle = null; showReadingLibrary = true }
        )
        return
    }

    if (showReadingLibrary) {
        ReadingLibraryScreen(
            onBack = { showReadingLibrary = false; showLearningHub = true },
            onSelectArticle = { article ->
                selectedReadingArticle = article
                showReadingLibrary = false
            }
        )
        return
    }

    if (selectedArticleCategory != null) {
        ArticleQuizScreen(
            category = selectedArticleCategory!!,
            onBack = { selectedArticleCategory = null; showArticleCategories = true },
            onComplete = { score -> recordScore(score) }
        )
        return
    }

    if (selectedVerbTopic != null) {
        VerbQuizScreen(
            topic = selectedVerbTopic!!,
            onBack = { selectedVerbTopic = null; showVerbCategories = true },
            onComplete = { score -> recordScore(score) }
        )
        return
    }

    if (showArticleCategories) {
        ArticleCategoryLibraryScreen(
            onBack = { showArticleCategories = false; showLearningHub = true },
            onSelectCategory = { category ->
                selectedArticleCategory = category
                showArticleCategories = false
            }
        )
        return
    }

    if (showVerbCategories) {
        VerbCategoryLibraryScreen(
            onBack = { showVerbCategories = false; showLearningHub = true },
            onSelectTopic = { topic ->
                selectedVerbTopic = topic
                showVerbCategories = false
            }
        )
        return
    }

    if (showLearningHub) {
        LearningHubScreen(
            onBack = { showLearningHub = false },
            onCourses = { showLearningHub = false; showLessonLibrary = true },
            onArticles = { showLearningHub = false; showArticleCategories = true },
            onVerbs = { showLearningHub = false; showVerbCategories = true },
            onDialogues = { showLearningHub = false; showConversation = true },
            onReading = { showLearningHub = false; showReadingLibrary = true }
        )
        return
    }

    if (selectedLesson != null) {
        LessonScreen(
            lesson = selectedLesson!!,
            onBack = { selectedLesson = null; showLessonLibrary = true },
            onComplete = { score, _ -> recordScore(score) }
        )
        return
    }

    if (showLessonLibrary) {
        LessonLibraryScreen(
            onBack = { showLessonLibrary = false; showLearningHub = true },
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
                        text = "Nauč se němčinu větu po větě.",
                        color = Color.White,
                        fontSize = 25.sp,
                        lineHeight = 31.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Jasné situace, přirozené věty a český překlad. Bez reklam a bez tlaku.",
                        color = Color(0xFFE0EAE5),
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                    Button(
                        onClick = { showLearningHub = true },
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
                    value = bestScore.toString() + "/25",
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
                title = "Knihovna učení",
                subtitle = "Kurzy, členy, slovíčka a důležitá slovesa",
                onClick = { showLearningHub = true }
            )
            FeatureCard(
                icon = { Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Forest) },
                title = "Řízené dialogy s Beatrice",
                subtitle = "Tři doporučené odpovědi, překlad a navazující rozhovor",
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
                text = "Vyber si kurz o 25 otázkách. Otázky i česká vysvětlení jsou dostupné bez internetu.",
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
    var questions by remember(lesson.id) { mutableStateOf(questions.shuffled()) }
    var questionIndex by remember(lesson.id) { mutableStateOf(0) }
    var selectedAnswer by remember(lesson.id) { mutableStateOf<Int?>(null) }
    var score by remember(lesson.id) { mutableStateOf(0) }
    var finished by remember(lesson.id) { mutableStateOf(false) }

    fun restartLesson() {
        questions = lesson.questions.shuffled()
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
                            score.toString() + " z " + questions.size + " správně",
                            color = Gold,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = when {
                                score == questions.size -> "Výborně! Všechny odpovědi jsou správně."
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
                val question = questions[questionIndex]

                Text(
                    text = "PRVNÍ KROKY V NĚMČINĚ",
                    color = Muted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.3.sp
                )
                Text(
                    text = "Otázka " + (questionIndex + 1) + " z " + questions.size,
                    color = Forest,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(questions.size) { index ->
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
                            if (questionIndex < questions.lastIndex) {
                                questionIndex += 1
                                selectedAnswer = null
                            } else {
                                onComplete(score, questions.size)
                                finished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (questionIndex == questions.lastIndex) "Zobrazit výsledek" else "Další otázka",
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

private data class GuidedDialogueLine(
    val german: String,
    val czech: String,
    val learner: Boolean
)

@Composable
private fun GuidedDialogueScreen(
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    var selectedTopic by remember { mutableStateOf<GuidedDialogueTopic?>(null) }
    var turnIndex by remember { mutableStateOf(0) }
    var completed by remember { mutableStateOf(false) }
    var completionRecorded by remember { mutableStateOf(false) }
    var lines by remember { mutableStateOf(emptyList<GuidedDialogueLine>()) }

    fun startTopic(topic: GuidedDialogueTopic) {
        selectedTopic = topic
        turnIndex = 0
        completed = false
        completionRecorded = false
        lines = emptyList()
    }

    fun resetToTopics() {
        selectedTopic = null
        turnIndex = 0
        completed = false
        completionRecorded = false
        lines = emptyList()
    }

    fun chooseOption(option: GuidedDialogueOption) {
        val topic = selectedTopic ?: return
        if (completed) return

        lines = lines +
            GuidedDialogueLine(option.german, option.czech, learner = true) +
            GuidedDialogueLine(option.replyGerman, option.replyCzech, learner = false)

        if (turnIndex >= topic.turns.lastIndex) {
            completed = true
            if (!completionRecorded) {
                completionRecorded = true
                onComplete()
            }
        } else {
            turnIndex += 1
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 18.dp, top = 14.dp, end = 18.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                    modifier = Modifier.size(46.dp).background(Forest, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("B", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Beatrice", color = Forest, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Řízené dialogy · A1 · offline", color = Muted, fontSize = 12.sp)
                }
            }

            if (selectedTopic == null) {
                Text("Vyber si situaci", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                Text(
                    "V každém kroku uvidíš tři přirozené odpovědi. Německá věta i český překlad jsou vidět současně. Vybranou větu si přečti nahlas.",
                    color = Muted,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
                guidedDialogueTopics.forEach { topic ->
                    Card(
                        onClick = { startTopic(topic) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(46.dp).background(Mint, RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Forest)
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(topic.title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                Text(topic.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                                Text(
                                    topic.turns.size.toString() + " kroků · 3 odpovědi v každém kroku",
                                    color = Forest,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Mint)
                ) {
                    Text(
                        "Tohle je začátek knihovny dialogů. Obsah je připravený předem, proto funguje bez modelu, bez internetu a bez náhodných odpovědí. Další témata budeme přidávat postupně.",
                        modifier = Modifier.padding(14.dp),
                        color = Forest,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            } else {
                val topic = selectedTopic!!
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(topic.title, color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(
                            if (completed) "Dialog dokončen" else "Krok " + (turnIndex + 1) + " z " + topic.turns.size,
                            color = Muted,
                            fontSize = 13.sp
                        )
                    }
                    Button(
                        onClick = { resetToTopics() },
                        colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.heightIn(min = 48.dp)
                    ) {
                        Text("Témata")
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    repeat(topic.turns.size) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .background(
                                    if (index < turnIndex || completed) Forest else Color(0xFFE1E5DF),
                                    CircleShape
                                )
                        )
                    }
                }

                if (lines.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Forest)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("BEATRICE", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text("Hallo! Schön, dass du da bist.", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Text("Ahoj! Jsem ráda, že jsi tady.", color = Color(0xFFE0EAE5), fontSize = 14.sp)
                        }
                    }
                }

                lines.forEach { line ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = if (line.learner) Forest else Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                if (line.learner) "TY · TVOJE ODPOVĚĎ" else "BEATRICE",
                                color = if (line.learner) Gold else Forest,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                line.german,
                                color = if (line.learner) Color.White else Ink,
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                line.czech,
                                color = if (line.learner) Color(0xFFE0EAE5) else Muted,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                if (completed) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Forest)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            Text("Gut gemacht!", color = Gold, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text("Výborně! Dokončil/a jsi celý dialog.", color = Color.White, fontSize = 15.sp)
                            Text("Můžeš ho zopakovat a tentokrát zvolit jiné odpovědi.", color = Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 19.sp)
                        }
                    }
                    Button(
                        onClick = { startTopic(topic) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Zopakovat dialog", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { resetToTopics() },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Vybrat jiné téma", fontWeight = FontWeight.Bold)
                    }
                } else {
                    val turn = topic.turns[turnIndex]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Mint)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            Text("BEATRICE SE PTÁ", color = Forest, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text(turn.promptGerman, color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold, lineHeight = 25.sp)
                            Text(turn.promptCzech, color = Forest, fontSize = 15.sp, lineHeight = 21.sp)
                        }
                    }
                    Text("Vyber odpověď a přečti ji nahlas", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    turn.options.forEachIndexed { index, option ->
                        Card(
                            onClick = { chooseOption(option) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier.size(30.dp).background(Mint, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text((index + 1).toString(), color = Forest, fontWeight = FontWeight.Bold)
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(option.german, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)
                                    Text(option.czech, color = Muted, fontSize = 14.sp, lineHeight = 19.sp)
                                    Text("Použít tuto odpověď →", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
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
