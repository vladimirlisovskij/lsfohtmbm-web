plugins {
    alias(versions.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.article)
            }
        }
    }
}
