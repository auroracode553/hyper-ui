/** 文件职责：统一绘制 HyperUI 表面的主题描边与双层空间阴影。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
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
    StructuralPanel
}

internal enum class HyperSurfaceDepthState {
    Resting,
    Pressed,
    Disabled
}

/**
 * 在组件材质之前建立统一空间层级：两层阴影，并在内容绘制后叠加主题描边。
 * 具体强度由调用组件按紧凑控件、浮层面板或结构表面的角色决定；覆盖描边会替换主题描边。
 */
internal fun Modifier.hyperSurfaceDepth(
    shape: Shape,
    visuals: HyperSurfaceDepthVisuals,
    borderOverride: BorderStroke? = null
): Modifier {
    val resolvedBorder = borderOverride ?: if (
        visuals.strokeWidth > 0.dp && visuals.strokeColor.alpha > 0f
    ) {
        BorderStroke(
            width = visuals.strokeWidth,
            color = visuals.strokeColor
        )
    } else {
        null
    }

    return hyperSurfaceShadow(
        shape = shape,
        visuals = visuals
    ).then(
        if (resolvedBorder != null) {
            Modifier.border(
                border = resolvedBorder,
                shape = shape
            )
        } else {
            Modifier
        }
    )
}

/** 只应用公共表面阴影，供不需要描边的组件复用相同空间层级参数。 */
internal fun Modifier.hyperSurfaceShadow(
    shape: Shape,
    visuals: HyperSurfaceDepthVisuals
): Modifier = then(
    if (visuals.elevation > 0.dp) {
        // 与 HyUiEffects.surfaceShadows 对齐：两层投影只用于玻璃容器。
        Modifier.dropShadow(shape, Shadow(
            radius = 28.dp, spread = (-6).dp, offset = DpOffset(0.dp, 12.dp),
            color = visuals.spotShadowColor
        )).dropShadow(shape, Shadow(
            radius = 8.dp, spread = (-3).dp, offset = DpOffset(0.dp, 3.dp),
            color = visuals.ambientShadowColor
        ))
    } else Modifier
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
    val enabled = state != HyperSurfaceDepthState.Disabled
    return hyperSurfaceDepthVisuals(
        strokeColor = if (enabled) hyperGlass.border else HyperColors.divider.copy(alpha = 110 / 255f),
        elevation = if (enabled) elevation else 0.dp,
        ambientShadowColor = Color(0f, 0f, 0f, if (HyperColors.isLight) 8 / 255f else 35 / 255f),
        spotShadowColor = Color(0f, 0f, 0f, if (HyperColors.isLight) 20 / 255f else 82 / 255f)
    )
}
