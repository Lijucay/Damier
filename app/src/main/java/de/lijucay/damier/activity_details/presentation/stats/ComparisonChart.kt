package de.lijucay.damier.activity_details.presentation.stats

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.activity_details.presentation.ActivityDetailsState
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ComparisonChart(
    modifier: Modifier = Modifier,
    reference: Double?,
    state: ActivityDetailsState
) {
    val thisWeek = stringResource(R.string.this_week)
    val lastWeek = stringResource(R.string.last_week)

    val today = remember { LocalDate.now() }
    val monday = remember { today.with(DayOfWeek.MONDAY) }
    val lastMonday = remember { monday.minusWeeks(1) }
    val locale = remember { Locale.getDefault() }

    val currentWeekColor = MaterialTheme.colorScheme.primary
    val lastWeekColor = MaterialTheme.colorScheme.secondary

    val bars = remember(state.allCheckIns, locale) {
        (0..6).map { offset ->
            val currentDate = monday.plusDays(offset.toLong())
            val lastWeekDate = lastMonday.plusDays(offset.toLong())

            val currentAmount = if (currentDate > today)
                0.0
            else state
                .allCheckIns
                .find { it.date.value == currentDate }
                ?.checkIns
                ?.sumOf { it.amount.toDouble() } ?: 0.0

            val lastWeekAmount = state.allCheckIns.find { it.date.value == lastWeekDate }
                ?.checkIns
                ?.sumOf { it.amount.toDouble() } ?: 0.0

            Bars(
                label = currentDate.dayOfWeek.getDisplayName(TextStyle.SHORT, locale),
                values = listOf(
                    Bars.Data(
                        label = thisWeek,
                        value = currentAmount,
                        color = SolidColor(currentWeekColor)
                    ),
                    Bars.Data(
                        label = lastWeek,
                        value = lastWeekAmount,
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