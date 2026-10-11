/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/ListComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import hyper_ui.HyperCheckbox
import hyper_ui.HyperListTile
import hyper_ui.HyperMenuGroup
import hyper_ui.HyperSwitch

@Composable
fun HyperMenuGroupDemo() {
    var notifications by remember { mutableStateOf(true) }
    var sync by remember { mutableStateOf(false) }
    var feedback by remember { mutableStateOf("选择设置项查看反馈") }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PreviewSectionLabel("设置", "账户与应用偏好")
        HyperMenuGroup(items = listOf("账户与安全", "外观与显示", "关于")) { item ->
            HyperListTile(
                leadingIcon = iconFor(item),
                headlineContent = { HyperText(item) },
                supportingContent = when (item) {
                    "账户与安全" -> ({ HyperText("密码、设备与登录记录") })
                    "外观与显示" -> ({ HyperText("主题、字号与动态效果") })
                    else -> null
                },
                trailingContent = if (item == "关于") ({ HyperText("v1.0.0") }) else null,
                onClick = { feedback = "已选择：$item" }
            )
        }
        PreviewSectionLabel("通知与同步")
        HyperMenuGroup {
            HyperListTile(
                leadingIcon = HyperReferenceIcons.Notifications,
                headlineContent = { HyperText("推送通知") },
                supportingContent = { HyperText("接收重要消息提醒") },
                dividerVisible = true,
                trailingContent = { HyperSwitch(notifications, { notifications = it }) }
            )
            HyperListTile(
                leadingIcon = HyperReferenceIcons.Refresh,
                headlineContent = { HyperText("自动同步") },
                supportingContent = { HyperText("网络可用时刷新数据") },
                trailingContent = { HyperCheckbox(sync, { sync = it == true }) }
            )
        }
        HyperText(feedback, size = "small", color = HyperColors.secondaryText)
    }
}

private fun iconFor(item: String): ImageVector = when (item) {
    "系统设置", "外观与显示" -> HyperReferenceIcons.Settings
    "通知权限" -> HyperReferenceIcons.Notifications
    "关于" -> HyperReferenceIcons.Info
    "同步策略" -> HyperReferenceIcons.Check
    else -> HyperReferenceIcons.Lock
}

@Composable
fun ListTileDemo() {
    var type by remember { mutableStateOf("auto") }
    var selected by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    var feedback by remember { mutableStateOf("等待操作") }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperSegmentedControl(listOf("auto", "plain", "glass"), type, { type = it }) { HyperText(it) }
        PreviewToggle("选中", selected) { selected = it }
        PreviewToggle("允许操作", enabled) { enabled = it }
        PreviewSectionLabel("基础 · 图标、标题、说明与元信息")
        HyperListTile(type = type, selected = selected, enabled = enabled, leadingIcon = HyperReferenceIcons.Settings,
            headlineContent = { HyperText("系统设置") }, supportingContent = { HyperText("查看配置与同步状态") },
            metaContent = { HyperText("刚刚更新") }, onClick = { feedback = "已点击：$type" })
        PreviewSectionLabel("禁用状态")
        HyperListTile(headlineContent = { HyperText("不可用项目") }, enabled = false, onClick = {})
        PreviewSectionLabel("组合 · 继承已有表面")
        HyperCard(titleContent = { HyperText("项目成员") }) {
            HyperListTile(headlineContent = { HyperText("管理员") }, leadingIcon = HyperReferenceIcons.Check,
                selected = selected, onClick = { feedback = "已点击卡片内条目" })
        }
        HyperText(feedback, size = "small")
    }
}
