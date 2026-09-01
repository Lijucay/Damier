package de.lijucay.damier.core.presentation

enum class SortMode {
    NAME_ASC, NAME_DESC, RECENTLY_CHECKED_IN;

    fun isNameAsc() = this == NAME_ASC
    fun isNameDesc() = this == NAME_DESC
    fun isRecentlyCheckedIn() = this == RECENTLY_CHECKED_IN
}