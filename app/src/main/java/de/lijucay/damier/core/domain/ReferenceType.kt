package de.lijucay.damier.core.domain

import de.lijucay.damier.R

enum class ReferenceType {
    GOAL,
    LIMIT,
    MAX;

    fun toStringResource(): Int {
        return when (this) {
            GOAL -> R.string.goal
            LIMIT -> R.string.limit
            MAX -> R.string.max
        }
    }

    fun isMax(): Boolean = this == MAX
    fun isLimit(): Boolean = this == LIMIT
}