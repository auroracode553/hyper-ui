/** 文件职责：为 Desktop/Wasm Preview 提供播放速度面板的跨平台图标替身。 */
package hyper_ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun HyperPlaybackSpeedGaugeGlyph(modifier: Modifier = Modifier) {
    HyperPlaybackSpeedPreviewIcon(
        imageVector = Icons.Default.PlayArrow,
        modifier = modifier
    )
}

@Composable
internal fun HyperPlaybackSpeedCloseGlyph(modifier: Modifier = Modifier) {
    HyperPlaybackSpeedPreviewIcon(
        imageVector = Icons.Default.Close,
        modifier = modifier
    )
}

@Composable
internal fun HyperPlaybackSpeedResetGlyph(modifier: Modifier = Modifier) {
    HyperPlaybackSpeedPreviewIcon(
        imageVector = Icons.Default.Refresh,
        modifier = modifier
    )
}

@Composable
internal fun HyperPlaybackSpeedHintGlyph(modifier: Modifier = Modifier) {
    HyperPlaybackSpeedPreviewIcon(
        imageVector = Icons.Default.PlayArrow,
        modifier = modifier
    )
}

@Composable
internal fun HyperPlaybackSpeedEditGlyph(modifier: Modifier = Modifier) {
    HyperPlaybackSpeedPreviewIcon(
        imageVector = Icons.Default.Edit,
        modifier = modifier
    )
}

@Composable
private fun HyperPlaybackSpeedPreviewIcon(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = modifier.fillMaxSize()
    )
}
