/** 文件职责：提供播放器长按临时倍速使用的柔性玻璃刻度反馈组件。 */
package hyper_ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Immutable
data class HyperPlaybackSpeedScaleColors(
    val glassHighlightColor: Color,
    val glassContainerColor: Color,
    val borderColor: Color,
    val trackColor: Color,
    val activeTrackColor: Color,
    val tickColor: Color,
    val selectedTickColor: Color,
    val labelColor: Color,
    val selectedLabelColor: Color,
    val valueColor: Color
)

/**
 * 播放倍速刻度反馈。组件只渲染速度选项和当前选择，长按与横向拖动由调用方处理。
 */
@Composable
fun HyperPlaybackSpeedScale(
    selectedSpeed: Float,
    modifier: Modifier = Modifier,
    speedOptions: List<Float> = HyperPlaybackSpeedScaleDefaults.SpeedOptions,
    shape: Shape = HyperPlaybackSpeedScaleDefaults.Shape,
    colors: HyperPlaybackSpeedScaleColors = HyperPlaybackSpeedScaleDefaults.colors(),
    leadingContent: (@Composable () -> Unit)? = null
) {
    val resolvedOptions = speedOptions.ifEmpty { HyperPlaybackSpeedScaleDefaults.SpeedOptions }
    val selectedIndex = resolvedOptions.indices.minByOrNull { index ->
        abs(resolvedOptions[index] - selectedSpeed)
    } ?: 0

    Column(
        modifier = modifier
            .widthIn(max = HyperPlaybackSpeedScaleDefaults.MaxWidth)
            .fillMaxWidth(HyperPlaybackSpeedScaleDefaults.WidthFraction)
            .shadow(HyperPlaybackSpeedScaleDefaults.Elevation, shape, clip = false)
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    listOf(colors.glassHighlightColor, colors.glassContainerColor)
                ),
                shape = shape
            )
            .border(HyperPlaybackSpeedScaleDefaults.BorderWidth, colors.borderColor, shape)
            .padding(HyperPlaybackSpeedScaleDefaults.ContentPadding)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = selectedIndex.toFloat(),
                    range = 0f..resolvedOptions.lastIndex.toFloat(),
                    steps = (resolvedOptions.size - 2).coerceAtLeast(0)
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HyperPlaybackSpeedScaleDefaults.ContentSpacing)
    ) {
        SpeedLabels(
            speedOptions = resolvedOptions,
            selectedIndex = selectedIndex,
            colors = colors
        )
        SpeedTrack(
            itemCount = resolvedOptions.size,
            selectedIndex = selectedIndex,
            colors = colors
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(HyperPlaybackSpeedScaleDefaults.LeadingIconSize),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(LocalContentColor provides colors.valueColor) {
                    if (leadingContent == null) {
                        DefaultSpeedGlyph()
                    } else {
                        leadingContent()
                    }
                }
            }
            Text(
                text = "${formatHyperPlaybackSpeed(selectedSpeed)}x",
                color = colors.valueColor,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun SpeedLabels(
    speedOptions: List<Float>,
    selectedIndex: Int,
    colors: HyperPlaybackSpeedScaleColors
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        speedOptions.forEachIndexed { index, speed ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${formatHyperPlaybackSpeed(speed)}x",
                    color = if (index == selectedIndex) {
                        colors.selectedLabelColor
                    } else {
                        colors.labelColor
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun SpeedTrack(
    itemCount: Int,
    selectedIndex: Int,
    colors: HyperPlaybackSpeedScaleColors
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(HyperPlaybackSpeedScaleDefaults.TrackHeight)
    ) {
        val segmentWidth = size.width / itemCount
        val trackStartX = segmentWidth / 2f
        val trackEndX = size.width - segmentWidth / 2f
        val selectedX = segmentWidth * (selectedIndex + 0.5f)
        val centerY = size.height / 2f

        drawLine(
            color = colors.trackColor,
            start = Offset(trackStartX, centerY),
            end = Offset(trackEndX, centerY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = colors.activeTrackColor,
            start = Offset(trackStartX, centerY),
            end = Offset(selectedX, centerY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
        repeat(itemCount) { index ->
            val tickX = segmentWidth * (index + 0.5f)
            drawCircle(
                color = colors.tickColor,
                radius = 2.5.dp.toPx(),
                center = Offset(tickX, centerY)
            )
        }
        drawCircle(
            color = colors.selectedTickColor.copy(alpha = 0.22f),
            radius = 9.dp.toPx(),
            center = Offset(selectedX, centerY)
        )
        drawCircle(
            color = colors.selectedTickColor,
            radius = 5.dp.toPx(),
            center = Offset(selectedX, centerY)
        )
    }
}

@Composable
private fun DefaultSpeedGlyph() {
    val color = LocalContentColor.current
    Canvas(modifier = Modifier.size(HyperPlaybackSpeedScaleDefaults.LeadingIconSize)) {
        val strokeWidth = 2.dp.toPx()
        val middleY = size.height / 2f
        val quarterWidth = size.width / 4f
        repeat(2) { index ->
            val startX = quarterWidth * index + quarterWidth * 0.35f
            drawLine(
                color = color,
                start = Offset(startX, size.height * 0.22f),
                end = Offset(startX + quarterWidth, middleY),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(startX + quarterWidth, middleY),
                end = Offset(startX, size.height * 0.78f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

object HyperPlaybackSpeedScaleDefaults {
    val SpeedOptions = listOf(0.25f, 0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 4f)
    val DragStep = 42.dp
    val MaxWidth = 480.dp
    const val WidthFraction = 0.9f
    val Shape: Shape = RoundedCornerShape(percent = 50)
    val Elevation = 16.dp
    val BorderWidth = 1.dp
    val ContentPadding = androidx.compose.foundation.layout.PaddingValues(
        horizontal = 20.dp,
        vertical = 12.dp
    )
    val ContentSpacing = 4.dp
    val TrackHeight = 20.dp
    val LeadingIconSize = 17.dp

    fun closestSpeed(
        targetSpeed: Float,
        speedOptions: List<Float> = SpeedOptions
    ): Float = speedOptions.ifEmpty { SpeedOptions }.minBy { speed ->
        abs(speed - targetSpeed)
    }

    @Composable
    fun colors(
        glassHighlightColor: Color = Color.Unspecified,
        glassContainerColor: Color = Color.Unspecified,
        borderColor: Color = Color.Unspecified,
        trackColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        tickColor: Color = Color.Unspecified,
        selectedTickColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        selectedLabelColor: Color = Color.Unspecified,
        valueColor: Color = Color.Unspecified
    ): HyperPlaybackSpeedScaleColors {
        val accent = HyperColors.accent
        return HyperPlaybackSpeedScaleColors(
            glassHighlightColor = resolveHyperContainerColor(
                glassHighlightColor,
                Color(1f, 1f, 1f, 0.2f)
            ),
            glassContainerColor = resolveHyperContainerColor(
                glassContainerColor,
                Color(0.05f, 0.06f, 0.08f, 0.76f)
            ),
            borderColor = resolveHyperContainerColor(
                borderColor,
                Color(1f, 1f, 1f, 0.24f)
            ),
            trackColor = resolveHyperContainerColor(
                trackColor,
                Color(1f, 1f, 1f, 0.34f)
            ),
            activeTrackColor = resolveHyperContainerColor(
                activeTrackColor,
                accent.copy(alpha = 0.78f)
            ),
            tickColor = resolveHyperContainerColor(
                tickColor,
                Color(1f, 1f, 1f, 0.86f)
            ),
            selectedTickColor = resolveHyperContainerColor(selectedTickColor, accent),
            labelColor = resolveHyperContainerColor(
                labelColor,
                Color(1f, 1f, 1f, 0.84f)
            ),
            selectedLabelColor = resolveHyperContainerColor(selectedLabelColor, accent),
            valueColor = resolveHyperContainerColor(
                valueColor,
                Color(1f, 1f, 1f, 0.96f)
            )
        )
    }
}

private fun formatHyperPlaybackSpeed(speed: Float): String = if (speed % 1f == 0f) {
    speed.toInt().toString()
} else {
    speed.toString()
}
