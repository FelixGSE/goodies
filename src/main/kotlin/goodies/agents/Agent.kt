package goodies.agents

import goodies.geometry.Point
import goodies.geometry.Polygon
import processing.core.PApplet
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Agent(
    var pos: Point,
    val boundary: Polygon,
    var size: Float = (Math.random() * SIZE_SPREAD + MIN_SIZE).toFloat(),
    var speed: Float = (Math.random() * SPEED_SPREAD + MIN_SPEED).toFloat(),
    var dir: Int = 1,
) {
    constructor(boundary: Polygon) : this(
        pos = boundary.randomPointOn(),
        boundary = boundary,
    )

    fun update(
        curly: Float,
        curlx: Float,
        noiseScale: Float,
        noiseStrength: Float,
    ) {
        val pApplet = PApplet()
        val angle = pApplet.noise(pos.x / noiseScale, pos.y / noiseScale) * noiseStrength
        val angle2 = atan2(y = (curly - pos.y).toDouble(), x = (curlx - pos.x).toDouble()).toFloat()
        val nextX = cos((angle2 - PApplet.HALF_PI).toDouble()).toFloat() * speed * dir
        val nextY = sin((angle + angle2 - PApplet.HALF_PI).toDouble()).toFloat() * speed * dir

        val nextPos = Point(nextX, nextY)
        if (!boundary.contains(nextPos)) {
            dir *= -1
            update(curly = curly, curlx = curlx, noiseScale = noiseScale, noiseStrength = noiseStrength)
        } else {
            pos = nextPos
        }
    }

    fun draw(sketch: PApplet) {
        sketch.point(pos.x, pos.y)
    }
}

private const val MIN_SIZE = 5
private const val SIZE_SPREAD = 45
private const val MIN_SPEED = 10
private const val SPEED_SPREAD = 10
