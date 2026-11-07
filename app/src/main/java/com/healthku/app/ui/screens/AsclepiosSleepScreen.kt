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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.R
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.data.SleepLogEntry
import com.healthku.app.data.SleepTagType
import com.healthku.app.ui.components.AsclepiosCalendarStrip
import com.healthku.app.ui.theme.AsclepiosBg
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosTextMuted
import com.healthku.app.ui.theme.AsclepiosTextPrimary
import com.healthku.app.ui.theme.AsclepiosTextSecondary
import com.healthku.app.ui.theme.SagePillBg
import com.healthku.app.ui.theme.SagePillGreen

@Composable
fun AsclepiosSleepScreen(
    summary: DailyHealthSummary,
    onBack: () -> Unit,
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
        // Top Bar: Back button + Calendar button
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

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AsclepiosCard)
                    .border(1.dp, AsclepiosCardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Calendar",
                    tint = AsclepiosTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Calendar Strip
        AsclepiosCalendarStrip()

        Spacer(modifier = Modifier.height(22.dp))

        // Section: Sleep Overview | See All
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sleep Overview",
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

        Spacer(modifier = Modifier.height(10.dp))

        // Large Green Hero Banner (with Serene Sleep Character)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFC7E2C7), Color(0xFFDDEEDE))
                        )
                    )
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Serene Avatar Graphic
                    Image(
                        painter = painterResource(id = R.drawable.sleep_character),
                        contentDescription = "Sleep Character",
                        modifier = Modifier.size(86.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    // Right: Stat + Subtitle + Action Button
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (summary.sleepHours > 0) "${summary.sleepHours}h" else "126+",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1B3824)
                        )
                        Text(
                            text = if (summary.sleepHours > 0) "Total waktu tidur hari ini" else "Sleep irregularities",
                            fontSize = 12.sp,
                            color = Color(0xFF385541)
                        )
                    }

                    // White circular '+' button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color(0xFF1B3824),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Disease Risks | ...
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Disease Risks",
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

        // Sleep logs list
        val defaultList = listOf(
            SleepLogEntry(dateText = "Oct", dayNumber = "17", durationHours = 5.2f, tagText = "Low", tagType = SleepTagType.LOW, suggestionCount = 0),
            SleepLogEntry(dateText = "Oct", dayNumber = "16", durationHours = 8.2f, tagText = "Rem", tagType = SleepTagType.REM, suggestionCount = 6),
            SleepLogEntry(dateText = "Oct", dayNumber = "15", durationHours = 2.2f, tagText = "Insomniac", tagType = SleepTagType.WARNING, suggestionCount = 78)
        )

        val displayLogs = if (summary.sleepHistory.isNotEmpty()) summary.sleepHistory else defaultList

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            displayLogs.forEach { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(22.dp)),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Date Box
                        Box(
                            modifier = Modifier
                                .size(width = 46.dp, height = 48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF5F3EC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = entry.dateText.take(3),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AsclepiosTextSecondary
                                )
                                Text(
                                    text = entry.dayNumber,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsclepiosTextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // Details
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Your slept for ${entry.durationHours}h",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AsclepiosTextPrimary
                                )

                                // Status Pill
                                val pillBg = when (entry.tagType) {
                                    SleepTagType.OPTIMAL, SleepTagType.LOW -> SagePillBg
                                    SleepTagType.REM -> Color(0xFFFBE4D2)
                                    SleepTagType.WARNING -> Color(0xFF1E2127)
                                }
                                val pillText = when (entry.tagType) {
                                    SleepTagType.OPTIMAL, SleepTagType.LOW -> SagePillGreen
                                    SleepTagType.REM -> Color(0xFFC9641D)
                                    SleepTagType.WARNING -> Color.White
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(pillBg)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = entry.tagText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = pillText
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Thin progress indicator
                            LinearProgressIndicator(
                                progress = { (entry.durationHours / 9f).coerceIn(0.1f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth(0.55f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = when (entry.tagType) {
                                    SleepTagType.OPTIMAL, SleepTagType.LOW -> SagePillGreen
                                    SleepTagType.REM -> Color(0xFFE27B30)
                                    SleepTagType.WARNING -> Color(0xFF333333)
                                },
                                trackColor = Color(0xFFEAE7DF)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (entry.suggestionCount > 0) "${entry.suggestionCount} AI Suggestion" else "No Suggestion",
                                fontSize = 11.sp,
                                color = AsclepiosTextSecondary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
