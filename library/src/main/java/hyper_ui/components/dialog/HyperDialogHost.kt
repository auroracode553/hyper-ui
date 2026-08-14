/** 文件职责：在 Android 平台创建无系统调暗层的 Compose Dialog 窗口。 */
package hyper_ui

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

@Composable
internal fun HyperDialogHost(
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            // 根节点固定铺满窗口，外部点击由 HyperDialog 内部的透明命中层处理。
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        )
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
        LaunchedEffect(dialogWindow) {
            // 每个窗口只配置一次，禁止正文重组反复触发 WindowManager 更新。
            dialogWindow?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        content()
    }
}
