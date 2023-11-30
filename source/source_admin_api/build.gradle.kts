plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("build_logic.detekt")
}

kotlin {
    js(IR) { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.entity.entityStorage)
            }
        }
    }
}
