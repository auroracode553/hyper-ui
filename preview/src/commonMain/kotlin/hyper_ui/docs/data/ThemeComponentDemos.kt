/** 文件职责：主题模式弹窗的交互预览。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.ThemeDialogDemo
import hyper_ui.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val GROUP_THEME = "反馈组件"

internal fun themeComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "theme-mode-dialog",
        group = GROUP_THEME,
        title = "HyperThemeDialog",
        description = "高遮蔽玻璃面板内显示跟随系统、浅色、深色三选项，选中后请求关闭。",
        code = """
            HyperThemeDialog(visible = visible, value = darkTheme,
                onChanged = { darkTheme = it }, onClose = { visible = false })
        """.trimIndent(),
        variants = listOf(
            DemoVariant("跟随系统", "value = null", "由应用解析系统明暗"),
            DemoVariant("浅色 / 深色", "value = false / true", "统一选项与圆环，保留当前值"),
            DemoVariant("关闭", "onClose", "选中、遮罩或返回请求关闭；取消不修改值")
        ),
        apiDocumentPaths = listOf("feedback/hyper-theme-dialog.md"),
        content = { ThemeDialogDemo() }
    )
)

/** 系统安全区仍可配置；此示例与表面材质无关。 */
@Composable
internal fun SystemSafeAreaDemo() {
    var useSystemSafeArea by remember { mutableStateOf(false) }
    HyperThemeConfig(themeColor = HyperTheme.colors.themeColor, darkTheme = HyperTheme.isDark,
        typography = HyperTheme.typography, shapes = HyperTheme.shapes, useSystemSafeArea = useSystemSafeArea) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            HyperButton(onPressed = { useSystemSafeArea = !useSystemSafeArea }) {
                HyperText(if (useSystemSafeArea) "系统安全区：开启" else "系统安全区：关闭（默认全面屏）")
            }
            Box(Modifier.fillMaxWidth().height(180.dp)) {
                HyperNavBarPage(navBar = { HyperNavBar(title = { HyperText("全面屏页面") }) }) { padding ->
                    HyperText("首屏避让导航与状态栏，上滑时正文进入导航后方", modifier = Modifier.padding(padding))
                }
            }
        }
    }
}
