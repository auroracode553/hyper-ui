# 组件索引

所有组件的公开包名均为 `hyper_ui`。状态归属只描述业务状态；动画、滚动等内部 UI 状态不需要调用方管理。

## 基础组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperButton](components/basic/hyper-button.md) | 六种语义变体的文字按钮 | 调用方处理点击 |
| [HyperIconButton](components/basic/hyper-icon-button.md) | 圆形图标按钮 | 调用方处理点击 |

## 表单组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperTextField](components/form/hyper-text-field.md) | 文本输入 | 调用方提供 `value` |
| [HyperSearchField](components/form/hyper-search-field.md) | 搜索输入与清空 | 调用方提供 `value` |
| [HyperSwitch](components/form/hyper-switch.md) | 开关 | 调用方提供 `checked` |
| [HyperCheckbox](components/form/hyper-checkbox.md) | 多选项 | 调用方提供 `checked` |
| [HyperRadioButton](components/form/hyper-radio-button.md) | 单选项 | 调用方提供 `selected` |
| [HyperFilterChip](components/form/hyper-filter-chip.md) | 横向滚动筛选标签与标签栏 | 调用方提供 `selectedKey` |

## 容器组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperPanel](components/container/hyper-panel.md) | 圆角内容面板 | 无业务状态 |
| [HyperColorPicker](components/container/hyper-color-picker.md) | 响应式颜色选择板 | 调用方提供 `selectedId` |

## 导航组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperTopBar](components/navigation/hyper-top-bar.md) | 页面标题、返回与右侧操作 | 调用方处理事件 |
| [HyperDrawer](components/navigation/hyper-drawer.md) | 四方向抽屉及抽屉条目 | 调用方提供 `open` 与选中态 |
| [HyperBottomBar](components/navigation/hyper-bottom-bar.md) | 底部导航栏 | 调用方提供 `selectedItemId` 并执行导航 |

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
| [HyperProgressBar](components/feedback/hyper-progress-bar.md) | 确定或不确定进度 | 调用方提供 `progress` |
| [HyperDialog](components/feedback/hyper-dialog.md) | 自定义内容弹窗 | 调用方提供 `show` 和内容状态 |
| [HyperConfirmDialog](components/feedback/hyper-confirm-dialog.md) | 确认/取消弹窗 | 调用方提供 `show` 并处理结果 |
