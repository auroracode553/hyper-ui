# 设置页

设置行的业务状态由页面持有，`HyperListTile` 只负责布局，`HyperSwitch` 只报告切换事件。

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

    HyperNavBarPage(
        navBar = { HyperNavBar(titleContent = { HyperText("设置") }) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HyperMenuGroup {
                HyperListTile(
                    headlineContent = { HyperText("推送通知") },
                    supportingContent = { HyperText("接收重要消息提醒") },
                    trailingContent = {
                        HyperSwitch(
                            checked = pushEnabled,
                            onCheckedChange = { pushEnabled = it }
                        )
                    }
                )
            }
        }
    }
}
```

导航栏保持库内默认透明样式与固定顶部避让；正文的 `padding(contentPadding)` 位于 `verticalScroll` 之后，滚动时内容可经过导航栏后方。
