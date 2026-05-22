package de.lijucay.damier.core.domain

import de.lijucay.damier.shared.UnitId


data class ActivityUnit(
    val unitId: UnitId,
    val group: UnitGroup
)

val units = listOf(
    ActivityUnit(
        unitId = UnitId.CALORIES,
        group = UnitGroup.HEALTH
    ),
    ActivityUnit(
        unitId = UnitId.TIMES,
        group = UnitGroup.COUNT
    ),
    ActivityUnit(
        unitId = UnitId.STEPS,
        group = UnitGroup.COUNT
    ),
    ActivityUnit(
        unitId = UnitId.PAGES,
        group = UnitGroup.COUNT
    ),
    ActivityUnit(
        unitId = UnitId.CHAPTERS,
        group = UnitGroup.COUNT
    ),
    ActivityUnit(
        unitId = UnitId.BOOKS,
        group = UnitGroup.COUNT
    ),
    ActivityUnit(
        unitId = UnitId.REPETITIONS,
        group = UnitGroup.COUNT
    ),
    ActivityUnit(
        unitId = UnitId.SETS,
        group = UnitGroup.COUNT
    ),
    ActivityUnit(
        unitId = UnitId.LAPS,
        group = UnitGroup.COUNT
    ),
    ActivityUnit(
        unitId = UnitId.HOURS,
        group = UnitGroup.TIME
    ),
    ActivityUnit(
        unitId = UnitId.MINUTES,
        group = UnitGroup.TIME
    ),
    ActivityUnit(
        unitId = UnitId.SECONDS,
        group = UnitGroup.TIME
    ),
    ActivityUnit(
        unitId = UnitId.KILOMETERS,
        group = UnitGroup.DISTANCE
    ),
    ActivityUnit(
        unitId = UnitId.METERS,
        group = UnitGroup.DISTANCE
    ),
    ActivityUnit(
        unitId = UnitId.MILES,
        group = UnitGroup.DISTANCE
    ),
    ActivityUnit(
        unitId = UnitId.FOOT,
        group = UnitGroup.DISTANCE
    ),
    ActivityUnit(
        unitId = UnitId.YARD,
        group = UnitGroup.DISTANCE
    ),
    ActivityUnit(
        unitId = UnitId.LITERS,
        group = UnitGroup.VOLUME
    ),
    ActivityUnit(
        unitId = UnitId.MILLILITERS,
        group = UnitGroup.VOLUME
    ),
    ActivityUnit(
        unitId = UnitId.CENTILITERS,
        group = UnitGroup.VOLUME
    ),
    ActivityUnit(
        unitId = UnitId.GALLONS,
        group = UnitGroup.VOLUME
    ),
    ActivityUnit(
        unitId = UnitId.FLUID_OUNCES,
        group = UnitGroup.VOLUME
    ),
    ActivityUnit(
        unitId = UnitId.GLASSES,
        group = UnitGroup.VOLUME
    ),
    ActivityUnit(
        unitId = UnitId.CUPS,
        group = UnitGroup.VOLUME
    ),
    ActivityUnit(
        unitId = UnitId.KILOGRAMS,
        group = UnitGroup.WEIGHT
    ),
    ActivityUnit(
        unitId = UnitId.GRAMS,
        group = UnitGroup.WEIGHT
    ),
    ActivityUnit(
        unitId = UnitId.MILLIGRAMS,
        group = UnitGroup.WEIGHT
    ),
    ActivityUnit(
        unitId = UnitId.POUNDS,
        group = UnitGroup.WEIGHT
    ),
    ActivityUnit(
        unitId = UnitId.OUNCE,
        group = UnitGroup.WEIGHT
    ),
)