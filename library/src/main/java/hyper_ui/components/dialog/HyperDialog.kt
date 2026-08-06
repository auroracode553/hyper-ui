package hyper_ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.ScrollState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun HyperDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    minWidth: Dp = HyperDialogDefaults.MinWidth,
    maxWidth: Dp = HyperDialogDefaults.MaxWidth,
    maxHeight: Dp = HyperDialogDefaults.MaxHeight,
    shape: Shape = HyperDialogDefaults.Shape,
    elevation: Dp = HyperDialogDefaults.Elevation,
    containerColor: Color = Color.Unspecified,
    contentPadding: PaddingValues = HyperDialogDefaults.ContentPadding,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperDialogDefaults.ContentSpacing),
    actionsArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperDialogDefaults.ActionSpacing,
        Alignment.End
    ),
    showScrollIndicator: Boolean = HyperDialogDefaults.ShowScrollIndicator,
    actions: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    var isFullyDismissed by remember { mutableStateOf(false) }

    LaunchedEffect(show) {
        if (show) {
            isFullyDismissed = false
        }
    }

    if (!show && isFullyDismissed) {
        return
    }

    val resolvedContainerColor = if (containerColor == Color.Unspecified) {
        HyperColors.cardContainer
    } else {
        containerColor
    }
    val scrollState = rememberScrollState()

    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(show) {
        if (show) {
            animProgress.animateTo(1f, animationSpec = tween(durationMillis = 300))
        } else {
            animProgress.animateTo(0f, animationSpec = tween(durationMillis = 300))
            isFullyDismissed = true
        }
    }

    DisableSelection {
        Popup(
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = modifier
                        .widthIn(min = minWidth, max = maxWidth)
                        .fillMaxWidth()
                        .heightIn(max = maxHeight)
                        .graphicsLayer {
                            alpha = animProgress.value
                            scaleX = 0.8f + 0.2f * animProgress.value
                            scaleY = 0.8f + 0.2f * animProgress.value
                        }
                        .shadow(elevation, shape)
                        .background(resolvedContainerColor, shape)
                        .padding(contentPadding),
                    horizontalAlignment = horizontalAlignment,
                    verticalArrangement = Arrangement.spacedBy(HyperDialogDefaults.ContentSpacing)
                ) {
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

                    if (actions != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = actionsArrangement,
                            verticalAlignment = Alignment.CenterVertically,
                            content = actions
                        )
                    }
                }
            }
        }
    }
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
                .clip(RoundedCornerShape(percent = 50))
                .background(HyperColors.divider)
        )
    }
}

object HyperDialogDefaults {
    val MinWidth = 280.dp
    val MaxWidth = 340.dp
    val MaxHeight = 480.dp
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)
    val Elevation = 8.dp
    val ContentPadding = PaddingValues(horizontal = 24.dp, vertical = 22.dp)
    val ContentSpacing = 16.dp
    val ActionSpacing = 12.dp
    const val ShowScrollIndicator = true
    val ScrollIndicatorWidth = 3.dp
    val ScrollIndicatorContentPadding = 10.dp
    val ScrollIndicatorMinHeight = 32.dp
}
