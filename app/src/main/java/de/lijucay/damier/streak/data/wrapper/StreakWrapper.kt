package de.lijucay.damier.streak.data.wrapper

import de.lijucay.damier.streak.data.model.dto.Streak
import de.lijucay.damier.streak.domain.StreakDomain

fun Streak.toStreakDomain(): StreakDomain {
    return StreakDomain(
        id = id,
        activityId = activityId,
        startDate = startDate,
        endDate = endDate,
        length = length
    )
}