plugins {
    alias(versions.plugins.kotlin.multiplatform)
    alias(versions.plugins.sqldelight)
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
                implementation(projects.databaseSourceApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(versions.sqldelight.sqlite)
                implementation(versions.kotlin.coroutines)
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
            packageName.set("tech.lsfohtmbm.databasesource.impl.database")
        }
    }
}
