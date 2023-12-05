package tech.lsfohtmbm.buildlogic.kotlin.jvm

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.lsfohtmbm.buildlogic.kotlin.utils.addKotlinMultiplatform
import tech.lsfohtmbm.buildlogic.kotlin.utils.configureToolchain

class Jvm : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            addKotlinMultiplatform(pluginManager)
            extensions.configure<KotlinMultiplatformExtension> {
                jvm {
                    jvmToolchain {
                        configureToolchain()
                    }
                }
            }
        }
    }
}
