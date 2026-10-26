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

HyperThemeConfig.useSystemSafeArea 默认为 false，根 Scaffold 与 TabBar 不留系统安全区。HyperNavBar 仍保护顶部交互区一次；自定义固定操作栏使用 Compose 窗口布局。正文视口保持全屏，HyperNavBarPage 测量的首屏顶部净空放入滚动内容，随上滑滚出。键盘 Insets 独立保留。禁止整页重复叠加系统栏 Padding。

<WasmPreview demo="theme-material" title="全局安全区布局开关" />

Preview 只模拟布局策略，Activity API 仅在 Android 正式应用中调用。
