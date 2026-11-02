---
description: 贴边、全高的模态抽屉，支持双侧展开和滑动关闭。
---

# HyperDrawer

包名 hyper_ui。Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于预览。

<WasmPreview demo="drawer" title="贴边抽屉" />

## 使用方式

```kotlin
Box {
    pageContent()
    HyperDrawer(open = open, onClose = { open = false }, title = "导航") {
        HyperListTile(title = { HyperText("首页") }, onPressed = onHome)
    }
}
```

## 布局与交互

抽屉是独立模态层，默认从 start 侧展开，面板贴边、铺满窗口高度，使用固定模糊玻璃背景。默认宽 320dp，窄屏最多占窗口宽度减 56dp，保留遮罩点击区；不在面板外侧添加留白、圆角、边框或卡片阴影。

系统安全区和键盘避让位于背景内部；正文默认 contentPadding 为零。标题与页脚固定，正文单独滚动；内容自行提供 LazyColumn 等滚动容器时设置 scrollable=false。

面板以 280ms 从自身宽度之外平移进入，遮罩单独淡入。向外滑动超过面板宽度的 30%，或释放速度达到 400dp/s 时请求关闭；未达到阈值以 180ms 回到展开位置。start/end 随 RTL 自动镜像。

open 由调用方持有。按钮、遮罩、系统返回键和滑动仅通过 onClose 请求关闭；退出动画结束前保留模态宿主并阻止正文继续操作。

**固定玻璃与动效**

背景使用 Drawer 固定玻璃，浅/深色模糊 16/8dp、白色底面 alpha 0.85/0.28；不增加透镜边缘、轮廓或阴影。宽度、全高、系统安全区、正文滚动、滑动阈值和退出流程保持原规则。

## 公开 API

```kotlin
@Composable
fun HyperDrawer(
    open: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    placement: HyperDrawerPlacement = HyperDrawerPlacement.start,
    width: Dp = 320.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scrollable: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    gesturesEnabled: Boolean = true,
    title: String? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| open | Boolean | 是 | — | 外部持有的显隐状态。 |
| onClose | () -&gt; Unit | 是 | — | 请求关闭；调用方更新 open。 |
| modifier | Modifier | 否 | Modifier | 模态根布局。 |
| placement | HyperDrawerPlacement | 否 | HyperDrawerPlacement.start | 逻辑起始侧／结束侧，RTL 自动镜像。 |
| width | Dp | 否 | 320.dp | 面板目标宽度，按窗口宽度减 56dp 收敛。 |
| contentPadding | PaddingValues | 否 | PaddingValues(0.dp) | 只影响正文，默认无额外间距。 |
| scrollable | Boolean | 否 | true | 正文内部滚动；LazyColumn 等设为 false。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击遮罩请求关闭。 |
| dismissOnBackPress | Boolean | 否 | true | 系统返回键请求关闭。 |
| gesturesEnabled | Boolean | 否 | true | 横向滑动关闭，未达阈值则回到展开位置。 |
| title | String? | 否 | null | 可选标题，提供时显示标题及关闭按钮。 |
| footer | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 固定页脚，内边距 16dp。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 抽屉正文。 |

### HyperDrawerPlacement

| 值 | 作用 |
| --- | --- |
| start | 逻辑起始侧，LTR 左侧、RTL 右侧。 |
| end | 逻辑结束侧，LTR 右侧、RTL 左侧。 |
