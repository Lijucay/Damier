package de.lijucay.damier.core.domain

import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.models.CheckInUi

sealed interface DeletionMode {
    data class Activity(val activity: ActivityUi) : DeletionMode
    data class CheckIn(
        val checkIn: CheckInUi,
        val activity: ActivityUi
    ) : DeletionMode
}