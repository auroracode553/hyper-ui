/** 文件职责：封装跨平台背景采样；材质组件只依赖本模块，不直接依赖 Haze。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource

@Stable
class HyperBackdropState internal constructor(internal val haze: HazeState)

@Composable
fun rememberHyperBackdropState(): HyperBackdropState = remember { HyperBackdropState(HazeState()) }

internal val LocalHyperBackdrop = staticCompositionLocalOf<HyperBackdropState?> { null }

/** 背景与前景分层录制，避免玻璃采样自身产生递归。动态图片或滚动内容放入 background。 */
@Composable
fun HyperBackdrop(
    modifier: Modifier = Modifier,
    state: HyperBackdropState = rememberHyperBackdropState(),
    background: @Composable BoxScope.() -> Unit = { HyperSoftBackground(Modifier.matchParentSize()) },
    content: @Composable BoxScope.() -> Unit
) {
    CompositionLocalProvider(LocalHyperBackdrop provides state) {
        Box(modifier) {
            Box(Modifier.matchParentSize().hazeSource(state.haze), content = background)
            content()
        }
    }
}

/** 为需要分开组合的背景层提供显式采样入口，例如导航栏后方的 LazyColumn。 */
fun Modifier.hyperBackdropSource(state: HyperBackdropState): Modifier = hazeSource(state.haze)

internal fun Modifier.hyperBackdropEffect(blur: Dp = 20.dp): Modifier = composed {
    val state = LocalHyperBackdrop.current
    if (state == null || blur <= 0.dp) Modifier else Modifier.hazeEffect(
        state = state.haze,
        style = HazeStyle(
            backgroundColor = HyperColors.pageBackground,
            tints = listOf(HazeTint(Color.Transparent)),
            blurRadius = blur,
            noiseFactor = 0f,
            fallbackTint = HazeTint(Color.Transparent)
        )
    )
}

/** 对齐 HySoftBackground 的三段静态环境色；不添加装饰性循环动画。 */
@Composable
fun HyperSoftBackground(
    modifier: Modifier = Modifier,
    intensity: Float = 1f,
    content: @Composable BoxScope.() -> Unit = {}
) {
    require(intensity in 0f..1f) { "intensity 必须在 0f..1f 内" }
    val alpha = kotlin.math.round(28 * intensity) / 255f
    val background = HyperColors.pageBackground
    Box(
        modifier.background(Brush.linearGradient(
            0f to HyperColors.accent.copy(alpha = alpha).compositeOver(background),
            0.52f to background,
            1f to rgba(154, 130, 215, alpha).compositeOver(background)
        )),
        content = content
    )
}
