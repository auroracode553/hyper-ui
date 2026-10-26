/** 文件职责：在 hyper_ui 中负责承载菜单列表容器 HyperMenuGroup。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val MenuListCornerRadius = 18.dp

@Immutable
data class HyperMenuGroupColors(
    val containerColor: Color
)

/**
 * 简单菜单列表。
 *
 * 仅用于少量菜单、设置项和操作入口；页面级动态数据使用 HyperList 或 HyperSectionedList。
 * modifier 控制菜单外壳，contentModifier 控制容器内部内容布局。
 * 组件不提供边框参数；需要外层描边时由调用方通过 modifier 组合。
 */
@Composable
fun <T> HyperMenuGroup(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperMenuGroupDefaults.ContentPadding),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    colors: HyperMenuGroupColors = HyperMenuGroupDefaults.colors(),
    title: String? = null,
    subtitle: String? = null,
    itemContent: @Composable (item: T) -> Unit
) {
    HyperMenuGroupHeading(title, subtitle) {

    val containerColor = resolveHyperContainerColor(colors.containerColor, hyperGlass.surface)
    val shape = HyperMenuGroupDefaults.Shape
    Column(
        modifier = modifier
            .fillMaxWidth()
            .hyperCardSurface(
                containerColor = containerColor,
                shape = shape
            )
            .then(contentModifier),
        verticalArrangement = verticalArrangement
    ) {
        items.forEachIndexed { index, item ->
            if (index > 0) HyperDivider()
            val isFirst = index == 0
            val isLast = index == items.lastIndex
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(menuListItemShape(isFirst, isLast))

            ) {
                CompositionLocalProvider(
                    LocalHyperListTileDividerSuppressed provides true,
                    LocalHyperListTileContainerColor provides containerColor
                ) {
                    itemContent(item)
                }
            }
        }
    }
    }
}

/**
 * DSL 菜单列表入口。
 *
 * 仅用于少量菜单、设置项和操作入口；不推荐用于普通列表或大量数据渲染。
 * modifier 控制菜单外壳，contentModifier 控制容器内部内容布局。
 * 组件不提供边框参数；需要外层描边时由调用方通过 modifier 组合。
 */
@Composable
fun HyperMenuGroup(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperMenuGroupDefaults.ContentPadding),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    colors: HyperMenuGroupColors = HyperMenuGroupDefaults.colors(),
    title: String? = null,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    HyperMenuGroupHeading(title, subtitle) {

    val containerColor = resolveHyperContainerColor(colors.containerColor, hyperGlass.surface)
    CompositionLocalProvider(
        LocalHyperListTileDividerSuppressed provides false,
        LocalHyperListTileContainerColor provides containerColor
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .hyperCardSurface(
                    containerColor = containerColor,
                    shape = HyperMenuGroupDefaults.Shape
                )
                .then(contentModifier),
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
    }
}

object HyperMenuGroupDefaults {
    val Shape: Shape = RoundedCornerShape(MenuListCornerRadius)
    /** 卡片内边距与页面外边距职责独立；组内行不再重复添加水平留白。 */
    val ContentPadding = PaddingValues(horizontal = HyperSpacing.PageHorizontal, vertical = 4.dp)

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperMenuGroupColors = HyperMenuGroupColors(
        containerColor = resolveHyperContainerColor(containerColor, hyperGlass.surface)
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

@Composable
private fun HyperMenuGroupHeading(title: String?, subtitle: String?, child: @Composable () -> Unit) {
    if (title == null && subtitle == null) { child(); return }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (title != null) HyperText(title,
            fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        child()
        if (subtitle != null) HyperText(subtitle,
            color = HyperColors.secondaryText, fontSize = 12.sp, lineHeight = 16.2.sp)
    }
}
