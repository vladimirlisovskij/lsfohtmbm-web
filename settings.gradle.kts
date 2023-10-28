enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }

    versionCatalogs {
        create("versions") {
            from(files("./gradle/versions.toml"))
        }
    }
}

rootProject.name = "blog"

include(":server")
include(":design")
include(":main_page")
include(":page_renderer")
include(":article")
include(":article_repository_api")
include(":article_repository_impl")