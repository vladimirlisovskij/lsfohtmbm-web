plugins {
    alias(versions.plugins.kotlin.multiplatform)
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
                implementation(projects.article)
                implementation(projects.databaseSourceApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(versions.ktor.server.core)
                implementation(versions.ktor.server.netty)
                implementation(versions.ktor.server.html)
                implementation(versions.ktor.server.ssl)
                implementation(versions.ktor.server.statusPages)
                implementation(versions.logback)
                implementation(versions.kotlin.coroutines)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(versions.ktor.server.test)
            }
        }
    }
}
