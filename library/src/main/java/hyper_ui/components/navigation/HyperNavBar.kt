/** 文件职责：协调透明导航栏与可滚动内容，提供内容上滚至系统状态栏后的沉浸式布局。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Immutable
data class HyperNavBarColors(
    val containerColor: Color,
    val contentColor: Color
)

/**
 * 透明沉浸式顶部导航布局。
 *
 * 导航栏固定在最上层，默认背景透明，由页面容器统一提供底色；组件自身不绘制描边和阴影。
 * 组件测量导航栏与状态栏安全区的总高度；可选 [headerContent] 同样固定并计入测量。组件通过
 * [content] 的 [PaddingValues] 返回首屏顶部净空。调用方应把该值设置为 `LazyColumn.contentPadding`，
 * 或设置在 `verticalScroll` 之后的内容 Padding 上，使净空可以随内容一起滚出屏幕，内容随后自然
 * 绘制到导航栏和状态栏后方。
 *
 * 组件只处理 Compose 布局，不修改宿主窗口的 edge-to-edge 配置或系统栏图标明暗。
 */
@Composable
fun HyperNavBar(
    titleContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    navBarModifier: Modifier = Modifier,
    colors: HyperNavBarColors = HyperNavBarDefaults.colors(),
    windowInsets: WindowInsets = WindowInsets.statusBars,
    contentPadding: PaddingValues = HyperNavBarDefaults.ContentPadding,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperNavBarDefaults.ContentGap),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null,
    headerContent: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    SubcomposeLayout(modifier = modifier.fillMaxSize()) { constraints ->
        val navBarPlaceable = subcompose(HyperNavBarSlot.NavBar) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(NAV_BAR_Z_INDEX)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(windowInsets)
                        .then(navBarModifier)
                        .defaultMinSize(minHeight = HyperNavBarDefaults.MinHeight)
                        .hyperSurface(
                            containerColor = colors.containerColor,
                            shape = HyperNavBarDefaults.Shape
                        )
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = horizontalArrangement,
                    verticalAlignment = verticalAlignment
                ) {
                    CompositionLocalProvider(LocalHyperContentColor provides colors.contentColor) {
                        navigationContent?.invoke(this)
                        CompositionLocalProvider(LocalHyperTextStyle provides HyperNavBarDefaults.TitleTextStyle) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = verticalAlignment,
                                content = titleContent
                            )
                        }
                        actionContent?.invoke(this)
                    }
                }
                headerContent?.invoke(this)
            }
        }.single().measure(constraints.copy(minHeight = 0))

        val resolvedContentPadding = contentPadding.withAdditionalTop(
            additionalTop = navBarPlaceable.height.toDp()
        )
        val contentPlaceable = subcompose(HyperNavBarSlot.Content) {
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

object HyperNavBarDefaults {
    val MinHeight = 56.dp
    val ContentGap = 8.dp
    val ContentPadding: PaddingValues = PaddingValues(0.dp)
    val Shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)

    val TitleTextStyle: TextStyle
        @Composable get() = HyperTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.SemiBold
        )

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified
    ): HyperNavBarColors = HyperNavBarColors(
        containerColor = resolveHyperContainerColor(containerColor, Color.Transparent),
        contentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText)
    )
}

private const val NAV_BAR_Z_INDEX = 1f

private enum class HyperNavBarSlot {
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
