/** 文件职责：提供播放器长按临时倍速使用的柔性玻璃刻度反馈组件。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.icon.HyperFastForwardIcon
import kotlin.math.abs

@Immutable
data class HyperPlaybackSpeedScaleColors(
    val containerColor: Color,
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
    value: Float,
    modifier: Modifier = Modifier,
    options: List<Float> = HyperPlaybackSpeedScaleDefaults.SpeedOptions,
    shape: Shape = HyperPlaybackSpeedScaleDefaults.Shape,
    colors: HyperPlaybackSpeedScaleColors = HyperPlaybackSpeedScaleDefaults.colors(),
    leading: (@Composable () -> Unit)? = null
) {
    val resolvedOptions = options.ifEmpty { HyperPlaybackSpeedScaleDefaults.SpeedOptions }
    val selectedIndex = resolvedOptions.indices.minByOrNull { index ->
        abs(resolvedOptions[index] - value)
    } ?: 0

    Box(
        modifier = modifier
            .widthIn(max = HyperPlaybackSpeedScaleDefaults.MaxWidth)
            .fillMaxWidth(HyperPlaybackSpeedScaleDefaults.WidthFraction)
            .hyperGlassSurface(
                shape = shape,
                visuals = hyperGlassSurfaceVisuals(
                    containerColor = colors.containerColor,
                    elevation = HyperPlaybackSpeedScaleDefaults.Elevation,

                )
            )
            .padding(HyperPlaybackSpeedScaleDefaults.ContentPadding)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = selectedIndex.toFloat(),
                    range = 0f..resolvedOptions.lastIndex.toFloat(),
                    steps = (resolvedOptions.size - 2).coerceAtLeast(0)
                )
            }
    ) {
        CompactSpeedScaleContent(
            options = resolvedOptions,
            selectedIndex = selectedIndex,
            value = value,
            colors = colors,
            leading = leading
        )
    }
}

@Composable
private fun CompactSpeedScaleContent(
    options: List<Float>,
    selectedIndex: Int,
    value: Float,
    colors: HyperPlaybackSpeedScaleColors,
    leading: (@Composable () -> Unit)?
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(CompactScaleHeight)
    ) {
        val endpointAreaWidth = minOf(EndpointAreaWidth, maxWidth * 0.25f)
        val trackWidth = (maxWidth - endpointAreaWidth * 2f).coerceAtLeast(0.dp)
        val thumbSize = HyperPlaybackSpeedScaleDefaults.TrackThumbSize
        val markerTravelWidth = (trackWidth - thumbSize).coerceAtLeast(0.dp)

        options.forEachIndexed { index, speed ->
            val isOnlyOption = options.size == 1
            val fraction = if (options.size <= 1) {
                0f
            } else {
                index / options.lastIndex.toFloat()
            }
            val isEndpoint = !isOnlyOption &&
                (index == 0 || index == options.lastIndex)
            val markerCenter = endpointAreaWidth + thumbSize * 0.5f +
                markerTravelWidth * fraction
            val labelOffset = if (isOnlyOption) {
                (maxWidth - HyperPlaybackSpeedScaleDefaults.LabelWidth) * 0.5f
            } else if (index == 0) {
                0.dp
            } else if (index == options.lastIndex) {
                maxWidth - endpointAreaWidth
            } else {
                markerCenter - HyperPlaybackSpeedScaleDefaults.LabelWidth * 0.5f
            }

            SpeedLabel(
                speed = speed,
                selected = index == selectedIndex,
                colors = colors,
                modifier = Modifier
                    .offset(
                        x = labelOffset,
                        y = if (isEndpoint) EndpointLabelTopOffset else 0.dp
                    )
                    .width(
                        if (isEndpoint) {
                            endpointAreaWidth
                        } else {
                            HyperPlaybackSpeedScaleDefaults.LabelWidth
                        }
                    )
            )
        }

        SpeedTrack(
            itemCount = options.size,
            selectedIndex = selectedIndex,
            colors = colors,
            modifier = Modifier
                .offset(x = endpointAreaWidth, y = TrackTopOffset)
                .width(trackWidth)
        )

        CurrentSpeedValue(
            value = value,
            colors = colors,
            leading = leading,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(CurrentValueHeight)
        )
    }
}

@Composable
private fun SpeedLabel(
    speed: Float,
    selected: Boolean,
    colors: HyperPlaybackSpeedScaleColors,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        HyperText(
            text = "${formatHyperPlaybackSpeed(speed)}x",
            color = if (selected) colors.selectedLabelColor else colors.labelColor,
            fontSize = 9.sp,
            lineHeight = 10.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
private fun SpeedTrack(
    itemCount: Int,
    selectedIndex: Int,
    colors: HyperPlaybackSpeedScaleColors,
    modifier: Modifier = Modifier
) {
    val sliderMaximum = (itemCount - 1).coerceAtLeast(1).toFloat()
    HyperSlider(
        showValue = false,
        value = selectedIndex.toFloat(),
        onChanged = {},
        modifier = modifier,
        readOnly = true,
        min = 0f, max = sliderMaximum,
        divisions = (itemCount - 1).coerceAtLeast(1),
        showSegmentMarkers = true,
        segmentValues = List(itemCount) { index -> index.toFloat() },
        minimumTouchHeight = HyperPlaybackSpeedScaleDefaults.TrackHeight,
        trackHeight = HyperPlaybackSpeedScaleDefaults.TrackStrokeWidth,
        thumbSize = HyperPlaybackSpeedScaleDefaults.TrackThumbSize,
        segmentMarkerSize = HyperPlaybackSpeedScaleDefaults.TrackMarkerSize,
        colors = HyperSliderDefaults.colors(
            trackColor = colors.trackColor,
            activeTrackColor = colors.activeTrackColor,
            segmentMarkerColor = colors.tickColor,
            thumbColor = colors.valueColor
        )
    )
}

@Composable
private fun CurrentSpeedValue(
    value: Float,
    colors: HyperPlaybackSpeedScaleColors,
    leading: (@Composable () -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(HyperPlaybackSpeedScaleDefaults.LeadingIconSize),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalHyperContentColor provides colors.valueColor) {
                if (leading == null) {
                    HyperFastForwardIcon(
                        Modifier.size(HyperPlaybackSpeedScaleDefaults.LeadingIconSize)
                    )
                } else {
                    leading()
                }
            }
        }
        HyperText(
            text = "${formatHyperPlaybackSpeed(value)}x",
            color = colors.valueColor,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            lineHeight = 12.sp,
            maxLines = 1
        )
    }
}

object HyperPlaybackSpeedScaleDefaults {
    val SpeedOptions = listOf(0.25f, 0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 4f)
    val DragStep = 42.dp
    val MaxWidth = 360.dp
    const val WidthFraction = 0.86f
    val Shape: Shape = RoundedCornerShape(percent = 50)
    val Elevation = 6.dp
    val ContentPadding = androidx.compose.foundation.layout.PaddingValues(
        horizontal = 12.dp,
        vertical = 5.dp
    )
    val TrackHeight = 22.dp
    val TrackStrokeWidth = 3.dp
    val TrackThumbSize = 12.dp
    val TrackMarkerSize = 4.dp
    val LabelWidth = 28.dp
    val LeadingIconSize = 13.dp

    fun closestSpeed(
        targetSpeed: Float,
        options: List<Float> = SpeedOptions
    ): Float = options.ifEmpty { SpeedOptions }.minBy { speed ->
        abs(speed - targetSpeed)
    }

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        trackColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        tickColor: Color = Color.Unspecified,
        selectedTickColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        selectedLabelColor: Color = Color.Unspecified,
        valueColor: Color = Color.Unspecified
    ): HyperPlaybackSpeedScaleColors {
        val accent = HyperColors.accent
        val primaryContent = HyperColors.primaryText
        return HyperPlaybackSpeedScaleColors(
            containerColor = resolveHyperContainerColor(
                containerColor,
                hyperGlass.surfaceStrong
            ),
            trackColor = resolveHyperContainerColor(
                trackColor,
                primaryContent.copy(alpha = 0.24f)
            ),
            activeTrackColor = resolveHyperContainerColor(
                activeTrackColor,
                accent.copy(alpha = 0.78f)
            ),
            tickColor = resolveHyperContainerColor(
                tickColor,
                primaryContent.copy(alpha = 0.74f)
            ),
            selectedTickColor = resolveHyperContainerColor(selectedTickColor, accent),
            labelColor = resolveHyperContainerColor(
                labelColor,
                primaryContent.copy(alpha = 0.72f)
            ),
            selectedLabelColor = resolveHyperContainerColor(selectedLabelColor, accent),
            valueColor = resolveHyperContainerColor(
                valueColor,
                primaryContent
            )
        )
    }
}

private fun formatHyperPlaybackSpeed(speed: Float): String = if (speed % 1f == 0f) {
    speed.toInt().toString()
} else {
    speed.toString()
}

private val CompactScaleHeight = 49.dp
private val EndpointAreaWidth = 30.dp
private val EndpointLabelTopOffset = 18.dp
private val TrackTopOffset = 12.dp
private val CurrentValueHeight: Dp = 14.dp
