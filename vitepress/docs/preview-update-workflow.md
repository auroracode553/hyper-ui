# 组件更新后刷新预览

本页用于记录维护者在更新组件、示例或 preview 主题后，如何让 VitePress 文档页中的 `<WasmPreview>` iframe 显示最新内容。

## 什么时候需要执行

只要改动会影响文档中的交互预览，就需要重新发布 Wasm 静态产物：

- 修改 `library/src/main/java/hyper_ui/` 下组件实现。
- 修改 `preview/src/commonMain/kotlin/hyper_ui/docs/data/` 下组件注册、变体元数据、API 文档映射或示例代码。
- 修改 `preview/src/commonMain/kotlin/hyper_ui/docs/ui/` 下交互 Showcase。
- 修改 `preview/src/commonMain/kotlin/hyper_ui/docs/theme/` 或 Wasm 入口资源，例如字体、主题、布局。

只查看 VitePress 页面时，修改普通 Markdown 不需要执行本流程；如果还要让 Wasm Preview 内嵌的 API 正文同步更新，则需要重新发布 Preview 资源。

## 正确命令

在 `preview/` 目录下手动执行：

```powershell
cd preview
.\gradlew.bat publishWasmToVitePress
```

这个 Gradle task 会生成 Wasm 静态产物，并复制到：

```text
vitepress/public/wasm-preview/
```

复制完成后，VitePress iframe 实际加载的入口应为：

```text
vitepress/public/wasm-preview/index.html
```

## 刷新确认

执行完成后，在浏览器中刷新 VitePress 页面：

```text
http://localhost:5173
```

如果仍然看到旧内容，先强制刷新页面；也可以直接访问预览入口确认：

```text
http://localhost:5173/wasm-preview/index.html#button
```

其中 `#button` 可以替换为具体组件页使用的 demo id。

## 开发期自动热更新（dev:watch）

开发组件时不需要手动反复执行上面的流程。在 `vitepress/` 目录执行：

```powershell
cd vitepress
npm run dev:watch
```

该命令由 `tools/dev-watch.mjs` 实现，会：

1. 启动时先执行一次 `publishWasmToVitePress`，然后拉起 VitePress dev。
2. 监听 `library/` 和 `preview/` 下的 `.kt` / `.kts` 文件（忽略 `build/` 产物目录），保存后防抖 2 秒自动重新发布 Wasm 产物；构建期间的新变化会记账并在本次构建结束后补跑一次。
3. 发布成功后更新 `public/wasm-preview/.build-version` 标记文件；页面中的 `<WasmPreview>` 组件在开发模式下每 2 秒轮询该标记，变化后自动重载预览 iframe，无需手动刷新浏览器。

可用环境变量：`HYPER_UI_VITE_PORT`（默认 5173）、`HYPER_UI_WATCH_DEBOUNCE`（默认 2000 毫秒）。按 Ctrl+C 同时停止文件监听与 VitePress。

注意：`npm run dev`（不带 watch）不会监听 Kotlin 源码，仍按上面的手动流程发布。

## 常见误区

不要用下面的命令来更新 VitePress iframe：

```powershell
cd preview
.\gradlew.bat run
```

`run` 启动的是 Desktop 预览，不会更新 `vitepress/public/wasm-preview/`。

也不要以为 `wasmJsBrowserDevelopmentRun` 会被 VitePress iframe 自动引用。它启动的是独立开发服务器，适合单独调试 Wasm 预览；VitePress iframe 加载的是自己站点路径下的静态文件：

```text
/wasm-preview/index.html
```

## 中文字体

Wasm 预览中的中文字体通过 preview 工程内置资源加载：

```text
preview/src/commonMain/composeResources/font/noto_sans_sc_wght.ttf
```

该字体用于 Compose canvas 文本渲染，不依赖 `index.html` 的 CSS `font-family`。字体授权文件保存在：

```text
preview/src/commonMain/composeResources/files/font-licenses/NotoSansSC-OFL.txt
```

## 提交流程提醒

- 不要提交生成的 `.wasm`、`.js`、`.map` 或生成后的 `index.html`。
- 只提交源码、文档和保留说明文件。
- 发布或本地验收前，由维护者按需手动执行 `publishWasmToVitePress`。

## 前置依赖

- JDK 17+
- 本地 Node.js，并确保 `node` 在 `PATH` 中
