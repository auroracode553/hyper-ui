/** 文件职责：统一预览视口、滚动归属、模拟安全区及玻璃回退验证。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.LocalHyperGlassSource
import hyper_ui.LocalHyperSafeAreaWindowInsets
import hyper_ui.docs.LocalThemeColor
import hyper_ui.docs.data.ComponentDemo

/** 保持原有内容布局，同时把窗口安全区传给独立浮层；不改变手机外框和底栏。 */
@Composable
internal fun ComponentPreviewViewport(
    demo: ComponentDemo,
    modifier: Modifier = Modifier,
    deviceSafeAreas: Boolean = false
) {
    key(demo.id) {
        val windowInsets = if (deviceSafeAreas) WindowInsets(top = PreviewTopSafeArea)
            else LocalHyperSafeAreaWindowInsets.current
        val fallbackPreview = demo.id in setOf("dropdown", "tabs", "toast", "level_capsule") &&
            !LocalThemeColor.current.patternedBackground
        CompositionLocalProvider(LocalHyperSafeAreaWindowInsets provides windowInsets) {
            PreviewSceneBackdrop(modifier.fillMaxSize(), glassScene = demo.id.startsWith("tab-bar-") || demo.id in setOf(
                "button", "slider", "switch", "dialog", "hyper_dialog", "checkbox", "radio", "date_picker",
                "tabs", "drawer", "dropdown", "toast", "progress", "level_capsule", "update_dialog"
            )) {
                // 复用现有柔色／纹理背景切换验证回退和采样，不新增控制项。
                val source = LocalHyperGlassSource.current.takeUnless { fallbackPreview }
                CompositionLocalProvider(LocalHyperGlassSource provides source) {
                    val top = if (deviceSafeAreas && !demo.fillsTopSafeArea) PreviewTopSafeArea else 0.dp
                    // 普通示例保留原留白；独立 Dialog/Popup 不依赖这层视觉 Padding。
                    Box(Modifier.fillMaxSize().padding(top = top)) {
                        if (demo.fullScreenPreview) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) { demo.content() }
                        } else {
                            Box(
                                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                                contentAlignment = Alignment.TopCenter
                            ) { demo.content() }
                        }
                    }
                }
            }
        }
    }
}
