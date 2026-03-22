package de.lijucay.damier.core.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.toCheckInUi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

sealed interface DetailsDestination {
    data object AddActivity : DetailsDestination
    data object ActivityDetails : DetailsDestination
    data object Settings : DetailsDestination
}

@Composable
fun Modifier.adaptiveHorizontalCutoutPadding(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
): Modifier {
    val layoutDirection = LocalLayoutDirection.current
    val cutoutPadding = WindowInsets.displayCutout.asPaddingValues()
    val startPadding = cutoutPadding.calculateLeftPadding(layoutDirection)
    val endPadding = cutoutPadding.calculateRightPadding(layoutDirection)

    return this.padding(
        start = startPadding + start,
        top = top,
        end = endPadding + end,
        bottom = bottom
    )
}

@Composable
fun Modifier.clipWithScreenSize(
    isWidthAtLeastExpanded: Boolean,
    showBottomBar: Boolean
): Modifier {
    return this.clip(
        shape = RoundedCornerShape(
            topStart = 40.dp,
            topEnd = 40.dp,
            bottomEnd = if (isWidthAtLeastExpanded || showBottomBar) 40.dp else 0.dp,
            bottomStart = if (isWidthAtLeastExpanded || showBottomBar) 40.dp else 0.dp
        )
    )
}

@Composable
fun Modifier.animateClipWithScreenSize(
    isWidthAtLeastExpanded: Boolean,
    showBottomBar: Boolean
): Modifier {
    val cornerRadius by animateDpAsState(
        targetValue = if (showBottomBar || isWidthAtLeastExpanded) 40.dp else 0.dp,
        animationSpec = tween(500, easing = LinearEasing),
        label = "MainBoxBottomCornerRadius"
    )

    return this.clip(
        shape = RoundedCornerShape(
            topStart = 40.dp,
            topEnd = 40.dp,
            bottomEnd = cornerRadius,
            bottomStart = cornerRadius
        )
    )
}

@Composable
fun Modifier.clipInnerContainer(): Modifier {
    return this.clip(shape = RoundedCornerShape(24.dp))
}

@Composable
fun Modifier.paddingWithSafeNavigationBar(all: Dp = 0.dp): Modifier {
    return this.padding(
        start = all,
        top = all,
        end = all,
        bottom = bottomPadding() + all
    )
}

@Composable
fun bottomPadding() = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

fun Map<LocalDate, List<CheckInUi>>.getCurrentStreak(from: LocalDate, reference: Int = 1): Int {
    var current = from
    var streak = 0

    while (true) {
        val reached = this[current]?.sumOf { it.checkInCount } ?: 0
        if (reached < reference) break
        streak++
        current = current.minusDays(1)
    }

    return streak
}