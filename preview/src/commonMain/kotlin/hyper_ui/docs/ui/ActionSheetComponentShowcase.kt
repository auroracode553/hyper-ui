/** 文件职责：同步 Flutter 底部弹层的自定义内容、操作列表与遮罩关闭示例。 */
package hyper_ui.docs.ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.FolderInput
import com.composables.icons.lucide.Link
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.Share2
import com.composables.icons.lucide.Trash2
import com.composables.icons.lucide.UserPlus

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.*

@Composable
fun ActionSheetDemo() {
    var active by remember { mutableStateOf<String?>(null) }
    Column {
        HyperText("自定义内容", size = "small", modifier = Modifier.padding(bottom = 8.dp))
        HyperButton(onPressed = { active = "custom" }) { HyperText("打开底部弹层") }
        Spacer(Modifier.height(20.dp))
        HyperText("操作列表", size = "small", modifier = Modifier.padding(bottom = 8.dp))
        HyperButton(onPressed = { active = "actions" }) { HyperText("打开操作菜单") }
        Spacer(Modifier.height(20.dp))
        HyperText("强制操作（dismissible: false）", size = "small", modifier = Modifier.padding(bottom = 8.dp))
        HyperButton(onPressed = { active = "required" }) { HyperText("打开强制阅读弹层") }
    }
    HyperActionSheet(active == "custom", { active = null }, title = "分享项目") {
        HyperListTile(title = { HyperText("复制链接") }, leadingIcon = Lucide.Link)
        HyperListTile(title = { HyperText("邀请成员") }, leadingIcon = Lucide.UserPlus)
    }
    HyperActionSheet(
        visible = active == "actions", onClose = { active = null }, title = "项目操作",
        actions = listOf(
            HyperAction("rename", "重命名", Lucide.Pencil),
            HyperAction("share", "分享", Lucide.Share2),
            HyperAction("move", "移动（无权限）", Lucide.FolderInput, enabled = false),
            HyperAction("delete", "删除", Lucide.Trash2, type = "danger")
        ), onChanged = { }
    )
    HyperActionSheet(active == "required", { active = null }, title = "服务条款", dismissible = false) {
        HyperText("点击遮罩无法关闭，只能通过按钮确认。")
        Spacer(Modifier.height(16.dp))
        HyperButton(onPressed = { active = null }, modifier = Modifier.fillMaxWidth(),
            type = "filled", colorStyle = "primary") { HyperText("同意并继续") }
    }
}
