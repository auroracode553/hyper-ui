# Android 手机端规范

## 全局全面屏约定

所有应用 Activity 统一调用 enableHyperEdgeToEdge()，根 HyperThemeConfig.useSystemSafeArea = false（默认值）。根布局不补系统安全区；HyperNavBar 的顶部操作区仍避让可见状态栏与挖孔一次，正文首屏净空位于滚动内容内部，可随上滑进入状态栏。自定义固定顶栏使用 Compose 窗口布局，IME 仍由输入宿主避让。下文旧条款中关于系统安全区的保留要求以本条为准。

HyperUI 当前只面向 Android 手机端。组件公开 API、默认尺寸和交互反馈以手机竖屏为基准；Preview 的 Desktop/Wasm 运行方式只服务于文档验收。

## AI 与开发者首读顺序

维护库或开发业务页面前，先读本文，再读[设计系统基线](style-reference.md)，然后核对实际依赖版本的组件 API、源码与交互 Preview。将下面的模板合并到业务项目根目录 AGENTS.md，把“HyperUI库根目录”替换为实际依赖文档路径；引用依赖不会自动继承库内 AGENTS.md。

## 全局全面屏与页面表面

- 所有 Activity 通过应用共用入口调用 enableEdgeToEdge，统一透明状态栏、手势导航栏和随主题变化的系统图标；HyperThemeConfig 只提供主题，不配置 Activity 窗口。
- 页面画布使用 HyperColors.pageBackground 全屏绘制，内容卡片保持独立表面和柔和双层阴影，不将画布改成卡片色。
- 不给整页叠加 systemBarsPadding/navigationBarsPadding 或固定安全区占位。具体控件或滚动末尾仅避让一次；表单宿主可使用 imePadding，避免与窗口调整重复计算键盘区域。
- HyperNavBarPage 负责正文采样和覆盖式导航。将它返回的顶部净空放入 LazyColumn.contentPadding，不能在滚动容器外固定留空；模糊与渐隐在状态栏顶部至 Navbar 底部内结束，禁止实色底板或业务裁切替换效果。
- 主滚动容器的状态传给 HyperNavBarPage.scrollState 后，库内 HyperNavBarScrollScope 自动监听并驱动 Navbar。页面不重复写 scrolledUnder 判断、derivedStateOf、snapshotFlow 或手势累计；程序滚动和位置恢复与手势滚动共用真实状态。导航区高度由库统一测量，页面不再单独监听尺寸计算净空。
- 已有HyperScaffold 或固定布局可只在共同父级接入 HyperNavBarScrollScope，不为接入监听改布局；同一个主 ScrollState/LazyListState/LazyGridState 交给库与容器。隐藏页或主列表被空状态替代时使用 active = false，库内负责派生判断与订阅，Navbar 单独读取稳定状态，不带动正文重组。弹窗、网页、传感器、分页和播放位置等业务监听保留。
- 正文使用 HyperCard、HyperMenuGroup、HyperListTile 等公开组件，不复制内部 hyperCardSurface；组内行共享一次表面。普通卡片无描边，选中轮廓和组内分隔线保留。
- 悬浮 TabBar 周围透明，正文绘制和滚动到其后方，不能用 Column 底部占位挤压正文。未经用户明确布局要求，不得调整既有底栏高度、位置、边距、安全区、末尾留白和滚动范围；不添加全宽底板或重复底部避让。

## 可复制到业务项目的 AGENTS.md 模板

```markdown
## Hyper UI 使用规则
- 新增或修改页面前，先读 HyperUI库根目录/vitepress/docs/mobile-guidelines.md 和 HyperUI库根目录/vitepress/docs/style-reference.md，再核对相关 API、源码与预览。
- 标准控件与表面使用 hyper_ui 公开组件；导航使用 HyperNavBarPage/HyperNavBar，卡片使用 HyperCard/HyperMenuGroup，不复制内部绘制，不自写平行皮肤。
- 把主滚动容器已有状态交给 HyperNavBarPage.scrollState，Navbar 自动读取库内监听。禁止逐页添加导航专用 derivedStateOf/snapshotFlow、scrolledUnder 判断或尺寸监听；首屏净空采用库测量的实际导航区高度。
- 已有正式布局仅用 HyperNavBarScrollScope 绑定同一个主滚动状态，不改变原 Scaffold、padding、Insets、正文约束或底栏；隐藏页或主列表空状态通过 active 停用来源。不在页面自己维护导航布尔值，也不删除业务自身的监听。
- 所有 Activity 在公共入口全局 enableEdgeToEdge；统一透明状态栏、手势导航栏与亮暗图标，pageBackground 贯穿屏幕。
- 禁止整页底部安全区或固定占位裁短正文。控件或滚动末尾的必要避让只处理一次，键盘出现仍可缩小或滚动表单。
- 顶部净空属于滚动内容，导航模糊由库在状态栏顶部至 Navbar 底部内绘制；不得向正文延伸，也不得添加实色底板或业务裁切遮住效果。
- 内容卡片沿用无普通边框、底色分层和统一双层阴影，保留选中态与组内分隔线；输入、浮层和导航保留各自专用材质。
- TabBar 保持原有悬浮布局与透明周围区域，正文可滚到后方。只有明确的底栏布局要求才允许改变高度、占位、位置、margin、padding、安全区、末尾留白和滚动范围；样式优化不构成授权。
- 完成后核对整个页面链路，确认顶部/底部画布、导航模糊、卡片、键盘和正文滚动，遵守业务项目对编译与验证的限制；未做真机核对时明确说明。
```

## 布局边界

- 以 320dp 至 430dp 的竖屏内容宽度为主要验收范围。
- 页面优先使用单列流式布局，外部宽度、间距和滚动由调用方通过 `Modifier` 管理。
- 页面列表和平铺行默认水平外边距为零；菜单卡片和独立卡片保留内部留白。调用方在页面层使用 HyperSpacing.PageHorizontal（14dp）统一提供一次水平边距，让搜索、分组标题、行内容和分隔线对齐，不在每行补另一层缩进。
- 不在组件库中加入平板双栏、桌面多栏或横屏专用参数。
- `HyperNavBar` 固定避让顶部状态栏；`HyperNavBarPage` 将首屏顶部净空与默认底部避让交给滚动内容，页面宿主不要重复添加顶部安全区。页面背景和滚动视口仍可覆盖系统栏区域。

## 尺寸词汇

基础组件统一使用：

| 值 | 用途 |
| --- | --- |
| `small` | 紧凑工具、次要操作、窄屏表单 |
| `default` | 手机页面中的常规控件，默认值 |
| `large` | 主要入口、媒体操作和需要更强视觉层级的控件 |

`type` 只表示同一组件内的渲染形态，例如按钮的 `filled` 或输入框的 `textarea`；按钮配色通过独立的 `colorStyle` 与 `color` 控制；`size` 只改变组件的视觉尺度。外部宽度不要通过新增 `width` API 解决，使用 `Modifier.fillMaxWidth()`、`widthIn()` 或父布局约束。

## 触控与反馈

- 交互组件在按下时立即反馈，取消或释放后恢复；禁用态不显示按压反馈。
- 组件状态由调用方持有，组件只通过参数和回调报告操作。
- 颜色不是唯一状态线索。错误、禁用、选中和加载态同时通过文字、图标、透明度或布局反馈表达。
- 键盘弹出时，页面应允许内容滚动到输入框；不要用固定高度裁切正文或错误提示。

## 页面组合

- 顶部页面操作使用 `HyperNavBar`，底部主导航使用 `HyperTabBar`。
- 设置和少量操作使用 `HyperMenuGroup`；连续数据使用 `HyperList` 或 `HyperSectionedList`。
- 对话框、抽屉和 Popup 只承载当前任务，网络请求、权限申请和路由仍由调用方处理。

## 相关文档

- [接入与最小配置](getting-started.md)
- [主题配置](theme.md)
- [组件索引](component-index.md)
- [状态与架构边界](state-model.md)

按需安全区统一使用 HyperSafeArea（Top / Bottom）。根画布继续全面屏；竖屏底部取导航区／缺口与库内最小 20dp 的最大值，横屏只保留实际系统区，同侧嵌套只消费一次。输入法只扩展自身面板底部，不处理宿主 IME Insets；页面不得自行复制安全区高度。

卡片边界：页面外边距与卡片内边距不能互相替代。HyperMenuGroup 为内容提供水平 14dp 内边距；独立 glass 列表行同样保留内部 14dp；plain 行不绘制卡片，使用父页面或父卡片已经提供的留白。
