# 组件索引

所有组件的公开包名均为 `hyper_ui`。状态归属只描述业务状态；滚动等内部 UI 状态不需要调用方管理。所有组件状态均即时渲染，不执行动画。组件外壳尺寸和外部间距统一通过 `modifier` 控制，具体内部节点使用组件签名中明确提供的 `contentModifier`、`drawerModifier`、`inputModifier` 等修饰符。

## 基础组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperButton](components/basic/hyper-button.md) | 不透明实色的 Slot-first 按钮容器 | 调用方处理点击 |
| [HyperIconButton](components/basic/hyper-icon-button.md) | Slot-first 不透明实色图标按钮容器 | 调用方处理点击 |

## 表单组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperTextField](components/form/hyper-text-field.md) | 紧凑文本输入，默认实色背景、轻描边与 40dp 最小高度 | 调用方提供 `value` |
| [HyperSwitch](components/form/hyper-switch.md) | 开关，轨道和滑块默认有轮廓层次 | 调用方提供 `checked` |
| [HyperCheckbox](components/form/hyper-checkbox.md) | 多选项 | 调用方提供 `checked` |
| [HyperRadio](components/form/hyper-radio.md) | 单选项 | 调用方提供 `selected` |
| [HyperSegmented](components/form/hyper-segmented.md) | 等宽分段控制器，选中项为实色抬升面板 | 调用方提供 `selectedItem` |
| [HyperSlider](components/form/hyper-slider.md) | 可点击、可拖动的进度与范围滑块 | 调用方提供 `value` |

## 容器组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperPanel](components/container/hyper-panel.md) | 圆角内容面板，默认带轻描边和 16dp 内容留白 | 无业务状态 |
| [HyperColorPicker](components/container/hyper-color-picker.md) | 响应式颜色选择板，色块默认带细描边 | 调用方提供 `selectedId` |

## 导航组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperNavBar](components/navigation/hyper-nav-bar.md) | 默认透明、继承页面背景的顶部栏三段 slot 容器 | 调用方处理事件 |
| [HyperDrawer](components/navigation/hyper-drawer.md) | 深色与页面同色、浅色轻微透明、无玻璃高光、无遮罩、无动画的四方向 slot 抽屉 | 调用方提供 `open`、选中态与嵌套列表滚动策略 |
| [HyperSlideMenu](components/navigation/hyper-slide-menu.md) | 横向分组菜单，未选中项默认带细描边 | 调用方提供 `selectedItem` |
| [HyperTabBar](components/navigation/hyper-tab-bar.md) | 深色与页面同色、浅色轻量透明且无玻璃高光，总高 60dp 的底部栏容器 | 调用方处理内容、选择与导航 |

## 列表组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperList](components/list/hyper-list.md) | 固定使用 LazyColumn 的页面级 Slot 懒列表 | 调用方提供 LazyListScope 项目 |
| [HyperMenuList](components/list/hyper-menu-list.md) | 仅用于少量菜单、设置项和操作入口的圆角菜单容器 | 调用方提供菜单 Slot；不用于数据列表 |
| [HyperListItem](components/list/hyper-list-item.md) | 单行 44dp、带说明 54dp 的自适应实色列表项 | 调用方处理点击和尾部状态 |

## 反馈组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperDropdown](components/feedback/hyper-dropdown.md) | Popup 浮层菜单，使用不透明实色面板 | 调用方提供 `expanded` |
| [hyperToast](components/feedback/hyper-toast.md) | Android 原生 Toast 的线程安全封装 | 调用即显示，无持久业务状态 |
| [HyperProgressIndicator](components/feedback/hyper-progress-indicator.md) | 无动画、不透明实色的线性/圆形进度指示器 | 调用方提供 `progress` |
| [HyperPopup](components/feedback/hyper-popup.md) | 最大高度为窗口 70%、长内容可滚动的居中内容浮层 | 调用方提供 `visible` 和内容状态 |
| [HyperAlertDialog](components/feedback/hyper-alert-dialog.md) | 最大高度为窗口 70%、正文可滚动的 Slot-first Alert 弹窗 | 调用方提供 `visible` 并处理结果 |
| [HyperUpdateDialog](components/feedback/hyper-update-dialog.md) | 最大高度为窗口 70% 的应用更新状态与下载确认弹窗 | 调用方持有状态，并注入 Release 加载和下载动作 |
