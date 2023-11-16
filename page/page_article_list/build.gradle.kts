plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.entity.entityArticle)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.design.designBlogWeb)
            }
        }
    }
}
