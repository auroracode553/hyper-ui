/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/FormComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.CheckboxDemo
import hyper_ui.docs.ui.RadioDemo
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
        variants = listOf(
            DemoVariant("选中", "selected = true", "主题实色与内部圆点"),
            DemoVariant("未选中", "selected = false", "实色容器与主题描边"),
            DemoVariant("禁用", "enabled = false", "禁用实色状态")
        ),
        apiDocumentPaths = listOf("form/hyper-radio-button.md"),
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
        variants = listOf(
            DemoVariant("选中", "checked = true", "主题实色与勾号"),
            DemoVariant("未选中", "checked = false", "实色容器与主题描边"),
            DemoVariant("禁用", "enabled = false", "禁用实色状态")
        ),
        apiDocumentPaths = listOf("form/hyper-checkbox.md"),
        content = { CheckboxDemo() }
    ),
    ComponentDemo(
        id = "text_field",
        group = GROUP_FORM,
        title = "HyperTextField",
        description = "Slot-first 输入框，普通表单、搜索框与地址栏统一使用 startContent/endContent 左右插槽组合。",
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

            HyperTextField(
                value = keyword,
                onValueChange = { keyword = it },
                placeholderContent = { Text("搜索组件") },
                startContent = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                endContent = {
                    HyperIconButton(
                        onClick = { keyword = "" },
                        size = 32.dp
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "清空")
                    }
                }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("标准单行", "singleLine = true", "label 与 placeholder"),
            DemoVariant("多行/错误", "minLines = 3, isError", "supporting 与错误描边"),
            DemoVariant("禁用", "enabled = false", "禁用实色状态"),
            DemoVariant("左右插槽", "startContent / endContent", "搜索图标与清除操作")
        ),
        apiDocumentPaths = listOf("form/hyper-text-field.md"),
        content = { TextFieldDemo() }
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
        variants = listOf(
            DemoVariant("开启", "checked = true", "主题实色轨道"),
            DemoVariant("关闭", "checked = false", "中性实色轨道"),
            DemoVariant("禁用", "enabled = false", "禁用实色轨道与滑块")
        ),
        apiDocumentPaths = listOf("form/hyper-switch.md"),
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
        variants = listOf(
            DemoVariant("连续", "steps = 0", "连续点击与拖动"),
            DemoVariant("分段", "steps = 4", "分段吸附与自定义色"),
            DemoVariant("禁用", "enabled = false", "禁用实色轨道与滑块")
        ),
        apiDocumentPaths = listOf("form/hyper-slider.md"),
        content = { SliderDemo() }
    )
)
