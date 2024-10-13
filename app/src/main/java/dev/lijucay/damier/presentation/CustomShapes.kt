@file:Suppress("unused", "unused")

package dev.lijucay.damier.presentation

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath

enum class PossibleShape {
    ROUNDED_POLYGON, ROUNDED_HEXAGON, ROUNDED_STAR, ROUNDED_TRIANGLE
}

val possibleShapes = PossibleShape.entries

fun roundedPolygon(size: Size): Path {
    val roundedPolygon = RoundedPolygon(
        numVertices = 3,
        radius = size.minDimension / 2,
        centerX = size.width / 2,
        centerY = size.height / 2,
        rounding = CornerRounding(
            size.minDimension / 10f,
            smoothing = 0.1f
        )
    )
    return roundedPolygon.toPath().asComposePath()
}

fun roundedHexagon(size: Size): Path {
    val hexagon = RoundedPolygon(
        6,
        radius = size.minDimension /2,
        centerX = size.width / 2,
        centerY = size.height / 2,
        rounding = CornerRounding(
            size.minDimension / 10f,
            smoothing = 0.1f
        )
    )
    return hexagon.toPath().asComposePath()
}

fun roundedPolygonStar(size: Size): Path {
    val star = RoundedPolygon.star(
        12,
        radius = size.minDimension * 0.45f,
        innerRadius = size.minDimension * 0.4f,
        centerX = size.width / 2,
        centerY = size.width / 2,
        rounding = CornerRounding(
            size.minDimension / 8f,
            smoothing = 0.3f
        )
    )

    return star.toPath().asComposePath()
}

fun roundedTriangle(size: Size): Path {
    val roundedPolygon = RoundedPolygon(
        numVertices = 3,
        radius = size.minDimension / 2,
        centerX = size.width / 2,
        centerY = size.height / 2,
        rounding = CornerRounding(
            size.minDimension / 10f,
            smoothing = 0.1f
        )
    )
    return roundedPolygon.toPath().asComposePath()
}

class RoundedPolygonShape(
    private val numVertices: Int,
    private val radius: Float,
    private val centerX: Float,
    private val centerY: Float,
    private val rounding: CornerRounding
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val roundedPolygon = RoundedPolygon(numVertices, radius, centerX, centerY, rounding)
        val path = roundedPolygon.toPath()
        return Outline.Generic(path.asComposePath())
    }
}

class RoundedPolygonStarShape(
    private val numVertices: Int,
    private val radius: Float,
    private val innerRadius: Float,
    private val centerX: Float,
    private val centerY: Float,
    private val rounding: CornerRounding
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val roundedPolygonStar = RoundedPolygon.star(
            numVertices,
            radius = radius,
            innerRadius = innerRadius,
            centerX = centerX,
            centerY = centerY,
            rounding = rounding
        )
        val path = roundedPolygonStar.toPath()
        return Outline.Generic(path.asComposePath())
    }
}

val roundedTriangleShape = GenericShape { size, _ ->
    val radius = size.minDimension / 2
    val centerX = size.width / 2
    val centerY = size.height / 2

    moveTo(centerX, centerY - radius)
    lineTo(centerX - radius * 0.866f, centerY + radius * 0.5f)
    lineTo(centerX + radius * 0.866f, centerY - radius * 0.5f)
    close()
}