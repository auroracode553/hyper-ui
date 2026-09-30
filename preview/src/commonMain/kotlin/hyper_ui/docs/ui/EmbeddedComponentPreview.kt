/** 文件职责：为 VitePress iframe 单独渲染指定组件的交互示例。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.docs.data.ComponentDemo
import hyper_ui.docs.LocalThemeColor
import hyper_ui.HyperTheme
import hyper_ui.HyperThemeConfig
import hyper_ui.docs.theme.docsGlassTokens

private val PhonePreviewTopInset = 48.dp

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
        glass = docsGlassTokens(settings.darkTheme, settings.material)
    ) {
        PreviewSceneBackdrop(modifier = Modifier.fillMaxSize()) {
            if (demo.fullScreenPreview) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = PhonePreviewTopInset,
                            bottom = if (demo.fillsBottomSafeArea) 0.dp else PreviewBottomSafeArea
                        ),
                    contentAlignment = Alignment.TopCenter
                ) { demo.content() }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = PhonePreviewTopInset,
                            bottom = PreviewBottomSafeArea
                        ),
                    contentAlignment = Alignment.TopCenter
                ) { demo.content() }
            }
        }
    }
}
