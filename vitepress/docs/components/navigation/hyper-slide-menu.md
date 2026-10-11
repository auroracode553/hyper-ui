# HyperSlideMenu

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="slide_menu" title="HyperSlideMenu 交互预览" />

## 样式约定

列表侧滑动作每项 64dp；到达边界使用 0.32 橡皮筋阻力，释放阈值 35%，速度投影 0.09 秒；弹簧 stiffness 470/damping 42。

## 状态归属与使用约束

展开状态由调用方持有；业务删除或确认通过回调处理。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperSlideMenu(reveal, { reveal = it }, endActions = listOf(HyperSlideAction("删除", onDelete))) {
    HyperListTile(headlineContent = { HyperText("项目") })
}
```

## HyperSlideMenu API / Props

```kotlin
@Composable
fun HyperSlideMenu(
    reveal: HyperSlideMenuReveal,
    onRevealChange: (HyperSlideMenuReveal) -> Unit,
    modifier: Modifier = Modifier,
    startActions: List<HyperSlideAction> = emptyList(),
    endActions: List<HyperSlideAction> = emptyList(),
    enabled: Boolean = true,
    actionExtent: Dp = 64.dp,
    shape: Shape = RoundedCornerShape(18.dp),
    content: @Composable () -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| reveal | HyperSlideMenuReveal | 是 | — | 侧滑展开状态，由调用方持有。 |
| onRevealChange | (HyperSlideMenuReveal) -&gt; Unit | 是 | — | 侧滑展开状态变化时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| startActions | List&lt;HyperSlideAction&gt; | 否 | emptyList() | 向右侧滑时露出的操作项。 |
| endActions | List&lt;HyperSlideAction&gt; | 否 | emptyList() | 向左侧滑时露出的操作项。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| actionExtent | Dp | 否 | 64.dp | 侧滑操作区的展开宽度。 |
| shape | Shape | 否 | RoundedCornerShape(18.dp) | 表面形状。 |
| content | @Composable () -&gt; Unit | 是 | — | 主体内容插槽。 |

## HyperSlideAction 配置

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| label | String | 是 | — | 可覆盖的 label 配置；默认表达式见此表。 |
| onClick | () -&gt; Unit | 是 | — | 用户点击回调。 |
| containerColor | Color | 否 | Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| contentColor | Color | 否 | Color(1f, 1f, 1f, 1f) | 主要文字与图标颜色。 |
| iconContent | (@Composable () -&gt; Unit)? | 否 | null | 可覆盖的 iconContent 配置；默认表达式见此表。 |
