/** 文件职责：基于 Compose Dialog 提供无窗口动画的模态窗口和可复用实色面板。 */
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
import androidx.compose.ui.unit.dp

@Immutable
data class HyperDialogColors(
    val containerColor: Color
)

/** 使用稳定的全尺寸 Dialog 根节点；显示状态与关闭结果由调用方管理。 */
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

            val availableWidth = (maxWidth - HyperDialogWindowPadding * 2)
                .coerceAtLeast(0.dp)
            val availableHeight = (maxHeight - HyperDialogWindowPadding * 2)
                .coerceAtLeast(0.dp)
            val resolvedMaxWidth = HyperDialogDefaults.MaxWidth.coerceAtMost(availableWidth)
            val resolvedMinWidth = HyperDialogDefaults.MinWidth.coerceAtMost(resolvedMaxWidth)

            HyperFloatingPanel(
                title = title,
                modifier = modifier
                    .widthIn(
                        min = resolvedMinWidth,
                        max = resolvedMaxWidth
                    )
                    .heightIn(max = availableHeight)
                    // 注册面板命中区域，但不消费事件，避免空白处点击穿透到关闭层。
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) awaitPointerEvent()
                        }
                    },
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

private val HyperDialogWindowPadding = 16.dp

object HyperDialogDefaults {
    val MinWidth = 280.dp
    val MaxWidth = 360.dp
    val Shape: Shape = HyperFloatingPanelDefaults.Shape
    val ContentSpacing = HyperFloatingPanelDefaults.ContentSpacing
    val ActionSpacing = HyperFloatingPanelDefaults.ActionSpacing
    const val ShowScrollIndicator = HyperFloatingPanelDefaults.ShowScrollIndicator

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
