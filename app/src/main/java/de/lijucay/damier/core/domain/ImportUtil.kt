package de.lijucay.damier.core.domain

import android.net.Uri

interface ImportUtil {
    suspend fun importData(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit,
        onIncompatibleVersion: () -> Unit
    )

    suspend fun calculateCount(fileUri: Uri, onTotalCountUpdate: (Int) -> Unit)
}