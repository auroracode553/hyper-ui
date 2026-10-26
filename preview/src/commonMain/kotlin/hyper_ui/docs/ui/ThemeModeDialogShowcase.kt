package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import hyper_ui.HyperButton
import hyper_ui.HyperText
import hyper_ui.HyperThemeModeDialog

/** 直接演示正式组件的三选项、当前值与选择后关闭行为。 */
@Composable
fun ThemeModeDialogDemo() {
    var visible by remember { mutableStateOf(false) }
    var mode by remember { mutableStateOf<Boolean?>(null) }
    Column {
        HyperText("当前主题：${when (mode) { null -> "跟随系统"; false -> "浅色"; true -> "深色" }}")
        HyperButton(onPressed = { visible = true }) { HyperText("主题设置") }
    }
    HyperThemeModeDialog(visible = visible, value = mode,
        onChanged = { mode = it }, onClose = { visible = false })
}
