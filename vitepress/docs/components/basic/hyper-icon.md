---
description: 统一图标大小和着色，在按钮、列表与导航插槽内继承对应尺寸。
---

# HyperIcon

统一图标大小和着色，在按钮、列表与导航插槽内继承对应尺寸。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="icon" title="HyperIcon 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search

HyperIcon(icon = Lucide.Search, contentDescription = "搜索")
```

</details>

## 使用约定

**样式约定**

small/default/large 基准尺寸 18/24/32dp。default 在按钮、列表和导航插槽内继承对应组件的参考尺寸。Painter 与 ImageVector 两种重载。内置图标直接引用 com.composables:icons-lucide-cmp:2.2.1，不在项目内维护图标路径。

**状态归属**

icon 由调用方提供。装饰图标使用 null 描述；可操作图标提供说明。显式 Modifier.size 优先。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperIcon · 重载 1

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperIcon(
    icon: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    color: Color = LocalHyperContentColor.current,
    size: String = "default"
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| icon | Painter | 是 | — | 图标或图标区域。 |
| contentDescription | String? | 是 | — | 无障碍内容描述。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| color | Color | 否 | LocalHyperContentColor.current | 自定义颜色，覆盖默认配色。 |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |

### HyperIcon · 重载 2

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperIcon(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    color: Color = LocalHyperContentColor.current,
    size: String = "default"
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| icon | ImageVector | 是 | — | 图标或图标区域。 |
| contentDescription | String? | 是 | — | 无障碍内容描述。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| color | Color | 否 | LocalHyperContentColor.current | 自定义颜色，覆盖默认配色。 |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |
