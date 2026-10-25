/** 文件职责：使用 Compose 基础布局承载页面、上下栏和悬浮操作，不依赖 Material。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp

private enum class PageSlot { TopBar, BottomBar, FloatingAction, Content }

/** 正文自行消费 contentPadding；栏高按实际测量值计算，安全区只在缺少对应栏时补入。 */
@Composable
fun HyperScaffold(
    modifier: Modifier = Modifier,
    containerColor: Color = HyperColors.pageBackground,
    contentWindowInsets: WindowInsets = HyperTheme.systemSafeAreaInsets,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val direction = LocalLayoutDirection.current
    val insets = contentWindowInsets.asPaddingValues()
    SubcomposeLayout(modifier.background(containerColor)) { constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val top = subcompose(PageSlot.TopBar) { Box { topBar() } }.single().measure(loose)
        val bottom = subcompose(PageSlot.BottomBar) { Box { bottomBar() } }.single().measure(loose)
        val startInset = insets.calculateStartPadding(direction)
        val endInset = insets.calculateEndPadding(direction)
        val bottomClearance = if (bottom.height > 0) bottom.height.toDp() else insets.calculateBottomPadding()
        val padding = PaddingValues(
            start = startInset,
            top = if (top.height > 0) top.height.toDp() else insets.calculateTopPadding(),
            end = endInset,
            bottom = bottomClearance
        )
        val body = subcompose(PageSlot.Content) { Box { content(padding) } }.single().measure(loose)
        val action = subcompose(PageSlot.FloatingAction) { Box { floatingActionButton() } }.single().measure(
            loose.copy(maxWidth = (width - startInset.roundToPx() - endInset.roundToPx()).coerceAtLeast(0))
        )
        layout(width, height) {
            body.placeRelative(0, 0)
            top.placeRelative(0, 0)
            bottom.placeRelative(0, height - bottom.height)
            if (action.width > 0 && action.height > 0) {
                val margin = 16.dp.roundToPx()
                action.placeRelative(
                    (width - endInset.roundToPx() - action.width - margin).coerceAtLeast(0),
                    (height - bottomClearance.roundToPx() - action.height - margin).coerceAtLeast(0)
                )
            }
        }
    }
}
