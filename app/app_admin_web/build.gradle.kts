plugins {
    id("build_logic.kotlin.jvm")
    id("build_logic.kotlin.detekt")
    application
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.entity.entityStorage)
                implementation(projects.source.sourceStorageApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.source.sourceStorageImpl)
                implementation(projects.server.serverAdminWeb)
                implementation(projects.page.pageAdmin)
                implementation(projects.utils.utilsApp)

                implementation(libs.kotlin.html)
            }
        }
    }
}

application {
    mainClass.set("tech.lsfohtmbm.app.adminweb.MainKt")
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}
