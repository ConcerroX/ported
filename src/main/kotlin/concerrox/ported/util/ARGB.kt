package concerrox.ported.util

import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object ARGB {

    fun alpha(color: Int): Int {
        return color ushr 24
    }

    fun red(color: Int): Int {
        return color shr 16 and 255
    }

    fun green(color: Int): Int {
        return color shr 8 and 255
    }

    fun blue(color: Int): Int {
        return color and 255
    }

    fun color(alpha: Int, red: Int, green: Int, blue: Int): Int {
        return alpha shl 24 or (red shl 16) or (green shl 8) or blue
    }

    fun color(red: Int, green: Int, blue: Int): Int {
        return color(255, red, green, blue)
    }

    fun color(pColor: Vec3): Int {
        return color(
            as8BitChannel(pColor.x().toFloat()),
            as8BitChannel(pColor.y().toFloat()),
            as8BitChannel(pColor.z().toFloat())
        )
    }

    fun multiply(color1: Int, color2: Int): Int {
        return if (color1 == -1) {
            color2
        } else {
            if (color2 == -1) color1 else color(
                alpha(color1) * alpha(color2) / 255,
                red(color1) * red(color2) / 255,
                green(color1) * green(color2) / 255,
                blue(color1) * blue(color2) / 255
            )
        }
    }

    fun scaleRGB(color: Int, scale: Float): Int {
        return scaleRGB(color, scale, scale, scale)
    }

    fun scaleRGB(pColor: Int, redScale: Float, greenScale: Float, blueScale: Float): Int {
        return color(
            alpha(pColor),
            Math.clamp((red(pColor).toFloat() * redScale).toLong(), 0, 255),
            Math.clamp((green(pColor).toFloat() * greenScale).toLong(), 0, 255),
            Math.clamp((blue(pColor).toFloat() * blueScale).toLong(), 0, 255)
        )
    }

    fun scaleRGB(pColor: Int, scale: Int): Int {
        return color(
            alpha(pColor),
            Math.clamp(red(pColor).toLong() * scale.toLong() / 255L, 0, 255),
            Math.clamp(green(pColor).toLong() * scale.toLong() / 255L, 0, 255),
            Math.clamp(blue(pColor).toLong() * scale.toLong() / 255L, 0, 255)
        )
    }

    fun greyscale(pColor: Int): Int {
        val i =
            (red(pColor).toFloat() * 0.3f + green(pColor).toFloat() * 0.59f + blue(pColor).toFloat() * 0.11f).toInt()
        return color(i, i, i)
    }

    fun lerp(delta: Float, color1: Int, color2: Int): Int {
        val i = Mth.lerpInt(delta, alpha(color1), alpha(color2))
        val j = Mth.lerpInt(delta, red(color1), red(color2))
        val k = Mth.lerpInt(delta, green(color1), green(color2))
        val l = Mth.lerpInt(delta, blue(color1), blue(color2))
        return color(i, j, k, l)
    }

    fun opaque(color: Int): Int {
        return color or -16777216
    }

    fun transparent(color: Int): Int {
        return color and 16777215
    }

    fun color(alpha: Int, color: Int): Int {
        return alpha shl 24 or (color and 16777215)
    }

    fun color(alpha: Float, color: Int): Int {
        return as8BitChannel(alpha) shl 24 or (color and 16777215)
    }

    fun white(alpha: Float): Int {
        return as8BitChannel(alpha) shl 24 or 16777215
    }

    fun colorFromFloat(alpha: Float, red: Float, green: Float, blue: Float): Int {
        return color(as8BitChannel(alpha), as8BitChannel(red), as8BitChannel(green), as8BitChannel(blue))
    }

    fun vector3fFromRGB24(color: Int): Vector3f {
        val f = red(color).toFloat() / 255f
        val f1 = green(color).toFloat() / 255f
        val f2 = blue(color).toFloat() / 255f
        return Vector3f(f, f1, f2)
    }

    fun average(color1: Int, color2: Int): Int {
        return color(
            (alpha(color1) + alpha(color2)) / 2,
            (red(color1) + red(color2)) / 2,
            (green(color1) + green(color2)) / 2,
            (blue(color1) + blue(color2)) / 2
        )
    }

    fun as8BitChannel(value: Float): Int {
        return Mth.floor(value * 255f)
    }

    fun alphaFloat(color: Int): Float {
        return from8BitChannel(alpha(color))
    }

    fun redFloat(color: Int): Float {
        return from8BitChannel(red(color))
    }

    fun greenFloat(color: Int): Float {
        return from8BitChannel(green(color))
    }

    fun blueFloat(color: Int): Float {
        return from8BitChannel(blue(color))
    }

    private fun from8BitChannel(value: Int): Float {
        return value.toFloat() / 255f
    }

    fun toABGR(color: Int): Int {
        return color and -16711936 or ((color and 16711680) shr 16) or ((color and 255) shl 16)
    }

    fun fromABGR(color: Int): Int {
        return toABGR(color)
    }

    fun setBrightness(p_color: Int, brightness: Float): Int {
        var i = red(p_color)
        var j = green(p_color)
        var k = blue(p_color)
        val l = alpha(p_color)
        val i1 = max(max(i, j), k)
        val j1 = min(min(i, j), k)
        val f = (i1 - j1).toFloat()
        val f1: Float
        if (i1 != 0) {
            f1 = f / i1.toFloat()
        } else {
            f1 = 0.0f
        }

        var f2: Float
        if (f1 == 0.0f) {
            f2 = 0.0f
        } else {
            val f3 = (i1 - i).toFloat() / f
            val f4 = (i1 - j).toFloat() / f
            val f5 = (i1 - k).toFloat() / f
            if (i == i1) {
                f2 = f5 - f4
            } else if (j == i1) {
                f2 = 2.0f + f3 - f5
            } else {
                f2 = 4.0f + f4 - f3
            }

            f2 /= 6.0f
            if (f2 < 0.0f) {
                ++f2
            }
        }

        if (f1 == 0.0f) {
            k = (brightness * 255f).roundToInt()
            j = k
            i = j
            return color(l, i, j, k)
        } else {
            val f8 = (f2 - floor(f2)) * 6f
            val f9 = f8 - floor(f8)
            val f10 = brightness * (1f - f1)
            val f6 = brightness * (1f - f1 * f9)
            val f7 = brightness * (1f - f1 * (1f - f9))
            when (f8.toInt()) {
                0 -> {
                    i = (brightness * 255f).roundToInt()
                    j = (f7 * 255f).roundToInt()
                    k = (f10 * 255f).roundToInt()
                }

                1 -> {
                    i = (f6 * 255f).roundToInt()
                    j = (brightness * 255f).roundToInt()
                    k = (f10 * 255f).roundToInt()
                }

                2 -> {
                    i = (f10 * 255f).roundToInt()
                    j = (brightness * 255f).roundToInt()
                    k = (f7 * 255f).roundToInt()
                }

                3 -> {
                    i = (f10 * 255f).roundToInt()
                    j = (f6 * 255f).roundToInt()
                    k = (brightness * 255f).roundToInt()
                }

                4 -> {
                    i = (f7 * 255f).roundToInt()
                    j = (f10 * 255f).roundToInt()
                    k = (brightness * 255f).roundToInt()
                }

                5 -> {
                    i = (brightness * 255f).roundToInt()
                    j = (f10 * 255f).roundToInt()
                    k = (f6 * 255f).roundToInt()
                }
            }

            return color(l, i, j, k)
        }
    }

}