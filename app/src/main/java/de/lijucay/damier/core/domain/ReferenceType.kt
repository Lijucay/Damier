package de.lijucay.damier.core.domain

enum class ReferenceType {
    GOAL,
    LIMIT,
    MAX;

    override fun toString(): String {
        return when (this) {
            GOAL -> "Goal"
            LIMIT -> "Limit"
            MAX -> "Max"
        }
    }

    fun isMax(): Boolean = this == MAX
}