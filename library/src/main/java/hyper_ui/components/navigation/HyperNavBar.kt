/** 文件职责：提供顶部透明、正文滚入时显示渐变的导航栏。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.platform.LocalDensity

@Composable
private fun navBarTopInsets(): WindowInsets =
    HyperTheme.topBarInsets

/** 固定避让顶部状态栏；宿主传入滚动状态，组件统一处理渐变和过渡。 */
@Composable
fun HyperNavBar(
    type: String = HyperNavBarDefaults.TypeStandard,
    layout: String = HyperNavBarDefaults.LayoutCustom,
    title: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    padding: PaddingValues = HyperNavBarDefaults.ContentPadding,
    spacing: Dp = HyperNavBarDefaults.ContentGap,
    titleSpacing: Dp = HyperNavBarDefaults.TitleGap,
    actionSpacing: Dp = HyperNavBarDefaults.ActionGap,
    centerTitle: Boolean = false,
    scrolledUnder: Boolean = false,
    onBackPressed: (() -> Unit)? = null,
    onMorePressed: (() -> Unit)? = null,
    onSavePressed: (() -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    actions: List<@Composable () -> Unit> = emptyList(),
    child: (@Composable BoxScope.() -> Unit)? = null
) {
    require(type in HyperNavBarDefaults.Types) { "不支持的 HyperNavBar type: $type" }
    if (type == HyperNavBarDefaults.TypeSimple) {
        require(layout == HyperNavBarDefaults.LayoutCustom && title == null && subtitle == null &&
            leading == null && trailing == null && actions.isEmpty() && child == null &&
            onBackPressed == null && onMorePressed == null && onSavePressed == null && !centerTitle) {
            "simple 仅绘制状态栏渐变，不接受标题、操作或内容布局。"
        }
        HyperScrollGradient(scrolledUnder = scrolledUnder,
            modifier = modifier.zIndex(HyperNavBarDefaults.ZIndex).fillMaxWidth()
                .windowInsetsTopHeight(navBarTopInsets()))
        return
    }
    require(layout in HyperNavBarDefaults.Layouts) { "不支持的 HyperNavBar layout: $layout" }
    val resolvedHeight = HyperNavBarDefaults.Height
    require(spacing >= 0.dp && titleSpacing >= 0.dp && actionSpacing >= 0.dp) { "间距不能为负数" }
    require(layout == HyperNavBarDefaults.LayoutCustom ||
        (child == null && subtitle == null && leading == null && trailing == null && actions.isEmpty() && !centerTitle)) {
        "固定类型 HyperNavBar 只接受标题内容与对应回调；需要完整插槽时使用 layout = custom。"
    }
    require(layout != HyperNavBarDefaults.LayoutBackOnly || (title == null && subtitle == null)) {
        "backOnly 类型不接受标题内容。"
    }
    require(layout == HyperNavBarDefaults.LayoutCustom ||
        layout == HyperNavBarDefaults.LayoutBackOnly || title != null) {
        "此 HyperNavBar 固定类型必须提供标题内容。"
    }
    require(child == null || (title == null && subtitle == null && leading == null && trailing == null && actions.isEmpty())) {
        "child 接管整行布局，不能同时设置其他内容插槽。"
    }
    require(trailing == null || actions.isEmpty()) { "trailing 与 actions 二选一" }

    val safeInsets = navBarTopInsets()
    HyperScrollGradient(scrolledUnder = scrolledUnder,
        modifier = modifier.zIndex(HyperNavBarDefaults.ZIndex)) {
      Column(modifier = Modifier.windowInsetsPadding(safeInsets)) {
        Box(modifier = Modifier.fillMaxWidth().height(resolvedHeight).padding(padding)) {
            if (child != null) {
                child.invoke(this)
            } else if (layout == HyperNavBarDefaults.LayoutCustom) {
                HyperNavBarSlots(
                    title = title,
                    subtitle = subtitle,
                    leading = leading,
                    trailing = trailing ?: actionsSlot(actions, actionSpacing),
                    spacing = spacing,
                    titleSpacing = titleSpacing,
                    centerTitle = centerTitle
                )
            } else {
                HyperNavBarSlots(
                    title = when (layout) {
                        HyperNavBarDefaults.LayoutTitleOnly,
                        HyperNavBarDefaults.LayoutBackWithTitle,
                        HyperNavBarDefaults.LayoutMore,
                        HyperNavBarDefaults.LayoutEdit -> title
                        else -> null
                    },
                    subtitle = when (layout) {
                        HyperNavBarDefaults.LayoutTitleOnly,
                        HyperNavBarDefaults.LayoutBackWithTitle,
                        HyperNavBarDefaults.LayoutMore,
                        HyperNavBarDefaults.LayoutEdit -> subtitle
                        else -> null
                    },
                    leading = if (layout == HyperNavBarDefaults.LayoutTitleOnly) null else {
                        { HyperNavBarBackButton(onPressed = onBackPressed) }
                    },
                    trailing = when (layout) {
                        HyperNavBarDefaults.LayoutMore -> { { HyperNavBarMoreButton(onClick = onMorePressed) } }
                        HyperNavBarDefaults.LayoutEdit -> {
                            {
                                HyperButton(onPressed = onSavePressed) {
                                    HyperText("保存")
                                }
                            }
                        }
                        else -> null
                    },
                    spacing = spacing,
                    titleSpacing = titleSpacing,
                    centerTitle = false
                )
            }
        }
      }
    }
}


/** 透明导航栏的全面屏页面容器；固定计入顶部状态栏，净空随滚动内容一起滚出。 */
@Composable
fun HyperNavBarPage(
    navBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    bottomSafeArea: Boolean = true,
    scrollState: ScrollableState? = null,
    reverseLayout: Boolean = false,
    active: Boolean = true,
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    val density = LocalDensity.current
    var measuredNavBarHeight by remember { mutableIntStateOf(-1) }
    val safeInsets = navBarTopInsets()
    val topInset = with(density) {
        safeInsets.getTop(this).toDp()
    }
    val bottomInset = if (bottomSafeArea) with(density) {
        // 键盘避让由页面宿主处理，避免与 imePadding 重复累加。
        HyperTheme.systemSafeAreaInsets.getBottom(this).toDp()
    } else 0.dp
    val resolvedPadding = contentPadding.withAdditionalTop(
        if (measuredNavBarHeight >= 0) with(density) { measuredNavBarHeight.toDp() }
        else topInset
    )
        .withAdditionalBottom(bottomInset)
    val page: @Composable () -> Unit = {
        Box(modifier.fillMaxSize()) {
            content(resolvedPadding)
            Box(Modifier.fillMaxWidth().zIndex(HyperNavBarDefaults.ZIndex)
                .onSizeChanged { measuredNavBarHeight = it.height }) { navBar() }
        }
    }
    if (scrollState != null) {
        HyperNavBarScrollScope(scrollState, reverseLayout, active, page)
    } else page()
}

@Composable
private fun HyperNavBarSlots(
    title: (@Composable () -> Unit)?,
    subtitle: (@Composable () -> Unit)?,
    leading: (@Composable RowScope.() -> Unit)?,
    trailing: (@Composable RowScope.() -> Unit)?,
    spacing: Dp,
    titleSpacing: Dp,
    centerTitle: Boolean
) {
    val hasTitle = title != null || subtitle != null
    CompositionLocalProvider(LocalHyperContentColor provides HyperColors.primaryText) {
        if (centerTitle) {
            Box(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                    leading?.invoke(this)
                    Box(Modifier.weight(1f))
                    trailing?.invoke(this)
                }
                if (hasTitle) {
                    Box(Modifier.align(Alignment.Center)) {
                        HyperNavBarTitle(title, subtitle, titleSpacing, true)
                    }
                }
            }
        } else {
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                leading?.invoke(this)
                if (leading != null && hasTitle) Box(Modifier.width(spacing))
                if (hasTitle) {
                    Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        HyperNavBarTitle(title, subtitle, titleSpacing, false)
                    }
                } else {
                    Box(Modifier.weight(1f))
                }
                if (trailing != null && hasTitle) Box(Modifier.width(spacing))
                trailing?.invoke(this)
            }
        }
    }
}

@Composable
private fun HyperNavBarTitle(
    title: (@Composable () -> Unit)?,
    subtitle: (@Composable () -> Unit)?,
    titleSpacing: Dp,
    centered: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(titleSpacing),
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start
    ) {
        title?.let {
            CompositionLocalProvider(LocalHyperTextStyle provides HyperNavBarDefaults.TitleTextStyle) {
                it()
            }
        }
        subtitle?.let {
            CompositionLocalProvider(LocalHyperTextStyle provides HyperNavBarDefaults.SubtitleTextStyle) { it() }
        }
    }
}

private fun actionsSlot(
    actions: List<@Composable () -> Unit>,
    spacing: Dp
): (@Composable RowScope.() -> Unit)? = if (actions.isEmpty()) null else {
    {
        Row(horizontalArrangement = Arrangement.spacedBy(spacing), verticalAlignment = Alignment.CenterVertically) {
            actions.forEach { it() }
        }
    }
}

private fun PaddingValues.withAdditionalTop(additionalTop: Dp): PaddingValues = object : PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection) =
        this@withAdditionalTop.calculateLeftPadding(layoutDirection)
    override fun calculateTopPadding() = this@withAdditionalTop.calculateTopPadding() + additionalTop
    override fun calculateRightPadding(layoutDirection: LayoutDirection) =
        this@withAdditionalTop.calculateRightPadding(layoutDirection)
    override fun calculateBottomPadding() = this@withAdditionalTop.calculateBottomPadding()
}

private fun PaddingValues.withAdditionalBottom(additionalBottom: Dp): PaddingValues = object : PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection) =
        this@withAdditionalBottom.calculateLeftPadding(layoutDirection)
    override fun calculateTopPadding() = this@withAdditionalBottom.calculateTopPadding()
    override fun calculateRightPadding(layoutDirection: LayoutDirection) =
        this@withAdditionalBottom.calculateRightPadding(layoutDirection)
    override fun calculateBottomPadding() = this@withAdditionalBottom.calculateBottomPadding() + additionalBottom
}

object HyperNavBarDefaults {
    const val TypeStandard = "standard"
    const val TypeSimple = "simple"
    val Types = setOf(TypeStandard, TypeSimple)
    const val LayoutCustom = "custom"
    const val LayoutBackOnly = "backOnly"
    const val LayoutTitleOnly = "titleOnly"
    const val LayoutBackWithTitle = "backWithTitle"
    const val LayoutMore = "more"
    const val LayoutEdit = "edit"
    val Layouts = setOf(LayoutCustom, LayoutBackOnly, LayoutTitleOnly, LayoutBackWithTitle, LayoutMore, LayoutEdit)
    val Height = 44.dp
    val ContentGap = 8.dp
    val TitleGap = 2.dp
    val ActionGap = 4.dp
    val ContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
    const val ZIndex = 1f

    val TitleTextStyle
        @Composable get() = HyperTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 19.2.sp,
            letterSpacing = (-0.2).sp
        )
    val SubtitleTextStyle
        @Composable get() = HyperTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 13.2.sp)

}
