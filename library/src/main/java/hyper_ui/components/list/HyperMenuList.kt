/** 文件职责：在 hyper_ui 中负责承载菜单列表容器 HyperMenuList。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

internal val MenuListCornerRadius = HyperStyleDefaults.LargeCornerRadius

@Immutable
data class HyperMenuListColors(
    val containerColor: Color
)

/**
 * 简单菜单列表。
 *
 * 组件不内置内边距，如需内容间距请通过 modifier.padding(...) 控制。
 */
@Composable
fun <T> HyperMenuList(
    items: List<T>,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = HyperMenuListDefaults.border(),
    colors: HyperMenuListColors = HyperMenuListDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
) {
    val containerColor = resolveHyperOpaqueColor(
        color = colors.containerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = HyperColors.pageBackground
    )
    val shape = HyperMenuListDefaults.Shape
    Column(
        modifier = modifier
            .fillMaxWidth()
            .hyperSolidSurface(
                containerColor = containerColor,
                shape = shape,
                border = border
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = verticalArrangement
    ) {
        items.forEachIndexed { index, item ->
            val isFirst = index == 0
            val isLast = index == items.lastIndex
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(menuListItemShape(isFirst, isLast))
                    .background(containerColor)
            ) {
                CompositionLocalProvider(
                    LocalHyperListItemDividerSuppressed provides isLast
                ) {
                    itemContent(item)
                }
            }
        }
    }
}

/**
 * DSL 菜单列表入口。
 *
 * 组件不内置内边距，如需内容间距请通过 modifier.padding(...) 控制。
 */
@Composable
fun HyperMenuList(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = HyperMenuListDefaults.border(),
    colors: HyperMenuListColors = HyperMenuListDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit
) {
    val containerColor = resolveHyperOpaqueColor(
        color = colors.containerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = HyperColors.pageBackground
    )
    CompositionLocalProvider(LocalHyperListItemDividerSuppressed provides false) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .hyperSolidSurface(
                    containerColor = containerColor,
                    shape = HyperMenuListDefaults.Shape,
                    border = border
                ),
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

object HyperMenuListDefaults {
    val Shape: Shape = RoundedCornerShape(MenuListCornerRadius)

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperMenuListColors = HyperMenuListColors(
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

internal fun menuListItemShape(
    isFirst: Boolean,
    isLast: Boolean
) = RoundedCornerShape(
    topStart = if (isFirst) MenuListCornerRadius else 0.dp,
    topEnd = if (isFirst) MenuListCornerRadius else 0.dp,
    bottomEnd = if (isLast) MenuListCornerRadius else 0.dp,
    bottomStart = if (isLast) MenuListCornerRadius else 0.dp
)
