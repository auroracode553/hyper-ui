/** 文件职责：提供透明顶部导航栏，视觉和布局对齐 Flutter HyNavBar。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.platform.LocalDensity

/** 透明顶部导航栏。使用 small/default/large 统一尺寸词汇，安全区位于内容高度之外。 */
@Composable
fun HyperNavBar(
    titleContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    size: String = "default",
    padding: PaddingValues = HyperNavBarDefaults.ContentPadding,
    spacing: Dp = HyperNavBarDefaults.ContentGap,
    titleSpacing: Dp = HyperNavBarDefaults.TitleGap,
    actionSpacing: Dp = HyperNavBarDefaults.ActionGap,
    safeArea: Boolean = true,
    centerTitle: Boolean = false,
    subtitleContent: (@Composable () -> Unit)? = null,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    actions: List<@Composable () -> Unit> = emptyList(),
    child: (@Composable BoxScope.() -> Unit)? = null
) {
    val resolvedHeight = hyperComponentSize(size, 40.dp, HyperNavBarDefaults.Height, 52.dp)
    require(spacing >= 0.dp && titleSpacing >= 0.dp && actionSpacing >= 0.dp) { "间距不能为负数" }
    require(child == null || (titleContent == null && subtitleContent == null && navigationContent == null && trailingContent == null && actions.isEmpty())) {
        "child 接管整行布局，不能同时设置其他内容插槽。"
    }
    require(trailingContent == null || actions.isEmpty()) { "trailingContent 与 actions 二选一" }

    val safeInsets = if (safeArea) WindowInsets.statusBars.only(WindowInsetsSides.Top) else WindowInsets(0, 0, 0, 0)
    Column(modifier = modifier.windowInsetsPadding(safeInsets).zIndex(HyperNavBarDefaults.ZIndex)) {
        Box(modifier = Modifier.fillMaxWidth().height(resolvedHeight).padding(padding)) {
            if (child != null) {
                child.invoke(this)
            } else {
                HyperNavBarSlots(
                    titleContent = titleContent,
                    subtitleContent = subtitleContent,
                    navigationContent = navigationContent,
                    trailingContent = trailingContent ?: actionsSlot(actions, actionSpacing),
                    spacing = spacing,
                    titleSpacing = titleSpacing,
                    centerTitle = centerTitle
                )
            }
        }
    }
}


/** 透明导航栏的全面屏页面容器；顶部净空会随滚动内容一起滚出。 */
@Composable
fun HyperNavBarPage(
    navBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    navBarSize: String = "default",
    safeArea: Boolean = true,
    bottomSafeArea: Boolean = true,
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    val density = LocalDensity.current
    val topInset = if (safeArea) with(density) {
        WindowInsets.statusBars.getTop(this).toDp()
    } else 0.dp
    val bottomInset = if (bottomSafeArea) with(density) {
        WindowInsets.safeDrawing.getBottom(this).toDp()
    } else 0.dp
    val resolvedPadding = contentPadding.withAdditionalTop(
        topInset + hyperComponentSize(navBarSize, 40.dp, HyperNavBarDefaults.Height, 52.dp)
    )
        .withAdditionalBottom(bottomInset)
    Box(modifier.fillMaxSize()) {
        content(resolvedPadding)
        Box(Modifier.fillMaxWidth().zIndex(HyperNavBarDefaults.ZIndex)) { navBar() }
    }
}

@Composable
private fun HyperNavBarSlots(
    titleContent: (@Composable () -> Unit)?,
    subtitleContent: (@Composable () -> Unit)?,
    navigationContent: (@Composable RowScope.() -> Unit)?,
    trailingContent: (@Composable RowScope.() -> Unit)?,
    spacing: Dp,
    titleSpacing: Dp,
    centerTitle: Boolean
) {
    val hasTitle = titleContent != null || subtitleContent != null
    CompositionLocalProvider(LocalHyperContentColor provides HyperColors.primaryText) {
        if (centerTitle) {
            Box(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                    navigationContent?.invoke(this)
                    Box(Modifier.weight(1f))
                    trailingContent?.invoke(this)
                }
                if (hasTitle) {
                    Box(Modifier.align(Alignment.Center)) {
                        HyperNavBarTitle(titleContent, subtitleContent, titleSpacing, true)
                    }
                }
            }
        } else {
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                navigationContent?.invoke(this)
                if (navigationContent != null && hasTitle) Box(Modifier.width(spacing))
                if (hasTitle) {
                    Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        HyperNavBarTitle(titleContent, subtitleContent, titleSpacing, false)
                    }
                } else {
                    Box(Modifier.weight(1f))
                }
                if (trailingContent != null && hasTitle) Box(Modifier.width(spacing))
                trailingContent?.invoke(this)
            }
        }
    }
}

@Composable
private fun HyperNavBarTitle(
    titleContent: (@Composable () -> Unit)?,
    subtitleContent: (@Composable () -> Unit)?,
    titleSpacing: Dp,
    centered: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(titleSpacing),
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start
    ) {
        titleContent?.let {
            CompositionLocalProvider(LocalHyperTextStyle provides HyperNavBarDefaults.TitleTextStyle) {
                Box(Modifier.semantics { heading() }) { it() }
            }
        }
        subtitleContent?.let {
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
