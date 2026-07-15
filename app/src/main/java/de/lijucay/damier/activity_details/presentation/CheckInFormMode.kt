package de.lijucay.damier.activity_details.presentation

import de.lijucay.damier.core.presentation.models.CheckInUi
import java.util.UUID

sealed interface CheckInFormMode {
    data class Add(val activityId: UUID, val defaultAmount: Int) : CheckInFormMode
    data class Edit(val checkIn: CheckInUi) : CheckInFormMode
}