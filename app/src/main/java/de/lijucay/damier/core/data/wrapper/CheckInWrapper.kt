package de.lijucay.damier.core.data.wrapper

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import de.lijucay.damier.R
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.presentation.models.CheckInUi
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import java.time.DayOfWeek
import java.time.LocalDate

fun CheckInUi.toCheckInInfo(): CheckInInfo {
    return CheckInInfo(
        id = id,
        activityId = activityId,
        timestamp = dateTime.value,
        amount = amount
    )
}

private fun Map<LocalDate, List<CheckInUi>>.toWeekChartCheckIn(): List<Double> {
    val today = LocalDate.now()
    val monday = today.with(DayOfWeek.MONDAY)

    return (0..6).map { offset ->
        val date = monday.plusDays(offset.toLong())
        if (date > today) {
            0.0
        } else {
            this[date]?.sumOf { it.amount.toDouble() } ?: 0.0
        }
    }
}

fun Map<LocalDate, List<CheckInUi>>.toScrollableChartEntries(
    startDate: LocalDate,
    today: LocalDate
): List<Double> {
    val result = mutableListOf<Double>()
    var date = startDate
    while (date <= today) {
        result.add(this[date]?.sumOf { it.amount.toDouble() } ?: 0.0)
        date = date.plusDays(1)
    }
    return result
}

@Composable
fun Map<LocalDate, List<CheckInUi>>.toTimelineLine(useLimitTheme: Boolean): Line {
    val lineColor = if (useLimitTheme) colorScheme.onErrorContainer else colorScheme.onTertiaryContainer

    return Line(
        label = stringResource(R.string.check_in),
        values = toWeekChartCheckIn(),
        color = SolidColor(lineColor),
        popupProperties = PopupProperties(
            enabled = true
        ),
        curvedEdges = false
    )
}