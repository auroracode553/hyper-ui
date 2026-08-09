/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/FormComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.CheckboxDemo
import hyper_ui.docs.ui.RadioDemo
import hyper_ui.docs.ui.SearchFieldDemo
import hyper_ui.docs.ui.SliderDemo
import hyper_ui.docs.ui.SwitchDemo
import hyper_ui.docs.ui.TextFieldDemo

private const val GROUP_FORM = "表单组件"

internal fun formComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "radio",
        group = GROUP_FORM,
        title = "HyperRadioButton",
        description = "单选按钮组件，选中状态由调用方维护。",
        code = """
            HyperRadioButton(
                selected = selected,
                onClick = onSelect
            )
        """.trimIndent(),
        content = { RadioDemo() }
    ),
    ComponentDemo(
        id = "checkbox",
        group = GROUP_FORM,
        title = "HyperCheckbox",
        description = "复选框组件，支持选中、未选中和禁用状态。",
        code = """
            HyperCheckbox(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        """.trimIndent(),
        content = { CheckboxDemo() }
    ),
    ComponentDemo(
        id = "text_field",
        group = GROUP_FORM,
        title = "HyperTextField",
        description = "Slot-first 输入框，默认使用不透明输入背景和轻描边，label、placeholder、supporting、leading、trailing 均由调用方渲染。",
        code = """
            HyperTextField(
                value = value,
                onValueChange = { value = it },
                labelContent = { Text("备注") },
                placeholderContent = { Text("写一点说明") },
                supportingContent = { Text("${'$'}{value.length}/80") },
                singleLine = false,
                minLines = 3,
                maxLines = 5
            )
        """.trimIndent(),
        content = { TextFieldDemo() }
    ),
    ComponentDemo(
        id = "search",
        group = GROUP_FORM,
        title = "HyperTextField Search Pattern",
        description = "搜索框、地址栏和页内查找栏都使用 HyperTextField 的 leading/trailing slot 组合。",
        code = """
            HyperTextField(
                value = keyword,
                onValueChange = { keyword = it },
                placeholderContent = { Text("搜索组件") },
                leadingContent = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingContent = {
                    HyperIconButton(onClick = { keyword = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "清空")
                    }
                }
            )
        """.trimIndent(),
        content = { SearchFieldDemo() }
    ),
    ComponentDemo(
        id = "switch",
        group = GROUP_FORM,
        title = "HyperSwitch",
        description = "开关组件，轨道和滑块默认带轮廓层次，适合二元状态设置。",
        code = """
            HyperSwitch(
                checked = enabled,
                onCheckedChange = { enabled = it }
            )
        """.trimIndent(),
        content = { SwitchDemo() }
    ),
    ComponentDemo(
        id = "slider",
        group = GROUP_FORM,
        title = "HyperSlider",
        description = "支持点击定位、连续拖动、分段吸附和禁用态，业务值与范围由调用方持有。",
        code = """
            HyperSlider(
                value = progress,
                onValueChange = { progress = it },
                valueRange = 0f..duration,
                onValueChangeStarted = onSeekStart,
                onValueChangeFinished = onSeekFinished
            )
        """.trimIndent(),
        content = { SliderDemo() }
    )
)
