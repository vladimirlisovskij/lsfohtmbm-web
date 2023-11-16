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
                implementation(projects.entity.entityArticle)
                implementation(projects.source.sourceDatabaseApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(versionCatalog.ktor.server.core)
                implementation(versionCatalog.ktor.server.netty)
                implementation(versionCatalog.ktor.server.html)
                implementation(versionCatalog.ktor.server.ssl)
                implementation(versionCatalog.ktor.server.statusPages)
                implementation(versionCatalog.logback)
                implementation(versionCatalog.kotlin.coroutines)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(versionCatalog.ktor.server.test)
            }
        }
    }
}
