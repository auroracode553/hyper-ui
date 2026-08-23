plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

android {
    namespace = "hyper_ui"
    compileSdk = 37

    defaultConfig {
        minSdk = 30
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    // HyperUI 状态切换均为即时渲染，组件层不引入 Compose Animation。
    api(platform(libs.androidx.compose.bom))
    api("androidx.compose.foundation:foundation")
    api("androidx.compose.runtime:runtime")
    api("androidx.compose.ui:ui")
    api("androidx.compose.ui:ui-graphics")
    api("androidx.compose.material3:material3")
    implementation(libs.lucide.icons.android)
}

publishing {
    publications {
        register("release", MavenPublication::class) {
            groupId = "com.hyperui"
            artifactId = "hyper-ui"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
