package com.healthku.app.ui.asclepios

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Cream = Color(0xFFF3E8DC)
val CreamDeep = Color(0xFFF6EAD9)
val CardWhite = Color(0xFFFFFAF0)
val Forest = Color(0xFF2E5A43)
val Ink = Color(0xFF111111)
val Muted = Color(0xFF7A7A7A)
val Olive = Color(0xFF8A9A52)
val Amber = Color(0xFFE8A35C)

@Composable
fun AsclepiosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(primary = Forest, background = Cream, surface = CardWhite, onSurface = Ink),
        content = content,
    )
}
