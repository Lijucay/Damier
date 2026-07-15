package de.lijucay.damier.cue

sealed interface NfcWriteState {
    data object Idle : NfcWriteState
    data object WaitingForTag : NfcWriteState
    data object Writing : NfcWriteState
    data object Erasing : NfcWriteState
    data object EraseSuccess : NfcWriteState
    data class EraseException(val cause: Exception) : NfcWriteState
    data object TagLost : NfcWriteState
    data object NotWriteable : NfcWriteState
    data object InsufficientSize : NfcWriteState
    data object NotNdefCompatible : NfcWriteState
    data object Unknown : NfcWriteState
    data class Success(val chipId: String) : NfcWriteState

    val NfcWriteState.isAutoClosing: Boolean
        get() = this is Success || this is EraseException || this in setOf(
            InsufficientSize,
            NotNdefCompatible,
            NotWriteable,
            TagLost,
            EraseSuccess,
            Erasing,
            Unknown
        )
}