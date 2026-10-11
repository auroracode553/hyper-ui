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
            HyperRadio(value = "balanced", groupValue = selectedMode,
                onChanged = { selectedMode = it })
        """.trimIndent(),
        variants = listOf(
            DemoVariant("选中", "value == groupValue", "21dp 圆环，选中环宽 6dp、内部 subtle 表面"),
            DemoVariant("未选中", "value != groupValue", "21dp 圆环，普通轮廓 1.5dp"),
            DemoVariant("禁用", "enabled = false", "禁用实色状态")
        ),
        apiDocumentPaths = listOf("form/hyper-radio.md"),
        content = { RadioDemo() }
    ),
    ComponentDemo(
        id = "segmented",
        group = GROUP_FORM,
        title = "HyperSegmentedControl",
        description = "默认总高 40dp 的等宽或内容宽度分段控制器；共用轨道、选中底板与受控标签插槽。",
        code = """
            HyperSegmentedControl(
                options = periods,
                selectedValue = selectedPeriod,
                onChanged = { selectedPeriod = it }
            ) { period ->
                HyperText(period.label)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认尺寸", "Height = 40.dp", "32dp 分段与 4dp 轨道留白；等宽底板平移 260ms"),
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
                value = checked,
                onChanged = { value = it }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("选中", "checked = true", "主题实色与 Lucide check 图标"),
            DemoVariant("未选中", "checked = false", "21dp 圆环，普通轮廓 1.5dp"),
            DemoVariant("禁用", "enabled = false", "禁用实色状态")
        ),
        apiDocumentPaths = listOf("form/hyper-checkbox.md"),
        content = { CheckboxDemo() }
    ),
    ComponentDemo(
        id = "text_field",
        group = GROUP_FORM,
        title = "HyperTextField",
        description = "分组呈现基础清空、密码显隐、金额前后缀和多行计数；尺寸、禁用、只读与错误状态可操作。",
        code = """
            HyperTextField(value, { value = it }, clearable = true, size = "default",
                placeholder = { HyperText("请输入内容") })
            HyperTextField(password, { password = it }, type = "password", showPasswordToggle = true)
        """.trimIndent(),
        variants = listOf(
            DemoVariant("尺寸", "size", "14/16/18sp；圆角 20dp，padding 按档位联动"),
            DemoVariant("聚焦", "inputModifier / interactionSource", "即时 1.5dp 主题描边，无额外阴影"),
            DemoVariant("状态", "enabled / readOnly / isError", "禁用、只读与错误可切换"),
            DemoVariant("内置操作", "clearable / showPasswordToggle", "30dp 控件、18dp Lucide 图标"),
            DemoVariant("多行计数", "type / rows / maxLength / showWordLimit", "3 行输入与字符限制")
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
                value = enabled,
                onChanged = { enabled = it }
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
        description = "6dp 轨道与 20dp 圆形手柄，支持连续拖动、等距吸附、可选分段点和只读态，业务值由调用方持有。",
        code = """
            HyperSlider(
                value = speed,
                onChanged = { speed = it },
                min = 0f, max = 5f,
                divisions = 5,
                showSegmentMarkers = true
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant(
                "默认连续",
                "min = 0f, max = 100f, showValue = true",
                "44dp 触摸高度、6dp 轨道、20dp 手柄、2dp 描边"
            ),
            DemoVariant("分段", "divisions = 5, showSegmentMarkers = true", "吸附并显示全部分段点"),
            DemoVariant("指定标记", "segmentValues", "只展示业务主刻度"),
            DemoVariant("只读", "readOnly = true", "保留正常配色但关闭交互"),
            DemoVariant("禁用", "enabled = false", "保留品牌轨道，手柄使用 muted 底色")
        ),
        apiDocumentPaths = listOf("form/hyper-slider.md"),
        content = { SliderDemo() }
    )
)
