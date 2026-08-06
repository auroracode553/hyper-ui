# 设置页

设置行的业务状态由页面持有，`HyperMenuItem` 只负责布局，`HyperSwitch` 只报告切换事件。

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.*

@Composable
fun SettingsScreen() {
    var pushEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperTopBar(title = "设置")
        HyperMenuGroup {
            HyperMenuItem(
                title = "推送通知",
                description = "接收重要消息提醒",
                trailing = {
                    HyperSwitch(
                        checked = pushEnabled,
                        onCheckedChange = { pushEnabled = it }
                    )
                }
            )
        }
    }
}
```
