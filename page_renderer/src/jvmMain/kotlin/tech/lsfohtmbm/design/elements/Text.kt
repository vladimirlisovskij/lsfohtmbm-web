package tech.lsfohtmbm.design.elements

import kotlinx.html.FlowContent
import kotlinx.html.div
import tech.lsfohtmbm.article.Text

fun FlowContent.renderText(
    text: Text
) {
    div(classes = "text") {
        + text.text
    }
}