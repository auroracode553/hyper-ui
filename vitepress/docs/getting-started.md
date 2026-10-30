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

`HyperThemeConfig` 提供主题色、成功色、排版、形状、material 材质档位和玻璃令牌；默认 material = HyperMaterial.Solid。颜色使用 `rgba`，不要写十六进制 `Color(0xFF...)`。

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
