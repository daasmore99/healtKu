package com.healthku.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WaveSparkline(
    data: List<Float>,
    lineColor: Color,
    modifier: Modifier = Modifier,
    width: Dp = 60.dp,
    height: Dp = 28.dp,
    strokeWidth: Float = 5f
) {
    if (data.size < 2) return

    val minVal = data.minOrNull() ?: 0f
    val maxVal = data.maxOrNull() ?: 1f
    val range = if (maxVal - minVal == 0f) 1f else maxVal - minVal

    Canvas(
        modifier = modifier
            .width(width)
            .height(height)
    ) {
        val w = size.width
        val h = size.height
        val stepX = w / (data.size - 1)

        val path = Path()

        data.forEachIndexed { i, value ->
            val normalizedY = 1f - ((value - minVal) / range)
            val x = i * stepX
            val y = (normalizedY * (h - 12f)) + 6f

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                val prevVal = data[i - 1]
                val prevNormalizedY = 1f - ((prevVal - minVal) / range)
                val prevX = (i - 1) * stepX
                val prevY = (prevNormalizedY * (h - 12f)) + 6f

                val controlX1 = prevX + (stepX / 2f)
                val controlY1 = prevY
                val controlX2 = prevX + (stepX / 2f)
                val controlY2 = y

                path.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
