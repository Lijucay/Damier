package de.lijucay.damier.core.data

import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.DisplayableDate
import kotlinx.serialization.Serializable

@Serializable
data class CheckInGroupUi(
    val date: DisplayableDate,
    val checkIns: List<CheckInUi>
)
