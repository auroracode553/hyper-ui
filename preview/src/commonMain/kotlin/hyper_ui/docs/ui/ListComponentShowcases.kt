/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/ListComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*
import hyper_ui.LocalHyperContentColor
import hyper_ui.docs.theme.LocalDocsColorScheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperCheckbox
import hyper_ui.HyperList
import hyper_ui.HyperListDefaults
import hyper_ui.HyperListTile
import hyper_ui.HyperListTileDefaults
import hyper_ui.HyperMenuGroup
import hyper_ui.HyperRadio
import hyper_ui.HyperSectionedList
import hyper_ui.HyperSectionedListDefaults
import hyper_ui.HyperSwitch

@Composable
fun HyperMenuGroupDemo() {
    val items = listOf("系统设置", "通知权限", "同步策略", "安全中心")
    var pushEnabled by remember { mutableStateOf(true) }
    var autoSync by remember { mutableStateOf(false) }
    var performanceMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperText(
            text = "菜单组 radius 18dp、padding 4dp，条目最小 46dp；组内自动使用 plain 行",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 13.sp
        )
        Box(modifier = Modifier.height(220.dp)) {
            HyperMenuGroup(items = items) { item ->
                HyperListTile(
                    leadingIcon = iconFor(item),
                    headlineContent = { ListTitle(item) },
                    supportingContent = { ListDescription("点击查看配置") },
                    dividerVisible = true,
                    dividerModifier = Modifier.padding(start = 70.dp)
                )
            }
        }

        HyperMenuGroup {
            HyperListTile(
                leadingIcon = HyperReferenceIcons.Notifications,
                headlineContent = { HyperText("推送通知") },
                supportingContent = { HyperText("接收系统消息提醒") },
                dividerVisible = true,
                dividerModifier = Modifier.padding(start = 70.dp),
                trailingContent = {
                    HyperSwitch(
                        checked = pushEnabled,
                        onCheckedChange = { pushEnabled = it }
                    )
                }
            )
            HyperListTile(
                leadingIcon = HyperReferenceIcons.Check,
                headlineContent = { HyperText("自动同步") },
                supportingContent = { HyperText("网络可用时自动刷新数据") },
                dividerVisible = true,
                dividerModifier = Modifier.padding(start = 70.dp),
                trailingContent = {
                    HyperCheckbox(
                        checked = autoSync,
                        onCheckedChange = { autoSync = it == true }
                    )
                }
            )
            HyperListTile(
                leadingIcon = HyperReferenceIcons.Settings,
                headlineContent = { HyperText("性能模式") },
                supportingContent = { HyperText("优先保证流畅度") },
                trailingContent = {
                    HyperRadio(
                        selected = performanceMode,
                        onClick = { performanceMode = true }
                    )
                }
            )
        }
    }
}

@Composable
fun HyperListDemo() {
    val items = listOf(
        "系统设置",
        "通知权限",
        "同步策略",
        "安全中心",
        "账户资料",
        "登录设备",
        "隐私权限",
        "数据备份"
    )
    var roundedCorners by remember { mutableStateOf(true) }
    var comfortableContent by remember { mutableStateOf(false) }
    var contentPaddingEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HyperText(
                text = if (roundedCorners) "轻圆角开启" else "直角容器",
                color = LocalHyperContentColor.current,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            HyperSwitch(
                checked = roundedCorners,
                onCheckedChange = { roundedCorners = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HyperText(
                text = if (comfortableContent) "宽松条目间距" else "默认自适应间距",
                color = LocalHyperContentColor.current,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            HyperSwitch(
                checked = comfortableContent,
                onCheckedChange = { comfortableContent = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HyperText(
                text = if (contentPaddingEnabled) "可滚动内容留白" else "无内容留白",
                color = LocalHyperContentColor.current,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            HyperSwitch(
                checked = contentPaddingEnabled,
                onCheckedChange = { contentPaddingEnabled = it }
            )
        }

        Box(modifier = Modifier.height(280.dp)) {
            HyperList(
                contentModifier = Modifier.padding(vertical = 4.dp),
                contentPadding = if (contentPaddingEnabled) {
                    PaddingValues(vertical = 24.dp)
                } else {
                    HyperListDefaults.ContentPadding
                },
                shape = if (roundedCorners) HyperListDefaults.Shape else RectangleShape
            ) {
                itemsIndexed(
                    items = items,
                    key = { _, item -> item },
                    contentType = { _, _ -> "setting-item" }
                ) { index, item ->
                    HyperListTile(
                        contentModifier = if (comfortableContent) {
                            Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        } else {
                            Modifier.padding(HyperListTileDefaults.ContentPadding)
                        },
                        leadingIcon = iconFor(item),
                        headlineContent = { ListTitle(item) },
                        supportingContent = { ListDescription("点击查看配置") },
                        dividerVisible = index < items.lastIndex,
                        dividerModifier = Modifier.padding(start = 70.dp)
                    )
                }
            }
        }
    }
}

private data class SectionedListPreviewGroup(
    val id: String,
    val title: String,
    val entries: List<String>
)

@Composable
fun HyperSectionedListDemo() {
    val groups = remember {
        listOf(
            SectionedListPreviewGroup(
                id = "today",
                title = "今天",
                entries = listOf("项目周报.docx", "会议记录.txt", "设计稿.pdf")
            ),
            SectionedListPreviewGroup(
                id = "yesterday",
                title = "昨天",
                entries = listOf("需求清单.xlsx", "发布说明.md")
            )
        )
    }
    var compactSections by remember { mutableStateOf(false) }
    var selectedEntry by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HyperText(
                text = if (compactSections) "紧凑分段间距" else "标准分段间距",
                color = LocalHyperContentColor.current,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            HyperSwitch(
                checked = compactSections,
                onCheckedChange = { compactSections = it }
            )
        }

        Box(modifier = Modifier.height(340.dp)) {
            HyperSectionedList(
                sections = groups,
                items = { group -> group.entries },
                sectionKey = { group -> "preview-section-${group.id}" },
                itemKey = { group, entry -> "preview-item-${group.id}-$entry" },
                sectionSpacing = if (compactSections) {
                    8.dp
                } else {
                    HyperSectionedListDefaults.SectionSpacing
                },
                headerContent = { group -> HyperText(group.title) }
            ) { _, entry ->
                HyperListTile(
                    onClick = { selectedEntry = entry },
                    leadingIcon = iconFor(entry),
                    headlineContent = { ListTitle(entry) },
                    supportingContent = { ListDescription("点击切换选中状态") },
                    trailingContent = {
                        HyperCheckbox(
                            checked = selectedEntry == entry,
                            onCheckedChange = { checked ->
                                selectedEntry = if (checked == true) entry else null
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ListIcon(imageVector: ImageVector) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LocalDocsColorScheme.current.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        HyperIcon(
            imageVector = imageVector,
            contentDescription = null,
            tint = LocalDocsColorScheme.current.primary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ListTitle(text: String) {
    HyperText(
        text = text,
        color = LocalHyperContentColor.current,
        fontSize = 14.sp,
        lineHeight = 17.5.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ListDescription(text: String) {
    HyperText(
        text = text,
        color = LocalHyperContentColor.current,
        fontSize = 12.sp,
        lineHeight = 16.2.sp
    )
}

private fun iconFor(item: String): ImageVector = when (item) {
    "系统设置" -> HyperReferenceIcons.Settings
    "通知权限" -> HyperReferenceIcons.Notifications
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
        HyperButton({ selected = !selected }, type = "ghost") { HyperText("切换选中") }
        HyperButton({ enabled = !enabled }, type = "ghost") { HyperText("切换禁用") }
        HyperListTile(type = type, selected = selected, enabled = enabled, leadingIcon = HyperReferenceIcons.Settings,
            headlineContent = { HyperText("系统设置") }, supportingContent = { HyperText("查看配置与同步状态") },
            metaContent = { HyperText("刚刚更新") }, onClick = { feedback = "已点击：$type" })
        HyperCard(titleContent = { HyperText("auto 在卡片内") }) {
            HyperListTile(headlineContent = { HyperText("继承已有表面") }, leadingIcon = HyperReferenceIcons.Check,
                selected = selected, onClick = { feedback = "已点击卡片内条目" })
        }
        HyperText(feedback, size = "small")
    }
}
