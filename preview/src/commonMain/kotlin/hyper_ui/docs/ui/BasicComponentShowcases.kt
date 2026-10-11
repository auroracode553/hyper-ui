/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/BasicComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ButtonDemo() {
    var clicks by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HyperButton(
                onClick = { clicks += 1 },
                size = "small"
            ) {
                HyperText(text = "小按钮")
            }
            HyperButton(onClick = { clicks += 1 }, size = "large") { HyperText("大按钮 44dp") }
            HyperButton(onClick = { clicks += 1 }) {
                HyperText(text = "默认按钮")
            }
            HyperButton(
                onClick = {},
                loading = true,
                type = "outline"
            ) {
                HyperText(text = "处理中")
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HyperButton(
                onClick = { clicks += 1 },
                type = "outline"
            ) {
                HyperText(text = "导出")
            }
            HyperButton(
                onClick = { clicks = 0 },
                type = "danger"
            ) {
                HyperText(text = "删除")
            }
            HyperButton(onClick = { clicks += 1 }, type = "tonal") {
                HyperIcon(HyperReferenceIcons.Search, contentDescription = null, modifier = Modifier)
                HyperText(text = "带图标胶囊", softWrap = false, maxLines = 1)
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HyperButton(type = "icon", onClick = { clicks += 1 }) {
                HyperIcon(HyperReferenceIcons.Search, contentDescription = "搜索", modifier = Modifier)
            }
            HyperButton(type = "icon", onClick = { clicks += 1 }) {
                HyperIcon(HyperReferenceIcons.Notifications, contentDescription = "通知", modifier = Modifier)
            }
            HyperButton(type = "icon", onClick = { clicks = 0 }) {
                HyperIcon(HyperReferenceIcons.Delete, contentDescription = "删除", modifier = Modifier)
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HyperButton(onClick = {}, enabled = false, type = "outline") {
                HyperText("禁用")
            }
            HyperButton(
                onClick = { clicks += 1 },
                size = "small",
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                HyperText(text = "小尺寸 slot", fontSize = 13.sp)
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HyperButton({ loading = !loading }, type = "ghost") { HyperText("切换加载") }
            HyperButton({ clicks += 1 }, loading = loading) { HyperText("提交") }
            HyperButton({ clicks += 1 }, type = "icon", loading = loading) { HyperIcon(HyperReferenceIcons.Search, "搜索") }
        }
        HyperText(
            text = "点击次数：$clicks · 按住观察缩放，拖出按钮再松开可取消点击",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun TextDemo() {
    var size by remember { mutableStateOf("default") }
    var emphasis by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperSegmentedControl(listOf("small", "default", "large"), size, { size = it }) { HyperText(it) }
        HyperButton({ emphasis = !emphasis }, type = "ghost") { HyperText("切换强调色") }
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
        HyperButton({ colored = !colored }, type = "ghost") { HyperText("切换着色") }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf(HyperReferenceIcons.Home, HyperReferenceIcons.Search, HyperReferenceIcons.Settings).forEach {
                HyperIcon(it, "图标示例", size = size, tint = if (colored) HyperColors.accent else HyperColors.primaryText)
            }
        }
        HyperText("18 / 24 / 32dp；按钮、列表和导航插槽继承各自的参考图标尺寸。", size = "small")
    }
}

@Composable
fun DividerDemo() {
    var dashed by remember { mutableStateOf(false) }
    var vertical by remember { mutableStateOf(false) }
    var inset by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        HyperButton({ dashed = !dashed }, type = "ghost") { HyperText(if (dashed) "虚线" else "实线") }
        HyperButton({ vertical = !vertical }, type = "ghost") { HyperText("切换方向") }
        HyperButton({ inset = !inset }, type = "ghost") { HyperText("切换 14dp 缩进") }
        HyperDivider(type = if (dashed) "dashed" else "solid", orientation = if (vertical) "vertical" else "horizontal",
            indent = if (inset) 14.dp else 0.dp, endIndent = if (inset) 14.dp else 0.dp,
            length = if (vertical) 80.dp else null)
    }
}
