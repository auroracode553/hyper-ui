# HyperUI

维护组件前必读 [UI 样式与功能实现基线](vitepress/docs/style-reference.md)：记录 Android 当前材质、尺寸、功能、状态及专用样式保留清单。已有样式默认保持不动，用途不明或可能影响交互的变化先向使用者确认，禁止批量替换。

通用内嵌月历参见 [HyperCalendarMonth](vitepress/docs/components/form/hyper-calendar-month.md)；日期格和附属条目参见 [HyperCalendarDay](vitepress/docs/components/form/hyper-calendar-day.md)。与日期选择器共用原有圆形选中及今日描边，业务历法与日程留在调用方。

日期格条目保持完整字高与紧凑留白；默认缩放下 large 预留三行的最小高度为 96dp，宿主通过 HyperCalendarDayDefaults.minimumHeight 计算。

emphasizedDays 使用既有主题色强调指定日期；条目最多三行，溢出数量单独占最后一行，可用 minimumHeight 的 itemRows 为条目预留高度。

[![在线文档](https://img.shields.io/badge/%F0%9F%93%96-在线文档-4FC08D?style=for-the-badge&logo=vuedotjs&logoColor=white)](https://auroracode553.github.io/hyper-ui)

> 面向 Android 手机竖屏的 Jetpack Compose UI 组件库，提供 HyperOS 风格的受控界面组件与独立 Android 工具；应用业务留在调用方。

**源码仓库**: `git@gitee.com:my_new_way/hyper_ui.git`

**在线文档**: [auroracode553.github.io/hyper-ui](https://auroracode553.github.io/hyper-ui)

## 两端统一 API

Compose 与 Flutter 对应组件使用相同的业务参数名称和默认语义。两端共同维护：

| 用途 | 统一 API |
| --- | --- |
| 点击操作 | onPressed |
| 表单与选择 | value / onChanged；单选为 value / groupValue / onChanged |
| 页签与底部导航 | selectedIndex / onChanged，索引从 0 开始 |
| 分段选择 | options / selectedValue / onChanged |
| 滑块 | min = 0、max = 100、divisions = null；onChangeStart / onChangeEnd 回传数值 |
| 文本输入 | placeholder、prefix、suffix、maxLength、enabled、readOnly |
| 标题与插槽 | title、subtitle、leading、trailing、actions、footer、child |
| 按钮外观 | type 控制形态，colorStyle 控制预设配色，color 覆盖预设 |
| 更新弹窗 | status、currentVersion、release、downloadProgress、progress、message 与 onCheck / onDownload / onInstall / onRetry / onCancel / onLater / onClose |

业务参数和回调保持同名。框架布局与宿主参数使用原生类型：Compose 的 Modifier、Composable、Dp、Float，与 Flutter 的 key、Widget、double、Duration；Flutter 的 controller 和 Navigator 弹层宿主、Compose 的受控 visible/open 与内容 DSL 按框架保留。组件特有参数以源码签名和完整参数表为准。

默认按钮省略 type 和 colorStyle，显示白色描边；显式 type 仅 filled、icon，colorStyle 为 primary、success、info、warning、danger。

## Flutter 参考设计与 Compose API

`HyperDatePicker.date` 对齐 `D:/my_project/flutter_project/flutter-hyper-ui/ui/lib/src/components/hyper_date_picker.dart` 的单日期模式和底部面板：30dp 圆角、16dp 内边距、42dp 日期行、36dp 选中圆、12 个月及年份跳转。公开日期类型为 Android 原生 `LocalDate`；默认范围为 1900-01-01 至 2100-12-31，临时选择在确定并关闭面板后提交。无新增依赖。完整签名、状态约定与交互示例见 [日期选择器](vitepress/docs/components/form/hyper-date-picker.md)。

默认视觉与 `D:/my_project/flutter_project/flutter-hyper-ui/ui` 的同名组件对齐。默认材质为 `HyperMaterial.Solid`，同时提供 `Soft`、`Clear`；已有颜色、字号、行高、圆角、间距、图标和动效按当前基线维护，不因参考差异自动重写。按钮高度 32/38/44dp，输入框圆角 20dp，分段控件 40dp，Tabs 54dp，TabBar 56dp。完整规则见 [样式与功能实现基线](vitepress/docs/style-reference.md)。

`HyperDropdown` 使用 options/value/onChanged 管理选择；width 省略时按文案自动取 160–240dp，菜单匹配字段宽度。`HyperPopover` 的 visible / onClose 由调用方控制，child 提供锚点并自行触发；默认宽 180dp，width 可显式指定，maxWidth 只限制上限。`HyperTabBar.type` 为 single/multiple，不提供整体或单项禁用状态；`HyperCheckbox.value` 为可空三态值。更新弹窗通过 status、展示数据与操作回调接入业务服务。

`HyperCheckbox`、`HyperRadio`、`HyperSwitch` 的 label 使用 plain 行，不自动附带卡片背景、外框或阴影；基础预览直接展示控件与标签。

`HyperNavBar` 的滚动背景覆盖状态栏，只有操作行避让顶部安全区。真机读取系统 Insets；文档预览注入模拟 Insets，导航与正文共用且只计算一次，与 Flutter 的背景及安全区层级一致。

导航的滚动感知统一由库内 HyperNavBarScrollScope 处理：覆盖式页面传入 HyperNavBarPage.scrollState，已有HyperScaffold 只包装作用域并绑定同一个主状态，不重排布局。作用域传递稳定派生 State，仅 Navbar 读取；active=false 可停用隐藏页或空视口，保留滚动位置。页面不维护导航专用监听、布尔缓存或回调链，不因迁移改变 TabBar 或滚动范围。完整接口见 [导航组件](vitepress/docs/components/navigation/hyper-nav-bar.md)。

共有组件约定：按钮主体使用 content 插槽；Checkbox 标签行不叠加独立触摸区；Card 选中边框使用完整强调色；日期范围使用亮暗语义底色；ListTile 尾部间距 8dp；Tabs 仅在索引改变时回调；SlideMenu 携带释放速度吸附；Toast 默认文字操作先请求关闭再回调。

```kotlin
HyperThemeConfig {
    HyperBackdrop() {
        HyperButton(onPressed = onSave, colorStyle = "primary", type = "filled") {
            HyperText("保存")
        }
    }
}
```

完整签名、逐项 Props 表格、状态归属和交互预览见 [组件索引](vitepress/docs/component-index.md)。

组件文档统一按用途、交互示例、最小调用、使用约定与公开 API 组织；长签名和辅助配置可展开查看，完整五列 Props 表始终保留。手机 Preview 提供 320/350/390/430dp 宽度与重置。页面级列表占满剩余视口并自行滚动，普通组件由宿主滚动；布局规则见 [交互预览](vitepress/docs/preview.md)。
手机 Preview 的底部手势条悬浮覆盖在内容上，宿主不预留底部安全区，与 Flutter 参考预览一致。
HyperListTile 预览直接展示条目和状态，不额外套 HyperCard；弹窗中的只读详情使用 HyperListTile(type = "plain") 的标签与说明槽，不逐项包装卡片。
HyperRadio 的标签行由整行处理点击，默认最小高度 46dp；放进列表行只展示时省略 onChanged，圆环占 21dp，不额外撑高行。无标签独立交互控件保留 44dp 触摸区。

## 先看结论

- 调用方可以通过 Maven 坐标、源码模块或 AAR 引入 HyperUI。
- 当前项目使用 AGP 9.4.0，Kotlin Android 支持由 AGP 内置，不再额外应用 `org.jetbrains.kotlin.android` 插件。
- 当前库的 `minSdk` 为 `30`，调用方应用的 `minSdk` 不能低于 30。
- HyperUI 和 Preview 直接引用 Lucide Compose 库图标，不在项目中复制路径；调用方可通过 slot 提供自己的图标。
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
- HyperUI 与 Preview 直接引用 Lucide Compose 图标库，不在项目中维护字体轮廓或 SVG 路径；业务代码直接使用 Lucide 时显式声明同一依赖。
```

## 适用范围

HyperUI 面向 Android Compose 项目，提供按钮、输入框、列表分组、设置项、弹窗、抽屉、加载进度条、可拖动进度滑块、顶部栏、底部导航等基础组件。

`HyperDrawer` 是贴边全高的独立模态层；调用方持有 open 并响应 onClose，正文通过 content 提供，默认零内边距。默认从 start 侧展开，支持遮罩、系统返回键及横向滑动关闭，固定标题与页脚；完整 API 见[抽屉文档](vitepress/docs/components/navigation/hyper-drawer.md)。

底部菜单使用 [HyperActionSheet](vitepress/docs/components/feedback/hyper-action-sheet.md)，支持自定义正文或 HyperAction 操作列表；不使用侧边 HyperDrawer 承载底部菜单。

不适合的场景：

- 传统 View XML 项目直接使用。
- 非 Android 目标直接依赖 AAR。
- 把项目业务状态、资源格式、数据库逻辑或专属网络协议放入展示组件。APK 更新由独立 tools/update 模块按公开配置执行。

## 工具：检查更新

[`tools/update/`](library/src/main/java/hyper_ui/tools/update/) 集中提供 APK 静态索引检查、系统下载、真实进度、任务恢复与系统安装，公开入口为 `rememberHyperUpdateController`、`HyperUpdateController` 和 `HyperUpdateHost`。只读取项目 `publish-config.json` 中的 repository 与 update 公开字段；构建约定位于 `gradle/hyper-update.gradle`，发布凭据不进入客户端。

默认自动检查额度由库统一设为每小时窗口最多 2 次，应用不必传入；冷启动约 5 秒、恢复前台约 2 秒后只判断是否有额度，失败也计次。手动检查不占自动额度并复用在途任务；特殊需求可选配置覆盖。

确认本应用已安装到下载目标版本或更高版本后，工具自动删除对应系统下载任务和 APK。系统安装完成通知与下次启动共用清理入口；取消或失败保留安装包，清理失败保留 ID 供后续重试，只处理本工具记录的文件。

完整配置、API、边界和交互预览见[工具 → 检查更新](vitepress/docs/components/tools/hyper-update.md)。本工具只处理 APK，既不定义资源 channels，也不维护词库、模型、Web 资源的地址、格式、校验或启用流程；这些业务保持在各项目。

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
        HyperButton(onPressed = { /* 调用方处理业务逻辑 */ }) {
            HyperText("保存")
        }
    }
}
```

## 图标方案

HyperUI 内置图标与 Preview 直接使用 [Lucide Compose](https://github.com/composablehorizons/compose-icons/tree/main/icons-lucide-cmp)，保留组件原有图标插槽。调用方直接使用 Lucide 图标时声明：

```kotlin
dependencies {
    implementation("com.composables:icons-lucide-cmp:2.2.1")
}
```

```kotlin
import androidx.compose.foundation.layout.size
import hyper_ui.HyperIcon
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import hyper_ui.*

HyperButton(type = "icon", onPressed = onSearch) {
    HyperIcon(
        icon = Lucide.Search,
        contentDescription = "搜索",
        modifier = Modifier.size(HyperButtonDefaults.IconSize)
    )
}
```

HyperIcon 继续负责插槽尺寸与颜色继承，图形由图标库提供；不复制字体轮廓、SVG 路径或添加图标别名表。

## 组件范围

- 主题与材质：`HyperThemeConfig`、`HyperTheme`、`HyperColors`、`HyperGlassTokens`、`HyperBackdrop`、`HyperSoftBackground`。
- 基础与表单：`HyperText`、`HyperIcon`、`HyperDivider`、`HyperButton`（含 `type = "icon"`）、`HyperTextField`、`HyperSwitch`（清晰的关闭态中性轨道）、`HyperCheckbox`、`HyperRadio`、`HyperSegmentedControl`、`HyperSlider`。
- 容器与列表：`HyperCard`、`HyperColorPicker`、`HyperList`、`HyperSectionedList`、`HyperMenuGroup`、`HyperListTile`。
- 导航与操作：`HyperNavBar`（顶部透明；宿主通过 `scrollState` 自动监听正文位置，滚入时用 160ms 过渡显示 20dp 毛玻璃，在状态栏顶部至导航底部内渐隐至透明，没有分隔线，固定顶部状态栏避让；通过 `type = custom/backOnly/titleOnly/backWithTitle/more/edit` 选择固定布局，`custom` 提供完整插槽；搭配 `HyperNavBarPage` 让正文滚到导航栏后方并提供背景采样源）、`HyperDrawer`、`HyperSlideMenu`（侧滑操作）、`HyperTabs`（横向分类）、`HyperTabBar`（文档页分别提供单动作与多标签交互预览）。
- 浮层与反馈：`HyperTooltip`、`HyperEmptyState`、`HyperPopover`、`HyperDialog`、`HyperAlertDialog`、`HyperUpdateDialog`、`HyperDropdown`、`HyperToast`（自适应宽度和语义色）、进度和播放速度组件。

`HyperSectionedList` 固定使用 8dp 紧凑分段间距，选择状态由调用方管理。

`HyperUpdateDialog` 的错误状态采用简洁布局，展示错误原因、重试与关闭。
更新弹窗的关闭、取消和稍后再说省略 `type`，使用 `HyperButton` 默认描边样式；主操作显式使用 `type = "filled"` 和 `colorStyle = "primary"`。
更新弹窗各状态标识块统一为 40dp、图标 20dp、环形进度 30dp；检查中隐藏当前版本，其他状态的版本信息直接排版，不嵌套版本卡片。
更新面板以 Flutter `HyperUpdateDialog` 源码为基准：最大宽 420dp、弹层安全区外间距 20dp、圆角 28dp、内容边距 24dp，正文滚动且底部按钮固定。文档与 Preview 共用组件面板，默认展示相同的“更新流程 / 状态预览”、2.7.3 → 2.8.0、2026.10.10、48.6 MB 和三条更新日志；状态标签与强制更新、总大小未知开关一致，关闭弹层后模拟下载继续。
按钮配色与显示形态独立：`type` 仅支持 `filled`、`icon`，省略时默认白色描边；`colorStyle` 支持 `primary`、`success`、`info`、`warning`、`danger`，自定义 `color` 优先于预设配色。按钮预览以 Flutter ButtonsExample 为基准，分组、文案、图标和 8/16dp 间距一致，保留单个禁用文字示例；手机外框、白色背景和 52dp 顶部留白同步，不添加额外状态栏、分组标题、开关或点击计数。库内组件和 Preview 均使用这些标准类型，并与 `D:/my_project/flutter_project/doc_ui_kit` 同步维护。

Preview 使用合并 Roboto 与 Noto Sans SC 的独立字体，避免 Wasm 中文回退失效并保持 Flutter 的拉丁字符宽度。生成工具位于 `preview/tools/generate-preview-font.py`，仅重新生成字体时需要 fontTools；正常构建直接使用已保存的字体，不需要 Python 或新依赖。Android 与 Preview 的按钮阴影分别由 `HyperButtonShadowRadius.kt` 与 `PreviewButtonShadowRadius.kt` 换算后端单位，公开按钮 API 不变。
- Android 系统工具：`hyperToast`、`HyperBatteryState`、`readHyperBatteryState`、`rememberHyperBatteryState`。它们不进入 Preview 的跨平台编译链，真实调用仅限 Android。

所有公开 API 位于 `hyper_ui` 包；`hyper_ui.core` 仅供库内部使用。详细签名和默认值以[组件索引](vitepress/docs/component-index.md)及各组件页为准。

## 状态管理原则

- 组件不持有业务状态。
- `value`、`groupValue`、`selectedIndex`、`visible`、`open` 等业务状态由调用方管理；Dropdown 内部维护临时展开状态。
- 组件通过 `onChanged`、`onPressed`、`onClose` 等回调通知调用方。
- 外部尺寸和间距由 `modifier` 控制；按钮等组件提供与参考设计对应的语义化尺寸参数，内部独立布局节点提供具名修饰符。
- `HyperPopover` 为锚定触发点的内容浮层，默认宽 180dp，maxWidth = 280dp 仅限制上限；`width = 240.dp` 可为复杂内容保留更多空间。scrollable 默认 true，内容自带惰性列表时设为 false，保留原高度和安全区约束。`HyperDropdown` 以最长选项或占位文字加 68dp 控件占位，默认限制在 160–240dp；宽表单用 width 显式设置，两端预览统一覆盖默认、220dp、长文案、禁用与空选项。
- `HyperDialog` 使用 Compose Dialog 的全尺寸根节点，面板宽度上限为 min(maxWidth, 实际窗口宽度 − 48dp)，两侧各保留 24dp；滚动正文与固定操作区分开，关闭由调用方维护 visible。
- 业务状态由调用方持有；组件可以在状态变化时执行按压、弹出、拖动吸附等视觉过渡。

示例：

```kotlin
var keyword by remember { mutableStateOf("") }

HyperTextField(
    value = keyword,
    onChanged = { keyword = it },
    placeholder = { HyperText("搜索") },
    prefix = {
    HyperIcon(
            icon = Lucide.Search,
            contentDescription = null
        )
    }
)
```

HyperTabBar 默认保留悬浮样式；`floating = false` 使用库内全宽非悬浮底栏，默认内容高 56dp，默认不增加底部系统安全区。浏览器类动作入口传 `selectedIndex = null`，允许重复点击并禁用横向拖动，通过 `itemEnabled` 控制各项可用性；参见 [TabBar API](vitepress/docs/components/navigation/hyper-tab-bar.md)。页面布局由 [HyperScaffold](vitepress/docs/components/navigation/hyper-scaffold.md) 统一承载，不需要 Material 页面宿主。

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

`ComponentDemos.kt` 只保留聚合入口和 `ComponentDemo` 数据结构；具体组件文档项按 `基础组件`、`表单组件`、`容器组件`、`导航组件`、`列表组件`、`反馈组件`、`工具` 拆在对应的 `*ComponentDemos.kt` 文件中。新增、删除或重命名公开组件时，必须同步更新同一分组的 data 文件、`*ComponentShowcases.kt`、`vitepress/docs/components/` 对应页面、[组件索引](vitepress/docs/component-index.md)、VitePress 侧栏和 README。

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

维护 UI 库和开发业务页面前，优先阅读[使用规则、全局全面屏与 AGENTS.md 模板](vitepress/docs/mobile-guidelines.md)和[设计系统基线](vitepress/docs/style-reference.md)。正文卡片统一无普通描边和双层柔阴影，选中轮廓保留；导航材质仅在状态栏顶部至 Navbar 底部内渐隐。既有悬浮 TabBar 的高度、占位与滚动范围不能因配色或卡片优化改变。

导航滚动监听统一由 HyperNavBarPage.scrollState / HyperNavBarScrollScope 管理，Navbar 自动读取；消费页面不得重复声明导航专用 derivedStateOf、snapshotFlow 或 scrolledUnder 判断。库测量实际导航区高度，顶部净空放进滚动内容；主滚动状态保留在容器，嵌套列表不代替主状态。

## Android 构建性能

`library/gradle.properties` 与消费应用使用相同的本地并行和缓存策略：最多 16 个 Gradle worker，独立任务按依赖图并行，开启守护进程、增量编译与构建缓存。Gradle 和 Kotlin 的堆上限分别为 4GB、3GB，针对当前 16 线程、约 32GB 内存的构建机配置；可按其他构建机资源覆盖。

复合构建引用同一份 library 输出时，多个应用逐个构建，库任务在输入不变时复用产物；不并发启动多个工程争用同一输出目录。性能配置不跳过正式包签名、混淆或校验。
# 统一全面屏入口

所有 Android Activity 在设置内容前调用 enableHyperEdgeToEdge()；根 HyperThemeConfig.useSystemSafeArea 默认 false。组件通过 HyperTheme.systemSafeAreaInsets 共用零系统安全区，IME 独立避让。API 与 Preview 见 [全面屏窗口](vitepress/docs/components/tools/hyper-edge-to-edge.md)。


全面屏顶部布局：HyperNavBar 自动保护顶部操作区，自定义固定顶栏使用 Compose 窗口布局；HyperNavBarPage 的正文净空必须放进滚动内容。

TabBar 尺寸由库统一管理：默认内容高度 56dp，悬浮多项默认顶部视觉间距 8dp、底部视觉间距 20dp，字体扩展仅在组件内处理。业务页面不覆写高度；并排入口在共同 Row 中布局，正文采用宿主实际测量的底部净空。

按需安全区统一使用 HyperSafeArea（Top / Bottom）。根画布继续全面屏；竖屏底部取导航区／缺口与库内最小 20dp 的最大值，横屏只保留实际系统区，同侧嵌套只消费一次。输入法只扩展自身面板底部，不处理宿主 IME Insets；页面不得自行复制安全区高度。
