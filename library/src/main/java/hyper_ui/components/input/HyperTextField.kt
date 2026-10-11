/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/input/HyperTextField 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 文本输入框组件。
 *
 * 组件内部已包含输入内容与输入容器的默认间距，外部间距请通过 modifier.padding(...) 控制。
 */
@Composable
fun HyperTextField(
    value: String,
    onChanged: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier,
    type: String = "text",
    size: String = "default",
    rows: Int = HyperTextFieldDefaults.TextareaRows,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    clearable: Boolean = false,
    showPasswordToggle: Boolean = false,
    maxLength: Int? = null,
    showWordLimit: Boolean = false,
    shape: Shape = HyperTextFieldDefaults.Shape,
    colors: HyperTextFieldColors = HyperTextFieldDefaults.colors(),
    textStyle: TextStyle = HyperTextFieldDefaults.textStyle(size),
    keyboardOptions: KeyboardOptions = HyperTextFieldDefaults.keyboardOptions(type),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    slotSpacing: Dp = HyperTextFieldDefaults.SlotSpacing,
    label: (@Composable ColumnScope.() -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    prefix: (@Composable RowScope.() -> Unit)? = null,
    suffix: (@Composable RowScope.() -> Unit)? = null,
    supporting: (@Composable ColumnScope.() -> Unit)? = null
) {
    val effectiveReadOnly = readOnly || onChanged == null
    require(rows > 0) { "rows 必须大于 0" }
    requireHyperComponentSize(size)
    require(type in setOf("text", "search", "password", "textarea", "email", "url", "number", "tel")) {
        "不支持的 HyperTextField type: $type"
    }
    require(maxLength == null || maxLength > 0) { "maxLength 必须大于 0" }
    // String API 仍由调用方持有文本，组件仅保存 selection/composition。
    // 首次挂载时把光标放到末尾；后续输入继续沿用用户当前选区。
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        )
    }
    val textFieldValue = textFieldValueState.copy(text = value)
    val focused by interactionSource.collectIsFocusedAsState()
    SideEffect {
        if (
            textFieldValue.text != textFieldValueState.text ||
            textFieldValue.selection != textFieldValueState.selection ||
            textFieldValue.composition != textFieldValueState.composition
        ) {
            textFieldValueState = textFieldValue
        }
    }

    val visuals = hyperInputFieldVisuals(
        enabled = enabled,
        readOnly = effectiveReadOnly,
        isError = isError,
        isFocused = focused,
        colors = colors
    )
    var passwordVisible by remember { mutableStateOf(false) }
    val singleLine = type != "textarea"
    val minLines = if (singleLine) 1 else rows
    val maxLines = if (singleLine) 1 else rows
    val effectiveTransformation = when {
        type == "password" && !passwordVisible -> PasswordVisualTransformation()
        else -> visualTransformation
    }
    val verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
    val builtInEndContent = defaultEndContent(
        type = type,
        value = value,
        enabled = enabled,
        readOnly = effectiveReadOnly,
        clearable = clearable,
        showPasswordToggle = showPasswordToggle,
        passwordVisible = passwordVisible,
        onClear = { onChanged?.invoke("") },
        onTogglePassword = { passwordVisible = !passwordVisible }
    )

    Column(modifier = modifier) {
        if (label != null) {
            CompositionLocalProvider(LocalHyperContentColor provides visuals.labelColor,
                LocalHyperTextStyle provides HyperTheme.typography.bodySmall.copy(fontSize = 13.sp, lineHeight = 16.9.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)) {
                Column(
                    modifier = Modifier.padding(start = 0.dp, bottom = 8.dp),
                    content = label
                )
            }
        }

        BasicTextField(
            value = textFieldValue,
            onValueChange = { updatedValue ->
                textFieldValueState = updatedValue
                val limitedText = if (maxLength == null) updatedValue.text else updatedValue.text.take(maxLength)
                if (limitedText != value) {
                    onChanged?.invoke(limitedText)
                }
            },
            modifier = inputModifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = effectiveReadOnly,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            textStyle = textStyle.copy(color = visuals.contentColor),
            cursorBrush = SolidColor(visuals.cursorColor),
            interactionSource = interactionSource,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = effectiveTransformation,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .hyperTextFieldSurface(
                            shape = shape,
                            visuals = visuals.surface
                        )
                        .padding(HyperTextFieldDefaults.contentPadding(size, !singleLine)),
                    verticalAlignment = verticalAlignment
                ) {
                    if (prefix != null) {
                        CompositionLocalProvider(LocalHyperContentColor provides visuals.placeholderColor,
                            LocalHyperIconSize provides (textStyle.fontSize.value + 2).dp) {
                            prefix()
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = if (prefix == null) 0.dp else slotSpacing,
                                end = if (suffix == null) 0.dp else slotSpacing
                            ),
                        contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
                    ) {
                        if (value.isEmpty() && placeholder != null) {
                            CompositionLocalProvider(LocalHyperContentColor provides visuals.placeholderColor,
                                LocalHyperTextStyle provides textStyle) {
                                placeholder()
                            }
                        }
                        innerTextField()
                    }

                    if (suffix != null) {
                        CompositionLocalProvider(LocalHyperContentColor provides visuals.contentColor) { suffix() }
                    }
                    builtInEndContent?.invoke(this)
                }
            }
        )

        if (supporting != null || (showWordLimit && maxLength != null)) {
            CompositionLocalProvider(LocalHyperContentColor provides visuals.supportingColor) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (supporting != null) Column(Modifier.padding(start = 4.dp, top = 6.dp), content = supporting)
                    if (showWordLimit && maxLength != null) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                            HyperText(text = "${value.length}/$maxLength", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

private fun defaultEndContent(
    type: String,
    value: String,
    enabled: Boolean,
    readOnly: Boolean,
    clearable: Boolean,
    showPasswordToggle: Boolean,
    passwordVisible: Boolean,
    onClear: () -> Unit,
    onTogglePassword: () -> Unit
): (@Composable RowScope.() -> Unit)? {
    val canInteract = enabled && !readOnly
    if (!(type == "password" && showPasswordToggle) && !clearable) return null
    return {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (type == "password" && showPasswordToggle) {
                HyperInputAffix(if (passwordVisible) HyperReferenceIcons.EyeOff else HyperReferenceIcons.Eye,
                    if (passwordVisible) "隐藏密码" else "显示密码", canInteract, onTogglePassword)
            }
            if (clearable) {
                HyperInputAffix(HyperReferenceIcons.Close, "清空", canInteract && value.isNotEmpty(), onClear,
                    Modifier.alpha(if (canInteract && value.isNotEmpty()) 1f else 0f))
            }
        }
    }
}

object HyperTextFieldDefaults {
    /** 无附加插槽时的默认字号行高与内边距总和；实际高度由内容测量。 */
    // 参考字体行高 20.8dp + 上下各 9dp；轮廓不增加布局尺寸。
    val MinHeight = 38.8.dp
    val Shape: Shape = RoundedCornerShape(20.dp)
    val ContentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 9.dp)
    fun contentPadding(size: String = "default", multiline: Boolean = false): androidx.compose.foundation.layout.PaddingValues =
        androidx.compose.foundation.layout.PaddingValues(
            horizontal = hyperComponentSize(size, 14.dp, 18.dp, 20.dp),
            vertical = hyperComponentSize(size, 7.dp, 9.dp, 11.dp) + if (multiline) 3.dp else 0.dp
        )

    @Composable
    fun textStyle(size: String = "default"): TextStyle {
        val fontSize = hyperComponentSize(size, 14.dp, 16.dp, 18.dp).value
        return HyperTheme.typography.bodyMedium.copy(fontSize = fontSize.sp, lineHeight = (fontSize * 1.3f).sp)
    }

    fun keyboardOptions(type: String = "text"): KeyboardOptions = KeyboardOptions(
        keyboardType = when (type) {
            "password" -> KeyboardType.Password
            "email" -> KeyboardType.Email
            "url" -> KeyboardType.Uri
            "number" -> KeyboardType.Number
            "tel" -> KeyboardType.Phone
            else -> KeyboardType.Text
        },
        imeAction = when (type) {
            "textarea" -> ImeAction.Default
            "search" -> ImeAction.Search
            else -> ImeAction.Done
        }
    )
    val SlotSpacing = 8.dp
    const val TextareaRows = 3

    @Composable
    fun colors(
        containerColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        errorContainerColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        contentColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        placeholderColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        labelColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        supportingColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        errorColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        cursorColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        disabledContainerColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        disabledContentColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified
    ): HyperTextFieldColors {
        val resolvedContentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText)
        val resolvedPlaceholderColor = resolveHyperContainerColor(placeholderColor, HyperColors.secondaryText)
        val resolvedErrorColor = resolveHyperContainerColor(errorColor, HyperColors.danger)
        val defaultContainerColor = hyperGlass.surfaceSubtle
        val defaultDisabledContainerColor = hyperGlass.controlTrack

        return HyperTextFieldColors(
            containerColor = resolveHyperContainerColor(containerColor, defaultContainerColor),
            errorContainerColor = resolveHyperContainerColor(
                errorContainerColor,
                defaultContainerColor
            ),
            contentColor = resolvedContentColor,
            placeholderColor = resolvedPlaceholderColor,
            labelColor = resolveHyperContainerColor(labelColor, HyperColors.primaryText),
            supportingColor = resolveHyperContainerColor(supportingColor, HyperColors.secondaryText),
            errorColor = resolvedErrorColor,
            cursorColor = resolveHyperContainerColor(cursorColor, HyperColors.accent),
            disabledContainerColor = resolveHyperContainerColor(
                disabledContainerColor,
                defaultDisabledContainerColor
            ),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                resolvedContentColor
            )
        )
    }
}
