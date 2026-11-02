/** 文件职责：录制不包含玻璃前景的背景，供固定组件镜片采样。 */
package hyper_ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned

@Stable
internal class HyperGlassSource(val layer: GraphicsLayer) {
    var coordinates: LayoutCoordinates? by mutableStateOf(null)
}

@Composable
internal fun rememberHyperGlassSource(): HyperGlassSource {
    val layer = rememberGraphicsLayer()
    return remember(layer) { HyperGlassSource(layer) }
}

internal val LocalHyperGlassSource = staticCompositionLocalOf<HyperGlassSource?> { null }

internal fun Modifier.hyperGlassSource(source: HyperGlassSource, tint: Color = Color.Unspecified): Modifier =
    onGloballyPositioned { source.coordinates = it }.drawWithContent {
        source.layer.record { this@drawWithContent.drawContent() }
        source.layer.colorFilter = if (tint == Color.Unspecified) null else ColorFilter.tint(tint)
        // 只对采样副本着色，不重复组合插槽，也不复制点击和语义节点。
        if (tint == Color.Unspecified) drawLayer(source.layer) else drawContent()
    }
