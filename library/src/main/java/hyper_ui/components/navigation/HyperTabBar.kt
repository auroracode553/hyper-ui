/** 文件职责：提供 HyperTabBar 容器、项目布局与主题颜色。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

/** 标签栏样式。 */
enum class HyperTabBarType {
    /** 贴底样式：0.5dp 顶部发丝线、无阴影的贴底容器（默认）。 */
    Docked,

    /** 悬浮样式：玻璃胶囊容器、可拖动水珠托盘与释放速度吸附反馈。 */
    Floating
}

enum class HyperTabBarItemLayout {
    Equal,
    Packed
}

@Immutable
data class HyperTabBarColors(
    val containerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContentColor: Color
)

class HyperTabBarItemScope internal constructor(
    val selected: Boolean,
    val enabled: Boolean,
    /** 0f~1f 的选中强度；贴底样式恒为 0f/1f，悬浮样式随指示胶囊位置连续变化。 */
    val selectionStrength: Float
)

/**
 * 底部导航栏组件（Slot 模式）。
 *
 * 组件内部已包含默认水平内容间距（16dp）和少量底部安全留白。
 * 外部间距请通过 modifier.padding(...) 控制。
 *
 * [type] 为 [HyperTabBarType.Floating] 时渲染悬浮玻璃胶囊容器：不使用 [topDivider]、
 * [shape] 与贴底留白，选中指示胶囊由调用方在 slot 中自行绘制。
 */
@Composable
fun HyperTabBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    shape: Shape = HyperTabBarDefaults.Shape,
    topDivider: BorderStroke? = HyperTabBarDefaults.topDivider(),
    colors: HyperTabBarColors = HyperTabBarDefaults.colors(),
    type: HyperTabBarType = HyperTabBarType.Docked,
    floatingColors: HyperFloatingTabBarColors = HyperFloatingTabBarDefaults.colors(),
    content: @Composable RowScope.() -> Unit
) {
    if (type == HyperTabBarType.Floating) {
        HyperFloatingTabBar(
            modifier = modifier,
            enabled = enabled,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            colors = floatingColors,
            content = content
        )
        return
    }
    val resolvedColors = colors
    val contentColor = if (enabled) {
        resolvedColors.unselectedContentColor
    } else {
        resolvedColors.disabledContentColor
    }

    HyperTabBarSurface(
        modifier = modifier,
        shape = shape,
        topDivider = topDivider,
        colors = resolvedColors
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(HyperTabBarDefaults.Height)
                .padding(horizontal = 16.dp),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            // Slot 模式只提供底栏外壳和默认内容色；具体点击、选中与禁用逻辑由调用方组合。
            CompositionLocalProvider(
                LocalHyperContentColor provides contentColor,
                LocalHyperTextStyle provides HyperTabBarDefaults.ItemTextStyle
            ) {
                content()
            }
        }
    }
}

/**
 * 底部导航栏组件（简单 items 模式）。
 *
 * 组件内部已包含默认水平内容间距（16dp）和少量底部安全留白。
 * 外部间距请通过 modifier.padding(...) 控制。
 *
 * [type] 为 [HyperTabBarType.Floating] 时渲染悬浮玻璃胶囊：忽略 [itemLayout]、
 * [itemSlotAlignment]、[horizontalArrangement]、[shape]、[topDivider] 与 [colors]，
 * 使用 [floatingColors]；项目等分宽度，指示胶囊随选中与按压状态滑动、加深。
 */
@Composable
fun <T> HyperTabBar(
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemLayout: HyperTabBarItemLayout = HyperTabBarItemLayout.Equal,
    itemSelected: (T) -> Boolean = { false },
    itemSlotAlignment: Alignment = Alignment.Center,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    shape: Shape = HyperTabBarDefaults.Shape,
    topDivider: BorderStroke? = HyperTabBarDefaults.topDivider(),
    colors: HyperTabBarColors = HyperTabBarDefaults.colors(),
    type: HyperTabBarType = HyperTabBarType.Docked,
    floatingColors: HyperFloatingTabBarColors = HyperFloatingTabBarDefaults.colors(),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
) {
    if (type == HyperTabBarType.Floating) {
        HyperFloatingTabBar(
            items = items,
            onItemClick = onItemClick,
            modifier = modifier,
            enabled = enabled,
            itemSelected = itemSelected,
            itemEnabled = itemEnabled,
            colors = floatingColors,
            itemContent = itemContent
        )
        return
    }
    val resolvedColors = colors
    HyperTabBar(
        modifier = modifier,
        enabled = enabled,
        horizontalArrangement = if (itemLayout == HyperTabBarItemLayout.Equal) {
            Arrangement.Start
        } else {
            horizontalArrangement
        },
        shape = shape,
        topDivider = topDivider,
        colors = resolvedColors
    ) {
        items.forEach { item ->
            val selected = itemSelected(item)
            val actualEnabled = enabled && itemEnabled(item)
            val scope = HyperTabBarItemScope(
                selected = selected,
                enabled = actualEnabled,
                selectionStrength = if (selected) 1f else 0f
            )
            val contentColor = when {
                !actualEnabled -> resolvedColors.disabledContentColor
                selected -> resolvedColors.selectedContentColor
                else -> resolvedColors.unselectedContentColor
            }

            if (itemLayout == HyperTabBarItemLayout.Equal) {
                HyperTabBarItemContainer(
                    onClick = { onItemClick(item) },
                    enabled = actualEnabled,
                    contentColor = contentColor,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = itemSlotAlignment
                ) {
                    scope.itemContent(item)
                }
            } else {
                HyperTabBarItemContainer(
                    onClick = { onItemClick(item) },
                    enabled = actualEnabled,
                    contentColor = contentColor,
                    modifier = Modifier
                        .widthIn(min = HyperTabBarDefaults.ItemWidth)
                        .fillMaxHeight(),
                    contentAlignment = itemSlotAlignment
                ) {
                    scope.itemContent(item)
                }
            }
        }
    }
}

@Composable
private fun HyperTabBarSurface(
    modifier: Modifier,
    shape: Shape,
    topDivider: BorderStroke?,
    colors: HyperTabBarColors,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .hyperSurface(
                containerColor = colors.containerColor,
                shape = shape
            )
            .padding(bottom = HyperTabBarDefaults.BottomPadding)
    ) {
        content()
        if (topDivider != null) {
            // 贴底栏只需要与上方内容分层；整框描边会形成不必要的卡片感。
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(topDivider.width)
                    .background(brush = topDivider.brush)
            )
        }
    }
}

@Composable
private fun RowScope.HyperTabBarItemContainer(
    onClick: () -> Unit,
    enabled: Boolean,
    contentColor: Color,
    modifier: Modifier,
    contentAlignment: Alignment,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.hyperNoRippleClickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick
        ),
        contentAlignment = contentAlignment
    ) {
        CompositionLocalProvider(LocalHyperContentColor provides contentColor) {
            content()
        }
    }
}

object HyperTabBarDefaults {
    /** 标签操作区高度。 */
    val Height = 55.dp

    /** 轻量底部留白，用于让标签内容与系统手势条保持少量距离。 */
    val BottomPadding = 5.dp

    /** 顶部分隔线使用半 dp 发丝线，避免在高密度屏幕上形成厚重边框。 */
    val TopDividerThickness = 0.5.dp
    val ItemWidth = 60.dp
    val Shape: Shape = RoundedCornerShape(0.dp)

    val ItemTextStyle: TextStyle
        @Composable get() = HyperTheme.typography.labelSmall

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperTabBarColors {
        val defaultUnselectedColor = if (HyperColors.isLight) {
            rgba(0, 0, 0, 0.72f)
        } else {
            rgba(255, 255, 255, 0.72f)
        }
        val resolvedSelectedColor = resolveHyperContainerColor(selectedContentColor, HyperColors.accent)
        val resolvedUnselectedColor = resolveHyperContainerColor(unselectedContentColor, defaultUnselectedColor)

        return HyperTabBarColors(
            containerColor = resolveHyperContainerColor(
                containerColor,
                defaultHyperTabBarContainerColor()
            ),
            selectedContentColor = resolvedSelectedColor,
            unselectedContentColor = resolvedUnselectedColor,
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                resolvedUnselectedColor.copy(alpha = HyperStyleDefaults.DisabledAlpha)
            )
        )
    }

    @Composable
    fun topDivider(color: Color = Color.Unspecified): BorderStroke = BorderStroke(
        width = TopDividerThickness,
        color = resolveHyperContainerColor(
            color,
            if (HyperColors.isLight) {
                rgba(0, 0, 0, 0.055f)
            } else {
                HyperColors.pageBackground
            }
        )
    )
}

/** 浅色保留轻量透明度；深色与页面背景完全一致。 */
@Composable
private fun defaultHyperTabBarContainerColor(): Color = hyperPageMatchedContainerColor(
    lightContainerColor = rgba(255, 255, 255, 0.92f)
)
