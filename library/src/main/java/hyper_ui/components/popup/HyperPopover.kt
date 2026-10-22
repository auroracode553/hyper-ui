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

/** visible 由调用方持有，child 提供锚点并自行处理触发动作。 */
@Composable
fun HyperPopover(
    visible: Boolean,
    onClose: () -> Unit,
    child: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 280.dp,
    width: Dp? = null,
    scrollable: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    require(maxWidth > 0.dp && maxWidth.value.isFinite()) { "maxWidth 必须为有限正数" }
    require(width == null || (width > 0.dp && width.value.isFinite())) { "width 必须为有限正数" }
    // 180dp 是默认内容宽度；maxWidth 只限制上限，不再把每个短菜单拉到 280dp。
    val preferredWidth = (width ?: 180.dp).coerceAtMost(maxWidth)
    var anchor by remember { mutableStateOf<Rect?>(null) }
    Box(modifier.onGloballyPositioned { anchor = it.boundsInWindow() }) {
        child()
        HyperAnchoredSurface(onClose, preferredWidth, Dp.Infinity, PaddingValues(14.dp),
            anchorBounds = anchor, heightFraction = 0.4f, visible = visible,
            scrollable = scrollable, content = content)
    }
}
