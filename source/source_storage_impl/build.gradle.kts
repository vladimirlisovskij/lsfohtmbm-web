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
                implementation(projects.source.sourceStorageApi)
                implementation(projects.api.apiStorage)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.utils.utilsCoroutines)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.contentNegotiation.core)
                implementation(libs.ktor.client.contentNegotiation.json)
                implementation(libs.kotlin.serialiaztion.json)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.ktor.client.test)
            }
        }
    }
}
