# HyperUI

HyperUI 是面向 **Android 手机端** 的 Jetpack Compose UI 组件库，提供 HyperOS 风格的柔性玻璃表面、受控交互和基础页面组件。

组件库只负责 UI：不持有业务数据，不发起网络请求，不访问数据库，不申请权限，也不实现路由或 ViewModel。

## 30 秒开始

1. 阅读[接入与最小配置](getting-started.md)。
2. 在应用根节点包裹 `HyperThemeConfig`。
3. 查看[移动端规范](mobile-guidelines.md)，确认手机宽度、安全区和触控约束。
4. 在[组件索引](component-index.md)选择组件，再打开对应组件页查看真实签名。

```kotlin
import hyper_ui.*

@Composable
fun SaveAction(onSave: () -> Unit) {
    HyperButton(
        onClick = onSave,
        type = "filled",
        size = "default"
    ) {
        HyperText("保存")
    }
}
```

## API 约定

| 约定 | 规则 |
| --- | --- |
| `type` | 同一组件内的视觉或结构形态，例如 `filled`、`outline`、`textarea` |
| `size` | `small`、`default`、`large` 三档视觉尺寸 |
| `modifier` | 外部宽度、间距、位置和页面布局 |
| 状态参数 | `value`、`checked`、`selected`、`visible`、`progress` 等由调用方持有 |
| Slot | 内容、图标、前后缀和操作区通过 `content` 或具名 Slot 注入 |
| 颜色 | 使用 `HyperThemeConfig` 或组件的 `colors`，不在调用方复制内部材质逻辑 |

公开 API 统一位于 `hyper_ui` 包。调用方使用 `import hyper_ui.*`，不要导入 `hyper_ui.core.*`。

## 手机端范围

当前产品目标是 320dp 至 430dp 的 Android 竖屏手机。组件不会为平板、桌面或横屏增加专用 API；更宽或更复杂的布局由页面通过 Compose `Modifier` 组合。

Preview 的 Desktop/Wasm 目标只用于文档中的交互验收，不代表公开 API 支持这些消费平台。详见[移动端规范](mobile-guidelines.md)和[交互预览](preview.md)。

## 按任务阅读

- 接入项目：[接入与最小配置](getting-started.md)
- 配置主题：[主题与颜色](theme.md)
- 管理受控状态：[状态与架构边界](state-model.md)
- 查找组件：[组件索引](component-index.md)
- 组合页面：[常见页面组合](patterns/index.md)
- 维护文档：[文档维护规则](maintenance.md)

## 事实来源

`library/src/main/java/hyper_ui/` 定义实现，`vitepress/docs/` Markdown 定义面向调用方的 API 说明，Preview 只展示真实交互。Markdown 必须包含包名、公开签名、默认值、状态归属、使用约束和最小调用方式。
