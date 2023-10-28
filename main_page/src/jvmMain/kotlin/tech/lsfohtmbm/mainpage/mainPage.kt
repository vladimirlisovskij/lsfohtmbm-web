package tech.lsfohtmbm.mainpage

import kotlinx.html.HTML
import tech.lsfohtmbm.design.defaultPage
import tech.lsfohtmbm.design.elements.picture
import tech.lsfohtmbm.design.elements.primaryHeader

fun HTML.mainPage() {
    defaultPage("Главная") {
        primaryHeader("Привет!")
        picture("cute_ctulhu.webp", "cute ctulhu")
    }
}