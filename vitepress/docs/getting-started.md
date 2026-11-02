# 接入与最小配置

## 支持范围

- Android 手机端 Jetpack Compose 项目。
- `minSdk` 不低于 30。
- 页面以竖屏单列布局为默认验收形态。

## 添加依赖

先从 [JitPack](https://jitpack.io/#auroracode553/hyper-ui) 或 [GitHub Tags](https://github.com/auroracode553/hyper-ui/tags) 查询最新 tag，文档不保存固定版本号。

```kotlin
dependencies {
    implementation("com.github.auroracode553:hyper-ui:<latest-tag>")
}
```

公开 API 从根包导入：

```kotlin
import hyper_ui.*
```

## 应用根节点

开始前先读[页面开发规则与 AGENTS.md 模板](mobile-guidelines.md)及[设计系统基线](style-reference.md)。规则需要合并到业务仓库，不能依赖 UI 库的 AGENTS.md 自动生效。

### 应用全局全面屏

在所有 Activity 共用的入口开启全面屏，独立提醒或编辑入口同样继承该配置。页面不要反复切换窗口模式，也不要在整个正文外添加底部安全区。

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

abstract class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        window.isNavigationBarContrastEnforced = false
    }
}
```

enableEdgeToEdge 处理透明状态栏与手势导航栏。应用自行选择亮暗主题时，在共用窗口配置处同步 SystemBarStyle，保持系统图标可读；不要逐页绘制系统栏色块。表单宿主处理键盘避让，具体控件或滚动末尾处理一次必要的系统安全区。

`HyperThemeConfig` 提供主题色、成功色、深浅色、排版、形状与系统安全区策略，普通组件使用实色表面；TabBar、Slider、Switch、Dialog 固定使用组件内玻璃。颜色使用 `rgba`，不要写十六进制 `Color(0xFF...)`。

```kotlin
@Composable
fun AppRoot() {
    HyperThemeConfig(
        themeColor = rgba(71, 111, 232),
        successColor = rgba(34, 197, 94)
    ) {
        AppHome()
    }
}
```

## 手机页面示例

页面状态放在调用方，组件只接收值和回调：

```kotlin
@Composable
fun AppHome() {
    var query by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(true) }

    val scrollState = rememberLazyListState()
    HyperNavBarPage(scrollState = scrollState,
        modifier = Modifier.fillMaxSize().background(HyperColors.pageBackground).imePadding(),
        contentPadding = PaddingValues(16.dp),
        navBar = {
            HyperNavBar(title = { HyperText("设置") })
        }
    ) { padding ->
      LazyColumn(state = scrollState, contentPadding = padding,
          verticalArrangement = Arrangement.spacedBy(12.dp)) {
       item { HyperTextField(
            value = query,
            onChanged = { query = it },
            type = "text",
            placeholder = { HyperText("搜索设置") }
        ) }
       item { HyperMenuGroup { HyperListTile(
            title = { HyperText("接收通知") },
            trailing = {
                HyperSwitch(
                    value = notificationsEnabled,
                    onChanged = { notificationsEnabled = it }
                )
            }
        ) } }
      }
    }
}
```

## API 选择顺序

1. 先用 `type` 选择组件形态。
2. 各组件采用一套默认规格，不设置 size 档位；页面布局通过 Modifier 组合。
3. 外部宽度和间距使用 `modifier`。
4. 业务状态通过参数和回调注入。
5. 只有组件页明确列出的 Slot 才能注入内容。

## 图标

库与 Preview 直接使用 com.composables:icons-lucide-cmp:2.2.1 的 Lucide 图标。调用方直接引用 Lucide 时，在业务模块显式声明同一依赖。通用图标不复制 SVG／字体路径，也不额外维护图标别名表。

## 不应依赖的目录

- `preview/`：文档交互示例和预览宿主，不是应用依赖。
- `vitepress/`：文档站源码，不参与应用编译。
- `hyper_ui.core`：组件内部工具包，不作为调用方 API。


## 基础外观与安全区

普通组件表面固定为实色，TabBar、Slider、Switch、Dialog 使用组件内固定玻璃。颜色、字体、形状与明暗模式由基础主题宿主提供。

## HyperThemeConfig API / Props

```kotlin
@Composable
fun HyperThemeConfig(
    themeColor: Color = HyperStyleDefaults.DefaultThemeColor,
    successColor: Color = HyperStyleDefaults.SuccessColor,
    darkTheme: Boolean = isSystemInDarkTheme(),
    typography: HyperTypography = HyperStyleDefaults.Typography,
    shapes: HyperShapes = HyperStyleDefaults.Shapes,
    useSystemSafeArea: Boolean = false,
    content: @Composable () -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| themeColor | Color | 否 | HyperStyleDefaults.DefaultThemeColor | 品牌色 rgba(71, 111, 232) |
| successColor | Color | 否 | HyperStyleDefaults.SuccessColor | 成功色 rgba(34, 197, 94) |
| darkTheme | Boolean | 否 | isSystemInDarkTheme() | 浅/深色模式，默认跟随系统 |
| typography | HyperTypography | 否 | HyperStyleDefaults.Typography | 主题字体与排版 |
| shapes | HyperShapes | 否 | HyperStyleDefaults.Shapes | 通用形状；组件的专用圆角见各组件页 |
| content | @Composable () -&gt; Unit | 是 | — | 使用该主题的子树 |
| useSystemSafeArea | Boolean | 否 | false | 全局系统安全区；默认全面屏，不补状态栏、手势条及显示缺口留白，键盘避让独立保留 |

Android Activity 在 setContent 前统一调用 [enableHyperEdgeToEdge()](components/tools/hyper-edge-to-edge.md)。组件通过 HyperTheme.systemSafeAreaInsets 读取主题策略，默认返回零 Insets；页面不得再重复添加根系统栏 padding；顶部交互保护使用 HyperNavBar 和固定顶部操作容器，不受此开关影响。


## HyperTypography 配置 Props

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| headlineSmall | TextStyle | 否 | Default 字体，Medium，24sp / 32sp | 大标题 |
| bodyLarge | TextStyle | 否 | Default 字体，Normal，16sp / 24sp，字距 0sp | 较大正文 |
| bodyMedium | TextStyle | 否 | Default 字体，Normal，14sp / 20sp，字距 0sp | 正文基础样式 |
| bodySmall | TextStyle | 否 | Default 字体，Normal，12sp / 16sp，字距 0sp | 辅助文字 |
| titleLarge | TextStyle | 否 | Default 字体，Medium，22sp / 28sp，字距 0sp | 大标题基础 |
| titleMedium | TextStyle | 否 | Default 字体，Medium，16sp / 24sp，字距 0sp | 标题基础 |
| titleSmall | TextStyle | 否 | Default 字体，Medium，14sp / 20sp，字距 0sp | 小标题基础 |
| labelLarge | TextStyle | 否 | Default 字体，Medium，14sp / 20sp，字距 0sp | 操作标签基础 |
| labelMedium | TextStyle | 否 | Default 字体，Medium，12sp / 16sp，字距 0sp | 中标签基础 |
| labelSmall | TextStyle | 否 | Default 字体，Medium，11sp / 16sp，字距 0sp | 小标签基础 |

各组件会在基础字体族上应用参考字号和行高。主题默认 HyperText 正文为 14sp、19.6sp 行高。

## HyperShapes 与 HyperThemeColors 配置 Props

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| extraSmall | Shape | 否 | RoundedCornerShape(7.dp) | 最小圆角 |
| small | Shape | 否 | RoundedCornerShape(16.dp) | 小圆角 |
| medium | Shape | 否 | RoundedCornerShape(24.dp) | 中圆角 |
| large | Shape | 否 | RoundedCornerShape(28.dp) | 大圆角 |
| themeColor | Color | 否 | HyperStyleDefaults.DefaultThemeColor | HyperThemeColors 品牌色 |
| successColor | Color | 否 | HyperStyleDefaults.SuccessColor | HyperThemeColors 成功色 |

## rgba API / Props

```kotlin
fun rgba(red: Int, green: Int, blue: Int, alpha: Float = 1f): Color
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| red | Int | 是 | — | 红分量，限制到 0..255 |
| green | Int | 是 | — | 绿分量，限制到 0..255 |
| blue | Int | 是 | — | 蓝分量，限制到 0..255 |
| alpha | Float | 否 | 1f | 透明度，限制到 0..1 |

也可用 Color(red, green, blue, alpha) 四个 Float 分量构造；禁止十六进制颜色构造。

## 状态归属与平台

调用方持有深浅色模式和品牌色。HyperTheme 的 colors、typography、shapes、isDark 为当前 Composition 的只读属性；HyperColors 的语义色也仅能在 Composable 中读取，不能缓存为全局变量。公开产品支持 Android 手机 320–430dp 竖屏，Desktop/Wasm 仅用于预览。

具体默认尺寸见 [样式对照](style-reference.md)；组件签名见 [组件索引](component-index.md)。

全面屏顶部布局：根系统安全区开关不影响顶部操作保护。标准 HyperNavBar 自动处理，自定义固定顶栏使用 Compose 窗口布局；正文首屏净空放在滚动内容内部，背景与滚动视口可覆盖状态栏。


## HyperSpacing API

公开包名为 hyper_ui。共用布局值不持有状态，也不会自动向组件插入边距。

```kotlin
object HyperSpacing {
    val PageHorizontal: Dp = 14.dp
    val PageVertical: Dp = 12.dp
    val SectionVertical: Dp = 16.dp
}
```

| 属性 | 类型 | 默认值 | 作用 |
| --- | --- | --- | --- |
| PageHorizontal | Dp | 14.dp | 页面内容、搜索与列表共用的水平留白；调用方在父容器或 contentPadding 中使用一次。 |
| PageVertical | Dp | 12.dp | 设置页正文首尾留白，不含导航或系统安全区。 |
| SectionVertical | Dp | 16.dp | 相邻完整设置分组之间的间距。 |

```kotlin
HyperList(contentPadding = PaddingValues(horizontal = HyperSpacing.PageHorizontal)) {
    items(entries, key = { it.id }) { entry ->
        HyperListTile(title = { HyperText(entry.title) })
    }
}
```

页面列表与 plain 行默认水平留白为 0dp，卡片内边距独立保留，不需要每个应用再定义相同数值。输入框内部文本间距、图标与文字间距以及垂直触控空间仍由各组件负责。示例与可操作的留白开关见 [HyperList](components/list/hyper-list.md)。

卡片边界：页面外边距与卡片内边距不能互相替代。HyperMenuGroup 为内容提供水平 14dp 内边距；独立 glass 列表行同样保留内部 14dp；plain 行不绘制卡片，使用父页面或父卡片已经提供的留白。
