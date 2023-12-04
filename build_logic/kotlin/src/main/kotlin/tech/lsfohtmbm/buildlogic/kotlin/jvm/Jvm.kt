package tech.lsfohtmbm.buildlogic.kotlin.jvm

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.lsfohtmbm.buildlogic.kotlin.utils.JDK_VERSION
import tech.lsfohtmbm.buildlogic.kotlin.utils.addKotlinMultiplatform

class Jvm : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            addKotlinMultiplatform(pluginManager)
            extensions.configure<KotlinMultiplatformExtension> {
                jvm {
                    jvmToolchain(JDK_VERSION)

                }
            }

            tasks.withType(KotlinCompile::class.java) {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(JDK_VERSION.toString()))
                }
            }

            extensions.findByType(JavaPluginExtension::class.java)?.apply {
                sourceCompatibility = JavaVersion.toVersion(JDK_VERSION)
                targetCompatibility = JavaVersion.toVersion(JDK_VERSION)
            }

        }
    }
}
