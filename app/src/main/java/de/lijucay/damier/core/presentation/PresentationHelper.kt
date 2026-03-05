package de.lijucay.damier.core.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.Activity
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import de.lijucay.damier.core.presentation.models.ActivityUi
import java.util.UUID

sealed interface DetailsDestination {
    data object AddActivity : DetailsDestination
    data class ActivityDetails(val activity: ActivityUi) : DetailsDestination
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
fun Modifier.clipWithScreenSize(isWidthAtLeastExpanded: Boolean, isHeightAtLeastExpanded: Boolean): Modifier {
    return this.clip(
        shape = RoundedCornerShape(
            topStart = if (isWidthAtLeastExpanded) 15.dp
            else 40.dp,
            topEnd = if (isWidthAtLeastExpanded) 15.dp
            else 40.dp,
            bottomEnd = if (isWidthAtLeastExpanded) 15.dp
            else if (isHeightAtLeastExpanded) 40.dp
            else 0.dp,
            bottomStart = if (isWidthAtLeastExpanded) 15.dp
            else if (isHeightAtLeastExpanded) 40.dp
            else 0.dp
        )
    )
}

@Composable
fun Modifier.paddingWithSafeNavigationBar(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
): Modifier {
    return this.padding(
        start = start,
        top = top,
        end = end,
        bottom = bottomPadding() + bottom
    )
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

@Composable
fun exampleActivities(): List<ActivityUi> = listOf(
    ActivityUi(
        id = UUID.randomUUID(),
        title = stringResource(R.string.reading),
        unitId = UnitId.PAGES,
        reference = 10,
        referenceType = ReferenceType.GOAL
    ),
    ActivityUi(
        id = UUID.randomUUID(),
        title = stringResource(R.string.code),
        unitId = UnitId.MINUTES,
        reference = 10,
        referenceType = ReferenceType.MAX
    ),
    ActivityUi(
        id = UUID.randomUUID(),
        title = stringResource(R.string.play_video_games),
        unitId = UnitId.HOURS,
        reference = 10,
        referenceType = ReferenceType.LIMIT
    )
)