/** 文件职责：提供可横向滚动的 HyperSlideMenu 及其菜单项。 */
package hyper_ui

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
data class HyperSlideMenuColors(
    val selectedContainerColor: Color,
    val unselectedContainerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

class HyperSlideMenuItemScope internal constructor(
    val selected: Boolean,
    val enabled: Boolean
)

/**
 * 分组菜单单项容器。
 *
 * UI 库只负责选中/禁用视觉、点击边界和基础布局；文字、计数、图标等业务内容由 content slot 渲染。
 */
@Composable
fun HyperSlideMenuItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperSlideMenuDefaults.ItemContentPadding),
    enabled: Boolean = true,
    shape: Shape = HyperSlideMenuDefaults.Shape,
    colors: HyperSlideMenuColors = HyperSlideMenuDefaults.colors(),
    role: Role = Role.Tab,
    content: @Composable HyperSlideMenuItemScope.() -> Unit
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
    val scope = HyperSlideMenuItemScope(selected = selected, enabled = enabled)

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = HyperSlideMenuDefaults.MinHeight)
            .hyperGlassSurface(
                shape = shape,
                visuals = hyperGlassSurfaceVisuals(
                    containerColor = targetContainerColor,
                    elevation = when {
                        !enabled -> 0.dp
                        selected -> HyperSlideMenuDefaults.SelectedElevation
                        else -> HyperSlideMenuDefaults.RestingElevation
                    },
                    topLightAlpha = when {
                        !enabled -> 0.08f
                        HyperColors.isLight -> 0.34f
                        else -> 0.12f
                    },
                    bottomShadeAlpha = if (HyperColors.isLight) 0.035f else 0.11f,
                    shadowAlpha = if (HyperColors.isLight) 0.13f else 0.27f
                )
            )
            .hyperNoRippleClickable(
                enabled = enabled,
                role = role,
                onClick = onClick
            )
            .then(contentModifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HyperSlideMenuDefaults.ItemContentGap)
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
fun <T> HyperSlideMenu(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperSlideMenuDefaults.ItemGap),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperSlideMenuItemScope.(item: T) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        items(items) { item ->
            val enabled = itemEnabled(item)
            HyperSlideMenuItem(
                selected = item == selectedItem,
                enabled = enabled,
                onClick = { onSelected(item) }
            ) {
                itemContent(item)
            }
        }
    }
}

object HyperSlideMenuDefaults {
    val MinHeight = 32.dp
    val ItemContentGap = 6.dp
    val ItemGap = 8.dp
    val ItemContentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
    val RestingElevation = 1.dp
    val SelectedElevation = 3.dp
    val Shape: Shape = RoundedCornerShape(percent = 50)

    @Composable
    fun colors(
        selectedContainerColor: Color = Color.Unspecified,
        unselectedContainerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperSlideMenuColors {
        val resolvedUnselectedContentColor = resolveHyperContainerColor(
            unselectedContentColor,
            HyperColors.primaryText
        )

        return HyperSlideMenuColors(
            selectedContainerColor = resolveHyperContainerColor(
                selectedContainerColor,
                HyperColors.accent.copy(alpha = if (HyperColors.isLight) 0.82f else 0.68f)
            ),
            unselectedContainerColor = resolveHyperContainerColor(
                unselectedContainerColor,
                Color(1f, 1f, 1f, if (HyperColors.isLight) 0.62f else 0.16f)
            ),
            selectedContentColor = resolveHyperContainerColor(
                selectedContentColor,
                rgba(255, 255, 255, 1f)
            ),
            unselectedContentColor = resolvedUnselectedContentColor,
            disabledContainerColor = resolveHyperContainerColor(
                disabledContainerColor,
                Color(1f, 1f, 1f, if (HyperColors.isLight) 0.26f else 0.07f)
            ),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                HyperColors.disabledText
            )
        )
    }
}
