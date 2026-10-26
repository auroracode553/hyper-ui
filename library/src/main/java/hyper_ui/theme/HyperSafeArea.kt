/** 全局系统安全区策略：默认全面屏，键盘 Insets 独立保留给输入宿主。 */
package hyper_ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalHyperUseSystemSafeArea = staticCompositionLocalOf { false }

val HyperTheme.systemSafeAreaInsets: WindowInsets
    @Composable get() = if (LocalHyperUseSystemSafeArea.current) {
        WindowInsets.safeDrawing.exclude(WindowInsets.ime)
    } else WindowInsets(0, 0, 0, 0)

/** 仅保护顶部操作区；不作为根布局 Padding，正文视口仍覆盖状态栏。 */
val HyperTheme.topBarInsets: WindowInsets
    @Composable get() = WindowInsets.statusBars.union(WindowInsets.displayCutout)
        .only(WindowInsetsSides.Top)
