package de.lijucay.damier.check_in.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class CheckInDomain(
    val id: UUID,
    val activityId: UUID,
    val timestamp: LocalDateTime,
    val amount: Int,
    val note: String?
)
