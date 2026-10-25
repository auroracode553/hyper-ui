/** 文件职责：提供关闭窗口动画且禁用平台默认宽度的 Android Compose Dialog 宿主。 */
package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

@Composable
internal fun HyperDialogHost(
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    edgeToEdge: Boolean = false,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            // 使用稳定的全宽宿主，避免 WRAP_CONTENT Window 首帧重新定位。
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = LocalHyperUseSystemSafeArea.current && !edgeToEdge,
        )
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
        // 遮罩由 Compose 绘制，以便 Android 和 Preview 共用令牌和退出进度。
        androidx.compose.runtime.SideEffect { dialogWindow?.setDimAmount(0f) }
        remember(dialogWindow) {
            // 这里必须同步设置非零的空动画样式：0 会允许 PhoneWindow 回退到系统/厂商主题。
            dialogWindow?.setWindowAnimations(R.style.HyperDialogNoWindowAnimation)
            Unit
        }

        content()
    }
}
