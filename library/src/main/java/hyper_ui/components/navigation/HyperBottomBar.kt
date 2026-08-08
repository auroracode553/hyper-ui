package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

data class HyperBottomBarItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)

/**
 * 底部导航菜单的横向分布方式。
 */
enum class HyperBottomMenuLayout {
    /**
     * 每个菜单项占用同等宽度槽位，槽内位置由 itemSlotAlignment 控制。
     */
    Equal,

    /**
     * 菜单项直接交给 Row 的 horizontalArrangement 排列。
     */
    Arrangement
}

/**
 * 底部导航栏视觉配置。
 */
data class HyperBottomBarConfig(
    val height: Dp = 70.dp,
    val contentHeight: Dp = 64.dp,
    val horizontalPadding: Dp = 24.dp,
    val itemWidth: Dp = 60.dp,
    val iconSize: Dp = 24.dp,
    val labelFontSize: TextUnit = 12.sp,
    val labelLineHeight: TextUnit = 14.sp,
    val backgroundAlpha: Float = 0.94f,
    val unselectedContentAlpha: Float = 0.72f,
    val horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    val menuLayout: HyperBottomMenuLayout = HyperBottomMenuLayout.Equal,
    val itemSlotAlignment: Alignment = Alignment.Center,
    val bottomBarModifier: Modifier = Modifier,
    val contentModifier: Modifier = Modifier,
    val itemModifier: Modifier = Modifier
)

@Composable
fun HyperBottomBar(
    items: List<HyperBottomBarItem>,
    selectedItemId: String?,
    onItemClick: (HyperBottomBarItem) -> Unit,
    modifier: Modifier = Modifier,
    config: HyperBottomBarConfig = HyperBottomBarConfig()
) {
    val isLight = HyperColors.isLight
    val selectedColor = HyperColors.accent
    val unselectedColor = if (isLight) {
        rgba(0, 0, 0, config.unselectedContentAlpha)
    } else {
        rgba(255, 255, 255, config.unselectedContentAlpha)
    }
    val hasVisibleBackground = HyperColors.elevatedContainer.alpha > 0f
    val highlightModifier = if (hasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(config.bottomBarModifier)
            .fillMaxWidth()
            .height(config.height)
            .background(HyperColors.elevatedContainer)
            .then(highlightModifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(config.contentHeight)
                .padding(horizontal = config.horizontalPadding)
                .then(config.contentModifier),
            horizontalArrangement = if (config.menuLayout == HyperBottomMenuLayout.Equal) {
                Arrangement.Start
            } else {
                config.horizontalArrangement
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = selectedItemId == item.id
                val itemClick = { onItemClick(item) }

                if (config.menuLayout == HyperBottomMenuLayout.Equal) {
                    HyperBottomBarItemContent(
                        item = item,
                        selected = selected,
                        selectedColor = selectedColor,
                        unselectedColor = unselectedColor,
                        config = config,
                        onClick = itemClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(config.contentHeight),
                        contentModifier = config.itemModifier.width(config.itemWidth),
                        contentAlignment = config.itemSlotAlignment
                    )
                } else {
                    HyperBottomBarItemContent(
                        item = item,
                        selected = selected,
                        selectedColor = selectedColor,
                        unselectedColor = unselectedColor,
                        config = config,
                        onClick = itemClick,
                        modifier = config.itemModifier
                            .width(config.itemWidth)
                            .height(config.contentHeight)
                    )
                }
            }
        }
    }
}

@Composable
private fun HyperBottomBarItemContent(
    item: HyperBottomBarItem,
    selected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    config: HyperBottomBarConfig,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center
) {
    val contentColor = if (selected) selectedColor else unselectedColor

    Box(
        modifier = modifier.hyperNoRippleClickable(onClick = onClick),
        contentAlignment = contentAlignment
    ) {
        Column(
            modifier = contentModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = contentColor,
                modifier = Modifier.size(config.iconSize)
            )
            Text(
                text = item.title,
                color = contentColor,
                fontSize = config.labelFontSize,
                lineHeight = config.labelLineHeight,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
