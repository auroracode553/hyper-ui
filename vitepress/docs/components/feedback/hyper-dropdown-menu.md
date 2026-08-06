# HyperDropdownMenu

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/menu/HyperDropdownMenu.kt`
- 状态归属：调用方提供 `expanded`
- Preview ID：`dropdown`

基于 `Popup` 的浮层菜单。菜单内容通过专用 scope 声明，每个启用菜单项点击后自动请求关闭。

## 公开签名

```kotlin
@Composable
fun HyperDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopEnd,
    offset: DpOffset = DpOffset(0.dp, HyperDropdownMenuDefaults.AnchorOffsetY),
    width: Dp = HyperDropdownMenuDefaults.MenuWidth,
    maxHeight: Dp = HyperDropdownMenuDefaults.MaxHeight,
    content: @Composable HyperDropdownMenuScope.() -> Unit
)
```

## Scope API

```kotlin
@Composable
fun Item(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    textColor: Color = Color.Unspecified
)

@Composable
fun Divider(modifier: Modifier = Modifier)
```

`Item` 和 `Divider` 只能在 `HyperDropdownMenu` 的 `content` scope 中直接调用。

## 默认值 API

```kotlin
object HyperDropdownMenuDefaults {
    val MenuWidth = 184.dp
    val MaxHeight = 420.dp
    val ItemHeight = 48.dp
    val AnchorOffsetY = 52.dp
}
```

## 参数

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `expanded` | 必填 | 是否显示菜单 |
| `onDismissRequest` | 必填 | Popup 关闭请求和菜单项自动关闭都会调用 |
| `alignment` | `Alignment.TopEnd` | Popup 对齐方式 |
| `offset` | `(0.dp, 52.dp)` | 相对对齐位置偏移 |
| `width` | `184.dp` | 菜单宽度 |
| `maxHeight` | `420.dp` | 最大高度，超出后内部滚动 |

## 最小用法

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import hyper_ui.*

@Composable
fun MoreActions() {
    var expanded by remember { mutableStateOf(false) }

    HyperButton(text = "更多", onClick = { expanded = true })
    HyperDropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        Item(text = "编辑", onClick = { /* 编辑 */ })
        Divider()
        Item(text = "删除", onClick = { /* 删除 */ })
    }
}
```

## 行为与约束

- `expanded = false` 时不创建 Popup。
- 启用项点击顺序为：先执行该项 `onClick`，再执行菜单的 `onDismissRequest`。
- `enabled = false` 时不响应点击，也不会自动关闭。
- 菜单只渲染自身面板，不渲染遮罩。

## 交互预览

<WasmPreview demo="dropdown" title="HyperDropdownMenu 交互预览" />
