---
message: 用于空列表、搜索无结果等场景，可组合恢复操作。
---

# HyperEmptyState

用于空列表、搜索无结果等场景，可组合恢复操作。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="empty_state" title="HyperEmptyState 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperEmptyState(title = "暂无内容", message = "数据会显示在这里", action = {
    HyperButton(onRefresh) { HyperText("刷新") }
})
```

</details>

## 使用约定

**样式约定**

内容 padding 20dp，图标徽章56dp、radius 24dp、24dp 图标；标题 16sp/600、说明 13sp/1.45。图标到标题 12dp、说明 8dp、操作 12dp。

**状态归属**

页面数据与操作结果由调用方持有。说明或插槽为 null 时省略。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperEmptyState

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    contentModifier: Modifier = Modifier,
    colors: HyperEmptyStateColors = HyperEmptyStateDefaults.colors(),
    icon: (@Composable () -> Unit)? = null,
    action: (@Composable RowScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| title | String | 是 | — | 组件标题文字或内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| message | String? | 否 | null | 提示消息文本。 |
| contentModifier | Modifier | 否 | Modifier | 内部内容区域的布局修饰符。 |
| colors | HyperEmptyStateColors | 否 | HyperEmptyStateDefaults.colors() | 组件各状态的颜色配置。 |
| icon | (@Composable () -&gt; Unit)? | 否 | null | 图标或图标区域。 |
| action | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 空状态操作内容。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperEmptyStateDefaults.colors**

```kotlin
@Composable
    fun colors(
        iconContentColor: Color = Color.Unspecified,
        titleColor: Color = Color.Unspecified,
        descriptionColor: Color = Color.Unspecified
    ): HyperEmptyStateColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| iconContentColor | Color | 否 | Color.Unspecified | 空状态图标颜色。 |
| titleColor | Color | 否 | Color.Unspecified | 标题颜色。 |
| descriptionColor | Color | 否 | Color.Unspecified | 说明颜色。 |

**HyperEmptyStateColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| iconContentColor | Color | 是 | — | 空状态图标颜色。 |
| titleColor | Color | 是 | — | 标题颜色。 |
| descriptionColor | Color | 是 | — | 说明颜色。 |

</details>
