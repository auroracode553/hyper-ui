/** 文件职责：集中维护播放速度面板的公开文案、颜色、尺寸、速度范围与格式化规则。 */
package hyper_ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Immutable
data class HyperPlaybackSpeedPanelTexts(
    val title: String = "播放速度",
    val realtimeHint: String = "拖动调节 · 实时生效",
    val customAction: String = "自定义",
    val sliderContentDescription: String = "播放速度滑块",
    val closeContentDescription: String = "关闭播放速度面板",
    val resetContentDescription: String = "恢复默认速度"
)

@Immutable
data class HyperPlaybackSpeedPanelColors(
    val scrimColor: Color,
    val containerTopColor: Color,
    val containerBottomColor: Color,
    val panelBorderColor: Color,
    val contentColor: Color,
    val supportingContentColor: Color,
    val accentColor: Color,
    val trackColor: Color,
    val segmentMarkerColor: Color
)

object HyperPlaybackSpeedPanelDefaults {
    const val SliderMinimum = 0.25f
    const val SliderMaximum = 4f
    const val SliderStep = 0.05f
    const val SliderSteps = 74
    const val DefaultSpeed = 1f
    const val CustomMinimum = 0.1f
    const val CustomMaximum = 8f

    val SliderRange = SliderMinimum..SliderMaximum
    val MajorSpeeds = listOf(0.25f, DefaultSpeed, 2f, 3f, 4f)
    val Shape: Shape = RoundedCornerShape(20.dp)
    val MaxWidth = 520.dp
    const val WidthFraction = 0.9f
    val Elevation = 14.dp
    val ContentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
    val ContentSpacing = 5.dp
    val OverlayHorizontalPadding = 12.dp
    val SliderTouchHeight = 36.dp
    val SliderTrackHeight = 5.dp
    val SliderThumbSize = 18.dp
    val SliderMarkerSize = 4.dp
    val SpeedLabelWidth = 38.dp
    val SpeedLabelHeight = 24.dp

    @Composable
    fun colors(
        scrimColor: Color = Color.Unspecified,
        containerTopColor: Color = Color.Unspecified,
        containerBottomColor: Color = Color.Unspecified,
        panelBorderColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        supportingContentColor: Color = Color.Unspecified,
        accentColor: Color = Color.Unspecified,
        trackColor: Color = Color.Unspecified,
        segmentMarkerColor: Color = Color.Unspecified
    ): HyperPlaybackSpeedPanelColors {
        val resolvedContentColor = resolveHyperContainerColor(
            contentColor,
            Color(1f, 1f, 1f, 1f)
        )
        return HyperPlaybackSpeedPanelColors(
            scrimColor = resolveHyperContainerColor(
                scrimColor,
                Color(0f, 0f, 0f, 0.08f)
            ),
            containerTopColor = resolveHyperContainerColor(
                containerTopColor,
                Color(0.145f, 0.157f, 0.2f, 0.95f)
            ),
            containerBottomColor = resolveHyperContainerColor(
                containerBottomColor,
                Color(0.086f, 0.09f, 0.114f, 0.96f)
            ),
            panelBorderColor = resolveHyperContainerColor(
                panelBorderColor,
                Color(1f, 1f, 1f, 0.15f)
            ),
            contentColor = resolvedContentColor,
            supportingContentColor = resolveHyperContainerColor(
                supportingContentColor,
                Color(1f, 1f, 1f, 0.58f)
            ),
            accentColor = resolveHyperContainerColor(accentColor, HyperColors.accent),
            trackColor = resolveHyperContainerColor(
                trackColor,
                Color(1f, 1f, 1f, 0.16f)
            ),
            segmentMarkerColor = resolveHyperContainerColor(
                segmentMarkerColor,
                Color(1f, 1f, 1f, 0.58f)
            )
        )
    }

    fun formatCompact(speed: Float): String {
        val hundredths = normalizedHundredths(speed)
        val whole = hundredths / 100
        val decimal = hundredths % 100
        return when {
            decimal == 0 -> whole.toString()
            decimal % 10 == 0 -> "$whole.${decimal / 10}"
            else -> "$whole.${decimal.toString().padStart(2, '0')}"
        }
    }

    fun formatFixed(speed: Float): String {
        val hundredths = normalizedHundredths(speed)
        val whole = hundredths / 100
        val decimal = hundredths % 100
        return "$whole.${decimal.toString().padStart(2, '0')}"
    }

    /** 只保留正数、小数点与两位小数，数值范围由调用方负责校验。 */
    fun normalizeInput(value: String): String {
        val result = StringBuilder()
        var decimalSeen = false
        var decimalDigits = 0

        value.replace(',', '.').forEach { character ->
            when {
                character.isDigit() && (!decimalSeen || decimalDigits < 2) -> {
                    result.append(character)
                    if (decimalSeen) decimalDigits++
                }
                character == '.' && !decimalSeen -> {
                    if (result.isEmpty()) result.append('0')
                    result.append(character)
                    decimalSeen = true
                }
            }
        }
        return result.toString().take(5)
    }
}

private fun normalizedHundredths(speed: Float): Int = if (speed.isFinite()) {
    (speed.coerceAtLeast(0f) * 100f).roundToInt()
} else {
    0
}
