/** 文件职责：统一玻璃材质；移植 HyGlass，背景模糊与前景绘制分离。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
internal data class HyperGlassSurfaceVisuals(
    val containerColor: Color,
    val depth: HyperSurfaceDepthVisuals,
    val blur: Dp = 20.dp
)

internal fun Modifier.hyperGlassSurface(
    shape: Shape, visuals: HyperGlassSurfaceVisuals, borderOverride: BorderStroke? = null
): Modifier =
    hyperSurfaceDepth(shape, visuals.depth, borderOverride)
        .clip(shape)
        .hyperBackdropEffect(visuals.blur)
        .background(visuals.containerColor)

@Composable
internal fun hyperGlassSurfaceVisuals(
    containerColor: Color,
    elevation: Dp,
    depth: HyperSurfaceDepthVisuals? = null,
    blur: Dp = 20.dp
): HyperGlassSurfaceVisuals = HyperGlassSurfaceVisuals(
    containerColor = containerColor,
    depth = depth ?: hyperSurfaceDepthVisuals(HyperSurfaceDepthRole.StructuralPanel, elevation),
    blur = blur
)

/** 常规容器共用入口；颜色和描边覆盖不会取消背景采样。 */
internal fun Modifier.hyperFrostedSurface(
    containerColor: Color,
    shape: Shape,
    elevation: Dp = 12.dp,
    border: BorderStroke? = null,
    blur: Dp = 20.dp
): Modifier = composed {
    val depth = hyperSurfaceDepthVisuals(HyperSurfaceDepthRole.StructuralPanel, elevation)
    hyperGlassSurface(shape, HyperGlassSurfaceVisuals(containerColor, depth, blur), border)
}
