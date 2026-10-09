package de.lijucay.damier.nfc.domain

import java.util.UUID

data class NfcCheckInResult(
    val activityId: UUID,
    val activityName: String
)
