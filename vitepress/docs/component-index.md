# 组件索引

所有组件的公开包名均为 `hyper_ui`。状态归属只描述业务状态；动画、滚动等内部 UI 状态不需要调用方管理。

## 基础组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperButton](components/basic/hyper-button.md) | Slot-first 按钮容器 | 调用方处理点击 |
| [HyperIconButton](components/basic/hyper-icon-button.md) | Slot-first 图标按钮容器 | 调用方处理点击 |

## 表单组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperTextField](components/form/hyper-text-field.md) | 文本输入 | 调用方提供 `value` |
| [HyperSwitch](components/form/hyper-switch.md) | 开关 | 调用方提供 `checked` |
| [HyperCheckbox](components/form/hyper-checkbox.md) | 多选项 | 调用方提供 `checked` |
| [HyperRadioButton](components/form/hyper-radio-button.md) | 单选项 | 调用方提供 `selected` |
| [HyperChip](components/form/hyper-chip.md) | Slot-first 标签与标签栏 | 调用方提供 `selectedItem` |

## 容器组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperPanel](components/container/hyper-panel.md) | 圆角内容面板 | 无业务状态 |
| [HyperColorPicker](components/container/hyper-color-picker.md) | 响应式颜色选择板 | 调用方提供 `selectedId` |

## 导航组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperTopBar](components/navigation/hyper-top-bar.md) | 顶部栏三段 slot 容器 | 调用方处理事件 |
| [HyperDrawer](components/navigation/hyper-drawer.md) | 四方向 slot 抽屉 | 调用方提供 `open` 与选中态 |
| [HyperBottomBar](components/navigation/hyper-bottom-bar.md) | 泛型底部栏容器 | 调用方处理选择与导航 |

## 列表组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperLazyList](components/list/hyper-lazy-list.md) | 大量动态数据的懒加载列表 | 调用方提供数据 |
| [HyperList](components/list/hyper-list.md) | 少量静态数据的滚动列表 | 调用方提供数据 |
| [HyperListItem](components/list/hyper-list-item.md) | 列表行内容 | 调用方处理点击和尾部状态 |
| [HyperMenuGroup](components/list/hyper-menu-group.md) | 设置类菜单分组与菜单项 | 调用方处理点击和尾部状态 |

## 反馈组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperDropdownMenu](components/feedback/hyper-dropdown-menu.md) | Popup 浮层菜单 | 调用方提供 `expanded` |
| [HyperProgressIndicator](components/feedback/hyper-progress-indicator.md) | 线性/圆形进度指示器 | 调用方提供 `progress` |
| [HyperDialog](components/feedback/hyper-dialog.md) | 自定义内容弹窗 | 调用方提供 `visible` 和内容状态 |
| [HyperAlertDialog](components/feedback/hyper-alert-dialog.md) | Slot-first Alert 弹窗结构 | 调用方提供 `visible` 并处理结果 |
