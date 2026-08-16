/** 文件职责：提供支持连续、等距吸附与可配置分段标记的受控滑块。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Immutable
data class HyperSliderColors(
    val trackColor: Color,
    val activeTrackColor: Color,
    val segmentMarkerColor: Color,
    val thumbColor: Color,
    val thumbCenterColor: Color,
    val thumbHaloColor: Color,
    val disabledTrackColor: Color,
    val disabledActiveTrackColor: Color,
    val disabledSegmentMarkerColor: Color,
    val disabledThumbColor: Color,
    val disabledThumbCenterColor: Color,
    val disabledThumbHaloColor: Color
)

/**
 * 可点击、可拖动的 HyperUI 受控滑块。
 *
 * `steps` 只负责等距吸附；`showSegmentMarkers` 与 `segmentValues` 只负责分段点视觉，
 * 两者可以独立使用。`readOnly` 保留正常配色和进度语义，但不响应用户输入。
 */
@Composable
fun HyperSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    showSegmentMarkers: Boolean = false,
    segmentValues: List<Float> = emptyList(),
    onValueChangeStarted: (() -> Unit)? = null,
    onValueChangeFinished: (() -> Unit)? = null,
    minimumTouchHeight: Dp = HyperSliderDefaults.MinTouchHeight,
    trackHeight: Dp = HyperSliderDefaults.TrackHeight,
    thumbSize: Dp = HyperSliderDefaults.ThumbSize,
    segmentMarkerSize: Dp = HyperSliderDefaults.SegmentMarkerSize,
    trackShape: Shape = HyperSliderDefaults.TrackShape,
    thumbShape: Shape = HyperSliderDefaults.ThumbShape,
    colors: HyperSliderColors = HyperSliderDefaults.colors(),
    trackBorder: BorderStroke? = null,
    thumbBorder: BorderStroke? = null
) {
    require(valueRange.start.isFinite() && valueRange.endInclusive.isFinite()) {
        "valueRange 的起止值必须是有限数值"
    }
    require(valueRange.endInclusive > valueRange.start) {
        "valueRange 的结束值必须大于起始值"
    }
    require(steps >= 0) { "steps 不能小于 0" }
    require(minimumTouchHeight >= 0.dp) { "minimumTouchHeight 不能小于 0.dp" }
    require(trackHeight >= 0.dp) { "trackHeight 不能小于 0.dp" }
    require(thumbSize > 0.dp) { "thumbSize 必须大于 0.dp" }
    require(segmentMarkerSize >= 0.dp) { "segmentMarkerSize 不能小于 0.dp" }

    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val currentOnValueChangeStarted by rememberUpdatedState(onValueChangeStarted)
    val currentOnValueChangeFinished by rememberUpdatedState(onValueChangeFinished)
    val density = LocalDensity.current
    val thumbHaloSize = maxOf(
        thumbSize,
        thumbSize * HyperSliderDefaults.ThumbHaloScale
    )
    val thumbCenterSize = (thumbSize * HyperSliderDefaults.ThumbCenterScale)
        .coerceAtMost(thumbSize)
    val thumbHaloSizePx = with(density) { thumbHaloSize.toPx() }
    var isDragging by remember { mutableStateOf(false) }

    val coercedValue = if (value.isFinite()) {
        value.coerceIn(valueRange.start, valueRange.endInclusive)
    } else {
        valueRange.start
    }
    val valueFraction = valueToFraction(coercedValue, valueRange)
    val markerValues = remember(showSegmentMarkers, segmentValues, valueRange, steps) {
        resolveSegmentMarkerValues(
            showSegmentMarkers = showSegmentMarkers,
            segmentValues = segmentValues,
            valueRange = valueRange,
            steps = steps
        )
    }
    val interactionEnabled = enabled && !readOnly
    val resolvedTrackColor = if (enabled) colors.trackColor else colors.disabledTrackColor
    val resolvedActiveTrackColor = if (enabled) {
        colors.activeTrackColor
    } else {
        colors.disabledActiveTrackColor
    }
    val resolvedSegmentMarkerColor = if (enabled) {
        colors.segmentMarkerColor
    } else {
        colors.disabledSegmentMarkerColor
    }
    val resolvedThumbColor = if (enabled) colors.thumbColor else colors.disabledThumbColor
    val resolvedThumbCenterColor = if (enabled) {
        colors.thumbCenterColor
    } else {
        colors.disabledThumbCenterColor
    }
    val resolvedThumbHaloColor = if (enabled) {
        colors.thumbHaloColor
    } else {
        colors.disabledThumbHaloColor
    }
    val interactionModifier = Modifier
        .pointerInput(
            interactionEnabled,
            valueRange.start,
            valueRange.endInclusive,
            steps,
            thumbHaloSizePx
        ) {
            if (!interactionEnabled) return@pointerInput

            detectDragGestures(
                onDragStart = { offset ->
                    isDragging = true
                    currentOnValueChangeStarted?.invoke()
                    currentOnValueChange(
                        pointerPositionToValue(
                            pointerX = offset.x,
                            widthPx = size.width.toFloat(),
                            thumbHaloSizePx = thumbHaloSizePx,
                            valueRange = valueRange,
                            steps = steps
                        )
                    )
                },
                onDragEnd = {
                    isDragging = false
                    currentOnValueChangeFinished?.invoke()
                },
                onDragCancel = {
                    isDragging = false
                    currentOnValueChangeFinished?.invoke()
                },
                onDrag = { change, _ ->
                    currentOnValueChange(
                        pointerPositionToValue(
                            pointerX = change.position.x,
                            widthPx = size.width.toFloat(),
                            thumbHaloSizePx = thumbHaloSizePx,
                            valueRange = valueRange,
                            steps = steps
                        )
                    )
                    change.consume()
                }
            )
        }
        .pointerInput(
            interactionEnabled,
            valueRange.start,
            valueRange.endInclusive,
            steps,
            thumbHaloSizePx
        ) {
            if (!interactionEnabled) return@pointerInput

            detectTapGestures { offset ->
                if (!isDragging) {
                    currentOnValueChangeStarted?.invoke()
                    currentOnValueChange(
                        pointerPositionToValue(
                            pointerX = offset.x,
                            widthPx = size.width.toFloat(),
                            thumbHaloSizePx = thumbHaloSizePx,
                            valueRange = valueRange,
                            steps = steps
                        )
                    )
                    currentOnValueChangeFinished?.invoke()
                }
            }
        }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(maxOf(minimumTouchHeight, thumbHaloSize))
            .then(interactionModifier)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = coercedValue,
                    range = valueRange,
                    steps = steps
                )
                if (interactionEnabled) {
                    setProgress { targetValue ->
                        currentOnValueChangeStarted?.invoke()
                        currentOnValueChange(snapValueToSteps(targetValue, valueRange, steps))
                        currentOnValueChangeFinished?.invoke()
                        true
                    }
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        val availableTrackWidth = (maxWidth - thumbHaloSize).coerceAtLeast(0.dp)

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(availableTrackWidth)
                .height(trackHeight)
                .hyperSurface(
                    containerColor = resolvedTrackColor,
                    shape = trackShape,
                    border = trackBorder
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(valueFraction)
                    .hyperSurface(
                        containerColor = resolvedActiveTrackColor,
                        shape = trackShape
                    )
            )
        }

        markerValues.forEach { markerValue ->
            val markerFraction = valueToFraction(markerValue, valueRange)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(
                        x = availableTrackWidth * markerFraction +
                            (thumbHaloSize - segmentMarkerSize) * 0.5f
                    )
                    .size(segmentMarkerSize)
                    .background(resolvedSegmentMarkerColor, CircleShape)
            )
        }

        Box(
            modifier = Modifier
                .offset(x = availableTrackWidth * valueFraction)
                .size(thumbHaloSize)
                .background(resolvedThumbHaloColor, thumbShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(thumbSize)
                    .hyperSurface(
                        containerColor = resolvedThumbColor,
                        shape = thumbShape,
                        border = thumbBorder
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(thumbCenterSize)
                        .background(resolvedThumbCenterColor, thumbShape)
                )
            }
        }
    }
}

object HyperSliderDefaults {
    val MinTouchHeight = 40.dp
    val TrackHeight = 6.dp
    val ThumbSize = 18.dp
    val SegmentMarkerSize = 5.dp
    const val ThumbHaloScale = 1.85f
    const val ThumbCenterScale = 0.52f
    val TrackShape: Shape = RoundedCornerShape(percent = 50)
    val ThumbShape: Shape = CircleShape

    @Composable
    fun colors(
        trackColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        segmentMarkerColor: Color = Color.Unspecified,
        thumbColor: Color = Color.Unspecified,
        thumbCenterColor: Color = Color.Unspecified,
        thumbHaloColor: Color = Color.Unspecified,
        disabledTrackColor: Color = Color.Unspecified,
        disabledActiveTrackColor: Color = Color.Unspecified,
        disabledSegmentMarkerColor: Color = Color.Unspecified,
        disabledThumbColor: Color = Color.Unspecified,
        disabledThumbCenterColor: Color = Color.Unspecified,
        disabledThumbHaloColor: Color = Color.Unspecified
    ): HyperSliderColors {
        val accent = HyperColors.accent
        val resolvedActiveTrackColor = resolveHyperContainerColor(activeTrackColor, accent)
        val defaultMarkerColor = HyperColors.primaryText.copy(alpha = 0.56f)
        return HyperSliderColors(
            trackColor = resolveHyperContainerColor(trackColor, HyperColors.fieldContainer),
            activeTrackColor = resolvedActiveTrackColor,
            segmentMarkerColor = resolveHyperContainerColor(
                segmentMarkerColor,
                defaultMarkerColor
            ),
            thumbColor = resolveHyperContainerColor(
                thumbColor,
                Color(1f, 1f, 1f, 1f)
            ),
            thumbCenterColor = resolveHyperContainerColor(
                thumbCenterColor,
                resolvedActiveTrackColor
            ),
            thumbHaloColor = resolveHyperContainerColor(
                thumbHaloColor,
                resolvedActiveTrackColor.copy(alpha = 0.22f)
            ),
            disabledTrackColor = resolveHyperContainerColor(
                disabledTrackColor,
                HyperColors.disabledContainer
            ),
            disabledActiveTrackColor = resolveHyperContainerColor(
                disabledActiveTrackColor,
                HyperColors.disabledText
            ),
            disabledSegmentMarkerColor = resolveHyperContainerColor(
                disabledSegmentMarkerColor,
                HyperColors.disabledText.copy(alpha = 0.72f)
            ),
            disabledThumbColor = resolveHyperContainerColor(
                disabledThumbColor,
                HyperColors.disabledContainer
            ),
            disabledThumbCenterColor = resolveHyperContainerColor(
                disabledThumbCenterColor,
                HyperColors.disabledText
            ),
            disabledThumbHaloColor = resolveHyperContainerColor(
                disabledThumbHaloColor,
                HyperColors.disabledText.copy(alpha = 0.16f)
            )
        )
    }

    @Composable
    fun trackBorder(color: Color = Color.Unspecified): BorderStroke = hyperPanelBorder(color)

    @Composable
    fun thumbBorder(color: Color = Color.Unspecified): BorderStroke = hyperPanelBorder(color)
}

private fun pointerPositionToValue(
    pointerX: Float,
    widthPx: Float,
    thumbHaloSizePx: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
): Float {
    val availableTrackWidth = (widthPx - thumbHaloSizePx).coerceAtLeast(1f)
    val fraction = ((pointerX - thumbHaloSizePx / 2f) / availableTrackWidth)
        .coerceIn(0f, 1f)
    val rawValue = valueRange.start + fraction * (valueRange.endInclusive - valueRange.start)
    return snapValueToSteps(rawValue, valueRange, steps)
}

private fun valueToFraction(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>
): Float = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start))
    .coerceIn(0f, 1f)

private fun snapValueToSteps(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
): Float {
    val coercedValue = value.coerceIn(valueRange.start, valueRange.endInclusive)
    if (steps == 0) return coercedValue

    val intervals = steps + 1
    val fraction = valueToFraction(coercedValue, valueRange)
    val snappedFraction = (fraction * intervals).roundToInt() / intervals.toFloat()
    return valueRange.start + snappedFraction * (valueRange.endInclusive - valueRange.start)
}

private fun resolveSegmentMarkerValues(
    showSegmentMarkers: Boolean,
    segmentValues: List<Float>,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
): List<Float> {
    if (!showSegmentMarkers) return emptyList()

    val values = if (segmentValues.isNotEmpty()) {
        segmentValues
    } else if (steps > 0) {
        val intervals = steps + 1
        List(intervals + 1) { index ->
            valueRange.start +
                (valueRange.endInclusive - valueRange.start) * index / intervals.toFloat()
        }
    } else {
        emptyList()
    }

    return values
        .asSequence()
        .filter { value -> value.isFinite() }
        .filter { value -> value in valueRange }
        .distinct()
        .sorted()
        .toList()
}
