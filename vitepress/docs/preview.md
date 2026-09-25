# Wasm 交互预览

该区域加载 Compose Multiplatform Wasm 组件示例，用于让浏览器访问者操作真实组件。嵌入 VitePress 时只显示所选组件的交互样式；公开签名、默认值、状态和约束写在对应的 Markdown 组件页。

Markdown 是开发者和 AI 的 API 文档来源。独立打开 Preview 应用仍可浏览组件目录；VitePress 内的 iframe 不重复显示目录、代码卡片或属性表。

<WasmPreview demo="button" title="HyperButton 交互预览" :height="480">
执行 `dev:watch` 时，此处依次显示依赖准备、Wasm 编译、资源加载和组件渲染阶段。编译失败时请查看终端日志。
</WasmPreview>

## 按组件嵌入

组件页可使用全局注册的 `WasmPreview`：

```html
<WasmPreview demo="button" title="HyperButton 交互预览" />
```

`demo` 会作为 URL hash 传给 preview，`embedded=1` 使 iframe 只渲染该组件示例。未知 ID 当前回退到第一个组件。

日常开发可在 `vitepress/` 执行 `npm run dev:watch`。它会启动文档、后台构建预览，并在 Kotlin 源码保存后自动重新发布；产物就绪后 iframe 自动加载和刷新。

开发时可按 [开发期实时预览](preview-update-workflow.md#开发期实时预览) 手动启动 Wasm 开发服务器，并在 VitePress 终端设置 `VITE_HYPER_UI_PREVIEW_DEV_URL`。此时 iframe 直接加载开发服务器，保存 Kotlin 源码后由其刷新预览。未设置变量时使用下方静态产物。

## 静态产物位置

组件、示例或 preview 主题更新后，按 [组件更新后刷新预览](preview-update-workflow.md) 在 `preview/` 目录下手动执行：

```powershell
cd preview
.\gradlew.bat kotlinWasmUpgradePackageLock
.\gradlew.bat publishWasmToVitePress
```

Preview 的默认发布产物目录：

```text
preview/build/dist/wasmJs/productionExecutable/
```

手动执行 `publishWasmToVitePress` 后，完整产物会复制到：

```text
vitepress/public/wasm-preview/
```

不能只复制 `index.html`，其关联的 Wasm、JavaScript 和资源文件也必须保持原目录结构。发布任务会写入 `preview-ready.json`，供文档页判断预览是否可用。

## 职责边界

- `vitepress/docs/`：语义化 Markdown，供 AI、搜索和开发者阅读。
- VitePress：渲染 Markdown、导航和本地搜索。
- `WasmPreview.vue`：显示构建和首帧加载阶段，通过 iframe 嵌入交互示例。
- `preview/`：维护 Compose 交互示例；嵌入模式只渲染组件，独立模式保留目录浏览。
