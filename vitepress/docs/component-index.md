# 组件索引

所有公开组件都从 `hyper_ui` 包导入。先按页面任务选择组件，再打开组件页确认完整签名；组件页是 API 事实来源，Preview 只用于操作状态和视觉变体。

## 基础组件

| 组件 | 用途 | 主要 API |
| --- | --- | --- |
| [HyperButton](components/basic/hyper-button.md) | 主要、次要、描边、幽灵和危险操作 | `type`、`size`、`loading`、`enabled` |
| [HyperIconButton](components/basic/hyper-icon-button.md) | 紧凑图标操作 | `size`、`enabled`、`colors` |

## 表单组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperTextField](components/form/hyper-text-field.md) | 文本、密码和多行输入 | 调用方持有 `value` |
| [HyperSwitch](components/form/hyper-switch.md) | 开关 | 调用方持有 `checked` |
| [HyperCheckbox](components/form/hyper-checkbox.md) | 多选 | 调用方持有 `checked` |
| [HyperRadio](components/form/hyper-radio.md) | 单选 | 调用方持有 `selected` |
| [HyperSegmented](components/form/hyper-segmented.md) | 同组少量选项切换 | 调用方持有 `selectedItem` |
| [HyperSlider](components/form/hyper-slider.md) | 连续或分段值调节 | 调用方持有 `value` |

## 容器组件

| 组件 | 用途 | 使用建议 |
| --- | --- | --- |
| [HyperBackdrop](components/container/hyper-backdrop.md) | 为玻璃表面提供背景采样 | 放在页面背景和内容之间 |
| [HyperPanel](components/container/hyper-panel.md) | 单个结构化内容面板 | 不要在面板内重复叠加卡片表面 |
| [HyperColorPicker](components/container/hyper-color-picker.md) | 颜色选择 | 由调用方保存选中颜色 |

## 导航组件

| 组件 | 用途 | 主要 API |
| --- | --- | --- |
| [HyperNavBar](components/navigation/hyper-nav-bar.md) | 手机页面顶部标题和操作 | `size`、标题/前后 Slot、安全区 |
| [HyperTabBar](components/navigation/hyper-tab-bar.md) | 贴底或悬浮底部导航 | `type = "docked" / "floating"` |
| [HyperDrawer](components/navigation/hyper-drawer.md) | 四方向任务抽屉 | 调用方持有 `open` |
| [HyperSlideMenu](components/navigation/hyper-slide-menu.md) | 列表项侧滑操作 | 调用方持有 `reveal` |
| [HyperFilterBar](components/navigation/hyper-filter-bar.md) | 横向少量分类筛选 | 调用方持有选中项 |

## 列表组件

| 组件 | 用途 | 使用约束 |
| --- | --- | --- |
| [HyperList](components/list/hyper-list.md) | 连续页面数据 | 使用 `LazyListScope` 提供行内容 |
| [HyperSectionedList](components/list/hyper-sectioned-list.md) | 按日期或类别分组的数据 | 提供稳定 key |
| [HyperMenuList](components/list/hyper-menu-list.md) | 少量菜单和设置入口 | 不用于历史、日志和搜索结果 |
| [HyperListItem](components/list/hyper-list-item.md) | 设置行和列表行 | 点击与尾部状态由调用方处理 |

## 反馈组件

| 组件 | 用途 | 状态归属 |
| --- | --- | --- |
| [HyperEmptyState](components/feedback/hyper-empty-state.md) | 页面空数据或无结果 | 调用方提供文案和操作 |
| [HyperDropdown](components/feedback/hyper-dropdown.md) | 锚定菜单 | 调用方持有 `expanded` |
| [HyperToast](components/feedback/hyper-toast.md) | 短暂操作反馈 | 调用方决定显示和关闭 |
| [HyperProgressIndicator](components/feedback/hyper-progress-indicator.md) | 线性和圆形进度 | 调用方提供 `progress` |
| [HyperDialog](components/feedback/hyper-dialog.md) | 模态任务面板 | 调用方持有 `visible` |
| [HyperAlertDialog](components/feedback/hyper-alert-dialog.md) | 确认或危险操作 | 回调由调用方处理 |
| [HyperTooltip](components/feedback/hyper-tooltip.md) | 补充提示 | 只放简短说明 |

## 如何选择

- 需要用户完成或取消一个任务：`HyperDialog` 或 `HyperAlertDialog`。
- 需要临时提示结果：`HyperToast`；需要持续显示状态：`HyperEmptyState` 或 `HyperProgressIndicator`。
- 需要少量设置入口：`HyperMenuList` + `HyperListItem`。
- 需要手机主导航：`HyperNavBar` + `HyperTabBar`。
