package de.lijucay.damier.core.domain

import android.content.Context
import de.lijucay.damier.R


data class Unit(
    val unitId: UnitId,
    val group: UnitGroup
)

fun getUnits(context: Context) = listOf(
    Unit(
        unitId = UnitId.CALORIES,
        group = UnitGroup.HEALTH
    ),
    Unit(
        unitId = UnitId.TIMES,
        group = UnitGroup.COUNT
    ),
    Unit(
        unitId = UnitId.STEPS,
        group = UnitGroup.COUNT
    ),
    Unit(
        unitId = UnitId.PAGES,
        group = UnitGroup.COUNT
    ),
    Unit(
        unitId = UnitId.CHAPTERS,
        group = UnitGroup.COUNT
    ),
    Unit(
        unitId = UnitId.BOOKS,
        group = UnitGroup.COUNT
    ),
    Unit(
        unitId = UnitId.REPETITIONS,
        group = UnitGroup.COUNT
    ),
    Unit(
        unitId = UnitId.SETS,
        group = UnitGroup.COUNT
    ),
    Unit(
        unitId = UnitId.LAPS,
        group = UnitGroup.COUNT
    ),
    Unit(
        unitId = UnitId.HOURS,
        group = UnitGroup.TIME
    ),
    Unit(
        unitId = UnitId.MINUTES,
        group = UnitGroup.TIME
    ),
    Unit(
        unitId = UnitId.SECONDS,
        group = UnitGroup.TIME
    ),
    Unit(
        unitId = UnitId.KILOMETERS,
        group = UnitGroup.DISTANCE
    ),
    Unit(
        unitId = UnitId.METERS,
        group = UnitGroup.DISTANCE
    ),
    Unit(
        unitId = UnitId.MILES,
        group = UnitGroup.DISTANCE
    ),
    Unit(
        unitId = UnitId.FOOT,
        group = UnitGroup.DISTANCE
    ),
    Unit(
        unitId = UnitId.YARD,
        group = UnitGroup.DISTANCE
    ),
    Unit(
        unitId = UnitId.LITERS,
        group = UnitGroup.VOLUME
    ),
    Unit(
        unitId = UnitId.MILLILITERS,
        group = UnitGroup.VOLUME
    ),
    Unit(
        unitId = UnitId.CENTILITERS,
        group = UnitGroup.VOLUME
    ),
    Unit(
        unitId = UnitId.GALLONS,
        group = UnitGroup.VOLUME
    ),
    Unit(
        unitId = UnitId.FLUID_OUNCES,
        group = UnitGroup.VOLUME
    ),
    Unit(
        unitId = UnitId.GLASSES,
        group = UnitGroup.VOLUME
    ),
    Unit(
        unitId = UnitId.CUPS,
        group = UnitGroup.VOLUME
    ),
    Unit(
        unitId = UnitId.KILOGRAMS,
        group = UnitGroup.WEIGHT
    ),
    Unit(
        unitId = UnitId.GRAMS,
        group = UnitGroup.WEIGHT
    ),
    Unit(
        unitId = UnitId.MILLIGRAMS,
        group = UnitGroup.WEIGHT
    ),
    Unit(
        unitId = UnitId.POUNDS,
        group = UnitGroup.WEIGHT
    ),
    Unit(
        unitId = UnitId.OUNCE,
        group = UnitGroup.WEIGHT
    ),
)