package de.lijucay.damier.shared

enum class ReferenceType {
    GOAL,
    LIMIT,
    MAX;

    fun isMax(): Boolean = this == MAX
    fun isLimit(): Boolean = this == LIMIT
}