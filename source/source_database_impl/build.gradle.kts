plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
    alias(versionCatalog.plugins.sqldelight)
}

kotlin {
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.source.sourceDatabaseApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(versionCatalog.sqldelight.sqlite)
                implementation(versionCatalog.kotlin.coroutines)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            srcDirs("src/jvmMain/sqldelight")
            packageName.set("tech.lsfohtmbm.source.database.impl.database")
            dialect("app.cash.sqldelight:sqlite-3-38-dialect:${versionCatalog.versions.sqldelight.get()}")
            deriveSchemaFromMigrations = true
            verifyMigrations = true
        }
    }
}
