package de.lijucay.damier.core.domain

import java.time.LocalDateTime
import java.util.UUID

data class CheckIn(
    val activityId: UUID,
    val dateTime: LocalDateTime,
    val checkIns: Int
)