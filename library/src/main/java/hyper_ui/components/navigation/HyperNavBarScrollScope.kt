/** 文件职责：观察主滚动容器的实际状态，让导航自动响应恢复、手势和程序滚动。 */
package hyper_ui

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalHyperNavBarScrolledUnder = staticCompositionLocalOf { false }

/**
 * 包住导航与主滚动容器的共同父级。支持 ScrollState、LazyListState 和 LazyGridState；
 * 不接管或重建状态，也不累计手势位移推测位置，嵌套列表不会误驱动主导航。
 */
@Composable
fun HyperNavBarScrollScope(
    scrollState: ScrollableState,
    reverseLayout: Boolean = false,
    content: @Composable () -> Unit
) {
    val scrolledUnder by remember(scrollState, reverseLayout) {
        derivedStateOf {
            if (reverseLayout) scrollState.canScrollForward else scrollState.canScrollBackward
        }
    }
    CompositionLocalProvider(LocalHyperNavBarScrolledUnder provides scrolledUnder, content = content)
}
