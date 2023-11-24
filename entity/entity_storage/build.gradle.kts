plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
    alias(versionCatalog.plugins.kotlin.serialization)
}

kotlin {
    jvm()
    js(IR) { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(versionCatalog.kotlin.serialiaztion.core)
            }
        }
    }
}