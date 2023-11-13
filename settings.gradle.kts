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
            from(files("./gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "blog"

include(":app")
include(":server")
include(":design")
include(":main_page")
include(":error_page")
include(":article_list_page")
include(":article_renderer")
include(":article")
include(":database_source_api")
include(":database_source_impl")