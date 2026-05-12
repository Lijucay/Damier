package de.lijucay.damier.core.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.lijucay.damier.core.domain.ReferenceType
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Cell(
    modifier: Modifier = Modifier,
    checkInCount: Int,
    currentDate: LocalDate,
    endDate: LocalDate,
    reference: Int,
    type: ReferenceType,
    isSelected: Boolean = false
) {
    val cellColor = getCellColor(
        type = type,
        checkInCount = checkInCount,
        currentDate = currentDate,
        endDate = endDate,
        reference = reference
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(MaterialShapes.Square.toShape())
            .background(color = cellColor)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = contentColorFor(cellColor),
                        shape = MaterialShapes.Square.toShape()
                    )
                } else Modifier
            )
    )
}

@Composable
private fun getCellColor(
    type: ReferenceType,
    checkInCount: Int,
    currentDate: LocalDate,
    endDate: LocalDate,
    reference: Int
): Color {
    val intensity = if (reference > 0)
            (checkInCount.toFloat() / reference).coerceIn(0f, 1f)
    else 0f

    val alpha = 0.1f + (0.9f * intensity)

    val targetColor = when {
        type == ReferenceType.LIMIT &&
        (
            currentDate.isBefore(endDate) ||
            currentDate.isEqual(endDate)
        ) &&
        checkInCount != 0 -> MaterialTheme.colorScheme.onErrorContainer.copy(alpha = alpha)
        type == ReferenceType.LIMIT &&
        currentDate.isAfter(endDate) -> MaterialTheme.colorScheme.errorContainer
        currentDate.isBefore(endDate) && checkInCount != 0 -> MaterialTheme.colorScheme.primary.copy(alpha = alpha)
        currentDate.isEqual(endDate) && checkInCount != 0 -> MaterialTheme.colorScheme.tertiary.copy(alpha = alpha)
        currentDate.isAfter(endDate) -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0f)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    }

    val animatedColor by animateColorAsState(targetValue = targetColor)
    return animatedColor
}