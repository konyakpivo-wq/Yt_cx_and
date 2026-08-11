package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val YouTubeDarkColorScheme = darkColorScheme(
    primary = YTRed,
    onPrimary = Color.White,
    primaryContainer = YTRedDark,
    onPrimaryContainer = Color.White,
    secondary = YTAmber,
    onSecondary = Color.Black,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkBorder
)

private val YouTubeLightColorScheme = lightColorScheme(
    primary = YTRed,
    onPrimary = Color.White,
    primaryContainer = YTRedDark,
    onPrimaryContainer = Color.White,
    secondary = YTAmber,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightBorder
)

@Composable
fun YouTubeCxTheme(
    darkTheme: Boolean = true, // Default to Youtube sleek Dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) YouTubeDarkColorScheme else YouTubeLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
