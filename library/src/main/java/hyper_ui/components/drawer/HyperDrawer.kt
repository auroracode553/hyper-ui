/** 贴边全高的模态抽屉；业务内容与关闭状态由调用方提供。 */
package hyper_ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

enum class HyperDrawerPlacement { start, end }

/** 安全区和键盘避让位于面板背景内部；contentPadding 只影响正文。 */
@Composable
fun HyperDrawer(
    open: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    placement: HyperDrawerPlacement = HyperDrawerPlacement.start,
    width: Dp = 320.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scrollable: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    gesturesEnabled: Boolean = true,
    title: String? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    require(width > 0.dp && width.value.isFinite()) { "width 必须是有限正数" }
    val progress = hyperOverlayProgress(open, durationMillis = HyperMotionSpec.OverlayDuration)
    val close by rememberUpdatedState(onClose)
    val drag = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(open) { if (open) drag.snapTo(0f) }
    if (!open && progress == 0f) return
    val isLeft = (placement == HyperDrawerPlacement.start) ==
        (LocalLayoutDirection.current == LayoutDirection.Ltr)
    val sign = if (isLeft) -1f else 1f
    val density = LocalDensity.current
    val dismiss = { if (open) close() }
    HyperDialogHost(onDismissRequest = dismiss,
        dismissOnBackPress = open && dismissOnBackPress, edgeToEdge = true) {
        BoxWithConstraints(modifier.fillMaxSize()) {
            val drawerWidth = minOf(width, (maxWidth - 56.dp).coerceAtLeast(0.dp))
            val widthPx = with(density) { drawerWidth.toPx() }
            val velocityThreshold = with(density) { 400.dp.toPx() }
            val visibility = if (widthPx == 0f) 0f else progress * (1f - drag.value / widthPx).coerceIn(0f, 1f)
            val scrim = hyperGlass.scrim
            Box(Modifier.fillMaxSize()
                .background(scrim.copy(alpha = scrim.alpha * visibility))
                .pointerInput(open, dismissOnClickOutside) {
                    detectTapGestures { if (dismissOnClickOutside) dismiss() }
                })
            Column(Modifier.align(if (placement == HyperDrawerPlacement.start) Alignment.CenterStart else Alignment.CenterEnd)
                .width(drawerWidth).fillMaxHeight()
                .graphicsLayer { translationX = sign * (widthPx * (1f - progress) + drag.value) }
                .background(HyperColors.cardContainer)
                .pointerInput(open) {
                    // 面板空白处也命中面板，不能穿透到遮罩。
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent(androidx.compose.ui.input.pointer.PointerEventPass.Initial)
                            if (!open) event.changes.forEach { it.consume() }
                        }
                    }
                }
                .draggable(
                    state = rememberDraggableState { delta ->
                        val next = (drag.value + delta * sign).coerceIn(0f, widthPx)
                        scope.launch { drag.snapTo(next) }
                    },
                    orientation = Orientation.Horizontal,
                    enabled = open && gesturesEnabled,
                    onDragStarted = { drag.stop() },
                    onDragStopped = { velocity ->
                        if (drag.value >= widthPx * .3f || velocity * sign >= velocityThreshold) {
                            dismiss()
                        } else {
                            drag.animateTo(0f, tween(180, easing = HyperMotionSpec.OverlayCurve))
                        }
                    }
                )
                .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                CompositionLocalProvider(LocalHyperSurfacePresent provides true,
                    LocalHyperContentColor provides HyperColors.primaryText) {
                    if (title != null) Row(
                        Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HyperText(title, Modifier.weight(1f), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        HyperTooltip("关闭") {
                            HyperButton(onPressed = dismiss, type = "icon") {
                                HyperIcon(Lucide.X, null)
                            }
                        }
                    }
                    Column(Modifier.weight(1f).fillMaxWidth()
                        .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier)
                        .padding(contentPadding), content = content)
                    if (footer != null) {
                        HyperDivider()
                        Column(Modifier.fillMaxWidth().padding(16.dp), content = footer)
                    }
                }
            }
        }
    }
}
