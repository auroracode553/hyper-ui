---
description: 页面水平、正文首尾与分组间距。
---

# HyperSpacing

<p class="component-doc__meta">包名 hyper_ui · Android 手机端</p>


`import hyper_ui.HyperSpacing`，由页面统一提供一次内容间距。

| 属性 | 类型 | 值 | 用途 |
| --- | --- | --- | --- |
| PageHorizontal | Dp | 14.dp | 页面水平留白 |
| PageVertical | Dp | 12.dp | 正文首尾留白 |
| SectionVertical | Dp | 16.dp | 分组间距 |

<WasmPreview demo="hyper-spacing" title="HyperSpacing 边距预览" />

切换三个开关，查看标题与卡片的水平留白、正文首尾留白及组间距。

## 使用方式

边距加在包住标题和卡片的父容器上。HyperMenuGroup.modifier 只作用于卡片。

```kotlin
Column(
    modifier = Modifier.padding(
        horizontal = HyperSpacing.PageHorizontal,
        vertical = HyperSpacing.PageVertical
    ),
    verticalArrangement = Arrangement.spacedBy(HyperSpacing.SectionVertical)
) {
    HyperMenuGroup(title = "网站显示") {
        HyperListTile(title = { HyperText("网页深色模式") })
    }
    HyperMenuGroup(title = "管理") {
        HyperListTile(title = { HyperText("网站权限") })
    }
}
```

导航净空另由页面宿主提供，放入滚动内容。
