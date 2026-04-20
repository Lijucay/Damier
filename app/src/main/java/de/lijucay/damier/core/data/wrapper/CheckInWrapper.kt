package de.lijucay.damier.core.data.wrapper

import androidx.collection.mutableDoubleListOf
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import de.lijucay.damier.R
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.presentation.models.CheckInUi
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import java.time.LocalDate

fun CheckInUi.toCheckInInfo(): CheckInInfo {
    return CheckInInfo(
        id = id,
        activityId = activityId,
        timestamp = dateTime.value,
        checkInCount = amount
    )
}

private fun Map<LocalDate, List<CheckInUi>>.toChartCheckIn(): List<Double> {
    val chartCheckIns = mutableListOf<Double>()

    val currentDate = LocalDate.now()

    if (keys.isEmpty()) return emptyList()
    val minDate = keys.min()

    var localDate = minDate

    while (localDate <= currentDate) {
        val totalCheckIns = this[localDate]?.sumOf { it.amount.toDouble() } ?: 0.0

        chartCheckIns.add(totalCheckIns)
        localDate = localDate.plusDays(1)
    }

    return chartCheckIns
}

@Composable
fun Map<LocalDate, List<CheckInUi>>.toTimelineLine(useLimitTheme: Boolean): Line {
    val lineColor = if (useLimitTheme) colorScheme.onErrorContainer else colorScheme.onTertiaryContainer

    return Line(
        label = stringResource(R.string.check_in),
        values = toChartCheckIn(),
        color = SolidColor(lineColor),
        popupProperties = PopupProperties(
            enabled = true
        ),
        curvedEdges = false
    )
}