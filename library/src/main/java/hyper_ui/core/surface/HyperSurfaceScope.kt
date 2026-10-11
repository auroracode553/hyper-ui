/** 文件职责：标记父容器已提供材质，避免 auto 列表行重复绘制卡片。 */
package hyper_ui

import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalHyperSurfacePresent = staticCompositionLocalOf { false }
