package de.lijucay.damier.core.domain

sealed interface InfoMode {
    data object BackupSuccess : InfoMode
    data object ImportSuccess : InfoMode

    data class BackupError(val message: String?) : InfoMode
    data class ImportError(val message: String?) : InfoMode
}