package dev.lijucay.damier.util

sealed class ResponseState {
    data object Loading : ResponseState()
    data object Success : ResponseState()
    data class Failure(val message: String) : ResponseState()
    data object Empty : ResponseState()
}