# 组件索引

修改组件前必须阅读 [UI 样式与功能实现基线](style-reference.md)，核对已有专用样式、状态归属和平台差异。不得仅因“统一”替换当前布局或操作样式；拿不准时先向使用者确认。

所有组件从 `hyper_ui` 包导入。组件页按用途、交互示例、最小调用、使用约定和公开 API 组织。长签名与辅助配置可以展开查看，五列 Props 表完整保留；手机预览支持切换 320/350/390/430dp 宽度和重置。

HyperList 与 HyperSectionedList 使用整屏视口，顶部控件下方的列表独自滚动；HyperMenuGroup 和 HyperListTile 按内容测量，由预览宿主负责整页滚动。
HyperListTile 预览直接展示条目，不额外套 HyperCard；弹窗只读详情使用 HyperListTile(type = "plain") 的文字插槽，不逐项包装卡片。
手机预览的底部手势条悬浮覆盖在内容上，宿主不预留底部安全区，与 Flutter 参考预览一致。

## 基础

| 组件 | 用途 |
| --- | --- |
| [HyperText](components/basic/hyper-text.md) | 12 / 14 / 18sp 文字 |
| [HyperIcon](components/basic/hyper-icon.md) | 18 / 24 / 32dp 图标与插槽继承 |
| [HyperDivider](components/basic/hyper-divider.md) | 实线、虚线和方向缩进 |
| [HyperButton](components/basic/hyper-button.md) | 默认白色描边；type 控制 filled/icon，color/colorStyle 控制配色；预览分组与 Flutter 一致，禁用态保留单个示例 |

## 表单

[HyperCalendarDay](components/form/hyper-calendar-day.md) 提供通用日期格、角标和轻量条目；[HyperCalendarMonth](components/form/hyper-calendar-month.md) 提供内嵌、紧凑与只读月历，与日期选择器共用实现。

日期格最多保留三行条目，溢出数量单独占最后一行；紧凑留白保留完整文字行高，默认缩放下 large 预留三行需 96dp。内嵌月历的 emphasizedDays 允许宿主指定主题色日期。

| 组件 | 用途 |
| --- | --- |
| [HyperTextField](components/form/hyper-text-field.md) | 文本、密码和多行输入 |
| [HyperSwitch](components/form/hyper-switch.md) | 开关；plain 标签行不附带卡片 |
| [HyperCheckbox](components/form/hyper-checkbox.md) | 多选；plain 标签行不附带卡片 |
| [HyperRadio](components/form/hyper-radio.md) | 单选；标签行承载点击，展示圆环不叠加触摸留白 |
| [HyperSegmentedControl](components/form/hyper-segmented-control.md) | 40dp 分段切换，选中底板平移 |
| [HyperSlider](components/form/hyper-slider.md) | 6dp 轨道、20dp 手柄，连续或分段值调节 |
| [HyperDatePicker](components/form/hyper-date-picker.md) | 底部单日期月历、月份选择与年份跳转；默认 1900–2100 年 |

共有组件的当前约定：HyperButton 主体插槽为 content；Checkbox 标签行只展示 21dp 方框；Card 选中边框使用完整强调色；日期格与只读月历共用范围底色；ListTile 尾部间距 8dp；Tabs 重复选中不回调；SlideMenu 携带释放速度吸附；Toast 默认文字操作先关闭再回调。

## 容器

| 组件 | 用途 |
| --- | --- |
| [HyperBackdrop](components/container/hyper-backdrop.md) | 玻璃背景采样 |
| [HyperCard](components/container/hyper-card.md) | 结构化内容容器 |
| [HyperColorPicker](components/container/hyper-color-picker.md) | 颜色选择 |

## 导航

| 组件 | 用途 |
| --- | --- |
| [HyperNavBar](components/navigation/hyper-nav-bar.md) | 库内观察同一主滚动状态，active 停用隐藏或空视口，仅导航读取派生值；背景在状态栏顶部至导航底部内渐隐，不改页面布局 |
| [HyperTabBar](components/navigation/hyper-tab-bar.md) | 悬浮内容高 56dp，贴底总高 60dp；single / multiple、可重复动作与单项禁用 |
| [HyperScaffold](components/navigation/hyper-scaffold.md) | 基础页面宿主；实际栏高、正文净空与悬浮操作，不依赖 Material |
| [HyperDrawer](components/navigation/hyper-drawer.md) | 贴边全高模态抽屉；固定标题／页脚，支持遮罩、返回键与滑动关闭 |
| [HyperSlideMenu](components/navigation/hyper-slide-menu.md) | 列表项侧滑操作 |
| [HyperTabs](components/navigation/hyper-tabs.md) | 分类筛选 |

## 列表

| 组件 | 用途 |
| --- | --- |
| [HyperList](components/list/hyper-list.md) | 连续数据列表 |
| [HyperSectionedList](components/list/hyper-sectioned-list.md) | 分组数据列表，固定 8dp 紧凑分段间距 |
| [HyperMenuGroup](components/list/hyper-menu-group.md) | 菜单和设置入口 |
| [HyperListTile](components/list/hyper-list-tile.md) | 设置行和列表行 |

## 反馈

| 组件 | 用途 |
| --- | --- |
| [HyperEmptyState](components/feedback/hyper-empty-state.md) | 空数据或无结果 |
| [HyperDropdown](components/feedback/hyper-dropdown.md) | 48dp 选择字段，默认文案宽度 160–240dp，选项菜单跟随字段 |
| [HyperToast](components/feedback/hyper-toast.md) | 短暂反馈 |
| [HyperProgress](components/feedback/hyper-progress.md) | 线性和圆形进度 |
| [HyperDialog](components/feedback/hyper-dialog.md) | 按实际窗口保留两侧各 24dp、最大宽 420dp 的模态任务面板 |
| [HyperActionSheet / HyperAction](components/feedback/hyper-action-sheet.md) | 底部自定义内容与操作列表，受控显隐、危险／禁用／选中项与取消操作 |
| [HyperAlertDialog](components/feedback/hyper-alert-dialog.md) | 确认和危险操作 |
| [HyperTooltip](components/feedback/hyper-tooltip.md) | 补充提示 |
| [HyperBatteryIndicator](components/feedback/hyper-battery-indicator.md) | 电量显示 |
| [HyperLevelCapsule](components/feedback/hyper-level-capsule.md) | 等级或数值胶囊 |
| [HyperPlaybackSpeedPanel](components/feedback/hyper-playback-speed-panel.md) | 播放速度面板 |
| [HyperPlaybackSpeedScale](components/feedback/hyper-playback-speed-scale.md) | 播放速度刻度 |
| [HyperPopover](components/feedback/hyper-popover.md) | visible / onClose 受控，child 自行触发；默认宽 180dp，width 可显式指定、maxWidth 限制上限；scrollable 可交由有界子列表滚动 |
| [HyperUpdateDialog](components/feedback/hyper-update-dialog.md) | 与 Flutter 共用视觉规则：420dp 最大宽、20dp 弹层外间距、28dp 圆角、24dp 内容边距；统一更新流程与状态预览 |

统一材质、尺寸和 API 约定见 [样式对照](style-reference.md)。

对应组件的参数命名、回调和默认语义遵循 两端统一 API。点击使用 onPressed，值变更使用 onChanged，关闭使用 onClose；平台布局与宿主差异见组件签名。

维护 UI 库和开发业务页面前，优先阅读[使用规则、全局全面屏与 AGENTS.md 模板](mobile-guidelines.md)和[设计系统基线](style-reference.md)。正文卡片统一无普通描边和双层柔阴影，选中轮廓保留；导航材质仅在状态栏顶部至 Navbar 底部内渐隐。既有悬浮 TabBar 的高度、占位与滚动范围不能因配色或卡片优化改变。

导航滚动监听统一由 HyperNavBarPage.scrollState / HyperNavBarScrollScope 管理，Navbar 自动读取；消费页面不得重复声明导航专用 derivedStateOf、snapshotFlow 或 scrolledUnder 判断。库测量实际导航区高度，顶部净空放进滚动内容；主滚动状态保留在容器，嵌套列表不代替主状态。

## 工具

| 工具 | 用途 |
| --- | --- |
| [检查更新](components/tools/hyper-update.md) | APK 静态 latest.json、系统下载、真实进度、恢复与安装；公开配置来自 publish-config.json |
| [HyperBatteryState](components/tools/hyper-battery-state.md) | Android 电量状态读取 |
