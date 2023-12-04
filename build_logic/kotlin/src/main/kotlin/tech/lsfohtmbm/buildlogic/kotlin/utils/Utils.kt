package tech.lsfohtmbm.buildlogic.kotlin.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType

internal const val JDK_VERSION = 17

internal val Project.libs
    get() = extensions
        .getByType<VersionCatalogsExtension>()
        .named("libs")

internal fun Project.addKotlinMultiplatform(pluginManager: PluginManager) {
    pluginManager.apply(libs.findPlugin("kotlin-multiplatform").get().get().pluginId)
}
