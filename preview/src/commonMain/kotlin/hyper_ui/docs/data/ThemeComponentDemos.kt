/** 文件职责：提供主题配置章节的材质切换交互预览。 */
package hyper_ui.docs.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.HyperButton
import hyper_ui.HyperCard
import hyper_ui.HyperText
import hyper_ui.HyperTextField
import hyper_ui.HyperTheme
import hyper_ui.HyperThemeConfig
import hyper_ui.HyperMaterial
import hyper_ui.docs.theme.docsGlassTokens

private const val GROUP_THEME = "主题配置"

internal fun themeComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "theme-material",
        group = GROUP_THEME,
        title = "主题材质",
        description = "切换实色、柔和与清透材质，观察同一组控件的表面层级。",
        code = """
            HyperThemeConfig(
                material = HyperMaterial.Solid
            ) { content() }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("实色", "HyperMaterial.Solid", "不透明表面"),
            DemoVariant("柔和", "HyperMaterial.Soft", "20/28dp 模糊玻璃表面"),
            DemoVariant("清透", "HyperMaterial.Clear", "更低透明度表面")
        ),
        apiDocumentPaths = emptyList(),
        content = { ThemeMaterialDemo() },
        useReferencePreview = false
    )
)

@Composable
private fun ThemeMaterialDemo() {
    var material by remember { mutableStateOf(HyperMaterial.Solid) }
    HyperThemeConfig(
        themeColor = HyperTheme.colors.themeColor,
        darkTheme = HyperTheme.isDark,
        typography = HyperTheme.typography,
        shapes = HyperTheme.shapes,
        material = material,
        glass = docsGlassTokens(HyperTheme.isDark, material)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HyperMaterial.values().forEach { option ->
                    HyperButton(
                        onClick = { material = option },
                        colorStyle = if (option == material) "primary" else null, type = "filled",
                        size = "small"
                    ) { HyperText(option.label()) }
                }
            }
            HyperCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HyperText("主题材质：${material.label()}")
                    HyperText("卡片、输入框和按钮读取同一套材质令牌")
                    HyperTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        placeholderContent = { HyperText("输入内容") }
                    )
                    HyperButton(onClick = {}, colorStyle = "primary", type = "filled") { HyperText("主要操作") }
                }
            }
        }
    }
}

private fun HyperMaterial.label(): String = when (this) {
    HyperMaterial.Solid -> "实色"
    HyperMaterial.Soft -> "柔和"
    HyperMaterial.Clear -> "清透"
}
