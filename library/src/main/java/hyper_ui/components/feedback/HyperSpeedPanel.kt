/** 文件职责：提供主题玻璃、受控且可直接覆盖在播放器上的播放速度设置面板。 */
package hyper_ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Gauge
import com.composables.icons.lucide.MoveHorizontal
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.RotateCcw
import com.composables.icons.lucide.X

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable
import kotlin.math.abs

/**
 * 播放速度覆盖层。业务速度与显示状态均由调用方持有，组件只处理主题玻璃布局与点击分发。
 */
@Composable
fun HyperSpeedPanelOverlay(
    visible: Boolean,
    value: Float,
    onChanged: (Float) -> Unit,
    onClose: () -> Unit,
    onCustomPressed: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    panelModifier: Modifier = Modifier,
    texts: HyperSpeedPanelTexts = HyperSpeedPanelTexts(),
    colors: HyperSpeedPanelColors = HyperSpeedPanelDefaults.colors(),
    options: List<Float> = HyperSpeedPanelDefaults.MajorSpeeds,
    valueRange: ClosedFloatingPointRange<Float> = HyperSpeedPanelDefaults.SliderRange,
    steps: Int = HyperSpeedPanelDefaults.SliderSteps,
    defaultSpeed: Float = HyperSpeedPanelDefaults.DefaultSpeed,
    shape: Shape = HyperSpeedPanelDefaults.Shape,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    close: (@Composable BoxScope.() -> Unit)? = null,
    hintLeading: (@Composable BoxScope.() -> Unit)? = null,
    reset: (@Composable BoxScope.() -> Unit)? = null,
    customLeading: (@Composable BoxScope.() -> Unit)? = null
) {
    if (!visible) return

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.scrimColor)
                .hyperNoRippleClickable(onClick = onClose)
        )
        HyperSpeedPanel(
            value = value,
            onChanged = onChanged,
            onClose = onClose,
            onCustomPressed = onCustomPressed,
            modifier = panelModifier
                .align(Alignment.Center)
                .padding(horizontal = HyperSpeedPanelDefaults.OverlayHorizontalPadding),
            texts = texts,
            colors = colors,
            options = options,
            valueRange = valueRange,
            steps = steps,
            defaultSpeed = defaultSpeed,
            shape = shape,
            leading = leading,
            close = close,
            hintLeading = hintLeading,
            reset = reset,
            customLeading = customLeading
        )
    }
}

/** 主题玻璃的受控播放速度面板，可脱离覆盖层单独组合。 */
@Composable
fun HyperSpeedPanel(
    value: Float,
    onChanged: (Float) -> Unit,
    onClose: () -> Unit,
    onCustomPressed: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    texts: HyperSpeedPanelTexts = HyperSpeedPanelTexts(),
    colors: HyperSpeedPanelColors = HyperSpeedPanelDefaults.colors(),
    options: List<Float> = HyperSpeedPanelDefaults.MajorSpeeds,
    valueRange: ClosedFloatingPointRange<Float> = HyperSpeedPanelDefaults.SliderRange,
    steps: Int = HyperSpeedPanelDefaults.SliderSteps,
    defaultSpeed: Float = HyperSpeedPanelDefaults.DefaultSpeed,
    shape: Shape = HyperSpeedPanelDefaults.Shape,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    close: (@Composable BoxScope.() -> Unit)? = null,
    hintLeading: (@Composable BoxScope.() -> Unit)? = null,
    reset: (@Composable BoxScope.() -> Unit)? = null,
    customLeading: (@Composable BoxScope.() -> Unit)? = null
) {
    require(valueRange.start.isFinite() && valueRange.endInclusive.isFinite()) {
        "valueRange 的起止值必须是有限数值"
    }
    require(valueRange.endInclusive > valueRange.start) {
        "valueRange 的结束值必须大于起始值"
    }
    require(steps >= 0) { "steps 不能小于 0" }

    val resolvedOptions = remember(options, valueRange) {
        options
            .asSequence()
            .filter { speed -> speed.isFinite() }
            .filter { speed -> speed in valueRange }
            .distinct()
            .sorted()
            .toList()
            .ifEmpty { listOf(valueRange.start, valueRange.endInclusive) }
    }

    HyperSpeedPanelTheme(colors) {
        Column(
            modifier = modifier
                .widthIn(max = HyperSpeedPanelDefaults.MaxWidth)
                .fillMaxWidth(HyperSpeedPanelDefaults.WidthFraction)
                .hyperFrostedSurface(colors.containerColor, shape,
                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.panelBorderColor))
                .padding(HyperSpeedPanelDefaults.ContentPadding),
            verticalArrangement = Arrangement.spacedBy(
                HyperSpeedPanelDefaults.ContentSpacing
            )
        ) {
            HyperSpeedPanelHeader(
                value = value,
                texts = texts,
                colors = colors,
                onClose = onClose,
                leading = leading,
                close = close
            )
            HyperPlaybackSpeedSlider(
                value = value,
                onChanged = onChanged,
                valueRange = valueRange,
                steps = steps,
                options = resolvedOptions,
                texts = texts,
                colors = colors
            )
            HyperPlaybackSpeedLabels(
                value = value,
                valueRange = valueRange,
                steps = steps,
                options = resolvedOptions,
                onChanged = onChanged,
                colors = colors
            )
            HyperSpeedPanelFooter(
                value = value,
                defaultSpeed = defaultSpeed,
                texts = texts,
                colors = colors,
                onResetRequest = { onChanged(defaultSpeed) },
                onCustomPressed = onCustomPressed,
                hintLeading = hintLeading,
                reset = reset,
                customLeading = customLeading
            )
        }
    }
}

@Composable
private fun HyperSpeedPanelHeader(
    value: Float,
    texts: HyperSpeedPanelTexts,
    colors: HyperSpeedPanelColors,
    onClose: () -> Unit,
    leading: (@Composable BoxScope.() -> Unit)?,
    close: (@Composable BoxScope.() -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(colors.accentColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalHyperContentColor provides colors.accentColor) {
                if (leading == null) {
                    HyperIcon(Lucide.Gauge, null, Modifier.size(16.dp))
                } else {
                    leading()
                }
            }
        }
        HyperText(
            text = texts.title,
            modifier = Modifier.weight(1f),
            color = colors.contentColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(percent = 50))
                .background(colors.accentColor.copy(alpha = 0.2f))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            HyperText(
                text = "${HyperSpeedPanelDefaults.formatFixed(value)}x",
                color = colors.contentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false
            )
        }
        HyperButton(
            onPressed = onClose,
            type = "icon",
            modifier = Modifier.semantics { contentDescription = texts.closeContentDescription },
            colors = panelButtonColors(colors)
        ) {
            Box(
                modifier = Modifier.size(18.dp),
                contentAlignment = Alignment.Center
            ) {
                if (close == null) {
                    HyperIcon(Lucide.X, null, Modifier.size(15.dp))
                } else {
                    close()
                }
            }
        }
    }
}

@Composable
private fun HyperPlaybackSpeedSlider(
    value: Float,
    onChanged: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    options: List<Float>,
    texts: HyperSpeedPanelTexts,
    colors: HyperSpeedPanelColors
) {
    HyperSlider(
        showValue = false,
        value = value,
        onChanged = onChanged,
        modifier = Modifier.semantics {
            contentDescription = texts.sliderContentDescription
        },
        min = valueRange.start, max = valueRange.endInclusive,
        divisions = if (steps > 0) steps + 1 else null,
        showSegmentMarkers = true,
        segmentValues = options,
        minimumTouchHeight = HyperSpeedPanelDefaults.SliderTouchHeight,
        trackHeight = HyperSpeedPanelDefaults.SliderTrackHeight,
        thumbSize = HyperSpeedPanelDefaults.SliderThumbSize,
        segmentMarkerSize = HyperSpeedPanelDefaults.SliderMarkerSize,
        colors = HyperSliderDefaults.colors(
            trackColor = colors.trackColor,
            activeTrackColor = colors.accentColor,
            segmentMarkerColor = colors.segmentMarkerColor,
            thumbColor = colors.contentColor
        )
    )
}

@Composable
private fun HyperPlaybackSpeedLabels(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    options: List<Float>,
    onChanged: (Float) -> Unit,
    colors: HyperSpeedPanelColors
) {
    val selectionTolerance = (valueRange.endInclusive - valueRange.start) /
        (steps + 1).coerceAtLeast(1) / 2f
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(HyperSpeedPanelDefaults.SpeedLabelHeight)
    ) {
        options.forEach { speed ->
            val fraction = (
                (speed - valueRange.start) /
                    (valueRange.endInclusive - valueRange.start)
                ).coerceIn(0f, 1f)
            val selected = abs(value - speed) < selectionTolerance
            Box(
                modifier = Modifier
                    .offset(
                        x = (maxWidth - HyperSpeedPanelDefaults.SpeedLabelWidth) * fraction
                    )
                    .width(HyperSpeedPanelDefaults.SpeedLabelWidth)
                    .height(HyperSpeedPanelDefaults.SpeedLabelHeight)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(
                        if (selected) {
                            colors.accentColor.copy(alpha = 0.2f)
                        } else {
                            Color.Transparent
                        }
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        role = Role.Button,
                        onClick = { onChanged(speed) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                HyperText(
                    text = "${HyperSpeedPanelDefaults.formatCompact(speed)}x",
                    color = if (selected) {
                        colors.accentColor
                    } else {
                        colors.contentColor.copy(alpha = 0.68f)
                    },
                    fontSize = 10.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
private fun HyperSpeedPanelFooter(
    value: Float,
    defaultSpeed: Float,
    texts: HyperSpeedPanelTexts,
    colors: HyperSpeedPanelColors,
    onResetRequest: () -> Unit,
    onCustomPressed: (() -> Unit)?,
    hintLeading: (@Composable BoxScope.() -> Unit)?,
    reset: (@Composable BoxScope.() -> Unit)?,
    customLeading: (@Composable BoxScope.() -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(14.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalHyperContentColor provides colors.supportingContentColor
            ) {
                if (hintLeading == null) {
                    HyperIcon(Lucide.MoveHorizontal, null)
                } else {
                    hintLeading()
                }
            }
        }
        HyperText(
            text = texts.realtimeHint,
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp),
            color = colors.supportingContentColor,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        HyperButton(
            onPressed = onResetRequest,
            enabled = abs(value - defaultSpeed) > 0.001f,
            type = "icon",
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .semantics { contentDescription = texts.resetContentDescription },
            colors = panelButtonColors(colors)
        ) {
            Box(
                modifier = Modifier.size(18.dp),
                contentAlignment = Alignment.Center
            ) {
                if (reset == null) {
                    HyperIcon(Lucide.RotateCcw, null, Modifier.size(17.dp))
                } else {
                    reset()
                }
            }
        }
        if (onCustomPressed != null) {
            HyperPlaybackSpeedCustomAction(
                text = texts.customAction,
                colors = colors,
                onClick = onCustomPressed,
                leading = customLeading
            )
        }
    }
}

@Composable
private fun HyperPlaybackSpeedCustomAction(
    text: String,
    colors: HyperSpeedPanelColors,
    onClick: () -> Unit,
    leading: (@Composable BoxScope.() -> Unit)?
) {
    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 32.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(colors.accentColor.copy(alpha = 0.16f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalHyperContentColor provides colors.accentColor) {
                if (leading == null) {
                    HyperIcon(Lucide.Pencil, null)
                } else {
                    leading()
                }
            }
        }
        HyperText(
            text = text,
            color = colors.contentColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun panelButtonColors(
    colors: HyperSpeedPanelColors
): HyperButtonColors = HyperButtonDefaults.colors(
    containerColor = colors.contentColor.copy(alpha = 0.10f),
    contentColor = colors.contentColor,
    disabledContainerColor = colors.contentColor.copy(alpha = 0.05f),
    disabledContentColor = colors.contentColor.copy(alpha = 0.28f)
)

@Composable
private fun HyperSpeedPanelTheme(
    colors: HyperSpeedPanelColors,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalHyperContentColor provides colors.contentColor,
        LocalHyperTextStyle provides HyperTheme.typography.bodyMedium
    ) {
        content()
    }
}
