package de.lijucay.damier.widget.domain

import androidx.room.Embedded
import de.lijucay.damier.activity.data.model.dto.ActivityInfo

data class WidgetActivityData(
    @Embedded val activityInfo: ActivityInfo,
    val todaysAmount: Int,
    val maxDailyAmount: Int
)
