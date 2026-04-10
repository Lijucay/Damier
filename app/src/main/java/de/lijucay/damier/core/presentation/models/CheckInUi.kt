package de.lijucay.damier.core.presentation.models

import java.util.UUID

data class CheckInUi(
    val id: UUID,
    val activityId: UUID,
    val dateTime: DisplayableDateTime,
    val amount: Int
)