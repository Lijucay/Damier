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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.lijucay.damier.core.domain.WaffleDiagramData
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.toDisplayableDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

sealed interface DetailsDestination {
    data object AddActivity : DetailsDestination
    data object ActivityDetails : DetailsDestination
    data object EditActivity : DetailsDestination
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
    var current = if ((this[from]?.sumOf { it.amount } ?: 0) >= reference) from else from.minusDays(1)
    var streak = 0

    while (true) {
        val reached = this[current]?.sumOf { it.amount } ?: 0
        if (reached < reference) break
        streak++
        current = current.minusDays(1)
    }

    return streak
}

fun getRandomCheckInInfo(
    from: Int = 0,
    until: Int = 50
): WaffleDiagramData {
    val checkIns = mutableListOf<CheckInUi>()

    repeat(365) { daysAgo ->
        checkIns.add(
            CheckInUi(
                id = UUID.randomUUID(),
                activityId = UUID.randomUUID(),
                dateTime = LocalDateTime.now()
                    .minusDays(daysAgo.toLong())
                    .toDisplayableDateTime(),
                amount = Random.nextInt(from, until)
            )
        )
    }

    return WaffleDiagramData(
        reference = 1,
        referenceType = ReferenceType.MAX,
        checkIns = checkIns
    )
}