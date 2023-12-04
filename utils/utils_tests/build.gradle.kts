plugins {
    id("build_logic.kotlin.jvm")
    id("build_logic.kotlin.detekt")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlin.html)
                implementation(libs.ktor.server.test)
            }
        }
    }
}
