/** 文件职责：提供按标题分段、逐项懒加载并自动管理组内圆角和分割线的页面列表。 */
package hyper_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class HyperSectionedListColors(
    val headerContentColor: Color,
    val itemContainerColor: Color,
    val dividerColor: Color
)

/**
 * 页面级分段懒列表。
 *
 * 日期等分段标题与数据行保持独立懒加载；组件负责每组卡片背景、首尾圆角和分割线，
 * 调用方只负责分组数据、稳定 key、标题和单行内容。
 */
@Composable
fun <S, T> HyperSectionedList(
    sections: List<S>,
    items: (S) -> List<T>,
    sectionKey: (S) -> Any,
    itemKey: (S, T) -> Any,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    firstSectionTopSpacing: Dp = HyperSectionedListDefaults.FirstSectionTopSpacing,
    sectionSpacing: Dp = HyperSectionedListDefaults.SectionSpacing,
    headerBottomSpacing: Dp = HyperSectionedListDefaults.HeaderBottomSpacing,
    dividerModifier: Modifier = Modifier.padding(
        start = HyperSectionedListDefaults.DividerInset
    ),
    colors: HyperSectionedListColors = HyperSectionedListDefaults.colors(),
    headerContent: @Composable (section: S) -> Unit,
    itemContent: @Composable (section: S, item: T) -> Unit
) {
    val pageBackground = HyperColors.pageBackground
    val itemContainerColor = resolveHyperOpaqueColor(
        color = colors.itemContainerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = pageBackground
    )
    val headerContentColor = resolveHyperOpaqueColor(
        color = colors.headerContentColor,
        fallbackColor = HyperColors.secondaryText,
        backgroundColor = pageBackground
    )
    val dividerColor = resolveHyperOpaqueColor(
        color = colors.dividerColor,
        fallbackColor = HyperColors.divider,
        backgroundColor = itemContainerColor
    )

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
                                sectionSpacing
                            },
                            bottom = headerBottomSpacing
                        )
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides headerContentColor,
                        LocalTextStyle provides HyperSectionedListDefaults.HeaderTextStyle
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
                        containerColor = itemContainerColor,
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
    val SectionSpacing = 16.dp
    val HeaderBottomSpacing = 8.dp
    val DividerInset = HyperListItemDefaults.DividerInset

    val HeaderTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.titleSmall.copy(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

    @Composable
    fun colors(
        headerContentColor: Color = Color.Unspecified,
        itemContainerColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperSectionedListColors {
        val resolvedItemContainerColor = resolveHyperOpaqueColor(
            color = itemContainerColor,
            fallbackColor = HyperColors.cardContainer,
            backgroundColor = HyperColors.pageBackground
        )
        return HyperSectionedListColors(
            headerContentColor = resolveHyperOpaqueColor(
                color = headerContentColor,
                fallbackColor = HyperColors.secondaryText,
                backgroundColor = HyperColors.pageBackground
            ),
            itemContainerColor = resolvedItemContainerColor,
            dividerColor = resolveHyperOpaqueColor(
                color = dividerColor,
                fallbackColor = HyperColors.divider,
                backgroundColor = resolvedItemContainerColor
            )
        )
    }
}

@Composable
private fun HyperSectionedListItemContainer(
    itemIndex: Int,
    lastItemIndex: Int,
    containerColor: Color,
    dividerColor: Color,
    dividerModifier: Modifier,
    content: @Composable () -> Unit
) {
    val isLastItem = itemIndex == lastItemIndex
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .hyperSolidSurface(
                containerColor = containerColor,
                shape = sectionItemShape(itemIndex, lastItemIndex)
            )
    ) {
        CompositionLocalProvider(
            // 分段容器统一绘制分割线，避免行组件重复绘制或越过组内圆角边界。
            LocalHyperListItemDividerSuppressed provides true,
            LocalHyperListItemContainerColor provides containerColor,
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

private fun sectionItemShape(itemIndex: Int, lastItemIndex: Int): Shape {
    val radius = HyperStyleDefaults.MediumCornerRadius
    return when {
        lastItemIndex == 0 -> RoundedCornerShape(radius)
        itemIndex == 0 -> RoundedCornerShape(
            topStart = radius,
            topEnd = radius,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        )
        itemIndex == lastItemIndex -> RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = radius,
            bottomStart = radius
        )
        else -> RoundedCornerShape(0.dp)
    }
}

private object HyperSectionHeaderContentType
private object HyperSectionItemContentType
