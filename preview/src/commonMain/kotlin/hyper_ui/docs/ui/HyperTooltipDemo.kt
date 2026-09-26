package hyper_ui.docs.ui

import androidx.compose.runtime.Composable
import hyper_ui.HyperText
import hyper_ui.HyperTooltip

@Composable
internal fun HyperTooltipDemo() {
    HyperTooltip(text = "这是一个提示") {
        HyperText("悬停查看提示")
    }
}
