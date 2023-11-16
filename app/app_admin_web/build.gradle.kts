plugins {
    alias(versionCatalog.plugins.kotlin.multiplatform)
    application
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.entity.entityArticle)
                implementation(projects.source.sourceDatabaseApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.page.pageMain)
                implementation(projects.page.pageError)
                implementation(projects.page.pageArticleList)
                implementation(projects.page.pageArticleRenderer)
                implementation(projects.source.sourceDatabaseImpl)
                implementation(projects.server.serverBlogWeb)

                implementation(versionCatalog.kotlin.html)
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
