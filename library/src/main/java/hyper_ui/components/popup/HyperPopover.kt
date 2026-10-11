/** 文件职责：移植参考 HyperPopover，在真实触发控件旁显示补充内容。 */
package hyper_ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** visible 由调用方持有，anchorContent 负责触发动作与语义。 */
@Composable
fun HyperPopover(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    anchorContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 280.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    require(maxWidth > 0.dp) { "maxWidth 必须大于 0.dp" }
    var anchor by remember { mutableStateOf<Rect?>(null) }
    Box(modifier.onGloballyPositioned { anchor = it.boundsInWindow() }) {
        anchorContent()
        HyperAnchoredSurface(onDismissRequest, maxWidth, Dp.Infinity, PaddingValues(14.dp),
            anchorBounds = anchor, heightFraction = 0.4f, visible = visible, content = content)
    }
}
