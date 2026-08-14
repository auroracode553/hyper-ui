/** 文件职责：为 Desktop/Wasm Preview 提供 HyperDialog 的跨平台窗口宿主。 */
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
