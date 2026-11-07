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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.data.SleepLogEntry
import com.healthku.app.data.SleepTagType
import com.healthku.app.ui.components.CalendarStrip
import com.healthku.app.ui.theme.AsclepiosBg
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosTextMuted
import com.healthku.app.ui.theme.AsclepiosTextPrimary
import com.healthku.app.ui.theme.AsclepiosTextSecondary
import com.healthku.app.ui.theme.SagePillBg
import com.healthku.app.ui.theme.SagePillGreen
import com.healthku.app.ui.theme.SleepCardBg

@Composable
fun SleepOverviewScreen(
    summary: DailyHealthSummary,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AsclepiosBg)
            .padding(horizontal = 20.dp, vertical = 16.dp)
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

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(AsclepiosCard)
                    .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = AsclepiosTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Calendar Strip
        CalendarStrip()

        Spacer(modifier = Modifier.height(18.dp))

        // Section: Sleep Overview | See All
        Row(
            modifier = Modifier.fillMaxWidth(),
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

        // Hero Green Sleep Overview Card (matching screenshot)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = SleepCardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Serene Avatar Graphic
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFB5D4BA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = null,
                        tint = Color(0xFF1E482C),
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Middle: Text
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    val duration = if (summary.sleepHours > 0f) String.format("%.1fh", summary.sleepHours) else "Belum Ada"
                    Text(
                        text = if (summary.sleepHours > 0f) "$duration tidur" else "126+ Sleep",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B3D25)
                    )
                    Text(
                        text = if (summary.sleepHours > 0f) "Deep sleep: ${String.format("%.1fh", summary.deepSleepHours)}" else "Sleep irregularities",
                        fontSize = 12.sp,
                        color = Color(0xFF336240)
                    )
                }

                // Right: White '+' Button
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
                        tint = Color(0xFF265135),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Disease Risks / Sleep History
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Disease Risks & History",
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

        // History List
        val displayList = if (summary.sleepHistory.isNotEmpty()) {
            summary.sleepHistory
        } else {
            listOf(
                SleepLogEntry("Oct 17", "17", 5.2f, "Low", SleepTagType.LOW, 0),
                SleepLogEntry("Oct 16", "16", 8.2f, "Rem", SleepTagType.REM, 8),
                SleepLogEntry("Oct 15", "15", 2.2f, "Insomniac", SleepTagType.WARNING, 78)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(displayList) { item ->
                SleepHistoryRowItem(item = item)
            }
        }
    }
}

@Composable
fun SleepHistoryRowItem(item: SleepLogEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Date circle
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3ECE0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = item.dateText.take(3),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = AsclepiosTextSecondary
                            )
                            Text(
                                text = item.dayNumber,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsclepiosTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "You slept for ${item.durationHours}h",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AsclepiosTextPrimary
                        )
                        Text(
                            text = if (item.suggestionCount > 0) "${item.suggestionCount} AI Suggestion" else "No Suggestion",
                            fontSize = 11.sp,
                            color = AsclepiosTextMuted
                        )
                    }
                }

                // Status Tag Pill
                val pillBg = when (item.tagType) {
                    SleepTagType.OPTIMAL -> SagePillBg
                    SleepTagType.LOW -> Color(0xFFE2EED9)
                    SleepTagType.REM -> Color(0xFFFEE8D6)
                    SleepTagType.WARNING -> Color(0xFF1E211F)
                }
                val pillTextColor = when (item.tagType) {
                    SleepTagType.OPTIMAL -> SagePillGreen
                    SleepTagType.LOW -> Color(0xFF3B6B2B)
                    SleepTagType.REM -> Color(0xFFC76214)
                    SleepTagType.WARNING -> Color.White
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(pillBg)
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = item.tagText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = pillTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Thin progress line underneath (matching screenshot)
            val progressFrac = (item.durationHours / 8.0f).coerceIn(0.1f, 1f)
            val lineColor = when (item.tagType) {
                SleepTagType.LOW -> Color(0xFF5D9B4A)
                SleepTagType.REM -> Color(0xFFE58B43)
                SleepTagType.WARNING -> Color(0xFF222222)
                SleepTagType.OPTIMAL -> SagePillGreen
            }

            LinearProgressIndicator(
                progress = { progressFrac },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = lineColor,
                trackColor = Color(0xFFEDE9DF),
                strokeCap = StrokeCap.Round
            )
        }
    }
}
