# Wasm 预览产物占位目录

此目录有意不包含任何编译产物。

使用者手动构建 Compose Multiplatform Web preview 后，将下面源目录中的**全部内容**复制到本目录，并保留文件名与子目录结构：

```text
preview/build/dist/wasmJs/productionExecutable/
```

在 `preview/` 目录下手动执行：

```powershell
cd preview
.\gradlew.bat publishWasmToVitePress
```

该任务会生成 Wasm 静态产物并复制到本目录。

复制完成后的入口应位于：

```text
vitepress/public/wasm-preview/index.html
```

注意事项：

- 不要把 Wasm、JavaScript、生成的 HTML 或资源文件提交到版本库。
- 不要在文档项目中添加自动构建、自动复制或一键启动脚本。
- 本目录中的生成文件已由 `vitepress/.gitignore` 排除；仅本说明文件需要保留。
