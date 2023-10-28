plugins {
    alias(versions.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.article)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.design)
            }
        }
    }
}
