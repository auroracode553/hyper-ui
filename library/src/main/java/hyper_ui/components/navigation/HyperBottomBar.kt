package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

@Composable
fun HyperBottomBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = HyperBottomBarDefaults.Height,
    contentHeight: Dp = HyperBottomBarDefaults.ContentHeight,
    contentPadding: PaddingValues = HyperBottomBarDefaults.ContentPadding,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    shape: Shape = HyperBottomBarDefaults.Shape,
    border: BorderStroke? = null,
    colors: HyperBottomBarColors = HyperBottomBarDefaults.colors(),
    content: @Composable RowScope.() -> Unit
) {
    val contentColor = if (enabled) {
        colors.unselectedContentColor
    } else {
        colors.disabledContentColor
    }

    HyperBottomBarSurface(
        modifier = modifier,
        height = height,
        shape = shape,
        border = border,
        colors = colors
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(contentHeight)
                .padding(contentPadding),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            // Slot 模式只提供底栏外壳和默认内容色；具体点击、选中与禁用逻辑由调用方组合。
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                content()
            }
        }
    }
}

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
    contentPadding: PaddingValues = HyperBottomBarDefaults.ContentPadding,
    itemWidth: Dp = HyperBottomBarDefaults.ItemWidth,
    itemSlotAlignment: Alignment = Alignment.Center,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    shape: Shape = HyperBottomBarDefaults.Shape,
    border: BorderStroke? = null,
    colors: HyperBottomBarColors = HyperBottomBarDefaults.colors(),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperBottomBarItemScope.(item: T) -> Unit
) {
    HyperBottomBar(
        modifier = modifier,
        enabled = enabled,
        height = height,
        contentHeight = contentHeight,
        contentPadding = contentPadding,
        horizontalArrangement = if (itemLayout == HyperBottomBarItemLayout.Equal) {
            Arrangement.Start
        } else {
            horizontalArrangement
        },
        shape = shape,
        border = border,
        colors = colors
    ) {
        items.forEach { item ->
            val selected = itemSelected(item)
            val actualEnabled = enabled && itemEnabled(item)
            val scope = HyperBottomBarItemScope(
                selected = selected,
                enabled = actualEnabled
            )
            val contentColor = when {
                !actualEnabled -> colors.disabledContentColor
                selected -> colors.selectedContentColor
                else -> colors.unselectedContentColor
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .hyperGlassSurface(
                containerColor = colors.containerColor,
                shape = shape,
                border = border
            )
    ) {
        content()
    }
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
    val ContentPadding = PaddingValues(horizontal = 24.dp)
    val ItemWidth = 60.dp
    val Shape: Shape = RoundedCornerShape(0.dp)

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperBottomBarColors {
        val defaultUnselectedColor = if (HyperColors.isLight) {
            rgba(0, 0, 0, 0.72f)
        } else {
            rgba(255, 255, 255, 0.72f)
        }
        val resolvedSelectedColor = resolveHyperContainerColor(selectedContentColor, HyperColors.accent)
        val resolvedUnselectedColor = resolveHyperContainerColor(unselectedContentColor, defaultUnselectedColor)

        return HyperBottomBarColors(
            containerColor = resolveHyperContainerColor(containerColor, HyperColors.elevatedContainer),
            selectedContentColor = resolvedSelectedColor,
            unselectedContentColor = resolvedUnselectedColor,
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                resolvedUnselectedColor.copy(alpha = HyperStyleDefaults.DisabledAlpha)
            )
        )
    }
}
