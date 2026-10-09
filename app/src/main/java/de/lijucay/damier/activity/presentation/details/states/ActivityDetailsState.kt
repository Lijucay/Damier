package de.lijucay.damier.activity.presentation.details.states

import de.lijucay.damier.activity.presentation.details.mappings.WeeklyComparisonEntry
import de.lijucay.damier.check_in.presentation.CheckInFormMode
import de.lijucay.damier.check_in.presentation.CheckInGroupUi
import de.lijucay.damier.activity.domain.WaffleDiagramData
import de.lijucay.damier.check_in.presentation.CheckInUi
import de.lijucay.damier.nfc.presentation.NfcChipUi
import de.lijucay.damier.streak.presentation.StreakUi
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId
import java.time.DayOfWeek
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
    val weeklyComparison: List<WeeklyComparisonEntry> = emptyList(),
    val chartStartDate: LocalDate = LocalDate.now(),
    val timelineEntries: List<Double> = emptyList(),
    val dayOfWeekDistribution: Map<DayOfWeek, Int> = mapOf(),
    val checkInFormMode: CheckInFormMode? = null,
    val showCheckInHistory: Boolean = false,
    val showStatsDialog: Boolean = false,
    val menuExpanded: Boolean = false,
    val showNfcList: Boolean = false,
    val showNameNfcTag: Boolean = false
) {
    val useLimitTheme: Boolean get() = referenceType == ReferenceType.LIMIT
    val showStreakCard: Boolean get() = referenceType != ReferenceType.LIMIT
}
