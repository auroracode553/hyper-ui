/** 文件职责：通过独立开关对比三种 HyperSpacing 内容间距。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.*

@Composable
fun HyperSpacingDemo() {
    var horizontalSpacing by remember { mutableStateOf(true) }
    var verticalSpacing by remember { mutableStateOf(true) }
    var sectionSpacing by remember { mutableStateOf(true) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // 控制区保留固定行间距，演示开关只影响下方内容。
        Column(
            modifier = Modifier.padding(
                horizontal = HyperSpacing.PageHorizontal,
                vertical = HyperSpacing.PageVertical
            ),
            verticalArrangement = Arrangement.spacedBy(HyperSpacing.PageVertical)
        ) {
            PreviewToggle("水平留白 14dp", horizontalSpacing) { horizontalSpacing = it }
            PreviewToggle("首尾留白 12dp", verticalSpacing) { verticalSpacing = it }
            PreviewToggle("组间距 16dp", sectionSpacing) { sectionSpacing = it }
        }
        // 外边距包住完整分组，开关只改变内容间距，卡片内边距由组件提供。
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (horizontalSpacing) HyperSpacing.PageHorizontal else 0.dp,
                    vertical = if (verticalSpacing) HyperSpacing.PageVertical else 0.dp
                ),
            verticalArrangement = Arrangement.spacedBy(
                if (sectionSpacing) HyperSpacing.SectionVertical else 0.dp
            )
        ) {
            HyperMenuGroup(title = "网站显示") {
                HyperListTile(
                    title = { HyperText("网页深色模式") },
                    subtitle = { HyperText("深色模式下自动调整网页颜色") }
                )
            }
            HyperMenuGroup(title = "管理") {
                HyperListTile(title = { HyperText("用户代理") })
                HyperListTile(title = { HyperText("网站权限") })
            }
        }
    }
}
