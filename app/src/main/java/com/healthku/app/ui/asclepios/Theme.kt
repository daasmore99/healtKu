package com.healthku.app.ui.asclepios

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Asclepios Color Palette - WCAG AA compliant, authentic design tokens matching reference
val AsclepiosBg = Color(0xFFF7F4EB)
val AsclepiosBgDeep = Color(0xFFECE5D8)
val Cream = Color(0xFFF7F4EB)
val CreamDeep = Color(0xFFECE5D8)
val CardWhite = Color(0xFFFFFFFF)
val CardBorder = Color(0xFFEBE5D8)
val SageGreen = Color(0xFF265135)
val SagePillBg = Color(0xFFD8EAD9)
val Forest = Color(0xFF265135)
val Ink = Color(0xFF191919)
val Muted = Color(0xFF8E8A82)
val Olive = Color(0xFF58A367)
val Amber = Color(0xFFE58A3A)

@Composable
fun AsclepiosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Forest,
            background = AsclepiosBg,
            surface = CardWhite,
            onSurface = Ink
        ),
        content = content,
    )
}
