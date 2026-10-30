/** 横向分类筛选栏：独立紧凑选项，不绘制整条胶囊底板，也不占用页面滚动视口。 */
package hyper_ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

class HyperFilterBarItemScope internal constructor(val selected: Boolean, val enabled: Boolean)

/** 业务持有选中索引，组件仅管理横向滚动；内容与字体可自然撑开最小高度。 */
@Composable
fun <T> HyperFilterBar(
    items: List<T>,
    selectedIndex: Int,
    onChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperFilterBarItemScope.(T) -> Unit,
) {
    require(selectedIndex in items.indices) { "selectedIndex 超出 items 范围" }
    val scrollState = rememberLazyListState()
    // 外部切换或恢复筛选时显示当前项；点击已完整可见的选项不重置横向位置。
    LaunchedEffect(selectedIndex, items.size) {
        val layout = scrollState.layoutInfo
        val selected = layout.visibleItemsInfo.firstOrNull { it.index == selectedIndex }
        if (selected == null || selected.offset < layout.viewportStartOffset ||
            selected.offset + selected.size > layout.viewportEndOffset) {
            scrollState.animateScrollToItem(selectedIndex)
        }
    }
    LazyRow(
        state = scrollState,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HyperFilterBarDefaults.ItemSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        itemsIndexed(items) { index, item ->
            val selected = index == selectedIndex
            val actualEnabled = enabled && itemEnabled(item)
            val background by animateColorAsState(
                if (selected) hyperGlass.selection else Color.Transparent,
                tween(HyperMotionSpec.StateDuration, easing = HyperMotionSpec.EaseOutCubic),
                label = "HyperFilterBarSelection",
            )
            Box(
                Modifier.defaultMinSize(minWidth = 48.dp, minHeight = HyperFilterBarDefaults.MinHeight)
                    .alpha(if (actualEnabled) 1f else 0.4f)
                    .background(background, RoundedCornerShape(percent = 50))
                    .hyperNoRippleClickable(enabled = actualEnabled,
                        onClick = { if (index != selectedIndex) onChanged(index) })
                    .padding(HyperFilterBarDefaults.ItemPadding),
                contentAlignment = Alignment.Center,
            ) {
                CompositionLocalProvider(
                    LocalHyperContentColor provides if (selected) HyperColors.primaryText else HyperColors.secondaryText,
                    LocalHyperTextStyle provides HyperTheme.typography.bodyMedium.copy(fontSize = 13.sp,
                        lineHeight = 18.2.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium),
                    LocalHyperIconSize provides 16.dp,
                ) { HyperFilterBarItemScope(selected, actualEnabled).itemContent(item) }
            }
        }
    }
}

object HyperFilterBarDefaults {
    val MinHeight = 32.dp
    val ItemSpacing = 6.dp
    val ItemPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
}
