# HyperUI

[![在线文档](https://img.shields.io/badge/%F0%9F%93%96-在线文档-4FC08D?style=for-the-badge&logo=vuedotjs&logoColor=white)](https://auroracode553.github.io/hyper-ui)

> 面向 Android 手机竖屏的 Jetpack Compose UI 组件库，专注提供 HyperOS 风格的基础界面组件，不承载业务逻辑。

**源码仓库**: `git@gitee.com:my_new_way/hyper_ui.git`

**在线文档**: [auroracode553.github.io/hyper-ui](https://auroracode553.github.io/hyper-ui)

## Flutter 参考设计与 Compose API

默认视觉令牌以 `D:/my_project/flutter_project/flutter-hyper-ui/ui` 为参考：蓝色主题色 `rgba(71, 111, 232)`、浅深色玻璃表面、38dp 按钮、16dp 基础圆角和即时按压反馈。组件公开 API 采用统一字符串词汇：`type` 负责渲染类型，`size` 负责 `small`、`default`、`large` 尺寸。按钮按下立即开始 85ms 的整体缩放与透明度过渡，释放或拖出取消后用 180ms 恢复。`HyperThemeConfig` 可注入 `themeColor`、`typography`、`shapes` 与 `glass`；需要真实背景模糊时用 `HyperBackdrop` 包裹背景和组件。`HyperSlideMenu` 是受控列表侧滑操作，旧的横向分类按钮更名为 `HyperFilterBar`。语义色由 `colors` 注入，加载态由 `loading` 控制。组件公开 API 仍统一在 `hyper_ui` 包。

```kotlin
HyperThemeConfig {
    HyperBackdrop() {
        HyperButton(onClick = onSave, type = "filled") {
            HyperText("保存")
        }
    }
}
```

完整签名、状态归属和交互预览见 [组件索引](vitepress/docs/component-index.md)。

## 先看结论

- 调用方可以通过 Maven 坐标、源码模块或 AAR 引入 HyperUI。
- 当前项目使用 AGP 9.4.0，Kotlin Android 支持由 AGP 内置，不再额外应用 `org.jetbrains.kotlin.android` 插件。
- 当前库的 `minSdk` 为 `30`，调用方应用的 `minSdk` 不能低于 30。
- HyperUI 内置语义图标统一使用 `com.composables:icons-lucide-android:2.2.1` VectorDrawable，不再用 Canvas 手绘图标；可替换图标继续保留 slot。
- AI 或新调用者应优先阅读 [vitepress/docs/index.md](vitepress/docs/index.md)，再按 [组件索引](vitepress/docs/component-index.md) 打开具体组件页。
- `vitepress/docs/` 是权威 Markdown 文档，`vitepress/` 负责网页渲染，`preview/` 只负责文档交互预览。当前公开支持范围是 Android 手机端。
- 手机端布局与交互约束见 [移动端规范](vitepress/docs/mobile-guidelines.md)。

## AI 接入说明

如果你在其他项目中使用 HyperUI，请让 AI 优先读取官网的纯文本入口：

```text
https://auroracode553.github.io/hyper-ui/llms.txt
https://auroracode553.github.io/hyper-ui/llms-full.txt
```

- `llms.txt` 是完整的 Markdown 文档索引。
- `llms-full.txt` 把全部权威文档合并到单个响应，适合不能继续跟随链接的 AI 抓取器。
- 每个网页都有同路径 `.md` 版本，例如 `components/basic/hyper-button.md`，适合按组件摘取。
- HTML 页面会声明 canonical、Markdown alternate、sitemap 与允许完整摘要的 robots meta。

如果所用 AI 无法访问 GitHub Pages，可退回仓库 Markdown 源文件：

```text
https://gitee.com/my_new_way/hyper_ui/blob/master/vitepress/docs/index.md
```

消费方项目不需要复制整个 `vitepress/docs/`。建议只在消费方项目根目录的 `AGENTS.md` 中保留简短说明：

```markdown
## HyperUI

本项目使用 HyperUI。

添加或更新依赖前，请从以下任一线上入口读取最新可用 tag，不要在项目文档中保存固定版本号：
- https://jitpack.io/#auroracode553/hyper-ui
- https://github.com/auroracode553/hyper-ui/tags

AI 编写 HyperUI 代码前，请优先参考：
https://gitee.com/my_new_way/hyper_ui/blob/master/vitepress/docs/index.md

关键规则：
- HyperUI 公开 API 统一在 `hyper_ui` 包，可使用 `import hyper_ui.*`。
- 使用前包裹 `HyperThemeConfig`。
- 组件不持有业务状态，状态由调用方管理。
- HyperUI 已在库内部使用 `com.composables:icons-lucide-android:2.2.1` 提供默认语义图标；调用方若要直接引用 `LucideR.drawable`，仍应显式声明该依赖。
```

## 适用范围

HyperUI 面向 Android Compose 项目，提供按钮、输入框、列表分组、设置项、弹窗、抽屉、加载进度条、可拖动进度滑块、顶部栏、底部导航等基础组件。

不适合的场景：

- 传统 View XML 项目直接使用。
- 非 Android 目标直接依赖 AAR。
- 把业务状态、网络请求、数据库逻辑放进 UI 组件库。

## 接入方式

### 方式一：JitPack 依赖

先从 [JitPack](https://jitpack.io/#auroracode553/hyper-ui) 或 [GitHub Tags](https://github.com/auroracode553/hyper-ui/tags) 读取最新可用 tag。文档不记录固定版本号，避免依赖示例与实际发布状态不一致。

在调用方项目的 `settings.gradle.kts` 中添加 JitPack 仓库：

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

在调用方模块的 `build.gradle.kts` 中加入依赖，并将 `<latest-tag>` 替换为线上查询到的完整 tag：

```kotlin
dependencies {
    implementation("com.github.auroracode553:hyper-ui:<latest-tag>")
}
```

JitPack 坐标格式为 `com.github.<user>:<repo>:<tag>`；tag 是否带 `v` 前缀，以线上实际名称为准。

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
    implementation("com.github.auroracode553:hyper-ui:<latest-tag>")
}
```

### 方式三：AAR 文件

把发布得到的 `hyper_ui-release.aar` 放到调用方模块的 `libs/` 目录：

```kotlin
dependencies {
    implementation(files("libs/hyper_ui-release.aar"))
}
```

如果只使用裸 AAR，需要调用方自行补齐 Compose 基础依赖；推荐优先使用 Maven 方式，让 Gradle 读取 POM 中的依赖信息。

## 最小使用示例

```kotlin
import androidx.compose.runtime.Composable
import hyper_ui.*

@Composable
fun App() {
    HyperThemeConfig(themeColor = rgba(255, 103, 0)) {
        HyperButton(onClick = { /* 调用方处理业务逻辑 */ }) {
            HyperText("保存")
        }
    }
}
```

## 推荐图标方案（Android）

HyperUI 内置的勾选、充电、快进及播放速度面板语义图标统一使用 [Lucide Android](https://github.com/composablehorizons/compose-icons/tree/main/icons-lucide-android)；支持自定义的组件仍保留图标 slot。调用方若要在自己的代码中直接引用 Lucide 资源，也需要显式声明：

```kotlin
dependencies {
    implementation("com.composables:icons-lucide-android:2.2.1")
}
```

```kotlin
import androidx.compose.foundation.layout.size
import hyper_ui.HyperIcon
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR
import hyper_ui.*

HyperIconButton(onClick = onSearch) {
    HyperIcon(
        painter = painterResource(LucideR.drawable.lucide_ic_search),
        contentDescription = "搜索",
        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
    )
}
```

该 Android artifact 使用 `VectorDrawable` 资源。HyperUI 当前引用 `check`、`zap`、`fast-forward`、`gauge`、`x`、`rotate-ccw`、`move-horizontal` 与 `pencil`；Release 资源裁剪可移除未引用资源。项目不引入 `material-icons-extended`。

## 组件范围

- 主题与材质：`HyperThemeConfig`、`HyperTheme`、`HyperColors`、`HyperGlassTokens`、`HyperBackdrop`、`HyperSoftBackground`。
- 基础与表单：`HyperButton`、`HyperIconButton`、`HyperTextField`、`HyperSwitch`（清晰的关闭态中性轨道）、`HyperCheckbox`、`HyperRadio`、`HyperSegmented`、`HyperSlider`。
- 容器与列表：`HyperPanel`、`HyperColorPicker`、`HyperList`、`HyperSectionedList`、`HyperMenuList`、`HyperListItem`。
- 导航与操作：`HyperNavBar`、`HyperDrawer`、`HyperSlideMenu`（侧滑操作）、`HyperFilterBar`（横向分类）、`HyperTabBar`。
- 浮层与反馈：`HyperTooltip`、`HyperEmptyState`、`HyperPopup`、`HyperDialog`、`HyperAlertDialog`、`HyperUpdateDialog`、`HyperDropdown`、`HyperToast`（自适应宽度和语义色）、进度和播放速度组件。
- Android 系统工具：`hyperToast`、`HyperBatteryState`、`readHyperBatteryState`、`rememberHyperBatteryState`。它们不进入 Preview 的跨平台编译链，真实调用仅限 Android。

所有公开 API 位于 `hyper_ui` 包；`hyper_ui.core` 仅供库内部使用。详细签名和默认值以[组件索引](vitepress/docs/component-index.md)及各组件页为准。

## 状态管理原则

- 组件不持有业务状态。
- `value`、`checked`、`selected`、`visible`、`open`、`expanded` 等状态由调用方管理。
- 组件通过 `onValueChange`、`onCheckedChange`、`onClick`、`onDismissRequest` 等回调通知调用方。
- 外部尺寸和间距由 `modifier` 控制；按钮等组件提供与参考设计对应的语义化尺寸参数，内部独立布局节点提供具名修饰符。
- `HyperPopup` 是窗口级轻量 Popup；`HyperDialog` 使用 Compose Dialog 宿主，并通过 `usePlatformDefaultWidth = false` 关闭平台默认宽度。该参数不是动画开关；它配合全尺寸根节点固定 Window 首帧测量，面板在根节点内居中并保持 280–360dp 宽度边界。外部点击由根节点命中层处理，返回键继续由 `dismissOnBackPress` 控制。`HyperAlertDialog` 基于 `HyperDialog` 提供 body/action 结构并固定使用标准实色描边。
- 业务状态由调用方持有；组件可以在状态变化时执行按压、弹出、拖动吸附等视觉过渡。

示例：

```kotlin
var keyword by remember { mutableStateOf("") }

HyperTextField(
    value = keyword,
    onValueChange = { keyword = it },
    placeholderContent = { HyperText("搜索") },
    startContent = {
    HyperIcon(
            painter = painterResource(LucideR.drawable.lucide_ic_search),
            contentDescription = null
        )
    }
)
```

## 文档与交互预览架构

文档采用三层结构：

- `vitepress/docs/`：Markdown 权威内容，记录真实公开签名、参数默认值、状态归属、约束与示例，供 AI 和调用方阅读。
- `vitepress/`：将 `vitepress/docs/` 渲染为语义化静态网页，并通过 iframe 嵌入 Wasm 预览。
- `preview/`：用于文档验收的交互示例，不能作为 Desktop/Wasm 产品支持声明。

VitePress 的 `WasmPreview` iframe 使用 `embedded=1`，只绘制当前组件的交互示例；组件参数、变体说明和最小用法由 Markdown 组件页承载。独立打开 Preview 应用时仍可使用其目录导航。

调用方只依赖 `hyper_ui`，不依赖文档源码、`vitepress/` 或 `preview/`。AI 不应从 Wasm 画面推断 API，应读取 [vitepress/docs/index.md](vitepress/docs/index.md) 和具体组件页。

Preview 中可见的组件卡片必须与 `library/src/main/java/hyper_ui/components/` 下公开的可视化组件和 Android-only 组件工具保持一致：组件目录有的，preview 和文档要有；组件目录没有的，不作为组件卡片展示。`State`、`Defaults`、`Config`、枚举等辅助 API 不单独登记为组件卡片；主题色切换等文档外壳能力可以保留在 docs UI 中，但不登记为组件 demo。

目录结构：

```text
preview/
├── build.gradle.kts
├── settings.gradle.kts
└── src/
    ├── commonMain/kotlin/hyper_ui/docs/
    │   ├── DocsApp.kt       # 文档预览共享根节点
    │   ├── data/            # 组件注册与示例代码片段
    │   ├── theme/           # 文档主题
    │   └── ui/              # 文档布局与交互示例
    ├── desktopMain/kotlin/hyper_ui/docs/Main.kt
    │                       # 维护者预览入口
    └── wasmJsMain/
        ├── kotlin/hyper_ui/docs/Main.kt
        │                   # ComposeViewport 浏览器入口
        └── resources/index.html
                            # iframe 宿主页面
```

与正式库的关系：

- `preview` 的 `commonMain` 直接引用 `library/src/main/java/hyper_ui/` 中的平台无关 Compose 源码，供文档预览使用。
- 公开组件源码按功能组放在 `library/src/main/java/hyper_ui/components/` 下，但包名统一声明为 `hyper_ui`，方便调用方 `import hyper_ui.*`。
- UI 库内部公共工具放在 `library/src/main/java/hyper_ui/core/` 下，供组件实现复用，不作为调用方公开入口。
- Android-only 工具不能进入 `commonMain` 编译链；Preview 页面只展示说明、可交互模拟和 Android 调用片段。
- 调用方接入时不需要依赖 `preview` 模块。
- Wasm 入口接受 `#组件-id`，例如 `index.html?embedded=1#button`，供 VitePress 组件页选择纯组件预览；未知 ID 回退到第一个组件。

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
├── .vitepress/ai-docs.mts                # 构建期派生 AI 文档入口与抓取元数据
└── public/
    ├── llms.txt                          # 开发期 AI 索引；生产构建会自动补全
    └── wasm-preview/README.md            # Wasm 产物放置说明
```

生产构建会从 `vitepress/docs/` 自动派生 `llms.txt`、`llms-full.txt`、`sitemap.xml`、`robots.txt` 和每篇文档的 `.md` 静态直链；这些都是构建产物，不维护第二份组件正文。

Wasm 静态产物不提交到仓库。使用者手动执行 `preview/` 中的 `publishWasmToVitePress` 后，完整产物会复制到 `vitepress/public/wasm-preview/`，并写入 `preview-ready.json`。`dev:watch` 额外写入本地构建阶段；首次产物未就绪时页面显示依赖准备或编译进度，不加载 404 iframe。

开发期需要在 VitePress 组件页实时查看 Kotlin 修改时，可手动启动 `preview/` 的 `wasmJsBrowserDevelopmentRun`，再把终端打印的地址通过 `VITE_HYPER_UI_PREVIEW_DEV_URL` 传给 VitePress。此时文档 iframe 直接嵌入开发服务器，源码保存后的重编译与刷新由它处理；具体步骤见 [预览更新流程](vitepress/docs/preview-update-workflow.md)。

日常开发也可在 `vitepress/` 手动执行 `npm run dev:watch`：一个命令启动文档，先更新 Kotlin/Wasm npm 锁文件、再后台构建预览，并在 Kotlin 源码或 Wasm 入口资源保存后串行重新发布；预览区域显示四个真实阶段（依赖准备、编译发布、资源加载、组件渲染），产物就绪后自动加载和刷新。阶段条不代表 Gradle 百分比。

若首次构建因访问 GitHub Release 下载 Binaryen 超时，可按 [预览更新流程](vitepress/docs/preview-update-workflow.md#github-连接超时时使用本地-binaryen-压缩包) 设置可选的 `HYPER_UI_BINARYEN_ARCHIVE_DIR`，使用已手动取得并校验的官方压缩包。变量未设置时仍由 Gradle 下载。

站点默认部署在域名根路径 `/`。部署到仓库子路径时，在手动启动或构建前设置 `VITEPRESS_BASE`，值必须以 `/` 开头和结尾，例如 `/hyper_ui/`；`WasmPreview` 会使用同一个 base 生成 iframe 地址。

依赖清单：

- Preview Web：`org.jetbrains.kotlinx:kotlinx-browser:0.3`（读取浏览器 hash）
- Node.js 18 或更高版本
- VitePress 1.6.4
- Vue 3.5.x

## 文档入口

- [vitepress/docs/index.md](vitepress/docs/index.md)：调用方和 AI 的首读入口。
- [vitepress/docs/component-index.md](vitepress/docs/component-index.md)：按分组进入每个组件的精确 API 文档。
- [vitepress/docs/mobile-guidelines.md](vitepress/docs/mobile-guidelines.md)：Android 手机端布局与交互约束。
- [vitepress/docs/preview.md](vitepress/docs/preview.md)：文档交互预览的职责边界。
- `preview/`：仅供文档验收的 Desktop/Wasm 交互预览工程。
- `vitepress/`：语义化静态文档站配置。
- [vitepress/docs/maintenance.md](vitepress/docs/maintenance.md)：本地开发运行与文档维护规则。

## 本地文档预览

项目约束禁止在此仓库执行代码打包、部署或发布。只阅读或编辑 Markdown 时，进入 `vitepress/` 手动启动文档开发服务器即可：

```powershell
npm run dev
```

组件交互预览的职责、平台边界和维护方式见[交互预览](vitepress/docs/preview.md)与[文档维护规则](vitepress/docs/maintenance.md)。
