/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/FormComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.CheckboxDemo
import hyper_ui.docs.ui.RadioDemo
import hyper_ui.docs.ui.SegmentedDemo
import hyper_ui.docs.ui.SliderDemo
import hyper_ui.docs.ui.SwitchDemo
import hyper_ui.docs.ui.TextFieldDemo

private const val GROUP_FORM = "表单组件"

internal fun formComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "radio",
        group = GROUP_FORM,
        title = "HyperRadio",
        description = "单选按钮组件，选中状态由调用方维护。",
        code = """
            HyperRadio(
                selected = selected,
                onClick = onSelect
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("选中", "selected = true", "主题实色与内部圆点"),
            DemoVariant("未选中", "selected = false", "实色容器与主题描边"),
            DemoVariant("禁用", "enabled = false", "禁用实色状态")
        ),
        apiDocumentPaths = listOf("form/hyper-radio.md"),
        content = { RadioDemo() }
    ),
    ComponentDemo(
        id = "segmented",
        group = GROUP_FORM,
        title = "HyperSegmentedControl",
        description = "默认总高 40dp 的等宽或内容宽度分段控制器；轨道负责布局，每个分段直接复用 HyperButton。",
        code = """
            HyperSegmentedControl(
                items = periods,
                selectedItem = selectedPeriod,
                onSelected = { selectedPeriod = it }
            ) { period ->
                Text(period.label)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("紧凑默认", "Height = 36.dp", "32dp 分段与 2dp 轨道留白"),
            DemoVariant("禁用项", "itemEnabled", "单独禁用指定分段"),
            DemoVariant("自定义颜色", "HyperSegmentedControlDefaults.colors", "覆盖选中项和内容色")
        ),
        apiDocumentPaths = listOf("form/hyper-segmented-control.md"),
        content = { SegmentedDemo() }
    ),
    ComponentDemo(
        id = "checkbox",
        group = GROUP_FORM,
        title = "HyperCheckbox",
        description = "使用 Lucide check 默认图标的复选框，支持选中、未选中和禁用状态。",
        code = """
            HyperCheckbox(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("选中", "checked = true", "主题实色与 Lucide check 图标"),
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
        description = "受控文本输入框：预览覆盖基础、清空、密码显隐、前后缀、多行计数、禁用、只读和错误态。",
        code = """
            HyperTextField(
                value = value,
                onValueChange = { value = it },
                labelContent = { Text("备注") },
                placeholderContent = { Text("写一点说明") },
                supportingContent = { Text("${'$'}{value.length}/80") },
                type = "textarea",
                rows = 3
            )

            HyperTextField(
                value = keyword,
                onValueChange = { keyword = it },
                placeholderContent = { Text("搜索组件") },
                startContent = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                endContent = {
                    HyperButton(
                        type = "icon",
                        onClick = { keyword = "" },
                        size = "small"
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "清空")
                    }
                }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认表面", "68.dp 圆角描边容器", "白色输入面、低对比度中性描边"),
            DemoVariant("type 驱动", "type = text / textarea / password", "同一组件切换单行、多行和密码形态"),
            DemoVariant("聚焦", "interactionSource focused", "主题色替换公共中性描边，阴影提高到 5dp"),
            DemoVariant("默认光标", "selection = TextRange(value.length)", "首次聚焦位于现有文本末尾"),
            DemoVariant("多行/错误", "type = Textarea, rows = 3, isError", "错误色替换公共中性描边"),
            DemoVariant("禁用/只读", "enabled / readOnly", "禁用态保留弱描边并关闭阴影，只读态抑制聚焦强调"),
            DemoVariant("左右插槽", "startContent / endContent", "搜索图标与清除操作"),
            DemoVariant("清空与密码", "endContent / visualTransformation", "清空按钮与密码显隐由调用方状态控制")
        ),
        apiDocumentPaths = listOf("form/hyper-text-field.md"),
        content = { TextFieldDemo() }
    ),
    ComponentDemo(
        id = "switch",
        group = GROUP_FORM,
        title = "HyperSwitch",
        description = "受控玻璃开关；关闭态使用清晰的中性轨道和细描边，预览覆盖开启、关闭与禁用状态。",
        code = """
            HyperSwitch(
                checked = enabled,
                onCheckedChange = { enabled = it }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("开启", "checked = true", "主题实色轨道"),
            DemoVariant("关闭", "checked = false", "中性轨道、细描边与固定尺寸滑块"),
            DemoVariant("禁用", "enabled = false", "分别展示禁用开启与禁用关闭")
        ),
        apiDocumentPaths = listOf("form/hyper-switch.md"),
        content = { SwitchDemo() }
    ),
    ComponentDemo(
        id = "slider",
        group = GROUP_FORM,
        title = "HyperSlider",
        description = "支持连续拖动、等距吸附、可选分段点、只读态与三层圆点，业务值由调用方持有。",
        code = """
            HyperSlider(
                value = speed,
                onValueChange = { speed = it },
                valueRange = 0f..5f,
                steps = 4,
                showSegmentMarkers = true
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant(
                "紧凑连续",
                "steps = 0, trackHeight = 3.dp, thumbSize = 12.dp",
                "无分段点的媒体进度条"
            ),
            DemoVariant("分段", "steps = 4, showSegmentMarkers = true", "吸附并显示全部分段点"),
            DemoVariant("指定标记", "segmentValues", "只展示业务主刻度"),
            DemoVariant("只读", "readOnly = true", "保留正常配色但关闭交互"),
            DemoVariant("禁用", "enabled = false", "禁用轨道、标记与三层圆点")
        ),
        apiDocumentPaths = listOf("form/hyper-slider.md"),
        content = { SliderDemo() }
    )
)
