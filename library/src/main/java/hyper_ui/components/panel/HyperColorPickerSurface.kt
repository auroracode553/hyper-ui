/** 文件职责：调色板渐变、定位环与连续取色手势，由 HyperColorPicker 组合使用。 */
package hyper_ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
internal fun HyperColorPickerSurface(
    modifier: Modifier,
    enabled: Boolean,
    x: Float,
    y: Float,
    label: String,
    brushes: List<Brush>,
    onChanged: (Float, Float) -> Unit
) {
    val currentOnChanged by rememberUpdatedState(onChanged)
    Canvas(modifier.clip(RoundedCornerShape(10.dp))
        .semantics {
            contentDescription = label
            if (!enabled) disabled()
        }
        .pointerInput(enabled) {
            if (!enabled) return@pointerInput
            awaitEachGesture {
                val down = awaitFirstDown()
                fun select(position: Offset) {
                    if (size.width > 0 && size.height > 0) currentOnChanged(
                        (position.x / size.width).coerceIn(0f, 1f),
                        (position.y / size.height).coerceIn(0f, 1f)
                    )
                }
                down.consume()
                select(down.position)
                do {
                    val event = awaitPointerEvent()
                    val pointer = event.changes.firstOrNull { it.id == down.id } ?: break
                    if (pointer.isConsumed) break
                    select(pointer.position)
                    pointer.consume()
                } while (pointer.pressed)
            }
        }
    ) {
        brushes.forEach { drawRect(it) }
        // 环完整留在画布内部，但取色仍覆盖完整范围。
        val radius = 6.dp.toPx()
        val inset = radius + 2.dp.toPx()
        val center = Offset(
            (size.width * x).coerceIn(minOf(inset, size.width / 2), maxOf(size.width - inset, size.width / 2)),
            (size.height * y).coerceIn(minOf(inset, size.height / 2), maxOf(size.height - inset, size.height / 2))
        )
        drawCircle(Color.Black.copy(alpha = 0.4f), radius + 1.dp.toPx(), center, style = Stroke(3.dp.toPx()))
        drawCircle(Color.White, radius, center, style = Stroke(2.dp.toPx()))
    }
}
