package hyper_ui.docs.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import hyper_ui.HyperCheckbox
import hyper_ui.HyperChip
import hyper_ui.HyperChipRow
import hyper_ui.HyperIconButton
import hyper_ui.HyperIconButtonDefaults
import hyper_ui.HyperRadioButton
import hyper_ui.HyperStyleDefaults
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
    val isNoteError = note.length > 80

    Column(
        modifier = Modifier.widthIn(max = 520.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperTextField(
            value = name,
            onValueChange = { name = it },
            labelContent = { FieldLabel("组件名称") },
            placeholderContent = { FieldPlaceholder("请输入名称") }
        )
        HyperTextField(
            value = note,
            onValueChange = { note = it },
            labelContent = { FieldLabel("备注") },
            placeholderContent = { FieldPlaceholder("写一点说明") },
            supportingContent = { FieldSupporting("${note.length}/80") },
            singleLine = false,
            minLines = 3,
            maxLines = 5,
            minHeight = 92.dp,
            isError = isNoteError
        )
        HyperTextField(
            value = "不可编辑内容",
            onValueChange = {},
            labelContent = { FieldLabel("禁用态") },
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
        HyperTextField(
            value = keyword,
            onValueChange = { keyword = it },
            placeholderContent = { FieldPlaceholder("搜索组件") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = LocalContentColor.current,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingContent = if (keyword.isNotEmpty()) {
                {
                    HyperIconButton(
                        onClick = { keyword = "" },
                        size = 32.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "清空搜索",
                            modifier = Modifier.size(HyperIconButtonDefaults.IconSize - 4.dp)
                        )
                    }
                }
            } else {
                null
            }
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
fun FilterChipDemo() {
    val categories = remember {
        listOf("全部", "恶意网址", "广告", "恶意跳转", "打开应用")
    }
    var selected by remember { mutableStateOf("全部") }

    Column(
        modifier = Modifier.widthIn(max = 520.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperChipRow(
            items = categories,
            selectedItem = selected,
            onSelected = { selected = it }
        ) { item ->
            Text(text = item, fontSize = 13.sp)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperChip(
                selected = false,
                onClick = {}
            ) {
                Text(text = "只读样式", fontSize = 13.sp)
            }
            HyperChip(
                selected = false,
                onClick = {},
                enabled = false
            ) {
                Text(text = "禁用", fontSize = 13.sp)
            }
        }
        Text(
            text = "当前选中：$selected",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = LocalContentColor.current,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
}

@Composable
private fun FieldPlaceholder(text: String) {
    Text(
        text = text,
        color = LocalContentColor.current,
        fontSize = 16.sp,
        lineHeight = 22.sp
    )
}

@Composable
private fun FieldSupporting(text: String) {
    Text(
        text = text,
        color = LocalContentColor.current,
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
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha),
            fontSize = 15.sp,
            lineHeight = 20.sp
        )
    }
}
