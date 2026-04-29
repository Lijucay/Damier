package de.lijucay.damier.widget.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import de.lijucay.damier.core.data.Activity
import de.lijucay.damier.core.domain.ReferenceType
import java.time.DayOfWeek
import java.time.LocalDate

fun createWaffleBitmap(
    context: Context,
    activityData: Activity,
    widthPx: Int,
    heightPx: Int,
    primaryContainerColor: Color,
    onPrimaryContainerColor: Color,
    onTertiaryContainerColor: Color,
    onErrorContainerColor: Color
): Bitmap {
    val availableWidth = widthPx / 2f

    val bitmap = createBitmap(availableWidth.toInt(), heightPx)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    val checkIns = activityData.checkIns
    val data = checkIns.groupingBy {
        it.timestamp.toLocalDate()
    }.fold(0) { acc, ci -> acc + ci.amount }

    val reference = activityData.activityInfo.reference
    val referenceType = activityData.activityInfo.referenceType
    val endDate = LocalDate.now()

    val cellsPerRow = 7
    val numberOfWeeks = (availableWidth / (heightPx / cellsPerRow.toFloat())).toInt().coerceAtLeast(4)

    // Von heute rückwärts rechnen, damit die aktuellsten Check-ins rechts erscheinen
    val startDate = endDate
        .minusWeeks(numberOfWeeks.toLong())
        .with(DayOfWeek.MONDAY)

    val cellW = availableWidth / numberOfWeeks
    val cellH = heightPx.toFloat() / cellsPerRow
    val padding = 4f
    val cornerRadius = cellW * 0.2f

    val resolvedReference = if (referenceType == ReferenceType.MAX)
        data.values.maxOfOrNull { it }?.coerceAtLeast(1) ?: 1
    else reference.coerceAtLeast(1)

    for (week in 0 until numberOfWeeks) {
        for (day in 0..6) {
            val dayOfWeek = DayOfWeek.of(day + 1)
            val date = startDate.plusWeeks(week.toLong()).with(dayOfWeek)
            val count = data[date] ?: 0
            val intensity = if (resolvedReference > 0)
                (count.toFloat() / resolvedReference).coerceIn(0f, 1f)
            else 0f
            val alpha = 0.1f + 0.9f * intensity

            paint.color = when {
                referenceType == ReferenceType.LIMIT && !date.isAfter(endDate) && count != 0 ->
                    onErrorContainerColor.copy(alpha = alpha).toArgb()
                referenceType == ReferenceType.LIMIT && date.isAfter(endDate) -> primaryContainerColor.toArgb()
                date.isBefore(endDate) && count != 0 ->
                    onPrimaryContainerColor.copy(alpha = alpha).toArgb()
                date.isEqual(endDate) && count != 0 ->
                    onTertiaryContainerColor.copy(alpha = alpha).toArgb()
                date.isAfter(endDate) ->
                    primaryContainerColor.toArgb()
                else -> primaryContainerColor.toArgb()
            }

            val left = week * cellW + padding
            val top = day * cellH + padding
            val right = left + cellW - padding * 2
            val bottom = top + cellH - padding * 2

            canvas.drawRoundRect(left, top, right, bottom, cornerRadius, cornerRadius, paint)
        }
    }

    return bitmap
}