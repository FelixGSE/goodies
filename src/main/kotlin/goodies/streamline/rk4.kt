package goodies.streamline

import goodies.geometry.Point


fun rk4(point: Point, timeStep: Double, getVelocity: (Point) -> Point?): Point? {
    val k1 = getVelocity(point)
    if (k1 == null) return null
    val k2 = getVelocity(point + k1 * (timeStep * 0.5))
    if (k2 == null) return null
    val k3 = getVelocity(point + k2 * (timeStep * 0.5))
    if (k3 == null) return null
    val k4 = getVelocity(point + k3 * timeStep)
    if (k4 == null) return null

    val res = k1 * (timeStep / 6) + k2 * (timeStep / 3) + k3 * (timeStep / 3) + k4 * (timeStep / 6)
    return res
}
