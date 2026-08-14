/** 文件职责：提供固定窗口根尺寸、面板居中且无蒙层的模态 Dialog。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp

@Immutable
data class HyperDialogColors(
    val containerColor: Color
)

/** Dialog 窗口根节点始终铺满可用窗口，正文尺寸变化只重排内部面板。 */
@Composable
fun HyperDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    shape: Shape = HyperDialogDefaults.Shape,
    colors: HyperDialogColors = HyperDialogDefaults.colors(),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperDialogDefaults.ContentSpacing),
    actionArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperDialogDefaults.ActionSpacing,
        Alignment.End
    ),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    showScrollIndicator: Boolean = HyperDialogDefaults.ShowScrollIndicator,
    actionContent: (@Composable RowScope.() -> Unit)? = null,
    border: BorderStroke? = HyperDialogDefaults.border(),
    content: @Composable ColumnScope.() -> Unit
) {
    if (!visible) return

    val layoutDirection = LocalLayoutDirection.current
    val currentOnDismissRequest by rememberUpdatedState(onDismissRequest)
    val containerColor = resolveHyperOpaqueColor(
        color = colors.containerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = HyperColors.pageBackground
    )

    HyperDialogHost(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = dismissOnBackPress
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (dismissOnClickOutside) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .pointerInput(Unit) {
                            detectTapGestures { currentOnDismissRequest() }
                        }
                )
            }

            val horizontalWindowPadding =
                HyperDialogDefaults.WindowPadding.calculateLeftPadding(layoutDirection) +
                    HyperDialogDefaults.WindowPadding.calculateRightPadding(layoutDirection)
            val verticalWindowPadding =
                HyperDialogDefaults.WindowPadding.calculateTopPadding() +
                    HyperDialogDefaults.WindowPadding.calculateBottomPadding()
            val availableWidth = (maxWidth - horizontalWindowPadding).coerceAtLeast(0.dp)
            val availableHeight = (maxHeight - verticalWindowPadding).coerceAtLeast(0.dp)
            val resolvedMaxWidth = HyperDialogDefaults.MaxWidth.coerceAtMost(availableWidth)
            val resolvedMinWidth = HyperDialogDefaults.MinWidth.coerceAtMost(resolvedMaxWidth)
            val resolvedWidth = (availableWidth * HyperDialogDefaults.WidthFraction)
                .coerceIn(resolvedMinWidth, resolvedMaxWidth)
            val resolvedMaxHeight = (maxHeight * HyperDialogDefaults.MaxHeightFraction)
                .coerceAtMost(availableHeight)

            HyperFloatingPanel(
                title = title,
                modifier = Modifier
                    .widthIn(max = availableWidth)
                    .heightIn(max = availableHeight)
                    .then(modifier)
                    .width(resolvedWidth)
                    .heightIn(max = resolvedMaxHeight)
                    // 面板作为最上层命中区域，空白点击不会穿透到外部关闭区域。
                    .pointerInput(Unit) { detectTapGestures { } },
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

object HyperDialogDefaults {
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
    fun colors(containerColor: Color = Color.Unspecified): HyperDialogColors = HyperDialogColors(
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
