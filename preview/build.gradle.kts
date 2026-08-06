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
        val commonMain by getting {
            // 组件和文档 UI 均为平台无关 Compose 源码，由 Desktop 与 Wasm 共同编译。
            kotlin.srcDir("../library/src/main/java")

            dependencies {
                implementation("org.jetbrains.compose.animation:animation:${libs.versions.composeMultiplatform.get()}")
                implementation("org.jetbrains.compose.foundation:foundation:${libs.versions.composeMultiplatform.get()}")
                implementation("org.jetbrains.compose.material3:material3:${libs.versions.composeMaterial3Version.get()}")
                implementation("org.jetbrains.compose.material:material-icons-core:${libs.versions.composeMaterialIconsVersion.get()}")
                implementation(compose.components.resources)
                implementation("org.jetbrains.compose.runtime:runtime:${libs.versions.composeMultiplatform.get()}")
                implementation("org.jetbrains.compose.ui:ui:${libs.versions.composeMultiplatform.get()}")
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-browser:0.3")
            }
        }
    }
}

compose.resources {
    packageOfResClass = "hyper_ui.docs.generated.resources"
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
