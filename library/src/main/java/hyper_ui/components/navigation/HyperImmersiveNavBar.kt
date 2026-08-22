/** 文件职责：协调固定导航栏与可滚动内容，提供内容上滚至系统状态栏后的沉浸式布局。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * 沉浸式顶部导航布局。
 *
 * 导航栏固定在顶层，背景沿用 [HyperNavBar] 的透明默认值。组件测量导航栏与状态栏安全区的
 * 总高度；可选 [headerContent] 同样固定并计入测量。组件通过 [content] 的 [PaddingValues]
 * 返回首屏顶部净空。调用方应把该值设置为
 * `LazyColumn.contentPadding`，或设置在 `verticalScroll` 之后的内容 Padding 上，使净空可以
 * 随内容一起滚出屏幕，内容随后自然绘制到导航栏和状态栏后方。
 *
 * 组件只处理 Compose 布局，不修改宿主窗口的 edge-to-edge 配置或系统栏图标明暗。
 */
@Composable
fun HyperImmersiveNavBar(
    titleContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    navBarModifier: Modifier = Modifier,
    colors: HyperNavBarColors = HyperNavBarDefaults.colors(),
    windowInsets: WindowInsets = WindowInsets.statusBars,
    contentPadding: PaddingValues = HyperImmersiveNavBarDefaults.ContentPadding,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperNavBarDefaults.ContentGap),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null,
    headerContent: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    SubcomposeLayout(modifier = modifier.fillMaxSize()) { constraints ->
        val navBarPlaceable = subcompose(HyperImmersiveNavBarSlot.NavBar) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(NAV_BAR_Z_INDEX)
            ) {
                HyperNavBar(
                    titleContent = titleContent,
                    modifier = Modifier
                        .windowInsetsPadding(windowInsets)
                        .then(navBarModifier),
                    colors = colors,
                    horizontalArrangement = horizontalArrangement,
                    verticalAlignment = verticalAlignment,
                    navigationContent = navigationContent,
                    actionContent = actionContent
                )
                headerContent?.invoke(this)
            }
        }.single().measure(constraints.copy(minHeight = 0))

        val resolvedContentPadding = contentPadding.withAdditionalTop(
            additionalTop = navBarPlaceable.height.toDp()
        )
        val contentPlaceable = subcompose(HyperImmersiveNavBarSlot.Content) {
            Box(modifier = Modifier.fillMaxSize()) {
                content(resolvedContentPadding)
            }
        }.single().measure(constraints)

        val layoutWidth = constraints.constrainWidth(
            maxOf(navBarPlaceable.width, contentPlaceable.width)
        )
        val layoutHeight = constraints.constrainHeight(
            maxOf(navBarPlaceable.height, contentPlaceable.height)
        )

        layout(layoutWidth, layoutHeight) {
            contentPlaceable.placeRelative(0, 0)
            navBarPlaceable.placeRelative(0, 0)
        }
    }
}

object HyperImmersiveNavBarDefaults {
    val ContentPadding: PaddingValues = PaddingValues(0.dp)
}

private const val NAV_BAR_Z_INDEX = 1f

private enum class HyperImmersiveNavBarSlot {
    NavBar,
    Content
}

private fun PaddingValues.withAdditionalTop(additionalTop: Dp): PaddingValues =
    object : PaddingValues {
        override fun calculateLeftPadding(layoutDirection: LayoutDirection) =
            this@withAdditionalTop.calculateLeftPadding(layoutDirection)

        override fun calculateTopPadding() =
            this@withAdditionalTop.calculateTopPadding() + additionalTop

        override fun calculateRightPadding(layoutDirection: LayoutDirection) =
            this@withAdditionalTop.calculateRightPadding(layoutDirection)

        override fun calculateBottomPadding() =
            this@withAdditionalTop.calculateBottomPadding()
    }
