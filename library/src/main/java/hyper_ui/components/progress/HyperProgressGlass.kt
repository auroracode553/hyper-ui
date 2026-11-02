/** 文件职责：进度专用光泽与空心轨道形状；复用共用玻璃采样，不绘制圆盘底板。 */
package hyper_ui

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

internal fun hyperProgressGlassBrush(color: Color): Brush = Brush.verticalGradient(listOf(
    Color(1f, 1f, 1f, 0.24f).compositeOver(color), color, color
))

internal class HyperProgressRingShape(private val strokeWidth: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val inset = with(density) { strokeWidth.toPx() }.coerceIn(0f, size.minDimension / 2f)
        return Outline.Generic(Path().apply {
            fillType = PathFillType.EvenOdd
            addOval(Rect(0f, 0f, size.width, size.height))
            if (inset < size.minDimension / 2f) addOval(Rect(inset, inset, size.width - inset, size.height - inset))
        })
    }
}
