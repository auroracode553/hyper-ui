/** 文件职责：菜单自动宽度按文字与控件占位计算，复用 160–240dp 的紧凑范围。 */
package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil

@Composable
internal fun hyperMenuPreferredWidth(labels: List<String>, horizontalSpace: Dp = 68.dp): Dp {
    val density = LocalDensity.current
    val measurer = rememberTextMeasurer()
    val style = HyperTheme.typography.bodyMedium.copy(
        fontSize = 14.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.sp)
    return remember(labels, horizontalSpace, density, measurer, style) {
        val textWidth = labels.maxOfOrNull { label ->
            measurer.measure(AnnotatedString(label), style = style, maxLines = 1, softWrap = false).size.width
        } ?: 0
        val contentWidth = with(density) { textWidth.toDp() } + horizontalSpace
        ceil(contentWidth.value).dp.coerceIn(160.dp, 240.dp)
    }
}
