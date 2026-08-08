package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    minHeight: Dp = HyperTextFieldDefaults.MinHeight,
    shape: Shape = HyperTextFieldDefaults.Shape,
    colors: HyperTextFieldColors = HyperTextFieldDefaults.colors(),
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    contentPadding: PaddingValues = HyperTextFieldDefaults.ContentPadding,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    labelContent: (@Composable ColumnScope.() -> Unit)? = null,
    placeholderContent: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
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
                        .heightIn(min = minHeight)
                        .hyperGlassSurface(
                            containerColor = visuals.containerColor,
                            shape = shape,
                            elevation = HyperTextFieldDefaults.ContainerElevation,
                            border = BorderStroke(
                                width = HyperTextFieldDefaults.BorderWidth,
                                color = visuals.borderColor
                            )
                        )
                        .padding(contentPadding),
                    verticalAlignment = verticalAlignment
                ) {
                    if (leadingContent != null) {
                        CompositionLocalProvider(LocalContentColor provides visuals.contentColor) {
                            leadingContent()
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = if (leadingContent == null) 0.dp else 10.dp,
                                end = if (trailingContent == null) 0.dp else 10.dp
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

                    if (trailingContent != null) {
                        CompositionLocalProvider(LocalContentColor provides visuals.contentColor) {
                            trailingContent()
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
    val MinHeight = 52.dp
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.MediumCornerRadius)
    val ContentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp)
    val ContainerElevation = 1.dp
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
            containerColor = resolveHyperContainerColor(containerColor, HyperColors.elevatedContainer),
            errorContainerColor = resolveHyperContainerColor(
                errorContainerColor,
                resolvedErrorColor.copy(alpha = 0.12f)
            ),
            contentColor = resolvedContentColor,
            placeholderColor = resolvedPlaceholderColor,
            labelColor = resolveHyperContainerColor(labelColor, HyperColors.secondaryText),
            supportingColor = resolveHyperContainerColor(supportingColor, HyperColors.secondaryText),
            errorColor = resolvedErrorColor,
            cursorColor = resolveHyperContainerColor(cursorColor, HyperColors.accent),
            disabledContainerColor = resolveHyperContainerColor(
                disabledContainerColor,
                HyperColors.elevatedContainer.copy(alpha = 0.72f)
            ),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                resolvedContentColor.copy(alpha = HyperStyleDefaults.DisabledAlpha)
            )
        )
    }
}
