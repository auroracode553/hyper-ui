package hyper_ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

/**
 * 横向筛选标签项数据。
 *
 * 用于 [HyperFilterChipBar] 的数据源，描述单个筛选项的标识、文案与可选计数。
 *
 * @param T 项标识类型，允许为 nullable 以表达“全部/清除选择”语义
 * @param key 项的唯一标识，可传入 null 表示“全部”项
 * @param label 显示文案
 * @param count 计数，为 null 时不显示计数
 * @param enabled 是否可用，为 false 时不可点击
 */
@Immutable
data class HyperFilterChipItem<T>(
    val key: T,
    val label: String,
    val count: Int? = null,
    val enabled: Boolean = true
)

/**
 * 单个筛选标签。
 *
 * 选中态使用主题色背景配白色文字；未选中态使用半透明毛玻璃托盘配主文字色。
 * 托盘风格与 [HyperIconButton] 一致：半透明 `elevatedContainer` 背景 + `glassHighlightBrush` 顶部高光渐变叠层，
 * 形成统一的玻璃质感容器语言。
 *
 * 适用于分类、过滤等单选场景，常作为 [HyperFilterChipBar] 的默认子项，
 * 也可独立使用。
 *
 * @param selected 是否选中
 * @param onClick 点击回调
 * @param label 显示文案
 * @param modifier 外层布局修饰符
 * @param count 计数，为 null 时不显示计数
 * @param enabled 是否可用，为 false 时不触发回调
 * @param selectedColor 选中背景色，未指定时使用 [HyperColors.accent]
 * @param unselectedColor 未选中背景色，未指定时使用 [HyperColors.elevatedContainer]
 * @param selectedTextColor 选中文字色，未指定时使用白色
 * @param unselectedTextColor 未选中文字色，未指定时使用 [HyperColors.primaryText]
 */
@Composable
fun HyperFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    count: Int? = null,
    enabled: Boolean = true,
    selectedColor: Color = Color.Unspecified,
    unselectedColor: Color = Color.Unspecified,
    selectedTextColor: Color = Color.Unspecified,
    unselectedTextColor: Color = Color.Unspecified
) {
    // 对齐 HyperIconButton 的容器与玻璃高光范式：
    // 1. 标记是否使用默认背景 → 禁用态使用 disabledContainer 回退
    // 2. 若背景可见（alpha > 0）叠 glassHighlightBrush 顶部高光渐变
    val usesDefaultContainerColor = if (selected) {
        selectedColor == Color.Unspecified
    } else {
        unselectedColor == Color.Unspecified
    }
    val resolvedContainerColor = if (selected) {
        if (selectedColor == Color.Unspecified) HyperColors.accent else selectedColor
    } else {
        if (unselectedColor == Color.Unspecified) HyperColors.elevatedContainer else unselectedColor
    }
    val contentAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha
    val containerColor = if (enabled) {
        resolvedContainerColor
    } else if (usesDefaultContainerColor) {
        HyperColors.disabledContainer
    } else {
        resolvedContainerColor.copy(alpha = resolvedContainerColor.alpha * contentAlpha)
    }
    val hasVisibleBackground = resolvedContainerColor.alpha > 0f
    val highlightModifier = if (hasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }
    val resolvedSelectedTextColor = if (selectedTextColor == Color.Unspecified) {
        rgba(255, 255, 255, 1f)
    } else {
        selectedTextColor
    }
    val resolvedUnselectedTextColor = if (unselectedTextColor == Color.Unspecified) {
        HyperColors.primaryText
    } else {
        unselectedTextColor
    }
    val backgroundColor by animateColorAsState(
        targetValue = containerColor,
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioNoBouncy
        ),
        label = "hyperFilterChipBackground"
    )
    val textColor by animateColorAsState(
        targetValue = (if (selected) resolvedSelectedTextColor else resolvedUnselectedTextColor).copy(alpha = contentAlpha),
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioNoBouncy
        ),
        label = "hyperFilterChipText"
    )
    val shape = RoundedCornerShape(percent = 50)

    Row(
        modifier = modifier
            .height(HyperFilterChipDefaults.Height)
            .clip(shape)
            .background(backgroundColor)
            .then(highlightModifier)
            .hyperNoRippleClickable(
                enabled = enabled,
                role = Role.Tab,
                onClick = onClick
            )
            .padding(horizontal = HyperFilterChipDefaults.HorizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HyperFilterChipDefaults.CountSpacing)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = HyperFilterChipDefaults.LabelFontSize,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (count != null) {
            Text(
                text = count.toString(),
                color = textColor.copy(alpha = HyperFilterChipDefaults.CountAlpha),
                fontSize = HyperFilterChipDefaults.CountFontSize,
                maxLines = 1
            )
        }
    }
}

/**
 * 横向滚动的筛选标签栏。
 *
 * 默认渲染 [HyperFilterChip]；可通过 [chip] 插槽自定义项内容。
 * 选中态由 [selectedKey] 决定，点击某项时回传该项的 key（可为 null）。
 *
 * 适用于“全部 / 分类 A / 分类 B …”这类横向滚动单选过滤场景，
 * 例如拦截日志分类、消息分类、下载状态过滤等。
 *
 * @param T 项标识类型，允许为 nullable 以表达“全部/清除选择”语义
 * @param items 标签项列表
 * @param selectedKey 当前选中项的 key，null 表示未选中或“全部”
 * @param onSelected 选中回调，回传被点击项的 key
 * @param modifier 外层布局修饰符
 * @param contentPadding 内容内边距
 * @param horizontalArrangement 项之间的水平排列
 * @param chip 自定义项渲染，默认渲染 [HyperFilterChip]
 */
@Composable
fun <T> HyperFilterChipBar(
    items: List<HyperFilterChipItem<T>>,
    selectedKey: T?,
    onSelected: (T?) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = HyperFilterChipDefaults.BarHorizontalPadding,
        vertical = HyperFilterChipDefaults.BarVerticalPadding
    ),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperFilterChipDefaults.Spacing),
    chip: @Composable (item: HyperFilterChipItem<T>, selected: Boolean) -> Unit = { item, selected ->
        HyperFilterChip(
            selected = selected,
            onClick = { onSelected(item.key) },
            label = item.label,
            count = item.count,
            enabled = item.enabled
        )
    }
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement
    ) {
        items(items) { item ->
            chip(item, item.key == selectedKey)
        }
    }
}

/**
 * HyperFilterChip 系列组件的尺寸与样式常量。
 */
object HyperFilterChipDefaults {
    /** 单个标签的高度 */
    val Height = 32.dp

    /** 单个标签的左右内边距 */
    val HorizontalPadding = 14.dp

    /** 标签文案与计数之间的间距 */
    val CountSpacing = 4.dp

    /** 标签栏中各项之间的间距 */
    val Spacing = 8.dp

    /** 标签栏默认水平内边距 */
    val BarHorizontalPadding = 16.dp

    /** 标签栏默认垂直内边距 */
    val BarVerticalPadding = 8.dp

    /** 标签文案字号 */
    val LabelFontSize = 13.sp

    /** 计数文案字号 */
    val CountFontSize = 12.sp

    /** 计数文案相对标签文案的透明度 */
    const val CountAlpha = 0.7f
}
