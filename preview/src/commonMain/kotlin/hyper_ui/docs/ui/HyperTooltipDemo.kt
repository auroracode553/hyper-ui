/** 文件职责：演示提示的悬停/长按/焦点触发、禁用、延迟与长文本宽度。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import hyper_ui.*

@Composable
internal fun HyperTooltipDemo() {
    var enabled by remember { mutableStateOf(true) }
    var longText by remember { mutableStateOf(false) }
    var delayMillis by remember { mutableStateOf(500L) }
    var clicks by remember { mutableStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperButton({ enabled = !enabled }, type = "outline") { HyperText(if (enabled) "禁用提示" else "启用提示") }
        HyperButton({ longText = !longText }, type = "outline") { HyperText("切换长文本") }
        HyperButton({ delayMillis = if (delayMillis == 500L) 0L else 500L }, type = "outline") {
            HyperText("悬停延迟 ${delayMillis}ms")
        }
        HyperTooltip(if (longText) "这是较长的补充提示，最大宽度为 240dp，内容会自动换行。" else "这是一个提示",
            enabled = enabled, hoverDelayMillis = delayMillis) {
            HyperButton({ clicks += 1 }, type = "outline") { HyperText("悬停或长按查看") }
        }
        HyperText("点击次数：$clicks；提示保留子控件点击，键盘焦点即时显示。", size = "small")
    }
}
