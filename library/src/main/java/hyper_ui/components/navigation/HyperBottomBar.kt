/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/navigation/HyperBottomBar 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

enum class HyperBottomBarItemLayout {
    Equal,
    Packed
}

@Immutable
data class HyperBottomBarColors(
    val containerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContentColor: Color
)

class HyperBottomBarItemScope internal constructor(
    val selected: Boolean,
    val enabled: Boolean
)

/**
 * 底部导航栏组件（Slot 模式）。
 *
 * 组件内部已包含默认水平内容间距（16dp），外部间距请通过 modifier.padding(...) 控制。
 */
@Composable
fun HyperBottomBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = HyperBottomBarDefaults.Height,
    contentHeight: Dp = HyperBottomBarDefaults.ContentHeight,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    shape: Shape = HyperBottomBarDefaults.Shape,
    border: BorderStroke? = HyperBottomBarDefaults.border(),
    colors: HyperBottomBarColors = HyperBottomBarDefaults.colors(),
    content: @Composable RowScope.() -> Unit
) {
    val resolvedColors = resolveHyperBottomBarColors(colors)
    val contentColor = if (enabled) {
        resolvedColors.unselectedContentColor
    } else {
        resolvedColors.disabledContentColor
    }

    HyperBottomBarSurface(
        modifier = modifier,
        height = height,
        shape = shape,
        border = border,
        colors = resolvedColors
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(contentHeight)
                .padding(horizontal = 16.dp),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            // Slot 模式只提供底栏外壳和默认内容色；具体点击、选中与禁用逻辑由调用方组合。
            CompositionLocalProvider(
                LocalContentColor provides contentColor,
                LocalTextStyle provides HyperBottomBarDefaults.ItemTextStyle
            ) {
                content()
            }
        }
    }
}

/**
 * 底部导航栏组件（简单 items 模式）。
 *
 * 组件内部已包含默认水平内容间距（16dp），外部间距请通过 modifier.padding(...) 控制。
 */
@Composable
fun <T> HyperBottomBar(
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemLayout: HyperBottomBarItemLayout = HyperBottomBarItemLayout.Equal,
    itemSelected: (T) -> Boolean = { false },
    height: Dp = HyperBottomBarDefaults.Height,
    contentHeight: Dp = HyperBottomBarDefaults.ContentHeight,
    itemWidth: Dp = HyperBottomBarDefaults.ItemWidth,
    itemSlotAlignment: Alignment = Alignment.Center,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    shape: Shape = HyperBottomBarDefaults.Shape,
    border: BorderStroke? = HyperBottomBarDefaults.border(),
    colors: HyperBottomBarColors = HyperBottomBarDefaults.colors(),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperBottomBarItemScope.(item: T) -> Unit
) {
    val resolvedColors = resolveHyperBottomBarColors(colors)
    HyperBottomBar(
        modifier = modifier,
        enabled = enabled,
        height = height,
        contentHeight = contentHeight,
        horizontalArrangement = if (itemLayout == HyperBottomBarItemLayout.Equal) {
            Arrangement.Start
        } else {
            horizontalArrangement
        },
        shape = shape,
        border = border,
        colors = resolvedColors
    ) {
        items.forEach { item ->
            val selected = itemSelected(item)
            val actualEnabled = enabled && itemEnabled(item)
            val scope = HyperBottomBarItemScope(
                selected = selected,
                enabled = actualEnabled
            )
            val contentColor = when {
                !actualEnabled -> resolvedColors.disabledContentColor
                selected -> resolvedColors.selectedContentColor
                else -> resolvedColors.unselectedContentColor
            }

            if (itemLayout == HyperBottomBarItemLayout.Equal) {
                HyperBottomBarItemContainer(
                    onClick = { onItemClick(item) },
                    enabled = actualEnabled,
                    contentColor = contentColor,
                    modifier = Modifier
                        .weight(1f)
                        .height(contentHeight),
                    contentAlignment = itemSlotAlignment
                ) {
                    Box(
                        modifier = Modifier.width(itemWidth),
                        contentAlignment = Alignment.Center
                    ) {
                        scope.itemContent(item)
                    }
                }
            } else {
                HyperBottomBarItemContainer(
                    onClick = { onItemClick(item) },
                    enabled = actualEnabled,
                    contentColor = contentColor,
                    modifier = Modifier
                        .width(itemWidth)
                        .height(contentHeight),
                    contentAlignment = itemSlotAlignment
                ) {
                    scope.itemContent(item)
                }
            }
        }
    }
}

@Composable
private fun HyperBottomBarSurface(
    modifier: Modifier,
    height: Dp,
    shape: Shape,
    border: BorderStroke?,
    colors: HyperBottomBarColors,
    content: @Composable BoxScope.() -> Unit
) {
    val sizedModifier = modifier
        .fillMaxWidth()
        .height(height)
    val surfaceModifier = if (HyperColors.isLight) {
        sizedModifier.hyperGlassSurface(
            containerColor = colors.containerColor,
            shape = shape,
            border = border
        )
    } else {
        sizedModifier.hyperSolidSurface(
            containerColor = colors.containerColor,
            shape = shape,
            border = border
        )
    }
    Box(modifier = surfaceModifier) {
        content()
    }
}

@Composable
private fun resolveHyperBottomBarColors(colors: HyperBottomBarColors): HyperBottomBarColors {
    if (HyperColors.isLight) {
        return colors
    }

    val containerColor = resolveHyperOpaqueColor(
        color = colors.containerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = HyperColors.pageBackground
    )
    return HyperBottomBarColors(
        containerColor = containerColor,
        selectedContentColor = resolveHyperOpaqueColor(
            color = colors.selectedContentColor,
            fallbackColor = HyperColors.accent,
            backgroundColor = containerColor
        ),
        unselectedContentColor = resolveHyperOpaqueColor(
            color = colors.unselectedContentColor,
            fallbackColor = HyperColors.secondaryText,
            backgroundColor = containerColor
        ),
        disabledContentColor = resolveHyperOpaqueColor(
            color = colors.disabledContentColor,
            fallbackColor = HyperColors.secondaryText,
            backgroundColor = containerColor
        )
    )
}

@Composable
private fun RowScope.HyperBottomBarItemContainer(
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
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

object HyperBottomBarDefaults {
    val Height = 70.dp
    val ContentHeight = 64.dp
    val ItemWidth = 60.dp
    val Shape: Shape = RoundedCornerShape(0.dp)

    val ItemTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.labelSmall

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperBottomBarColors {
        val isLight = HyperColors.isLight
        val defaultUnselectedColor = if (isLight) {
            rgba(0, 0, 0, 0.72f)
        } else {
            rgba(255, 255, 255, 0.72f)
        }
        val resolvedSelectedColor = resolveHyperContainerColor(selectedContentColor, HyperColors.accent)
        val resolvedUnselectedColor = resolveHyperContainerColor(unselectedContentColor, defaultUnselectedColor)

        return resolveHyperBottomBarColors(
            HyperBottomBarColors(
                containerColor = resolveHyperContainerColor(
                    containerColor,
                    if (isLight) HyperColors.elevatedContainer else HyperColors.cardContainer
                ),
                selectedContentColor = resolvedSelectedColor,
                unselectedContentColor = resolvedUnselectedColor,
                disabledContentColor = resolveHyperContainerColor(
                    disabledContentColor,
                    resolvedUnselectedColor.copy(alpha = HyperStyleDefaults.DisabledAlpha)
                )
            )
        )
    }

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = if (HyperColors.isLight) {
        hyperPanelBorder(color)
    } else {
        hyperSolidPanelBorder(
            color = color,
            backgroundColor = HyperColors.cardContainer
        )
    }
}
