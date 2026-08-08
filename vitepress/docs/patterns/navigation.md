# 底部导航

`HyperBottomBar` 不依赖任何导航框架。它报告被点击的项目，由调用方更新选中项并决定是否导航。

```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import hyper_ui.*

data class AppDestination(
    val id: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun MainBottomBar(onDestinationSelected: (String) -> Unit) {
    var selectedId by remember { mutableStateOf("home") }
    val items = listOf(
        AppDestination("home", "首页", Icons.Default.Home),
        AppDestination("settings", "设置", Icons.Default.Settings)
    )

    HyperBottomBar(
        items = items,
        itemSelected = { it.id == selectedId },
        onItemClick = { item ->
            selectedId = item.id
            onDestinationSelected(item.id)
        }
    ) { item ->
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(item.icon, contentDescription = item.label)
            Text(item.label)
        }
    }
}
```
