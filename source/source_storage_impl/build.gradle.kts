plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.source.sourceStorageApi)
                implementation(projects.api.apiStorage)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(versionCatalog.ktor.client.core)
                implementation(versionCatalog.ktor.client.cio)
                implementation(versionCatalog.ktor.client.contentNegotiation.core)
                implementation(versionCatalog.ktor.client.contentNegotiation.json)
                implementation(versionCatalog.kotlin.serialiaztion.json)
            }
        }
    }
}
