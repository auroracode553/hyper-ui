/** 文件职责：受控更新弹窗；仅派发回调，布局逐项对齐 doc_ui_kit。 */
package hyper_ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HyperUpdateDialog(
    state: HyperUpdateDialogState,
    onDismissRequest: () -> Unit,
    onRetry: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
    texts: HyperUpdateDialogTexts = HyperUpdateDialogTexts(),
    dismissOnClickOutside: Boolean = true,
    mandatory: Boolean = false,
    onInstall: () -> Unit = {},
    onCheck: () -> Unit = onRetry,
    onCancel: () -> Unit = onDismissRequest
) {
    if (state == HyperUpdateDialogState.Idle) return
    val presentation = hyperUpdatePresentation(state, texts)
    val locked = state is HyperUpdateDialogState.Installing ||
        (mandatory && state !is HyperUpdateDialogState.Completed && state !is HyperUpdateDialogState.UpToDate)
    HyperDialog(
        visible = true,
        onDismissRequest = if (locked) ({}) else onDismissRequest,
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        dismissOnBackPress = !locked,
        dismissOnClickOutside = dismissOnClickOutside && !locked,
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        actionContent = if (hyperUpdateHasActions(presentation, locked)) ({
            HyperUpdateActions(presentation, texts, locked, onDismissRequest, onRetry, onDownload, onInstall, onCheck, onCancel)
        }) else null
    ) {
        HyperUpdateBody(presentation, locked, mandatory, onDismissRequest)
    }
}
