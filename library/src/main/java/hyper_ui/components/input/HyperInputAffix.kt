/** 文件职责：输入框 30dp 尾部操作与 Lucide 眼睛/关闭图标，供 HyperTextField 使用。 */
package hyper_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Composable
internal fun HyperInputAffix(
    icon: ImageVector,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HyperTooltip(description, enabled = enabled) {
        Box(modifier.size(30.dp).hyperNoRippleClickable(enabled = enabled, onClick = onClick),
            contentAlignment = Alignment.Center) {
            HyperIcon(icon, description, Modifier.size(18.dp), tint = HyperColors.secondaryText)
        }
    }
}
