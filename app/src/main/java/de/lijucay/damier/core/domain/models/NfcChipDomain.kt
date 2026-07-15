package de.lijucay.damier.core.domain.models

import java.time.LocalDateTime
import java.util.UUID

data class NfcChipDomain(
    val id: UUID,
    val activityId: UUID,
    val linkedAt: LocalDateTime,
    val label: String?
)
