/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/ContainerComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

import hyper_ui.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.font.FontWeight
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
        HyperButton({ pickerEnabled = !pickerEnabled }, variant = HyperButtonVariant.Ghost) { HyperText(if (pickerEnabled) "禁用颜色选择" else "启用颜色选择") }
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
    var acknowledged by remember { mutableStateOf(false) }

    HyperPanel(
        modifier = Modifier.widthIn(max = 520.dp),
        colors = HyperPanelDefaults.colors(
            containerColor = LocalDocsColorScheme.current.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContainerIconBadge(
                imageVector = if (acknowledged) Icons.Default.Star else Icons.Default.Check,
                tint = LocalDocsColorScheme.current.secondary,
                background = LocalDocsColorScheme.current.secondaryContainer
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                HyperText(
                    text = "系统状态",
                    color = LocalDocsColorScheme.current.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                HyperText(
                    text = if (acknowledged) "已查看状态详情" else "运行正常，最近同步 2 分钟前",
                    color = LocalDocsColorScheme.current.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }
        HyperDivider(color = LocalDocsColorScheme.current.outlineVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                onClick = { acknowledged = true }
            ) {
                HyperText(text = "查看详情")
            }
            HyperButton(
                onClick = { acknowledged = false },
                variant = HyperButtonVariant.Outline
            ) {
                HyperText(text = "重置")
            }
        }
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
    var opacity by remember { mutableStateOf(217 / 255f) }
    val glass = HyperTheme.glass.copy(surface = HyperTheme.glass.surface.copy(alpha = opacity))
    HyperThemeConfig(
        themeColor = HyperTheme.colors.themeColor,
        darkTheme = HyperTheme.isDark,
        typography = HyperTheme.typography,
        shapes = HyperTheme.shapes,
        glass = glass
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            HyperText("四档重量（weight 控制表面层级）", fontSize = 12.sp,
                color = LocalDocsColorScheme.current.onSurfaceVariant)
            listOf(
                "Subtle · 小面积和辅助表面" to glass.surfaceSubtle,
                "Regular · 常规内容表面" to glass.surface,
                "Prominent · 浮层和模态表面" to glass.surfaceStrong,
                "Solid · 不透明内容表面" to HyperColors.cardContainer
            ).forEach { (label, surface) ->
                HyperPanel(colors = HyperPanelDefaults.colors(surface), elevation = 8.dp) {
                    HyperText(label, fontSize = 13.sp)
                }
            }
            HyperSlider(opacity, { opacity = it }, valueRange = 0.1f..1f)
        }
    }
}
