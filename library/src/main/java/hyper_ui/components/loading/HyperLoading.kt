package hyper_ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import hyper_ui.core.loading.LoadingRenderer
import hyper_ui.core.loading.loadingPalette
import hyper_ui.core.loading.loadingSize
import kotlinx.coroutines.isActive

/** HyperLoading 的十款原生绘制形态；每款保持一套默认规格。 */
enum class HyperLoadingType {
    travelingPulse, wovenSignal, ribbonDuet, threeBody, cometExchange,
    photonRelay, elasticSpark, goldenHourglass, frequencyFins, flexRaster
}

/** 不确定加载指示器；固定原始节奏的 2×，业务负责显示和移除。 */
@Composable
fun HyperLoading(
    type: HyperLoadingType? = null,
    modifier: Modifier = Modifier,
    label: String? = null
) {
    // 随机形态属于当前组件实例，父级重组与文字变化不会重新抽取。
    val randomType = remember { HyperLoadingType.entries.random() }
    val resolvedType = type ?: randomType
    val seconds = remember(resolvedType) { mutableStateOf(0.0) }
    val renderer = remember { LoadingRenderer() }
    val scope = rememberCoroutineScope()
    val durationScale = scope.coroutineContext[MotionDurationScale]?.scaleFactor ?: 1f
    LaunchedEffect(resolvedType, durationScale) {
        if (durationScale <= 0f) return@LaunchedEffect
        var previous: Long? = null
        while (isActive) {
            withFrameNanos { now ->
                previous?.let { seconds.value += (now - it) / 1_000_000_000.0 * 2 / durationScale }
                previous = now
            }
        }
    }
    val palette = loadingPalette(resolvedType)
    val reference = loadingSize(resolvedType)
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // 只在绘制阶段读取时钟，动画不触发整个宿主重组。
        Canvas(Modifier.size(reference.width.dp, reference.height.dp)) {
            drawIntoCanvas { canvas ->
                val factor = minOf(size.width / reference.width, size.height / reference.height)
                canvas.save()
                canvas.translate((size.width - reference.width * factor) / 2, (size.height - reference.height * factor) / 2)
                canvas.scale(factor, factor)
                renderer.draw(canvas, resolvedType, seconds.value, palette.first, palette.second)
                canvas.restore()
            }
        }
        if (label != null) {
            Spacer(Modifier.height(12.dp))
            HyperText(label)
        }
    }
}
