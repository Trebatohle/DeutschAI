package com.deutschai.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Lightbulb
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private data class BranchingDialogueExchange(
    val node: BranchingDialogueNode,
    val selected: BranchingDialogueOption
)

@Composable
internal fun BranchingDialogueSessionScreen(
    topic: BranchingDialogueTopic,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    var currentNodeId by remember(topic.id) { mutableStateOf(topic.startNodeId) }
    var exchanges by remember(topic.id) { mutableStateOf(emptyList<BranchingDialogueExchange>()) }
    var pendingNextNodeId by remember(topic.id) { mutableStateOf<String?>(null) }
    var isTransitioning by remember(topic.id) { mutableStateOf(false) }
    var completed by remember(topic.id) { mutableStateOf(false) }
    var completionRecorded by remember(topic.id) { mutableStateOf(false) }

    val currentNode = topic.nodes.firstOrNull { it.id == currentNodeId }
    val earnedPoints = exchanges.sumOf { it.selected.quality.points }
    val maximumPoints = exchanges.size * DialogueAnswerQuality.RECOMMENDED.points

    fun restart() {
        currentNodeId = topic.startNodeId
        exchanges = emptyList()
        pendingNextNodeId = null
        isTransitioning = false
        completed = false
        completionRecorded = false
    }

    fun chooseOption(optionIndex: Int) {
        val node = currentNode ?: return
        if (completed || isTransitioning || optionIndex !in node.options.indices) return
        val option = node.options[optionIndex]
        val review = buildBranchingDialogueReview(topic, node, optionIndex)
        LearningReviewStore.recordAnswer(
            context = context,
            question = review.question,
            wasCorrect = review.wasCorrect
        )
        exchanges = exchanges + BranchingDialogueExchange(node, option)
        pendingNextNodeId = option.nextNodeId
        isTransitioning = true
    }

    LaunchedEffect(isTransitioning, topic.id) {
        if (isTransitioning) {
            delay(850L)
            val nextId = pendingNextNodeId
            if (nextId == null) {
                completed = true
                if (!completionRecorded) {
                    completionRecorded = true
                    onComplete()
                }
            } else {
                currentNodeId = nextId
            }
            pendingNextNodeId = null
            isTransitioning = false
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 18.dp, top = 14.dp, end = 18.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
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
                    modifier = Modifier.heightIn(min = 48.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Box(
                    modifier = Modifier.size(42.dp).background(Forest, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("B", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Beatrice", color = ForestText, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    Text("Větvený dialog · ${topic.level} · offline", color = Muted, fontSize = 12.sp)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(topic.title, color = Ink, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                    Text(
                        if (completed) "Rozhovor dokončen" else "Průběh rozhovoru se mění podle tvých odpovědí",
                        color = Muted, fontSize = 12.sp
                    )
                }
                Button(
                    onClick = { restart() },
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(11.dp)
                ) { Text("Restart") }
            }

            LinearProgressIndicator(
                progress = {
                    if (completed) 1f
                    else (exchanges.size.toFloat() / topic.maximumSteps.coerceAtLeast(1)).coerceIn(0f, 0.92f)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(17.dp),
                colors = CardDefaults.cardColors(containerColor = Forest)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Gold)
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("BEATRICE", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text(
                            "Každá tvoje odpověď může otevřít jinou cestu. Rozhovor je připravený předem, takže funguje celý offline.",
                            color = Color.White, fontSize = 13.sp, lineHeight = 19.sp
                        )
                    }
                }
            }

            exchanges.forEach { exchange ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = if (exchange.selected.quality == DialogueAnswerQuality.NEEDS_WORK) Incorrect else Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("TY · TVOJE ODPOVĚĎ", color = if (exchange.selected.quality == DialogueAnswerQuality.NEEDS_WORK) ForestText else Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.7.sp)
                        Text(exchange.selected.german, color = if (exchange.selected.quality == DialogueAnswerQuality.NEEDS_WORK) Ink else Color.White, fontSize = 15.sp, lineHeight = 21.sp, fontWeight = FontWeight.SemiBold)
                        Text(exchange.selected.czech, color = if (exchange.selected.quality == DialogueAnswerQuality.NEEDS_WORK) Muted else Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 18.sp)
                        Text(
                            when (exchange.selected.quality) {
                                DialogueAnswerQuality.RECOMMENDED -> "Výborná volba"
                                DialogueAnswerQuality.ACCEPTABLE -> "Přirozená alternativa"
                                DialogueAnswerQuality.NEEDS_WORK -> "Tady je prostor ke zlepšení"
                            },
                            color = if (exchange.selected.quality == DialogueAnswerQuality.NEEDS_WORK) ForestText else Gold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("BEATRICE", color = ForestText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.7.sp)
                        Text(exchange.selected.replyGerman, color = Ink, fontSize = 15.sp, lineHeight = 21.sp, fontWeight = FontWeight.SemiBold)
                        Text(exchange.selected.replyCzech, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                        Text(exchange.selected.feedback, color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
                    }
                }
            }

            if (completed) {
                val mistakes = exchanges.filter { it.selected.quality == DialogueAnswerQuality.NEEDS_WORK }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text("Gespräch geschafft!", color = Gold, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "$earnedPoints z $maximumPoints bodů",
                            color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Dokončil/a jsi ${exchanges.size} kroků. ${mistakes.size} odpovědí jsme zařadili do dlouhodobého opakování.",
                            color = Color(0xFFE0EAE5), fontSize = 13.sp, lineHeight = 19.sp
                        )
                    }
                }

                Text("Co příště říct lépe", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                if (mistakes.isEmpty()) {
                    Text("Bez problematických odpovědí. Sehr gut!", color = ForestText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                } else {
                    mistakes.forEach { exchange ->
                        val recommended = exchange.node.options.first { it.quality == DialogueAnswerQuality.RECOMMENDED }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Incorrect)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(exchange.node.promptGerman, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Tvoje odpověď: ${exchange.selected.german}", color = Ink, fontSize = 13.sp)
                                Text("Lepší formulace: ${recommended.german}", color = ForestText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(recommended.czech, color = Muted, fontSize = 13.sp)
                                Text(exchange.selected.feedback, color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
                                GermanAudioControls(player = speaker, text = recommended.german)
                            }
                        }
                    }
                }

                Text("Užitečné fráze z rozhovoru", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                exchanges.forEach { exchange ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(modifier = Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(exchange.selected.german, color = Ink, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp)
                            Text(exchange.selected.czech, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
                            GermanAudioControls(player = speaker, text = exchange.selected.german)
                            Text(exchange.selected.replyGerman, color = ForestText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp)
                            Text(exchange.selected.replyCzech, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
                            GermanAudioControls(player = speaker, text = exchange.selected.replyGerman)
                        }
                    }
                }

                Button(
                    onClick = { restart() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                    shape = RoundedCornerShape(13.dp)
                ) { Text("Zkusit rozhovor znovu", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(13.dp)
                ) { Text("Vybrat jiné téma", fontWeight = FontWeight.Bold) }
            } else if (isTransitioning) {
                Text("Beatrice právě reaguje…", color = ForestText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            } else if (currentNode != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(19.dp),
                    colors = CardDefaults.cardColors(containerColor = Mint)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("BEATRICE SE PTÁ", color = ForestText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text(currentNode.promptGerman, color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold, lineHeight = 25.sp)
                        Text(currentNode.promptCzech, color = ForestText, fontSize = 14.sp, lineHeight = 20.sp)
                    }
                }
                Text("Jak odpovíš?", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                currentNode.options.forEachIndexed { index, option ->
                    Card(
                        onClick = { chooseOption(index) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(11.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier.size(30.dp).background(Mint, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text((index + 1).toString(), color = ForestText, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(option.german, color = Ink, fontSize = 15.sp, lineHeight = 21.sp, fontWeight = FontWeight.SemiBold)
                                Text(option.czech, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                                Text("Použít tuto odpověď", color = ForestText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Incorrect)
                ) {
                    Text(
                        "Tento krok se nepodařilo načíst. Vrať se prosím k výběru dialogů.",
                        modifier = Modifier.padding(15.dp),
                        color = Ink, fontSize = 14.sp
                    )
                }
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Zpět k tématům") }
            }
        }
    }
}
