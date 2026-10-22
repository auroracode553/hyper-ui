/** 文件职责：提供导航栏常用的尖括号返回按钮与其矢量图标。 */
package hyper_ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.Ellipsis

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/** 返回动作由调用方处理；按钮沿用 HyperButton 的交互与材质。 */
@Composable
fun HyperNavBarBackButton(
    onPressed: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    contentDescription: String = "返回"
) {
    HyperButton(
        onPressed = onPressed,
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        type = "icon",
        colors = HyperButtonDefaults.colors(null, containerColor = hyperGlass.surfaceStrong)
    ) {
        HyperIcon(
            icon = Lucide.ChevronLeft,
            contentDescription = null,
            modifier = Modifier.size(HyperButtonDefaults.IconSize)
        )
    }
}

/** 固定类型 HyperNavBar 使用的更多操作按钮。 */
@Composable
internal fun HyperNavBarMoreButton(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    contentDescription: String = "更多"
) {
    HyperButton(
        onPressed = onClick,
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        type = "icon",
        colors = HyperButtonDefaults.colors(null, containerColor = hyperGlass.surfaceStrong)
    ) {
        HyperIcon(
            icon = Lucide.Ellipsis,
            contentDescription = null,
            modifier = Modifier.size(HyperButtonDefaults.IconSize)
        )
    }
}
