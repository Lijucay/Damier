package de.lijucay.damier.backup.domain.exports

sealed interface ExportResult {
    data object Success : ExportResult
    data class Failure(val message: String?) : ExportResult
}