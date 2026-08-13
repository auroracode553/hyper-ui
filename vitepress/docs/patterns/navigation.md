# 底部导航

`HyperTabBar` 不依赖任何导航框架。深色模式下容器与默认描边直接采用当前页面背景色，避免出现发灰的玻璃层；浅色模式保留轻量透明度。组件不叠加玻璃高光，使用 55dp 标签操作区和 5dp 轻量底部留白，默认总高度为 60dp。需要完全自定义内容时使用完整 slot；需要统一处理点击与选中颜色时使用泛型 items 入口。

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

    HyperTabBar(
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
