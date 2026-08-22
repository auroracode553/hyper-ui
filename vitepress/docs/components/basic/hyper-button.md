# HyperButton

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/button/HyperButton.kt`
- 预览：`button`

`HyperButton` 是不透明实色的 slot-first 按钮容器。组件只负责点击、禁用态、tone、颜色、边框、形状和内容排列；按钮里的文字、图标、计数或加载状态全部由调用方通过 `content` slot 渲染。所有 tone 统一复用公共 `hyperSurfaceDepth` 的控件描边和单层阴影，不叠加玻璃高光，也不通过透明度表达 tone 或禁用状态。

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
    contentPadding: PaddingValues = HyperButtonDefaults.ContentPadding,
    role: Role = Role.Button,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperButtonDefaults.ContentSpacing,
        Alignment.CenterHorizontally
    ),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
)
```

## 默认值与配置

```kotlin
object HyperButtonDefaults {
    val MinHeight = 40.dp
    val ContentSpacing = 8.dp
    val ContentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)

    @Composable
    fun colors(
        tone: HyperButtonTone = HyperButtonTone.Primary,
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperButtonColors

    @Composable
    fun border(
        tone: HyperButtonTone = HyperButtonTone.Primary,
        color: Color = Color.Unspecified
    ): BorderStroke?
}
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
`HyperButton` 作为原子组件，内部默认使用 `HyperButtonDefaults.ContentPadding`；分段控制器等组合组件可以通过 `contentPadding` 复用按钮交互和表面，同时保持自己的紧凑布局。

## 约束

- 不存在 `text`、`leadingIcon`、`trailingIcon` 参数；这些内容必须由调用方放入 `content`。
- 默认最小高度为 `HyperButtonDefaults.MinHeight`；调用方需要其他尺寸时使用 `modifier`，不传 `minHeight` 配置参数。
- `contentPadding` 只控制内容 Slot 的内部留白；`role` 默认是 `Role.Button`，组合为分段标签时可传 `Role.Tab`。
- `LocalContentColor` 会传递给 slot 内的 `Text` 与 `Icon`。
- 所有 tone 都使用不透明背景，并通过公共 `CompactControl` 深度获得 1dp 中性描边和 4dp 单层阴影；`Tonal` 使用预混合后的实色强调背景，`Plain` 也保留公共中性描边与阴影。
- `Outline` 的默认主题描边或调用方传入的 `border` 会作为公共深度的描边覆盖项，替换中性描边而不是叠加第二圈。
- `border = null` 表示不提供覆盖描边，组件仍会绘制公共中性描边；禁用态忽略覆盖描边并切换为公共弱描边。
- 指针按下时立即切换为公共 `Pressed` 深度，阴影降至 1.5dp；松开或取消时恢复。禁用态使用不透明的 `softContainer` 与 `secondaryText`、保留公共弱描边并移除阴影。
- 通过 `HyperButtonColors` 或 `HyperButtonDefaults.colors(...)` 传入含 alpha 的颜色时，组件会先与自身实色背景合成再绘制，不会透出下层内容。

<WasmPreview demo="button" title="HyperButton 交互预览" />
