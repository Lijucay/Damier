package dev.lijucay.damier.presentation.composables

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.lijucay.damier.R
import kotlin.math.max

@Composable
fun LastSevenDaysGraph(data: List<Pair<String, Int>>, goal: Int) {
    val maxCheckIns = data.maxOf { it.second }
    val maxValue = max(maxCheckIns, goal).coerceAtLeast(1)

    val onPrimaryContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    Column(modifier = Modifier.padding(16.dp)) {
        MediumTitleText(
            text = stringResource(R.string.seven_days_check_ins),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(200.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barWidth = canvasWidth / 9 // Divided with by 9 for 7 bars and some padding

            // X-Axis:
            drawLine(
                color = onPrimaryContainerColor,
                start = Offset(30f, canvasHeight),
                end = Offset(canvasWidth, canvasHeight),
                strokeWidth = 2f
            )

            // Y-Axis
            drawLine(
                color = onPrimaryContainerColor,
                start = Offset(30f, 0f),
                end = Offset(30f, canvasHeight),
                strokeWidth = 2f
            )

            data.forEachIndexed { index, (day, count) ->
                // Calculate bar height based on count and maximum value
                val barHeight = (count.toFloat() / maxValue) * canvasHeight
                val currentBarColor = if (index != data.size-1) primaryColor else tertiaryColor
                val xPos = index * (barWidth + barWidth / 7) + barWidth / 2

                drawRoundRect(
                    color = currentBarColor,
                    topLeft = Offset(xPos, canvasHeight - barHeight),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(100f, 100f)
                )

                drawContext.canvas.nativeCanvas.drawText(
                    day,
                    xPos + barWidth / 2,
                    canvasHeight + 30f,
                    Paint().apply {
                        color = currentBarColor.toArgb()
                        textSize = 30f
                        textAlign = Paint.Align.CENTER
                    }
                )

                drawContext.canvas.nativeCanvas.drawText(
                    count.toString(),
                    xPos + barWidth / 2,
                    canvasHeight - barHeight - 10f,
                    Paint().apply {
                        color = currentBarColor.toArgb()
                        textSize = 30f
                        textAlign = Paint.Align.CENTER
                    }
                )
            }

            for (i in 0..4) {
                val y = canvasHeight - (i * canvasHeight / 4)
                drawLine(
                    color = onPrimaryContainerColor,
                    start = Offset(30f, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )

                drawContext.canvas.nativeCanvas.drawText(
                    (maxValue.toDouble() * i.toDouble() / 4.0).toString(),
                    20f,
                    y,
                    Paint().apply {
                        color = primaryColor.toArgb()
                        textSize = 30f
                        textAlign = Paint.Align.RIGHT
                    }
                )
            }
        }
    }
}