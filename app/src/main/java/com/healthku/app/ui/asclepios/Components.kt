package com.healthku.app.ui.asclepios

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/** Fade + slide-up when the item first enters composition (i.e. scrolls into view in a Lazy list). */
@Composable
fun Reveal(delayMs: Int = 0, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(delayMs.toLong())
        progress.animateTo(1f, tween(600, easing = FastOutSlowInEasing))
    }
    Box(
        modifier.graphicsLayer {
            alpha = progress.value
            translationY = (1f - progress.value) * 48f
            val s = 0.97f + 0.03f * progress.value
            scaleX = s; scaleY = s
        },
    ) { content() }
}

@Composable
fun SqButton(icon: ImageVector, size: Dp = 44.dp, tint: Color = Ink, bg: Color = CreamDeep, onClick: () -> Unit = {}) {
    Box(
        Modifier.size(size).clip(RoundedCornerShape(16.dp)).background(bg).clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) { Icon(icon, null, tint = tint, modifier = Modifier.size(size * 0.45f)) }
}

@Composable
fun MoreButton(items: List<Pair<String, () -> Unit>>, size: Dp = 26.dp, bg: Color = Color.White) {
    var open by remember { mutableStateOf(false) }
    Box {
        SqButton(Icons.Default.MoreHoriz, size, bg = bg) { open = true }
        androidx.compose.material3.DropdownMenu(open, { open = false }) {
            items.forEach { (label, action) ->
                androidx.compose.material3.DropdownMenuItem(text = { Text(label) }, onClick = { open = false; action() })
            }
        }
    }
}

@Composable
fun CircleIcon(icon: ImageVector, tint: Color, bg: Color, size: Dp = 32.dp) {
    Box(Modifier.size(size).clip(CircleShape).background(bg), contentAlignment = Alignment.Center) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(size * 0.5f))
    }
}

@Composable
fun SectionHeader(title: String, trailing: @Composable () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 17.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold, modifier = Modifier.weight(1f))
        trailing()
    }
}

@Composable
fun Heart3D(size: Dp, green: Boolean = true, modifier: Modifier = Modifier) {
    val beat by rememberInfiniteTransition(label = "beat").animateFloat(
        1f, 1.08f,
        infiniteRepeatable(keyframes { durationMillis = 1200; 1f at 0; 1.08f at 180; 1f at 360; 1.05f at 540; 1f at 720 }),
        label = "scale",
    )
    val c = if (green) listOf(Color(0xFF9DB591), Color(0xFF3F5A3D), Color(0xFF1F2F1F))
    else listOf(Color(0xFFF08A7E), Color(0xFFB3262A), Color(0xFF5C0F14))
    Canvas(modifier.size(size).scale(beat)) {
        val w = this.size.width; val h = this.size.height
        val p = Path().apply {
            moveTo(w * .5f, h * .92f)
            cubicTo(w * .20f, h * .70f, w * .06f, h * .52f, w * .08f, h * .34f)
            cubicTo(w * .10f, h * .18f, w * .24f, h * .10f, w * .36f, h * .14f)
            cubicTo(w * .42f, h * .16f, w * .47f, h * .20f, w * .50f, h * .26f)
            cubicTo(w * .53f, h * .20f, w * .58f, h * .16f, w * .64f, h * .14f)
            cubicTo(w * .76f, h * .10f, w * .90f, h * .18f, w * .92f, h * .34f)
            cubicTo(w * .94f, h * .52f, w * .80f, h * .70f, w * .5f, h * .92f)
            close()
        }
        drawPath(p, Brush.radialGradient(c, center = Offset(w * .35f, h * .30f), radius = w * .8f))
        val stroke = androidx.compose.ui.graphics.drawscope.Stroke(width = w * .05f, cap = StrokeCap.Round)
        drawPath(Path().apply { moveTo(w * .44f, h * .18f); cubicTo(w * .42f, h * .08f, w * .48f, h * .04f, w * .53f, h * .04f) }, c[2], style = stroke)
        drawPath(Path().apply { moveTo(w * .58f, h * .20f); cubicTo(w * .62f, h * .12f, w * .70f, h * .12f, w * .72f, h * .16f) }, c[2], style = stroke)
        drawPath(Path().apply { moveTo(w * .30f, h * .40f); cubicTo(w * .36f, h * .50f, w * .40f, h * .64f, w * .48f, h * .76f) }, c[2].copy(alpha = .5f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * .02f))
    }
}
