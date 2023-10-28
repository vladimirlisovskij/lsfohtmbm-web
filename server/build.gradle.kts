plugins {
    alias(versions.plugins.kotlin.multiplatform)
    application
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.article)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.mainPage)
                implementation(projects.pageRenderer)

                implementation(versions.ktor.server.core)
                implementation(versions.ktor.server.netty)
                implementation(versions.ktor.server.html)
                implementation(versions.logback)
            }
        }
    }
}

application {
    mainClass.set("tech.lsfohtmbm.server.MainKt")
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}
