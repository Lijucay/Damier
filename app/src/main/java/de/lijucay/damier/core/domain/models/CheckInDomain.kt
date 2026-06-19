package de.lijucay.damier.core.domain.models

import java.time.LocalDateTime
import java.util.UUID

data class CheckInDomain(
    val id: UUID,
    val activityId: UUID,
    val timestamp: LocalDateTime,
    val amount: Int
)
