package hyper_ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Immutable
data class HyperDialogColors(
    val containerColor: Color
)

@Composable
fun HyperDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String? = null,
    modifier: Modifier = Modifier,
    minWidth: Dp = HyperDialogDefaults.MinWidth,
    maxWidth: Dp = HyperDialogDefaults.MaxWidth,
    maxHeight: Dp = HyperDialogDefaults.MaxHeight,
    shape: Shape = HyperDialogDefaults.Shape,
    colors: HyperDialogColors = HyperDialogDefaults.colors(),
    contentPadding: PaddingValues = HyperDialogDefaults.ContentPadding,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperDialogDefaults.ContentSpacing),
    actionArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperDialogDefaults.ActionSpacing,
        Alignment.End
    ),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = false,
    showScrollIndicator: Boolean = HyperDialogDefaults.ShowScrollIndicator,
    actionContent: (@Composable RowScope.() -> Unit)? = null,
    border: BorderStroke? = HyperDialogDefaults.border(),
    content: @Composable ColumnScope.() -> Unit
) {
    var isFullyDismissed by remember { mutableStateOf(!visible) }

    LaunchedEffect(visible) {
        if (visible) {
            isFullyDismissed = false
        }
    }

    if (!visible && isFullyDismissed) {
        return
    }

    val resolvedTitle = title?.trim()?.takeIf { it.isNotEmpty() }
    val scrollState = rememberScrollState()
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(visible) {
        if (visible) {
            animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 300))
        } else {
            animationProgress.animateTo(0f, animationSpec = tween(durationMillis = 300))
            isFullyDismissed = true
        }
    }

    DisableSelection {
        Popup(
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnClickOutside
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth(HyperDialogDefaults.WidthFraction)
                        .widthIn(min = minWidth, max = maxWidth)
                        .heightIn(max = maxHeight)
                        .graphicsLayer {
                            alpha = animationProgress.value
                            scaleX = 0.8f + 0.2f * animationProgress.value
                            scaleY = 0.8f + 0.2f * animationProgress.value
                        }
                        .clip(shape)
                        .background(color = colors.containerColor, shape = shape)
                        .then(if (border != null) Modifier.border(border, shape) else Modifier)
                        .padding(contentPadding),
                    horizontalAlignment = horizontalAlignment,
                    verticalArrangement = Arrangement.spacedBy(HyperDialogDefaults.ContentSpacing)
                ) {
                    CompositionLocalProvider(LocalContentColor provides HyperColors.primaryText) {
                        if (resolvedTitle != null) {
                            HyperDialogTitle(title = resolvedTitle)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = false)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        end = if (showScrollIndicator) {
                                            HyperDialogDefaults.ScrollIndicatorContentPadding
                                        } else {
                                            0.dp
                                        }
                                    )
                                    .verticalScroll(scrollState),
                                horizontalAlignment = horizontalAlignment,
                                verticalArrangement = verticalArrangement,
                                content = content
                            )

                            if (showScrollIndicator) {
                                HyperDialogScrollIndicator(
                                    scrollState = scrollState,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }

                        if (actionContent != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = actionArrangement,
                                verticalAlignment = Alignment.CenterVertically,
                                content = actionContent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HyperDialogTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.fillMaxWidth(),
        color = HyperColors.primaryText,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun HyperDialogScrollIndicator(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    if (scrollState.maxValue <= 0) {
        return
    }

    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .width(HyperDialogDefaults.ScrollIndicatorWidth)
    ) {
        if (!constraints.hasBoundedHeight) {
            return@BoxWithConstraints
        }

        val viewportPx = constraints.maxHeight.toFloat()
        if (viewportPx <= 0f) {
            return@BoxWithConstraints
        }

        val contentPx = viewportPx + scrollState.maxValue.toFloat()
        val minThumbPx = with(density) { HyperDialogDefaults.ScrollIndicatorMinHeight.toPx() }
        val thumbHeightPx = (viewportPx * viewportPx / contentPx).coerceAtLeast(minThumbPx)
        val travelPx = (viewportPx - thumbHeightPx).coerceAtLeast(0f)
        val scrollProgress = scrollState.value / scrollState.maxValue.toFloat()
        val thumbOffsetPx = travelPx * scrollProgress

        Box(
            modifier = Modifier
                .offset(y = with(density) { thumbOffsetPx.toDp() })
                .width(HyperDialogDefaults.ScrollIndicatorWidth)
                .height(with(density) { thumbHeightPx.toDp() })
                .background(HyperColors.divider, RoundedCornerShape(percent = 50))
        )
    }
}

object HyperDialogDefaults {
    val MinWidth = 280.dp
    const val WidthFraction = 0.92f
    val MaxWidth = 360.dp
    val MaxHeight = 480.dp
    val Shape: Shape = RoundedCornerShape(20.dp)
    val ContentPadding = PaddingValues(20.dp)
    val ContentSpacing = 16.dp
    val ActionSpacing = 12.dp
    const val ShowScrollIndicator = true
    val ScrollIndicatorWidth = 3.dp
    val ScrollIndicatorContentPadding = 10.dp
    val ScrollIndicatorMinHeight = 32.dp

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperDialogColors = HyperDialogColors(
        containerColor = resolveHyperContainerColor(
            containerColor = containerColor,
            fallbackColor = HyperColors.cardContainer
        )
    )

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = hyperPanelBorder(color)
}
