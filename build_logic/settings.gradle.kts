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
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include(":detekt")
