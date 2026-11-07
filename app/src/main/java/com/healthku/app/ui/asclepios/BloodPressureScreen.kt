package com.healthku.app.ui.asclepios

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun BloodPressureScreen(onBack: () -> Unit, go: (String) -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val progress = remember { Animatable(0f) } // 0..1 along the black arc (= 15 s)
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }
    val secLeft = kotlin.math.ceil(15 - progress.value * 15).toInt()
    val sys = 121 + (sin(progress.value * 40f) * 2).roundToInt()

    // Save a finished reading to history exactly once per run.
    LaunchedEffect(progress.value >= 1f) {
        if (progress.value >= 1f) com.healthku.app.ui.asclepios.Store.readings.add(0, com.healthku.app.ui.asclepios.Reading("Baru saja", sys, 80))
    }

    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFF3E8DC), Color(0xFFF0E2D3))))) {
        Column(
            Modifier.fillMaxSize().verticalScroll(androidx.compose.foundation.rememberScrollState()).statusBarsPadding()
                .padding(horizontal = 20.dp).padding(top = 12.dp, bottom = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                SqButton(Icons.AutoMirrored.Filled.ArrowBackIosNew, 48.dp, onClick = onBack)
                Text("Blood Pressure", fontSize = 17.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                MoreButton(
                    listOf(
                        "Bagikan hasil" to { shareText(ctx, "Tekanan darah saya: $sys/80 mmHg") },
                        "Hapus riwayat" to { com.healthku.app.ui.asclepios.Store.readings.clear() },
                        "Tentang tekanan darah" to { openUrl(ctx, com.healthku.app.ui.asclepios.Store.metric("Blood Pressure").url) },
                    ), 48.dp, CreamDeep,
                )
            }
            Spacer(Modifier.height(24.dp))
            Text("$sys/80", fontSize = 52.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-1).sp)
            Text("SYSmm/hg", fontSize = 13.sp, color = Muted)
            Spacer(Modifier.height(28.dp))
            Text("15s", fontSize = 13.sp)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("0s", fontSize = 13.sp, modifier = Modifier.width(32.dp))
                Box(Modifier.size(230.dp), contentAlignment = Alignment.Center) {
                    TimerRing(progress.value)
                    Box(
                        Modifier.size(140.dp).background(Brush.radialGradient(listOf(Color(0xFFCFE6C3), Color.Transparent)), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) { Heart3D(110.dp) }
                }
                Text("20s", fontSize = 13.sp, modifier = Modifier.width(32.dp), textAlign = TextAlign.End)
            }
            Spacer(Modifier.height(6.dp))
            Text("$secLeft Sec Left", fontSize = 13.sp)
            Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Main Overview", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                MoreButton(listOf("Semua metrik" to { go("metrics") }), bg = Color(0xFFECDCCB))
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Reveal(modifier = Modifier.weight(1f)) { OverviewCard("Oxygen", "98%", Icons.Default.Air, Color(0xFFEC4899), Color(0xFFF6DFF0), CardWhite) { go("detail/Oxygen") } }
                Reveal(150, Modifier.weight(1f)) { OverviewCard("Cholesterol", "172", Icons.Default.Science, Color(0xFF64748B), Color(0xFFE1E8E4), Color(0xFFF3E4D5)) { go("detail/Cholesterol") } }
            }
            Spacer(Modifier.height(20.dp))
            Text("Riwayat Pengukuran", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            if (com.healthku.app.ui.asclepios.Store.readings.isEmpty()) Text("Belum ada riwayat. Geser tombol di bawah untuk mulai.", fontSize = 12.sp, color = Muted)
            com.healthku.app.ui.asclepios.Store.readings.forEach { r ->
                Reveal {
                    Row(Modifier.padding(bottom = 8.dp).fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(CardWhite).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(r.label, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        Text("${r.sys}/${r.dia} mmHg", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
        SwipeToStart(
            running = running, done = progress.value >= 1f,
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(16.dp),
        ) {
            running = true
            scope.launch {
                progress.snapTo(0f)
                progress.animateTo(1f, tween(15000, easing = LinearEasing))
                running = false
            }
        }
    }
}

@Composable
private fun OverviewCard(name: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color, iconBg: Color, bg: Color, onClick: () -> Unit) {
    Column(Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(26.dp)).background(bg).clickable(onClick = onClick).padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleIcon(icon, tint, iconBg)
            Text(name, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp), maxLines = 1)
        }
        Spacer(Modifier.weight(1f))
        Text(value, fontSize = 26.sp, fontWeight = FontWeight.SemiBold)
    }
}

/** Symmetric rounded-square timer: starts at top-centre, black arc runs counter-clockwise to the right handle. */
@Composable
private fun TimerRing(progress: Float) {
    Canvas(Modifier.size(200.dp)) {
        val sw = 7.dp.toPx(); val inset = 14.dp.toPx(); val handle = 12.dp.toPx()
        val l = inset; val t = inset; val r = size.width - inset; val b = size.height - inset
        val cx = size.width / 2; val cy = size.height / 2; val rad = (r - l) * 0.24f
        fun corner(x: Float, y: Float) = Rect(x, y, x + 2 * rad, y + 2 * rad)

        val black = Path().apply {
            moveTo(cx, t); lineTo(l + rad, t)
            arcTo(corner(l, t), 270f, -90f, false); lineTo(l, b - rad)
            arcTo(corner(l, b - 2 * rad), 180f, -90f, false); lineTo(r - rad, b)
            arcTo(corner(r - 2 * rad, b - 2 * rad), 90f, -90f, false); lineTo(r, cy)
        }
        val track = Path().apply {
            moveTo(cx, t); lineTo(l + rad, t)
            arcTo(corner(l, t), 270f, -90f, false); lineTo(l, b - rad)
            arcTo(corner(l, b - 2 * rad), 180f, -90f, false); lineTo(r - rad, b)
            arcTo(corner(r - 2 * rad, b - 2 * rad), 90f, -90f, false); lineTo(r, t + rad)
            arcTo(corner(r - 2 * rad, t), 0f, -90f, false); close()
        }
        drawPath(track, Color(0xFFD3CDC4), style = Stroke(sw, cap = StrokeCap.Round, join = StrokeJoin.Round))

        val pm = PathMeasure().apply { setPath(black, false) }
        val seg = Path()
        pm.getSegment(0f, pm.length * progress, seg, true)
        drawPath(seg, Ink, style = Stroke(sw, cap = StrokeCap.Round, join = StrokeJoin.Round))

        fun handleAt(p: Offset, filled: Boolean) {
            val tl = Offset(p.x - handle / 2, p.y - handle / 2)
            if (filled) {
                drawRoundRect(Color.White, tl - Offset(2f, 2f), Size(handle + 4f, handle + 4f), CornerRadius(4.dp.toPx()))
                drawRoundRect(Ink, tl, Size(handle, handle), CornerRadius(3.dp.toPx()))
            } else drawRoundRect(Ink.copy(alpha = .25f), tl, Size(handle, handle), CornerRadius(3.dp.toPx()), style = Stroke(2f))
        }
        handleAt(Offset(r, cy), false)
        handleAt(Offset(cx, t), true)
        handleAt(pm.getPosition(pm.length * progress), true)
    }
}

@Composable
private fun SwipeToStart(running: Boolean, done: Boolean, modifier: Modifier, onStart: () -> Unit) {
    val knob = 52.dp
    val density = LocalDensity.current
    var trackPx by remember { mutableFloatStateOf(0f) }
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val maxPx = (trackPx - with(density) { (knob + 8.dp).toPx() }).coerceAtLeast(1f)
    val nudge by rememberInfiniteTransition(label = "n").animateFloat(
        0f, 6f, infiniteRepeatable(tween(700), RepeatMode.Reverse), label = "nx",
    )
    Box(
        modifier.fillMaxWidth().height(60.dp).clip(CircleShape).background(Ink)
            .androidxOnSize { trackPx = it },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(Modifier.fillMaxHeight().width(with(density) { (offset.value + knob.toPx() + 8.dp.toPx()).toDp() }).background(Color.White.copy(alpha = .1f)))
        Text(
            if (running) "Reading in progress..." else if (done) "Done - swipe again" else "Swipe to start reading",
            color = Color.White, fontSize = 13.sp, modifier = Modifier.padding(start = 76.dp),
        )
        Row(Modifier.align(Alignment.CenterEnd).padding(end = 14.dp).offset(x = nudge.dp)) {
            repeat(2) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.White.copy(alpha = if (it == 0) .5f else 1f), modifier = Modifier.size(22.dp).offset(x = (-8 * it).dp)) }
        }
        Box(
            Modifier.padding(4.dp).offset { IntOffset(offset.value.roundToInt(), 0) }.size(knob).clip(CircleShape).background(Color.White)
                .pointerInput(running) {
                    if (running) return@pointerInput
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                val finished = offset.value > maxPx * 0.8f
                                offset.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                                if (finished) onStart()
                            }
                        },
                    ) { change, delta ->
                        change.consume()
                        scope.launch { offset.snapTo((offset.value + delta).coerceIn(0f, maxPx)) }
                    }
                },
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Ink, modifier = Modifier.size(26.dp)) }
    }
}

private fun Modifier.androidxOnSize(cb: (Float) -> Unit) =
    this.then(Modifier.onSizeChanged { cb(it.width.toFloat()) })
