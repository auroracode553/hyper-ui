/** 文件职责：为 VitePress iframe 单独渲染指定组件的交互示例。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.docs.data.ComponentDemo
import hyper_ui.HyperColors
import hyper_ui.HyperTheme
import hyper_ui.HyperThemeConfig

internal val LocalEmbeddedDarkMode = staticCompositionLocalOf<MutableState<Boolean>?> { null }

@Composable
internal fun EmbeddedComponentPreview(demo: ComponentDemo) {
    val darkMode = remember { mutableStateOf(false) }
    val themeColor = HyperTheme.colors.themeColor
    val typography = HyperTheme.typography
    val shapes = HyperTheme.shapes
    CompositionLocalProvider(LocalEmbeddedDarkMode provides darkMode) {
        HyperThemeConfig(themeColor = themeColor, darkTheme = darkMode.value,
            typography = typography, shapes = shapes) {
            if (demo.fullScreenPreview) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (HyperColors.isLight) HyperColors.cardContainer else HyperColors.pageBackground),
                    contentAlignment = Alignment.TopCenter
                ) { demo.content() }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (HyperColors.isLight) HyperColors.cardContainer else HyperColors.pageBackground)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    contentAlignment = Alignment.TopCenter
                ) { demo.content() }
            }
        }
    }
}
