package goodies.fonts

import extensions.modules.shapes.Line
import goodies.geometry.Point
import processing.core.PGraphics
import processing.core.PVector

class SimpleFont2(
    val baseScaleUpperX: Number = 1,
    val baseScaleUpperY: Number = 1,
    val baseScaleLowerX: Number = 1,
    val baseScaleLowerY: Number = 1,
) : Font {
    private val upperLines =
        listOf(
            Line(PVector(0F, 0F), PVector(1F, 0F)),
            Line(PVector(1F, 0F), PVector(1F, 1F)),
            Line(PVector(0F, 1F), PVector(1F, 1F)),
            Line(PVector(0F, 0F), PVector(0F, 1F)),
        ).map { scaleLines(line = it, scaleX = baseScaleUpperX, scaleY = baseScaleUpperY) }

    val upperXMin: Int = 0
    val upperXMax: Float = 1 * baseScaleUpperX.toFloat()
    val upperYMin: Int = 0
    private val upperYMax = 1 * baseScaleUpperY.toFloat()

    private val lowerLines =
        listOf(
            Line(PVector(0F, 0F), PVector(1F, 0F)),
            Line(PVector(1F, 0F), PVector(1F, 1F)),
            Line(PVector(0F, 1F), PVector(1F, 1F)),
            Line(PVector(0F, 0F), PVector(0F, 1F)),
        ).map { scaleLines(line = it, scaleX = baseScaleLowerX, scaleY = baseScaleLowerY) }

    private fun scaleLines(
        line: Line,
        scaleX: Number,
        scaleY: Number,
    ): Line {
        val newStartX = line.start.x * scaleX.toFloat()
        val newStartY = line.start.y * scaleY.toFloat()

        val newEndX = line.end.x * scaleX.toFloat()
        val newEndY = line.end.y * scaleY.toFloat()

        return Line(PVector(newStartX, newStartY), PVector(newEndX, newEndY))
    }

    fun drawA(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(
            upperLines[BOTTOM],
            scale,
            scale,
        ).getShifted(position.toPVector()).scaleFromEnd(CROSSBAR_LENGTH).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawB(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(
            upperLines[BOTTOM],
            scale,
            scale,
        ).getShifted(position.toPVector()).scaleFromEnd(CROSSBAR_LENGTH).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawC(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(
            upperLines[RIGHT],
            scale,
            scale,
        ).getShifted(position.toPVector()).scaleFromStart(CROSSBAR_LENGTH).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale)
            .getShifted(position.toPVector())
            .getShifted(offSet)
            .scaleFromEnd(CROSSBAR_LENGTH)
            .draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    private fun drawD(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        val topLeft = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).start
        val topMid = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).midPoint
        val rightMid = scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).midPoint
        val topBottomRight = scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).end

        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        Line(
            topLeft,
            topMid,
        ).draw(sketch)

        Line(
            topMid,
            rightMid,
        ).draw(sketch)

        Line(
            rightMid,
            topBottomRight,
        ).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        val bottomRightMid =
            scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).midPoint
        val bottomMid =
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).midPoint
        val bottomLeft =
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).start

        Line(
            topBottomRight,
            bottomRightMid,
        ).draw(sketch)

        Line(
            bottomRightMid,
            bottomMid,
        ).draw(sketch)

        Line(
            bottomLeft,
            bottomMid,
        ).draw(sketch)

        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    private fun drawE(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).scaleFromStart(0.0F).draw(sketch)
        scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale)
            .getShifted(position.toPVector())
            .getShifted(offSet)
            .scaleFromEnd(0.0F)
            .draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    private fun drawF(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    private fun drawG(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(
            upperLines[BOTTOM],
            scale,
            scale,
        ).getShifted(position.toPVector()).scaleFromEnd(CROSSBAR_LENGTH).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawH(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[TOP], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawI(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        Line(
            scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).midPoint,
            scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).midPoint,
        ).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        Line(
            scaleLines(lowerLines[TOP], scale, scale).getShifted(position.toPVector()).getShifted(offSet).midPoint,
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).midPoint,
        ).draw(sketch)
    }

    fun drawJ(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale)
            .getShifted(position.toPVector())
            .getShifted(offSet)
            .scaleFromEnd(HOOK_LENGTH)
            .draw(sketch)
    }

    fun drawK(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)

        val mid = scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).end
        val topRight = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).end
        val bottomRight =
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).end

        Line(mid, topRight).draw(sketch)
        Line(mid, bottomRight).draw(sketch)
    }

    fun drawL(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawM(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        Line(
            scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).start,
            scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).midPoint,
        ).draw(sketch)

        Line(
            scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).start,
            scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).midPoint,
        ).draw(sketch)

        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawN(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        val offSet = PVector(0F, upperYMax * scale.toFloat())
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        Line(
            scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).start,
            scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).end,
        ).draw(sketch)

        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawO(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawP(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(
            upperLines[BOTTOM],
            scale,
            scale,
        ).getShifted(position.toPVector()).scaleFromEnd(BOWL_LENGTH).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawQ(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)

        Line(
            scaleLines(lowerLines[TOP], scale, scale).getShifted(position.toPVector()).getShifted(offSet).midPoint,
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).end,
        ).draw(sketch)
    }

    fun drawR(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(
            upperLines[BOTTOM],
            scale,
            scale,
        ).getShifted(position.toPVector()).scaleFromEnd(BOWL_LENGTH).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        Line(
            scaleLines(
                upperLines[BOTTOM],
                scale,
                scale,
            ).getShifted(position.toPVector()).scaleFromEnd(BOWL_LENGTH).start,
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).end,
        ).draw(sketch)
    }

    fun drawS(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawT(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        drawI(sketch, position, scale)
    }

    fun drawU(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawV(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        val topLine = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector())

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        val bottom =
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).midPoint

        Line(topLine.start, bottom).draw(sketch)
        Line(topLine.end, bottom).draw(sketch)
    }

    fun drawW(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val mid = scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).midPoint

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)

        Line(
            scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).end,
            mid,
        ).draw(sketch)

        Line(
            scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).end,
            mid,
        ).draw(sketch)
    }

    fun drawX(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        val topLeft = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).start
        val topRight = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).end

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        val bottomLeft =
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).start
        val bottomRight =
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).end

        Line(
            topLeft,
            bottomRight,
        ).draw(sketch)

        Line(
            topRight,
            bottomLeft,
        ).draw(sketch)
    }

    fun drawY(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        val topLeft = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).start
        val topRight = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).end

        val mid = scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).midPoint

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        val bottom =
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).midPoint

        Line(
            topLeft,
            mid,
        ).draw(sketch)
        Line(
            topRight,
            mid,
        ).draw(sketch)
        Line(
            mid,
            bottom,
        ).draw(sketch)
    }

    fun drawZ(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        val topLeft = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).start
        val topRight = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).end

        val offSet = PVector(0F, upperYMax * scale.toFloat())

        val bottomLeft =
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).start
        val bottomRight =
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).end

        Line(
            topLeft,
            topRight,
        ).draw(sketch)

        Line(
            topRight,
            bottomLeft,
        ).draw(sketch)

        Line(
            bottomLeft,
            bottomRight,
        ).draw(sketch)
    }

    fun drawDash(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).draw(sketch)
    }

    fun drawSlash(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        val topRight = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).end
        val offSet = PVector(0F, upperYMax * scale.toFloat())
        val bottomLeft =
            scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).start
        Line(bottomLeft, topRight).draw(sketch)
    }

    fun drawZero(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        drawO(sketch, position, scale)
    }

    fun drawOne(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        val topBottomLeft = scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).start
        val topTopRight = scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).end
        Line(topBottomLeft, topTopRight).scaleFromEnd(DIGIT_STROKE_LENGTH).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())
        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawTwo(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawThree(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(
            upperLines[BOTTOM],
            scale,
            scale,
        ).getShifted(position.toPVector()).scaleFromEnd(DIGIT_STROKE_LENGTH).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())
        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawFour(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())
        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawFive(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(
            upperLines[LEFT],
            scale,
            scale,
        ).getShifted(position.toPVector()).scaleFromEnd(DIGIT_STROKE_LENGTH).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())
        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawSix(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())
        scaleLines(lowerLines[TOP], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawSeven(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)

        drawSlash(sketch, position, scale)
    }

    fun drawEight(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())
        scaleLines(lowerLines[TOP], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[BOTTOM], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
        scaleLines(lowerLines[LEFT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawNine(
        sketch: PGraphics,
        position: Point,
        scale: Number,
    ) {
        scaleLines(upperLines[TOP], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[RIGHT], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[BOTTOM], scale, scale).getShifted(position.toPVector()).draw(sketch)
        scaleLines(upperLines[LEFT], scale, scale).getShifted(position.toPVector()).draw(sketch)

        val offSet = PVector(0F, upperYMax * scale.toFloat())
        scaleLines(lowerLines[RIGHT], scale, scale).getShifted(position.toPVector()).getShifted(offSet).draw(sketch)
    }

    fun drawLetter(
        sketch: PGraphics,
        letter: String,
        position: Point,
        scale: Number,
    ) {
        when (letter) {
            "-" -> {
                drawDash(sketch, position, scale)
            }

            "/" -> {
                drawSlash(sketch, position, scale)
            }

            " " -> {
                return
            }

            "0" -> {
                drawZero(sketch, position, scale)
            }

            "1" -> {
                drawOne(sketch, position, scale)
            }

            "2" -> {
                drawTwo(sketch, position, scale)
            }

            "3" -> {
                drawThree(sketch, position, scale)
            }

            "4" -> {
                drawFour(sketch, position, scale)
            }

            "5" -> {
                drawFive(sketch, position, scale)
            }

            "6" -> {
                drawSix(sketch, position, scale)
            }

            "7" -> {
                drawSeven(sketch, position, scale)
            }

            "8" -> {
                drawEight(sketch, position, scale)
            }

            "9" -> {
                drawNine(sketch, position, scale)
            }

            "A" -> {
                drawA(sketch, position, scale)
            }

            "B" -> {
                drawB(sketch, position, scale)
            }

            "C" -> {
                drawC(sketch, position, scale)
            }

            "D" -> {
                drawD(sketch, position, scale)
            }

            "E" -> {
                drawE(sketch, position, scale)
            }

            "F" -> {
                drawF(sketch, position, scale)
            }

            "G" -> {
                drawG(sketch, position, scale)
            }

            "H" -> {
                drawH(sketch, position, scale)
            }

            "I" -> {
                drawI(sketch, position, scale)
            }

            "J" -> {
                drawJ(sketch, position, scale)
            }

            "K" -> {
                drawK(sketch, position, scale)
            }

            "L" -> {
                drawL(sketch, position, scale)
            }

            "M" -> {
                drawM(sketch, position, scale)
            }

            "N" -> {
                drawN(sketch, position, scale)
            }

            "O" -> {
                drawO(sketch, position, scale)
            }

            "P" -> {
                drawP(sketch, position, scale)
            }

            "Q" -> {
                drawQ(sketch, position, scale)
            }

            "R" -> {
                drawR(sketch, position, scale)
            }

            "S" -> {
                drawS(sketch, position, scale)
            }

            "T" -> {
                drawT(sketch, position, scale)
            }

            "U" -> {
                drawU(sketch, position, scale)
            }

            "V" -> {
                drawV(sketch, position, scale)
            }

            "W" -> {
                drawW(sketch, position, scale)
            }

            "X" -> {
                drawX(sketch, position, scale)
            }

            "Y" -> {
                drawY(sketch, position, scale)
            }

            "Z" -> {
                drawZ(sketch, position, scale)
            }
        }
    }

    fun drawLetters(
        sketch: PGraphics,
        letters: String,
        position: Point,
        offset: Number,
        scale: Number,
    ) {
        for ((i, letter) in letters.withIndex()) {
            val pos = Point(position.x + offset.toFloat() * i, position.y)
            drawLetter(sketch = sketch, letter = letter.toString(), position = pos, scale = scale)
        }
    }
}

private const val TOP = 0
private const val RIGHT = 1
private const val BOTTOM = 2
private const val LEFT = 3
private const val CROSSBAR_LENGTH = 0.65F
private const val HOOK_LENGTH = 0.3F
private const val BOWL_LENGTH = 0.8F
private const val DIGIT_STROKE_LENGTH = 0.6F
