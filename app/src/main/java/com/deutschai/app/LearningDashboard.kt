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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun CefrKnowledgeLevelCard(
    modifier: Modifier = Modifier,
    knowledge: CefrLevelKnowledge
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    knowledge.level,
                    color = ForestText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    knowledge.accuracyPercent?.let { "$it %" } ?: "—",
                    color = if (knowledge.accuracyPercent == null) Muted else Ink,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (knowledge.accuracyPercent != null) {
                LinearProgressIndicator(
                    progress = { (knowledge.accuracyPercent / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth(),
                    color = ForestText,
                    trackColor = Mint
                )
            } else {
                Text("Bez výsledků", color = Muted, fontSize = 11.sp)
            }
            Text(
                knowledge.statusLabel,
                color = when {
                    knowledge.accuracyPercent == null -> Muted
                    knowledge.answeredQuestions < 5 -> Muted
                    knowledge.accuracyPercent >= 85 -> ForestText
                    knowledge.accuracyPercent < 65 -> Color(0xFFB56A45)
                    else -> Ink
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )
            Text(
                if (knowledge.accuracyPercent == null) {
                    "Zatím neověřeno"
                } else {
                    "${knowledge.correctAnswers}/${knowledge.answeredQuestions} · ${knowledge.sourceLabel}"
                },
                color = Muted,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun SentenceCourseProgressCard(
    progress: SentenceCourseLearningProgress,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(progress.title, modifier = Modifier.weight(1f), color = Ink, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(progress.statusLabel, color = if (progress.isCompleted) ForestText else Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Text(progress.subtitle, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
            LinearProgressIndicator(
                progress = { progress.progressPercent / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = ForestText,
                trackColor = Mint
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (progress.isCompleted) "Celý kurz dokončen" else "Zodpovězeno ${progress.answeredQuestions}/${progress.totalQuestions} různých otázek",
                    color = Muted,
                    fontSize = 11.sp
                )
                progress.latestResult?.let { result ->
                    Text("Výsledek ${result.score}/${result.total}", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
@Composable
internal fun LearningOverviewScreen(
    onBack: () -> Unit,
    onPlacementTest: () -> Unit,
    onHome: () -> Unit = onBack,
    onStartTraining: () -> Unit,
    onOpenCourse: (String) -> Unit
) {
    val context = LocalContext.current
    val placement = PlacementAssessmentStore.load(context)
    val allReviewEntries = remember(context) { LearningReviewStore.all(context) }
    val skillStats = LearningReviewStore.skillStats(context)
    val cefrMap = buildCefrKnowledgeMap(allReviewEntries, placement)
    val courseResults = remember(context) {
        sentenceCourseCatalog.associate { course ->
            course.id to CourseResultStore.load(context, "sentence_${course.id}")
        }
    }
    val courseProgress = remember(allReviewEntries, courseResults) {
        buildSentenceCourseLearningProgress(sentenceCourseCatalog, allReviewEntries, courseResults)
    }
    var selectedCourseLevel by remember(placement?.assessedAtMillis) {
        mutableStateOf(placement?.estimatedLevel?.takeIf { it in listOf("A1", "A2", "B1", "B2") } ?: "A1")
    }
    val totalAnswered = LearningReviewStore.totalAnswered(context)
    val masteredCount = LearningReviewStore.masteredCount(context)
    val dueCount = LearningReviewStore.pendingCount(context)
    val recurringMistakes = LearningReviewStore.recurringMistakes(context, limit = 5)
    val frequencyLevelStats = listOf("A1", "A2", "B1", "B2").map { level ->
        level to buildFrequencyVocabularyProgress(
            frequencyVocabularyForLevel(level),
            allReviewEntries
        )
    }
    val frequencyPassedTotal = frequencyLevelStats.sumOf { it.second.successfullyPassedWords }
    val frequencyPracticedTotal = frequencyLevelStats.sumOf { it.second.practicedWords }
    val frequencyMasteredTotal = frequencyLevelStats.sumOf { it.second.longTermMasteredWords }
    val frequencyWordTotal = frequencyLevelStats.sumOf { it.second.totalWords }
    val frequencyDueTotal = frequencyLevelStats.sumOf { it.second.dueWords }
    val weakest = skillStats.filter { it.attempts >= 5 }.minByOrNull { it.accuracyPercent }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AppScreenHeader(title = "Přehled znalostí", onBack = onBack, onHome = onHome)

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

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(19.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface)
            ) {
                Column(
                    modifier = Modifier.padding(17.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Text("NEJPOUŽÍVANĚJŠÍ SLOVNÍ ZÁSOBA", color = ForestText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(frequencyPassedTotal.toString() + " / " + frequencyWordTotal, color = Ink, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                            Text("slov úspěšně zvládnutých na 100 % alespoň jednou", color = Muted, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(frequencyMasteredTotal.toString(), color = ForestText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("dlouhodobě upevněno", color = Muted, fontSize = 10.sp)
                            Text(frequencyDueTotal.toString() + " k opakování", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    frequencyLevelStats.forEach { stat ->
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(stat.first, color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(stat.second.successfullyPassedWords.toString() + " / " + stat.second.totalWords, color = Muted, fontSize = 11.sp)
                            }
                            LinearProgressIndicator(
                                progress = {
                                    if (stat.second.totalWords == 0) 0f
                                    else stat.second.successfullyPassedWords.toFloat() / stat.second.totalWords
                                },
                                modifier = Modifier.fillMaxWidth(),
                                color = ForestText,
                                trackColor = Mint
                            )
                            Text(
                                "Procvičeno " + stat.second.practicedWords +
                                    " · dlouhodobě upevněno " + stat.second.longTermMasteredWords,
                                color = Muted,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Text(
                        "100 % znamená zvládnutí všech tří částí Drilu. Dlouhodobé upevnění se počítá po třech úspěšných opakováních.",
                        color = Muted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            if (placement == null) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(19.dp), colors = CardDefaults.cardColors(containerColor = Mint)) {
                    Column(modifier = Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            androidx.compose.material3.Icon(Icons.Default.BarChart, contentDescription = null, tint = ForestText)
                            Text("Zatím neznáme tvoji výchozí úroveň", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Text("24 krátkých offline otázek ověří úroveň A1–C2 a doporučí vhodný začátek.", color = ForestText, fontSize = 13.sp, lineHeight = 19.sp)
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

            Text("Mapa znalostí A1–C2", color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(
                placement?.let { "Výchozí úroveň podle posledního rozřazovacího testu: ${it.estimatedLevel}. Jednotlivé úrovně se dále zpřesňují podle výsledků kurzů, poslechu a četby." }
                    ?: "Postupně si zde vytvoříš přehled toho, jak zvládáš jednotlivé úrovně. Výsledky se počítají z reálných odpovědí v aplikaci.",
                color = Muted, fontSize = 13.sp, lineHeight = 19.sp
            )
            cefrMap.chunked(2).forEach { rowLevels ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    rowLevels.forEach { level ->
                        CefrKnowledgeLevelCard(
                            modifier = Modifier.weight(1f),
                            knowledge = level
                        )
                    }
                    if (rowLevels.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Text(
                "Procenta vyjadřují úspěšnost zaznamenaných odpovědí, nikoli oficiální CEFR certifikaci. Úrovně bez dostatku dat zůstávají označené jako neověřené.",
                color = Muted, fontSize = 11.sp, lineHeight = 17.sp
            )

            Text("Postup v jednotlivých kurzech", color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(
                "U každé úrovně uvidíš, které tematické kurzy už máš dokončené, které máš rozpracované a kde můžeš pokračovat. Klepnutím na kurz se rovnou vrátíš k jeho procvičování.",
                color = Muted, fontSize = 13.sp, lineHeight = 19.sp
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                listOf("A1", "A2", "B1", "B2").forEach { level ->
                    val selected = selectedCourseLevel == level
                    Card(
                        onClick = { selectedCourseLevel = level },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = if (selected) Forest else CardSurface)
                    ) {
                        Text(
                            level,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            color = if (selected) Color.White else ForestText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
            val visibleCourses = courseProgress.filter { it.level == selectedCourseLevel }
            val completedCourses = visibleCourses.count { it.isCompleted }
            val startedCourses = visibleCourses.count { !it.isCompleted && it.answeredQuestions > 0 }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "$completedCourses/${visibleCourses.size} kurzů dokončeno",
                    color = ForestText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("$startedCourses rozpracováno", color = Muted, fontSize = 12.sp)
            }
            if (visibleCourses.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(17.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Text(
                        "Pro tuto úroveň zatím nejsou v knihovně tematické kurzy.",
                        modifier = Modifier.padding(15.dp),
                        color = Muted,
                        fontSize = 13.sp
                    )
                }
            } else {
                visibleCourses.forEach { course ->
                    SentenceCourseProgressCard(
                        progress = course,
                        onClick = { onOpenCourse(course.courseId) }
                    )
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

            Text("Opakující se chyby", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                "Tady se zobrazují položky, u kterých se chyba opakovala. Jsou zahrnuté také v dlouhodobém opakování a denní trénink jim dává přednost.",
                color = Muted, fontSize = 13.sp, lineHeight = 19.sp
            )
            if (recurringMistakes.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(17.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Text(
                        "Zatím nemáme zaznamenanou opakující se chybu. Jakmile se některá položka objeví jako problém opakovaně, zobrazí se zde.",
                        modifier = Modifier.padding(16.dp),
                        color = Muted, fontSize = 13.sp, lineHeight = 20.sp
                    )
                }
            } else {
                recurringMistakes.forEach { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(17.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(15.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    entry.question.sourceTitle,
                                    color = ForestText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Chyby: ${entry.wrongCount}×",
                                    color = Muted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                entry.question.prompt,
                                color = Ink,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 20.sp
                            )
                            if (entry.question.germanSentence.isNotBlank()) {
                                Text(
                                    entry.question.germanSentence,
                                    color = Ink,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }
                            val correctAnswer = entry.question.choices.getOrNull(entry.question.correctIndex)
                            if (!correctAnswer.isNullOrBlank()) {
                                Text(
                                    "Správná odpověď: $correctAnswer",
                                    color = ForestText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            if (entry.question.translation.isNotBlank()) {
                                Text(entry.question.translation, color = Muted, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            if (recurringMistakes.isNotEmpty() || dueCount > 0) {
                Button(
                    onClick = onStartTraining,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Text("Procvičit doporučené položky", fontWeight = FontWeight.Bold)
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
