/** 文件职责：为 HyperTextField 组合单一容器底色、公共阴影和语义状态边缘。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
internal data class HyperTextFieldSurfaceVisuals(
    val containerColor: Color,
    val depth: HyperSurfaceDepthVisuals
) {
    companion object {
        val RestingElevation = 4.dp
        val FocusedElevation = 5.dp
        val DisabledElevation = 0.dp
    }
}

/**
 * 输入框表面只绘制单一容器色，不叠加任何高光、明暗渐变或纹理层。
 * 主题描边与单层阴影统一由公共 HyperSurfaceDepth 绘制。
 */
internal fun Modifier.hyperTextFieldSurface(
    shape: Shape,
    visuals: HyperTextFieldSurfaceVisuals
): Modifier = hyperSurfaceDepth(
        shape = shape,
        visuals = visuals.depth
    )
        .clip(shape)
        .background(visuals.containerColor)
