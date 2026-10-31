/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/BasicComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data
import hyper_ui.*
import hyper_ui.docs.ui.*

private const val GROUP_BASIC = "基础组件"

internal fun basicComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "button",
        group = GROUP_BASIC,
        title = "HyperButton",
        description = "对齐 Flutter ButtonsExample 的按钮排列、文案、图标与状态；type 控制形态，color / colorStyle 控制配色。",
        code = """
            HyperButton(onPressed = onSave) {
                HyperIcon(searchIcon, contentDescription = null)
                HyperText("搜索")
            }

            HyperButton(
                onPressed = onDelete,
                type = "filled",
                colorStyle = "danger"
            ) {
                HyperText("删除")
            }

            HyperButton(onPressed = onSearch, type = "icon") {
                HyperIcon(searchIcon, contentDescription = "搜索")
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认按钮", "省略 type", "标准描边胶囊表面"),
            DemoVariant("主操作", "type = filled / colorStyle = primary", "品牌色胶囊表面"),
            DemoVariant("语义配色", "colorStyle", "primary/success/info/warning/danger，与显示类型独立"),
            DemoVariant("自定义颜色", "color", "优先于 colorStyle，作为显示类型使用的基色"),
            DemoVariant("处理中", "loading = true", "加载期间不可重复点击"),
            DemoVariant("次级操作", "省略 type", "默认白色描边"),
            DemoVariant("危险操作", "type = filled / colorStyle = danger", "危险语义色"),
            DemoVariant("按压与取消", "pointer down / cancel", "按下立即开始 85ms 缩放和透明度过渡；释放或拖出后 180ms 恢复"),
            DemoVariant("禁用", "enabled = false", "保留单个禁用按钮示例，不重复展示外观相同的配色"),
            DemoVariant("统一规格", "38dp", "按钮不提供尺寸档位"),
            DemoVariant("图标按钮", "type = icon", "与普通按钮共用 type、size 和状态 API"),
            DemoVariant("组合复用", "content / contentPadding / role", "主体通过 content 插槽组合，继承按钮颜色与排版")
        ),
        apiDocumentPaths = listOf("basic/hyper-button.md"),
        content = { ButtonDemo() }
    ),
    ComponentDemo("text", GROUP_BASIC, "HyperText", "12 / 14 / 18sp 参考字号，状态与强调色可交互切换。",
        """HyperText("正文")""", listOf(DemoVariant("字号", "size", "small/default/large"),
            DemoVariant("截断", "maxLines / overflow", "有限行数默认省略号，显式 overflow 优先"),
            DemoVariant("布局观察", "onTextLayout", "默认 null；需要布局结果时显式传入回调")),
        listOf("basic/hyper-text.md"), { TextDemo() }),
    ComponentDemo("icon", GROUP_BASIC, "HyperIcon", "直接使用 Lucide 图标库与 18 / 24 / 32dp 图标尺寸。",
        """HyperIcon(icon = icon, contentDescription = "图标")""", listOf(DemoVariant("大小与颜色", "size / color", "插槽继承与显式覆盖")),
        listOf("basic/hyper-icon.md"), { IconDemo() }),
    ComponentDemo("divider", GROUP_BASIC, "HyperDivider", "1dp 实线或 4dp / 4dp 虚线，支持横竖方向与缩进。",
        """HyperDivider(type = "dashed", indent = 14.dp)""", listOf(DemoVariant("方向", "orientation", "horizontal/vertical")),
        listOf("basic/hyper-divider.md"), { DividerDemo() })

)
