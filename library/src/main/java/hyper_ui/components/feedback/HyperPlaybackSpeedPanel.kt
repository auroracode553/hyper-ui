/** 文件职责：提供固定深色、受控且可直接覆盖在播放器上的播放速度设置面板。 */
package hyper_ui

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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
 * 播放速度覆盖层。业务速度与显示状态均由调用方持有，组件只处理固定深色布局与点击分发。
 */
@Composable
fun HyperPlaybackSpeedPanelOverlay(
    visible: Boolean,
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    onDismissRequest: () -> Unit,
    onCustomSpeedRequest: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    panelModifier: Modifier = Modifier,
    texts: HyperPlaybackSpeedPanelTexts = HyperPlaybackSpeedPanelTexts(),
    colors: HyperPlaybackSpeedPanelColors = HyperPlaybackSpeedPanelDefaults.colors(),
    speedOptions: List<Float> = HyperPlaybackSpeedPanelDefaults.MajorSpeeds,
    valueRange: ClosedFloatingPointRange<Float> = HyperPlaybackSpeedPanelDefaults.SliderRange,
    steps: Int = HyperPlaybackSpeedPanelDefaults.SliderSteps,
    defaultSpeed: Float = HyperPlaybackSpeedPanelDefaults.DefaultSpeed,
    shape: Shape = HyperPlaybackSpeedPanelDefaults.Shape,
    leadingContent: (@Composable BoxScope.() -> Unit)? = null,
    closeContent: (@Composable BoxScope.() -> Unit)? = null,
    hintLeadingContent: (@Composable BoxScope.() -> Unit)? = null,
    resetContent: (@Composable BoxScope.() -> Unit)? = null,
    customActionLeadingContent: (@Composable BoxScope.() -> Unit)? = null
) {
    if (!visible) return

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.scrimColor)
                .hyperNoRippleClickable(onClick = onDismissRequest)
        )
        HyperPlaybackSpeedPanel(
            currentSpeed = currentSpeed,
            onSpeedChange = onSpeedChange,
            onDismissRequest = onDismissRequest,
            onCustomSpeedRequest = onCustomSpeedRequest,
            modifier = panelModifier
                .align(Alignment.Center)
                .padding(horizontal = HyperPlaybackSpeedPanelDefaults.OverlayHorizontalPadding),
            texts = texts,
            colors = colors,
            speedOptions = speedOptions,
            valueRange = valueRange,
            steps = steps,
            defaultSpeed = defaultSpeed,
            shape = shape,
            leadingContent = leadingContent,
            closeContent = closeContent,
            hintLeadingContent = hintLeadingContent,
            resetContent = resetContent,
            customActionLeadingContent = customActionLeadingContent
        )
    }
}

/** 固定深色的受控播放速度面板，可脱离覆盖层单独组合。 */
@Composable
fun HyperPlaybackSpeedPanel(
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    onDismissRequest: () -> Unit,
    onCustomSpeedRequest: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    texts: HyperPlaybackSpeedPanelTexts = HyperPlaybackSpeedPanelTexts(),
    colors: HyperPlaybackSpeedPanelColors = HyperPlaybackSpeedPanelDefaults.colors(),
    speedOptions: List<Float> = HyperPlaybackSpeedPanelDefaults.MajorSpeeds,
    valueRange: ClosedFloatingPointRange<Float> = HyperPlaybackSpeedPanelDefaults.SliderRange,
    steps: Int = HyperPlaybackSpeedPanelDefaults.SliderSteps,
    defaultSpeed: Float = HyperPlaybackSpeedPanelDefaults.DefaultSpeed,
    shape: Shape = HyperPlaybackSpeedPanelDefaults.Shape,
    leadingContent: (@Composable BoxScope.() -> Unit)? = null,
    closeContent: (@Composable BoxScope.() -> Unit)? = null,
    hintLeadingContent: (@Composable BoxScope.() -> Unit)? = null,
    resetContent: (@Composable BoxScope.() -> Unit)? = null,
    customActionLeadingContent: (@Composable BoxScope.() -> Unit)? = null
) {
    require(valueRange.start.isFinite() && valueRange.endInclusive.isFinite()) {
        "valueRange 的起止值必须是有限数值"
    }
    require(valueRange.endInclusive > valueRange.start) {
        "valueRange 的结束值必须大于起始值"
    }
    require(steps >= 0) { "steps 不能小于 0" }

    val resolvedOptions = remember(speedOptions, valueRange) {
        speedOptions
            .asSequence()
            .filter { speed -> speed.isFinite() }
            .filter { speed -> speed in valueRange }
            .distinct()
            .sorted()
            .toList()
            .ifEmpty { listOf(valueRange.start, valueRange.endInclusive) }
    }

    HyperPlaybackSpeedPanelDarkTheme(colors) {
        Column(
            modifier = modifier
                .widthIn(max = HyperPlaybackSpeedPanelDefaults.MaxWidth)
                .fillMaxWidth(HyperPlaybackSpeedPanelDefaults.WidthFraction)
                .hyperSurfaceDepth(
                    shape = shape,
                    visuals = hyperSurfaceDepthVisuals(
                        strokeColor = colors.panelBorderColor,
                        elevation = HyperPlaybackSpeedPanelDefaults.Elevation,
                        ambientShadowColor = Color(0f, 0f, 0f, 0.24f),
                        spotShadowColor = Color(0f, 0f, 0f, 0.36f)
                    )
                )
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        listOf(colors.containerTopColor, colors.containerBottomColor)
                    )
                )
                .hyperNoRippleClickable(onClick = {})
                .padding(HyperPlaybackSpeedPanelDefaults.ContentPadding),
            verticalArrangement = Arrangement.spacedBy(
                HyperPlaybackSpeedPanelDefaults.ContentSpacing
            )
        ) {
            HyperPlaybackSpeedPanelHeader(
                currentSpeed = currentSpeed,
                texts = texts,
                colors = colors,
                onDismissRequest = onDismissRequest,
                leadingContent = leadingContent,
                closeContent = closeContent
            )
            HyperPlaybackSpeedSlider(
                currentSpeed = currentSpeed,
                onSpeedChange = onSpeedChange,
                valueRange = valueRange,
                steps = steps,
                speedOptions = resolvedOptions,
                texts = texts,
                colors = colors
            )
            HyperPlaybackSpeedLabels(
                currentSpeed = currentSpeed,
                valueRange = valueRange,
                steps = steps,
                speedOptions = resolvedOptions,
                onSpeedChange = onSpeedChange,
                colors = colors
            )
            HyperPlaybackSpeedPanelFooter(
                currentSpeed = currentSpeed,
                defaultSpeed = defaultSpeed,
                texts = texts,
                colors = colors,
                onResetRequest = { onSpeedChange(defaultSpeed) },
                onCustomSpeedRequest = onCustomSpeedRequest,
                hintLeadingContent = hintLeadingContent,
                resetContent = resetContent,
                customActionLeadingContent = customActionLeadingContent
            )
        }
    }
}

@Composable
private fun HyperPlaybackSpeedPanelHeader(
    currentSpeed: Float,
    texts: HyperPlaybackSpeedPanelTexts,
    colors: HyperPlaybackSpeedPanelColors,
    onDismissRequest: () -> Unit,
    leadingContent: (@Composable BoxScope.() -> Unit)?,
    closeContent: (@Composable BoxScope.() -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(colors.accentColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides colors.accentColor) {
                if (leadingContent == null) {
                    HyperPlaybackSpeedGaugeGlyph(Modifier.size(20.dp))
                } else {
                    leadingContent()
                }
            }
        }
        Text(
            text = texts.title,
            modifier = Modifier.weight(1f),
            color = colors.contentColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(percent = 50))
                .background(colors.accentColor.copy(alpha = 0.2f))
                .padding(horizontal = 13.dp, vertical = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${HyperPlaybackSpeedPanelDefaults.formatFixed(currentSpeed)}x",
                color = colors.contentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false
            )
        }
        HyperIconButton(
            onClick = onDismissRequest,
            modifier = Modifier
                .size(36.dp)
                .semantics { contentDescription = texts.closeContentDescription },
            colors = panelIconButtonColors(colors)
        ) {
            if (closeContent == null) {
                HyperPlaybackSpeedCloseGlyph(Modifier.size(18.dp))
            } else {
                closeContent()
            }
        }
    }
}

@Composable
private fun HyperPlaybackSpeedSlider(
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    speedOptions: List<Float>,
    texts: HyperPlaybackSpeedPanelTexts,
    colors: HyperPlaybackSpeedPanelColors
) {
    HyperSlider(
        value = currentSpeed,
        onValueChange = onSpeedChange,
        modifier = Modifier.semantics {
            contentDescription = texts.sliderContentDescription
        },
        valueRange = valueRange,
        steps = steps,
        showSegmentMarkers = true,
        segmentValues = speedOptions,
        minimumTouchHeight = HyperPlaybackSpeedPanelDefaults.SliderTouchHeight,
        trackHeight = HyperPlaybackSpeedPanelDefaults.SliderTrackHeight,
        thumbSize = HyperPlaybackSpeedPanelDefaults.SliderThumbSize,
        segmentMarkerSize = HyperPlaybackSpeedPanelDefaults.SliderMarkerSize,
        colors = HyperSliderDefaults.colors(
            trackColor = colors.trackColor,
            activeTrackColor = colors.accentColor,
            segmentMarkerColor = colors.segmentMarkerColor,
            thumbColor = colors.contentColor,
            thumbCenterColor = colors.accentColor,
            thumbHaloColor = colors.accentColor.copy(alpha = 0.22f)
        )
    )
}

@Composable
private fun HyperPlaybackSpeedLabels(
    currentSpeed: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    speedOptions: List<Float>,
    onSpeedChange: (Float) -> Unit,
    colors: HyperPlaybackSpeedPanelColors
) {
    val selectionTolerance = (valueRange.endInclusive - valueRange.start) /
        (steps + 1).coerceAtLeast(1) / 2f
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(HyperPlaybackSpeedPanelDefaults.SpeedLabelHeight)
    ) {
        speedOptions.forEach { speed ->
            val fraction = (
                (speed - valueRange.start) /
                    (valueRange.endInclusive - valueRange.start)
                ).coerceIn(0f, 1f)
            val selected = abs(currentSpeed - speed) < selectionTolerance
            Box(
                modifier = Modifier
                    .offset(
                        x = (maxWidth - HyperPlaybackSpeedPanelDefaults.SpeedLabelWidth) * fraction
                    )
                    .width(HyperPlaybackSpeedPanelDefaults.SpeedLabelWidth)
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
                        onClick = { onSpeedChange(speed) }
                    )
                    .padding(vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${HyperPlaybackSpeedPanelDefaults.formatCompact(speed)}x",
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
private fun HyperPlaybackSpeedPanelFooter(
    currentSpeed: Float,
    defaultSpeed: Float,
    texts: HyperPlaybackSpeedPanelTexts,
    colors: HyperPlaybackSpeedPanelColors,
    onResetRequest: () -> Unit,
    onCustomSpeedRequest: (() -> Unit)?,
    hintLeadingContent: (@Composable BoxScope.() -> Unit)?,
    resetContent: (@Composable BoxScope.() -> Unit)?,
    customActionLeadingContent: (@Composable BoxScope.() -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalContentColor provides colors.supportingContentColor
            ) {
                if (hintLeadingContent == null) {
                    HyperPlaybackSpeedHintGlyph()
                } else {
                    hintLeadingContent()
                }
            }
        }
        Text(
            text = texts.realtimeHint,
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp),
            color = colors.supportingContentColor,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        HyperIconButton(
            onClick = onResetRequest,
            enabled = abs(currentSpeed - defaultSpeed) > 0.001f,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(38.dp)
                .semantics { contentDescription = texts.resetContentDescription },
            colors = panelIconButtonColors(colors)
        ) {
            if (resetContent == null) {
                HyperPlaybackSpeedResetGlyph(Modifier.size(17.dp))
            } else {
                resetContent()
            }
        }
        if (onCustomSpeedRequest != null) {
            HyperPlaybackSpeedCustomAction(
                text = texts.customAction,
                colors = colors,
                onClick = onCustomSpeedRequest,
                leadingContent = customActionLeadingContent
            )
        }
    }
}

@Composable
private fun HyperPlaybackSpeedCustomAction(
    text: String,
    colors: HyperPlaybackSpeedPanelColors,
    onClick: () -> Unit,
    leadingContent: (@Composable BoxScope.() -> Unit)?
) {
    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 38.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(colors.accentColor.copy(alpha = 0.16f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = 13.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides colors.accentColor) {
                if (leadingContent == null) {
                    HyperPlaybackSpeedEditGlyph()
                } else {
                    leadingContent()
                }
            }
        }
        Text(
            text = text,
            color = colors.contentColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun panelIconButtonColors(
    colors: HyperPlaybackSpeedPanelColors
): HyperIconButtonColors = HyperIconButtonDefaults.colors(
    containerColor = colors.contentColor.copy(alpha = 0.10f),
    pressedContainerColor = colors.contentColor.copy(alpha = 0.18f),
    contentColor = colors.contentColor,
    disabledContainerColor = colors.contentColor.copy(alpha = 0.05f),
    disabledContentColor = colors.contentColor.copy(alpha = 0.28f)
)

@Composable
private fun HyperPlaybackSpeedPanelDarkTheme(
    colors: HyperPlaybackSpeedPanelColors,
    content: @Composable () -> Unit
) {
    val typography = MaterialTheme.typography
    val shapes = MaterialTheme.shapes
    val colorScheme = remember(colors) {
        darkColorScheme(
            primary = colors.accentColor,
            onPrimary = colors.contentColor,
            background = colors.containerBottomColor,
            onBackground = colors.contentColor,
            surface = colors.containerTopColor,
            onSurface = colors.contentColor,
            surfaceVariant = colors.contentColor.copy(alpha = 0.12f),
            onSurfaceVariant = colors.supportingContentColor,
            outline = colors.panelBorderColor
        )
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
