package tech.lsfohtmbm.errorpage

import kotlinx.html.HTML
import tech.lsfohtmbm.design.defaultPage
import tech.lsfohtmbm.design.elements.defaultText
import tech.lsfohtmbm.design.elements.primaryHeader

fun HTML.errorPage(
    code: Int
) {
    val title = "Ошибка $code"
    defaultPage(title) {
        primaryHeader(title)
        defaultText(code.getErrorMessage())
    }
}

private fun Int.getErrorMessage(): String {
    return when(this) {
        404 -> "Страница не найдена"
        else -> "Что-то пошло не так"
    }
}
