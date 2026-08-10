/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/panel/HyperPanel 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class HyperPanelColors(
    val containerColor: Color
)

/**
 * 面板容器组件。
 *
 * 组件不内置内边距，如需内容间距请通过 modifier.padding(...) 控制。
 */
@Composable
fun HyperPanel(
    modifier: Modifier = Modifier,
    colors: HyperPanelColors = HyperPanelDefaults.colors(),
    shape: Shape = HyperPanelDefaults.Shape,
    elevation: Dp = HyperPanelDefaults.Elevation,
    border: BorderStroke? = HyperPanelDefaults.border(),
    clipContent: Boolean = true,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperPanelDefaults.ContentSpacing),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .hyperSurface(
                containerColor = colors.containerColor,
                shape = shape,
                elevation = elevation,
                border = border,
                clipContent = clipContent
            ),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        content = content
    )
}

object HyperPanelDefaults {
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)
    val Elevation = 0.dp
    val ContentSpacing = 12.dp

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperPanelColors = HyperPanelColors(
        containerColor = resolveHyperContainerColor(
            containerColor = containerColor,
            fallbackColor = HyperColors.elevatedContainer
        )
    )

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = hyperPanelBorder(color)
}
