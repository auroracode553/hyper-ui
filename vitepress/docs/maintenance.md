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
│  通过 iframe 加载自己端口下的         │   │                              │
│  /wasm-preview/index.html            │   │                              │
│  而不是去连 8081 端口!                │   │                              │
└──────────────────────────────────────┘   └──────────────────────────────┘
```

**关键结论：**

- VitePress 文档站的 `<WasmPreview>` iframe 只能加载 VitePress 自己端口下的静态文件。它不会去连接 `wasmJsBrowserDevelopmentRun` 启动的开发服务器（8081 等端口）。
- 要让文档里出现组件预览，必须先 `wasmJsBrowserDistribution` 构建静态产物，再手动复制到 `vitepress/public/wasm-preview/`。
- 组件、示例或 preview 主题更新后，按 [组件更新后刷新预览](preview-update-workflow.md) 在 `preview/` 目录下执行 `.\gradlew.bat publishWasmToVitePress`。
- 如果你只想看文档/写文档，只启动 VitePress 就够了，不需要碰 preview。

---

### 第一步：启动 VitePress 文档站（必须）

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

如果文档页面没有 `<WasmPreview>` 组件，到这一步就够了，preview 项目完全不需要启动。

---

### 第二步（可选）：让文档中的 WasmPreview 显示组件预览

```powershell
cd preview
.\gradlew.bat publishWasmToVitePress
```

这个 task 会自动执行 `wasmJsBrowserDistribution` 并把产物复制到 `vitepress/public/wasm-preview/`，一步完成。刷新 `http://localhost:5173` 即可。

**为什么不能直接用 `wasmJsBrowserDevelopmentRun`？**

`wasmJsBrowserDevelopmentRun` 启动的是一个**独立的** webpack-dev-server（端口号不固定，比如 8081），它提供源代码级热更新，适合开发/调试组件交互代码。但 VitePress 的 `<WasmPreview>` 组件通过 iframe 加载的是 VitePress 自己 5173 端口下的 `/wasm-preview/index.html`，**根本不会去连接 8081 端口的服务**。

所以正确的做法只有：`wasmJsBrowserDistribution` → 复制静态文件 → VitePress 从自己端口提供。

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
- Desktop/Wasm preview 展示真实交互，但不能替代参数与约束文档。

## 组件改动检查表

新增、删除、重命名组件，或修改参数、默认值、枚举、配置类型、状态规则时，必须同步：

1. `library/src/main/java/hyper_ui/` 对应源码。
2. `vitepress/docs/components/` 对应组件页。
3. `vitepress/docs/component-index.md`。
4. `preview/src/commonMain/kotlin/hyper_ui/docs/data/` 对应 `*ComponentDemos.kt`。
5. `preview/src/commonMain/kotlin/hyper_ui/docs/ui/` 对应 `*ComponentShowcases.kt`。
6. `vitepress/.vitepress/config.mts` 的导航和侧栏（分组变化时）。
7. 根目录 `README.md`。

每个组件页至少包含：

- 包名、源码路径、状态归属、适用场景。
- 与源码一致的完整公开签名。
- 参数类型、默认值与行为说明。
- 最小可用示例。
- 关键约束、常见错误和关联 API。

## Wasm 预览边界

Wasm 预览仅用于浏览器交互。AI 不应通过画面推断参数、默认值或状态规则；这些信息必须来自 Markdown。

Android-only API 不能进入 Wasm 编译链，应在预览中提供交互模拟和 Android 调用代码，并在组件页明确标记平台差异。
