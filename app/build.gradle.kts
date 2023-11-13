plugins {
    alias(versions.plugins.kotlin.multiplatform)
    application
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.article)
                implementation(projects.databaseSourceApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.mainPage)
                implementation(projects.errorPage)
                implementation(projects.articleListPage)
                implementation(projects.articleRenderer)
                implementation(projects.databaseSourceImpl)
                implementation(projects.server)

                implementation(versions.kotlin.html)
            }
        }
    }
}

application {
    mainClass.set("tech.lsfohtmbm.app.MainKt")
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}
