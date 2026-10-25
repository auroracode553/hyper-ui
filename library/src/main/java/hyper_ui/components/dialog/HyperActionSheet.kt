/** 文件职责：与 Flutter 对齐的底部弹层，提供自定义正文与操作列表两个入口。 */
package hyper_ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Check

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 自定义内容入口；visible 与 onClose 由调用方管理。 */
@Composable
fun HyperActionSheet(
    visible: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissible: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val progress = hyperOverlayProgress(visible)
    val currentOnClose by rememberUpdatedState(onClose)
    if (!visible && progress == 0f) return
    val dismiss = { if (visible) currentOnClose() }
    val glass = hyperGlass
    HyperDialogHost(onDismissRequest = dismiss, dismissOnBackPress = visible, edgeToEdge = true) {
        BoxWithConstraints(Modifier.fillMaxSize().imePadding()) {
            Box(Modifier.fillMaxSize()
                .background(glass.scrim.copy(alpha = glass.scrim.alpha * progress))
                .pointerInput(visible, dismissible) {
                    detectTapGestures { if (dismissible) dismiss() }
                })
            Column(modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .heightIn(max = maxHeight * .85f)
                .graphicsLayer {
                    alpha = progress
                    translationY = 24.dp.toPx() * (1f - progress)
                }
                .hyperFrostedSurface(
                    containerColor = glass.surfaceStrong,
                    shape = RoundedCornerShape(30.dp),
                    border = BorderStroke(1.dp, glass.border),
                    blur = glass.blurStrong
                )
                .pointerInput(visible) {
                    // 注册整块面板命中；退出期间阻止操作正文，背景仍保持模态。
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Initial)
                            if (!visible) event.changes.forEach { it.consume() }
                        }
                    }
                }
                .padding(16.dp)
                .windowInsetsPadding(HyperTheme.systemSafeAreaInsets.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))
                .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                CompositionLocalProvider(LocalHyperSurfacePresent provides true,
                    LocalHyperContentColor provides HyperColors.primaryText) {
                    Box(Modifier.align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp).size(width = 32.dp, height = 4.dp)
                        .background(HyperColors.divider, RoundedCornerShape(4.dp)))
                    if (title != null) HyperText(title,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Column(Modifier.fillMaxWidth(), content = content)
                }
            }
        }
    }
}

/** 操作列表入口；选择先请求关闭再回传 value，禁用项不触发回调。 */
@Composable
fun <T> HyperActionSheet(
    visible: Boolean,
    onClose: () -> Unit,
    actions: List<HyperAction<T>>,
    onChanged: (T) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissible: Boolean = true,
    cancelLabel: String = "取消"
) {
    HyperActionSheet(visible, onClose, modifier, title, dismissible) {
        actions.forEach { action ->
            val color = if (action.isDanger) HyperColors.danger else HyperColors.accent
            HyperListTile(
                type = "plain", enabled = action.enabled, selected = action.selected,
                title = { HyperText(action.label) },
                colors = HyperListTileDefaults.colors(contentColor = if (action.isDanger) color else HyperColors.primaryText),
                leadingIcon = action.icon, leadingColor = color, showChevron = false,
                trailing = if (action.selected) ({ HyperIcon(Lucide.Check, null) }) else null,
                onPressed = { onClose(); onChanged(action.value) }
            )
        }
        Spacer(Modifier.height(12.dp))
        HyperButton(onPressed = onClose, modifier = Modifier.fillMaxWidth()) { HyperText(cancelLabel) }
    }
}
