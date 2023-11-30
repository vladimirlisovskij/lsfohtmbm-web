package tech.lsfohtmbm.buildlogic.detekt

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File

class Detekt : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("detekt").get().get().pluginId)
            }

            afterEvaluate {
                val sourceDirs = kotlin.sourceSets.names.map { "src/$it/kotlin" }
                extensions.configure<DetektExtension> {
                    parallel = true
                    buildUponDefaultConfig = true
                    autoCorrect = true
                    config.setFrom("${rootDir}/detekt/config.yml")
                    source = files(*(sourceDirs.toTypedArray()))
                }
            }

            dependencies {
                "detektPlugins"(libs.findLibrary("detekt-formatting").get())
            }
        }
    }

    private val Project.libs
        get() = extensions
            .getByType<VersionCatalogsExtension>()
            .named("libs")

    private val Project.kotlin
        get() = extensions
            .getByType<KotlinMultiplatformExtension>()
}