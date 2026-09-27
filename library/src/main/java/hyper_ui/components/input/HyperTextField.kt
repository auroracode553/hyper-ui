/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/input/HyperTextField 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
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

/** HyperTextField 的显示与交互形态。 */
enum class HyperTextFieldType {
    Text,
    Textarea,
    Password
}

/**
 * 文本输入框组件。
 *
 * 组件内部已包含输入内容与输入容器的默认间距，外部间距请通过 modifier.padding(...) 控制。
 */
@Composable
fun HyperTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier,
    type: HyperTextFieldType = HyperTextFieldType.Text,
    rows: Int = HyperTextFieldDefaults.TextareaRows,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    clearable: Boolean = false,
    showPasswordToggle: Boolean = true,
    maxlength: Int? = null,
    showWordLimit: Boolean = false,
    shape: Shape = HyperTextFieldDefaults.Shape,
    colors: HyperTextFieldColors = HyperTextFieldDefaults.colors(),
    textStyle: TextStyle = HyperTheme.typography.bodyMedium.copy(
        fontSize = 14.sp,
        lineHeight = 19.sp
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
) {
    require(rows > 0) { "rows 必须大于 0" }
    require(maxlength == null || maxlength > 0) { "maxlength 必须大于 0" }
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
        readOnly = readOnly,
        isError = isError,
        isFocused = focused,
        colors = colors
    )
    var passwordVisible by remember { mutableStateOf(false) }
    val singleLine = type != HyperTextFieldType.Textarea
    val minLines = if (singleLine) 1 else rows
    val maxLines = if (singleLine) 1 else rows
    val effectiveTransformation = when {
        type == HyperTextFieldType.Password && !passwordVisible -> PasswordVisualTransformation()
        else -> visualTransformation
    }
    val verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
    val effectiveEndContent = endContent ?: defaultEndContent(
        type = type,
        value = value,
        enabled = enabled,
        readOnly = readOnly,
        clearable = clearable,
        showPasswordToggle = showPasswordToggle,
        passwordVisible = passwordVisible,
        onClear = { onValueChange("") },
        onTogglePassword = { passwordVisible = !passwordVisible }
    )

    Column(modifier = modifier) {
        if (labelContent != null) {
            CompositionLocalProvider(LocalHyperContentColor provides visuals.labelColor) {
                Column(
                    modifier = Modifier.padding(start = 0.dp, bottom = 6.dp),
                    content = labelContent
                )
            }
        }

        BasicTextField(
            value = textFieldValue,
            onValueChange = { updatedValue ->
                textFieldValueState = updatedValue
                val limitedText = if (maxlength == null) updatedValue.text else updatedValue.text.take(maxlength)
                if (limitedText != value) {
                    onValueChange(limitedText)
                }
            },
            modifier = inputModifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
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
                        .defaultMinSize(minHeight = HyperTextFieldDefaults.MinHeight)
                        .hyperTextFieldSurface(
                            shape = shape,
                            visuals = visuals.surface
                        )
                        .padding(HyperTextFieldDefaults.ContentPadding),
                    verticalAlignment = verticalAlignment
                ) {
                    if (startContent != null) {
                        CompositionLocalProvider(LocalHyperContentColor provides visuals.contentColor) {
                            startContent()
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = if (startContent == null) 0.dp else slotSpacing,
                                end = if (endContent == null) 0.dp else slotSpacing
                            ),
                        contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
                    ) {
                        if (value.isEmpty() && placeholderContent != null) {
                            CompositionLocalProvider(LocalHyperContentColor provides visuals.placeholderColor) {
                                placeholderContent()
                            }
                        }
                        innerTextField()
                    }

                    if (effectiveEndContent != null) {
                        CompositionLocalProvider(LocalHyperContentColor provides visuals.contentColor) {
                            effectiveEndContent()
                        }
                    }
                }
            }
        )

        if (supportingContent != null || (showWordLimit && maxlength != null)) {
            CompositionLocalProvider(LocalHyperContentColor provides visuals.supportingColor) {
                Column(
                    modifier = Modifier.padding(start = 0.dp, top = 6.dp),
                ) {
                    supportingContent?.invoke(this)
                    if (showWordLimit && maxlength != null) {
                        HyperText(text = "${value.length}/$maxlength", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

private fun defaultEndContent(
    type: HyperTextFieldType,
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
    if (type != HyperTextFieldType.Password && !(clearable && value.isNotEmpty())) return null
    return {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (type == HyperTextFieldType.Password && showPasswordToggle) {
                HyperText(
                    text = if (passwordVisible) "隐藏" else "显示",
                    modifier = Modifier.clickable(enabled = canInteract, onClick = onTogglePassword),
                    fontSize = 13.sp
                )
            }
            if (clearable && value.isNotEmpty()) {
                HyperText(
                    text = "×",
                    modifier = Modifier.padding(start = 10.dp).clickable(enabled = canInteract, onClick = onClear),
                    fontSize = 24.sp,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

object HyperTextFieldDefaults {
    /** 当前默认 slot（含 28dp 扫码图标）可在同一紧凑行高内稳定居中。 */
    val MinHeight = 68.dp
    val Shape: Shape = RoundedCornerShape(20.dp)
    val ContentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 16.dp)
    val SlotSpacing = 8.dp
    const val TextareaRows = 5

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
            labelColor = resolveHyperContainerColor(labelColor, HyperColors.secondaryText),
            supportingColor = resolveHyperContainerColor(supportingColor, HyperColors.secondaryText),
            errorColor = resolvedErrorColor,
            cursorColor = resolveHyperContainerColor(cursorColor, HyperColors.accent),
            disabledContainerColor = resolveHyperContainerColor(
                disabledContainerColor,
                defaultDisabledContainerColor
            ),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                HyperColors.disabledText
            )
        )
    }
}
