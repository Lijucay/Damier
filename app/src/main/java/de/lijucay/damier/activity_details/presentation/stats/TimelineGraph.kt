package de.lijucay.damier.activity_details.presentation.stats

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.lijucay.damier.activity_details.presentation.ActivityDetailsState
import de.lijucay.damier.core.data.wrapper.toTimelineLine
import ir.ehsannarmani.compose_charts.LineChart

@Composable
fun TimelineGraph(
    modifier: Modifier,
    state: ActivityDetailsState
) {
    val checkInLine = state.allCheckIns.toTimelineLine(state.useLimitTheme)

    LineChart(
        modifier = modifier,
        data = listOf(checkInLine)
    )
}