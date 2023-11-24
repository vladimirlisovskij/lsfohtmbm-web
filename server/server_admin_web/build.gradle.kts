plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
}

kotlin {
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.entity.entityStorage)
                implementation(projects.api.apiAdminWeb)
                implementation(projects.source.sourceStorageApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(versionCatalog.ktor.server.core)
                implementation(versionCatalog.ktor.server.netty)
                implementation(versionCatalog.ktor.server.html)
                implementation(versionCatalog.ktor.server.contentNegotiation.core)
                implementation(versionCatalog.ktor.server.contentNegotiation.json)
                implementation(versionCatalog.logback)
                implementation(versionCatalog.kotlin.serialiaztion.json)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(versionCatalog.ktor.server.test)
                implementation(versionCatalog.ktor.client.contentNegotiation.core)
                implementation(versionCatalog.ktor.client.contentNegotiation.json)
            }
        }
    }
}
