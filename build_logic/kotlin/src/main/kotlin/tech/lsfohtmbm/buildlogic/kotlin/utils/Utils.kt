package tech.lsfohtmbm.buildlogic.kotlin.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.getByType

internal val Project.libs
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.addKotlinMultiplatform(pluginManager: PluginManager) {
    pluginManager.apply(libs.findPlugin("kotlin-multiplatform").get().get().pluginId)
}

internal fun JavaToolchainSpec.configureToolchain() {
    languageVersion.set(JavaLanguageVersion.of(17))
    vendor.set(JvmVendorSpec.AMAZON)
}
