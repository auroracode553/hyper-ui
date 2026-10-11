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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.semantics.disabled
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
    val disabledTrackColor: Color,
    val disabledActiveTrackColor: Color,
    val disabledSegmentMarkerColor: Color,
    val disabledThumbColor: Color
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
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    showValue: Boolean = true,
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
    thumbBorder: BorderStroke? = HyperSliderDefaults.thumbBorder()
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
    val interactionModifier = Modifier
        .pointerInput(
            interactionEnabled,
            valueRange.start,
            valueRange.endInclusive,
            steps
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
            steps
        ) {
            if (!interactionEnabled) return@pointerInput

            detectTapGestures { offset ->
                if (!isDragging) {
                    currentOnValueChangeStarted?.invoke()
                    currentOnValueChange(
                        pointerPositionToValue(
                            pointerX = offset.x,
                            widthPx = size.width.toFloat(),
                            valueRange = valueRange,
                            steps = steps
                        )
                    )
                    currentOnValueChangeFinished?.invoke()
                }
            }
        }

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        // 参考轨道使用物理左坐标；外层数值仍跟随调用方的布局方向。
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            BoxWithConstraints(
                modifier = Modifier.weight(1f)
                    .height(maxOf(minimumTouchHeight, thumbSize))
                    .then(interactionModifier)
                    .semantics {
                        if (!enabled) disabled()
                        progressBarRangeInfo = ProgressBarRangeInfo(coercedValue, valueRange, steps)
                        if (interactionEnabled) setProgress { targetValue ->
                            currentOnValueChangeStarted?.invoke()
                            currentOnValueChange(snapValueToSteps(targetValue, valueRange, steps))
                            currentOnValueChangeFinished?.invoke()
                            true
                        }
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                val availableTrackWidth = maxWidth
                Box(Modifier.align(Alignment.Center).width(availableTrackWidth).height(trackHeight)
                    .hyperSurface(resolvedTrackColor, trackShape, border = trackBorder)) {
                    Box(Modifier.fillMaxHeight().fillMaxWidth(valueFraction)
                        .hyperSurface(resolvedActiveTrackColor, trackShape))
                }
                markerValues.forEach { markerValue ->
                    val markerFraction = valueToFraction(markerValue, valueRange)
                    Box(Modifier.align(Alignment.CenterStart)
                        .offset(x = (availableTrackWidth * markerFraction - segmentMarkerSize / 2)
                            .coerceIn(0.dp, (availableTrackWidth - segmentMarkerSize).coerceAtLeast(0.dp)))
                        .size(segmentMarkerSize).background(resolvedSegmentMarkerColor, CircleShape))
                }
                Box(Modifier.offset(x = (availableTrackWidth * valueFraction - thumbSize / 2)
                    .coerceIn(0.dp, (availableTrackWidth - thumbSize).coerceAtLeast(0.dp)))
                    .size(thumbSize)
                    .dropShadow(thumbShape, Shadow(radius = 5.dp, color = hyperGlass.shadow))
                    .hyperSurface(resolvedThumbColor, thumbShape, border = thumbBorder))
            }
        }
        if (showValue) HyperText(coercedValue.roundToInt().toString(),
            Modifier.absolutePadding(left = 12.dp), fontSize = 14.sp)
    }
}

object HyperSliderDefaults {
    val MinTouchHeight = 44.dp
    val TrackHeight = 6.dp
    val ThumbSize = 20.dp
    val SegmentMarkerSize = 5.dp
    val TrackShape: Shape = RoundedCornerShape(percent = 50)
    val ThumbShape: Shape = CircleShape

    @Composable
    fun colors(
        trackColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        segmentMarkerColor: Color = Color.Unspecified,
        thumbColor: Color = Color.Unspecified,
        disabledTrackColor: Color = Color.Unspecified,
        disabledActiveTrackColor: Color = Color.Unspecified,
        disabledSegmentMarkerColor: Color = Color.Unspecified,
        disabledThumbColor: Color = Color.Unspecified
    ): HyperSliderColors {
        val accent = HyperColors.accent
        val resolvedActiveTrackColor = resolveHyperContainerColor(activeTrackColor, accent)
        val defaultMarkerColor = HyperColors.primaryText.copy(alpha = 0.56f)
        return HyperSliderColors(
            trackColor = resolveHyperContainerColor(trackColor, hyperGlass.controlTrack),
            activeTrackColor = resolvedActiveTrackColor,
            segmentMarkerColor = resolveHyperContainerColor(
                segmentMarkerColor,
                defaultMarkerColor
            ),
            thumbColor = resolveHyperContainerColor(
                thumbColor,
                HyperColors.cardContainer
            ),
            disabledTrackColor = resolveHyperContainerColor(
                disabledTrackColor,
                HyperColors.disabledContainer
            ),
            disabledActiveTrackColor = resolveHyperContainerColor(
                disabledActiveTrackColor,
                resolvedActiveTrackColor
            ),
            disabledSegmentMarkerColor = resolveHyperContainerColor(
                disabledSegmentMarkerColor,
                HyperColors.disabledText.copy(alpha = 0.72f)
            ),
            disabledThumbColor = resolveHyperContainerColor(
                disabledThumbColor,
                HyperColors.softContainer
            )
        )
    }

    @Composable
    fun trackBorder(color: Color = Color.Unspecified): BorderStroke = hyperPanelBorder(color)

    @Composable
    fun thumbBorder(color: Color = Color.Unspecified): BorderStroke =
        BorderStroke(2.dp, resolveHyperContainerColor(color, HyperColors.accent))
}

private fun pointerPositionToValue(
    pointerX: Float,
    widthPx: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
): Float {
    val availableTrackWidth = widthPx.coerceAtLeast(1f)
    val fraction = (pointerX / availableTrackWidth)
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
