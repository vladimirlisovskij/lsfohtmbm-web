plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
}

kotlin {
    js(IR) { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.source.sourceAdminApi)
                implementation(projects.api.apiAdminWeb)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(versionCatalog.ktor.client.core)
                implementation(versionCatalog.ktor.client.js)
                implementation(versionCatalog.ktor.client.contentNegotiation.core)
                implementation(versionCatalog.ktor.client.contentNegotiation.json)
                implementation(versionCatalog.kotlin.serialiaztion.json)
            }
        }
    }
}
