package goodies.utils

import processing.core.PApplet.cos
import processing.core.PApplet.pow
import processing.core.PApplet.sin
import processing.core.PApplet.sqrt
import processing.core.PConstants.PI

// Taken from https://github.com/sighack/easing-functions/blob/master/code/easing/easing.pde

enum class EaseFunctionType {
    LINEAR,
    QUADRATIC,
    CUBIC,
    QUARTIC,
    QUINTIC,
    SINUSOIDAL,
    EXPONENTIAL,
    CIRCULAR,
    SQRT,
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
    var t = value - start1
    val d = stop1 - start1
    val p = 0.5.toFloat()

    when (type) {
        EaseFunctionType.LINEAR -> {
            return c * t / d + start2
        }

        EaseFunctionType.SQRT -> {
            when (easingType) {
                EasingType.EASE_IN -> {
                    t /= d
                    return c * pow(t, p) + start2
                }

                EasingType.EASE_OUT -> {
                    t /= d
                    return c * (1 - pow(1 - t, p)) + start2
                }

                EasingType.EASE_IN_OUT -> {
                    t /= d / 2
                    return if (t < 1) c / 2 * pow(t, p) + start2 else c / 2 * (2 - pow(2 - t, p)) + start2
                }
            }
        }

        EaseFunctionType.QUADRATIC -> {
            when (easingType) {
                EasingType.EASE_IN -> {
                    t /= d
                    return c * t * t + start2
                }

                EasingType.EASE_OUT -> {
                    t /= d
                    return -c * t * (t - 2) + start2
                }

                EasingType.EASE_IN_OUT -> {
                    t /= d / 2
                    if (t < 1) return c / 2 * t * t + start2
                    t--
                    return -c / 2 * (t * (t - 2) - 1) + start2
                }
            }
        }

        EaseFunctionType.CUBIC -> {
            when (easingType) {
                EasingType.EASE_IN -> {
                    t /= d
                    return c * t * t * t + start2
                }

                EasingType.EASE_OUT -> {
                    t /= d
                    t--
                    return c * (t * t * t + 1) + start2
                }

                EasingType.EASE_IN_OUT -> {
                    t /= d / 2
                    if (t < 1) return c / 2 * t * t * t + start2
                    t -= 2f
                    return c / 2 * (t * t * t + 2) + start2
                }
            }
        }

        EaseFunctionType.QUARTIC -> {
            when (easingType) {
                EasingType.EASE_IN -> {
                    t /= d
                    return c * t * t * t * t + start2
                }

                EasingType.EASE_OUT -> {
                    t /= d
                    t--
                    return -c * (t * t * t * t - 1) + start2
                }

                EasingType.EASE_IN_OUT -> {
                    t /= d / 2
                    if (t < 1) return c / 2 * t * t * t * t + start2
                    t -= 2f
                    return -c / 2 * (t * t * t * t - 2) + start2
                }
            }
        }

        EaseFunctionType.QUINTIC -> {
            when (easingType) {
                EasingType.EASE_IN -> {
                    t /= d
                    return c * t * t * t * t * t + start2
                }

                EasingType.EASE_OUT -> {
                    t /= d
                    t--
                    return c * (t * t * t * t * t + 1) + start2
                }

                EasingType.EASE_IN_OUT -> {
                    t /= d / 2
                    if (t < 1) return c / 2 * t * t * t * t * t + start2
                    t -= 2f
                    return c / 2 * (t * t * t * t * t + 2) + start2
                }
            }
        }

        EaseFunctionType.SINUSOIDAL -> {
            return when (easingType) {
                EasingType.EASE_IN -> -c * cos(t / d * (PI / 2)) + c + start2
                EasingType.EASE_OUT -> c * sin(t / d * (PI / 2)) + start2
                EasingType.EASE_IN_OUT -> -c / 2 * (cos(PI * t / d) - 1) + start2
            }
        }

        EaseFunctionType.EXPONENTIAL -> {
            when (easingType) {
                EasingType.EASE_IN -> {
                    return c * pow(2F, 10 * (t / d - 1)) + start2
                }

                EasingType.EASE_OUT -> {
                    return c * (-pow(2F, -10 * t / d) + 1) + start2
                }

                EasingType.EASE_IN_OUT -> {
                    t /= d / 2
                    if (t < 1) return c / 2 * pow(2F, 10 * (t - 1)) + start2
                    t--
                    return c / 2 * (-pow(2F, -10 * t) + 2) + start2
                }
            }
        }

        EaseFunctionType.CIRCULAR -> {
            when (easingType) {
                EasingType.EASE_IN -> {
                    t /= d
                    return -c * (sqrt(1 - t * t) - 1) + start2
                }

                EasingType.EASE_OUT -> {
                    t /= d
                    t--
                    return c * sqrt(1 - t * t) + start2
                }

                EasingType.EASE_IN_OUT -> {
                    t /= d / 2
                    if (t < 1) return -c / 2 * (sqrt(1 - t * t) - 1) + start2
                    t -= 2f
                    return c / 2 * (sqrt(1 - t * t) + 1) + start2
                }
            }
        }
    }
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
            out = c * (1 - pow((1 - t), v)) + start2
        }

        EasingType.EASE_IN_OUT -> {
            t /= d / 2
            if (t < 1) return c / 2 * pow(t, v) + start2
            out = c / 2 * (2 - pow((2 - t), v)) + start2
        }
    }
    return out
}
