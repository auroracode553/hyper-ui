# 底部导航

`HyperBottomBar` 不依赖任何导航框架。浅色模式保留透明玻璃效果，深色模式使用不透明实色底栏。需要完全自定义按钮、徽标、输入框或快捷功能时，直接使用完整内容 slot；需要统一处理项目点击和选中颜色时，可使用泛型 items 入口。

```kotlin
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR
import hyper_ui.*

data class AppDestination(
    val id: String,
    val label: String,
    @DrawableRes val iconRes: Int
)

@Composable
fun MainBottomBar(onDestinationSelected: (String) -> Unit) {
    var selectedId by remember { mutableStateOf("home") }
    val items = listOf(
        AppDestination("home", "首页", LucideR.drawable.lucide_ic_house),
        AppDestination("settings", "设置", LucideR.drawable.lucide_ic_settings)
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
            Icon(
                painter = painterResource(item.iconRes),
                contentDescription = item.label
            )
            Text(item.label)
        }
    }
}
```
