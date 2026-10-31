package hyper_ui.docs.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.*

/** 与 Flutter 相同的双列加载动画画廊。 */
@Composable
fun LoadingDemo() {
    val types = HyperLoadingType.entries
    val names = listOf("行波脉冲", "双弦错织", "弧带换向", "三体协奏", "双彗交汇",
        "光子穿梭", "磁光星芒", "流金沙漏", "变频鳍片", "柔波光栅")
    Column(Modifier.fillMaxWidth()) {
        HyperText("默认随机")
        Spacer(Modifier.height(16.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            HyperLoading(label = "加载中")
        }
        Spacer(Modifier.height(24.dp))
        HyperText("十款原生动画")
        Spacer(Modifier.height(8.dp))
        repeat(5) { row ->
            Row(Modifier.fillMaxWidth()) {
                repeat(2) { col ->
                    val index = row * 2 + col
                    Column(Modifier.weight(1f).height(170.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Box(Modifier.height(132.dp), contentAlignment = Alignment.Center) {
                            HyperLoading(type = types[index], label = names[index])
                        }
                        Spacer(Modifier.height(8.dp))
                        HyperText(types[index].name, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
