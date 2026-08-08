# HyperUI

[![在线文档](https://img.shields.io/badge/%F0%9F%93%96-在线文档-4FC08D?style=for-the-badge&logo=vuedotjs&logoColor=white)](https://auroracode553.github.io/hyper-ui)

> Android Jetpack Compose UI 组件库，专注提供 HyperOS 风格的基础界面组件，不承载业务逻辑。

**源码仓库**: `git@gitee.com:my_new_way/hyper_ui.git`

**在线文档**: [auroracode553.github.io/hyper-ui](https://auroracode553.github.io/hyper-ui)

## 先看结论

- 调用方可以通过 Maven 坐标、源码模块或 AAR 引入 HyperUI。
- 当前项目使用 AGP 9.1.1，Kotlin Android 支持由 AGP 内置，不再额外应用 `org.jetbrains.kotlin.android` 插件。
- 当前库的 `minSdk` 为 `30`，调用方应用的 `minSdk` 不能低于 30。
- AI 或新调用者应优先阅读 [vitepress/docs/index.md](vitepress/docs/index.md)，再按 [组件索引](vitepress/docs/component-index.md) 打开具体组件页。
- `vitepress/docs/` 是权威 Markdown 文档，`vitepress/` 负责网页渲染，`preview/` 负责 Desktop 与 Wasm 交互预览。

## AI 接入说明

如果你在其他项目中使用 HyperUI，请让 AI 先阅读 Markdown 文档入口：

```text
https://gitee.com/my_new_way/hyper_ui/blob/master/vitepress/docs/index.md
```

消费方项目不需要复制整个 `vitepress/docs/`。建议只在消费方项目根目录的 `AGENTS.md` 中保留简短说明：

```markdown
## HyperUI

本项目使用 HyperUI：`com.hyperui:hyper-ui:1.0.0`

AI 编写 HyperUI 代码前，请优先参考：
https://gitee.com/my_new_way/hyper_ui/blob/master/vitepress/docs/index.md

关键规则：
- HyperUI 公开 API 统一在 `hyper_ui` 包，可使用 `import hyper_ui.*`。
- 使用前包裹 `HyperThemeConfig`。
- 组件不持有业务状态，状态由调用方管理。
```

## 适用范围

HyperUI 面向 Android Compose 项目，提供按钮、输入框、列表分组、设置项、弹窗、抽屉、加载进度条、顶部栏、底部导航等基础组件。

不适合的场景：

- 传统 View XML 项目直接使用。
- 非 Android 目标直接依赖 AAR。
- 把业务状态、网络请求、数据库逻辑放进 UI 组件库。

## 接入方式

### 方式一：Maven 依赖

在调用方项目的 `settings.gradle.kts` 中加入实际发布使用的 Maven 仓库。仓库地址取决于发布方案，例如 Maven Central、JitPack、GitHub Pages / 对象存储静态 Maven、GitHub Packages 或私有制品库。

如果发布到普通公开 Maven 仓库或静态 Maven 仓库，常见格式如下：

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://你的-maven-仓库地址/")
    }
}
```

在调用方模块的 `build.gradle.kts` 中加入依赖：

```kotlin
dependencies {
    implementation("com.hyperui:hyper-ui:1.0.0")
}
```

如果使用 JitPack，仓库地址通常为 `https://jitpack.io`，依赖坐标会改为 JitPack 生成的 `com.github.<user>:<repo>:<tag>` 格式，不能继续假设一定是 `com.hyperui:hyper-ui:1.0.0`。

本项目的 JitPack 地址：[https://jitpack.io/#auroracode553/hyper-ui](https://jitpack.io/#auroracode553/hyper-ui)

### 方式二：本地源码联调

适合本地联调或需要直接改组件源码的场景。推荐使用 Gradle composite build，在调用方仍保留正式 Maven/JitPack 坐标，本地存在源码仓库时由 Gradle 自动替换为本地工程。

调用方 `settings.gradle.kts`：

```kotlin
val hyperUiLocal = file("../hyper_ui/library")
if (hyperUiLocal.exists()) {
    includeBuild(hyperUiLocal) {
        dependencySubstitution {
            substitute(module("com.github.auroracode553:hyper-ui")).using(project(":"))
        }
    }
}
```

调用方模块依赖：

```kotlin
dependencies {
    implementation("com.github.auroracode553:hyper-ui:0.0.4")
}
```

### 方式三：AAR 文件

把发布得到的 `hyper_ui-release.aar` 放到调用方模块的 `libs/` 目录：

```kotlin
dependencies {
    implementation(files("libs/hyper_ui-release.aar"))
}
```

如果只使用裸 AAR，需要调用方自行补齐 Compose、Material3 等依赖；推荐优先使用 Maven 方式，让 Gradle 读取 POM 中的依赖信息。

## 最小使用示例

```kotlin
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import hyper_ui.*

@Composable
fun App() {
    MaterialTheme {
        HyperThemeConfig(themeColor = rgba(255, 103, 0)) {
            HyperButton(onClick = { /* 调用方处理业务逻辑 */ }) {
                Text("保存")
            }
        }
    }
}
```

## 组件范围

- 公开 API 包名统一为 `hyper_ui`，调用方可以用 `import hyper_ui.*` 一次导入 HyperUI 组件、配置、枚举和工具方法。Kotlin 通配符导入只影响源码可见性，不会因为写了 `import hyper_ui.*` 就强制把所有组件打进调用方最终产物；最终未使用代码裁剪取决于调用方的 release/minify/R8 配置。
- 主题与样式：`HyperThemeConfig`, `HyperTheme`, `HyperColors`, `HyperStyleDefaults`, `rgba`
- 基础组件：`HyperButton`, `HyperIconButton`（slot-first 容器，内容由调用方渲染；视觉通过 `tone`、`colors`、`shape`、`border` 控制）
- 表单组件：`HyperTextField`, `HyperSwitch`, `HyperCheckbox`, `HyperRadioButton`（输入框和开关默认带轻描边/阴影轮廓；搜索框、地址栏通过 `HyperTextField` 的 leading/trailing slots 组合）
- 容器组件：`HyperPanel`, `HyperColorPicker`（主题色选择板，选中状态由调用方管理）
- 列表组件：`HyperLazyList`, `HyperList`, `HyperListItem`, `HyperMenuGroup`, `HyperMenuItem`
- 浮层反馈：`HyperDialog`, `HyperDialogDefaults`, `HyperAlertDialog`, `HyperDropdownMenu`
- 加载反馈：`HyperLinearProgressIndicator`, `HyperCircularProgressIndicator`（`progress = null` 表示不确定加载）
- 导航组件：`HyperTopBar`, `HyperDrawer`, `HyperDrawerHeader`, `HyperDrawerItem`, `HyperDrawerPosition`, `HyperGroupMenus`, `HyperBottomBar`, `HyperBottomBarItemLayout`（`HyperGroupMenus` 用于横向分组菜单；`HyperBottomBar` 支持完整内容 slot 与泛型 items 两种入口，页面切换由调用方处理）
- 内部公共工具：`hyper_ui.core` 目录仅供 UI 库内部复用，调用方不要直接依赖。

## 状态管理原则

- 组件不持有业务状态。
- `value`、`checked`、`selected`、`visible`、`open`、`expanded` 等状态由调用方管理。
- 组件通过 `onValueChange`、`onCheckedChange`、`onClick`、`onDismissRequest` 等回调通知调用方。
- `HyperDialog` 正文内容由 slot 渲染，长内容在内容区滚动并显示滚动指示条，固定底部操作放入 `actionContent`。默认从屏幕居中弹出，带淡入+缩放动画，无遮罩，面板有阴影浮层效果；弹窗内容会隔离外层文本选择容器，支持放入输入框。
- 组件内部只处理焦点、动画、禁用透明度、描边/阴影等视觉反馈 UI 状态；`HyperTextField` 聚焦时不改变容器背景。

示例：

```kotlin
var keyword by remember { mutableStateOf("") }

HyperTextField(
    value = keyword,
    onValueChange = { keyword = it },
    placeholderContent = { Text("搜索") },
    leadingContent = {
        Icon(Icons.Default.Search, contentDescription = null)
    }
)
```

## 文档与交互预览架构

文档采用三层结构：

- `vitepress/docs/`：Markdown 权威内容，记录真实公开签名、参数默认值、状态归属、约束与示例，供 AI 和调用方阅读。
- `vitepress/`：将 `vitepress/docs/` 渲染为语义化静态网页，并通过 iframe 嵌入 Wasm 预览。
- `preview/`：Compose Multiplatform Desktop/Wasm 组件演示，用来操作真实组件状态。

调用方只依赖 `hyper_ui`，不依赖文档源码、`vitepress/` 或 `preview/`。AI 不应从 Wasm 画面推断 API，应读取 [vitepress/docs/index.md](vitepress/docs/index.md) 和具体组件页。

Preview 中可见的组件卡片必须与 `library/src/main/java/hyper_ui/components/` 下公开的可视化组件和 Android-only 组件工具保持一致：组件目录有的，preview 和文档要有；组件目录没有的，不作为组件卡片展示。`State`、`Defaults`、`Config`、枚举等辅助 API 不单独登记为组件卡片；主题色切换等文档外壳能力可以保留在 docs UI 中，但不登记为组件 demo。

目录结构：

```text
preview/
├── build.gradle.kts
├── settings.gradle.kts
└── src/
    ├── commonMain/kotlin/hyper_ui/docs/
    │   ├── DocsApp.kt       # Desktop/Wasm 共享文档根节点
    │   ├── data/            # 组件注册与示例代码片段
    │   ├── theme/           # 文档主题
    │   └── ui/              # 文档布局与交互示例
    ├── desktopMain/kotlin/hyper_ui/docs/Main.kt
    │                       # Desktop Window 入口
    └── wasmJsMain/
        ├── kotlin/hyper_ui/docs/Main.kt
        │                   # ComposeViewport 浏览器入口
        └── resources/index.html
                            # iframe 宿主页面
```

与正式库的关系：

- `preview` 的 `commonMain` 直接引用 `library/src/main/java/hyper_ui/` 中的平台无关 Compose 源码，供 Desktop 与 Wasm 共用。
- 公开组件源码按功能组放在 `library/src/main/java/hyper_ui/components/` 下，但包名统一声明为 `hyper_ui`，方便调用方 `import hyper_ui.*`。
- UI 库内部公共工具放在 `library/src/main/java/hyper_ui/core/` 下，供组件实现复用，不作为调用方公开入口。
- Android-only 工具不能进入 `commonMain` 编译链；Preview 页面只展示说明、可交互模拟和 Android 调用片段。
- `HyperBottomBar` 不依赖任何导航框架；Preview 直接展示正式组件，页面状态、内部按钮布局或跳转由调用方在 slot / `onItemClick` 中处理。
- 调用方接入时不需要依赖 `preview` 模块。
- Wasm 入口接受 `#组件-id`，例如 `index.html#button`，供 VitePress 组件页选择初始预览项；未知 ID 回退到第一个组件。

维护 preview 时优先看：

```text
preview/src/commonMain/kotlin/hyper_ui/docs/data/ComponentDemos.kt
preview/src/commonMain/kotlin/hyper_ui/docs/data/*ComponentDemos.kt
preview/src/commonMain/kotlin/hyper_ui/docs/ui/*ComponentShowcases.kt
```

`ComponentDemos.kt` 只保留聚合入口和 `ComponentDemo` 数据结构；具体组件文档项按 `基础组件`、`表单组件`、`容器组件`、`导航组件`、`列表组件`、`反馈组件` 拆在对应的 `*ComponentDemos.kt` 文件中。新增、删除或重命名公开组件时，必须同步更新同一分组的 data 文件、`*ComponentShowcases.kt`、`vitepress/docs/components/` 对应页面、[组件索引](vitepress/docs/component-index.md)、VitePress 侧栏和 README。

## VitePress 文档站

`vitepress/` 是完整的文档站目录，使用 `srcDir: 'docs'` 渲染 `vitepress/docs/` 下的 Markdown，不维护第二份组件正文。配置通过 Vite 的 `publicDir` 明确把静态目录指向 `vitepress/public/`。

```text
vitepress/
├── package.json                         # 依赖清单，不含自动脚本
├── docs/                                # Markdown 文档源码
├── .vitepress/config.mts                # 导航、侧栏、本地搜索
├── .vitepress/theme/
│   ├── index.ts                         # 注册文档主题组件
│   ├── custom.css
│   └── components/WasmPreview.vue       # iframe 预览组件
└── public/
    ├── llms.txt                          # AI 可发现的网页索引
    └── wasm-preview/README.md            # Wasm 产物放置说明
```

Wasm 静态产物不会自动复制，也不提交到仓库。使用者手动生成后，需要把 `preview/build/dist/wasmJs/productionExecutable/` 的**全部内容**放入 `vitepress/public/wasm-preview/`，不能只复制 `index.html`。

站点默认部署在域名根路径 `/`。部署到仓库子路径时，在手动启动或构建前设置 `VITEPRESS_BASE`，值必须以 `/` 开头和结尾，例如 `/hyper_ui/`；`WasmPreview` 会使用同一个 base 生成 iframe 地址。

依赖清单：

- Preview Web：`org.jetbrains.kotlinx:kotlinx-browser:0.3`（读取浏览器 hash）
- Node.js 18 或更高版本
- VitePress 1.6.4
- Vue 3.5.x

## 文档入口

- [vitepress/docs/index.md](vitepress/docs/index.md)：调用方和 AI 的首读入口。
- [vitepress/docs/component-index.md](vitepress/docs/component-index.md)：按分组进入每个组件的精确 API 文档。
- [vitepress/docs/preview.md](vitepress/docs/preview.md)：VitePress 与 Wasm iframe 的职责和放置方式。
- `preview/`：Desktop/Wasm 交互预览工程。
- `vitepress/`：语义化静态文档站配置。
- [vitepress/docs/maintenance.md](vitepress/docs/maintenance.md)：本地开发运行与文档维护规则。

## 手动运行 / 打包 / 发布参考

以下命令仅作为手动执行说明，按需在终端中执行：

Desktop preview：

```powershell
cd preview
.\gradlew.bat run
.\gradlew.bat run --continuous
.\gradlew.bat compileKotlinDesktop
```

Wasm preview（由使用者手动执行）：

```powershell
cd preview
.\gradlew.bat wasmJsBrowserDistribution
```

产物位于 `preview/build/dist/wasmJs/productionExecutable/`。将完整内容手动放入 `vitepress/public/wasm-preview/` 后，可在 `vitepress/` 目录按需手动执行：

```powershell
npm install
npx vitepress dev .
npx vitepress build .
```

VitePress 默认静态输出位于 `vitepress/.vitepress/dist/`。本仓库不提供自动构建、自动复制或一键部署逻辑。

如果静态站点部署在 `/hyper_ui/` 子路径，可由使用者在同一终端手动设置：

```powershell
$env:VITEPRESS_BASE = "/hyper_ui/"
npx vitepress build .
```

部署时上传 `vitepress/.vitepress/dist/` 的全部内容，并确认静态服务可以返回 `.wasm` 文件。

Android library：

```powershell
cd library
.\gradlew.bat assembleRelease
.\gradlew.bat publishToMavenLocal
```

发布信息：

```text
groupId:    com.hyperui
artifactId: hyper-ui
version:    1.0.0
```
