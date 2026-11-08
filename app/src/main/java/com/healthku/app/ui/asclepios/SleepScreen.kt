package com.healthku.app.ui.asclepios

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.ui.asclepios.SleepLog
import com.healthku.app.ui.asclepios.Store
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

private fun SleepLog.color() = when (tag) { "Insomniac" -> Ink; "Rem" -> Amber; else -> Olive }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    var selected by remember { mutableStateOf(LocalDate.of(2025, 10, 20)) }
    var pickDate by remember { mutableStateOf(false) }
    var addLog by remember { mutableStateOf(false) }
    var detail by remember { mutableStateOf<SleepLog?>(null) }
    var showAll by remember { mutableStateOf(false) }
    val weekStart = selected.minusDays((selected.dayOfWeek.value % 7).toLong()) // week starts Sunday
    val week = (0..6).map { weekStart.plusDays(it.toLong()) }
    val logs = Store.sleepLogs.sortedByDescending { it.date }
    val irregularities = 126 + Store.sleepLogs.count { it.tag != "Rem" } - 4

    if (pickDate) {
        val st = rememberDatePickerState(selected.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
        DatePickerDialog(
            onDismissRequest = { pickDate = false },
            confirmButton = { TextButton({ st.selectedDateMillis?.let { selected = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate() }; pickDate = false }) { Text("OK") } },
            dismissButton = { TextButton({ pickDate = false }) { Text("Batal") } },
        ) { DatePicker(st) }
    }
    if (addLog) {
        var h by remember { mutableFloatStateOf(7f) }
        AlertDialog(
            onDismissRequest = { addLog = false },
            title = { Text("Catat tidur ${selected.dayOfMonth} ${selected.month.getDisplayName(TextStyle.SHORT, Locale("id"))}") },
            text = { Column { Text("%.1f jam".format(h), fontSize = 22.sp, fontWeight = FontWeight.SemiBold); Slider(h, { h = (it * 2).toInt() / 2f }, valueRange = 1f..12f) } },
            confirmButton = { TextButton({
                Store.sleepLogs.removeAll { it.date == selected }; Store.sleepLogs.add(SleepLog(selected, h)); addLog = false
            }) { Text("Simpan") } },
            dismissButton = { TextButton({ addLog = false }) { Text("Batal") } },
        )
    }
    detail?.let { d ->
        AlertDialog(
            onDismissRequest = { detail = null },
            title = { Text("${d.date.dayOfMonth} ${d.date.month.getDisplayName(TextStyle.FULL, Locale("id"))}: ${d.tag}") },
            text = { Text("Anda tidur ${d.hours} jam. ${d.note}.") },
            confirmButton = { TextButton({ detail = null; openUrl(ctx, "https://www.cdc.gov/sleep/about/index.html") }) { Text("Tips tidur") } },
            dismissButton = { Row {
                TextButton({ Store.sleepLogs.remove(d); detail = null }) { Text("Hapus") }
                TextButton({ detail = null }) { Text("Tutup") }
            } },
        )
    }

    LazyColumn(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFF3E8DC), Color(0xFFEFE2D2)))),
        contentPadding = PaddingValues(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Column(
                Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .statusBarsPadding().padding(horizontal = 20.dp).padding(top = 12.dp, bottom = 20.dp),
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SqButton(Icons.AutoMirrored.Filled.ArrowBack, 48.dp, onClick = onBack)
                    SqButton(Icons.Default.CalendarMonth, 48.dp) { pickDate = true }
                }
                Spacer(Modifier.height(18.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("${selected.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${selected.year}", fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    Box(Modifier.size(28.dp).clip(RoundedCornerShape(8.dp)).background(Forest).clickable { selected = selected.minusWeeks(1) }, contentAlignment = Alignment.Center) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(6.dp))
                    Box(Modifier.size(28.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF1F1F1)).clickable { selected = selected.plusWeeks(1) }, contentAlignment = Alignment.Center) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    week.forEach { d ->
                        val sel = d == selected
                        val bg by animateColorAsState(if (sel) Forest else Color.Transparent, label = "bg")
                        val sc by animateFloatAsState(if (sel) 1.1f else 1f, label = "sc")
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { selected = d },
                        ) {
                            Text(d.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH), fontSize = 11.sp, color = if (sel) Forest else Muted, fontWeight = if (sel) FontWeight.SemiBold else FontWeight.Normal)
                            Spacer(Modifier.height(6.dp))
                            Box(
                                Modifier.size(38.dp).scale(sc).clip(CircleShape).background(bg).then(if (sel) Modifier else Modifier.border(1.dp, Color(0xFFD4D4D4), CircleShape)),
                                contentAlignment = Alignment.Center,
                            ) { Text("${d.dayOfMonth}", fontSize = 13.sp, color = if (sel) Color.White else Ink) }
                        }
                    }
                }
            }
        }
        item {
            Reveal {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader("Sleep Overview") {
                        Text(if (showAll) "Ringkas" else "See All", fontSize = 11.sp, color = Muted, modifier = Modifier.clickable { showAll = !showAll }.padding(4.dp))
                    }
                    SleepCard(irregularities) { addLog = true }
                }
            }
        }
        item {
            Reveal {
                SectionHeader("Disease Ricks") {
                    MoreButton(listOf("Catat tidur hari ini" to { addLog = true }, "Bagikan ringkasan" to { shareText(ctx, "Saya punya $irregularities ketidakteraturan tidur") }, "Hapus semua" to { Store.sleepLogs.clear() }))
                }
            }
        }
        items(if (showAll) logs else logs.take(3)) { r -> Reveal { RiskRow(r) { detail = r } } }
        if (logs.isEmpty()) item { Text("Belum ada catatan. Tekan + untuk menambah.", fontSize = 12.sp, color = Muted, modifier = Modifier.padding(horizontal = 20.dp)) }
    }
}

@Composable
private fun SleepCard(irregularities: Int, onAdd: () -> Unit) {
    val y by rememberInfiniteTransition(label = "y").animateFloat(
        0f, -5f, infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "y",
    )
    Row(
        Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(110.dp).clip(RoundedCornerShape(28.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFFBFE3B5), Color(0xFFD6E8A4)))),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.foundation.Canvas(Modifier.width(110.dp).fillMaxHeight().offset(y = y.dp)) {
            val w = size.width; val h = size.height
            val hair = Stroke(w * .14f, cap = StrokeCap.Round)
            drawPath(Path().apply { moveTo(w * .12f, h); cubicTo(0f, h * .6f, w * .08f, h * .2f, w * .28f, h * .12f) }, Color(0xFF1D7F5C), style = hair)
            drawPath(Path().apply { moveTo(w * .88f, h); cubicTo(w, h * .6f, w * .92f, h * .2f, w * .72f, h * .12f) }, Color(0xFF1D7F5C), style = hair)
            drawCircle(Color(0xFF3FC9A0), w * .17f, Offset(w * .5f, h * .38f))
            drawPath(Path().apply {
                moveTo(w * .24f, h); cubicTo(w * .24f, h * .7f, w * .34f, h * .58f, w * .5f, h * .58f)
                cubicTo(w * .66f, h * .58f, w * .76f, h * .7f, w * .76f, h); close()
            }, Color(0xFF2AA882))
        }
        Column(Modifier.weight(1f)) {
            Text("$irregularities+", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Text("Sleep irregularities", fontSize = 12.sp, color = Color(0xFF444444))
        }
        Box(Modifier.padding(end = 16.dp).size(36.dp).clip(CircleShape).background(Ink.copy(alpha = .85f)).clickable(onClick = onAdd), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun RiskRow(r: SleepLog, onClick: () -> Unit) {
    val bar = remember { Animatable(0f) }
    LaunchedEffect(r.hours) { bar.animateTo((r.hours / 12f).coerceIn(0.05f, 1f), tween(1000, 300, FastOutSlowInEasing)) }
    Row(
        Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(68.dp).clip(CircleShape).background(Color(0xFFFFFDF8))
            .clickable(onClick = onClick).padding(start = 8.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(52.dp).clip(CircleShape).background(Cream), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(r.date.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH), fontSize = 10.sp)
                Text("${r.date.dayOfMonth}", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(Modifier.weight(1f).padding(start = 12.dp)) {
            Text("Your slept for ${r.hours}h", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Box(Modifier.width(130.dp).height(4.dp).clip(CircleShape).background(Color(0xFFE5E5E5))) {
                Box(Modifier.fillMaxHeight().fillMaxWidth(bar.value).clip(CircleShape).background(r.color()))
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(r.tag, fontSize = 10.sp, color = Color.White, modifier = Modifier.clip(CircleShape).background(r.color()).padding(horizontal = 10.dp, vertical = 3.dp))
            Spacer(Modifier.height(4.dp))
            Text(r.note, fontSize = 8.sp, color = Muted)
        }
    }
}
