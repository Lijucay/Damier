package de.lijucay.damier.core.domain

import android.net.Uri

interface ExportUtil {
    suspend fun exportData(uri: Uri): ExportResult
    suspend fun prepareFileData(): Map<String, String>
}