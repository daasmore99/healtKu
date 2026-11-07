package com.healthku.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.healthku.app.ui.theme.LumiAmber
import com.healthku.app.ui.theme.LumiCoral
import com.healthku.app.ui.theme.LumiLilac
import com.healthku.app.ui.theme.LumiPeriwinkle

@Composable
fun LumiOrb(
    modifier: Modifier = Modifier,
    sizeDp: Dp = 120.dp,
    primaryColor: Color = LumiPeriwinkle,
    secondaryColor: Color = LumiAmber,
    content: @Composable () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LumiOrbBreathing")

    // Gentle breathing scale
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbScale"
    )

    // Ambient glow pulse
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbGlow"
    )

    // Center offset gentle shift
    val offsetShift by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbShift"
    )

    Box(
        modifier = modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(sizeDp)) {
            val w = size.width
            val h = size.height
            val center = Offset(w / 2f + offsetShift, h / 2f - offsetShift)
            val baseRadius = (w.coerceAtMost(h) / 2f) * scale

            // 1. Outer Ambient Aura
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = glowAlpha * 0.5f),
                        secondaryColor.copy(alpha = glowAlpha * 0.25f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = baseRadius * 1.25f
                ),
                radius = baseRadius * 1.25f,
                center = center
            )

            // 2. Middle Chromatic Body
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.9f),
                        LumiLilac.copy(alpha = 0.85f),
                        primaryColor.copy(alpha = 0.75f),
                        secondaryColor.copy(alpha = 0.5f),
                        Color.Transparent
                    ),
                    center = Offset(center.x - baseRadius * 0.15f, center.y - baseRadius * 0.15f),
                    radius = baseRadius
                ),
                radius = baseRadius,
                center = center
            )

            // 3. Inner Ethereal Highlight
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        LumiLilac.copy(alpha = 0.6f),
                        Color.Transparent
                    ),
                    center = Offset(center.x - baseRadius * 0.22f, center.y - baseRadius * 0.22f),
                    radius = baseRadius * 0.55f
                ),
                radius = baseRadius * 0.55f,
                center = Offset(center.x - baseRadius * 0.22f, center.y - baseRadius * 0.22f)
            )
        }

        content()
    }
}
