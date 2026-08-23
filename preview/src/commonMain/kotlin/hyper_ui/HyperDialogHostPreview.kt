/** 文件职责：为 Desktop/Wasm Preview 提供禁用平台默认宽度的 Dialog 宿主。 */
package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

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
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        content = content
    )
}
