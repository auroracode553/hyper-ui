/** 文件职责：参考 HyperTabs 的共享胶囊、等宽/滚动标签与受控选中状态。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

class HyperTabsItemScope internal constructor(val selected: Boolean, val enabled: Boolean)

@Composable
fun <T> HyperTabs(
    tabs: List<T>,
    selectedIndex: Int,
    onChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabsItemScope.(item: T) -> Unit
) {
    require(selectedIndex in tabs.indices) { "selectedIndex 超出 tabs 范围" }
    val shape = RoundedCornerShape(27.dp)
    Row(modifier.fillMaxWidth().height(HyperTabsDefaults.Height)
        .hyperFrostedSurface(hyperGlass.surfaceSubtle, shape, elevation = 0.dp)
        .padding(3.dp)
        .then(if (scrollable) Modifier.horizontalScroll(rememberScrollState()) else Modifier)) {
        tabs.forEachIndexed { index, item ->
            val selected = index == selectedIndex
            val enabled = itemEnabled(item)
            val background by androidx.compose.animation.animateColorAsState(
                if (selected) hyperGlass.selection else Color.Transparent,
                androidx.compose.animation.core.tween(if (scrollable) HyperMotionSpec.StateDuration else 0,
                    easing = HyperMotionSpec.EaseOutCubic),
                label = "HyperTabsBackground")
            Box(Modifier.then(if (scrollable) Modifier.widthIn(min = 76.dp) else Modifier.weight(1f))
                .fillMaxHeight()
                .hyperNoRippleClickable(enabled = enabled, role = Role.Tab, pressedScale = 0.985f,
                    onClick = { onChanged(index) })
                .padding(2.dp).background(background, RoundedCornerShape(24.dp))
                .semantics { this.selected = selected }
                .padding(horizontal = if (scrollable) 16.dp else 4.dp), contentAlignment = Alignment.Center) {
                CompositionLocalProvider(
                    LocalHyperContentColor provides if (selected) HyperColors.primaryText else HyperColors.secondaryText,
                    LocalHyperTextStyle provides LocalHyperTextStyle.current.copy(
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium)
                ) { HyperTabsItemScope(selected, enabled).itemContent(item) }
            }
        }
    }
}

object HyperTabsDefaults { val Height = 54.dp }
