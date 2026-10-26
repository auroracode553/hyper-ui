/** 文件职责：提供按标题分段、逐项懒加载并自动管理组内分割线的页面列表。 */
package hyper_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class HyperSectionedListColors(
    val headerContentColor: Color,
    val dividerColor: Color
)

/**
 * 页面级分段懒列表。
 *
 * 日期等分段标题与数据行保持独立懒加载；组件负责无卡片平铺行与组内分割线，
 * 分组之间固定使用 8dp 紧凑间距，调用方只负责分组数据、稳定 key、标题和单行内容。
 */
@Composable
fun <S, T> HyperSectionedList(
    sections: List<S>,
    items: (S) -> List<T>,
    sectionKey: (S) -> Any,
    itemKey: (S, T) -> Any,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = HyperSectionedListDefaults.ContentPadding,
    firstSectionTopSpacing: Dp = HyperSectionedListDefaults.FirstSectionTopSpacing,
    headerBottomSpacing: Dp = HyperSectionedListDefaults.HeaderBottomSpacing,
    dividerModifier: Modifier = Modifier,
    colors: HyperSectionedListColors = HyperSectionedListDefaults.colors(),
    headerContent: @Composable (section: S) -> Unit,
    itemContent: @Composable (section: S, item: T) -> Unit
) {
    val headerContentColor = resolveHyperContainerColor(colors.headerContentColor, HyperColors.secondaryText)
    val dividerColor = resolveHyperContainerColor(colors.dividerColor, HyperColors.divider)

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = state,
        contentPadding = contentPadding
    ) {
        var visibleSectionIndex = 0

        sections.forEach { section ->
            val sectionItems = items(section)
            if (sectionItems.isEmpty()) return@forEach

            val currentSectionIndex = visibleSectionIndex++
            item(
                key = sectionKey(section),
                contentType = HyperSectionHeaderContentType
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = if (currentSectionIndex == 0) {
                                firstSectionTopSpacing
                            } else {
                                HyperSectionSpacing
                            },
                            bottom = headerBottomSpacing
                        )
                ) {
                    CompositionLocalProvider(
                        LocalHyperContentColor provides headerContentColor,
                        LocalHyperTextStyle provides HyperSectionedListDefaults.HeaderTextStyle
                    ) {
                        headerContent(section)
                    }
                }
            }

            sectionItems.forEachIndexed { itemIndex, sectionItem ->
                item(
                    key = itemKey(section, sectionItem),
                    contentType = HyperSectionItemContentType
                ) {
                    HyperSectionedListItemContainer(
                        itemIndex = itemIndex,
                        lastItemIndex = sectionItems.lastIndex,
                        dividerColor = dividerColor,
                        dividerModifier = dividerModifier
                    ) {
                        itemContent(section, sectionItem)
                    }
                }
            }
        }
    }
}

object HyperSectionedListDefaults {
    val FirstSectionTopSpacing = 0.dp
    val HeaderBottomSpacing = 8.dp
    /** 分组标题和数据行共用调用方传入的页面留白，库不添加水平缩进。 */
    val ContentPadding: PaddingValues = PaddingValues(0.dp)

    val HeaderTextStyle: TextStyle
        @Composable get() = HyperTheme.typography.titleSmall.copy(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Bold
        )

    @Composable
    fun colors(
        headerContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperSectionedListColors {
        return HyperSectionedListColors(
            headerContentColor = resolveHyperContainerColor(headerContentColor, HyperColors.secondaryText),
            dividerColor = resolveHyperContainerColor(dividerColor, HyperColors.divider)
        )
    }
}

@Composable
private fun HyperSectionedListItemContainer(
    itemIndex: Int,
    lastItemIndex: Int,
    dividerColor: Color,
    dividerModifier: Modifier,
    content: @Composable () -> Unit
) {
    val isLastItem = itemIndex == lastItemIndex
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CompositionLocalProvider(
            // 分段容器统一绘制分割线，避免行组件重复绘制。
            LocalHyperListTileDividerSuppressed provides true,
            LocalHyperListTileContainerColor provides Color.Transparent,
            content = content
        )
        if (!isLastItem) {
            HyperListDivider(
                modifier = dividerModifier,
                color = dividerColor
            )
        }
    }
}

private val HyperSectionSpacing = 8.dp

private object HyperSectionHeaderContentType
private object HyperSectionItemContentType
