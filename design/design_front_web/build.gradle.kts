plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("build_logic.detekt")
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(libs.kotlin.html)
            }
        }
    }
}