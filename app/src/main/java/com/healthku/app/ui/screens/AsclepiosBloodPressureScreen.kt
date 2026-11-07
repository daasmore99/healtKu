package com.healthku.app.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.R
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.ui.theme.AsclepiosBg
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosTextMuted
import com.healthku.app.ui.theme.AsclepiosTextPrimary
import com.healthku.app.ui.theme.AsclepiosTextSecondary
import com.healthku.app.ui.theme.MetricBpPink
import com.healthku.app.ui.theme.MetricBpPinkBg
import com.healthku.app.ui.theme.MetricHeartBlue
import com.healthku.app.ui.theme.MetricHeartBlueBg
import com.healthku.app.ui.theme.SagePillBg
import com.healthku.app.ui.theme.SagePillGreen
import kotlinx.coroutines.delay

@Composable
fun AsclepiosBloodPressureScreen(
    summary: DailyHealthSummary,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var isReading by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(15) }

    LaunchedEffect(isReading) {
        if (isReading) {
            secondsLeft = 15
            while (secondsLeft > 0) {
                delay(1000L)
                secondsLeft -= 1
            }
            isReading = false
        }
    }

    // Subtle gentle heartbeat pulse
    val infiniteTransition = rememberInfiniteTransition(label = "Heartbeat")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isReading) 1.10f else 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isReading) 500 else 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AsclepiosBg)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Bar: Back button + Title + More button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AsclepiosCard)
                    .border(1.dp, AsclepiosCardBorder, CircleShape)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = AsclepiosTextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = "Blood Pressure",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = AsclepiosTextPrimary
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AsclepiosCard)
                    .border(1.dp, AsclepiosCardBorder, CircleShape),
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

        Spacer(modifier = Modifier.height(28.dp))

        // Large Reading Display (e.g. 121/80 SYSmm/hg)
        val sysVal = summary.bloodPressureSys ?: 121
        val diaVal = summary.bloodPressureDia ?: 80

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$sysVal/$diaVal",
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AsclepiosTextPrimary,
                letterSpacing = (-1).sp
            )

            Text(
                text = "SYSmm/hg",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = AsclepiosTextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Center Dial Gauge with 3D Heart Model
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentAlignment = Alignment.Center
        ) {
            // Dial border / arc graphic
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .border(12.dp, Color(0xFF1F2228), RoundedCornerShape(105.dp, 105.dp, 40.dp, 40.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Soft Green Circular Glow Backdrop
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(SagePillBg),
                    contentAlignment = Alignment.Center
                ) {
                    // Exact 3D Anatomical Heart from Screen 2
                    Image(
                        painter = painterResource(id = R.drawable.heart_3d),
                        contentDescription = "3D Heart",
                        modifier = Modifier
                            .size(115.dp)
                            .scale(pulseScale),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Labels around the dial
            Text(
                text = "0s",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AsclepiosTextPrimary,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 28.dp)
            )

            Text(
                text = "15s",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AsclepiosTextPrimary,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 2.dp)
            )

            Text(
                text = "20s",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AsclepiosTextPrimary,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 28.dp)
            )

            // Timer display below the dial
            Text(
                text = if (isReading) "$secondsLeft Sec Left" else "15 Sec Left",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = AsclepiosTextPrimary,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Main Overview Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Main Overview",
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

        Spacer(modifier = Modifier.height(10.dp))

        // Two Overview Cards: Oxygen & Cholesterol
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Oxygen Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MetricBpPinkBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = MetricBpPink,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Oxygen",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AsclepiosTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (summary.oxygenSaturationPercent != null) "${summary.oxygenSaturationPercent}%" else "98%",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsclepiosTextPrimary
                    )
                }
            }

            // Cholest... Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MetricHeartBlueBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.InvertColors,
                                contentDescription = null,
                                tint = MetricHeartBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cholest...",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AsclepiosTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "--",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsclepiosTextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Bottom Action Capsule: Swipe to start reading › › ›
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFF191919))
                .clickable { isReading = true }
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // White arrow circle inside
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Start",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = if (isReading) "Reading in progress ($secondsLeft s)..." else "Swipe to start reading   ›   ›   ›",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
