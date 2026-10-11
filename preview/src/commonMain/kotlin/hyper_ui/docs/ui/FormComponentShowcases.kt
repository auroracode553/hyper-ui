/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/FormComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*
import hyper_ui.LocalHyperContentColor
import hyper_ui.docs.theme.LocalDocsColorScheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperCheckbox
import hyper_ui.HyperRadio
import hyper_ui.HyperSegmentedControl
import hyper_ui.HyperSegmentedControlDefaults
import hyper_ui.HyperSlider
import hyper_ui.HyperSliderDefaults
import hyper_ui.HyperSwitch
import hyper_ui.HyperTextField

@Composable
fun RadioDemo() {
    var mode by remember { mutableStateOf("balanced") }

    FormControlGroup {
        FormControlOption(
            text = "均衡模式",
            onClick = { mode = "balanced" }
        ) {
            HyperRadio(
                selected = mode == "balanced",
                onClick = { mode = "balanced" }
            )
        }
        FormControlOption(
            text = "性能模式",
            onClick = { mode = "performance" }
        ) {
            HyperRadio(
                selected = mode == "performance",
                onClick = { mode = "performance" }
            )
        }
        FormControlOption(
            text = "禁用选项",
            enabled = false
        ) {
            HyperRadio(
                selected = false,
                onClick = null,
                enabled = false
            )
        }
    }
}

@Composable
fun SegmentedDemo() {
    val periods = remember { listOf("日", "周", "月", "年") }
    var selectedPeriod by remember { mutableStateOf("年") }
    var equalWidth by remember { mutableStateOf(true) }
    val modes = remember { listOf("轻量", "标准", "停用") }
    var selectedMode by remember { mutableStateOf("标准") }

    Column(
        modifier = Modifier.widthIn(max = 520.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HyperButton({ equalWidth = !equalWidth }, type = "ghost") {
            HyperText(if (equalWidth) "等宽分段" else "内容宽度分段")
        }
        HyperSegmentedControl(
            equalWidth = equalWidth,
            items = periods,
            selectedItem = selectedPeriod,
            onSelected = { selectedPeriod = it }
        ) { period ->
            HyperText(
                text = period,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
        HyperText(
            text = "当前周期：$selectedPeriod",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 13.sp
        )

        HyperSegmentedControl(
            items = modes,
            selectedItem = selectedMode,
            onSelected = { selectedMode = it },
            itemEnabled = { it != "停用" },
            colors = HyperSegmentedControlDefaults.colors(
                selectedItemColor = Color(0.03f, 0.76f, 0.38f, 1f),
                selectedContentColor = Color(1f, 1f, 1f, 1f)
            )
        ) { mode ->
            HyperText(
                text = mode,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun CheckboxDemo() {
    var checkedA by remember { mutableStateOf(true) }
    var checkedB by remember { mutableStateOf(false) }
    var triState by remember { mutableStateOf<Boolean?>(null) }

    FormControlGroup {
        FormControlOption(
            text = "备选项 A",
            onClick = { checkedA = !checkedA }
        ) {
            HyperCheckbox(
                checked = checkedA,
                onCheckedChange = { checkedA = it == true }
            )
        }
        FormControlOption(
            text = "备选项 B",
            onClick = { checkedB = !checkedB }
        ) {
            HyperCheckbox(
                checked = checkedB,
                onCheckedChange = { checkedB = it == true }
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            HyperCheckbox(triState, { triState = it }, tristate = true)
            HyperText("三态：${triState ?: "不确定"}")
        }
        FormControlOption(
            text = "禁用选项",
            enabled = false
        ) {
            HyperCheckbox(
                checked = true,
                onCheckedChange = {},
                enabled = false
            )
        }
    }
}

@Composable
fun TextFieldDemo() {
    var value by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("password") }
    var note by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("default") }
    var enabled by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }
    var readOnly by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperSegmentedControl(listOf("small", "default", "large"), size, { size = it }) { HyperText(it) }
        HyperButton({ enabled = !enabled }, type = "ghost") { HyperText("切换禁用") }
        HyperButton({ error = !error }, type = "ghost") { HyperText("切换错误") }
        HyperButton({ readOnly = !readOnly }, type = "ghost") { HyperText("切换只读") }
        HyperTextField(value, { value = it }, size = size, clearable = true, enabled = enabled, isError = error, readOnly = readOnly,
            inputModifier = Modifier.focusRequester(focusRequester), labelContent = { HyperText("基础输入与清空") },
            placeholderContent = { HyperText("请输入内容") })
        HyperTextField(password, { password = it }, type = "password", size = size, showPasswordToggle = true,
            labelContent = { HyperText("密码显隐") }, placeholderContent = { HyperText("请输入密码") })
        HyperTextField(note, { note = it }, type = "textarea", size = size, rows = 3, maxlength = 80, showWordLimit = true,
            labelContent = { HyperText("多行输入") }, placeholderContent = { HyperText("请输入多行内容") })
        HyperButton({ focusRequester.requestFocus() }, size = "small") { HyperText("查看聚焦") }
    }
}

@Composable
fun SwitchDemo() {
    var notifications by remember { mutableStateOf(true) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            HyperText("受控开关", color = LocalDocsColorScheme.current.onSurfaceVariant, fontSize = 12.sp)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                HyperText("接收消息通知", fontSize = 14.sp)
                HyperSwitch(checked = notifications, onCheckedChange = { notifications = it })
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            HyperText("禁用状态", color = LocalDocsColorScheme.current.onSurfaceVariant, fontSize = 12.sp)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                HyperText("禁用 · 开启", color = LocalDocsColorScheme.current.onSurfaceVariant, fontSize = 14.sp)
                HyperSwitch(checked = true, onCheckedChange = {}, enabled = false)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                HyperText("禁用 · 关闭", color = LocalDocsColorScheme.current.onSurfaceVariant, fontSize = 14.sp)
                HyperSwitch(checked = false, onCheckedChange = {}, enabled = false)
            }
        }
    }
}

@Composable
fun SliderDemo() {
    var continuousValue by remember { mutableStateOf(42f) }
    var steppedValue by remember { mutableStateOf(2f) }
    var showSegmentMarkers by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.widthIn(max = 520.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HyperText(text = "紧凑连续进度 ${continuousValue.toInt()}%")
        HyperSlider(
            value = continuousValue,
            onValueChange = { continuousValue = it },
            modifier = Modifier.fillMaxWidth(),
            showSegmentMarkers = false,
            showValue = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HyperText(text = "分段进度 ${steppedValue.toInt()}/5")
            HyperButton(onClick = { showSegmentMarkers = !showSegmentMarkers }) {
                HyperText(if (showSegmentMarkers) "隐藏分段点" else "显示分段点")
            }
        }
        HyperSlider(
            value = steppedValue,
            onValueChange = { steppedValue = it },
            valueRange = 0f..5f,
            steps = 4,
            showSegmentMarkers = showSegmentMarkers,
            modifier = Modifier.fillMaxWidth(),
            colors = HyperSliderDefaults.colors(
                activeTrackColor = Color(0.03f, 0.76f, 0.38f, 1f)
            )
        )

        HyperText(text = "只读分段 · 指定主刻度")
        HyperSlider(
            value = 3f,
            onValueChange = {},
            readOnly = true,
            valueRange = 0f..5f,
            showSegmentMarkers = true,
            segmentValues = listOf(0f, 1f, 3f, 5f),
            modifier = Modifier.fillMaxWidth()
        )

        HyperText(text = "禁用状态")
        HyperSlider(
            value = 65f,
            onValueChange = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FieldLabel(text: String) {
    HyperText(
        text = text,
        color = LocalHyperContentColor.current,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
}

@Composable
private fun FieldPlaceholder(text: String) {
    HyperText(
        text = text,
        color = LocalHyperContentColor.current,
        fontSize = 16.sp,
        lineHeight = 22.sp
    )
}

@Composable
private fun FieldSupporting(text: String) {
    HyperText(
        text = text,
        color = LocalHyperContentColor.current,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
}

@Composable
private fun FormControlGroup(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.widthIn(max = 360.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        content = { content() }
    )
}

@Composable
private fun FormControlOption(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    control: @Composable RowScope.() -> Unit
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable(enabled = enabled, onClick = onClick)
    } else {
        Modifier
    }
    Row(
        modifier = modifier.then(clickModifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            content = control
        )
        HyperText(
            text = text,
            color = if (enabled) {
                LocalDocsColorScheme.current.onSurface
            } else {
                LocalDocsColorScheme.current.onSurfaceVariant
            },
            fontSize = 15.sp,
            lineHeight = 20.sp
        )
    }
}
