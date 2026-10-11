/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/drawer/HyperDrawer 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import hyper_ui.core.icon.HyperCloseIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

enum class HyperDrawerPosition {
    Left,
    Right,
    Top,
    Bottom
}

@Immutable
data class HyperDrawerColors(
    val containerColor: Color,
    val contentColor: Color,
    val supportingColor: Color,
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val disabledContentColor: Color,
    val dividerColor: Color
)

/**
 * 四方向抽屉容器。
 *
 * @param defaultSetPadding 是否应用组件默认的方向化内容间距与 safeDrawing 避让
 * @param drawerContentModifier 在默认 Padding 策略之后追加的场景内容修饰符
 */
@Composable
fun HyperDrawer(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    drawerModifier: Modifier = Modifier,
    position: HyperDrawerPosition = HyperDrawerPosition.Right,
    defaultSetPadding: Boolean = true,
    drawerContentModifier: Modifier = Modifier,
    drawerContentScrollEnabled: Boolean = true,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    dismissOnClickOutside: Boolean = true,
    title: String? = null,
    footerContent: (@Composable ColumnScope.() -> Unit)? = null,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val transitionProgress = hyperOverlayProgress(open, durationMillis = HyperMotionSpec.OverlayDuration)
    val isPresent = open || transitionProgress > 0f
    val resolvedColors = resolveHyperDrawerColors(colors)
    val drawerContentScrollState = rememberScrollState()
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    val safe = WindowInsets.safeDrawing.exclude(WindowInsets.ime)
    val outerPadding = if (defaultSetPadding) with(density) {
        val left = maxOf(12.dp, safe.getLeft(this, direction).toDp())
        val right = maxOf(12.dp, safe.getRight(this, direction).toDp())
        PaddingValues(start = if (direction == androidx.compose.ui.unit.LayoutDirection.Rtl) right else left,
            end = if (direction == androidx.compose.ui.unit.LayoutDirection.Rtl) left else right,
            top = maxOf(12.dp, safe.getTop(this).toDp()), bottom = maxOf(12.dp, safe.getBottom(this).toDp()))
    } else PaddingValues(12.dp)
    val drawerAlignment = when (position) {
        HyperDrawerPosition.Left -> Alignment.CenterStart
        HyperDrawerPosition.Right -> Alignment.CenterEnd
        HyperDrawerPosition.Top -> Alignment.TopCenter
        HyperDrawerPosition.Bottom -> Alignment.BottomCenter
    }

    Box(modifier = modifier.fillMaxSize()) {
        content()

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .zIndex(HyperDrawerDefaults.DrawerZIndex)
        ) {
            if (isPresent) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(hyperGlass.scrim.copy(alpha = hyperGlass.scrim.alpha * transitionProgress))
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null,
                            onClick = { if (dismissOnClickOutside) onDismissRequest() })
                )
            }

            val maxDrawerWidth = maxWidth
            val maxDrawerHeight = (maxHeight - 24.dp).coerceAtLeast(0.dp)
            val drawerSizeModifier = when (position) {
                HyperDrawerPosition.Left,
                HyperDrawerPosition.Right -> Modifier
                    .widthIn(max = maxDrawerWidth)
                    .then(drawerModifier)
                    .fillMaxHeight()
                    .width(HyperDrawerDefaults.Width + 24.dp)

                HyperDrawerPosition.Top,
                HyperDrawerPosition.Bottom -> Modifier
                    .heightIn(max = maxDrawerHeight)
                    .then(drawerModifier)
                    .fillMaxWidth()
            }

            if (isPresent) {
                Column(
                    modifier = drawerSizeModifier
                        .align(drawerAlignment)
                        .padding(outerPadding)
                        .graphicsLayer {
                            alpha = transitionProgress
                            when (position) {
                                HyperDrawerPosition.Left -> translationX = constraints.maxWidth * (if (direction == androidx.compose.ui.unit.LayoutDirection.Rtl) 0.12f else -0.12f) * (1f - transitionProgress)
                                HyperDrawerPosition.Right -> translationX = constraints.maxWidth * (if (direction == androidx.compose.ui.unit.LayoutDirection.Rtl) -0.12f else 0.12f) * (1f - transitionProgress)
                                HyperDrawerPosition.Top -> translationY = -size.height * 0.12f * (1f - transitionProgress)
                                HyperDrawerPosition.Bottom -> translationY = size.height * 0.12f * (1f - transitionProgress)
                            }
                        }
                        .hyperGlassSurface(
                            shape = drawerShape(position),
                            visuals = hyperGlassSurfaceVisuals(
                                containerColor = resolvedColors.containerColor,
                                elevation = HyperDrawerDefaults.Elevation,
                                blur = hyperGlass.blurStrong,

                                depth = hyperSurfaceDepthVisuals(
                                    role = HyperSurfaceDepthRole.StructuralPanel,
                                    elevation = HyperDrawerDefaults.Elevation
                                )
                            )
                        )
                        // 调用方修饰符始终叠加在默认策略之后，用于补充场景布局。
                        .then(drawerContentModifier),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    CompositionLocalProvider(LocalHyperContentColor provides resolvedColors.contentColor,
                        LocalHyperSurfacePresent provides true) {
                        Row(Modifier.fillMaxWidth().padding(start = 20.dp, top = 12.dp, end = 8.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            if (title != null) HyperText(title, Modifier.weight(1f), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            else androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))
                            HyperTooltip("关闭") {
                                HyperButton(onDismissRequest, type = "icon") { HyperCloseIcon(Modifier.size(20.dp)) }
                            }
                        }
                        Column(Modifier.weight(1f, fill = position == HyperDrawerPosition.Left || position == HyperDrawerPosition.Right)
                            .then(if (drawerContentScrollEnabled) Modifier.verticalScroll(drawerContentScrollState) else Modifier)
                            .then(if (defaultSetPadding) Modifier.padding(16.dp) else Modifier), content = drawerContent)
                        if (footerContent != null) {
                            HyperDivider()
                            Column(Modifier.padding(16.dp), content = footerContent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HyperDrawerHeader(
    headlineContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDrawerDefaults.HeaderPadding),
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(contentModifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent?.invoke(this)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = if (leadingContent == null) 0.dp else 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            headlineContent()
            supportingContent?.invoke(this)
        }
    }
}

@Composable
fun HyperDrawerItem(
    headlineContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDrawerDefaults.ItemPadding),
    selected: Boolean = false,
    enabled: Boolean = true,
    dividerVisible: Boolean = false,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
) {
    val resolved = resolveHyperDrawerColors(colors)
    val foreground = if (selected) resolved.selectedContentColor else resolved.contentColor
    HyperListTile(
        headlineContent = headlineContent,
        modifier = modifier.background(if (selected) resolved.selectedContainerColor else Color.Transparent, RoundedCornerShape(12.dp)),
        contentModifier = contentModifier,
        type = "plain", enabled = enabled, showChevron = false,
        dividerVisible = dividerVisible, onClick = onClick,
        colors = HyperListTileColors(foreground, resolved.supportingColor, resolved.disabledContentColor, resolved.dividerColor),
        leadingContent = leadingContent, supportingContent = supportingContent, trailingContent = trailingContent
    )
}

private fun drawerShape(position: HyperDrawerPosition): RoundedCornerShape {
    return RoundedCornerShape(30.dp)
}

object HyperDrawerDefaults {
    val Width = 320.dp
    val Elevation = 12.dp
    val SelectedItemElevation = 0.dp
    val HeaderPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    val ItemPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    val ItemMinHeight = 46.dp
    const val MaxWidthFraction = 0.88f
    const val MaxHeightFraction = 0.88f
    const val DrawerZIndex = 9f

    /** 默认 Padding 开启时使用的方向化内容留白。 */
    fun contentPadding(position: HyperDrawerPosition): PaddingValues = when (position) {
        HyperDrawerPosition.Top,
        HyperDrawerPosition.Bottom -> PaddingValues(horizontal = 20.dp, vertical = 16.dp)
        HyperDrawerPosition.Left,
        HyperDrawerPosition.Right -> PaddingValues(vertical = 16.dp)
    }

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        supportingColor: Color = Color.Unspecified,
        selectedContainerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperDrawerColors = resolveHyperDrawerColors(
        HyperDrawerColors(
            containerColor = containerColor,
            contentColor = contentColor,
            supportingColor = supportingColor,
            selectedContainerColor = selectedContainerColor,
            selectedContentColor = selectedContentColor,
            disabledContentColor = disabledContentColor,
            dividerColor = dividerColor
        )
    )
}

@Composable
private fun resolveHyperDrawerColors(colors: HyperDrawerColors): HyperDrawerColors {
    val containerColor = resolveHyperContainerColor(colors.containerColor, defaultHyperDrawerContainerColor())
    val selectedContainerColor = resolveHyperContainerColor(
        colors.selectedContainerColor,
        hyperGlass.selection
    )

    return HyperDrawerColors(
        containerColor = containerColor,
        contentColor = resolveHyperContainerColor(colors.contentColor, HyperColors.primaryText),
        supportingColor = resolveHyperContainerColor(colors.supportingColor, HyperColors.secondaryText),
        selectedContainerColor = selectedContainerColor,
        selectedContentColor = resolveHyperContainerColor(colors.selectedContentColor, HyperColors.primaryText),
        disabledContentColor = resolveHyperContainerColor(colors.disabledContentColor, HyperColors.disabledText),
        dividerColor = resolveHyperContainerColor(
            colors.dividerColor,
            if (HyperColors.isLight) Color(0f, 0f, 0f, 0.08f) else Color(1f, 1f, 1f, 0.10f)
        )
    )
}

/** 抽屉采用当前材质的 prominent 表面。 */
@Composable
private fun defaultHyperDrawerContainerColor(): Color = hyperGlass.surfaceStrong
