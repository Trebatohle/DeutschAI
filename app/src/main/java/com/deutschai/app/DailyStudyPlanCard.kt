package com.deutschai.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek

@Composable
internal fun DailyStudyPlanCard(
    progress: DailyStudyPlanProgress,
    dueReviewCount: Int = 0,
    onGoalChange: (Int) -> Unit,
    onStartDrill: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fraction = (progress.learnedToday.toFloat() / progress.targetWords).coerceIn(0f, 1f)
    val message = if (progress.goalReachedToday) {
        "Denní cíl splněn. Skvělá práce! Můžeš pokračovat a naučit se další slova."
    } else if (progress.learnedToday == 0) {
        "Začni dnešní dávkou. Každé slovo se započítá až po zvládnutí významu, zápisu i poslechu."
    } else {
        "Ještě ${progress.remainingToday} ${czechWord(progress.remainingToday)} a dnešní cíl bude splněný."
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Gold)
                    Text(
                        "TVŮJ DENNÍ STUDIJNÍ PLÁN",
                        color = ForestText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
                Text(
                    if (progress.goalReachedToday) "SPLNĚNO" else "DNEŠNÍ CÍL",
                    color = if (progress.goalReachedToday) ForestText else Muted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        "Každý den o kousek dál.",
                        color = Ink,
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Nová slova, která opravdu zvládneš.",
                        color = Muted,
                        fontSize = 12.sp
                    )
                }
                Text(
                    "${progress.learnedToday}/${progress.targetWords}",
                    color = ForestText,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            LinearProgressIndicator(
                progress = { fraction },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = ForestText,
                trackColor = Mint
            )
            Text(message, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
            Text(
                if (dueReviewCount > 0) {
                    "K samostatnému opakování: $dueReviewCount položek. Tyto položky se do cíle nových slov nezapočítávají."
                } else {
                    "Opakování se sleduje zvlášť a neubírá z cíle nových slov."
                },
                color = Muted,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )

            Text("Denní cíl", color = Ink, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dailyStudyGoalOptions.forEach { goal ->
                    val selected = progress.targetWords == goal
                    Button(
                        onClick = { onGoalChange(goal) },
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selected) ActionColor else Mint,
                            contentColor = if (selected) ActionText else ForestText
                        )
                    ) {
                        Text("$goal slov", fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TENTO TÝDEN", color = ForestText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.7.sp)
                Text("${progress.learnedThisWeek} nových slov", color = Ink, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                progress.week.forEach { day ->
                    val dayFraction = (day.newWords.toFloat() / progress.targetWords).coerceIn(0f, 1f)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            day.newWords.toString(),
                            color = if (day.newWords >= progress.targetWords) ForestText else Muted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Box(
                            modifier = Modifier.width(24.dp).height(34.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Mint),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth().fillMaxHeight(dayFraction)
                                    .background(ForestText)
                            )
                        }
                        Text(dayLabel(day.date.dayOfWeek), color = Muted, fontSize = 10.sp)
                    }
                }
            }

            Button(
                onClick = onStartDrill,
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ActionColor,
                    contentColor = ActionText
                )
            ) {
                Text(
                    if (progress.goalReachedToday) "Naučit se další slova" else "Pokračovat v denním plánu",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun czechWord(count: Int): String = when {
    count == 1 -> "slovo"
    count in 2..4 -> "slova"
    else -> "slov"
}

private fun dayLabel(day: DayOfWeek): String = when (day) {
    DayOfWeek.MONDAY -> "Po"
    DayOfWeek.TUESDAY -> "Út"
    DayOfWeek.WEDNESDAY -> "St"
    DayOfWeek.THURSDAY -> "Čt"
    DayOfWeek.FRIDAY -> "Pá"
    DayOfWeek.SATURDAY -> "So"
    DayOfWeek.SUNDAY -> "Ne"
}
