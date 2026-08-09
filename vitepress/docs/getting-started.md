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

Compose 状态、布局和图标仍使用各自的标准包。例如：

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
```

禁止在调用方导入 `hyper_ui.core.*`，该目录仅供组件内部复用。

## 推荐图标方案（Android）

HyperUI 通过 slot 接收图标内容，本身不传递图标库。Android 调用方需要通用图标时，默认推荐 `icons-lucide-android`：

```kotlin
dependencies {
    implementation("com.composables:icons-lucide-android:2.2.1")
}
```

它提供 Android `VectorDrawable` 资源，可直接交给 Compose 的 `painterResource`：

```kotlin
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR
import hyper_ui.*

HyperIconButton(onClick = onSearch) {
    Icon(
        painter = painterResource(LucideR.drawable.lucide_ic_search),
        contentDescription = "搜索",
        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
    )
}
```

在使用 AGP 9.x 的调用方 Release 构建中开启统一优化：

```kotlin
android {
    buildTypes {
        release {
            optimization {
                enable = true
            }
        }
    }
}
```

代码与资源裁剪启用后，未引用的 Lucide drawable 可以从最终产物中移除。该依赖只添加到需要图标的 Android 应用模块，不添加到 HyperUI；除非现有项目已经使用，否则不要为少量图标引入 `material-icons-extended`。其他 AGP 版本应使用该版本对应的代码压缩和资源缩减配置。

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
