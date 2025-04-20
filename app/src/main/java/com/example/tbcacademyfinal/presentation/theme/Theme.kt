package com.example.tbcacademyfinal.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    secondary = WalnutBrown,
    tertiary = GoldAccent, // Or another accent
    background = CreamWhite,
    surface = CreamWhite, // Often same as background for light theme
    onPrimary = Color.White,
    onSecondary = Color.White, // Adjust if needed based on secondary color lightness
    onTertiary = Color.Black, // Adjust based on tertiary color
    onBackground = Color(0xFF1C1B1F), // Dark text on light background
    onSurface = Color(0xFF1C1B1F), // Dark text on light surface
    error = LightError,
    onError = LightOnError
    // You can define other colors like surfaceVariant, outline, etc.
)

private val DarkColorScheme = darkColorScheme(
    primary = GreenVariant, // Often a lighter/desaturated version for dark theme
    secondary = WalnutBrown, // Can stay same or adjust
    tertiary = GoldAccent, // Can stay same or adjust
    background = DarkBackground,
    surface = DarkSurface, // Slightly lighter than background
    onPrimary = Color.Black, // Adjust if primary is light enough
    onSecondary = Color.Black, // Adjust
    onTertiary = Color.Black, // Adjust
    onBackground = Color(0xFFE6E1E5), // Light text on dark background
    onSurface = Color(0xFFE6E1E5), // Light text on dark surface
    error = DarkError,
    onError = DarkOnError
    // Define other colors
)

@Composable
fun TBCAcademyFinalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set to true if you want dynamic colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}