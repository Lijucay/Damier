package de.lijucay.damier.core.data.wrapper

import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.domain.models.StreakDomain

fun Streak.toStreakDomain(): StreakDomain {
    return StreakDomain(
        id = id,
        activityId = activityId,
        startDate = startDate,
        endDate = endDate,
        length = length
    )
}