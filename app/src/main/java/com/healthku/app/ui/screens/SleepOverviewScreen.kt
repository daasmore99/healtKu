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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CalendarMonth
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
import com.healthku.app.ui.theme.LumiAmber
import com.healthku.app.ui.theme.LumiCanvasDark
import com.healthku.app.ui.theme.LumiCardBorderDark
import com.healthku.app.ui.theme.LumiCardDark
import com.healthku.app.ui.theme.LumiPeriwinkle
import com.healthku.app.ui.theme.LumiSage
import com.healthku.app.ui.theme.LumiTextMuted
import com.healthku.app.ui.theme.LumiTextPrimary
import com.healthku.app.ui.theme.LumiTextSecondary

@Composable
fun SleepOverviewScreen(
    summary: DailyHealthSummary,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LumiCanvasDark)
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
                text = "Sleep Architecture",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = LumiTextPrimary
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LumiCardDark)
                    .border(1.dp, LumiCardBorderDark, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = LumiTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Calendar Strip
        CalendarStrip()

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Sleep Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LumiCardBorderDark, RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = LumiCardDark)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x227D8EC2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = LumiPeriwinkle,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    val duration = if (summary.sleepHours > 0f) "${String.format("%.1f", summary.sleepHours)}h" else "--"
                    Text(
                        text = "$duration Sleep Rest",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumiTextPrimary
                    )
                    Text(
                        text = if (summary.deepSleepHours > 0f) "Deep: ${String.format("%.1f", summary.deepSleepHours)}h • Optimal Phase" else "Data sinkron via Health Connect",
                        fontSize = 12.sp,
                        color = LumiTextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x227D8EC2))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Real-time",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumiPeriwinkle
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Sleep History & Trends",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = LumiTextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Real or Recent Sleep Logs
        val displayList = if (summary.sleepHistory.isNotEmpty()) {
            summary.sleepHistory
        } else {
            listOf(
                SleepLogEntry("Nov 07", "07", 7.2f, "Optimal", SleepTagType.OPTIMAL, 0),
                SleepLogEntry("Nov 06", "06", 6.4f, "Low", SleepTagType.LOW, 2),
                SleepLogEntry("Nov 05", "05", 8.1f, "Optimal", SleepTagType.OPTIMAL, 0)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(displayList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LumiCardBorderDark, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = LumiCardDark)
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1B1E2B)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = item.dateText.take(3),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = LumiTextMuted
                                        )
                                        Text(
                                            text = item.dayNumber,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LumiTextPrimary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "${item.durationHours} hours sleep",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = LumiTextPrimary
                                    )
                                    Text(
                                        text = if (item.suggestionCount > 0) "${item.suggestionCount} AI Tips" else "Healthy Rhythm",
                                        fontSize = 11.sp,
                                        color = LumiTextMuted
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when (item.tagType) {
                                            SleepTagType.OPTIMAL -> Color(0x225E8C4A)
                                            SleepTagType.LOW -> Color(0x22E2A55E)
                                            SleepTagType.REM -> Color(0x227D8EC2)
                                            SleepTagType.WARNING -> Color(0x22F16751)
                                        }
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = item.tagText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (item.tagType) {
                                        SleepTagType.OPTIMAL -> LumiSage
                                        SleepTagType.LOW -> LumiAmber
                                        SleepTagType.REM -> LumiPeriwinkle
                                        SleepTagType.WARNING -> Color(0xFFF16751)
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val progressFrac = (item.durationHours / 8.0f).coerceIn(0.1f, 1f)
                        LinearProgressIndicator(
                            progress = { progressFrac },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = when (item.tagType) {
                                SleepTagType.OPTIMAL -> LumiSage
                                SleepTagType.LOW -> LumiAmber
                                SleepTagType.REM -> LumiPeriwinkle
                                SleepTagType.WARNING -> Color(0xFFF16751)
                            },
                            trackColor = Color(0xFF1E212D),
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}
