package de.lijucay.damier.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.lijucay.damier.core.domain.WaffleDiagramData
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.presentation.models.DisplayableDate
import de.lijucay.damier.core.presentation.models.toDisplayableDate
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WaffleDiagram(
    modifier: Modifier = Modifier,
    waffleDiagramData: WaffleDiagramData,
    selectedDate: LocalDate? = null,
    onCellSelected: ((DisplayableDate) -> Unit)? = null
) {
    BoxWithConstraints(
        modifier = modifier.padding(4.dp)
    ) {
        val availableWidthDp = this.maxWidth
        val minCellSize = 24.dp
        val numberOfWeeks = remember(availableWidthDp, minCellSize) {
            (availableWidthDp.value / minCellSize.value).toInt().coerceAtLeast(4)
        }

        val endDate = LocalDate.now()

        val startDate = remember(endDate, numberOfWeeks) {
            endDate.minusWeeks(
                numberOfWeeks.toLong()
            ).with(DayOfWeek.MONDAY)
        }

        val data = remember(waffleDiagramData.checkIns) {
            waffleDiagramData.checkIns.groupingBy { it.dateTime.value.toLocalDate() }
                .fold(0) { acc, checkInUi -> acc + checkInUi.amount }
        }

        val reference = with(waffleDiagramData) {
            if (referenceType == ReferenceType.MAX)
                data.values.maxOfOrNull { it }?.coerceAtLeast(1) ?: 0
            else reference
        }

        Column {
            DayOfWeek.entries.forEach { dayOfWeek ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    repeat(numberOfWeeks + 1) { weekIndex ->
                        val date = startDate
                            .plusWeeks(weekIndex.toLong())
                            .with(dayOfWeek)
                        val checkInCount = data[date] ?: 0

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                        ) {
                            Cell(
                                modifier = Modifier.let {
                                    onCellSelected?.let { _ ->
                                        if (!date.isAfter(endDate))
                                            it
                                                .clickable {
                                                    onCellSelected.invoke(date.toDisplayableDate())
                                                }
                                        else it
                                    } ?: it
                                },
                                checkInCount = checkInCount,
                                currentDate = date,
                                endDate = endDate,
                                reference = reference,
                                type = waffleDiagramData.referenceType,
                                isSelected = (date == selectedDate)
                            )
                        }
                    }
                }
            }
        }
    }
}