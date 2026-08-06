# Wasm 交互预览

该区域加载 Compose Multiplatform Wasm 文档应用，用于让浏览器访问者操作真实组件示例。

Markdown 中的公开签名、参数、状态与约束仍是 AI 的文档来源；不要从预览画面推断 API。

<WasmPreview title="HyperUI 组件交互预览" :height="760">
若此区域显示空白或加载失败，请确认 Wasm 完整静态产物已由使用者手动放入 `vitepress/public/wasm-preview/`，并保留原始目录结构。
</WasmPreview>

## 按组件嵌入

组件页可使用全局注册的 `WasmPreview`：

```html
<WasmPreview demo="button" title="HyperButton 交互预览" />
```

`demo` 会作为 URL hash 传给 preview。浏览器入口支持该 ID 时直接选中对应组件；无法识别时回退到文档首页。

## 静态产物位置

组件、示例或 preview 主题更新后，按 [组件更新后刷新预览](preview-update-workflow.md) 在 `preview/` 目录下手动执行：

```powershell
cd preview
.\gradlew.bat publishWasmToVitePress
```

Preview 的默认发布产物目录：

```text
preview/build/dist/wasmJs/productionExecutable/
```

使用者需要手动将该目录的全部内容复制到：

```text
vitepress/public/wasm-preview/
```

不能只复制 `index.html`，其关联的 Wasm、JavaScript 和资源文件也必须保持原目录结构。

## 职责边界

- `vitepress/docs/`：语义化 Markdown，供 AI、搜索和开发者阅读。
- VitePress：渲染 Markdown、导航和本地搜索。
- `WasmPreview.vue`：通过 iframe 隔离并嵌入交互预览。
- `preview/`：维护 Compose 交互示例。
