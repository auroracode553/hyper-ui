package hyper_ui.docs.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.*

@Composable
fun RadioDemo() {
    var mode by remember { mutableStateOf("balanced") }

    FormControlGroup {
        FormControlOption(
            text = "均衡模式",
            onClick = { mode = "balanced" }
        ) {
            HyperRadioButton(
                selected = mode == "balanced",
                onClick = { mode = "balanced" }
            )
        }
        FormControlOption(
            text = "性能模式",
            onClick = { mode = "performance" }
        ) {
            HyperRadioButton(
                selected = mode == "performance",
                onClick = { mode = "performance" }
            )
        }
        FormControlOption(
            text = "禁用选项",
            enabled = false
        ) {
            HyperRadioButton(
                selected = false,
                onClick = null,
                enabled = false
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
    var name by remember { mutableStateOf("HyperUI") }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.widthIn(max = 520.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperTextField(
            value = name,
            onValueChange = { name = it },
            label = "组件名称",
            placeholder = "请输入名称"
        )
        HyperTextField(
            value = note,
            onValueChange = { note = it },
            label = "备注",
            placeholder = "写一点说明",
            singleLine = false,
            minHeight = 92.dp
        )
        HyperTextField(
            value = "不可编辑内容",
            onValueChange = {},
            label = "禁用态",
            enabled = false
        )
    }
}

@Composable
fun SearchFieldDemo() {
    var keyword by remember { mutableStateOf("HyperUI") }

    Column(
        modifier = Modifier.widthIn(max = 520.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperSearchField(
            value = keyword,
            onValueChange = { keyword = it },
            placeholder = "搜索组件"
        )
        HyperSearchField(
            value = "",
            onValueChange = {},
            placeholder = "空状态"
        )
        Text(
            text = if (keyword.isBlank()) "当前未输入关键词" else "当前关键词：$keyword",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun SwitchDemo() {
    var enabled by remember { mutableStateOf(true) }
    var quietMode by remember { mutableStateOf(false) }

    FormControlGroup {
        FormControlOption(
            text = "启用状态",
            onClick = { enabled = !enabled }
        ) {
            HyperSwitch(
                checked = enabled,
                onCheckedChange = { enabled = it }
            )
        }
        FormControlOption(
            text = "勿扰模式",
            onClick = { quietMode = !quietMode }
        ) {
            HyperSwitch(
                checked = quietMode,
                onCheckedChange = { quietMode = it }
            )
        }
        FormControlOption(
            text = "禁用状态",
            enabled = false
        ) {
            HyperSwitch(
                checked = true,
                onCheckedChange = {},
                enabled = false
            )
        }
    }
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
    val contentAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha

    Row(
        modifier = modifier.then(clickModifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            content = control
        )
        Text(
            text = text,
            color = HyperColors.primaryText.copy(alpha = contentAlpha),
            fontSize = 15.sp,
            lineHeight = 20.sp
        )
    }
}
