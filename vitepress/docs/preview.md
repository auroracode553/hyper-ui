# 交互预览

Preview 是文档维护工具，用来操作真实 Compose 组件。它不是 Android 应用依赖，也不改变 HyperUI 的手机端支持范围。

## 手机视口

预览示例按手机竖屏内容组织，重点检查 320dp 至 430dp 宽度下的：

- `small`、`default`、`large` 尺寸变化
- 普通、选中、聚焦、错误、加载和禁用状态
- 键盘或长文本出现时的布局稳定性
- 顶部和底部安全区

## 文档嵌入

组件页通过 `WasmPreview` 嵌入对应 Preview ID：

```html
<WasmPreview demo="button" title="HyperButton 交互预览" />
```

Markdown 中的公开签名、默认值和约束始终优先于预览画面。AI 或调用方不能通过画面推断 API。

## 预览目标

- Desktop/Wasm 只用于维护者查看和浏览器交互。
- 预览中的设备框、文档工具栏或示例容器不属于组件公开 API。
- Android-only 工具在 Preview 中使用跨平台模拟；真实 Android 调用方式写在组件页。

组件修改后，需要同步对应的 `*ComponentDemos.kt`、`*ComponentShowcases.kt` 和组件 Markdown。预览构建、复制和发布由使用者手动执行，项目不会自动部署。
