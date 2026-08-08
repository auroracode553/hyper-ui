package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HyperAlertDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    titleContent: (@Composable ColumnScope.() -> Unit)? = null,
    bodyContent: (@Composable ColumnScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
) {
    HyperDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        actionContent = actionContent
    ) {
        val dialogColumnScope = this

        titleContent?.let { title ->
            CompositionLocalProvider(LocalContentColor provides HyperColors.primaryText) {
                title.invoke(dialogColumnScope)
            }
        }

        bodyContent?.let { body ->
            CompositionLocalProvider(LocalContentColor provides HyperColors.secondaryText) {
                body.invoke(dialogColumnScope)
            }
        }
    }
}
