plugins {
    alias(versions.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(versions.kotlin.html)
            }
        }
    }
}