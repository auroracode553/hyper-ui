/** 文件职责：提供带轻量结构描边和阴影的三段式 HyperNavBar 导航容器。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Immutable
data class HyperNavBarColors(
    val containerColor: Color,
    val contentColor: Color
)

/**
 * 顶部导航栏组件。
 *
 * 默认背景透明，由页面容器统一提供底色；轻量描边和阴影负责区分导航层级。
 * 组件内部已包含默认水平内容间距（16dp），外部间距请通过 modifier.padding(...) 控制。
 */
@Composable
fun HyperNavBar(
    titleContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    colors: HyperNavBarColors = HyperNavBarDefaults.colors(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperNavBarDefaults.ContentGap),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = HyperNavBarDefaults.MinHeight)
            .hyperSurfaceDepth(
                shape = HyperNavBarDefaults.Shape,
                visuals = hyperNavBarDepthVisuals()
            )
            .hyperSurface(
                containerColor = colors.containerColor,
                shape = HyperNavBarDefaults.Shape
            )
            .padding(horizontal = 16.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
            navigationContent?.invoke(this)
            CompositionLocalProvider(LocalTextStyle provides HyperNavBarDefaults.TitleTextStyle) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = verticalAlignment,
                    content = titleContent
                )
            }
            actionContent?.invoke(this)
        }
    }
}

object HyperNavBarDefaults {
    val MinHeight = 56.dp
    val ContentGap = 8.dp
    val Shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)

    val TitleTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.SemiBold
        )

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified
    ): HyperNavBarColors = HyperNavBarColors(
        containerColor = resolveHyperContainerColor(containerColor, Color.Transparent),
        contentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText)
    )
}

@Composable
private fun hyperNavBarDepthVisuals(): HyperSurfaceDepthVisuals =
    hyperSurfaceDepthVisuals(
        role = HyperSurfaceDepthRole.NavigationBar,
        elevation = 3.dp
    )
