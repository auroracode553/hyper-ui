/** 文件职责：在 hyper_ui 中提供核心图标组件 HyperIcon，基于 foundation Image + ui-graphics，替代 基础图标组件。 */
package hyper_ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

internal val LocalHyperIconSize = staticCompositionLocalOf { 24.dp }

/**
 * 超UI 图标组件，签名与 基础图标组件 保持一致。
 * 默认着色取 [LocalHyperContentColor]，传 [Color.Unspecified] 时不做着色。
 */
@Composable
fun HyperIcon(
    icon: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    color: Color = LocalHyperContentColor.current,
    size: String = "default"
) {
    requireHyperComponentSize(size)
    Image(
        painter = icon,
        contentDescription = contentDescription,
        modifier = modifier.size(if (size == "default") LocalHyperIconSize.current else hyperComponentSize(size, 18.dp, 24.dp, 32.dp)),
        colorFilter = if (color != Color.Unspecified) ColorFilter.tint(color) else null
    )
}

/** ImageVector 重载：内部转换为 vector painter 后走 [HyperIcon]。 */
@Composable
fun HyperIcon(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    color: Color = LocalHyperContentColor.current,
    size: String = "default"
) {
    HyperIcon(
        icon = rememberVectorPainter(icon),
        contentDescription = contentDescription,
        modifier = modifier,
        color = color,
        size = size
    )
}
