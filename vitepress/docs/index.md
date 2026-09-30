---
layout: home
title: HyperUI
description: 面向 Android 手机端的 Jetpack Compose UI 组件库
sidebar: false
aside: false
hero:
  name: HyperUI
  text: Android 手机端 Compose UI
  tagline: HyperOS 风格玻璃材质与受控交互组件
  actions:
    - theme: brand
      text: 开始接入
      link: /getting-started
    - theme: alt
      text: 组件索引
      link: /component-index
features:
  - title: 统一 API
    details: 使用 type 表示形态，size 表示尺寸。
  - title: 手机端优先
    details: 面向 320dp 至 430dp Android 竖屏布局。
  - title: 可控状态
    details: 业务状态由调用方持有，组件只负责界面与回调。
---

<WasmPreview demo="button" title="HyperButton 交互预览" />

## 快速开始

```kotlin
import hyper_ui.*

HyperButton(
    onClick = onSave,
    type = "filled",
    size = "default"
) { HyperText("保存") }
```

## API 约定

| 参数 | 用途 |
| --- | --- |
| `type` | 组件形态或视觉层级 |
| `size` | `small`、`default`、`large` |
| `modifier` | 页面中的尺寸、间距和位置 |
| 状态参数 | `value`、`checked`、`selected`、`visible`、`progress` 由调用方持有 |

颜色通过 `HyperThemeConfig` 或组件 `colors` 配置；调用方使用 `import hyper_ui.*`。

## 文档入口

- [接入与最小配置](getting-started.md)
- [移动端规范](mobile-guidelines.md)
- [主题与颜色](theme.md)
- [状态与架构边界](state-model.md)
- [交互预览](preview.md)
