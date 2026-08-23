/** 文件职责：为 Desktop/Wasm Preview 提供 Android Lucide 图标的跨平台替身。 */
package hyper_ui.core.icon

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
internal fun HyperGaugeIcon(modifier: Modifier = Modifier) {
    HyperPreviewIcon(Icons.Default.PlayArrow, modifier)
}

@Composable
internal fun HyperCloseIcon(modifier: Modifier = Modifier) {
    HyperPreviewIcon(Icons.Default.Close, modifier)
}

@Composable
internal fun HyperResetIcon(modifier: Modifier = Modifier) {
    HyperPreviewIcon(Icons.Default.Refresh, modifier)
}

@Composable
internal fun HyperHorizontalDragIcon(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxSize()) {
        HyperPreviewIcon(
            imageVector = Icons.Default.ArrowBack,
            modifier = Modifier.weight(1f)
        )
        HyperPreviewIcon(
            imageVector = Icons.Default.ArrowForward,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
internal fun HyperEditIcon(modifier: Modifier = Modifier) {
    HyperPreviewIcon(Icons.Default.Edit, modifier)
}

@Composable
internal fun HyperFastForwardIcon(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxSize()) {
        repeat(2) {
            HyperPreviewIcon(
                imageVector = Icons.Default.PlayArrow,
                modifier = Modifier.weight(1f)
            )
        }
}

@Composable
internal fun HyperChargingIcon(modifier: Modifier = Modifier) {
    HyperPreviewIcon(Icons.Default.Star, modifier)
}

@Composable
internal fun HyperCheckIcon(modifier: Modifier = Modifier) {
    HyperPreviewIcon(Icons.Default.Check, modifier)
}

@Composable
private fun HyperPreviewIcon(
    imageVector: ImageVector,
    modifier: Modifier
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = modifier.fillMaxSize()
    )
}
