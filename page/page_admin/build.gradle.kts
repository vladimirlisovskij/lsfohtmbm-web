plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()
    js(IR) {
        binaries.executable()
        browser()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(projects.design.designFrontWeb)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(projects.entity.entityStorage)
                implementation(projects.source.sourceAdminApi)
                implementation(projects.source.sourceAdminImpl)
                implementation(versionCatalog.kotlin.react.core)
                implementation(versionCatalog.kotlin.react.dom)
                implementation(versionCatalog.kotlin.coroutines)
            }
        }
    }
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}
