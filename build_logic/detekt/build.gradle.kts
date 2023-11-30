plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugin.kotlin.multiplatform)
    implementation(libs.plugin.detekt)
}

gradlePlugin {
    plugins {
        register("build_logic.detekt") {
            id = "build_logic.detekt"
            implementationClass = "tech.lsfohtmbm.buildlogic.detekt.Detekt"
        }
    }
}