/** 文件职责：调色板 RGB/HSV 换算和颜色文本格式，不依赖 Android 平台。 */
package hyper_ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import kotlin.math.roundToInt

internal data class HyperHsv(val hue: Float, val saturation: Float, val brightness: Float)

internal fun Color.toHyperHsv(fallbackHue: Float, fallbackSaturation: Float): HyperHsv {
    val rgb = convert(ColorSpaces.Srgb)
    val r = rgb.red.coerceIn(0f, 1f)
    val g = rgb.green.coerceIn(0f, 1f)
    val b = rgb.blue.coerceIn(0f, 1f)
    val maximum = maxOf(r, g, b)
    val delta = maximum - minOf(r, g, b)
    val hue = when {
        delta < 0.00001f -> fallbackHue
        maximum == r -> 60f * (((g - b) / delta + 6f) % 6f)
        maximum == g -> 60f * ((b - r) / delta + 2f)
        else -> 60f * ((r - g) / delta + 4f)
    }
    val resolvedHue = if (hue == 0f && fallbackHue == 360f) 360f else hue
    return HyperHsv(resolvedHue, if (maximum == 0f) fallbackSaturation else delta / maximum, maximum)
}

internal fun formatHyperColor(value: Color): String {
    val rgb = value.convert(ColorSpaces.Srgb)
    val channels = listOf(rgb.red, rgb.green, rgb.blue) + if (rgb.alpha < 1f) listOf(rgb.alpha) else emptyList()
    return "#" + channels.joinToString("") {
        (it.coerceIn(0f, 1f) * 255f).roundToInt().toString(16).padStart(2, '0').uppercase()
    }
}
