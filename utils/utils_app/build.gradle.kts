plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
}

kotlin {
    jvm {
        withJava()
    }
}
