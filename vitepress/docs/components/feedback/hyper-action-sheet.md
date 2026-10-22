---
description: 受控底部弹层，支持自定义内容与操作列表。
---

# HyperActionSheet / HyperAction

包名 hyper_ui。Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于文档预览。

<WasmPreview demo="action_sheet" title="底部弹层" />

## 布局与状态

与 Flutter 保持相同的底部布局：全宽、圆角 30dp、内边距 16dp、strong 材质，最大高度为键盘避让后可用高度的 85%。32×4dp 顶部指示条下留 16dp，标题为 18sp/700，下留 16dp。内容整体滚动，底部安全区放在背景内部；250ms 淡入与 24dp 纵向平移。

visible 由调用方持有，onClose 请求关闭。dismissible 只控制点击遮罩，系统返回仍可关闭。退出期间保留模态宿主并阻止正文操作。底部弹层不提供左右抽屉布局或滑动关闭。

## 自定义正文入口

```kotlin
@Composable
fun HyperActionSheet(
    visible: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissible: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 外部持有的显隐状态。 |
| onClose | () -&gt; Unit | 是 | — | 请求关闭，调用方更新 visible。 |
| modifier | Modifier | 否 | Modifier | 面板布局修饰符。 |
| title | String? | 否 | null | 可选标题。 |
| dismissible | Boolean | 否 | true | 允许点击遮罩关闭。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 自定义正文，宿主负责整体滚动。 |

```kotlin
HyperActionSheet(visible, { visible = false }, title = "分享项目") {
    HyperListTile(title = { HyperText("复制链接") }, onPressed = {
        visible = false
        onCopy()
    })
}
```

## 操作列表入口

```kotlin
@Composable
fun <T> HyperActionSheet(
    visible: Boolean,
    onClose: () -> Unit,
    actions: List<HyperAction<T>>,
    onChanged: (T) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissible: Boolean = true,
    cancelLabel: String = "取消"
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 外部持有的显隐状态。 |
| onClose | () -&gt; Unit | 是 | — | 关闭请求。 |
| actions | List&lt;HyperAction&lt;T&gt;&gt; | 是 | — | 顺序展示的操作项。 |
| onChanged | (T) -&gt; Unit | 是 | — | 点击有效操作时先 onClose，再回传 value。 |
| modifier | Modifier | 否 | Modifier | 面板布局修饰符。 |
| title | String? | 否 | null | 可选标题。 |
| dismissible | Boolean | 否 | true | 允许点击遮罩关闭。 |
| cancelLabel | String | 否 | 取消 | 底部默认按钮文字；与列表间距 12dp，点击只关闭。 |

```kotlin
HyperActionSheet(
    visible = visible,
    onClose = { visible = false },
    actions = listOf(HyperAction("delete", "删除", type = "danger")),
    onChanged = { action -> handleAction(action) }
)
```

## HyperAction

```kotlin
data class HyperAction<T>(
    val value: T,
    val label: String,
    val icon: ImageVector? = null,
    val type: String = "default",
    val enabled: Boolean = true,
    val selected: Boolean = false
) {
    val isDanger: Boolean
}
```

| 参数／属性 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | T | 是 | — | 操作返回值。 |
| label | String | 是 | — | 操作文字。 |
| icon | ImageVector? | 否 | null | 可选图标。 |
| type | String | 否 | default | default / danger，危险项使用主题危险色。 |
| enabled | Boolean | 否 | true | 禁用项不触发关闭或结果回调。 |
| selected | Boolean | 否 | false | 选中底板和尾部勾选图标。 |
| isDanger | Boolean | 只读 | type == danger | 派生的危险状态，不是构造参数。 |

Android 使用受控 Composable 宿主和 ImageVector；Flutter 的 show / choose 使用根导航器返回 Future 结果。业务参数与默认视觉一致，保留宿主和原生类型差异。
