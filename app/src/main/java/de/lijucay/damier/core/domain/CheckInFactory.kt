package de.lijucay.damier.core.domain

import de.lijucay.damier.core.data.entities.CheckInInfo
import java.time.LocalDateTime
import java.util.UUID

object CheckInFactory {
    fun createQuickCheckIn(activityId: UUID, defaultAmount: Int): CheckInInfo {
        return CheckInInfo(
            activityId = activityId,
            amount = defaultAmount,
            timestamp = LocalDateTime.now()
        )
    }
}