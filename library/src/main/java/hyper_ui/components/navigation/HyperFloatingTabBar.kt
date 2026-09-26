/** 文件职责：提供悬浮玻璃胶囊样式的底部标签栏实现，供 HyperTabBar(type = Floating) 内部使用。 */
package hyper_ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.pointerInput
import hyper_ui.core.interaction.hyperNoRippleClickable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Immutable
data class HyperFloatingTabBarColors(
    val containerColor: Color,
    val indicatorColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContentColor: Color
)

/**
 * 悬浮玻璃胶囊样式的底部标签栏（items 模式）。
 *
 * 静止时托盘以实际标签格中心定位；按下展开为半透明水珠，拖动时跟手移动并在边界增加阻力，
 * 松手后按速度投影并用弹簧吸附。内容颜色和缩放按水珠位置连续变化。
 */
@Composable
internal fun <T> HyperFloatingTabBar(
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemSelected: (T) -> Boolean = { false },
    itemEnabled: (T) -> Boolean = { true },
    colors: HyperFloatingTabBarColors = HyperFloatingTabBarDefaults.colors(),
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
) {
    require(items.size >= 2) { "HyperFloatingTabBar 至少需要 2 个标签项" }
    val layoutDirection = LocalLayoutDirection.current
    val selectedIndex = items.indexOfFirst(itemSelected)
    val visualSelectedIndex = if (selectedIndex >= 0) {
        if (layoutDirection == LayoutDirection.Rtl) {
            items.lastIndex - selectedIndex
        } else {
            selectedIndex
        }
    } else {
        0
    }
    val position = remember { Animatable(visualSelectedIndex.toFloat()) }
    val lensExpansion = remember { Animatable(0f) }
    var pressedIndex by remember { mutableStateOf<Int?>(null) }
    var dragActive by remember { mutableStateOf(false) }
    var dragVelocity by remember { mutableStateOf(0f) }
    var grabOffsetPx by remember { mutableStateOf(0f) }
    var pointerDownX by remember { mutableStateOf(0f) }
    val velocityTracker = remember { VelocityTracker() }
    val latestOnItemClick by rememberUpdatedState(onItemClick)
    val latestItemEnabled by rememberUpdatedState(itemEnabled)
    val latestSelectedIndex by rememberUpdatedState(selectedIndex)
    val latestVisualSelectedIndex by rememberUpdatedState(visualSelectedIndex)

    LaunchedEffect(selectedIndex, layoutDirection) {
        val target = visualSelectedIndex
        if (target >= 0) {
            position.animateTo(target.toFloat(), HyperFloatingTabBarDefaults.SelectionSpring)
        }
    }

    HyperFloatingTabBarSurface(modifier = modifier, colors = colors, enabled = enabled) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(items, enabled, layoutDirection) {
                    coroutineScope {
                        var positionJob: kotlinx.coroutines.Job? = null
                        var lensJob: kotlinx.coroutines.Job? = null
                        awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        if (!enabled) return@awaitEachGesture
                        val inset = HyperFloatingTabBarDefaults.InnerPadding.toPx()
                        val cell = (size.width - inset * 2f) / items.size
                        if (cell <= 0f) return@awaitEachGesture
                        val visual = ((down.position.x - inset) / cell)
                            .toInt()
                            .coerceIn(0, items.lastIndex)
                        val logicalDown = if (layoutDirection == LayoutDirection.Rtl) {
                            items.lastIndex - visual
                        } else {
                            visual
                        }
                        if (!latestItemEnabled(items[logicalDown])) return@awaitEachGesture
                        val center = inset + cell * (visual + 0.5f)
                        pointerDownX = down.position.x
                        grabOffsetPx = down.position.x - center
                        pressedIndex = visual
                        dragActive = false
                        dragVelocity = 0f
                        velocityTracker.resetTracking()
                        velocityTracker.addPosition(down.uptimeMillis, down.position)
                        positionJob?.cancel()
                        positionJob = launch {
                            position.snapTo(visual.toFloat())
                        }
                        lensJob?.cancel()
                        lensJob = launch {
                            lensExpansion.snapTo(0f)
                            lensExpansion.animateTo(
                                1f,
                                tween(HyperFloatingTabBarDefaults.LensInMillis)
                            )
                        }

                        var finished = false
                        try {
                            while (!finished) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == down.id }
                                    ?: break
                                velocityTracker.addPosition(change.uptimeMillis, change.position)
                                if (!change.pressed) {
                                    finished = true
                                    continue
                                }
                                if (!dragActive && kotlin.math.abs(change.position.x - pointerDownX) <
                                    HyperFloatingTabBarDefaults.DragSlop.toPx()
                                ) continue
                                dragActive = true
                                val minCenter = inset + cell / 2f
                                val maxCenter = minCenter + cell * items.lastIndex
                                val desired = change.position.x - grabOffsetPx
                                val resisted = resistDrag(desired, minCenter, maxCenter, cell)
                                positionJob?.cancel()
                                positionJob = launch {
                                    position.snapTo((resisted - minCenter) / cell)
                                }
                                dragVelocity = velocityTracker.calculateVelocity().x / cell
                                change.consume()
                            }
                            val releaseVelocity = velocityTracker.calculateVelocity().x / cell
                            val projected = position.value + releaseVelocity *
                                HyperFloatingTabBarDefaults.ProjectionSeconds
                            val wasDragging = dragActive
                            val targetVisual = if (wasDragging) {
                                projected.roundToInt().coerceIn(0, items.lastIndex)
                            } else {
                                pressedIndex ?: latestVisualSelectedIndex
                            }
                            val logical = if (layoutDirection == LayoutDirection.Rtl) {
                                items.lastIndex - targetVisual
                            } else {
                                targetVisual
                            }
                            pressedIndex = null
                            dragActive = false
                            dragVelocity = releaseVelocity
                            lensJob?.cancel()
                            lensJob = launch {
                                lensExpansion.animateTo(
                                    0f,
                                    tween(HyperFloatingTabBarDefaults.LensOutMillis)
                                )
                            }
                            positionJob?.cancel()
                            positionJob = launch {
                                position.animateTo(
                                    targetVisual.toFloat(),
                                    spring(
                                        dampingRatio = HyperFloatingTabBarDefaults.SnapDampingRatio,
                                        stiffness = HyperFloatingTabBarDefaults.SnapStiffness
                                    )
                                )
                            }
                            if (wasDragging && logical != latestSelectedIndex) {
                                latestOnItemClick(items[logical])
                            }
                        } catch (_: kotlinx.coroutines.CancellationException) {
                            pressedIndex = null
                            dragActive = false
                            lensJob?.cancel()
                            lensJob = launch {
                                lensExpansion.animateTo(
                                    0f,
                                    tween(HyperFloatingTabBarDefaults.LensOutMillis)
                                )
                            }
                            positionJob?.cancel()
                            positionJob = launch {
                                position.animateTo(
                                    latestVisualSelectedIndex.toFloat(),
                                    HyperFloatingTabBarDefaults.SelectionSpring
                                )
                            }
                        }
                        }
                    }
                }
        ) {
            val itemCount = items.size
            val contentWidth = (maxWidth - HyperFloatingTabBarDefaults.InnerPadding * 2)
                .coerceAtLeast(0.dp)
            val itemWidth = contentWidth / itemCount
            val pillWidth = (itemWidth - HyperFloatingTabBarDefaults.PillGap)
                .coerceIn(0.dp, HyperFloatingTabBarDefaults.MaxPillWidth)
            val expansion = lensExpansion.value
            val indicatorWidth = pillWidth.coerceAtMost(itemWidth)
            val indicatorHeight = (maxHeight - HyperFloatingTabBarDefaults.IndicatorVerticalInset * 2)
                .coerceAtLeast(0.dp)
            val centerX = HyperFloatingTabBarDefaults.InnerPadding +
                itemWidth * (position.value + 0.5f)
            val left = (centerX - indicatorWidth / 2)
                .coerceIn(
                    HyperFloatingTabBarDefaults.InnerPadding,
                    (maxWidth - HyperFloatingTabBarDefaults.InnerPadding - indicatorWidth)
                        .coerceAtLeast(HyperFloatingTabBarDefaults.InnerPadding)
                )
            val top = (maxHeight - indicatorHeight) / 2
            val indicatorVisible = selectedIndex >= 0 || pressedIndex != null
            val indicatorAlpha = when {
                !indicatorVisible -> 0f
                !enabled -> 0.5f
                else -> (1f - expansion * 1.5f).coerceIn(0f, 1f)
            }
            val lensWidth = (
                pillWidth + HyperFloatingTabBarDefaults.LensWidthGrowth * expansion +
                    (HyperFloatingTabBarDefaults.VelocityWidthGrowth * abs(dragVelocity))
                        .coerceAtMost(HyperFloatingTabBarDefaults.MaxVelocityWidthGrowth) * expansion
                ).coerceAtMost(maxWidth + 24.dp)
            val lensHeight = (
                maxHeight - 8.dp + HyperFloatingTabBarDefaults.LensHeightGrowth * expansion
                ).coerceAtMost(maxHeight + 18.dp)
            val lensLeft = (centerX - lensWidth / 2)
                .coerceIn(-12.dp, (maxWidth + 12.dp - lensWidth).coerceAtLeast(-12.dp))
            val lensTop = (maxHeight - lensHeight) / 2

            // 指示胶囊先绘制，位于内容 Row 下层。
            Box(
                modifier = Modifier
                    .absoluteOffset(x = left, y = top)
                    .width(indicatorWidth)
                    .height(indicatorHeight)
                    .alpha(indicatorAlpha)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(color = colors.indicatorColor)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(HyperFloatingTabBarDefaults.InnerPadding)
            ) {
                items.forEachIndexed { index, item ->
                    val actualEnabled = enabled && itemEnabled(item)
                    val selected = selectedIndex == index
                    val visualIndex = if (layoutDirection == LayoutDirection.Rtl) {
                        itemCount - 1 - index
                    } else {
                        index
                    }
                    val strength = (1f - abs(position.value - visualIndex.toFloat()) *
                        HyperFloatingTabBarDefaults.SelectionDistanceFalloff)
                        .coerceIn(0f, 1f)
                    val scope = HyperTabBarItemScope(
                        selected = selected,
                        enabled = actualEnabled,
                        selectionStrength = strength
                    )
                    val contentColor = when {
                        !actualEnabled -> colors.disabledContentColor
                        else -> lerp(
                            colors.unselectedContentColor,
                            colors.selectedContentColor,
                            strength
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .graphicsLayer {
                                val scale = 1f + strength * expansion *
                                    HyperFloatingTabBarDefaults.LensScaleGrowth
                                scaleX = scale
                                scaleY = scale
                            }
                            .semantics { this.selected = selected }
                            .hyperNoRippleClickable(
                                enabled = actualEnabled,
                                role = Role.Tab,
                                onClick = { onItemClick(item) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CompositionLocalProvider(
                            LocalContentColor provides contentColor,
                            LocalTextStyle provides HyperFloatingTabBarDefaults.ItemTextStyle
                        ) {
                            scope.itemContent(item)
                        }
                    }
                }
            }

            // 水珠最后绘制，使经过的图文保留在其下方并得到放大反馈。
            if (expansion > 0.001f) {
                Box(
                    modifier = Modifier
                        .absoluteOffset(x = lensLeft, y = lensTop)
                        .width(lensWidth)
                        .height(lensHeight)
                        .alpha(if (enabled) expansion else 0f)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = expansion * 0.22f),
                                    colors.indicatorColor.copy(alpha = 0.22f + expansion * 0.12f),
                                    Color.Black.copy(alpha = expansion * 0.04f)
                                )
                            )
                        )
                )
            }
        }
    }
}

/** 拖动越过首尾标签时逐渐增加阻力，避免托盘硬撞边缘。 */
private fun resistDrag(value: Float, minimum: Float, maximum: Float, cell: Float): Float {
    if (value < minimum) {
        val overshoot = minimum - value
        return minimum - overshoot * cell * 0.32f / (cell + overshoot * 0.32f)
    }
    if (value > maximum) {
        val overshoot = value - maximum
        return maximum + overshoot * cell * 0.32f / (cell + overshoot * 0.32f)
    }
    return value
}

/**
 * 悬浮玻璃胶囊容器入口（slot 模式）。
 *
 * 只提供悬浮胶囊外壳与默认内容色；组件不持有选中索引，也不会绘制指示胶囊，
 * 选中视觉由调用方在 slot 中自行组合（与贴底样式的 slot 语义一致）。
 */
@Composable
internal fun HyperFloatingTabBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    colors: HyperFloatingTabBarColors = HyperFloatingTabBarDefaults.colors(),
    content: @Composable RowScope.() -> Unit
) {
    val contentColor = if (enabled) {
        colors.unselectedContentColor
    } else {
        colors.disabledContentColor
    }
    HyperFloatingTabBarSurface(modifier = modifier, colors = colors, enabled = enabled) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(HyperFloatingTabBarDefaults.InnerPadding),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            CompositionLocalProvider(
                LocalContentColor provides contentColor,
                LocalTextStyle provides HyperFloatingTabBarDefaults.ItemTextStyle
            ) {
                content()
            }
        }
    }
}

@Composable
private fun HyperFloatingTabBarSurface(
    modifier: Modifier,
    colors: HyperFloatingTabBarColors,
    enabled: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .padding(HyperFloatingTabBarDefaults.Margin)
            .fillMaxWidth()
            .height(HyperFloatingTabBarDefaults.Height)
            .hyperGlassSurface(
                shape = RoundedCornerShape(HyperFloatingTabBarDefaults.Height / 2),
                visuals = hyperGlassSurfaceVisuals(
                    containerColor = if (enabled) colors.containerColor else {
                        colors.containerColor.copy(alpha = colors.containerColor.alpha * 0.6f)
                    },
                    elevation = if (enabled) HyperFloatingTabBarDefaults.Elevation else 0.dp,
                    topLightAlpha = if (!enabled) 0f else if (HyperColors.isLight) 0.10f else 0.06f,
                    bottomShadeAlpha = if (!enabled) 0f else if (HyperColors.isLight) 0.02f else 0.04f,
                    shadowAlpha = if (!enabled) 0f else if (HyperColors.isLight) 0.12f else 0.22f
                )
            )
    ) {
        content()
    }
}

object HyperFloatingTabBarDefaults {
    /** 胶囊高度。 */
    val Height = 56.dp

    /** 胶囊与页面边缘的悬浮留白。 */
    val Margin = PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 12.dp)

    /** 胶囊内边距；指示胶囊与内容均在此范围内居中。 */
    val InnerPadding = 4.dp

    /** 指示托盘相对外层胶囊的单侧垂直留白。 */
    val IndicatorVerticalInset = 4.dp

    /** 悬浮抬升高度，控制阴影强度。 */
    val Elevation = 5.dp

    /** 指示胶囊最大宽度。 */
    val MaxPillWidth = 112.dp

    /** 指示托盘相对等分格子的总宽度差。 */
    val PillGap = 2.dp

    /** 水珠进入/收回动画时长。 */
    const val LensInMillis = 180
    const val LensOutMillis = 230

    /** 拖动越过边界前的触摸阈值与速度投影时间。 */
    val DragSlop = 6.dp
    const val ProjectionSeconds = 0.09f

    /** 水珠相对静态托盘的膨胀参数。 */
    val LensWidthGrowth = 38.dp
    val LensHeightGrowth = 18.dp
    val VelocityWidthGrowth = 1.1.dp
    val MaxVelocityWidthGrowth = 14.dp
    const val LensScaleGrowth = 0.15f
    const val SelectionDistanceFalloff = 1.6f

    /** Flutter HyTabBar 的弹簧参数。 */
    const val SnapDampingRatio = 0.9686f
    const val SnapStiffness = 520f

    /** 外部选中状态变化时的吸附弹簧。拖动释放使用上方的 Snap 参数。 */
    val SelectionSpring: SpringSpec<Float> = spring(dampingRatio = 0.9686f, stiffness = 470f)

    val ItemTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.labelSmall

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        indicatorColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperFloatingTabBarColors {
        val isLight = HyperColors.isLight
        val resolvedUnselected = resolveHyperContainerColor(
            unselectedContentColor,
            if (isLight) rgba(107, 114, 128, 1f) else rgba(184, 192, 204, 1f)
        )
        return HyperFloatingTabBarColors(
            containerColor = resolveHyperContainerColor(
                containerColor,
                defaultHyperFloatingContainerColor()
            ),
            indicatorColor = resolveHyperContainerColor(
                indicatorColor,
                if (isLight) Color(220f / 255f, 226f / 255f, 233f / 255f, 0.82f)
                else Color(1f, 1f, 1f, 0.14f)
            ),
            selectedContentColor = resolveHyperContainerColor(
                selectedContentColor,
                MaterialTheme.colorScheme.primary
            ),
            unselectedContentColor = resolvedUnselected,
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                resolvedUnselected.copy(alpha = HyperStyleDefaults.DisabledAlpha)
            )
        )
    }
}

/** 两种主题都以白色半透明底形成中性磨砂材质。 */
@Composable
private fun defaultHyperFloatingContainerColor(): Color = if (HyperColors.isLight) {
    Color(1f, 1f, 1f, 0.78f)
} else {
    Color(1f, 1f, 1f, 0.30f)
}
