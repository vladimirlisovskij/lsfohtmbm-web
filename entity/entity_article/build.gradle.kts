plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
    alias(versionCatalog.plugins.kotlin.serialization)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(versionCatalog.kotlin.serialiaztion.core)
            }
        }
    }
}