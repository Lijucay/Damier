package de.lijucay.damier.core.domain

import android.net.Uri

interface ExportUtil {
    suspend fun exportData(uri: Uri): Pair<Boolean, String?>
    suspend fun prepareFileData(): Map<String, String>
}