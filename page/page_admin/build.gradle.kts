plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("build_logic.detekt")
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
                implementation(libs.kotlin.react.core)
                implementation(libs.kotlin.react.dom)
                implementation(libs.kotlin.coroutines)
            }
        }
    }
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}
