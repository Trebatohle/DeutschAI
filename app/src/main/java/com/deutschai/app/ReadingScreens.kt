package com.deutschai.app

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlin.math.roundToInt
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val readingLevels = listOf("A1", "A2", "B1", "B2", "C1", "C2")

@Composable
internal fun ReadingLibraryScreen(
    onBack: () -> Unit,
    onSelectArticle: (ReadingArticle) -> Unit
) {
    var selectedLevel by remember { mutableStateOf("A1") }
    val articles = readingArticles.filter { it.level == selectedLevel }
    val context = LocalContext.current
    var translationModelReady by remember { mutableStateOf(false) }
    var translationModelStatus by remember { mutableStateOf("Při prvním použití připravíme offline překladový slovník.") }
    LaunchedEffect(Unit) {
        OfflineWordTranslator.prepare(context.applicationContext) { ready ->
            translationModelReady = ready
            translationModelStatus = if (ready) {
                "Offline překladový model je připraven. Neznámá slova lze překládat bez internetu."
            } else {
                "Pro automatický překlad neznámých slov je potřeba stáhnout model přes Wi-Fi (přibližně 30 MB). Potom překlady fungují přímo v telefonu."
            }
        }
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
            ReadingHeader("Čtení v němčině", onBack)
            Text("Vyber úroveň", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text(
                "Všechny příběhy jsou pouze v němčině. Když podržíš prst na slovu, zobrazí se jeho český význam; po puštění zmizí.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (translationModelReady) Mint else CardSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        if (translationModelReady) "Offline překlady připraveny" else "Příprava offline překladů",
                        color = ForestText, fontWeight = FontWeight.Bold, fontSize = 14.sp
                    )
                    Text(translationModelStatus, color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                readingLevels.forEach { level ->
                    Button(
                        onClick = { selectedLevel = level },
                        modifier = Modifier.weight(1f).heightIn(min = 44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedLevel == level) ActionColor else Mint,
                            contentColor = if (selectedLevel == level) ActionText else ForestText
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text(level, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Mint)
            ) {
                Row(
                    modifier = Modifier.padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.TouchApp, contentDescription = null, tint = ForestText, modifier = Modifier.size(26.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Slovníček pod prstem", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(
                            "Překlady jsou dostupné offline. U méně běžného slova, které ještě ve slovníčku není, to aplikace výslovně oznámí.",
                            color = ForestText,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
            Text(
                "Úroveň $selectedLevel · ${articles.size} příběhy",
                color = ForestText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            articles.forEachIndexed { index, article ->
                Card(
                    onClick = { onSelectArticle(article) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(17.dp),
                        horizontalArrangement = Arrangement.spacedBy(13.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp).background(Mint, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text((index + 1).toString(), color = ForestText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(article.title, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)
                            Text(article.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                            Text(
                                article.wordCount.toString() + " slov · " + article.level,
                                color = ForestText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            if (articles.any { it.wordCount < 1000 }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0D8))
                ) {
                    Text(
                        "Některé příběhy v této sadě ještě nedosahují cílové délky 1 000 slov.",
                        modifier = Modifier.padding(13.dp),
                        color = Ink,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
internal fun ReadingArticleScreen(
    article: ReadingArticle,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    var textLayout by remember(article.id) { mutableStateOf<TextLayoutResult?>(null) }
    var pressedMeaning by remember(article.id) { mutableStateOf<Pair<String, String>?>(null) }
    var activePressedWord by remember(article.id) { mutableStateOf<String?>(null) }
    var pressToken by remember(article.id) { mutableStateOf(0) }
    var popupOffset by remember(article.id) { mutableStateOf(IntOffset.Zero) }
    val comprehensionQuestions = remember(article.id) { buildReadingComprehensionQuestions(article) }
    var comprehensionStarted by remember(article.id) { mutableStateOf(false) }
    var comprehensionIndex by remember(article.id) { mutableStateOf(0) }
    var comprehensionSelected by remember(article.id) { mutableStateOf<Int?>(null) }
    var comprehensionAnswers by remember(article.id) { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var comprehensionScore by remember(article.id) { mutableStateOf(0) }
    var comprehensionFinished by remember(article.id) { mutableStateOf(false) }
    val currentComprehensionQuestion = comprehensionQuestions.getOrNull(comprehensionIndex)

    fun restartComprehension() {
        comprehensionStarted = true
        comprehensionIndex = 0
        comprehensionSelected = null
        comprehensionAnswers = emptyMap()
        comprehensionScore = 0
        comprehensionFinished = false
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            ReadingHeader(article.level + " · Čtení", onBack)
            Text(article.title, color = Ink, fontSize = 27.sp, lineHeight = 33.sp, fontWeight = FontWeight.Bold)
            Text(article.subtitle, color = Muted, fontSize = 14.sp, lineHeight = 20.sp)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.MenuBook, contentDescription = null, tint = ForestText, modifier = Modifier.size(19.dp))
                Text(
                    article.wordCount.toString() + " slov · " + article.level,
                    color = ForestText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface)
            ) {
                Column(modifier = Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Lies auf Deutsch", color = ForestText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text(
                        "Čti příběh pouze v němčině. Podrž prst přímo na slovu; jeho překlad se objeví nad ním a po puštění zmizí.",
                        color = Muted,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }

            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val popupWidth = 260.dp
                val popupWidthPx = with(density) { popupWidth.toPx() }
                val textInsetPx = with(density) { 18.dp.toPx() }
                val popupLiftPx = with(density) { 78.dp.toPx() }
                val containerWidthPx = with(density) { maxWidth.toPx() }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Text(
                        text = article.body,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                            .pointerInput(article.id, article.body) {
                                detectTapGestures(
                                    onPress = {
                                        try {
                                            awaitRelease()
                                        } finally {
                                            pressToken += 1
                                            activePressedWord = null
                                            pressedMeaning = null
                                        }
                                    },
                                    onLongPress = { offset ->
                                        val body = article.body
                                        val layout = textLayout
                                        if (layout != null && body.isNotEmpty()) {
                                            var at = layout.getOffsetForPosition(offset).coerceIn(0, body.lastIndex)
                                            if (!body[at].isLetterOrDigit()) {
                                                val left = at - 1
                                                if (left >= 0 && body[left].isLetterOrDigit()) at = left
                                                else return@detectTapGestures
                                            }
                                            var wordStart = at
                                            var wordEnd = at + 1
                                            while (wordStart > 0 && body[wordStart - 1].isLetterOrDigit()) wordStart--
                                            while (wordEnd < body.length && body[wordEnd].isLetterOrDigit()) wordEnd++
                                            val word = body.substring(wordStart, wordEnd)
                                            val bounds = layout.getBoundingBox(wordStart)
                                            val newToken = pressToken + 1
                                            pressToken = newToken
                                            activePressedWord = word
                                            val maxX = (containerWidthPx - popupWidthPx).roundToInt().coerceAtLeast(0)
                                            val x = (textInsetPx + bounds.center.x - popupWidthPx / 2f)
                                                .roundToInt().coerceIn(0, maxX)
                                            val y = (textInsetPx + bounds.top - popupLiftPx).roundToInt()
                                            popupOffset = IntOffset(x, y)

                                            val knownMeaning = lookupReadingWordTranslation(article, word)
                                            pressedMeaning = word to (knownMeaning ?: "Připravuji offline překlad…")
                                            if (knownMeaning == null) {
                                                OfflineWordTranslator.translate(context.applicationContext, word) { translated ->
                                                    if (pressToken == newToken && activePressedWord == word) {
                                                        pressedMeaning = word to (
                                                            translated ?: "Automatický překlad není právě dostupný. Otevři knihovnu čtení na Wi-Fi, aby se model stáhl nebo znovu připravil."
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )
                            },
                        onTextLayout = { textLayout = it },
                        color = Ink,
                        fontSize = 16.sp,
                        lineHeight = 27.sp
                    )
                }

                if (pressedMeaning != null) {
                    Popup(
                        alignment = Alignment.TopStart,
                        offset = popupOffset,
                        properties = PopupProperties(
                            focusable = false,
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false,
                            clippingEnabled = false
                        )
                    ) {
                        Card(
                            modifier = Modifier.width(popupWidth),
                            shape = RoundedCornerShape(15.dp),
                            colors = CardDefaults.cardColors(containerColor = Mint),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 13.dp, vertical = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    pressedMeaning!!.first,
                                    color = ForestText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    pressedMeaning!!.second,
                                    color = Ink,
                                    fontSize = 15.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }
                }
            }

            if (pressedMeaning == null) {
                Text(
                    "Tip: podrž prst na slovu. Běžné významy se načítají z vestavěného slovníčku; ostatní se překládají modelem staženým jednou a běžícím přímo v telefonu.",
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }

            if (!comprehensionStarted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Mint)
                ) {
                    Column(modifier = Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text("Ověř si porozumění", color = ForestText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Na konci článku tě čeká 5 otázek v němčině. U každé vyber informaci, která se v příběhu skutečně objevila. Výsledek ti ukáže, kolik důležitých detailů sis zapamatoval/a.",
                            color = ForestText, fontSize = 13.sp, lineHeight = 19.sp
                        )
                        Button(
                            onClick = { restartComprehension() },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = RoundedCornerShape(13.dp)
                        ) { Text("Ověřit porozumění · 5 otázek", fontWeight = FontWeight.Bold) }
                    }
                }
            } else if (comprehensionFinished) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(modifier = Modifier.padding(19.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Přehled porozumění", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "$comprehensionScore z ${comprehensionQuestions.size} správně",
                            color = Gold, fontSize = 28.sp, fontWeight = FontWeight.Bold
                        )
                        Text(
                            when {
                                comprehensionScore == 5 -> "Výborně! Zachytil/a jsi všechny ověřované informace z příběhu."
                                comprehensionScore >= 4 -> "Velmi dobré porozumění. Většinu důležitých informací sis zapamatoval/a."
                                comprehensionScore >= 3 -> "Základnímu ději rozumíš. Některé detaily doporučujeme ještě jednou přečíst."
                                else -> "Zkus si text projít znovu. Zaměř se na konkrétní události a detaily v jednotlivých odstavcích."
                            },
                            color = Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 19.sp
                        )
                    }
                }
                Text("Co se ti podařilo", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                comprehensionQuestions.forEach { item ->
                    val correct = comprehensionAnswers[item.id] == item.correctIndex
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Text(if (correct) "✓" else "•", color = if (correct) ForestText else Muted, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(item.facet, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text(if (correct) "Informaci sis vybavil/a správně." else "Tuto informaci si ještě zopakuj.", color = Muted, fontSize = 12.sp)
                        }
                    }
                }
                Button(
                    onClick = { restartComprehension() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                    shape = RoundedCornerShape(13.dp)
                ) { Text("Zopakovat otázky", fontWeight = FontWeight.Bold) }
            } else if (currentComprehensionQuestion != null) {
                Text(
                    "POROZUMĚNÍ TEXTU · OTÁZKA ${comprehensionIndex + 1} Z ${comprehensionQuestions.size}",
                    color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.6.sp
                )
                LinearProgressIndicator(
                    progress = { (comprehensionIndex + 1).toFloat() / comprehensionQuestions.size },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(currentComprehensionQuestion.facet, color = Muted, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Text(
                        currentComprehensionQuestion.prompt,
                        modifier = Modifier.padding(17.dp),
                        color = Ink, fontSize = 17.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold
                    )
                }
                currentComprehensionQuestion.choices.forEachIndexed { choiceIndex, choice ->
                    val background = when {
                        comprehensionSelected == null -> OptionSurface
                        choiceIndex == currentComprehensionQuestion.correctIndex -> Correct
                        choiceIndex == comprehensionSelected -> Incorrect
                        else -> OptionSurface
                    }
                    Card(
                        onClick = {
                            if (comprehensionSelected == null) {
                                comprehensionSelected = choiceIndex
                                comprehensionAnswers = comprehensionAnswers + (currentComprehensionQuestion.id to choiceIndex)
                                val correct = choiceIndex == currentComprehensionQuestion.correctIndex
                                if (correct) comprehensionScore += 1
                                LearningReviewStore.recordAnswer(
                                    context = context,
                                    question = ReviewQuestion(
                                        id = currentComprehensionQuestion.id,
                                        sourceKind = "reading",
                                        sourceTitle = article.title,
                                        prompt = currentComprehensionQuestion.prompt,
                                        choices = currentComprehensionQuestion.choices,
                                        correctIndex = currentComprehensionQuestion.correctIndex,
                                        explanation = currentComprehensionQuestion.explanation
                                    ),
                                    wasCorrect = correct
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(13.dp),
                        colors = CardDefaults.cardColors(containerColor = background)
                    ) {
                        Text(choice, modifier = Modifier.fillMaxWidth().padding(15.dp), color = Ink, fontSize = 14.sp, lineHeight = 20.sp)
                    }
                }
                if (comprehensionSelected != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = if (comprehensionSelected == currentComprehensionQuestion.correctIndex) Correct else Incorrect)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(
                                if (comprehensionSelected == currentComprehensionQuestion.correctIndex) "Správně! Tuto informaci jsi v textu našel/našla." else "Správná odpověď:",
                                color = ForestText, fontSize = 14.sp, fontWeight = FontWeight.Bold
                            )
                            if (comprehensionSelected != currentComprehensionQuestion.correctIndex) {
                                Text(currentComprehensionQuestion.choices[currentComprehensionQuestion.correctIndex], color = Ink, fontSize = 13.sp, lineHeight = 19.sp)
                            }
                            Text(currentComprehensionQuestion.explanation, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
                        }
                    }
                    Button(
                        onClick = {
                            if (comprehensionIndex < comprehensionQuestions.lastIndex) {
                                comprehensionIndex += 1
                                comprehensionSelected = null
                            } else {
                                comprehensionFinished = true
                                CourseResultStore.save(context, "reading_${article.id}", comprehensionScore, comprehensionQuestions.size)
                                StudyHabitStore.recordSession(context)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(13.dp)
                    ) {
                        Text(if (comprehensionIndex < comprehensionQuestions.lastIndex) "Další otázka" else "Zobrazit přehled", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Zpět na knihovnu", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ReadingHeader(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.heightIn(min = 50.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
            Text("Zpět")
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("DEUTSCHAI", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
            Text(title, color = Muted, fontSize = 13.sp)
        }
    }
}
