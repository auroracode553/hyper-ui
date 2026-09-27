/** 文件职责：提供相对应用窗口居中的轻量 Popup，不承担模态 Dialog 职责。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
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
    val transitionProgress = hyperOverlayProgress(visible)
    if (!visible && transitionProgress == 0f) return

    val layoutDirection = LocalLayoutDirection.current
    val containerColor = resolveHyperContainerColor(colors.containerColor, hyperGlass.surfaceStrong)
    val showPositionedContent = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Popup 首帧需要完成内容尺寸定位，定位完成前保持透明以避免从窗口起点跳入中心。
        withFrameNanos { }
        showPositionedContent.value = true
    }

    Popup(
        popupPositionProvider = HyperPopupWindowCenterPositionProvider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier.alpha(if (showPositionedContent.value) 1f else 0f)
        ) {
            val horizontalWindowPadding =
                HyperPopupDefaults.WindowPadding.calculateLeftPadding(layoutDirection) +
                    HyperPopupDefaults.WindowPadding.calculateRightPadding(layoutDirection)
            val verticalWindowPadding =
                HyperPopupDefaults.WindowPadding.calculateTopPadding() +
                    HyperPopupDefaults.WindowPadding.calculateBottomPadding()
            val availableWidth = (maxWidth - horizontalWindowPadding).coerceAtLeast(0.dp)
            val availableHeight = (maxHeight - verticalWindowPadding).coerceAtLeast(0.dp)
            val resolvedMaxWidth = HyperPopupDefaults.MaxWidth.coerceAtMost(availableWidth)
            val resolvedMinWidth = HyperPopupDefaults.MinWidth.coerceAtMost(resolvedMaxWidth)
            val resolvedWidth = (availableWidth * HyperPopupDefaults.WidthFraction)
                .coerceIn(resolvedMinWidth, resolvedMaxWidth)
            val resolvedMaxHeight = (maxHeight * HyperPopupDefaults.MaxHeightFraction)
                .coerceAtMost(availableHeight)

            HyperFloatingPanel(
                title = title,
                modifier = Modifier
                    .widthIn(max = availableWidth)
                    .heightIn(max = availableHeight)
                    .then(modifier)
                    .hyperOverlayMotion(transitionProgress)
                    .width(resolvedWidth)
                    .heightIn(max = resolvedMaxHeight),
                shape = shape,
                containerColor = containerColor,
                border = border,
                horizontalAlignment = horizontalAlignment,
                verticalArrangement = verticalArrangement,
                actionArrangement = actionArrangement,
                showScrollIndicator = showScrollIndicator,
                actionContent = actionContent,
                content = content
            )
        }
    }
}

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

object HyperPopupDefaults {
    val MinWidth = 280.dp
    val MaxWidth = 360.dp
    const val WidthFraction = 0.9f
    const val MaxHeightFraction = 0.7f
    val WindowPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    val Shape: Shape = HyperFloatingPanelDefaults.Shape
    val ContentPadding = HyperFloatingPanelDefaults.ContentPadding
    val ContentSpacing = HyperFloatingPanelDefaults.ContentSpacing
    val ActionSpacing = HyperFloatingPanelDefaults.ActionSpacing
    const val ShowScrollIndicator = HyperFloatingPanelDefaults.ShowScrollIndicator
    val ScrollIndicatorWidth = HyperFloatingPanelDefaults.ScrollIndicatorWidth
    val ScrollIndicatorContentPadding = HyperFloatingPanelDefaults.ScrollIndicatorContentPadding
    val ScrollIndicatorMinHeight = HyperFloatingPanelDefaults.ScrollIndicatorMinHeight

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperPopupColors = HyperPopupColors(
        containerColor = resolveHyperContainerColor(containerColor, hyperGlass.surfaceStrong)
    )

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = BorderStroke(1.dp, resolveHyperContainerColor(color, hyperGlass.border))
}
