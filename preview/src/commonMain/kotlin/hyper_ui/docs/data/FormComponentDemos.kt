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
        title = "HyperSegmented",
        description = "等宽分段控制器；轨道负责布局，每个分段直接复用 HyperButton，调用方维护选中项。",
        code = """
            HyperSegmented(
                items = periods,
                selectedItem = selectedPeriod,
                onSelected = { selectedPeriod = it }
            ) { period ->
                Text(period.label)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认", "selectedItem", "玻璃轨道与标准 HyperButton 分段"),
            DemoVariant("禁用项", "itemEnabled", "单独禁用指定分段"),
            DemoVariant("自定义颜色", "HyperSegmentedDefaults.colors", "覆盖选中项和内容色")
        ),
        apiDocumentPaths = listOf("form/hyper-segmented.md"),
        content = { SegmentedDemo() }
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
        description = "紧凑型 Slot-first 澎湃 OS 风格输入框。浅色主题使用纯白单色表面并复用公共结构阴影，不叠加材质渐变。",
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
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "清空")
                    }
                }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认表面", "white container", "浅色主题纯白底面，无渐变或纹理色带"),
            DemoVariant("统一单行", "MinHeight = 40.dp", "地址栏、首页搜索与搜索态共享默认高度"),
            DemoVariant("聚焦", "interactionSource focused", "单一主题色纯色边缘与 3dp 公共结构阴影"),
            DemoVariant("默认光标", "selection = TextRange(value.length)", "首次聚焦位于现有文本末尾"),
            DemoVariant("多行/错误", "minLines = 3, isError", "supporting 与单一错误边缘"),
            DemoVariant("禁用/只读", "enabled / readOnly", "禁用态关闭公共阴影，只读态抑制聚焦强调"),
            DemoVariant("左右插槽", "startContent / endContent", "自定义单色表面、搜索图标与清除操作")
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
