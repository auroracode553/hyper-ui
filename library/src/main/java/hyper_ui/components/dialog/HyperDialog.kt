/** 文件职责：基于 Compose 标准 Dialog 提供模态窗口和可复用实色面板。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Immutable
data class HyperDialogColors(
    val containerColor: Color
)

/** 使用标准模态窗口；平台负责背景调暗、焦点、返回键和外部点击。 */
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

    val containerColor = resolveHyperOpaqueColor(
        color = colors.containerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = HyperColors.pageBackground
    )

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        HyperFloatingPanel(
            title = title,
            modifier = modifier.widthIn(
                min = HyperDialogDefaults.MinWidth,
                max = HyperDialogDefaults.MaxWidth
            ),
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
