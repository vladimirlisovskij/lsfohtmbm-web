plugins {
    id("build_logic.kotlin.jvm")
    id("build_logic.kotlin.js_browser")
    id("build_logic.kotlin.detekt")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.entity.entityStorage)
            }
        }
    }
}
