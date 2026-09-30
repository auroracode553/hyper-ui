/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/panel/HyperCard 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
data class HyperCardColors(
    val containerColor: Color
)

/**
 * 面板容器组件。
 *
 * modifier 控制面板外壳，contentModifier 控制内部内容区。
 */
@Composable
fun HyperCard(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperCardDefaults.ContentPadding),
    colors: HyperCardColors = HyperCardDefaults.colors(),
    shape: Shape = HyperCardDefaults.Shape,
    elevation: Dp = HyperCardDefaults.Elevation,
    border: BorderStroke? = HyperCardDefaults.border(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperCardDefaults.ContentSpacing),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
                        .hyperFrostedSurface(
                containerColor = colors.containerColor,
                shape = shape,
                elevation = elevation,
                border = border
            )
    ) {
        Column(
            modifier = contentModifier.fillMaxWidth(),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

object HyperCardDefaults {
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)
    val Elevation = 12.dp
    // 16dp 足以避开圆角边界，也避免调用方组合 40dp 控件时形成过高卡片。
    val ContentPadding = PaddingValues(16.dp)
    val ContentSpacing = 12.dp

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperCardColors = HyperCardColors(
        containerColor = resolveHyperContainerColor(
            containerColor = containerColor,
            fallbackColor = hyperGlass.surface
        )
    )

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = BorderStroke(1.dp, resolveHyperContainerColor(color, hyperGlass.border))
}
