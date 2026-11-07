package com.healthku.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnatomicalHeartGraphic(
    modifier: Modifier = Modifier,
    sizeDp: Dp = 80.dp,
    showGlow: Boolean = true,
    glowColor: Color = Color(0xFFC7E2CE)
) {
    Box(
        modifier = modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(sizeDp)) {
            val w = size.width
            val h = size.height

            // 1. Soft Ambient Radial Glow
            if (showGlow) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            glowColor.copy(alpha = 0.85f),
                            glowColor.copy(alpha = 0.4f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.5f, h * 0.52f),
                        radius = w * 0.48f
                    ),
                    radius = w * 0.48f,
                    center = Offset(w * 0.5f, h * 0.52f)
                )
            }

            // 2. Aorta Arch (Top vascular tubes)
            val aortaPath = Path().apply {
                moveTo(w * 0.44f, h * 0.32f)
                cubicTo(w * 0.42f, h * 0.16f, w * 0.58f, h * 0.14f, w * 0.62f, h * 0.28f)
                lineTo(w * 0.54f, h * 0.35f)
                close()
            }
            drawPath(
                path = aortaPath,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFC44B40), Color(0xFF8B251E)),
                    start = Offset(w * 0.44f, h * 0.14f),
                    end = Offset(w * 0.62f, h * 0.35f)
                )
            )

            // Superior Vena Cava / Pulmonary Artery
            val venaPath = Path().apply {
                moveTo(w * 0.34f, h * 0.30f)
                cubicTo(w * 0.32f, h * 0.18f, w * 0.42f, h * 0.18f, w * 0.44f, h * 0.32f)
                lineTo(w * 0.38f, h * 0.36f)
                close()
            }
            drawPath(
                path = venaPath,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF4A6B82), Color(0xFF2C4354)),
                    start = Offset(w * 0.32f, h * 0.18f),
                    end = Offset(w * 0.44f, h * 0.36f)
                )
            )

            // 3. Main Ventricle Muscle Body
            val ventriclePath = Path().apply {
                moveTo(w * 0.35f, h * 0.36f)
                cubicTo(w * 0.20f, h * 0.42f, w * 0.24f, h * 0.68f, w * 0.46f, h * 0.86f)
                cubicTo(w * 0.50f, h * 0.89f, w * 0.54f, h * 0.88f, w * 0.58f, h * 0.82f)
                cubicTo(w * 0.78f, h * 0.65f, w * 0.80f, h * 0.40f, w * 0.64f, h * 0.34f)
                cubicTo(w * 0.52f, h * 0.30f, w * 0.42f, h * 0.32f, w * 0.35f, h * 0.36f)
                close()
            }
            drawPath(
                path = ventriclePath,
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFD6584E),
                        Color(0xFFB13930),
                        Color(0xFF6E201B)
                    ),
                    center = Offset(w * 0.48f, h * 0.52f),
                    radius = w * 0.40f
                )
            )

            // 4. Muscle Texture & Shading Highlight
            val highlightPath = Path().apply {
                moveTo(w * 0.38f, h * 0.42f)
                cubicTo(w * 0.30f, h * 0.48f, w * 0.34f, h * 0.66f, w * 0.48f, h * 0.78f)
                cubicTo(w * 0.44f, h * 0.65f, w * 0.40f, h * 0.52f, w * 0.38f, h * 0.42f)
                close()
            }
            drawPath(
                path = highlightPath,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFEF8B82).copy(alpha = 0.7f), Color.Transparent)
                )
            )

            // 5. Coronary Arteries / Veins (Fine branch lines)
            val vesselPath = Path().apply {
                moveTo(w * 0.48f, h * 0.40f)
                cubicTo(w * 0.46f, h * 0.52f, w * 0.50f, h * 0.62f, w * 0.47f, h * 0.76f)
                moveTo(w * 0.47f, h * 0.54f)
                lineTo(w * 0.56f, h * 0.62f)
                moveTo(w * 0.48f, h * 0.62f)
                lineTo(w * 0.42f, h * 0.68f)
            }
            drawPath(
                path = vesselPath,
                color = Color(0xFF4A7C9B),
                style = Stroke(width = w * 0.024f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }
    }
}
