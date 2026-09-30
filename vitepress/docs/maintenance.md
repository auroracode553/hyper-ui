# 文档维护规则

文档维护目标是让 Android 手机端 API、Preview 示例和 Markdown 始终一致。项目不自动安装依赖，不自动构建、部署或发布代码。

## 目录职责

```text
library/src/main/java/hyper_ui/  公开组件实现
preview/src/commonMain/         交互示例与文档 Demo
vitepress/docs/                 权威 Markdown 文档
vitepress/.vitepress/           导航、主题和文档站配置
```

## 修改组件时同步

新增、删除、重命名组件，或修改参数、默认值、类型和状态规则时，按以下顺序检查：

1. 修改 `library/src/main/java/hyper_ui/` 源码。
2. 更新 `vitepress/docs/components/` 对应组件页。
3. 更新 `vitepress/docs/component-index.md`。
4. 更新对应分组的 `*ComponentDemos.kt` 和 `*ComponentShowcases.kt`。
5. 必要时更新 `vitepress/.vitepress/config.mts`、根目录 `README.md` 和 `AGENTS.md`。

每个组件页必须包含：

- 包名和支持平台。
- 与源码一致的完整公开签名。
- 适用的 `type`、`size`、状态参数和默认值。
- 最小可用示例。
- 状态归属、使用约束和常见错误。
- 对应的 `WasmPreview` ID（如果提供交互预览）。

## 文档写作规则

- 组件页只保留一句用途或关键约束，再给签名和最小用法。
- 使用 `type` 描述渲染形态，使用 `size` 描述 `small/default/large` 尺寸。
- 不把 Preview 的设备框、工具栏和加载状态写成组件 API。
- 不在组件页维护与源码无关的第二份参数表。
- 示例只展示最小调用；业务网络、数据库、权限、导航和 ViewModel 放在调用方。
- 颜色示例使用 `rgba(...)` 或主题令牌，不写十六进制 `Color(0xFF...)`。

## 预览边界

Wasm/Desktop 预览只用于浏览器或维护者操作真实组件。它们不扩大 Android 手机端支持范围，也不能替代 Markdown 作为 API 事实来源。Android-only 工具在 Preview 中使用状态模拟，并在组件页提供 Android 调用说明。

## 文档检查

只改 Markdown 时，检查链接、代码块、签名和目录导航即可。修改组件或 Preview 时，至少执行：

```powershell
git diff --check
rg -n "旧参数|旧类型" library preview vitepress/docs
```

按仓库约束，不在本项目中执行打包、部署或发布命令。
