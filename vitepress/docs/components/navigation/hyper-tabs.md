# HyperTabs

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="tabs" title="HyperTabs 交互预览" />

## 样式约定

共享 subtle 胶囊 radius 27dp，高 54dp。项目 48dp，指示底面 44dp。普通项继承 14sp/500，选中 600；scrollable 模式每项最小 76dp。

滚动模式底色切换使用 160ms easeOutCubic。文字继承当前插槽排版，选中 600、普通 500。

## 状态归属与使用约束

同一共享表面承载全部条目。选中值由调用方持有。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperTabs(items, selected, { selected = it }) { HyperText(it) }
```

## HyperTabs API / Props

```kotlin
@Composable
fun <T> HyperTabs(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabsItemScope.(item: T) -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 待呈现条目。 |
| selectedItem | T | 是 | — | 调用方持有的当前选项。 |
| onSelected | (T) -&gt; Unit | 是 | — | 选中条目时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| scrollable | Boolean | 否 | false | 允许横向滚动；false 使用等宽条目。 |
| itemEnabled | (T) -&gt; Boolean | 否 | { true } | 按项提供可用状态。 |
| itemContent | @Composable HyperTabsItemScope.(item: T) -&gt; Unit | 是 | — | 条目内容插槽。 |
