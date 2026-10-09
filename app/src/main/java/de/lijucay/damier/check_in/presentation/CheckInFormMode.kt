package de.lijucay.damier.check_in.presentation

import java.util.UUID

sealed interface CheckInFormMode {
    data class Add(val activityId: UUID, val defaultAmount: Int) : CheckInFormMode
    data class Edit(val checkIn: CheckInUi) : CheckInFormMode
}