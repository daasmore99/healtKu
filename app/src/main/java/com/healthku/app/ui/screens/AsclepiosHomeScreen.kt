package com.healthku.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.R
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.ui.components.AsclepiosHeroCard
import com.healthku.app.ui.components.AsclepiosMetricCards
import com.healthku.app.ui.theme.AsclepiosBg
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosTextMuted
import com.healthku.app.ui.theme.AsclepiosTextPrimary
import com.healthku.app.ui.theme.AsclepiosTextSecondary
import com.healthku.app.ui.theme.SagePillBg
import com.healthku.app.ui.theme.SagePillGreen

@Composable
fun AsclepiosHomeScreen(
    summary: DailyHealthSummary,
    onOpenLiveBp: () -> Unit,
    onSyncHealthConnect: () -> Unit,
    onOpenHealthConnectSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AsclepiosBg)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Bar: Profile avatar + Greeting + Menu button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Profile Avatar Image
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEBE5D8))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_user),
                        contentDescription = "User Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Hey, Michelle 👋",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsclepiosTextPrimary
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (summary.hasHealthConnectPermission) "✦ Health Connect Aktif" else "✦ + 88% | Pro Member",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (summary.hasHealthConnectPermission) SagePillGreen else AsclepiosTextSecondary
                        )
                    }
                }
            }

            // More Options Button
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AsclepiosCard)
                    .border(1.dp, AsclepiosCardBorder, CircleShape)
                    .clickable { onSyncHealthConnect() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "Options",
                    tint = AsclepiosTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Search Pill Bar (matching screenshot "Search aklepios...")
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
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Search aklepios...",
                    fontSize = 14.sp,
                    color = AsclepiosTextMuted
                )

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = AsclepiosTextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Health Connect Permissions Banner if not connected
        if (!summary.hasHealthConnectPermission) {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenHealthConnectSettings() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SagePillBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hubungkan ke Health Connect",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SagePillGreen
                        )
                        Text(
                            text = "Ketuk untuk izinkan sinkronisasi sensor Smartwatch",
                            fontSize = 11.sp,
                            color = AsclepiosTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SagePillGreen)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Buka Izin",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Card: Asclepios Score
        AsclepiosHeroCard(score = summary.readinessScore)

        Spacer(modifier = Modifier.height(18.dp))

        // 2-Column Grid & Secondary Metric Cards
        AsclepiosMetricCards(
            summary = summary,
            onOpenLiveBp = onOpenLiveBp
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}
