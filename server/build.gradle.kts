plugins {
    alias(versions.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

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
                implementation(versions.logback)
                implementation(versions.kotlin.coroutines)
            }
        }
    }
}
