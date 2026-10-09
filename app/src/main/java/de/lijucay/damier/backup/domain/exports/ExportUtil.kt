package de.lijucay.damier.backup.domain.exports

import android.net.Uri

interface ExportUtil {
    suspend fun exportData(uri: Uri): ExportResult
    suspend fun prepareFileData(): Map<String, String>
}