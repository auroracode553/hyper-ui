# 组件更新后刷新预览

本页用于记录维护者在更新组件、示例或 preview 主题后，如何让 VitePress 文档页中的 `<WasmPreview>` iframe 显示最新内容。

## 什么时候需要执行

只要改动会影响文档中的交互预览，就需要重新发布 Wasm 静态产物：

- 修改 `library/src/main/java/hyper_ui/` 下组件实现。
- 修改 `preview/src/commonMain/kotlin/hyper_ui/docs/data/` 下组件注册或示例代码。
- 修改 `preview/src/commonMain/kotlin/hyper_ui/docs/ui/` 下交互 Showcase。
- 修改 `preview/src/commonMain/kotlin/hyper_ui/docs/theme/` 或 Wasm 入口资源，例如字体、主题、布局。

只修改普通 Markdown 文档时，不需要执行本流程；VitePress 会刷新 Markdown 页面。

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
