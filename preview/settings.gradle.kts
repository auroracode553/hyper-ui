pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
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
