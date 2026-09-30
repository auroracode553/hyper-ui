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

`HyperThemeConfig` 提供主题色、成功色、排版、形状和玻璃令牌。颜色使用 `rgba`，不要写十六进制 `Color(0xFF...)`。

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperNavBar(
            titleContent = { HyperText("设置") },
            size = "default"
        )
        HyperTextField(
            value = query,
            onValueChange = { query = it },
            type = "text",
            size = "default",
            placeholderContent = { HyperText("搜索设置") }
        )
        HyperListItem(
            headlineContent = { HyperText("接收通知") },
            trailingContent = {
                HyperSwitch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }
        )
    }
}
```

## API 选择顺序

1. 先用 `type` 选择组件形态。
2. 再用 `size` 选择 `small`、`default` 或 `large`。
3. 外部宽度和间距使用 `modifier`。
4. 业务状态通过参数和回调注入。
5. 只有组件页明确列出的 Slot 才能注入内容。

## 图标

库内语义图标使用 Lucide VectorDrawable。调用方直接引用 Lucide 资源时，需要在自己的模块显式声明对应依赖；少量图标不要引入 `material-icons-extended`。

## 不应依赖的目录

- `preview/`：文档交互示例和预览宿主，不是应用依赖。
- `vitepress/`：文档站源码，不参与应用编译。
- `hyper_ui.core`：组件内部工具包，不作为调用方 API。
