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

/** 返回动作由调用方处理；按钮沿用 HyperButton 的交互与材质。 */
@Composable
fun HyperNavBarBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "返回"
) {
    HyperButton(
        onClick = onClick,
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        type = "icon"
    ) {
        HyperIcon(
            imageVector = NavBarChevronLeft,
            contentDescription = null,
            modifier = Modifier.size(HyperButtonDefaults.IconSize)
        )
    }
}

/** 固定类型 HyperNavBar 使用的更多操作按钮。 */
@Composable
internal fun HyperNavBarMoreButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "更多"
) {
    HyperButton(
        onClick = onClick,
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        type = "icon"
    ) {
        HyperIcon(
            imageVector = NavBarMoreIcon,
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

private val NavBarMoreIcon: ImageVector = ImageVector.Builder(
    name = "NavBarMore",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(fill = SolidColor(Color(0f, 0f, 0f, 1f))) {
        for (centerY in listOf(5f, 12f, 19f)) {
            moveTo(12f, centerY - 1.5f)
            arcToRelative(1.5f, 1.5f, 0f, true, true, 0f, 3f)
            arcToRelative(1.5f, 1.5f, 0f, true, true, 0f, -3f)
            close()
        }
    }
}.build()
