package tech.lsfohtmbm.page.frontmain

import kotlinx.html.HTML
import tech.lsfohtmbm.design.frontweb.defaultPage
import tech.lsfohtmbm.design.frontweb.elements.defaultText
import tech.lsfohtmbm.design.frontweb.elements.picture
import tech.lsfohtmbm.design.frontweb.elements.primaryHeader

fun HTML.mainPage() {
    defaultPage("Главная") {
        primaryHeader("Привет!")
        defaultText("Я Владимир. В 2021 году я начал заниматься коммерческой разработкой Android-приложений. В этом блоге я буду рассказывать о своем опыте разработки и применения связанных с ней технологий. Возможно в будущем добавиться что-то еще.")
        picture("cute_ctulhu.webp", "cute ctulhu")
    }
}