/** 文件职责：为 HyperTextField 组合单一容器底色、公共阴影和语义状态边缘。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
internal data class HyperTextFieldSurfaceVisuals(
    val containerColor: Color,
    val indicatorColor: Color,
    val depth: HyperSurfaceDepthVisuals
) {
    companion object {
        val RestingElevation = 2.dp
        val FocusedElevation = 3.dp
        val DisabledElevation = 0.dp
    }
}

/**
 * 输入框表面只绘制单一容器色，不叠加任何高光、明暗渐变或纹理层。
 * 普通态无描边；聚焦或错误时只绘制一条纯色语义边缘。
 */
internal fun Modifier.hyperTextFieldSurface(
    shape: Shape,
    visuals: HyperTextFieldSurfaceVisuals
): Modifier {
    val surface = hyperSurfaceShadow(
        shape = shape,
        visuals = visuals.depth
    )
        .clip(shape)
        .background(visuals.containerColor)

    return if (visuals.indicatorColor.alpha > 0f) {
        surface.border(
            width = StateIndicatorWidth,
            color = visuals.indicatorColor,
            shape = shape
        )
    } else {
        surface
    }
}

private val StateIndicatorWidth = 1.dp
