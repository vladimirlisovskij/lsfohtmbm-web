package tech.lsfohtmbm.buildlogic.kotlin.jvm

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.lsfohtmbm.buildlogic.kotlin.utils.JDK_VERSION
import tech.lsfohtmbm.buildlogic.kotlin.utils.addKotlinMultiplatform

class Jvm : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            addKotlinMultiplatform(pluginManager)
            kotlinExtension.jvmToolchain(JDK_VERSION)
            tasks.withType(KotlinCompile::class.java) {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(JDK_VERSION.toString()))
                }
            }
        }
    }
}
