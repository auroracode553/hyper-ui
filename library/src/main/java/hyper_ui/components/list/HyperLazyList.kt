/** 文件职责：在 hyper_ui 中负责承载页面级懒加载列表 HyperLazyList，并集中维护其视觉默认值。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Immutable
data class HyperLazyListColors(
    val containerColor: Color
)

@Composable
fun <T> HyperLazyList(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = null,
    colors: HyperLazyListColors = HyperLazyListDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
) {
    val containerColor = colors.containerColor
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .then(if (border != null) Modifier.border(border, RectangleShape) else Modifier),
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement
    ) {
        itemsIndexed(
            items = items,
            key = key?.let { itemKey ->
                { _: Int, item: T -> itemKey(item) }
            }
        ) { index, item ->
            val isLast = index == items.lastIndex
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
 * 面向异构项目、分组和分页内容的懒列表入口。
 *
 * 列表状态与内容 DSL 由调用方持有，容器只提供页面级平铺背景，不添加圆角。
 */
@Composable
fun HyperLazyList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = null,
    colors: HyperLazyListColors = HyperLazyListDefaults.colors(),
    content: LazyListScope.() -> Unit
) {
    val containerColor = colors.containerColor
    CompositionLocalProvider(LocalHyperListItemDividerSuppressed provides false) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .background(containerColor)
                .then(if (border != null) Modifier.border(border, RectangleShape) else Modifier),
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

object HyperLazyListDefaults {
    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperLazyListColors = HyperLazyListColors(
        containerColor = resolveHyperContainerColor(
            containerColor = containerColor,
            fallbackColor = HyperColors.cardContainer
        )
    )

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = hyperPanelBorder(color)
}
