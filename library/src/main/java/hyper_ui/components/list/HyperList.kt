/** 文件职责：提供带 HyperUI 统一容器样式的页面级懒加载列表。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class HyperListColors(
    val containerColor: Color
)

/**
 * 页面级懒加载列表。
 *
 * 组件只负责 LazyColumn 与统一容器样式，项目结构完全由 LazyListScope slot 提供。
 * modifier 控制列表外壳，contentModifier 控制容器节点布局，contentPadding 随列表内容滚动。
 */
@Composable
fun HyperList(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = HyperListDefaults.ContentPadding,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    shape: Shape = HyperListDefaults.Shape,
    border: BorderStroke? = null,
    colors: HyperListColors = HyperListDefaults.colors(),
    content: LazyListScope.() -> Unit
) {
    val containerColor = resolveHyperContainerColor(colors.containerColor, hyperGlass.surface)

    CompositionLocalProvider(
        LocalHyperListTileDividerSuppressed provides false,
        LocalHyperListTileContainerColor provides containerColor
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .hyperFrostedSurface(containerColor, shape, blur = 18.dp, border = border)
                .then(contentModifier),
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

object HyperListDefaults {
    val Shape: Shape = RoundedCornerShape(18.dp)
    /** 列表容器默认提供左右 16.dp 内容留白，子项（HyperListTile 等）无需重复设置水平 padding。 */
    val ContentPadding: PaddingValues = PaddingValues(4.dp)

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperListColors = HyperListColors(
        containerColor = resolveHyperContainerColor(containerColor, hyperGlass.surface)
    )

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = hyperSolidPanelBorder(
        color = color,
        backgroundColor = hyperGlass.surface
    )
}
