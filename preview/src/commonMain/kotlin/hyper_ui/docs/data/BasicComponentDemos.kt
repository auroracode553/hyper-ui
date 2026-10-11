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
        description = "38dp 动作按钮，默认描边无需 type；type 控制显示形态，color / colorStyle 控制配色。",
        code = """
            HyperButton(onClick = onSave) {
                HyperIcon(searchIcon, contentDescription = null)
                HyperText("搜索")
            }

            HyperButton(
                onClick = onDelete,
                type = "filled",
                colorStyle = "danger"
            ) {
                HyperText("删除")
            }

            HyperButton(onClick = onSearch, type = "icon") {
                HyperIcon(searchIcon, contentDescription = "搜索")
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认按钮", "省略 type", "标准描边胶囊表面"),
            DemoVariant("主操作", "type = filled / colorStyle = primary", "品牌色胶囊表面"),
            DemoVariant("语义配色", "colorStyle", "primary/success/info/warning/danger，与显示类型独立"),
            DemoVariant("自定义颜色", "color", "优先于 colorStyle，作为显示类型使用的基色"),
            DemoVariant("处理中", "loading = true", "加载期间不可重复点击"),
            DemoVariant("次级操作", "省略 type / type = tonal", "默认描边或柔色底面"),
            DemoVariant("危险操作", "type = filled / colorStyle = danger", "危险语义色"),
            DemoVariant("按压与取消", "pointer down / cancel", "按下立即开始 85ms 缩放和透明度过渡；释放或拖出后 180ms 恢复"),
            DemoVariant("禁用", "enabled = false", "保留单个禁用按钮示例，不重复展示外观相同的配色"),
            DemoVariant("尺寸", "size = small/default/large", "32/38/44dp 高度"),
            DemoVariant("图标按钮", "type = icon", "与普通按钮共用 type、size 和状态 API"),
            DemoVariant("组合复用", "contentPadding / role", "供调用方通过插槽组合布局与语义")
        ),
        apiDocumentPaths = listOf("basic/hyper-button.md"),
        content = { ButtonDemo() }
    ),
    ComponentDemo("text", GROUP_BASIC, "HyperText", "12 / 14 / 18sp 参考字号，状态与强调色可交互切换。",
        """HyperText("正文", size = "default")""", listOf(DemoVariant("字号", "size", "small/default/large")),
        listOf("basic/hyper-text.md"), { TextDemo() }),
    ComponentDemo("icon", GROUP_BASIC, "HyperIcon", "参考版本 Lucide 字体轮廓与 18 / 24 / 32dp 图标尺寸。",
        """HyperIcon(imageVector = icon, contentDescription = "图标")""", listOf(DemoVariant("大小与颜色", "size / tint", "插槽继承与显式覆盖")),
        listOf("basic/hyper-icon.md"), { IconDemo() }),
    ComponentDemo("divider", GROUP_BASIC, "HyperDivider", "1dp 实线或 4dp / 4dp 虚线，支持横竖方向与缩进。",
        """HyperDivider(type = "dashed", indent = 14.dp)""", listOf(DemoVariant("方向", "orientation", "horizontal/vertical")),
        listOf("basic/hyper-divider.md"), { DividerDemo() })

)
