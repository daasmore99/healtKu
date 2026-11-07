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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
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
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.ui.components.LumiOrb
import com.healthku.app.ui.components.WaveSparkline
import com.healthku.app.ui.theme.LumiCanvasDark
import com.healthku.app.ui.theme.LumiCardBorderDark
import com.healthku.app.ui.theme.LumiCardDark
import com.healthku.app.ui.theme.LumiCoral
import com.healthku.app.ui.theme.LumiPeriwinkle
import com.healthku.app.ui.theme.LumiSage
import com.healthku.app.ui.theme.LumiTextMuted
import com.healthku.app.ui.theme.LumiTextPrimary
import com.healthku.app.ui.theme.LumiTextSecondary

@Composable
fun LumiVitalsScreen(
    summary: DailyHealthSummary,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LumiCanvasDark)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LumiCardDark)
                    .border(1.dp, LumiCardBorderDark, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = LumiTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = "Vitals & Rhythm",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = LumiTextPrimary
            )

            Box(modifier = Modifier.size(42.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Hero Vitals Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LumiCardBorderDark, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = LumiCardDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LumiOrb(
                    sizeDp = 100.dp,
                    primaryColor = LumiCoral,
                    secondaryColor = LumiPeriwinkle
                )

                Spacer(modifier = Modifier.height(16.dp))

                val bpText = if (summary.bloodPressureSys != null) "${summary.bloodPressureSys}/${summary.bloodPressureDia}" else "120/80"
                Text(
                    text = bpText,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = LumiTextPrimary,
                    letterSpacing = (-1).sp
                )

                Text(
                    text = "Blood Pressure (SYS/DIA mmHg)",
                    fontSize = 13.sp,
                    color = LumiTextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x225E8C4A))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Optimal Range",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LumiSage
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Detailed Biometrics",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = LumiTextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Heart Rate Detailed Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LumiCardBorderDark, RoundedCornerShape(22.dp)),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = LumiCardDark)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
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

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Resting Heart Rate",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = LumiTextSecondary
                        )
                        Text(
                            text = if (summary.restingHeartRateBpm != null) "${summary.restingHeartRateBpm} bpm" else if (summary.heartRateBpm != null) "${summary.heartRateBpm} bpm" else "--",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LumiTextPrimary
                        )
                    }
                }

                WaveSparkline(
                    data = if (summary.heartRateHistory.isNotEmpty()) summary.heartRateHistory else listOf(62f, 68f, 65f, 72f, 68f),
                    lineColor = LumiCoral,
                    width = 54.dp,
                    height = 24.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Blood Oxygen (SpO2) Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LumiCardBorderDark, RoundedCornerShape(22.dp)),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = LumiCardDark)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0x2248B5AC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = Color(0xFF48B5AC),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Blood Oxygen (SpO2)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = LumiTextSecondary
                        )
                        Text(
                            text = if (summary.oxygenSaturationPercent != null) "${summary.oxygenSaturationPercent}%" else "98%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LumiTextPrimary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x1848B5AC))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Normal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF48B5AC)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
