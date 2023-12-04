plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugin.kotlin.multiplatform)
    implementation(libs.plugin.detekt)
}

gradlePlugin {
    plugins {
        register("build_logic.kotlin.jvm") {
            id = "build_logic.kotlin.jvm"
            implementationClass = "tech.lsfohtmbm.buildlogic.kotlin.jvm.Jvm"
        }

        register("build_logic.kotlin.js_browser") {
            id = "build_logic.kotlin.js_browser"
            implementationClass = "tech.lsfohtmbm.buildlogic.kotlin.js.JsBrowser"
        }

        register("build_logic.kotlin.detekt") {
            id = "build_logic.kotlin.detekt"
            implementationClass = "tech.lsfohtmbm.buildlogic.kotlin.detekt.Detekt"
        }
    }
}
