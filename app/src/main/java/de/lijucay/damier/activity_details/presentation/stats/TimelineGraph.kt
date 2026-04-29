package de.lijucay.damier.activity_details.presentation.stats

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.FadingEdges
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.MarkerCornerBasedShape
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import de.lijucay.damier.activity_details.presentation.ActivityDetailsState
import de.lijucay.damier.core.data.wrapper.toScrollableChartEntries
import de.lijucay.damier.core.domain.getLongUnitNamesById
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TimelineGraph(
    modifier: Modifier,
    state: ActivityDetailsState
) {
    val context = LocalContext.current

    val modelProducer = remember { CartesianChartModelProducer() }
    val today = remember { LocalDate.now() }
    val locale = remember { Locale.getDefault() }

    val startDate = remember(state.allCheckIns) {
        state.allCheckIns.keys.minOrNull() ?: today
    }

    val entries = remember (state.allCheckIns) {
        state.allCheckIns.toScrollableChartEntries(startDate, today)
    }

    LaunchedEffect(entries) {
        modelProducer.runTransaction {
            lineSeries { series(entries) }
        }
    }

    val singularUnit = state.unitId.getLongUnitNamesById(context).singularName
    val pluralUnit = state.unitId.getLongUnitNamesById(context).pluralName

    val bottomAxisValueFormatter = CartesianValueFormatter { _, x, _ ->
        startDate.plusDays(x.toLong())
            .dayOfWeek
            .getDisplayName(TextStyle.SHORT, locale)
    }

    val markerValueFormatter = remember(startDate, state.unitId.getLongUnitNamesById(context), locale) {
        DefaultCartesianMarker.ValueFormatter { _, targets ->
            val target = targets.first() as LineCartesianLayerMarkerTarget
            val date = startDate.plusDays(target.x.toLong())
            val formattedDate = date.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale)
            )
            val value = target.points.firstOrNull()?.entry?.y
                ?.let { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() }
                ?: "0"
            "$formattedDate\n$value ${if (value.toDouble() == 1.0) singularUnit else pluralUnit}"
        }
    }

    val line = LineCartesianLayer.rememberLine(
        pointProvider = LineCartesianLayer.PointProvider.single(
            LineCartesianLayer.Point(
                component = rememberShapeComponent(
                    fill = Fill(MaterialTheme.colorScheme.primary),
                    shape = CircleShape
                ),
                size = 8.dp
            )
        )
    )

    CartesianChartHost(
        modifier = modifier,
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(line)
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = bottomAxisValueFormatter
            ),
            marker = rememberDefaultCartesianMarker(
                label = rememberTextComponent(
                    background = rememberShapeComponent(
                        shape = MarkerCornerBasedShape(
                            base = RoundedCornerShape(6.dp)
                        ),
                        fill = Fill(MaterialTheme.colorScheme.primaryContainer),
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                    lineCount = 2,
                    overflow = TextOverflow.Ellipsis,
                    padding = Insets(4.dp)
                ),
                labelPosition = DefaultCartesianMarker.LabelPosition.AbovePoint,
                valueFormatter = markerValueFormatter
            ),
        ),
        modelProducer = modelProducer
    )
}