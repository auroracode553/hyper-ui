package hyper_ui

import androidx.compose.ui.unit.Dp

/**
 * Shared size vocabulary used by public components.
 * Flutter HyperUI uses the same `small`/`default`/`large` strings so callers
 * can change visual scale without passing component-specific pixel values.
 */
internal fun requireHyperComponentSize(size: String) {
    require(size == "small" || size == "default" || size == "large") {
        "size 必须是 small、default 或 large，实际为: $size"
    }
}

internal fun hyperComponentSize(
    size: String,
    small: Dp,
    normal: Dp,
    large: Dp
): Dp {
    requireHyperComponentSize(size)
    return when (size) {
        "small" -> small
        "large" -> large
        else -> normal
    }
}
