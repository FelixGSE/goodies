package goodies.draw

import goodies.geometry.Point
import processing.core.PApplet
import java.io.File
import java.util.*
import kotlin.random.Random

abstract class GoodiesSketch : PApplet() {

    private val availableSketchRenderer = listOf(P2D, P3D)
    private val noiseSeedValue = Random.nextLong()
    private val randomSeedValue = Random.nextLong()
    private val sketchRenderer = P2D
    val outputFolder = getDesktopPath()
    private val outputFormat = "png"
    private val defaultStrokeWeight = 1F
    private val defaultStrokeColor = 0
    private val exitOnSave = true

    val center: Point
        get() {
            return Point(width / 2, height / 2)
        }

    override fun settings() {
        fullScreen(sketchRenderer)

    }

    override fun setup() {
        throw NotImplementedError("setup() must be implemented in subclasses.")
    }

    override fun draw() {
        throw NotImplementedError("draw() must be implemented in subclasses.")
    }

    private fun saveAndExit() {
        val currentTimestamp = System.currentTimeMillis()
        val sketchName = this.javaClass.name
        save("$outputFolder/$sketchName _$currentTimestamp _$noiseSeedValue _$randomSeedValue.$outputFormat")
        if(exitOnSave) {
            exit()
        }
    }

    fun getDesktopPath(): String {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())

        val desktopFolderName = when {
            os.contains("win") -> "Desktop"
            os.contains("mac") -> "Desktop"
            os.contains("nix") || os.contains("nux") || os.contains("bsd") -> "Desktop"
            else -> null
        }

        if (desktopFolderName == null) {
            throw Exception("Cannot determine the Desktop path for this operating system.")
        } else {
            return System.getProperty("user.home") + File.separator + desktopFolderName
        }


    }




}

