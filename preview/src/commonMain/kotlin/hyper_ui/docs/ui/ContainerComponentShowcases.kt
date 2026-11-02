/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/ContainerComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Ellipsis
import com.composables.icons.lucide.Sparkles
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColorPickerDemo() {
    var color by remember { mutableStateOf(Color(0.25f, 0.47f, 1f, 1f)) }
    var enabled by remember { mutableStateOf(true) }
    Column(Modifier.widthIn(max = 400.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperColorPicker(value = color, onChanged = { color = it }, enabled = enabled)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton({ enabled = !enabled }) { HyperText(if (enabled) "禁用调色板" else "启用调色板") }
            HyperButton({ color = Color(0.25f, 0.47f, 1f, 1f) }) { HyperText("重置") }
        }
        HyperText("拖动渐变区域调整饱和度与明度，拖动彩虹条调整色相。")
    }
}

@Composable
fun PanelDemo() {
    // 正文卡片共用柔和双层阴影；选中轮廓继续由组件维护。
    var selected by remember { mutableStateOf(true) }
    var feedback by remember { mutableStateOf("上次更新：今天 09:30") }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PreviewSectionLabel("完整结构 · 标题、说明、操作、正文与页脚")
        HyperCard(
            title = { HyperText("本周专注") },
            subtitle = { HyperText("保持轻量、清晰的内容层级") },
            leading = { HyperIcon(Lucide.Sparkles, null) },
            actions = { HyperButton({ feedback = "已打开更多操作" }, type = "icon") { HyperIcon(Lucide.Ellipsis, "更多") } },
            footer = { HyperText(feedback) }
        ) {
            HyperText("卡片组织内容与操作，业务状态由调用方持有。")
        }
        PreviewSectionLabel("选中状态")
        PreviewToggle("选中方案", selected) { selected = it }
        HyperCard(selected = selected, onPressed = { selected = !selected },
            title = { HyperText("已选择的方案") }, subtitle = { HyperText("点击卡片切换选择状态") }) {
            HyperText("选中时以品牌色强调当前方案。")
        }
        PreviewSectionLabel("纯内容容器")
        HyperCard { HyperText("省略标题和副标题，直接承载正文。") }
    }
}

@Composable
private fun ContainerIconBadge(
    imageVector: ImageVector,
    tint: Color,
    background: Color
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        HyperIcon(
            icon = imageVector,
            contentDescription = null,
            color = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}

/** 表面只提供实色，预览保留内容与浮层的层级。 */
@Composable
fun GlassMaterialDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperCard { HyperText("实色内容表面") }
        HyperText("颜色、阴影与选中状态由组件管理。")
    }
}
