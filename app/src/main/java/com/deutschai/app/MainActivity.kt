package com.deutschai.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val darkModeState = mutableStateOf(true)
private val mintColorState = mutableStateOf(Color(0xFF243A32))
private val creamColorState = mutableStateOf(Color(0xFF101714))
private val inkColorState = mutableStateOf(Color(0xFFE9F0EC))
private val mutedColorState = mutableStateOf(Color(0xFF9BAAA3))
private val correctColorState = mutableStateOf(Color(0xFF214236))
private val incorrectColorState = mutableStateOf(Color(0xFF482E2B))

internal val IsDarkTheme: Boolean get() = darkModeState.value
internal val Forest = Color(0xFF163D35)
internal val Mint: Color get() = mintColorState.value
internal val Cream: Color get() = creamColorState.value
internal val Ink: Color get() = inkColorState.value
internal val Muted: Color get() = mutedColorState.value
internal val ForestText: Color get() = if (IsDarkTheme) Color(0xFF8CD7B0) else Forest
internal val CardSurface: Color get() = if (IsDarkTheme) Color(0xFF1A2420) else Color.White
internal val OptionSurface: Color get() = if (IsDarkTheme) Color(0xFF26362E) else Color.White
internal val ActionColor: Color get() = if (IsDarkTheme) Color(0xFF8CD7B0) else Forest
internal val ActionText: Color get() = if (IsDarkTheme) Color(0xFF10241B) else Color.White
internal val GoldText = Color(0xFF1B2B23)
internal val RatingHard: Color get() = if (IsDarkTheme) Color(0xFF3D3B32) else Color(0xFFF5E9D4)
internal val RatingGood: Color get() = if (IsDarkTheme) Color(0xFF355643) else Mint
internal val RatingEasy: Color get() = if (IsDarkTheme) Color(0xFF8CD7B0) else Color(0xFFD9EDE1)
internal val Gold = Color(0xFFE7B96A)
internal val Correct: Color get() = correctColorState.value
internal val Incorrect: Color get() = incorrectColorState.value

internal fun applyBeatriceTheme(dark: Boolean) {
    darkModeState.value = dark
    mintColorState.value = if (dark) Color(0xFF243A32) else Color(0xFFDDEFE6)
    creamColorState.value = if (dark) Color(0xFF101714) else Color(0xFFF7F5EF)
    inkColorState.value = if (dark) Color(0xFFE9F0EC) else Color(0xFF1E2925)
    mutedColorState.value = if (dark) Color(0xFF9BAAA3) else Color(0xFF66736D)
    correctColorState.value = if (dark) Color(0xFF214236) else Color(0xFFD9EDE1)
    incorrectColorState.value = if (dark) Color(0xFF482E2B) else Color(0xFFF7E0DC)
}

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

        val appearance = getSharedPreferences("beatrice_appearance", Context.MODE_PRIVATE)
        applyBeatriceTheme(appearance.getBoolean("dark_mode", false))

        setContent {
            val appColors = if (IsDarkTheme) {
                darkColorScheme(
                    primary = ForestText,
                    onPrimary = Forest,
                    secondary = Mint,
                    onSecondary = Ink,
                    background = Cream,
                    onBackground = Ink,
                    surface = CardSurface,
                    onSurface = Ink,
                    tertiary = Gold
                )
            } else {
                lightColorScheme(
                    primary = Forest,
                    onPrimary = Color.White,
                    secondary = Mint,
                    onSecondary = Forest,
                    background = Cream,
                    onBackground = Ink,
                    surface = CardSurface,
                    onSurface = Ink,
                    tertiary = Gold
                )
            }
            MaterialTheme(colorScheme = appColors) {
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
    var habitStats by remember(preferences) { mutableStateOf(StudyHabitStore.load(context)) }
    var pendingReviewCount by remember(preferences) { mutableStateOf(LearningReviewStore.pendingCount(context)) }

    var showConversation by remember { mutableStateOf(false) }
    var showDailyTraining by remember { mutableStateOf(false) }
    var showWordTools by remember { mutableStateOf(false) }
    var showLearningHub by remember { mutableStateOf(false) }
    var showLessonLibrary by remember { mutableStateOf(false) }
    var lessonLibraryOpenedFromHub by remember { mutableStateOf(false) }
    var readingLibraryOpenedFromHub by remember { mutableStateOf(false) }
    var dialogueOpenedFromHub by remember { mutableStateOf(false) }
    var showArticleCategories by remember { mutableStateOf(false) }
    var selectedArticleCategory by remember { mutableStateOf<ArticleCategory?>(null) }
    var showVerbCategories by remember { mutableStateOf(false) }
    var selectedVerbTopic by remember { mutableStateOf<VerbPracticeLesson?>(null) }
    var showReadingLibrary by remember { mutableStateOf(false) }
    var selectedReadingArticle by remember { mutableStateOf<ReadingArticle?>(null) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    var selectedPhraseReviewLevel by remember { mutableStateOf<String?>(null) }
    var selectedListeningLevel by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf("Domů") }
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
            "daily-training" -> showDailyTraining = true
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
        if (total <= 0) return
        habitStats = StudyHabitStore.recordSession(context)
        pendingReviewCount = LearningReviewStore.pendingCount(context)
        progressStats = if (score == total) {
            LearningProgressStore.recordPerfectSession(context, activityKey)
        } else {
            LearningProgressStore.load(context)
        }
    }

    if (showDailyTraining) {
        DailyTrainingScreen(
            onBack = { showDailyTraining = false },
            onComplete = { score, total ->
                CourseResultStore.save(context, "daily_training", score, total)
                recordScore(score, total, "daily_training")
            }
        )
        return
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
            onBack = { showConversation = false; showLearningHub = dialogueOpenedFromHub },
            // A completed dialogue counts as a study day, but not as a scored quiz session.
            onComplete = { habitStats = StudyHabitStore.recordSession(context) }
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
            onBack = { showReadingLibrary = false; showLearningHub = readingLibraryOpenedFromHub },
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
            onCourses = { lessonLibraryOpenedFromHub = true; showLearningHub = false; showLessonLibrary = true },
            onArticles = { showLearningHub = false; showArticleCategories = true },
            onVerbs = { showLearningHub = false; showVerbCategories = true },
            onDialogues = {
                saveLastActivity("dialogue", "dialogue", "Řízené dialogy s Beatrice")
                dialogueOpenedFromHub = true
                showLearningHub = false
                showConversation = true
            },
            onReading = { readingLibraryOpenedFromHub = true; showLearningHub = false; showReadingLibrary = true },
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
            onBack = { showLessonLibrary = false; showLearningHub = lessonLibraryOpenedFromHub },
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

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (selectedTab == "Pokroky") Modifier
                        else Modifier.verticalScroll(rememberScrollState())
                    )
                    .padding(start = 18.dp, top = 12.dp, end = 18.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(if (selectedTab == "Domů") 10.dp else 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("BEATRICE", color = ForestText, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        Text(
                            when (selectedTab) {
                                "Pokroky" -> "Tvůj pokrok, krok za krokem."
                                "Knihovna" -> "Vyber si, co dnes procvičíš."
                                else -> "Němčina vlastním tempem."
                            },
                            color = Muted, fontSize = 13.sp
                        )
                    }
                    IconButton(
                        onClick = {
                            val nextTheme = !IsDarkTheme
                            context.getSharedPreferences("beatrice_appearance", Context.MODE_PRIVATE)
                                .edit().putBoolean("dark_mode", nextTheme).apply()
                            applyBeatriceTheme(nextTheme)
                        },
                        modifier = Modifier.size(46.dp).background(Mint, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (IsDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (IsDarkTheme) "Přepnout na světlý motiv" else "Přepnout na tmavý motiv",
                            tint = ForestText
                        )
                    }
                }

                when (selectedTab) {
                    "Domů" -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (IsDarkTheme) Color(0xFF192A23) else Forest
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(15.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Gold, modifier = Modifier.size(15.dp))
                                    Text(
                                        "TVŮJ OSOBNÍ JAZYKOVÝ PRŮVODCE",
                                        color = Color(0xFFC7DFD3),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                }
                                Text(
                                    if (lastActivityTitle.isNotBlank()) "Pokračuj tam, kde jsi skončil." else "Začni svou další lekci.",
                                    color = Color.White,
                                    fontSize = 21.sp,
                                    lineHeight = 25.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    if (lastActivityTitle.isNotBlank()) "Naposledy: $lastActivityTitle" else "Pár minut denně. Vlastní tempo.",
                                    color = Color(0xFFE0EAE5),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    maxLines = 2
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            if (lastActivityType.isNotBlank()) continueLastActivity()
                                            else showLearningHub = true
                                        },
                                        modifier = Modifier.weight(1f).heightIn(min = 44.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = GoldText),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                                    ) {
                                        Text("Pokračovat", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false, fontSize = 13.sp)
                                    }
                                    Button(
                                        onClick = { showLearningHub = true },
                                        modifier = Modifier.weight(1f).heightIn(min = 44.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = OptionSurface,
                                            contentColor = Ink
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                                    ) {
                                        Text("Vybrat lekci", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false, fontSize = 13.sp)
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            ProgressCard(
                                modifier = Modifier.weight(1f),
                                title = "Dokončeno",
                                value = progressStats.successfullyCompletedActivities.toString(),
                                detail = "aktivit"
                            )
                            ProgressCard(
                                modifier = Modifier.weight(1f),
                                title = "Série dní",
                                value = habitStats.currentStreakDays.toString(),
                                detail = "v řadě"
                            )
                            ProgressCard(
                                modifier = Modifier.weight(1f),
                                title = "Týdenní cíl",
                                value = "${habitStats.studyDaysInLastWeek}/5",
                                detail = if (habitStats.studyDaysInLastWeek >= 5) "Cíl splněn ✓" else "z 7 dní"
                            )
                        }

                        Card(
                            onClick = {
                                saveLastActivity("daily-training", "daily-training", "Osobní denní trénink")
                                showDailyTraining = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(19.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (IsDarkTheme) ActionColor else Mint
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 13.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(11.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(42.dp).background(
                                        if (IsDarkTheme) Color(0xFF244334) else Forest,
                                        RoundedCornerShape(13.dp)
                                    ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(21.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text(
                                        "Denní trénink",
                                        color = if (IsDarkTheme) ActionText else ForestText,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "10 otázek · chytré opakování · offline",
                                        color = if (IsDarkTheme) Color(0xFF244334) else Ink,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp,
                                        maxLines = 2
                                    )
                                    Text(
                                        "$pendingReviewCount chyb připravených k procvičení",
                                        color = if (IsDarkTheme) Color(0xFF315142) else Muted,
                                        fontSize = 10.sp,
                                        maxLines = 1
                                    )
                                }
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = null,
                                    tint = if (IsDarkTheme) ActionText else ForestText,
                                    modifier = Modifier.size(18.dp).rotate(180f)
                                )
                            }
                        }

                        Text("Procvičuj", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        FeatureCard(
                            icon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = ForestText) },
                            title = "Kurzy",
                            subtitle = "Věty a gramatika",
                            onClick = { showLearningHub = true }
                        )
                        FeatureCard(
                            icon = { Icon(Icons.Default.VolumeUp, contentDescription = null, tint = ForestText) },
                            title = "Poslech",
                            subtitle = "Poslech a porozumění A1–B2",
                            onClick = { lessonLibraryOpenedFromHub = false; showLessonLibrary = true }
                        )
                        FeatureCard(
                            icon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = ForestText) },
                            title = "Čtení",
                            subtitle = "Příběhy a texty podle úrovně",
                            onClick = { readingLibraryOpenedFromHub = false; showReadingLibrary = true }
                        )
                        FeatureCard(
                            icon = { Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = ForestText) },
                            title = "Dialogy",
                            subtitle = "Konverzace s Beatrice",
                            onClick = {
                                saveLastActivity("dialogue", "dialogue", "Řízené dialogy s Beatrice")
                                dialogueOpenedFromHub = false
                                showConversation = true
                            }
                        )
                    }

                    "Pokroky" -> {
                        Text("Tvoje pokroky", color = Ink, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                        Card(
                            onClick = {
                                saveLastActivity("daily-training", "daily-training", "Opakování chyb")
                                showDailyTraining = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = ActionColor)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(44.dp).background(
                                        if (IsDarkTheme) Color(0xFF315142) else Color(0xFF285545),
                                        RoundedCornerShape(14.dp)
                                    ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.White, modifier = Modifier.size(23.dp))
                                }
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text(
                                        "OPAKOVÁNÍ CHYB",
                                        color = ActionText,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        if (pendingReviewCount > 0) "$pendingReviewCount položek čeká na procvičení" else "Upevni si, co ses už naučil/a",
                                        color = ActionText,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 19.sp
                                    )
                                    Text(
                                        "Chytré opakování podle tvých výsledků",
                                        color = if (IsDarkTheme) Color(0xFF244334) else Color(0xFFE0EAE5),
                                        fontSize = 11.sp,
                                        lineHeight = 14.sp
                                    )
                                }
                                Icon(Icons.Default.ArrowBack, contentDescription = "Spustit opakování", tint = ActionText, modifier = Modifier.size(20.dp).rotate(180f))
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            ProgressCard(
                                modifier = Modifier.weight(1f),
                                title = "Dokončeno",
                                value = progressStats.successfullyCompletedActivities.toString(),
                                detail = "aktivit"
                            )
                            ProgressCard(
                                modifier = Modifier.weight(1f),
                                title = "Procvičeno",
                                value = progressStats.practicedActivities.toString(),
                                detail = "sad"
                            )
                            ProgressCard(
                                modifier = Modifier.weight(1f),
                                title = "Série",
                                value = habitStats.currentStreakDays.toString(),
                                detail = "rekord ${habitStats.bestStreakDays}"
                            )
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 13.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Týdenní cíl", color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        if (habitStats.studyDaysInLastWeek >= 5) "Splněno ✓" else "${habitStats.studyDaysInLastWeek}/5 dní",
                                        color = ForestText,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { (habitStats.studyDaysInLastWeek / 5f).coerceIn(0f, 1f) },
                                    modifier = Modifier.fillMaxWidth().height(7.dp),
                                    color = ForestText,
                                    trackColor = Mint
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        if (habitStats.studyDaysInLastWeek >= 5) "Cíl splněn — pokračuj dál." else "Stačí 5 dní z posledních 7.",
                                        color = Muted,
                                        fontSize = 11.sp
                                    )
                                    Text("${habitStats.currentStreakDays} dní v řadě", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    else -> {
                        Text("Knihovna učení", color = Ink, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text("Vše, co potřebuješ k procvičování němčiny, na jednom místě.", color = Muted, fontSize = 14.sp, lineHeight = 20.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                            FeatureTile(
                                modifier = Modifier.weight(1f),
                                icon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = ForestText) },
                                title = "Kurzy",
                                subtitle = "Věty a gramatika",
                                onClick = { showLearningHub = true }
                            )
                            FeatureTile(
                                modifier = Modifier.weight(1f),
                                icon = { Icon(Icons.Default.VolumeUp, contentDescription = null, tint = ForestText) },
                                title = "Poslech",
                                subtitle = "Cvičení A1–B2",
                                onClick = { showLessonLibrary = true }
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                            FeatureTile(
                                modifier = Modifier.weight(1f),
                                icon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = ForestText) },
                                title = "Čtení",
                                subtitle = "Příběhy a texty",
                                onClick = { showReadingLibrary = true }
                            )
                            FeatureTile(
                                modifier = Modifier.weight(1f),
                                icon = { Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = ForestText) },
                                title = "Dialogy",
                                subtitle = "Konverzace",
                                onClick = {
                                    saveLastActivity("dialogue", "dialogue", "Řízené dialogy s Beatrice")
                                    showConversation = true
                                }
                            )
                        }
                        FeatureTile(
                            modifier = Modifier.fillMaxWidth(),
                            icon = { Icon(Icons.Default.School, contentDescription = null, tint = ForestText) },
                            title = "Slovní nástroje",
                            subtitle = "Slovíčka, tvary a chytré procvičování",
                            onClick = {
                                saveLastActivity("word-tools", "word-tools", "Slovní nástroje")
                                showWordTools = true
                            }
                        )
                    }
                }
            }

            NavigationBar(
                containerColor = CardSurface,
                contentColor = Ink,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == "Domů",
                    onClick = { selectedTab = "Domů" },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Domů") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ForestText,
                        selectedTextColor = ForestText,
                        indicatorColor = Mint,
                        unselectedIconColor = Muted,
                        unselectedTextColor = Muted
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == "Pokroky",
                    onClick = { selectedTab = "Pokroky" },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("Pokroky") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ForestText,
                        selectedTextColor = ForestText,
                        indicatorColor = Mint,
                        unselectedIconColor = Muted,
                        unselectedTextColor = Muted
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == "Knihovna",
                    onClick = { selectedTab = "Knihovna" },
                    icon = { Icon(Icons.Default.LibraryBooks, contentDescription = null) },
                    label = { Text("Knihovna") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ForestText,
                        selectedTextColor = ForestText,
                        indicatorColor = Mint,
                        unselectedIconColor = Muted,
                        unselectedTextColor = Muted
                    )
                )
            }
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
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
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
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
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
                                Text(level, color = ForestText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text("$level · $name", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                Text(description, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                                Text("${levelLessons.size} tematických kurzů · 25 otázek v každém", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
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
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = ForestText, modifier = Modifier.size(25.dp))
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text("Poslech a porozumění", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "Poslechni si větu v němčině (de-DE) a vyber její český význam. Text se zobrazí až po odpovědi.",
                                color = Muted, fontSize = 13.sp, lineHeight = 19.sp
                            )
                            Text("25 poslechových otázek", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
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
                                Text((index + 1).toString(), color = ForestText, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text(lesson.title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                Text(lesson.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                                Text("${lesson.level}  •  ${lesson.questions.size} otázek", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
    var wrongQuestions by remember(lesson.id) { mutableStateOf<List<LessonQuestion>>(emptyList()) }
    var isErrorReview by remember(lesson.id) { mutableStateOf(false) }

    fun restartLesson() {
        questions = lesson.questions.shuffled()
        questionIndex = 0
        selectedAnswer = null
        score = 0
        finished = false
        wrongQuestions = emptyList()
        isErrorReview = false
    }

    fun repeatWrongQuestions() {
        if (wrongQuestions.isEmpty()) return
        questions = wrongQuestions.shuffled()
        wrongQuestions = emptyList()
        questionIndex = 0
        selectedAnswer = null
        score = 0
        finished = false
        isErrorReview = true
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
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
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
                        Text(if (isErrorReview) "Opakování chyb dokončeno" else "Lekce dokončena!", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text(
                            score.toString() + " z " + questions.size + " správně",
                            color = Gold,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = when {
                                isErrorReview && wrongQuestions.isEmpty() -> "Skvělé! V tomto kole už nezůstala žádná chybná odpověď."
                                isErrorReview -> "Některé odpovědi stále potřebují procvičit. Můžeš je zopakovat znovu."
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
                if (wrongQuestions.isNotEmpty()) {
                    Button(
                        onClick = { repeatWrongQuestions() },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Opakovat jen chyby (${wrongQuestions.size})", fontWeight = FontWeight.Bold)
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
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
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
                    color = ForestText,
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
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
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
                                selectedAnswer == null -> OptionSurface
                                index == question.correctIndex -> Correct
                                selectedAnswer == index -> Incorrect
                                else -> OptionSurface
                            }
                            Card(
                                onClick = {
                                    if (selectedAnswer == null) {
                                        selectedAnswer = index
                                        val sourceIndex = lesson.questions.indexOfFirst {
                                        it.germanSentence == question.germanSentence &&
                                            it.translation == question.translation
                                    }
                                    if (sourceIndex >= 0) {
                                        val reviewQuestion = question.toReviewQuestion(
                                            id = stableReviewId("sentence", lesson.id, sourceIndex.toString()),
                                            sourceKind = "sentences",
                                            sourceTitle = lesson.title
                                        )
                                        LearningReviewStore.recordAnswer(
                                            context = context,
                                            question = reviewQuestion,
                                            wasCorrect = index == question.correctIndex,
                                            isMistakeReview = isErrorReview
                                        )
                                    }
                                    if (index == question.correctIndex) {
                                        score += 1
                                    } else {
                                        wrongQuestions = wrongQuestions + question
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
                                color = ForestText,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (question.germanSentence.isNotBlank()) {
                                Text("Celá věta:", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(question.germanSentence, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, lineHeight = 23.sp)
                                GermanAudioControls(player = speaker, text = question.germanSentence)
                            }
                            if (question.translation.isNotBlank()) {
                                Text("Česky:", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                                if (!isErrorReview) {
                                    CourseResultStore.save(context, "sentence_${lesson.id}", score, questions.size)
                                    onComplete(score, questions.size)
                                }
                                finished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
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
    var spokenAttempt by remember { mutableStateOf("") }

    fun startTopic(topic: GuidedDialogueTopic) {
        isTransitioning = false
        selectedTopic = topic
        turnIndex = 0
        completed = false
        completionRecorded = false
        lines = emptyList()
        spokenAttempt = ""
    }

    fun resetToTopics() {
        isTransitioning = false
        selectedTopic = null
        turnIndex = 0
        completed = false
        completionRecorded = false
        lines = emptyList()
        spokenAttempt = ""
    }

    fun chooseOption(option: GuidedDialogueOption) {
        val topic = selectedTopic ?: return
        if (completed || isTransitioning) return

        lines = lines +
            GuidedDialogueLine(option.german, option.czech, learner = true) +
            GuidedDialogueLine(option.replyGerman, option.replyCzech, learner = false)
        spokenAttempt = ""
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
                    spokenAttempt = ""
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
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
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
                    Text("Beatrice", color = ForestText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
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
                                Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = ForestText)
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(topic.title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                Text(topic.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                                Text(
                                    topic.turns.size.toString() + " kroků · 3 odpovědi v každém kroku",
                                    color = ForestText,
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
                        color = ForestText,
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
                        colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
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
                        colors = CardDefaults.cardColors(containerColor = if (line.learner) Forest else CardSurface)
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
                        colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
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
                            Text("BEATRICE SE PTÁ", color = ForestText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text(turn.promptGerman, color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold, lineHeight = 25.sp)
                            Text(turn.promptCzech, color = ForestText, fontSize = 15.sp, lineHeight = 21.sp)
                        }
                    }
                    if (isTransitioning) {
                        Text(
                            "Beatrice právě reaguje… přečti si odpověď a její překlad. Další otázka se zobrazí za chvilku.",
                            color = ForestText,
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    } else {
                        Text("Vyber odpověď a přečti ji nahlas", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    if (!isTransitioning) {
                    Text("Zkus nejdřív odpovědět vlastními slovy:", color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    OfflineGermanSpeechInput(onRecognized = { spokenAttempt = it })
                    if (spokenAttempt.isNotBlank()) {
                        Text("Dobrá práce s mluvením. Teď porovnej svou větu s možnostmi níže a vyber nejbližší odpověď.", color = ForestText, fontSize = 13.sp, lineHeight = 19.sp)
                    }
                    turn.options.forEachIndexed { index, option ->
                        Card(
                            onClick = { chooseOption(option) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface)
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
                                    Text((index + 1).toString(), color = ForestText, fontWeight = FontWeight.Bold)
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(option.german, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)
                                    Text(option.czech, color = Muted, fontSize = 14.sp, lineHeight = 19.sp)
                                    Text("Použít tuto odpověď →", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
    detail: String?
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(title, color = Muted, fontSize = 13.sp)
            Text(value, color = ForestText, fontSize = 21.sp, fontWeight = FontWeight.Bold)
            if (!detail.isNullOrBlank()) Text(detail, color = Muted, fontSize = 12.sp)
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
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(Mint, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(subtitle, color = Muted, fontSize = 12.sp, lineHeight = 16.sp, maxLines = 1)
            }
        }
    }
}

@Composable
private fun FeatureTile(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier.size(38.dp).background(Mint, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Text(title, color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Muted, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}
