import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "hyper-ui-preview"
        browser {
            commonWebpackConfig {
                outputFileName = "hyper-ui-preview.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        getByName("commonMain") {
            // 组件和文档 UI 均为平台无关 Compose 源码，由 Desktop 与 Wasm 共同编译。
            kotlin.srcDir("../library/src/main/java")
            // Android 原生 Toast 由跨平台 Preview 使用交互模拟展示，避免引入 Android 编译链。
            kotlin.exclude("**/HyperToast.kt")
            // Android 电池状态读取由跨平台 Preview 使用交互模拟展示，避免引入 Android 编译链。
            kotlin.exclude("**/HyperBatteryState.kt")
            // Android Dialog 窗口动画配置由跨平台 Preview 使用同包宿主替代。
            kotlin.exclude("**/HyperDialogHost.kt")
            // Android Lucide VectorDrawable 由跨平台 Preview 使用同包图标替身。
            kotlin.exclude("**/HyperPlaybackSpeedPanelGlyphs.kt")
            dependencies {
                // Preview 与正式组件保持一致，不额外引入动画运行时。
                implementation("org.jetbrains.compose.foundation:foundation:${libs.versions.composeMultiplatform.get()}")
                implementation("org.jetbrains.compose.material3:material3:${libs.versions.composeMaterial3Version.get()}")
                implementation("org.jetbrains.compose.material:material-icons-core:${libs.versions.composeMaterialIconsVersion.get()}")
                implementation("org.jetbrains.compose.components:components-resources:${libs.versions.composeMultiplatform.get()}")
                implementation("org.jetbrains.compose.runtime:runtime:${libs.versions.composeMultiplatform.get()}")
                implementation("org.jetbrains.compose.ui:ui:${libs.versions.composeMultiplatform.get()}")
            }
        }

        getByName("desktopMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        getByName("wasmJsMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-browser:${libs.versions.kotlinxBrowser.get()}")
            }
        }
    }
}

// VitePress Markdown 是 API 正文的唯一来源；Preview 只消费构建目录中的资源副本。
// 合并默认 composeResources（字体等）与 API 文档到统一的资源目录
val mergedResourcesDir = layout.buildDirectory.dir("generated/merged-compose-resources")
val prepareMergedResources = tasks.register<Sync>("prepareMergedResources") {
    // 首先复制默认的 composeResources（包含字体等）
    from(layout.projectDirectory.dir("src/commonMain/composeResources"))
    // 然后复制 API 文档 markdown 文件
    from(layout.projectDirectory.dir("../vitepress/docs/components")) {
        into("files/api")
        include("**/*.md")
    }
    into(mergedResourcesDir)
}

compose.resources {
    packageOfResClass = "hyper_ui.docs.generated.resources"
    customDirectory(
        sourceSetName = "commonMain",
        directoryProvider = prepareMergedResources.map { mergedResourcesDir.get() }
    )
}

compose.desktop {
    application {
        mainClass = "hyper_ui.docs.MainKt"
    }
}

// 自动将 Wasm 静态产物复制到 VitePress 静态目录，省去手动复制的步骤
tasks.register<Copy>("publishWasmToVitePress") {
    dependsOn("wasmJsBrowserDistribution")
    from("build/dist/wasmJs/productionExecutable")
    into("../vitepress/public/wasm-preview")
    doLast {
        println("Wasm 产物已复制到 vitepress/public/wasm-preview/")
        println("刷新 http://localhost:5173 即可在文档中看到交互预览")
    }
}
