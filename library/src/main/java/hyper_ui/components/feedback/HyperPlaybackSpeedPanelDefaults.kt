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
    val containerColor: Color,
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
    val Shape: Shape = RoundedCornerShape(24.dp)
    val MaxWidth = 520.dp
    const val WidthFraction = 0.9f
    val Elevation = 14.dp
    val ContentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
    val ContentSpacing = 5.dp
    val OverlayHorizontalPadding = 12.dp
    val SliderTouchHeight = 36.dp
    val SliderTrackHeight = 5.dp
    val SliderThumbSize = 20.dp
    val SliderMarkerSize = 4.dp
    val SpeedLabelWidth = 38.dp
    val SpeedLabelHeight = 24.dp

    @Composable
    fun colors(
        scrimColor: Color = Color.Unspecified,
        containerColor: Color = Color.Unspecified,
        panelBorderColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        supportingContentColor: Color = Color.Unspecified,
        accentColor: Color = Color.Unspecified,
        trackColor: Color = Color.Unspecified,
        segmentMarkerColor: Color = Color.Unspecified
    ): HyperPlaybackSpeedPanelColors {
        val resolvedContentColor = resolveHyperContainerColor(
            contentColor,
            HyperColors.primaryText
        )
        return HyperPlaybackSpeedPanelColors(
            scrimColor = resolveHyperContainerColor(
                scrimColor,
                hyperGlass.scrim
            ),
            containerColor = resolveHyperContainerColor(containerColor, hyperGlass.surfaceStrong),
            panelBorderColor = resolveHyperContainerColor(
                panelBorderColor,
                hyperGlass.border
            ),
            contentColor = resolvedContentColor,
            supportingContentColor = resolveHyperContainerColor(
                supportingContentColor,
                HyperColors.secondaryText
            ),
            accentColor = resolveHyperContainerColor(accentColor, HyperColors.accent),
            trackColor = resolveHyperContainerColor(
                trackColor,
                hyperGlass.controlTrack
            ),
            segmentMarkerColor = resolveHyperContainerColor(
                segmentMarkerColor,
                HyperColors.secondaryText
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
