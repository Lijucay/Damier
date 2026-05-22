package de.lijucay.damier.activity_details.presentation

import de.lijucay.damier.core.domain.WaffleDiagramData
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.StreakUi
import de.lijucay.damier.cue.NfcWriteState
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId
import java.time.LocalDate

data class ActivityDetailsState(
    val title: String = "",
    val unitId: UnitId = UnitId.TIMES,
    val referenceType: ReferenceType = ReferenceType.MAX,
    val defaultAmount: Int = 1,
    val waffleDiagramData: WaffleDiagramData? = null,
    val allCheckIns: Map<LocalDate, List<CheckInUi>> = emptyMap(),
    val streaks: List<StreakUi> = emptyList(),
    val todaysCheckIns: List<CheckInUi> = emptyList(),
    val todaysTotal: Int = 0,
    val currentStreakLength: Int = 0,
    val longestStreakLength: Int = 0,
    val checkInFormMode: CheckInFormMode? = null,
    val showCheckInHistory: Boolean = false,
    val showStatsDialog: Boolean = false,
    val nfcChipId: String? = null,
    val nfcWriteState: NfcWriteState = NfcWriteState.Idle,
    val menuExpanded: Boolean = false
) {
    val useLimitTheme: Boolean get() = referenceType == ReferenceType.LIMIT
    val showStreakCard: Boolean get() = referenceType != ReferenceType.LIMIT

    companion object {
        fun fromActivityUi(activity: ActivityUi, today: LocalDate): ActivityDetailsState {
            val todaysCheckIns = activity.groupedCheckIns[today] ?: emptyList()
            return ActivityDetailsState(
                title = activity.title,
                unitId = activity.unitId,
                referenceType = activity.referenceType,
                defaultAmount = activity.defaultAmount,
                waffleDiagramData = WaffleDiagramData(
                    reference = activity.reference,
                    referenceType = activity.referenceType,
                    checkIns = activity.groupedCheckIns.values.flatten()
                ),
                allCheckIns = activity.groupedCheckIns,
                streaks = activity.streaks,
                todaysCheckIns = todaysCheckIns,
                todaysTotal = todaysCheckIns.sumOf { it.amount },
                currentStreakLength = activity.streaks
                    .maxByOrNull { it.endDate.value }
                    ?.takeIf { streak ->
                        val end = streak.endDate.value
                        end == today || end == today.minusDays(1)
                    }?.length ?: 0,
                longestStreakLength = activity.streaks.maxByOrNull { it.length }?.length ?: 0,
                nfcChipId = activity.nfcChipId
            )
        }
    }
}
