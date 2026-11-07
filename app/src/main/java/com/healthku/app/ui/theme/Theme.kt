package com.healthku.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AsclepiosColorScheme = lightColorScheme(
    primary = SageDark,
    onPrimary = AsclepiosCard,
    secondary = SagePillGreen,
    onSecondary = AsclepiosCard,
    tertiary = MetricBpPink,
    background = AsclepiosBg,
    surface = AsclepiosCard,
    onBackground = AsclepiosTextPrimary,
    onSurface = AsclepiosTextPrimary,
    surfaceVariant = AsclepiosSearchBg,
    outline = AsclepiosCardBorder
)

@Composable
fun HealthKuTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = AsclepiosColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = AsclepiosBg.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
