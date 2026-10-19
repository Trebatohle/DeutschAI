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
    var selectedTab by remember { mutableStateOf("Učení") }
    var showLessonMessage by remember { mutableStateOf(false) }

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
                        onClick = { showLessonMessage = !showLessonMessage },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gold,
                            contentColor = Ink
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Začít dnešní lekci", fontWeight = FontWeight.Bold)
                    }
                    if (showLessonMessage) {
                        Text(
                            text = "Brzy tu na tebe bude čekat první lekce A1. Tohle je zatím návrh úvodní obrazovky.",
                            color = Color.White,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
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
                    value = "10 min",
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
                subtitle = "Užitečná němčina s vysvětlením v češtině",
                onClick = { selectedTab = "Lekce" }
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
                        text = "Vybráno: $selectedTab. Tuto část doplníme v další fázi.",
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
