/** 文件职责：使用 Lucide Android 资源提供播放速度面板的默认图标。 */
package hyper_ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR

@Composable
internal fun HyperPlaybackSpeedGaugeGlyph(modifier: Modifier = Modifier) {
    HyperPlaybackSpeedLucideIcon(
        drawableResource = LucideR.drawable.lucide_ic_gauge,
        modifier = modifier
    )
}

@Composable
internal fun HyperPlaybackSpeedCloseGlyph(modifier: Modifier = Modifier) {
    HyperPlaybackSpeedLucideIcon(
        drawableResource = LucideR.drawable.lucide_ic_x,
        modifier = modifier
    )
}

@Composable
internal fun HyperPlaybackSpeedResetGlyph(modifier: Modifier = Modifier) {
    HyperPlaybackSpeedLucideIcon(
        drawableResource = LucideR.drawable.lucide_ic_rotate_ccw,
        modifier = modifier
    )
}

@Composable
internal fun HyperPlaybackSpeedHintGlyph(modifier: Modifier = Modifier) {
    HyperPlaybackSpeedLucideIcon(
        drawableResource = LucideR.drawable.lucide_ic_move_horizontal,
        modifier = modifier
    )
}

@Composable
internal fun HyperPlaybackSpeedEditGlyph(modifier: Modifier = Modifier) {
    HyperPlaybackSpeedLucideIcon(
        drawableResource = LucideR.drawable.lucide_ic_pencil,
        modifier = modifier
    )
}

@Composable
private fun HyperPlaybackSpeedLucideIcon(
    drawableResource: Int,
    modifier: Modifier
) {
    Icon(
        painter = painterResource(drawableResource),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        tint = LocalContentColor.current
    )
}
