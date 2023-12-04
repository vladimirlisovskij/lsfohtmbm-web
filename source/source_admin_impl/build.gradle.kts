plugins {
    id("build_logic.kotlin.jvm")
    id("build_logic.kotlin.js_browser")
    id("build_logic.kotlin.detekt")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.source.sourceAdminApi)
                implementation(projects.api.apiAdminWeb)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(projects.utils.utilsCoroutines)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.js)
                implementation(libs.ktor.client.contentNegotiation.core)
                implementation(libs.ktor.client.contentNegotiation.json)
                implementation(libs.kotlin.serialiaztion.json)
            }
        }
    }
}
