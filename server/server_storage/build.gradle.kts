plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("build_logic.detekt")
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
                implementation(projects.api.apiStorage)
                implementation(projects.source.sourceDatabaseApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.netty)
                implementation(libs.ktor.server.contentNegotiation.core)
                implementation(libs.ktor.server.contentNegotiation.json)
                implementation(libs.logback)
                implementation(libs.kotlin.serialiaztion.json)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.ktor.server.test)
            }
        }
    }
}
