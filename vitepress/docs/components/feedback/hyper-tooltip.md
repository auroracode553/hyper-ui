# HyperTooltip

- 包名：`hyper_ui`
- 状态模型：显示与隐藏由组件根据锚点悬停事件内部管理，调用方不持有状态
- 源码：`library/src/main/java/hyper_ui/components/feedback/HyperTooltip.kt`
- 预览：`tooltip`

`HyperTooltip` 为任意锚点提供 HyperOS 风格轻量提示浮层。指针进入锚点时在锚点上方显示提示，退出或按下释放时隐藏；点击与子组件的点击事件不会被占用。提示使用窗口级 Popup 渲染，不创建模态 Dialog，也不可聚焦。

<WasmPreview demo="tooltip" title="HyperTooltip 交互预览" />

## 公开签名

```kotlin
@Composable
fun HyperTooltip(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
)
```

## Slot 入口参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `text` | `String` | 是 | 无 | 提示文案；为空或全空白字符串时不渲染浮层。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 锚点的宽高与外部间距，作用在锚点容器上。 |
| `enabled` | `Boolean` | 否 | `true` | 关闭后不注册悬停监听，提示永不显示。 |
| `content` | `@Composable () -> Unit` | 是 | 无 | 锚点内容 slot。 |

## 最小用法

```kotlin
HyperTooltip(text = "提示文本") {
    HyperText("悬停查看")
}
```

## 行为与约束

- 提示由组件内部状态驱动：`PointerEventType.Enter` 显示，`Exit` 与 `Release` 隐藏；组件只观察事件，不消费点击，子组件点击仍正常触发。
- 提示默认显示在锚点上方，与锚点间隔 `8.dp`；上方空间不足时自动放到锚点下方。
- 水平方向以锚点中心对齐，并约束在窗口范围内，不会越界。
- `text` 为空或全空白字符串时不渲染浮层；`enabled = false` 时悬停监听不生效。
- 提示使用 `PopupProperties(focusable = false)` 的窗口级 Popup，不拦截焦点、不渲染遮罩，显示与隐藏均不执行动画。
- 提示样式固定：`HyperColors.primaryText` 背景、`8.dp` 圆角、`10.dp × 6.dp` 内边距，文字使用 `HyperColors.pageBackground` 与 `HyperTheme.typography.labelMedium`。
- 不存在 `colors`、`defaults` 或其他样式/定位参数；定位由内部 `PopupPositionProvider` 负责。
