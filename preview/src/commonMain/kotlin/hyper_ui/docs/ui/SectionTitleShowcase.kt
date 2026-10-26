package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import hyper_ui.*

/** 页面只提供布局尺寸，标题、设置行、开关和容器均直接用库默认样式。 */
@Composable
fun SectionTitleDemo() {
    var enabled by remember { mutableStateOf(true) }
    Column(Modifier.padding(horizontal = HyperSpacing.PageHorizontal, vertical = HyperSpacing.PageVertical),
        verticalArrangement = Arrangement.spacedBy(HyperSpacing.SectionVertical)) {
        HyperSectionTitle("独立分组标题")
        HyperMenuGroup(title = "通用设置") {
            HyperListTile(title = { HyperText("通知") }, subtitle = { HyperText("接收重要消息") },
                onPressed = { enabled = !enabled },
                trailing = { HyperSwitch(value = enabled, onChanged = { enabled = it }) })
        }
    }
}
