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
                implementation(projects.source.sourceStorageApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.page.pageFrontMain)
                implementation(projects.page.pageFrontError)
                implementation(projects.page.pageFrontArticleList)
                implementation(projects.page.pageFrontArticleRenderer)
                implementation(projects.source.sourceStorageImpl)
                implementation(projects.server.serverFrontWeb)
                implementation(projects.utils.utilsApp)

                implementation(libs.kotlin.html)
            }
        }
    }
}

application {
    mainClass.set("tech.lsfohtmbm.app.frontweb.MainKt")
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}
