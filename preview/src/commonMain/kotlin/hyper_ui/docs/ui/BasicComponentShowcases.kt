/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/BasicComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hyper_ui.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ButtonDemo() {
    var clicks by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PreviewSectionLabel("文字按钮 · 按内容收缩")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton({ clicks += 1 }, size = "small") { HyperText("小按钮") }
            HyperButton({ clicks += 1 }) { HyperText("默认按钮") }
            HyperButton({ clicks += 1 }, size = "large") { HyperText("大按钮") }
            HyperButton({}, loading = true) { HyperText("处理中") }
        }
        PreviewSectionLabel("视觉变体与图标")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton({ clicks += 1 }, type = "filled") { HyperText("主操作") }
            HyperButton({ clicks += 1 }) { HyperIcon(HyperReferenceIcons.Search, null); HyperText("查找") }
            HyperButton({ clicks = 0 }, type = "danger") { HyperIcon(HyperReferenceIcons.Delete, null); HyperText("删除") }
            HyperButton({ clicks += 1 }, type = "tonal") { HyperIcon(HyperReferenceIcons.Settings, null); HyperText("设置") }
            HyperButton({ clicks += 1 }) { HyperText("稍后处理") }
        }
        PreviewSectionLabel("独立图标入口")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton({ clicks += 1 }, type = "icon") { HyperIcon(HyperReferenceIcons.Search, "搜索") }
            HyperButton({ clicks += 1 }, type = "icon") { HyperIcon(HyperReferenceIcons.Notifications, "通知") }
            HyperButton({ clicks += 1 }, type = "icon", loading = loading) { HyperIcon(HyperReferenceIcons.Refresh, "刷新") }
            HyperButton({}, type = "icon", enabled = false) { HyperIcon(HyperReferenceIcons.Lock, "不可用") }
        }
        PreviewSectionLabel("通栏与交互状态")
        PreviewToggle("加载中", loading) { loading = it }
        HyperButton({ clicks += 1 }, modifier = Modifier.fillMaxWidth(), loading = loading, type = "filled") { HyperText("通栏按钮") }
        HyperButton({}, enabled = false) { HyperText("禁用操作") }
        HyperText("点击次数：$clicks", size = "small", color = HyperColors.secondaryText)
    }
}

@Composable
fun TextDemo() {
    var size by remember { mutableStateOf("default") }
    var emphasis by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PreviewSectionLabel("文字尺寸")
        HyperText("Large 大号文字", size = "large")
        HyperText("Default 默认文字")
        HyperText("Small 辅助文字", size = "small")
        PreviewSectionLabel("颜色与可操作状态")
        HyperSegmentedControl(listOf("small", "default", "large"), size, { size = it }) { HyperText(it) }
        PreviewToggle("强调色", emphasis) { emphasis = it }
        HyperText("参考排版 · HyperUI", size = size, color = if (emphasis) HyperColors.accent else Color.Unspecified)
        HyperText("small 12sp / default 14sp / large 18sp，行高均为字号的 1.4 倍。", size = "small")
    }
}

@Composable
fun IconDemo() {
    var size by remember { mutableStateOf("default") }
    var colored by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperSegmentedControl(listOf("small", "default", "large"), size, { size = it }) { HyperText(it) }
        PreviewToggle("品牌色", colored) { colored = it }
        PreviewSectionLabel("常用语义图标")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf(HyperReferenceIcons.Home, HyperReferenceIcons.Search, HyperReferenceIcons.Settings, HyperReferenceIcons.Check).forEach {
                HyperIcon(it, "图标示例", size = size, tint = if (colored) HyperColors.accent else HyperColors.primaryText)
            }
        }
        PreviewSectionLabel("尺寸对照")
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            listOf("small", "default", "large").forEach { HyperIcon(HyperReferenceIcons.Info, "尺寸 $it", size = it) }
        }
    }
}

@Composable
fun DividerDemo() {
    var dashed by remember { mutableStateOf(false) }
    var vertical by remember { mutableStateOf(false) }
    var inset by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PreviewSectionLabel("实线与虚线")
        HyperText("实线分隔")
        HyperDivider()
        HyperText("虚线分隔")
        HyperDivider(type = "dashed")
        PreviewSectionLabel("纵向分隔")
        Row(Modifier.height(58.dp), verticalAlignment = Alignment.CenterVertically) {
            HyperText("左侧", modifier = Modifier.weight(1f))
            HyperDivider(orientation = "vertical")
            HyperText("右侧", modifier = Modifier.weight(1f).padding(start = 14.dp))
        }
        PreviewSectionLabel("可操作变体")
        PreviewToggle("虚线", dashed) { dashed = it }
        PreviewToggle("纵向", vertical) { vertical = it }
        PreviewToggle("缩进 14dp", inset) { inset = it }
        HyperDivider(type = if (dashed) "dashed" else "solid", orientation = if (vertical) "vertical" else "horizontal",
            indent = if (inset) 14.dp else 0.dp, endIndent = if (inset) 14.dp else 0.dp,
            length = if (vertical) 80.dp else null)
    }
}
