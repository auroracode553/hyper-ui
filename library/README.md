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
    ├── selection/        # HyperRadio / HyperCheckbox 等选择控件
    ├── panel/
    ├── list/
    ├── menu/             # HyperDropdown / HyperSlideMenu
    ├── dialog/           # HyperAlertDialog 结构化弹窗
    ├── popup/            # HyperPopover 基础浮层
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

HyperUI 与 Preview 直接使用 com.composables:icons-lucide-cmp:2.2.1 的 Lucide 图标，不维护本地轮廓或别名表。HyperIcon 负责显示尺寸与颜色继承，组件图标插槽保持开放。业务代码直接使用 Lucide 时显式声明同一依赖，详见 [样式对照](../vitepress/docs/style-reference.md)。

维护 UI 库和开发业务页面前，优先阅读[使用规则、全局全面屏与 AGENTS.md 模板](../vitepress/docs/mobile-guidelines.md)和[设计系统基线](../vitepress/docs/style-reference.md)。正文卡片统一无普通描边和双层柔阴影，选中轮廓保留；导航材质仅在状态栏顶部至 Navbar 底部内渐隐。既有悬浮 TabBar 的高度、占位与滚动范围不能因配色或卡片优化改变。

导航滚动监听统一由 HyperNavBarPage.scrollState / HyperNavBarScrollScope 管理，Navbar 自动读取；消费页面不得重复声明导航专用 derivedStateOf、snapshotFlow 或 scrolledUnder 判断。库测量实际导航区高度，顶部净空放进滚动内容；主滚动状态保留在容器，嵌套列表不代替主状态。
