plugins {
    id("build_logic.kotlin.jvm")
    id("build_logic.kotlin.detekt")
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
                implementation(projects.utils.utilsTests)
                implementation(libs.ktor.client.test)
            }
        }
    }
}
