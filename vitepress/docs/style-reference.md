# doc_ui_kit 样式对照

本次参考源为 D:/my_project/flutter_project/doc_ui_kit/ui/lib/src/components 与同项目 theme。源码默认值是样式依据；Android 手机端目标为 320–430dp 竖屏。Desktop/Wasm 只用于文档 Preview。

## 同名组件参数

| 组件 | 参考默认值 | Compose 对应 |
| --- | --- | --- |
| HyperText | 12 / 14 / 18，行高 1.4 | size 三档；插槽继承局部排版 |
| HyperIcon | 18 / 24 / 32，插槽图标另定 | size 三档，共享 Lucide 字体原始轮廓 |
| HyperDivider | 1dp；虚线 4dp + 4dp | type、orientation、indent/endIndent/length |
| HyperButton | 高 32 / 38 / 44，radius 高度的一半 | 字号 12 / 13 / 14sp；默认标签图标 17dp、纯图标 20dp |
| HyperTextField | radius 20；字号 14 / 16 / 18；padding 14×7 / 18×9 / 20×11 | 1dp 普通轮廓、1.5dp 聚焦；多行纵向 +3dp |
| HyperCheckbox | 44dp 触摸、21dp 方框、radius 7、轮廓 1.5 | nullable 三态、勾 15dp、160ms 状态动画 |
| HyperRadio | 44dp 触摸、21dp 圆环、轮廓 1.5 / 6 | 160ms 选中颜色和环宽动画 |
| HyperSwitch | 52×32，padding 4 + Container 边框 1 | 实际滑块 22dp，160ms selectionCurve |
| HyperSlider | 44dp 触摸、6dp 轨道、20dp 滑块、2dp 轮廓 | 默认范围 0..100，showValue=true |
| HyperSegmentedControl | radius 18，padding 4、条目 32，总高 40 | 13sp，选中 600、未选中 500，图标 16dp；等宽底板平移 260ms |
| HyperTabs | radius 27，padding 3、条目 48，总高 54 | 单一共享表面，44dp 选中底面，滚动项目最小 76dp |
| HyperTabBar | 高 56，单元格最小 76，额外宽 8 | single / multiple；图标 22 / 23；指示器 160ms easeOutCubic |
| HyperNavBar | 高 44，水平 padding 16，标题 16/600 | 透明操作层，标题字距 -0.2sp、副标题 11sp、安全区另计 |
| HyperListTile | plain 最小 46，padding 14×8；外层 glass padding 3 | auto/plain/glass，标题 14/600、说明 12、meta 11，图标块 34 |
| HyperMenuGroup | radius 18，padding 4，分隔缩进 14 | items 入口自动分隔；自由内容入口显式组合分隔 |
| HyperCard | radius 24，padding 12，头/正文/页脚间距 12 | 标题 14/600，副标题 13，selected strong 与强调轮廓 |
| HyperDropdown | 字段 minHeight 48，radius 16，padding 14×11 | 受控选项选择，菜单 radius 18、padding 5、默认最大高 320 |
| HyperPopover | 宽 min(屏幕-32,280)，高窗口 40%，padding 14 | 真实锚点、6dp 间距、12dp 安全边距、自动翻转 |
| HyperTooltip | radius 12，padding 10×7，12sp/1.3 | 500ms 悬停/长按；焦点即时；125ms 0.97→1 缩放淡入 |
| HyperDialog | maxWidth 420，窗口间距 24，radius 26，padding 18 | 标题 20/600、正文 10、操作区 18、操作间距 8 |
| HyperDrawer | 两侧宽 320，外侧最小安全间距 12，radius 30 | strong 材质，280ms、窗口 12% 位移；正文 padding 16 |
| HyperSlideMenu | 每动作 64，边界阻力 0.32，释放阈值 35% | 0.09s 速度投影、stiffness 470/damping 42 |
| HyperProgress | linear 4/6/12；circle 48/64/80、stroke 4/6/8 | showLabel=true；线性动画 1400ms、圆环 1100ms |
| HyperEmptyState | 图标块 56、radius 24、图标 24、padding 20 | 标题 16/600、说明 13/1.45、间距 12/8/12 |
| HyperToast | minWidth 180/maxWidth 420，radius 18，padding 12/10/8/10 | 30dp 图标块、17dp 图标、14sp/500/1.35、默认 2000ms |
| HyperUpdateDialog | maxWidth 420，radius 28，padding 24，标识 64 | 标题 24、进度 28、版本信息与摘要、固定操作区 |

HyperGlass 的 DecoratedBox 边框只绘制，不增加布局尺寸。Container 控件按各自固定尺寸和隐式边框内边距处理。普通按压为 0.975 缩放、0.92 透明度，85ms 进入、180ms 恢复，曲线 Cubic(0.23,1,0.32,1)。模态弹窗为 250ms、0.96→1 缩放；锚定浮层为 180ms。主题令牌详见 [主题配置](theme.md)。

图标轮廓从参考 Preview 实际解析的 lucide_icons_flutter 3.1.24 默认字体提取，保留原始填充轮廓与 em 坐标；Android 与 Wasm 共用同一份 ImageVector。

## API 迁移

| 组件 | 当前明确入口 |
| --- | --- |
| HyperThemeConfig | material = HyperMaterial.Solid / Soft / Clear；glass 可显式覆盖 |
| HyperDropdown | options: List&lt;HyperOption&lt;T&gt;&gt;，value，onValueChange；临时 expanded 在组件内部 |
| HyperPopover | visible、onDismissRequest、anchorContent、content；内容贴触发点定位 |
| HyperTabs | items、selectedItem、onSelected、scrollable；共享表面 |
| HyperTabBar | type=single/multiple、itemLabel、itemSelected、onItemClick；单动作角标 badgeCount |
| HyperCheckbox | checked: Boolean?、onCheckedChange: (Boolean?)-&gt;Unit、tristate |
| HyperText | size=small/default/large；局部插槽样式优先 |
| HyperIcon | size=small/default/large；default 继承插槽尺寸 |
| HyperSlider | 默认 0..100、showValue=true；删除旧中心点/光晕颜色配置 |
| HyperUpdateDialog | Downloading/ReadyToInstall/Installing/Completed、mandatory、onInstall；业务执行由调用方承担 |

全部公开 API 在 hyper_ui 包；完整签名和参数默认值见 [组件索引](component-index.md)。原项目特有的电量、倍速、颜色选择、懒列表等组件继续使用现有 API，并跟随统一主题；参考项目没有这些同名组件，未虚构对应尺寸。

## 文件职责

- theme/HyperGlassTokens.kt 统一三档材质，theme/HyperStyle.kt 提供模式、语义色与排版。
- core/interaction/HyperMotionSpec.kt 统一按压、选择和浮层的曲线与时长。
- core/icon/HyperReferenceIcons.kt 共享参考版本 Lucide 字体轮廓，HyperIcon.kt 继承插槽尺寸。
- core/surface/ 提供玻璃、背景采样与阴影；边框和内容间距分开管理。
- components/popup/HyperAnchoredSurface.kt 共用于 Dropdown、Popover，处理安全区、键盘和上下翻转。
- components/update/HyperUpdatePresentation.kt 只映射显示数据，HyperUpdateContent.kt 组合内容与操作区，HyperUpdateDialog.kt 承担受控入口。
- preview 的 ComponentDemos 聚合各分组元数据，*ComponentShowcases 提供可操作的大小、禁用、选中与进度状态。

## 验收状态与手动说明

已按参考源码核对参数，并对 91 个 Kotlin 源文件、37 个 Preview 入口和 33 个共享图标进行静态检查：调用参数、项目内导入、文档签名、完整 Props 表、Preview ID、颜色格式、文件规模与差异格式均未发现问题。静态检查不能替代编译和运行验收。遵照 AGENTS.md，未运行、编译、打包、部署或安装依赖。

因此当前结论是源码参数与结构对齐，尚不能声明经过验证的像素 100% 一致。Flutter 与 Compose 的字体度量、阴影和背景模糊后端不同，需要在相同 Android 设备、密度、字号缩放、安全区与字体配置下，分别截图核对。现有 Wasm 静态产物也未重新生成，页面可能仍显示此前版本；请在允许构建的环境中自主重新生成 Preview 产物。

建议按浅/深色 × Solid/Soft/Clear，依次检查 320/360/390/430dp、禁用/聚焦/错误/选中/加载、键盘展开、RTL 与长文本。不附自动构建命令或启动脚本。

## 依赖

本次未新增或安装依赖。继续使用仓库声明的 Compose foundation/ui/runtime/animation 与 Haze 1.7.1；调用方可选的 Lucide Android 图标资源依赖需自行安装。内置共享路径的许可保存在 library/NOTICE-LUCIDE.txt。
