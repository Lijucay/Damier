package de.lijucay.damier.core.domain

import de.lijucay.damier.activity.presentation.ActivityUi
import de.lijucay.damier.check_in.presentation.CheckInUi

sealed interface DeletionMode {
    data class Activity(val activity: ActivityUi) : DeletionMode
    data class CheckIn(
        val checkIn: CheckInUi,
        val activity: ActivityUi
    ) : DeletionMode
}