/** 文件职责：在 hyper_ui 中负责承载页面级列表 HyperList，并集中维护懒加载与普通列表的渲染策略。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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

@Immutable
data class HyperListColors(
    val containerColor: Color
)

/**
 * 简单数据列表入口。
 *
 * modifier 控制列表外壳，contentModifier 控制容器内部内容布局。
 */
@Composable
fun <T> HyperList(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    lazyLoading: Boolean = true,
    lazyListState: LazyListState = rememberLazyListState(),
    scrollState: ScrollState = rememberScrollState(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    shape: Shape = HyperListDefaults.Shape,
    border: BorderStroke? = null,
    colors: HyperListColors = HyperListDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
) {
    val containerColor = resolveHyperOpaqueColor(
        color = colors.containerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = HyperColors.pageBackground
    )
    val containerModifier = modifier.hyperListContainer(
        containerColor = containerColor,
        shape = shape,
        border = border
    ).then(contentModifier)

    if (lazyLoading) {
        LazyColumn(
            modifier = containerModifier,
            state = lazyListState,
            verticalArrangement = verticalArrangement
        ) {
            itemsIndexed(
                items = items,
                key = key?.let { itemKey ->
                    { _: Int, item: T -> itemKey(item) }
                }
            ) { index, item ->
                HyperListItemContainer(
                    containerColor = containerColor,
                    dividerSuppressed = index == items.lastIndex
                ) {
                    itemContent(item)
                }
            }
        }
    } else {
        Column(
            modifier = containerModifier
                .verticalScroll(scrollState),
            verticalArrangement = verticalArrangement
        ) {
            items.forEachIndexed { index, item ->
                HyperListItemContainer(
                    containerColor = containerColor,
                    dividerSuppressed = index == items.lastIndex
                ) {
                    itemContent(item)
                }
            }
        }
    }
}

/**
 * 面向异构项目、分组和分页内容的懒列表入口。
 *
 * DSL 入口固定使用 LazyColumn；简单数据列表如需关闭懒加载，使用 items 入口的 lazyLoading 参数。
 * modifier 控制列表外壳，contentModifier 控制容器内部内容布局。
 */
@Composable
fun HyperList(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    shape: Shape = HyperListDefaults.Shape,
    border: BorderStroke? = null,
    colors: HyperListColors = HyperListDefaults.colors(),
    content: LazyListScope.() -> Unit
) {
    val containerColor = resolveHyperOpaqueColor(
        color = colors.containerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = HyperColors.pageBackground
    )
    CompositionLocalProvider(LocalHyperListItemDividerSuppressed provides false) {
        LazyColumn(
            modifier = modifier.hyperListContainer(
                containerColor = containerColor,
                shape = shape,
                border = border
            ).then(contentModifier),
            state = state,
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

object HyperListDefaults {
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.SmallCornerRadius)

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperListColors = HyperListColors(
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

private fun Modifier.hyperListContainer(
    containerColor: Color,
    shape: Shape,
    border: BorderStroke?
): Modifier = this
    .fillMaxWidth()
    .clip(shape)
    .background(containerColor, shape)
    .then(if (border != null) Modifier.border(border, shape) else Modifier)

@Composable
private fun HyperListItemContainer(
    containerColor: Color,
    dividerSuppressed: Boolean,
    itemContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor)
    ) {
        CompositionLocalProvider(
            LocalHyperListItemDividerSuppressed provides dividerSuppressed
        ) {
            itemContent()
        }
    }
}
