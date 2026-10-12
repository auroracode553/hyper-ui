# doc_ui_kit 样式对照

视觉基准为 D:/my_project/flutter_project/doc_ui_kit/ui/lib/src/components 与同项目 theme。源码默认值是样式依据；Android 手机端目标为 320–430dp 竖屏。Desktop/Wasm 用于文档 Preview。

## 同名组件参数

| 组件 | 参考默认值 | Compose 对应 |
| --- | --- | --- |
| HyperText | 12 / 14 / 18，行高 1.4 | size 三档；插槽继承局部排版 |
| HyperIcon | 18 / 24 / 32，插槽图标另定 | size 三档，共享 Lucide 字体原始轮廓 |
| HyperDivider | 1dp；虚线 4dp + 4dp | type、axis、indent/endIndent/length |
| HyperButton | 高 32 / 38 / 44，radius 高度的一半 | 字号 12 / 13 / 14sp；默认标签图标 17dp、纯图标 20dp |
| HyperTextField | radius 20；字号 14 / 16 / 18；padding 14×7 / 18×9 / 20×11 | 1dp 普通轮廓、1.5dp 聚焦；多行纵向 +3dp |
| HyperCheckbox | 44dp 触摸、21dp 方框、radius 7、轮廓 1.5 | nullable 三态、勾 15dp、160ms 状态动画 |
| HyperRadio | 独立交互 44dp 触摸、展示 21dp 圆环、轮廓 1.5 / 6 | 标签行最小 46dp，不叠加触摸留白；160ms 选中颜色和环宽动画 |
| HyperSwitch | 52×32，padding 4 + Container 边框 1 | 实际滑块 22dp，160ms selectionCurve |
| HyperSlider | 44dp 触摸、6dp 轨道、20dp 滑块、2dp 轮廓 | 默认范围 0..100，showValue=true |
| HyperSegmentedControl | radius 18，padding 4、条目 32，总高 40 | 13sp，选中 600、未选中 500，图标 16dp；等宽底板平移 260ms |
| HyperTabs | radius 27，padding 3、条目 48，总高 54 | 单一共享表面，44dp 选中底面，滚动项目最小 76dp |
| HyperTabBar | 高 56，单元格最小 76，额外宽 8 | single / multiple；图标 22 / 23；指示器 160ms easeOutCubic |
| HyperNavBar | 高 44，水平 padding 16，标题 16/600 | 透明操作层，标题字距 -0.2sp、副标题 11sp、安全区另计 |
| HyperListTile | plain 最小 46，padding 14×8；外层 glass padding 3 | auto/plain/glass，标题 14/600、说明 12、meta 11，图标块 34 |
| HyperMenuGroup | radius 18，padding 4，分隔缩进 14 | items 入口自动分隔；自由内容入口显式组合分隔 |
| HyperCard | radius 24，padding 12，头/正文/页脚间距 12 | 标题 14/600，副标题 13，selected strong 与强调轮廓 |
| HyperDropdown | 字段 minHeight 48，radius 16，padding 14×11；自动宽度 clamp(最长文案 + 68,160,240) | width 可显式指定，菜单匹配字段实际宽度；radius 18、padding 5、默认最大高 320 |
| HyperPopover | 默认宽 180，width 可显式指定，maxWidth 280 仅作上限；高窗口 40%，padding 14 | 真实锚点、6dp 间距、12dp 安全边距、自动翻转 |
| HyperTooltip | radius 12，padding 10×7，12sp/1.3 | 500ms 悬停/长按；焦点即时；125ms 0.97→1 缩放淡入 |
| HyperDialog | min(maxWidth,实际窗口宽度-48)，maxWidth 420，窗口间距 24，radius 26，padding 18 | 标题 20/600、正文 10、操作区 18、操作间距 8 |
| HyperDrawer | 两侧宽 320，外侧最小安全间距 12，radius 30 | strong 材质，280ms、窗口 12% 位移；正文 padding 16 |
| HyperSlideMenu | 每动作 64，边界阻力 0.32，释放阈值 35% | 0.09s 速度投影、stiffness 470/damping 42 |
| HyperProgress | linear 4/6/12；circle 48/64/80、stroke 4/6/8 | showLabel=true；线性动画 1400ms、圆环 1100ms |
| HyperEmptyState | 图标块 56、radius 24、图标 24、padding 20 | 标题 16/600、说明 13/1.45、间距 12/8/12 |
| HyperToast | minWidth 180/maxWidth 420，radius 18，padding 12/10/8/10 | 30dp 图标块、17dp 图标、14sp/500/1.35、默认 2000ms |
| HyperUpdateDialog | maxWidth 420，模态安全区外间距 20，radius 28，padding 24，非错误状态标识块 40、图标 20、环形进度 30 | 标题 24/1.2、说明 14/1.55、进度 28/1.2；其余文字按主题 1.4 行高随字号换算。检查中隐藏版本；错误仅保留原因、重试与关闭 |

HyperGlass 的 DecoratedBox 边框只绘制，不增加布局尺寸。Container 控件按各自固定尺寸和隐式边框内边距处理。普通按压为 0.975 缩放、0.92 透明度，85ms 进入、180ms 恢复，曲线 Cubic(0.23,1,0.32,1)。模态弹窗为 250ms、0.96→1 缩放；锚定浮层为 180ms。主题令牌详见 [主题配置](theme.md)。

图标轮廓从参考 Preview 实际解析的 lucide_icons_flutter 3.1.24 默认字体提取，保留原始填充轮廓与 em 坐标；Android 与 Wasm 共用同一份 ImageVector。

## API 约定

| 组件 | 当前明确入口 |
| --- | --- |
| HyperThemeConfig | material = HyperMaterial.Solid / Soft / Clear；glass 可显式覆盖 |
| HyperDropdown | options: List&lt;HyperOption&lt;T&gt;&gt;，value，onChanged；临时 expanded 在组件内部 |
| HyperPopover | visible、onClose、child、content；内容贴触发点定位 |
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
- core/icon/HyperReferenceIcons.kt 共享参考版本 Lucide 字体轮廓，HyperIcon.kt 继承插槽尺寸。
- core/surface/ 提供玻璃、背景采样与阴影；边框和内容间距分开管理。
- components/menu/HyperMenuSizing.kt 计算默认紧凑文字宽度；components/popup/HyperAnchoredSurface.kt 共用于 Dropdown、Popover，处理实际字段宽度、安全区、键盘和上下翻转。
- components/update/HyperUpdatePresentation.kt 只映射显示数据，HyperUpdateContent.kt 组合正文与操作区，HyperUpdateLayout.kt 共用内嵌面板与模态宿主布局，HyperUpdateDialog.kt 承担受控入口。
- preview/ui/UpdateDialogShowcase.kt 对齐 Flutter 更新流程与状态预览；UpdateDemoController.kt 只持有页面的模拟任务，关闭弹层不会取消下载。
- preview 的 ComponentDemos 聚合各分组元数据，*ComponentShowcases 提供可操作的大小、禁用、选中与进度状态。
- ComponentPreviewViewport 统一有限视口与滚动归属；PageListShowcases 为页面级列表提供剩余高度，PreviewSectionLabel 统一示例分组与操作行。
- component-doc.css 负责文档层级、折叠源码与参数表排版；MarkdownDocument 让桌面 Preview 读取相同 Markdown 并呈现折叠内容。

## 视觉核对

两端在相同设备、密度、字号缩放、安全区与字体配置下核对外观。检查浅色和深色、三种材质、320–430dp 宽度，以及禁用、聚焦、错误、选中、加载、键盘展开、RTL 和长文本。

组件预览使用当前源码生成的产物。修改文档正文只更新文档，组件画面由 Preview 产物提供；核对时确认产物版本。列表应能滚动到最后一项，输入框清空与前后缀可同时使用，完整参数表保持可见。

## 依赖

使用仓库声明的 Compose foundation/ui/runtime/animation 与 Haze；调用方可选的 Lucide Android 图标资源依赖由使用者准备，版本以构建配置为准。
