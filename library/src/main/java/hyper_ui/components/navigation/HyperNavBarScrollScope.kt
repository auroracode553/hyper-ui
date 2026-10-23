/** 文件职责：观察主滚动容器的实际状态，让导航自动响应恢复、手势和程序滚动。 */
package hyper_ui

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf

// 传递稳定的 State 引用，仅由 Navbar 读取值，避免滚动阈值变化使整页重组。
internal val LocalHyperNavBarScrolledUnder = staticCompositionLocalOf<State<Boolean>?> { null }

/**
 * 包住导航与主滚动容器的共同父级。支持 ScrollState、LazyListState 和 LazyGridState；
 * 不接管或重建状态，也不累计手势位移推测位置，嵌套列表不会误驱动主导航。
 */
@Composable
fun HyperNavBarScrollScope(
    scrollState: ScrollableState,
    reverseLayout: Boolean = false,
    active: Boolean = true,
    content: @Composable () -> Unit
) {
    val activeState = rememberUpdatedState(active)
    val scrolledUnder = remember(scrollState, reverseLayout) {
        derivedStateOf {
            activeState.value &&
                (if (reverseLayout) scrollState.canScrollForward else scrollState.canScrollBackward)
        }
    }
    CompositionLocalProvider(LocalHyperNavBarScrolledUnder provides scrolledUnder, content = content)
}
