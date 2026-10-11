# HyperSwitch

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="switch" title="HyperSwitch 交互预览" />

## 样式约定

开关 52×32dp，圆形滑块实际可见 22dp，轨道轮廓 1dp；160ms selectionCurve。关闭色为 controlTrack 合成到 surfaceStrong，禁用合成到 surfaceSubtle。

## 状态归属与使用约束

checked 由调用方持有；禁用时不派发回调。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperSwitch(checked, { checked = it })
```

## HyperSwitch API / Props

```kotlin
@Composable
fun HyperSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedTrackColor: Color = Color.Unspecified,
    uncheckedTrackColor: Color = Color.Unspecified,
    checkedThumbColor: Color = Color(1f, 1f, 1f, 1f),
    uncheckedThumbColor: Color = Color.Unspecified
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| checked | Boolean | 是 | — | 调用方持有的选择值。 |
| onCheckedChange | (Boolean) -&gt; Unit | 是 | — | 用户改变选中值时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| checkedTrackColor | Color | 否 | Color.Unspecified | 开启时轨道颜色。 |
| uncheckedTrackColor | Color | 否 | Color.Unspecified | 关闭时轨道颜色。 |
| checkedThumbColor | Color | 否 | Color(1f, 1f, 1f, 1f) | 开启时滑块颜色。 |
| uncheckedThumbColor | Color | 否 | Color.Unspecified | 关闭时滑块颜色。 |
