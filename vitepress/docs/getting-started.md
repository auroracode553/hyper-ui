# 接入与最小配置

## 接入前提

- 调用方是启用了 Jetpack Compose 的 Android 项目。
- 调用方 `minSdk` 不低于 30。
- HyperUI 只提供 UI，不要求调用方采用特定导航、网络或状态管理框架。

## JitPack 依赖

先从 [JitPack](https://jitpack.io/#auroracode553/hyper-ui) 或 [GitHub Tags](https://github.com/auroracode553/hyper-ui/tags) 读取最新可用 tag。文档不保存固定版本号。

在调用方 `settings.gradle.kts` 中添加 JitPack 仓库：

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

在调用方模块中添加依赖，将 `<latest-tag>` 替换为线上查询到的完整 tag：

```kotlin
dependencies {
    implementation("com.github.auroracode553:hyper-ui:<latest-tag>")
}
```

tag 是否带 `v` 前缀，以 JitPack 或仓库 Tags 页面显示的实际名称为准。

## 推荐 imports

HyperUI 的公开 API 都声明在根包：

```kotlin
import hyper_ui.*
```

Compose 状态、布局和 Material Icons 仍使用各自的标准包。例如：

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
```

禁止在调用方导入 `hyper_ui.core.*`，该目录仅供组件内部复用。

## 应用根节点

HyperUI 会读取 `MaterialTheme.colorScheme` 判断明暗配色，并从 `HyperThemeConfig` 获取主题色和成功色。

```kotlin
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import hyper_ui.*

@Composable
fun AppRoot() {
    MaterialTheme {
        HyperThemeConfig(
            themeColor = rgba(255, 103, 0),
            successColor = rgba(52, 199, 89)
        ) {
            AppContent()
        }
    }
}
```

如果应用已经有自己的 `MaterialTheme`，保留原有主题，只在其内容中增加 `HyperThemeConfig`。

## 最小状态示例

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import hyper_ui.*

@Composable
fun NotificationSetting() {
    var enabled by remember { mutableStateOf(true) }

    HyperSwitch(
        checked = enabled,
        onCheckedChange = { enabled = it }
    )
}
```

这里的 `enabled` 属于调用方；HyperUI 不会替调用方保存业务状态。

## 不应依赖的模块

调用方只依赖发布的 `hyper_ui` 库，不依赖以下目录：

- `preview/`：Desktop 与 Wasm 交互预览。
- `vitepress/`：文档站工程与使用说明源码，可供人或 AI 阅读，但不参与应用编译。
