# HyperUI Library

> Android Jetpack Compose UI 组件库模块。详细文档和接入指南见项目根目录 [README.md](../README.md)。

## Maven 坐标

```text
groupId:    com.hyperui
artifactId: hyper-ui
version:    1.0.0
```

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

```kotlin
dependencies {
    implementation("com.github.auroracode553:hyper-ui:1.0.0")
}
```

> 完整文档和版本历史：[JitPack - auroracode553/hyper-ui](https://jitpack.io/#auroracode553/hyper-ui)

## 自动发布

推送代码到 `main` 会自动触发 [release.yml](./.github/workflows/release.yml)：基于最新 tag 自增 patch 版本（如 `v1.0.0` → `v1.0.1`），自动创建 tag、GitHub Release，并触发 JitPack 构建。无需手动打 tag

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
    ├── selection/
    ├── panel/
    ├── list/
    ├── menu/
    ├── dialog/
    ├── drawer/
    ├── navigation/
    └── progress/
```

## 技术栈

- AGP 9.1.1
- Kotlin 2.3.21（Compose 插件由 AGP 内置）
- Compose BOM 2025.05.00
- minSdk 30 / compileSdk 36
