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
                implementation(projects.source.sourceStorageApi)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.page.pageMain)
                implementation(projects.page.pageError)
                implementation(projects.page.pageArticleList)
                implementation(projects.page.pageArticleRenderer)
                implementation(projects.source.sourceStorageImpl)
                implementation(projects.server.serverBlogWeb)
                implementation(projects.utils.utilsApp)

                implementation(versionCatalog.kotlin.html)
            }
        }
    }
}

application {
    mainClass.set("tech.lsfohtmbm.app.blogweb.MainKt")
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}
