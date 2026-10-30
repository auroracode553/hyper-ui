---
description: 随主滚动渐显的主题渐变，不采样背景，不增加栏高或安全区。
---

# HyperScrollGradient

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

<WasmPreview demo="scroll-gradient" title="HyperScrollGradient 交互预览" />

导航与自定义覆盖区共用的滚动渐变。默认透明，正文离顶时以 160ms easeOutCubic 过渡显示 pageBackground 向下渐隐的渐变。组件不录制正文、不调用 Haze、不做像素模糊；child 保持清晰，纯材质层透传正文手势。

## 公开 API

```kotlin
import hyper_ui.*

@Composable
fun HyperScrollGradient(
    scrolledUnder: Boolean = false,
    modifier: Modifier = Modifier,
    child: @Composable BoxScope.() -> Unit = {}
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| scrolledUnder | Boolean | 否 | false | true 强制显示；否则读取已有 HyperNavBarScrollScope，无来源时透明。外部宿主可传自身滚动状态。 |
| modifier | Modifier | 否 | Modifier | 尺寸和位置；渐变仅绘制于实际测量范围。 |
| child | @Composable BoxScope.() -&gt; Unit | 否 | 空内容 | 清晰的前景插槽，无内容时由父级或 modifier 指定范围。 |

## 使用方式

标准页面和仅状态栏页面优先使用 [HyperNavBar](hyper-nav-bar.md) 的 standard / simple；自定义覆盖区才直接组合此组件。

```kotlin
val scrollState = rememberLazyListState()
HyperNavBarPage(scrollState = scrollState, navBar = {
    HyperScrollGradient(modifier = Modifier.fillMaxWidth()) {
        HyperText("自定义操作区", modifier = Modifier.padding(16.dp))
    }
}) { padding ->
    LazyColumn(state = scrollState, contentPadding = padding) { /* 正文 */ }
}
```

沿用 HyperNavBarPage / HyperNavBarScrollScope 的真实主滚动状态，不增加导航专用监听。组件本身不添加栏高、系统 Insets、滚动容器或永久占位；局部操作只保护一次安全区，首屏净空只进入滚动内容。无需 HyperBackdrop，WebView 与原生正文均不进入渐变的采样层；组件不管理 Bridge、连接和页面生命周期。

渐变透明度从顶部 0.96 经 55% 位置 0.94、78% 位置 0.74 降到自身底部 0，整体乘动画进度。背景绘制不超出组件边界，不增加底部分隔线。

Desktop/Wasm 仅用于文档 Preview。
