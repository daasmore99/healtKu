package com.healthku.app.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.R
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosTextMuted
import com.healthku.app.ui.theme.AsclepiosTextPrimary
import com.healthku.app.ui.theme.AsclepiosTextSecondary
import com.healthku.app.ui.theme.MetricBpPink
import com.healthku.app.ui.theme.MetricBpPinkBg
import com.healthku.app.ui.theme.MetricCalorieBg
import com.healthku.app.ui.theme.MetricCalorieOrange
import com.healthku.app.ui.theme.MetricHeartBlue
import com.healthku.app.ui.theme.MetricHeartBlueBg
import com.healthku.app.ui.theme.SagePillGreen

@Composable
fun AsclepiosMetricCards(
    summary: DailyHealthSummary,
    onOpenLiveBp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header: Smart Health Metrics | See All
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Smart Health Metrics",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = AsclepiosTextPrimary
            )
            Text(
                text = "See All",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = AsclepiosTextSecondary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 2-Column Grid (Heart Rate & Blood Pressure) matching screenshot exactly
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Heart Rate Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
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
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MetricHeartBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Heart Rate",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AsclepiosTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Real 3D Heart Artwork from design
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.heart_card_mini),
                            contentDescription = "Heart 3D",
                            modifier = Modifier.size(76.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = if (summary.heartRateBpm != null) "${summary.heartRateBpm}" else "70",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsclepiosTextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "bpm",
                                fontSize = 12.sp,
                                color = AsclepiosTextSecondary,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }

                        WaveSparkline(
                            data = if (summary.heartRateHistory.isNotEmpty()) summary.heartRateHistory else listOf(65f, 70f, 68f, 74f, 70f),
                            lineColor = MetricHeartBlue,
                            width = 38.dp,
                            height = 20.dp
                        )
                    }
                }
            }

            // Blood Pressure Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                onClick = onOpenLiveBp
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
                                imageVector = Icons.Default.MonitorHeart,
                                contentDescription = null,
                                tint = MetricBpPink,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Blood Press",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AsclepiosTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Real Stethoscope Heart Artwork from design
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.stethoscope_heart),
                            contentDescription = "Stethoscope Heart",
                            modifier = Modifier.size(76.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = if (summary.bloodPressureSys != null) {
                                    if (summary.bloodPressureDia != null) "${summary.bloodPressureSys}/${summary.bloodPressureDia}" else "${summary.bloodPressureSys}"
                                } else "120",
                                fontSize = if (summary.bloodPressureDia != null) 17.sp else 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsclepiosTextPrimary
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "mmHg",
                                fontSize = 11.sp,
                                color = AsclepiosTextSecondary,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }

                        WaveSparkline(
                            data = listOf(118f, 122f, 120f, 124f, 120f),
                            lineColor = MetricBpPink,
                            width = 38.dp,
                            height = 20.dp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Section 2 Header: Smart Health Metrics | ...
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Smart Health Metrics",
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

        Spacer(modifier = Modifier.height(6.dp))

        // Calories Burned Card (exact from design)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MetricCalorieBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = MetricCalorieOrange,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Calories Burned",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AsclepiosTextPrimary
                        )
                        Text(
                            text = if (summary.caloriesBurned > 0) "${summary.caloriesBurned} kcal aktif" else "2000 kcal • No Suggestion",
                            fontSize = 12.sp,
                            color = AsclepiosTextSecondary
                        )
                    }
                }

                Text(
                    text = if (summary.caloriesBurned > 0) "${summary.caloriesBurned} kcal" else "2000 kcal",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AsclepiosTextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Daily Steps & Distance Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(androidx.compose.ui.graphics.Color(0xFFE9F3EC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsWalk,
                            contentDescription = null,
                            tint = SagePillGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Walking & Distance",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AsclepiosTextPrimary
                        )
                        Text(
                            text = if (summary.steps > 0) "${summary.distanceKm} km jarak tempuh" else "5.6 kilometer • Normal",
                            fontSize = 12.sp,
                            color = AsclepiosTextSecondary
                        )
                    }
                }

                Text(
                    text = if (summary.steps > 0) "${summary.steps} steps" else "7,420 steps",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AsclepiosTextPrimary
                )
            }
        }
    }
}
