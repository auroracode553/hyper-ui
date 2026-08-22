/** 文件职责：绘制播放速度面板不依赖外部图标库的默认符号。 */
package hyper_ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
internal fun HyperPlaybackSpeedGaugeGlyph(modifier: Modifier = Modifier) {
    val color = LocalContentColor.current
    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidth = 1.8.dp.toPx()
        drawArc(
            color = color,
            startAngle = 200f,
            sweepAngle = 140f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.5f, size.height * 0.58f),
            end = Offset(size.width * 0.72f, size.height * 0.34f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
internal fun HyperPlaybackSpeedCloseGlyph(modifier: Modifier = Modifier) {
    val color = LocalContentColor.current
    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidth = 1.8.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.24f, size.height * 0.24f),
            end = Offset(size.width * 0.76f, size.height * 0.76f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.76f, size.height * 0.24f),
            end = Offset(size.width * 0.24f, size.height * 0.76f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
internal fun HyperPlaybackSpeedResetGlyph(modifier: Modifier = Modifier) {
    val color = LocalContentColor.current
    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidth = 1.7.dp.toPx()
        drawArc(
            color = color,
            startAngle = 35f,
            sweepAngle = 285f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.18f, size.height * 0.39f),
            end = Offset(size.width * 0.17f, size.height * 0.17f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.17f, size.height * 0.17f),
            end = Offset(size.width * 0.39f, size.height * 0.2f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
internal fun HyperPlaybackSpeedHintGlyph(modifier: Modifier = Modifier) {
    val color = LocalContentColor.current
    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidth = 1.6.dp.toPx()
        listOf(0.14f to 0.38f, 0.86f to 0.62f).forEach { (outerX, innerX) ->
            val direction = if (outerX < innerX) 1f else -1f
            drawLine(
                color = color,
                start = Offset(innerX * size.width, size.height * 0.25f),
                end = Offset(outerX * size.width, size.height * 0.5f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(outerX * size.width, size.height * 0.5f),
                end = Offset((innerX + direction * 0.01f) * size.width, size.height * 0.75f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
internal fun HyperPlaybackSpeedEditGlyph(modifier: Modifier = Modifier) {
    val color = LocalContentColor.current
    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidth = 1.8.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.25f, size.height * 0.73f),
            end = Offset(size.width * 0.72f, size.height * 0.26f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.22f, size.height * 0.78f),
            end = Offset(size.width * 0.4f, size.height * 0.73f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
