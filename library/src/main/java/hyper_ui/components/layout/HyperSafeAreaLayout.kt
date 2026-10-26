/** 公共安全区布局；背景、IME 避让和业务控件高度由原宿主负责。 */
package hyper_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class HyperSafeAreaEdge { Top, Bottom }

object HyperSafeAreaDefaults {
    val MinimumBottomSpacing = 20.dp
}

// 文档预览模拟窗口几何；正式应用默认直接读取当前窗口，不维护应用级高度缓存。
internal val LocalHyperSafeAreaWindowInsets = staticCompositionLocalOf<WindowInsets?> { null }
internal val LocalHyperSafeAreaPortraitOverride = staticCompositionLocalOf<Boolean?> { null }

@Composable
internal fun hyperSafeAreaInsets(edge: HyperSafeAreaEdge): WindowInsets {
    val simulated = LocalHyperSafeAreaWindowInsets.current
    val portrait = LocalHyperSafeAreaPortraitOverride.current ?: isHyperSafeAreaPortrait()
    val minimumBottom = if (portrait) HyperSafeAreaDefaults.MinimumBottomSpacing else 0.dp
    return when (edge) {
        HyperSafeAreaEdge.Top -> (simulated ?: WindowInsets.statusBars.union(WindowInsets.displayCutout))
            .only(WindowInsetsSides.Top)
        HyperSafeAreaEdge.Bottom -> (simulated ?: WindowInsets.navigationBars.union(WindowInsets.displayCutout))
            .only(WindowInsetsSides.Bottom)
            .union(WindowInsets(bottom = minimumBottom))
    }
}

/** 独立于根安全区开关；Compose 消费同侧 Insets，嵌套容器不会重复增加系统区或最小留白。 */
@Composable
fun HyperSafeArea(
    edge: HyperSafeAreaEdge = HyperSafeAreaEdge.Bottom,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier.windowInsetsPadding(hyperSafeAreaInsets(edge)), content = content)
}
