# HyperButton

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/button/HyperButton.kt`
- 预览：`button`

`HyperButton` 是不透明实色的 slot-first 按钮容器。组件只负责点击、禁用态、tone、颜色、边框、形状和内容排列；按钮里的文字、图标、计数或加载状态全部由调用方通过 `content` slot 渲染。组件不再叠加玻璃高光，也不通过透明度表达 tone 或禁用状态。

## 公开签名

```kotlin
enum class HyperButtonTone {
    Primary, Secondary, Tonal, Outline, Plain, Success, Info, Warning, Danger
}

data class HyperButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

@Composable
fun HyperButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tone: HyperButtonTone = HyperButtonTone.Primary,
    colors: HyperButtonColors = HyperButtonDefaults.colors(tone),
    border: BorderStroke? = HyperButtonDefaults.border(tone),
    shape: Shape = HyperButtonDefaults.Shape,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperButtonDefaults.ContentSpacing,
        Alignment.CenterHorizontally
    ),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
)
```

## 最小用法

```kotlin
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR

HyperButton(onClick = onSave) {
    Icon(
        painter = painterResource(LucideR.drawable.lucide_ic_search),
        contentDescription = null
    )
    Text("搜索")
}
```

## 间距说明

组件尺寸和外部间距通过 `Modifier` 控制，例如 `Modifier.height(32.dp)`、`Modifier.widthIn(...)` 与 `Modifier.padding(...)`。
`HyperButton` 作为原子组件，内部已有合理默认内边距。

## 约束

- 不存在 `text`、`leadingIcon`、`trailingIcon` 参数；这些内容必须由调用方放入 `content`。
- 默认最小高度为 `HyperButtonDefaults.MinHeight`；调用方需要其他尺寸时使用 `modifier`，不传 `minHeight` 配置参数。
- `LocalContentColor` 会传递给 slot 内的 `Text` 与 `Icon`。
- 所有 tone 都使用不透明背景：`Tonal` 使用预混合后的实色强调背景，`Outline` 使用实色卡片背景和 1.dp 实色强调边框，`Plain` 使用无边框实色卡片背景。
- 禁用态使用不透明的 `softContainer` 与 `secondaryText`。通过 `HyperButtonColors` 或 `HyperButtonDefaults.colors(...)` 传入含 alpha 的颜色时，组件会先与自身实色背景合成再绘制，不会透出下层内容。

<WasmPreview demo="button" title="HyperButton 交互预览" />
