package de.lijucay.damier.core.presentation

import android.content.Context
import de.lijucay.damier.R
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId

data class ShortUnitName(val shortUnitSingular: String, val shortUnitPlural: String)

data class LongUnitName(val singularName: String, val pluralName: String)

fun UnitId.getLongUnitNamesById(context: Context): LongUnitName {
    return when(this) {
        UnitId.CALORIES -> LongUnitName(
            singularName = context.getString(R.string.calorie),
            pluralName = context.getString(R.string.calories),
        )
        UnitId.TIMES -> LongUnitName(
            singularName = context.getString(R.string.time),
            pluralName = context.getString(R.string.times),
        )
        UnitId.STEPS -> LongUnitName(
            singularName = context.getString(R.string.step),
            pluralName = context.getString(R.string.steps)
        )
        UnitId.PAGES -> LongUnitName(
            singularName = context.getString(R.string.page),
            pluralName = context.getString(R.string.pages)
        )
        UnitId.CHAPTERS -> LongUnitName(
            singularName = context.getString(R.string.chapter),
            pluralName = context.getString(R.string.chapters),
        )
        UnitId.BOOKS -> LongUnitName(
            singularName = context.getString(R.string.book),
            pluralName = context.getString(R.string.books)
        )
        UnitId.REPETITIONS -> LongUnitName(
            singularName = context.getString(R.string.repetition),
            pluralName = context.getString(R.string.repetitions),
        )
        UnitId.SETS -> LongUnitName(
            singularName = context.getString(R.string.set),
            pluralName = context.getString(R.string.sets),
        )
        UnitId.LAPS -> LongUnitName(
            singularName = context.getString(R.string.lap),
            pluralName = context.getString(R.string.laps),
        )
        UnitId.HOURS -> LongUnitName(
            singularName = context.getString(R.string.hour),
            pluralName = context.getString(R.string.hours),
        )
        UnitId.MINUTES -> LongUnitName(
            singularName = context.getString(R.string.minute),
            pluralName = context.getString(R.string.minutes),
        )
        UnitId.SECONDS -> LongUnitName(
            singularName = context.getString(R.string.second),
            pluralName = context.getString(R.string.seconds),
        )
        UnitId.KILOMETERS -> LongUnitName(
            singularName = context.getString(R.string.kilometer),
            pluralName = context.getString(R.string.kilometers),
        )
        UnitId.METERS -> LongUnitName(
            singularName = context.getString(R.string.meter),
            pluralName = context.getString(R.string.meters),
        )
        UnitId.MILES -> LongUnitName(
            singularName = context.getString(R.string.mile),
            pluralName = context.getString(R.string.miles),
        )
        UnitId.FOOT -> LongUnitName(
            singularName = context.getString(R.string.foot),
            pluralName = context.getString(R.string.feet),
        )
        UnitId.YARD -> LongUnitName(
            singularName = context.getString(R.string.yard),
            pluralName = context.getString(R.string.yards),
        )
        UnitId.LITERS -> LongUnitName(
            singularName = context.getString(R.string.liter),
            pluralName = context.getString(R.string.liters),
        )
        UnitId.MILLILITERS -> LongUnitName(
            singularName = context.getString(R.string.milliliter),
            pluralName = context.getString(R.string.milliliters),
        )
        UnitId.CENTILITERS -> LongUnitName(
            singularName = context.getString(R.string.centiliter),
            pluralName = context.getString(R.string.centiliters),
        )
        UnitId.GALLONS -> LongUnitName(
            singularName = context.getString(R.string.gallon),
            pluralName = context.getString(R.string.gallons),
        )
        UnitId.FLUID_OUNCES -> LongUnitName(
            singularName = context.getString(R.string.fluid_ounce),
            pluralName = context.getString(R.string.fluid_ounces),
        )
        UnitId.GLASSES -> LongUnitName(
            singularName = context.getString(R.string.glass),
            pluralName = context.getString(R.string.glasses),
        )
        UnitId.CUPS -> LongUnitName(
            singularName = context.getString(R.string.cup),
            pluralName = context.getString(R.string.cups),
        )
        UnitId.KILOGRAMS -> LongUnitName(
            singularName = context.getString(R.string.kilogram),
            pluralName = context.getString(R.string.kilograms),
        )
        UnitId.GRAMS -> LongUnitName(
            singularName = context.getString(R.string.gram),
            pluralName = context.getString(R.string.grams),
        )
        UnitId.MILLIGRAMS -> LongUnitName(
            singularName = context.getString(R.string.milligram),
            pluralName = context.getString(R.string.milligrams),
        )
        UnitId.POUNDS -> LongUnitName(
            singularName = context.getString(R.string.pound),
            pluralName = context.getString(R.string.pounds),
        )
        UnitId.OUNCE -> LongUnitName(
            singularName = context.getString(R.string.ounce),
            pluralName = context.getString(R.string.ounces),
        )
    }
}

fun UnitId.getShortUnitNamesById(context: Context): ShortUnitName {
    return when (this) {
        UnitId.CALORIES -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.kcal),
            shortUnitPlural = context.getString(R.string.kcal),
        )
        UnitId.TIMES -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.x),
            shortUnitPlural = context.getString(R.string.x),
        )
        UnitId.STEPS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.step),
            shortUnitPlural = context.getString(R.string.steps),
        )
        UnitId.PAGES -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.page),
            shortUnitPlural = context.getString(R.string.pages),
        )
        UnitId.CHAPTERS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.chapter),
            shortUnitPlural = context.getString(R.string.chapters),
        )
        UnitId.BOOKS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.book),
            shortUnitPlural = context.getString(R.string.books),
        )
        UnitId.REPETITIONS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.repetition), // "rep"
            shortUnitPlural = context.getString(R.string.repetitions),
        )
        UnitId.SETS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.set),
            shortUnitPlural = context.getString(R.string.sets),
        )
        UnitId.LAPS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.lap),
            shortUnitPlural = context.getString(R.string.laps),
        )
        UnitId.HOURS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.hour_short_singular),
            shortUnitPlural = context.getString(R.string.hour_short_plural),
        )
        UnitId.MINUTES -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.minute_short_singular),
            shortUnitPlural = context.getString(R.string.minute_short_plural),
        )
        UnitId.SECONDS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.second_short_singular),
            shortUnitPlural = context.getString(R.string.second_short_plural),
        )
        UnitId.KILOMETERS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.kilometer_short),
            shortUnitPlural = context.getString(R.string.kilometer_short),
        )
        UnitId.METERS -> ShortUnitName(
            shortUnitPlural = context.getString(R.string.meter_short),
            shortUnitSingular = context.getString(R.string.meter_short),
        )
        UnitId.MILES -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.mile_short),
            shortUnitPlural = context.getString(R.string.mile_short),
        )
        UnitId.FOOT -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.foot_short),
            shortUnitPlural = context.getString(R.string.foot_short),
        )
        UnitId.YARD -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.yard_short),
            shortUnitPlural = context.getString(R.string.yard_short),
        )
        UnitId.LITERS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.liter_short),
            shortUnitPlural = context.getString(R.string.liter_short),
        )
        UnitId.MILLILITERS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.milliliter_short),
            shortUnitPlural = context.getString(R.string.milliliter_short),
        )
        UnitId.CENTILITERS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.centiliter_short),
            shortUnitPlural = context.getString(R.string.centiliter_short),
        )
        UnitId.GALLONS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.gallon_short),
            shortUnitPlural = context.getString(R.string.gallon_short),
        )
        UnitId.FLUID_OUNCES -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.fluid_ounce_short),
            shortUnitPlural = context.getString(R.string.fluid_ounce_short),
        )
        UnitId.GLASSES -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.glass),
            shortUnitPlural = context.getString(R.string.glasses),
        )
        UnitId.CUPS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.cup),
            shortUnitPlural = context.getString(R.string.cups),
        )
        UnitId.KILOGRAMS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.kilogram_short),
            shortUnitPlural = context.getString(R.string.kilogram_short),
        )
        UnitId.GRAMS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.gram_short),
            shortUnitPlural = context.getString(R.string.gram_short),
        )
        UnitId.MILLIGRAMS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.milligram_short),
            shortUnitPlural = context.getString(R.string.milligram_short),
        )
        UnitId.POUNDS -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.pound_short),
            shortUnitPlural = context.getString(R.string.pound_short),
        )
        UnitId.OUNCE -> ShortUnitName(
            shortUnitSingular = context.getString(R.string.ounce_short),
            shortUnitPlural = context.getString(R.string.ounce_short),
        )
    }
}

fun ReferenceType.toStringResource(): Int {
    return when (this) {
        ReferenceType.GOAL -> R.string.goal
        ReferenceType.LIMIT -> R.string.limit
        ReferenceType.MAX -> R.string.max
    }
}