# 组件索引

所有组件的公开包名均为 `hyper_ui`。状态归属只描述业务状态；动画、滚动等内部 UI 状态不需要调用方管理。组件外壳尺寸和外部间距统一通过 `modifier` 控制，具体内部节点使用组件签名中明确提供的 `contentModifier`、`drawerModifier`、`inputModifier` 等修饰符。

## 基础组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperButton](components/basic/hyper-button.md) | 不透明实色的 Slot-first 按钮容器 | 调用方处理点击 |
| [HyperIconButton](components/basic/hyper-icon-button.md) | Slot-first 不透明实色图标按钮容器 | 调用方处理点击 |

## 表单组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperTextField](components/form/hyper-text-field.md) | 文本输入，默认实色背景和轻描边 | 调用方提供 `value` |
| [HyperSwitch](components/form/hyper-switch.md) | 开关，轨道和滑块默认有轮廓层次 | 调用方提供 `checked` |
| [HyperCheckbox](components/form/hyper-checkbox.md) | 多选项 | 调用方提供 `checked` |
| [HyperRadioButton](components/form/hyper-radio-button.md) | 单选项 | 调用方提供 `selected` |
| [HyperSlider](components/form/hyper-slider.md) | 可点击、可拖动的进度与范围滑块 | 调用方提供 `value` |

## 容器组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperPanel](components/container/hyper-panel.md) | 圆角内容面板，默认带轻描边 | 无业务状态 |
| [HyperColorPicker](components/container/hyper-color-picker.md) | 响应式颜色选择板，色块默认带细描边 | 调用方提供 `selectedId` |

## 导航组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperTopBar](components/navigation/hyper-top-bar.md) | 不透明卡片背景的顶部栏三段 slot 容器 | 调用方处理事件 |
| [HyperDrawer](components/navigation/hyper-drawer.md) | 不透明实色、无遮罩的四方向 slot 抽屉 | 调用方提供 `open` 与选中态 |
| [HyperGroupMenus](components/navigation/hyper-group-menus.md) | 横向分组菜单，未选中项默认带细描边 | 调用方提供 `selectedItem` |
| [HyperBottomBar](components/navigation/hyper-bottom-bar.md) | 浅色透明、深色不透明的底部栏容器 | 调用方处理内容、选择与导航 |

## 列表组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperList](components/list/hyper-list.md) | 不透明实色的轻圆角页面级列表，可切换懒加载或普通列表 | 调用方提供数据 |
| [HyperMenuList](components/list/hyper-menu-list.md) | 不透明实色的圆角菜单列表和设置分组 | 调用方提供数据或内容 |
| [HyperListItem](components/list/hyper-list-item.md) | 不透明实色的列表行内容 | 调用方处理点击和尾部状态 |

## 反馈组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperDropdownMenu](components/feedback/hyper-dropdown-menu.md) | Popup 浮层菜单，使用不透明实色面板 | 调用方提供 `expanded` |
| [HyperProgressIndicator](components/feedback/hyper-progress-indicator.md) | 不透明实色的线性/圆形进度指示器 | 调用方提供 `progress` |
| [HyperDialog](components/feedback/hyper-dialog.md) | 无动画、全程不透明并支持固定标题的内容弹窗 | 调用方提供 `visible` 和内容状态 |
| [HyperAlertDialog](components/feedback/hyper-alert-dialog.md) | 全程不透明的 Slot-first Alert 弹窗 | 调用方提供 `visible` 并处理结果 |
| [HyperUpdateDialog](components/feedback/hyper-update-dialog.md) | 全程不透明的应用更新状态与下载确认弹窗 | 调用方持有状态，并注入 Release 加载和下载动作 |
