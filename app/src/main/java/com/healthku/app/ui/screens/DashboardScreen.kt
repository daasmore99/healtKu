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
import com.healthku.app.ui.components.CalendarStrip
import com.healthku.app.ui.components.HealthScoreCard
import com.healthku.app.ui.components.SmartMetricsSection
import com.healthku.app.ui.theme.BgCream
import com.healthku.app.ui.theme.BorderSubtle
import com.healthku.app.ui.theme.CardWhite
import com.healthku.app.ui.theme.SageGreenDark
import com.healthku.app.ui.theme.SageGreenMedium
import com.healthku.app.ui.theme.TextMuted
import com.healthku.app.ui.theme.TextPrimary
import com.healthku.app.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    summary: DailyHealthSummary,
    aiReport: AICoachReport,
    selectedWorkout: WorkoutCategory,
    onWorkoutChange: (WorkoutCategory) -> Unit,
    onSyncHealthConnect: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgCream)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top App Bar / Profile (Asclepios Style)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Profile Avatar / Badge
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE4EFE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = "Avatar",
                        tint = SageGreenDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Hey, Atlet",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "👋", fontSize = 14.sp)
                    }
                    Text(
                        text = if (summary.isSyncedWithHealthConnect) "GloryFit Pro Connected" else "Mode Mandiri (Health Connect)",
                        fontSize = 12.sp,
                        color = if (summary.isSyncedWithHealthConnect) SageGreenMedium else TextMuted
                    )
                }
            }

            // Sync Button
            IconButton(
                onClick = onSyncHealthConnect,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF0ECE1))
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = "Sync",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar mockup from the screenshot
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardWhite)
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cari metrik, log tidur, atau latihan...",
                fontSize = 13.sp,
                color = TextMuted
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekly Calendar Strip
        CalendarStrip()

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Health Score Card (Asclepios Rating)
        HealthScoreCard(
            score = summary.readinessScore,
            statusText = summary.readinessStatus
        )

        Spacer(modifier = Modifier.height(16.dp))

        // AI Athletic Coach (Tegas + Saintifik)
        AICoachCard(
            report = aiReport,
            currentSteps = summary.steps,
            selectedWorkout = selectedWorkout,
            onWorkoutChange = onWorkoutChange,
            onRefresh = onSyncHealthConnect
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Smart Health Metrics (Heart Rate, Blood Pressure, Sleep, Walking, Calories)
        SmartMetricsSection(summary = summary)

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom Health Connect Sync Trigger Button
        Button(
            onClick = onSyncHealthConnect,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SageGreenDark,
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
                text = "Sinkronkan Smartwatch & Health Connect",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
