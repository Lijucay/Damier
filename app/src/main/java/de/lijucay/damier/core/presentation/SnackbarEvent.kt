package de.lijucay.damier.core.presentation

data class SnackbarEvent(
    val message: String,
    val showButton: Boolean = false,
    val buttonText: String? = null,
    val action: (() -> Unit)? = null
)
