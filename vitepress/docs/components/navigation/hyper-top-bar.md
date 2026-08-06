# HyperTopBar

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperTopBar.kt`
- 状态归属：调用方处理返回与右侧操作
- Preview ID：`topbar`

用于页面顶部标题区域，可选显示返回按钮和右侧自定义内容。组件不执行导航。

## 公开签名

```kotlin
@Composable
fun HyperTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    rightSlot: (@Composable () -> Unit)? = null
)
```

## 参数

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `title` | `String` | 必填 | 标题文本 |
| `modifier` | `Modifier` | `Modifier` | 根 `Row` 的修饰符 |
| `onBack` | `(() -> Unit)?` | `null` | 非空时显示内置返回图标按钮，点击后调用该回调 |
| `rightSlot` | `(@Composable () -> Unit)?` | `null` | 标题右侧内容，可放按钮或状态信息 |

## 最小用法

```kotlin
import androidx.compose.runtime.Composable
import hyper_ui.*

@Composable
fun DetailsHeader(onBack: () -> Unit) {
    HyperTopBar(
        title = "详情",
        onBack = onBack
    )
}
```

## 带右侧操作

```kotlin
HyperTopBar(
    title = "通知设置",
    rightSlot = {
        HyperButton(
            text = "保存",
            onClick = onSave
        )
    }
)
```

## 约束

- `onBack` 只报告点击，不会调用导航框架。
- 真实参数名是 `rightSlot`，不存在 `actions` 或 `navigationIcon` 参数。
- 返回图标的可访问性说明固定为“返回”。

## 交互预览

<WasmPreview demo="topbar" title="HyperTopBar 交互预览" />
