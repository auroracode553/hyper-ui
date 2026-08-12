# hyperToast

- 分类：反馈工具
- 包名：`hyper_ui`
- 平台：Android-only
- 状态模型：调用即显示，不持有业务状态
- 源码：`library/src/main/java/hyper_ui/components/feedback/HyperToast.kt`
- Preview ID：`toast`（跨平台交互模拟）

`hyperToast` 封装 Android 原生 `Toast`。调用方显式传入 `Context`，组件把短/长时长映射到平台常量，并保证最终在 Android 主线程显示。

## 公开 API

```kotlin
enum class HyperToastDuration {
    Short,
    Long
}

fun hyperToast(
    context: Context,
    message: CharSequence,
    duration: HyperToastDuration = HyperToastDuration.Short
)

fun hyperToast(
    context: Context,
    messageResource: Int,
    duration: HyperToastDuration = HyperToastDuration.Short
)
```

## 参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `context` | `Context` | 是 | 无 | Android 上下文；内部使用 `applicationContext` 创建 Toast。 |
| `message` | `CharSequence` | 文本重载必填 | 无 | 直接显示的文本。 |
| `messageResource` | `Int` | 资源重载必填 | 无 | 字符串资源 ID，内部通过 `context.getText(...)` 读取。 |
| `duration` | `HyperToastDuration` | 否 | `Short` | 映射到 `Toast.LENGTH_SHORT` 或 `Toast.LENGTH_LONG`。 |

## 最小用法

```kotlin
val context = LocalContext.current

HyperButton(onClick = { hyperToast(context, "保存成功") }) {
    Text("保存")
}
```

字符串资源与长提示：

```kotlin
hyperToast(
    context = context,
    messageResource = R.string.saved,
    duration = HyperToastDuration.Long
)
```

## 约束

- 这是普通 Android 函数，不是 `@Composable`；可在 Compose 回调、Receiver、ViewModel 协作层或后台线程调用。
- 工具只负责原生 Toast 的显示与主线程调度，不保存队列、不去重，也不承载业务状态。
- `messageResource` 必须是有效字符串资源 ID；数字消息应先转换为文本再调用 `message` 重载。
- Desktop/Wasm Preview 不引入 Android 类，因而展示等价的交互模拟和真实 Android 调用片段。

<WasmPreview demo="toast" title="hyperToast 交互模拟" />
