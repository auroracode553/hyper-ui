## 全面屏与悬浮栏约束

- Activity 使用 `enableHyperEdgeToEdge()`，根 `HyperThemeConfig.useSystemSafeArea = false`。根画布、滚动视口和 WebView 保持全屏，正文可滚入顶部状态栏与底部 TabBar 后方。
- 禁止用固定高度、`Spacer`、根布局系统栏 padding、滚动容器外的栏高 padding、扣减屏幕高度或裁切，为状态栏、导航栏、悬浮 TabBar 建立永久占位；禁止把悬浮栏放进 `Column` 挤占正文。不得把 `HyperScaffold` 返回的上下净空直接用于滚动视口的 `Modifier.padding`。
- 顶部操作区由 `HyperNavBar` 或局部操作容器根据真实窗口 Insets 保护一次。首屏顶部净空与末尾底部净空只进入 `LazyColumn` / `LazyGrid` 的 `contentPadding` 或 `verticalScroll(...).padding(...)` 内部，随正文滚动；不能在视口外固定留空。
- 悬浮 TabBar 使用覆盖布局，周围透明并透传正文手势。需要末尾避让时使用宿主实测遮挡量，禁止用默认栏高、硬编码 dp 或状态栏高度估算永久占位。Web 宿主传入的安全区与遮挡量同样只用于网页滚动内容及局部操作区，不能缩短网页滚动视口。
- 保留按钮、导航控件、输入法面板、视频画面和编辑器等业务组件自身所需尺寸与 IME 避让；本限制针对系统栏与悬浮栏占位，不要求删除正常控件高度。修改后从布局与滚动链路核对，不能只凭高度值未变或编译成功判定正常。

# AGENTS.md

## UI 库维护与业务接入必读

- 开始维护组件或开发页面前，优先阅读[应用接入与页面开发规则](vitepress/docs/mobile-guidelines.md)，再读[设计系统基线](vitepress/docs/style-reference.md)、组件 API 和真实 Preview。业务项目必须把使用模板合并到自身 AGENTS.md，依赖库的规则不会自动生效。
- 应用在所有 Activity 的公共入口全局 enableEdgeToEdge，主题宿主统一透明状态栏、透明手势导航栏及亮暗图标。画布使用 pageBackground 覆盖系统栏，不逐页另画色块。
- 禁止整页 systemBarsPadding/navigationBarsPadding 或固定安全区占位截短正文。必要控件或滚动末尾只避让一次；键盘继续通过页面宿主正常避让。
- HyperNavBar 保留库内滚动渐变和渐显/渐隐；材质范围由库内部限定在状态栏顶部至导航底部，不向正文越界绘制。页面使用 HyperNavBarPage(scrollState = 主滚动状态) 叠放导航与滚动视口，库内 HyperNavBarScrollScope 自动监听 ScrollState/LazyListState/LazyGridState，不在页面另写监听或 scrolledUnder 判断，顶部净空属于滚动内容，禁止实色底板或业务裁切替代材质。
- HyperCard、HyperMenuGroup、独立 HyperListTile 和 HyperSlideMenu 共用内部 hyperCardSurface/ContentCard 深度角色：普通无边框、画布与卡片分层、统一柔和双层阴影。选中态保留强调边界，组内分隔线保留；浮层、输入和控件不套用内容卡片样式。
- TabBar 保持原有悬浮布局，正文可绘制和滚动到后方，两侧响应正文手势。只有用户明确要求修改底栏布局，才能改变高度、占位、位置、边距、安全区、正文底部留白及滚动范围；配色、阴影或重构不构成授权。不加全宽不透明底板、重复安全区或固定底部占位。
- 修改共用导航和表面时，交付前核对系统栏、正文视口、末尾余量、键盘和底栏实际布局；不得只凭 height 没变或编译通过断言界面一致。

## 修改 UI 前必读

- 修改 `library/src/main/java/hyper_ui/` 或 `preview/src/` 前，必须阅读 [UI 样式与功能实现基线](vitepress/docs/style-reference.md)，并核对目标源码、对应组件页和真实 Preview 示例。
- 默认保留已有样式、尺寸、材质、状态展示、点击区域和回调时机。不得以“统一样式”“清理自定义”“复用基础组件”或“与另一端对齐”为由，批量替换控件、删除选中态或改变专用布局。
- 按用户明确授权的范围修改；拿不准用途、可能影响外观或交互的地方，先列出位置、现状、修改影响和建议，找用户确认后再改，不自行猜测。参考项目的实现不是改动授权。
- 调整获授权的样式或功能时，同步维护基线、组件文档和必要的两端预览；不得先擅自改实现，再通过改文档认可变化。源码与基线有冲突时先查明原因，不直接用其中一方覆盖另一方。
- 文档中记录的专用样式和平台差异应当保留；Flutter 中存在但 Android 中尚未实现的组件，不得仅为补齐基线自动新增。

## 代码架构规范

- 遵循单一职责原则，组件、工具、配置、业务逻辑分层隔离。
- 展示组件禁止持有业务状态、网络请求、数据库访问或权限申请。`tools/update/` 按用户授权集中维护通用静态索引检查、系统下载与安装入口；项目仓库、发布信息和时机配置由调用方注入，不在组件中发起更新请求。
- 禁止全局变量滥用；组件依赖通过参数、回调或接口注入。
- 公共通用逻辑抽离至独立工具文件，不重复粘贴。
- 涉及重构、组件 API 调整或架构修正时，优先采用成熟、清晰的破坏性重构方案；不要为了兼容旧调用而堆叠废弃别名、兼容壳或双轨实现。完成后必须同步更新所有调用点、文档说明与必要示例，保持组件库只有一套明确方案。

## 文件规模限制

- 单个源码文件有效代码行数不超过 1000 行。
- 临近上限时必须主动拆分文件，并说明文件用途与引入关系。
- 不默认扫描 `dist` 和 `node_modules` 目录。

## 文件清理规则

- 禁止新增版权、许可、授权等声明文件，也不要在 README、组件文档或源码注释中追加这类声明。
- 禁止创建与当前功能、构建或必要文档无关的文件；优先复用现有文件，不额外生成过程报告、备份或一次性脚本。
- 验证截图、日志等临时产物放在系统临时目录，任务完成后清理，不保存在项目目录。
- 修改完成后清理重复、废弃和未使用的文件，同时删除失效引用。

## 平台范围

- 当前项目的产品目标是 Android 手机端 Compose UI，优先覆盖 320dp 至 430dp 的竖屏宽度和系统安全区。
- 不为平板、桌面、横屏或多栏桌面布局增加专用 API；需要更宽空间时由调用方通过 `Modifier` 组合布局。
- `preview/` 的 Desktop/Wasm 目标仅用于组件文档和交互验收，不代表公开 API 支持 Desktop、Wasm 或其他消费平台。
- 文档必须明确区分“Android 手机端支持范围”和“Preview 运行方式”，不要把预览目标写成产品平台。

## 颜色使用规范

- 禁止使用 `Color(0xFFRRGGBB)` 十六进制硬编码颜色。
- 必须使用 RGBA 分量格式：`Color(red, green, blue, alpha)`，四个参数均为 `Float`，范围 `0f..1f`。
- 示例：`Color(1f, 0f, 0f, 1f)` 表示红色，`Color(0.2f, 0.6f, 1f, 1f)` 表示蓝色。

## 组件与 Preview 规范

- 对应组件预览以 `D:/my_project/flutter_project/flutter-hyper-ui` 为视觉基准；手机外框、背景、安全区、分组顺序、文案、图标、尺寸、间距与展示状态保持一致，不添加仅 Compose 端存在的标题、控制项或计数。

- 本项目与 `D:/my_project/flutter_project/flutter-hyper-ui` 的对应组件保持默认 UI 一致。修改默认布局、尺寸、按钮类型或状态展示规则时，同时更新另一端的实现、文档与预览示例；框架 API 差异不能导致默认视觉规则分叉。

- UI 库中所有配置、组件、工具方法和可见功能，都必须在 `preview/` 中呈现。
- 新增或修改组件时，必须同步更新 `preview/src/commonMain/kotlin/hyper_ui/docs/data/ComponentDemos.kt` 聚合入口和对应分组的 `*ComponentDemos.kt`。
- 新增或修改交互效果时，必须同步更新 `preview/src/commonMain/kotlin/hyper_ui/docs/ui/` 下对应分组的 `*ComponentShowcases.kt` 可交互示例。
- Preview 示例必须允许用户操作查看状态变化、变体、禁用态、方向、颜色、进度或反馈结果。
- Android-only 工具方法不能直接引入 Desktop preview 编译链；应在 preview 中提供可交互模拟和 Android 端调用示例代码。
- 文档代码片段应保持简洁，展示最小可用调用方式。

## 文档同步

- 新增或修改组件、配置、枚举、工具方法后，必须同步更新 `README.md`、`vitepress/docs/component-index.md` 和 `vitepress/docs/components/` 下对应组件页。
- `vitepress/docs/index.md` 是 AI 与调用方的首读入口；具体组件页必须包含包名、真实公开签名、参数默认值、状态归属、使用约束和最小调用方式。
- 每个 VitePress 组件页必须保留完整的 API/Props 参数表格，列出参数、类型、是否必填、默认值和作用；不得在重构或精简文档时删除表格。修改公开 API 时同步更新表格并核对源码；表格单元格允许长内容换行，内容不用反引号包裹。
- VitePress 负责渲染 `vitepress/docs/` 中的 Markdown；不得维护第二份组件正文。
- Wasm 只负责交互预览，不能作为 AI 推断 API 的事实来源。新增组件页时应通过 `WasmPreview` 引用对应 Preview ID。
- HyperUI 公开 API 统一声明在 `hyper_ui` 包，示例可使用 `import hyper_ui.*`；`hyper_ui.core` 仅为 UI 库内部公共工具目录，调用方示例不要直接导入。
- 通用图标直接引用 Lucide 图标库，HyperIcon 仅负责显示尺寸和颜色继承。禁止在项目内手写或复制 SVG／字体轮廓、ImageVector.Builder 图标路径，以及另建图标别名转接表；正式组件与 Preview 使用同一套库图标。

## 两端 API 一致性

- 对应组件的业务参数、状态枚举、回调名称和默认语义必须保持一致；仅保留框架布局、宿主及原生类型差异。
- 点击统一 onPressed，值变化统一 onChanged，关闭统一 onClose；页签采用 selectedIndex，单选采用 value/groupValue，插槽使用 title/subtitle/leading/trailing/actions/footer/child。
- HyperButton 的主体插槽两端统一使用 content；不要改为 child，也不保留两套参数或别名。
- type 表示形态；按钮预设颜色使用 colorStyle，自定义颜色使用 color，禁止恢复 ghost、outline 或把颜色写回按钮 type。
- API 重构直接删除旧参数、旧类型和旧名称，更新所有调用方；禁止废弃别名、兼容入口和双轨实现。
- 文档仅描述当前 API、默认值和使用方式，不保留迁移对照、旧写法或历史重构说明；同步两端实现、预览、README 和完整参数表。

导航滚动监听统一由 HyperNavBarPage.scrollState / HyperNavBarScrollScope 管理，Navbar 自动读取；消费页面不得重复声明导航专用 derivedStateOf、snapshotFlow 或 scrolledUnder 判断。库测量实际导航区高度，顶部净空放进滚动内容；主滚动状态保留在容器，嵌套列表不代替主状态。

作用域提供稳定派生 State，只由 Navbar 读取值，避免阈值切换带动整个正文重组。普通 HyperScaffold 或固定布局通过 HyperNavBarScrollScope 绑定同一主状态，不改原布局；active 用于隐藏页或空主视口，不修改滚动位置。不得用页面布尔缓存、手势累计、弹窗列表或播放器位置替代主状态；业务监听按职责保留。用户明确要求仅改源码、由本人测试时，不自动执行静态分析、构建、安装、运行或测试，不生成测试与辅助脚本。

## 组件规格与分类筛选

- 每类组件只提供一套默认规格，禁止新增或恢复 small/default/large 等尺寸档位及兼容参数。文字、图标继续继承宿主样式，必要的业务布局与字体缩放独立保留。
- HyperTabs 用于固定分段切换，使用 tabs / selectedIndex / onChanged；可左右滑动的分类筛选使用紧凑 HyperFilterBar，分类项保持浅底细线、淡主题色选中态，不添加阴影和厚重描边。禁止恢复整条 54dp 分类胶囊或旧分段组件名称，不保留废弃别名与兼容壳。

导航形态统一使用 HyperNavBar.type = standard / simple；standard 的内容布局通过 layout 选择。两种形态只保留滚动渐变，不录制或采样正文、不做像素模糊。simple 不生成操作行，仅读取真实顶部系统 Insets，禁止用固定栏高模拟。
