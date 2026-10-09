package de.lijucay.damier.core.domain

sealed interface InfoAction {
    data object GrantNotificationPermission : InfoAction
}
