/** 文件职责：共用 Flutter 单日期选择器的底部玻璃面板、遮罩与确认流程。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun HyperDatePickerSheet(
    visible: Boolean,
    onClose: () -> Unit,
    onChanged: (HyperCalendarDate) -> Unit,
    initialDate: HyperCalendarDate,
    firstDate: HyperCalendarDate,
    lastDate: HyperCalendarDate,
    today: HyperCalendarDate,
) {
    require(firstDate <= lastDate)
    val progress = hyperOverlayProgress(visible)
    val currentOnClose by rememberUpdatedState(onClose)
    val currentOnChanged by rememberUpdatedState(onChanged)
    val initial = initialDate.coerceIn(firstDate, lastDate)
    var selection by remember { mutableStateOf(initial) }
    var result by remember { mutableStateOf<HyperCalendarDate?>(null) }
    LaunchedEffect(visible, initial, firstDate, lastDate) {
        if (visible) {
            selection = initial
            result = null
        }
    }
    // 与 Flutter route.completed 一致：宿主退出后才提交，后续导航不会叠着旧弹层。
    LaunchedEffect(visible, progress) {
        if (!visible && progress == 0f) {
            result?.let { date ->
                result = null
                currentOnChanged(date)
            }
        }
    }
    if (!visible && progress == 0f) return
    val glass = hyperGlass
    val dismiss = { if (visible) { result = null; currentOnClose() } }
    HyperDialogHost(onDismissRequest = dismiss, dismissOnBackPress = visible, edgeToEdge = true) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().background(glass.scrim.copy(alpha = glass.scrim.alpha * progress)),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(Modifier.matchParentSize().pointerInput(visible) { detectTapGestures { dismiss() } })
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(max = maxHeight * 0.85f)
                    .graphicsLayer { alpha = progress; translationY = 24.dp.toPx() * (1f - progress) }
                    .hyperFrostedSurface(glass.surfaceStrong, RoundedCornerShape(30.dp), border = BorderStroke(1.dp, glass.border), blur = glass.blurStrong)
                    // 面板空白区域参与命中测试，避免穿透至遮罩。
                    .pointerInput(visible) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                if (!visible) event.changes.forEach { it.consume() }
                            }
                        }
                    }
                    .padding(16.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CompositionLocalProvider(
                    LocalHyperSurfacePresent provides true,
                    LocalHyperContentColor provides HyperColors.primaryText,
                    LocalHyperTextStyle provides HyperTheme.typography.bodyMedium.copy(lineHeight = 19.6.sp),
                ) {
                    Box(Modifier.size(32.dp, 4.dp).background(HyperColors.divider, RoundedCornerShape(4.dp)))
                    Spacer(Modifier.height(16.dp))
                    HyperText("选择日期", modifier = Modifier.fillMaxWidth(), fontSize = 18.sp, lineHeight = 25.2.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    HyperCalendarPickerMonth(
                        visible = visible,
                        initialDate = initial,
                        firstDate = firstDate,
                        lastDate = lastDate,
                        selectedDate = selection,
                        today = today,
                        onChanged = { if (visible) selection = it },
                    )
                    Spacer(Modifier.height(12.dp))
                    HyperButton(
                        onPressed = { result = selection; currentOnClose() },
                        enabled = visible,
                        modifier = Modifier.fillMaxWidth(),
                        size = "large", type = "filled", colorStyle = "primary",
                    ) { HyperText("确定") }
                }
            }
        }
    }
}
