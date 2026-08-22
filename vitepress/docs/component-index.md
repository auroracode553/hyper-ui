# 组件索引

所有组件的公开包名均为 `hyper_ui`。状态归属只描述业务状态；滚动等内部 UI 状态不需要调用方管理。所有组件状态均即时渲染，不执行动画。组件外壳尺寸和外部间距统一通过 `modifier` 控制，具体内部节点使用组件签名中明确提供的 `contentModifier`、`drawerModifier`、`inputModifier` 等修饰符。

## 基础组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperButton](components/basic/hyper-button.md) | 不透明实色的 Slot-first 按钮容器 | 调用方处理点击 |
| [HyperIconButton](components/basic/hyper-icon-button.md) | Slot-first 磨砂玻璃图标按钮，内置柔和折射边缘与单层悬浮阴影 | 调用方处理点击 |

## 表单组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperTextField](components/form/hyper-text-field.md) | 紧凑文本输入，已有文本首次聚焦默认光标置末尾 | 调用方提供 `value` |
| [HyperSwitch](components/form/hyper-switch.md) | 开关，轨道和滑块默认有轮廓层次 | 调用方提供 `checked` |
| [HyperCheckbox](components/form/hyper-checkbox.md) | 多选项 | 调用方提供 `checked` |
| [HyperRadio](components/form/hyper-radio.md) | 单选项 | 调用方提供 `selected` |
| [HyperSegmented](components/form/hyper-segmented.md) | 等宽分段控制器，选中项为实色抬升面板 | 调用方提供 `selectedItem` |
| [HyperSlider](components/form/hyper-slider.md) | 支持连续/分段轨道、指定标记、只读态与三层圆点的受控滑块 | 调用方提供 `value` |

## 容器组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperPanel](components/container/hyper-panel.md) | 圆角内容面板，默认带轻描边和 16dp 内容留白 | 无业务状态 |
| [HyperColorPicker](components/container/hyper-color-picker.md) | 响应式颜色选择板，色块默认带细描边 | 调用方提供 `selectedId` |

## 导航组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperNavBar](components/navigation/hyper-nav-bar.md) | 默认透明、继承页面背景的顶部栏三段 slot 容器 | 调用方处理事件 |
| [HyperImmersiveNavBar](components/navigation/hyper-immersive-nav-bar.md) | 固定透明导航操作层，首屏避让、滚动后内容进入导航栏与状态栏后方 | 调用方持有滚动状态并消费组件返回的内容 Padding |
| [HyperDrawer](components/navigation/hyper-drawer.md) | 深色模式带低对比度柔光轮廓，默认提供方向化间距与系统安全区且可关闭、无遮罩、无动画的四方向抽屉 | 调用方提供 `open`、默认 Padding 策略、附加间距、选中态与嵌套列表滚动策略 |
| [HyperSlideMenu](components/navigation/hyper-slide-menu.md) | 横向分组菜单，未选中项默认带细描边 | 调用方提供 `selectedItem` |
| [HyperTabBar](components/navigation/hyper-tab-bar.md) | 使用低对比度顶部发丝线、无阴影与玻璃高光，总高 60dp 的贴底栏容器 | 调用方处理内容、选择与导航 |

## 列表组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperList](components/list/hyper-list.md) | 固定使用 LazyColumn、支持可滚动 contentPadding 的页面级 Slot 懒列表 | 调用方提供 LazyListScope 项目与可选滚动状态 |
| [HyperSectionedList](components/list/hyper-sectioned-list.md) | 日期、历史等动态数据的分段懒列表 | 调用方提供分组数据、稳定 key 与行内容 |
| [HyperMenuList](components/list/hyper-menu-list.md) | 仅用于少量菜单、设置项和操作入口的圆角菜单容器 | 调用方提供菜单 Slot；不用于数据列表 |
| [HyperListItem](components/list/hyper-list-item.md) | 单行 44dp、带说明 54dp 的自适应实色列表项 | 调用方处理点击和尾部状态 |

## 反馈组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperEmptyState](components/feedback/hyper-empty-state.md) | 使用居中 HyperPanel 承载的页面级空数据状态 | 调用方提供文案、图标与可选操作 |
| [HyperDropdown](components/feedback/hyper-dropdown.md) | Popup 浮层菜单，使用不透明实色面板 | 调用方提供 `expanded` |
| [hyperToast](components/feedback/hyper-toast.md) | Android 原生 Toast 的线程安全封装 | 调用即显示，无持久业务状态 |
| [HyperProgressIndicator](components/feedback/hyper-progress-indicator.md) | 无动画、不透明实色的线性/圆形进度指示器 | 调用方提供 `progress` |
| [HyperLevelCapsule](components/feedback/hyper-level-capsule.md) | 柔性玻璃竖向比例反馈，支持图标插槽 | 调用方提供 `progress`、文案、图标与显示时机 |
| [HyperPlaybackSpeedScale](components/feedback/hyper-playback-speed-scale.md) | 复用 HyperSlider 分段视觉的长按临时加速玻璃刻度 | 调用方提供当前速度，并处理长按与横向手势 |
| [HyperBatteryIndicator](components/feedback/hyper-battery-indicator.md) | 百分比内显、充电闪电外置的紧凑电池图标 | 调用方提供电量和充电状态 |
| [HyperPopup](components/feedback/hyper-popup.md) | 最大高度为窗口 70%、长内容可滚动的居中内容浮层 | 调用方提供 `visible` 和内容状态 |
| [HyperDialog](components/feedback/hyper-dialog.md) | 保留平台标准模态背景与窗口行为的 Compose 对话框 | 调用方提供 `visible` 并处理结果 |
| [HyperAlertDialog](components/feedback/hyper-alert-dialog.md) | 遵循平台窗口约束、正文可滚动的 Slot-first Alert 弹窗 | 调用方提供 `visible` 并处理结果 |
| [HyperUpdateDialog](components/feedback/hyper-update-dialog.md) | 使用标准模态窗口的应用更新状态与下载确认弹窗 | 调用方持有状态，并注入 Release 加载和下载动作 |

## Android 系统工具

| 工具 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperBatteryState](components/tools/hyper-battery-state.md) | 一次性读取或生命周期安全订阅 Android 电量与充电状态 | 工具读取系统广播，调用方决定如何展示 |
