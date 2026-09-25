# 组件更新后刷新预览

本页用于记录维护者在更新组件、示例或 preview 主题后，如何让 VitePress 文档页中的 `<WasmPreview>` iframe 显示最新内容。

## 一条命令更新文档和预览

在 `vitepress/` 目录手动执行 `npm run dev:watch`。该命令会启动文档站，先执行 `kotlinWasmUpgradePackageLock` 更新 Kotlin/Wasm 的 npm 锁文件，再执行 `publishWasmToVitePress`；之后监听 `library/`、`preview/` 的 Kotlin 源码和 `preview/src/wasmJsMain/resources/` 的入口资源变化，并按相同顺序串行重新发布。Gradle 生成目录不会触发重建。文档可先打开；预览区域依次显示依赖准备、Wasm 编译、资源加载和组件渲染四个阶段，失败时显示错误提示，成功后自动加载并在后续保存源码时刷新。阶段条表示当前步骤，不是 Gradle 的百分比；详细任务日志仍在终端。按 Ctrl+C 停止监听和文档站。

首次构建仍需下载 Binaryen 等 Wasm 工具依赖，耗时取决于仓库连接；日志持续停在依赖解析时参照下方诊断步骤。`dev:watch` 不会把文档 404 当作 Gradle 错误。

## 开发期实时预览

需要直接使用 Wasm 开发服务器的刷新机制时，可选用两个独立服务：

1. 在第一个终端进入 `preview/`，手动执行 `.\gradlew.bat wasmJsBrowserDevelopmentRun`，等待终端打印 Wasm 开发服务器的实际地址。首次启动仍需解析和下载 Wasm 工具依赖。
2. 在第二个 PowerShell 终端进入 `vitepress/`，将该地址设置为 `VITE_HYPER_UI_PREVIEW_DEV_URL`，再手动执行 `npm run dev`。例如开发服务器打印 `http://localhost:8081/` 时：

   ```powershell
   $env:VITE_HYPER_UI_PREVIEW_DEV_URL = 'http://localhost:8081/'
   npm run dev
   ```

配置后，文档中的 `<WasmPreview>` 直接嵌入 Wasm 开发服务器页面，沿用其源码监听与浏览器刷新。端口以第一个终端实际打印的地址为准。开发服务器未就绪时组件会显示连接提示，并定期重试。两个服务均由使用者手动启动和停止；`npm run dev` 不会调用 Gradle。

未设置此环境变量且使用普通 `npm run dev` 时，文档使用下方的静态发布模式。`npm run dev:watch` 会自行发布静态产物并自动刷新，无需设置该变量。

## 什么时候需要手动发布静态预览

使用静态模式时，只要改动会影响文档中的交互预览，就需要重新发布 Wasm 静态产物：

- 修改 `library/src/main/java/hyper_ui/` 下组件实现。
- 修改 `preview/src/commonMain/kotlin/hyper_ui/docs/data/` 下组件注册、变体元数据、API 文档映射或示例代码。
- 修改 `preview/src/commonMain/kotlin/hyper_ui/docs/ui/` 下交互 Showcase。
- 修改 `preview/src/commonMain/kotlin/hyper_ui/docs/theme/` 或 Wasm 入口资源，例如字体、主题、布局。

只查看 VitePress 页面时，修改普通 Markdown 不需要执行本流程；嵌入的 Wasm Preview 只显示组件示例，不承载 API 正文。

## 正确命令

在 `preview/` 目录下手动执行：

```powershell
cd preview
.\gradlew.bat kotlinWasmUpgradePackageLock
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

发布任务还会在确认入口和 JavaScript 文件存在后写入 `preview-ready.json`。文档页先读取该就绪文件，再创建 iframe；构建尚未完成或发布失败时会显示说明，而不会显示 404 页面。开发模式会定期重新检查，发布完成后自动加载新版本。

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

## 静态文档与预览分开启动

只阅读文档时，可在 `vitepress/` 目录手动执行 `npm run dev`。该命令不会启动 Gradle。使用静态模式并需要更新交互预览时，再按上面的步骤手动执行 `publishWasmToVitePress`。构建期间旧预览仍可阅读；首次构建前页面会显示“尚未发布”。

日志若长时间停在 `Resolve files of configuration ':detachedConfiguration…' > binaryen-version…`，说明仍处于 Wasm 工具依赖解析阶段，不能用固定耗时判断构建是否正常。手动重试时可为 Gradle 添加 `--info --stacktrace` 查看正在访问的仓库和具体错误；检查网络、代理或仓库连通性后再决定是否重试。不要只根据 VitePress 的 404 判断 Gradle 已失败。

若手动直接执行 `publishWasmToVitePress` 时看到 `kotlinWasmStorePackageLock` 报 `Lock file was changed`，需先手动执行 `kotlinWasmUpgradePackageLock`。上方手动步骤和 `dev:watch` 均已按仓库 CI 的顺序处理。锁文件位于 `preview/kotlin-js-store/`，属于本地生成内容。

### GitHub 连接超时时使用本地 Binaryen 压缩包

如果错误明确显示访问 `github.com/WebAssembly/binaryen/releases` 超时，可从 [Binaryen 官方 version_125 发布页](https://github.com/WebAssembly/binaryen/releases/tag/version_125)在可访问 GitHub 的网络中取得 `binaryen-version_125-x86_64-windows.tar.gz` 及对应的 `.sha256` 文件。核对校验值后，把未解压的 `.tar.gz` 放入一个本地目录；不需要复制到仓库或 Gradle 缓存。

在启动 `dev:watch` 的同一个 PowerShell 终端设置目录地址，例如：

```powershell
$env:HYPER_UI_BINARYEN_ARCHIVE_DIR = 'D:\Downloads\binaryen'
npm run dev:watch
```

目录中应直接包含 `binaryen-version_125-x86_64-windows.tar.gz`。设置该变量后，`preview/build.gradle.kts` 只从这个目录解析 Binaryen；文件缺失会直接报错，避免再次等待 GitHub 超时。变量未设置时仍使用 Kotlin/Gradle 默认下载方式。这里的 `125` 是当前日志显示的版本；升级 Kotlin 后应以新的错误日志要求为准。

## 常见误区

不要用下面的命令来更新 VitePress iframe：

```powershell
cd preview
.\gradlew.bat run
```

`run` 启动的是 Desktop 预览，不会更新 `vitepress/public/wasm-preview/`。

若未设置 `VITE_HYPER_UI_PREVIEW_DEV_URL`，`wasmJsBrowserDevelopmentRun` 不会被 VitePress iframe 自动引用。此时 VitePress iframe 加载的是自己站点路径下的静态文件：

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
- 静态发布或本地验收前，可手动执行 `publishWasmToVitePress`；使用 `dev:watch` 时由该命令按源码变化触发发布。

## 前置依赖

- JDK 17+
- 本地 Node.js，并确保 `node` 在 `PATH` 中
