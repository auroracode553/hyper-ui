/** 文件职责：逐分量移植 Flutter HyGlassTheme，供所有材质组件共享。 */
package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** 与参考项目相同的应用/局部子树材质档位。 */
enum class HyperMaterial { Solid, Soft, Clear }

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
    val scrim: Color,
    val blur: Dp = 20.dp,
    val blurStrong: Dp = 28.dp
) {
    val border: Color get() = edgeShade.compositeOver(edgeHighlight)

    companion object {
        fun forMaterial(darkTheme: Boolean, material: HyperMaterial): HyperGlassTokens {
            val base = if (darkTheme) dark() else light()
            if (material == HyperMaterial.Soft) return base
            val solid = material == HyperMaterial.Solid
            val surface = if (darkTheme) rgba(27, 31, 39) else Color(1f, 1f, 1f, 1f)
            return base.copy(
                surface = surface.copy(alpha = if (solid) 1f else 118 / 255f),
                surfaceStrong = surface.copy(alpha = if (solid) 1f else 153 / 255f),
                surfaceSubtle = surface.copy(alpha = if (solid) 1f else 77 / 255f),
                edgeHighlight = if (solid) {
                    if (darkTheme) rgba(52, 59, 71) else rgba(17, 18, 22, 24 / 255f)
                } else base.edgeHighlight,
                edgeShade = if (solid) Color(0f, 0f, 0f, 0f) else base.edgeShade,
                blur = if (solid) 0.dp else 32.dp,
                blurStrong = if (solid) 0.dp else 40.dp
            )
        }

        fun light(): HyperGlassTokens = HyperGlassTokens(
            rgba(255, 255, 255, 217 / 255f), rgba(255, 255, 255, 247 / 255f),
            rgba(255, 255, 255, 191 / 255f), rgba(255, 255, 255, 230 / 255f),
            rgba(17, 18, 22, 18 / 255f), rgba(17, 26, 40, 20 / 255f),
            rgba(0, 0, 0, 22 / 255f), rgba(0, 0, 0, 31 / 255f),
            rgba(0, 0, 0, 20 / 255f), rgba(8, 11, 18, 82 / 255f)
        )

        fun dark(): HyperGlassTokens = HyperGlassTokens(
            rgba(30, 32, 38, 217 / 255f), rgba(44, 46, 51, 242 / 255f),
            rgba(30, 32, 38, 179 / 255f), rgba(255, 255, 255, 46 / 255f),
            rgba(255, 255, 255, 31 / 255f), rgba(0, 0, 0, 82 / 255f),
            rgba(255, 255, 255, 36 / 255f), rgba(255, 255, 255, 31 / 255f),
            rgba(255, 255, 255, 31 / 255f), rgba(0, 0, 0, 153 / 255f)
        )
    }
}

internal val hyperGlass: HyperGlassTokens
    @Composable @ReadOnlyComposable get() = HyperTheme.glass
