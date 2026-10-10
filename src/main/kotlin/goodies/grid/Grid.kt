package goodies.grid

import goodies.geometry.Point

interface Grid {
    val coordinates: List<List<Point>>
}