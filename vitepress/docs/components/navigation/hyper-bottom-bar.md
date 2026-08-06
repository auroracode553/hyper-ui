# HyperBottomBar

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperBottomBar.kt`
- 状态归属：调用方提供选中项并执行导航
- Preview ID：`bottom-bar`

纯 UI 底部导航栏。组件负责选中态、图标、标题和点击区域，不依赖任何导航框架。

## 公开类型

```kotlin
data class HyperBottomBarItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)

enum class HyperBottomMenuLayout {
    Equal,
    Arrangement
}

data class HyperBottomBarConfig(
    val height: Dp = 70.dp,
    val contentHeight: Dp = 64.dp,
    val horizontalPadding: Dp = 24.dp,
    val itemWidth: Dp = 60.dp,
    val iconSize: Dp = 24.dp,
    val labelFontSize: TextUnit = 12.sp,
    val labelLineHeight: TextUnit = 14.sp,
    val backgroundAlpha: Float = 0.94f,
    val unselectedContentAlpha: Float = 0.72f,
    val horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    val menuLayout: HyperBottomMenuLayout = HyperBottomMenuLayout.Equal,
    val itemSlotAlignment: Alignment = Alignment.Center,
    val bottomBarModifier: Modifier = Modifier,
    val contentModifier: Modifier = Modifier,
    val itemModifier: Modifier = Modifier
)
```

## 公开签名

```kotlin
@Composable
fun HyperBottomBar(
    items: List<HyperBottomBarItem>,
    selectedItemId: String?,
    onItemClick: (HyperBottomBarItem) -> Unit,
    modifier: Modifier = Modifier,
    config: HyperBottomBarConfig = HyperBottomBarConfig()
)
```

## 参数

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `items` | `List<HyperBottomBarItem>` | 必填 | 按顺序显示的导航项 |
| `selectedItemId` | `String?` | 必填 | 与 `item.id` 匹配的当前选中项；可传 `null` 表示无选中项 |
| `onItemClick` | `(HyperBottomBarItem) -> Unit` | 必填 | 任意项目点击回调，包括再次点击已选项目 |
| `modifier` | `Modifier` | `Modifier` | 底栏根容器修饰符 |
| `config` | `HyperBottomBarConfig` | 默认配置 | 尺寸、布局、颜色透明度与修饰符配置 |

## 最小用法

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
fun MainBottomBar(onNavigate: (String) -> Unit) {
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
            onNavigate(item.id)
        }
    )
}
```

## 布局模式

- `Equal`：每项占一个等宽槽位，槽内位置由 `itemSlotAlignment` 控制。
- `Arrangement`：每项按 `itemWidth` 布局，由 `horizontalArrangement` 排列。

## 约束

- `selectedItemId` 不会在组件内部自动更新。
- `onItemClick` 不会自动导航；导航由调用方执行。
- `backgroundAlpha` 和 `unselectedContentAlpha` 建议保持在 `0f..1f`。

## 交互预览

<WasmPreview demo="bottom-bar" title="HyperBottomBar 交互预览" />
