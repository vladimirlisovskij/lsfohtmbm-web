plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("build_logic.detekt")
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
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.js)
                implementation(libs.ktor.client.contentNegotiation.core)
                implementation(libs.ktor.client.contentNegotiation.json)
                implementation(libs.kotlin.serialiaztion.json)
            }
        }
    }
}
