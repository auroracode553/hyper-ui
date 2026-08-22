# HyperTextField

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/input/HyperTextField.kt`
- 预览：`text_field`

`HyperTextField` 是 slot-first 输入框。搜索框、地址栏、页内查找栏和普通表单输入都通过同一个组件组合；UI 库不再提供固定搜索图标或固定清空按钮。
默认容器遵循项目 `hyper-ui-glass-design`：白色半透明底材、宽而弱的上沿柔光、底部轻微压暗和单层低位柔影共同形成结构性磨砂玻璃。普通态不绘制硬边框；聚焦与错误状态各自只使用一条渐变语义边缘，错误状态优先。
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
- 默认底材来自 `HyperTextFieldDefaults.colors()`：浅色主题使用白色 `0.66f` alpha，深色主题使用白色 `0.18f` alpha；自定义 `containerColor` 仍作为玻璃底色继续叠加材质明暗。
- 普通态使用 2dp 低位柔影且不绘制边框；可编辑输入框聚焦时立即切换为单一主题色渐变边缘并将柔影提高到 3dp，布局尺寸和内边距不变。
- `readOnly = true` 保留正常材质和内容对比度，但不会显示可编辑聚焦强调；`enabled = false` 使用更低透明度底材并关闭投影。
- `startContent` 与 `endContent` 按布局方向放置左右内容，间距由 `slotSpacing` 控制。
- 错误态通过 `isError` 和 `supportingContent` 组合表达，并使用一条低透明度错误色渐变边缘；它会替代聚焦边缘，不叠加第二圈。
- 组件不采样或模糊调用方背景，磨砂层由自身半透明底材、光学渐变和投影构成。

## 从旧版迁移

- 删除 `HyperTextFieldDefaults.BorderWidth`；普通态不再绘制永久 1dp 描边。
- 调用方无需为默认迁移新增参数；如曾直接读取该常量，应删除对应自定义描边，或在组件外按业务需要自行组合。

<WasmPreview demo="text_field" title="HyperTextField 交互预览" />
