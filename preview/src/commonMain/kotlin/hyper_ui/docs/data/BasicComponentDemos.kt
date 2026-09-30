/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/BasicComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

import hyper_ui.docs.ui.ButtonDemo
import hyper_ui.docs.ui.IconButtonDemo

private const val GROUP_BASIC = "基础组件"

internal fun basicComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "button",
        group = GROUP_BASIC,
        title = "HyperButton",
        description = "38dp 动作按钮，五种视觉变体、加载态与随高度联动的排版。",
        code = """
            HyperButton(onClick = onSave) {
                HyperIcon(Icons.Default.Search, contentDescription = null)
                HyperText("搜索")
            }

            HyperButton(
                onClick = onDelete,
                type = "danger"
            ) {
                HyperText("删除")
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认按钮", "type = filled", "品牌渐变与品牌色投影"),
            DemoVariant("处理中", "loading = true", "加载期间不可重复点击"),
            DemoVariant("次级操作", "type = outline / tonal", "轻量玻璃与柔色底面"),
            DemoVariant("危险操作", "type = danger", "危险语义色"),
            DemoVariant("按压与取消", "pointer down / cancel", "按下立即开始 85ms 缩放和透明度过渡；释放或拖出后 180ms 恢复"),
            DemoVariant("禁用", "enabled = false", "弱公共描边、移除阴影的禁用实色状态"),
            DemoVariant("紧凑", "size = small", "小尺寸 slot"),
            DemoVariant("组合复用", "contentPadding / role", "供分段等组合组件复用布局与语义")
        ),
        apiDocumentPaths = listOf("basic/hyper-button.md"),
        content = { ButtonDemo() }
    ),
    ComponentDemo(
        id = "icon_button",
        group = GROUP_BASIC,
        title = "HyperIconButton",
        description = "36dp 图标按钮，18dp 图标，玻璃底面与浅色边缘。",
        code = """
            HyperIconButton(
                onClick = onSearch
            ) {
                HyperIcon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                )
            }

            HyperIconButton(
                onClick = onPlay,
                size = "large",
                colors = HyperIconButtonDefaults.colors(
                    containerColor = LocalDocsColorScheme.current.primary.copy(alpha = 0.64f),
                    pressedContainerColor = LocalDocsColorScheme.current.primaryContainer.copy(alpha = 0.76f),
                    contentColor = LocalDocsColorScheme.current.onPrimaryContainer
                )
            ) {
                HyperIcon(Icons.Default.PlayArrow, contentDescription = "播放")
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认玻璃", "shape = CircleShape", "浅深色玻璃令牌，保持细边缘"),
            DemoVariant("主题玻璃", "colors = primaryContainer.copy(alpha = 0.52f)", "主题色玻璃与按压反馈"),
            DemoVariant("危险玻璃", "shape = RoundedCornerShape(12.dp)", "危险语义色玻璃"),
            DemoVariant("按压状态", "pointer down / cancel", "整块玻璃表面立即缩放；拖出取消时恢复"),
            DemoVariant("禁用状态", "enabled = false", "无描边并移除投影"),
            DemoVariant("大尺寸主题", "size = large", "主题色媒体按钮"),
            DemoVariant("大尺寸中性", "size = large", "中性玻璃工具按钮")
        ),
        apiDocumentPaths = listOf("basic/hyper-icon-button.md"),
        content = { IconButtonDemo() }
    )
)
