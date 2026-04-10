package de.lijucay.damier.core.domain

import de.lijucay.damier.core.presentation.models.ActivityUi

sealed interface ActivityFormMode {
    data object Add : ActivityFormMode
    data class Edit(val activity: ActivityUi) : ActivityFormMode
}