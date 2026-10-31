# 底部导航

`HyperTabBar` 不依赖导航框架，通过泛型 items 与 itemContent 提供内容。single 独立圆形动作入口固定直径 48dp，multiple 按钮组默认内容高度 56dp，仅按钮组按标签字体在库内扩展；并排时垂直居中，不由业务层覆写尺寸。multiple 默认采用悬浮胶囊，floating=false 为全宽平直贴底表面；正文保持全屏，末尾避让使用宿主实测净空。完整参数及示例见 [HyperTabBar](../components/navigation/hyper-tab-bar.md)。

```kotlin
import androidx.compose.foundation.layout.Column
import hyper_ui.HyperIcon
import hyper_ui.HyperText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Settings
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
        AppDestination("home", "首页", Lucide.House),
        AppDestination("settings", "设置", Lucide.Settings)
    )

    HyperTabBar(
        items = items,
        selectedIndex = items.indexOfFirst { it.id == selectedId },
        onChanged = { index ->
            val item = items[index]
            selectedId = item.id
            onDestinationSelected(item.id)
        }
    ) { item ->
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HyperIcon(
                icon = item.icon,
                contentDescription = item.label
            )
            HyperText(item.label)
        }
    }
}
```
