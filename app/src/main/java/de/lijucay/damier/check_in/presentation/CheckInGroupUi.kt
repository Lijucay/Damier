package de.lijucay.damier.check_in.presentation

import de.lijucay.damier.core.presentation.models.DisplayableDate
import kotlinx.serialization.Serializable

@Serializable
data class CheckInGroupUi(
    val date: DisplayableDate,
    val checkIns: List<CheckInUi>
)
