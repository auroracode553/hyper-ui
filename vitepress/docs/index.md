---
layout: home
title: HyperUI
description: 面向 Android 手机端的 Jetpack Compose UI 组件库
sidebar: false
aside: false
hero:
  name: HyperUI
  text: Android 手机端 Compose UI
  tagline: 统一材质、尺寸与受控交互的 Android 手机端组件
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
    onPressed = onSave,
    colorStyle = "primary", type = "filled",
    size = "default"
) { HyperText("保存") }
```

默认 `HyperThemeConfig(material = HyperMaterial.Solid)`。同名组件的尺寸、材质和 API 约定见 [样式对照](style-reference.md)。Dropdown 的值由调用方管理，临时展开状态由组件维护。

## API 约定

| 参数 | 用途 |
| --- | --- |
| `type` | 组件形态或视觉层级 |
| `size` | `small`、`default`、`large` |
| `modifier` | 页面中的尺寸、间距和位置 |
| 状态参数 | `value`、`groupValue`、`selectedIndex`、`visible`、`value` 由调用方持有 |

颜色通过 `HyperThemeConfig` 或组件 `colors` 配置；调用方使用 `import hyper_ui.*`。

## 文档入口

- [UI 样式与功能实现基线](style-reference.md)：维护组件前必读；已有样式保留，涉及用途不明的变化先确认
- [接入与最小配置](getting-started.md)
- [移动端规范](mobile-guidelines.md)
- [主题配置](theme.md)
- [状态与架构边界](state-model.md)
- [交互预览](preview.md)
- [日期选择器](components/form/hyper-date-picker.md)：单日期月历与完整 LocalDate 参数表

两端对应组件统一使用 onPressed、onChanged、onClose。按钮 type 控制形态，colorStyle 选择预设配色，color 覆盖预设；页签使用 selectedIndex。当前参数与默认值见完整组件 API 表格。

维护 UI 库和开发业务页面前，优先阅读[使用规则、全局全面屏与 AGENTS.md 模板](mobile-guidelines.md)和[设计系统基线](style-reference.md)。正文卡片统一无普通描边和双层柔阴影，选中轮廓保留；导航材质仅在状态栏顶部至 Navbar 底部内渐隐。既有悬浮 TabBar 的高度、占位与滚动范围不能因配色或卡片优化改变。
