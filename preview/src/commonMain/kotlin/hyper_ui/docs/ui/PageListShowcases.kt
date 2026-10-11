/** 文件职责：页面级列表占用有限的剩余视口；外层宿主不再套垂直滚动或固定列表高度。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import hyper_ui.*

@Composable
fun HyperListDemo() {
    val items = remember { listOf("系统设置", "通知权限", "同步策略", "安全中心", "账户资料", "登录设备", "隐私权限", "数据备份", "帮助与反馈", "关于应用") }
    var rounded by remember { mutableStateOf(true) }
    var comfortable by remember { mutableStateOf(false) }
    var padded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<String?>(null) }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.fillMaxWidth()) {
            PreviewToggle("圆角容器", rounded) { rounded = it }
            PreviewToggle("宽松行间距", comfortable) { comfortable = it }
            PreviewToggle("滚动内容留白", padded) { padded = it }
        }
        // weight 保留有限高度，LazyColumn 独自负责剩余空间的滚动。
        HyperList(
            modifier = Modifier.weight(1f),
            contentPadding = if (padded) PaddingValues(horizontal = 4.dp, vertical = 24.dp) else HyperListDefaults.ContentPadding,
            shape = if (rounded) HyperListDefaults.Shape else RectangleShape
        ) {
            itemsIndexed(items, key = { _, item -> item }, contentType = { _, _ -> "setting" }) { index, item ->
                HyperListTile(
                    selected = selected == item,
                    contentModifier = Modifier.padding(horizontal = 14.dp, vertical = if (comfortable) 14.dp else 8.dp),
                    leadingIcon = when (item) {
                        "通知权限" -> HyperReferenceIcons.Notifications
                        "同步策略", "数据备份" -> HyperReferenceIcons.Refresh
                        "安全中心", "隐私权限" -> HyperReferenceIcons.Lock
                        else -> HyperReferenceIcons.Settings
                    },
                    headlineContent = { HyperText(item) },
                    supportingContent = { HyperText(if (selected == item) "当前选中" else "点击查看配置") },
                    dividerVisible = index < items.lastIndex,
                    onClick = { selected = item }
                )
            }
        }
    }
}

private data class PreviewFileSection(val id: String, val title: String, val entries: List<String>)

@Composable
fun HyperSectionedListDemo() {
    val groups = remember {
        listOf(
            PreviewFileSection("today", "今天", listOf("项目周报.docx", "会议记录.txt", "设计稿.pdf")),
            PreviewFileSection("yesterday", "昨天", listOf("需求清单.xlsx", "发布说明.md", "接口文档.pdf", "测试记录.txt")),
            PreviewFileSection("earlier", "更早", listOf("迭代计划.docx", "预算表.xlsx", "操作指南.pdf", "归档说明.md"))
        )
    }
    var compact by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<String?>(null) }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            PreviewToggle(if (compact) "紧凑分段间距" else "标准分段间距", compact) { compact = it }
        }
        HyperSectionedList(
            modifier = Modifier.weight(1f),
            sections = groups,
            items = { it.entries },
            sectionKey = { "section-${it.id}" },
            itemKey = { group, entry -> "file-${group.id}-$entry" },
            sectionSpacing = if (compact) 8.dp else HyperSectionedListDefaults.SectionSpacing,
            headerContent = { HyperText(it.title) }
        ) { _, entry ->
            HyperListTile(
                selected = selected == entry,
                leadingIcon = HyperReferenceIcons.Lock,
                headlineContent = { HyperText(entry) },
                supportingContent = { HyperText(if (selected == entry) "已选中" else "点击切换选中状态") },
                onClick = { selected = if (selected == entry) null else entry },
                trailingContent = {
                    HyperCheckbox(selected == entry, { selected = if (it == true) entry else null })
                }
            )
        }
    }
}
