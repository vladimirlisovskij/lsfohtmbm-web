package tech.lsfohtmbm.page.fronterror

import kotlinx.html.HTML
import tech.lsfohtmbm.design.frontweb.defaultPage
import tech.lsfohtmbm.design.frontweb.elements.defaultText
import tech.lsfohtmbm.design.frontweb.elements.primaryHeader

fun HTML.errorPage(
    code: Int
) {
    val title = "Ошибка $code"
    defaultPage(title) {
        primaryHeader(title)
        defaultText(code.getErrorMessage())
    }
}

@Suppress("MagicNumber")
private fun Int.getErrorMessage(): String {
    return if (this == 404) {
        "Страница не найдена"
    } else {
        "Что-то пошло не так"
    }
}
