plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.source.sourceStorageApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(versionCatalog.ktor.client.core)
                implementation(versionCatalog.ktor.client.jetty)
                implementation(versionCatalog.ktor.client.cio)
                implementation(versionCatalog.ktor.client.contentNegotiation.core)
                implementation(versionCatalog.ktor.client.contentNegotiation.json)
            }
        }
    }
}
