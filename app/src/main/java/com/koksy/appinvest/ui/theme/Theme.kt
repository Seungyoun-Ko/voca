package com.koksy.appinvest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = DeepTeal,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD5F2EE),
    onPrimaryContainer = Color(0xFF083F3A),
    secondary = WarmAmber,
    onSecondary = Color(0xFF231A00),
    background = Cloud,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Color(0xFFE7EDF2),
    onSurfaceVariant = Color(0xFF5D6873),
    error = MarketRed,
)

@Composable
fun AppInvestTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}
