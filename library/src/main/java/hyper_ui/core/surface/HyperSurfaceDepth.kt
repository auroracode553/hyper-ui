/** 文件职责：统一绘制 HyperUI 表面的主题描边与单层空间阴影。 */
package hyper_ui

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
internal data class HyperSurfaceDepthVisuals(
    val strokeColor: Color,
    val strokeWidth: Dp,
    val elevation: Dp,
    val ambientShadowColor: Color,
    val spotShadowColor: Color
)

internal enum class HyperSurfaceDepthRole {
    CompactControl,
    FloatingPanel,
    StructuralPanel,
    NavigationBar
}

internal enum class HyperSurfaceDepthState {
    Resting,
    Pressed,
    Disabled
}

/**
 * 在组件材质之前建立统一空间层级：最多一层阴影，并在内容绘制后叠加主题描边。
 * 具体强度由调用组件按紧凑控件、浮层面板或结构表面的角色决定。
 */
internal fun Modifier.hyperSurfaceDepth(
    shape: Shape,
    visuals: HyperSurfaceDepthVisuals
): Modifier = then(
    if (visuals.elevation > 0.dp) {
        Modifier.shadow(
            elevation = visuals.elevation,
            shape = shape,
            clip = false,
            ambientColor = visuals.ambientShadowColor,
            spotColor = visuals.spotShadowColor
        )
    } else {
        Modifier
    }
).then(
    if (visuals.strokeWidth > 0.dp && visuals.strokeColor.alpha > 0f) {
        Modifier.border(
            width = visuals.strokeWidth,
            color = visuals.strokeColor,
            shape = shape
        )
    } else {
        Modifier
    }
)

internal fun hyperSurfaceDepthVisuals(
    strokeColor: Color,
    elevation: Dp,
    ambientShadowColor: Color,
    spotShadowColor: Color,
    strokeWidth: Dp = 1.dp
): HyperSurfaceDepthVisuals = HyperSurfaceDepthVisuals(
    strokeColor = strokeColor,
    strokeWidth = strokeWidth,
    elevation = elevation,
    ambientShadowColor = ambientShadowColor,
    spotShadowColor = spotShadowColor
)

/** 为常用组件角色集中提供主题描边和阴影强度，避免各组件重复维护同一组参数。 */
@Composable
internal fun hyperSurfaceDepthVisuals(
    role: HyperSurfaceDepthRole,
    elevation: Dp,
    state: HyperSurfaceDepthState = HyperSurfaceDepthState.Resting
): HyperSurfaceDepthVisuals {
    val isLight = HyperColors.isLight
    val strokeAlpha = when {
        state == HyperSurfaceDepthState.Disabled -> if (isLight) 0.035f else 0.07f
        state == HyperSurfaceDepthState.Pressed -> if (isLight) 0.055f else 0.11f
        role == HyperSurfaceDepthRole.CompactControl -> if (isLight) 0.075f else 0.14f
        role == HyperSurfaceDepthRole.FloatingPanel -> if (isLight) 0.065f else 0.14f
        role == HyperSurfaceDepthRole.StructuralPanel -> if (isLight) 0.055f else 0.11f
        else -> if (isLight) 0.055f else 0.10f
    }
    val ambientShadowAlpha = when {
        state == HyperSurfaceDepthState.Disabled -> 0f
        state == HyperSurfaceDepthState.Pressed -> if (isLight) 0.025f else 0.06f
        role == HyperSurfaceDepthRole.CompactControl -> if (isLight) 0.055f else 0.10f
        role == HyperSurfaceDepthRole.FloatingPanel -> if (isLight) 0.075f else 0.14f
        role == HyperSurfaceDepthRole.StructuralPanel -> if (isLight) 0.115f else 0.23f
        else -> if (isLight) 0.035f else 0.08f
    }
    val spotShadowAlpha = when {
        state == HyperSurfaceDepthState.Disabled -> 0f
        state == HyperSurfaceDepthState.Pressed -> if (isLight) 0.055f else 0.12f
        role == HyperSurfaceDepthRole.CompactControl -> if (isLight) 0.13f else 0.20f
        role == HyperSurfaceDepthRole.FloatingPanel -> if (isLight) 0.15f else 0.24f
        role == HyperSurfaceDepthRole.StructuralPanel -> if (isLight) 0.16f else 0.32f
        else -> if (isLight) 0.08f else 0.14f
    }

    return hyperSurfaceDepthVisuals(
        strokeColor = if (isLight) {
            Color(0f, 0f, 0f, strokeAlpha)
        } else {
            Color(1f, 1f, 1f, strokeAlpha)
        },
        elevation = if (state == HyperSurfaceDepthState.Disabled) 0.dp else elevation,
        ambientShadowColor = Color(0f, 0f, 0f, ambientShadowAlpha),
        spotShadowColor = Color(0f, 0f, 0f, spotShadowAlpha)
    )
}
