# HyperTextField

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/input/HyperTextField.kt`
- 预览：`text_field`

`HyperTextField` 是 slot-first 输入框。搜索框、地址栏、页内查找栏和普通表单输入都通过同一个组件组合；UI 库不再提供固定搜索图标或固定清空按钮。
默认容器采用澎湃 OS 风格的白色单色底面：浅色主题为不透明纯白，深色主题为白色 `0.18f` alpha 材质；不叠加高光、明暗渐变或纹理层，直接复用公共 `hyperSurfaceDepth` 的控件描边与单层阴影。聚焦与错误状态会用对应语义色替换中性描边，不会叠加第二圈，错误状态优先。
组件内部维护 selection/composition，首次挂载已有文本时，光标默认位于文本末尾；用户开始编辑后会保留当前选区。

## 公开签名

```kotlin
data class HyperTextFieldColors(
    val containerColor: Color,
    val errorContainerColor: Color,
    val contentColor: Color,
    val placeholderColor: Color,
    val labelColor: Color,
    val supportingColor: Color,
    val errorColor: Color,
    val cursorColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

@Composable
fun HyperTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = HyperTextFieldDefaults.Shape,
    colors: HyperTextFieldColors = HyperTextFieldDefaults.colors(),
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    slotSpacing: Dp = HyperTextFieldDefaults.SlotSpacing,
    labelContent: (@Composable ColumnScope.() -> Unit)? = null,
    placeholderContent: (@Composable () -> Unit)? = null,
    startContent: (@Composable RowScope.() -> Unit)? = null,
    endContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null
)
```

## 关键公开类型

```kotlin
object HyperTextFieldDefaults {
    val MinHeight = 40.dp
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.MediumCornerRadius)
    val ContentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    val SlotSpacing = 8.dp

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        errorContainerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        placeholderColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        supportingColor: Color = Color.Unspecified,
        errorColor: Color = Color.Unspecified,
        cursorColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperTextFieldColors
}
```

## 最小用法

```kotlin
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR

HyperTextField(
    value = value,
    onValueChange = { value = it },
    labelContent = { Text("备注") },
    placeholderContent = { Text("写一点说明") },
    supportingContent = { Text("${value.length}/80") },
    singleLine = false,
    minLines = 3,
    maxLines = 5
)
```

## 搜索/地址栏组合

```kotlin
HyperTextField(
    value = keyword,
    onValueChange = { keyword = it },
    placeholderContent = { Text("搜索或输入网址") },
    startContent = {
        Icon(
            painter = painterResource(LucideR.drawable.lucide_ic_search),
            contentDescription = null
        )
    },
    endContent = {
        HyperIconButton(
            onClick = { keyword = "" },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(LucideR.drawable.lucide_ic_x),
                contentDescription = "清空"
            )
        }
    }
)
```

## 约束

- 不存在 `label`、`placeholder`、`errorText` 字符串参数；可见文本全部通过 slot 渲染。
- `inputModifier` 用于传入 `focusRequester`、`heightIn(...)` 等需要作用在 `BasicTextField` 上的修饰符；整个字段（含 label/supporting）的外部布局使用 `modifier`。
- `value` 仍由调用方持有；组件只保存输入法组合态和选区。已有文本首次聚焦时光标位于末尾，适合重命名、编辑标题等场景。
- 输入容器默认最小高度为紧凑的 `40.dp`，内部默认使用水平 `16.dp`、垂直 `6.dp` 留白；目前地址栏、首页搜索框和搜索聚焦态的最大 slot 为 `28.dp`，三者会保持相同行高。多行内容仍按行数自然增高，不提供重复的 `minHeight` 参数。
- 默认底色来自 `HyperTextFieldDefaults.colors()`：浅色主题使用 `Color(1f, 1f, 1f, 1f)`，深色主题使用 `Color(1f, 1f, 1f, 0.18f)`；自定义 `containerColor` 会作为整张输入表面的唯一底色。
- 输入框不使用 `Brush` 或 `drawWithCache` 绘制材质层，避免在较宽的单行输入框内形成横向白线或色带。
- 普通态通过公共 `hyperSurfaceDepth` 使用 `CompactControl` 的 1dp 中性描边和 4dp 单层阴影；浅色主题描边 alpha 为 `0.075f`，环境阴影 alpha 为 `0.10f`，点阴影 alpha 为 `0.24f`，使纯白输入框在白底上仍可辨识。可编辑输入框聚焦时立即以主题色替换中性描边并将同一公共阴影提高到 5dp，布局尺寸和内边距不变。
- `readOnly = true` 保留正常材质和内容对比度，但不会显示可编辑聚焦强调；`enabled = false` 使用更低透明度底材并关闭投影。
- `startContent` 与 `endContent` 按布局方向放置左右内容，间距由 `slotSpacing` 控制。
- 错误态通过 `isError` 和 `supportingContent` 组合表达，并使用一条低透明度错误色纯色边缘；它会替代聚焦边缘，不叠加第二圈。
- 组件不采样或模糊调用方背景，默认表面只由白色底材和公共投影构成。

## 从旧版迁移

- 删除 `HyperTextFieldDefaults.BorderWidth`；描边宽度和主题强度统一由内部公共 `hyperSurfaceDepth` 管理，不再作为输入框独立公开参数。
- 调用方无需为默认迁移新增参数；如曾直接读取该常量，应删除对应自定义描边，避免与组件的公共描边重复。

<WasmPreview demo="text_field" title="HyperTextField 交互预览" />
