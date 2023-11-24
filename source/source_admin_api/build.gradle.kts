plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
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
