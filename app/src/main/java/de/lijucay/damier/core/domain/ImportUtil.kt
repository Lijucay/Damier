package de.lijucay.damier.core.domain

import android.net.Uri

interface ImportUtil {
    suspend fun importData(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit
    )
    suspend fun importDataFromV1(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit
    )

    suspend fun calculateCount(fileUri: Uri, onTotalCountUpdate: (Int) -> Unit)
}