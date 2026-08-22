# 状态与架构边界

## 状态归属

HyperUI 采用调用方持有状态的方式。组件接收当前值并通过回调报告用户操作：

| 状态类型 | 常见参数 | 调用方职责 |
| --- | --- | --- |
| 文本 | `value` / `onValueChange` | 保存、校验和提交文本 |
| 选择 | `checked`、`selected` / 对应回调 | 保存当前选择 |
| 显示 | `visible`、`open`、`expanded` | 决定何时显示或关闭 |
| 进度 | `progress` | 计算并传入 `0f..1f`，或用 `null` 表示不确定进度 |
| 可拖动进度 | `value` / `onValueChange` | 持有当前值，并在拖动回调中执行预览或提交 |
| 导航选择 | `itemSelected` / `onItemClick` | 更新选中项并执行应用导航 |

组件可以在内部保存焦点或滚动等纯 UI 状态，但不能保存业务结果。HyperUI 不保存或执行动画状态。

## 依赖注入边界

以下能力必须由调用方通过参数、回调或自己的业务层提供：

- 网络请求与重试
- 数据库读写
- 系统权限申请
- 页面路由与返回栈
- ViewModel、Repository 或 UseCase
- 表单提交、持久化与错误映射

## 弹出层规则

HyperUI 的模态 Dialog 使用平台标准 scrim；Popup、菜单和当前抽屉组件只渲染自身面板。`HyperDrawer` 使用主题卡片色的不透明结构玻璃，并复用公共低对比度描边和单层低抬升投影；未选中项不重复铺底，选中项通过轻量主题染色表达状态。

- `HyperPopup` 默认在点击面板外空白区域时调用 `onDismissRequest`；传入 `dismissOnClickOutside = false` 可禁用空白关闭。
- `HyperDropdown` 的菜单项点击后会先调用项目回调，再调用关闭回调。
- `HyperDrawer` 可使用不参与绘制的外部点击区域处理关闭，但不绘制遮罩。

## 禁止直接修改组件内部状态

错误思路：获取组件对象后修改其字段。

正确方式：状态放在调用方，并通过参数重新组合 UI。

```kotlin
var showPopup by remember { mutableStateOf(false) }

HyperPopup(
    visible = showPopup,
    onDismissRequest = { showPopup = false },
    title = "编辑"
) {
    // 内容
}
```
