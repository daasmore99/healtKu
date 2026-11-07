package com.healthku.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
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
import com.healthku.app.ui.components.LumiBentoGrid
import com.healthku.app.ui.components.LumiOrb
import com.healthku.app.ui.theme.LumiAmber
import com.healthku.app.ui.theme.LumiCanvasDark
import com.healthku.app.ui.theme.LumiCapsule
import com.healthku.app.ui.theme.LumiCardBorderDark
import com.healthku.app.ui.theme.LumiCardDark
import com.healthku.app.ui.theme.LumiCoral
import com.healthku.app.ui.theme.LumiPeriwinkle
import com.healthku.app.ui.theme.LumiSage
import com.healthku.app.ui.theme.LumiTextMuted
import com.healthku.app.ui.theme.LumiTextPrimary
import com.healthku.app.ui.theme.LumiTextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LumiHomeScreen(
    summary: DailyHealthSummary,
    aiReport: AICoachReport,
    selectedWorkout: WorkoutCategory,
    onWorkoutChange: (WorkoutCategory) -> Unit,
    onOpenChat: () -> Unit,
    onOpenSleep: () -> Unit,
    onOpenVitals: () -> Unit,
    onSyncHealthConnect: () -> Unit,
    onOpenHealthConnectSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val todayText = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale("id", "ID")))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LumiCanvasDark)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        // Top Bar: Greeting & Quick Sync
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = todayText.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LumiTextMuted,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "LUMI Wellness",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = LumiTextPrimary,
                    letterSpacing = (-0.5).sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Sync Button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(LumiCardDark)
                        .border(1.dp, LumiCardBorderDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onSyncHealthConnect) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Sync",
                            tint = LumiTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Health Connect Status Capsule Alert (If not connected)
        if (!summary.hasHealthConnectPermission) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LumiPeriwinkle.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF101420)),
                onClick = onOpenHealthConnectSettings
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0x337D8EC2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sensors,
                                contentDescription = null,
                                tint = LumiPeriwinkle,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Aktifkan Health Connect",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = LumiTextPrimary
                            )
                            Text(
                                text = "Ketuk untuk buka pengaturan izin Health Connect di HP",
                                fontSize = 11.sp,
                                color = LumiTextSecondary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(LumiPeriwinkle)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Buka",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Hero LUMI AI Companion Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LumiCardBorderDark, RoundedCornerShape(30.dp)),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = LumiCardDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x227D8EC2))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = LumiPeriwinkle,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LUMI COMPANION",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LumiPeriwinkle,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val scoreText = if (summary.readinessScore != null) "${summary.readinessScore}" else "88"
                        Text(
                            text = "$scoreText% Readiness",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = LumiTextPrimary,
                            letterSpacing = (-0.5).sp
                        )

                        Text(
                            text = summary.readinessStatus,
                            fontSize = 13.sp,
                            color = LumiSage,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Signature Animated LUMI Orb
                    LumiOrb(
                        sizeDp = 96.dp,
                        primaryColor = LumiPeriwinkle,
                        secondaryColor = LumiAmber
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // AI Conversational Insight
                Text(
                    text = aiReport.morningBriefing,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = LumiTextSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Action Pill Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onOpenChat,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumiCapsule,
                            contentColor = LumiTextPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Tanya LUMI", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = onSyncHealthConnect,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumiPeriwinkle,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Sync Health", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Section Title: Daily Vitals & Activity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Biometric Overview",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = LumiTextPrimary,
                letterSpacing = (-0.5).sp
            )

            Text(
                text = "Health Connect Real Data",
                fontSize = 11.sp,
                color = LumiTextMuted
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Modular Bento Grid (Sleep, Heart, Movement, Vitals)
        LumiBentoGrid(
            summary = summary,
            onOpenSleep = onOpenSleep,
            onOpenVitals = onOpenVitals
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Evening Debrief Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LumiCardBorderDark, RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = LumiCardDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rekomendasi Malam",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumiTextPrimary
                    )

                    Text(
                        text = "Target: ${aiReport.dynamicStepGoal} langkah",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = LumiAmber
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = aiReport.eveningRecap,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = LumiTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
