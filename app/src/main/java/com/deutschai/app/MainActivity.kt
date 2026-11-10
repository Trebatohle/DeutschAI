package com.deutschai.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Assessment
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.clip
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
    val translation: String = "",
    val acceptedAnswers: List<String> = emptyList()
)

internal data class Lesson(
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
    val onboardingPreferences = remember(context) {
        context.getSharedPreferences("deutschai_onboarding", Context.MODE_PRIVATE)
    }
    var showPlacementWelcome by remember(onboardingPreferences) {
        mutableStateOf(
            !onboardingPreferences.getBoolean("placement_choice_made_v1", false) &&
                PlacementAssessmentStore.load(context) == null
        )
    }
    var placementStartedFromWelcome by remember { mutableStateOf(false) }
    var progressStats by remember(preferences) {
        mutableStateOf(LearningProgressStore.load(context))
    }
    var habitStats by remember(preferences) { mutableStateOf(StudyHabitStore.load(context)) }
    var pendingReviewCount by remember(preferences) { mutableStateOf(LearningReviewStore.pendingCount(context)) }
    var reviewEntries by remember(preferences) { mutableStateOf(LearningReviewStore.all(context)) }
    var dueReviewEntries by remember(preferences) { mutableStateOf(LearningReviewStore.prioritizedDue(context)) }
    var rewardPoints by remember(preferences) { mutableStateOf(RewardPointsStore.load(context)) }

    var showConversation by remember { mutableStateOf(false) }
    var showDailyTraining by remember { mutableStateOf(false) }
    var dailyTrainingReturnToOverview by remember { mutableStateOf(false) }
    var showPlacementAssessment by remember { mutableStateOf(false) }
    var showLearningOverview by remember { mutableStateOf(false) }
    var showWordTools by remember { mutableStateOf(false) }
    var showNumberPractice by remember { mutableStateOf(false) }
    var showLearningHub by remember { mutableStateOf(false) }
    var showLessonLibrary by remember { mutableStateOf(false) }
    var lessonLibraryOpenedFromHub by remember { mutableStateOf(false) }
    var readingLibraryOpenedFromHub by remember { mutableStateOf(false) }
    var dialogueOpenedFromHub by remember { mutableStateOf(false) }
    var showArticleCategories by remember { mutableStateOf(false) }
    var showVocabularyPractice by remember { mutableStateOf(false) }
    var showVocabularyNotebook by remember { mutableStateOf(false) }
    var showSavedVocabularyPractice by remember { mutableStateOf(false) }
    var selectedArticleCategory by remember { mutableStateOf<ArticleCategory?>(null) }
    var showVerbCategories by remember { mutableStateOf(false) }
    var selectedVerbTopic by remember { mutableStateOf<VerbPracticeLesson?>(null) }
    var showReadingLibrary by remember { mutableStateOf(false) }
    var selectedReadingArticle by remember { mutableStateOf<ReadingArticle?>(null) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    var courseOpenedFromOverview by remember { mutableStateOf(false) }
    var selectedPhraseReviewLevel by remember { mutableStateOf<String?>(null) }
    var selectedListeningLevel by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf("Domů") }
    val savedLastActivityType = preferences.getString("last_activity_type", "") ?: ""
    val savedLastActivityTitle = preferences.getString("last_activity_title", "") ?: ""
    val savedLastActivityIsPlacement =
        savedLastActivityType.equals("placement", ignoreCase = true) ||
            savedLastActivityTitle.startsWith("Rozřazovací test", ignoreCase = true) ||
            savedLastActivityTitle.startsWith("Vstupní rozřazovací test", ignoreCase = true)
    var lastActivityType by remember(preferences) {
        mutableStateOf(if (savedLastActivityIsPlacement) "" else savedLastActivityType)
    }
    var lastActivityId by remember(preferences) {
        mutableStateOf(if (savedLastActivityIsPlacement) "" else preferences.getString("last_activity_id", "") ?: "")
    }
    var lastActivityTitle by remember(preferences) {
        mutableStateOf(if (savedLastActivityIsPlacement) "" else savedLastActivityTitle)
    }

    LaunchedEffect(savedLastActivityIsPlacement) {
        if (savedLastActivityIsPlacement) {
            preferences.edit()
                .remove("last_activity_type")
                .remove("last_activity_id")
                .remove("last_activity_title")
                .apply()
        }
    }

    fun navigateHome() {
        courseOpenedFromOverview = false
        showConversation = false
        showDailyTraining = false
        dailyTrainingReturnToOverview = false
        showPlacementAssessment = false
        showLearningOverview = false
        showWordTools = false
        showNumberPractice = false
        showLearningHub = false
        showLessonLibrary = false
        lessonLibraryOpenedFromHub = false
        readingLibraryOpenedFromHub = false
        dialogueOpenedFromHub = false
        showArticleCategories = false
        showVocabularyPractice = false
        showVocabularyNotebook = false
        showSavedVocabularyPractice = false
        selectedArticleCategory = null
        showVerbCategories = false
        selectedVerbTopic = null
        showReadingLibrary = false
        selectedReadingArticle = null
        selectedLesson = null
        selectedPhraseReviewLevel = null
        selectedListeningLevel = null
        selectedTab = "Domů"
        placementStartedFromWelcome = false
    }

    fun saveLastActivity(type: String, id: String, title: String) {
        if (
            type.equals("placement", ignoreCase = true) ||
            title.startsWith("Rozřazovací test", ignoreCase = true) ||
            title.startsWith("Vstupní rozřazovací test", ignoreCase = true)
        ) return

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
        if (
            lastActivityType.equals("placement", ignoreCase = true) ||
            lastActivityTitle.startsWith("Rozřazovací test", ignoreCase = true) ||
            lastActivityTitle.startsWith("Vstupní rozřazovací test", ignoreCase = true)
        ) {
            lastActivityType = ""
            lastActivityId = ""
            lastActivityTitle = ""
            preferences.edit()
                .remove("last_activity_type")
                .remove("last_activity_id")
                .remove("last_activity_title")
                .apply()
            return
        }

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
            "number-practice" -> showNumberPractice = true
            "vocabulary-practice" -> showVocabularyPractice = true
            "vocabulary-notebook" -> showVocabularyNotebook = true
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

    fun refreshLearningMetrics() {
        habitStats = StudyHabitStore.load(context)
        pendingReviewCount = LearningReviewStore.pendingCount(context)
        reviewEntries = LearningReviewStore.all(context)
        dueReviewEntries = LearningReviewStore.prioritizedDue(context)
        progressStats = LearningProgressStore.load(context)
        rewardPoints = RewardPointsStore.load(context)
    }

    fun recordScore(score: Int, total: Int, activityKey: String) {
        if (total <= 0) return
        habitStats = StudyHabitStore.recordSession(context)
        pendingReviewCount = LearningReviewStore.pendingCount(context)
        reviewEntries = LearningReviewStore.all(context)
        dueReviewEntries = LearningReviewStore.prioritizedDue(context)
        rewardPoints = RewardPointsStore.recordTestResult(context, activityKey, score, total)
        progressStats = if (score == total) {
            LearningProgressStore.recordPerfectSession(context, activityKey)
        } else {
            LearningProgressStore.load(context)
        }
    }

    if (showPlacementWelcome) {
        PlacementWelcomeScreen(
            onStart = {
                onboardingPreferences.edit().putBoolean("placement_choice_made_v1", true).apply()
                showPlacementWelcome = false
                placementStartedFromWelcome = true
                showPlacementAssessment = true
            },
            onSkip = {
                onboardingPreferences.edit().putBoolean("placement_choice_made_v1", true).apply()
                showPlacementWelcome = false
            }
        )
        return
    }

    if (showLearningOverview) {
        LearningOverviewScreen(
            onBack = { showLearningOverview = false; selectedTab = "Pokroky" },
            onPlacementTest = { showLearningOverview = false; showPlacementAssessment = true },
            onHome = { navigateHome() },
            onStartTraining = {
                dailyTrainingReturnToOverview = true
                showLearningOverview = false
                showDailyTraining = true
            },
            onOpenCourse = { courseId ->
                val lesson = lessons.firstOrNull { it.id == courseId }
                if (lesson != null) {
                    saveLastActivity("lesson", lesson.id, lesson.title)
                    courseOpenedFromOverview = true
                    selectedLesson = lesson
                    showLearningOverview = false
                }
            }
        )
        return
    }

    if (showPlacementAssessment) {
        PlacementAssessmentScreen(
            onHome = { navigateHome() },
            onBack = {
                showPlacementAssessment = false
                if (placementStartedFromWelcome) {
                    placementStartedFromWelcome = false
                    selectedTab = "Domů"
                } else {
                    showLearningOverview = true
                }
            },
            onComplete = {
                placementStartedFromWelcome = false
                habitStats = StudyHabitStore.recordSession(context)
                pendingReviewCount = LearningReviewStore.pendingCount(context)
                progressStats = LearningProgressStore.load(context)
            }
        )
        return
    }

    if (showDailyTraining) {
        DailyTrainingScreen(
            onBack = {
                showDailyTraining = false
                if (dailyTrainingReturnToOverview) {
                    dailyTrainingReturnToOverview = false
                    showLearningOverview = true
                }
            },
            onHome = { navigateHome() },
            onComplete = { score, total ->
                CourseResultStore.save(context, "daily_training", score, total)
                recordScore(score, total, "daily_training")
            }
        )
        return
    }

    if (showNumberPractice) {
        NumberPracticeScreen(
            onBack = { showNumberPractice = false; showWordTools = true },
            onHome = { navigateHome() },
            onComplete = { score, total, activityKey ->
                CourseResultStore.save(context, activityKey, score, total)
                recordScore(score, total, activityKey)
            }
        )
        return
    }

    if (showWordTools) {
        WordToolsScreen(
            onBack = { showWordTools = false; showLearningHub = true },
            onHome = { navigateHome() },
            onNumbers = {
                saveLastActivity("number-practice", "number-practice", "Číslovky")
                showNumberPractice = true
            },
            onComplete = { score, total, activityKey ->
                recordScore(score, total, activityKey)
            }
        )
        return
    }

    if (showConversation) {
        GuidedDialogueScreen(
            onBack = { showConversation = false; showLearningHub = dialogueOpenedFromHub },
            onHome = { navigateHome() },
            // A completed dialogue counts as a study day, but not as a scored quiz session.
            onComplete = { habitStats = StudyHabitStore.recordSession(context) }
        )
        return
    }

    if (selectedReadingArticle != null) {
        ReadingArticleScreen(
            article = selectedReadingArticle!!,
            onHome = { navigateHome() },
            onBack = {
                selectedReadingArticle = null
                showReadingLibrary = true
                refreshLearningMetrics()
            }
        )
        return
    }

    if (showReadingLibrary) {
        ReadingLibraryScreen(
            onBack = { showReadingLibrary = false; showLearningHub = readingLibraryOpenedFromHub },
            onHome = { navigateHome() },
            onSelectArticle = { article ->
                saveLastActivity("reading", article.id, article.title)
                selectedReadingArticle = article
                showReadingLibrary = false
            }
        )
        return
    }

    if (showSavedVocabularyPractice) {
        PersonalNotebookPracticeScreen(
            onHome = { navigateHome() },
            onBack = {
                showSavedVocabularyPractice = false
                showVocabularyNotebook = true
            },
            onComplete = { score, total ->
                recordScore(score, total, "saved_vocabulary")
            }
        )
        return
    }

    if (showVocabularyNotebook) {
        VocabularyNotebookScreen(
            onBack = { showVocabularyNotebook = false },
            onHome = { navigateHome() },
            onPractice = {
                showVocabularyNotebook = false
                showSavedVocabularyPractice = true
            }
        )
        return
    }

    if (showVocabularyPractice) {
        VocabularyPracticeScreen(
            onBack = { showVocabularyPractice = false; showArticleCategories = true },
            onHome = { navigateHome() },
            onComplete = { score, total ->
                recordScore(score, total, "vocabulary_learning")
            }
        )
        return
    }

    if (selectedArticleCategory != null) {
        ArticleQuizScreen(
            category = selectedArticleCategory!!,
            onHome = { navigateHome() },
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
            onHome = { navigateHome() },
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
            onHome = { navigateHome() },
            onVocabulary = {
                saveLastActivity("vocabulary-practice", "vocabulary-practice", "Slovní zásoba")
                showArticleCategories = false
                showVocabularyPractice = true
            },
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
            onHome = { navigateHome() },
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
            onHome = { navigateHome() },
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
                saveLastActivity("word-tools", "word-tools", "Slovní druhy a gramatika")
                showLearningHub = false
                showWordTools = true
            }
        )
        return
    }

    if (selectedPhraseReviewLevel != null) {
        PhraseReviewScreen(
            level = selectedPhraseReviewLevel!!,
            onHome = { navigateHome() },
            onBack = {
                selectedPhraseReviewLevel = null
                showLessonLibrary = true
                refreshLearningMetrics()
            }
        )
        return
    }

    if (selectedListeningLevel != null) {
        ListeningPracticeScreen(
            level = selectedListeningLevel!!,
            onHome = { navigateHome() },
            onBack = { selectedListeningLevel = null; showLessonLibrary = true; refreshLearningMetrics() }
        )
        return
    }

    if (selectedLesson != null) {
        LessonScreen(
            lesson = selectedLesson!!,
            onHome = { navigateHome() },
            onBack = {
                selectedLesson = null
                if (courseOpenedFromOverview) {
                    courseOpenedFromOverview = false
                    showLearningOverview = true
                } else {
                    showLessonLibrary = true
                }
            },
            onComplete = { score, total ->
                recordScore(score, total, "sentence_${selectedLesson!!.id}")
            }
        )
        return
    }

    if (showLessonLibrary) {
        LessonLibraryScreen(
            onBack = { showLessonLibrary = false; showLearningHub = lessonLibraryOpenedFromHub },
            onHome = { navigateHome() },
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

    val homeRecommendation = remember(reviewEntries, dueReviewEntries, lastActivityType) {
        buildHomeStudyRecommendation(
            allEntries = reviewEntries,
            dueEntries = dueReviewEntries,
            hasLastActivity = lastActivityType.isNotBlank()
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (selectedTab == "Pokroky" || selectedTab == "Knihovna") Modifier
                        else Modifier.verticalScroll(rememberScrollState())
                    )
                    .padding(
                        start = if (selectedTab == "Knihovna") 15.dp else 18.dp,
                        top = if (selectedTab == "Knihovna") 8.dp else 12.dp,
                        end = if (selectedTab == "Knihovna") 15.dp else 18.dp,
                        bottom = if (selectedTab == "Knihovna") 8.dp else 12.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(
                    when (selectedTab) {
                        "Domů" -> 10.dp
                        "Knihovna" -> 8.dp
                        else -> 12.dp
                    }
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GermanFlagMark()
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("DeutschAI", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text(
                                when (selectedTab) {
                                    "Pokroky" -> "Tvůj pokrok, krok za krokem."
                                    "Knihovna" -> "Vše pro tvoje další studium."
                                    else -> "Osobní plán němčiny."
                                },
                                color = Muted,
                                fontSize = 12.sp
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            val nextTheme = !IsDarkTheme
                            context.getSharedPreferences("beatrice_appearance", Context.MODE_PRIVATE)
                                .edit().putBoolean("dark_mode", nextTheme).apply()
                            applyBeatriceTheme(nextTheme)
                        },
                        modifier = Modifier.size(44.dp).background(Mint, CircleShape)
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
                        Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    "Guten Tag!",
                                    color = ForestText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    "Dnes se posuneš zase o krok dál.",
                                    color = Ink,
                                    fontSize = 24.sp,
                                    lineHeight = 29.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Tvůj plán vychází z toho, co už umíš a co potřebuješ upevnit.",
                                    color = Muted,
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                            }

                            HomeRecommendationCard(
                                recommendation = homeRecommendation,
                                onAction = {
                                    when (homeRecommendation.type) {
                                        HomeRecommendationType.REVIEW_DUE -> {
                                            saveLastActivity("daily-training", "daily-training", "Osobní denní trénink")
                                            showDailyTraining = true
                                        }
                                        HomeRecommendationType.FOCUS_SKILL -> {
                                            when (homeRecommendation.sourceKind) {
                                                "sentences", "phrases" -> {
                                                    lessonLibraryOpenedFromHub = false
                                                    showLessonLibrary = true
                                                }
                                                "articles" -> showArticleCategories = true
                                                "verbs" -> showVerbCategories = true
                                                "wordtools" -> showWordTools = true
                                                "numbers" -> showWordTools = true
                                                "listening" -> {
                                                    lessonLibraryOpenedFromHub = false
                                                    showLessonLibrary = true
                                                }
                                                "reading" -> {
                                                    readingLibraryOpenedFromHub = false
                                                    showReadingLibrary = true
                                                }
                                                "vocabulary" -> showVocabularyPractice = true
                                                else -> showLearningHub = true
                                            }
                                        }
                                        HomeRecommendationType.CONTINUE_ACTIVITY -> continueLastActivity()
                                        HomeRecommendationType.EXPLORE_LIBRARY -> selectedTab = "Knihovna"
                                    }
                                }
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                ProgressCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Zvládnuté",
                                    value = reviewEntries.count { it.correctStreak >= 3 && it.intervalDays >= 7 }.toString(),
                                    detail = "položek"
                                )
                                ProgressCard(
                                    modifier = Modifier.weight(1f),
                                    title = "K opakování",
                                    value = dueReviewEntries.size.toString(),
                                    detail = "připraveno"
                                )
                                ProgressCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Série dní",
                                    value = habitStats.currentStreakDays.toString(),
                                    detail = "v řadě"
                                )
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(19.dp),
                                colors = CardDefaults.cardColors(containerColor = CardSurface)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(15.dp),
                                    verticalArrangement = Arrangement.spacedBy(9.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Tvůj studijní rytmus", color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            habitStats.studyDaysInLastWeek.toString() + "/5 dní",
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
                                    Text(
                                        if (habitStats.studyDaysInLastWeek >= 5) {
                                            "Týdenní cíl máš splněný. Udrž si pravidelnost."
                                        } else {
                                            "Stačí krátké soustředěné studium alespoň 5 dní v týdnu."
                                        },
                                        color = Muted,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp
                                    )
                                }
                            }

                            if (
                                lastActivityTitle.isNotBlank() &&
                                !lastActivityType.equals("placement", ignoreCase = true) &&
                                !lastActivityTitle.startsWith("Rozřazovací test", ignoreCase = true) &&
                                !lastActivityTitle.startsWith("Vstupní rozřazovací test", ignoreCase = true) &&
                                homeRecommendation.type != HomeRecommendationType.CONTINUE_ACTIVITY
                            ) {
                                Card(
                                    onClick = { continueLastActivity() },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(11.dp)
                                    ) {
                                        Icon(Icons.Default.Assessment, contentDescription = null, tint = ForestText)
                                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text("Pokračovat v poslední aktivitě", color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            Text(lastActivityTitle, color = Muted, fontSize = 12.sp, maxLines = 1)
                                        }
                                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = ForestText, modifier = Modifier.rotate(180f))
                                    }
                                }
                            }

                            Card(
                                onClick = { selectedTab = "Knihovna" },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = if (IsDarkTheme) Color(0xFF1A2C24) else Mint)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Default.LibraryBooks, contentDescription = null, tint = ForestText, modifier = Modifier.size(22.dp))
                                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text("Knihovna kurzů a materiálů", color = ForestText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        Text("Kurzy, poslech, četba, dialogy a slovní zásoba", color = Muted, fontSize = 11.sp, lineHeight = 15.sp)
                                    }
                                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = ForestText, modifier = Modifier.rotate(180f))
                                }
                            }
                        }
                    }

                    "Pokroky" -> {
                        Text("Tvoje pokroky", color = Ink, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                        FeatureCard(
                            icon = { Icon(Icons.Default.BarChart, contentDescription = null, tint = ForestText) },
                            title = "Přehled znalostí",
                            subtitle = "Skutečná úspěšnost podle oblastí a dlouhodobé opakování",
                            onClick = { showLearningOverview = true }
                        )
                        FeatureCard(
                            icon = { Icon(Icons.Default.BarChart, contentDescription = null, tint = ForestText) },
                            title = "Vstupní rozřazovací test",
                            subtitle = "24 krátkých otázek · doporučená úroveň A1–C2",
                            onClick = { showPlacementAssessment = true }
                        )

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
                        Text("Knihovna učení", color = Ink, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Všechny části aplikace na jednom místě.",
                            color = Muted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            maxLines = 1
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                            FeatureTile(
                                modifier = Modifier.weight(1f).height(116.dp),
                                compact = true,
                                icon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = ForestText) },
                                title = "Kurzy",
                                subtitle = "Věty a gramatika",
                                onClick = { showLearningHub = true }
                            )
                            FeatureTile(
                                modifier = Modifier.weight(1f).height(116.dp),
                                compact = true,
                                icon = { Icon(Icons.Default.VolumeUp, contentDescription = null, tint = ForestText) },
                                title = "Poslech",
                                subtitle = "Cvičení A1–B2",
                                onClick = { showLessonLibrary = true }
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                            FeatureTile(
                                modifier = Modifier.weight(1f).height(116.dp),
                                compact = true,
                                icon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = ForestText) },
                                title = "Čtení",
                                subtitle = "Příběhy a texty",
                                onClick = { showReadingLibrary = true }
                            )
                            FeatureTile(
                                modifier = Modifier.weight(1f).height(116.dp),
                                compact = true,
                                icon = { Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = ForestText) },
                                title = "Dialogy",
                                subtitle = "Konverzace",
                                onClick = {
                                    saveLastActivity("dialogue", "dialogue", "Řízené dialogy s Beatrice")
                                    showConversation = true
                                }
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                            FeatureTile(
                                modifier = Modifier.weight(1f).height(116.dp),
                                compact = true,
                                icon = { Icon(Icons.Default.School, contentDescription = null, tint = ForestText) },
                                title = "Slovní nástroje",
                                subtitle = "Slovíčka a gramatika",
                                onClick = {
                                    saveLastActivity("word-tools", "word-tools", "Slovní nástroje")
                                    showWordTools = true
                                }
                            )
                            FeatureTile(
                                modifier = Modifier.weight(1f).height(116.dp),
                                compact = true,
                                icon = { Icon(Icons.Default.LibraryBooks, contentDescription = null, tint = ForestText) },
                                title = "Můj slovníček",
                                subtitle = "${VocabularyNotebookStore.count(context) + ReadingWordNotebookStore.entries(context).size} uložených slov",
                                onClick = {
                                    saveLastActivity("vocabulary-notebook", "my-notebook", "Můj slovníček")
                                    showVocabularyNotebook = true
                                }
                            )
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth().height(68.dp),
                color = CardSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainBottomTab(
                        modifier = Modifier.weight(1f),
                        selected = selectedTab == "Domů",
                        icon = Icons.Default.Home,
                        label = "Domů",
                        onClick = { selectedTab = "Domů" }
                    )
                    MainBottomTab(
                        modifier = Modifier.weight(1f),
                        selected = selectedTab == "Pokroky",
                        icon = Icons.Default.BarChart,
                        label = "Pokroky",
                        onClick = { selectedTab = "Pokroky" }
                    )
                    MainBottomTab(
                        modifier = Modifier.weight(1f),
                        selected = selectedTab == "Knihovna",
                        icon = Icons.Default.LibraryBooks,
                        label = "Knihovna",
                        onClick = { selectedTab = "Knihovna" }
                    )
                }
            }
        }
    }
}

@Composable
private fun GermanFlagMark() {
    Column(
        modifier = Modifier.size(29.dp).clip(CircleShape)
    ) {
        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color(0xFF111111)))
        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color(0xFFDD0000)))
        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color(0xFFFFCE00)))
    }
}

@Composable
private fun HomeRecommendationCard(
    recommendation: HomeStudyRecommendation,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (IsDarkTheme) Color(0xFF192A23) else Forest
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Gold, modifier = Modifier.size(17.dp))
                Text(
                    "DOPORUČENÝ DALŠÍ KROK",
                    color = Color(0xFFC7DFD3),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Text(
                recommendation.title,
                color = Color.White,
                fontSize = 21.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                recommendation.explanation,
                color = Color(0xFFE0EAE5),
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Gold, modifier = Modifier.size(14.dp))
                Text(
                    "Přibližně " + recommendation.estimatedMinutes + " min",
                    color = Color(0xFFE0EAE5),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                if (recommendation.dueCount > 0) {
                    Text("·", color = Color(0xFFE0EAE5), fontSize = 12.sp)
                    Text(
                        recommendation.dueCount.toString() + " položek k opakování",
                        color = Color(0xFFE0EAE5),
                        fontSize = 11.sp
                    )
                } else if (recommendation.accuracyPercent != null) {
                    Text("·", color = Color(0xFFE0EAE5), fontSize = 12.sp)
                    Text(
                        "Úspěšnost " + recommendation.accuracyPercent + " %",
                        color = Color(0xFFE0EAE5),
                        fontSize = 11.sp
                    )
                }
            }
            Button(
                onClick = onAction,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor = GoldText
                ),
                shape = RoundedCornerShape(13.dp)
            ) {
                Text(recommendation.actionLabel, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun LessonLibraryScreen(
    onBack: () -> Unit,
    onSelectLesson: (Lesson) -> Unit,
    onSelectPhraseReview: (String) -> Unit,
    onSelectListening: (String) -> Unit,
    onHome: () -> Unit = onBack
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
            AppScreenHeader(
                title = if (selectedLevel == null) "Kurzy podle úrovně" else "Kurzová knihovna · $selectedLevel",
                onBack = { if (selectedLevel != null) selectedLevel = null else onBack() },
                onHome = onHome
            )

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
    onComplete: (score: Int, total: Int) -> Unit,
    onHome: () -> Unit = onBack
) {
    LessonPracticeScreen(
        lesson = lesson,
        onBack = onBack,
        onComplete = onComplete,
        onHome = onHome
    )
}

private data class GuidedDialogueLine(
    val german: String,
    val czech: String,
    val learner: Boolean
)

@Composable
private fun GuidedDialogueScreen(
    onBack: () -> Unit,
    onComplete: () -> Unit,
    onHome: () -> Unit = onBack
) {
    var selectedTopic by remember { mutableStateOf<GuidedDialogueTopic?>(null) }
    var selectedBranchTopic by remember { mutableStateOf<BranchingDialogueTopic?>(null) }
    var turnIndex by remember { mutableStateOf(0) }
    var completed by remember { mutableStateOf(false) }
    var completionRecorded by remember { mutableStateOf(false) }
    var lines by remember { mutableStateOf(emptyList<GuidedDialogueLine>()) }
    var isTransitioning by remember { mutableStateOf(false) }

    fun startTopic(topic: GuidedDialogueTopic) {
        isTransitioning = false
        selectedBranchTopic = null
        selectedTopic = topic
        turnIndex = 0
        completed = false
        completionRecorded = false
        lines = emptyList()
    }

    fun resetToTopics() {
        isTransitioning = false
        selectedTopic = null
        selectedBranchTopic = null
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

    if (selectedBranchTopic != null) {
        BranchingDialogueSessionScreen(
            topic = selectedBranchTopic!!,
            onBack = { selectedBranchTopic = null },
            onComplete = onComplete
        )
        return
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
            AppScreenHeader(title = "Řízené dialogy · A1 · offline", onBack = onBack, onHome = onHome)

            if (selectedTopic == null) {
                Text("Vyber si situaci", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                Text(
                    "V každém kroku uvidíš tři přirozené odpovědi. Německá věta i český překlad jsou vidět současně.",
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
                Text("Interaktivní scénáře s různým pokračováním", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                branchingGuidedDialogueTopics.forEach { topic ->
                    Card(
                        onClick = { selectedBranchTopic = topic },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Mint)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(42.dp).background(Forest, RoundedCornerShape(13.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Color.White)
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(topic.title, color = ForestText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(topic.subtitle, color = ForestText, fontSize = 12.sp, lineHeight = 17.sp)
                                Text(
                                    "${topic.level} · ${topic.nodes.size} situací · skutečné větvení",
                                    color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold
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
                        "Klasické dialogy mají pevnou posloupnost. Interaktivní scénáře reagují na tvoje volby, zaznamenávají chyby do chytrého opakování a na konci ukážou užitečné fráze. Vše funguje offline.",
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
                        Text("Vyber jednu z nabízených odpovědí", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    if (!isTransitioning) {
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
private fun MainBottomTab(
    modifier: Modifier = Modifier,
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().clickable(onClick = onClick).padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(width = 64.dp, height = 34.dp)
                .background(if (selected) Mint else Color.Transparent, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) ForestText else Muted,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            label,
            color = if (selected) ForestText else Muted,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun FeatureTile(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    compact: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(if (compact) 11.dp else 15.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 10.dp)
        ) {
            Box(
                modifier = Modifier.size(if (compact) 30.dp else 38.dp)
                    .background(Mint, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Text(
                title,
                color = Ink,
                fontSize = if (compact) 14.sp else 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                subtitle,
                color = Muted,
                fontSize = if (compact) 11.sp else 12.sp,
                lineHeight = if (compact) 14.sp else 16.sp,
                maxLines = 2
            )
        }
    }
}
