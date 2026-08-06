package hyper_ui.docs.data

import hyper_ui.docs.ui.CheckboxDemo
import hyper_ui.docs.ui.RadioDemo
import hyper_ui.docs.ui.SearchFieldDemo
import hyper_ui.docs.ui.SwitchDemo
import hyper_ui.docs.ui.TextFieldDemo

private const val GROUP_FORM = "表单组件"

internal fun formComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "radio",
        group = GROUP_FORM,
        title = "HyperRadioButton",
        description = "单选按钮组件，适合同组互斥选项，选中状态由调用方维护。",
        code = """
            var mode by remember { mutableStateOf("balanced") }

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                HyperRadioButton(
                    selected = mode == "balanced",
                    onClick = { mode = "balanced" }
                )
                HyperRadioButton(
                    selected = mode == "performance",
                    onClick = { mode = "performance" }
                )
                HyperRadioButton(
                    selected = false,
                    onClick = null,
                    enabled = false
                )
            }
        """.trimIndent(),
        content = { RadioDemo() }
    ),
    ComponentDemo(
        id = "checkbox",
        group = GROUP_FORM,
        title = "HyperCheckbox",
        description = "复选框组件，适合多选表单项，支持选中、未选中和禁用状态。",
        code = """
            var checkedA by remember { mutableStateOf(true) }
            var checkedB by remember { mutableStateOf(false) }

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                HyperCheckbox(
                    checked = checkedA,
                    onCheckedChange = { checkedA = it }
                )
                HyperCheckbox(
                    checked = checkedB,
                    onCheckedChange = { checkedB = it }
                )
                HyperCheckbox(
                    checked = true,
                    onCheckedChange = {},
                    enabled = false
                )
            }
        """.trimIndent(),
        content = { CheckboxDemo() }
    ),
    ComponentDemo(
        id = "text_field",
        group = GROUP_FORM,
        title = "HyperTextField",
        description = "Input 输入框，支持标签、占位、禁用态和单行/多行输入。",
        code = """
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
        """.trimIndent(),
        content = { TextFieldDemo() }
    ),
    ComponentDemo(
        id = "search",
        group = GROUP_FORM,
        title = "HyperSearchField",
        description = "搜索输入框，包含聚焦态、占位文案和一键清空按钮。",
        code = """
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
                    text = if (keyword.isBlank()) "当前未输入关键词" else "当前关键词：${'$'}keyword",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        """.trimIndent(),
        content = { SearchFieldDemo() }
    ),
    ComponentDemo(
        id = "switch",
        group = GROUP_FORM,
        title = "HyperSwitch",
        description = "开关组件，适合二元状态设置，默认选中轨道色跟随主题色，支持未选中和禁用状态。",
        code = """
            var enabled by remember { mutableStateOf(true) }
            var quietMode by remember { mutableStateOf(false) }

            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                HyperSwitch(
                    checked = enabled,
                    onCheckedChange = { enabled = it }
                )
                HyperSwitch(
                    checked = quietMode,
                    onCheckedChange = { quietMode = it }
                )
                HyperSwitch(
                    checked = true,
                    onCheckedChange = {},
                    enabled = false
                )
            }
        """.trimIndent(),
        content = { SwitchDemo() }
    )
)
