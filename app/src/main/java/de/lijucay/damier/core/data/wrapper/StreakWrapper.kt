package de.lijucay.damier.core.data.wrapper

import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.presentation.models.StreakUi
import de.lijucay.damier.core.presentation.models.toDisplayableDate

fun Streak.toStreakUi(): StreakUi {
    return StreakUi(
        id = id,
        activityId = activityId,
        startDate = startDate.toDisplayableDate(),
        endDate = endDate.toDisplayableDate(),
        length = length
    )
}