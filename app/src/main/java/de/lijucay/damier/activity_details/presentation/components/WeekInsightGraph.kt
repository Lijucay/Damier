package de.lijucay.damier.activity_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.pie.PieChart
import com.patrykandpatrick.vico.compose.pie.PieChartHost
import com.patrykandpatrick.vico.compose.pie.data.PieChartModelProducer
import com.patrykandpatrick.vico.compose.pie.data.PieValueFormatter
import com.patrykandpatrick.vico.compose.pie.data.pieModel
import com.patrykandpatrick.vico.compose.pie.rememberPieChart
import de.lijucay.damier.activity_details.presentation.states.ActivityDetailsState
import de.lijucay.damier.core.presentation.toDayOfWeekDistribution
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekInsightGraph(modifier: Modifier = Modifier, state: ActivityDetailsState) {
    val distribution = remember(state.allCheckIns) {
        state.allCheckIns.toDayOfWeekDistribution()
    }

    val dayLabels = remember {
        DayOfWeek.entries.map { it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) }
    }

    val modelProducer = remember { PieChartModelProducer() }
    LaunchedEffect(distribution) {
        modelProducer.runTransaction {
            pieModel { series(distribution.values) }
        }
    }

    val colorScheme = MaterialTheme.colorScheme

    val slicePairs = remember(colorScheme) {
        listOf(
            colorScheme.background to colorScheme.onBackground,
            colorScheme.primaryContainer to colorScheme.onPrimaryContainer,
            colorScheme.secondaryContainer to colorScheme.onSecondaryContainer,
            colorScheme.onTertiaryContainer to colorScheme.tertiaryContainer,
            colorScheme.onPrimaryContainer to colorScheme.primaryContainer,
            colorScheme.onSecondaryContainer to colorScheme.secondaryContainer,
            colorScheme.tertiaryContainer to colorScheme.onTertiaryContainer
        )
    }

    val sliceProvider = PieChart.SliceProvider.series(
        slices = slicePairs.map { (background, onColor) ->
            PieChart.Slice(
                fill = Fill(background),
                label = PieChart.SliceLabel.Inside(
                    textComponent = rememberTextComponent(
                        style = androidx.compose.ui.text.TextStyle(color = onColor, fontWeight = FontWeight.Bold)
                    )
                )
            )
        }
    )

    val chart = rememberPieChart(
        sliceProvider = sliceProvider,
        valueFormatter = PieValueFormatter { _, _, index ->
            "${distribution.values.toList()[index]}%"
        }
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PieChartHost(
            chart = chart,
            modelProducer = modelProducer,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                slicePairs.forEachIndexed { index, (background, _) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(background, CircleShape)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = dayLabels[index],
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}