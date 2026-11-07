---
description: 跟随系统、浅色、深色三选项的紧凑主题弹窗。
---

# HyperThemeDialog

通过共用 Dialog 宿主保护窗口安全区和键盘空间，保留原有三选项规格与关闭行为。大字号时内容仍受安全范围约束；文档预览不以外侧 Padding 代替独立模态窗口的 Insets。

统一三选项主题设置。选项最小行高 36dp、垂直内边距 4dp，无额外行间距和关闭按钮；圆环为 21dp。大字体可自然撑高行，弹窗正文可滚动。面板复用 [HyperDialog 的高遮蔽玻璃](hyper-dialog.md#使用约定)。

<WasmPreview demo="theme-mode-dialog" title="主题设置" />

```kotlin
HyperThemeDialog(
    visible = visible,
    value = darkTheme,
    onChanged = { darkTheme = it },
    onClose = { visible = false }
)
```

value 为 null 表示跟随系统、false 为浅色、true 为深色，与 HyperThemeConfig.darkTheme 的语义一致。组件只呈现选项并通知选择，不读写偏好、不自行修改全局主题。应用在回调中转换自己的枚举或存储值。

选中任意一项先调用 onChanged，再调用 onClose；点遮罩或系统返回仅调用 onClose。调用方持有 visible，即使选中当前模式也会请求关闭。

## 公开 API

```kotlin
@Composable
fun HyperThemeDialog(
    visible: Boolean,
    value: Boolean?,
    onChanged: (Boolean?) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "主题设置"
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 由调用方持有的显示状态。 |
| value | Boolean? | 是 | — | null 跟随系统，false 浅色，true 深色。 |
| onChanged | (Boolean?) -> Unit | 是 | — | 选择后通知调用方保存并应用主题。 |
| onClose | () -> Unit | 是 | — | 请求关闭，不自动持有显示状态。 |
| modifier | Modifier | 否 | Modifier | 弹窗面板外部布局。 |
| title | String | 否 | 主题设置 | 弹窗标题。 |
