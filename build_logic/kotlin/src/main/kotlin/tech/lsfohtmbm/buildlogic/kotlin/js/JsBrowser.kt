package tech.lsfohtmbm.buildlogic.kotlin.js

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.lsfohtmbm.buildlogic.kotlin.utils.addKotlinMultiplatform

class JsBrowser : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            addKotlinMultiplatform(pluginManager)
            extensions.configure<KotlinMultiplatformExtension> {
                js(IR) {
                    browser()
                }
            }
        }
    }
}
