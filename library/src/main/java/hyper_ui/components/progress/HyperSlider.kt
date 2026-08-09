/** 文件职责：在 HyperUI 中提供可点击、可拖动并支持业务值范围的进度滑块。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
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
    val thumbColor: Color,
    val disabledTrackColor: Color,
    val disabledActiveTrackColor: Color,
    val disabledThumbColor: Color
)

/**
 * 支持点击定位和拖动的 HyperUI 进度滑块。
 *
 * value、拖动开始/结束后的业务处理均由调用方持有，组件只处理交互和视觉状态。
 */
@Composable
fun HyperSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeStarted: (() -> Unit)? = null,
    onValueChangeFinished: (() -> Unit)? = null,
    trackHeight: Dp = HyperSliderDefaults.TrackHeight,
    thumbSize: Dp = HyperSliderDefaults.ThumbSize,
    trackShape: Shape = HyperSliderDefaults.TrackShape,
    thumbShape: Shape = HyperSliderDefaults.ThumbShape,
    colors: HyperSliderColors = HyperSliderDefaults.colors(),
    trackBorder: BorderStroke? = HyperSliderDefaults.trackBorder(),
    thumbBorder: BorderStroke? = HyperSliderDefaults.thumbBorder()
) {
    require(valueRange.start.isFinite() && valueRange.endInclusive.isFinite()) {
        "valueRange 的起止值必须是有限数值"
    }
    require(valueRange.endInclusive > valueRange.start) {
        "valueRange 的结束值必须大于起始值"
    }
    require(steps >= 0) { "steps 不能小于 0" }

    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val currentOnValueChangeStarted by rememberUpdatedState(onValueChangeStarted)
    val currentOnValueChangeFinished by rememberUpdatedState(onValueChangeFinished)
    val density = LocalDensity.current
    val thumbSizePx = with(density) { thumbSize.toPx() }
    var isDragging by remember { mutableStateOf(false) }

    val coercedValue = if (value.isFinite()) {
        value.coerceIn(valueRange.start, valueRange.endInclusive)
    } else {
        valueRange.start
    }
    val valueFraction = valueToFraction(coercedValue, valueRange)
    val resolvedTrackColor = if (enabled) colors.trackColor else colors.disabledTrackColor
    val resolvedActiveTrackColor = if (enabled) {
        colors.activeTrackColor
    } else {
        colors.disabledActiveTrackColor
    }
    val resolvedThumbColor = if (enabled) colors.thumbColor else colors.disabledThumbColor
    val interactionModifier = Modifier
        .pointerInput(enabled, valueRange.start, valueRange.endInclusive, steps, thumbSizePx) {
            if (!enabled) return@pointerInput

            detectDragGestures(
                onDragStart = { offset ->
                    isDragging = true
                    currentOnValueChangeStarted?.invoke()
                    currentOnValueChange(
                        pointerPositionToValue(
                            pointerX = offset.x,
                            widthPx = size.width.toFloat(),
                            thumbSizePx = thumbSizePx,
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
                            thumbSizePx = thumbSizePx,
                            valueRange = valueRange,
                            steps = steps
                        )
                    )
                }
            )
        }
        .pointerInput(enabled, valueRange.start, valueRange.endInclusive, steps, thumbSizePx) {
            if (!enabled) return@pointerInput

            detectTapGestures { offset ->
                if (!isDragging) {
                    currentOnValueChangeStarted?.invoke()
                    currentOnValueChange(
                        pointerPositionToValue(
                            pointerX = offset.x,
                            widthPx = size.width.toFloat(),
                            thumbSizePx = thumbSizePx,
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
            .height(maxOf(HyperSliderDefaults.MinTouchHeight, thumbSize))
            .then(interactionModifier)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = coercedValue,
                    range = valueRange,
                    steps = steps
                )
                if (enabled) {
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
        val availableTrackWidth = (maxWidth - thumbSize).coerceAtLeast(0.dp)

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

        Box(
            modifier = Modifier
                .offset(x = availableTrackWidth * valueFraction)
                .size(thumbSize)
                .hyperSurface(
                    containerColor = resolvedThumbColor,
                    shape = thumbShape,
                    border = thumbBorder
                )
        )
    }
}

object HyperSliderDefaults {
    val MinTouchHeight = 40.dp
    val TrackHeight = 6.dp
    val ThumbSize = 22.dp
    val TrackShape: Shape = RoundedCornerShape(percent = 50)
    val ThumbShape: Shape = CircleShape

    @Composable
    fun colors(
        trackColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        thumbColor: Color = Color.Unspecified,
        disabledTrackColor: Color = Color.Unspecified,
        disabledActiveTrackColor: Color = Color.Unspecified,
        disabledThumbColor: Color = Color.Unspecified
    ): HyperSliderColors {
        val resolvedTrackColor = resolveHyperContainerColor(trackColor, HyperColors.elevatedContainer)
        val resolvedActiveTrackColor = resolveHyperContainerColor(activeTrackColor, HyperColors.accent)
        val resolvedThumbColor = resolveHyperContainerColor(thumbColor, HyperColors.cardContainer)

        return HyperSliderColors(
            trackColor = resolvedTrackColor,
            activeTrackColor = resolvedActiveTrackColor,
            thumbColor = resolvedThumbColor,
            disabledTrackColor = resolveHyperContainerColor(
                disabledTrackColor,
                HyperColors.disabledContainer
            ),
            disabledActiveTrackColor = resolveHyperContainerColor(
                disabledActiveTrackColor,
                HyperColors.disabledText
            ),
            disabledThumbColor = resolveHyperContainerColor(
                disabledThumbColor,
                HyperColors.disabledText
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
    thumbSizePx: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
): Float {
    val availableTrackWidth = (widthPx - thumbSizePx).coerceAtLeast(1f)
    val fraction = ((pointerX - thumbSizePx / 2f) / availableTrackWidth).coerceIn(0f, 1f)
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
