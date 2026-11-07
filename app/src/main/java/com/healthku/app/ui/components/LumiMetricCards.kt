package com.healthku.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.ui.theme.LumiAmber
import com.healthku.app.ui.theme.LumiCardBorderDark
import com.healthku.app.ui.theme.LumiCardDark
import com.healthku.app.ui.theme.LumiCardSurfaceHover
import com.healthku.app.ui.theme.LumiCoral
import com.healthku.app.ui.theme.LumiPeriwinkle
import com.healthku.app.ui.theme.LumiSage
import com.healthku.app.ui.theme.LumiTextMuted
import com.healthku.app.ui.theme.LumiTextPrimary
import com.healthku.app.ui.theme.LumiTextSecondary

@Composable
fun LumiBentoGrid(
    summary: DailyHealthSummary,
    onOpenSleep: () -> Unit,
    onOpenVitals: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Row 1: Sleep Architecture (Large 1.4x) & Recovery Index
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Sleep Modular Card
            LumiSleepModularCard(
                sleepHours = summary.sleepHours,
                deepSleepHours = summary.deepSleepHours,
                onClick = onOpenSleep,
                modifier = Modifier.weight(1.15f)
            )

            // Heart Rate & Rhythm Card
            LumiHeartModularCard(
                bpm = summary.heartRateBpm,
                restingBpm = summary.restingHeartRateBpm,
                history = summary.heartRateHistory,
                onClick = onOpenVitals,
                modifier = Modifier.weight(0.85f)
            )
        }

        // Row 2: Movement/Steps (Wide modular) & Blood Pressure / SpO2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Movement Activity Card
            LumiMovementCard(
                steps = summary.steps,
                goal = summary.stepGoal,
                distanceKm = summary.distanceKm,
                calories = summary.caloriesBurned,
                modifier = Modifier.weight(1.1f)
            )

            // Vitals Card (BP & Oxygen)
            LumiVitalsSummaryCard(
                sys = summary.bloodPressureSys,
                dia = summary.bloodPressureDia,
                oxygen = summary.oxygenSaturationPercent,
                onClick = onOpenVitals,
                modifier = Modifier.weight(0.9f)
            )
        }
    }
}

@Composable
fun LumiSleepModularCard(
    sleepHours: Float,
    deepSleepHours: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, LumiCardBorderDark, RoundedCornerShape(26.dp)),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = LumiCardDark),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x227D8EC2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = LumiPeriwinkle,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x187D8EC2))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (sleepHours >= 7f) "Well Rested" else if (sleepHours > 0f) "Rest Needed" else "No Data",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LumiPeriwinkle
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Sleep Rest",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = LumiTextSecondary
            )

            Text(
                text = if (sleepHours > 0f) "${String.format("%.1f", sleepHours)}h" else "--",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = LumiTextPrimary,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Animated Deep Sleep Bar
            val targetProgress = if (sleepHours > 0f) (deepSleepHours / sleepHours).coerceIn(0.1f, 1f) else 0f
            val animatedProgress by animateFloatAsState(
                targetValue = targetProgress,
                animationSpec = tween(900),
                label = "SleepBarAnim"
            )

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = LumiPeriwinkle,
                trackColor = Color(0xFF1E212D),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (deepSleepHours > 0f) "Deep: ${String.format("%.1f", deepSleepHours)}h (${(targetProgress * 100).toInt()}%)" else "Pending Health Connect sync",
                fontSize = 11.sp,
                color = LumiTextMuted
            )
        }
    }
}

@Composable
fun LumiHeartModularCard(
    bpm: Int?,
    restingBpm: Int?,
    history: List<Float>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, LumiCardBorderDark, RoundedCornerShape(26.dp)),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = LumiCardDark),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0x22F16751)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = LumiCoral,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Heart Rate",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = LumiTextSecondary
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = if (bpm != null) "$bpm" else "--",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = LumiTextPrimary,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "bpm",
                    fontSize = 12.sp,
                    color = LumiTextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Smooth Sparkline Wave
            WaveSparkline(
                data = if (history.isNotEmpty()) history else listOf(65f, 72f, 68f, 74f, 70f),
                lineColor = LumiCoral,
                width = 72.dp,
                height = 24.dp
            )
        }
    }
}

@Composable
fun LumiMovementCard(
    steps: Int,
    goal: Int,
    distanceKm: Float,
    calories: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, LumiCardBorderDark, RoundedCornerShape(26.dp)),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = LumiCardDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x22E2A55E)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsWalk,
                        contentDescription = null,
                        tint = LumiAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "${distanceKm} km",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LumiAmber
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Movement",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = LumiTextSecondary
            )

            Text(
                text = "$steps",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = LumiTextPrimary,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            val progress = (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(900),
                label = "StepsBarAnim"
            )

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = LumiAmber,
                trackColor = Color(0xFF262118),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${calories} kcal burned",
                fontSize = 11.sp,
                color = LumiTextMuted
            )
        }
    }
}

@Composable
fun LumiVitalsSummaryCard(
    sys: Int?,
    dia: Int?,
    oxygen: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, LumiCardBorderDark, RoundedCornerShape(26.dp)),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = LumiCardDark),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0x225E8C4A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = LumiSage,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Vitals & SpO2",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = LumiTextSecondary
            )

            Text(
                text = if (sys != null) "$sys/$dia" else if (oxygen != null) "$oxygen%" else "--",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = LumiTextPrimary,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x185E8C4A))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (oxygen != null) "Oxygen $oxygen%" else "Tap for details",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = LumiSage
                )
            }
        }
    }
}
