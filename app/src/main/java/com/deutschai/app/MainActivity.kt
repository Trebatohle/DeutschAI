package com.deutschai.app

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

private val firstLesson = listOf(
    LessonQuestion(
        prompt = "Jak se německy řekne „Dobrý den“?",
        choices = listOf("Gute Nacht", "Guten Tag", "Auf Wiedersehen", "Danke"),
        correctIndex = 1,
        explanation = "„Guten Tag“ znamená „Dobrý den“. Použiješ ho při běžném denním pozdravu."
    ),
    LessonQuestion(
        prompt = "Doplň větu: Ich ___ Erik.",
        choices = listOf("bist", "ist", "bin", "sind"),
        correctIndex = 2,
        explanation = "Sloveso „sein“ (být): ich bin, du bist, er/sie/es ist. Proto říkáme „Ich bin Erik“."
    ),
    LessonQuestion(
        prompt = "Co znamená německé „Danke“?",
        choices = listOf("Prosím", "Promiňte", "Na shledanou", "Děkuji"),
        correctIndex = 3,
        explanation = "„Danke“ znamená „Děkuji“. Odpovědět můžeš například „Bitte“ — „Prosím / Není zač“."
    ),
    LessonQuestion(
        prompt = "Vyber správný člen: ___ Haus (dům)",
        choices = listOf("der", "die", "das", "den"),
        correctIndex = 2,
        explanation = "Správně je „das Haus“. Německá podstatná jména se učíme společně s členem: das Haus, die Tür, der Tisch."
    ),
    LessonQuestion(
        prompt = "Co znamená „Wie geht es dir?“",
        choices = listOf("Jak se máš?", "Kde bydlíš?", "Kolik je hodin?", "Jak se jmenuješ?"),
        correctIndex = 0,
        explanation = "„Wie geht es dir?“ znamená „Jak se máš?“. Neformálně můžeš odpovědět „Gut, danke“ — „Dobře, děkuji“."
    )
)

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
    var showLesson by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Učení") }

    if (showLesson) {
        LessonScreen(onBack = { showLesson = false })
        return
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Cream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 24.dp),
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
                        onClick = { showLesson = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gold,
                            contentColor = Ink
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Začít dnešní lekci", fontWeight = FontWeight.Bold)
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
                    title = "Dnešní cíl",
                    value = "5 otázek",
                    detail = "Krátká lekce"
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
                subtitle = "První A1 lekce s vysvětlením v češtině",
                onClick = { showLesson = true }
            )
            FeatureCard(
                icon = { Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Forest) },
                title = "Konverzace s Beatrice",
                subtitle = "Procvičuj běžné situace a uč se z chyb",
                onClick = { selectedTab = "Konverzace" }
            )
            FeatureCard(
                icon = { Icon(Icons.Default.School, contentDescription = null, tint = Forest) },
                title = "Moje pokroky",
                subtitle = "Postupně budovaný přehled tvého učení",
                onClick = { selectedTab = "Pokroky" }
            )

            if (selectedTab != "Učení") {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Mint),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = if (selectedTab == "Konverzace")
                            "Konverzaci s Beatrice připravíme v další fázi. První lekce už funguje offline."
                        else
                            "Historii výsledků a přehled pokroku doplníme v další fázi.",
                        color = Forest,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Medium
                    )
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
private fun LessonScreen(onBack: () -> Unit) {
    var questionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    fun restartLesson() {
        questionIndex = 0
        selectedAnswer = null
        score = 0
        finished = false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Cream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Mint,
                        contentColor = Forest
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "DEUTSCHAI",
                        color = Forest,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 2.sp
                    )
                    Text("Lekce A1 · Základy", color = Muted, fontSize = 13.sp)
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
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Gold,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Lekce dokončena!",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$score z ${firstLesson.size} správně",
                            color = Gold,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = when {
                                score == firstLesson.size -> "Výborně! Všech pět odpovědí je správně."
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
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold,
                        contentColor = Ink
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Zopakovat lekci", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Mint,
                        contentColor = Forest
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Zpět na hlavní obrazovku", fontWeight = FontWeight.Bold)
                }
            } else {
                val question = firstLesson[questionIndex]

                Text(
                    text = "PRVNÍ KROKY V NĚMČINĚ",
                    color = Muted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.3.sp
                )
                Text(
                    text = "Otázka ${questionIndex + 1} z ${firstLesson.size}",
                    color = Forest,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(firstLesson.size) { index ->
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
                                    fontWeight = if (selectedAnswer != null && index == question.correctIndex)
                                        FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 15.dp)
                                )
                            }
                        }
                    }
                }

                if (selectedAnswer != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedAnswer == question.correctIndex) Correct else Incorrect
                        )
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
                            Text(
                                text = question.explanation,
                                color = Ink,
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )
                        }
                    }
                    Button(
                        onClick = {
                            if (questionIndex < firstLesson.lastIndex) {
                                questionIndex += 1
                                selectedAnswer = null
                            } else {
                                finished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Forest,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (questionIndex == firstLesson.lastIndex) "Zobrazit výsledek" else "Další otázka",
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
                modifier = Modifier
                    .size(48.dp)
                    .background(Mint, RoundedCornerShape(15.dp)),
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
