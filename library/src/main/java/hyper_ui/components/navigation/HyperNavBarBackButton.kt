/** 文件职责：提供导航栏常用的尖括号返回按钮与其矢量图标。 */
package hyper_ui

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/** 返回动作由调用方处理；禁用态沿用 HyperButton 的交互与材质。 */
@Composable
fun HyperNavBarBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = "返回"
) {
    HyperButton(
        onClick = onClick,
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        enabled = enabled,
        type = "icon"
    ) {
        HyperIcon(
            imageVector = NavBarChevronLeft,
            contentDescription = null,
            modifier = Modifier.size(HyperButtonDefaults.IconSize)
        )
    }
}

private val NavBarChevronLeft: ImageVector = ImageVector.Builder(
    name = "NavBarChevronLeft",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
    autoMirror = true
).apply {
    path(
        fill = null,
        stroke = SolidColor(Color(0f, 0f, 0f, 1f)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(15f, 5f)
        lineTo(8f, 12f)
        lineTo(15f, 19f)
    }
}.build()
