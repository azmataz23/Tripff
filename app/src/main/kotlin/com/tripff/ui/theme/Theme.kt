package com.tripff.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary          = BlueCardLight,
    secondary        = RedCardLight,
    background       = AppBackground,
    surface          = AppSurface,
    onPrimary        = Color.White,
    onSecondary      = Color.White,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary
)

@Composable
fun TripffTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}
