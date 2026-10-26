package hyper_ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

/** 三选项主题弹窗。null 跟随系统，false 浅色，true 深色；保存和应用主题由调用方负责。 */
@Composable
fun HyperThemeModeDialog(
    visible: Boolean,
    value: Boolean?,
    onChanged: (Boolean?) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "主题设置"
) {
    HyperAlertDialog(
        visible = visible,
        onClose = onClose,
        modifier = modifier,
        title = title,
        content = {
            ThemeModeOption("跟随系统", null, value, onChanged, onClose)
            ThemeModeOption("浅色", false, value, onChanged, onClose)
            ThemeModeOption("深色", true, value, onChanged, onClose)
        }
    )
}

@Composable
private fun ThemeModeOption(
    label: String,
    option: Boolean?,
    value: Boolean?,
    onChanged: (Boolean?) -> Unit,
    onClose: () -> Unit
) {
    // 主题选项使用 36dp 紧凑行，不继承普通设置菜单的 46dp 行高；大字体仍可自然增高。
    Row(
        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 36.dp)
            .hyperNoRippleClickable(role = Role.RadioButton, pressedScale = 0.992f, onClick = {
                onChanged(option)
                onClose()
            })
            .semantics { selected = option == value }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HyperText(label, modifier = Modifier.weight(1f).padding(end = 8.dp),
            style = HyperListTileDefaults.HeadlineTextStyle)
        HyperRadio(value = option, groupValue = value,
            modifier = Modifier.clearAndSetSemantics { })
    }
}
