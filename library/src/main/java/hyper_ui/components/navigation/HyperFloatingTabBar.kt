/** 文件职责：提供悬浮玻璃胶囊样式的底部标签栏实现，供 HyperTabBar(type = Floating) 内部使用。 */
package hyper_ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable
import kotlin.math.abs

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
 * 指示托盘以实际标签格中心定位，宽度限制在格宽之内；
 * 按下时指示胶囊吸附到所按项目并略微放大，释放未命中选中则弹回；选中切换使用
 * 与 Flutter HyTabBar 一致的弹簧吸附。内容颜色按指示位置在选中/未选中色之间渐变。
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
    val selectedIndex = items.indexOfFirst(itemSelected)
    val position = remember { Animatable(selectedIndex.coerceAtLeast(0).toFloat()) }
    val pressDepth = remember { Animatable(0f) }
    var pressedIndex by remember { mutableStateOf<Int?>(null) }
    val layoutDirection = LocalLayoutDirection.current

    LaunchedEffect(pressedIndex) {
        pressDepth.animateTo(
            targetValue = if (pressedIndex != null) 1f else 0f,
            animationSpec = tween(
                durationMillis = if (pressedIndex != null) {
                    HyperFloatingTabBarDefaults.PressInMillis
                } else {
                    HyperFloatingTabBarDefaults.PressOutMillis
                }
            )
        )
    }
    LaunchedEffect(pressedIndex, selectedIndex) {
        val target = pressedIndex ?: selectedIndex
        if (target >= 0) {
            position.animateTo(target.toFloat(), HyperFloatingTabBarDefaults.SelectionSpring)
        }
    }

    HyperFloatingTabBarSurface(modifier = modifier, colors = colors, enabled = enabled) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val itemCount = items.size
            val contentWidth = (maxWidth - HyperFloatingTabBarDefaults.InnerPadding * 2)
                .coerceAtLeast(0.dp)
            val itemWidth = contentWidth / itemCount
            val pillWidth = (itemWidth - HyperFloatingTabBarDefaults.PillGap)
                .coerceIn(0.dp, HyperFloatingTabBarDefaults.MaxPillWidth)
            val press = pressDepth.value
            val indicatorWidth = (pillWidth + HyperFloatingTabBarDefaults.PressWidthGrowth * press)
                .coerceAtMost(itemWidth)
            val indicatorHeight = (
                maxHeight -
                    HyperFloatingTabBarDefaults.IndicatorVerticalInset * 2 +
                    HyperFloatingTabBarDefaults.PressHeightGrowth * press
                ).coerceIn(0.dp, maxHeight)
            val visualPosition = if (layoutDirection == LayoutDirection.Rtl) {
                itemCount - 1 - position.value
            } else {
                position.value
            }
            val centerX = HyperFloatingTabBarDefaults.InnerPadding +
                itemWidth * (visualPosition + 0.5f)
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
                else -> 1f
            }

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
                    val interactionSource = remember(item, enabled) {
                        MutableInteractionSource()
                    }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    LaunchedEffect(isPressed, index) {
                        if (isPressed) {
                            pressedIndex = index
                        } else if (pressedIndex == index) {
                            pressedIndex = null
                        }
                    }
                    val actualEnabled = enabled && itemEnabled(item)
                    val selected = selectedIndex == index
                    val strength = (1f - abs(position.value - index.toFloat()))
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
                            .semantics { this.selected = selected }
                            .hyperNoRippleClickable(
                                enabled = actualEnabled,
                                role = Role.Tab,
                                interactionSource = interactionSource,
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
        }
    }
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
    val Margin = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 12.dp)

    /** 胶囊内边距；指示胶囊与内容均在此范围内居中。 */
    val InnerPadding = 5.dp

    /** 指示托盘相对外层胶囊的单侧垂直留白。 */
    val IndicatorVerticalInset = 7.dp

    /** 悬浮抬升高度，控制阴影强度。 */
    val Elevation = 5.dp

    /** 指示胶囊最大宽度。 */
    val MaxPillWidth = 80.dp

    /** 指示托盘相对等分格子的总宽度差。 */
    val PillGap = 16.dp

    /** 按压时指示胶囊的宽度增长。 */
    val PressWidthGrowth = 4.dp

    /** 按压时指示胶囊的高度增长。 */
    val PressHeightGrowth = 2.dp

    /** 按压加深动画时长（毫秒）。 */
    const val PressInMillis = 85

    /** 按压释放动画时长（毫秒）。 */
    const val PressOutMillis = 180

    /** 选中吸附弹簧，对应 Flutter HyTabBar 的 settleSpring（mass=1, stiffness=470, damping=42）。 */
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
                if (isLight) rgba(26, 29, 38, 1f) else rgba(243, 244, 246, 1f)
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
