package com.healthku.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.data.AICoachReport
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.data.WorkoutCategory
import com.healthku.app.ui.components.AICoachCard
import com.healthku.app.ui.components.HealthScoreCard
import com.healthku.app.ui.components.SmartMetricsSection
import com.healthku.app.ui.theme.AsclepiosBg
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosSearchBg
import com.healthku.app.ui.theme.AsclepiosTextMuted
import com.healthku.app.ui.theme.AsclepiosTextPrimary
import com.healthku.app.ui.theme.AsclepiosTextSecondary
import com.healthku.app.ui.theme.SageDark
import com.healthku.app.ui.theme.SagePillGreen

@Composable
fun DashboardScreen(
    summary: DailyHealthSummary,
    aiReport: AICoachReport,
    selectedWorkout: WorkoutCategory,
    onWorkoutChange: (WorkoutCategory) -> Unit,
    onOpenLiveBp: () -> Unit,
    onOpenSleepOverview: () -> Unit,
    onSyncHealthConnect: () -> Unit,
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
        // Top Bar (matching screenshot exactly)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Profile Avatar in circle with soft border
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2DDD1))
                        .border(1.5.dp, Color(0xFFD6CEBD), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color(0xFF6B604E),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Hey, Atlet",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsclepiosTextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "👋", fontSize = 14.sp)
                    }
                    Text(
                        text = if (summary.hasHealthConnectPermission) "✦ Health Connect Active" else "✦ Perlu Izin Health Connect",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (summary.hasHealthConnectPermission) SagePillGreen else AsclepiosTextMuted
                    )
                }
            }

            // Top right `...` button
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(AsclepiosCard)
                    .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onSyncHealthConnect) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Sync",
                        tint = AsclepiosTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Search Bar (Exact pill style from screenshot)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(AsclepiosSearchBg)
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Search aklepios...",
                fontSize = 13.sp,
                color = AsclepiosTextMuted
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = AsclepiosTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 1: Health Score (Asclepios Score Card)
        HealthScoreCard(
            score = summary.readinessScore,
            statusText = summary.readinessStatus
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Section 2: Smart Health Metrics (Heart Rate, Blood Pressure, Calories, Steps)
        SmartMetricsSection(
            summary = summary,
            onOpenLiveBp = onOpenLiveBp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // AI Athletic Coach Debriefing Card
        AICoachCard(
            report = aiReport,
            currentSteps = summary.steps,
            selectedWorkout = selectedWorkout,
            onWorkoutChange = onWorkoutChange,
            onRefresh = onSyncHealthConnect
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sync Trigger
        Button(
            onClick = onSyncHealthConnect,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SageDark,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tarik Data Real dari Health Connect",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
