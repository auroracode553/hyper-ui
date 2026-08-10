# HyperBottomBar

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperBottomBar.kt`
- 预览：`bottom-bar`

`HyperBottomBar` 是底部栏容器，默认带 1dp 轻描边和标签文字样式。浅色模式完整保留原有半透明容器、玻璃高光和内容透明度；深色模式改用不透明实色容器，不绘制玻璃高光，并将选中、未选中、禁用内容色与默认描边合成为实色。组件负责底栏面板与横向布局；调用方可以直接传入完整内容 slot，也可以使用泛型 items 入口。

## 公开签名

```kotlin
enum class HyperBottomBarItemLayout { Equal, Packed }

@Composable
fun HyperBottomBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    shape: Shape = HyperBottomBarDefaults.Shape,
    border: BorderStroke? = HyperBottomBarDefaults.border(),
    colors: HyperBottomBarColors = HyperBottomBarDefaults.colors(),
    content: @Composable RowScope.() -> Unit
)

@Composable
fun <T> HyperBottomBar(
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemLayout: HyperBottomBarItemLayout = HyperBottomBarItemLayout.Equal,
    itemSelected: (T) -> Boolean = { false },
    itemSlotAlignment: Alignment = Alignment.Center,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    shape: Shape = HyperBottomBarDefaults.Shape,
    border: BorderStroke? = HyperBottomBarDefaults.border(),
    colors: HyperBottomBarColors = HyperBottomBarDefaults.colors(),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperBottomBarItemScope.(item: T) -> Unit
)
```

## 关键公开类型

```kotlin
object HyperBottomBarDefaults {
    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 最小用法

完整内容 slot：

```kotlin
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR

HyperBottomBar {
    HyperIconButton(onClick = onBack) {
        Icon(
            painter = painterResource(LucideR.drawable.lucide_ic_arrow_left),
            contentDescription = "返回"
        )
    }
    Spacer(modifier = Modifier.weight(1f))
    HyperIconButton(onClick = onMore) {
        Icon(
            painter = painterResource(LucideR.drawable.lucide_ic_ellipsis_vertical),
            contentDescription = "更多"
        )
    }
}
```

泛型 items：

```kotlin
HyperBottomBar(
    items = bottomItems,
    itemSelected = { it.id == selectedItemId },
    onItemClick = { selectedItemId = it.id }
) { item ->
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(item.icon, contentDescription = item.label)
        Text(item.label)
    }
}
```

## 约束

- 不存在 `HyperBottomBarItem`、`selectedItemId`、`HyperBottomBarConfig`。
- 底栏默认高度为 `HyperBottomBarDefaults.Height`；自定义尺寸使用 `modifier.height(...)`。内容自动填满底栏高度，不再提供重复的 `height`、`contentHeight`、`itemWidth` 参数。
- `Equal` 模式中的项目等分可用宽度；`Packed` 模式使用 `HyperBottomBarDefaults.ItemWidth` 作为最小项目宽度，slot 内容可以自然撑宽。
- 泛型 items 入口的页面切换和导航由调用方在 `onItemClick` 中完成。
- 完整内容 slot 只提供底栏外壳和默认内容色；点击、选中、禁用与内部布局由调用方自行组合。
- 单项可用状态由 `itemEnabled` 决定，全局禁用仍使用 `enabled`。
- `HyperBottomBarItemScope` 暴露 `selected` 与 `enabled`，slot 可据此渲染字体、徽标或动画。
- 浅色模式继续使用 `HyperColors.elevatedContainer` 和玻璃高光，既有透明效果不变。
- 深色模式默认使用不透明的 `HyperColors.cardContainer`；即使通过 `HyperBottomBarColors` 或 `colors(...)` 传入含 alpha 的颜色，也会先与底栏背景合成为实色。
- 默认描边来自 `HyperBottomBarDefaults.border()`：浅色模式保持原有 `HyperColors.panelBorder`，深色模式使用合成后的实色轻描边；如需无边框，传入 `border = null`。

<WasmPreview demo="bottom-bar" title="HyperBottomBar 交互预览" />
