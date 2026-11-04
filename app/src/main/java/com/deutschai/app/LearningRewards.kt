package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal const val LICENSE_DISCOUNT_TARGET_POINTS = 15
internal const val LICENSE_DISCOUNT_PERCENT = 15

internal data class RewardPointsProgress(val earnedPoints: Int) {
    val displayedPoints: Int
        get() = earnedPoints.coerceIn(0, LICENSE_DISCOUNT_TARGET_POINTS)

    val progressFraction: Float
        get() = displayedPoints.toFloat() / LICENSE_DISCOUNT_TARGET_POINTS

    val pointsRemaining: Int
        get() = (LICENSE_DISCOUNT_TARGET_POINTS - earnedPoints).coerceAtLeast(0)

    val discountUnlocked: Boolean
        get() = earnedPoints >= LICENSE_DISCOUNT_TARGET_POINTS
}

internal fun qualifiesForRewardPoint(score: Int, total: Int): Boolean =
    total > 0 && score == total

internal fun addRewardedTest(existingTestIds: Set<String>, testId: String): Set<String> =
    if (testId.isBlank()) existingTestIds else existingTestIds + testId

internal object RewardPointsStore {
    private const val PREFS = "deutschai_reward_points"
    private const val KEY_REWARDED_TEST_IDS = "perfect_test_ids"

    fun load(context: Context): RewardPointsProgress {
        val ids = context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getStringSet(KEY_REWARDED_TEST_IDS, emptySet())
            .orEmpty()
        return RewardPointsProgress(ids.size)
    }

    /**
     * A unique test awards at most one point, and only for a perfect result.
     * Results and awarded test identifiers remain on the device.
     */
    fun recordTestResult(
        context: Context,
        testId: String,
        score: Int,
        total: Int
    ): RewardPointsProgress {
        val preferences = context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val existingIds = preferences
            .getStringSet(KEY_REWARDED_TEST_IDS, emptySet())
            .orEmpty()
        if (!qualifiesForRewardPoint(score, total) || testId.isBlank()) {
            return RewardPointsProgress(existingIds.size)
        }

        val updatedIds = addRewardedTest(existingIds, testId)
        if (updatedIds.size != existingIds.size) {
            preferences.edit()
                .putStringSet(KEY_REWARDED_TEST_IDS, updatedIds)
                .apply()
        }
        return RewardPointsProgress(updatedIds.size)
    }
}

@Composable
internal fun RewardPointsCard(
    progress: RewardPointsProgress,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 13.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                Box(
                    modifier = Modifier.size(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        "Body za úspěšné testy",
                        color = Ink,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "100 % v testu = 1 bod",
                        color = Muted,
                        fontSize = 11.sp
                    )
                }
                Text(
                    "${progress.displayedPoints}/$LICENSE_DISCOUNT_TARGET_POINTS",
                    color = ForestText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            LinearProgressIndicator(
                progress = { progress.progressFraction },
                modifier = Modifier.fillMaxWidth().height(7.dp),
                color = ActionColor,
                trackColor = Mint
            )

            Text(
                text = if (progress.discountUnlocked) {
                    "Odměna dosažena: $LICENSE_DISCOUNT_PERCENT % sleva na licenci."
                } else {
                    "Ještě ${progress.pointsRemaining} ${czechPointWord(progress.pointsRemaining)} do $LICENSE_DISCOUNT_PERCENT % slevy na licenci."
                },
                color = if (progress.discountUnlocked) ForestText else Muted,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                fontWeight = if (progress.discountUnlocked) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

private fun czechPointWord(count: Int): String = when {
    count == 1 -> "bod"
    count in 2..4 -> "body"
    else -> "bodů"
}
