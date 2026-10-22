pluginManagement {
    val useTencentMirror = !(System.getenv("CI") ?: "").equals("true", ignoreCase = true) &&
        !(System.getenv("GITHUB_ACTIONS") ?: "").equals("true", ignoreCase = true) &&
        !(System.getenv("JITPACK") ?: "").equals("true", ignoreCase = true)

    repositories {
        if (useTencentMirror) {
            maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
        }
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    val useTencentMirror = !(System.getenv("CI") ?: "").equals("true", ignoreCase = true) &&
        !(System.getenv("GITHUB_ACTIONS") ?: "").equals("true", ignoreCase = true) &&
        !(System.getenv("JITPACK") ?: "").equals("true", ignoreCase = true)

    repositories {
        if (useTencentMirror) {
            maven {
                url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
                // Lucide 镜像元数据与 AAR 不同步，图标依赖从后面的 Maven Central 解析。
                content { excludeGroup("com.composables") }
            }
        }
        mavenCentral()
        google()
        // Kotlin/Wasm 插件需要 Node.js 分发仓库
        ivy {
            url = uri("https://nodejs.org/dist")
            patternLayout {
                artifact("v[revision]/[artifact]-v[revision]-[classifier].[ext]")
            }
            metadataSources { artifact() }
            content { includeModule("org.nodejs", "node") }
        }
    }
    versionCatalogs {
        create("libs") {
            from(files("../library/gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "hyper_ui_docs"
