# 全面屏窗口与全局安全区

包名 hyper_ui。Android Activity 在 setContent 前调用 enableHyperEdgeToEdge()，统一透明状态栏、透明导航栏、显示缺口绘制及关闭系统对比度底板。系统栏图标明暗由应用主题同步。

~~~kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableHyperEdgeToEdge()
    setContent {
        HyperThemeConfig(useSystemSafeArea = false) { AppContent() }
    }
}
~~~

## API

~~~kotlin
fun Activity.enableHyperEdgeToEdge()
~~~

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| 接收者 | Activity | 是 | — | 当前应用 Activity；不修改输入法宿主或其他应用窗口 |

HyperThemeConfig.useSystemSafeArea 默认为 false。导航、页面宿主、TabBar、弹窗、抽屉与更新浮层不增加系统栏或挖孔安全区；正文仍按真实业务上下栏高度布局。键盘 Insets 独立保留，不用系统安全区模拟键盘避让。调用方不要再添加 statusBarsPadding、navigationBarsPadding 或网页 CSS 的系统安全区留白。

<WasmPreview demo="theme-material" title="全局安全区布局开关" />

Preview 只模拟布局策略，Activity API 仅在 Android 正式应用中调用。
