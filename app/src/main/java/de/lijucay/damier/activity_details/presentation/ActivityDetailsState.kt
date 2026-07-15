package de.lijucay.damier.activity_details.presentation

import de.lijucay.damier.core.data.CheckInGroupUi
import de.lijucay.damier.core.domain.WaffleDiagramData
import de.lijucay.damier.core.domain.currentStreakLength
import de.lijucay.damier.core.domain.longestStreakLength
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.NfcChipUi
import de.lijucay.damier.core.presentation.models.StreakUi
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId
import java.time.LocalDate

data class ActivityDetailsState(
    val title: String = "",
    val unitId: UnitId = UnitId.TIMES,
    val referenceType: ReferenceType = ReferenceType.MAX,
    val defaultAmount: Int = 1,
    val allNfcChips: List<NfcChipUi> = emptyList(),
    val waffleDiagramData: WaffleDiagramData? = null,
    val allCheckIns: List<CheckInGroupUi> = emptyList(),
    val streaks: List<StreakUi> = emptyList(),
    val todaysCheckIns: List<CheckInUi> = emptyList(),
    val todaysTotal: Int = 0,
    val currentStreakLength: Int = 0,
    val longestStreakLength: Int = 0,
    val checkInFormMode: CheckInFormMode? = null,
    val showCheckInHistory: Boolean = false,
    val showStatsDialog: Boolean = false,
    val menuExpanded: Boolean = false,
    val showNfcList: Boolean = false,
    val showNameNfcTag: Boolean = false
) {
    val useLimitTheme: Boolean get() = referenceType == ReferenceType.LIMIT
    val showStreakCard: Boolean get() = referenceType != ReferenceType.LIMIT

    companion object {
        fun fromActivityUi(activity: ActivityUi, today: LocalDate): ActivityDetailsState {
            val todaysCheckIns = activity
                .groupedCheckIns
                .find { it.date.value == today }
                ?.checkIns
                ?: emptyList()

            return ActivityDetailsState(
                title = activity.title,
                unitId = activity.unitId,
                referenceType = activity.referenceType,
                defaultAmount = activity.defaultAmount,
                allNfcChips = activity.nfcChipId,
                waffleDiagramData = WaffleDiagramData(
                    reference = activity.reference,
                    referenceType = activity.referenceType,
                    checkIns = activity.groupedCheckIns.flatMap { it.checkIns }
                ),
                allCheckIns = activity.groupedCheckIns,
                streaks = activity.streaks,
                todaysCheckIns = todaysCheckIns,
                todaysTotal = todaysCheckIns.sumOf { it.amount },
                currentStreakLength = activity.streaks.currentStreakLength(today),
                longestStreakLength = activity.streaks.longestStreakLength(),
            )
        }
    }
}
