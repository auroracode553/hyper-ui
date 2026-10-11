/** 文件职责：参考分隔线，支持实线、4dp/4dp 虚线及逻辑方向缩进。 */
package hyper_ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HyperDivider(
    modifier: Modifier = Modifier,
    color: Color = HyperColors.divider,
    thickness: Dp = 1.dp,
    type: String = "solid",
    orientation: String = "horizontal",
    indent: Dp = 0.dp,
    endIndent: Dp = 0.dp,
    length: Dp? = null
) {
    require(type == "solid" || type == "dashed") { "type 必须为 solid 或 dashed" }
    require(orientation == "horizontal" || orientation == "vertical") { "orientation 必须为 horizontal 或 vertical" }
    require(thickness > 0.dp && indent >= 0.dp && endIndent >= 0.dp && (length == null || length >= 0.dp))
    val horizontal = orientation == "horizontal"
    val sizing = if (horizontal) Modifier.padding(start = indent, end = endIndent)
        .then(if (length == null) Modifier.fillMaxWidth() else Modifier.width(length)).height(thickness)
        else Modifier.padding(top = indent, bottom = endIndent).width(thickness).height(length ?: 24.dp)
    Canvas(modifier.then(sizing)) {
        val extent = if (horizontal) size.width else size.height
        if (extent <= 0f) return@Canvas
        val stroke = thickness.toPx()
        var position = 0f
        while (position < extent) {
            val end = if (type == "dashed") (position + 4.dp.toPx()).coerceAtMost(extent) else extent
            drawLine(color, if (horizontal) Offset(position, stroke / 2) else Offset(stroke / 2, position),
                if (horizontal) Offset(end, stroke / 2) else Offset(stroke / 2, end), strokeWidth = stroke)
            position += if (type == "dashed") 8.dp.toPx() else extent
        }
    }
}
