---
description: 提供悬浮、非悬浮贴底导航与可重复触发的动作入口。
---

# HyperTabBar

底部入口，提供单动作与多项导航；多项支持悬浮和非悬浮贴底布局。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="tab-bar-multiple" title="HyperTabBar 交互预览" />

栏内为首页、最近、列表、发现、设置五项导航，消息以 48dp 独立入口放在栏外；两端示例以此 Kotlin 文档及对应 Preview 为基准。

<WasmPreview demo="tab-bar-docked" title="非悬浮动作栏：重复点击与禁用项" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperTabBar(items, selectedIndex = selectedIndex, onChanged = { selectedIndex = it }, itemLabel = { it.label }) { item ->
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1.dp)) {
        HyperIcon(item.icon, null)
        HyperText(item.label)
    }
}
```

<WasmPreview demo="tab-bar-single" title="单动作与角标" />

</details>

## 使用约定

**样式约定**

type 为 multiple 或 single。floating 默认 true，保留内容收缩的悬浮胶囊：multiple 默认高 56dp，单元格为 max(76dp, 最宽 12sp 粗体标签 +32dp)，容器另加 8dp；外间距 start/end 12dp、top 8dp、bottom 20dp。floating=false 仅用于 multiple，默认零外间距，全宽平直页面表面，无胶囊、圆角或浮层阴影，底部安全区位于表面内部。图标 23dp、文字 12sp、间距 1dp。选中镜片保持按钮组内容高度减 8dp，默认可见高 48dp、上下各 4dp；点击区域覆盖整项，不单独压缩镜片高度。single 使用固定直径 48dp 的圆形玻璃表面、22dp 图标、16dp 角标。

非悬浮形态直接放入 HyperScaffold.bottomBar，内部固定通过 HyperSafeArea(edge = HyperSafeAreaEdge.Bottom) 保护操作行，独立于根 useSystemSafeArea 开关和 safeArea 参数。竖屏底部取真实导航栏／缺口与最小 20dp 的最大值，横屏只避让实际底部系统区；同侧嵌套只消费一次，不包含 IME。safeArea 参数仅控制悬浮栏的系统区避让。

悬浮多项与贴底选择模式以 56dp 为默认内容高度，按标签字体在库内扩展。非悬浮动作模式（floating=false、selectedIndex=null）按图文实际高度测量，56dp 作为包含安全区的整栏最小高度；默认零 margin 时，总高为 max(56dp, 图文高度 + 未消费的底部安全区)，字号较大时最小高度也随原字号规则扩展。single 独立入口固定为 48dp，不随按钮组高度或标签字体放大。

非悬浮栏的底色连同安全区延伸到底边，可点击内容只位于安全区上方。调用方不再额外叠加系统底部 inset、固定留白或业务 height；宿主让正文经过底栏后方，末尾余量只放进滚动内容。

```kotlin
HyperScaffold(bottomBar = {
    HyperTabBar(items = actions, floating = false, selectedIndex = null,
        onChanged = onAction, itemLabel = { it.label }) { HyperText(it.label) }
}) { padding ->
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = padding) {
        // 正文；padding 已包含实测底栏及其安全区，不再叠加底部 Insets。
    }
}
```

**状态归属**

悬浮容器固定使用 8dp 背景模糊、24dp 边缘深度及 24dp 折射位移。选中镜片合成容器与主题色标签的独立录制层，按压时从灰色选中底面过渡到透镜：折射边缘 0→10dp、位移 0→14dp，七波段色散、方向高光和 0→8dp 内阴影随按压进度变化。镜片位于标签上方，图标文字会随镜片边缘折射；原插槽只组合一次，点击与语义不重复。

拖动按手指位置连续预览，点击与松手吸附使用临界阻尼弹簧（stiffness 1000、dampingRatio 1）。点击及切换途中，整条 Bar 保持原比例；镜片按压目标为横向 1.04、纵向 1.08，横纵弹簧分别为 dampingRatio 0.85/0.9、stiffness 400。移动速度先除以 max(1, 项数−1) 归一化，再经 dampingRatio 0.85、stiffness 400 弹簧平滑；其绝对值除以 10 并限制至 0–1，控制横向最多附加 3%、纵向最多压缩 1.5%。最终横纵缩放限制在 0.98–1.08 和 0.98–1.10，左右切换使用相同幅度。镜片距目标小于总行程的 2.5% 后释放，回弹至原形。

横向拖动带动悬浮 Bar 与镜片同向偏移：按累计位移 / 栏宽经 EaseOut 缓动映射到最多 ±4dp，松手或取消使用 dampingRatio 1、stiffness 300 回正；柔和交互高光跟随镜片中心。贴底形态不偏移整条栏。以上仅改变瞬时绘制，不修改测量和布局；single 48dp、multiple 默认 56dp、外边距、安全区、点击区域及正文滚动范围保持原值。选中、取消与外部 selectedIndex 更新共用同一视觉位置；取消返回受控值，拖动松手才通知 onChanged，禁用项不触发按压动效。

multiple 只接受 2–5 项，single 恰好 1 项，超出范围抛出 IllegalArgumentException，不静默截断。itemLabel 必须与实际显示文字一致。320..430dp 内父约束优先；选中值由调用方管理。

itemEnabled 返回 false 的项目降低透明度并停止点击及拖动提交。single 点击时调用 onChanged(0)；multiple 在 selectedIndex 非空时仍采用选择模式，重复点击当前项不回调，拖动松手才提交。

selectedIndex=null 表示动作模式：没有常驻选中块，每次有效点击都会调用 onChanged，且横向拖动不触发动作。浏览器的前进、后退、标签管理和菜单应采用此模式，不能伪造固定选中项。

非悬浮动作模式按同一行中最高的插槽测量内容，各项共享内容行高和点击区域，不强制在安全区上方保留 56dp。真实系统 Insets 与最小安全区照常生效，系统手势区不接收菜单点击；悬浮栏与有选中值的导航模式继续使用原有居中布局。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperTabBar

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun <T> HyperTabBar(
    items: List<T>,
    onChanged: (Int) -> Unit,
    selectedIndex: Int?,
    modifier: Modifier = Modifier,
    type: String = "multiple",
    itemLabel: (T) -> String = { "" },
    badgeCount: Int = 0,
    safeArea: Boolean = false,
    floating: Boolean = true,
    margin: PaddingValues = if (!floating || type == "single") PaddingValues(0.dp) else HyperTabBarDefaults.FloatingMargin,
    color: Color = HyperColors.accent,
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 由调用方提供的选项；multiple 为 2–5 项，single 为 1 项。 |
| onChanged | (Int) -&gt; Unit | 是 | — | 用户修改值或选择项时通知调用方。 |
| selectedIndex | Int? | 是 | — | 当前选中索引；null 为可重复点击的动作模式。 |
| modifier | Modifier | 否 | Modifier | 外部宽度、位置和间距；高度与直径由库管理。 |
| type | String | 否 | &quot;multiple&quot; | multiple 默认高 56dp；非悬浮动作模式以此为含安全区的整栏最小高度。single 为固定直径 48dp 的独立圆形入口。 |
| itemLabel | (T) -&gt; String | 否 | { &quot;&quot; } | 每个导航项的文字。 |
| badgeCount | Int | 否 | 0 | 单动作角标数量，0 隐藏，超过 99 显示 99+。 |
| safeArea | Boolean | 否 | false | 仅控制悬浮栏的底部系统区避让；非悬浮栏固定通过 HyperSafeArea 保护操作行，不受此值影响。 |
| floating | Boolean | 否 | true | true 为悬浮胶囊；false 为全宽非悬浮 multiple 底栏，内部包含底部安全区。 |
| margin | PaddingValues | 否 | 非悬浮与 single 为零；悬浮 multiple 为 start/end 12dp、top 8dp、bottom 20dp | 导航容器外部间距。 |
| color | Color | 否 | HyperColors.accent | 自定义颜色，覆盖默认配色。 |
| itemEnabled | (T) -&gt; Boolean | 否 | { true } | 每项是否可交互，禁用项透明度为 0.4。 |
| itemContent | @Composable HyperTabBarItemScope.(item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |

### HyperTabBarItemScope

itemContent 的接收者，仅由组件创建。

```kotlin
class HyperTabBarItemScope internal constructor(val selected: Boolean)
```

| 属性 | 类型 | 作用 |
| --- | --- | --- |
| selected | Boolean | 当前项是否选中；single 始终为 true。 |

## 默认尺寸与并排操作

业务调用不得给 HyperTabBar 或它的包裹容器设置 height、requiredHeight、size 等限制高度的修饰符，也不能通过缩放替代库默认规格。single 必须保持库内固定的 48dp 直径，multiple 按钮组默认高 56dp；禁止为“并排等高”将独立入口扩大为 56dp。宽度、页面位置与组合间距由宿主控制，同一 Row 使用 Alignment.CenterVertically，保留两种形态的尺寸差异。组合示例的独立入口与导航间距为 8dp；导航胶囊内部最多 5 项，胶囊外最多另放 1 个独立入口，两者分别计数；消息入口在胶囊外，不替换或删减导航项。

<WasmPreview demo="tab-bar-trailing" title="56dp 主导航与 48dp 独立操作居中并排" />

```kotlin
Row(Modifier.padding(HyperTabBarDefaults.FloatingMargin),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically) {
    HyperTabBar(items = tabs, selectedIndex = index, onChanged = onTab,
        margin = PaddingValues(0.dp)) { HyperText(it) }
    HyperTabBar(items = listOf("打开"), type = "single", selectedIndex = null,
        onChanged = { onOpen() }) { HyperIcon(openIcon, "打开") }
}
```

| 默认值 | 类型 | 必填 | 值 | 用途 |
| --- | --- | --- | --- | --- |
| HyperTabBarDefaults.SingleDiameter | Dp | 否 | 48dp | single 独立入口的固定直径，不与按钮组等高 |
| HyperTabBarDefaults.Height | Dp | 否 | 56dp | multiple 默认内容高度；非悬浮动作模式为含安全区的整栏最小高度；字体扩展由组件内部负责 |
| HyperTabBarDefaults.FloatingMargin | PaddingValues | 否 | start/end 12dp，top 8dp、bottom 20dp | 独立悬浮导航默认外边距；并排布局由共同 Row 消费一次 |

正文末尾余量优先消费 HyperScaffold 测量的 bottom padding，不能用另一套手写底栏高度。

背景由 HyperBackdrop.background 提供，组件放在 content 中；这只定义取样来源，不是材质开关。未提供背景源时保留半透明底色与按压反馈。Android 13+ 执行折射、Android 12/12L 仅背景模糊，旧系统保留底色和交互。文档 Wasm 使用 Skia 执行同一模型。
