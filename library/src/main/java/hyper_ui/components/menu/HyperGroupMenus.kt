/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/menu/HyperGroupMenus 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperGroupMenusColors(
    val selectedContainerColor: Color,
    val unselectedContainerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

class HyperGroupMenusItemScope internal constructor(
    val selected: Boolean,
    val enabled: Boolean
)

/**
 * 分组菜单单项容器。
 *
 * UI 库只负责选中/禁用视觉、点击边界和基础布局；文字、计数、图标等业务内容由 content slot 渲染。
 */
@Composable
fun HyperGroupMenuItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperGroupMenusDefaults.ItemContentPadding),
    enabled: Boolean = true,
    shape: Shape = HyperGroupMenusDefaults.Shape,
    colors: HyperGroupMenusColors = HyperGroupMenusDefaults.colors(),
    role: Role = Role.Tab,
    content: @Composable HyperGroupMenusItemScope.() -> Unit
) {
    val targetContainerColor = when {
        !enabled -> colors.disabledContainerColor
        selected -> colors.selectedContainerColor
        else -> colors.unselectedContainerColor
    }
    val targetContentColor = when {
        !enabled -> colors.disabledContentColor
        selected -> colors.selectedContentColor
        else -> colors.unselectedContentColor
    }
    val targetBorderColor = if (enabled) HyperColors.fieldBorder else HyperColors.divider
    val scope = HyperGroupMenusItemScope(selected = selected, enabled = enabled)

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = HyperGroupMenusDefaults.MinHeight)
            .hyperSurface(
                containerColor = targetContainerColor,
                shape = shape,
                border = if (selected) null else BorderStroke(
                    width = HyperGroupMenusDefaults.ItemBorderWidth,
                    color = targetBorderColor
                )
            )
            .hyperNoRippleClickable(
                enabled = enabled,
                role = role,
                onClick = onClick
            )
            .then(contentModifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HyperGroupMenusDefaults.ItemContentGap)
    ) {
        CompositionLocalProvider(LocalContentColor provides targetContentColor) {
            scope.content()
        }
    }
}

/**
 * 横向分组菜单。
 *
 * 典型用于页面顶部分类、筛选分组或同级视图切换；组件不内置 label/count 模型，业务内容通过 itemContent slot 传入。
 * 组件本身不添加任何内边距，请通过 modifier.padding(...) 控制外部间距。
 */
@Composable
fun <T> HyperGroupMenus(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperGroupMenusDefaults.ItemGap),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperGroupMenusItemScope.(item: T) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        items(items) { item ->
            val enabled = itemEnabled(item)
            HyperGroupMenuItem(
                selected = item == selectedItem,
                enabled = enabled,
                onClick = { onSelected(item) }
            ) {
                itemContent(item)
            }
        }
    }
}

object HyperGroupMenusDefaults {
    val MinHeight = 32.dp
    val ItemContentGap = 6.dp
    val ItemGap = 8.dp
    val ItemContentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
    val ItemBorderWidth = 1.dp
    val Shape: Shape = RoundedCornerShape(percent = 50)

    @Composable
    fun colors(
        selectedContainerColor: Color = Color.Unspecified,
        unselectedContainerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperGroupMenusColors {
        val resolvedUnselectedContentColor = resolveHyperContainerColor(
            unselectedContentColor,
            HyperColors.primaryText
        )

        return HyperGroupMenusColors(
            selectedContainerColor = resolveHyperContainerColor(selectedContainerColor, HyperColors.accent),
            unselectedContainerColor = resolveHyperContainerColor(
                unselectedContainerColor,
                HyperColors.elevatedContainer
            ),
            selectedContentColor = resolveHyperContainerColor(
                selectedContentColor,
                rgba(255, 255, 255, 1f)
            ),
            unselectedContentColor = resolvedUnselectedContentColor,
            disabledContainerColor = resolveHyperContainerColor(disabledContainerColor, HyperColors.disabledContainer),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                HyperColors.disabledText
            )
        )
    }
}
