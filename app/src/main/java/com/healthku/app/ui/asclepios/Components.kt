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

/** Rounded icon container with pastel background using custom Painter / Drawable. */
@Composable
fun CircleIcon(painter: androidx.compose.ui.graphics.painter.Painter, tint: Color, bg: Color, size: Dp = 32.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Icon(painter, null, tint = tint, modifier = Modifier.size(size * 0.52f))
    }
}

/** Rounded icon container with pastel background using ImageVector. */
@Composable
fun CircleIcon(icon: ImageVector, tint: Color, bg: Color, size: Dp = 32.dp) {
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
