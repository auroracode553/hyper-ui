/** 安全区预览：模拟系统区、切换方向与嵌套，检验统一最小留白及单次消费。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.background
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton(onPressed = { top = !top }) { HyperText(if (top) "顶部" else "底部") }
            HyperButton(onPressed = { nested = !nested }) { HyperText(if (nested) "嵌套：开" else "嵌套：关") }
        }
        HyperButton(onPressed = { systemArea = !systemArea }) {
            HyperText(if (systemArea) "系统区：开" else "系统区：关")
        }
        HyperButton(onPressed = { portrait = !portrait }) { HyperText(if (portrait) "竖屏" else "横屏") }
        HyperText("嵌套不重复；竖屏底部最小 20dp，横屏只避系统区；操作 $count 次")
        CompositionLocalProvider(LocalHyperSafeAreaWindowInsets provides
            WindowInsets(top = if (systemArea) 48.dp else 0.dp,
                bottom = if (systemArea) 28.dp else 0.dp),
            LocalHyperSafeAreaPortraitOverride provides portrait) {
            Box(Modifier.fillMaxWidth().height(220.dp).background(HyperColors.softContainer)) {
                val action: @Composable BoxScope.() -> Unit = {
                    HyperButton(onPressed = { count++ }) { HyperText("操作按钮") }
                }
                HyperSafeArea(edge, Modifier.align(if (top) Alignment.TopCenter else Alignment.BottomCenter)) {
                    if (nested) HyperSafeArea(edge, content = action) else action()
                }
            }
        }
    }
}
