# UI 样式与功能实现基线

三选项主题弹窗统一使用 HyperThemeModeDialog：跟随系统、浅色、深色；36dp 最小行高、4dp 垂直内边距、无额外行间距，沿用 21dp 单选圆环。普通 HyperRadio 标签行与设置菜单的默认高度保持独立。

## 全面屏与系统安全区

Android Activity 统一使用 enableHyperEdgeToEdge()，根主题 useSystemSafeArea 默认为 false。HyperTheme.systemSafeAreaInsets 为组件的唯一系统安全区来源，默认返回零；Scaffold、TabBar 不增加根系统安全区。Navbar 和自定义固定顶栏独立保护顶部交互区域一次；HyperNavBarPage 将测量净空放进滚动正文，页面背景和滚动视口保持全屏。键盘 Insets、业务组件高度和控件本身的间距继续保留。下文旧版关于状态栏/底部安全区的尺寸说明以此规则为准。

本文依据当前 library/src/main/java/hyper_ui/ 实现整理，基线日期为 2026-10-21，复用本页已有的样式对照作为持续维护入口。对应 Flutter 仓库为 D:/my_project/flutter_project/flutter-hyper-ui，维护时同时参考其 vitepress/guide/design-system.md；以各端实际源码核对功能，不能把另一端 API 直接当成本端已有实现。

Android 手机端目标为 320–430dp 竖屏；Desktop/Wasm 仅用于文档 Preview。本文记录默认视觉、功能行为和修改边界，完整公开签名与参数表仍见[组件索引](component-index.md)中的各组件页，不用本文替代。

## 修改边界

1. 修改前一起核对本文、目标源码、组件页和 Preview 的实际调用；不能只凭截图推断用途。
2. 已有样式默认保留。用户明确授权的改动按范围执行；用途不明、可能改变布局、点击区、状态或回调的地方，先说明位置、现状、影响与建议，得到用户确认后再改。
3. 不以“统一”为由把所有点击区域改成 HyperButton。输入框操作区、页签、日期格、颜色选项、侧滑操作和倍速刻度有自己的功能与空间约束。
4. controlTrack 是轨道／禁用背景，selection 是选中底板，secondaryText 是次级文字。不能看到灰色就当成按钮多余背景删除。
5. 普通动作复用 HyperButton 的默认样式，不重复手写前景和禁用色；但已有专用 colors、shape、border、padding 或 Modifier 覆盖不能批量删除，先核对保留清单与具体用途。
6. 参考仓库存在差异不等于允许重写 Android。明确要调整对应组件时，同步两端实现、文档与预览；找不到对应组件时说明情况，不为补齐文档自动新增实现。
7. 源码与本文冲突先查原因；不能擅自改源码迎合文档，也不能通过更新文档认可未经授权的变化。用户明确的新要求优先，完成后更新本页。

## 源码与职责

下表源码目录均相对于 library/src/main/java/hyper_ui/；Android 的公开 API 包为 hyper_ui，不按目录名改成多个对外包。

| 位置 | 当前职责与依赖边界 |
| --- | --- |
| theme/HyperStyle.kt | HyperThemeConfig、HyperTheme、HyperColors、HyperTypography、HyperShapes 与局部内容色／排版；统一主题入口 |
| theme/HyperGlassTokens.kt | HyperMaterial 与 HyperGlassTokens；材质、边缘、阴影、轨道、选中和遮罩令牌 |
| core/interaction/ | hyperNoRippleClickable、HyperMotionSpec、hyperOverlayProgress；按压、长按和浮层过渡 |
| core/surface/ | HyperBackdrop、表面采样、裁切、阴影、深度和共享表面作用域；不读取业务状态 |
| core/text/、core/icon/ | HyperText、HyperIcon、共享 Lucide 轮廓及图标桥接；保留插槽继承规则 |
| components/ | 受控组件及组合；通过参数与回调接入业务，不添加网络、数据库或权限申请 |
| tools/system/ | Android 电池状态读取及生命周期订阅；平台工具与展示组件分开 |
| tools/update/ | 独立 APK 静态索引检查、系统下载、安装与成功后定向清理工具；默认每小时窗口最多自动检查 2 次，失败计次，手动不占额度；不定义业务资源通道，不改变弹窗视觉 |
| preview/src/commonMain/kotlin/hyper_ui/docs/data/ | ComponentDemos 与分组元数据、文档路径、Preview ID |
| preview/src/commonMain/kotlin/hyper_ui/docs/ui/ | 交互示例、手机外框、有限视口及页面级滚动宿主；模拟业务留在示例 |
| vitepress/docs/ | 唯一 Markdown 正文；组件页保留真实签名和完整五列参数表 |

## 主题、材质与交互令牌

HyperThemeConfig 默认跟随系统亮暗，material 为 HyperMaterial.Solid。HyperColors 负责语义色，HyperGlassTokens 负责表面层级；两者不可仅因颜色相似而互换。Color.Unspecified 表示采用默认值，与透明色不同。调用方通过主题或组件公开的 colors 参数换肤，不在组件中写业务颜色常量。

| 项目 | 当前默认实现 |
| --- | --- |
| Solid | surface / surfaceStrong / surfaceSubtle 均不透明；blur / blurStrong 均为 0dp |
| Soft | 使用亮暗基础表面，标准／加强模糊为 20 / 28dp |
| Clear | 三档表面 alpha 分别为 118 / 153 / 77 除以 255；模糊为 32 / 40dp |
| 主题色 | RGBA 8 位通道为 71 / 111 / 232 / 255 |
| success / info / warning / danger | RGB 分别为 34/197/94、59/130/246、245/158/11、239/68/68，均不透明 |
| pageBackground | 亮色 RGB 241/243/247；深色 17/19/24 |
| primaryText | 亮色 RGB 26/29/38；深色 243/244/246 |
| secondaryText | 亮色 RGB 107/114/128；深色 184/192/204 |
| 日期范围底色 | 亮色 RGB 239/246/255；深色 20/37/68，均不透明；普通日期格和只读月历共用 |
| HyperShapes | extraSmall / small / medium / large 圆角 7 / 16 / 24 / 28dp；专用组件可有不同圆角 |
| 按压 | 默认缩放 0.975、透明度 0.92；按下 85ms、恢复 180ms；无水波纹 |
| 状态／选择／菜单／提示／模态／抽屉 | 分别为 160 / 260 / 180 / 125 / 250 / 280ms |
| 曲线 | OverlayCurve 为 CubicBezier(0.23,1,0.32,1)；SelectionCurve 为 CubicBezier(0.77,0,0.175,1) |

hyperNoRippleClickable 在 Initial 指针阶段观察按下，即使位于滚动容器中也立即反馈；业务点击仍由 clickable / combinedClickable 处理。不能仅保留视觉动画而删掉点击、取消和长按生命周期。不要把 Flutter 特有的实现分支写成本端已经具备的机制。

HyperText 独立使用为 12 / 14 / 18sp、行高 1.4；default 可继承 LocalHyperTextStyle，因此按钮和其他插槽内会使用宿主字号。HyperIcon 的 default 继承 LocalHyperIconSize，small / large 为 18 / 32dp；显式 Modifier 尺寸也参与约束。不能为了“统一图标”删掉按钮内的局部尺寸。

## 内容卡片与导航维护规则

页面使用 pageBackground，实色卡片使用白色或深色 RGB 27/31/39；不要将画布覆盖成卡片色。HyperCard、HyperMenuGroup、独立卡片形态 HyperListTile、HyperSlideMenu 的内容表面统一复用内部 hyperCardSurface 和 HyperSurfaceDepthRole.ContentCard，普通状态无描边。双层阴影参数与 Flutter 一致：主题 shadow 的 75% alpha、radius 18dp、spread -4dp、向下 4dp；40% alpha、radius 4dp、spread -1dp、向下 1dp。选中态保留完整强调色边框，组内行共享容器和分隔线，不逐行叠加阴影。原有圆角、间距、回调和专用控件样式保留。

浮层仍使用原有 surfaceShadows，按钮、输入、选择控件、提示和导航不批量换成内容卡片。Android 当前没有 HyperCollapse 对应实现，不为本次同步新增组件。

HyperNavBar 以 pageBackground 着色，滚动后显示 20dp 采样与 160ms 渐显；模糊和渐隐都限定在状态栏顶部至 Navbar 底部内，不再越界覆盖正文。HyperNavBarPage 注册采样源，顶部净空由库测量实际导航区高度，放入滚动内容；没有采样内容时不能用实色底板伪装模糊。全面屏、底部避让与冻结既有 TabBar 布局的约束见[业务页面规范](mobile-guidelines.md)。

导航监听统一在 HyperNavBarScrollScope 内派生真实主滚动状态，作用域传递稳定 State，由 Navbar 读取值，不因阈值切换重组整页。active=false 用于隐藏页或主列表被空状态替代的场景；不改滚动位置，恢复后继续读取真实值。HyperScaffold 只补作用域与同一状态绑定，保留原布局；不以 nestedScroll 位移、业务页面布尔缓存或次级视口替代真实状态。默认样式、导航测量、卡片阴影及 TabBar 布局保持原样。

## HyperButton 默认样式与复用规则

源码为 components/button/HyperButton.kt、HyperButtonSurface.kt；阴影后端换算由 HyperButtonShadowRadius.kt 负责，Preview 使用独立内部换算适配。

| 项目 | 当前规则 |
| --- | --- |
| type | 省略为默认描边；显式值仅 filled、icon |
| colorStyle | primary / success / info / warning / danger；不承担按钮形态含义 |
| color | 优先于 colorStyle；指定色调也会影响描边，不能只看内容色是否相同 |
| 默认描边 | surfaceSubtle 背景、primaryText 前景、divider 描边 |
| 默认图标按钮 | surface 背景、primaryText 前景、edgeHighlight 描边；宽高相等 |
| filled | 基色渐变、淡描边和阴影；内容色按色调与亮度计算；不用已有 colors 参数推断可删除所有背景覆盖 |
| 禁用 | enabled 为 false 或 onPressed 为空；默认 controlTrack 背景、secondaryText 的 alpha 150/255、divider 的 alpha 110/255 |
| loading | 停止重复点击；图标按钮替换内容，文字按钮保留内容并显示加载；不自动改成禁用颜色 |
| 内容与尺寸 | content 为 RowScope 插槽，继承局部文字／图标大小；默认按内容收缩，Modifier 控制宿主布局 |
| 自定义入口 | colors、border、shape、contentPadding 是当前公开能力；不因整理删除这些参数或替调用方决定布局 |

| size | 高度 | 最小文字按钮宽度 | 默认水平内边距 | 字号 | 纯图标 | 标签图标 |
| --- | --- | --- | --- | --- | --- | --- |
| small | 32dp | 53dp | 10dp | 12sp | 18dp | 16dp |
| default | 38dp | 68dp | 14dp | 13sp | 20dp | 17dp |
| large | 44dp | 83dp | 18dp | 14sp | 22dp | 18dp |

表中水平内边距为 Defaults 的 contentPadding；非图标按钮当前还会追加 1dp 内容留白，图标按钮忽略文字按钮内边距。圆角为高度的一半。取消／关闭等普通操作沿用默认按钮，主操作按既有用途显式 filled 与 primary，危险操作按既有用途使用 danger；不得批量把业务配色清空。

## 同名组件参数

| 组件 | 参考默认值 | Compose 对应 |
| --- | --- | --- |
| HyperText | 12 / 14 / 18，行高 1.4 | size 三档；插槽继承局部排版 |
| HyperIcon | 18 / 24 / 32，插槽图标另定 | size 三档，直接使用 Lucide 库图标 |
| HyperDivider | 1dp；虚线 4dp + 4dp | type、axis、indent/endIndent/length |
| HyperButton | 高 32 / 38 / 44，radius 高度的一半 | 字号 12 / 13 / 14sp；标签图标 16 / 17 / 18dp、纯图标 18 / 20 / 22dp |
| HyperTextField | radius 20；字号 14 / 16 / 18；padding 14×7 / 18×9 / 20×11 | 1dp 普通轮廓、1.5dp 聚焦；多行纵向 +3dp |
| HyperCheckbox | 44dp 触摸、21dp 方框、radius 7、轮廓 1.5 | nullable 三态、勾 15dp、160ms 状态动画 |
| HyperRadio | 独立交互 44dp 触摸、展示 21dp 圆环、轮廓 1.5 / 6 | 标签行最小 46dp，不叠加触摸留白；160ms 选中颜色和环宽动画 |
| HyperSwitch | 52×32，padding 4 + Container 边框 1 | 实际滑块 22dp，160ms selectionCurve |
| HyperSlider | 44dp 触摸、6dp 轨道、20dp 滑块、2dp 轮廓 | 默认范围 0..100，showValue=true |
| HyperSegmentedControl | radius 18，padding 4、条目 32，总高 40 | 13sp，选中 600、未选中 500，图标 16dp；等宽底板平移 260ms |
| HyperTabs | radius 27，padding 3、条目 48，总高 54 | 单一共享表面，44dp 选中底面；滚动项目最小 76dp，选中背景 160ms，非滚动模式背景即时切换 |
| HyperTabBar | single / multiple / 贴底默认内容高 56；悬浮顶部间距 8、底部间距 20 | single / multiple；图标 22 / 23；指示器 160ms easeOutCubic；大字体仅由库按内容扩展 |
| HyperNavBar | 高 44，水平 padding 16，标题 16/600 | 透明操作层，标题字距 -0.2sp、副标题 11sp、安全区另计 |
| HyperListTile | plain 最小 46，水平留白 0、垂直 8；外层 glass 仅垂直留白 3 | auto/plain/glass，标题 14/600、说明 12、meta 11，图标块 34 |
| HyperMenuGroup | radius 18，padding 4，分隔缩进 14 | items 入口自动分隔；自由内容入口显式组合分隔 |
| HyperCard | radius 24，padding 12，头/正文/页脚间距 12 | 标题 14/600，副标题 13，selected strong 与强调轮廓 |
| HyperDropdown | 字段 minHeight 48，radius 16，padding 14×11；自动宽度 clamp(最长文案 + 68,160,240) | width 可显式指定，菜单匹配字段实际宽度；radius 18、padding 5、默认最大高 320 |
| HyperPopover | 默认宽 180，width 可显式指定，maxWidth 280 仅作上限；高窗口 40%，padding 14 | 真实锚点、6dp 间距、12dp 安全边距、自动翻转 |
| HyperTooltip | radius 12，padding 10×7，12sp/1.3 | 500ms 悬停/长按；焦点即时；125ms 0.97→1 缩放淡入 |
| HyperDialog | min(maxWidth,实际窗口宽度-48)，maxWidth 420，窗口间距 24，radius 26，padding 18 | 标题 20/600、正文 10、操作区 18、操作间距 8 |
| HyperDrawer | 贴边全高，默认宽 320，窄屏最大窗口宽减 56，正文默认零间距 | 实色背景，无外侧留白／圆角／描边／卡片阴影；280ms 从面板自身宽度之外平移，遮罩独立淡入 |
| HyperActionSheet | 贴底全宽，圆角 30、内边距 16、最大高为键盘避让后可用高度的 85% | strong 材质，32×4 顶部指示条；250ms 淡入与 24dp 纵向平移；整体滚动，内部避让底部安全区 |
| HyperSlideMenu | 每动作 64，边界阻力 0.32，释放阈值 35% | 0.09s 速度投影、stiffness 470/damping 42 |
| HyperProgress | linear 4/6/12；circle 48/64/80、stroke 4/6/8 | showLabel=true；线性动画 1400ms、圆环 1100ms |
| HyperEmptyState | 图标块 56、radius 24、图标 24、padding 20 | 标题 16/600、说明 13/1.45、间距 12/8/12 |
| HyperToast | minWidth 180/maxWidth 420，radius 18，padding 12/10/8/10 | 30dp 图标块、17dp 图标、14sp/500/1.35、默认 2000ms |
| HyperUpdateDialog | maxWidth 420，模态安全区外间距 20，radius 28，padding 24，非错误状态标识块 40、图标 20、环形进度 30 | 标题 24/1.2、说明 14/1.55、进度 28/1.2；其余文字按主题 1.4 行高随字号换算。检查中隐藏版本；错误仅保留原因、重试与关闭 |

HyperGlass 的 DecoratedBox 边框只绘制，不增加布局尺寸。Container 控件按各自固定尺寸和隐式边框内边距处理。普通按压为 0.975 缩放、0.92 透明度，85ms 进入、180ms 恢复，曲线 Cubic(0.23,1,0.32,1)。模态弹窗为 250ms、0.96→1 缩放；锚定浮层为 180ms。主题令牌详见 [主题配置](theme.md)。

Android 与 Preview 直接引用 com.composables:icons-lucide-cmp:2.2.1 的 Lucide 图标；Flutter 使用 lucide_icons_flutter。选择相同语义图标并保留宿主尺寸与颜色继承，不在项目中复制字体轮廓、SVG 路径或维护图标别名表。

## API 约定

| 组件 | 当前明确入口 |
| --- | --- |
| HyperThemeConfig | material = HyperMaterial.Solid / Soft / Clear；glass 可显式覆盖 |
| HyperDropdown | options: List&lt;HyperOption&lt;T&gt;&gt;，value，onChanged；临时 expanded 在组件内部 |
| HyperPopover | visible、onClose、child、content；内容贴触发点定位 |
| HyperActionSheet / HyperAction | visible 与 onClose 受控；自定义 content 或 actions / onChanged 二选一；dismissible 只控制遮罩关闭，返回键仍可关闭；操作先关闭再回传，取消仅关闭 |
| HyperTabs | tabs、selectedIndex、onChanged、scrollable；共享表面 |
| HyperTabBar | type=single/multiple、items、selectedIndex、onChanged；单动作角标 badgeCount |
| HyperCheckbox | value: Boolean?、onChanged: (Boolean?)-&gt;Unit、tristate |
| HyperText | size=small/default/large；局部插槽样式优先 |
| HyperIcon | size=small/default/large；default 继承插槽尺寸 |
| HyperSlider | value、onChanged、min=0、max=100、divisions=null、showValue=true；拖动起止回传数值 |
| HyperUpdateDialog | status、release、downloadProgress、progress、mandatory 与操作回调；业务执行由调用方承担 |
| HyperPlaybackSpeedPanel | value、onChanged、onClose、options、onCustomPressed；leading 等插槽提供图标 |
| HyperPlaybackSpeedScale | value、options、leading；只展示速度刻度 |

全部公开 API 在 hyper_ui 包；完整签名和参数默认值见 [组件索引](component-index.md)。电量、倍速、颜色选择与懒列表等 Compose 专用组件遵循同一主题与命名约定，具体尺寸见各组件页。

## 文件职责

- theme/HyperGlassTokens.kt 统一三档材质，theme/HyperStyle.kt 提供模式、语义色与排版。
- core/interaction/HyperMotionSpec.kt 统一按压、选择和浮层的曲线与时长。
- core/icon/HyperIcon.kt 负责尺寸与颜色继承；组件直接引用 Lucide 库图标，不维护本地图形或转接表。
- core/surface/ 提供玻璃、背景采样与阴影；边框和内容间距分开管理。
- components/menu/HyperMenuSizing.kt 计算默认紧凑文字宽度；components/popup/HyperAnchoredSurface.kt 共用于 Dropdown、Popover，处理实际字段宽度、安全区、键盘和上下翻转。
- components/update/HyperUpdatePresentation.kt 只映射显示数据，HyperUpdateContent.kt 组合正文与操作区，HyperUpdateLayout.kt 共用内嵌面板与模态宿主布局，HyperUpdateDialog.kt 承担受控入口。
- preview/ui/UpdateDialogShowcase.kt 对齐 Flutter 更新流程与状态预览；UpdateDemoController.kt 只持有页面的模拟任务，关闭弹层不会取消下载。
- preview 的 ComponentDemos 聚合各分组元数据，*ComponentShowcases 提供可操作的大小、禁用、选中与进度状态。
- ComponentPreviewViewport 统一有限视口与滚动归属；PageListShowcases 为页面级列表提供剩余高度，PreviewSectionLabel 统一示例分组与操作行。
- component-doc.css 负责文档层级、折叠源码与参数表排版；MarkdownDocument 让桌面 Preview 读取相同 Markdown 并呈现折叠内容。

## 组件功能与状态归属

以下文件均相对于 library/src/main/java/hyper_ui/。尺寸使用前面的对照表；此处补充功能、状态与复用关系，不能据此扩展未公开 API。

### 基础、表单与日历

| 组件与文档 | 实现位置 | 当前功能与边界 |
| --- | --- | --- |
| [HyperText](components/basic/hyper-text.md) | core/text/HyperText.kt | BasicText 包装；默认继承内容色、字体和插槽排版，显式文本参数覆盖；不强制截断所有文字 |
| [HyperIcon](components/basic/hyper-icon.md) | core/icon/HyperIcon.kt | Painter / ImageVector 重载共用绘制入口；default 继承局部大小，不能强制所有图标 24dp |
| [HyperDivider](components/basic/hyper-divider.md) | core/surface/HyperDivider.kt | solid / dashed、水平／垂直、长度与两端缩进；分隔线不等价于按钮边框 |
| [HyperTextField](components/form/hyper-text-field.md) | components/input/ | value 受控，onChanged 为空或 readOnly 时只读；保留 BasicTextField、键盘类型、错误、聚焦、密码切换、清空、maxLength 和字数限制；操作区复用 HyperInputAffix |
| [HyperSwitch / HyperCheckbox / HyperRadio](components/form/hyper-checkbox.md) | components/selection/HyperSelectionControls.kt | value / groupValue 由调用方维护；Checkbox 支持 nullable 三态；plain 标签行承载点击，无卡片外观；Checkbox 尾随方框只占 21dp，无标签仍保留 44dp；基础示例不包 HyperCard |
| [HyperSlider](components/form/hyper-slider.md) | components/progress/HyperSlider.kt | value 受控，默认范围 0–100；divisions 管吸附，showSegmentMarkers / segmentValues 管刻度，二者独立；readOnly 保持正常外观并停止修改，保留起止回调和 RTL |
| [HyperSegmentedControl](components/form/hyper-segmented-control.md) | components/selection/HyperSegmentedControl.kt | selectedValue 受控；整组与单项禁用分开；equalWidth 默认 true，共享选中块，非等宽按项目绘制状态；不替换为一排普通按钮 |
| [HyperDatePicker.date](components/form/hyper-date-picker.md) | components/date/HyperDatePicker.kt、HyperDatePickerSheet.kt | Android 日期为 LocalDate；默认 1900-01-01 至 2100-12-31；临时选中在面板内，确定先 onClose，退出完成再 onChanged；取消不提交；当前仅公开单日期入口 |
| [HyperCalendarDay / HyperCalendarItem](components/form/hyper-calendar-day.md) | components/date/HyperCalendarDay.kt | 选中圆、今日描边、范围、相邻及禁用态；辅助字、角标、最多三行条目，溢出数量独占最后一行；minimumHeight 按字体缩放和预留行数计算 |
| [HyperCalendarMonth](components/form/hyper-calendar-month.md) | components/date/HyperCalendar.kt、HyperCalendarMonthContent.kt | Android LocalDate 包装平台无关网格；固定六行七列、默认周一；选中和范围由调用方提供；readOnly 用 Canvas 合并绘制并保留视觉；月历面板另由 HyperCalendarMonth.kt 管理翻月／选月 |
| [HyperColorPicker](components/container/hyper-color-picker.md) | components/panel/HyperColorPicker.kt | selectedId 受控，回调返回 HyperColorOption；默认 36dp 色块、选中环和标签，FlowRow 自动换行；不自动修改全应用主题 |

### 容器、列表与导航

| 组件与文档 | 实现位置 | 当前功能与边界 |
| --- | --- | --- |
| [HyperBackdrop / HyperSoftBackground](components/container/hyper-backdrop.md) | core/surface/HyperBackdrop.kt | 页面背景及 Haze 采样作用域；不同材质共用表面实现，不把 Preview 场景背景复制到正式组件 |
| [HyperCard](components/container/hyper-card.md) | components/panel/HyperCard.kt | title / subtitle / leading / actions / footer / child 插槽；selected 使用较实表面与原始强调色边框，不额外降低边框透明度；显式 border 可覆盖；点击可选 |
| [HyperListTile](components/list/hyper-list-tile.md) | components/list/HyperListTile.kt | auto / plain / glass；根据已有表面作用域决定是否绘制外壳；选中、禁用、尾箭头和分隔线各自处理；图标块为独立视觉，不当成按钮 |
| [HyperMenuGroup](components/list/hyper-menu-group.md) | components/list/HyperMenuGroup.kt | 外层单一表面，条目共享容器；items 入口负责分隔，自由内容入口由调用方组合；不嵌套逐行卡片 |
| [HyperList](components/list/hyper-list.md) | components/list/HyperList.kt | 页面级 LazyColumn；state 可传入，modifier 与 contentModifier 管不同节点，contentPadding 随内容滚动；无卡片表面，默认内边距 0dp，auto 行使用 plain |
| [HyperSectionedList](components/list/hyper-sectioned-list.md) | components/list/HyperSectionedList.kt | 分组标题与数据行分别懒加载，调用方给稳定 key；跳过空分组，组间固定 8dp；无卡片平铺行，组件管理组内分割线 |
| [HyperNavBar / HyperNavBarPage](components/navigation/hyper-nav-bar.md) | components/navigation/HyperNavBar.kt、HyperNavBarSurface.kt、HyperNavBarScrollScope.kt | 固定 type 与 custom 插槽校验互斥；安全区位于背景内部，背景从屏幕顶部覆盖状态栏，只有操作行避让；预览注入 52dp 模拟 Insets，导航与正文共用且只计一次；库内观察主滚动状态并驱动背景，页面宿主叠放导航并传递 padding，不自行接管业务路由 |
| [HyperTabs](components/navigation/hyper-tabs.md) | components/menu/HyperTabs.kt | tabs / selectedIndex / onChanged 受控；重复点击当前项不回调；scrollable 决定等宽或水平滚动，itemEnabled 控制项目；不内置 Flutter 的页面控制器 |
| [HyperTabBar](components/navigation/hyper-tab-bar.md) | components/navigation/HyperTabBar.kt | single / multiple；floating 默认 true 保留原悬浮样式，false 为零默认外间距的全宽贴底平直表面；selectedIndex=null 为无选中块、可重复点击且不响应拖动的动作模式；itemEnabled 控制单项禁用。选择模式仍按拖动预览、松手提交，保留标签测宽与安全区 |
| [HyperScaffold](components/navigation/hyper-scaffold.md) | components/navigation/HyperScaffold.kt | 使用 Compose 基础布局承载页面、上下栏和悬浮操作；正文消费实际测量净空，不依赖 Material，也不接管业务滚动或导航监听 |
| [HyperSlideMenu](components/navigation/hyper-slide-menu.md) | components/menu/HyperSlideMenu.kt | reveal 为 Closed / Start / End，调用方响应 onRevealChange；内部持有拖动偏移，松手时速度投影与弹簧共用释放速度；操作区填满侧滑空间，危险和自定义色保留；先请求关闭再执行操作 |
| [HyperDrawer](components/navigation/hyper-drawer.md) | components/drawer/HyperDrawer.kt | 独立模态层，open 与 onClose 受控；默认 start，RTL 换边；正文 content、内边距 contentPadding 默认零；安全区／键盘避让在背景内；标题页脚固定；懒列表设 scrollable=false；滑动超过宽度 30% 或速度达 400dp/s 请求关闭，否则 180ms 复位；遮罩、返回键与滑动分别控制 |

### 浮层、反馈与系统工具

| 组件与文档 | 实现位置 | 当前功能与边界 |
| --- | --- | --- |
| [HyperDropdown / HyperOption](components/feedback/hyper-dropdown.md) | components/menu/HyperDropdown.kt | value 受控，expanded 在内部；启用取决于 enabled 与 onChanged；菜单匹配字段最终测量宽度，点选后回调并收起，选项复用 HyperListTile |
| [HyperPopover](components/feedback/hyper-popover.md) | components/popup/HyperPopover.kt、HyperAnchoredSurface.kt | visible 与 onClose 受控，child 提供触发内容；scrollable 默认 true，false 时交由有界子列表滚动；锚点上下翻转、键盘与窗口边界避让；不要改成内部持有业务显示状态 |
| [HyperTooltip](components/feedback/hyper-tooltip.md) | components/feedback/HyperTooltip.kt | 悬停／长按与焦点展示；锚点定位、延迟和退出过渡内部处理，不在调用方再写一份浮层 |
| [HyperDialog](components/feedback/hyper-dialog.md) | components/dialog/HyperDialog.kt、HyperDialogHost.kt | visible 与 onClose 受控；返回键和点击外部关闭分别配置；共享 HyperFloatingPanel，宽高根据实际窗口收敛，保留滚动指示和 actions 插槽 |
| [HyperAlertDialog](components/feedback/hyper-alert-dialog.md) | components/dialog/HyperAlertDialog.kt | 基于 HyperDialog 组合标题、正文与操作插槽；不自动附加业务按钮或提交表单 |
| [HyperToast / hyperToast](components/feedback/hyper-toast.md) | components/feedback/HyperToastSurface.kt、HyperToast.kt | Compose 提示 visible 受控，默认 2000ms 后请求 onClose，duration=0 不自动关闭；actionLabel 与 onAction 同时提供才显示主题色文字操作，先请求关闭再回调；action 插槽优先并自行关闭；Android hyperToast 为原生文本／资源提示 |
| [HyperProgress](components/feedback/hyper-progress.md) | components/progress/HyperProgress.kt | linear / circular，自绘轨道／圆环；value=null 是不定进度，不伪造百分比；显示文字、尺寸与颜色由现有参数控制 |
| [HyperEmptyState](components/feedback/hyper-empty-state.md) | components/feedback/HyperEmptyState.kt | 图标、标题、说明和 action 插槽；只负责空态展示，不请求业务数据或自动生成重试行为 |
| [HyperLevelCapsule](components/feedback/hyper-level-capsule.md) | components/progress/HyperLevelCapsule.kt | 默认 52×156dp 的竖向比例反馈，progress 收敛至 0–1；保留图标与 label；自身不处理音量／亮度服务或手势 |
| [HyperPlaybackSpeedPanel / HyperPlaybackSpeedPanelOverlay](components/feedback/hyper-playback-speed-panel.md) | components/feedback/HyperPlaybackSpeedPanel.kt 与 Defaults 文件 | value / onChanged / onClose 受控，Overlay 管宿主显隐；默认 0.25–4 倍滑块、0.05 步进、重置 1 倍；预设点、关闭、重置、自定义和插槽是专用布局，不操作播放器 |
| [HyperPlaybackSpeedScale](components/feedback/hyper-playback-speed-scale.md) | components/feedback/HyperPlaybackSpeedScale.kt | 只展示速度刻度，默认选项 0.25 / 0.5 / 1 / 1.5 / 2 / 2.5 / 3 / 4；不提供拖拽改速或自定义输入逻辑 |
| [HyperBatteryIndicator](components/feedback/hyper-battery-indicator.md) | components/feedback/HyperBatteryIndicator.kt | 受控 percentage 与 charging，默认显示百分比；电池主体 38×18dp、端子与充电图标独立绘制；不自动读取系统电量 |
| [HyperBatteryState](components/tools/hyper-battery-state.md) | tools/system/HyperBatteryState.kt | readHyperBatteryState 一次性读取；rememberHyperBatteryState 订阅公开系统广播并在离开 Composition 时注销；使用 applicationContext，未知数据为 Unavailable，不申请权限 |
| [HyperUpdateDialog](components/feedback/hyper-update-dialog.md) | components/update/ | status 与操作回调受控；idle 不显示，强制更新／安装中限制退出；仅展示版本、日志和进度，不执行检查、下载或安装服务 |

HyperUpdateStatus 的值为 idle / checking / available / upToDate / downloadQueued / downloading / readyToInstall / installing / completed / error。HyperUpdateRelease 与 HyperUpdateDownloadProgress 是纯展示模型，总大小未知时保持不定进度；格式化共用 core/HyperTransferFormat.kt。hyperUpdatePresentation 只映射展示，HyperUpdateContent 只组合正文，HyperUpdateLayout 负责共用面板与模态宿主，不合并成业务管理器。

Defaults、Colors 和各 ItemScope 继续各司其职：Defaults 解析默认值，Colors 是显式样式入参，ItemScope 提供当前 selected / enabled 等插槽上下文。不能为减少类型数量把这些公开能力删掉，或把组件内部字段暴露为可变全局状态。

## 专用样式保留清单

下表是当前样式的维护边界，不是待替换清单。除非用户明确要求修改，否则保持现有外观；无法确认用途的覆盖先问，不用“默认样式更统一”代替确认。

| 位置 | 当前专用实现 | 保留原因与修改边界 |
| --- | --- | --- |
| HyperNavBarBackButton / HyperNavBarMoreButton | type=icon，colors 使用 surfaceStrong 背景 | 用于固定导航外观；用户已明确保留对应专用背景，不能批量清空 colors |
| HyperInputAffix | 30dp 点击区、18dp 次级色图标、Tooltip | 输入框内的清空／密码可见性操作；改为 32dp 默认图标按钮会改变尺寸、背景和输入布局 |
| HyperPlaybackSpeedPanel 的 panelButtonColors | 关闭／重置按钮使用内容色 alpha 0.10 背景，禁用为 0.05；前景随面板配色 | 现有面板专用覆盖；不是已获授权清理的灰底，调整前确认 |
| HyperPlaybackSpeedPanel 的标签与自定义入口 | 速度标签、预设刻度、提示和插槽单独组合 | 空间、点击及数值输入规则属于倍速面板，不改为一组默认按钮 |
| HyperListTile 的 leading 图标块 | 34dp、圆角 11、强调色浅底 | 列表视觉与按钮交互不同，不因圆角背景删除或替换为按钮 |
| HyperTabs / HyperSegmentedControl | 共享表面、项目背景、选中状态与项目禁用 | 是选择控件核心视觉，不删 selection 底板，不增加逐项按钮描边 |
| HyperTabBar single 与 badgeCount | 56dp 自绘入口、22dp 图标及独立计数角标 | 底栏自身管理 margin、安全区和尺寸，不替换为默认 HyperButton |
| HyperCalendarDay 的选中圆、角标和条目 | 日期专用圆形状态、紧凑文字和溢出行 | 需要保持日期网格与最小行高，不自动换成普通按钮或其他徽标 |
| HyperColorPicker 的色块和选中环 | 圆形预设色块、强调环、下方标签 | 颜色就是控件内容，不能按普通按钮配色清理 |
| HyperSlideMenu 的操作区 | 全高操作背景、文字／图标和危险色 | 必须填满侧滑宽高；胶囊按钮会改变露出区域与手势目标 |
| Slider、Progress、Switch、Radio、Checkbox | 自绘轨道、滑块、圆环、方框及禁用态 | 灰色有轨道和状态用途，不能全局删除 controlTrack 或次级色 |
| HyperBatteryIndicator / HyperLevelCapsule / HyperPlaybackSpeedScale | 电量、比例填充、刻度与端点 | 是数值展示专用绘制，不迁移为按钮或统一胶囊皮肤 |
| HyperDialogHost / HyperButtonShadowRadius | Android 宿主和阴影单位换算 | 正式库与 Preview 后端不同，不能删掉适配以追求文件一致 |
| Preview 中的显式主题和自定义色 | 用于演示公开 API 与状态 | 核对用途后保留，不与库内部多余覆盖混为一类 |

## 生命周期、平台与两端边界

- 状态归属以源码为准：SlideMenu 的 reveal、Drawer 的 open、Popover 的 visible 由外部控制，Dropdown 的 expanded、日期面板临时选择、按压与拖动过渡由内部维护。不能把全部组件改成同一种状态模型。
- HyperButton 的 loading 与 enabled 不同；Slider 的 readOnly 保持正常配色；月历 readOnly 改为合并绘制；省略回调不一定意味着弱化整个展示。不能统一套透明度或补空回调改变状态。
- HyperButton 的主体插槽统一命名为 content；ListTile 的 leading 与正文间距为 12dp，trailing 与正文间距为 8dp；NavBar 固定类型不接受 subtitle，副标题使用 custom。
- TextField 的 maxLength 与字数统计当前按 UTF-16 单元处理，字素分段对齐为独立专项；Toast 的 duration=0 保留不自动关闭语义。两项为已确认的维护边界，不顺带重构。
- HyperText 未指定 overflow 时，有限 maxLines 默认省略号，否则裁切；显式 overflow 优先。Popover / SlideMenu / Tabs 保留 Compose 受控宿主，对齐共有交互与默认视觉；不为补齐 Flutter 目录自动新增组件或日期时间／区间入口。
- Dropdown 展开后 enabled=false 或 onChanged=null 时自动收起；保留这项 Android 行为，不为对齐 Flutter 当前的展开状态而删除收起逻辑。
- 日期确认先请求关闭，退出完成再提交一次选中值；取消清除待提交结果，不能在每次点击日期时直接提交业务值。
- 浮层关闭回调只是请求修改外部状态；保留退出动画与其所需内容，不在外部 visible=false 时提前移除整棵宿主而跳过当前退出路径。
- HyperDialogHost 禁用平台默认宽度、原生 dim 与窗口动画，由 Compose 内容绘制遮罩和过渡；Preview 使用同包宿主替代 Android 实现，不能把这份替代实现用于 Android 发布物。
- Preview 的 commonMain 复用 library 源码，但在 preview/build.gradle.kts 中排除 Android Toast、电池广播、Dialog 宿主、阴影换算、LocalDate 日期入口及内嵌月历入口，并用共用内容或模拟替代。不能把 Preview 能运行视为 Android 入口已验证。
- 只记录本端实际能力。Flutter 的 Stepper、Pagination、Uploader 等不能据其文档推断 Android 已经存在；本次基线不新增对应组件。也不能把 Flutter 的自动关闭或动作回调时机直接写进 Android Toast。
- 对应组件的业务参数与默认视觉继续对齐，但 Android 原生日期、Modifier、Composable、窗口与状态宿主差异按本端 API 保留；发现外观差异先列清单，未经授权不顺带修复。

## 视觉核对

两端在相同设备、密度、字号缩放、安全区与字体配置下核对外观。检查浅色和深色、三种材质、320–430dp 宽度，以及禁用、聚焦、错误、选中、加载、键盘展开、RTL 和长文本。

组件预览使用当前源码生成的产物。修改文档正文只更新文档，组件画面由 Preview 产物提供；核对时确认产物版本。列表应能滚动到最后一项，输入框清空与前后缀可同时使用，完整参数表保持可见。

本文、AGENTS.md、README、文档首读入口和组件索引使用同一份基线链接。后续获授权修改时同步相关组件页和分组 Preview；不得通过仅修改示例掩盖正式组件的变化。只做文档维护时不顺带改组件源码或格式化整个项目；不安装依赖、不自动部署、不生成单元测试或一次性报告。验证结果直接说明，临时产物不进入项目和提交。

## 依赖

使用仓库声明的 Compose foundation/ui/runtime/animation 与 Haze；调用方可选的 Lucide Android 图标资源依赖由使用者准备，版本以构建配置为准。

导航滚动监听统一由 HyperNavBarPage.scrollState / HyperNavBarScrollScope 管理，Navbar 自动读取；消费页面不得重复声明导航专用 derivedStateOf、snapshotFlow 或 scrolledUnder 判断。库测量实际导航区高度，顶部净空放进滚动内容；主滚动状态保留在容器，嵌套列表不代替主状态。

TabBar 尺寸由库统一管理：默认内容高度 56dp，悬浮多项默认顶部视觉间距 8dp、底部视觉间距 20dp，字体扩展仅在组件内处理。业务页面不覆写高度；并排入口在共同 Row 中布局，正文采用宿主实际测量的底部净空。

按需安全区统一使用 HyperSafeArea（Top / Bottom）。根画布继续全面屏；竖屏底部取导航区／缺口与库内最小 20dp 的最大值，横屏只保留实际系统区，同侧嵌套只消费一次。输入法只扩展自身面板底部，不处理宿主 IME Insets；页面不得自行复制安全区高度。


页面数据列表：`HyperList` 与 `HyperSectionedList` 使用无卡片的简约平铺样式，不绘制卡片背景、阴影或分组圆角；保留分隔线与行选中反馈。设置菜单的 `HyperMenuGroup` 和独立 `HyperListTile` 表面规则保持独立。

列表水平留白归调用方：HyperList、HyperSectionedList 与 plain 行默认不添加水平外边距；HyperMenuGroup 和独立 glass 行保留卡片内部留白；页面通过 HyperSpacing.PageHorizontal（14dp）统一提供一次。组件保留垂直触控留白、内容间距和选中反馈。

卡片边界：页面外边距与卡片内边距不能互相替代。HyperMenuGroup 为内容提供水平 14dp 内边距；独立 glass 列表行同样保留内部 14dp；plain 行不绘制卡片，使用父页面或父卡片已经提供的留白。
