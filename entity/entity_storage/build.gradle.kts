plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("build_logic.detekt")
}

kotlin {
    jvm()
    js(IR) { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialiaztion.core)
            }
        }
    }
}