package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.domain.models.StreakDomain

fun StreakDomain.toStreakUi(): StreakUi {
    return StreakUi(
        id = id,
        activityId = activityId,
        startDate = startDate.toDisplayableDate(),
        endDate = endDate.toDisplayableDate(),
        length = length
    )
}

fun StreakUi.toStreakDomain(): StreakDomain {
    return StreakDomain(
        id = id,
        activityId = activityId,
        startDate = startDate.value,
        endDate = endDate.value,
        length = length
    )
}

fun List<StreakUi>.toStreakDomains(): List<StreakDomain> = this.map { it.toStreakDomain() }