package goodies.utils

import processing.core.PApplet.cos
import processing.core.PApplet.pow
import processing.core.PApplet.sin
import processing.core.PApplet.sqrt
import processing.core.PConstants.PI

// Taken from https://github.com/sighack/easing-functions/blob/master/code/easing/easing.pde

enum class EaseFunctionType {
    LINEAR {
        override fun ease(
            change: Float,
            elapsed: Float,
            duration: Float,
            easingType: EasingType,
        ): Float = change * elapsed / duration
    },

    QUADRATIC {
        override fun ease(
            change: Float,
            elapsed: Float,
            duration: Float,
            easingType: EasingType,
        ): Float =
            when (easingType) {
                EasingType.EASE_IN -> {
                    val s = elapsed / duration
                    change * s * s
                }

                EasingType.EASE_OUT -> {
                    val s = elapsed / duration
                    -change * s * (s - 2)
                }

                EasingType.EASE_IN_OUT -> {
                    val s = elapsed / (duration / 2)
                    if (s < 1) change / 2 * s * s else -change / 2 * ((s - 1) * (s - 1 - 2) - 1)
                }
            }
    },

    CUBIC {
        override fun ease(
            change: Float,
            elapsed: Float,
            duration: Float,
            easingType: EasingType,
        ): Float =
            when (easingType) {
                EasingType.EASE_IN -> {
                    val s = elapsed / duration
                    change * s * s * s
                }

                EasingType.EASE_OUT -> {
                    val s = elapsed / duration - 1
                    change * (s * s * s + 1)
                }

                EasingType.EASE_IN_OUT -> {
                    val s = elapsed / (duration / 2)
                    if (s < 1) change / 2 * s * s * s else change / 2 * ((s - 2f) * (s - 2f) * (s - 2f) + 2)
                }
            }
    },

    QUARTIC {
        override fun ease(
            change: Float,
            elapsed: Float,
            duration: Float,
            easingType: EasingType,
        ): Float =
            when (easingType) {
                EasingType.EASE_IN -> {
                    val s = elapsed / duration
                    change * s * s * s * s
                }

                EasingType.EASE_OUT -> {
                    val s = elapsed / duration - 1
                    -change * (s * s * s * s - 1)
                }

                EasingType.EASE_IN_OUT -> {
                    val s = elapsed / (duration / 2)
                    if (s <
                        1
                    ) {
                        change / 2 * s * s * s * s
                    } else {
                        -change / 2 * ((s - 2f) * (s - 2f) * (s - 2f) * (s - 2f) - 2)
                    }
                }
            }
    },

    QUINTIC {
        override fun ease(
            change: Float,
            elapsed: Float,
            duration: Float,
            easingType: EasingType,
        ): Float =
            when (easingType) {
                EasingType.EASE_IN -> {
                    val s = elapsed / duration
                    change * s * s * s * s * s
                }

                EasingType.EASE_OUT -> {
                    val s = elapsed / duration - 1
                    change * (s * s * s * s * s + 1)
                }

                EasingType.EASE_IN_OUT -> {
                    val s = elapsed / (duration / 2)
                    val u = s - 2f
                    if (s < 1) change / 2 * s * s * s * s * s else change / 2 * (u * u * u * u * u + 2)
                }
            }
    },

    SINUSOIDAL {
        override fun ease(
            change: Float,
            elapsed: Float,
            duration: Float,
            easingType: EasingType,
        ): Float =
            when (easingType) {
                EasingType.EASE_IN -> -change * cos(elapsed / duration * (PI / 2)) + change
                EasingType.EASE_OUT -> change * sin(elapsed / duration * (PI / 2))
                EasingType.EASE_IN_OUT -> -change / 2 * (cos(PI * elapsed / duration) - 1)
            }
    },

    EXPONENTIAL {
        override fun ease(
            change: Float,
            elapsed: Float,
            duration: Float,
            easingType: EasingType,
        ): Float =
            when (easingType) {
                EasingType.EASE_IN -> {
                    change * pow(2F, EXPONENT_SCALE * (elapsed / duration - 1))
                }

                EasingType.EASE_OUT -> {
                    change * (-pow(2F, -EXPONENT_SCALE * elapsed / duration) + 1)
                }

                EasingType.EASE_IN_OUT -> {
                    val s = elapsed / (duration / 2)
                    val u = s - 1
                    val half = change / 2
                    if (s < 1) half * pow(2F, EXPONENT_SCALE * u) else half * (2 - pow(2F, -EXPONENT_SCALE * u))
                }
            }
    },

    CIRCULAR {
        override fun ease(
            change: Float,
            elapsed: Float,
            duration: Float,
            easingType: EasingType,
        ): Float =
            when (easingType) {
                EasingType.EASE_IN -> {
                    val s = elapsed / duration
                    -change * (sqrt(1 - s * s) - 1)
                }

                EasingType.EASE_OUT -> {
                    val s = elapsed / duration - 1
                    change * sqrt(1 - s * s)
                }

                EasingType.EASE_IN_OUT -> {
                    val s = elapsed / (duration / 2)
                    val u = s - 2f
                    if (s < 1) -change / 2 * (sqrt(1 - s * s) - 1) else change / 2 * (sqrt(1 - u * u) + 1)
                }
            }
    },

    SQRT {
        override fun ease(
            change: Float,
            elapsed: Float,
            duration: Float,
            easingType: EasingType,
        ): Float =
            when (easingType) {
                EasingType.EASE_IN -> {
                    change * pow(elapsed / duration, SQRT_EXPONENT)
                }

                EasingType.EASE_OUT -> {
                    change * (1 - pow(1 - elapsed / duration, SQRT_EXPONENT))
                }

                EasingType.EASE_IN_OUT -> {
                    val s = elapsed / (duration / 2)
                    if (s < 1) change / 2 * pow(s, SQRT_EXPONENT) else change / 2 * (2 - pow(2 - s, SQRT_EXPONENT))
                }
            }
    },
    ;

    internal abstract fun ease(
        change: Float,
        elapsed: Float,
        duration: Float,
        easingType: EasingType,
    ): Float
}

enum class EasingType {
    EASE_IN,
    EASE_OUT,
    EASE_IN_OUT,
}

fun map2(
    value: Float,
    start1: Float,
    stop1: Float,
    start2: Float,
    stop2: Float,
    type: EaseFunctionType = EaseFunctionType.LINEAR,
    easingType: EasingType = EasingType.EASE_IN_OUT,
): Float {
    val c = stop2 - start2
    val t = value - start1
    val d = stop1 - start1
    return type.ease(change = c, elapsed = t, duration = d, easingType = easingType) + start2
}

/*
 * A map() replacement that allows for specifying easing curves
 * with arbitrary exponents.
 *
 * value :   The value to map
 * start1:   The lower limit of the input range
 * stop1 :   The upper limit of the input range
 * start2:   The lower limit of the output range
 * stop2 :   The upper limit of the output range
 * v     :   The exponent value (e.g., 0.5, 0.1, 0.3)
 * when  :   One of EASE_IN, EASE_OUT, or EASE_IN_OUT
 */
fun map3(
    value: Float,
    start1: Float,
    stop1: Float,
    start2: Float,
    stop2: Float,
    v: Float,
    easingType: EasingType,
): Float {
    val c = stop2 - start2
    var t = value - start1
    val d = stop1 - start1
    var out = 0f

    when (easingType) {
        EasingType.EASE_IN -> {
            t /= d
            out = c * pow(t, v) + start2
        }

        EasingType.EASE_OUT -> {
            t /= d
            out = c * (1 - pow(1 - t, v)) + start2
        }

        EasingType.EASE_IN_OUT -> {
            t /= d / 2
            if (t < 1) return c / 2 * pow(t, v) + start2
            out = c / 2 * (2 - pow(2 - t, v)) + start2
        }
    }
    return out
}

private const val SQRT_EXPONENT = 0.5f
private const val EXPONENT_SCALE = 10
