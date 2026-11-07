package com.healthku.app.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.ui.components.AnatomicalHeartGraphic
import com.healthku.app.ui.theme.AsclepiosBg
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosTextMuted
import com.healthku.app.ui.theme.AsclepiosTextPrimary
import com.healthku.app.ui.theme.AsclepiosTextSecondary
import com.healthku.app.ui.theme.BlackCapsule
import com.healthku.app.ui.theme.MetricBpPink
import com.healthku.app.ui.theme.MetricBpPinkBg
import com.healthku.app.ui.theme.MetricOxygenCyan
import com.healthku.app.ui.theme.MetricOxygenBg

@Composable
fun BloodPressureLiveScreen(
    summary: DailyHealthSummary,
    onBack: () -> Unit,
    onTriggerReading: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsclepiosBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top App Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(AsclepiosCard)
                        .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AsclepiosTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
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
                        .clip(RoundedCornerShape(14.dp))
                        .background(AsclepiosCard)
                        .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = null,
                        tint = AsclepiosTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Large Blood Pressure Reading
            val sysText = if (summary.bloodPressureSys != null) "${summary.bloodPressureSys}" else "121"
            val diaText = if (summary.bloodPressureDia != null) "${summary.bloodPressureDia}" else "80"
            Text(
                text = "$sysText/$diaText",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = AsclepiosTextPrimary,
                letterSpacing = (-1).sp
            )

            Text(
                text = "SYSmm/hg",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = AsclepiosTextSecondary
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Gauge with 3D Anatomical Heart in center
            Box(
                modifier = Modifier
                    .size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                // Circular arc track with 0s, 15s, 20s
                Canvas(modifier = Modifier.size(220.dp)) {
                    val stroke = 12f
                    // Outer gray track
                    drawArc(
                        color = Color(0xFFE2DDD2),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                    // Active black arc
                    drawArc(
                        color = Color(0xFF1B1F1C),
                        startAngle = 135f,
                        sweepAngle = 200f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }

                // Time labels around gauge
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "0s",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AsclepiosTextSecondary,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 4.dp)
                    )
                    Text(
                        text = "15s",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AsclepiosTextSecondary,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 4.dp)
                    )
                    Text(
                        text = "20s",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AsclepiosTextSecondary,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = "15 Sec Left",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AsclepiosTextPrimary,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                    )
                }

                // Center 3D Anatomical Heart Graphic
                AnatomicalHeartGraphic(
                    sizeDp = 110.dp,
                    showGlow = true,
                    glowColor = Color(0xFFCCE4D3)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Main Overview Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Main Overview",
                    fontSize = 18.sp,
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

            Spacer(modifier = Modifier.height(12.dp))

            // Overview Cards (Oxygen & Heart Rate)
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
                    colors = CardDefaults.cardColors(containerColor = AsclepiosCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MetricBpPinkBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = MetricBpPink,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Oxygen",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = AsclepiosTextSecondary
                            )
                            Text(
                                text = if (summary.oxygenSaturationPercent != null) "${summary.oxygenSaturationPercent}%" else "98%",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsclepiosTextPrimary
                            )
                        }
                    }
                }

                // Pulse Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(22.dp)),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = AsclepiosCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEAF1FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color(0xFF5A8DF4),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Pulse",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = AsclepiosTextSecondary
                            )
                            Text(
                                text = if (summary.heartRateBpm != null) "${summary.heartRateBpm} bpm" else "72 bpm",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsclepiosTextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Black Pill "Swipe to start reading >>>"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(BlackCapsule)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // White circle slider button
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(18.dp))

                    Text(
                        text = "Swipe to start reading  ›  ›  ›",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
