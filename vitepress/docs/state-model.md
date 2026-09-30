# 状态与架构边界

HyperUI 采用受控组件模型：调用方保存状态，组件根据参数渲染并通过回调报告用户操作。

## 状态归属

| 状态 | 组件参数 | 调用方职责 |
| --- | --- | --- |
| 文本 | `value` / `onValueChange` | 保存、校验和提交文本 |
| 选择 | `checked`、`selected` / 回调 | 保存当前选项 |
| 显示 | `visible`、`open`、`expanded` | 决定显示和关闭时机 |
| 进度 | `progress` | 传入 `0f..1f`，或传入 `null` 表示不确定 |
| 拖动 | `value` / `onValueChange` | 持有当前值并处理提交 |
| 导航 | `itemSelected` / `onItemClick` | 更新选中项并执行页面导航 |

组件可以保存焦点、滚动位置和按压动画等纯 UI 状态，但不能保存业务结果。

## 依赖边界

以下能力必须由调用方通过参数、回调或业务层提供：

- 网络请求与重试
- 数据库读写
- 系统权限申请
- 页面路由与返回栈
- ViewModel、Repository 和 UseCase
- 表单提交、持久化和错误映射

## 受控示例

```kotlin
@Composable
fun NotificationSetting() {
    var enabled by remember { mutableStateOf(true) }

    HyperSwitch(
        checked = enabled,
        onCheckedChange = { enabled = it }
    )
}
```

## 弹层规则

- `HyperDialog` 和 `HyperAlertDialog` 的显示状态由调用方控制。
- `HyperDropdown`、`HyperPopup` 和 `HyperDrawer` 只渲染面板并触发关闭回调。
- 弹层内部可以组合表单和按钮，但网络、权限和导航仍放在调用方。
