package de.lijucay.damier.core.data.wrapper

import de.lijucay.damier.core.data.Activity
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.models.toCheckInUi

fun Activity.toActivityUi(): ActivityUi {
    val groupedCheckIns = checkIns
        .map { it.toCheckInUi() }
        .groupBy {
            it.dateTime.value.toLocalDate()
        }

    return ActivityUi(
        id = activityInfo.id,
        title = activityInfo.activityName,
        unitId = activityInfo.unit,
        reference = activityInfo.reference,
        referenceType = activityInfo.referenceType,
        defaultAmount = activityInfo.defaultAmount,
        groupedCheckIns = groupedCheckIns,
        streaks = streaks.map { it.toStreakUi() }
    )
}

fun List<Activity>.toActivityUis(): List<ActivityUi> =
    this.map { it.toActivityUi() }