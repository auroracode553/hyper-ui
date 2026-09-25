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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
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
 * 胶囊内边距 3dp，指示胶囊宽度取「等分格宽度 - 8dp」并夹在 16dp~112dp 之间；
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

    HyperFloatingTabBarSurface(modifier = modifier, colors = colors) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val itemCount = items.size
            val pillWidth = (maxWidth / itemCount - HyperFloatingTabBarDefaults.PillGap)
                .coerceAtMost(HyperFloatingTabBarDefaults.MaxPillWidth)
                .coerceAtLeast(HyperFloatingTabBarDefaults.MinPillWidth)
            val step = ((maxWidth - pillWidth) / (itemCount - 1)).coerceAtLeast(0.dp)
            val press = pressDepth.value
            val indicatorWidth =
                pillWidth + HyperFloatingTabBarDefaults.PressWidthGrowth * press
            val indicatorHeight = (
                maxHeight -
                    HyperFloatingTabBarDefaults.InnerPadding * 2 -
                    HyperFloatingTabBarDefaults.IndicatorVerticalInset * 2 +
                    HyperFloatingTabBarDefaults.PressHeightGrowth * press
                ).coerceAtLeast(0.dp)
            val centerX = pillWidth / 2 + step * position.value
            val left = (centerX - indicatorWidth / 2)
                .coerceIn(0.dp, (maxWidth - indicatorWidth).coerceAtLeast(0.dp))
            val top = (maxHeight - indicatorHeight) / 2
            val indicatorVisible = selectedIndex >= 0 || pressedIndex != null

            // 指示胶囊先绘制，位于内容 Row 下层。
            Box(
                modifier = Modifier
                    .offset(x = left, y = top)
                    .width(indicatorWidth)
                    .height(indicatorHeight)
                    .alpha(if (indicatorVisible) 1f else 0f)
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
                    if (isPressed) {
                        pressedIndex = index
                    } else if (pressedIndex == index) {
                        pressedIndex = null
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
                            .hyperNoRippleClickable(
                                enabled = actualEnabled,
                                role = Role.Button,
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
    HyperFloatingTabBarSurface(modifier = modifier, colors = colors) {
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
                    containerColor = colors.containerColor,
                    elevation = HyperFloatingTabBarDefaults.Elevation,
                    topLightAlpha = if (HyperColors.isLight) 0.36f else 0.14f,
                    bottomShadeAlpha = if (HyperColors.isLight) 0.05f else 0.12f,
                    shadowAlpha = if (HyperColors.isLight) 0.16f else 0.30f
                )
            )
    ) {
        content()
    }
}

object HyperFloatingTabBarDefaults {
    /** 胶囊高度。 */
    val Height = 50.dp

    /** 胶囊与页面边缘的悬浮留白。 */
    val Margin = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 10.dp)

    /** 胶囊内边距；指示胶囊与内容均在此范围内居中。 */
    val InnerPadding = 3.dp

    /** 指示胶囊相对内容区的单侧垂直留白。 */
    val IndicatorVerticalInset = 2.dp

    /** 悬浮抬升高度，控制阴影强度。 */
    val Elevation = 4.dp

    /** 指示胶囊最小宽度。 */
    val MinPillWidth = 16.dp

    /** 指示胶囊最大宽度。 */
    val MaxPillWidth = 112.dp

    /** 指示胶囊相对等分格子的单侧间隙。 */
    val PillGap = 8.dp

    /** 按压时指示胶囊的宽度增长。 */
    val PressWidthGrowth = 5.dp

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
                if (isLight) rgba(241, 243, 245, 1f) else rgba(39, 44, 53, 1f)
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

/** 浅色保留轻量透明度；深色使用略高于页面背景的深色玻璃。 */
@Composable
private fun defaultHyperFloatingContainerColor(): Color = if (HyperColors.isLight) {
    rgba(255, 255, 255, 0.92f)
} else {
    rgba(27, 31, 39, 0.92f)
}
