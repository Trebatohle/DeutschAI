package com.deutschai.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun DailyStudyPlanCard(
    progress: DailyStudyPlanProgress,
    dueReviewCount: Int = 0,
    onGoalChange: (Int) -> Unit,
    onStartDrill: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fraction = (progress.learnedToday.toFloat() / progress.targetWords).coerceIn(0f, 1f)
    val message = when {
        progress.goalReachedToday ->
            "Dnešní cíl máš splněný. Skvělá práce — můžeš pokračovat vlastním tempem."
        progress.learnedToday == 0 ->
            "Začni malou dávkou nových slov. Každé projde významem, zápisem i poslechem."
        else ->
            "Ještě ${progress.remainingToday} ${czechWord(progress.remainingToday)} a dnešní cíl bude splněný."
    }
    val heroBackground = if (IsDarkTheme) Color(0xFF1C3027) else Color(0xFF173F35)
    val heroText = Color(0xFFF2F6F3)
    val heroMuted = Color(0xFFC4D4CB)
    val heroTrack = Color(0xFF456255)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(containerColor = heroBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Gold)
                    Text(
                        "TVŮJ DENNÍ PLÁN",
                        color = Color(0xFFE8C58B),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    if (progress.goalReachedToday) "SPLNĚNO" else "DNEŠNÍ CÍL",
                    color = if (progress.goalReachedToday) Color(0xFFBDE8CB) else heroMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        "Dobrý den. Co dnes zvládneme?",
                        color = heroText,
                        fontSize = 21.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        message,
                        color = heroMuted,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
                Text(
                    "${progress.learnedToday}/${progress.targetWords}",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            LinearProgressIndicator(
                progress = { fraction },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Gold,
                trackColor = heroTrack
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Denní cíl",
                    color = heroMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                dailyStudyGoalOptions.forEach { goal ->
                    val selected = progress.targetWords == goal
                    Button(
                        onClick = { onGoalChange(goal) },
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(11.dp),
                        contentPadding = PaddingValues(horizontal = 3.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selected) Gold else Color(0xFF355347),
                            contentColor = if (selected) GoldText else heroText
                        )
                    ) {
                        Text(
                            "$goal slov",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }

            Button(
                onClick = onStartDrill,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor = GoldText
                )
            ) {
                Text(
                    if (progress.goalReachedToday) "Naučit se další slova" else "Začít se učit",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                if (dueReviewCount > 0) {
                    "Opakování samostatně · $dueReviewCount ${czechReviewWord(dueReviewCount)} čeká"
                } else {
                    "Opakování se sleduje zvlášť a neubírá z cíle nových slov."
                },
                color = heroMuted,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}

private fun czechWord(count: Int): String = when {
    count == 1 -> "slovo"
    count in 2..4 -> "slova"
    else -> "slov"
}

private fun czechReviewWord(count: Int): String = when {
    count == 1 -> "položka"
    count in 2..4 -> "položky"
    else -> "položek"
}
