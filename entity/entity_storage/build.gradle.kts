plugins {
    id("build_logic.kotlin.jvm")
    id("build_logic.kotlin.js_browser")
    alias(libs.plugins.kotlin.serialization)
    id("build_logic.kotlin.detekt")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialiaztion.core)
            }
        }
    }
}
