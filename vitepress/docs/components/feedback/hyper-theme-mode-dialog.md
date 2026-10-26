---
description: 跟随系统、浅色、深色三选项的紧凑主题弹窗。
---

# HyperThemeModeDialog

统一三选项主题设置。选项最小行高 36dp、垂直内边距 4dp，无额外行间距和关闭按钮；圆环为 21dp。大字体可自然撑高行，弹窗正文可滚动。

<WasmPreview demo="theme-mode-dialog" title="主题设置" />

```kotlin
HyperThemeModeDialog(
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
fun HyperThemeModeDialog(
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
