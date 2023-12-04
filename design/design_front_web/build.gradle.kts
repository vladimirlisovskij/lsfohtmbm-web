plugins {
    id("build_logic.kotlin.jvm")
    id("build_logic.kotlin.detekt")
}

kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(libs.kotlin.html)
            }
        }
    }
}
