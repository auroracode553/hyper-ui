# HyperTooltip

轻量提示浮层。状态由组件根据锚点悬停事件管理，内容通过 slot 提供。

```kotlin
HyperTooltip(text = "提示文本") {
    HyperText("悬停查看")
}
```

提示会在锚点上方显示，空间不足时自动放到下方。
