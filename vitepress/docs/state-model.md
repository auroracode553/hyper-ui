# 状态与架构边界

## 状态归属

HyperUI 采用调用方持有状态的方式。组件接收当前值并通过回调报告用户操作：

| 状态类型 | 常见参数 | 调用方职责 |
| --- | --- | --- |
| 文本 | `value` / `onValueChange` | 保存、校验和提交文本 |
| 选择 | `checked`、`selected` / 对应回调 | 保存当前选择 |
| 显示 | `visible`、`open`、`expanded` | 决定何时显示或关闭 |
| 进度 | `progress` | 计算并传入 `0f..1f`，或用 `null` 表示不确定进度 |
| 导航选择 | `itemSelected` / `onItemClick` | 更新选中项并执行应用导航 |

组件可以在内部保存动画、焦点或滚动等纯 UI 状态，但不能保存业务结果。

## 依赖注入边界

以下能力必须由调用方通过参数、回调或自己的业务层提供：

- 网络请求与重试
- 数据库读写
- 系统权限申请
- 页面路由与返回栈
- ViewModel、Repository 或 UseCase
- 表单提交、持久化与错误映射

## 弹出层规则

HyperUI 的弹窗、菜单和抽屉只渲染自身面板，不添加 scrim、overlay 或半透明遮罩。

- `HyperDialog` 默认不因点击外部关闭；通过 `onDismissRequest` 处理返回键等关闭请求。
- `HyperDropdownMenu` 的菜单项点击后会先调用项目回调，再调用关闭回调。
- `HyperDrawer` 可使用透明的外部点击区域处理关闭，但不绘制遮罩。

## 禁止直接修改组件内部状态

错误思路：获取组件对象后修改其字段。

正确方式：状态放在调用方，并通过参数重新组合 UI。

```kotlin
var showDialog by remember { mutableStateOf(false) }

HyperDialog(
    visible = showDialog,
    onDismissRequest = { showDialog = false },
    title = "编辑"
) {
    // 内容
}
```
