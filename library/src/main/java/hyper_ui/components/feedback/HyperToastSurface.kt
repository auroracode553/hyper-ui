/** 文件职责：平台无关的受控玻璃 Toast；Android 原生 Toast 工具保留在 HyperToast.kt。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/** 放置在页面 Box 的底部；显隐由调用方持有，计时结束仅请求关闭。 */
@Composable
fun HyperToast(
    visible: Boolean,
    message: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = 3000L,
    leadingContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
) {
    require(durationMillis >= 0L) { "durationMillis 不能小于 0" }
    val dismiss by rememberUpdatedState(onDismissRequest)
    LaunchedEffect(visible, message, durationMillis) {
        if (visible && durationMillis > 0) { delay(durationMillis); dismiss() }
    }
    val transitionProgress = hyperOverlayProgress(visible)
    if (!visible && transitionProgress == 0f) return
    Row(
        modifier.hyperOverlayMotion(transitionProgress).widthIn(max = 480.dp)
            .hyperFrostedSurface(hyperGlass.surfaceStrong, RoundedCornerShape(18.dp), blur = 28.dp)
            .semantics { liveRegion = LiveRegionMode.Polite }
            .padding(start = 12.dp, top = 10.dp, end = 8.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalHyperContentColor provides HyperColors.primaryText) {
            leadingContent?.invoke()
            HyperText(message, Modifier.weight(1f, fill = false),
                style = HyperTheme.typography.bodyMedium.copy(fontSize = 14.sp))
            actionContent?.invoke(this)
        }
    }
}
