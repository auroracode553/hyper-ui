/** 安全区预览：模拟系统区、切换方向与嵌套，检验统一最小留白及单次消费。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.*

@Composable
fun SafeAreaDemo() {
    var portrait by remember { mutableStateOf(true) }
    var top by remember { mutableStateOf(false) }
    var nested by remember { mutableStateOf(false) }
    var systemArea by remember { mutableStateOf(true) }
    var count by remember { mutableStateOf(0) }
    val edge = if (top) HyperSafeAreaEdge.Top else HyperSafeAreaEdge.Bottom
    // 整个手机屏幕就是演示窗口，安全区不再落在页面中间的局部画框上。
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier.align(Alignment.Center).fillMaxWidth()
                .verticalScroll(rememberScrollState()).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HyperText("手机边缘安全区")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HyperButton(onPressed = { top = !top }) { HyperText(if (top) "顶部" else "底部") }
                HyperButton(onPressed = { nested = !nested }) { HyperText(if (nested) "嵌套：开" else "嵌套：关") }
            }
            HyperButton(onPressed = { systemArea = !systemArea }) {
                HyperText(if (systemArea) "系统区：开" else "系统区：关")
            }
            HyperButton(onPressed = { portrait = !portrait }) { HyperText(if (portrait) "方向规则：竖屏" else "方向规则：横屏") }
            HyperText("操作区直接贴在手机${if (top) "顶部" else "底部"}，背景延伸到屏幕边缘。")
            HyperText(if (systemArea) "同侧嵌套只避让一次。操作 $count 次" else "贴边对照：操作区直接到达屏幕边缘。操作 $count 次")
        }
        val action: @Composable BoxScope.() -> Unit = {
            HyperButton(onPressed = { count++ }) { HyperText("操作按钮") }
        }
        Box(
            Modifier.align(if (top) Alignment.TopCenter else Alignment.BottomCenter)
                .fillMaxWidth().background(HyperColors.softContainer),
            contentAlignment = Alignment.Center
        ) {
            if (systemArea) {
                CompositionLocalProvider(
                    LocalHyperSafeAreaWindowInsets provides WindowInsets(top = PreviewTopSafeArea, bottom = 28.dp),
                    LocalHyperSafeAreaPortraitOverride provides portrait
                ) {
                    HyperSafeArea(edge) {
                        if (nested) HyperSafeArea(edge, content = action) else action()
                    }
                }
            } else {
                action()
            }
        }
    }
}
