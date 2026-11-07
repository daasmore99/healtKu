package com.healthku.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosTextMuted
import com.healthku.app.ui.theme.AsclepiosTextPrimary
import com.healthku.app.ui.theme.AsclepiosTextSecondary
import com.healthku.app.ui.theme.SagePillBg
import com.healthku.app.ui.theme.SagePillGreen

@Composable
fun AsclepiosHeroCard(
    score: Int?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Health Score",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = AsclepiosTextPrimary
            )
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = null,
                tint = AsclepiosTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text(
                        text = "Asclepios Score",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsclepiosTextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Based on your data, we think your health status is above average",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = AsclepiosTextSecondary
                    )
                }

                // Right Column: Vertical Selector (87, 88, 89)
                val displayScore = score ?: 88
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "${displayScore - 1}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AsclepiosTextMuted
                    )

                    Box(
                        modifier = Modifier
                            .size(width = 56.dp, height = 48.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(SagePillBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$displayScore",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = SagePillGreen
                        )
                    }

                    Text(
                        text = "${displayScore + 1}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AsclepiosTextMuted
                    )
                }
            }
        }
    }
}
