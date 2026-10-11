/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/dialog/HyperAlertDialog 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun HyperAlertDialog(
    visible: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissOnClickOutside: Boolean = true,
    content: (@Composable ColumnScope.() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    HyperDialog(
        visible = visible,
        onClose = onClose,
        title = title,
        modifier = modifier,
        dismissOnClickOutside = dismissOnClickOutside,
        actions = actions
    ) {
        val dialogColumnScope = this

        content?.let { body ->
            CompositionLocalProvider(LocalHyperContentColor provides HyperColors.primaryText) {
                body.invoke(dialogColumnScope)
            }
        }
    }
}
