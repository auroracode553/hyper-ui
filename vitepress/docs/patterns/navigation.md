# 底部导航

`HyperBottomBar` 不依赖任何导航框架。它报告被点击的项目，由调用方更新选中项并决定是否导航。

```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import hyper_ui.*

@Composable
fun MainBottomBar(onDestinationSelected: (String) -> Unit) {
    var selectedId by remember { mutableStateOf("home") }
    val items = listOf(
        HyperBottomBarItem("home", "首页", Icons.Default.Home),
        HyperBottomBarItem("settings", "设置", Icons.Default.Settings)
    )

    HyperBottomBar(
        items = items,
        selectedItemId = selectedId,
        onItemClick = { item ->
            selectedId = item.id
            onDestinationSelected(item.id)
        }
    )
}
```
