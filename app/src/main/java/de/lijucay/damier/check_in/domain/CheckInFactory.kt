package de.lijucay.damier.check_in.domain

import de.lijucay.damier.check_in.data.model.dto.CheckInInfo
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