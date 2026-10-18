/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = YoutubeRed,
    secondary = DarkRed,
    tertiary = DarkOnSurfaceVariant,
    background = DarkBackground,
    surface = DarkBackground,
    surfaceVariant = DarkSurface,
    surfaceContainer = DarkSurface,
    surfaceContainerHigh = Color(0xFF252525),
    surfaceContainerHighest = Color(0xFF303030),
    surfaceContainerLow = Color(0xFF151515),
    surfaceContainerLowest = Color(0xFF0A0A0A),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = DarkOnSurface,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = Color(0xFF383838),
    outlineVariant = Color(0xFF282828)
)

private val LightColorScheme = DarkColorScheme

@Composable
fun GuruTubeTheme(
    darkTheme: Boolean = true,
    isDynamicColorEnabled: Boolean = false,
    content: @Composable () -> Unit
) {
    // Full UI is permanently locked to Dark Mode
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun PlayTubeTheme(
    darkTheme: Boolean = true,
    isDynamicColorEnabled: Boolean = false,
    content: @Composable () -> Unit
) = GuruTubeTheme(darkTheme, isDynamicColorEnabled, content)

