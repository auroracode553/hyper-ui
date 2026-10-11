# 组件索引

所有组件从 `hyper_ui` 包导入。组件页按用途、交互示例、最小调用、使用约定和公开 API 组织。长签名与辅助配置可以展开查看，五列 Props 表完整保留；手机预览支持切换 320/350/390/430dp 宽度和重置。

HyperList 与 HyperSectionedList 使用整屏视口，顶部控件下方的列表独自滚动；HyperMenuGroup 和 HyperListTile 按内容测量，由预览宿主负责整页滚动。

## 基础

| 组件 | 用途 |
| --- | --- |
| [HyperText](components/basic/hyper-text.md) | 12 / 14 / 18sp 文字 |
| [HyperIcon](components/basic/hyper-icon.md) | 18 / 24 / 32dp 图标与插槽继承 |
| [HyperDivider](components/basic/hyper-divider.md) | 实线、虚线和方向缩进 |
| [HyperButton](components/basic/hyper-button.md) | filled、tonal、outline、danger、icon 五种按钮 |

## 表单

| 组件 | 用途 |
| --- | --- |
| [HyperTextField](components/form/hyper-text-field.md) | 文本、密码和多行输入 |
| [HyperSwitch](components/form/hyper-switch.md) | 开关 |
| [HyperCheckbox](components/form/hyper-checkbox.md) | 多选 |
| [HyperRadio](components/form/hyper-radio.md) | 单选 |
| [HyperSegmentedControl](components/form/hyper-segmented-control.md) | 40dp 分段切换，选中底板平移 |
| [HyperSlider](components/form/hyper-slider.md) | 6dp 轨道、20dp 手柄，连续或分段值调节 |

## 容器

| 组件 | 用途 |
| --- | --- |
| [HyperBackdrop](components/container/hyper-backdrop.md) | 玻璃背景采样 |
| [HyperCard](components/container/hyper-card.md) | 结构化内容容器 |
| [HyperColorPicker](components/container/hyper-color-picker.md) | 颜色选择 |

## 导航

| 组件 | 用途 |
| --- | --- |
| [HyperNavBar](components/navigation/hyper-nav-bar.md) | 默认透明，固定顶部状态栏避让；通过 type 选择 backOnly、titleOnly、backWithTitle、more、edit 或 custom 布局 |
| [HyperTabBar](components/navigation/hyper-tab-bar.md) | 56dp 导航；single 单动作与 multiple 多标签 |
| [HyperDrawer](components/navigation/hyper-drawer.md) | 任务抽屉 |
| [HyperSlideMenu](components/navigation/hyper-slide-menu.md) | 列表项侧滑操作 |
| [HyperTabs](components/navigation/hyper-tabs.md) | 分类筛选 |

## 列表

| 组件 | 用途 |
| --- | --- |
| [HyperList](components/list/hyper-list.md) | 连续数据列表 |
| [HyperSectionedList](components/list/hyper-sectioned-list.md) | 分组数据列表 |
| [HyperMenuGroup](components/list/hyper-menu-group.md) | 菜单和设置入口 |
| [HyperListTile](components/list/hyper-list-tile.md) | 设置行和列表行 |

## 反馈与工具

| 组件 | 用途 |
| --- | --- |
| [HyperEmptyState](components/feedback/hyper-empty-state.md) | 空数据或无结果 |
| [HyperDropdown](components/feedback/hyper-dropdown.md) | 48dp 选择字段与锚定选项 |
| [HyperToast](components/feedback/hyper-toast.md) | 短暂反馈 |
| [HyperProgress](components/feedback/hyper-progress.md) | 线性和圆形进度 |
| [HyperDialog](components/feedback/hyper-dialog.md) | 模态任务面板 |
| [HyperAlertDialog](components/feedback/hyper-alert-dialog.md) | 确认和危险操作 |
| [HyperTooltip](components/feedback/hyper-tooltip.md) | 补充提示 |
| [HyperBatteryIndicator](components/feedback/hyper-battery-indicator.md) | 电量显示 |
| [HyperLevelCapsule](components/feedback/hyper-level-capsule.md) | 等级或数值胶囊 |
| [HyperPlaybackSpeedPanel](components/feedback/hyper-playback-speed-panel.md) | 播放速度面板 |
| [HyperPlaybackSpeedScale](components/feedback/hyper-playback-speed-scale.md) | 播放速度刻度 |
| [HyperPopover](components/feedback/hyper-popover.md) | 触发点附近的补充内容浮层 |
| [HyperUpdateDialog](components/feedback/hyper-update-dialog.md) | 更新提示；统一小图标、版本直接排版、简洁错误反馈和描边次操作 |
| [HyperBatteryState](components/tools/hyper-battery-state.md) | Android 电量状态读取 |

统一材质、尺寸和 API 迁移见 [样式对照](style-reference.md)。
