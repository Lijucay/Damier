package de.lijucay.damier.ui.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

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

@Composable
fun DamierTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
        typography = Typography,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}

@Composable
fun ActivityTheme(
    useLimitTheme: Boolean,
    content: @Composable () -> Unit
) {
    val base = MaterialTheme.colorScheme
    val target = if (useLimitTheme) base.toErrorScheme() else base

    val primary by animateColorAsState(target.primary)
    val onPrimary by animateColorAsState(target.onPrimary)
    val primaryContainer by animateColorAsState(target.primaryContainer)
    val onPrimaryContainer by animateColorAsState(target.onPrimaryContainer)
    val secondary by animateColorAsState(target.secondary)
    val onSecondary by animateColorAsState(target.onSecondary)
    val secondaryContainer by animateColorAsState(target.secondaryContainer)
    val onSecondaryContainer by animateColorAsState(target.onSecondaryContainer)
    val tertiary by animateColorAsState(target.tertiary)
    val onTertiary by animateColorAsState(target.onTertiary)
    val tertiaryContainer by animateColorAsState(target.tertiaryContainer)
    val onTertiaryContainer by animateColorAsState(target.onTertiaryContainer)

    MaterialTheme(
        colorScheme = base.copy(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
        ),
        typography = Typography,
        shapes = MaterialTheme.shapes,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}

@Composable
fun ColorScheme.toErrorScheme() = copy(
    primary = error,
    onPrimary = onError,
    primaryContainer = errorContainer,
    onPrimaryContainer = onErrorContainer,
    secondary = error,
    onSecondary = onError,
    secondaryContainer = errorContainer,
    onSecondaryContainer = onErrorContainer,
    tertiary = error,
    onTertiary = onError,
    tertiaryContainer = errorContainer,
    onTertiaryContainer = onErrorContainer,
)