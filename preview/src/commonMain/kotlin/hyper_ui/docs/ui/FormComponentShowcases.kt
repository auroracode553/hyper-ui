/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/FormComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*
import hyper_ui.LocalHyperContentColor
import hyper_ui.docs.theme.LocalDocsColorScheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperCheckbox
import hyper_ui.HyperButton
import hyper_ui.HyperIconButton
import hyper_ui.HyperIconButtonDefaults
import hyper_ui.HyperRadio
import hyper_ui.HyperSegmented
import hyper_ui.HyperSegmentedDefaults
import hyper_ui.HyperSlider
import hyper_ui.HyperSliderDefaults
import hyper_ui.HyperSwitch
import hyper_ui.HyperTextField
import hyper_ui.HyperTextFieldDefaults

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
        HyperSegmented(
            equalWidth = equalWidth,
            items = periods,
            selectedItem = selectedPeriod,
            onSelected = { selectedPeriod = it }
        ) { period ->
            HyperText(
                text = period,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
        HyperText(
            text = "当前周期：$selectedPeriod",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 13.sp
        )

        HyperSegmented(
            items = modes,
            selectedItem = selectedMode,
            onSelected = { selectedMode = it },
            itemEnabled = { it != "停用" },
            colors = HyperSegmentedDefaults.colors(
                selectedItemColor = Color(0.03f, 0.76f, 0.38f, 1f),
                selectedContentColor = Color(1f, 1f, 1f, 1f)
            )
        ) { mode ->
            HyperText(
                text = mode,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
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

    FormControlGroup {
        FormControlOption(
            text = "备选项 A",
            onClick = { checkedA = !checkedA }
        ) {
            HyperCheckbox(
                checked = checkedA,
                onCheckedChange = { checkedA = it }
            )
        }
        FormControlOption(
            text = "备选项 B",
            onClick = { checkedB = !checkedB }
        ) {
            HyperCheckbox(
                checked = checkedB,
                onCheckedChange = { checkedB = it }
            )
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
    var name by remember { mutableStateOf("") }
    var clearableValue by remember { mutableStateOf("示例内容") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("") }
    var keyword by remember { mutableStateOf("") }
    var errorValue by remember { mutableStateOf("") }
    var forceNoteError by remember { mutableStateOf(false) }
    val nameFocusRequester = remember { FocusRequester() }
    val isNoteError = forceNoteError || note.length > 80

    Column(
        modifier = Modifier.widthIn(max = 320.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HyperTextField(
            value = name,
            onValueChange = { name = it },
            labelContent = { FieldLabel("基础输入") },
            placeholderContent = { FieldPlaceholder("请输入内容") },
            inputModifier = Modifier.focusRequester(nameFocusRequester)
        )
        HyperTextField(
            value = clearableValue,
            onValueChange = { clearableValue = it },
            labelContent = { FieldLabel("可清空") },
            placeholderContent = { FieldPlaceholder("请输入内容") },
            endContent = if (clearableValue.isNotEmpty()) {{
                HyperIconButton(onClick = { clearableValue = "" }, size = "small") {
                    HyperIcon(Icons.Default.Close, contentDescription = "清空内容", modifier = Modifier.size(18.dp))
                }
            }} else null
        )
        HyperTextField(
            value = password,
            onValueChange = { password = it },
            labelContent = { FieldLabel("密码显隐") },
            placeholderContent = { FieldPlaceholder("请输入密码") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            endContent = {
                HyperButton(
                    onClick = { passwordVisible = !passwordVisible },
                    type = "ghost",
                    size = "small"
                ) { HyperText(if (passwordVisible) "隐藏" else "显示") }
            }
        )
        HyperTextField(
            value = keyword,
            onValueChange = { keyword = it },
            labelContent = { FieldLabel("前区与后区插槽") },
            placeholderContent = { FieldPlaceholder("搜索组件") },
            startContent = {
                HyperIcon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp))
            },
            endContent = {
                HyperIconButton(onClick = { keyword = "" }, size = "small") {
                    HyperIcon(Icons.Default.Close, contentDescription = "清空搜索", modifier = Modifier.size(18.dp))
                }
            }
        )
        HyperTextField(
            value = note,
            onValueChange = { note = it.take(80) },
            labelContent = { FieldLabel("多行输入") },
            placeholderContent = { FieldPlaceholder("请输入多行内容") },
            supportingContent = { FieldSupporting("${note.length}/80") },
            type = "textarea",
            rows = 3,
            inputModifier = Modifier.heightIn(min = 92.dp),
            isError = isNoteError
        )
        HyperTextField(value = "禁用内容", onValueChange = {},
            labelContent = { FieldLabel("禁用态") }, enabled = false)
        HyperTextField(
            value = "只读内容",
            onValueChange = {},
            labelContent = { FieldLabel("只读态") },
            readOnly = true
        )
        HyperTextField(value = errorValue, onValueChange = { errorValue = it },
            labelContent = { FieldLabel("错误态") },
            placeholderContent = { FieldPlaceholder("请输入有效内容") },
            isError = true)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton(onClick = { nameFocusRequester.requestFocus() }) { HyperText("查看聚焦") }
            HyperButton(onClick = { forceNoteError = !forceNoteError }) {
                HyperText(if (forceNoteError) "关闭多行错误" else "多行错误态")
            }
        }
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
    var continuousValue by remember { mutableStateOf(0.42f) }
    var steppedValue by remember { mutableStateOf(2f) }
    var showSegmentMarkers by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.widthIn(max = 520.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HyperText(text = "紧凑连续进度 ${(continuousValue * 100).toInt()}%")
        HyperSlider(
            value = continuousValue,
            onValueChange = { continuousValue = it },
            modifier = Modifier.fillMaxWidth(),
            showSegmentMarkers = false,
            trackHeight = 3.dp,
            thumbSize = 12.dp
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
            value = 0.65f,
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
