/** 文件职责：在 hyper_ui 中负责承载页面级列表 HyperList，并集中维护懒加载与普通列表的渲染策略。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Immutable
data class HyperListColors(
    val containerColor: Color
)

@Composable
fun <T> HyperList(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    lazyLoading: Boolean = true,
    lazyListState: LazyListState = rememberLazyListState(),
    scrollState: ScrollState = rememberScrollState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = null,
    colors: HyperListColors = HyperListDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
) {
    val containerColor = colors.containerColor
    val containerModifier = modifier.hyperListContainer(
        containerColor = containerColor,
        border = border
    )

    if (lazyLoading) {
        LazyColumn(
            modifier = containerModifier,
            state = lazyListState,
            contentPadding = contentPadding,
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
                .verticalScroll(scrollState)
                .padding(contentPadding),
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
 */
@Composable
fun HyperList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = null,
    colors: HyperListColors = HyperListDefaults.colors(),
    content: LazyListScope.() -> Unit
) {
    val containerColor = colors.containerColor
    CompositionLocalProvider(LocalHyperListItemDividerSuppressed provides false) {
        LazyColumn(
            modifier = modifier.hyperListContainer(
                containerColor = containerColor,
                border = border
            ),
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

object HyperListDefaults {
    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperListColors = HyperListColors(
        containerColor = resolveHyperContainerColor(
            containerColor = containerColor,
            fallbackColor = HyperColors.cardContainer
        )
    )

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = hyperPanelBorder(color)
}

private fun Modifier.hyperListContainer(
    containerColor: Color,
    border: BorderStroke?
): Modifier = this
    .fillMaxWidth()
    .background(containerColor)
    .then(if (border != null) Modifier.border(border, RectangleShape) else Modifier)

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

@Deprecated(
    message = "HyperLazyList 已更名为 HyperList；通过 lazyLoading 参数控制是否开启懒加载。",
    replaceWith = ReplaceWith("HyperList(items = items, modifier = modifier, key = key, lazyLoading = true, contentPadding = contentPadding, verticalArrangement = verticalArrangement, border = border, colors = colors, itemContent = itemContent)")
)
@Composable
fun <T> HyperLazyList(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = null,
    colors: HyperLazyListColors = HyperListDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
) {
    HyperList(
        items = items,
        modifier = modifier,
        key = key,
        lazyLoading = true,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        border = border,
        colors = colors,
        itemContent = itemContent
    )
}

@Deprecated(
    message = "HyperLazyList 已更名为 HyperList。",
    replaceWith = ReplaceWith("HyperList(modifier = modifier, state = state, contentPadding = contentPadding, verticalArrangement = verticalArrangement, border = border, colors = colors, content = content)")
)
@Composable
fun HyperLazyList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = null,
    colors: HyperLazyListColors = HyperListDefaults.colors(),
    content: LazyListScope.() -> Unit
) {
    HyperList(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        border = border,
        colors = colors,
        content = content
    )
}

@Deprecated("HyperLazyListColors 已更名为 HyperListColors。", ReplaceWith("HyperListColors"))
typealias HyperLazyListColors = HyperListColors

@Deprecated("HyperLazyListDefaults 已更名为 HyperListDefaults。", ReplaceWith("HyperListDefaults"))
object HyperLazyListDefaults {
    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperListColors =
        HyperListDefaults.colors(containerColor)

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke =
        HyperListDefaults.border(color)
}
