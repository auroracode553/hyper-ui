/** 文件职责：标记父容器已提供材质，避免 auto 列表行重复绘制卡片。 */
package hyper_ui

import androidx.compose.runtime.staticCompositionLocalOf

/** 父级是否已经绘制共用表面。 */
internal val LocalHyperSurfacePresent = staticCompositionLocalOf { false }

/** 父级是否提供行内容的水平留白；浮层外框不属于内容留白。 */
internal val LocalHyperSurfaceSharesPadding = staticCompositionLocalOf { true }
