/** 文件职责：HSV 调色板组合；绘制和手势见 HyperColorPickerSurface，换算见 HyperColorPickerValue。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** 连续选择色相、饱和度与明度；保留输入 alpha，应用主题由调用方管理。 */
@Composable
fun HyperColorPicker(
    value: Color,
    onChanged: (Color) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    require(value != Color.Unspecified) { "value 必须是有效颜色" }
    // 灰色没有色相，黑色没有饱和度，保留其编辑坐标以便继续选色。
    var lastHue by remember { mutableStateOf(0f) }
    var lastSaturation by remember { mutableStateOf(1f) }
    val hsv = value.toHyperHsv(lastHue, lastSaturation)
    SideEffect {
        lastHue = hsv.hue
        lastSaturation = hsv.saturation
    }
    Column(
        modifier = modifier.fillMaxWidth().alpha(if (enabled) 1f else 0.45f),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperColorPickerSurface(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            enabled = enabled, x = hsv.saturation, y = 1f - hsv.brightness,
            label = "饱和度与明度",
            brushes = listOf(
                Brush.horizontalGradient(listOf(Color.White, Color.hsv(hsv.hue, 1f, 1f))),
                Brush.verticalGradient(listOf(Color.Transparent, Color.Black))
            ),
            onChanged = { x, y ->
                lastHue = hsv.hue
                lastSaturation = x
                onChanged(Color.hsv(hsv.hue, x, 1f - y, value.alpha))
            }
        )
        HyperColorPickerSurface(
            modifier = Modifier.fillMaxWidth().height(32.dp),
            enabled = enabled, x = hsv.hue / 360f, y = 0.5f,
            label = "色相",
            brushes = listOf(Brush.horizontalGradient((0..6).map { Color.hsv(it * 60f, 1f, 1f) })),
            onChanged = { x, _ ->
                lastHue = x * 360f
                lastSaturation = hsv.saturation
                onChanged(Color.hsv(lastHue, hsv.saturation, hsv.brightness, value.alpha))
            }
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.size(28.dp).clip(RoundedCornerShape(8.dp))
                .background(HyperColors.pageBackground).background(value))
            HyperText(formatHyperColor(value))
        }
    }
}
