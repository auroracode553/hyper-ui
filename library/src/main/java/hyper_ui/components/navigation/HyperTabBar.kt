/** 文件职责：统一悬浮、贴底导航与可重复触发的底部动作入口。 */
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
import androidx.compose.ui.draw.alpha
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

class HyperTabBarItemScope internal constructor(val selected: Boolean)

/** 选择值由调用方持有；itemLabel 用于与参考实现相同的内容宽度测量。 */
@Composable
fun <T> HyperTabBar(
    items: List<T>,
    onChanged: (Int) -> Unit,
    selectedIndex: Int?,
    modifier: Modifier = Modifier,
    type: String = "multiple",
    itemLabel: (T) -> String = { "" },
    badgeCount: Int = 0,
    safeArea: Boolean = false,
    floating: Boolean = true,
    margin: PaddingValues = if (!floating || type == "single") PaddingValues(0.dp) else PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp),
    color: Color = HyperColors.accent,
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
) {
    require(type in setOf("multiple", "single")) { "type 必须为 multiple 或 single" }
    require(if (type == "single") items.size == 1 else items.size >= 2) { "single 需要 1 项，multiple 至少需要 2 项" }
    require(selectedIndex == null || selectedIndex in items.indices) { "selectedIndex 超出 items 范围" }
    require(floating || type == "multiple") { "非悬浮底栏使用 multiple 形态" }
    val density = androidx.compose.ui.platform.LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold,
        fontFamily = HyperTheme.typography.bodyMedium.fontFamily)
    val itemWidth = items.maxOf { item ->
        with(density) { textMeasurer.measure(itemLabel(item), labelStyle).size.width.toDp() } + 32.dp
    }.coerceAtLeast(76.dp)
    val safeInsets = HyperTheme.systemSafeAreaInsets
    val bottomInset = if (safeArea) safeInsets.asPaddingValues().calculateBottomPadding() else 0.dp
    // 贴底沿用原 55+5dp 的总尺寸，安全区计入其中；大字体或三键导航才按内容需要增长。
    val height = if (floating) {
        maxOf(56.dp, with(density) { 12.sp.toDp() } * 1.08f + 36.dp)
    } else {
        maxOf(HyperTabBarDefaults.DockedHeight - bottomInset, 40.dp, with(density) { 12.sp.toDp() } * 1.08f + 26.dp)
    }
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    var dragIndex by remember(items.size, selectedIndex == null, floating) { mutableStateOf<Int?>(null) }
    val logicalSelected = selectedIndex
    val visualSelectedIndex = dragIndex ?: logicalSelected?.let { if (rtl) items.lastIndex - it else it } ?: 0
    val position by animateFloatAsState(visualSelectedIndex.toFloat(), tween(HyperMotionSpec.StateDuration,
        easing = HyperMotionSpec.EaseOutCubic), label = "HyperTabBarSelection")
    val latestLogicalSelected by rememberUpdatedState(logicalSelected)
    val latestOnClick by rememberUpdatedState(onChanged)
    val latestItemEnabled by rememberUpdatedState(itemEnabled)
    // 贴底表面连同底部安全区由库绘制，调用方不再叠加底板或二次避让。
    Box(modifier.then(if (floating) Modifier else Modifier.fillMaxWidth().background(HyperColors.pageBackground))
        .then(if (safeArea) Modifier.windowInsetsPadding(safeInsets.only(WindowInsetsSides.Bottom)) else Modifier)
        .padding(margin)) {
        if (type == "single") {
            val item = items.single()
            val enabled = itemEnabled(item)
            Box(Modifier.size(56.dp)
                .hyperFrostedSurface(hyperGlass.surface, RoundedCornerShape(28.dp))
                .alpha(if (enabled) 1f else 0.4f)
                .hyperNoRippleClickable(enabled = enabled, onClick = { onChanged(0) }),
                contentAlignment = Alignment.Center) {
                CompositionLocalProvider(LocalHyperContentColor provides color, LocalHyperIconSize provides 22.dp) {
                    HyperTabBarItemScope(true).itemContent(item)
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
            val surfaceModifier = if (floating) {
                Modifier.width(itemWidth * items.size + 8.dp)
                    .hyperFrostedSurface(hyperGlass.surfaceStrong, RoundedCornerShape(height / 2), blur = hyperGlass.blurStrong)
            } else Modifier.fillMaxWidth()
            val inset = if (floating) 4.dp else 0.dp
            BoxWithConstraints(surfaceModifier.height(height)
                .pointerInput(items, rtl, floating, selectedIndex == null) {
                    // 动作模式没有选中项，不将横向滑动转换为后退、菜单等业务操作。
                    if (selectedIndex == null) return@pointerInput
                    fun visualIndex(x: Float): Int = ((x - inset.toPx()) / (size.width - inset.toPx() * 2).coerceAtLeast(1f) * items.size)
                        .toInt().coerceIn(0, items.lastIndex)
                    detectHorizontalDragGestures(
                        onDragStart = { dragIndex = visualIndex(it.x) },
                        onHorizontalDrag = { change, _ -> dragIndex = visualIndex(change.position.x); change.consume() },
                        onDragCancel = { dragIndex = null },
                        onDragEnd = {
                            dragIndex?.let { visual ->
                                val logical = if (rtl) items.lastIndex - visual else visual
                                if (logical != latestLogicalSelected && latestItemEnabled(items[logical])) latestOnClick(logical)
                            }
                            dragIndex = null
                        }
                    )
                }) {
                val cellWidth = (maxWidth - inset * 2) / items.size
                if (selectedIndex != null) Box(Modifier.absoluteOffset(x = inset + 1.dp + cellWidth * position, y = 4.dp)
                    .width((cellWidth - 2.dp).coerceAtLeast(0.dp)).height(height - 8.dp)
                    .background(hyperGlass.selection, RoundedCornerShape(height / 2)))
                Row(Modifier.fillMaxSize().padding(horizontal = inset)) {
                    items.forEachIndexed { index, item ->
                        val selected = index == selectedIndex
                        val enabled = itemEnabled(item)
                        Box(Modifier.weight(1f).fillMaxHeight()
                            .alpha(if (enabled) 1f else 0.4f)
                            .hyperNoRippleClickable(enabled = enabled, role = if (selectedIndex == null) Role.Button else Role.Tab, pressedScale = 1f,
                                onClick = { if (index != selectedIndex) onChanged(index) })
                            .then(if (selectedIndex == null) Modifier else Modifier.semantics { this.selected = selected }), contentAlignment = Alignment.Center) {
                            CompositionLocalProvider(
                                LocalHyperContentColor provides if (selected) color else HyperColors.primaryText,
                                LocalHyperTextStyle provides labelStyle.copy(lineHeight = 12.96.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium),
                                LocalHyperIconSize provides 23.dp
                            ) { HyperTabBarItemScope(selected).itemContent(item) }
                        }
                    }
                }
            }
        }
    }
}

object HyperTabBarDefaults {
    val Height = 56.dp
    /** 非悬浮总高度包含底部安全区，不由消费页再次叠加。 */
    val DockedHeight = 60.dp
}
