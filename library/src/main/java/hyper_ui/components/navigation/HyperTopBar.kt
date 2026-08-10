/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/navigation/HyperTopBar 模块实现，并集中维护其依赖协作与核心逻辑。 */
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
data class HyperTopBarColors(
    val containerColor: Color,
    val contentColor: Color
)

/**
 * 顶部导航栏组件。
 *
 * 组件内部已包含默认水平内容间距（16dp），外部间距请通过 modifier.padding(...) 控制。
 */
@Composable
fun HyperTopBar(
    titleContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    colors: HyperTopBarColors = HyperTopBarDefaults.colors(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperTopBarDefaults.ContentGap),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = HyperTopBarDefaults.MinHeight)
            .hyperSurface(
                containerColor = colors.containerColor,
                shape = HyperTopBarDefaults.Shape
            )
            .padding(horizontal = 16.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
            navigationContent?.invoke(this)
            CompositionLocalProvider(LocalTextStyle provides HyperTopBarDefaults.TitleTextStyle) {
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

object HyperTopBarDefaults {
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
    ): HyperTopBarColors = HyperTopBarColors(
        containerColor = resolveHyperContainerColor(containerColor, HyperColors.cardContainer),
        contentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText)
    )
}
