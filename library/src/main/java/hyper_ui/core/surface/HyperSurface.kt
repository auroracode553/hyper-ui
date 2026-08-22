/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/core/surface/HyperSurface 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@PublishedApi
internal fun resolveHyperContainerColor(
    containerColor: Color,
    fallbackColor: Color
): Color = if (containerColor == Color.Unspecified) fallbackColor else containerColor

/**
 * 将组件颜色预先合成到不透明背景上，避免把调用方传入的 alpha 继续带到最终界面。
 */
@PublishedApi
internal fun resolveHyperOpaqueColor(
    color: Color,
    fallbackColor: Color,
    backgroundColor: Color
): Color {
    val resolvedColor = resolveHyperContainerColor(color, fallbackColor)
    return if (resolvedColor.alpha >= 1f) {
        resolvedColor
    } else {
        resolvedColor.compositeOver(backgroundColor).copy(alpha = 1f)
    }
}

@PublishedApi
internal fun blendHyperOpaqueColors(
    backgroundColor: Color,
    foregroundColor: Color,
    foregroundFraction: Float
): Color = lerp(
    start = backgroundColor,
    stop = foregroundColor,
    fraction = foregroundFraction.coerceIn(0f, 1f)
).copy(alpha = 1f)

@PublishedApi
internal fun resolveHyperDisabledContainerColor(
    containerColor: Color,
    usesDefaultContainerColor: Boolean,
    fallbackDisabledColor: Color,
    disabledAlpha: Float = HyperStyleDefaults.DisabledAlpha
): Color = if (usesDefaultContainerColor) {
    fallbackDisabledColor
} else {
    containerColor.copy(alpha = containerColor.alpha * disabledAlpha)
}

/** 浅色使用组件自己的容器色，深色直接跟随当前页面主题背景。 */
@Composable
@PublishedApi
internal fun hyperPageMatchedContainerColor(lightContainerColor: Color): Color = if (HyperColors.isLight) {
    lightContainerColor
} else {
    MaterialTheme.colorScheme.background
}

@Composable
@PublishedApi
internal fun hyperPanelBorder(
    color: Color = Color.Unspecified
): BorderStroke = if (color == Color.Unspecified) {
    HyperColors.panelBorder
} else {
    BorderStroke(width = 1.dp, color = color)
}

@Composable
@PublishedApi
internal fun hyperSolidPanelBorder(
    color: Color = Color.Unspecified,
    backgroundColor: Color
): BorderStroke {
    val fallbackColor = if (HyperColors.isLight) {
        rgba(0, 0, 0, 0.08f)
    } else {
        rgba(255, 255, 255, 0.18f)
    }
    return BorderStroke(
        width = 1.dp,
        color = resolveHyperOpaqueColor(
            color = color,
            fallbackColor = fallbackColor,
            backgroundColor = backgroundColor
        )
    )
}

@PublishedApi
internal fun Modifier.hyperSolidSurface(
    containerColor: Color,
    shape: Shape,
    elevation: Dp = 0.dp,
    border: BorderStroke? = null
): Modifier = this
    .then(
        if (elevation > 0.dp) {
            Modifier.shadow(elevation = elevation, shape = shape, clip = false)
        } else {
            Modifier
        }
    )
    .clip(shape)
    .background(color = containerColor, shape = shape)
    .then(if (border != null) Modifier.border(border, shape) else Modifier)

/** 通用普通表面修饰符；只绘制传入颜色、阴影和描边，不添加玻璃高光。 */
@PublishedApi
internal fun Modifier.hyperSurface(
    containerColor: Color,
    shape: Shape,
    elevation: Dp = 0.dp,
    border: BorderStroke? = null
): Modifier = hyperSolidSurface(
    containerColor = containerColor,
    shape = shape,
    elevation = elevation,
    border = border
)
