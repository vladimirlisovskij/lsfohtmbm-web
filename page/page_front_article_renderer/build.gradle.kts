plugins {
    id("build_logic.kotlin.jvm")
    id("build_logic.kotlin.detekt")
}

kotlin {
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
