package com.healthku.app.ui.asclepios

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    var selected by remember { mutableStateOf(LocalDate.of(2025, 10, 20)) }
    var pickDate by remember { mutableStateOf(false) }
    var addLog by remember { mutableStateOf(false) }
    var detail by remember { mutableStateOf<SleepLog?>(null) }
    var showAll by remember { mutableStateOf(false) }
    val weekStart = selected.minusDays((selected.dayOfWeek.value % 7).toLong())
    val week = (0..6).map { weekStart.plusDays(it.toLong()) }
    val logs = Store.sleepLogs.sortedByDescending { it.date }
    val irregularities = 126 + Store.sleepLogs.count { it.tag != "Rem" } - 4

    if (pickDate) {
        val st = rememberDatePickerState(selected.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
        DatePickerDialog(
            onDismissRequest = { pickDate = false },
            confirmButton = {
                TextButton(onClick = {
                    st.selectedDateMillis?.let {
                        selected = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    pickDate = false
                }) { Text("OK", color = SageGreen) }
            },
            dismissButton = {
                TextButton(onClick = { pickDate = false }) { Text("Batal", color = Muted) }
            },
        ) { DatePicker(st) }
    }

    if (addLog) {
        var h by remember { mutableFloatStateOf(7f) }
        AlertDialog(
            onDismissRequest = { addLog = false },
            title = { Text("Catat Tidur: ${selected.dayOfMonth} ${selected.month.getDisplayName(TextStyle.SHORT, Locale("id"))}", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("%.1f jam".format(h), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Spacer(Modifier.height(8.dp))
                    Slider(
                        value = h,
                        onValueChange = { h = (it * 2).toInt() / 2f },
                        valueRange = 1f..12f,
                        colors = SliderDefaults.colors(thumbColor = SageGreen, activeTrackColor = SageGreen)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    Store.sleepLogs.removeAll { it.date == selected }
                    Store.sleepLogs.add(SleepLog(selected, h))
                    addLog = false
                }) { Text("Simpan", color = SageGreen, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { addLog = false }) { Text("Batal", color = Muted) }
            },
        )
    }

    detail?.let { d ->
        AlertDialog(
            onDismissRequest = { detail = null },
            title = { Text("${d.date.dayOfMonth} ${d.date.month.getDisplayName(TextStyle.FULL, Locale("id"))}: ${d.tag}", fontWeight = FontWeight.Bold) },
            text = { Text("Anda tidur ${d.hours} jam. Status: ${d.note}.") },
            confirmButton = {
                TextButton(onClick = {
                    detail = null
                    openUrl(ctx, "https://www.cdc.gov/sleep/about/index.html")
                }) { Text("Tips Tidur", color = SageGreen) }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { Store.sleepLogs.remove(d); detail = null }) {
                        Text("Hapus", color = Color(0xFFC85B7A))
                    }
                    TextButton(onClick = { detail = null }) { Text("Tutup", color = Muted) }
                }
            },
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AsclepiosBg),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Top Header & Weekly Calendar Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SqButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        size = 44.dp,
                        onClick = onBack
                    )
                    SqButton(
                        icon = Icons.Default.CalendarMonth,
                        size = 44.dp,
                        onClick = { pickDate = true }
                    )
                }

                // Month Navigation Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${selected.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${selected.year}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(CardWhite)
                            .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { selected = selected.minusWeeks(1) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, tint = Ink, modifier = Modifier.size(18.dp))
                        }
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { selected = selected.plusWeeks(1) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Ink, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                // 7-Day Weekly Calendar Strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week.forEach { d ->
                        val isSelected = d == selected
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { selected = d },
                        ) {
                            Text(
                                text = d.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                                fontSize = 12.sp,
                                color = if (isSelected) SageGreen else Muted,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            Spacer(Modifier.height(8.dp))
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 36.dp, height = 46.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(SageGreen),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = "${d.dayOfMonth}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(width = 36.dp, height = 46.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = "${d.dayOfMonth}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Ink
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sleep Overview Hero Banner
        item {
            Reveal {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sleep Overview",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = if (showAll) "Ringkas" else "See All",
                            fontSize = 13.sp,
                            color = Muted,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable { showAll = !showAll }
                                .padding(vertical = 4.dp)
                        )
                    }

                    // Sage Green Hero Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFFCCE4CD), Color(0xFFDFEEDF))))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Real Serenity Avatar Character
                        Image(
                            painter = painterResource(id = R.drawable.sleep_character),
                            contentDescription = "Sleep Character",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(86.dp)
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "$irregularities+",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Sleep irregularities",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = SageGreen
                            )
                        }

                        // Circular Add Button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(CardWhite)
                                .clickable(onClick = { addLog = true }),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, null, tint = SageGreen, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }

        // Disease Risks Section Header
        item {
            Reveal {
                SectionHeader("Disease Risks") {
                    MoreButton(
                        items = listOf(
                            "Catat tidur hari ini" to { addLog = true },
                            "Bagikan ringkasan" to { shareText(ctx, "Saya punya $irregularities ketidakteraturan tidur") },
                            "Hapus semua" to { Store.sleepLogs.clear() }
                        ),
                        size = 32.dp
                    )
                }
            }
        }

        // Disease Risks Cards List
        items(if (showAll) logs else logs.take(3)) { r ->
            Reveal {
                RiskCard(r) { detail = r }
            }
        }

        if (logs.isEmpty()) {
            item {
                Text(
                    text = "Belum ada catatan. Tekan tombol + untuk menambah riwayat tidur.",
                    fontSize = 12.sp,
                    color = Muted,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun RiskCard(r: SleepLog, onClick: () -> Unit) {
    val bar = remember { Animatable(0f) }
    LaunchedEffect(r.hours) {
        bar.animateTo((r.hours / 12f).coerceIn(0.05f, 1f), tween(900, easing = FastOutSlowInEasing))
    }

    val (tagBg, tagText, barColor) = when (r.tag) {
        "Insomniac" -> Triple(Ink, Color.White, Ink)
        "Rem" -> Triple(Color(0xFFFDECDD), Color(0xFFD96B14), Color(0xFFE58A3A))
        else -> Triple(Color(0xFFE5F4E7), Color(0xFF2E7D32), Color(0xFF58A367))
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(74.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(CardWhite)
            .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Date Box
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(AsclepiosBgDeep),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = r.date.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    fontSize = 10.sp,
                    color = Muted
                )
                Text(
                    text = "${r.date.dayOfMonth}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // Middle Progress and Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Your slept for ${r.hours}h",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Ink
            )
            Spacer(Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDE7DA))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(bar.value)
                        .clip(CircleShape)
                        .background(barColor)
                )
            }
            Spacer(Modifier.height(3.dp))
            Text(
                text = r.note,
                fontSize = 10.sp,
                color = Muted
            )
        }

        // Status Pill Tag
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(tagBg)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = r.tag,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = tagText
            )
        }
    }
}
