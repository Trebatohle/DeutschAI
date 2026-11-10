package com.deutschai.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun AppScreenHeader(
    title: String,
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(40.dp).background(Mint, RoundedCornerShape(13.dp))
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Zpět", tint = ForestText)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                "DEUTSCHAI",
                color = ForestText,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 1.8.sp
            )
            Text(
                title,
                color = Ink,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
        IconButton(
            onClick = onHome,
            modifier = Modifier.size(40.dp).background(Mint, RoundedCornerShape(13.dp))
        ) {
            Icon(Icons.Default.Home, contentDescription = "Domů", tint = ForestText)
        }
    }
}
