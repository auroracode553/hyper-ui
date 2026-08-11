/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/input/HyperTextField 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 文本输入框组件。
 *
 * 组件内部已包含输入内容与边框的默认间距，外部间距请通过 modifier.padding(...) 控制。
 */
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
) {
    val visuals = hyperInputFieldVisuals(
        enabled = enabled,
        isError = isError,
        colors = colors
    )
    val verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top

    Column(modifier = modifier) {
        if (labelContent != null) {
            CompositionLocalProvider(LocalContentColor provides visuals.labelColor) {
                Column(
                    modifier = Modifier.padding(start = 18.dp, bottom = 6.dp),
                    content = labelContent
                )
            }
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
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
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = HyperTextFieldDefaults.MinHeight)
                        .clip(shape)
                        .background(color = visuals.containerColor, shape)
                        .border(
                            border = BorderStroke(
                                width = HyperTextFieldDefaults.BorderWidth,
                                color = visuals.borderColor
                            ),
                            shape = shape
                        )
                        .padding(HyperTextFieldDefaults.ContentPadding),
                    verticalAlignment = verticalAlignment
                ) {
                    if (startContent != null) {
                        CompositionLocalProvider(LocalContentColor provides visuals.contentColor) {
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
                            CompositionLocalProvider(LocalContentColor provides visuals.placeholderColor) {
                                placeholderContent()
                            }
                        }
                        innerTextField()
                    }

                    if (endContent != null) {
                        CompositionLocalProvider(LocalContentColor provides visuals.contentColor) {
                            endContent()
                        }
                    }
                }
            }
        )

        if (supportingContent != null) {
            CompositionLocalProvider(LocalContentColor provides visuals.supportingColor) {
                Column(
                    modifier = Modifier.padding(start = 18.dp, top = 6.dp),
                    content = supportingContent
                )
            }
        }
    }
}

object HyperTextFieldDefaults {
    /** 当前默认 slot（含 28dp 扫码图标）可在同一紧凑行高内稳定居中。 */
    val MinHeight = 40.dp
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.MediumCornerRadius)
    val ContentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    val SlotSpacing = 8.dp
    val BorderWidth = 1.dp

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

        return HyperTextFieldColors(
            containerColor = resolveHyperContainerColor(containerColor, HyperColors.fieldContainer),
            errorContainerColor = resolveHyperContainerColor(
                errorContainerColor,
                HyperColors.fieldContainer
            ),
            contentColor = resolvedContentColor,
            placeholderColor = resolvedPlaceholderColor,
            labelColor = resolveHyperContainerColor(labelColor, HyperColors.secondaryText),
            supportingColor = resolveHyperContainerColor(supportingColor, HyperColors.secondaryText),
            errorColor = resolvedErrorColor,
            cursorColor = resolveHyperContainerColor(cursorColor, HyperColors.accent),
            disabledContainerColor = resolveHyperContainerColor(
                disabledContainerColor,
                HyperColors.fieldContainer
            ),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                HyperColors.disabledText
            )
        )
    }
}
