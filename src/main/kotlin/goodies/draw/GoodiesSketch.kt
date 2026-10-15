package goodies.draw

import goodies.geometry.Point
import processing.core.PApplet
import java.io.File
import java.util.Locale

abstract class GoodiesSketch : PApplet() {
    private val sketchRenderer = P2D
    val outputFolder: String = getDesktopPath()

    val center: Point
        get() = Point(width / 2, height / 2)

    override fun settings() {
        fullScreen(sketchRenderer)
    }

    override fun setup(): Unit = throw NotImplementedError("setup() must be implemented in subclasses.")

    override fun draw(): Unit = throw NotImplementedError("draw() must be implemented in subclasses.")

    fun getDesktopPath(): String {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        check(
            DESKTOP_SYSTEMS.any { os.contains(it) },
        ) { "Cannot determine the Desktop path for this operating system." }
        return System.getProperty("user.home") + File.separator + "Desktop"
    }
}

private val DESKTOP_SYSTEMS = listOf("win", "mac", "nix", "nux", "bsd")
