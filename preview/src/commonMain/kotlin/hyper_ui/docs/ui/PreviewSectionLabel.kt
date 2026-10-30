/** 文件职责：组件示例的分组标题与操作行；只提供文档排版，不绘制额外卡片。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import hyper_ui.HyperColors
import hyper_ui.HyperSwitch
import hyper_ui.HyperText

@Composable
internal fun PreviewSectionLabel(title: String, description: String? = null) {
    Column(Modifier.fillMaxWidth()) {
        HyperText(title, color = HyperColors.secondaryText, fontWeight = FontWeight.Medium)
        if (description != null) HyperText(description, color = HyperColors.secondaryText)
    }
}

@Composable
internal fun PreviewToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HyperText(label, modifier = Modifier.weight(1f))
        HyperSwitch(checked, onCheckedChange)
    }
}
