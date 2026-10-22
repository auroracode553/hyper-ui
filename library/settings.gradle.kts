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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
    }
}

rootProject.name = "hyper-ui"
