---
description: 与菜单分组共用排版的独立标题。
---

# HyperSectionTitle

独立分组标题与 HyperMenuGroup 的 title 使用同一实现：14sp、Bold。组件不附加页面边距或卡片；页面只负责位置与间距。

<WasmPreview demo="section-title" title="分组标题" />

```kotlin
HyperSectionTitle(text = "通用设置")
```

## 公开 API

```kotlin
@Composable
fun HyperSectionTitle(text: String, modifier: Modifier = Modifier)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| text | String | 是 | — | 分组标题。 |
| modifier | Modifier | 否 | Modifier | 调用方提供的外部布局。 |
