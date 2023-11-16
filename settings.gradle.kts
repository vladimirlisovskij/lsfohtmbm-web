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
        create("versionCatalog") {
            from(files("./gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "blog"

include(":app:app_blog_web")
include(":app:app_admin_web")
include(":app:app_storage")

include(":server:server_blog_web")
include(":server:server_admin_web")
include(":server:server_storage")

include(":entity:entity_article")

include(":design:design_blog_web")

include(":page:page_main")
include(":page:page_error")
include(":page:page_article_list")
include(":page:page_article_renderer")

include(":source:source_database_api")
include(":source:source_database_impl")
include(":source:source_storage_api")
include(":source:source_storage_impl")

include(":utils:utils_app")
