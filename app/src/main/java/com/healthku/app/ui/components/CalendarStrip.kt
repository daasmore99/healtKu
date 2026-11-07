package com.healthku.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.ui.theme.LumiCardBorderDark
import com.healthku.app.ui.theme.LumiCardDark
import com.healthku.app.ui.theme.LumiPeriwinkle
import com.healthku.app.ui.theme.LumiTextMuted
import com.healthku.app.ui.theme.LumiTextPrimary
import com.healthku.app.ui.theme.LumiTextSecondary
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarStrip(
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit = {}
) {
    val today = remember { LocalDate.now() }
    var selectedDayOffset by remember { mutableIntStateOf(0) }

    val daysOfWeek = remember(today) {
        (-3..3).map { today.plusDays(it.toLong()) }
    }

    val currentMonthText = today.month.getDisplayName(TextStyle.FULL, Locale("id", "ID"))
    val currentYear = today.year

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(LumiCardDark)
            .border(1.dp, LumiCardBorderDark, RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        // Month Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$currentMonthText $currentYear",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = LumiTextPrimary
            )

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B1E2B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Kalender",
                    tint = LumiPeriwinkle,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Days of week row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysOfWeek.forEach { date ->
                val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                Text(
                    text = dayName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = LumiTextMuted,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // Dates numbers row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            daysOfWeek.forEach { date ->
                val isSelected = date == today.plusDays(selectedDayOffset.toLong())
                val isToday = date == today

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) LumiPeriwinkle else Color.Transparent
                            )
                            .clickable {
                                selectedDayOffset = date.dayOfYear - today.dayOfYear
                                onDateSelected(date)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            fontSize = 13.sp,
                            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else LumiTextPrimary
                        )
                    }
                }
            }
        }
    }
}
