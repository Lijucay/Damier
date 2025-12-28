package de.lijucay.damier.core.data.wrapper

import de.lijucay.damier.core.presentation.models.ChartCheckIn
import de.lijucay.damier.core.presentation.models.CheckInUi
import java.time.format.TextStyle
import java.util.Locale

fun CheckInUi.toChartCheckIn(): ChartCheckIn {
    val weekDay = this.dateTime.value.dayOfWeek.getDisplayName(
        TextStyle.SHORT_STANDALONE,
        Locale.getDefault()
    )

    return ChartCheckIn(
        label = weekDay,
        count = this.checkInCount
    )
}