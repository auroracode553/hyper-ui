# Wasm 预览产物占位目录

此目录有意不包含任何编译产物。

维护者手动运行现有 `npm run dev` 监听流程后会调用下方 Gradle 任务；也可按需手动执行。任务会将下面源目录中的**全部内容**复制到本目录，并保留文件名与子目录结构：

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
监听流程运行期间还会原子写入 `preview-build-status.json`，包含实际 Gradle 任务、最新输出、构建和阶段起点、输出时间及每秒心跳。VitePress 展示编译进度、失败、停止与心跳中断；加载资源和渲染首帧由页面判断，并核对实际组件 ID。编译期间不显示旧产物，缺失或过期时不回退到其他示例。这两个 JSON 都是本地生成文件，不需要提交。

复制完成后的入口应位于：

```text
vitepress/public/wasm-preview/index.html
```

注意事项：

- 不要把 Wasm、JavaScript、生成的 HTML 或资源文件提交到版本库。
- `npm run dev:doc` 仅启动文档；现有 `npm run dev` 负责监听与构建，需由维护者按运行约束手动选择。
- 本目录中的生成文件已由仓库根目录 `.gitignore` 排除；仅本说明文件需要保留。
