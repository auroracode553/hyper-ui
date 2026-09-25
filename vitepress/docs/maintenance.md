# 文档维护规则

## 本地开发运行

先搞清楚两个东西的关系：

```
┌──────────────────────────────────────┐   ┌──────────────────────────────┐
│  VitePress 文档站 (vitepress/)        │   │  Wasm 预览 (preview/)        │
│  npx vitepress dev                   │   │  .\gradlew.bat wasmJs...     │
│  → http://localhost:5173             │   │  → http://localhost:8081     │
│                                      │   │                              │
│  这是你看到的文档网站。               │   │  独立组件预览页面，跟文档站    │
│  vitepress/docs/ 里的 .md 由它渲染。  │   │  是两个完全不同的服务。       │
│                                      │   │  两者之间没有任何自动连接！   │
│                                      │   │                              │
│  文档里的 <WasmPreview> 组件          │   │                              │
│  默认加载同站点静态产物；配置开发      │   │                              │
│  服务器地址后直接嵌入右侧页面。        │   │                              │
└──────────────────────────────────────┘   └──────────────────────────────┘
```

**关键结论：**

- 日常开发可在 `vitepress/` 手动执行 `npm run dev:watch`：一条命令启动文档、首次发布和 Kotlin 源码监听；预览就绪后自动加载与刷新。
- `<WasmPreview>` 默认加载 VitePress 静态产物；设置 `VITE_HYPER_UI_PREVIEW_DEV_URL` 后会直接嵌入独立的 Wasm 开发服务器。
- 使用静态模式时，由使用者手动执行 `publishWasmToVitePress`，一次生成并复制完整静态产物和就绪标记。
- 静态模式下组件、示例或 preview 主题更新后，按 [组件更新后刷新预览](preview-update-workflow.md) 在 `preview/` 目录下执行 `.\gradlew.bat publishWasmToVitePress`。
- 如果你只想看文档/写文档，只启动 VitePress 就够了，不需要碰 preview。

---

### 第一步：启动 VitePress 文档站

```powershell
# 从项目根目录开始
cd vitepress

# 首次运行：安装依赖（之后不用再装）
# 如果 npm install 报 404，先执行：npm config set registry https://registry.npmmirror.com
npm install

# 启动开发服务器
npm run dev
```

浏览器访问 **`http://localhost:5173`**。Markdown 修改自动热更新，立刻能看到效果。

如需组件源码保存后也自动更新文档里的预览，请在同一目录改用 `npm run dev:watch`，不要同时启动两个 VitePress 进程。该命令在后台串行执行 Wasm 发布；首次产物就绪前页面会显示说明。

只阅读文档时，到这一步就够了，preview 项目不需要启动。

### 实时预览组件修改

需要在文档页面看到 Kotlin 修改的实时结果时，先在一个终端手动执行 `cd preview`、`.\gradlew.bat wasmJsBrowserDevelopmentRun`。等待终端打印实际地址后，在另一个 PowerShell 终端进入 `vitepress/`，将 `VITE_HYPER_UI_PREVIEW_DEV_URL` 设置为该地址，再手动执行 `npm run dev`。具体示例见 [开发期实时预览](preview-update-workflow.md#开发期实时预览)。

在这个双终端模式下，普通 `npm run dev` 不会启动 Gradle；保存源码后的重编译和浏览器刷新由已启动的 Wasm 开发服务器处理。

---

### 第二步（可选）：让文档中的 WasmPreview 显示组件预览

```powershell
cd preview
.\gradlew.bat kotlinWasmUpgradePackageLock
.\gradlew.bat publishWasmToVitePress
```

这个 task 会执行 `wasmJsBrowserDistribution` 并把产物复制到 `vitepress/public/wasm-preview/`，最后写入 `preview-ready.json`。VitePress 开发页会检测就绪标记并加载新预览；首次发布前显示说明，不显示 404 iframe。

**静态模式与开发服务器的关系**

`wasmJsBrowserDevelopmentRun` 启动独立的 webpack 开发服务器，端口以终端输出为准。只有设置 `VITE_HYPER_UI_PREVIEW_DEV_URL` 后，文档 iframe 才会嵌入这个开发服务器；未设置时仍加载 VitePress 自己端口下的 `/wasm-preview/index.html`。

静态预览由 `publishWasmToVitePress` 构建并复制；实时预览直接嵌入开发服务器。两种模式均由使用者按需手动启动。

---

### 独立调试 Wasm 预览（不嵌入文档）

如果你只想调试组件交互效果，不需要 VitePress：

```powershell
cd preview
.\gradlew.bat wasmJsBrowserDevelopmentRun
```

启动后终端会打印实际端口（如 `http://localhost:8081`），直接在浏览器打开。这是一个单独的页面（组件列表 + 交互区）。

**Desktop 桌面预览（推荐用于调试，最快最稳定，无需 Node.js）：**

```powershell
cd preview
.\gradlew.bat desktopRun
```

启动后打开桌面窗口（标题 "HyperUI Docs"，尺寸 1200×820dp）。

---

### 三种运行方式对比

| 命令 | 是什么 | 能看到什么 | 需要 Node.js |
|------|--------|-----------|-------------|
| `npm run dev` | VitePress 文档站 | 文档网站（`http://localhost:5173`） | 是 |
| `cd preview && .\gradlew.bat desktopRun` | Desktop 预览窗口 | 组件交互（桌面应用） | 否 |
| `cd preview && .\gradlew.bat wasmJsBrowserDevelopmentRun` | Wasm 开发服务器 | 组件交互（浏览器，如 `http://localhost:8081`） | 是 |
| `cd preview && .\gradlew.bat publishWasmToVitePress` | 构建 + 自动复制 Wasm 到 VitePress | 刷新文档站后可见交互预览 | 是 |

---

### 前置依赖

- **VitePress**：Node.js >= 18，npm 源设为 `https://registry.npmmirror.com`
- **preview Desktop**：JDK 17+
- **preview Wasm**：JDK 17+、本地 Node.js（在 PATH 中）

---

## 单一事实来源

- Kotlin 源码定义真实公开 API。
- `vitepress/docs/` 用准确的 Markdown 解释这些 API，是 AI 和调用方的首读入口。
- VitePress 导航只引用 `vitepress/docs/`，不维护第二份组件正文。
- Desktop/Wasm preview 展示真实交互，并从构建资源加载同一份 Markdown；属性表从公开签名自动提取，不能替代或另行维护参数与约束正文。

## 组件改动检查表

新增、删除、重命名组件，或修改参数、默认值、枚举、配置类型、状态规则时，必须同步：

1. `library/src/main/java/hyper_ui/` 对应源码。
2. `vitepress/docs/components/` 对应组件页。
3. `vitepress/docs/component-index.md`。
4. `preview/src/commonMain/kotlin/hyper_ui/docs/data/` 对应 `*ComponentDemos.kt`。
5. `preview/src/commonMain/kotlin/hyper_ui/docs/ui/` 对应 `*ComponentShowcases.kt`。
6. `vitepress/.vitepress/config.mts` 的导航和侧栏（分组变化时）。
7. 根目录 `README.md`。

每个 `ComponentDemo` 必须提供 `variants`（逐项标识预览属性与样式）和 `apiDocumentPaths`（关联 `vitepress/docs/components/` 下的权威 Markdown）。

每个组件页至少包含：

- 包名、源码路径、状态归属、适用场景。
- 与源码一致的完整公开签名。
- 参数类型、默认值与行为说明。
- 最小可用示例。
- 关键约束、常见错误和关联 API。

## Wasm 预览边界

Wasm 预览仅用于浏览器交互。AI 不应通过画面推断参数、默认值或状态规则；这些信息必须来自 Markdown。

Android-only API 不能进入 Wasm 编译链，应在预览中提供交互模拟和 Android 调用代码，并在组件页明确标记平台差异。
