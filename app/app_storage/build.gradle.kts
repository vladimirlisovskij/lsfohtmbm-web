plugins {
    alias(libs.plugins.kotlin.multiplatform)
    application
    id("build_logic.detekt")
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.entity.entityStorage)
                implementation(projects.source.sourceDatabaseApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.source.sourceDatabaseImpl)
                implementation(projects.server.serverStorage)
                implementation(projects.utils.utilsApp)
            }
        }
    }
}

application {
    mainClass.set("tech.lsfohtmbm.app.storage.MainKt")
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}
