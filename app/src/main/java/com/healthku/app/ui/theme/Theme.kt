package com.healthku.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LumiDarkColorScheme = darkColorScheme(
    primary = LumiPeriwinkle,
    onPrimary = LumiTextPrimary,
    secondary = LumiAmber,
    onSecondary = LumiCanvasDark,
    tertiary = LumiCoral,
    background = LumiCanvasDark,
    surface = LumiCardDark,
    onBackground = LumiTextPrimary,
    onSurface = LumiTextPrimary,
    surfaceVariant = LumiCardSurfaceHover,
    outline = LumiCardBorderDark
)

@Composable
fun HealthKuTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LumiDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = LumiCanvasDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
