/** 文件职责：平台无关的受控玻璃 Toast；Android 原生 Toast 工具保留在 HyperToast.kt。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import hyper_ui.core.icon.HyperCheckIcon
import hyper_ui.core.icon.HyperCloseIcon
import kotlinx.coroutines.delay

enum class HyperToastTone { Neutral, Success, Info, Warning, Error }

/** 放置在页面 Box 的底部；显隐由调用方持有，计时结束仅请求关闭。 */
@Composable
fun HyperToast(
    visible: Boolean,
    message: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = 3000L,
    tone: HyperToastTone = HyperToastTone.Neutral,
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
    val toneColor = when (tone) {
        HyperToastTone.Neutral -> HyperColors.accent
        HyperToastTone.Success -> HyperColors.success
        HyperToastTone.Info -> HyperColors.info
        HyperToastTone.Warning -> HyperColors.warning
        HyperToastTone.Error -> HyperColors.danger
    }
    val surfaceColor = toneColor.copy(alpha = 0.04f).compositeOver(hyperGlass.surfaceStrong)
    Row(
        modifier.hyperOverlayMotion(transitionProgress).widthIn(min = 180.dp, max = 420.dp)
            .hyperFrostedSurface(
                surfaceColor, RoundedCornerShape(18.dp), blur = 28.dp,
                border = BorderStroke(1.dp, toneColor.copy(alpha = 0.4f))
            )
            .semantics { liveRegion = LiveRegionMode.Polite }
            .padding(start = 12.dp, top = 10.dp, end = 8.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalHyperContentColor provides HyperColors.primaryText) {
            if (leadingContent != null) leadingContent() else HyperToastLeading(tone, toneColor)
            HyperText(message, Modifier.weight(1f, fill = false),
                style = HyperTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                maxLines = 3, overflow = TextOverflow.Ellipsis)
            actionContent?.invoke(this)
        }
    }
}

@Composable
private fun HyperToastLeading(tone: HyperToastTone, color: Color) {
    Box(Modifier.size(30.dp).background(color.copy(alpha = 0.12f), CircleShape),
        contentAlignment = Alignment.Center) {
        CompositionLocalProvider(LocalHyperContentColor provides color) {
            when (tone) {
                HyperToastTone.Success -> HyperCheckIcon(Modifier.size(17.dp))
                HyperToastTone.Error -> HyperCloseIcon(Modifier.size(17.dp))
                HyperToastTone.Neutral -> Box(Modifier.size(6.dp).background(color, CircleShape))
                HyperToastTone.Info -> HyperText("i", style = HyperTheme.typography.labelLarge)
                HyperToastTone.Warning -> HyperText("!", style = HyperTheme.typography.labelLarge)
            }
        }
    }
}
