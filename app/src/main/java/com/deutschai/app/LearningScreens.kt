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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun LearningHubScreen(
    onBack: () -> Unit,
    onCourses: () -> Unit,
    onArticles: () -> Unit,
    onVerbs: () -> Unit,
    onDialogues: () -> Unit,
    onReading: () -> Unit
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
            ScreenHeader("Knihovna učení", onBack)
            Text("Co chceš dnes procvičit?", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text(
                "Vyber hlavní kategorii a potom konkrétní téma. Každé cvičení funguje offline.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
            LearningCard(
                icon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = Forest) },
                title = "Kurzy A1",
                subtitle = "Základy, sloveso sein a smíšené procvičování. Každý kurz má 25 otázek.",
                detail = "3 kurzy",
                onClick = onCourses
            )
            LearningCard(
                icon = { Icon(Icons.Default.Book, contentDescription = null, tint = Forest) },
                title = "Členy a podstatná jména",
                subtitle = "Vyber si slovní téma: bydlení, jídlo, práce, cestování a další.",
                detail = articleCategories.size.toString() + " tematických sad · 25 slov v každé",
                onClick = onArticles
            )
            LearningCard(
                icon = { Icon(Icons.Default.School, contentDescription = null, tint = Forest) },
                title = "Důležitá slovesa",
                subtitle = "Procvič si časování ve větách, ne jen izolované tvary.",
                detail = verbPracticeTopics.size.toString() + " sloves · 25 otázek na každé",
                onClick = onVerbs
            )
            LearningCard(
                icon = { Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Forest) },
                title = "Řízené dialogy",
                subtitle = "Tři doporučené odpovědi, vždy s českým překladem a navazující replikou.",
                detail = guidedDialogueTopics.size.toString() + " dialogy v první sadě",
                onClick = onDialogues
            )
            LearningCard(
                icon = { Icon(Icons.Default.Book, contentDescription = null, tint = Forest) },
                title = "Čtení v němčině",
                subtitle = "Dlouhé příběhy podle úrovně A1–C2; podrž slovo a zobrazíš český význam.",
                detail = readingArticles.size.toString() + " příběhů · A1 až C2",
                onClick = onReading
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
                    Text("Postupně rozšiřovaná knihovna", color = Forest, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        "Aplikace je nyní postavená na jasných scénářích a strukturovaných otázkách místo volné AI konverzace. Další dialogy a slovní sady budeme přidávat jako ověřený obsah.",
                        color = Forest,
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
    onSelectCategory: (ArticleCategory) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ScreenHeader("Členy a slovíčka", onBack)
            Text("Vyber slovní téma", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text(
                "V každé sadě je 25 podstatných jmen. Vyber správný člen der, die nebo das a uč se zároveň český význam.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
            articleCategories.forEachIndexed { index, category ->
                Card(
                    onClick = { onSelectCategory(category) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                            Text((index + 1).toString(), color = Forest, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(category.title, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Text(category.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                            Text("25 otázek · A1", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
    onSelectTopic: (VerbPracticeLesson) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ScreenHeader("Důležitá slovesa", onBack)
            Text("Procvič časování", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text(
                "Každé sloveso procvičíš v 25 různých větách. Před výběrem odpovědi si větu přečti i česky v hlavě.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
            verbPracticeTopics.forEachIndexed { index, topic ->
                Card(
                    onClick = { onSelectTopic(topic) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                            Icon(Icons.Default.Verified, contentDescription = null, tint = Forest)
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(topic.title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                            Text(topic.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                            val verbNames = topic.verbIds.mapNotNull { id -> verbDefinitions.find { it.id == id }?.infinitive }
                            Text(verbNames.joinToString(" · "), color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("25 otázek · 5 sloves v jedné sadě", color = Forest, fontSize = 12.sp)
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
    onComplete: (Int) -> Unit
) {
    var words by remember(category.id) { mutableStateOf(category.words.shuffled()) }
    var questionIndex by remember(category.id) { mutableStateOf(0) }
    var selectedArticle by remember(category.id) { mutableStateOf<String?>(null) }
    var score by remember(category.id) { mutableStateOf(0) }
    var finished by remember(category.id) { mutableStateOf(false) }
    val choices = listOf("der", "die", "das")
    val total = words.size.coerceAtMost(25)

    fun restart() {
        words = category.words.shuffled()
        questionIndex = 0
        selectedArticle = null
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
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            ScreenHeader(category.title, onBack)
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
                        Text("Sada dokončena", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("$score z $total správně", color = Gold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "U každého slova si zapamatuj celý tvar se členem, například „der Tisch“.",
                            color = Color(0xFFE0EAE5),
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
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
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Vybrat jiné téma", fontWeight = FontWeight.Bold) }
            } else {
                val word = words[questionIndex]
                Text("OTÁZKA ${questionIndex + 1} Z $total", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / total.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Doplň správný člen", color = Ink, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("___ ${word.word}", color = Forest, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(word.czech, color = Muted, fontSize = 17.sp)
                        Text("Vyber: der, die nebo das.", color = Muted, fontSize = 13.sp)
                    }
                }
                choices.forEach { article ->
                    val bg = when {
                        selectedArticle == null -> Color.White
                        article == word.article -> Correct
                        article == selectedArticle -> Incorrect
                        else -> Color.White
                    }
                    Card(
                        onClick = {
                            if (selectedArticle == null) {
                                selectedArticle = article
                                if (article == word.article) score += 1
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
                                color = Forest,
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
                                onComplete(score)
                                finished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = Color.White),
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
    onComplete: (Int) -> Unit
) {
    var questions by remember(topic.id) { mutableStateOf(createVerbQuestions(topic).shuffled()) }
    var questionIndex by remember(topic.id) { mutableStateOf(0) }
    var selectedAnswer by remember(topic.id) { mutableStateOf<Int?>(null) }
    var score by remember(topic.id) { mutableStateOf(0) }
    var finished by remember(topic.id) { mutableStateOf(false) }

    fun restart() {
        questions = createVerbQuestions(topic).shuffled()
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
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            ScreenHeader(topic.title, onBack)
            Text(topic.subtitle, color = Muted, fontSize = 14.sp, lineHeight = 20.sp)
            Text(
                "Slovesa: " + topic.verbIds.mapNotNull { id -> verbDefinitions.find { it.id == id }?.infinitive }.joinToString(" · "),
                color = Forest,
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
                        Text("Slovesná sada dokončena", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                        Text("$score z ${questions.size} správně", color = Gold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Procvičil/a jsi pět důležitých sloves v různých větách. Říkej si nahlas celé německé věty i jejich český význam.",
                            color = Color(0xFFE0EAE5),
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
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
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Vybrat jinou sadu", fontWeight = FontWeight.Bold) }
            } else {
                val question = questions[questionIndex]
                Text("OTÁZKA ${questionIndex + 1} Z ${questions.size}", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                        selectedAnswer == null -> Color.White
                        index == question.correctIndex -> Correct
                        index == selectedAnswer -> Incorrect
                        else -> Color.White
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
                                color = Forest,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            if (question.germanSentence.isNotBlank()) {
                                Text("Celá věta:", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(question.germanSentence, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, lineHeight = 23.sp)
                            }
                            if (question.translation.isNotBlank()) {
                                Text("Česky:", color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                                onComplete(score)
                                finished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(if (questionIndex == questions.lastIndex) "Zobrazit výsledek" else "Další otázka", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
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
private fun ScreenHeader(title: String, onBack: () -> Unit) {
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
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("DEUTSCHAI", color = Forest, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
            Text(title, color = Muted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun LearningCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    detail: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(17.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(Mint, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) { icon() }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                Text(detail, color = Forest, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
