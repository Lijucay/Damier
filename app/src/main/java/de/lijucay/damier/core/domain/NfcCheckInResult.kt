package de.lijucay.damier.core.domain

import java.util.UUID

data class NfcCheckInResult(
    val activityId: UUID,
    val activityName: String
)
