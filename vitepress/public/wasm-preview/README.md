# Wasm 预览产物占位目录

此目录有意不包含任何编译产物。

`npm run dev:watch` 会在开发期自动调用下方 Gradle 任务；也可按需手动执行。任务会将下面源目录中的**全部内容**复制到本目录，并保留文件名与子目录结构：

```text
preview/build/dist/wasmJs/productionExecutable/
```

在 `preview/` 目录下手动执行：

```powershell
cd preview
.\gradlew.bat kotlinWasmUpgradePackageLock
.\gradlew.bat publishWasmToVitePress
```

该任务会生成 Wasm 静态产物、复制到本目录，并在确认入口文件后写入 `preview-ready.json`。
`dev:watch` 运行期间还会写入 `preview-build-status.json`，供 VitePress 显示依赖准备、编译发布和失败状态；浏览器加载资源与渲染首帧的阶段由页面自身判断。这两个 JSON 都是本地生成文件，不需要提交。

复制完成后的入口应位于：

```text
vitepress/public/wasm-preview/index.html
```

注意事项：

- 不要把 Wasm、JavaScript、生成的 HTML 或资源文件提交到版本库。
- 普通 `npm run dev` 不触发 Gradle；`npm run dev:watch` 会自动发布并监听 Kotlin 源码。
- 本目录中的生成文件已由仓库根目录 `.gitignore` 排除；仅本说明文件需要保留。
