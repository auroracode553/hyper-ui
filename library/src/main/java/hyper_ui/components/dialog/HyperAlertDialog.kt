/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/dialog/HyperAlertDialog 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun HyperAlertDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissOnClickOutside: Boolean = true,
    bodyContent: (@Composable ColumnScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
) {
    HyperDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        title = title,
        modifier = modifier,
        dismissOnClickOutside = dismissOnClickOutside,
        actionContent = actionContent
    ) {
        val dialogColumnScope = this

        bodyContent?.let { body ->
            CompositionLocalProvider(LocalContentColor provides HyperColors.secondaryText) {
                body.invoke(dialogColumnScope)
            }
        }
    }
}
