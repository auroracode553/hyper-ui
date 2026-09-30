/** 文件职责：在预览宿主的整个视口绘制可采样背景，不为单个组件增加内层画框。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import hyper_ui.HyperBackdrop
import hyper_ui.HyperColors
import hyper_ui.HyperSoftBackground
import hyper_ui.docs.LocalThemeColor

/** 预览手机的状态栏与手势条安全区。 */
internal val PreviewTopSafeArea = 48.dp
internal val PreviewBottomSafeArea = 32.dp

@Composable
internal fun PreviewSceneBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val settings = LocalThemeColor.current
    HyperBackdrop(
        modifier = modifier,
        background = {
            if (settings.patternedBackground) {
                HyperSoftBackground(Modifier.matchParentSize())
                Canvas(Modifier.matchParentSize()) {
                    repeat(12) { index ->
                        drawCircle(
                            color = settings.color.copy(alpha = 0.3f),
                            radius = 30.dp.toPx(),
                            center = Offset(
                                size.width * index / 11f,
                                size.height * (index % 3 + 1) / 4f
                            )
                        )
                    }
                }
            } else {
                Box(Modifier.matchParentSize().background(HyperColors.pageBackground))
            }
        },
        content = content
    )
}
