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
import androidx.compose.material.icons.filled.VolumeUp
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
import kotlinx.coroutines.delay

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
    val level: String,
    val questions: List<LessonQuestion>
)

private val lessons = sentenceCourseCatalog.map { course ->
    Lesson(
        id = course.id,
        title = course.title,
        subtitle = course.subtitle,
        level = course.level,
        questions = sentenceCourseQuestions(course.id)
    )
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
                BeatriceHome()
            }
        }
    }
}

@Composable
private fun BeatriceHome() {
    val context = LocalContext.current
    val preferences = remember(context) {
        context.getSharedPreferences("deutschai_progress", Context.MODE_PRIVATE)
    }
    var progressStats by remember(preferences) {
        mutableStateOf(LearningProgressStore.load(context))
    }

    var showConversation by remember { mutableStateOf(false) }
    var showWordTools by remember { mutableStateOf(false) }
    var showLearningHub by remember { mutableStateOf(false) }
    var showLessonLibrary by remember { mutableStateOf(false) }
    var showArticleCategories by remember { mutableStateOf(false) }
    var selectedArticleCategory by remember { mutableStateOf<ArticleCategory?>(null) }
    var showVerbCategories by remember { mutableStateOf(false) }
    var selectedVerbTopic by remember { mutableStateOf<VerbPracticeLesson?>(null) }
    var showReadingLibrary by remember { mutableStateOf(false) }
    var selectedReadingArticle by remember { mutableStateOf<ReadingArticle?>(null) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    var selectedPhraseReviewLevel by remember { mutableStateOf<String?>(null) }
    var selectedListeningLevel by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf("Učení") }
    var lastActivityType by remember(preferences) {
        mutableStateOf(preferences.getString("last_activity_type", "") ?: "")
    }
    var lastActivityId by remember(preferences) {
        mutableStateOf(preferences.getString("last_activity_id", "") ?: "")
    }
    var lastActivityTitle by remember(preferences) {
        mutableStateOf(preferences.getString("last_activity_title", "") ?: "")
    }

    fun saveLastActivity(type: String, id: String, title: String) {
        lastActivityType = type
        lastActivityId = id
        lastActivityTitle = title
        preferences.edit()
            .putString("last_activity_type", type)
            .putString("last_activity_id", id)
            .putString("last_activity_title", title)
            .apply()
    }

    fun continueLastActivity() {
        when (lastActivityType) {
            "lesson" -> {
                val item = lessons.firstOrNull { it.id == lastActivityId }
                if (item != null) selectedLesson = item else showLearningHub = true
            }
            "listening" -> selectedListeningLevel = lastActivityId
            "phrase-review" -> selectedPhraseReviewLevel = lastActivityId
            "reading" -> {
                val item = readingArticles.firstOrNull { it.id == lastActivityId }
                if (item != null) selectedReadingArticle = item else showLearningHub = true
            }
            "dialogue" -> showConversation = true
            "word-tools" -> showWordTools = true
            "article-quiz" -> {
                val item = articleCategories.firstOrNull { it.id == lastActivityId }
                if (item != null) selectedArticleCategory = item else showLearningHub = true
            }
            "verb-quiz" -> {
                val item = (verbPracticeTopics + advancedVerbPracticeTopics)
                    .firstOrNull { it.id == lastActivityId }
                if (item != null) selectedVerbTopic = item else showLearningHub = true
            }
            else -> showLearningHub = true
        }
    }

    fun recordScore(score: Int, total: Int, activityKey: String) {
        if (total > 0 && score == total) {
            progressStats = LearningProgressStore.recordPerfectSession(context, activityKey)
        }
    }

    if (showWordTools) {
        WordToolsScreen(
            onBack = { showWordTools = false; showLearningHub = true },
            onComplete = { score, total, activityKey ->
                recordScore(score, total, activityKey)
            }
        )
        return
    }

    if (showConversation) {
        GuidedDialogueScreen(
            onBack = { showConversation = false },
            // Dialogues are conversation practice, not scored quizzes; they do not
            // count toward the metric for sessions with 100% correct answers.
            onComplete = {}
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
                saveLastActivity("reading", article.id, article.title)
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
            onComplete = { score, total ->
                recordScore(score, total, "articles_${selectedArticleCategory!!.id}")
            }
        )
        return
    }

    if (selectedVerbTopic != null) {
        VerbQuizScreen(
            topic = selectedVerbTopic!!,
            onBack = { selectedVerbTopic = null; showVerbCategories = true },
            onComplete = { score, total ->
                recordScore(score, total, "verbs_${selectedVerbTopic!!.id}")
            }
        )
        return
    }

    if (showArticleCategories) {
        ArticleCategoryLibraryScreen(
            onBack = { showArticleCategories = false; showLearningHub = true },
            onSelectCategory = { category ->
                saveLastActivity("article-quiz", category.id, category.title)
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
                saveLastActivity("verb-quiz", topic.id, topic.title)
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
            onDialogues = {
                saveLastActivity("dialogue", "dialogue", "Řízené dialogy s Beatrice")
                showLearningHub = false
                showConversation = true
            },
            onReading = { showLearningHub = false; showReadingLibrary = true },
            onWordTools = {
                saveLastActivity("word-tools", "word-tools", "Slovní nástroje")
                showLearningHub = false
                showWordTools = true
            }
        )
        return
    }

    if (selectedPhraseReviewLevel != null) {
        PhraseReviewScreen(
            level = selectedPhraseReviewLevel!!,
            onBack = { selectedPhraseReviewLevel = null; showLessonLibrary = true }
        )
        return
    }

    if (selectedListeningLevel != null) {
        ListeningPracticeScreen(
            level = selectedListeningLevel!!,
            onBack = { selectedListeningLevel = null; showLessonLibrary = true }
        )
        return
    }

    if (selectedLesson != null) {
        LessonScreen(
            lesson = selectedLesson!!,
            onBack = { selectedLesson = null; showLessonLibrary = true },
            onComplete = { score, total ->
                recordScore(score, total, "sentence_${selectedLesson!!.id}")
            }
        )
        return
    }

    if (showLessonLibrary) {
        LessonLibraryScreen(
            onBack = { showLessonLibrary = false; showLearningHub = true },
            onSelectLesson = { lesson ->
                saveLastActivity("lesson", lesson.id, lesson.title)
                selectedLesson = lesson
                showLessonLibrary = false
            },
            onSelectPhraseReview = { level ->
                saveLastActivity("phrase-review", level, "Opakování frází $level")
                selectedPhraseReviewLevel = level
                showLessonLibrary = false
            },
            onSelectListening = { level ->
                saveLastActivity("listening", level, "Poslech a porozumění $level")
                selectedListeningLevel = level
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
                        text = "BEATRICE",
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
                        contentDescription = "Beatrice",
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { showLearningHub = true },
                            modifier = Modifier.weight(1f).heightIn(min = 50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Gold,
                                contentColor = Ink
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Vybrat lekci", fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                        Button(
                            onClick = { continueLastActivity() },
                            enabled = lastActivityType.isNotBlank(),
                            modifier = Modifier.weight(1f).heightIn(min = 50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Forest,
                                disabledContainerColor = Color(0xFF31584F),
                                disabledContentColor = Color(0xFFB9CDC4)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Pokračovat", fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }
                    Text(
                        text = if (lastActivityTitle.isNotBlank()) {
                            "Naposledy: $lastActivityTitle"
                        } else {
                            "Poslední aktivita se uloží automaticky."
                        },
                        color = Color(0xFFC7DFD3),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        maxLines = 2
                    )
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
                    value = "A1–B2",
                    detail = "Dostupné úrovně"
                )
                ProgressCard(
                    modifier = Modifier.weight(1f),
                    title = "Úspěšně dokončeno",
                    value = progressStats.successfullyCompletedActivities.toString(),
                    detail = "Sady se 100 %"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProgressCard(
                    modifier = Modifier.weight(1f),
                    title = "Úspěšná sezení",
                    value = progressStats.successfulSessions.toString(),
                    detail = "100 % správně"
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
                onClick = {
                    saveLastActivity("dialogue", "dialogue", "Řízené dialogy s Beatrice")
                    showConversation = true
                }
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
                                "Úspěšně dokončeno: " + progressStats.successfullyCompletedActivities +
                                    "\nÚspěšně dokončená výuková sezení: " + progressStats.successfulSessions +
                                    "\nPočítají se jen výsledky se 100 % správných odpovědí." +
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
    onSelectLesson: (Lesson) -> Unit,
    onSelectPhraseReview: (String) -> Unit,
    onSelectListening: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedLevel by remember { mutableStateOf<String?>(null) }
    val levelDetails = listOf(
        Triple("A1", "Začátečník", "Základní fráze, pozdravy a běžné situace."),
        Triple("A2", "Mírně pokročilý", "Praktická domluva, plány a každodenní problémy."),
        Triple("B1", "Středně pokročilý", "Práce, argumentace, samostatná komunikace a složitější situace."),
        Triple("B2", "Vyšší středně pokročilý", "Přesné vyjadřování, profesní komunikace a náročnější témata.")
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding().verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { if (selectedLevel != null) selectedLevel = null else onBack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = Forest, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                    Text(if (selectedLevel == null) "Kurzy podle úrovně" else "Kurzová knihovna · ${selectedLevel}", color = Muted, fontSize = 13.sp)
                }
            }

            if (selectedLevel == null) {
                Text("Vyber svou úroveň", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
                Text(
                    "Každá úroveň obsahuje tematické lekce po 25 otázkách a vlastní podkategorii chytrého opakování frází. Pokrok se ukládá přímo v telefonu.",
                    color = Muted, fontSize = 14.sp, lineHeight = 21.sp
                )
                levelDetails.forEach { (level, name, description) ->
                    val levelLessons = lessons.filter { it.level == level }
                    Card(
                        onClick = { selectedLevel = level },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(52.dp).background(Mint, RoundedCornerShape(15.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(level, color = Forest, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text("$level · $name", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                Text(description, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                                Text("${levelLessons.size} tematických kurzů · 25 otázek v každém", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                val level = selectedLevel!!
                val levelLessons = lessons.filter { it.level == level }
                Text("Úroveň $level", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
                Text(
                    "Vyber si konkrétní téma, nebo si procvič fráze, které aplikace vybere podle toho, kdy je potřebuješ zopakovat.",
                    color = Muted, fontSize = 14.sp, lineHeight = 21.sp
                )
                Card(
                    onClick = { onSelectListening(level) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp).background(Mint, RoundedCornerShape(15.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Forest, modifier = Modifier.size(25.dp))
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text("Poslech a porozumění", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "Poslechni si větu v němčině (de-DE) a vyber její český význam. Text se zobrazí až po odpovědi.",
                                color = Muted, fontSize = 13.sp, lineHeight = 19.sp
                            )
                            Text("25 poslechových otázek", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            LatestResultLine(CourseResultStore.load(context, "listening_$level"))
                        }
                    }
                }
                Card(
                    onClick = { onSelectPhraseReview(level) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp).background(Color(0xFF2B5B4F), RoundedCornerShape(15.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Gold, modifier = Modifier.size(25.dp))
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text("Chytré opakování frází", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "Zkus si frázi vybavit před odhalením odpovědi. Opakování se načasuje podle toho, jak dobře ji znáš.",
                                color = Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 19.sp
                            )
                            Text("${levelLessons.sumOf { it.questions.size }} frází z úrovně $level", color = Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                levelLessons.forEachIndexed { index, lesson ->
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
                                modifier = Modifier.size(48.dp).background(Mint, RoundedCornerShape(15.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text((index + 1).toString(), color = Forest, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text(lesson.title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                Text(lesson.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                                Text("${lesson.level}  •  ${lesson.questions.size} otázek", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                LatestResultLine(CourseResultStore.load(context, "sentence_${lesson.id}"))
                            }
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
    val context = LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    var questions by remember(lesson.id) { mutableStateOf(lesson.questions.shuffled()) }
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
                    Text(lesson.title + " · " + lesson.level, color = Muted, fontSize = 13.sp)
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
                                score >= (questions.size * 0.6).toInt() -> "Dobrá práce. Krátké opakování ti pomůže zapamatovat si zbytek."
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
                    text = "PRAKTICKÉ VĚTY " + lesson.level,
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
                                        if (index == question.correctIndex) {
                                            score += 1
                                        } else {
                                            val sourceIndex = lesson.questions.indexOfFirst {
                                                it.germanSentence == question.germanSentence &&
                                                    it.translation == question.translation
                                            }
                                            if (sourceIndex >= 0) {
                                                markPhraseNeedsReview(context, lesson.level, lesson.id, sourceIndex)
                                            }
                                        }
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
                            if (question.germanSentence.isNotBlank()) {
                                Text("Celá věta:", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(question.germanSentence, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, lineHeight = 23.sp)
                                GermanAudioControls(player = speaker, text = question.germanSentence)
                            }
                            if (question.translation.isNotBlank()) {
                                Text("Česky:", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(question.translation, color = Ink, fontSize = 15.sp, lineHeight = 22.sp)
                            }
                        }
                    }
                    Button(
                        onClick = {
                            if (questionIndex < questions.lastIndex) {
                                questionIndex += 1
                                selectedAnswer = null
                            } else {
                                CourseResultStore.save(context, "sentence_${lesson.id}", score, questions.size)
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
    var isTransitioning by remember { mutableStateOf(false) }

    fun startTopic(topic: GuidedDialogueTopic) {
        isTransitioning = false
        selectedTopic = topic
        turnIndex = 0
        completed = false
        completionRecorded = false
        lines = emptyList()
    }

    fun resetToTopics() {
        isTransitioning = false
        selectedTopic = null
        turnIndex = 0
        completed = false
        completionRecorded = false
        lines = emptyList()
    }

    fun chooseOption(option: GuidedDialogueOption) {
        val topic = selectedTopic ?: return
        if (completed || isTransitioning) return

        lines = lines +
            GuidedDialogueLine(option.german, option.czech, learner = true) +
            GuidedDialogueLine(option.replyGerman, option.replyCzech, learner = false)
        isTransitioning = true
    }

    LaunchedEffect(isTransitioning) {
        if (isTransitioning) {
            delay(900L)
            val topic = selectedTopic
            if (topic != null && !completed) {
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
            isTransitioning = false
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
                    if (isTransitioning) {
                        Text(
                            "Beatrice právě reaguje… přečti si odpověď a její překlad. Další otázka se zobrazí za chvilku.",
                            color = Forest,
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    } else {
                        Text("Vyber odpověď a přečti ji nahlas", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    if (!isTransitioning) {
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
