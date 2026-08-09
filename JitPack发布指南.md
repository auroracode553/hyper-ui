# JitPack 发布与使用指南

## 概述

`library/` 目录作为独立仓库推送到 GitHub（`auroracode553/hyper-ui`），通过 JitPack 发布 Android 组件库。调用方无需手动下载 AAR，直接在 Gradle 中声明依赖即可。

## 仓库关系

| 目录 | 推送目标 | 用途 |
|------|---------|------|
| `library/` | `auroracode553/hyper-ui` | 组件库源码 + JitPack 发布 |
| `preview/` | 不单独发布 | 本地 Desktop/Wasm 预览 |
| `vitepress/` | 不单独发布 | 在线文档站 |

## 发布流程

### 第一步：推送 library 到 GitHub

```powershell
cd library
git init
git remote add origin git@github.com:auroracode553/hyper-ui.git
git add .
git commit -m "初始提交"
git push -u origin main
```

### 第二步：打发布 Tag

在 `library/` 目录下：

```powershell
git tag <tag>
git push origin <tag>
```

Tag 名称与 `library/build.gradle.kts` 中的 `publishing.version` 保持一致。

### 第三步：JitPack 构建

1. 打开 [https://jitpack.io](https://jitpack.io)
2. 输入 GitHub 仓库地址：`https://github.com/auroracode553/hyper-ui`
3. 点击 **Look up**
4. 在 **Releases** 列表中选择刚推送的 tag
5. 点击 **Get it**，等待构建完成

构建日志显示绿色即表示成功。失败时点击日志图标查看 `build.log`。

### 发布后续 Tag

1. 修改 `library/build.gradle.kts` 中 `publishing.version`
2. 在 `library/` 目录下打新 tag 并推送：
   ```powershell
   git tag <tag>
   git push origin <tag>
   ```
3. 在 JitPack 网站对新 tag 点击 **Get it**

## 调用方使用

### 添加 JitPack 仓库

调用方项目的 `settings.gradle.kts`：

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

### 添加依赖

先从 [JitPack](https://jitpack.io/#auroracode553/hyper-ui) 或 [GitHub Tags](https://github.com/auroracode553/hyper-ui/tags) 读取最新可用 tag。文档不固定记录发布版本，避免与线上状态不一致。将 `<latest-tag>` 替换为查询到的完整 tag：

调用方模块的 `build.gradle.kts`：

```kotlin
dependencies {
    implementation("com.github.auroracode553:hyper-ui:<latest-tag>")
}
```

> JitPack 坐标格式：`com.github.<用户名>:<仓库名>:<tag>`

## 本地调试（不通过 JitPack）

开发调试时直接引用源码模块，避免频繁打 tag：

```kotlin
// settings.gradle.kts
include(":hyper_ui")
project(":hyper_ui").projectDir = file("../hyper_ui_repo/library")

// build.gradle.kts
dependencies {
    implementation(project(":hyper_ui"))
}
```

也可以发布到本地 Maven：

```powershell
cd library
.\gradlew.bat publishToMavenLocal
```

然后调用方添加 `mavenLocal()` 仓库，并从 `library/build.gradle.kts` 的 `publishing` 配置读取本地发布坐标，不在文档中重复维护固定值。

## 构建失败排查

| 问题 | 解决 |
|------|------|
| JDK 版本不匹配 | `jitpack.yml` 已指定 `openjdk17` |
| `buildToolsVersion` 缺失 | 已移除硬编码，AGP 自动选择可用版本 |
| 依赖下载失败 | JitPack CI 可能网络受限，重试或检查日志 |
