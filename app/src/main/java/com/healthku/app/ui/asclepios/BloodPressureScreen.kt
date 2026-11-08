package com.healthku.app.ui.asclepios

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
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
    val ctx = LocalContext.current
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }
    val secLeft = kotlin.math.ceil(15 - progress.value * 15).toInt()
    val sys = 121 + (sin(progress.value * 40f) * 2).roundToInt()

    // Save reading to history when measurement completes
    LaunchedEffect(progress.value >= 1f) {
        if (progress.value >= 1f) {
            Store.readings.add(0, Reading("Baru saja", sys, 80))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AsclepiosBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SqButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    size = 44.dp,
                    onClick = onBack
                )
                Text(
                    text = "Blood Pressure",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                MoreButton(
                    items = listOf(
                        "Bagikan hasil" to { shareText(ctx, "Tekanan darah saya: $sys/80 mmHg") },
                        "Hapus riwayat" to { Store.readings.clear() },
                        "Tentang tekanan darah" to { openUrl(ctx, Store.metric("Blood Pressure").url) },
                    ),
                    size = 44.dp
                )
            }

            Spacer(Modifier.height(20.dp))

            // Big Numerical Display
            Text(
                text = "$sys/80",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                letterSpacing = (-1).sp
            )
            Text(
                text = "SYSmm/hg",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Muted
            )

            Spacer(Modifier.height(20.dp))

            // Squircle Gauge Dial
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("15s", fontSize = 12.sp, color = Muted, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("0s", fontSize = 12.sp, color = Muted, fontWeight = FontWeight.Medium, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                    Box(
                        modifier = Modifier.size(210.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TimerRing(progress.value)
                        // Soft green circular glow + 3D Heart
                        Box(
                            modifier = Modifier
                                .size(136.dp)
                                .clip(CircleShape)
                                .background(SagePillBg),
                            contentAlignment = Alignment.Center,
                        ) {
                            Heart3D(size = 94.dp)
                        }
                    }
                    Text("20s", fontSize = 12.sp, color = Muted, fontWeight = FontWeight.Medium, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = if (running) "$secLeft Sec Left" else "15 Sec Left",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Ink
                )
            }

            Spacer(Modifier.height(24.dp))

            // Main Overview Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Main Overview",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink,
                    modifier = Modifier.weight(1f)
                )
                MoreButton(listOf("Semua metrik" to { go("metrics") }), size = 32.dp)
            }

            Spacer(Modifier.height(12.dp))

            // 2 Overview Cards Side by Side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Reveal(modifier = Modifier.weight(1f)) {
                    OverviewCard(
                        name = "Oxygen",
                        value = "98 %",
                        type = HealthIconType.OXYGEN,
                        tint = Color(0xFFC85B7A),
                        iconBg = Color(0xFFF7E4EA)
                    ) { go("detail/Oxygen") }
                }
                Reveal(100, Modifier.weight(1f)) {
                    OverviewCard(
                        name = "Cholest...",
                        value = "210",
                        type = HealthIconType.CHOLESTEROL,
                        tint = Color(0xFF4B8E9D),
                        iconBg = Color(0xFFE0EFF2)
                    ) { go("detail/Cholesterol") }
                }
            }

            Spacer(Modifier.height(24.dp))

            // History Log Section
            Text(
                text = "Riwayat Pengukuran",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            if (Store.readings.isEmpty()) {
                Text(
                    text = "Belum ada riwayat. Geser tombol di bawah untuk mulai.",
                    fontSize = 12.sp,
                    color = Muted,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Store.readings.forEach { r ->
                Reveal {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(CardWhite)
                            .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(r.label, fontSize = 13.sp, color = Ink, modifier = Modifier.weight(1f))
                        Text("${r.sys}/${r.dia} mmHg", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Ink)
                    }
                }
            }
        }

        // Bottom Action Capsule
        SwipeToStart(
            running = running,
            done = progress.value >= 1f,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
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
private fun OverviewCard(
    name: String,
    value: String,
    type: HealthIconType,
    tint: Color,
    iconBg: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(CardWhite)
            .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleIcon(type = type, tint = tint, bg = iconBg, size = 30.dp)
            Spacer(Modifier.width(8.dp))
            Text(name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Ink, maxLines = 1)
        }
        Spacer(Modifier.weight(1f))
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Ink)
    }
}

/** Rounded-square squircle dial with active progress arc. */
@Composable
private fun TimerRing(progress: Float) {
    Canvas(Modifier.size(200.dp)) {
        val sw = 8.dp.toPx()
        val inset = 14.dp.toPx()
        val handle = 12.dp.toPx()
        val l = inset
        val t = inset
        val r = size.width - inset
        val b = size.height - inset
        val cx = size.width / 2
        val cy = size.height / 2
        val rad = (r - l) * 0.26f

        fun corner(x: Float, y: Float) = Rect(x, y, x + 2 * rad, y + 2 * rad)

        val black = Path().apply {
            moveTo(cx, t)
            lineTo(l + rad, t)
            arcTo(corner(l, t), 270f, -90f, false)
            lineTo(l, b - rad)
            arcTo(corner(l, b - 2 * rad), 180f, -90f, false)
            lineTo(r - rad, b)
            arcTo(corner(r - 2 * rad, b - 2 * rad), 90f, -90f, false)
            lineTo(r, cy)
        }
        val track = Path().apply {
            moveTo(cx, t)
            lineTo(l + rad, t)
            arcTo(corner(l, t), 270f, -90f, false)
            lineTo(l, b - rad)
            arcTo(corner(l, b - 2 * rad), 180f, -90f, false)
            lineTo(r - rad, b)
            arcTo(corner(r - 2 * rad, b - 2 * rad), 90f, -90f, false)
            lineTo(r, t + rad)
            arcTo(corner(r - 2 * rad, t), 0f, -90f, false)
            close()
        }
        drawPath(track, Color(0xFFDCD6C9), style = Stroke(sw, cap = StrokeCap.Round, join = StrokeJoin.Round))

        val pm = PathMeasure().apply { setPath(black, false) }
        val seg = Path()
        pm.getSegment(0f, pm.length * progress, seg, true)
        drawPath(seg, Ink, style = Stroke(sw, cap = StrokeCap.Round, join = StrokeJoin.Round))

        fun handleAt(p: Offset, filled: Boolean) {
            val tl = Offset(p.x - handle / 2, p.y - handle / 2)
            if (filled) {
                drawRoundRect(Color.White, tl - Offset(2f, 2f), Size(handle + 4f, handle + 4f), CornerRadius(4.dp.toPx()))
                drawRoundRect(Ink, tl, Size(handle, handle), CornerRadius(3.dp.toPx()))
            } else {
                drawRoundRect(Ink.copy(alpha = .35f), tl, Size(handle, handle), CornerRadius(3.dp.toPx()), style = Stroke(2f))
            }
        }
        handleAt(Offset(r, cy), false)
        handleAt(Offset(cx, t), true)
        handleAt(pm.getPosition(pm.length * progress), true)
    }
}

/** Bottom Action Slider: Swipe to start reading. */
@Composable
private fun SwipeToStart(running: Boolean, done: Boolean, modifier: Modifier, onStart: () -> Unit) {
    val knob = 48.dp
    val density = LocalDensity.current
    var trackPx by remember { mutableFloatStateOf(0f) }
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val maxPx = (trackPx - with(density) { (knob + 10.dp).toPx() }).coerceAtLeast(1f)
    val nudge by rememberInfiniteTransition(label = "n").animateFloat(
        0f, 6f, infiniteRepeatable(tween(700), RepeatMode.Reverse), label = "nx",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(29.dp))
            .background(Ink)
            .onSizeChanged { trackPx = it.width.toFloat() },
        contentAlignment = Alignment.CenterStart,
    ) {
        // Track fill
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(with(density) { (offset.value + knob.toPx() + 10.dp.toPx()).toDp() })
                .background(Color.White.copy(alpha = .1f))
        )
        // Label
        Text(
            text = if (running) "Reading in progress..." else if (done) "Done - swipe again" else "Swipe to start reading   ›   ›   ›",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 68.dp, end = 16.dp),
            textAlign = TextAlign.Start
        )
        // Draggable Knob Button
        Box(
            modifier = Modifier
                .padding(5.dp)
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .size(knob)
                .clip(CircleShape)
                .background(Color.White)
                .pointerInput(running) {
                    if (running) return@pointerInput
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                val finished = offset.value > maxPx * 0.75f
                                offset.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                                if (finished) onStart()
                            }
                        },
                    ) { change, delta ->
                        change.consume()
                        scope.launch { offset.snapTo((offset.value + delta).coerceIn(0f, maxPx)) }
                    }
                }
                .clickable {
                    if (!running) onStart()
                },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Swipe Arrow",
                tint = Ink,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
