---
description: 通过左右拖动列表行展开快捷操作，支持速度投影与回弹。
---

# HyperSlideMenu

通过左右拖动列表行展开快捷操作，支持速度投影与回弹。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="slide_menu" title="HyperSlideMenu 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperSlideMenu(reveal, { reveal = it }, endActions = listOf(HyperSlideAction("删除", onDelete))) {
    HyperListTile(title = { HyperText("项目") })
}
```

</details>

## 使用约定

**样式约定**

列表侧滑动作每项 64dp；到达边界使用 0.32 橡皮筋阻力，释放阈值 35%，速度投影 0.09 秒；弹簧 stiffness 470/damping 42，携带释放速度开始吸附。

**状态归属**

展开状态由调用方持有；业务删除或确认通过回调处理。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperSlideMenu

<details class="component-doc__details">
<summary>查看公开签名</summary>

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
    child: @Composable () -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| reveal | HyperSlideMenuReveal | 是 | — | 侧滑展开状态，由调用方持有。 |
| onRevealChange | (HyperSlideMenuReveal) -&gt; Unit | 是 | — | 侧滑展开状态变化时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| startActions | List&lt;HyperSlideAction&gt; | 否 | emptyList() | 逻辑起始侧操作，RTL 时自动镜像。 |
| endActions | List&lt;HyperSlideAction&gt; | 否 | emptyList() | 逻辑结束侧操作，RTL 时自动镜像。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| actionExtent | Dp | 否 | 64.dp | 侧滑操作区的展开宽度。 |
| shape | Shape | 否 | RoundedCornerShape(18.dp) | 组件容器的形状。 |
| child | @Composable () -&gt; Unit | 是 | — | 组件主体或触发器内容。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperSlideAction 配置**

```kotlin
data class HyperSlideAction(
    val label: String,
    val onPressed: () -> Unit,
    val color: Color = Color.Unspecified,
    val foregroundColor: Color = Color.Unspecified,
    val icon: (@Composable () -> Unit)? = null,
    val type: String = "default"
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| label | String | 是 | — | 数值旁显示的标签文字。 |
| onPressed | () -&gt; Unit | 是 | — | 先请求关闭侧滑区域，再执行操作回调。 |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |
| foregroundColor | Color | 否 | Color.Unspecified | 动作文字和图标颜色。 |
| icon | (@Composable () -&gt; Unit)? | 否 | null | 图标或图标区域。 |
| type | String | 否 | &quot;default&quot; | 组件的视觉或布局形态。 |

</details>
