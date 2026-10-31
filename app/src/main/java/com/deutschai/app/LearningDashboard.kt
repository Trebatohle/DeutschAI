package com.deutschai.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun LearningOverviewScreen(
    onBack: () -> Unit,
    onPlacementTest: () -> Unit
) {
    val context = LocalContext.current
    val skillStats = LearningReviewStore.skillStats(context)
    val totalAnswered = LearningReviewStore.totalAnswered(context)
    val masteredCount = LearningReviewStore.masteredCount(context)
    val dueCount = LearningReviewStore.pendingCount(context)
    val placement = PlacementAssessmentStore.load(context)
    val weakest = skillStats.filter { it.attempts >= 5 }.minByOrNull { it.accuracyPercent }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    androidx.compose.material3.Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text("Přehled znalostí", color = Muted, fontSize = 13.sp)
                }
            }

            Text("Jak na tom skutečně jsi?", color = Ink, fontSize = 26.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold)
            Text(
                "Přehled vychází z odpovědí v jednotlivých cvičeních a z posledního rozřazovacího testu. Po několika pokusech se začne zobrazovat spolehlivější obrázek tvých silných a slabších oblastí.",
                color = Muted, fontSize = 14.sp, lineHeight = 21.sp
            )

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Forest)) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("CELKOVÁ STUDIJNÍ DATA", color = Color(0xFFE0EAE5), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(totalAnswered.toString(), color = Gold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            Text("zodpovězených otázek", color = Color.White, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(masteredCount.toString(), color = Gold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            Text("dlouhodobě upevněných", color = Color.White, fontSize = 11.sp)
                        }
                    }
                    Text("$dueCount položek čeká na opakování", color = Color(0xFFE0EAE5), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            if (placement == null) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(19.dp), colors = CardDefaults.cardColors(containerColor = Mint)) {
                    Column(modifier = Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            androidx.compose.material3.Icon(Icons.Default.Assessment, contentDescription = null, tint = ForestText)
                            Text("Zatím neznáme tvoji výchozí úroveň", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Text("Krátký offline test A1–B2 ověří praktické věty a čtení s porozuměním a doporučí vhodnou úroveň.", color = ForestText, fontSize = 13.sp, lineHeight = 19.sp)
                        Button(
                            onClick = onPlacementTest,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = RoundedCornerShape(13.dp)
                        ) { Text("Spustit vstupní test", fontWeight = FontWeight.Bold) }
                    }
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(19.dp), colors = CardDefaults.cardColors(containerColor = Mint)) {
                    Column(modifier = Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text("POSLEDNÍ ROZŘAZOVACÍ TEST", color = ForestText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.7.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Doporučená úroveň", color = ForestText, fontSize = 14.sp)
                            Text(placement.estimatedLevel, color = ForestText, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("${placement.correctAnswers} z ${placement.totalQuestions} správně", color = ForestText, fontSize = 13.sp)
                        placement.skillResults.forEach { (skill, result) ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(skill, color = ForestText, fontSize = 12.sp)
                                Text("${result.accuracyPercent} %", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            LinearProgressIndicator(progress = { result.accuracyPercent / 100f }, modifier = Modifier.fillMaxWidth())
                        }
                        Button(
                            onClick = onPlacementTest,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                            shape = RoundedCornerShape(13.dp)
                        ) { Text("Zopakovat rozřazovací test", fontWeight = FontWeight.Bold) }
                    }
                }
            }

            Text("Znalosti podle oblastí", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            if (skillStats.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = CardSurface)) {
                    Text(
                        "Zatím nemáme dost odpovědí pro vyhodnocení jednotlivých oblastí. Začni některou lekcí nebo vstupním testem; průběžné výsledky se budou ukládat přímo v telefonu.",
                        modifier = Modifier.padding(16.dp), color = Muted, fontSize = 13.sp, lineHeight = 20.sp
                    )
                }
            } else {
                skillStats.forEach { stat ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = CardSurface)) {
                        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(stat.title, color = Ink, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("${stat.accuracyPercent} %", color = ForestText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                            LinearProgressIndicator(progress = { stat.accuracyPercent / 100f }, modifier = Modifier.fillMaxWidth())
                            Text("${stat.correct} správně z ${stat.attempts} odpovědí · ${stat.trackedItems} sledovaných položek", color = Muted, fontSize = 11.sp)
                            Text(
                                when {
                                    stat.attempts < 5 -> "Zatím sbíráme další výsledky."
                                    stat.accuracyPercent >= 85 -> "Silná oblast – udržuj znalosti pravidelným opakováním."
                                    stat.accuracyPercent >= 65 -> "Dobré základy – pomůže další procvičování."
                                    else -> "Doporučená oblast k procvičení."
                                },
                                color = Muted, fontSize = 12.sp, lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            if (weakest != null) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Mint)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                            androidx.compose.material3.Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ForestText)
                            Text("Doporučení Beatrice", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Text(
                            "Nejvíce prostoru ke zlepšení je nyní v oblasti „${weakest.title}“ (${weakest.accuracyPercent} %). Zařaď ji do příštího denního tréninku.",
                            color = ForestText, fontSize = 13.sp, lineHeight = 19.sp
                        )
                    }
                }
            }

            Text(
                "Úspěšnost se počítá z odpovědí, které aplikace zaznamenala od této aktualizace. Nejde o oficiální jazykovou zkoušku ani certifikované CEFR hodnocení.",
                color = Muted, fontSize = 11.sp, lineHeight = 17.sp
            )
        }
    }
}
