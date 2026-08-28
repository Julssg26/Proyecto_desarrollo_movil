package com.julm.mitecmi.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = TecmiDarkGreen,
    onPrimary = White,
    primaryContainer = TecmiGreen,
    onPrimaryContainer = TecmiDarkGreen,

    secondary = TecmiGreen,
    onSecondary = White,
    secondaryContainer = TecmiLightGreen,
    onSecondaryContainer = TecmiDarkGreen,

    background = TecmiBackground,
    onBackground = TextPrimary,

    surface = White,
    onSurface = TextPrimary,

    surfaceVariant = TecmiLightGreen,
    onSurfaceVariant = TextSecondary
)

private val DarkColorScheme = darkColorScheme(
    primary = TecmiGreen,
    onPrimary = Black,

    secondary = TecmiLightGreen,
    onSecondary = Black,

    background = TecmiDarkGreen,
    onBackground = White,

    surface = TecmiDarkGreen,
    onSurface = White
)

@Composable
fun MiTecmiTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme

        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}