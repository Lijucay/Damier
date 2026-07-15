package de.lijucay.damier.nfc.read

sealed interface ReadResult {
    data class Success(val host: String, val chipId: String) : ReadResult
    data object NotACueChip : ReadResult
    data object EmptyTag : ReadResult
    data object NotNdefCompatible : ReadResult
    data class UnknownError(val cause: Exception) : ReadResult
}