package de.lijucay.damier.activity_details.presentation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.TextStyle.Companion
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lijucay.damier.core.presentation.models.ChartCheckIn
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties

@Composable
fun WeeklyChart(
    currentWeekCheckIns: List<ChartCheckIn>,
) {
    val maxValue = currentWeekCheckIns.maxOfOrNull { it.count } ?: 0

    ColumnChart(
        modifier = Modifier.height(250.dp).padding(20.dp),
        data = currentWeekCheckIns.map { checkIn ->
            Bars(
                label = checkIn.label,
                values = listOf(
                    Bars.Data(
                        value = checkIn.count.toDouble(),
                        color = SolidColor(MaterialTheme.colorScheme.tertiary)
                    )
                )
            )
        },
        maxValue = if (maxValue > 10) maxValue.toDouble() else 10.0,
        minValue = 0.0,
        barProperties = BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topLeft = 6.dp, topRight = 6.dp)
        ),
        labelProperties = LabelProperties(
            enabled = true,
            rotation = LabelProperties.Rotation(degree = 0f),
            textStyle = TextStyle.Default.copy(
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ),
        gridProperties = GridProperties(
            enabled = true,
            xAxisProperties = GridProperties.AxisProperties(lineCount = 10),
            yAxisProperties = GridProperties.AxisProperties(lineCount = 7)
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            textStyle = TextStyle.Default.copy(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    )
}