# 状态与架构边界

HyperUI 采用受控组件模型：调用方保存状态，组件根据参数渲染并通过回调报告用户操作。

修改前同时阅读 [UI 样式与功能实现基线](style-reference.md)，按实际组件区分业务状态与内部临时交互状态，不为统一实现而改变已有状态归属。

## 状态归属

| 状态 | 组件参数 | 调用方职责 |
| --- | --- | --- |
| 文本 | `value` / `onChanged` | 保存、校验和提交文本 |
| 选择 | `value`、`groupValue` / `onChanged` | 保存当前选项 |
| 显示 | `visible`、`open`、`expanded` | 决定显示和关闭时机 |
| 进度 | `value` | 传入 `0f..1f`，或传入 `null` 表示不确定 |
| 拖动 | `value` / `onChanged` | 持有当前值并处理提交 |
| 导航 | `selectedIndex` / `onChanged` | 更新选中项并执行页面导航 |

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
        value = enabled,
        onChanged = { enabled = it }
    )
}
```

## 弹层规则

- `HyperDialog` 和 `HyperAlertDialog` 的显示状态由调用方控制。
- `HyperDropdown` 的 value 由调用方持有，临时 expanded 由组件内部维护；`HyperPopover` 的 visible 和 `HyperDrawer` 的 open 由调用方持有，组件通过 onClose 请求关闭。
- 弹层内部可以组合表单和按钮，但网络、权限和导航仍放在调用方。
