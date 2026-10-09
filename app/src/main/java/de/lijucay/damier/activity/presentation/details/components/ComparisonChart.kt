package de.lijucay.damier.activity.presentation.details.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.activity.presentation.details.states.ActivityDetailsState
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import java.time.format.TextStyle

@Composable
fun ComparisonChart(
    modifier: Modifier = Modifier,
    state: ActivityDetailsState
) {
    val thisWeek = stringResource(R.string.this_week)
    val lastWeek = stringResource(R.string.last_week)
    val locale = LocalLocale.current

    val currentWeekColor = MaterialTheme.colorScheme.primary
    val lastWeekColor = MaterialTheme.colorScheme.secondary

    val bars = remember(state.weeklyComparison, thisWeek, lastWeek, currentWeekColor, lastWeekColor, locale) {
       state.weeklyComparison.map { entry ->
           Bars(
               label = entry.date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale.platformLocale),
               values = listOf(
                   Bars.Data(
                       label = thisWeek,
                       value = entry.currentAmount,
                       color = SolidColor(currentWeekColor)
                   ),
                   Bars.Data(
                       label = lastWeek,
                       value = entry.lastWeekAmount,
                       color = SolidColor(lastWeekColor)
                   )
               )
           )
       }
    }

    ColumnChart(
        modifier = modifier,
        data = bars,
        barProperties = BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 20.dp, topLeft = 20.dp),
            spacing = 2.dp,
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            textStyle = MaterialTheme.typography.bodySmall
                .copy(color = MaterialTheme.colorScheme.onSurface)
        ),
        labelProperties = LabelProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.bodySmall
                .copy(color = MaterialTheme.colorScheme.onSurface),
            rotation = LabelProperties.Rotation(degree = 0f)
        ),
        labelHelperProperties = LabelHelperProperties(
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
        ),
        gridProperties = GridProperties(
            enabled = true,
            xAxisProperties = GridProperties.AxisProperties(
                lineCount = 5,
                style = StrokeStyle.Dashed(intervals = floatArrayOf(10f, 10f)),
                color = SolidColor(MaterialTheme.colorScheme.outlineVariant),
                thickness = 0.5.dp
            ),
            yAxisProperties = GridProperties.AxisProperties(
                lineCount = 7,
                style = StrokeStyle.Dashed(intervals = floatArrayOf(10f, 10f)),
                color = SolidColor(MaterialTheme.colorScheme.outlineVariant),
                thickness = 0.5.dp
            )
        )
    )
}