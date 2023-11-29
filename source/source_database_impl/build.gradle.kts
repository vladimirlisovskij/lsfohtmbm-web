plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.sqldelight)
    id("build_logic.detekt")
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
                implementation(libs.sqldelight.sqlite)
                implementation(libs.kotlin.coroutines)
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
            dialect("app.cash.sqldelight:sqlite-3-38-dialect:${libs.versions.sqldelight.get()}")
            deriveSchemaFromMigrations = true
            verifyMigrations = true
        }
    }
}
