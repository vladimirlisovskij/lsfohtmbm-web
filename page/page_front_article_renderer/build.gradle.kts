plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("build_logic.detekt")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.entity.entityStorage)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.design.designFrontWeb)
            }
        }
    }
}
