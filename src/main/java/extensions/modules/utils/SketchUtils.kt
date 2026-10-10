package extensions.modules.utils

import processing.core.PApplet


object SketchUtils {

    @JvmStatic
    private fun getSketchName(sketch: PApplet): String {
        return sketch.javaClass.name
    }

    @JvmStatic
    fun getSketchNameAndSetSeed(sketch: PApplet): String {
        val baseNAme = getSketchName(sketch)
        val seed = sketch.millis()
        sketch.noiseSeed(seed.toLong())
        sketch.randomSeed(seed.toLong())
        return baseNAme + "_" + seed
    }
}
