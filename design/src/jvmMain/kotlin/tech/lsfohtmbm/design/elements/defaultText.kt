package tech.lsfohtmbm.design.elements

import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.defaultText(
    text: String
) {
    p("defaultText") {
        + text
    }
}
