/** 文件职责：在 hyper_ui 中提供核心分隔线组件 HyperDivider，基于 foundation layout，替代 material3 HorizontalDivider。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 超UI 水平分隔线，签名与 material3 HorizontalDivider 保持一致。
 */
@Composable
fun HyperDivider(
    modifier: Modifier = Modifier,
    color: Color = HyperColors.divider,
    thickness: Dp = 1.dp
) {
    Box(
        modifier = modifier
            .height(thickness)
            .background(color)
    )
}
