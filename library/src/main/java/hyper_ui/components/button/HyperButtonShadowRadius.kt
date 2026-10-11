/** 文件职责：Android 按钮阴影的模糊半径，Preview 以平台实现替换此文件。 */
package hyper_ui

import androidx.compose.ui.unit.Dp

// Android BlurMaskFilter 自行把半径换算为 sigma，保持 Flutter 的 16dp 配置。
internal fun hyperButtonShadowRadius(blurRadius: Dp): Dp = blurRadius
