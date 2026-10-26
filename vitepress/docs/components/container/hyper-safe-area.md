---
description: 复用顶部和底部安全区布局，统一最小底部空间，不修改窗口或键盘高度。
---

# HyperSafeArea

包名 hyper_ui。Android 手机端按需布局容器，根画布保持全面屏，只保护包裹的操作内容。适用于计算器底部数字键盘、输入法自身面板与固定操作区。

<WasmPreview demo="safe-area" title="方向、系统区与嵌套交互" />

## 设计与使用

- Top 读取可见状态栏与显示缺口的顶部并集；隐藏状态栏后仍保护实际缺口。
- Bottom 在竖屏取导航栏／底部缺口与库内最小 20dp 的最大值；横屏取消最小撑高，只避让仍然存在的实际底部系统区。应用不提供私有高度。
- 组件独立于根 useSystemSafeArea 开关，显式包裹时按需生效；不绘制色块，不修改 Activity / IME 窗口标记。
- 使用 Compose Insets 消费机制，同侧嵌套不会重复添加系统区或最小留白。
- 不包含 IME Insets。普通页面继续由宿主处理 imePadding；输入法只包裹自身面板，保留按键区配置高度。
- 滚动页面首屏净空仍放入滚动内容，标准导航使用 HyperNavBarPage；不要把整个滚动视口套成固定顶部留白。

```kotlin
HyperSafeArea { NumericKeypad() }
HyperSafeArea(edge = HyperSafeAreaEdge.Top) { CustomHeader() }
```

## API / Props

```kotlin
enum class HyperSafeAreaEdge { Top, Bottom }

@Composable
fun HyperSafeArea(
    edge: HyperSafeAreaEdge = HyperSafeAreaEdge.Bottom,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| edge | HyperSafeAreaEdge | 否 | Bottom | 选择顶部或底部内容保护 |
| modifier | Modifier | 否 | Modifier | 容器宽度、位置与背景；不需要业务手写安全区高度 |
| content | Composable BoxScope.() -> Unit | 是 | — | 不缩减自身业务尺寸的内容 |

| 公开值 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| Top | HyperSafeAreaEdge | 否 | — | 状态栏与缺口顶部并集 |
| Bottom | HyperSafeAreaEdge | 否 | — | 导航栏、缺口与最小底部空间的最大值 |
| HyperSafeAreaDefaults.MinimumBottomSpacing | Dp | 否 | 20dp | UI 库维护的竖屏最小底部空间；横屏不应用这段撑高，与悬浮 TabBar 共用间距令牌 |

源码：components/layout/HyperSafeAreaLayout.kt；根安全区策略仍位于 theme/HyperSafeArea.kt。计算器在共用 NumericKeypad 接入一次；输入法在 KeyboardScreen 面板外接入，新增空间由输入窗口测量承担，不从字母行或候选区扣除。

Android 方向通过 LocalConfiguration 读取，不按容器宽高推断；因此竖屏中的矮宽 IME 窗口仍按竖屏处理。顶部始终按实际状态栏／缺口计算，不因横屏凭空增加空白；左右边缘布局不由本组件处理。
