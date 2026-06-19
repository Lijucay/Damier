package de.lijucay.damier.widget.domain

import de.lijucay.damier.core.data.Activity

sealed interface WidgetActivityState {
    data object Loading : WidgetActivityState
    data object Deleted : WidgetActivityState
    data class Loaded(val activity: Activity) : WidgetActivityState
}