package de.lijucay.damier.core.domain

sealed interface ExportResult {
    data object Success : ExportResult
    data class Failure(val message: String?) : ExportResult
}