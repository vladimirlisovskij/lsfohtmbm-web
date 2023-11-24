package tech.lsfohtmbm.design.frontweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.defaultText(
    text: String
) {
    p("defaultText") {
        + text
    }
}
