package com.deutschai.app

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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * One stable visual identity for every learning category. The icons use native
 * Material vectors (not generated lettering), so they remain crisp and cannot
 * introduce accidental foreign characters into the UI.
 */
internal enum class LearningCategoryVisual(
    val image: ImageVector,
    val lightBackground: Color,
    val lightTint: Color,
    val darkBackground: Color,
    val darkTint: Color
) {
    COURSES(
        Icons.Default.Layers,
        Color(0xFFE8EFF7), Color(0xFF315B82),
        Color(0xFF263747), Color(0xFFAFCBE6)
    ),
    VERBS(
        Icons.Default.SwapHoriz,
        Color(0xFFF6EFDF), Color(0xFF8A672F),
        Color(0xFF3C3426), Color(0xFFE4C58B)
    ),
    NOUNS(
        Icons.Default.Label,
        Color(0xFFF5EBEE), Color(0xFF8E5363),
        Color(0xFF3E2D34), Color(0xFFE2B5C2)
    ),
    WORD_TOOLS(
        Icons.Default.School,
        Color(0xFFEAF1E9), Color(0xFF47704E),
        Color(0xFF29392D), Color(0xFFB1D4B3)
    ),
    READING(
        Icons.Default.MenuBook,
        Color(0xFFE9F1F6), Color(0xFF35617E),
        Color(0xFF253743), Color(0xFFAFCBDD)
    ),
    DIALOGUES(
        Icons.Default.Forum,
        Color(0xFFF0ECF7), Color(0xFF67568E),
        Color(0xFF342D43), Color(0xFFC8B9E8)
    ),
    LISTENING(
        Icons.Default.VolumeUp,
        Color(0xFFE5F2EE), Color(0xFF2F6A5C),
        Color(0xFF233B35), Color(0xFFA6D9CB)
    ),
    NOTEBOOK(
        Icons.Default.LibraryBooks,
        Color(0xFFEEF1E8), Color(0xFF637348),
        Color(0xFF343B2B), Color(0xFFD0D9AA)
    )
}

@Composable
internal fun learningCategoryBackground(category: LearningCategoryVisual): Color =
    if (IsDarkTheme) category.darkBackground else category.lightBackground

@Composable
internal fun learningCategoryTint(category: LearningCategoryVisual): Color =
    if (IsDarkTheme) category.darkTint else category.lightTint

@Composable
internal fun LearningHubScreen(
    onBack: () -> Unit,
    onCourses: () -> Unit,
    onArticles: () -> Unit,
    onVerbs: () -> Unit,
    onDialogues: () -> Unit,
    onReading: () -> Unit,
    onWordTools: () -> Unit,
    onVocabularyDrill: () -> Unit,
    onHome: () -> Unit = onBack
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            ScreenHeader("Knihovna učení", onBack, onHome)
            Text("Co chceš dnes procvičit?", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text(
                "Vyber hlavní kategorii a potom konkrétní téma. Každé cvičení funguje offline.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
            LearningCard(
                icon = { Icon(LearningCategoryVisual.COURSES.image, contentDescription = null, tint = learningCategoryTint(LearningCategoryVisual.COURSES)) },
                iconBackground = learningCategoryBackground(LearningCategoryVisual.COURSES),
                title = "Kurzy A1–B2",
                subtitle = "Čtyři úrovně podle náročnosti, tematické lekce a samostatné chytré opakování frází v každé úrovni.",
                detail = "Tematické lekce A1–B2 · 25 otázek v každé",
                onClick = onCourses
            )
            LearningCard(
                icon = { Icon(LearningCategoryVisual.VERBS.image, contentDescription = null, tint = learningCategoryTint(LearningCategoryVisual.VERBS)) },
                iconBackground = learningCategoryBackground(LearningCategoryVisual.VERBS),
                title = "Důležitá slovesa",
                subtitle = "Procvič si časování ve větách, ne jen izolované tvary.",
                detail = "Časování sloves v praktických větách · 25 otázek",
                onClick = onVerbs
            )
            LearningCard(
                icon = { Icon(LearningCategoryVisual.NOUNS.image, contentDescription = null, tint = learningCategoryTint(LearningCategoryVisual.NOUNS)) },
                iconBackground = learningCategoryBackground(LearningCategoryVisual.NOUNS),
                title = "Členy a podstatná jména",
                subtitle = "Vyber si slovní téma: bydlení, jídlo, práce, cestování a další.",
                detail = "Tematické sady · 25 slov v každé",
                onClick = onArticles
            )
            LearningCard(
                icon = { Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color(0xFF8A672F)) },
                iconBackground = Color(0xFFF6EFDF),
                title = "Dril nejpoužívanějších slov",
                subtitle = "Běžná mluvená němčina. V A1 a A2 dostávají přednost důležitá slovesa.",
                detail = "Úrovně A1–B2 · volba 5 nebo 10 slov",
                onClick = onVocabularyDrill
            )
            LearningCard(
                icon = { Icon(LearningCategoryVisual.WORD_TOOLS.image, contentDescription = null, tint = learningCategoryTint(LearningCategoryVisual.WORD_TOOLS)) },
                iconBackground = learningCategoryBackground(LearningCategoryVisual.WORD_TOOLS),
                title = "Slovní druhy a gramatika",
                subtitle = "Předložky, spojky, příslovce, přivlastňovací zájmena a číslovky.",
                detail = "Gramatické oblasti + číslovky · 3 způsoby procvičení",
                onClick = onWordTools
            )
            LearningCard(
                icon = { Icon(LearningCategoryVisual.READING.image, contentDescription = null, tint = learningCategoryTint(LearningCategoryVisual.READING)) },
                iconBackground = learningCategoryBackground(LearningCategoryVisual.READING),
                title = "Čtení v němčině",
                subtitle = "Dlouhé příběhy podle úrovně A1–C2; podrž slovo a zobrazíš český význam.",
                detail = "Příběhy podle úrovní A1–C2",
                onClick = onReading
            )
            LearningCard(
                icon = { Icon(LearningCategoryVisual.DIALOGUES.image, contentDescription = null, tint = learningCategoryTint(LearningCategoryVisual.DIALOGUES)) },
                iconBackground = learningCategoryBackground(LearningCategoryVisual.DIALOGUES),
                title = "Řízené dialogy",
                subtitle = "Tři doporučené odpovědi, vždy s českým překladem a navazující replikou.",
                detail = "Řízené scénáře s doporučenými odpověďmi",
                onClick = onDialogues
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Mint)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Postupně rozšiřovaná knihovna", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        "Aplikace je nyní postavená na jasných scénářích a strukturovaných otázkách místo volné AI konverzace. Další dialogy a slovní sady budeme přidávat jako ověřený obsah.",
                        color = ForestText,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
internal fun ArticleCategoryLibraryScreen(
    onBack: () -> Unit,
    onVocabulary: () -> Unit,
    onDrill: () -> Unit,
    onSelectCategory: (ArticleCategory) -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ScreenHeader("Členy a slovíčka", onBack, onHome)
            Text("Vyber slovní téma", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text(
                "V každé sadě je 25 podstatných jmen. Vyber správný člen der, die nebo das a uč se zároveň český význam.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
            LearningCard(
                icon = { Icon(Icons.Default.Book, contentDescription = null, tint = ForestText) },
                title = "Slovní zásoba",
                subtitle = "Každá otázka ukáže pouze německý člen a podstatné jméno (der/die/das). Příkladovou větu uslyšíš až po odpovědi.",
                detail = "${vocabularyCatalog.size} slov · náhodné sady po 25",
                onClick = onVocabulary
            )
            LearningCard(
                icon = { Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color(0xFF8A672F)) },
                iconBackground = Color(0xFFF6EFDF),
                title = "Dril nejpoužívanějších slov",
                subtitle = "Slova pro každodenní mluvu. V jedné sadě 5 nebo 10 výrazů s aktivním vybavováním a poslechem.",
                detail = "${frequencyVocabularyCatalog.size} slov · A1–B2",
                onClick = onDrill
            )
            articleCategories.forEachIndexed { index, category ->
                Card(
                    onClick = { onSelectCategory(category) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(42.dp).background(Mint, RoundedCornerShape(13.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text((index + 1).toString(), color = ForestText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(category.title, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Text(category.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                            Text("25 otázek · A1", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            LatestResultLine(CourseResultStore.load(context, "articles_${category.id}"))
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun VerbCategoryLibraryScreen(
    onBack: () -> Unit,
    onFrequencyDrill: () -> Unit,
    onSelectTopic: (VerbPracticeLesson) -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    var selectedPart by remember { mutableStateOf<String?>(null) }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ScreenHeader(
                "Důležitá slovesa",
                onBack = { if (selectedPart != null) selectedPart = null else onBack() },
                onHome = onHome
            )
            Text(
                if (selectedPart == null) "Vyber způsob procvičování"
                else if (selectedPart == "advanced") "Pokročilé procvičování"
                else "Základní procvičování",
                color = Ink,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                if (selectedPart == null) {
                    "Nejprve si osvoj tvary v běžných větách. Až si budeš jistější, přejdi na obtížnější výběr mezi různými slovesy."
                } else if (selectedPart == "advanced") {
                    "V každé otázce jsou čtyři různá slovesa. Vyber jediné, které významově i tvarem správně zapadá do věty."
                } else {
                    "Doplň správný tvar jednoho procvičovaného slovesa. Otázky střídají pět sloves v jedné sadě."
                },
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            if (selectedPart == null) {
                LearningCard(
                    icon = { Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color(0xFF8A672F)) },
                    iconBackground = Color(0xFFF6EFDF),
                    title = "Frekvenční dril sloves A1–B2",
                    subtitle = "Procvič nejpoužívanější slovesa v běžných větách, včetně důležitých tvarů a poslechu.",
                    detail = frequencyVocabularyCatalog.count { it.kind == FrequencyWordKind.VERB }.toString() + " sloves · možnost 5 nebo 10",
                    onClick = onFrequencyDrill
                )
                LearningCard(
                    icon = { Icon(Icons.Default.TextFields, contentDescription = null, tint = learningCategoryTint(LearningCategoryVisual.VERBS)) },
                    iconBackground = learningCategoryBackground(LearningCategoryVisual.VERBS),
                    title = "1. Základní procvičování",
                    subtitle = "Nauč se tvary pěti důležitých sloves v různých větách.",
                    detail = verbPracticeTopics.size.toString() + " lekcí · 25 otázek v každé",
                    onClick = { selectedPart = "basic" }
                )
                LearningCard(
                    icon = { Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF315B82)) },
                    iconBackground = Color(0xFFE8EFF7),
                    title = "2. Pokročilé procvičování",
                    subtitle = "Rozlišuj významy pěti sloves a vybírej správný tvar ze čtyř různých sloves.",
                    detail = verbPracticeTopics.size.toString() + " lekcí · 25 otázek v každé",
                    onClick = { selectedPart = "advanced" }
                )
            } else {
                val topics = if (selectedPart == "advanced") advancedVerbPracticeTopics else verbPracticeTopics
                topics.forEachIndexed { index, topic ->
                    Card(
                        onClick = { onSelectTopic(topic) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(42.dp).background(learningCategoryBackground(LearningCategoryVisual.VERBS), RoundedCornerShape(13.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text((index + 1).toString(), color = learningCategoryTint(LearningCategoryVisual.VERBS), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(topic.title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                Text(topic.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                                val verbNames = topic.verbIds.mapNotNull { id -> verbDefinitions.find { it.id == id }?.infinitive }
                                Text(verbNames.joinToString(" · "), color = learningCategoryTint(LearningCategoryVisual.VERBS), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("25 otázek · stejných 5 sloves v sadě", color = Muted, fontSize = 12.sp)
                                LatestResultLine(CourseResultStore.load(context, "verbs_${topic.id}"))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ArticleQuizScreen(
    category: ArticleCategory,
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    var words by remember(category.id) { mutableStateOf(category.words.shuffled()) }
    var questionIndex by remember(category.id) { mutableStateOf(0) }
    var selectedArticle by remember(category.id) { mutableStateOf<String?>(null) }
    var score by remember(category.id) { mutableStateOf(0) }
    var finished by remember(category.id) { mutableStateOf(false) }
    var wrongWords by remember(category.id) { mutableStateOf<List<ArticleWord>>(emptyList()) }
    var isErrorReview by remember(category.id) { mutableStateOf(false) }
    val choices = listOf("der", "die", "das")
    val total = words.size.coerceAtMost(25)

    fun restart() {
        words = category.words.shuffled()
        questionIndex = 0
        selectedArticle = null
        score = 0
        finished = false
        wrongWords = emptyList()
        isErrorReview = false
    }

    fun repeatWrongWords() {
        if (wrongWords.isEmpty()) return
        words = wrongWords.shuffled()
        wrongWords = emptyList()
        questionIndex = 0
        selectedArticle = null
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
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            ScreenHeader(category.title, onBack, onHome)
            if (finished) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Translate, contentDescription = null, tint = Gold, modifier = Modifier.size(44.dp))
                        Text(if (isErrorReview) "Opakování chyb dokončeno" else "Sada dokončena", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("$score z $total správně", color = Gold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(
                            if (isErrorReview) {
                                if (wrongWords.isEmpty()) "Skvělé! V tomto kole už nezůstalo žádné chybné slovo."
                                else "Zbývající chyby můžeš procvičit ještě jednou."
                            } else {
                                "U každého slova si zapamatuj celý tvar se členem, například „der Tisch“."
                            },
                            color = Color(0xFFE0EAE5),
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                }
                if (wrongWords.isNotEmpty()) {
                    Button(
                        onClick = { repeatWrongWords() },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text("Opakovat jen chyby (${wrongWords.size})", fontWeight = FontWeight.Bold) }
                }
                Button(
                    onClick = { restart() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zopakovat sadu", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Vybrat jiné téma", fontWeight = FontWeight.Bold) }
            } else {
                val word = words[questionIndex]
                Text("OTÁZKA ${questionIndex + 1} Z $total", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / total.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Doplň správný člen", color = Ink, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("___ ${word.word}", color = ForestText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(word.czech, color = Muted, fontSize = 17.sp)
                        Text("Vyber: der, die nebo das.", color = Muted, fontSize = 13.sp)
                    }
                }
                choices.forEach { article ->
                    val bg = when {
                        selectedArticle == null -> OptionSurface
                        article == word.article -> Correct
                        article == selectedArticle -> Incorrect
                        else -> OptionSurface
                    }
                    Card(
                        onClick = {
                            if (selectedArticle == null) {
                                selectedArticle = article
                                val correct = article == word.article
                                val reviewQuestion = ReviewQuestion(
                                    id = stableReviewId("articles", category.id, word.word.lowercase()),
                                    sourceKind = "articles",
                                    sourceTitle = category.title,
                                    prompt = "Jaký člen patří ke slovu „${word.word}“? Česky: ${word.czech}",
                                    choices = choices,
                                    correctIndex = choices.indexOf(word.article),
                                    explanation = "${word.article} ${word.word} = ${word.czech}. Uč se podstatné jméno vždy společně se členem.",
                                    germanSentence = "${word.article} ${word.word}",
                                    translation = word.czech
                                )
                                LearningReviewStore.recordAnswer(
                                    context = context,
                                    question = reviewQuestion,
                                    wasCorrect = correct,
                                    isMistakeReview = isErrorReview
                                )
                                if (correct) {
                                    score += 1
                                } else {
                                    wrongWords = wrongWords + word
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = bg)
                    ) {
                        Text(
                            article,
                            modifier = Modifier.fillMaxWidth().padding(17.dp),
                            color = Ink,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                if (selectedArticle != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = if (selectedArticle == word.article) Correct else Incorrect)
                    ) {
                        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(
                                if (selectedArticle == word.article) "Správně! Gut gemacht!" else "Správný člen je „${word.article}“.",
                                color = ForestText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text("${word.article} ${word.word} = ${word.czech}", color = Ink, fontSize = 15.sp)
                            Text("Uč se slovo vždy společně se členem.", color = Muted, fontSize = 13.sp)
                        }
                    }
                    Button(
                        onClick = {
                            if (questionIndex < total - 1) {
                                questionIndex += 1
                                selectedArticle = null
                            } else {
                                if (!isErrorReview) {
                                    CourseResultStore.save(context, "articles_${category.id}", score, total)
                                    onComplete(score, total)
                                }
                                finished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(if (questionIndex == total - 1) "Zobrazit výsledek" else "Další slovo", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
internal fun VerbQuizScreen(
    topic: VerbPracticeLesson,
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int) -> Unit,
    onHome: () -> Unit = onBack
) {
    val context = LocalContext.current
    var questions by remember(topic.id) { mutableStateOf(buildVerbQuestions(topic).shuffled()) }
    var questionIndex by remember(topic.id) { mutableStateOf(0) }
    var selectedAnswer by remember(topic.id) { mutableStateOf<Int?>(null) }
    var score by remember(topic.id) { mutableStateOf(0) }
    var finished by remember(topic.id) { mutableStateOf(false) }
    var wrongQuestions by remember(topic.id) { mutableStateOf<List<LessonQuestion>>(emptyList()) }
    var isErrorReview by remember(topic.id) { mutableStateOf(false) }

    fun restart() {
        questions = buildVerbQuestions(topic).shuffled()
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
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            ScreenHeader(topic.title, onBack, onHome)
            Text(topic.subtitle, color = Muted, fontSize = 14.sp, lineHeight = 20.sp)
            Text(
                "Slovesa: " + topic.verbIds.mapNotNull { id -> verbDefinitions.find { it.id == id }?.infinitive }.joinToString(" · "),
                color = ForestText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (finished) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(if (isErrorReview) "Opakování chyb dokončeno" else "Slovesná sada dokončena", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                        Text("$score z ${questions.size} správně", color = Gold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(
                            if (isErrorReview) {
                                if (wrongQuestions.isEmpty()) "Skvělé! V tomto kole už nezůstala žádná chybná odpověď."
                                else "Některé odpovědi stále potřebují procvičit. Můžeš je zopakovat znovu."
                            } else {
                                "Procvičil/a jsi pět důležitých sloves v různých větách. Říkej si nahlas celé německé věty i jejich český význam."
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
                    ) { Text("Opakovat jen chyby (${wrongQuestions.size})", fontWeight = FontWeight.Bold) }
                }
                Button(
                    onClick = { restart() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Zopakovat sadu (otázky se zamíchají)", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Vybrat jinou sadu", fontWeight = FontWeight.Bold) }
            } else {
                val question = questions[questionIndex]
                Text("OTÁZKA ${questionIndex + 1} Z ${questions.size}", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            question.prompt,
                            color = Ink,
                            fontSize = 20.sp,
                            lineHeight = 29.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                question.choices.forEachIndexed { index, choice ->
                    val bg = when {
                        selectedAnswer == null -> OptionSurface
                        index == question.correctIndex -> Correct
                        index == selectedAnswer -> Incorrect
                        else -> OptionSurface
                    }
                    Card(
                        onClick = {
                            if (selectedAnswer == null) {
                                selectedAnswer = index
                                val correct = index == question.correctIndex
                                val reviewQuestion = question.toReviewQuestion(
                                    id = stableReviewId("verbs", topic.id, question.prompt, question.germanSentence, question.translation),
                                    sourceKind = "verbs",
                                    sourceTitle = topic.title
                                )
                                LearningReviewStore.recordAnswer(
                                    context = context,
                                    question = reviewQuestion,
                                    wasCorrect = correct,
                                    isMistakeReview = isErrorReview
                                )
                                if (correct) {
                                    score += 1
                                } else {
                                    wrongQuestions = wrongQuestions + question
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = bg)
                    ) {
                        Text(
                            choice,
                            modifier = Modifier.fillMaxWidth().padding(17.dp),
                            color = Ink,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
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
                                if (selectedAnswer == question.correctIndex) "Správně! Gut gemacht!" else "Správná odpověď: ${question.choices[question.correctIndex]}",
                                color = ForestText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            if (question.germanSentence.isNotBlank()) {
                                Text("Celá věta:", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(question.germanSentence, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, lineHeight = 23.sp)
                            }
                            if (question.translation.isNotBlank()) {
                                Text("Česky:", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(question.translation, color = Ink, fontSize = 15.sp, lineHeight = 22.sp)
                            }
                            Text(question.explanation, color = Ink, fontSize = 14.sp, lineHeight = 21.sp)
                        }
                    }
                    Button(
                        onClick = {
                            if (questionIndex < questions.lastIndex) {
                                questionIndex += 1
                                selectedAnswer = null
                            } else {
                                if (!isErrorReview) {
                                    CourseResultStore.save(context, "verbs_${topic.id}", score, questions.size)
                                    onComplete(score, questions.size)
                                }
                                finished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(if (questionIndex == questions.lastIndex) "Zobrazit výsledek" else "Další otázka", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


internal fun buildVerbQuestions(lesson: VerbPracticeLesson): List<LessonQuestion> =
    if (lesson.id.startsWith("advanced_")) createAdvancedVerbQuestions(lesson)
    else createVerbQuestions(lesson)

private fun createAdvancedVerbQuestions(lesson: VerbPracticeLesson): List<LessonQuestion> {
    val definitions = lesson.verbIds.mapNotNull { id -> verbDefinitions.find { it.id == id } }
    val result = mutableListOf<LessonQuestion>()

    definitions.forEach { verb ->
        verb.phrases.take(5).forEachIndexed { phraseIndex, phrase ->
            val subjectIndex = phraseIndex % verb.subjects.size
            val subject = verb.subjects[subjectIndex]
            val czechSubject = verb.czechSubjects[subjectIndex]
            val correctForm = verb.forms[subjectIndex]
            val correctLabel = "$correctForm (${verb.infinitive})"
            val distractorVerbs = definitions.filter { it.id != verb.id }.shuffled().take(3)
            val choices = (listOf(verb) + distractorVerbs)
                .map { candidate -> "${candidate.forms[subjectIndex]} (${candidate.infinitive})" }
                .shuffled()
            val correctIndex = choices.indexOf(correctLabel)
            val germanSentence = "$subject $correctForm ${phrase.german.trimEnd('.')}."
            val czechSentence = "$czechSubject ${verb.czechForms[subjectIndex]} ${phrase.czech.trimEnd('.')}."

            result.add(
                LessonQuestion(
                    prompt = "Vyber sloveso a jeho správný tvar podle českého významu:\n„$czechSubject ${verb.czechForms[subjectIndex]} ${phrase.czech.trimEnd('.')}“\n\n$subject ___ ${phrase.german.trimEnd('.')}.",
                    choices = choices,
                    correctIndex = correctIndex,
                    explanation = "„${verb.infinitive}“ znamená „${verb.czech}“. U osoby „$subject“ je správný tvar „$correctForm“.",
                    germanSentence = germanSentence,
                    translation = czechSentence
                )
            )
        }
    }
    return result.shuffled().take(25)
}

private fun createVerbQuestions(lesson: VerbPracticeLesson): List<LessonQuestion> {
    val definitions = lesson.verbIds.mapNotNull { id -> verbDefinitions.find { it.id == id } }
    val result = mutableListOf<LessonQuestion>()
    definitions.forEach { verb ->
        verb.phrases.take(5).forEachIndexed { index, phrase ->
            val subjectIndex = index.coerceAtMost(verb.subjects.lastIndex)
            val subject = verb.subjects[subjectIndex]
            val czechSubject = verb.czechSubjects[subjectIndex]
            val correctForm = verb.forms[subjectIndex]
            val choices = (listOf(correctForm) + verb.forms.filter { it != correctForm }.distinct().take(2)).shuffled()
            val correctIndex = choices.indexOf(correctForm)
            val germanSentence = "$subject $correctForm ${phrase.german.replaceFirstChar { it.lowercase() }}."
            val czechSentence = "$czechSubject ${verb.czechForms[subjectIndex]} ${phrase.czech.trimEnd('.')}."
            result.add(
                LessonQuestion(
                    prompt = "Doplň správný tvar slovesa „${verb.infinitive}“ (${verb.czech}): $subject ___ ${phrase.german}.",
                    choices = choices,
                    correctIndex = correctIndex,
                    explanation = "Sloveso „${verb.infinitive}“ znamená „${verb.czech}“. Po osobě „$subject“ použijeme tvar „$correctForm“.",
                    germanSentence = germanSentence,
                    translation = czechSentence
                )
            )
        }
    }
    return result.shuffled().take(25)
}

@Composable
private fun ScreenHeader(title: String, onBack: () -> Unit, onHome: () -> Unit = onBack) {
    AppScreenHeader(title = title, onBack = onBack, onHome = onHome)
}

@Composable
private fun LearningCard(
    icon: @Composable () -> Unit,
    iconBackground: Color = Mint,
    title: String,
    subtitle: String,
    detail: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(52.dp).background(iconBackground, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) { icon() }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(title, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
                Text(detail, color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Muted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
