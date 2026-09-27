/** 文件职责：移植 HySlideMenu 列表侧滑操作；分类按钮另见 HyperFilterBar。 */
package hyper_ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable
import kotlin.math.roundToInt

enum class HyperSlideMenuReveal { Closed, Start, End }

data class HyperSlideAction(
    val label: String,
    val onClick: () -> Unit,
    val containerColor: Color = Color.Unspecified,
    val contentColor: Color = Color(1f, 1f, 1f, 1f),
    val iconContent: (@Composable () -> Unit)? = null
)

/** reveal 为受控展开方向，拖动中的瞬时位移由组件持有；操作执行后请求关闭。 */
@Composable
fun HyperSlideMenu(
    reveal: HyperSlideMenuReveal,
    onRevealChange: (HyperSlideMenuReveal) -> Unit,
    modifier: Modifier = Modifier,
    startActions: List<HyperSlideAction> = emptyList(),
    endActions: List<HyperSlideAction> = emptyList(),
    enabled: Boolean = true,
    actionExtent: Dp = 64.dp,
    shape: Shape = RoundedCornerShape(18.dp),
    content: @Composable () -> Unit
) {
    require(actionExtent > 0.dp) { "actionExtent 必须大于 0.dp" }
    require(startActions.isNotEmpty() || endActions.isNotEmpty()) { "至少提供一个侧滑操作" }
    val extentPx = with(LocalDensity.current) { actionExtent.toPx() }
    val startExtent = startActions.size * extentPx
    val endExtent = endActions.size * extentPx
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    var offset by remember { mutableStateOf(0f) }
    var dragging by remember { mutableStateOf(false) }
    LaunchedEffect(reveal, dragging, startExtent, endExtent, enabled) {
        if (!dragging) {
            val target = if (!enabled) 0f else when (reveal) {
                HyperSlideMenuReveal.Closed -> 0f
                HyperSlideMenuReveal.Start -> startExtent
                HyperSlideMenuReveal.End -> -endExtent
            }
            Animatable(offset).animateTo(target, spring(dampingRatio = 42f / (2f * kotlin.math.sqrt(470f)), stiffness = 470f)) {
                offset = value
            }
        }
    }
    Box(modifier.clip(shape).background(hyperGlass.controlTrack)
        .semantics {
            if (enabled) customActions = buildList {
                if (startActions.isNotEmpty()) add(CustomAccessibilityAction("展开起始侧操作") {
                    onRevealChange(HyperSlideMenuReveal.Start); true
                })
                if (endActions.isNotEmpty()) add(CustomAccessibilityAction("展开结束侧操作") {
                    onRevealChange(HyperSlideMenuReveal.End); true
                })
                add(CustomAccessibilityAction("关闭侧滑操作") { onRevealChange(HyperSlideMenuReveal.Closed); true })
            }
        }) {
        if (offset > 1f && startActions.isNotEmpty()) HyperSlideActionStrip(
            startActions, actionExtent, HyperColors.accent, enabled,
            Modifier.matchParentSize(), if (rtl) Alignment.CenterEnd else Alignment.CenterStart,
            onClose = { onRevealChange(HyperSlideMenuReveal.Closed) }
        )
        if (offset < -1f && endActions.isNotEmpty()) HyperSlideActionStrip(
            endActions, actionExtent, HyperColors.danger, enabled,
            Modifier.matchParentSize(), if (rtl) Alignment.CenterStart else Alignment.CenterEnd,
            onClose = { onRevealChange(HyperSlideMenuReveal.Closed) }
        )
        Box(Modifier.offset { IntOffset((if (rtl) -offset else offset).roundToInt(), 0) }
            .draggable(
                state = rememberDraggableState { delta ->
                    val proposed = offset + delta
                    val bounded = proposed.coerceIn(-endExtent, startExtent)
                    offset = bounded + (proposed - bounded) * 0.22f
                },
                orientation = Orientation.Horizontal, enabled = enabled, reverseDirection = rtl,
                onDragStarted = { dragging = true },
                onDragStopped = { velocity ->
                    val projected = offset + velocity * 0.09f
                    val target = when {
                        startExtent > 0f && projected > startExtent * 0.5f -> HyperSlideMenuReveal.Start
                        endExtent > 0f && projected < -endExtent * 0.5f -> HyperSlideMenuReveal.End
                        else -> HyperSlideMenuReveal.Closed
                    }
                    onRevealChange(target)
                    dragging = false
                }
            )
            .hyperFrostedSurface(hyperGlass.surface, shape, elevation = 0.dp, blur = 18.dp)) {
            content()
            if (!dragging && kotlin.math.abs(offset) > 1f) Box(Modifier.matchParentSize()
                .clickable { onRevealChange(HyperSlideMenuReveal.Closed) })
        }
    }
}

@Composable
private fun HyperSlideActionStrip(
    actions: List<HyperSlideAction>,
    extent: Dp,
    fallback: Color,
    enabled: Boolean,
    modifier: Modifier,
    alignment: Alignment,
    onClose: () -> Unit
) {
    Box(modifier, contentAlignment = alignment) {
        Row(Modifier.fillMaxHeight()) {
            actions.forEach { action ->
                Column(Modifier.width(extent).fillMaxHeight()
                    .background(resolveHyperContainerColor(action.containerColor, fallback))
                    .hyperNoRippleClickable(enabled = enabled, onClick = { action.onClick(); onClose() }),
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    CompositionLocalProvider(LocalHyperContentColor provides action.contentColor) {
                        action.iconContent?.invoke()
                        HyperText(action.label, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
