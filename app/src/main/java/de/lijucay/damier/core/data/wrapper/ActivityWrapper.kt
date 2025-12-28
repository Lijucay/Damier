package de.lijucay.damier.core.data.wrapper

import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import de.lijucay.damier.core.presentation.models.ActivityUi

fun ActivityInfo.toActivityUi(): ActivityUi =
    ActivityUi(
        id = id,
        title = activityName,
        unitId = UnitId.valueOf(unit),
        reference = reference,
        referenceType = ReferenceType.valueOf(referenceType)
    )

fun List<ActivityInfo>.toActivityUis(): List<ActivityUi> =
    this.map { it.toActivityUi() }