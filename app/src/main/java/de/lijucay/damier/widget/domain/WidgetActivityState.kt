package de.lijucay.damier.widget.domain

sealed interface WidgetActivityState {
    data object Loading : WidgetActivityState
    data object Deleted : WidgetActivityState
    data class Loaded(val data: WidgetActivityData) : WidgetActivityState
}