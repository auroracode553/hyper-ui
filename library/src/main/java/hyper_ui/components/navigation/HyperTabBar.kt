/** 文件职责：移植参考 HyperTabBar 的内容收缩悬浮导航与单动作模式。 */
package hyper_ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

class HyperTabBarItemScope internal constructor(val selected: Boolean, val enabled: Boolean)

/** 选择值由调用方持有；itemLabel 用于与参考实现相同的内容宽度测量。 */
@Composable
fun <T> HyperTabBar(
    items: List<T>,
    onChanged: (Int) -> Unit,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    type: String = "multiple",
    enabled: Boolean = true,
    itemEnabled: (T) -> Boolean = { true },
    itemLabel: (T) -> String = { "" },
    badgeCount: Int = 0,
    safeArea: Boolean = true,
    margin: PaddingValues = if (type == "single") PaddingValues(0.dp) else PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp),
    color: Color = HyperColors.accent,
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
) {
    require(type in setOf("multiple", "single")) { "type 必须为 multiple 或 single" }
    require(if (type == "single") items.size == 1 else items.size >= 2) { "single 需要 1 项，multiple 至少需要 2 项" }
    require(selectedIndex in items.indices) { "selectedIndex 超出 items 范围" }
    val density = androidx.compose.ui.platform.LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold,
        fontFamily = HyperTheme.typography.bodyMedium.fontFamily)
    val itemWidth = items.maxOf { item ->
        with(density) { textMeasurer.measure(itemLabel(item), labelStyle).size.width.toDp() } + 32.dp
    }.coerceAtLeast(76.dp)
    val height = maxOf(56.dp, with(density) { 12.sp.toDp() } * 1.08f + 36.dp)
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    var dragIndex by remember(items.size) { mutableStateOf<Int?>(null) }
    val logicalSelected = selectedIndex
    val visualSelectedIndex = dragIndex ?: if (rtl) items.lastIndex - logicalSelected else logicalSelected
    val position by animateFloatAsState(visualSelectedIndex.toFloat(), tween(HyperMotionSpec.StateDuration,
        easing = HyperMotionSpec.EaseOutCubic), label = "HyperTabBarSelection")
    val latestLogicalSelected by rememberUpdatedState(logicalSelected)
    val latestOnClick by rememberUpdatedState(onChanged)
    val latestItemEnabled by rememberUpdatedState(itemEnabled)
    Box(modifier.then(if (safeArea) Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)) else Modifier)
        .padding(margin)) {
        if (type == "single") {
            val item = items.single()
            Box(Modifier.size(56.dp)
                .hyperFrostedSurface(hyperGlass.surface, RoundedCornerShape(28.dp))
                .hyperNoRippleClickable(enabled = enabled && itemEnabled(item), onClick = { onChanged(items.indexOf(item)) }),
                contentAlignment = Alignment.Center) {
                CompositionLocalProvider(LocalHyperContentColor provides color, LocalHyperIconSize provides 22.dp) {
                    HyperTabBarItemScope(true, enabled && itemEnabled(item)).itemContent(item)
                }
            }
            if (badgeCount > 0) Box(Modifier.align(Alignment.TopEnd).offset(x = 5.dp, y = (-5).dp)
                .defaultMinSize(minWidth = 16.dp).height(16.dp)
                .hyperSolidSurface(HyperColors.danger, RoundedCornerShape(50), border = androidx.compose.foundation.BorderStroke(1.dp, HyperColors.pageBackground))
                .padding(horizontal = 5.dp), contentAlignment = Alignment.Center) {
                HyperText(if (badgeCount > 99) "99+" else "$badgeCount", color = Color(1f, 1f, 1f, 1f),
                    fontSize = 10.sp, lineHeight = 10.sp, fontWeight = FontWeight.SemiBold)
            }
        } else {
            BoxWithConstraints(Modifier.width(itemWidth * items.size + 8.dp).height(height)
                .hyperFrostedSurface(hyperGlass.surfaceStrong, RoundedCornerShape(height / 2), blur = hyperGlass.blurStrong)
                .pointerInput(items, enabled, rtl) {
                    if (!enabled) return@pointerInput
                    fun visualIndex(x: Float): Int = ((x - 4.dp.toPx()) / (size.width - 8.dp.toPx()) * items.size)
                        .toInt().coerceIn(0, items.lastIndex)
                    detectHorizontalDragGestures(
                        onDragStart = { dragIndex = visualIndex(it.x) },
                        onHorizontalDrag = { change, _ -> dragIndex = visualIndex(change.position.x); change.consume() },
                        onDragCancel = { dragIndex = null },
                        onDragEnd = {
                            dragIndex?.let { visual ->
                                val logical = if (rtl) items.lastIndex - visual else visual
                                if (latestItemEnabled(items[logical]) && logical != latestLogicalSelected) latestOnClick(logical)
                            }
                            dragIndex = null
                        }
                    )
                }) {
                val cellWidth = (maxWidth - 8.dp) / items.size
                Box(Modifier.absoluteOffset(x = 5.dp + cellWidth * position, y = 4.dp)
                    .width((cellWidth - 2.dp).coerceAtLeast(0.dp)).height(height - 8.dp)
                    .background(hyperGlass.selection, RoundedCornerShape(height / 2)))
                Row(Modifier.fillMaxSize().padding(horizontal = 4.dp)) {
                    items.forEachIndexed { index, item ->
                        val selected = index == selectedIndex
                        val active = enabled && itemEnabled(item)
                        Box(Modifier.weight(1f).fillMaxHeight()
                            .hyperNoRippleClickable(enabled = active, role = Role.Tab, pressedScale = 1f,
                                onClick = { if (index != selectedIndex) onChanged(index) })
                            .semantics { this.selected = selected }, contentAlignment = Alignment.Center) {
                            CompositionLocalProvider(
                                LocalHyperContentColor provides when { !active -> HyperColors.secondaryText; selected -> color; else -> HyperColors.primaryText },
                                LocalHyperTextStyle provides labelStyle.copy(lineHeight = 12.96.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium),
                                LocalHyperIconSize provides 23.dp
                            ) { HyperTabBarItemScope(selected, active).itemContent(item) }
                        }
                    }
                }
            }
        }
    }
}

object HyperTabBarDefaults { val Height = 56.dp }
