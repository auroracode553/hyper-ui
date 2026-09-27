/** 文件职责：逐分量移植 Flutter HyGlassTheme，供所有材质组件共享。 */
package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

@Immutable
data class HyperGlassTokens(
    val surface: Color,
    val surfaceStrong: Color,
    val surfaceSubtle: Color,
    val edgeHighlight: Color,
    val edgeShade: Color,
    val shadow: Color,
    val controlTrack: Color,
    val selection: Color,
    val pressed: Color,
    val scrim: Color
) {
    val border: Color get() = edgeShade.compositeOver(edgeHighlight)

    companion object {
        fun light(): HyperGlassTokens = HyperGlassTokens(
            rgba(255, 255, 255, 217 / 255f), rgba(255, 255, 255, 247 / 255f),
            rgba(255, 255, 255, 191 / 255f), rgba(255, 255, 255, 230 / 255f),
            rgba(17, 18, 22, 18 / 255f), rgba(17, 26, 40, 24 / 255f),
            rgba(0, 0, 0, 22 / 255f), rgba(0, 0, 0, 31 / 255f),
            rgba(0, 0, 0, 20 / 255f), rgba(8, 11, 18, 82 / 255f)
        )

        fun dark(): HyperGlassTokens = HyperGlassTokens(
            rgba(30, 32, 38, 217 / 255f), rgba(44, 46, 51, 242 / 255f),
            rgba(30, 32, 38, 179 / 255f), rgba(255, 255, 255, 46 / 255f),
            rgba(255, 255, 255, 31 / 255f), rgba(0, 0, 0, 128 / 255f),
            rgba(255, 255, 255, 36 / 255f), rgba(255, 255, 255, 31 / 255f),
            rgba(255, 255, 255, 31 / 255f), rgba(0, 0, 0, 153 / 255f)
        )
    }
}

internal val hyperGlass: HyperGlassTokens
    @Composable @ReadOnlyComposable get() = HyperTheme.glass
