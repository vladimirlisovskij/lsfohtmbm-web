plugins {
    id("build_logic.kotlin.jvm")
    id("build_logic.kotlin.detekt")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.entity.entityStorage)
            }
        }
    }
}
