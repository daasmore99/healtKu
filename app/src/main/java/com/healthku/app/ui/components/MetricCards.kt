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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.ui.theme.AmberSleep
import com.healthku.app.ui.theme.BorderSubtle
import com.healthku.app.ui.theme.CardWhite
import com.healthku.app.ui.theme.CoralHeart
import com.healthku.app.ui.theme.PeachCalories
import com.healthku.app.ui.theme.SageGreenMedium
import com.healthku.app.ui.theme.TextMuted
import com.healthku.app.ui.theme.TextPrimary
import com.healthku.app.ui.theme.TextSecondary
import com.healthku.app.ui.theme.VioletBloodPressure

@Composable
fun SmartMetricsSection(
    summary: DailyHealthSummary,
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
                text = "Smart Health Metrics",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "GloryFit Pro",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = SageGreenMedium
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Grid Row 1: Heart Rate & Blood Pressure/Stress
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCardCompact(
                modifier = Modifier.weight(1f),
                title = "Heart Rate",
                value = "${summary.heartRateBpm} bpm",
                subtext = "Resting: ${summary.restingHeartRateBpm} bpm",
                icon = Icons.Default.Favorite,
                iconTint = CoralHeart,
                iconBg = Color(0xFFFDEEEC),
                sparklineData = summary.heartRateHistory,
                sparklineColor = CoralHeart
            )

            MetricCardCompact(
                modifier = Modifier.weight(1f),
                title = "Blood Pressure",
                value = "${summary.bloodPressureSys}/${summary.bloodPressureDia}",
                subtext = "Optimal mmHg",
                icon = Icons.Default.MonitorHeart,
                iconTint = VioletBloodPressure,
                iconBg = Color(0xFFF4EFF8),
                sparklineData = summary.bloodPressureHistory,
                sparklineColor = VioletBloodPressure
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Detailed Metric List Cards (Sleep, Walking, Calories)
        DetailedMetricCard(
            title = "Sleep Duration & Stages",
            value = "${summary.sleepHours} hours",
            subtext = "Deep Sleep: ${String.format("%.1f", summary.deepSleepHours)}h (${(summary.deepSleepHours/summary.sleepHours * 100).toInt()}%)",
            icon = Icons.Default.Bedtime,
            iconTint = AmberSleep,
            iconBg = Color(0xFFFBF4E8),
            sparklineData = summary.sleepHistory,
            sparklineColor = AmberSleep
        )

        Spacer(modifier = Modifier.height(10.dp))

        DetailedMetricCard(
            title = "Walking & Activity",
            value = "${summary.distanceKm} kilometer",
            subtext = "${summary.steps} / ${summary.stepGoal} langkah",
            icon = Icons.Default.DirectionsWalk,
            iconTint = SageGreenMedium,
            iconBg = Color(0xFFEBF3EE),
            sparklineData = listOf(3.2f, 4.5f, 5.0f, 4.2f, summary.distanceKm),
            sparklineColor = SageGreenMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        DetailedMetricCard(
            title = "Active Calories",
            value = "${summary.caloriesBurned} kcal",
            subtext = "Target pembakaran terpenuhi",
            icon = Icons.Default.LocalFireDepartment,
            iconTint = PeachCalories,
            iconBg = Color(0xFFFCF0EB),
            sparklineData = listOf(1400f, 1650f, 1500f, 1720f, summary.caloriesBurned.toFloat()),
            sparklineColor = PeachCalories
        )
    }
}

@Composable
fun MetricCardCompact(
    title: String,
    value: String,
    subtext: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    sparklineData: List<Float>,
    sparklineColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }

                WaveSparkline(
                    data = sparklineData,
                    lineColor = sparklineColor,
                    width = 46.dp,
                    height = 24.dp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )

            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtext,
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
fun DetailedMetricCard(
    title: String,
    value: String,
    subtext: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    sparklineData: List<Float>,
    sparklineColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                    Text(
                        text = value,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtext,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            WaveSparkline(
                data = sparklineData,
                lineColor = sparklineColor,
                width = 54.dp,
                height = 26.dp
            )
        }
    }
}
