/** 文件职责：将按钮阴影换算为 Desktop/Wasm Skia 使用的 sigma。 */
package hyper_ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Skia Preview 的 Shadow 使用 sigma；与 Flutter blurRadius 的视觉范围保持一致。
internal fun hyperButtonShadowRadius(blurRadius: Dp): Dp =
    (blurRadius.value * 0.57735f + 0.5f).dp
