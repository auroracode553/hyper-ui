# 接入与最小配置

## 接入前提

- 调用方是启用了 Jetpack Compose 的 Android 项目。
- 调用方 `minSdk` 不低于 30。
- HyperUI 只提供 UI，不要求调用方采用特定导航、网络或状态管理框架。

## Maven 依赖

```kotlin
dependencies {
    implementation("com.hyperui:hyper-ui:1.0.0")
}
```

仓库地址取决于实际发布位置。调用方需要在 `settings.gradle.kts` 中自行配置对应 Maven 仓库；JitPack 坐标不能直接套用以上坐标。

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
