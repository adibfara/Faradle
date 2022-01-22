package com.snakyapps.khiardle.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
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
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFCCC2DC),
    secondary = Color(0xFFD1D1D1),
    tertiary = Color(0xFFD1D1D1),
    onBackground = Color(0xFFD1D1D1),
    onPrimary = Color(0xFF2A2A2A),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFCCC2DC),
    secondary = Color(0xFFD1D1D1),
    tertiary = Pink40,
    onBackground = Color(0xFFD1D1D1),
    onPrimary = Color(0xFFD1D1D1),

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val ColorScheme.correctBackground @Composable get() = Color(0xFF4CAF50)
val ColorScheme.wrongPositionBackground @Composable get() = Color(0xFFFFA000)
val ColorScheme.incorrectBackground @Composable get() = Color(0xFF5B5B5B)
val ColorScheme.enteringBackground @Composable get() = MaterialTheme.colorScheme.background
val ColorScheme.keyboard @Composable get() = Color(0xFF393939)
val ColorScheme.keyboardDisabled @Composable get() = Color(0xFF642424)
val ColorScheme.onKeyboard @Composable get() = Color(0xFFE7E7E7)

@Composable
fun KhiardleTheme(
    darkTheme: Boolean = true,//isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}