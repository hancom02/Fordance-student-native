package com.fordance.student.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = FordancePrimary,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = FordancePrimaryLight,
    surface = FordanceSurface,
    onSurface = FordanceOnSurface,
    background = FordanceBackground,
    onBackground = TextPrimary,
    error = ErrorColor
)

private val DarkColorScheme = darkColorScheme(
    primary = FordancePrimary,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = FordancePrimaryDark,
    surface = androidx.compose.ui.graphics.Color(0xFF121212),
    onSurface = androidx.compose.ui.graphics.Color.White,
    background = androidx.compose.ui.graphics.Color(0xFF000000),
    onBackground = androidx.compose.ui.graphics.Color.White,
    error = ErrorColor
)

@Composable
fun ForDanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
