/** 文件职责：提供可横向滚动的 HyperSlideMenu 及其菜单项。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

class HyperSlideMenuItemScope internal constructor(
    val selected: Boolean,
    val enabled: Boolean
)

/**
 * 横向分组菜单。
 *
 * 典型用于页面顶部分类、筛选分组或同级视图切换；组件不内置 label/count 模型，业务内容通过 itemContent slot 传入。
 * 每个菜单项直接复用 HyperButton，菜单只负责横向排列、选中状态和 Tab 语义。
 * 组件本身不添加任何内边距，请通过 modifier.padding(...) 控制外部间距。
 */
@Composable
fun <T> HyperSlideMenu(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperSlideMenuDefaults.ItemGap),
    itemEnabled: (T) -> Boolean = { true },
    selectedTone: HyperButtonTone = HyperButtonTone.Primary,
    unselectedTone: HyperButtonTone = HyperButtonTone.Secondary,
    selectedColors: HyperButtonColors = HyperButtonDefaults.colors(selectedTone),
    unselectedColors: HyperButtonColors = HyperButtonDefaults.colors(unselectedTone),
    selectedBorder: BorderStroke? = HyperButtonDefaults.border(selectedTone),
    unselectedBorder: BorderStroke? = HyperButtonDefaults.border(unselectedTone),
    itemShape: Shape = HyperButtonDefaults.Shape,
    itemContentPadding: PaddingValues = HyperButtonDefaults.ContentPadding,
    itemContent: @Composable HyperSlideMenuItemScope.(item: T) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        items(items) { item ->
            val enabled = itemEnabled(item)
            val selected = item == selectedItem
            val scope = HyperSlideMenuItemScope(selected = selected, enabled = enabled)

            HyperButton(
                modifier = Modifier.semantics { this.selected = selected },
                tone = if (selected) selectedTone else unselectedTone,
                colors = if (selected) selectedColors else unselectedColors,
                border = if (selected) selectedBorder else unselectedBorder,
                shape = itemShape,
                contentPadding = itemContentPadding,
                enabled = enabled,
                role = Role.Tab,
                onClick = { onSelected(item) }
            ) {
                scope.itemContent(item)
            }
        }
    }
}

object HyperSlideMenuDefaults {
    val ItemGap = 8.dp
}
