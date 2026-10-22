/** 文件职责：平台无关的受控玻璃 Toast；Android 原生 Toast 工具保留在 HyperToast.kt。 */
package hyper_ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Sparkles
import com.composables.icons.lucide.X

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import hyper_ui.core.interaction.hyperNoRippleClickable
import kotlinx.coroutines.delay


/** 放置在页面 Box 的底部；显隐由调用方持有，计时结束仅请求关闭。 */
@Composable
fun HyperToast(
    visible: Boolean,
    message: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    duration: Long = 2000L,
    type: String = "neutral",
    color: Color = Color.Unspecified,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    action: (@Composable RowScope.() -> Unit)? = null
) {
    require(duration >= 0L) { "duration 不能小于 0" }
    val dismiss by rememberUpdatedState(onClose)
    LaunchedEffect(visible, message, duration) {
        if (visible && duration > 0) { delay(duration); dismiss() }
    }
    if (!visible) return
    val toneColor = resolveHyperContainerColor(color, when (type) {
        "neutral" -> HyperColors.secondaryText
        "primary" -> HyperColors.accent
        "success" -> HyperColors.success
        "info" -> HyperColors.info
        "warning" -> HyperColors.warning
        "error" -> HyperColors.danger
        else -> error("不支持的 HyperToast type: $type")
    })
    val surfaceColor = toneColor.copy(alpha = 10 / 255f).compositeOver(hyperGlass.surfaceStrong)
    Row(
        modifier.widthIn(min = 180.dp, max = 420.dp)
            .hyperFrostedSurface(
                surfaceColor, RoundedCornerShape(18.dp), blur = hyperGlass.blurStrong,
                border = BorderStroke(1.dp, toneColor.copy(alpha = 105 / 255f))
            )
            .semantics { liveRegion = LiveRegionMode.Polite }
            .padding(start = 12.dp, top = 10.dp, end = 8.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalHyperContentColor provides HyperColors.primaryText) {
            if (leading != null) leading() else HyperToastLeading(type, toneColor)
            Spacer(Modifier.width(10.dp))
            HyperText(message, Modifier.weight(1f, fill = false),
                style = HyperTheme.typography.bodyMedium.copy(fontSize = 14.sp, lineHeight = 18.9.sp, fontWeight = FontWeight.Medium),
                maxLines = 3, overflow = TextOverflow.Ellipsis)
            if (action != null) {
                Spacer(Modifier.width(10.dp))
                action.invoke(this)
            } else if (actionLabel != null && onAction != null) {
                HyperText(actionLabel,
                    modifier = Modifier.hyperNoRippleClickable(onClick = {
                        onClose()
                        onAction()
                    }).padding(horizontal = 8.dp, vertical = 6.dp),
                    color = toneColor, fontSize = 14.sp, lineHeight = 19.6.sp,
                    fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun HyperToastLeading(type: String, color: Color) {
    Box(Modifier.size(30.dp).background(color.copy(alpha = 24 / 255f), CircleShape),
        contentAlignment = Alignment.Center) {
        CompositionLocalProvider(LocalHyperContentColor provides color) {
            HyperIcon(when (type) {
                "success" -> Lucide.Check
                "error" -> Lucide.X
                "neutral" -> Lucide.Bell
                "primary" -> Lucide.Sparkles
                "info" -> Lucide.Info
                "warning" -> Lucide.CircleAlert
                else -> Lucide.Bell
            }, null, Modifier.size(17.dp))
        }
    }
}
