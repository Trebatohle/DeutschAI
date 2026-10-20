package com.deutschai.app

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.pointerinput.pointerInput
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookOpen
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                readingLevels.forEach { level ->
                    Button(
                        onClick = { selectedLevel = level },
                        modifier = Modifier.weight(1f).heightIn(min = 44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedLevel == level) Forest else Mint,
                            contentColor = if (selectedLevel == level) Color.White else Forest
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
                    Icon(Icons.Default.TouchApp, contentDescription = null, tint = Forest, modifier = Modifier.size(26.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Slovníček pod prstem", color = Forest, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(
                            "Překlady jsou dostupné offline. U méně běžného slova, které ještě ve slovníčku není, to aplikace výslovně oznámí.",
                            color = Forest,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
            Text(
                "Úroveň $selectedLevel · ${articles.size} příběhy",
                color = Forest,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            articles.forEachIndexed { index, article ->
                Card(
                    onClick = { onSelectArticle(article) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                            Text((index + 1).toString(), color = Forest, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(article.title, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)
                            Text(article.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                            Text(
                                article.wordCount.toString() + " slov · " + article.level,
                                color = Forest,
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
    var textLayout by remember(article.id) { mutableStateOf<TextLayoutResult?>(null) }
    var pressedMeaning by remember(article.id) { mutableStateOf<Pair<String, String>?>(null) }

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
                Icon(Icons.Default.BookOpen, contentDescription = null, tint = Forest, modifier = Modifier.size(19.dp))
                Text(
                    article.wordCount.toString() + " slov · " + article.level,
                    color = Forest,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Lies auf Deutsch", color = Forest, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text(
                        "Čti příběh pouze v němčině. Podrž prst přímo na slovu, kterému nerozumíš; překlad se ukáže, dokud prst nepustíš.",
                        color = Muted,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
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
                                        var start = at
                                        var end = at + 1
                                        while (start > 0 && body[start - 1].isLetterOrDigit()) start--
                                        while (end < body.length && body[end].isLetterOrDigit()) end++
                                        val word = body.substring(start, end)
                                        pressedMeaning = word to translateReadingWord(article, word)
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(17.dp),
                    colors = CardDefaults.cardColors(containerColor = Mint)
                ) {
                    Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(pressedMeaning!!.first, color = Forest, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(pressedMeaning!!.second, color = Ink, fontSize = 15.sp, lineHeight = 21.sp)
                        Text("Překlad se skryje po puštění prstu.", color = Forest, fontSize = 11.sp)
                    }
                }
            } else {
                Text(
                    "Tip: podrž slovo přímo v textu, dokud se neobjeví překlad.",
                    color = Muted,
                    fontSize = 12.sp
                )
            }
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = Color.White),
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
            colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.heightIn(min = 50.dp)
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
