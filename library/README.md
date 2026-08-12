# HyperUI Library

> Android Jetpack Compose UI 组件库模块。详细文档和接入指南见项目根目录 [README.md](../README.md)。

## 发布坐标

```text
groupId:    com.hyperui
artifactId: hyper-ui
```

发布 tag 不在文档中固定记录。使用依赖前，从 [JitPack](https://jitpack.io/#auroracode553/hyper-ui) 或 [GitHub Tags](https://github.com/auroracode553/hyper-ui/tags) 读取最新可用 tag。

## 接入方式（JitPack）

### Step 1. 在 `settings.gradle.kts` 中添加 JitPack 仓库

```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2. 在模块的 `build.gradle.kts` 中添加依赖

将 `<latest-tag>` 替换为线上查询到的完整 tag：

```kotlin
dependencies {
    implementation("com.github.auroracode553:hyper-ui:<latest-tag>")
}
```

> 可用 tag 与构建状态：[JitPack - auroracode553/hyper-ui](https://jitpack.io/#auroracode553/hyper-ui)

## 自动发布

推送代码到 `main` 会自动触发 [release.yml](./.github/workflows/release.yml)：基于最新 tag 创建下一个发布 tag、GitHub Release，并触发 JitPack 构建。无需手动打 tag。

## 构建命令

在 `library/` 目录下执行：

```powershell
# 编译
.\gradlew.bat assembleRelease

# 发布到本地 Maven
.\gradlew.bat publishToMavenLocal
```

## 源码结构

```
src/main/java/hyper_ui/
├── theme/             # 主题与样式
├── core/              # 内部公共工具（调用方不可导入）
└── components/        # 公开组件（按功能分组）
    ├── button/
    ├── input/
    ├── selection/        # HyperRadio / HyperSegmented 等选择控件
    ├── panel/
    ├── list/
    ├── menu/             # HyperDropdown / HyperSlideMenu
    ├── dialog/           # HyperAlertDialog 结构化弹窗
    ├── popup/            # HyperPopup 基础浮层
    ├── drawer/
    ├── navigation/       # HyperNavBar / HyperTabBar
    ├── feedback/         # Android-only hyperToast
    └── progress/         # 只读进度指示器与可拖动 HyperSlider
```

## 技术栈

- AGP 9.3.1
- Kotlin 2.4.10（Compose 插件与 Kotlin 版本一致）
- Compose BOM 2026.06.01
- minSdk 30 / compileSdk 37

## 图标依赖策略

HyperUI 不传递图标库依赖，图标由调用方通过组件 slot 提供。Android 调用方需要通用图标时，优先推荐可按资源引用裁剪的 `com.composables:icons-lucide-android:2.2.1`；完整依赖方式和示例见 [接入与最小配置](../vitepress/docs/getting-started.md#推荐图标方案android)。
