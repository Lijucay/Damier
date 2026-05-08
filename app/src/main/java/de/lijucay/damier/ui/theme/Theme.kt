package de.lijucay.damier.ui.theme

import android.os.Build
import android.util.Log
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

    val context = LocalContext.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val light = dynamicLightColorScheme(context).tertiaryContainer
        val dark = dynamicDarkColorScheme(context).tertiaryContainer
        Log.d("DamierTheme", "light=$light dark=$dark")
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


    Log.d("ActivityTheme", "useLimitTheme=$useLimitTheme")
    Log.d("ActivityTheme", "base.tertiaryContainer=${base.tertiaryContainer}")
    Log.d("ActivityTheme", "target.tertiaryContainer=${target.tertiaryContainer}")

    val primary by animateColorAsState(target.primary, label = "primary")
    val onPrimary by animateColorAsState(target.onPrimary, label = "onPrimary")
    val primaryContainer by animateColorAsState(target.primaryContainer, label = "primaryContainer")
    val onPrimaryContainer by animateColorAsState(target.onPrimaryContainer, label = "onPrimaryContainer")
    val inversePrimary by animateColorAsState(target.inversePrimary, label = "inversePrimary")
    val secondary by animateColorAsState(target.secondary, label = "secondary")
    val onSecondary by animateColorAsState(target.onSecondary, label = "onSecondary")
    val secondaryContainer by animateColorAsState(target.secondaryContainer, label = "secondaryContainer")
    val onSecondaryContainer by animateColorAsState(target.onSecondaryContainer, label = "onSecondaryContainer")
    val tertiary by animateColorAsState(target.tertiary, label = "tertiary")
    val onTertiary by animateColorAsState(target.onTertiary, label = "onTertiary")
    val tertiaryContainer by animateColorAsState(target.tertiaryContainer, label = "tertiaryContainer")
    val onTertiaryContainer by animateColorAsState(target.onTertiaryContainer, label = "onTertiaryContainer")
    val background by animateColorAsState(target.background, label = "background")
    val onBackground by animateColorAsState(target.onBackground, label = "onBackground")
    val surface by animateColorAsState(target.surface, label = "surface")
    val onSurface by animateColorAsState(target.onSurface, label = "onSurface")
    val surfaceVariant by animateColorAsState(target.surfaceVariant, label = "surfaceVariant")
    val onSurfaceVariant by animateColorAsState(target.onSurfaceVariant, label = "onSurfaceVariant")
    val surfaceTint by animateColorAsState(target.surfaceTint, label = "surfaceTint")
    val inverseSurface by animateColorAsState(target.inverseSurface, label = "inverseSurface")
    val inverseOnSurface by animateColorAsState(target.inverseOnSurface, label = "inverseOnSurface")
    val error by animateColorAsState(target.error, label = "error")
    val onError by animateColorAsState(target.onError, label = "onError")
    val errorContainer by animateColorAsState(target.errorContainer, label = "errorContainer")
    val onErrorContainer by animateColorAsState(target.onErrorContainer, label = "onErrorContainer")
    val outline by animateColorAsState(target.outline, label = "outline")
    val outlineVariant by animateColorAsState(target.outlineVariant, label = "outlineVariant")
    val scrim by animateColorAsState(target.scrim, label = "scrim")
    val surfaceBright by animateColorAsState(target.surfaceBright, label = "surfaceBright")
    val surfaceDim by animateColorAsState(target.surfaceDim, label = "surfaceDim")
    val surfaceContainer by animateColorAsState(target.surfaceContainer, label = "surfaceContainer")
    val surfaceContainerHigh by animateColorAsState(target.surfaceContainerHigh, label = "surfaceContainerHigh")
    val surfaceContainerHighest by animateColorAsState(target.surfaceContainerHighest, label = "surfaceContainerHighest")
    val surfaceContainerLow by animateColorAsState(target.surfaceContainerLow, label = "surfaceContainerLow")
    val surfaceContainerLowest by animateColorAsState(target.surfaceContainerLowest, label = "surfaceContainerLowest")

    MaterialTheme(
        colorScheme = base.copy(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            inversePrimary = inversePrimary,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            surfaceTint = surfaceTint,
            inverseSurface = inverseSurface,
            inverseOnSurface = inverseOnSurface,
            error = error,
            onError = onError,
            errorContainer = errorContainer,
            onErrorContainer = onErrorContainer,
            outline = outline,
            outlineVariant = outlineVariant,
            scrim = scrim,
            surfaceBright = surfaceBright,
            surfaceDim = surfaceDim,
            surfaceContainer = surfaceContainer,
            surfaceContainerHigh = surfaceContainerHigh,
            surfaceContainerHighest = surfaceContainerHighest,
            surfaceContainerLow = surfaceContainerLow,
            surfaceContainerLowest = surfaceContainerLowest,
        ),
        typography = Typography,
        shapes = MaterialTheme.shapes,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}

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
