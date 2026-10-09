package de.lijucay.damier.activity.presentation

sealed interface ActivityFormMode {
    data object Add : ActivityFormMode
    data class Edit(val activity: ActivityUi) : ActivityFormMode
}