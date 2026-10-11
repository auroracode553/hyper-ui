# HyperEmptyState

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="empty_state" title="HyperEmptyState 交互预览" />

## 样式约定

内容 padding 20dp，图标徽章56dp、radius 24dp、24dp 图标；标题 16sp/600、说明 13sp/1.45。图标到标题 12dp、说明 8dp、操作 12dp。

## 状态归属与使用约束

页面数据与操作结果由调用方持有。说明或插槽为 null 时省略。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperEmptyState(title = "暂无内容", description = "数据会显示在这里", actionContent = {
    HyperButton(onRefresh) { HyperText("刷新") }
})
```

## HyperEmptyState API / Props

```kotlin
@Composable
fun HyperEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    contentModifier: Modifier = Modifier,
    colors: HyperEmptyStateColors = HyperEmptyStateDefaults.colors(),
    iconContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| title | String | 是 | — | 标题；null 时省略。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| description | String? | 否 | null | 空状态的说明文字。 |
| contentModifier | Modifier | 否 | Modifier | 内容区修饰符；显式传入时覆盖默认内容留白。 |
| colors | HyperEmptyStateColors | 否 | HyperEmptyStateDefaults.colors() | 各状态配色配置。 |
| iconContent | (@Composable () -&gt; Unit)? | 否 | null | 图标区域的自定义内容。 |
| actionContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 操作区插槽。 |

## HyperEmptyStateDefaults.colors API / Props

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

## HyperEmptyStateColors 配置

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| iconContentColor | Color | 是 | — | 空状态图标颜色。 |
| titleColor | Color | 是 | — | 标题颜色。 |
| descriptionColor | Color | 是 | — | 说明颜色。 |
