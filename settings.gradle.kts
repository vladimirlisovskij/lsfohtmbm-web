rootProject.name = "blog"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("./build_logic")
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
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.5.0")
}

include(":app:app_front_web")
include(":app:app_admin_web")
include(":app:app_storage")

include(":api:api_storage")
include(":api:api_admin_web")

include(":server:server_front_web")
include(":server:server_admin_web")
include(":server:server_storage")

include(":entity:entity_storage")

include(":design:design_front_web")

include(":page:page_front_main")
include(":page:page_front_error")
include(":page:page_front_article_list")
include(":page:page_front_article_renderer")
include(":page:page_admin")

include(":source:source_database_api")
include(":source:source_database_impl")
include(":source:source_storage_api")
include(":source:source_storage_impl")
include(":source:source_admin_api")
include(":source:source_admin_impl")

include(":utils:utils_app")
include(":utils:utils_coroutines")
include(":utils:utils_tests")
