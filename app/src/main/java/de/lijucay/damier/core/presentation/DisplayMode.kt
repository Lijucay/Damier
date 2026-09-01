package de.lijucay.damier.core.presentation

enum class DisplayMode {
    EXPANDED, COMPACT, EXTRA_COMPACT;

    fun isExpanded() = this == EXPANDED
    fun isCompact() = this == COMPACT
    fun isExtraCompact() = this == EXTRA_COMPACT
}