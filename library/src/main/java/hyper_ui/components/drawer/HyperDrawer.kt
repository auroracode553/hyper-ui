/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/drawer/HyperDrawer 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import hyper_ui.core.interaction.hyperNoRippleClickable

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

@Composable
fun HyperDrawer(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    drawerModifier: Modifier = Modifier,
    position: HyperDrawerPosition = HyperDrawerPosition.Left,
    drawerContentModifier: Modifier = Modifier.padding(
        HyperDrawerDefaults.contentPadding(position)
    ),
    drawerContentScrollEnabled: Boolean = true,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    dismissOnClickOutside: Boolean = false,
    border: BorderStroke? = HyperDrawerDefaults.border(),
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val containerColor = resolveHyperContainerColor(
        containerColor = colors.containerColor,
        fallbackColor = defaultHyperDrawerContainerColor()
    )
    val drawerContentScrollState = rememberScrollState()
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
                .zIndex(HyperDrawerDefaults.DrawerZIndex)
        ) {
            if (open && dismissOnClickOutside) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .hyperNoRippleClickable(onClick = onDismissRequest)
                )
            }

            val maxDrawerWidth = maxWidth * HyperDrawerDefaults.MaxWidthFraction
            val maxDrawerHeight = maxHeight * HyperDrawerDefaults.MaxHeightFraction
            val drawerSizeModifier = when (position) {
                HyperDrawerPosition.Left,
                HyperDrawerPosition.Right -> Modifier
                    .widthIn(max = maxDrawerWidth)
                    .then(drawerModifier)
                    .fillMaxHeight()
                    .width(HyperDrawerDefaults.Width)

                HyperDrawerPosition.Top,
                HyperDrawerPosition.Bottom -> Modifier
                    .heightIn(max = maxDrawerHeight)
                    .then(drawerModifier)
                    .fillMaxWidth()
            }

            if (open) {
                Column(
                    modifier = drawerSizeModifier
                        .align(drawerAlignment)
                        .hyperSurface(
                            containerColor = containerColor,
                            shape = drawerShape(position),
                            border = border
                        )
                        // 面板背景可延伸到系统栏，内容始终留在安全绘制区域内。
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(drawerSafeDrawingSides(position))
                        )
                        .then(drawerContentModifier)
                        .then(
                            if (drawerContentScrollEnabled) {
                                // 普通内容由抽屉负责溢出滚动；懒列表必须关闭此层滚动。
                                Modifier.verticalScroll(drawerContentScrollState)
                            } else {
                                Modifier
                            }
                        ),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
                        drawerContent()
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
    val rowClickModifier = if (onClick != null) {
        Modifier.hyperNoRippleClickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick
        )
    } else {
        Modifier
    }
    val containerColor = if (selected) colors.selectedContainerColor else Color.Transparent
    val contentColor = when {
        !enabled -> colors.disabledContentColor
        selected -> colors.selectedContentColor
        else -> colors.contentColor
    }
    val supportingColor = if (enabled) colors.supportingColor else colors.disabledContentColor

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .hyperSurface(
                    containerColor = containerColor,
                    shape = RoundedCornerShape(HyperStyleDefaults.SmallCornerRadius)
                )
                .then(rowClickModifier)
                .defaultMinSize(minHeight = HyperDrawerDefaults.ItemMinHeight)
                .then(contentModifier),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingContent != null) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    leadingContent()
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = if (leadingContent == null) 0.dp else 14.dp,
                        end = if (trailingContent == null) 0.dp else 14.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    headlineContent()
                }
                if (supportingContent != null) {
                    CompositionLocalProvider(LocalContentColor provides supportingColor) {
                        supportingContent()
                    }
                }
            }

            if (trailingContent != null) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    trailingContent()
                }
            }
        }

        if (dividerVisible) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                color = colors.dividerColor
            )
        }
    }
}

private fun drawerShape(position: HyperDrawerPosition): RoundedCornerShape {
    val radius = HyperStyleDefaults.ExtraLargeCornerRadius
    return when (position) {
        HyperDrawerPosition.Left -> RoundedCornerShape(
            topEnd = radius,
            bottomEnd = radius
        )
        HyperDrawerPosition.Right -> RoundedCornerShape(
            topStart = radius,
            bottomStart = radius
        )
        HyperDrawerPosition.Top -> RoundedCornerShape(
            bottomStart = radius,
            bottomEnd = radius
        )
        HyperDrawerPosition.Bottom -> RoundedCornerShape(
            topStart = radius,
            topEnd = radius
        )
    }
}

/** 按抽屉方向保留对应系统栏与横向安全区，避免内容落入手势导航区域。 */
private fun drawerSafeDrawingSides(position: HyperDrawerPosition): WindowInsetsSides = when (position) {
    HyperDrawerPosition.Left -> WindowInsetsSides.Start + WindowInsetsSides.Vertical
    HyperDrawerPosition.Right -> WindowInsetsSides.End + WindowInsetsSides.Vertical
    HyperDrawerPosition.Top -> WindowInsetsSides.Top + WindowInsetsSides.Horizontal
    HyperDrawerPosition.Bottom -> WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal
}

object HyperDrawerDefaults {
    val Width = 320.dp
    val HeaderPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    val ItemPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
    val ItemMinHeight = 54.dp
    const val MaxWidthFraction = 0.88f
    const val MaxHeightFraction = 0.88f
    const val DrawerZIndex = 9f

    /** 上下抽屉默认提供完整内容留白，左右抽屉让 Header/Item 自己管理水平留白。 */
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
    ): HyperDrawerColors {
        val resolvedContentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText)

        return HyperDrawerColors(
            containerColor = resolveHyperContainerColor(containerColor, defaultHyperDrawerContainerColor()),
            contentColor = resolvedContentColor,
            supportingColor = resolveHyperContainerColor(supportingColor, HyperColors.secondaryText),
            selectedContainerColor = resolveHyperContainerColor(
                selectedContainerColor,
                HyperColors.accent.copy(alpha = 0.12f)
            ),
            selectedContentColor = resolveHyperContainerColor(selectedContentColor, HyperColors.accent),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                resolvedContentColor.copy(alpha = HyperStyleDefaults.DisabledAlpha)
            ),
            dividerColor = resolveHyperContainerColor(dividerColor, HyperColors.divider)
        )
    }

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = hyperPageMatchedPanelBorder(color)
}

/** 浅色仅保留轻微透明度；深色与页面背景完全一致。 */
@Composable
private fun defaultHyperDrawerContainerColor(): Color = hyperPageMatchedContainerColor(
    lightContainerColor = rgba(255, 255, 255, 0.96f)
)
