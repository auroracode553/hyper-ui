# HyperIconButton

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/button/HyperIconButton.kt`
- 预览：`icon_button`

`HyperIconButton` 是默认 38dp 的 slot-first 点击容器，默认图标尺寸为 18dp。它不接收 `ImageVector`；调用方在 `content` slot 中放入任意 `Icon`、进度或状态内容。
默认容器按澎湃 OS 风格的磨砂玻璃圆片实现：明暗主题都以白色半透明材质叠在页面上，深色背景自然混合为中灰玻璃；宽而弱的上沿柔光、底部轻微压暗和面内渐隐折射带表达材质。组件不绘制描边，只复用公共 `hyperSurfaceShadow` 内部能力提供单层悬浮阴影；浅色模式使用更高抬升和更深的黑色阴影，避免按钮消失在白色背景中。

## 公开签名

```kotlin
data class HyperIconButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val pressedContainerColor: Color,
    val pressedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

@Composable
fun HyperIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = HyperIconButtonDefaults.Shape,
    colors: HyperIconButtonColors = HyperIconButtonDefaults.colors(),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
)
```

## 关键公开类型

```kotlin
object HyperIconButtonDefaults {
    val Size = 38.dp
    val IconSize = 18.dp
    val Shape: Shape = CircleShape

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        pressedContainerColor: Color = Color.Unspecified,
        pressedContentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperIconButtonColors
}
```

## 最小用法

```kotlin
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR

HyperIconButton(onClick = onSearch) {
    Icon(
        painter = painterResource(LucideR.drawable.lucide_ic_search),
        contentDescription = "搜索",
        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
    )
}
```

自定义播放器按钮色：

```kotlin
HyperIconButton(
    onClick = onPlay,
    modifier = Modifier.size(56.dp),
    colors = HyperIconButtonDefaults.colors(
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.64f),
        pressedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.76f),
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        pressedContentColor = MaterialTheme.colorScheme.primary
    )
) {
    Icon(
        painter = painterResource(LucideR.drawable.lucide_ic_play),
        contentDescription = "播放"
    )
}
```

## 约束

- 不存在 `imageVector`、`contentDescription`、`tint`、`backgroundColor` 参数；这些通过 slot 或 `colors` 表达。
- 默认尺寸为 `HyperIconButtonDefaults.Size`；自定义尺寸使用 `modifier = Modifier.size(...)`。
- Android 调用方需要通用图标时，优先使用可通过资源裁剪按引用保留的 `com.composables:icons-lucide-android:2.2.1`；HyperUI 不传递该可选依赖。
- 默认容器在浅色主题使用白色 `0.72f` alpha，在深色主题使用白色 `0.34f` alpha；深色模式不再使用近黑底材。内容色使用 `HyperColors.primaryText`。
- 默认态通过公共阴影能力绘制单层投影：浅色模式为 8dp，并使用 `0.10f` 环境阴影和 `0.24f` 聚光阴影；深色模式维持 6dp。按压时阴影收低，禁用态不产生投影，所有状态都不绘制外层描边。
- 自定义 `containerColor` 会作为玻璃底色继续叠加材质高光。需要保留背景透色时，应传入带 alpha 的颜色；传入完全不透明的颜色则得到更厚重的染色玻璃。
- 组件不采样或模糊调用方背景，玻璃层由自身半透明底色、光学渐变和投影构成，不会引入离屏背景捕获。
- 玻璃按钮适合直接浮在页面内容或背景上；避免把浅色半透明按钮叠在另一层浅色玻璃容器上，否则材质边界会变浑浊。
- `LocalContentColor` 会传递给 slot 内容。
- 按下时立即缩放至 `0.97f`，同时收低容器、内容与投影强度，不等待点击释放；不绘制 Android ripple，也不执行补间动画。业务状态仍由调用方维护。

## 从旧版迁移

- 删除 `HyperIconButtonColors` 中的 `outlineColor`、`pressedOutlineColor`、`disabledOutlineColor`。
- 删除 `HyperIconButtonDefaults.colors(...)` 中同名参数以及 `HyperIconButtonDefaults.OutlineWidth`。
- 旧调用点直接移除这些参数即可；当前组件内部也不再生成低对比度描边，不恢复公开描边参数或兼容别名。

<WasmPreview demo="icon_button" title="HyperIconButton 交互预览" />
