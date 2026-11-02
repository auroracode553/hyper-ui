/** 文件职责：统一实色表面的阴影、裁切与底色。 */
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
    val depth: HyperSurfaceDepthVisuals
)

internal fun Modifier.hyperGlassSurface(
    shape: Shape, visuals: HyperGlassSurfaceVisuals, borderOverride: BorderStroke? = null
): Modifier =
    hyperSurfaceDepth(shape, visuals.depth, borderOverride)
        .clip(shape)
        .background(visuals.containerColor)

@Composable
internal fun hyperGlassSurfaceVisuals(
    containerColor: Color,
    elevation: Dp,
    depth: HyperSurfaceDepthVisuals? = null
): HyperGlassSurfaceVisuals = HyperGlassSurfaceVisuals(
    containerColor = containerColor,
    depth = depth ?: hyperSurfaceDepthVisuals(HyperSurfaceDepthRole.StructuralPanel, elevation)
)

/** 常规容器共用入口；统一底色、裁切与深度。 */
internal fun Modifier.hyperFrostedSurface(
    containerColor: Color,
    shape: Shape,
    elevation: Dp = 12.dp,
    border: BorderStroke? = null
): Modifier = composed {
    val depth = hyperSurfaceDepthVisuals(HyperSurfaceDepthRole.StructuralPanel, elevation)
    hyperGlassSurface(shape, HyperGlassSurfaceVisuals(containerColor, depth), border)
}

/** 正文卡片共用无普通描边与柔和双层阴影；浮层、导航和控件不使用此入口。 */
internal fun Modifier.hyperCardSurface(
    containerColor: Color,
    shape: Shape,
    elevation: Dp = 12.dp,
    border: BorderStroke? = null
): Modifier = composed {
    val depth = hyperSurfaceDepthVisuals(HyperSurfaceDepthRole.ContentCard, elevation)
    hyperGlassSurface(shape, HyperGlassSurfaceVisuals(containerColor, depth), border)
}
