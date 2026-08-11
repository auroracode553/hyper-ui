/** 文件职责：提供不依赖调用节点位置的 HyperPopup 窗口居中浮层。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

@Immutable
data class HyperPopupColors(
    val containerColor: Color
)

/**
 * 弹出层组件。
 *
 * 组件内部已包含默认内边距，外部间距请通过 modifier 控制。
 */
@Composable
fun HyperPopup(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    shape: Shape = HyperPopupDefaults.Shape,
    colors: HyperPopupColors = HyperPopupDefaults.colors(),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperPopupDefaults.ContentSpacing),
    actionArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperPopupDefaults.ActionSpacing,
        Alignment.End
    ),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    showScrollIndicator: Boolean = HyperPopupDefaults.ShowScrollIndicator,
    actionContent: (@Composable RowScope.() -> Unit)? = null,
    border: BorderStroke? = HyperPopupDefaults.border(),
    content: @Composable ColumnScope.() -> Unit
) {
    if (!visible) {
        return
    }

    val resolvedTitle = title?.trim()?.takeIf { it.isNotEmpty() }
    val scrollState = rememberScrollState()
    val layoutDirection = LocalLayoutDirection.current
    val containerColor = resolveHyperOpaqueColor(
        color = colors.containerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = HyperColors.pageBackground
    )
    val windowPadding = HyperPopupDefaults.WindowPadding
    val contentPadding = HyperPopupDefaults.ContentPadding

    DisableSelection {
        Popup(
            popupPositionProvider = HyperPopupWindowCenterPositionProvider,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnClickOutside
            )
        ) {
            BoxWithConstraints {
                val horizontalWindowPadding =
                    windowPadding.calculateLeftPadding(layoutDirection) +
                        windowPadding.calculateRightPadding(layoutDirection)
                val verticalWindowPadding =
                    windowPadding.calculateTopPadding() + windowPadding.calculateBottomPadding()
                val availableWidth =
                    (this.maxWidth - horizontalWindowPadding).coerceAtLeast(0.dp)
                val availableHeight =
                    (this.maxHeight - verticalWindowPadding).coerceAtLeast(0.dp)
                val resolvedMaxWidth = HyperPopupDefaults.MaxWidth.coerceAtMost(availableWidth)
                val resolvedMinWidth = HyperPopupDefaults.MinWidth.coerceAtMost(resolvedMaxWidth)
                // 先按窗口比例收窄，再应用尺寸边界，兼顾竖屏留白与小窗口不越界。
                val resolvedWidth = (availableWidth * HyperPopupDefaults.WidthFraction)
                    .coerceIn(resolvedMinWidth, resolvedMaxWidth)
                val resolvedMaxHeight = HyperPopupDefaults.MaxHeight.coerceAtMost(availableHeight)

                Column(
                    modifier = Modifier
                        .widthIn(max = availableWidth)
                        .heightIn(max = availableHeight)
                        .then(modifier)
                        .width(resolvedWidth)
                        .heightIn(max = resolvedMaxHeight)
                        .clip(shape)
                        .background(color = containerColor, shape)
                        .then(if (border != null) Modifier.border(border, shape) else Modifier)
                        .padding(contentPadding),
                    horizontalAlignment = horizontalAlignment,
                    verticalArrangement = Arrangement.spacedBy(HyperPopupDefaults.ContentSpacing)
                ) {
                    CompositionLocalProvider(LocalContentColor provides HyperColors.primaryText) {
                        if (resolvedTitle != null) {
                            HyperPopupTitle(title = resolvedTitle)
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
                                            HyperPopupDefaults.ScrollIndicatorContentPadding
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
                                HyperPopupScrollIndicator(
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

/**
 * Popup 默认会以调用节点作为锚点；基础弹出层必须忽略锚点并相对应用窗口居中。
 * 该规则是组件内部实现，不向调用方暴露定位参数。
 */
private object HyperPopupWindowCenterPositionProvider : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset = IntOffset(
        x = ((windowSize.width - popupContentSize.width) / 2).coerceAtLeast(0),
        y = ((windowSize.height - popupContentSize.height) / 2).coerceAtLeast(0)
    )
}

@Composable
private fun HyperPopupTitle(title: String) {
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
private fun HyperPopupScrollIndicator(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    if (scrollState.maxValue <= 0) {
        return
    }

    val density = LocalDensity.current
    val indicatorColor = resolveHyperOpaqueColor(
        color = HyperColors.divider,
        fallbackColor = HyperColors.divider,
        backgroundColor = HyperColors.cardContainer
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .width(HyperPopupDefaults.ScrollIndicatorWidth)
    ) {
        if (!constraints.hasBoundedHeight) {
            return@BoxWithConstraints
        }

        val viewportPx = constraints.maxHeight.toFloat()
        if (viewportPx <= 0f) {
            return@BoxWithConstraints
        }

        val contentPx = viewportPx + scrollState.maxValue.toFloat()
        val minThumbPx = with(density) { HyperPopupDefaults.ScrollIndicatorMinHeight.toPx() }
        val thumbHeightPx = (viewportPx * viewportPx / contentPx).coerceAtLeast(minThumbPx)
        val travelPx = (viewportPx - thumbHeightPx).coerceAtLeast(0f)
        val scrollProgress = scrollState.value / scrollState.maxValue.toFloat()
        val thumbOffsetPx = travelPx * scrollProgress

        Box(
            modifier = Modifier
                .offset(y = with(density) { thumbOffsetPx.toDp() })
                .width(HyperPopupDefaults.ScrollIndicatorWidth)
                .height(with(density) { thumbHeightPx.toDp() })
                .background(indicatorColor, RoundedCornerShape(percent = 50))
        )
    }
}

object HyperPopupDefaults {
    val MinWidth = 280.dp
    val MaxWidth = 360.dp
    const val WidthFraction = 0.9f
    val MaxHeight = 480.dp
    val WindowPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    val Shape: Shape = RoundedCornerShape(20.dp)
    val ContentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp)
    val ContentSpacing = 16.dp
    val ActionSpacing = 12.dp
    const val ShowScrollIndicator = true
    val ScrollIndicatorWidth = 3.dp
    val ScrollIndicatorContentPadding = 10.dp
    val ScrollIndicatorMinHeight = 32.dp

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperPopupColors = HyperPopupColors(
        containerColor = resolveHyperOpaqueColor(
            color = containerColor,
            fallbackColor = HyperColors.cardContainer,
            backgroundColor = HyperColors.pageBackground
        )
    )

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = hyperSolidPanelBorder(
        color = color,
        backgroundColor = HyperColors.cardContainer
    )
}
