package com.healthku.app.ui.asclepios

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.R
import kotlinx.coroutines.delay

enum class HealthIconType {
    HEART_RATE,
    BLOOD_PRESSURE,
    OXYGEN,
    CHOLESTEROL,
    CALORIES,
    STEPS,
    WATER,
    SLEEP,
    SMARTWATCH
}

/** Fade + subtle slide-up when the item enters composition. */
@Composable
fun Reveal(delayMs: Int = 0, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(delayMs.toLong())
        progress.animateTo(1f, tween(500, easing = FastOutSlowInEasing))
    }
    Box(
        modifier = modifier.graphicsLayer {
            alpha = progress.value
            translationY = (1f - progress.value) * 32f
            val s = 0.98f + 0.02f * progress.value
            scaleX = s; scaleY = s
        },
    ) { content() }
}

/** Asclepios tactile squircle action button matching reference design. */
@Composable
fun SqButton(
    icon: ImageVector,
    size: Dp = 44.dp,
    tint: Color = Ink,
    bg: Color = CardWhite,
    borderColor: Color = CardBorder,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(size * 0.44f))
    }
}

/** Three-dots context menu button. */
@Composable
fun MoreButton(
    items: List<Pair<String, () -> Unit>>,
    size: Dp = 32.dp,
    bg: Color = CardWhite,
    borderColor: Color = CardBorder
) {
    var open by remember { mutableStateOf(false) }
    Box {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(10.dp))
                .background(bg)
                .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                .clickable { open = true },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.MoreHoriz, null, tint = Ink, modifier = Modifier.size(size * 0.6f))
        }
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            items.forEach { (label, action) ->
                DropdownMenuItem(
                    text = { Text(label, fontSize = 13.sp) },
                    onClick = { open = false; action() }
                )
            }
        }
    }
}

/** Custom Health Vector Icons rendered natively on Canvas with smooth rounded anti-aliased geometry. */
@Composable
fun HealthIcon(
    type: HealthIconType,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = (w * 0.09f).coerceAtLeast(1.8f.dp.toPx()), cap = StrokeCap.Round, join = StrokeJoin.Round)

        when (type) {
            HealthIconType.HEART_RATE -> {
                val heart = Path().apply {
                    moveTo(w * 0.5f, h * 0.85f)
                    cubicTo(w * 0.15f, h * 0.62f, w * 0.05f, h * 0.35f, w * 0.22f, h * 0.18f)
                    cubicTo(w * 0.36f, h * 0.08f, w * 0.46f, h * 0.20f, w * 0.5f, h * 0.30f)
                    cubicTo(w * 0.54f, h * 0.20f, w * 0.64f, h * 0.08f, w * 0.78f, h * 0.18f)
                    cubicTo(w * 0.95f, h * 0.35f, w * 0.85f, h * 0.62f, w * 0.5f, h * 0.85f)
                    close()
                }
                drawPath(heart, color = tint.copy(alpha = 0.28f), style = Stroke(width = stroke.width * 0.75f, cap = StrokeCap.Round))
                val ecg = Path().apply {
                    moveTo(w * 0.10f, h * 0.52f)
                    lineTo(w * 0.32f, h * 0.52f)
                    lineTo(w * 0.42f, h * 0.28f)
                    lineTo(w * 0.56f, h * 0.74f)
                    lineTo(w * 0.66f, h * 0.42f)
                    lineTo(w * 0.74f, h * 0.52f)
                    lineTo(w * 0.90f, h * 0.52f)
                }
                drawPath(ecg, color = tint, style = stroke)
            }
            HealthIconType.BLOOD_PRESSURE -> {
                val tube = Path().apply {
                    moveTo(w * 0.24f, h * 0.15f)
                    lineTo(w * 0.24f, h * 0.38f)
                    cubicTo(w * 0.24f, h * 0.64f, w * 0.76f, h * 0.64f, w * 0.76f, h * 0.38f)
                    lineTo(w * 0.76f, h * 0.15f)
                }
                drawPath(tube, color = tint, style = stroke)
                drawLine(tint, Offset(w * 0.18f, h * 0.18f), Offset(w * 0.30f, h * 0.18f), strokeWidth = stroke.width)
                drawLine(tint, Offset(w * 0.70f, h * 0.18f), Offset(w * 0.82f, h * 0.18f), strokeWidth = stroke.width)
                val stem = Path().apply {
                    moveTo(w * 0.5f, h * 0.62f)
                    lineTo(w * 0.5f, h * 0.78f)
                }
                drawPath(stem, color = tint, style = stroke)
                drawCircle(color = tint, radius = w * 0.13f, center = Offset(w * 0.5f, h * 0.84f), style = stroke)
            }
            HealthIconType.CALORIES -> {
                val flame = Path().apply {
                    moveTo(w * 0.5f, h * 0.10f)
                    cubicTo(w * 0.66f, h * 0.32f, w * 0.85f, h * 0.54f, w * 0.85f, h * 0.72f)
                    cubicTo(w * 0.85f, h * 0.92f, w * 0.68f, h * 0.96f, w * 0.5f, h * 0.96f)
                    cubicTo(w * 0.32f, h * 0.96f, w * 0.15f, h * 0.92f, w * 0.15f, h * 0.72f)
                    cubicTo(w * 0.15f, h * 0.54f, w * 0.34f, h * 0.32f, w * 0.5f, h * 0.10f)
                    close()
                }
                drawPath(flame, color = tint, style = stroke)
                val core = Path().apply {
                    moveTo(w * 0.5f, h * 0.56f)
                    cubicTo(w * 0.62f, h * 0.68f, w * 0.70f, h * 0.78f, w * 0.70f, h * 0.85f)
                    cubicTo(w * 0.70f, h * 0.94f, w * 0.60f, h * 0.96f, w * 0.5f, h * 0.96f)
                    cubicTo(w * 0.40f, h * 0.96f, w * 0.30f, h * 0.94f, w * 0.30f, h * 0.85f)
                    cubicTo(w * 0.30f, h * 0.78f, w * 0.38f, h * 0.68f, w * 0.5f, h * 0.56f)
                    close()
                }
                drawPath(core, color = tint)
            }
            HealthIconType.STEPS -> {
                val left = Path().apply {
                    moveTo(w * 0.32f, h * 0.44f)
                    cubicTo(w * 0.18f, h * 0.44f, w * 0.18f, h * 0.18f, w * 0.32f, h * 0.12f)
                    cubicTo(w * 0.46f, h * 0.18f, w * 0.46f, h * 0.44f, w * 0.32f, h * 0.44f)
                }
                drawPath(left, color = tint, style = stroke)
                drawCircle(color = tint, radius = w * 0.075f, center = Offset(w * 0.32f, h * 0.60f))

                val right = Path().apply {
                    moveTo(w * 0.68f, h * 0.70f)
                    cubicTo(w * 0.54f, h * 0.70f, w * 0.54f, h * 0.44f, w * 0.68f, h * 0.38f)
                    cubicTo(w * 0.82f, h * 0.44f, w * 0.82f, h * 0.70f, w * 0.68f, h * 0.70f)
                }
                drawPath(right, color = tint, style = stroke)
                drawCircle(color = tint, radius = w * 0.075f, center = Offset(w * 0.68f, h * 0.86f))
            }
            HealthIconType.OXYGEN -> {
                val wave1 = Path().apply {
                    moveTo(w * 0.12f, h * 0.34f)
                    cubicTo(w * 0.35f, h * 0.20f, w * 0.55f, h * 0.46f, w * 0.88f, h * 0.32f)
                }
                val wave2 = Path().apply {
                    moveTo(w * 0.12f, h * 0.54f)
                    cubicTo(w * 0.40f, h * 0.40f, w * 0.60f, h * 0.66f, w * 0.88f, h * 0.52f)
                }
                val wave3 = Path().apply {
                    moveTo(w * 0.20f, h * 0.74f)
                    cubicTo(w * 0.45f, h * 0.64f, w * 0.65f, h * 0.86f, w * 0.82f, h * 0.72f)
                }
                drawPath(wave1, color = tint, style = stroke)
                drawPath(wave2, color = tint, style = stroke)
                drawPath(wave3, color = tint, style = stroke)
            }
            HealthIconType.CHOLESTEROL -> {
                val flask = Path().apply {
                    moveTo(w * 0.40f, h * 0.14f)
                    lineTo(w * 0.60f, h * 0.14f)
                    moveTo(w * 0.45f, h * 0.14f)
                    lineTo(w * 0.45f, h * 0.38f)
                    lineTo(w * 0.18f, h * 0.85f)
                    cubicTo(w * 0.16f, h * 0.90f, w * 0.24f, h * 0.92f, w * 0.50f, h * 0.92f)
                    cubicTo(w * 0.76f, h * 0.92f, w * 0.84f, h * 0.90f, w * 0.82f, h * 0.85f)
                    lineTo(w * 0.55f, h * 0.38f)
                    lineTo(w * 0.55f, h * 0.14f)
                }
                drawPath(flask, color = tint, style = stroke)
                drawLine(tint, Offset(w * 0.28f, h * 0.72f), Offset(w * 0.72f, h * 0.72f), strokeWidth = stroke.width)
            }
            HealthIconType.WATER -> {
                val drop = Path().apply {
                    moveTo(w * 0.5f, h * 0.14f)
                    cubicTo(w * 0.76f, h * 0.44f, w * 0.84f, h * 0.64f, w * 0.82f, h * 0.76f)
                    cubicTo(w * 0.78f, h * 0.94f, w * 0.22f, h * 0.94f, w * 0.18f, h * 0.76f)
                    cubicTo(w * 0.16f, h * 0.64f, w * 0.24f, h * 0.44f, w * 0.5f, h * 0.14f)
                    close()
                }
                drawPath(drop, color = tint, style = stroke)
            }
            HealthIconType.SLEEP -> {
                val moon = Path().apply {
                    moveTo(w * 0.68f, h * 0.16f)
                    cubicTo(w * 0.32f, h * 0.24f, w * 0.22f, h * 0.62f, w * 0.45f, h * 0.88f)
                    cubicTo(w * 0.18f, h * 0.70f, w * 0.20f, h * 0.32f, w * 0.58f, h * 0.14f)
                    close()
                }
                drawPath(moon, color = tint)
                drawLine(tint, Offset(w * 0.72f, h * 0.36f), Offset(w * 0.84f, h * 0.36f), strokeWidth = stroke.width * 0.8f)
                drawLine(tint, Offset(w * 0.78f, h * 0.30f), Offset(w * 0.78f, h * 0.42f), strokeWidth = stroke.width * 0.8f)
            }
            HealthIconType.SMARTWATCH -> {
                val strapTop = Path().apply {
                    moveTo(w * 0.35f, h * 0.26f)
                    lineTo(w * 0.35f, h * 0.10f)
                    lineTo(w * 0.65f, h * 0.10f)
                    lineTo(w * 0.65f, h * 0.26f)
                }
                val strapBottom = Path().apply {
                    moveTo(w * 0.35f, h * 0.74f)
                    lineTo(w * 0.35f, h * 0.90f)
                    lineTo(w * 0.65f, h * 0.90f)
                    lineTo(w * 0.65f, h * 0.74f)
                }
                drawPath(strapTop, color = tint, style = stroke)
                drawPath(strapBottom, color = tint, style = stroke)
                drawCircle(color = tint, radius = w * 0.30f, center = Offset(w * 0.5f, h * 0.5f), style = stroke)
                drawLine(tint, Offset(w * 0.5f, h * 0.5f), Offset(w * 0.5f, h * 0.36f), strokeWidth = stroke.width)
                drawLine(tint, Offset(w * 0.5f, h * 0.5f), Offset(w * 0.62f, h * 0.58f), strokeWidth = stroke.width)
            }
        }
    }
}

/** Rounded icon container with custom HealthIconType. */
@Composable
fun CircleIcon(
    type: HealthIconType,
    tint: Color,
    bg: Color,
    size: Dp = 32.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        HealthIcon(type = type, tint = tint, modifier = Modifier.size(size * 0.56f))
    }
}

/** Rounded icon container with standard ImageVector fallback. */
@Composable
fun CircleIcon(
    icon: ImageVector,
    tint: Color,
    bg: Color,
    size: Dp = 32.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(size * 0.52f))
    }
}

/** Section header with bold title and trailing action. */
@Composable
fun SectionHeader(title: String, trailing: @Composable () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
            modifier = Modifier.weight(1f)
        )
        trailing()
    }
}

/** Authentic 3D Anatomical Heart with organic gentle heartbeat pulse. */
@Composable
fun Heart3D(
    size: Dp,
    modifier: Modifier = Modifier
) {
    val beat by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1400
                1f at 0
                1.06f at 220
                1f at 420
                1.04f at 620
                1f at 820
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "heartbeat"
    )
    Image(
        painter = painterResource(id = R.drawable.heart_3d),
        contentDescription = "3D Anatomical Heart",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(size)
            .scale(beat)
    )
}

/** Sparkline wave curve for mini metric trends. */
@Composable
fun SparklineWave(
    color: Color,
    modifier: Modifier = Modifier.size(width = 44.dp, height = 18.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(0f, h * 0.6f)
            quadraticBezierTo(w * 0.2f, h * 0.7f, w * 0.35f, h * 0.5f)
            lineTo(w * 0.45f, h * 0.1f)
            lineTo(w * 0.55f, h * 0.95f)
            lineTo(w * 0.65f, h * 0.4f)
            quadraticBezierTo(w * 0.8f, h * 0.5f, w, h * 0.5f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}
