# HyperPanel

- 分类：容器组件
- 包名：`hyper_ui`
- 状态模型：无业务状态；内容和内容状态由调用方提供
- 源码：`library/src/main/java/hyper_ui/components/panel/HyperPanel.kt`
- Preview 注册：`preview/src/commonMain/kotlin/hyper_ui/docs/data/ContainerComponentDemos.kt`
- Preview 交互：`preview/src/commonMain/kotlin/hyper_ui/docs/ui/ContainerComponentShowcases.kt`

## 公开 API

```kotlin
@Composable
fun HyperPanel(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp),
    content: @Composable ColumnScope.() -> Unit
)
```

## 关键公开类型

无专属配置类型；布局参数使用 Compose 的 `PaddingValues` 与 `Arrangement.Vertical`。

## 参数

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `modifier` | `Modifier` | 否 | `Modifier` | 调整面板外层布局。 |
| `containerColor` | `Color` | 否 | `Color.Unspecified` | 面板背景色；未指定时使用 `HyperColors.cardContainer`。 |
| `contentPadding` | `PaddingValues` | 否 | `PaddingValues(20.dp)` | 内容内边距。 |
| `verticalArrangement` | `Arrangement.Vertical` | 否 | `Arrangement.spacedBy(12.dp)` | `Column` 内子项的垂直排列。 |
| `content` | `@Composable ColumnScope.() -> Unit` | 是 | 无 | 面板内容插槽，可使用 `ColumnScope` 能力。 |

## 状态归属

- `HyperPanel` 只负责容器背景、圆角、内边距和垂直排列。
- 表单值、展开状态、点击结果、加载状态等均由 `content` 中的调用方组件管理。
- 组件本身没有点击、选择或展开状态。

## 最小用法

```kotlin
HyperPanel {
    Text("账户信息")
    Text("昵称：HyperUI")
}
```

自定义间距：

```kotlin
HyperPanel(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    Text("标题")
    HyperButton(text = "操作", onClick = { /* 处理操作 */ })
}
```

## 约束与行为

- 组件会调用 `fillMaxWidth()`，并使用 `HyperStyleDefaults.LargeCornerRadius` 裁剪圆角。
- 公开 API 没有 `shape`、`onClick`、`enabled` 或滚动参数。
- 若内容需要滚动，由调用方在外层或内容中选择合适的滚动容器。
- 不要在 UI 组件中放入网络请求、数据库访问或业务状态保存逻辑。
- 项目颜色规范禁止十六进制硬编码；自定义颜色使用项目允许的 RGBA 写法。

## 常见误用

```kotlin
// 错误：HyperPanel 没有 onClick 参数。
HyperPanel(onClick = onOpen) {
    Text("详情")
}
```

需要点击行为时，在内容中放置按钮或由调用方显式组合可点击区域。

## 相关 API

- `HyperColors.cardContainer`
- `HyperStyleDefaults.LargeCornerRadius`

## 交互预览

<WasmPreview demo="panel" title="HyperPanel 交互预览" />
