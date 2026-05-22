package de.lijucay.damier.core.presentation.models

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import de.lijucay.damier.core.domain.WaffleDiagramData
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId

data class ActivityFormState(
    val title: String = "",
    val unitId: UnitId = UnitId.TIMES,
    val defaultAmount: TextFieldValue = TextFieldValue("1", selection = TextRange(1)),
    val reference: TextFieldValue = TextFieldValue("1", selection = TextRange(1)),
    val referenceType: ReferenceType = ReferenceType.MAX,
    val useUnits: Boolean = false,
    val showUnits: Boolean = false,
    val useDefaultAmount: Boolean = false,
    val useReference: Boolean = false,
    val showReferenceTypes: Boolean = false,
    val checkInInfo: WaffleDiagramData? = null,
    val showUnitsSelectionDialog: Boolean = false
) {
    val useLimitTheme: Boolean get() = referenceType == ReferenceType.LIMIT

    val isSaveEnabled: Boolean get() =
        title.isNotBlank()
                && reference.text.toIntOrNull().let { it != null && it > 0 }
                && (!useDefaultAmount || defaultAmount.text.toIntOrNull() != null)

    companion object {
        fun fromExisting(activity: ActivityUi) = ActivityFormState(
            title = activity.title,
            unitId = activity.unitId,
            defaultAmount = activity.defaultAmount.toString().let {
                TextFieldValue(it, selection = TextRange(it.length))
            },
            reference = activity.reference.toString().let {
                TextFieldValue(it, selection = TextRange(it.length))
            },
            referenceType = activity.referenceType,
            useUnits = true,
            showUnits = true,
            useDefaultAmount = true,
            useReference = activity.referenceType != ReferenceType.MAX,
            showReferenceTypes = activity.referenceType != ReferenceType.MAX,
            checkInInfo = WaffleDiagramData(
                reference = activity.reference,
                referenceType = activity.referenceType,
                checkIns = activity.groupedCheckIns.values.flatten()
            )
        )
    }
}