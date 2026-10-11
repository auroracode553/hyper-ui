/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/ContainerComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
    var selectedColorId by remember { mutableStateOf("ocean_blue") }
    var pickerEnabled by remember { mutableStateOf(true) }
    val currentOption = remember(selectedColorId) {
        HyperColorPickerDefaults.presetOptions.find { it.id == selectedColorId }
            ?: HyperColorPickerDefaults.presetOptions.first()
    }

    Column(
        modifier = Modifier.widthIn(max = 400.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HyperButton({ pickerEnabled = !pickerEnabled }) { HyperText(if (pickerEnabled) "禁用颜色选择" else "启用颜色选择") }
        HyperColorPicker(
            enabled = pickerEnabled,
            selectedId = selectedColorId,
            onSelected = { option -> selectedColorId = option.id }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(currentOption.color)
            )
            HyperText(
                text = "当前选择：${currentOption.label}",
                fontSize = 14.sp,
                color = LocalDocsColorScheme.current.onSurface
            )
        }
    }
}

@Composable
fun PanelDemo() {
    var selected by remember { mutableStateOf(true) }
    var feedback by remember { mutableStateOf("上次更新：今天 09:30") }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PreviewSectionLabel("完整结构 · 标题、说明、操作、正文与页脚")
        HyperCard(
            titleContent = { HyperText("本周专注") },
            subtitleContent = { HyperText("保持轻量、清晰的内容层级") },
            leadingContent = { HyperIcon(HyperReferenceIcons.Sparkles, null) },
            actionContent = { HyperButton({ feedback = "已打开更多操作" }, type = "icon", size = "small") { HyperIcon(HyperReferenceIcons.More, "更多") } },
            footerContent = { HyperText(feedback, size = "small") }
        ) {
            HyperText("卡片组织内容与操作，业务状态由调用方持有。")
        }
        PreviewSectionLabel("选中状态")
        PreviewToggle("选中方案", selected) { selected = it }
        HyperCard(selected = selected, onClick = { selected = !selected },
            titleContent = { HyperText("已选择的方案") }, subtitleContent = { HyperText("点击卡片切换选择状态") }) {
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
            imageVector = imageVector,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}

/** 材质令牌可注入；使用已有主题方向，避免在预览中误重置深色模式。 */
@Composable
fun GlassMaterialDemo() {
    val surfaceAlpha = HyperTheme.glass.surface.alpha
    var opacity by remember(surfaceAlpha) { mutableStateOf(surfaceAlpha) }
    val glass = HyperTheme.glass.copy(surface = HyperTheme.glass.surface.copy(alpha = opacity))
    HyperThemeConfig(
        themeColor = HyperTheme.colors.themeColor,
        darkTheme = HyperTheme.isDark,
        typography = HyperTheme.typography,
        shapes = HyperTheme.shapes,
        material = HyperTheme.material,
        glass = glass
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PreviewSectionLabel("同一材质的表面层级")
            listOf(
                "Subtle · 小面积和辅助表面" to glass.surfaceSubtle,
                "Regular · 常规内容表面" to glass.surface,
                "Prominent · 浮层和模态表面" to glass.surfaceStrong,
                "Solid · 不透明内容表面" to HyperColors.cardContainer
            ).forEach { (label, surface) ->
                HyperCard(colors = HyperCardDefaults.colors(surface), elevation = 8.dp) {
                    HyperText(label, fontSize = 13.sp)
                }
            }
            HyperSlider(opacity, { opacity = it }, valueRange = 0.1f..1f)
        }
    }
}
