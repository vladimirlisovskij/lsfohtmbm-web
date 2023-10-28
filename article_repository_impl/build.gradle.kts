plugins {
    alias(versions.plugins.kotlin.multiplatform)
    alias(versions.plugins.sqldelight)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.articleRepositoryApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(versions.sqldelight.sqlite)
            }
        }
    }
}

sqldelight {
    databases {
        create("ArticlesDatabase") {
            srcDirs("src/jvmMain/sqldelight")
            packageName.set("tech.lsfohtmbm.articlerepository.impl.database.generated")
        }
    }
}
