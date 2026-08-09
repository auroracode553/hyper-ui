/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/core/surface/HyperSurface 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@PublishedApi
internal fun resolveHyperContainerColor(
    containerColor: Color,
    fallbackColor: Color
): Color = if (containerColor == Color.Unspecified) fallbackColor else containerColor

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
internal fun Modifier.hyperSurface(
    containerColor: Color,
    shape: Shape,
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    clipContent: Boolean = true
): Modifier {
    return this
        .then(
            if (elevation > 0.dp) {
                Modifier.shadow(elevation = elevation, shape = shape, clip = false)
            } else {
                Modifier
            }
        )
        .then(if (clipContent) Modifier.clip(shape) else Modifier)
        .background(color = containerColor, shape = shape)
        .then(if (border != null) Modifier.border(border, shape) else Modifier)
}
