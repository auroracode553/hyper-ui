/** 文件职责：为 HyperButton 组合公共描边、单层阴影和不透明实色底面。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
internal data class HyperButtonSurfaceVisuals(
    val depth: HyperSurfaceDepthVisuals
) {
    companion object {
        val RestingElevation = 4.dp
        val PressedElevation = 1.5.dp
        val DisabledElevation = 0.dp
    }
}

@Composable
internal fun hyperButtonSurfaceVisuals(
    enabled: Boolean,
    pressed: Boolean
): HyperButtonSurfaceVisuals {
    val state = when {
        !enabled -> HyperSurfaceDepthState.Disabled
        pressed -> HyperSurfaceDepthState.Pressed
        else -> HyperSurfaceDepthState.Resting
    }
    val elevation = when (state) {
        HyperSurfaceDepthState.Disabled -> HyperButtonSurfaceVisuals.DisabledElevation
        HyperSurfaceDepthState.Pressed -> HyperButtonSurfaceVisuals.PressedElevation
        HyperSurfaceDepthState.Resting -> HyperButtonSurfaceVisuals.RestingElevation
    }

    return HyperButtonSurfaceVisuals(
        depth = hyperSurfaceDepthVisuals(
            role = HyperSurfaceDepthRole.CompactControl,
            elevation = elevation,
            state = state
        )
    )
}

/** 自定义/Outline 描边替换公共中性描边，确保表面始终只有一条描边。 */
internal fun Modifier.hyperButtonSurface(
    containerColor: Color,
    shape: Shape,
    visuals: HyperButtonSurfaceVisuals,
    borderOverride: BorderStroke?
): Modifier = hyperSurfaceDepth(
    shape = shape,
    visuals = visuals.depth,
    borderOverride = borderOverride
)
    .clip(shape)
    .background(containerColor)
