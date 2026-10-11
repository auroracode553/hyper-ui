/** 文件职责：为 VitePress iframe 单独渲染指定组件的交互示例。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hyper_ui.docs.data.ComponentDemo
import hyper_ui.docs.LocalThemeColor
import hyper_ui.HyperTheme
import hyper_ui.HyperThemeConfig
import hyper_ui.docs.theme.docsGlassTokens

@Composable
internal fun EmbeddedComponentPreview(demo: ComponentDemo) {
    val settings = LocalThemeColor.current
    val themeColor = settings.color
    val typography = HyperTheme.typography
    val shapes = HyperTheme.shapes
    HyperThemeConfig(
        themeColor = themeColor,
        darkTheme = settings.darkTheme,
        typography = typography,
        shapes = shapes,
        material = settings.material,
        glass = docsGlassTokens(settings.darkTheme, settings.material)
    ) {
        ComponentPreviewViewport(demo, Modifier.fillMaxSize(), deviceSafeAreas = true)
    }
}
